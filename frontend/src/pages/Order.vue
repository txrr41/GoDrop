<template>
  <div class="my-orders-page">
    <header class="page-header">
      <div class="title-section">
        <div class="title-icon-wrapper">
          <v-icon color="indigo-accent-3" size="32">mdi-package-variant-closed</v-icon>
        </div>
        <div class="title-text-group">
          <h1>Meus Pedidos</h1>
          <p>Acompanhe o status de todos os seus pedidos em tempo real</p>
        </div>
      </div>
    </header>

    <div v-if="orderStore.loading" class="loading-state">
      <div class="loader-visual">
        <v-progress-circular indeterminate color="indigo-accent-3" size="64" width="3" />
      </div>
      <p>Sincronizando seus pedidos...</p>
    </div>

    <div v-else-if="orderStore.orders.length > 0" class="orders-list">
      <div
          v-for="order in orderStore.orders"
          :key="order.id"
          class="order-card-premium"
          @click="showOrderDetails(order.id)"
      >
        <div class="order-header-custom">
          <div class="header-left">
            <span class="order-label">PEDIDO</span>
            <span class="order-id">#{{ order.id }}</span>
          </div>
          <div :class="['status-badge-glow', getStatusColor(order.status)]">
            <span class="status-dot"></span>
            {{ getStatusLabel(order.status) }}
          </div>
        </div>

        <v-divider class="divider-minimal" />

        <div class="order-body-content">
          <div class="info-grid">
            <div class="info-block">
              <div class="info-row">
                <v-icon size="18" class="info-icon">mdi-currency-usd</v-icon>
                <div class="info-data">
                  <span class="label">Total do Pedido</span>
                  <span class="value">{{ formatCurrency(order.totalAmount) }}</span>
                </div>
              </div>

              <div class="info-row">
                <v-icon size="18" class="info-icon">mdi-calendar-month</v-icon>
                <div class="info-data">
                  <span class="label">Realizado em</span>
                  <span class="value">{{ formatDate(order.createdAt) }}</span>
                </div>
              </div>
            </div>

            <div class="info-block">
              <div class="info-row address">
                <v-icon size="18" class="info-icon">mdi-map-marker-outline</v-icon>
                <div class="info-data">
                  <span class="label">Endereço de Entrega</span>
                  <span class="value address-text">{{ order.shippingAddress }}</span>
                </div>
              </div>
            </div>
          </div>

          <div v-if="order.trackingCode" class="tracking-container" @click.stop>
            <div class="tracking-main">
              <div class="tracking-info">
                <v-icon color="indigo-accent-3" class="mr-3">mdi-truck-delivery-outline</v-icon>
                <div class="code-group">
                  <span class="tracking-tag">CÓDIGO DE RASTREIO</span>
                  <span class="tracking-code">{{ order.trackingCode }}</span>
                </div>
              </div>
              <div class="tracking-actions">
                <v-btn
                    variant="tonal"
                    color="indigo-accent-3"
                    size="small"
                    class="action-btn"
                    @click.stop="copyTrackingCode(order.trackingCode)"
                >
                  <v-icon start size="16">mdi-content-copy</v-icon>
                  Copiar
                </v-btn>
                <v-btn
                    color="indigo-accent-3"
                    variant="flat"
                    size="small"
                    class="action-btn"
                    :href="getCorreiosLink(order.trackingCode)"
                    target="_blank"
                    @click.stop
                >
                  <v-icon start size="16">mdi-open-in-new</v-icon>
                  Rastrear
                </v-btn>
              </div>
            </div>
          </div>

          <div class="order-items-minimal">
            <p class="items-header">
              <v-icon size="16" class="mr-2">mdi-basket-outline</v-icon>
              Resumo dos Itens ({{ order.items.length }})
            </p>
            <div class="items-scroller">
              <div v-for="item in order.items" :key="item.productName" class="mini-item">
                <span class="qty">{{ item.quantity }}x</span>
                <span class="name">{{ item.productName }}</span>
                <span class="price">{{ formatCurrency(item.totalPrice) }}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="card-footer-interactive">
          <span class="footer-msg">Toque para ver detalhes completos</span>
          <v-icon size="20" class="footer-arrow">mdi-chevron-right</v-icon>
        </div>
      </div>
    </div>

    <div v-else class="empty-state">
      <div class="empty-visual-box">
        <v-icon size="80" color="indigo-lighten-4">mdi-package-variant-closed</v-icon>
      </div>
      <h2>Sua lista de pedidos está vazia</h2>
      <p>Você ainda não realizou nenhuma compra. Explore nossos produtos!</p>
      <v-btn
          color="indigo-accent-4"
          size="x-large"
          to="/produtos"
          class="explore-btn"
          elevation="0"
      >
        Começar a Comprar
        <v-icon end>mdi-shopping-outline</v-icon>
      </v-btn>
    </div>

    <v-snackbar
        v-model="snackbar"
        :timeout="2000"
        color="indigo-darken-4"
        class="custom-snackbar"
    >
      <div class="d-flex align-center">
        <v-icon class="mr-3">mdi-check-circle</v-icon>
        Código de rastreio copiado com sucesso!
      </div>
    </v-snackbar>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useOrderStore } from '../stores/order'

const router = useRouter()
const orderStore = useOrderStore()
const snackbar = ref(false)

onMounted(async () => {
  try {
    await orderStore.fetchOrders()
  } catch (error) {
    console.error('Erro ao carregar pedidos:', error)
  }
})

const formatCurrency = (value) => {
  return value.toLocaleString('pt-BR', {
    style: 'currency',
    currency: 'BRL'
  })
}

const formatDate = (date) => {
  return new Date(date).toLocaleDateString('pt-BR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const getStatusColor = (status) => {
  const colors = {
    'PENDING': 'warning',
    'PAYMENT_APPROVED': 'info',
    'PROCESSING': 'primary',
    'SHIPPED': 'success',
    'DELIVERED': 'success',
    'CANCELLED': 'error'
  }
  return colors[status] || 'default'
}

const getStatusLabel = (status) => {
  const labels = {
    'PENDING': 'Aguardando Pagamento',
    'PAYMENT_APPROVED': 'Pagamento Aprovado',
    'PROCESSING': 'Em Processamento',
    'SHIPPED': 'Enviado',
    'DELIVERED': 'Entregue',
    'CANCELLED': 'Cancelado'
  }
  return labels[status] || status
}

const copyTrackingCode = async (code) => {
  try {
    await navigator.clipboard.writeText(code)
    snackbar.value = true
  } catch (error) {
    alert('Erro ao copiar código')
  }
}

const getCorreiosLink = (code) => {
  return `https://rastreamento.correios.com.br/app/index.php?objeto=${code}`
}

const showOrderDetails = (orderId) => {
  router.push(`/pedidos/${orderId}`)
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap');

.my-orders-page {
  font-family: 'Plus Jakarta Sans', sans-serif;
  padding: 40px 24px;
  max-width: 1000px;
  margin: 0 auto;
  background-color: #ffffff;
  min-height: 100vh;
}

/* --- Header --- */
.page-header {
  margin-bottom: 48px;
  border-bottom: 1px solid #f1f5f9;
  padding-bottom: 32px;
}

.title-section {
  display: flex;
  align-items: center;
  gap: 20px;
}

.title-icon-wrapper {
  width: 64px;
  height: 64px;
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05);
}

.title-text-group h1 {
  font-size: 32px;
  font-weight: 800;
  color: #0f172a;
  letter-spacing: -0.03em;
  margin: 0;
}

.title-text-group p {
  font-size: 16px;
  color: #64748b;
  margin: 4px 0 0 0;
}

.orders-list {
  display: grid;
  grid-template-columns: 1fr;
  gap: 32px;
}

.order-card-premium {
  background: #ffffff;
  border: 1px solid #f1f5f9;
  border-radius: 24px;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.1);
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.02);
}

.order-card-premium:hover {
  transform: translateY(-6px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.06);
  border-color: #e2e8f0;
}

.order-header-custom {
  padding: 24px 32px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  flex-direction: column;
}

.order-label {
  font-size: 10px;
  font-weight: 800;
  color: #94a3b8;
  letter-spacing: 0.1em;
}

.order-id {
  font-size: 20px;
  font-weight: 800;
  color: #0f172a;
}

/* Status Badges */
.status-badge-glow {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 16px;
  border-radius: 100px;
  font-size: 12px;
  font-weight: 700;
  text-transform: uppercase;
}

.status-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
}

.divider-minimal {
  border-color: #f8fafc !important;
  opacity: 1;
}

.order-body-content {
  padding: 32px;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 32px;
  margin-bottom: 32px;
}

.info-row {
  display: flex;
  gap: 16px;
  align-items: flex-start;
}

.info-icon {
  margin-top: 4px;
  color: #94a3b8;
}

.info-data {
  display: flex;
  flex-direction: column;
}

.info-data .label {
  font-size: 11px;
  color: #94a3b8;
  font-weight: 700;
  text-transform: uppercase;
  margin-bottom: 4px;
}

.info-data .value {
  font-size: 15px;
  color: #334155;
  font-weight: 600;
}

.address-text {
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* --- Tracking --- */
.tracking-container {
  background: #f8fafc;
  border: 1px solid #f1f5f9;
  border-radius: 20px;
  padding: 20px;
  margin-bottom: 32px;
}

.tracking-main {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
  flex-wrap: wrap;
}

.tracking-info {
  display: flex;
  align-items: center;
}

.code-group {
  display: flex;
  flex-direction: column;
}

.tracking-tag {
  font-size: 10px;
  font-weight: 800;
  color: #94a3b8;
}

.tracking-code {
  font-size: 18px;
  font-weight: 800;
  color: #4338ca;
  letter-spacing: 0.05em;
}

.tracking-actions {
  display: flex;
  gap: 12px;
}

.action-btn {
  border-radius: 12px !important;
  text-transform: none !important;
  font-weight: 700 !important;
  letter-spacing: 0 !important;
}

.order-items-minimal {
  background: #ffffff;
  border: 1px solid #f1f5f9;
  border-radius: 16px;
  padding: 20px;
}

.items-header {
  font-size: 13px;
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 16px;
  display: flex;
  align-items: center;
}

.items-scroller {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.mini-item {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 14px;
  padding-bottom: 8px;
  border-bottom: 1px dashed #f1f5f9;
}

.mini-item:last-child { border: none; }

.mini-item .qty {
  color: #94a3b8;
  font-weight: 800;
  font-size: 12px;
}

.mini-item .name {
  flex: 1;
  color: #475569;
  font-weight: 500;
}

.mini-item .price {
  color: #0f172a;
  font-weight: 700;
}

.card-footer-interactive {
  padding: 16px 32px;
  background: #fafafa;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-top: 1px solid #f8fafc;
}

.footer-msg {
  font-size: 12px;
  font-weight: 600;
  color: #94a3b8;
}

.footer-arrow {
  color: #cbd5e1;
  transition: transform 0.3s ease;
}

.order-card-premium:hover .footer-arrow {
  transform: translateX(5px);
  color: #4338ca;
}

.order-card-premium:hover .footer-msg {
  color: #4338ca;
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100px 0;
  gap: 24px;
  color: #64748b;
  font-weight: 500;
}

.empty-state {
  text-align: center;
  padding: 80px 40px;
  background: #f8fafc;
  border-radius: 40px;
  border: 2px dashed #e2e8f0;
}

.empty-visual-box {
  margin-bottom: 32px;
}

.empty-state h2 {
  font-size: 26px;
  font-weight: 800;
  color: #0f172a;
  margin-bottom: 12px;
}

.empty-state p {
  color: #64748b;
  margin-bottom: 40px;
  max-width: 400px;
  margin-left: auto;
  margin-right: auto;
}

.explore-btn {
  height: 64px !important;
  padding: 0 48px !important;
  border-radius: 20px !important;
  text-transform: none !important;
  font-weight: 800 !important;
  font-size: 18px !important;
}

@media (max-width: 600px) {
  .my-orders-page { padding: 24px 16px; }
  .title-section { flex-direction: column; text-align: center; }
  .order-header-custom { padding: 20px; flex-direction: column; gap: 16px; align-items: flex-start; }
  .tracking-main { flex-direction: column; align-items: stretch; }
  .tracking-actions { flex-direction: column; }
}
</style>
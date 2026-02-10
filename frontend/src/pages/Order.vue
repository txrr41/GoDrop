
<template>
  <div class="my-orders-page">
    <div class="page-header">
      <h1>Meus Pedidos</h1>
      <p>Acompanhe o status de todos os seus pedidos</p>
    </div>

    <div v-if="orderStore.loading" class="loading-state">
      <v-progress-circular indeterminate color="primary" size="64" />
      <p>Carregando seus pedidos...</p>
    </div>


    <div v-else-if="orderStore.orders.length > 0" class="orders-list">
      <v-card
          v-for="order in orderStore.orders"
          :key="order.id"
          class="order-card"
          elevation="2"
          @click="showOrderDetails(order.id)"
      >
        <v-card-title class="order-header">
          <div class="header-left">
            <v-icon color="primary" class="mr-2">mdi-package-variant</v-icon>
            <span class="order-id">Pedido #{{ order.id }}</span>
          </div>
          <v-chip
              :color="getStatusColor(order.status)"
              variant="flat"
              size="small"
          >
            {{ getStatusLabel(order.status) }}
          </v-chip>
        </v-card-title>

        <v-divider />

        <v-card-text>
          <div class="order-info">
            <div class="info-section">
              <div class="info-row">
                <v-icon size="20" class="mr-2">mdi-currency-usd</v-icon>
                <span class="label">Total:</span>
                <span class="value">{{ formatCurrency(order.totalAmount) }}</span>
              </div>

              <div class="info-row">
                <v-icon size="20" class="mr-2">mdi-calendar</v-icon>
                <span class="label">Data:</span>
                <span class="value">{{ formatDate(order.createdAt) }}</span>
              </div>

              <div class="info-row">
                <v-icon size="20" class="mr-2">mdi-map-marker</v-icon>
                <span class="label">Endereço:</span>
                <span class="value">{{ order.shippingAddress }}</span>
              </div>
            </div>

            <v-alert
                v-if="order.trackingCode"
                type="info"
                variant="tonal"
                class="tracking-alert"
            >
              <div class="tracking-content">
                <div class="tracking-header">
                  <v-icon size="24">mdi-truck-fast</v-icon>
                  <span class="tracking-label">Código de Rastreio</span>
                </div>

                <div class="tracking-code-wrapper">
                  <span class="tracking-code">{{ order.trackingCode }}</span>
                  <v-btn
                      size="small"
                      variant="outlined"
                      color="primary"
                      @click.stop="copyTrackingCode(order.trackingCode)"
                  >
                    <v-icon start size="16">mdi-content-copy</v-icon>
                    Copiar
                  </v-btn>
                </div>

                <v-btn
                    size="small"
                    color="primary"
                    variant="text"
                    :href="getCorreiosLink(order.trackingCode)"
                    target="_blank"
                    @click.stop
                >
                  <v-icon start size="16">mdi-open-in-new</v-icon>
                  Rastrear nos Correios
                </v-btn>
              </div>
            </v-alert>
          </div>

          <div class="order-items">
            <p class="items-title">
              <v-icon size="18" class="mr-1">mdi-cart</v-icon>
              Produtos ({{ order.items.length }})
            </p>
            <ul class="items-list">
              <li v-for="item in order.items" :key="item.productName" class="item">
                <span class="item-quantity">{{ item.quantity }}x</span>
                <span class="item-name">{{ item.productName }}</span>
                <span class="item-price">{{ formatCurrency(item.totalPrice) }}</span>
              </li>
            </ul>
          </div>
        </v-card-text>

        <v-card-actions class="order-actions">
          <v-btn
              variant="text"
              color="primary"
              @click.stop="showOrderDetails(order.id)"
          >
            Ver Detalhes
            <v-icon end>mdi-arrow-right</v-icon>
          </v-btn>
        </v-card-actions>
      </v-card>
    </div>

    <div v-else class="empty-state">
      <div class="empty-icon">
        <v-icon size="120" color="grey-lighten-1">mdi-package-variant-closed</v-icon>
      </div>
      <h2>Nenhum pedido encontrado</h2>
      <p>Você ainda não realizou nenhuma compra</p>
      <v-btn
          color="primary"
          size="large"
          to="/produtos"
          class="mt-4"
      >
        <v-icon start>mdi-shopping</v-icon>
        Começar a Comprar
      </v-btn>
    </div>

    <v-snackbar
        v-model="snackbar"
        :timeout="2000"
        color="success"
    >
      Código de rastreio copiado!
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
.my-orders-page {
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
  min-height: calc(100vh - 64px);
}

.page-header {
  margin-bottom: 32px;
}

.page-header h1 {
  font-size: 32px;
  font-weight: 700;
  color: #1A2332;
  margin-bottom: 8px;
}

.page-header p {
  font-size: 16px;
  color: #5E6D82;
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
  gap: 20px;
}

.loading-state p {
  font-size: 16px;
  color: #5E6D82;
}

.orders-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.order-card {
  cursor: pointer;
  transition: all 0.3s ease;
  border-radius: 12px !important;
}

.order-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12) !important;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px !important;
}

.header-left {
  display: flex;
  align-items: center;
}

.order-id {
  font-size: 18px;
  font-weight: 600;
  color: #1A2332;
}

.order-info {
  margin-bottom: 20px;
}

.info-section {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 16px;
}

.info-row {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
}

.info-row .label {
  color: #5E6D82;
  font-weight: 500;
}

.info-row .value {
  color: #1A2332;
  font-weight: 600;
}

.tracking-alert {
  margin-top: 16px;
}

.tracking-content {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.tracking-header {
  display: flex;
  align-items: center;
  gap: 8px;
}

.tracking-label {
  font-size: 14px;
  font-weight: 600;
  color: #1976D2;
}

.tracking-code-wrapper {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.tracking-code {
  font-family: 'Courier New', monospace;
  font-size: 18px;
  font-weight: 700;
  color: #1565C0;
  background: rgba(25, 118, 210, 0.1);
  padding: 8px 16px;
  border-radius: 6px;
  letter-spacing: 1px;
}

.order-items {
  background: #F5F7FA;
  padding: 16px;
  border-radius: 8px;
}

.items-title {
  font-size: 14px;
  font-weight: 600;
  color: #1A2332;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
}

.items-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.item {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 14px;
}

.item-quantity {
  color: #5E6D82;
  font-weight: 600;
  min-width: 30px;
}

.item-name {
  flex: 1;
  color: #1A2332;
}

.item-price {
  color: #1565C0;
  font-weight: 600;
}

.order-actions {
  padding: 16px 24px !important;
  border-top: 1px solid #E3E8EF;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
  text-align: center;
}

.empty-icon {
  margin-bottom: 24px;
  opacity: 0.5;
}

.empty-state h2 {
  font-size: 24px;
  font-weight: 600;
  color: #1A2332;
  margin-bottom: 8px;
}

.empty-state p {
  font-size: 16px;
  color: #5E6D82;
  margin-bottom: 24px;
}

@media (max-width: 768px) {
  .my-orders-page {
    padding: 16px;
  }

  .page-header h1 {
    font-size: 24px;
  }

  .tracking-code {
    font-size: 14px;
  }
}
</style>
<template>
  <div class="admin-orders-page">
    <div class="page-header">
      <div class="header-content">
        <div class="title-section">
          <div class="brand-accent">
            <v-icon size="32" color="primary">mdi-shield-crown-outline</v-icon>
          </div>
          <div class="title-text">
            <span class="pre-title">Gerenciamento Executivo</span>
            <h1>Fluxo de Pedidos</h1>
          </div>
        </div>

        <v-btn
            class="action-btn-sync"
            variant="flat"
            @click="loadOrders"
            :loading="adminStore.loading"
        >
          <v-icon start size="18">mdi-rotate-right</v-icon>
          Sincronizar
        </v-btn>
      </div>


      <div class="stats-grid">
        <div class="stat-glass-card pending">
          <div class="stat-meta">
            <span class="stat-label">Pendentes</span>
            <v-icon size="16" class="stat-icon">mdi-clock-outline</v-icon>
          </div>
          <div class="stat-data">{{ adminStore.pendingOrders.length }}</div>
        </div>

        <div class="stat-glass-card approved">
          <div class="stat-meta">
            <span class="stat-label">Aprovados</span>
            <v-icon size="16" class="stat-icon">mdi-check-all</v-icon>
          </div>
          <div class="stat-data">{{ adminStore.approvedOrders.length }}</div>
        </div>

        <div class="stat-glass-card processing">
          <div class="stat-meta">
            <span class="stat-label">Produção</span>
            <v-icon size="16" class="stat-icon">mdi-package-variant</v-icon>
          </div>
          <div class="stat-data">{{ adminStore.processingOrders.length }}</div>
        </div>

        <div class="stat-glass-card shipped">
          <div class="stat-meta">
            <span class="stat-label">Enviados</span>
            <v-icon size="16" class="stat-icon">mdi-truck-outline</v-icon>
          </div>
          <div class="stat-data">{{ adminStore.shippedOrders.length }}</div>
        </div>
      </div>
    </div>

    <div v-if="adminStore.loading" class="premium-loader">
      <v-progress-circular indeterminate color="primary" size="48" width="3" />
      <span class="loader-text">Consultando base de dados...</span>
    </div>

    <v-card v-else class="master-table-card" elevation="0">
      <div class="table-top-bar">
        <div class="bar-left">
          <h2>Ordens de Serviço</h2>
          <div class="counter-badge">{{ adminStore.orders.length }}</div>
        </div>
      </div>

      <v-data-table
          :headers="headers"
          :items="adminStore.orders"
          :items-per-page="15"
          class="elegant-table"
          hover
      >
        <template v-slot:item.id="{ item }">
          <span class="id-label">#{{ item.id }}</span>
        </template>

        <template v-slot:item.buyerName="{ item }">
          <div class="profile-stack">
            <div class="avatar-minimal">{{ item.buyerName.charAt(0) }}</div>
            <div class="profile-info">
              <span class="p-name">{{ item.buyerName }}</span>
              <span class="p-email">{{ item.buyerEmail }}</span>
            </div>
          </div>
        </template>

        <template v-slot:item.totalAmount="{ item }">
          <span class="value-text">{{ formatCurrency(item.totalAmount) }}</span>
        </template>

        <template v-slot:item.status="{ item }">
          <div :class="['status-dot-pill', item.status.toLowerCase().replace('_', '')]">
            <span class="dot-indicator"></span>
            {{ getStatusLabel(item.status) }}
          </div>
        </template>

        <template v-slot:item.createdAt="{ item }">
          <div class="timestamp-cell">
            {{ formatDate(item.createdAt) }}
          </div>
        </template>

        <template v-slot:item.actions="{ item }">
          <div class="table-actions">
            <v-btn
                v-if="item.status === 'PAYMENT_APPROVED'"
                class="btn-ops process"
                variant="flat"
                @click="startProcessing(item)"
            >
              Processar
            </v-btn>

            <v-btn
                v-if="item.status === 'PROCESSING'"
                class="btn-ops ship"
                variant="outlined"
                @click="openShipDialog(item)"
            >
              Despachar
            </v-btn>

            <div v-if="item.trackingCode" class="tracking-summary">
              <v-icon size="14">mdi-link-variant</v-icon>
              <span>{{ item.trackingCode }}</span>
            </div>
          </div>
        </template>
      </v-data-table>
    </v-card>

    <v-dialog v-model="shipDialog" max-width="500px" persistent transition="scale-transition">
      <v-card class="shipping-modal-card">
        <div class="modal-gradient-bg"></div>

        <div class="modal-header-premium">
          <div class="icon-box">
            <v-icon color="white" size="24">mdi-truck-fast-outline</v-icon>
          </div>
          <div class="header-text-group">
            <h3>Logística de Envio</h3>
            <p>Pedido: <span class="highlight">#{{ selectedOrder?.id }}</span></p>
          </div>
          <v-btn
              icon="mdi-close"
              variant="text"
              size="small"
              class="close-btn"
              @click="closeShipDialog"
          ></v-btn>
        </div>

        <v-card-text class="pa-8">
          <div class="shipping-info-box">
            <v-icon size="20" color="primary">mdi-information-outline</v-icon>
            <p>Ao inserir o código, o status do pedido será alterado para <strong>Enviado</strong> e o cliente será notificado.</p>
          </div>

          <div class="input-wrapper">
            <label class="custom-field-label">Código de Rastreamento</label>
            <v-text-field
                v-model="trackingCode"
                bg-color="#F8FAFC"
                class="premium-input-field"
                color="primary"
                hide-details
                placeholder="Ex: AA123456789BR"
                prepend-inner-icon="mdi-barcode-scan"
                variant="flat"
            >
              <template v-slot:append-inner>
                <v-fade-transition>
                  <v-icon v-if="trackingCode.length > 5" color="success">mdi-check-circle</v-icon>
                </v-fade-transition>
              </template>
            </v-text-field>
          </div>
        </v-card-text>

        <v-divider class="opacity-10"></v-divider>

        <v-card-actions class="pa-8">
          <v-btn
              variant="text"
              class="btn-cancel"
              @click="closeShipDialog"
          >
            Cancelar
          </v-btn>
          <v-spacer></v-spacer>
          <v-btn
              class="btn-confirm-shipping"
              elevation="0"
              :loading="shipping"
              :disabled="!trackingCode"
              @click="confirmShip"
          >
            Finalizar Envio
            <v-icon end size="18">mdi-arrow-right</v-icon>
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-snackbar v-model="snackbar.show" :color="snackbar.color" location="bottom right" flat class="premium-snack">
      <div class="snack-content">
        <v-icon size="20" class="mr-2">{{ snackbar.color === 'error' ? 'mdi-alert-circle-outline' : 'mdi-check-circle-outline' }}</v-icon>
        {{ snackbar.message }}
      </div>
    </v-snackbar>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAdminOrderStore } from '../stores/adminOrder'

const adminStore = useAdminOrderStore()
const shipDialog = ref(false)
const selectedOrder = ref(null)
const trackingCode = ref('')
const shipping = ref(false)
const snackbar = ref({ show: false, message: '', color: 'success' })

const headers = [
  { title: 'ORDEM', key: 'id', sortable: true, width: '120px' },
  { title: 'CLIENTE', key: 'buyerName', sortable: false },
  { title: 'TOTAL', key: 'totalAmount', sortable: true },
  { title: 'STATUS', key: 'status', sortable: true },
  { title: 'DATA', key: 'createdAt', sortable: true },
  { title: 'AÇÕES', key: 'actions', sortable: false, align: 'end' }
]

onMounted(() => { loadOrders() })
const loadOrders = async () => {
  try { await adminStore.fetchAllOrders() } catch (e) { showSnackbar('Erro ao carregar pedidos', 'error') }
}
const startProcessing = async (order) => {
  try {
    await adminStore.updateOrderStatus(order.id, 'PROCESSING')
    showSnackbar(`Pedido #${order.id} em fase de preparação`)
  } catch (e) { showSnackbar('Erro ao atualizar status', 'error') }
}
const openShipDialog = (order) => {
  selectedOrder.value = order
  trackingCode.value = ''
  shipDialog.value = true
}
const closeShipDialog = () => {
  shipDialog.value = false
  selectedOrder.value = null
}
const confirmShip = async () => {
  try {
    shipping.value = true
    await adminStore.shipOrder(selectedOrder.value.id, trackingCode.value)
    showSnackbar(`Pedido #${selectedOrder.value.id} enviado`)
    closeShipDialog()
  } catch (e) { showSnackbar('Erro ao despachar', 'error') } finally { shipping.value = false }
}
const showSnackbar = (message, color = 'success') => { snackbar.value = { show: true, message, color } }
const formatCurrency = (v) => v.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
const formatDate = (d) => new Date(d).toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit', year: '2-digit', hour: '2-digit', minute: '2-digit' })
const getStatusLabel = (s) => {
  const l = { 'PENDING': 'Aguardando', 'PAYMENT_APPROVED': 'Aprovado', 'PROCESSING': 'Em Produção', 'SHIPPED': 'Enviado', 'DELIVERED': 'Entregue', 'CANCELLED': 'Cancelado' }
  return l[s] || s
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap');

.admin-orders-page {
  padding: 32px 48px;
  width: 100%;
  min-height: 100vh;
  background-color: #F8FAFC;
  color: #1E293B;
  font-family: 'Inter', sans-serif;
  box-sizing: border-box;
}

/* Header */
.page-header {
  margin-bottom: 40px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
}

.title-section {
  display: flex;
  align-items: center;
  gap: 20px;
}

.brand-accent {
  background: #FFFFFF;
  width: 56px;
  height: 56px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.03);
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.pre-title {
  display: block;
  font-size: 11px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 1.5px;
  color: #94A3B8;
  margin-bottom: 2px;
}

.title-text h1 {
  font-size: 32px;
  font-weight: 800;
  letter-spacing: -0.8px;
  color: #0F172A;
}

.action-btn-sync {
  background: #FFFFFF !important;
  color: #475569 !important;
  border-radius: 12px !important;
  border: 1px solid #E2E8F0 !important;
  text-transform: none !important;
  font-weight: 600 !important;
  height: 44px !important;
}

/* Stats Grid */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

.stat-glass-card {
  background: #FFFFFF;
  padding: 28px;
  border-radius: 24px;
  border: 1px solid rgba(0, 0, 0, 0.04);
}

.stat-label {
  font-size: 13px;
  font-weight: 600;
  color: #64748B;
}

.stat-data {
  font-size: 36px;
  font-weight: 800;
  color: #0F172A;
  margin-top: 8px;
}

.stat-icon { color: #CBD5E1; }
.stat-meta { display: flex; justify-content: space-between; align-items: center; }

/* Status Accents */
.pending { border-bottom: 4px solid #F59E0B; }
.approved { border-bottom: 4px solid #3B82F6; }
.processing { border-bottom: 4px solid #8B5CF6; }
.shipped { border-bottom: 4px solid #10B981; }

/* Table Full Width */
.master-table-card {
  background: #FFFFFF !important;
  border-radius: 24px !important;
  border: 1px solid rgba(0, 0, 0, 0.05) !important;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.02) !important;
  margin-top: 32px;
}

.table-top-bar {
  padding: 24px 32px;
  border-bottom: 1px solid #F1F5F9;
  display: flex;
  align-items: center;
}

.counter-badge {
  background: #F1F5F9;
  padding: 4px 12px;
  border-radius: 8px;
  font-size: 12px;
  font-weight: 700;
  color: #64748B;
  margin-left: 12px;
}

.elegant-table {
  width: 100%;
}

:deep(.v-data-table-header th) {
  background: #F8FAFC !important;
  font-size: 11px !important;
  font-weight: 800 !important;
  text-transform: uppercase;
  color: #94A3B8 !important;
  height: 60px !important;
}

:deep(.v-data-table__td) {
  height: 80px !important;
  border-bottom: 1px solid #F1F5F9 !important;
}

/* User Profile Cell */
.profile-stack {
  display: flex;
  align-items: center;
  gap: 16px;
}

.avatar-minimal {
  width: 40px;
  height: 40px;
  background: #F1F5F9;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 800;
  color: #475569;
}

.p-name { font-weight: 700; color: #0F172A; display: block; }
.p-email { font-size: 12px; color: #94A3B8; }

/* Status Pills */
.status-dot-pill {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 6px 14px;
  border-radius: 100px;
  font-size: 12px;
  font-weight: 700;
}

.dot-indicator { width: 8px; height: 8px; border-radius: 50%; }
.status-dot-pill.pending { background: #FFF7ED; color: #9A3412; }
.status-dot-pill.pending .dot-indicator { background: #F59E0B; }
.status-dot-pill.paymentapproved { background: #EFF6FF; color: #1E40AF; }
.status-dot-pill.paymentapproved .dot-indicator { background: #3B82F6; }
.status-dot-pill.processing { background: #FAF5FF; color: #6B21A8; }
.status-dot-pill.processing .dot-indicator { background: #8B5CF6; }
.status-dot-pill.shipped { background: #F0FDF4; color: #166534; }
.status-dot-pill.shipped .dot-indicator { background: #10B981; }

/* Actions */
.btn-ops {
  border-radius: 10px !important;
  text-transform: none !important;
  font-weight: 700 !important;
  font-size: 13px !important;
  height: 38px !important;
}

.btn-ops.process { background: #0F172A !important; color: white !important; }
.btn-ops.ship { border: 1px solid #E2E8F0 !important; color: #475569 !important; }

/* --- NOVO MODAL DE ENVIO --- */
.shipping-modal-card {
  border-radius: 28px !important;
  overflow: hidden !important;
  border: none !important;
  background: #FFFFFF !important;
}

.modal-gradient-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 120px;
  background: linear-gradient(135deg, #0F172A 0%, #1E293B 100%);
  z-index: 0;
}

.modal-header-premium {
  position: relative;
  z-index: 1;
  padding: 32px 32px 10px 32px;
  display: flex;
  align-items: center;
  gap: 16px;
}

.icon-box {
  width: 48px;
  height: 48px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(8px);
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.header-text-group h3 {
  color: white;
  font-size: 20px;
  font-weight: 700;
}

.header-text-group p {
  color: rgba(255, 255, 255, 0.7);
  font-size: 13px;
  margin-top: 2px;
}

.header-text-group .highlight {
  color: white;
  font-weight: 700;
}

.close-btn {
  margin-left: auto;
  color: rgba(255, 255, 255, 0.5) !important;
}

.shipping-info-box {
  background: #F0F9FF;
  border: 1px solid #BAE6FD;
  padding: 16px;
  border-radius: 16px;
  display: flex;
  gap: 12px;
  margin-bottom: 32px;
}

.shipping-info-box p {
  font-size: 13px;
  line-height: 1.5;
  color: #0369A1;
}

.custom-field-label {
  display: block;
  font-size: 13px;
  font-weight: 700;
  color: #475569;
  margin-bottom: 8px;
  margin-left: 4px;
}

.premium-input-field {
  border-radius: 14px !important;
  overflow: hidden;
}

:deep(.premium-input-field .v-field) {
  border: 1px solid #E2E8F0 !important;
  box-shadow: none !important;
  transition: all 0.2s ease;
}

:deep(.premium-input-field .v-field--focused) {
  border-color: #3B82F6 !important;
  background: white !important;
  box-shadow: 0 0 0 4px rgba(59, 130, 246, 0.1) !important;
}

.btn-cancel {
  font-weight: 700 !important;
  color: #94A3B8 !important;
  text-transform: none !important;
}

.btn-confirm-shipping {
  background: #3B82F6 !important;
  color: white !important;
  text-transform: none !important;
  font-weight: 700 !important;
  padding: 0 24px !important;
  height: 50px !important;
  border-radius: 16px !important;
  letter-spacing: 0 !important;
}

.btn-confirm-shipping:disabled {
  background: #E2E8F0 !important;
  color: #94A3B8 !important;
}

@media (max-width: 1200px) {
  .stats-grid { grid-template-columns: repeat(2, 1fr); }
}
@media (max-width: 600px) {
  .admin-orders-page { padding: 20px; }
  .stats-grid { grid-template-columns: 1fr; }
}
</style>
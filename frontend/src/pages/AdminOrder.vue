
<template>
  <div class="admin-orders-page">
    <div class="page-header">
      <div class="header-content">
        <div class="title-section">
          <v-icon size="32" color="primary" class="mr-3">mdi-shield-crown</v-icon>
          <div>
            <h1>Painel Administrativo</h1>
            <p>Gerenciar todos os pedidos do sistema</p>
          </div>
        </div>

        <v-btn
            color="primary"
            variant="flat"
            prepend-icon="mdi-refresh"
            @click="loadOrders"
            :loading="adminStore.loading"
        >
          Atualizar
        </v-btn>
      </div>

      <div class="stats-cards">
        <v-card class="stat-card" elevation="1">
          <v-card-text>
            <div class="stat-icon pending">
              <v-icon size="32">mdi-clock-outline</v-icon>
            </div>
            <div class="stat-info">
              <span class="stat-value">{{ adminStore.pendingOrders.length }}</span>
              <span class="stat-label">Aguardando Pagamento</span>
            </div>
          </v-card-text>
        </v-card>

        <v-card class="stat-card" elevation="1">
          <v-card-text>
            <div class="stat-icon approved">
              <v-icon size="32">mdi-check-circle</v-icon>
            </div>
            <div class="stat-info">
              <span class="stat-value">{{ adminStore.approvedOrders.length }}</span>
              <span class="stat-label">Pagos (Preparar)</span>
            </div>
          </v-card-text>
        </v-card>

        <v-card class="stat-card" elevation="1">
          <v-card-text>
            <div class="stat-icon processing">
              <v-icon size="32">mdi-package-variant</v-icon>
            </div>
            <div class="stat-info">
              <span class="stat-value">{{ adminStore.processingOrders.length }}</span>
              <span class="stat-label">Em Preparação</span>
            </div>
          </v-card-text>
        </v-card>

        <v-card class="stat-card" elevation="1">
          <v-card-text>
            <div class="stat-icon shipped">
              <v-icon size="32">mdi-truck-fast</v-icon>
            </div>
            <div class="stat-info">
              <span class="stat-value">{{ adminStore.shippedOrders.length }}</span>
              <span class="stat-label">Despachados</span>
            </div>
          </v-card-text>
        </v-card>
      </div>
    </div>

    <div v-if="adminStore.loading" class="loading-state">
      <v-progress-circular indeterminate color="primary" size="64" />
      <p>Carregando pedidos...</p>
    </div>

    <v-card v-else class="orders-table-card" elevation="2">
      <v-card-title class="table-header">
        <v-icon class="mr-2">mdi-format-list-bulleted</v-icon>
        Todos os Pedidos ({{ adminStore.orders.length }})
      </v-card-title>

      <v-divider />

      <v-data-table
          :headers="headers"
          :items="adminStore.orders"
          :items-per-page="10"
          class="orders-table"
      >

        <template v-slot:item.id="{ item }">
          <span class="order-id">#{{ item.id }}</span>
        </template>


        <template v-slot:item.buyerName="{ item }">
          <div class="customer-info">
            <v-icon size="20" class="mr-2">mdi-account</v-icon>
            <div>
              <div class="customer-name">{{ item.buyerName }}</div>
              <div class="customer-email">{{ item.buyerEmail }}</div>
            </div>
          </div>
        </template>

        <template v-slot:item.totalAmount="{ item }">
          <span class="order-total">{{ formatCurrency(item.totalAmount) }}</span>
        </template>


        <template v-slot:item.status="{ item }">
          <v-chip
              :color="getStatusColor(item.status)"
              variant="flat"
              size="small"
          >
            {{ getStatusLabel(item.status) }}
          </v-chip>
        </template>


        <template v-slot:item.createdAt="{ item }">
          <span class="order-date">{{ formatDate(item.createdAt) }}</span>
        </template>

        <template v-slot:item.actions="{ item }">
          <div class="action-buttons">
            <v-btn
                v-if="item.status === 'PAYMENT_APPROVED'"
                size="small"
                color="primary"
                variant="tonal"
                @click="startProcessing(item)"
            >
              <v-icon start size="18">mdi-play</v-icon>
              Preparar
            </v-btn>

            <v-btn
                v-if="item.status === 'PROCESSING'"
                size="small"
                color="success"
                variant="tonal"
                @click="openShipDialog(item)"
            >
              <v-icon start size="18">mdi-truck-delivery</v-icon>
              Despachar
            </v-btn>

            <div v-if="item.trackingCode" class="tracking-badge">
              <v-icon size="16" color="success">mdi-check</v-icon>
              {{ item.trackingCode }}
            </div>
          </div>
        </template>
      </v-data-table>
    </v-card>

    <v-dialog v-model="shipDialog" max-width="500px">
      <v-card>
        <v-card-title class="dialog-title">
          <v-icon color="success" class="mr-2">mdi-truck-delivery</v-icon>
          Despachar Pedido #{{ selectedOrder?.id }}
        </v-card-title>

        <v-divider />

        <v-card-text class="pa-6">
          <p class="mb-4">Informe o código de rastreio dos Correios:</p>

          <v-text-field
              v-model="trackingCode"
              label="Código de Rastreio"
              placeholder="BR123456789BR"
              variant="outlined"
              density="comfortable"
              prepend-inner-icon="mdi-barcode"
              :rules="[v => !!v || 'Campo obrigatório']"
              hint="Exemplo: BR123456789BR"
              persistent-hint
          />

          <v-alert
              type="info"
              variant="tonal"
              class="mt-4"
          >
            <div class="alert-content">
              <v-icon size="20">mdi-information</v-icon>
              <span>O cliente receberá um email com o código de rastreio</span>
            </div>
          </v-alert>
        </v-card-text>

        <v-divider />

        <v-card-actions class="pa-4">
          <v-spacer />
          <v-btn
              variant="text"
              @click="closeShipDialog"
          >
            Cancelar
          </v-btn>
          <v-btn
              color="success"
              variant="flat"
              :loading="shipping"
              :disabled="!trackingCode"
              @click="confirmShip"
          >
            <v-icon start>mdi-send</v-icon>
            Confirmar Despacho
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-snackbar
        v-model="snackbar.show"
        :color="snackbar.color"
        :timeout="3000"
    >
      {{ snackbar.message }}
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

const snackbar = ref({
  show: false,
  message: '',
  color: 'success'
})

const headers = [
  { title: 'ID', key: 'id', sortable: true },
  { title: 'Cliente', key: 'buyerName', sortable: false },
  { title: 'Total', key: 'totalAmount', sortable: true },
  { title: 'Status', key: 'status', sortable: true },
  { title: 'Data', key: 'createdAt', sortable: true },
  { title: 'Ações', key: 'actions', sortable: false, align: 'end' }
]


onMounted(() => {
  loadOrders()
})

const loadOrders = async () => {
  try {
    await adminStore.fetchAllOrders()
  } catch (error) {
    showSnackbar('Erro ao carregar pedidos', 'error')
  }
}

const startProcessing = async (order) => {
  try {
    await adminStore.updateOrderStatus(order.id, 'PROCESSING')
    showSnackbar(`Pedido #${order.id} marcado como "Em Preparação"`, 'success')
  } catch (error) {
    showSnackbar('Erro ao atualizar status', 'error')
  }
}

const openShipDialog = (order) => {
  selectedOrder.value = order
  trackingCode.value = ''
  shipDialog.value = true
}

const closeShipDialog = () => {
  shipDialog.value = false
  selectedOrder.value = null
  trackingCode.value = ''
}

const confirmShip = async () => {
  if (!trackingCode.value.trim()) {
    showSnackbar('Informe o código de rastreio', 'warning')
    return
  }

  try {
    shipping.value = true

    await adminStore.shipOrder(selectedOrder.value.id, trackingCode.value)

    showSnackbar(
        `Pedido #${selectedOrder.value.id} despachado com sucesso!`,
        'success'
    )

    closeShipDialog()

  } catch (error) {
    showSnackbar('Erro ao despachar pedido', 'error')
  } finally {
    shipping.value = false
  }
}

const showSnackbar = (message, color = 'success') => {
  snackbar.value = { show: true, message, color }
}

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
    'PROCESSING': 'Em Preparação',
    'SHIPPED': 'Despachado',
    'DELIVERED': 'Entregue',
    'CANCELLED': 'Cancelado'
  }
  return labels[status] || status
}
</script>

<style scoped>
.admin-orders-page {
  padding: 24px;
  max-width: 1600px;
  margin: 0 auto;
  min-height: calc(100vh - 64px);
  background: #F5F7FA;
}

.page-header {
  margin-bottom: 24px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.title-section {
  display: flex;
  align-items: center;
}

.title-section h1 {
  font-size: 28px;
  font-weight: 700;
  color: #1A2332;
  margin: 0;
}

.title-section p {
  font-size: 14px;
  color: #5E6D82;
  margin: 4px 0 0 0;
}

.stats-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 16px;
}

.stat-card {
  border-radius: 12px !important;
}

.stat-card .v-card-text {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px !important;
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-icon.pending {
  background: #FFF3E0;
  color: #F57C00;
}

.stat-icon.approved {
  background: #E3F2FD;
  color: #1976D2;
}

.stat-icon.processing {
  background: #E8EAF6;
  color: #5E35B1;
}

.stat-icon.shipped {
  background: #E8F5E9;
  color: #388E3C;
}

.stat-info {
  display: flex;
  flex-direction: column;
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
  color: #1A2332;
  line-height: 1;
}

.stat-label {
  font-size: 13px;
  color: #5E6D82;
  margin-top: 4px;
}

.orders-table-card {
  border-radius: 12px !important;
}

.table-header {
  font-size: 18px !important;
  font-weight: 600 !important;
  padding: 20px 24px !important;
}

.order-id {
  font-weight: 700;
  color: #1976D2;
}

.customer-info {
  display: flex;
  align-items: center;
}

.customer-name {
  font-weight: 600;
  color: #1A2332;
  font-size: 14px;
}

.customer-email {
  font-size: 12px;
  color: #5E6D82;
}

.order-total {
  font-weight: 700;
  color: #1565C0;
  font-size: 15px;
}

.order-date {
  font-size: 13px;
  color: #5E6D82;
}

.action-buttons {
  display: flex;
  gap: 8px;
  align-items: center;
  justify-content: flex-end;
}

.tracking-badge {
  display: flex;
  align-items: center;
  gap: 6px;
  background: #E8F5E9;
  padding: 6px 12px;
  border-radius: 16px;
  font-size: 12px;
  font-weight: 600;
  color: #2E7D32;
}

.dialog-title {
  font-size: 20px !important;
  font-weight: 600 !important;
  padding: 20px 24px !important;
}

.alert-content {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
  gap: 20px;
}

@media (max-width: 768px) {
  .admin-orders-page {
    padding: 16px;
  }

  .header-content {
    flex-direction: column;
    gap: 16px;
    align-items: flex-start;
  }

  .stats-cards {
    grid-template-columns: 1fr;
  }
}
</style>
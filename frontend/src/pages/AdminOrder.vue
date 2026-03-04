<template>
  <div class="page-root">

    <header class="page-topbar">
      <div class="tb-left">
        <p class="tb-breadcrumb">Admin <span class="sep">/</span> <span class="current">Pedidos</span></p>
        <h1 class="tb-title">Fluxo de Pedidos</h1>
      </div>
      <button class="tb-btn-sync" @click="loadOrders" :disabled="adminStore.loading">
        <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
          <path d="M21 2v6h-6M3 12a9 9 0 0 1 15-6.7L21 8M3 22v-6h6M21 12a9 9 0 0 1-15 6.7L3 16"/>
        </svg>
        Sincronizar
      </button>
    </header>

    <section class="kpi-strip">
      <div class="kpi-tile">
        <div class="kpi-head"><span class="kpi-label">Pendentes</span><span class="kpi-dot dot-amber"></span></div>
        <p class="kpi-val">{{ adminStore.pendingOrders.length }}</p>
        <p class="kpi-sub">Aguardando pagamento</p>
      </div>
      <div class="kpi-div"></div>
      <div class="kpi-tile">
        <div class="kpi-head"><span class="kpi-label">Aprovados</span><span class="kpi-dot dot-blue"></span></div>
        <p class="kpi-val">{{ adminStore.approvedOrders.length }}</p>
        <p class="kpi-sub">Prontos para processar</p>
      </div>
      <div class="kpi-div"></div>
      <div class="kpi-tile">
        <div class="kpi-head"><span class="kpi-label">Em Produção</span><span class="kpi-dot dot-purple"></span></div>
        <p class="kpi-val">{{ adminStore.processingOrders.length }}</p>
        <p class="kpi-sub">Sendo preparados</p>
      </div>
      <div class="kpi-div"></div>
      <div class="kpi-tile">
        <div class="kpi-head"><span class="kpi-label">Enviados</span><span class="kpi-dot dot-green"></span></div>
        <p class="kpi-val">{{ adminStore.shippedOrders.length }}</p>
        <p class="kpi-sub">A caminho do cliente</p>
      </div>
    </section>

    <div v-if="adminStore.loading" class="loader-state">
      <div class="loader-ring"></div>
      <span>Carregando pedidos...</span>
    </div>

    <section v-else class="table-card">
      <div class="table-topbar">
        <h2 class="table-title">Ordens de Serviço <span class="count-badge">{{ adminStore.orders.length }}</span></h2>
      </div>

      <v-data-table
          :headers="headers"
          :items="adminStore.orders"
          :items-per-page="15"
          class="data-table"
          hover
      >
        <template v-slot:item.id="{ item }">
          <span class="id-tag">#{{ item.id }}</span>
        </template>

        <template v-slot:item.buyerName="{ item }">
          <div class="buyer-cell">
            <div class="buyer-avatar">{{ item.buyerName.charAt(0) }}</div>
            <div>
              <p class="buyer-name">{{ item.buyerName }}</p>
              <p class="buyer-email">{{ item.buyerEmail }}</p>
            </div>
          </div>
        </template>

        <template v-slot:item.totalAmount="{ item }">
          <span class="amount-val">{{ formatCurrency(item.totalAmount) }}</span>
        </template>

        <template v-slot:item.status="{ item }">
          <span :class="['status-pill', 'status-' + item.status.toLowerCase().replace('_', '')]">
            <span class="status-dot-sm"></span>
            {{ getStatusLabel(item.status) }}
          </span>
        </template>

        <template v-slot:item.createdAt="{ item }">
          <span class="date-val">{{ formatDate(item.createdAt) }}</span>
        </template>

        <template v-slot:item.actions="{ item }">
          <div class="row-actions">
            <button class="act-btn act-info" @click="openInfoDialog(item)">
              <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="2" y="5" width="20" height="14" rx="2"/>
                <path d="M2 10h20"/>
              </svg>
              Envio
            </button>
            <button v-if="item.status === 'PAYMENT_APPROVED'" class="act-btn act-process"
                    @click="startProcessing(item)">
              Processar
            </button>
            <button v-if="item.status === 'PROCESSING'" class="act-btn act-ship" @click="openShipDialog(item)">
              Despachar
            </button>
            <span v-if="item.trackingCode" class="tracking-tag">
              <svg width="11" height="11" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path
                  d="M10 13a5 5 0 0 0 7.54.54l3-3a5 5 0 0 0-7.07-7.07l-1.72 1.71"/><path
                  d="M14 11a5 5 0 0 0-7.54-.54l-3 3a5 5 0 0 0 7.07 7.07l1.71-1.71"/></svg>
              {{ item.trackingCode }}
            </span>
          </div>
        </template>
      </v-data-table>
    </section>

    <!-- MODAL: DADOS DE ENVIO -->
    <v-dialog v-model="infoDialog" max-width="580" scrollable transition="scale-transition">
      <div v-if="infoOrder" class="modal-card">
        <div class="modal-hd">
          <div>
            <p class="modal-pre">Pedido #{{ infoOrder.id }}</p>
            <h2 class="modal-title">Dados para Envio</h2>
          </div>
          <button class="modal-close-btn" @click="infoDialog = false">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M18 6 6 18M6 6l12 12"/>
            </svg>
          </button>
        </div>

        <div class="modal-body">
          <div class="info-block">
            <p class="block-label">DESTINATÁRIO</p>
            <div class="two-col">
              <div class="info-field">
                <span class="field-key">Nome</span>
                <div class="field-row">
                  <span class="field-val">{{ infoOrder.buyerName }}</span>
                  <button class="copy-sm" @click="copy(infoOrder.buyerName)">
                    <svg width="11" height="11" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <rect x="9" y="9" width="13" height="13" rx="2"/>
                      <path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"/>
                    </svg>
                  </button>
                </div>
              </div>
              <div class="info-field">
                <span class="field-key">E-mail</span>
                <div class="field-row">
                  <span class="field-val">{{ infoOrder.buyerEmail }}</span>
                  <button class="copy-sm" @click="copy(infoOrder.buyerEmail)">
                    <svg width="11" height="11" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <rect x="9" y="9" width="13" height="13" rx="2"/>
                      <path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"/>
                    </svg>
                  </button>
                </div>
              </div>
            </div>
          </div>

          <div class="block-divider"></div>

          <div class="info-block">
            <p class="block-label">ENDEREÇO</p>
            <div class="address-box">
              <span class="address-text">{{ infoOrder.shippingAddress }}</span>
              <button class="copy-btn" @click="copy(infoOrder.shippingAddress)">Copiar</button>
            </div>
          </div>

          <div class="block-divider"></div>

          <div class="info-block">
            <p class="block-label">ITENS ({{ infoOrder.items?.length ?? 0 }})</p>
            <div class="items-list">
              <div v-for="(it, i) in infoOrder.items" :key="i" class="item-row">
                <span class="item-qty">{{ it.quantity }}x</span>
                <span class="item-name">{{ it.productName }}</span>
                <span class="item-price">{{ formatCurrency(it.totalPrice) }}</span>
              </div>
            </div>
          </div>

          <div class="block-divider"></div>

          <div class="info-block">
            <p class="block-label">RESUMO</p>
            <div class="summary-rows">
              <div class="sum-row">
                <span>Total</span>
                <strong class="sum-highlight">{{ formatCurrency(infoOrder.totalAmount) }}</strong>
              </div>
              <div class="sum-row">
                <span>Data</span>
                <span>{{ formatDate(infoOrder.createdAt) }}</span>
              </div>
              <div class="sum-row">
                <span>Status</span>
                <span :class="['status-pill', 'status-' + infoOrder.status.toLowerCase().replace('_','')]">
                  <span class="status-dot-sm"></span>{{ getStatusLabel(infoOrder.status) }}
                </span>
              </div>
            </div>
          </div>

          <template v-if="infoOrder.trackingCode">
            <div class="block-divider"></div>
            <div class="info-block">
              <p class="block-label">RASTREIO</p>
              <div class="tracking-box">
                <span class="tracking-code">{{ infoOrder.trackingCode }}</span>
                <button class="copy-btn" @click="copy(infoOrder.trackingCode)">Copiar</button>
              </div>
            </div>
          </template>
        </div>

        <div class="modal-ft">
          <button class="ft-copy-all" @click="copyAll(infoOrder)">
            <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2"/>
              <rect x="8" y="2" width="8" height="4" rx="1" ry="1"/>
            </svg>
            Copiar Tudo
          </button>
          <button class="ft-close" @click="infoDialog = false">Fechar</button>
        </div>
      </div>
    </v-dialog>

    <!-- MODAL: DESPACHAR -->
    <v-dialog v-model="shipDialog" max-width="460" persistent transition="scale-transition">
      <div class="modal-card">
        <div class="modal-hd">
          <div>
            <p class="modal-pre">Pedido #{{ selectedOrder?.id }}</p>
            <h2 class="modal-title">Despachar Pedido</h2>
          </div>
          <button class="modal-close-btn" @click="closeShipDialog">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M18 6 6 18M6 6l12 12"/>
            </svg>
          </button>
        </div>

        <div class="modal-body">
          <div class="ship-info-box">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <path d="M12 16v-4M12 8h.01"/>
            </svg>
            <p>Ao inserir o código, o status muda para <strong>Enviado</strong> e o cliente é notificado.</p>
          </div>
          <div class="field-group">
            <label class="field-label">Código de Rastreamento</label>
            <input v-model="trackingCode" class="field-input" placeholder="Ex: AA123456789BR"/>
          </div>
        </div>

        <div class="modal-ft">
          <button class="ft-cancel" @click="closeShipDialog">Cancelar</button>
          <button class="ft-confirm" :disabled="!trackingCode || shipping" @click="confirmShip">
            <span v-if="shipping" class="spin-sm"></span>
            <span v-else>Finalizar Envio →</span>
          </button>
        </div>
      </div>
    </v-dialog>

    <transition name="toast-slide">
      <div v-if="snackbar.show" class="pm-toast"
           :class="`pm-toast--${snackbar.color === 'error' ? 'error' : 'success'}`">
        {{ snackbar.message }}
      </div>
    </transition>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import {useAdminOrderStore} from '../stores/adminOrder'

const adminStore = useAdminOrderStore()
const shipDialog = ref(false)
const selectedOrder = ref(null)
const trackingCode = ref('')
const shipping = ref(false)
const infoDialog = ref(false)
const infoOrder = ref(null)
const snackbar = ref({show: false, message: '', color: 'success'})

const headers = [
  {title: 'ORDEM', key: 'id', sortable: true, width: '90px'},
  {title: 'CLIENTE', key: 'buyerName', sortable: false},
  {title: 'TOTAL', key: 'totalAmount', sortable: true},
  {title: 'STATUS', key: 'status', sortable: true},
  {title: 'DATA', key: 'createdAt', sortable: true},
  {title: 'AÇÕES', key: 'actions', sortable: false, align: 'end', width: '260px'}
]

onMounted(() => loadOrders())

const loadOrders = async () => {
  try {
    await adminStore.fetchAllOrders()
  } catch {
    showSnackbar('Erro ao carregar pedidos', 'error')
  }
}
const startProcessing = async (order) => {
  try {
    await adminStore.updateOrderStatus(order.id, 'PROCESSING');
    showSnackbar(`Pedido #${order.id} em produção`)
  } catch {
    showSnackbar('Erro ao atualizar status', 'error')
  }
}
const openShipDialog = (order) => {
  selectedOrder.value = order;
  trackingCode.value = '';
  shipDialog.value = true
}
const closeShipDialog = () => {
  shipDialog.value = false;
  selectedOrder.value = null
}
const confirmShip = async () => {
  try {
    shipping.value = true
    await adminStore.shipOrder(selectedOrder.value.id, trackingCode.value)
    showSnackbar(`Pedido #${selectedOrder.value.id} enviado`)
    closeShipDialog()
  } catch {
    showSnackbar('Erro ao despachar', 'error')
  } finally {
    shipping.value = false
  }
}
const openInfoDialog = (order) => {
  infoOrder.value = order;
  infoDialog.value = true
}
const copy = async (text) => {
  try {
    await navigator.clipboard.writeText(text);
    showSnackbar('Copiado!')
  } catch {
    showSnackbar('Erro ao copiar', 'error')
  }
}
const copyAll = async (order) => {
  const items = (order.items ?? []).map(i => `  - ${i.quantity}x ${i.productName} (${formatCurrency(i.totalPrice)})`).join('\n')
  const text = `📦 Pedido #${order.id}\n👤 ${order.buyerName} — ${order.buyerEmail}\n📍 ${order.shippingAddress}\n🛍️\n${items}\n💰 ${formatCurrency(order.totalAmount)}\n📅 ${formatDate(order.createdAt)}${order.trackingCode ? `\n📬 ${order.trackingCode}` : ''}`
  await copy(text);
  showSnackbar('Todos os dados copiados!')
}
const showSnackbar = (message, color = 'success') => {
  snackbar.value = {show: true, message, color}
  setTimeout(() => {
    snackbar.value.show = false
  }, 3000)
}
const formatCurrency = v => v.toLocaleString('pt-BR', {style: 'currency', currency: 'BRL'})
const formatDate = d => new Date(d).toLocaleDateString('pt-BR', {
  day: '2-digit',
  month: '2-digit',
  year: '2-digit',
  hour: '2-digit',
  minute: '2-digit'
})
const getStatusLabel = s => ({
  PENDING: 'Aguardando',
  PAYMENT_APPROVED: 'Aprovado',
  PROCESSING: 'Em Produção',
  SHIPPED: 'Enviado',
  DELIVERED: 'Entregue',
  CANCELLED: 'Cancelado'
}[s] || s)
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=DM+Mono:wght@500&display=swap');

*, *::before, *::after {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

.page-root {
  font-family: 'DM Sans', sans-serif;
  background: #F8FAFC;
  min-height: 100vh;
  padding: 28px 32px 40px;
  color: #0F172A;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* TOPBAR */
.page-topbar {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
}

.tb-breadcrumb {
  font-size: 12px;
  color: #94A3B8;
  font-family: 'DM Mono', monospace;
  margin-bottom: 4px;
}

.tb-breadcrumb .sep {
  color: #CBD5E1;
  margin: 0 4px;
}

.tb-breadcrumb .current {
  color: #64748B;
}

.tb-title {
  font-size: 22px;
  font-weight: 700;
  letter-spacing: -0.025em;
  color: #0F172A;
}

.tb-btn-sync {
  display: flex;
  align-items: center;
  gap: 8px;
  background: #fff;
  border: 1px solid #E2E8F0;
  border-radius: 10px;
  padding: 0 16px;
  height: 38px;
  font: 600 13px 'DM Sans', sans-serif;
  color: #475569;
  cursor: pointer;
  transition: all .15s;
}

.tb-btn-sync:hover {
  border-color: #CBD5E1;
  color: #0F172A;
}

/* KPI */
.kpi-strip {
  display: flex;
  align-items: stretch;
  background: #fff;
  border: 1px solid #E2E8F0;
  border-radius: 14px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, .04);
}

.kpi-tile {
  flex: 1;
  padding: 18px 22px;
  transition: background .15s;
}

.kpi-tile:hover {
  background: #F8FAFC;
}

.kpi-div {
  width: 1px;
  background: #F1F5F9;
  margin: 12px 0;
}

.kpi-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.kpi-label {
  font-size: 11px;
  font-weight: 600;
  color: #94A3B8;
  text-transform: uppercase;
  letter-spacing: .06em;
}

.kpi-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
}

.dot-amber {
  background: #F59E0B;
}

.dot-blue {
  background: #3B82F6;
}

.dot-purple {
  background: #8B5CF6;
}

.dot-green {
  background: #10B981;
}

.kpi-val {
  font-size: 26px;
  font-weight: 700;
  color: #0F172A;
  letter-spacing: -0.03em;
  font-family: 'DM Mono', monospace;
  margin-bottom: 4px;
}

.kpi-sub {
  font-size: 11px;
  color: #CBD5E1;
  font-family: 'DM Mono', monospace;
}

/* LOADER */
.loader-state {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 14px;
  padding: 60px;
  color: #64748B;
  font-size: 14px;
}

.loader-ring {
  width: 28px;
  height: 28px;
  border: 2px solid #E2E8F0;
  border-top-color: #6366F1;
  border-radius: 50%;
  animation: spin .7s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

/* TABLE CARD */
.table-card {
  background: #fff;
  border: 1px solid #E2E8F0;
  border-radius: 14px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, .03);
}

.table-topbar {
  padding: 20px 24px;
  border-bottom: 1px solid #F1F5F9;
}

.table-title {
  font-size: 15px;
  font-weight: 700;
  color: #0F172A;
  display: flex;
  align-items: center;
  gap: 10px;
}

.count-badge {
  background: #F1F5F9;
  color: #64748B;
  font-size: 12px;
  font-weight: 700;
  padding: 2px 10px;
  border-radius: 7px;
}

:deep(.v-data-table-header th) {
  background: #F8FAFC !important;
  font-size: 11px !important;
  font-weight: 700 !important;
  text-transform: uppercase;
  color: #94A3B8 !important;
  letter-spacing: .06em;
}

:deep(.v-data-table__td) {
  height: 68px !important;
  border-bottom: 1px solid #F8FAFC !important;
}

.id-tag {
  font-family: 'DM Mono', monospace;
  font-size: 12px;
  font-weight: 600;
  color: #6366F1;
}

.buyer-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}

.buyer-avatar {
  width: 36px;
  height: 36px;
  background: #F1F5F9;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  color: #475569;
  font-size: 14px;
  flex-shrink: 0;
}

.buyer-name {
  font-size: 13px;
  font-weight: 600;
  color: #0F172A;
}

.buyer-email {
  font-size: 11px;
  color: #94A3B8;
}

.amount-val {
  font-family: 'DM Mono', monospace;
  font-size: 13px;
  font-weight: 600;
  color: #0F172A;
}

.date-val {
  font-size: 12px;
  color: #64748B;
  font-family: 'DM Mono', monospace;
}

/* STATUS PILLS */
.status-pill {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 10px;
  border-radius: 100px;
  font-size: 11px;
  font-weight: 700;
}

.status-dot-sm {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: currentColor;
}

.status-pending {
  background: #FFF7ED;
  color: #9A3412;
}

.status-paymentapproved {
  background: #EFF6FF;
  color: #1E40AF;
}

.status-processing {
  background: #FAF5FF;
  color: #6B21A8;
}

.status-shipped {
  background: #F0FDF4;
  color: #166534;
}

.status-delivered {
  background: #F0FDF4;
  color: #166534;
}

.status-cancelled {
  background: #FEF2F2;
  color: #991B1B;
}

/* ROW ACTIONS */
.row-actions {
  display: flex;
  align-items: center;
  gap: 6px;
  justify-content: flex-end;
  flex-wrap: wrap;
}

.act-btn {
  display: flex;
  align-items: center;
  gap: 5px;
  border-radius: 8px;
  border: 1px solid;
  padding: 5px 11px;
  font: 600 11px 'DM Sans', sans-serif;
  cursor: pointer;
  transition: all .15s;
  height: 30px;
}

.act-info {
  background: #EEF2FF;
  border-color: #C7D2FE;
  color: #4338CA;
}

.act-info:hover {
  background: #E0E7FF;
}

.act-process {
  background: #0F172A;
  border-color: #0F172A;
  color: #fff;
}

.act-process:hover {
  background: #1E293B;
}

.act-ship {
  background: #fff;
  border-color: #E2E8F0;
  color: #475569;
}

.act-ship:hover {
  border-color: #CBD5E1;
  color: #0F172A;
}

.tracking-tag {
  display: flex;
  align-items: center;
  gap: 4px;
  font: 500 11px 'DM Mono', monospace;
  color: #94A3B8;
}

/* MODAIS */
.modal-card {
  background: #fff;
  border-radius: 20px;
  box-shadow: 0 20px 48px rgba(0, 0, 0, .15);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  font-family: 'DM Sans', sans-serif;
}

.modal-hd {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 24px 28px;
  border-bottom: 1px solid #F1F5F9;
}

.modal-pre {
  font-size: 11px;
  color: #94A3B8;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: .08em;
  margin-bottom: 3px;
  font-family: 'DM Mono', monospace;
}

.modal-title {
  font-size: 18px;
  font-weight: 700;
  color: #0F172A;
  letter-spacing: -0.02em;
}

.modal-close-btn {
  width: 32px;
  height: 32px;
  border: 1px solid #E2E8F0;
  border-radius: 8px;
  background: transparent;
  color: #94A3B8;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all .15s;
}

.modal-close-btn:hover {
  background: #F8FAFC;
  color: #0F172A;
}

.modal-body {
  padding: 0;
  max-height: 60vh;
  overflow-y: auto;
}

.info-block {
  padding: 16px 28px;
}

.block-label {
  font-size: 10px;
  font-weight: 700;
  color: #94A3B8;
  text-transform: uppercase;
  letter-spacing: .12em;
  margin-bottom: 10px;
}

.block-divider {
  border-top: 1px solid #F8FAFC;
  margin: 0;
}

.two-col {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 14px;
}

.info-field {
  display: flex;
  flex-direction: column;
  gap: 3px;
}

.field-key {
  font-size: 11px;
  color: #94A3B8;
  font-weight: 600;
  text-transform: uppercase;
}

.field-row {
  display: flex;
  align-items: center;
  gap: 6px;
}

.field-val {
  font-size: 13px;
  font-weight: 600;
  color: #0F172A;
}

.copy-sm {
  width: 22px;
  height: 22px;
  border: 1px solid #E2E8F0;
  border-radius: 6px;
  background: transparent;
  color: #94A3B8;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all .12s;
}

.copy-sm:hover {
  border-color: #6366F1;
  color: #6366F1;
}

.address-box {
  background: #F8FAFC;
  border: 1px solid #E2E8F0;
  border-radius: 10px;
  padding: 12px 14px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 12px;
}

.address-text {
  font-size: 13px;
  font-weight: 500;
  color: #334155;
  line-height: 1.5;
  flex: 1;
}

.copy-btn {
  background: #fff;
  border: 1px solid #E2E8F0;
  border-radius: 7px;
  padding: 4px 10px;
  font: 600 11px 'DM Sans', sans-serif;
  color: #475569;
  cursor: pointer;
  white-space: nowrap;
  transition: all .12s;
  flex-shrink: 0;
}

.copy-btn:hover {
  border-color: #6366F1;
  color: #6366F1;
}

.items-list {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.item-row {
  display: flex;
  align-items: center;
  gap: 8px;
  background: #F8FAFC;
  border-radius: 8px;
  padding: 8px 12px;
}

.item-qty {
  background: #E0E7FF;
  color: #4338CA;
  font-size: 11px;
  font-weight: 700;
  padding: 2px 7px;
  border-radius: 5px;
  flex-shrink: 0;
}

.item-name {
  flex: 1;
  font-size: 12px;
  color: #334155;
  font-weight: 500;
}

.item-price {
  font-size: 12px;
  font-weight: 700;
  color: #0F172A;
  font-family: 'DM Mono', monospace;
}

.summary-rows {
  display: flex;
  flex-direction: column;
}

.sum-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 7px 0;
  border-bottom: 1px dashed #F1F5F9;
  font-size: 13px;
  color: #64748B;
}

.sum-row:last-child {
  border: none;
}

.sum-highlight {
  font-size: 17px;
  font-weight: 700;
  color: #4338CA;
  font-family: 'DM Mono', monospace;
}

.tracking-box {
  display: flex;
  align-items: center;
  gap: 10px;
  background: #EEF2FF;
  border: 1px solid #C7D2FE;
  border-radius: 8px;
  padding: 10px 14px;
}

.tracking-code {
  flex: 1;
  font-family: 'DM Mono', monospace;
  font-size: 15px;
  font-weight: 700;
  color: #4338CA;
  letter-spacing: .04em;
}

.modal-ft {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 28px;
  border-top: 1px solid #F1F5F9;
  background: #FAFAFA;
}

.ft-copy-all {
  display: flex;
  align-items: center;
  gap: 6px;
  background: #EEF2FF;
  border: none;
  border-radius: 8px;
  padding: 8px 14px;
  font: 700 12px 'DM Sans', sans-serif;
  color: #4338CA;
  cursor: pointer;
  transition: all .15s;
}

.ft-copy-all:hover {
  background: #E0E7FF;
}

.ft-close {
  background: #0F172A;
  border: none;
  border-radius: 8px;
  padding: 8px 16px;
  font: 700 13px 'DM Sans', sans-serif;
  color: #fff;
  cursor: pointer;
  transition: all .15s;
}

.ft-close:hover {
  background: #1E293B;
}

.ft-cancel {
  background: transparent;
  border: 1px solid #E2E8F0;
  border-radius: 8px;
  padding: 8px 16px;
  font: 600 13px 'DM Sans', sans-serif;
  color: #64748B;
  cursor: pointer;
}

.ft-confirm {
  background: #3B82F6;
  border: none;
  border-radius: 8px;
  padding: 8px 20px;
  font: 700 13px 'DM Sans', sans-serif;
  color: #fff;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 6px;
  transition: all .15s;
}

.ft-confirm:hover:not(:disabled) {
  background: #2563EB;
}

.ft-confirm:disabled {
  background: #E2E8F0;
  color: #94A3B8;
  cursor: not-allowed;
}

.ship-info-box {
  background: #F0F9FF;
  border: 1px solid #BAE6FD;
  border-radius: 10px;
  padding: 12px 14px;
  display: flex;
  gap: 10px;
  align-items: flex-start;
  margin-bottom: 20px;
  font-size: 13px;
  color: #0369A1;
  line-height: 1.5;
}

.field-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding: 0 28px 24px;
}

.field-label {
  font-size: 12px;
  font-weight: 600;
  color: #475569;
}

.field-input {
  border: 1.5px solid #E2E8F0;
  border-radius: 10px;
  padding: 10px 14px;
  font: 500 14px 'DM Sans', sans-serif;
  color: #0F172A;
  outline: none;
  transition: border-color .2s, box-shadow .2s;
}

.field-input:focus {
  border-color: #6366F1;
  box-shadow: 0 0 0 3px rgba(99, 102, 241, .1);
}

.spin-sm {
  width: 14px;
  height: 14px;
  border: 2px solid rgba(255, 255, 255, .3);
  border-top-color: #fff;
  border-radius: 50%;
  animation: spin .7s linear infinite;
}

.pm-toast {
  position: fixed;
  bottom: 28px;
  right: 28px;
  z-index: 9999;
  padding: 12px 18px;
  border-radius: 10px;
  font: 600 13px 'DM Sans', sans-serif;
  box-shadow: 0 8px 24px rgba(0, 0, 0, .15);
  color: #fff;
}

.pm-toast--success {
  background: #065F46;
}

.pm-toast--error {
  background: #991B1B;
}

.toast-slide-enter-active, .toast-slide-leave-active {
  transition: all .25s;
}

.toast-slide-enter-from, .toast-slide-leave-to {
  opacity: 0;
  transform: translateY(8px);
}

@media (max-width: 768px) {
  .page-root {
    padding: 16px;
  }

  .kpi-strip {
    flex-wrap: wrap;
  }

  .kpi-div {
    display: none;
  }

  .kpi-tile {
    min-width: 45%;
  }
}
</style>
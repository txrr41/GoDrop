<template>
  <div class="page-root">

    <header class="page-topbar">
      <div class="tb-left">
        <p class="tb-breadcrumb">Admin <span class="sep">/</span> <span class="current">Ofertas</span></p>
        <h1 class="tb-title">Gerenciar Ofertas</h1>
      </div>
      <button class="tb-btn-primary" @click="openOfferModal()">
        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
          <path d="M12 5v14M5 12h14"/>
        </svg>
        Nova Oferta
      </button>
    </header>

    <section class="kpi-strip">
      <div class="kpi-tile">
        <div class="kpi-head"><span class="kpi-label">Agendadas</span><span class="kpi-dot dot-blue"></span></div>
        <p class="kpi-val">{{ offerStore.scheduledOffers.length }}</p>
        <p class="kpi-sub">Aguardando início</p>
      </div>
      <div class="kpi-div"></div>
      <div class="kpi-tile">
        <div class="kpi-head"><span class="kpi-label">Ativas Agora</span><span class="kpi-dot dot-green"></span></div>
        <p class="kpi-val">{{ offerStore.activeOffers.length }}</p>
        <p class="kpi-sub">Descontos aplicados</p>
      </div>
      <div class="kpi-div"></div>
      <div class="kpi-tile">
        <div class="kpi-head"><span class="kpi-label">Finalizadas</span><span class="kpi-dot dot-gray"></span></div>
        <p class="kpi-val">{{ offerStore.expiredOffers.length }}</p>
        <p class="kpi-sub">Encerradas</p>
      </div>
    </section>

    <section class="table-card">
      <div class="table-topbar">
        <h2 class="table-title">Ofertas <span class="count-badge">{{ filteredOffers.length }}</span></h2>
        <div class="filter-tabs">
          <button v-for="tab in tabs" :key="tab.val" class="filter-tab"
                  :class="{ active: currentTab === tab.val }"
                  @click="currentTab = tab.val">
            {{ tab.label }}
          </button>
        </div>
      </div>

      <div v-if="offerStore.loading" class="loader-state">
        <div class="loader-ring"></div>
        <span>Carregando ofertas...</span>
      </div>

      <div v-else-if="filteredOffers.length === 0" class="empty-state">
        <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="#CBD5E1" stroke-width="1.5">
          <path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 12V2h10l8.59 8.59a2 2 0 0 1 0 2.82z"/>
          <line x1="7" y1="7" x2="7.01" y2="7"/>
        </svg>
        <p class="empty-title">Nenhuma oferta encontrada</p>
        <p class="empty-sub">Crie sua primeira oferta para começar</p>
      </div>

      <div v-else class="offers-list">
        <div v-for="offer in filteredOffers" :key="offer.id" class="offer-row"
             :class="'status-' + offer.status.toLowerCase()">
          <div class="offer-left">
            <div class="offer-disc-badge" :class="offer.type === 'PERCENTAGE' ? 'badge-purple' : 'badge-blue'">
              {{ offer.type === 'PERCENTAGE' ? `-${offer.discountValue}%` : `-R$${offer.discountValue}` }}
            </div>
            <div class="offer-info">
              <div class="offer-name-row">
                <span class="offer-name">{{ offer.name }}</span>
                <span :class="['status-chip', 'chip-' + offer.status.toLowerCase()]">
                  {{ getStatusLabel(offer.status) }}
                </span>
              </div>
              <p v-if="offer.description" class="offer-desc">{{ offer.description }}</p>
              <div class="offer-meta">
                <span class="meta-item">
                  <svg width="11" height="11" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <rect x="3" y="4" width="18" height="18" rx="2"/>
                    <line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/>
                    <line x1="3" y1="10" x2="21" y2="10"/>
                  </svg>
                  {{ formatDateTime(offer.startDate) }}
                </span>
                <span class="meta-sep">→</span>
                <span class="meta-item">{{ formatDateTime(offer.endDate) }}</span>
                <span v-if="offer.stockLimit" class="meta-item meta-stock">
                  <svg width="11" height="11" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path
                        d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"/>
                  </svg>
                  {{ offer.usedStock }}/{{ offer.stockLimit }} usados
                </span>
              </div>
            </div>
          </div>

          <div class="offer-right">
            <div class="offer-products" v-if="offer.products?.length">
              <div v-for="(p, i) in offer.products.slice(0, 3)" :key="p.id" class="prod-avatar"
                   :style="{ zIndex: 10 - i }">
                <img :src="p.imagem || '/placeholder.png'" :alt="p.nome"/>
              </div>
              <span v-if="offer.products.length > 3" class="prod-more">+{{ offer.products.length - 3 }}</span>
            </div>
            <label class="toggle-wrap" :title="offer.active ? 'Desativar' : 'Ativar'">
              <input type="checkbox" v-model="offer.active" @change="handleToggle(offer.id)"/>
              <span class="toggle-track"><span class="toggle-thumb"></span></span>
            </label>
            <button class="act-btn act-edit" @click="openOfferModal(offer)">
              <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
                <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
              </svg>
            </button>
            <button class="act-btn act-delete" @click="handleDelete(offer.id)">
              <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <polyline points="3 6 5 6 21 6"/>
                <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a1 1 0 0 1 1-1h4a1 1 0 0 1 1 1v2"/>
              </svg>
            </button>
          </div>
        </div>
      </div>
    </section>

    <!-- MODAL OFERTA -->
    <v-dialog v-model="offerModal" max-width="700" persistent scrollable>
      <div class="modal-card">
        <div class="modal-hd">
          <div>
            <p class="modal-pre">{{ editingOffer ? 'Editar' : 'Nova' }} Oferta</p>
            <h2 class="modal-title">{{ editingOffer ? 'Editar Oferta' : 'Criar Nova Oferta' }}</h2>
          </div>
          <button class="modal-close-btn" @click="closeOfferModal">
            <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M18 6 6 18M6 6l12 12"/>
            </svg>
          </button>
        </div>

        <div class="modal-body">
          <div class="form-section">
            <p class="form-section-label">Informações Básicas</p>
            <div class="field-group">
              <label class="field-label">Nome da Oferta <span class="req">*</span></label>
              <input
                  v-model="offerData.name"
                  class="field-input"
                  :class="{ 'field-input--error': fieldErrors.name }"
                  placeholder="Ex: Black Friday — 20% off"
              />
              <span v-if="fieldErrors.name" class="field-error">{{ fieldErrors.name }}</span>
            </div>
            <div class="field-group">
              <label class="field-label">Descrição</label>
              <textarea v-model="offerData.description" class="field-input field-textarea" rows="2"
                        placeholder="Descrição opcional..."></textarea>
            </div>
          </div>

          <div class="form-section">
            <p class="form-section-label">Desconto</p>
            <div class="form-row">
              <div class="field-group">
                <label class="field-label">Tipo <span class="req">*</span></label>
                <select v-model="offerData.type" class="field-input field-select">
                  <option value="PERCENTAGE">Porcentagem (%)</option>
                  <option value="FIXED">Valor Fixo (R$)</option>
                </select>
              </div>
              <div class="field-group">
                <label class="field-label">Valor <span class="req">*</span></label>
                <div class="input-prefix-wrap">
                  <span class="input-prefix">{{ offerData.type === 'FIXED' ? 'R$' : '%' }}</span>
                  <input
                      v-model.number="offerData.discountValue"
                      type="number"
                      class="field-input input-with-prefix"
                      :class="{ 'field-input--error': fieldErrors.discountValue }"
                      placeholder="0"
                  />
                </div>
                <span v-if="fieldErrors.discountValue" class="field-error">{{ fieldErrors.discountValue }}</span>
              </div>
            </div>
          </div>

          <div class="form-section">
            <p class="form-section-label">Período</p>
            <div class="form-row">
              <div class="field-group">
                <label class="field-label">Início <span class="req">*</span></label>
                <input
                    v-model="offerData.startDate"
                    type="datetime-local"
                    class="field-input"
                    :class="{ 'field-input--error': fieldErrors.startDate }"
                    :min="minDateTime"
                />
                <span v-if="fieldErrors.startDate" class="field-error">{{ fieldErrors.startDate }}</span>
              </div>
              <div class="field-group">
                <label class="field-label">Fim <span class="req">*</span></label>
                <input
                    v-model="offerData.endDate"
                    type="datetime-local"
                    class="field-input"
                    :class="{ 'field-input--error': fieldErrors.endDate }"
                />
                <span v-if="fieldErrors.endDate" class="field-error">{{ fieldErrors.endDate }}</span>
              </div>
            </div>
          </div>

          <div class="form-section">
            <p class="form-section-label">Produtos</p>
            <v-autocomplete
                v-model="offerData.productIds"
                :items="products"
                item-title="nome"
                item-value="id"
                label="Selecionar Produtos"
                variant="outlined"
                multiple
                chips
                closable-chips
                density="compact"
                class="prd-autocomplete"
            />
          </div>

          <div class="form-section">
            <p class="form-section-label">Configurações</p>
            <div class="form-row">
              <div class="field-group">
                <label class="field-label">Limite de Estoque</label>
                <input v-model.number="offerData.stockLimit" type="number" class="field-input" placeholder="Ilimitado"/>
              </div>
              <div class="field-group">
                <label class="field-label">Prioridade</label>
                <input v-model.number="offerData.priority" type="number" class="field-input" placeholder="0"/>
              </div>
            </div>
          </div>
        </div>

        <div class="modal-ft">
          <button class="ft-cancel" @click="closeOfferModal">Cancelar</button>
          <button class="ft-save" :disabled="offerStore.loading" @click="saveOffer">
            <span v-if="offerStore.loading" class="spin-sm"></span>
            <span v-else>{{ editingOffer ? 'Salvar Alterações' : 'Criar Oferta' }}</span>
          </button>
        </div>
      </div>
    </v-dialog>

    <transition name="toast-slide">
      <div v-if="snackbar" class="pm-toast" :class="`pm-toast--${snackbarColor}`">{{ snackbarMessage }}</div>
    </transition>
  </div>
</template>

<script setup>
import {ref, computed, onMounted, reactive} from 'vue'
import {useOfferStore} from '../stores/offer'
import api from '../api/api'

const offerStore = useOfferStore()

const products = ref([])
const currentTab = ref('all')
const offerModal = ref(false)
const editingOffer = ref(null)
const snackbar = ref(false)
const snackbarMessage = ref('')
const snackbarColor = ref('success')

const fieldErrors = reactive({
  name: '',
  discountValue: '',
  startDate: '',
  endDate: ''
})

const minDateTime = computed(() => {
  return new Date().toISOString().slice(0, 16)
})

const tabs = [
  {val: 'all', label: 'Todas'},
  {val: 'scheduled', label: 'Agendadas'},
  {val: 'active', label: 'Ativas'},
  {val: 'expired', label: 'Finalizadas'},
]

const offerData = ref({
  name: '',
  description: '',
  type: 'PERCENTAGE',
  discountValue: 0,
  startDate: '',
  endDate: '',
  active: true,
  category: '',
  stockLimit: null,
  priority: 0,
  productIds: []
})

const filteredOffers = computed(() => {
  if (currentTab.value === 'scheduled') return offerStore.scheduledOffers
  if (currentTab.value === 'active') return offerStore.activeOffers
  if (currentTab.value === 'expired') return offerStore.expiredOffers
  return offerStore.offers
})

onMounted(async () => {
  await offerStore.fetchOffers()
  await loadProducts()
})

const loadProducts = async () => {
  try {
    const {data} = await api.get('/produtos')
    products.value = data
  } catch {
    console.error('Erro ao carregar produtos')
  }
}

const clearFieldErrors = () => {
  fieldErrors.name = ''
  fieldErrors.discountValue = ''
  fieldErrors.startDate = ''
  fieldErrors.endDate = ''
}

const validateForm = () => {
  clearFieldErrors()
  let valid = true

  if (!offerData.value.name?.trim()) {
    fieldErrors.name = 'Nome é obrigatório'
    valid = false
  }
  if (!offerData.value.discountValue || offerData.value.discountValue <= 0) {
    fieldErrors.discountValue = 'Informe um valor de desconto válido'
    valid = false
  }
  if (offerData.value.type === 'PERCENTAGE' && offerData.value.discountValue > 100) {
    fieldErrors.discountValue = 'Desconto não pode ser maior que 100%'
    valid = false
  }
  if (!offerData.value.startDate) {
    fieldErrors.startDate = 'Data de início é obrigatória'
    valid = false
  }
  if (!offerData.value.endDate) {
    fieldErrors.endDate = 'Data de fim é obrigatória'
    valid = false
  }
  if (offerData.value.startDate && offerData.value.endDate) {
    if (new Date(offerData.value.startDate) >= new Date(offerData.value.endDate)) {
      fieldErrors.endDate = 'Data de fim deve ser posterior ao início'
      valid = false
    }
  }
  return valid
}

const openOfferModal = (offer = null) => {
  clearFieldErrors()
  if (offer) {
    editingOffer.value = offer
    offerData.value = {
      name: offer.name,
      description: offer.description || '',
      type: offer.type,
      discountValue: offer.discountValue,
      startDate: formatForInput(offer.startDate),
      endDate: formatForInput(offer.endDate),
      active: offer.active,
      category: offer.category || '',
      stockLimit: offer.stockLimit,
      priority: offer.priority || 0,
      productIds: offer.productIds || []
    }
  } else {
    editingOffer.value = null
    offerData.value = {
      name: '', description: '', type: 'PERCENTAGE', discountValue: 0,
      startDate: '', endDate: '', active: true, category: '',
      stockLimit: null, priority: 0, productIds: []
    }
  }
  offerModal.value = true
}

const closeOfferModal = () => {
  offerModal.value = false
  editingOffer.value = null
  clearFieldErrors()
}

const saveOffer = async () => {
  if (!validateForm()) return

  try {
    const payload = {
      ...offerData.value,
      startDate: new Date(offerData.value.startDate).toISOString(),
      endDate: new Date(offerData.value.endDate).toISOString()
    }

    if (editingOffer.value) {
      await offerStore.updateOffer(editingOffer.value.id, payload)
      showSnackbar('Oferta atualizada!')
    } else {
      await offerStore.createOffer(payload)
      showSnackbar('Oferta criada!')
    }
    closeOfferModal()
  } catch (error) {
    const data = error.response?.data

    if (data?.errors) {
      Object.entries(data.errors).forEach(([field, msg]) => {
        if (field in fieldErrors) fieldErrors[field] = msg
      })
      showSnackbar('Corrija os campos destacados', 'error')
    } else if (data?.message) {
      showSnackbar(data.message, 'error')
    } else {
      showSnackbar('Erro ao salvar oferta. Tente novamente.', 'error')
    }
  }
}

const handleToggle = async (offerId) => {
  try {
    await offerStore.toggleOffer(offerId)
    showSnackbar('Status atualizado!')
  } catch {
    showSnackbar('Erro ao atualizar', 'error')
    await offerStore.fetchOffers()
  }
}

const handleDelete = async (offerId) => {
  if (!confirm('Excluir esta oferta?')) return
  try {
    await offerStore.deleteOffer(offerId)
    showSnackbar('Oferta excluída.')
  } catch {
    showSnackbar('Erro ao excluir', 'error')
  }
}

const getStatusLabel = s => ({
  SCHEDULED: 'Agendada', ACTIVE: 'Ativa', EXPIRED: 'Finalizada',
  PAUSED: 'Pausada', CANCELLED: 'Cancelada'
}[s] || s)

const formatDateTime = d => new Date(d).toLocaleString('pt-BR', {
  day: '2-digit', month: '2-digit', year: '2-digit',
  hour: '2-digit', minute: '2-digit'
})

const formatForInput = d => new Date(d).toISOString().slice(0, 16)

const showSnackbar = (message, color = 'success') => {
  snackbarMessage.value = message
  snackbarColor.value = color
  snackbar.value = true
  setTimeout(() => {
    snackbar.value = false
  }, 3000)
}
</script>

<style scoped>
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

.tb-btn-primary {
  display: flex;
  align-items: center;
  gap: 7px;
  background: #0F172A;
  border: none;
  border-radius: 10px;
  padding: 0 18px;
  height: 38px;
  font: 600 13px 'DM Sans', sans-serif;
  color: #fff;
  cursor: pointer;
  transition: background .15s;
}

.tb-btn-primary:hover {
  background: #1E293B;
}

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

.dot-blue {
  background: #3B82F6;
}

.dot-green {
  background: #10B981;
}

.dot-gray {
  background: #CBD5E1;
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

.table-card {
  background: #fff;
  border: 1px solid #E2E8F0;
  border-radius: 14px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, .03);
}

.table-topbar {
  padding: 16px 20px;
  border-bottom: 1px solid #F1F5F9;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
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

.filter-tabs {
  display: flex;
  gap: 2px;
  background: #F8FAFC;
  border: 1px solid #E2E8F0;
  border-radius: 9px;
  padding: 3px;
}

.filter-tab {
  border: none;
  background: transparent;
  padding: 5px 12px;
  border-radius: 6px;
  font: 500 12px 'DM Sans', sans-serif;
  color: #94A3B8;
  cursor: pointer;
  transition: all .15s;
}

.filter-tab.active {
  background: #fff;
  color: #0F172A;
  box-shadow: 0 1px 3px rgba(0, 0, 0, .08);
  font-weight: 600;
}

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

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  padding: 64px;
  text-align: center;
}

.empty-title {
  font-size: 16px;
  font-weight: 600;
  color: #475569;
}

.empty-sub {
  font-size: 13px;
  color: #94A3B8;
}

.offers-list {
  display: flex;
  flex-direction: column;
}

.offer-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding: 16px 20px;
  border-bottom: 1px solid #F8FAFC;
  transition: background .15s;
}

.offer-row:last-child {
  border-bottom: none;
}

.offer-row:hover {
  background: #FAFAFA;
}

.offer-row.status-active {
  border-left: 3px solid #10B981;
}

.offer-row.status-scheduled {
  border-left: 3px solid #3B82F6;
}

.offer-row.status-expired {
  border-left: 3px solid #E2E8F0;
  opacity: .7;
}

.offer-left {
  display: flex;
  align-items: center;
  gap: 14px;
  flex: 1;
  min-width: 0;
}

.offer-disc-badge {
  font-size: 13px;
  font-weight: 800;
  padding: 6px 12px;
  border-radius: 9px;
  white-space: nowrap;
  flex-shrink: 0;
  font-family: 'DM Mono', monospace;
}

.badge-purple {
  background: #F5F3FF;
  color: #6D28D9;
}

.badge-blue {
  background: #EFF6FF;
  color: #1D4ED8;
}

.offer-info {
  flex: 1;
  min-width: 0;
}

.offer-name-row {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 3px;
  flex-wrap: wrap;
}

.offer-name {
  font-size: 14px;
  font-weight: 600;
  color: #0F172A;
}

.status-chip {
  font-size: 10px;
  font-weight: 700;
  padding: 2px 8px;
  border-radius: 100px;
  text-transform: uppercase;
  letter-spacing: .05em;
}

.chip-active {
  background: #ECFDF5;
  color: #059669;
}

.chip-scheduled {
  background: #EFF6FF;
  color: #1E40AF;
}

.chip-expired {
  background: #F1F5F9;
  color: #64748B;
}

.chip-paused {
  background: #FFFBEB;
  color: #B45309;
}

.chip-cancelled {
  background: #FEF2F2;
  color: #9F1239;
}

.offer-desc {
  font-size: 12px;
  color: #64748B;
  margin-bottom: 5px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 400px;
}

.offer-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 11px;
  color: #94A3B8;
  font-family: 'DM Mono', monospace;
}

.meta-sep {
  color: #CBD5E1;
  font-size: 11px;
}

.meta-stock {
  color: #64748B;
}

.offer-right {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-shrink: 0;
}

.offer-products {
  display: flex;
  align-items: center;
}

.prod-avatar {
  width: 28px;
  height: 28px;
  border-radius: 8px;
  border: 2px solid #fff;
  overflow: hidden;
  position: relative;
  margin-left: -6px;
  background: #F1F5F9;
}

.prod-avatar:first-child {
  margin-left: 0;
}

.prod-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.prod-more {
  font-size: 11px;
  color: #94A3B8;
  font-weight: 600;
  margin-left: 4px;
}

.toggle-wrap {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.toggle-wrap input {
  display: none;
}

.toggle-track {
  width: 36px;
  height: 20px;
  background: #E2E8F0;
  border-radius: 10px;
  position: relative;
  transition: background .2s;
}

.toggle-wrap input:checked + .toggle-track {
  background: #10B981;
}

.toggle-thumb {
  position: absolute;
  top: 2px;
  left: 2px;
  width: 16px;
  height: 16px;
  background: #fff;
  border-radius: 50%;
  transition: left .2s;
  box-shadow: 0 1px 3px rgba(0, 0, 0, .2);
}

.toggle-wrap input:checked + .toggle-track .toggle-thumb {
  left: 18px;
}

.act-btn {
  width: 30px;
  height: 30px;
  border-radius: 8px;
  border: 1px solid;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all .15s;
}

.act-edit {
  background: #F8FAFC;
  border-color: #E2E8F0;
  color: #475569;
}

.act-edit:hover {
  border-color: #CBD5E1;
  color: #0F172A;
}

.act-delete {
  background: #FEF2F2;
  border-color: #FECACA;
  color: #DC2626;
}

.act-delete:hover {
  background: #FEE2E2;
}

.modal-card {
  background: #fff;
  border-radius: 18px;
  box-shadow: 0 20px 48px rgba(0, 0, 0, .12);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  font-family: 'DM Sans', sans-serif;
}

.modal-hd {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 22px 26px;
  border-bottom: 1px solid #F1F5F9;
}

.modal-pre {
  font-size: 10px;
  color: #94A3B8;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: .1em;
  margin-bottom: 3px;
}

.modal-title {
  font-size: 17px;
  font-weight: 700;
  color: #0F172A;
  letter-spacing: -0.02em;
}

.modal-close-btn {
  width: 30px;
  height: 30px;
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
  padding: 22px 26px;
  max-height: 60vh;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 22px;
}

.form-section {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.form-section-label {
  font-size: 11px;
  font-weight: 700;
  color: #94A3B8;
  text-transform: uppercase;
  letter-spacing: .1em;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.field-group {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.field-label {
  font-size: 12px;
  font-weight: 600;
  color: #475569;
}

.req {
  color: #EF4444;
}

.field-input {
  border: 1.5px solid #E2E8F0;
  border-radius: 9px;
  padding: 9px 12px;
  font: 500 13px 'DM Sans', sans-serif;
  color: #0F172A;
  background: #fff;
  outline: none;
  transition: border-color .2s, box-shadow .2s;
  width: 100%;
}

.field-input:focus {
  border-color: #6366F1;
  box-shadow: 0 0 0 3px rgba(99, 102, 241, .1);
}

.field-input--error {
  border-color: #EF4444 !important;
}

.field-input--error:focus {
  box-shadow: 0 0 0 3px rgba(239, 68, 68, .1);
}

.field-error {
  font-size: 11px;
  font-weight: 600;
  color: #EF4444;
  margin-top: 2px;
}

.field-select {
  appearance: none;
  cursor: pointer;
}

.field-textarea {
  resize: vertical;
  min-height: 60px;
  font-family: inherit;
}

.input-prefix-wrap {
  position: relative;
}

.input-prefix {
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 12px;
  font-weight: 600;
  color: #94A3B8;
  pointer-events: none;
}

.input-with-prefix {
  padding-left: 30px;
}

:deep(.prd-autocomplete .v-field) {
  border-radius: 9px !important;
  font-size: 13px !important;
}

.modal-ft {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding: 16px 26px;
  border-top: 1px solid #F1F5F9;
  background: #FAFAFA;
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

.ft-save {
  background: #0F172A;
  border: none;
  border-radius: 8px;
  padding: 8px 20px;
  font: 700 13px 'DM Sans', sans-serif;
  color: #fff;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 6px;
  transition: background .15s;
}

.ft-save:hover:not(:disabled) {
  background: #1E293B;
}

.ft-save:disabled {
  background: #E2E8F0;
  color: #94A3B8;
  cursor: not-allowed;
}

.spin-sm {
  width: 14px;
  height: 14px;
  border: 2px solid rgba(255, 255, 255, .3);
  border-top-color: #fff;
  border-radius: 50%;
  animation: spin .7s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
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

  .offer-row {
    flex-direction: column;
    align-items: flex-start;
  }

  .offer-right {
    width: 100%;
  }

  .form-row {
    grid-template-columns: 1fr;
  }

  .kpi-strip {
    flex-wrap: wrap;
  }

  .kpi-div {
    display: none;
  }
}
</style>
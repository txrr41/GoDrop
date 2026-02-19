<template>
  <div class="offers-page">
    <header class="page-header">
      <div class="header-content">
        <div class="title-section">
          <div class="title-icon-wrapper">
            <v-icon color="deep-purple-accent-3" size="32">mdi-tag-heart</v-icon>
          </div>
          <div class="title-text-group">
            <h1>Gerenciar Ofertas</h1>
            <p>Configure ofertas agendadas e descontos automáticos</p>
          </div>
        </div>
        <v-btn
            color="deep-purple-accent-3"
            elevation="0"
            class="action-btn"
            @click="openOfferModal()"
        >
          <v-icon start>mdi-plus</v-icon>
          Nova Oferta
        </v-btn>
      </div>
    </header>

    <!-- Stats Cards -->
    <div class="stats-grid">
      <div class="stat-card scheduled">
        <div class="stat-icon">
          <v-icon>mdi-clock-outline</v-icon>
        </div>
        <div class="stat-info">
          <span class="stat-label">Agendadas</span>
          <span class="stat-value">{{ scheduledOffers.length }}</span>
        </div>
      </div>

      <div class="stat-card active">
        <div class="stat-icon">
          <v-icon>mdi-fire</v-icon>
        </div>
        <div class="stat-info">
          <span class="stat-label">Ativas Agora</span>
          <span class="stat-value">{{ activeOffers.length }}</span>
        </div>
      </div>

      <div class="stat-card expired">
        <div class="stat-icon">
          <v-icon>mdi-history</v-icon>
        </div>
        <div class="stat-info">
          <span class="stat-label">Finalizadas</span>
          <span class="stat-value">{{ expiredOffers.length }}</span>
        </div>
      </div>
    </div>

    <!-- Offers List -->
    <v-card class="offers-card" elevation="0">
      <v-tabs v-model="currentTab" class="offers-tabs">
        <v-tab value="all">Todas</v-tab>
        <v-tab value="scheduled">Agendadas</v-tab>
        <v-tab value="active">Ativas</v-tab>
        <v-tab value="expired">Finalizadas</v-tab>
      </v-tabs>

      <div v-if="loading" class="loading-state">
        <v-progress-circular indeterminate color="deep-purple-accent-3" size="64" />
        <p>Carregando ofertas...</p>
      </div>

      <div v-else-if="filteredOffers.length === 0" class="empty-state">
        <v-icon size="80" color="grey-lighten-2">mdi-tag-off-outline</v-icon>
        <h3>Nenhuma oferta encontrada</h3>
        <p>Crie sua primeira oferta para começar</p>
      </div>

      <div v-else class="offers-list">
        <div
            v-for="offer in filteredOffers"
            :key="offer.id"
            class="offer-card"
            :class="getOfferCardClass(offer)"
        >
          <div class="offer-header">
            <div class="offer-title-group">
              <h3>{{ offer.name }}</h3>
              <div class="offer-badges">
                <v-chip
                    size="small"
                    :color="getStatusColor(offer.status)"
                    variant="flat"
                >
                  {{ getStatusLabel(offer.status) }}
                </v-chip>
                <v-chip
                    v-if="offer.type === 'PERCENTAGE'"
                    size="small"
                    color="deep-purple"
                    variant="tonal"
                >
                  -{{ offer.discountValue }}%
                </v-chip>
                <v-chip
                    v-else
                    size="small"
                    color="deep-purple"
                    variant="tonal"
                >
                  -R$ {{ offer.discountValue }}
                </v-chip>
              </div>
            </div>

            <div class="offer-actions">
              <v-switch
                  v-model="offer.active"
                  color="deep-purple-accent-3"
                  hide-details
                  density="compact"
                  @change="toggleOffer(offer.id)"
              />
              <v-btn
                  icon
                  variant="text"
                  size="small"
                  @click="openOfferModal(offer)"
              >
                <v-icon>mdi-pencil</v-icon>
              </v-btn>
              <v-btn
                  icon
                  variant="text"
                  size="small"
                  color="error"
                  @click="deleteOffer(offer.id)"
              >
                <v-icon>mdi-delete</v-icon>
              </v-btn>
            </div>
          </div>

          <p v-if="offer.description" class="offer-description">
            {{ offer.description }}
          </p>

          <div class="offer-details">
            <div class="detail-item">
              <v-icon size="16">mdi-calendar-start</v-icon>
              <span>{{ formatDateTime(offer.startDate) }}</span>
            </div>
            <div class="detail-item">
              <v-icon size="16">mdi-calendar-end</v-icon>
              <span>{{ formatDateTime(offer.endDate) }}</span>
            </div>
            <div v-if="offer.stockLimit" class="detail-item">
              <v-icon size="16">mdi-package-variant</v-icon>
              <span>{{ offer.usedStock }} / {{ offer.stockLimit }} usados</span>
            </div>
          </div>

          <div v-if="offer.products && offer.products.length > 0" class="offer-products">
            <span class="products-label">Produtos:</span>
            <div class="products-avatars">
              <v-avatar
                  v-for="(product, index) in offer.products.slice(0, 3)"
                  :key="product.id"
                  size="32"
                  :style="{ marginLeft: index > 0 ? '-8px' : '0' }"
              >
                <v-img :src="product.imagem || '/placeholder.png'" />
              </v-avatar>
              <span v-if="offer.products.length > 3" class="products-more">
                +{{ offer.products.length - 3 }}
              </span>
            </div>
          </div>

          <v-progress-linear
              v-if="offer.isCurrentlyActive"
              :model-value="getOfferProgress(offer)"
              color="deep-purple-accent-3"
              height="4"
              rounded
              class="offer-progress"
          />
        </div>
      </div>
    </v-card>

    <!-- Offer Modal -->
    <v-dialog v-model="offerModal" max-width="800px" persistent scrollable>
      <v-card class="offer-modal">
        <v-card-title class="modal-header">
          <span>{{ editingOffer ? 'Editar Oferta' : 'Nova Oferta' }}</span>
          <v-btn icon variant="text" @click="closeOfferModal">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-card-title>

        <v-card-text class="pa-6">
          <v-form ref="offerForm">
            <!-- Nome e Descrição -->
            <div class="form-section">
              <h3 class="section-title">Informações Básicas</h3>
              <v-text-field
                  v-model="offerData.name"
                  label="Nome da Oferta *"
                  variant="outlined"
                  :rules="[rules.required]"
              />
              <v-textarea
                  v-model="offerData.description"
                  label="Descrição"
                  variant="outlined"
                  rows="3"
              />
            </div>

            <!-- Tipo de Desconto -->
            <div class="form-section">
              <h3 class="section-title">Desconto</h3>
              <v-row>
                <v-col cols="6">
                  <v-select
                      v-model="offerData.type"
                      :items="discountTypes"
                      label="Tipo de Desconto *"
                      variant="outlined"
                      :rules="[rules.required]"
                  />
                </v-col>
                <v-col cols="6">
                  <v-text-field
                      v-model.number="offerData.discountValue"
                      label="Valor do Desconto *"
                      type="number"
                      variant="outlined"
                      :prefix="offerData.type === 'FIXED' ? 'R$' : ''"
                      :suffix="offerData.type === 'PERCENTAGE' ? '%' : ''"
                      :rules="[rules.required, rules.positive]"
                  />
                </v-col>
              </v-row>
            </div>

            <!-- Data e Hora -->
            <div class="form-section">
              <h3 class="section-title">Período</h3>
              <v-row>
                <v-col cols="6">
                  <v-text-field
                      v-model="offerData.startDate"
                      label="Data/Hora de Início *"
                      type="datetime-local"
                      variant="outlined"
                      :rules="[rules.required]"
                  />
                </v-col>
                <v-col cols="6">
                  <v-text-field
                      v-model="offerData.endDate"
                      label="Data/Hora de Fim *"
                      type="datetime-local"
                      variant="outlined"
                      :rules="[rules.required]"
                  />
                </v-col>
              </v-row>
            </div>

            <!-- Produtos -->
            <div class="form-section">
              <h3 class="section-title">Produtos</h3>
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
              >
                <template #chip="{ item }">
                  <v-chip closable>
                    {{ item.raw.nome }}
                  </v-chip>
                </template>
              </v-autocomplete>
            </div>

            <!-- Configurações Avançadas -->
            <div class="form-section">
              <h3 class="section-title">Configurações Avançadas</h3>
              <v-row>
                <v-col cols="6">
                  <v-text-field
                      v-model.number="offerData.stockLimit"
                      label="Limite de Estoque"
                      type="number"
                      variant="outlined"
                      hint="Deixe vazio para ilimitado"
                      persistent-hint
                  />
                </v-col>
                <v-col cols="6">
                  <v-text-field
                      v-model.number="offerData.priority"
                      label="Prioridade"
                      type="number"
                      variant="outlined"
                      hint="Maior = mais prioritário"
                      persistent-hint
                  />
                </v-col>
              </v-row>
            </div>
          </v-form>
        </v-card-text>

        <v-card-actions class="pa-6 pt-0">
          <v-spacer />
          <v-btn variant="text" @click="closeOfferModal">
            Cancelar
          </v-btn>
          <v-btn
              color="deep-purple-accent-3"
              variant="flat"
              @click="saveOffer"
              :loading="saving"
          >
            {{ editingOffer ? 'Salvar Alterações' : 'Criar Oferta' }}
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-snackbar v-model="snackbar" :color="snackbarColor" timeout="3000">
      {{ snackbarMessage }}
    </v-snackbar>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import api from '../api/api'

const loading = ref(false)
const saving = ref(false)
const offers = ref([])
const products = ref([])
const currentTab = ref('all')
const offerModal = ref(false)
const editingOffer = ref(null)
const snackbar = ref(false)
const snackbarMessage = ref('')
const snackbarColor = ref('success')

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

const discountTypes = [
  { title: 'Porcentagem (%)', value: 'PERCENTAGE' },
  { title: 'Valor Fixo (R$)', value: 'FIXED' }
]

const rules = {
  required: v => !!v || 'Campo obrigatório',
  positive: v => v > 0 || 'Valor deve ser positivo'
}

const scheduledOffers = computed(() =>
    offers.value.filter(o => o.status === 'SCHEDULED')
)

const activeOffers = computed(() =>
    offers.value.filter(o => o.status === 'ACTIVE')
)

const expiredOffers = computed(() =>
    offers.value.filter(o => o.status === 'EXPIRED')
)

const filteredOffers = computed(() => {
  if (currentTab.value === 'all') return offers.value
  if (currentTab.value === 'scheduled') return scheduledOffers.value
  if (currentTab.value === 'active') return activeOffers.value
  if (currentTab.value === 'expired') return expiredOffers.value
  return offers.value
})

onMounted(async () => {
  await loadOffers()
  await loadProducts()
})

const loadOffers = async () => {
  try {
    loading.value = true
    const { data } = await api.get('/api/offers')
    offers.value = data
  } catch (error) {
    showSnackbar('Erro ao carregar ofertas', 'error')
  } finally {
    loading.value = false
  }
}

const loadProducts = async () => {
  try {
    const { data } = await api.get('/produtos')
    products.value = data
  } catch (error) {
    console.error('Erro ao carregar produtos:', error)
  }
}

const openOfferModal = (offer = null) => {
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
    resetOfferData()
  }
  offerModal.value = true
}

const closeOfferModal = () => {
  offerModal.value = false
  editingOffer.value = null
  resetOfferData()
}

const resetOfferData = () => {
  offerData.value = {
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
  }
}

const saveOffer = async () => {
  try {
    saving.value = true

    const payload = {
      ...offerData.value,
      startDate: new Date(offerData.value.startDate).toISOString(),
      endDate: new Date(offerData.value.endDate).toISOString()
    }

    if (editingOffer.value) {
      await api.put(`/api/offers/${editingOffer.value.id}`, payload)
      showSnackbar('Oferta atualizada com sucesso!')
    } else {
      await api.post('/api/offers', payload)
      showSnackbar('Oferta criada com sucesso!')
    }

    await loadOffers()
    closeOfferModal()
  } catch (error) {
    showSnackbar('Erro ao salvar oferta', 'error')
  } finally {
    saving.value = false
  }
}

const toggleOffer = async (offerId) => {
  try {
    await api.patch(`/api/offers/${offerId}/toggle`)
    showSnackbar('Status atualizado!')
  } catch (error) {
    showSnackbar('Erro ao atualizar status', 'error')
    await loadOffers()
  }
}

const deleteOffer = async (offerId) => {
  if (!confirm('Tem certeza que deseja excluir esta oferta?')) return

  try {
    await api.delete(`/api/offers/${offerId}`)
    showSnackbar('Oferta excluída com sucesso!')
    await loadOffers()
  } catch (error) {
    showSnackbar('Erro ao excluir oferta', 'error')
  }
}

const getStatusColor = (status) => {
  const colors = {
    SCHEDULED: 'blue',
    ACTIVE: 'success',
    EXPIRED: 'grey',
    PAUSED: 'warning',
    CANCELLED: 'error'
  }
  return colors[status] || 'grey'
}

const getStatusLabel = (status) => {
  const labels = {
    SCHEDULED: 'Agendada',
    ACTIVE: 'Ativa',
    EXPIRED: 'Finalizada',
    PAUSED: 'Pausada',
    CANCELLED: 'Cancelada'
  }
  return labels[status] || status
}

const getOfferCardClass = (offer) => {
  return `status-${offer.status.toLowerCase()}`
}

const getOfferProgress = (offer) => {
  const now = new Date()
  const start = new Date(offer.startDate)
  const end = new Date(offer.endDate)
  const total = end - start
  const elapsed = now - start
  return Math.min(100, Math.max(0, (elapsed / total) * 100))
}

const formatDateTime = (dateString) => {
  return new Date(dateString).toLocaleString('pt-BR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const formatForInput = (dateString) => {
  const date = new Date(dateString)
  return date.toISOString().slice(0, 16)
}

const showSnackbar = (message, color = 'success') => {
  snackbarMessage.value = message
  snackbarColor.value = color
  snackbar.value = true
}
</script>

<style scoped>
.offers-page {
  padding: 40px;
  background-color: #fafafa;
  min-height: 100vh;
}

.page-header {
  margin-bottom: 32px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title-section {
  display: flex;
  align-items: center;
  gap: 20px;
}

.title-icon-wrapper {
  width: 64px;
  height: 64px;
  background: #f5f3ff;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid #e9d5ff;
}

.title-text-group h1 {
  font-size: 28px;
  font-weight: 800;
  margin: 0;
}

.title-text-group p {
  font-size: 14px;
  color: #64748b;
  margin: 4px 0 0 0;
}

.action-btn {
  text-transform: none;
  font-weight: 700;
  height: 48px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-bottom: 32px;
}

.stat-card {
  background: white;
  padding: 24px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  border: 1px solid #f1f5f9;
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-card.scheduled .stat-icon {
  background: #dbeafe;
  color: #3b82f6;
}

.stat-card.active .stat-icon {
  background: #dcfce7;
  color: #22c55e;
}

.stat-card.expired .stat-icon {
  background: #f3f4f6;
  color: #9ca3af;
}

.stat-info {
  display: flex;
  flex-direction: column;
}

.stat-label {
  font-size: 13px;
  color: #64748b;
  font-weight: 600;
}

.stat-value {
  font-size: 32px;
  font-weight: 800;
  color: #0f172a;
}

.offers-card {
  border-radius: 24px !important;
  border: 1px solid #f1f5f9;
}

.offers-tabs {
  border-bottom: 1px solid #f1f5f9;
}

.loading-state,
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
  text-align: center;
}

.empty-state h3 {
  margin-top: 16px;
  font-size: 20px;
  font-weight: 700;
}

.empty-state p {
  color: #64748b;
  margin-top: 8px;
}

.offers-list {
  padding: 24px;
  display: grid;
  gap: 16px;
}

.offer-card {
  background: white;
  border: 1px solid #f1f5f9;
  border-radius: 16px;
  padding: 24px;
  transition: all 0.2s;
}

.offer-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.offer-card.status-active {
  border-left: 4px solid #22c55e;
}

.offer-card.status-scheduled {
  border-left: 4px solid #3b82f6;
}

.offer-card.status-expired {
  opacity: 0.6;
}

.offer-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.offer-title-group h3 {
  font-size: 18px;
  font-weight: 700;
  margin: 0 0 8px 0;
}

.offer-badges {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.offer-actions {
  display: flex;
  gap: 8px;
  align-items: center;
}

.offer-description {
  color: #64748b;
  font-size: 14px;
  margin-bottom: 16px;
}

.offer-details {
  display: flex;
  gap: 24px;
  flex-wrap: wrap;
  margin-bottom: 16px;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: #64748b;
}

.offer-products {
  display: flex;
  align-items: center;
  gap: 12px;
  padding-top: 16px;
  border-top: 1px solid #f1f5f9;
}

.products-label {
  font-size: 13px;
  font-weight: 600;
  color: #64748b;
}

.products-avatars {
  display: flex;
  align-items: center;
}

.products-more {
  margin-left: 8px;
  font-size: 12px;
  font-weight: 600;
  color: #64748b;
}

.offer-progress {
  margin-top: 16px;
}

.offer-modal .modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px;
  border-bottom: 1px solid #f1f5f9;
}

.form-section {
  margin-bottom: 32px;
}

.section-title {
  font-size: 14px;
  font-weight: 700;
  text-transform: uppercase;
  color: #64748b;
  margin-bottom: 16px;
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }

  .header-content {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }
}
</style>
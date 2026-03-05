<template>
  <div class="dash-root">

    <div class="store-banner"
         :style="`background: linear-gradient(135deg, ${store.primaryColor}E6, ${store.secondaryColor}E6)`">
      <div class="banner-noise"></div>
      <div class="banner-inner">
        <div class="banner-left">
          <div class="store-avatar"
               :style="`background: rgba(255,255,255,0.15); border: 2px solid rgba(255,255,255,0.3)`">
            <span>{{ store.storeName?.charAt(0) }}</span>
          </div>
          <div class="store-info">
            <div class="store-status-row">
              <div class="status-pill" :class="store.active ? 'online' : 'offline'">
                <span class="status-dot"></span>
                {{ store.active ? 'Online' : 'Offline' }}
              </div>
              <span class="store-level">{{ dropperProfile?.level }}</span>
            </div>
            <h1 class="store-name">{{ store.storeName }}</h1>
            <p class="store-slogan">{{ store.slogan }}</p>
          </div>
        </div>
        <div class="banner-right">
          <a :href="`/loja/${store.slug}`" target="_blank" class="banner-btn primary">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
              <path d="M18 13v6a2 2 0 01-2 2H5a2 2 0 01-2-2V8a2 2 0 012-2h6"/>
              <polyline points="15 3 21 3 21 9"/>
              <line x1="10" y1="14" x2="21" y2="3"/>
            </svg>
            Visitar loja
          </a>
          <button class="banner-btn secondary" @click="copyStoreLink">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
              <rect x="9" y="9" width="13" height="13" rx="2"/>
              <path d="M5 15H4a2 2 0 01-2-2V4a2 2 0 012-2h9a2 2 0 012 2v1"/>
            </svg>
            {{ copied ? 'Copiado!' : 'Copiar link' }}
          </button>
        </div>
      </div>

      <div class="banner-footer">
        <div class="slug-row">
          <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M10 13a5 5 0 007.54.54l3-3a5 5 0 00-7.07-7.07l-1.72 1.71"/>
            <path d="M14 11a5 5 0 00-7.54-.54l-3 3a5 5 0 007.07 7.07l1.71-1.71"/>
          </svg>
          godrop.com.br/loja/<strong>{{ store.slug }}</strong>
        </div>
        <label class="toggle-wrap">
          <span class="toggle-label">{{ store.active ? 'Loja online' : 'Loja offline' }}</span>
          <div class="toggle" :class="{ on: store.active }" @click="toggleStore">
            <div class="toggle-thumb"></div>
          </div>
        </label>
      </div>
    </div>

    <div class="stats-row">
      <div class="stat-card">
        <div class="sc-icon blue">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path
                d="M21 16V8a2 2 0 00-1-1.73l-7-4a2 2 0 00-2 0l-7 4A2 2 0 003 8v8a2 2 0 001 1.73l7 4a2 2 0 002 0l7-4A2 2 0 0021 16z"/>
          </svg>
        </div>
        <div class="sc-data">
          <span class="sc-num">{{ store.products?.length ?? 0 }}</span>
          <span class="sc-label">Produtos ativos</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="sc-icon green">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="12" y1="1" x2="12" y2="23"/>
            <path d="M17 5H9.5a3.5 3.5 0 000 7h5a3.5 3.5 0 010 7H6"/>
          </svg>
        </div>
        <div class="sc-data">
          <span class="sc-num">R$ {{ formatMoney(dropperProfile?.totalSales) }}</span>
          <span class="sc-label">Total em vendas</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="sc-icon amber">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polygon
                points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/>
          </svg>
        </div>
        <div class="sc-data">
          <span class="sc-num">{{ dropperProfile?.level }}</span>
          <span class="sc-label">Nível atual</span>
        </div>
      </div>
      <!-- Card de status Stripe -->
      <div class="stat-card" style="cursor:pointer" @click="tab = 'payments'">
        <div class="sc-icon" :class="stripeIconClass">
          <svg width="20" height="20" viewBox="0 0 48 48" fill="none">
            <path
                d="M22.5 18.5c0-1.1.9-1.5 2.3-1.5 2.1 0 4.7.6 6.7 1.7v-6.3c-2.2-.9-4.5-1.4-6.7-1.4-5.5 0-9.2 2.9-9.2 7.7 0 7.5 10.3 6.3 10.3 9.5 0 1.3-1.1 1.7-2.6 1.7-2.3 0-5.2-.9-7.5-2.2v6.4c2.5 1.1 5.1 1.6 7.5 1.6 5.7 0 9.6-2.8 9.6-7.7-.1-8.1-10.4-6.6-10.4-9.5z"
                fill="currentColor"/>
          </svg>
        </div>
        <div class="sc-data">
          <span class="sc-num" style="font-size:14px">{{ stripeStatusLabel }}</span>
          <span class="sc-label">Conta Stripe</span>
        </div>
      </div>
    </div>

    <div class="tabs-nav">
      <button class="tab-btn" :class="{ active: tab === 'products' }" @click="tab = 'products'">
        <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path
              d="M21 16V8a2 2 0 00-1-1.73l-7-4a2 2 0 00-2 0l-7 4A2 2 0 003 8v8a2 2 0 001 1.73l7 4a2 2 0 002 0l7-4A2 2 0 0021 16z"/>
        </svg>
        Produtos
      </button>
      <button class="tab-btn" :class="{ active: tab === 'appearance' }" @click="tab = 'appearance'">
        <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="13.5" cy="6.5" r=".5"/>
          <circle cx="17.5" cy="10.5" r=".5"/>
          <circle cx="8.5" cy="7.5" r=".5"/>
          <circle cx="6.5" cy="12.5" r=".5"/>
          <path
              d="M12 2C6.5 2 2 6.5 2 12s4.5 10 10 10c.926 0 1.648-.746 1.648-1.688 0-.437-.18-.835-.437-1.125-.29-.289-.438-.652-.438-1.125a1.64 1.64 0 011.668-1.668h1.996c3.051 0 5.555-2.503 5.555-5.554C21.965 6.012 17.461 2 12 2z"/>
        </svg>
        Aparência
      </button>
      <button class="tab-btn" :class="{ active: tab === 'payments' }" @click="tab = 'payments'">
        <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <rect x="1" y="4" width="22" height="16" rx="2"/>
          <line x1="1" y1="10" x2="23" y2="10"/>
        </svg>
        Pagamentos
        <!-- bolinha vermelha se não conectou ainda -->
        <span v-if="stripeStatus === 'NOT_CONNECTED' || stripeStatus === 'PENDING'" class="tab-alert"></span>
      </button>
    </div>

    <!-- ABA: PRODUTOS -->
    <div v-show="tab === 'products'" class="tab-panel">
      <div v-if="!managingProducts">
        <div class="panel-header">
          <div>
            <h2 class="panel-title">Produtos na sua loja</h2>
            <p class="panel-sub">Gerencie quais produtos aparecem para seus clientes</p>
          </div>
          <button class="manage-btn" @click="managingProducts = true">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
              <path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"/>
              <path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"/>
            </svg>
            Gerenciar produtos
          </button>
        </div>

        <div v-if="!store.products?.length" class="empty-state">
          <div class="es-icon">📦</div>
          <p class="es-title">Nenhum produto ainda</p>
          <p class="es-sub">Clique em "Gerenciar produtos" para adicionar itens à sua loja</p>
          <button class="manage-btn" style="margin: 16px auto 0; display: flex;" @click="managingProducts = true">
            + Adicionar produtos
          </button>
        </div>

        <div v-else class="products-grid">
          <div v-for="product in store.products" :key="product.id" class="product-card">
            <div class="pc-img-wrap">
              <img :src="product.imagem || 'https://via.placeholder.com/300x200'" :alt="product.nome" class="pc-img"/>
              <div class="pc-img-overlay"></div>
            </div>
            <div class="pc-body">
              <p class="pc-name">{{ product.nome }}</p>
              <p class="pc-price" :style="`color: ${store.primaryColor}`">
                R$ {{ product.preco?.toFixed(2).replace('.', ',') }}
              </p>
            </div>
          </div>
        </div>
      </div>

      <div v-else>
        <div class="panel-header">
          <div>
            <h2 class="panel-title">Selecionar produtos</h2>
            <p class="panel-sub">Escolha produtos do catálogo para exibir na sua loja</p>
          </div>
          <button class="manage-btn" @click="managingProducts = false">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
              <polyline points="15 18 9 12 15 6"/>
            </svg>
            Voltar
          </button>
        </div>
        <DropperProductSelector
            :initial-products="store.products?.map(p => ({ productId: p.id, customPrice: p.preco })) ?? []"
            @updated="onProductsUpdated"
            @cancel="managingProducts = false"
        />
      </div>
    </div>

    <div v-show="tab === 'appearance'" class="tab-panel">
      <div class="appearance-grid">
        <div class="appearance-form">
          <div class="panel-header">
            <div>
              <h2 class="panel-title">Visual da loja</h2>
              <p class="panel-sub">Personalize como seus clientes vão ver sua loja</p>
            </div>
          </div>

          <div class="form-section">
            <div class="fs-label">Identidade</div>
            <div class="form-row">
              <div class="form-group">
                <label class="form-label">Nome da loja</label>
                <input v-model="editForm.storeName" class="form-input"/>
              </div>
              <div class="form-group">
                <label class="form-label">Slogan</label>
                <input v-model="editForm.slogan" class="form-input"/>
              </div>
            </div>
          </div>

          <div class="form-section">
            <div class="fs-label">Cores</div>
            <div class="colors-row">
              <div class="color-field" v-for="c in colorFields" :key="c.key">
                <div class="cf-preview" :style="`background: ${editForm[c.key]}`"
                     :class="{ bordered: c.key === 'backgroundColor' }">
                  <input type="color" v-model="editForm[c.key]" class="cf-input"/>
                </div>
                <span class="cf-label">{{ c.label }}</span>
                <span class="cf-hex">{{ editForm[c.key] }}</span>
              </div>
            </div>
          </div>

          <button class="save-btn" :class="{ loading: storeStore.loading }" @click="saveAppearance">
            <span v-if="storeStore.loading" class="btn-spinner"></span>
            <svg v-else width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
              <polyline points="20 6 9 17 4 12"/>
            </svg>
            {{ storeStore.loading ? 'Salvando...' : 'Salvar alterações' }}
          </button>
        </div>

        <div class="appearance-preview">
          <div class="ap-label">Preview ao vivo</div>
          <div class="ap-browser">
            <div class="apb-bar">
              <div class="apb-dots"><span></span><span></span><span></span></div>
              <div class="apb-url">sua-loja.godrop.com.br</div>
            </div>
            <div class="apb-body" :style="`background: ${editForm.backgroundColor}`">
              <div class="apb-hero"
                   :style="`background: linear-gradient(135deg, ${editForm.primaryColor}, ${editForm.secondaryColor})`">
                <span class="apb-name">{{ editForm.storeName || 'Minha Loja' }}</span>
                <span class="apb-slogan">{{ editForm.slogan || 'Seu slogan aqui' }}</span>
              </div>
              <div class="apb-products">
                <div v-for="i in 4" :key="i" class="apb-product">
                  <div class="apbp-img" :style="`background: ${editForm.primaryColor}18`"></div>
                  <div class="apbp-info">
                    <div class="apbp-name"></div>
                    <div class="apbp-price" :style="`background: ${editForm.primaryColor}40`"></div>
                    <div class="apbp-btn" :style="`background: ${editForm.primaryColor}`"></div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- ABA: PAGAMENTOS -->
    <div v-show="tab === 'payments'" class="tab-panel">
      <div class="panel-header">
        <div>
          <h2 class="panel-title">Pagamentos</h2>
          <p class="panel-sub">Conecte sua conta Stripe para receber automaticamente sua parte nas vendas</p>
        </div>
      </div>

      <div v-if="stripeStatus === 'NOT_CONNECTED' || stripeStatus === 'PENDING'" class="stripe-warning">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="12" cy="12" r="10"/>
          <line x1="12" y1="8" x2="12" y2="12"/>
          <line x1="12" y1="16" x2="12.01" y2="16"/>
        </svg>
        <span>
          <strong>Atenção:</strong>
          {{
            stripeStatus === 'NOT_CONNECTED'
                ? 'Você ainda não conectou sua conta Stripe. Sem isso, não é possível receber pagamentos das suas vendas.'
                : 'Seu cadastro na Stripe está incompleto. Finalize para começar a receber.'
          }}
        </span>
      </div>

      <StripeConnect
          :initial-status="stripeStatus"
          @status-changed="onStripeStatusChanged"
      />

      <div class="split-explainer">
        <div class="se-title">Como funciona o split de pagamento?</div>
        <div class="se-steps">
          <div class="se-step">
            <div class="se-num">1</div>
            <div class="se-text">
              <strong>Cliente compra na sua loja</strong>
              <span>Paga o preço que você definiu no produto</span>
            </div>
          </div>
          <div class="se-arrow">→</div>
          <div class="se-step">
            <div class="se-num">2</div>
            <div class="se-text">
              <strong>Split automático</strong>
              <span>GoDrop recebe o preço de custo, você recebe a diferença</span>
            </div>
          </div>
          <div class="se-arrow">→</div>
          <div class="se-step">
            <div class="se-num">3</div>
            <div class="se-text">
              <strong>Saque direto</strong>
              <span>Sua margem vai direto para sua conta bancária via Stripe</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <transition name="toast">
      <div v-if="snackbar" class="toast" :class="snackbarColor">
        {{ snackbarMessage }}
      </div>
    </transition>

  </div>
</template>

<script setup>
import {ref, computed, onMounted} from 'vue'
import {useStoreStore} from '../stores/store'
import {useDropperStore} from '../stores/dropper'
import DropperProductSelector from '../components/DropperProductSelector.vue'
import StripeConnect from '../components/StripeConnect.vue'

const storeStore = useStoreStore()
const dropperStore = useDropperStore()

const tab = ref('products')
const managingProducts = ref(false)
const snackbar = ref(false)
const snackbarMessage = ref('')
const snackbarColor = ref('success')
const copied = ref(false)

const store = computed(() => storeStore.myStore)
const dropperProfile = computed(() => dropperStore.profile)

// Status Stripe vem do perfil do dropper
const stripeStatus = ref('NOT_CONNECTED')

const stripeStatusLabel = computed(() => ({
  NOT_CONNECTED: 'Não conectada',
  PENDING: 'Incompleta',
  RESTRICTED: 'Pendente',
  ACTIVE: 'Ativa ✓',
  DISABLED: 'Desabilitada',
}[stripeStatus.value] ?? 'Desconhecido'))

const stripeIconClass = computed(() => ({
  NOT_CONNECTED: 'stripe-nc',
  PENDING: 'stripe-pending',
  RESTRICTED: 'stripe-restricted',
  ACTIVE: 'stripe-active',
  DISABLED: 'stripe-nc',
}[stripeStatus.value] ?? 'stripe-nc'))

const colorFields = [
  {key: 'primaryColor', label: 'Primária'},
  {key: 'secondaryColor', label: 'Secundária'},
  {key: 'backgroundColor', label: 'Fundo'},
]

const editForm = ref({
  storeName: '',
  slogan: '',
  primaryColor: '#2563EB',
  secondaryColor: '#0EA5E9',
  backgroundColor: '#F8FAFC',
})

function formatMoney(value) {
  return (value ?? 0).toLocaleString('pt-BR', {minimumFractionDigits: 2})
}

async function toggleStore() {
  await storeStore.toggleStore()
  notify(store.value.active ? 'Loja online! 🟢' : 'Loja offline 🔴', store.value.active ? 'success' : 'warning')
}

async function saveAppearance() {
  await storeStore.updateStore(editForm.value)
  notify('Aparência salva com sucesso!')
}

async function copyStoreLink() {
  const url = `${window.location.origin}/loja/${store.value.slug}`
  await navigator.clipboard.writeText(url)
  copied.value = true
  setTimeout(() => copied.value = false, 2000)
  notify('Link copiado! 📋')
}

async function onProductsUpdated(newIds) {
  await storeStore.fetchMyStore()
  managingProducts.value = false
  notify(`${newIds.length} produto(s) salvos na loja! 🛍️`)
}

// Chamado pelo StripeConnect quando o status muda
function onStripeStatusChanged(data) {
  stripeStatus.value = data.status
  if (data.status === 'ACTIVE') {
    notify('Conta Stripe verificada! Você já pode receber pagamentos. 🎉')
  }
}

function notify(message, color = 'success') {
  snackbarMessage.value = message
  snackbarColor.value = color
  snackbar.value = true
  setTimeout(() => snackbar.value = false, 3000)
}

onMounted(() => {
  if (store.value) {
    editForm.value = {
      storeName: store.value.storeName,
      slogan: store.value.slogan,
      primaryColor: store.value.primaryColor,
      secondaryColor: store.value.secondaryColor,
      backgroundColor: store.value.backgroundColor,
    }
  }
  // Pega o status Stripe do perfil do dropper
  if (dropperProfile.value?.stripeAccountStatus) {
    stripeStatus.value = dropperProfile.value.stripeAccountStatus
  }

  // Se voltou do onboarding da Stripe
  const params = new URLSearchParams(window.location.search)
  if (params.get('stripe') === 'return' || params.get('stripe') === 'refresh') {
    tab.value = 'payments'
    window.history.replaceState({}, '', window.location.pathname)
  }
})
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Instrument+Serif:ital@0;1&family=Geist:wght@300;400;500;600;700&display=swap');

* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

.dash-root {
  font-family: 'Geist', sans-serif;
  background: #FAFAFA;
  min-height: 100vh;
  color: #0F172A;
}

/* ── BANNER ── */
.store-banner {
  position: relative;
  overflow: hidden;
  border-radius: 20px;
  margin-bottom: 24px;
}

.banner-noise {
  position: absolute;
  inset: 0;
  opacity: 0.04;
  background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 256 256' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noise'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.9' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23noise)'/%3E%3C/svg%3E");
  pointer-events: none;
}

.banner-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 32px 32px 20px;
}

.banner-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.store-avatar {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: 'Instrument Serif', serif;
  font-size: 28px;
  color: white;
  flex-shrink: 0;
}

.store-info {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.store-status-row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.status-pill {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 10px;
  border-radius: 100px;
  font-size: 12px;
  font-weight: 600;
  background: rgba(255, 255, 255, 0.2);
  color: white;
}

.status-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
}

.status-pill.online .status-dot {
  background: #4ADE80;
}

.status-pill.offline .status-dot {
  background: #F87171;
}

.store-level {
  padding: 4px 10px;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 100px;
  font-size: 11px;
  font-weight: 700;
  color: rgba(255, 255, 255, 0.9);
  text-transform: uppercase;
  letter-spacing: 0.06em;
}

.store-name {
  font-family: 'Instrument Serif', serif;
  font-size: 28px;
  font-weight: 400;
  color: white;
  line-height: 1.1;
}

.store-slogan {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.75);
}

.banner-right {
  display: flex;
  gap: 10px;
}

.banner-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  border-radius: 10px;
  font-family: 'Geist', sans-serif;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  border: none;
  text-decoration: none;
}

.banner-btn.primary {
  background: white;
  color: #0F172A;
}

.banner-btn.primary:hover {
  background: #F1F5F9;
}

.banner-btn.secondary {
  background: rgba(255, 255, 255, 0.15);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.25);
}

.banner-btn.secondary:hover {
  background: rgba(255, 255, 255, 0.25);
}

.banner-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 32px 20px;
}

.slug-row {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: rgba(255, 255, 255, 0.7);
}

.slug-row strong {
  color: white;
}

.toggle-wrap {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
}

.toggle-label {
  font-size: 13px;
  font-weight: 500;
  color: rgba(255, 255, 255, 0.8);
}

.toggle {
  width: 44px;
  height: 24px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 100px;
  position: relative;
  transition: background 0.3s;
  cursor: pointer;
}

.toggle.on {
  background: #4ADE80;
}

.toggle-thumb {
  position: absolute;
  top: 3px;
  left: 3px;
  width: 18px;
  height: 18px;
  background: white;
  border-radius: 50%;
  transition: transform 0.3s;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
}

.toggle.on .toggle-thumb {
  transform: translateX(20px);
}

/* ── STATS ── */
.stats-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.stat-card {
  background: white;
  border: 1px solid #E2E8F0;
  border-radius: 16px;
  padding: 20px 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  transition: all 0.2s;
}

.stat-card:hover {
  border-color: #CBD5E1;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.04);
  transform: translateY(-1px);
}

.sc-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.sc-icon.blue {
  background: #EFF6FF;
  color: #2563EB;
}

.sc-icon.green {
  background: #F0FDF4;
  color: #16A34A;
}

.sc-icon.amber {
  background: #FFFBEB;
  color: #D97706;
}

.sc-icon.stripe-active {
  background: #F0FDF4;
  color: #16A34A;
}

.sc-icon.stripe-pending {
  background: #FFFBEB;
  color: #D97706;
}

.sc-icon.stripe-restricted {
  background: #FFF7ED;
  color: #EA580C;
}

.sc-icon.stripe-nc {
  background: #F1F5F9;
  color: #94A3B8;
}

.sc-data {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.sc-num {
  font-size: 20px;
  font-weight: 700;
  color: #0F172A;
}

.sc-label {
  font-size: 12px;
  color: #64748B;
  font-weight: 500;
}

/* ── TABS ── */
.tabs-nav {
  display: flex;
  gap: 4px;
  background: white;
  border: 1px solid #E2E8F0;
  border-radius: 14px;
  padding: 5px;
  margin-bottom: 20px;
  width: fit-content;
}

.tab-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 9px 20px;
  border-radius: 10px;
  font-family: 'Geist', sans-serif;
  font-size: 13px;
  font-weight: 500;
  color: #64748B;
  background: transparent;
  border: none;
  cursor: pointer;
  transition: all 0.2s;
  position: relative;
}

.tab-btn:hover {
  color: #0F172A;
}

.tab-btn.active {
  background: #0F172A;
  color: white;
}

.tab-alert {
  position: absolute;
  top: 6px;
  right: 6px;
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: #EF4444;
}

/* ── PANELS ── */
.tab-panel {
  background: white;
  border: 1px solid #E2E8F0;
  border-radius: 20px;
  padding: 28px;
  margin-bottom: 24px;
}

.panel-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: 24px;
}

.panel-title {
  font-size: 18px;
  font-weight: 700;
  color: #0F172A;
}

.panel-sub {
  font-size: 13px;
  color: #64748B;
  margin-top: 3px;
}

.manage-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 9px 18px;
  background: #F8FAFC;
  border: 1px solid #E2E8F0;
  border-radius: 10px;
  font-family: 'Geist', sans-serif;
  font-size: 13px;
  font-weight: 500;
  color: #374151;
  cursor: pointer;
  transition: all 0.2s;
  flex-shrink: 0;
}

.manage-btn:hover {
  border-color: #94A3B8;
  color: #0F172A;
}

.empty-state {
  text-align: center;
  padding: 56px 0;
}

.es-icon {
  font-size: 48px;
  margin-bottom: 12px;
}

.es-title {
  font-size: 16px;
  font-weight: 600;
  color: #0F172A;
  margin-bottom: 6px;
}

.es-sub {
  font-size: 14px;
  color: #64748B;
}

/* ── PRODUCTS ── */
.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
  gap: 14px;
}

.product-card {
  border: 1px solid #E2E8F0;
  border-radius: 14px;
  overflow: hidden;
  transition: all 0.25s;
  background: white;
}

.product-card:hover {
  border-color: #CBD5E1;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.06);
  transform: translateY(-3px);
}

.pc-img-wrap {
  position: relative;
  height: 120px;
  overflow: hidden;
}

.pc-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.4s;
}

.product-card:hover .pc-img {
  transform: scale(1.05);
}

.pc-img-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(to bottom, transparent 60%, rgba(0, 0, 0, 0.08));
}

.pc-body {
  padding: 12px;
}

.pc-name {
  font-size: 13px;
  font-weight: 600;
  color: #0F172A;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-bottom: 4px;
}

.pc-price {
  font-size: 13px;
  font-weight: 700;
}

/* ── APPEARANCE ── */
.appearance-grid {
  display: grid;
  grid-template-columns: 1fr 380px;
  gap: 28px;
  align-items: start;
}

.appearance-form {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.form-section {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.fs-label {
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: #94A3B8;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 14px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.form-label {
  font-size: 12px;
  font-weight: 600;
  color: #475569;
}

.form-input {
  padding: 11px 14px;
  background: #F8FAFC;
  border: 1px solid #E2E8F0;
  border-radius: 10px;
  font-family: 'Geist', sans-serif;
  font-size: 14px;
  color: #0F172A;
  outline: none;
  transition: all 0.2s;
}

.form-input:focus {
  border-color: #2563EB;
  background: white;
  box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.08);
}

.colors-row {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}

.color-field {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
}

.cf-preview {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.cf-preview.bordered {
  border: 1.5px solid #E2E8F0;
}

.cf-input {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  opacity: 0;
  cursor: pointer;
}

.cf-label {
  font-size: 12px;
  font-weight: 600;
  color: #374151;
}

.cf-hex {
  font-size: 11px;
  color: #94A3B8;
  font-family: monospace;
}

.save-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 13px 28px;
  background: #0F172A;
  color: white;
  border: none;
  border-radius: 12px;
  font-family: 'Geist', sans-serif;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  align-self: flex-start;
}

.save-btn:hover {
  background: #1E293B;
  box-shadow: 0 4px 12px rgba(15, 23, 42, 0.25);
}

.save-btn.loading {
  opacity: 0.7;
  cursor: wait;
}

.btn-spinner {
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

/* ── LIVE PREVIEW ── */
.appearance-preview {
  position: sticky;
  top: 24px;
}

.ap-label {
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: #94A3B8;
  margin-bottom: 12px;
}

.ap-browser {
  background: white;
  border: 1px solid #E2E8F0;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.06);
}

.apb-bar {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 14px;
  background: #F8FAFC;
  border-bottom: 1px solid #E2E8F0;
}

.apb-dots {
  display: flex;
  gap: 5px;
}

.apb-dots span {
  width: 9px;
  height: 9px;
  border-radius: 50%;
}

.apb-dots span:nth-child(1) {
  background: #EF4444;
}

.apb-dots span:nth-child(2) {
  background: #F59E0B;
}

.apb-dots span:nth-child(3) {
  background: #22C55E;
}

.apb-url {
  flex: 1;
  background: rgba(0, 0, 0, 0.04);
  border-radius: 5px;
  padding: 4px 10px;
  font-size: 11px;
  color: #94A3B8;
}

.apb-hero {
  padding: 20px 18px;
  transition: background 0.4s;
}

.apb-name {
  display: block;
  font-family: 'Instrument Serif', serif;
  font-size: 18px;
  color: white;
}

.apb-slogan {
  display: block;
  font-size: 11px;
  color: rgba(255, 255, 255, 0.75);
  margin-top: 2px;
}

.apb-products {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
  padding: 14px;
  transition: background 0.4s;
}

.apb-product {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.apbp-img {
  height: 64px;
  transition: background 0.4s;
}

.apbp-info {
  padding: 8px;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.apbp-name {
  height: 7px;
  background: #E2E8F0;
  border-radius: 4px;
}

.apbp-price {
  height: 7px;
  width: 55%;
  border-radius: 4px;
  transition: background 0.4s;
}

.apbp-btn {
  height: 22px;
  border-radius: 5px;
  margin-top: 2px;
  transition: background 0.4s;
}

/* ── PAYMENTS TAB ── */
.stripe-warning {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  background: #FFFBEB;
  border: 1px solid #FDE68A;
  border-radius: 12px;
  padding: 14px 16px;
  margin-bottom: 20px;
  font-size: 13px;
  color: #92400E;
}

.stripe-warning strong {
  font-weight: 700;
}

.split-explainer {
  background: #F8FAFC;
  border: 1px solid #E2E8F0;
  border-radius: 16px;
  padding: 24px;
  margin-top: 20px;
}

.se-title {
  font-size: 14px;
  font-weight: 700;
  color: #0F172A;
  margin-bottom: 20px;
}

.se-steps {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.se-step {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  flex: 1;
  min-width: 160px;
}

.se-num {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: #0F172A;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 700;
  flex-shrink: 0;
}

.se-text {
  display: flex;
  flex-direction: column;
  gap: 3px;
}

.se-text strong {
  font-size: 13px;
  font-weight: 600;
  color: #0F172A;
}

.se-text span {
  font-size: 12px;
  color: #64748B;
}

.se-arrow {
  font-size: 20px;
  color: #CBD5E1;
  flex-shrink: 0;
}

/* ── TOAST ── */
.toast {
  position: fixed;
  bottom: 28px;
  left: 50%;
  transform: translateX(-50%);
  padding: 12px 24px;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 500;
  z-index: 999;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.toast.success {
  background: #0F172A;
  color: white;
}

.toast.warning {
  background: #FEF3C7;
  color: #92400E;
  border: 1px solid #FDE68A;
}

.toast-enter-active, .toast-leave-active {
  transition: all 0.3s ease;
}

.toast-enter-from, .toast-leave-to {
  opacity: 0;
  transform: translateX(-50%) translateY(10px);
}

/* ── RESPONSIVE ── */
@media (max-width: 900px) {
  .stats-row {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .stats-row {
    grid-template-columns: 1fr;
  }

  .appearance-grid {
    grid-template-columns: 1fr;
  }

  .banner-inner {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .banner-right {
    flex-direction: row;
  }

  .appearance-preview {
    position: static;
  }

  .form-row {
    grid-template-columns: 1fr;
  }

  .se-steps {
    flex-direction: column;
  }

  .se-arrow {
    transform: rotate(90deg);
  }
}
</style>
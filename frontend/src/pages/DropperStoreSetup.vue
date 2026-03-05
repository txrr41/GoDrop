<template>
  <div class="setup-root">
    <template v-if="!storeStore.hasStore">
      <div class="wizard-layout">

        <aside class="wizard-sidebar">
          <div class="sidebar-brand">
            <div class="brand-dot"></div>
            <span>GoDrop Studio</span>
          </div>

          <div class="steps-track">
            <div v-for="(s, i) in wizardSteps" :key="i"
                 class="track-item"
                 :class="{ active: step === i + 1, done: step > i + 1 }">
              <div class="track-icon">
                <svg v-if="step > i + 1" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                     stroke-width="2.5">
                  <polyline points="20 6 9 17 4 12"/>
                </svg>
                <span v-else>{{ i + 1 }}</span>
              </div>
              <div class="track-text">
                <span class="track-label">{{ s.label }}</span>
                <span class="track-sub">{{ s.sub }}</span>
              </div>
            </div>
          </div>

          <div class="sidebar-footer">
            <div class="sf-card">
              <div class="sf-icon">⚡</div>
              <p class="sf-text">Loja ativa em menos de 2 minutos com nossa IA</p>
            </div>
          </div>
        </aside>

        <main class="wizard-main">

          <div v-if="step === 1" class="step-panel">
            <div class="step-header">
              <span class="step-eyebrow">Passo 1 de 3</span>
              <h1 class="step-title">Descreva sua <span class="title-accent">visão</span></h1>
              <p class="step-desc">Conte para nossa IA sobre o nicho, público e estilo que você imagina. Quanto mais
                detalhes, melhor o resultado.</p>
            </div>

            <div class="prompt-examples-label">Inspirações rápidas</div>
            <div class="prompt-chips">
              <button
                  v-for="ex in promptExamples" :key="ex"
                  class="prompt-chip"
                  :class="{ selected: aiPrompt === ex }"
                  @click="aiPrompt = ex"
              >{{ ex }}
              </button>
            </div>

            <div class="textarea-wrapper">
              <textarea
                  v-model="aiPrompt"
                  class="prompt-textarea"
                  placeholder="Ex: Quero uma loja de suplementos fitness com visual moderno e vibrante, focada em jovens atletas que buscam performance..."
                  maxlength="300"
              ></textarea>
              <div class="textarea-footer">
                <span class="char-count">{{ aiPrompt.length }}/300</span>
              </div>
            </div>

            <button
                class="action-btn"
                :class="{ loading: storeStore.aiLoading, disabled: aiPrompt.trim().length < 20 }"
                :disabled="aiPrompt.trim().length < 20 || storeStore.aiLoading"
                @click="generateTheme"
            >
              <span v-if="storeStore.aiLoading" class="btn-spinner"></span>
              <span v-else class="btn-icon">✦</span>
              {{ storeStore.aiLoading ? 'Gerando tema...' : 'Gerar com Inteligência Artificial' }}
            </button>
          </div>

          <div v-if="step === 2 && storeStore.aiTheme" class="step-panel">
            <div class="step-header">
              <span class="step-eyebrow">Passo 2 de 3</span>
              <h1 class="step-title">Seu tema foi <span class="title-accent">criado</span></h1>
              <p class="step-desc">{{ storeStore.aiTheme.reasoning }}</p>
            </div>

            <div class="store-preview-card">
              <div class="preview-browser-bar">
                <div class="pbb-dots"><span></span><span></span><span></span></div>
                <div class="pbb-url">godrop.com.br/loja/<strong>{{ slugify(storeStore.aiTheme.storeName) }}</strong>
                </div>
                <div class="pbb-badge"
                     :style="`background: ${storeStore.aiTheme.primaryColor}22; color: ${storeStore.aiTheme.primaryColor}`">
                  {{ storeStore.aiTheme.theme }}
                </div>
              </div>

              <div class="preview-hero"
                   :style="`background: linear-gradient(135deg, ${storeStore.aiTheme.primaryColor}, ${storeStore.aiTheme.secondaryColor})`">
                <div class="ph-logo">{{ storeStore.aiTheme.storeName }}</div>
                <div class="ph-slogan">{{ storeStore.aiTheme.slogan }}</div>
              </div>

              <div class="preview-products" :style="`background: ${storeStore.aiTheme.backgroundColor}`">
                <div v-for="i in 3" :key="i" class="preview-product-card">
                  <div class="ppc-img" :style="`background: ${storeStore.aiTheme.primaryColor}15`"></div>
                  <div class="ppc-body">
                    <div class="ppc-name">Produto {{ i }}</div>
                    <div class="ppc-price" :style="`color: ${storeStore.aiTheme.primaryColor}`">R$ 99,90</div>
                    <button class="ppc-btn" :style="`background: ${storeStore.aiTheme.primaryColor}`">Comprar</button>
                  </div>
                </div>
              </div>
            </div>

            <!-- Paleta e metadados -->
            <div class="theme-meta-grid">
              <div class="tm-item">
                <span class="tm-label">Nome</span>
                <span class="tm-val">{{ storeStore.aiTheme.storeName }}</span>
              </div>
              <div class="tm-item">
                <span class="tm-label">Estilo</span>
                <span class="tm-badge"
                      :style="`color: ${storeStore.aiTheme.primaryColor}; background: ${storeStore.aiTheme.primaryColor}15`">{{
                    storeStore.aiTheme.theme
                  }}</span>
              </div>
              <div class="tm-item tm-full">
                <span class="tm-label">Slogan</span>
                <span class="tm-val">{{ storeStore.aiTheme.slogan }}</span>
              </div>
              <div class="tm-item tm-full">
                <span class="tm-label">Paleta</span>
                <div class="tm-colors">
                  <div class="color-swatch" :style="`background: ${storeStore.aiTheme.primaryColor}`">
                    <span>Primária</span>
                  </div>
                  <div class="color-swatch" :style="`background: ${storeStore.aiTheme.secondaryColor}`">
                    <span>Secundária</span>
                  </div>
                  <div class="color-swatch bordered" :style="`background: ${storeStore.aiTheme.backgroundColor}`">
                    <span :style="`color: #374151`">Fundo</span>
                  </div>
                </div>
              </div>
            </div>

            <div class="step-actions">
              <button class="action-btn-secondary" @click="step = 1">
                ↺ Gerar outro tema
              </button>
              <button class="action-btn" @click="applyAndNext">
                Usar este tema <span>→</span>
              </button>
            </div>
          </div>

          <!-- STEP 3: Ajuste e finalize -->
          <div v-if="step === 3" class="step-panel">
            <div class="step-header">
              <span class="step-eyebrow">Passo 3 de 3</span>
              <h1 class="step-title">Ajuste e <span class="title-accent">publique</span></h1>
              <p class="step-desc">Personalize os últimos detalhes e selecione os produtos que vão estar na sua
                loja.</p>
            </div>

            <div class="form-grid">
              <div class="form-group">
                <label class="form-label">Nome da loja *</label>
                <input v-model="form.storeName" class="form-input" placeholder="Ex: MaxForce Store"/>
              </div>
              <div class="form-group">
                <label class="form-label">Slogan</label>
                <input v-model="form.slogan" class="form-input" placeholder="Ex: Potência máxima."/>
              </div>
              <div class="form-group fg-full">
                <label class="form-label">Descrição da loja</label>
                <textarea v-model="form.description" class="form-textarea" rows="2"
                          placeholder="Breve descrição para seus clientes..."></textarea>
              </div>

              <div class="form-group fg-full">
                <label class="form-label">Cores da loja</label>
                <div class="color-pickers">
                  <div class="cp-item">
                    <input type="color" v-model="form.primaryColor" class="cp-input"/>
                    <span class="cp-label">Primária</span>
                  </div>
                  <div class="cp-item">
                    <input type="color" v-model="form.secondaryColor" class="cp-input"/>
                    <span class="cp-label">Secundária</span>
                  </div>
                  <div class="cp-item">
                    <input type="color" v-model="form.backgroundColor" class="cp-input"/>
                    <span class="cp-label">Fundo</span>
                  </div>
                </div>
              </div>

              <!-- Mini preview ao vivo -->
              <div class="form-group fg-full">
                <label class="form-label">Preview ao vivo</label>
                <div class="live-preview" :style="`background: ${form.backgroundColor}`">
                  <div class="lp-header"
                       :style="`background: linear-gradient(135deg, ${form.primaryColor}, ${form.secondaryColor})`">
                    <span class="lp-name">{{ form.storeName || 'Minha Loja' }}</span>
                    <span class="lp-slogan">{{ form.slogan || 'Slogan da sua loja' }}</span>
                  </div>
                  <div class="lp-body">
                    <div v-for="i in 3" :key="i" class="lp-card">
                      <div class="lp-img" :style="`background: ${form.primaryColor}15`"></div>
                      <div class="lp-info">
                        <div class="lp-prodname"></div>
                        <div class="lp-price" :style="`background: ${form.primaryColor}40`"></div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Seleção de produtos -->
              <div class="form-group fg-full">
                <label class="form-label">Produtos na loja <span class="label-count">{{ form.productIds.length }} selecionados</span></label>
                <div class="products-selector">
                  <button
                      v-for="product in productStore.products"
                      :key="product.id"
                      class="product-chip"
                      :class="{ selected: form.productIds.includes(product.id) }"
                      :style="form.productIds.includes(product.id) ? `background: ${form.primaryColor}; border-color: ${form.primaryColor}` : ''"
                      @click="toggleProduct(product.id)"
                  >
                    <span class="pc-check">✓</span>
                    {{ product.nome }}
                  </button>
                </div>
              </div>
            </div>

            <button
                class="action-btn"
                :class="{ loading: storeStore.loading, disabled: !form.storeName }"
                :disabled="!form.storeName || storeStore.loading"
                @click="createStore"
            >
              <span v-if="storeStore.loading" class="btn-spinner"></span>
              <span v-else>🚀</span>
              {{ storeStore.loading ? 'Criando sua loja...' : 'Criar Minha Loja' }}
            </button>
          </div>

        </main>
      </div>
    </template>

    <!-- === COM LOJA: DASHBOARD === -->
    <template v-else>
      <DropperStoreDashboard/>
    </template>

  </div>
</template>

<script setup>
import {ref, computed, onMounted} from 'vue'
import {useStoreStore} from '../stores/store'
import {useProductStore} from '../stores/product'
import DropperStoreDashboard from '../pages/DropperStoreDashboard.vue'

const storeStore = useStoreStore()
const productStore = useProductStore()

const step = ref(1)
const aiPrompt = ref('')

const wizardSteps = [
  {label: 'Descreva sua loja', sub: 'Conte sua visão para a IA'},
  {label: 'Tema gerado', sub: 'Revise o resultado da IA'},
  {label: 'Publique', sub: 'Ajuste e ative sua loja'},
]

const form = ref({
  storeName: '',
  slogan: '',
  description: '',
  primaryColor: '#2563EB',
  secondaryColor: '#0EA5E9',
  backgroundColor: '#F8FAFC',
  theme: 'MODERN',
  productIds: [],
})

const promptExamples = [
  'Suplementos fitness vibrante e energético',
  'Moda feminina elegante e sofisticada',
  'Eletrônicos e gadgets tech moderno',
  'Cosméticos naturais e orgânicos clean',
  'Loja infantil colorida e divertida',
]

function slugify(name) {
  return (name || '').toLowerCase().replace(/\s+/g, '-').replace(/[^a-z0-9-]/g, '')
}

function toggleProduct(id) {
  const idx = form.value.productIds.indexOf(id)
  if (idx === -1) form.value.productIds.push(id)
  else form.value.productIds.splice(idx, 1)
}

async function generateTheme() {
  await storeStore.generateAiTheme(aiPrompt.value)
  step.value = 2
}

function applyAndNext() {
  const ai = storeStore.aiTheme
  form.value.storeName = ai.storeName
  form.value.slogan = ai.slogan
  form.value.primaryColor = ai.primaryColor
  form.value.secondaryColor = ai.secondaryColor
  form.value.backgroundColor = ai.backgroundColor
  form.value.theme = ai.theme
  form.value.description = ai.description || ''
  step.value = 3
}

async function createStore() {
  await storeStore.createStore({...form.value, aiPrompt: aiPrompt.value})
}

onMounted(async () => {
  await storeStore.fetchMyStore()
  await productStore.fetchProducts()
})
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Instrument+Serif:ital@0;1&family=Geist:wght@300;400;500;600;700&display=swap');

* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

.setup-root {
  min-height: 100vh;
  background: #FAFAFA;
  font-family: 'Geist', sans-serif;
}

/* ── WIZARD LAYOUT ── */
.wizard-layout {
  display: flex;
  min-height: 100vh;
}

/* ── SIDEBAR ── */
.wizard-sidebar {
  width: 280px;
  flex-shrink: 0;
  background: #0F172A;
  display: flex;
  flex-direction: column;
  padding: 32px 24px;
  position: sticky;
  top: 0;
  height: 100vh;
}

.sidebar-brand {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 15px;
  font-weight: 600;
  color: #fff;
  margin-bottom: 48px;
}

.brand-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background: linear-gradient(135deg, #2563EB, #0EA5E9);
}

.steps-track {
  display: flex;
  flex-direction: column;
  gap: 8px;
  flex: 1;
}

.track-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 14px 16px;
  border-radius: 12px;
  cursor: default;
  transition: all 0.2s;
}

.track-item.done {
  background: rgba(37, 99, 235, 0.1);
}

.track-item.active {
  background: rgba(37, 99, 235, 0.18);
}

.track-icon {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  border: 1.5px solid rgba(255, 255, 255, 0.15);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 700;
  color: #64748B;
  flex-shrink: 0;
  transition: all 0.2s;
}

.track-item.done .track-icon {
  background: #2563EB;
  border-color: #2563EB;
  color: white;
}

.track-item.active .track-icon {
  border-color: #2563EB;
  color: #60A5FA;
}

.track-text {
  display: flex;
  flex-direction: column;
}

.track-label {
  font-size: 13px;
  font-weight: 600;
  color: #94A3B8;
}

.track-sub {
  font-size: 11px;
  color: #475569;
  margin-top: 1px;
}

.track-item.active .track-label {
  color: #fff;
}

.track-item.done .track-label {
  color: #60A5FA;
}

.sidebar-footer {
  margin-top: auto;
}

.sf-card {
  background: rgba(37, 99, 235, 0.12);
  border: 1px solid rgba(37, 99, 235, 0.2);
  border-radius: 12px;
  padding: 16px;
}

.sf-icon {
  font-size: 20px;
  margin-bottom: 8px;
}

.sf-text {
  font-size: 12px;
  color: #94A3B8;
  line-height: 1.5;
}

/* ── WIZARD MAIN ── */
.wizard-main {
  flex: 1;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  padding: 48px 48px;
  overflow-y: auto;
}

.step-panel {
  width: 100%;
  max-width: 720px;
}

/* ── STEP HEADER ── */
.step-eyebrow {
  display: inline-block;
  font-size: 11px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.1em;
  color: #2563EB;
  margin-bottom: 12px;
  background: rgba(37, 99, 235, 0.08);
  border: 1px solid rgba(37, 99, 235, 0.15);
  padding: 4px 12px;
  border-radius: 100px;
}

.step-title {
  font-family: 'Instrument Serif', serif;
  font-size: clamp(32px, 4vw, 48px);
  font-weight: 400;
  line-height: 1.1;
  color: #0F172A;
  margin-bottom: 12px;
}

.title-accent {
  font-style: italic;
  background: linear-gradient(135deg, #2563EB, #0EA5E9);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.step-desc {
  font-size: 15px;
  color: #64748B;
  line-height: 1.6;
  margin-bottom: 32px;
  max-width: 560px;
}

/* ── STEP 1: PROMPT ── */
.prompt-examples-label {
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: #94A3B8;
  margin-bottom: 12px;
}

.prompt-chips {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 24px;
}

.prompt-chip {
  padding: 8px 16px;
  background: #FFFFFF;
  border: 1px solid #E2E8F0;
  border-radius: 100px;
  font-size: 13px;
  color: #475569;
  cursor: pointer;
  transition: all 0.2s;
  font-family: 'Geist', sans-serif;
}

.prompt-chip:hover {
  border-color: #2563EB;
  color: #2563EB;
  background: #EFF6FF;
}

.prompt-chip.selected {
  background: #EFF6FF;
  border-color: #2563EB;
  color: #2563EB;
  font-weight: 600;
}

.textarea-wrapper {
  background: #FFFFFF;
  border: 1px solid #E2E8F0;
  border-radius: 16px;
  overflow: hidden;
  margin-bottom: 24px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
  transition: border-color 0.2s;
}

.textarea-wrapper:focus-within {
  border-color: #2563EB;
  box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.08);
}

.prompt-textarea {
  width: 100%;
  padding: 20px;
  border: none;
  outline: none;
  font-size: 15px;
  line-height: 1.6;
  color: #0F172A;
  resize: none;
  font-family: 'Geist', sans-serif;
  min-height: 140px;
  background: transparent;
}

.prompt-textarea::placeholder {
  color: #94A3B8;
}

.textarea-footer {
  display: flex;
  justify-content: flex-end;
  padding: 8px 16px 12px;
  border-top: 1px solid #F1F5F9;
}

.char-count {
  font-size: 12px;
  color: #94A3B8;
}

/* ── BUTTONS ── */
.action-btn {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  padding: 16px 32px;
  background: linear-gradient(135deg, #1D4ED8, #2563EB);
  color: white;
  border: none;
  border-radius: 14px;
  font-family: 'Geist', sans-serif;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.25s;
  box-shadow: 0 4px 16px rgba(37, 99, 235, 0.3), 0 1px 2px rgba(0, 0, 0, 0.05);
  width: 100%;
  justify-content: center;
}

.action-btn:hover:not(.disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(37, 99, 235, 0.4);
}

.action-btn.disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.action-btn.loading {
  opacity: 0.8;
  cursor: wait;
}

.btn-icon {
  font-size: 16px;
}

.btn-spinner {
  width: 18px;
  height: 18px;
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

.action-btn-secondary {
  padding: 14px 28px;
  background: white;
  border: 1px solid #E2E8F0;
  border-radius: 14px;
  font-family: 'Geist', sans-serif;
  font-size: 14px;
  font-weight: 500;
  color: #475569;
  cursor: pointer;
  transition: all 0.2s;
}

.action-btn-secondary:hover {
  border-color: #94A3B8;
  color: #0F172A;
}

.step-actions {
  display: flex;
  gap: 12px;
  margin-top: 28px;
}

.step-actions .action-btn {
  flex: 1;
  width: auto;
}

/* ── STEP 2: PREVIEW ── */
.store-preview-card {
  background: white;
  border: 1px solid #E2E8F0;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.06);
  margin-bottom: 24px;
}

.preview-browser-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  background: #F8FAFC;
  border-bottom: 1px solid #E2E8F0;
}

.pbb-dots {
  display: flex;
  gap: 6px;
}

.pbb-dots span {
  width: 10px;
  height: 10px;
  border-radius: 50%;
}

.pbb-dots span:nth-child(1) {
  background: #EF4444;
}

.pbb-dots span:nth-child(2) {
  background: #F59E0B;
}

.pbb-dots span:nth-child(3) {
  background: #22C55E;
}

.pbb-url {
  flex: 1;
  background: rgba(0, 0, 0, 0.04);
  border-radius: 6px;
  padding: 5px 12px;
  font-size: 12px;
  color: #64748B;
}

.pbb-url strong {
  color: #2563EB;
}

.pbb-badge {
  padding: 3px 10px;
  border-radius: 100px;
  font-size: 11px;
  font-weight: 700;
}

.preview-hero {
  padding: 32px 24px;
}

.ph-logo {
  font-family: 'Instrument Serif', serif;
  font-size: 26px;
  font-weight: 400;
  color: white;
}

.ph-slogan {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.8);
  margin-top: 4px;
}

.preview-products {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
  padding: 16px;
}

.preview-product-card {
  background: white;
  border-radius: 10px;
  overflow: hidden;
  border: 1px solid rgba(0, 0, 0, 0.06);
}

.ppc-img {
  height: 80px;
}

.ppc-body {
  padding: 10px;
}

.ppc-name {
  font-size: 12px;
  font-weight: 600;
  color: #0F172A;
  margin-bottom: 4px;
}

.ppc-price {
  font-size: 13px;
  font-weight: 700;
  margin-bottom: 8px;
}

.ppc-btn {
  width: 100%;
  padding: 6px;
  border: none;
  border-radius: 6px;
  font-size: 11px;
  font-weight: 600;
  color: white;
  cursor: default;
}

.theme-meta-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
  margin-bottom: 4px;
}

.tm-item {
  background: white;
  border: 1px solid #E2E8F0;
  border-radius: 14px;
  padding: 16px;
}

.tm-full {
  grid-column: 1 / -1;
}

.tm-label {
  display: block;
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: #94A3B8;
  margin-bottom: 8px;
}

.tm-val {
  font-size: 15px;
  font-weight: 600;
  color: #0F172A;
}

.tm-badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 100px;
  font-size: 12px;
  font-weight: 700;
}

.tm-colors {
  display: flex;
  gap: 12px;
}

.color-swatch {
  flex: 1;
  height: 48px;
  border-radius: 10px;
  display: flex;
  align-items: flex-end;
  padding: 6px 8px;
}

.color-swatch.bordered {
  border: 1px solid #E2E8F0;
}

.color-swatch span {
  font-size: 10px;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.9);
}

.color-swatch.bordered span {
  color: #64748B;
}

/* ── STEP 3: FORM ── */
.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
  margin-bottom: 28px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.fg-full {
  grid-column: 1 / -1;
}

.form-label {
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.06em;
  text-transform: uppercase;
  color: #64748B;
  display: flex;
  align-items: center;
  gap: 8px;
}

.label-count {
  font-size: 11px;
  background: #EFF6FF;
  color: #2563EB;
  padding: 2px 8px;
  border-radius: 100px;
  text-transform: none;
  letter-spacing: 0;
}

.form-input, .form-textarea {
  padding: 12px 16px;
  background: white;
  border: 1px solid #E2E8F0;
  border-radius: 12px;
  font-family: 'Geist', sans-serif;
  font-size: 14px;
  color: #0F172A;
  outline: none;
  transition: all 0.2s;
  width: 100%;
}

.form-input:focus, .form-textarea:focus {
  border-color: #2563EB;
  box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.08);
}

.form-textarea {
  resize: none;
}

.color-pickers {
  display: flex;
  gap: 20px;
}

.cp-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
}

.cp-input {
  width: 52px;
  height: 52px;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  padding: 2px;
  background: white;
  box-shadow: 0 0 0 1px #E2E8F0;
}

.cp-label {
  font-size: 11px;
  color: #64748B;
  font-weight: 600;
}

.live-preview {
  border-radius: 14px;
  overflow: hidden;
  border: 1px solid #E2E8F0;
}

.lp-header {
  padding: 20px 18px;
}

.lp-name {
  display: block;
  font-family: 'Instrument Serif', serif;
  font-size: 18px;
  color: white;
}

.lp-slogan {
  display: block;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.75);
  margin-top: 3px;
}

.lp-body {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
  padding: 14px;
}

.lp-card {
  background: white;
  border-radius: 8px;
  overflow: hidden;
}

.lp-img {
  height: 56px;
}

.lp-info {
  padding: 8px;
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.lp-prodname {
  height: 8px;
  background: #E2E8F0;
  border-radius: 4px;
}

.lp-price {
  height: 8px;
  width: 50%;
  border-radius: 4px;
}

.products-selector {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.product-chip {
  padding: 9px 18px;
  background: white;
  border: 1px solid #E2E8F0;
  border-radius: 100px;
  font-family: 'Geist', sans-serif;
  font-size: 13px;
  color: #475569;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  gap: 6px;
}

.product-chip:hover {
  border-color: #2563EB;
  color: #2563EB;
}

.product-chip.selected {
  color: white;
  font-weight: 600;
}

.pc-check {
  opacity: 0;
  font-size: 11px;
}

.product-chip.selected .pc-check {
  opacity: 1;
}

/* ── RESPONSIVE ── */
@media (max-width: 768px) {
  .wizard-sidebar {
    display: none;
  }

  .wizard-main {
    padding: 24px 20px;
  }

  .form-grid {
    grid-template-columns: 1fr;
  }

  .theme-meta-grid {
    grid-template-columns: 1fr;
  }

  .preview-products {
    grid-template-columns: repeat(3, 1fr);
  }
}
</style>
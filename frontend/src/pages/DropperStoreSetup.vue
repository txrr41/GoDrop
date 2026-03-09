<template>
  <div class="setup-root">
    <template v-if="!storeStore.hasStore">
      <div class="wizard-layout">

        <!-- ── SIDEBAR ───────────────────────────────── -->
        <aside class="wizard-sidebar">
          <div class="sidebar-orb"></div>
          <div class="sidebar-dots"></div>

          <div class="sidebar-brand">
            <svg width="28" height="28" viewBox="0 0 32 32" fill="none">
              <rect width="32" height="32" rx="8" fill="#2563EB"/>
              <path d="M8 22L16 10L24 22H8Z" fill="white" fill-opacity=".9"/>
              <circle cx="16" cy="16" r="3" fill="white"/>
            </svg>
            <span>GoDrop Studio</span>
          </div>

          <div class="steps-track">
            <div
                v-for="(s, i) in wizardSteps" :key="i"
                class="track-item"
                :class="{ active: step === i + 1, done: step > i + 1 }"
            >
              <div class="track-connector" v-if="i > 0"></div>
              <div class="track-row">
                <div class="track-icon">
                  <svg v-if="step > i + 1" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
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
          </div>

          <div class="sidebar-footer">
            <div class="sf-pill">
              <span class="sf-dot"></span>
              IA pronta para criar seu tema
            </div>
            <div class="sf-card">
              <div class="sf-icon">⚡</div>
              <p class="sf-text">Loja ativa em menos de 2 minutos com nossa IA</p>
            </div>
          </div>
        </aside>

        <!-- ── MAIN ──────────────────────────────────── -->
        <main class="wizard-main">

          <!-- STEP 1: Prompt -->
          <div v-if="step === 1" class="step-panel fade-in">
            <div class="step-header">
              <span class="step-eyebrow">Passo 1 de 3</span>
              <h1 class="step-title">Descreva sua <em class="title-em">visão</em></h1>
              <p class="step-desc">Conte para nossa IA sobre o nicho, público e estilo que você imagina. Quanto mais detalhes, melhor o resultado.</p>
            </div>

            <div class="chips-label">Inspirações rápidas</div>
            <div class="prompt-chips">
              <button
                  v-for="ex in promptExamples" :key="ex"
                  class="prompt-chip"
                  :class="{ active: aiPrompt === ex }"
                  @click="aiPrompt = ex"
              >{{ ex }}</button>
            </div>

            <div class="textarea-wrap" :class="{ focused: textareaFocused }">
              <textarea
                  v-model="aiPrompt"
                  class="prompt-textarea"
                  placeholder="Ex: Quero uma loja de suplementos fitness com visual moderno e vibrante, focada em jovens atletas que buscam performance..."
                  maxlength="300"
                  @focus="textareaFocused = true"
                  @blur="textareaFocused = false"
              ></textarea>
              <div class="textarea-bottom">
                <span class="char-count" :class="{ warn: aiPrompt.length > 250 }">{{ aiPrompt.length }}/300</span>
              </div>
            </div>

            <button
                class="action-btn"
                :class="{ 'action-btn--loading': storeStore.aiLoading, 'action-btn--disabled': aiPrompt.trim().length < 20 }"
                :disabled="aiPrompt.trim().length < 20 || storeStore.aiLoading"
                @click="generateTheme"
            >
              <span v-if="storeStore.aiLoading" class="btn-spinner"></span>
              <svg v-else width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/>
              </svg>
              {{ storeStore.aiLoading ? 'Gerando tema...' : 'Gerar com Inteligência Artificial' }}
            </button>
          </div>

          <!-- STEP 2: Preview do tema -->
          <div v-if="step === 2 && storeStore.aiTheme" class="step-panel fade-in">
            <div class="step-header">
              <span class="step-eyebrow">Passo 2 de 3</span>
              <h1 class="step-title">Seu tema foi <em class="title-em">criado</em></h1>
              <p class="step-desc">{{ storeStore.aiTheme.reasoning }}</p>
            </div>

            <div class="preview-card">
              <div class="preview-topbar">
                <div class="ptb-dots"><span class="ptb-dot red"></span><span class="ptb-dot yellow"></span><span class="ptb-dot green"></span></div>
                <div class="ptb-url">godrop.com.br/loja/<strong>{{ slugify(storeStore.aiTheme.storeName) }}</strong></div>
                <span class="ptb-theme-tag" :style="`color:${storeStore.aiTheme.primaryColor}; background:${storeStore.aiTheme.primaryColor}18`">
                  {{ storeStore.aiTheme.theme }}
                </span>
              </div>

              <div class="preview-hero-band" :style="`background: linear-gradient(135deg, ${storeStore.aiTheme.primaryColor}, ${storeStore.aiTheme.secondaryColor})`">
                <div class="phb-name">{{ storeStore.aiTheme.storeName }}</div>
                <div class="phb-slogan">{{ storeStore.aiTheme.slogan }}</div>
              </div>

              <div class="preview-products-row" :style="`background:${storeStore.aiTheme.backgroundColor}`">
                <div v-for="i in 3" :key="i" class="ppr-card">
                  <div class="ppr-img" :style="`background:${storeStore.aiTheme.primaryColor}15`"></div>
                  <div class="ppr-body">
                    <div class="ppr-name">Produto {{ i }}</div>
                    <div class="ppr-price" :style="`color:${storeStore.aiTheme.primaryColor}`">R$ 99,90</div>
                    <button class="ppr-btn" :style="`background:${storeStore.aiTheme.primaryColor}`">Comprar</button>
                  </div>
                </div>
              </div>
            </div>

            <div class="meta-grid">
              <div class="meta-item">
                <span class="meta-label">Nome gerado</span>
                <span class="meta-val">{{ storeStore.aiTheme.storeName }}</span>
              </div>
              <div class="meta-item">
                <span class="meta-label">Estilo</span>
                <span class="meta-tag" :style="`color:${storeStore.aiTheme.primaryColor}; background:${storeStore.aiTheme.primaryColor}15`">{{ storeStore.aiTheme.theme }}</span>
              </div>
              <div class="meta-item meta-full">
                <span class="meta-label">Slogan</span>
                <span class="meta-val">{{ storeStore.aiTheme.slogan }}</span>
              </div>
              <div class="meta-item meta-full">
                <span class="meta-label">Paleta de cores</span>
                <div class="meta-colors">
                  <div class="color-pill" :style="`background:${storeStore.aiTheme.primaryColor}`">
                    <span>Primária</span>
                  </div>
                  <div class="color-pill" :style="`background:${storeStore.aiTheme.secondaryColor}`">
                    <span>Secundária</span>
                  </div>
                  <div class="color-pill bordered" :style="`background:${storeStore.aiTheme.backgroundColor}; color:#374151; border:1.5px solid #E2E8F0`">
                    <span>Fundo</span>
                  </div>
                </div>
              </div>
            </div>

            <div class="step-actions">
              <button class="btn-ghost-dark" @click="step = 1">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M1 4v6h6"/><path d="M3.51 15a9 9 0 1 0 2.13-9.36L1 10"/></svg>
                Gerar outro tema
              </button>
              <button class="action-btn" @click="applyAndNext">
                Usar este tema
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M5 12h14M12 5l7 7-7 7"/></svg>
              </button>
            </div>
          </div>

          <!-- STEP 3: Personalizar e publicar -->
          <div v-if="step === 3" class="step-panel fade-in">
            <div class="step-header">
              <span class="step-eyebrow">Passo 3 de 3</span>
              <h1 class="step-title">Ajuste e <em class="title-em">publique</em></h1>
              <p class="step-desc">Personalize os últimos detalhes e selecione os produtos que vão aparecer na sua loja.</p>
            </div>

            <div class="form-cols">
              <div class="form-group">
                <label class="form-label">Nome da loja <span class="req">*</span></label>
                <input v-model="form.storeName" class="form-input" placeholder="Ex: MaxForce Store"/>
              </div>
              <div class="form-group">
                <label class="form-label">Slogan</label>
                <input v-model="form.slogan" class="form-input" placeholder="Ex: Potência máxima."/>
              </div>
              <div class="form-group form-full">
                <label class="form-label">Descrição da loja</label>
                <textarea v-model="form.description" class="form-input form-textarea" rows="2" placeholder="Breve descrição para seus clientes..."></textarea>
              </div>

              <div class="form-group form-full">
                <label class="form-label">Cores da loja</label>
                <div class="color-pickers">
                  <div class="cp-item">
                    <input type="color" v-model="form.primaryColor" class="cp-swatch"/>
                    <div class="cp-info">
                      <span class="cp-name">Primária</span>
                      <span class="cp-hex">{{ form.primaryColor }}</span>
                    </div>
                  </div>
                  <div class="cp-item">
                    <input type="color" v-model="form.secondaryColor" class="cp-swatch"/>
                    <div class="cp-info">
                      <span class="cp-name">Secundária</span>
                      <span class="cp-hex">{{ form.secondaryColor }}</span>
                    </div>
                  </div>
                  <div class="cp-item">
                    <input type="color" v-model="form.backgroundColor" class="cp-swatch"/>
                    <div class="cp-info">
                      <span class="cp-name">Fundo</span>
                      <span class="cp-hex">{{ form.backgroundColor }}</span>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Live preview -->
              <div class="form-group form-full">
                <label class="form-label">Preview ao vivo</label>
                <div class="live-preview" :style="`background:${form.backgroundColor}`">
                  <div class="lp-header" :style="`background: linear-gradient(135deg, ${form.primaryColor}, ${form.secondaryColor})`">
                    <span class="lp-name">{{ form.storeName || 'Minha Loja' }}</span>
                    <span class="lp-slogan">{{ form.slogan || 'Slogan da sua loja' }}</span>
                  </div>
                  <div class="lp-body">
                    <div v-for="i in 4" :key="i" class="lp-card">
                      <div class="lp-img" :style="`background:${form.primaryColor}15`"></div>
                      <div class="lp-info">
                        <div class="lp-line long"></div>
                        <div class="lp-line short" :style="`background:${form.primaryColor}60`"></div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Seleção de produtos -->
              <div class="form-group form-full">
                <label class="form-label">
                  Produtos na loja
                  <span class="label-count" :style="`color:${form.primaryColor}; background:${form.primaryColor}15`">
                    {{ form.productIds.length }} selecionados
                  </span>
                </label>
                <div class="products-selector">
                  <button
                      v-for="product in productStore.products"
                      :key="product.id"
                      class="product-chip"
                      :class="{ 'product-chip--on': form.productIds.includes(product.id) }"
                      :style="form.productIds.includes(product.id) ? `background:${form.primaryColor}; border-color:${form.primaryColor}; color:white` : ''"
                      @click="toggleProduct(product.id)"
                  >
                    <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3">
                      <polyline points="20 6 9 17 4 12"/>
                    </svg>
                    {{ product.nome }}
                  </button>
                </div>
              </div>
            </div>

            <button
                class="action-btn"
                :class="{ 'action-btn--loading': storeStore.loading, 'action-btn--disabled': !form.storeName }"
                :disabled="!form.storeName || storeStore.loading"
                @click="createStore"
            >
              <span v-if="storeStore.loading" class="btn-spinner"></span>
              <svg v-else width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M22 2L11 13"/><path d="M22 2L15 22 11 13 2 9l20-7z"/>
              </svg>
              {{ storeStore.loading ? 'Criando sua loja...' : 'Publicar minha loja' }}
            </button>
          </div>

        </main>
      </div>
    </template>

    <template v-else>
      <DropperStoreDashboard/>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useStoreStore }   from '../stores/store'
import { useProductStore } from '../stores/product'
import DropperStoreDashboard from '../pages/DropperStoreDashboard.vue'

const storeStore   = useStoreStore()
const productStore = useProductStore()

const step           = ref(1)
const aiPrompt       = ref('')
const textareaFocused = ref(false)

const wizardSteps = [
  { label: 'Descreva sua loja', sub: 'Conte sua visão para a IA' },
  { label: 'Tema gerado',       sub: 'Revise o resultado da IA'  },
  { label: 'Publique',          sub: 'Ajuste e ative sua loja'   },
]

const form = ref({
  storeName: '', slogan: '', description: '',
  primaryColor: '#2563EB', secondaryColor: '#0EA5E9',
  backgroundColor: '#F8FAFC', theme: 'MODERN', productIds: [],
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
  Object.assign(form.value, {
    storeName: ai.storeName, slogan: ai.slogan,
    primaryColor: ai.primaryColor, secondaryColor: ai.secondaryColor,
    backgroundColor: ai.backgroundColor, theme: ai.theme,
    description: ai.description || '',
  })
  step.value = 3
}

async function createStore() {
  await storeStore.createStore({ ...form.value, aiPrompt: aiPrompt.value })
}

onMounted(async () => {
  await storeStore.fetchMyStore()
  await productStore.fetchProducts()
})
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=DM+Serif+Display:ital@0;1&display=swap');

*, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

/* ── ROOT ─────────────────────────────────────────── */
.setup-root {
  --blue-50:  #EFF6FF;
  --blue-100: #DBEAFE;
  --blue-200: #BFDBFE;
  --blue-600: #2563EB;
  --blue-700: #1D4ED8;
  --text:     #0F172A;
  --text-2:   #475569;
  --text-3:   #94A3B8;
  --border:   #E2E8F0;

  font-family: 'Plus Jakarta Sans', sans-serif;
  background: var(--blue-50);
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 32px 24px;
}

/* ── WIZARD LAYOUT ────────────────────────────────── */
.wizard-layout {
  display: flex;
  background: white;
  border-radius: 24px;
  box-shadow: 0 24px 64px rgba(37,99,235,.12), 0 4px 16px rgba(0,0,0,.04);
  overflow: hidden;
  max-width: 1080px;
  width: 100%;
  border: 1.5px solid var(--border);
}

/* ── SIDEBAR ──────────────────────────────────────── */
.wizard-sidebar {
  width: 300px;
  flex-shrink: 0;
  background: #060C1A;
  padding: 40px 32px;
  display: flex;
  flex-direction: column;
  position: relative;
  overflow: hidden;
}

.sidebar-orb {
  position: absolute;
  top: -80px; left: -80px;
  width: 320px; height: 320px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(37,99,235,.25) 0%, transparent 70%);
  pointer-events: none;
}

.sidebar-dots {
  position: absolute;
  inset: 0;
  background-image: radial-gradient(rgba(255,255,255,.07) 1px, transparent 1px);
  background-size: 24px 24px;
  pointer-events: none;
}

.sidebar-brand {
  display: flex;
  align-items: center;
  gap: 10px;
  font-family: 'DM Serif Display', serif;
  font-size: 18px;
  font-weight: 400;
  color: white;
  letter-spacing: -.01em;
  margin-bottom: 48px;
  position: relative;
  z-index: 1;
}

.steps-track {
  display: flex;
  flex-direction: column;
  gap: 0;
  position: relative;
  z-index: 1;
  flex: 1;
}

.track-item { position: relative; }

.track-connector {
  width: 2px;
  height: 24px;
  background: rgba(255,255,255,.08);
  margin-left: 17px;
  margin-bottom: 0;
}

.track-item.done .track-connector { background: rgba(34,197,94,.4); }
.track-item.active .track-connector { background: rgba(37,99,235,.4); }

.track-row {
  display: flex;
  align-items: flex-start;
  gap: 14px;
  padding: 6px 0;
  opacity: .35;
  transition: opacity .3s, transform .3s;
}
.track-item.active .track-row { opacity: 1; transform: translateX(4px); }
.track-item.done  .track-row { opacity: .7; }

.track-icon {
  width: 34px; height: 34px;
  border-radius: 50%;
  background: rgba(255,255,255,.06);
  border: 1.5px solid rgba(255,255,255,.1);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 700;
  color: rgba(255,255,255,.6);
  flex-shrink: 0;
  transition: all .3s;
}
.track-item.active .track-icon {
  background: var(--blue-600);
  border-color: var(--blue-600);
  color: white;
  box-shadow: 0 0 20px rgba(37,99,235,.5);
}
.track-item.done .track-icon {
  background: #22c55e;
  border-color: #22c55e;
  color: white;
}

.track-text { display: flex; flex-direction: column; gap: 3px; }
.track-label { font-size: 14px; font-weight: 700; color: white; letter-spacing: -.01em; }
.track-sub   { font-size: 11.5px; color: rgba(255,255,255,.4); font-weight: 500; }

.sidebar-footer {
  position: relative;
  z-index: 1;
  margin-top: 32px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.sf-pill {
  display: inline-flex;
  align-items: center;
  gap: 7px;
  font-size: 11px;
  font-weight: 700;
  color: rgba(255,255,255,.5);
  letter-spacing: .08em;
  text-transform: uppercase;
}

.sf-dot {
  width: 6px; height: 6px;
  border-radius: 50%;
  background: #22c55e;
  box-shadow: 0 0 8px #22c55e;
  animation: sfPulse 2s ease-in-out infinite;
}
@keyframes sfPulse {
  0%,100% { opacity: 1; }
  50%     { opacity: .4; }
}

.sf-card {
  display: flex;
  align-items: center;
  gap: 12px;
  background: rgba(255,255,255,.05);
  border: 1px solid rgba(255,255,255,.08);
  border-radius: 14px;
  padding: 16px;
}
.sf-icon { font-size: 18px; flex-shrink: 0; }
.sf-text { font-size: 12.5px; color: rgba(255,255,255,.5); line-height: 1.5; font-weight: 500; }

/* ── MAIN ─────────────────────────────────────────── */
.wizard-main {
  flex: 1;
  padding: 52px 48px;
  background: white;
  overflow-y: auto;
  max-height: 90vh;
}

.fade-in { animation: fadeIn .35s ease both; }
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(12px); }
  to   { opacity: 1; transform: translateY(0); }
}

/* ── STEP HEADER ──────────────────────────────────── */
.step-eyebrow {
  display: inline-block;
  font-size: 11px;
  font-weight: 700;
  letter-spacing: .14em;
  text-transform: uppercase;
  color: var(--blue-600);
  background: var(--blue-50);
  border: 1px solid var(--blue-200);
  padding: 4px 12px;
  border-radius: 100px;
  margin-bottom: 14px;
}

.step-title {
  font-family: 'DM Serif Display', serif;
  font-size: clamp(28px, 3vw, 38px);
  font-weight: 400;
  color: var(--text);
  letter-spacing: -.03em;
  line-height: 1.1;
  margin-bottom: 10px;
}
.title-em { font-style: italic; color: var(--blue-600); }

.step-desc {
  font-size: 15px;
  color: var(--text-2);
  line-height: 1.65;
  margin-bottom: 32px;
  max-width: 520px;
}

/* ── STEP 1 ───────────────────────────────────────── */
.chips-label {
  font-size: 11px;
  font-weight: 700;
  letter-spacing: .1em;
  text-transform: uppercase;
  color: var(--text-3);
  margin-bottom: 10px;
}

.prompt-chips {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 20px;
}

.prompt-chip {
  padding: 7px 14px;
  background: var(--blue-50);
  border: 1.5px solid var(--blue-100);
  border-radius: 100px;
  font: 600 13px 'Plus Jakarta Sans', sans-serif;
  color: var(--blue-600);
  cursor: pointer;
  transition: all .15s;
}
.prompt-chip:hover { border-color: var(--blue-400); background: var(--blue-100); }
.prompt-chip.active { background: var(--blue-600); color: white; border-color: var(--blue-600); }

.textarea-wrap {
  border: 1.5px solid var(--border);
  border-radius: 16px;
  overflow: hidden;
  transition: border-color .2s, box-shadow .2s;
  margin-bottom: 24px;
  background: #FAFBFC;
}
.textarea-wrap.focused {
  border-color: var(--blue-600);
  box-shadow: 0 0 0 4px rgba(37,99,235,.08);
  background: white;
}

.prompt-textarea {
  width: 100%;
  min-height: 130px;
  padding: 18px 20px;
  background: transparent;
  border: none;
  outline: none;
  font: 500 15px 'Plus Jakarta Sans', sans-serif;
  color: var(--text);
  resize: none;
  line-height: 1.6;
}
.prompt-textarea::placeholder { color: var(--text-3); }

.textarea-bottom {
  padding: 10px 18px;
  border-top: 1px solid var(--border);
  display: flex;
  justify-content: flex-end;
  background: #F8FAFC;
}

.char-count {
  font-size: 12px;
  font-weight: 600;
  color: var(--text-3);
  transition: color .15s;
}
.char-count.warn { color: #EF4444; }

/* ── ACTION BTN ───────────────────────────────────── */
.action-btn {
  display: inline-flex;
  align-items: center;
  gap: 9px;
  padding: 14px 28px;
  background: var(--blue-600);
  color: white;
  border: none;
  border-radius: 13px;
  font: 700 15px 'Plus Jakarta Sans', sans-serif;
  cursor: pointer;
  box-shadow: 0 4px 20px rgba(37,99,235,.35);
  transition: background .15s, transform .1s, box-shadow .2s;
  letter-spacing: .01em;
}
.action-btn:hover { background: var(--blue-700); transform: translateY(-2px); box-shadow: 0 8px 28px rgba(37,99,235,.45); }
.action-btn:active { transform: scale(.98); }
.action-btn--loading { opacity: .8; cursor: not-allowed; transform: none !important; }
.action-btn--disabled { background: var(--border); color: var(--text-3); box-shadow: none; cursor: not-allowed; }

.btn-spinner {
  width: 16px; height: 16px;
  border: 2px solid rgba(255,255,255,.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin .7s linear infinite;
  flex-shrink: 0;
}
@keyframes spin { to { transform: rotate(360deg); } }

.btn-ghost-dark {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 13px 20px;
  background: transparent;
  color: var(--text-2);
  border: 1.5px solid var(--border);
  border-radius: 13px;
  font: 600 14px 'Plus Jakarta Sans', sans-serif;
  cursor: pointer;
  transition: all .15s;
}
.btn-ghost-dark:hover { border-color: var(--blue-200); color: var(--blue-600); background: var(--blue-50); }

.step-actions {
  display: flex;
  gap: 12px;
  align-items: center;
  margin-top: 24px;
}

/* ── STEP 2: PREVIEW ──────────────────────────────── */
.preview-card {
  border: 1.5px solid var(--border);
  border-radius: 16px;
  overflow: hidden;
  margin-bottom: 20px;
  box-shadow: 0 4px 20px rgba(37,99,235,.07);
}

.preview-topbar {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 11px 14px;
  background: #F8FAFC;
  border-bottom: 1px solid var(--border);
}
.ptb-dots { display: flex; gap: 5px; }
.ptb-dot  { width: 10px; height: 10px; border-radius: 50%; }
.ptb-dot.red    { background: #FF5F57; }
.ptb-dot.yellow { background: #FEBC2E; }
.ptb-dot.green  { background: #28C840; }
.ptb-url {
  flex: 1; text-align: center;
  font-size: 11.5px; color: var(--text-3); font-weight: 500;
  background: white; border: 1px solid var(--border);
  padding: 4px 12px; border-radius: 6px;
}
.ptb-url strong { color: var(--blue-600); font-weight: 700; }
.ptb-theme-tag {
  font-size: 10px; font-weight: 800;
  padding: 3px 10px; border-radius: 20px;
  letter-spacing: .08em; text-transform: uppercase;
  flex-shrink: 0;
}

.preview-hero-band {
  padding: 24px 28px;
  transition: background .4s;
}
.phb-name   { font-family: 'DM Serif Display', serif; font-size: 22px; color: white; letter-spacing: -.02em; }
.phb-slogan { font-size: 13px; color: rgba(255,255,255,.8); margin-top: 4px; font-weight: 500; }

.preview-products-row {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
  padding: 16px;
  transition: background .4s;
}
.ppr-card { background: white; border-radius: 10px; overflow: hidden; border: 1px solid rgba(0,0,0,.05); }
.ppr-img  { height: 72px; transition: background .4s; }
.ppr-body { padding: 10px; }
.ppr-name { font-size: 12px; font-weight: 700; color: var(--text); margin-bottom: 4px; }
.ppr-price{ font-size: 13px; font-weight: 800; margin-bottom: 8px; transition: color .4s; }
.ppr-btn  { width: 100%; padding: 6px; border: none; border-radius: 6px; color: white; font: 700 11px 'Plus Jakarta Sans', sans-serif; cursor: pointer; transition: background .4s; }

/* ── META GRID ────────────────────────────────────── */
.meta-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1px;
  background: var(--border);
  border: 1.5px solid var(--border);
  border-radius: 14px;
  overflow: hidden;
  margin-bottom: 24px;
}
.meta-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 14px 18px;
  background: white;
}
.meta-full { grid-column: 1 / -1; }
.meta-label { font-size: 11px; font-weight: 700; text-transform: uppercase; letter-spacing: .1em; color: var(--text-3); flex-shrink: 0; }
.meta-val   { font-size: 14px; font-weight: 700; color: var(--text); text-align: right; }
.meta-tag   { font-size: 11px; font-weight: 800; padding: 3px 10px; border-radius: 20px; letter-spacing: .06em; }
.meta-colors { display: flex; gap: 8px; }
.color-pill {
  display: flex;
  align-items: center;
  padding: 5px 12px 5px 8px;
  border-radius: 100px;
  gap: 7px;
  font-size: 11px;
  font-weight: 700;
  color: white;
}
.color-pill::before {
  content: '';
  width: 12px; height: 12px;
  border-radius: 50%;
  background: rgba(255,255,255,.4);
  flex-shrink: 0;
}

/* ── STEP 3: FORM ─────────────────────────────────── */
.form-cols {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
  margin-bottom: 28px;
}
.form-full { grid-column: 1 / -1; }

.form-group { display: flex; flex-direction: column; gap: 7px; }

.form-label {
  font-size: 12.5px;
  font-weight: 700;
  color: var(--text);
  letter-spacing: -.01em;
  display: flex;
  align-items: center;
  gap: 8px;
}
.req { color: var(--blue-600); }

.label-count {
  font-size: 11px;
  font-weight: 800;
  padding: 2px 9px;
  border-radius: 100px;
  letter-spacing: .04em;
}

.form-input {
  padding: 12px 16px;
  border: 1.5px solid var(--border);
  border-radius: 12px;
  font: 500 14px 'Plus Jakarta Sans', sans-serif;
  color: var(--text);
  background: #FAFBFC;
  outline: none;
  transition: border-color .15s, box-shadow .15s, background .15s;
  resize: none;
}
.form-input:focus {
  border-color: var(--blue-600);
  background: white;
  box-shadow: 0 0 0 4px rgba(37,99,235,.08);
}
.form-input::placeholder { color: var(--text-3); }
.form-textarea { min-height: 72px; }

/* color pickers */
.color-pickers { display: flex; gap: 10px; }
.cp-item {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 14px;
  border: 1.5px solid var(--border);
  border-radius: 12px;
  background: #FAFBFC;
  cursor: pointer;
  transition: border-color .15s;
}
.cp-item:hover { border-color: var(--blue-200); }

.cp-swatch {
  width: 32px; height: 32px;
  border: none; border-radius: 8px;
  padding: 0; cursor: pointer;
  flex-shrink: 0;
}
.cp-info { display: flex; flex-direction: column; gap: 1px; }
.cp-name { font-size: 11.5px; font-weight: 700; color: var(--text); }
.cp-hex  { font-size: 10.5px; color: var(--text-3); font-weight: 600; font-family: monospace; }

/* live preview */
.live-preview {
  border: 1.5px solid var(--border);
  border-radius: 14px;
  overflow: hidden;
  transition: background .3s;
}
.lp-header {
  padding: 16px 20px;
  transition: background .3s;
  display: flex;
  flex-direction: column;
  gap: 3px;
}
.lp-name   { font-family: 'DM Serif Display', serif; font-size: 16px; color: white; }
.lp-slogan { font-size: 11.5px; color: rgba(255,255,255,.75); font-weight: 500; }
.lp-body {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 8px;
  padding: 12px;
}
.lp-card { background: white; border-radius: 8px; overflow: hidden; border: 1px solid rgba(0,0,0,.04); }
.lp-img  { height: 52px; transition: background .3s; }
.lp-info { padding: 7px 8px; display: flex; flex-direction: column; gap: 4px; }
.lp-line { height: 6px; border-radius: 3px; background: #E2E8F0; }
.lp-line.long  { width: 80%; }
.lp-line.short { width: 50%; transition: background .3s; }

/* products selector */
.products-selector {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  padding: 16px;
  background: #FAFBFC;
  border: 1.5px solid var(--border);
  border-radius: 14px;
  max-height: 200px;
  overflow-y: auto;
}

.product-chip {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 7px 14px;
  background: white;
  border: 1.5px solid var(--border);
  border-radius: 100px;
  font: 600 13px 'Plus Jakarta Sans', sans-serif;
  color: var(--text-2);
  cursor: pointer;
  transition: all .15s;
}
.product-chip svg { opacity: 0; transition: opacity .15s; flex-shrink: 0; }
.product-chip:hover { border-color: var(--blue-200); color: var(--text); }
.product-chip--on svg { opacity: 1; }

/* ── RESPONSIVE ───────────────────────────────────── */
@media (max-width: 860px) {
  .wizard-layout { flex-direction: column; }
  .wizard-sidebar {
    width: 100%;
    padding: 24px;
  }
  .steps-track { flex-direction: row; gap: 0; }
  .track-connector { width: 24px; height: 2px; margin: 17px 0 0; }
  .track-text { display: none; }
  .wizard-main { padding: 32px 24px; max-height: none; }
}

@media (max-width: 640px) {
  .form-cols { grid-template-columns: 1fr; }
  .color-pickers { flex-direction: column; }
  .preview-products-row { grid-template-columns: 1fr 1fr; }
  .lp-body { grid-template-columns: repeat(2,1fr); }
}
</style>
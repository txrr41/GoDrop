<template>
  <div class="dropper-page">

    <!-- HERO SECTION -->
    <section class="hero">
      <div class="hero-bg-shape"></div>
      <div class="hero-dots"></div>

      <nav class="hero-nav">
        <img :src="LogoDrop" alt="Go Drop" class="nav-logo" />
        <a href="/home" class="nav-back">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M19 12H5M12 5l-7 7 7 7"/></svg>
          Voltar para a loja
        </a>
      </nav>

      <div class="hero-inner">
        <div class="hero-left">
          <div class="hero-pill">
            <span class="pill-dot"></span>
            Go Drop Partners
          </div>

          <h1 class="hero-h1">
            Escale suas vendas<br/>
            <em class="hero-em">sem investir em estoque.</em>
          </h1>

          <p class="hero-p">
            O programa definitivo para empreendedores. Cadastre seu CNPJ, conecte-se ao nosso centro de distribuição e transforme seu volume de vendas em até <strong>30% de desconto real</strong>.
          </p>

          <div class="hero-btns">
            <button class="btn-solid" @click="scrollToForm">
              Iniciar meu cadastro
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M5 12h14M12 5l7 7-7 7"/></svg>
            </button>
            <button class="btn-outline" @click="scrollToTiers">
              Conhecer os Níveis
            </button>
          </div>

          <div class="hero-trust">
            <div class="trust-stat">
              <span class="ts-num">2.4k+</span>
              <span class="ts-lbl">Empresas parceiras</span>
            </div>
            <div class="trust-sep"></div>
            <div class="trust-stat">
              <span class="ts-num">R$ 0</span>
              <span class="ts-lbl">Custo de adesão</span>
            </div>
            <div class="trust-sep"></div>
            <div class="trust-stat">
              <span class="ts-num">Auto</span>
              <span class="ts-lbl">Cálculo de nível</span>
            </div>
          </div>
        </div>

        <div class="hero-right">
          <div class="glass-panel main-dashboard">
            <div class="dash-header">
              <div class="dash-title">Performance Mensal</div>
              <div class="dash-badge">Nível Ouro</div>
            </div>
            <div class="dash-body">
              <div class="dash-stat">
                <span class="stat-lbl">Volume de Vendas</span>
                <span class="stat-val">R$ 42.500,00</span>
                <span class="stat-growth">+14.2% vs mês anterior</span>
              </div>
              <div class="dash-chart">
                <div class="chart-bar" v-for="(h, i) in [40, 65, 45, 80, 55, 90, 100]" :key="i" :style="{height: h + '%', animationDelay: (i * 0.1) + 's'}"></div>
              </div>
            </div>
          </div>

          <div class="floating-stat fs-1">
            <div class="fs-icon blue-bg">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/></svg>
            </div>
            <div class="fs-info">
              <span class="fs-val">100% Sincronizado</span>
              <span class="fs-lbl">Estoque em tempo real</span>
            </div>
          </div>

          <div class="floating-stat fs-2">
            <div class="fs-icon green-bg">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2"><line x1="12" y1="1" x2="12" y2="23"/><path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"/></svg>
            </div>
            <div class="fs-info">
              <span class="fs-val">Margem de Lucro</span>
              <span class="fs-lbl">Aumentou 15%</span>
            </div>
          </div>
        </div>
      </div>

      <div class="scroll-hint">
        <div class="scroll-line"></div>
        <span>Role para baixo</span>
      </div>
    </section>

    <!-- COMO FUNCIONA -->
    <section class="how-section" id="how">
      <div class="section-wrap">
        <div class="section-header">
          <p class="section-eyebrow">Aceleração de negócios</p>
          <h2 class="section-title">Operação otimizada em <span class="text-blue">4 passos</span></h2>
          <p class="section-subtitle">Simples, rápido e sem burocracia. Do cadastro à primeira venda em minutos.</p>
        </div>

        <div class="steps-grid">
          <div class="step-line"></div>
          <div class="step-card" v-for="(s, i) in steps" :key="i">
            <div class="step-num-big">0{{ i + 1 }}</div>
            <div class="step-icon-box">{{ s.icon }}</div>
            <h3 class="step-title">{{ s.title }}</h3>
            <p class="step-desc">{{ s.desc }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- NÍVEIS -->
    <section class="tiers-section" id="tiers" ref="tiersSection">
      <div class="section-wrap">
        <div class="section-header">
          <p class="section-eyebrow">Estrutura de benefícios</p>
          <h2 class="section-title">Crescimento <span class="text-blue">escalonável</span></h2>
          <p class="section-subtitle">Um modelo meritocrático: seu volume de vendas dita sua margem de lucro.</p>
        </div>

        <div class="tiers-grid">
          <div class="tier-card" v-for="tier in tiers" :key="tier.name"
               :class="{ 'tier-featured': tier.featured }">
            <div v-if="tier.featured" class="featured-banner">Plano Recomendado</div>

            <div class="tier-header">
              <div class="tier-icon-wrapper" :style="{ color: tier.color, background: tier.lightBg }">
                <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/></svg>
              </div>
              <h3 class="tier-name" :style="{ color: tier.color }">{{ tier.name }}</h3>
            </div>

            <div class="tier-discount">
              <span class="discount-num">{{ tier.discount }}</span>
              <span class="discount-pct">%</span>
            </div>
            <div class="tier-lbl">desconto em todas as compras</div>

            <div class="tier-threshold">
              Meta: <strong>{{ tier.threshold }}</strong>/mês
            </div>

            <div class="tier-divider"></div>

            <ul class="tier-perks">
              <li v-for="perk in tier.perks" :key="perk">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#2563EB" stroke-width="2.5"><path d="M20 6L9 17l-5-5"/></svg>
                <span>{{ perk }}</span>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </section>

    <!-- FORMULÁRIO -->
    <section class="form-section" id="form" ref="formSection">
      <div class="section-wrap">
        <div class="form-wrapper">
          <div class="form-left">
            <p class="section-eyebrow">Cadastro de Parceiro</p>
            <h2 class="section-title">Inicie sua jornada<br/><span class="text-blue">como Parceiro</span></h2>
            <p class="section-subtitle">
              O processo de homologação é instantâneo. Insira os dados da sua empresa e libere o acesso ao nosso ecossistema de logística e vendas.
            </p>

            <div class="benefits-compact">
              <div class="bc-item" v-for="b in benefitsCompact" :key="b.title">
                <div class="bc-icon">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#2563EB" stroke-width="2.5"><polyline points="20 6 9 17 4 12"/></svg>
                </div>
                <div>
                  <h4>{{ b.title }}</h4>
                  <p>{{ b.desc }}</p>
                </div>
              </div>
            </div>
          </div>

          <div class="form-right">
            <div class="form-card">
              <div v-if="!submitted" class="signup-form">

                <div v-if="!auth.isLogged" class="form-not-logged">
                  <div class="lock-icon">
                    <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
                  </div>
                  <h3>Autenticação Necessária</h3>
                  <p>Acesse sua conta corporativa Go Drop para vincular seu CNPJ ao programa de parceiros.</p>
                  <button class="btn-solid full-width" @click="goToLogin">
                    Fazer Login na Plataforma
                  </button>
                </div>

                <div v-else>
                  <div class="active-user-badge">
                    <div class="aub-avatar">{{ auth.user?.name?.charAt(0) || 'G' }}</div>
                    <div class="aub-info">
                      <span class="aub-name">{{ auth.user?.name || 'Usuário Autenticado' }}</span>
                      <span class="aub-email">{{ auth.user?.email || 'email@empresa.com' }}</span>
                    </div>
                  </div>

                  <div v-if="auth.user?.role === 'DROPPER'" class="already-dropper">
                    <div class="success-ring">
                      <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="#16A34A" stroke-width="2"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/></svg>
                    </div>
                    <h3>Conta Homologada</h3>
                    <p>Seu CNPJ já está vinculado ao programa de Parceiros. Boas vendas!</p>
                  </div>

                  <div v-else class="form-fields">
                    <div class="input-group">
                      <label class="input-label">CNPJ da Empresa</label>
                      <div class="input-wrapper" :class="{ 'is-valid': cnpjStatus === 'valid', 'is-invalid': cnpjStatus === 'invalid' }">
                        <input
                            :value="cnpj"
                            type="text"
                            placeholder="00.000.000/0001-00"
                            class="form-input"
                            maxlength="18"
                            @input="formatCNPJ"
                        />
                        <div class="input-status-icon">
                          <svg v-if="cnpjStatus === 'valid'" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#16A34A" stroke-width="2"><polyline points="20 6 9 17 4 12"/></svg>
                          <svg v-if="cnpjStatus === 'invalid'" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#DC2626" stroke-width="2"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
                        </div>
                      </div>
                      <p v-if="cnpjError" class="input-error">{{ cnpjError }}</p>
                    </div>

                    <div class="input-group">
                      <label class="input-label">Nome Fantasia / Marca</label>
                      <input v-model="storeName" type="text" placeholder="Ex: Acme Corp Digital" class="form-input" />
                    </div>

                    <div class="input-group">
                      <label class="input-label">WhatsApp Comercial</label>
                      <input v-model="whatsapp" type="text" placeholder="(00) 00000-0000" class="form-input" @input="formatPhone" maxlength="15" />
                    </div>

                    <label class="checkbox-label">
                      <input type="checkbox" v-model="acceptedTerms" class="checkbox-input" />
                      <span class="checkbox-box">
                        <svg width="11" height="11" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="3"><polyline points="20 6 9 17 4 12"/></svg>
                      </span>
                      <span class="checkbox-text">Declaro que li e concordo com os <a href="#">Termos e Condições Comerciais</a>.</span>
                    </label>

                    <button
                        class="btn-solid btn-submit full-width"
                        :disabled="loading || !acceptedTerms || cnpj.length < 18"
                        @click="handleSubmit"
                    >
                      <span v-if="loading" class="spinner"></span>
                      <span v-else>Validar e Ativar Conta</span>
                    </button>
                  </div>
                </div>
              </div>

              <div v-else class="form-success">
                <div class="success-shield">
                  <svg width="36" height="36" viewBox="0 0 24 24" fill="none" stroke="#2563EB" stroke-width="1.5"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/><polyline points="9 12 11 14 15 10"/></svg>
                </div>
                <h3 class="success-title">Empresa Homologada</h3>
                <p class="success-desc">Bem-vindo ao Go Drop Partners. Sua infraestrutura de e-commerce acaba de subir de nível.</p>
                <div class="success-card">
                  <div class="sc-row">
                    <span class="sc-lbl">CNPJ Registrado</span>
                    <span class="sc-val">{{ cnpj }}</span>
                  </div>
                  <div class="sc-row">
                    <span class="sc-lbl">Nível Inicial</span>
                    <span class="sc-val bronze-text">Bronze (5% OFF)</span>
                  </div>
                </div>
                <button class="btn-solid full-width" @click="goToLogin">
                  Acessar Dashboard
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useAuthStore } from '../stores/auth'
import LogoDrop from '../assets/LogoDrop.png'
import api from "../api/api.js"
import { useDropperStore } from "../stores/dropper.js"

const auth = useAuthStore()
const dropperStore = useDropperStore()

const cnpj = ref('')
const storeName = ref('')
const whatsapp = ref('')
const acceptedTerms = ref(false)
const cnpjStatus = ref(null)
const cnpjError = ref('')
const loading = ref(false)
const submitted = ref(false)

const tiersSection = ref(null)
const formSection = ref(null)

const steps = [
  { icon: '🏢', title: 'Criação da Conta',      desc: 'Registro na plataforma Go Drop com foco em dados corporativos.' },
  { icon: '✅', title: 'Homologação de CNPJ',   desc: 'Validação fiscal automática e imediata pela nossa API.' },
  { icon: '📦', title: 'Seleção de Portfólio',  desc: 'Acesso ao catálogo de produtos com preço de custo para revenda.' },
  { icon: '🚀', title: 'Operação e Escala',     desc: 'Venda para seus clientes; nós cuidamos da separação, embalagem e envio.' },
]

const tiers = [
  {
    key: 'bronze', name: 'Bronze', color: '#92400E', lightBg: 'rgba(180,83,9,0.08)', discount: 5, threshold: 'R$ 0',
    featured: false,
    perks: ['Desconto base ativo', 'Acesso ao catálogo atacadista', 'Suporte ticket standard']
  },
  {
    key: 'silver', name: 'Silver', color: '#475569', lightBg: 'rgba(71,85,105,0.08)', discount: 10, threshold: 'R$ 5.000',
    featured: false,
    perks: ['Selo de parceiro verificado', 'Suporte prioritário (SLA 12h)', 'Dashboard analítico avançado']
  },
  {
    key: 'gold', name: 'Gold', color: '#D97706', lightBg: 'rgba(217,119,6,0.08)', discount: 15, threshold: 'R$ 20.000',
    featured: true,
    perks: ['Acesso early-bird a estoques', 'Canal direto no WhatsApp', 'Comissionamento em cross-sell']
  },
  {
    key: 'platinum', name: 'Platinum', color: '#2563EB', lightBg: 'rgba(37,99,235,0.08)', discount: 22, threshold: 'R$ 60.000',
    featured: false,
    perks: ['Account Manager dedicado', 'White-label nas embalagens', 'Integração via API REST']
  },
  {
    key: 'diamond', name: 'Diamond', color: '#7C3AED', lightBg: 'rgba(124,58,237,0.08)', discount: 30, threshold: 'R$ 150.000',
    featured: false,
    perks: ['Condições B2B customizadas', 'Participação no conselho parceiro', 'Fundo de Co-marketing']
  },
]

const benefitsCompact = [
  { title: 'Zero investimento em infra',  desc: 'Armazenamento, picking e packing por nossa conta.' },
  { title: 'Rastreabilidade B2B',         desc: 'Logística transparente para você e seu cliente final.' },
  { title: 'Integração tecnológica',      desc: 'Sincronização de estoque contra quebras de venda.' },
]

const scrollToForm   = () => formSection.value?.scrollIntoView({ behavior: 'smooth' })
const scrollToTiers  = () => tiersSection.value?.scrollIntoView({ behavior: 'smooth' })
const goToLogin      = () => window.location.href = '/home'

const formatCNPJ = (e) => {
  let v = e.target.value.replace(/\D/g, '').slice(0, 14)
  if (v.length > 12) v = `${v.slice(0,2)}.${v.slice(2,5)}.${v.slice(5,8)}/${v.slice(8,12)}-${v.slice(12)}`
  else if (v.length > 8) v = `${v.slice(0,2)}.${v.slice(2,5)}.${v.slice(5,8)}/${v.slice(8)}`
  else if (v.length > 5) v = `${v.slice(0,2)}.${v.slice(2,5)}.${v.slice(5)}`
  else if (v.length > 2) v = `${v.slice(0,2)}.${v.slice(2)}`
  cnpj.value = v
  if (v.length === 18) validateCNPJ(v)
  else cnpjStatus.value = null
}

const formatPhone = (e) => {
  let v = e.target.value.replace(/\D/g, '')
  v = v.length <= 10
      ? v.replace(/(\d{2})(\d{4})(\d{4})/, '($1) $2-$3')
      : v.replace(/(\d{2})(\d{5})(\d{4})/, '($1) $2-$3')
  whatsapp.value = v
}

const validateCNPJ = (value) => {
  const nums = value.replace(/\D/g, '')
  if (nums.length !== 14) { cnpjStatus.value = 'invalid'; return }
  if (/^(\d)\1+$/.test(nums)) { cnpjStatus.value = 'invalid'; cnpjError.value = 'CNPJ inválido'; return }
  let sum = 0, pos = 5
  for (let i = 0; i < 12; i++) { sum += parseInt(nums[i]) * pos--; if (pos < 2) pos = 9 }
  let r = sum % 11 < 2 ? 0 : 11 - (sum % 11)
  if (r !== parseInt(nums[12])) { cnpjStatus.value = 'invalid'; cnpjError.value = 'CNPJ inválido'; return }
  sum = 0; pos = 6
  for (let i = 0; i < 13; i++) { sum += parseInt(nums[i]) * pos--; if (pos < 2) pos = 9 }
  r = sum % 11 < 2 ? 0 : 11 - (sum % 11)
  if (r !== parseInt(nums[13])) { cnpjStatus.value = 'invalid'; cnpjError.value = 'CNPJ inválido'; return }
  cnpjStatus.value = 'valid'
  cnpjError.value = ''
}

const handleSubmit = async () => {
  if (cnpjStatus.value !== 'valid') { cnpjError.value = 'Verifique o CNPJ informado'; return }
  loading.value = true
  try {
    await api.post('/api/dropper/register', {
      cnpj: cnpj.value,
      storeName: storeName.value,
      whatsapp: whatsapp.value
    })
    await auth.fetchUser()
    await dropperStore.fetchProfile()
    submitted.value = true
  } catch (err) {
    cnpjError.value = err.response?.data?.message || 'Erro ao processar cadastro.'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=DM+Serif+Display:ital@0;1&display=swap');

*, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

/* ── TOKENS ───────────────────────────────────── */
.dropper-page {
  --blue-50:  #EFF6FF;
  --blue-100: #DBEAFE;
  --blue-200: #BFDBFE;
  --blue-400: #60A5FA;
  --blue-600: #2563EB;
  --blue-700: #1D4ED8;
  --text:     #0F172A;
  --text-2:   #475569;
  --text-3:   #94A3B8;
  --border:   #E2E8F0;
  --bg:       #FFFFFF;
  --bg-2:     #F8FAFF;
  --success:  #16A34A;
  --error:    #DC2626;

  font-family: 'Plus Jakarta Sans', sans-serif;
  background: var(--bg);
  color: var(--text);
  overflow-x: hidden;
}

.text-blue { color: var(--blue-600); }

/* ── HERO ─────────────────────────────────────── */
.hero {
  position: relative;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background: var(--bg);
}

.hero-bg-shape {
  position: absolute;
  top: -200px; right: -200px;
  width: 800px; height: 800px;
  border-radius: 50%;
  background: radial-gradient(circle, var(--blue-50) 0%, transparent 70%);
  pointer-events: none;
}

.hero-dots {
  position: absolute;
  inset: 0;
  background-image: radial-gradient(var(--blue-200) 1px, transparent 1px);
  background-size: 36px 36px;
  mask-image: radial-gradient(ellipse 60% 80% at 80% 40%, black 20%, transparent 70%);
  -webkit-mask-image: radial-gradient(ellipse 60% 80% at 80% 40%, black 20%, transparent 70%);
  opacity: .45;
  pointer-events: none;
}

.hero-nav {
  position: relative;
  z-index: 10;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 24px 48px;
  background: rgba(255,255,255,.92);
  backdrop-filter: blur(16px);
  border-bottom: 1px solid var(--border);
}

.nav-logo {
  height: 30px;
  /* remove o filtro dark — agora o logo aparece como é */
}

.nav-back {
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--text-2);
  text-decoration: none;
  font-size: 14px;
  font-weight: 600;
  transition: color .15s;
}
.nav-back:hover { color: var(--blue-600); }

.hero-inner {
  flex: 1;
  position: relative;
  z-index: 2;
  max-width: 1200px;
  margin: 0 auto;
  padding: 80px 48px 60px;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 64px;
  align-items: center;
  width: 100%;
}

.hero-pill {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 6px 14px;
  background: var(--blue-50);
  border: 1px solid var(--blue-200);
  border-radius: 100px;
  font-size: 12px;
  font-weight: 700;
  color: var(--blue-600);
  letter-spacing: .04em;
  text-transform: uppercase;
  margin-bottom: 24px;
}

.pill-dot {
  width: 6px; height: 6px;
  border-radius: 50%;
  background: var(--blue-600);
  box-shadow: 0 0 0 3px rgba(37,99,235,.2);
  animation: pillPulse 2s ease-in-out infinite;
}
@keyframes pillPulse {
  0%,100% { box-shadow: 0 0 0 3px rgba(37,99,235,.2); }
  50%     { box-shadow: 0 0 0 6px rgba(37,99,235,.08); }
}

.hero-h1 {
  font-family: 'DM Serif Display', serif;
  font-size: clamp(40px, 5vw, 68px);
  font-weight: 400;
  line-height: 1.08;
  letter-spacing: -.03em;
  color: var(--text);
  margin-bottom: 20px;
}

.hero-em {
  font-style: italic;
  color: var(--blue-600);
}

.hero-p {
  font-size: 17px;
  line-height: 1.72;
  color: var(--text-2);
  max-width: 460px;
  margin-bottom: 36px;
}
.hero-p strong { color: var(--text); font-weight: 700; }

.hero-btns {
  display: flex;
  gap: 12px;
  margin-bottom: 48px;
  flex-wrap: wrap;
}

.btn-solid {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 9px;
  padding: 13px 26px;
  background: var(--blue-600);
  color: white;
  border: none;
  border-radius: 12px;
  font: 700 15px 'Plus Jakarta Sans', sans-serif;
  cursor: pointer;
  box-shadow: 0 4px 20px rgba(37,99,235,.35);
  transition: background .15s, transform .1s, box-shadow .2s;
}
.btn-solid:hover:not(:disabled) {
  background: var(--blue-700);
  transform: translateY(-2px);
  box-shadow: 0 8px 28px rgba(37,99,235,.45);
}
.btn-solid:disabled { opacity: .6; cursor: not-allowed; }
.btn-solid.full-width { width: 100%; }
.btn-solid.btn-large  { padding: 16px 32px; font-size: 16px; border-radius: 14px; }

.btn-outline {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 13px 22px;
  background: transparent;
  color: var(--text-2);
  border: 1.5px solid var(--border);
  border-radius: 12px;
  font: 600 15px 'Plus Jakarta Sans', sans-serif;
  cursor: pointer;
  transition: border-color .15s, color .15s, background .15s;
}
.btn-outline:hover {
  border-color: var(--blue-200);
  color: var(--blue-600);
  background: var(--blue-50);
}

.hero-trust {
  display: flex;
  align-items: center;
  gap: 24px;
  padding: 20px 24px;
  background: var(--bg-2);
  border: 1px solid var(--border);
  border-radius: 14px;
  width: fit-content;
}
.trust-stat  { display: flex; flex-direction: column; gap: 2px; }
.ts-num      { font-family: 'DM Serif Display', serif; font-size: 26px; font-weight: 400; color: var(--text); letter-spacing: -.02em; }
.ts-lbl      { font-size: 11px; font-weight: 600; color: var(--text-3); text-transform: uppercase; letter-spacing: .06em; }
.trust-sep   { width: 1px; height: 36px; background: var(--border); }

/* ── HERO RIGHT — dashboard mockup ───────────── */
.hero-right {
  position: relative;
}

.glass-panel {
  background: white;
  border: 1.5px solid var(--border);
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(37,99,235,.1), 0 4px 12px rgba(0,0,0,.04);
  padding: 28px;
}

.main-dashboard { width: 100%; }

.dash-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}
.dash-title { font-size: 13px; font-weight: 600; color: var(--text-3); }
.dash-badge { font-size: 12px; font-weight: 700; color: #D97706; background: rgba(217,119,6,.1); padding: 4px 10px; border-radius: 6px; }

.dash-body { display: flex; flex-direction: column; gap: 20px; }
.dash-stat { display: flex; flex-direction: column; gap: 4px; }
.stat-lbl    { font-size: 12px; color: var(--text-3); font-weight: 600; }
.stat-val    { font-family: 'DM Serif Display', serif; font-size: 32px; color: var(--text); letter-spacing: -.02em; }
.stat-growth { font-size: 13px; color: var(--success); font-weight: 600; }

.dash-chart {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  height: 72px;
  gap: 6px;
}
.chart-bar {
  flex: 1;
  background: var(--blue-600);
  border-radius: 4px 4px 0 0;
  opacity: .75;
  transform-origin: bottom;
  animation: growBar 1s ease-out forwards;
}
@keyframes growBar { from { transform: scaleY(0); } to { transform: scaleY(1); } }

.floating-stat {
  position: absolute;
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 14px 18px;
  background: white;
  border: 1.5px solid var(--border);
  border-radius: 14px;
  box-shadow: 0 8px 24px rgba(37,99,235,.1);
}
.fs-1 { top: 8%; right: -28px; animation: floatY 6s ease-in-out infinite; }
.fs-2 { bottom: 12%; left: -36px; animation: floatY 5s ease-in-out infinite reverse; }
@keyframes floatY { 0%,100% { transform: translateY(0); } 50% { transform: translateY(-12px); } }

.fs-icon {
  width: 38px; height: 38px;
  border-radius: 10px;
  display: flex; align-items: center; justify-content: center;
}
.blue-bg  { background: linear-gradient(135deg, var(--blue-400), var(--blue-600)); }
.green-bg { background: linear-gradient(135deg, #34D399, #16A34A); }
.fs-info  { display: flex; flex-direction: column; gap: 2px; }
.fs-val   { font-size: 13px; font-weight: 700; color: var(--text); }
.fs-lbl   { font-size: 11.5px; color: var(--text-3); font-weight: 500; }

/* ── SCROLL HINT ──────────────────────────────── */
.scroll-hint {
  position: relative;
  z-index: 2;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding-bottom: 36px;
  color: var(--text-3);
  font-size: 11px;
  font-weight: 600;
  letter-spacing: .1em;
  text-transform: uppercase;
}
.scroll-line {
  width: 1px; height: 40px;
  background: linear-gradient(var(--blue-200), transparent);
  animation: scrollPulse 2s ease-in-out infinite;
}
@keyframes scrollPulse { 0%,100% { opacity: 1; } 50% { opacity: .3; } }

/* ── SECTIONS SHARED ──────────────────────────── */
section {
  padding: 120px 0;
  border-top: 1px solid var(--border);
}

.section-wrap {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 48px;
}

.section-header {
  text-align: center;
  margin-bottom: 64px;
}

.section-eyebrow {
  display: inline-flex;
  padding: 5px 14px;
  background: var(--blue-50);
  border: 1px solid var(--blue-200);
  border-radius: 100px;
  font-size: 11px;
  font-weight: 700;
  color: var(--blue-600);
  letter-spacing: .12em;
  text-transform: uppercase;
  margin-bottom: 16px;
}

.section-title {
  font-family: 'DM Serif Display', serif;
  font-size: clamp(30px, 3.5vw, 48px);
  font-weight: 400;
  line-height: 1.1;
  letter-spacing: -.03em;
  color: var(--text);
  margin-bottom: 12px;
}

.section-subtitle {
  font-size: 16px;
  color: var(--text-2);
  line-height: 1.7;
  max-width: 600px;
  margin: 0 auto;
}

/* ── COMO FUNCIONA ────────────────────────────── */
.how-section { background: var(--bg-2); }

.steps-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 2px;
  background: var(--border);
  border-radius: 20px;
  overflow: hidden;
  border: 1.5px solid var(--border);
  position: relative;
}
.step-line { display: none; }

.step-card {
  background: white;
  padding: 40px 28px;
  transition: background .2s;
}
.step-card:hover { background: var(--blue-50); }

.step-num-big {
  font-family: 'DM Serif Display', serif;
  font-size: 56px;
  font-weight: 400;
  color: var(--blue-100);
  line-height: 1;
  margin-bottom: 12px;
  letter-spacing: -.04em;
}
.step-icon-box {
  font-size: 32px;
  margin-bottom: 16px;
  line-height: 1;
}
.step-title { font-size: 16px; font-weight: 800; color: var(--text); margin-bottom: 8px; letter-spacing: -.01em; }
.step-desc  { font-size: 13.5px; color: var(--text-2); line-height: 1.65; }

/* ── NÍVEIS ───────────────────────────────────── */
.tiers-section { background: white; }

.tiers-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 14px;
  align-items: end;
}

.tier-card {
  background: var(--bg-2);
  border: 1.5px solid var(--border);
  border-radius: 18px;
  padding: 28px 18px;
  position: relative;
  display: flex;
  flex-direction: column;
  transition: transform .2s, box-shadow .2s, border-color .2s;
}
.tier-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(37,99,235,.1);
  border-color: var(--blue-200);
}

.tier-featured {
  border-color: #D97706;
  background: white;
  box-shadow: 0 8px 32px rgba(217,119,6,.12);
  transform: translateY(-12px);
}
.tier-featured:hover { transform: translateY(-18px); }

.featured-banner {
  position: absolute;
  top: -12px; left: 50%;
  transform: translateX(-50%);
  background: #D97706;
  color: white;
  font-size: 10px;
  font-weight: 800;
  text-transform: uppercase;
  letter-spacing: .06em;
  padding: 4px 14px;
  border-radius: 100px;
  white-space: nowrap;
  box-shadow: 0 3px 10px rgba(217,119,6,.35);
}

.tier-header     { display: flex; align-items: center; gap: 12px; margin-bottom: 20px; }
.tier-icon-wrapper {
  width: 38px; height: 38px;
  border-radius: 10px;
  display: flex; align-items: center; justify-content: center;
}
.tier-name   { font-size: 18px; font-weight: 800; margin: 0; letter-spacing: -.01em; }

.tier-discount   { display: flex; align-items: baseline; gap: 2px; }
.discount-num    { font-family: 'DM Serif Display', serif; font-size: 40px; font-weight: 400; color: var(--text); line-height: 1; }
.discount-pct    { font-size: 18px; font-weight: 700; color: var(--text-3); }
.tier-lbl        { font-size: 12px; color: var(--text-3); margin-bottom: 16px; font-weight: 500; }

.tier-threshold  { font-size: 12px; color: var(--text-2); }
.tier-threshold strong { color: var(--text); font-weight: 700; }

.tier-divider    { height: 1px; background: var(--border); margin: 18px 0; }

.tier-perks      { list-style: none; display: flex; flex-direction: column; gap: 10px; }
.tier-perks li   { display: flex; align-items: flex-start; gap: 10px; font-size: 12.5px; color: var(--text-2); line-height: 1.4; font-weight: 500; }

/* ── FORMULÁRIO ───────────────────────────────── */
.form-section { background: var(--bg-2); }

.form-wrapper {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 80px;
  align-items: start;
}

.form-left .section-eyebrow  { display: inline-flex; }
.form-left .section-title    { text-align: left; }
.form-left .section-subtitle { text-align: left; margin: 0 0 40px; max-width: 100%; }

.benefits-compact  { display: flex; flex-direction: column; gap: 20px; }
.bc-item           { display: flex; gap: 14px; }
.bc-icon {
  width: 30px; height: 30px;
  border-radius: 8px;
  background: var(--blue-50);
  border: 1px solid var(--blue-100);
  display: flex; align-items: center; justify-content: center;
  flex-shrink: 0;
}
.bc-item h4 { font-size: 14px; font-weight: 700; color: var(--text); margin-bottom: 3px; }
.bc-item p  { font-size: 13px; color: var(--text-2); line-height: 1.5; }

.form-card {
  background: white;
  border: 1.5px solid var(--border);
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 12px 40px rgba(37,99,235,.08), 0 2px 8px rgba(0,0,0,.04);
}

.form-not-logged { text-align: center; }
.lock-icon {
  width: 60px; height: 60px;
  border-radius: 16px;
  background: var(--bg-2);
  border: 1.5px solid var(--border);
  display: flex; align-items: center; justify-content: center;
  margin: 0 auto 20px;
  color: var(--text-3);
}
.form-not-logged h3 { font-size: 18px; font-weight: 800; margin-bottom: 10px; color: var(--text); }
.form-not-logged p  { font-size: 14px; color: var(--text-2); margin-bottom: 28px; line-height: 1.6; }

.active-user-badge {
  display: flex; align-items: center; gap: 14px;
  padding: 14px 16px;
  background: var(--bg-2);
  border: 1.5px solid var(--border);
  border-radius: 12px;
  margin-bottom: 28px;
}
.aub-avatar {
  width: 38px; height: 38px;
  border-radius: 8px;
  background: var(--blue-600);
  color: white;
  display: flex; align-items: center; justify-content: center;
  font-weight: 700; font-size: 15px;
}
.aub-info  { display: flex; flex-direction: column; gap: 2px; }
.aub-name  { font-weight: 700; font-size: 14px; color: var(--text); }
.aub-email { font-size: 12px; color: var(--text-3); }

.already-dropper { text-align: center; padding: 16px 0; }
.success-ring {
  width: 52px; height: 52px;
  border-radius: 50%;
  background: rgba(22,163,74,.08);
  border: 1.5px solid rgba(22,163,74,.2);
  display: flex; align-items: center; justify-content: center;
  margin: 0 auto 20px;
}
.already-dropper h3 { font-size: 18px; font-weight: 800; margin-bottom: 8px; color: var(--text); }
.already-dropper p  { font-size: 14px; color: var(--text-2); }

.form-fields { display: flex; flex-direction: column; gap: 18px; }
.input-group { display: flex; flex-direction: column; gap: 6px; }
.input-label { font-size: 12.5px; font-weight: 600; color: var(--text-2); text-transform: uppercase; letter-spacing: .04em; }

.input-wrapper { position: relative; display: flex; align-items: center; }
.form-input {
  width: 100%;
  background: var(--bg-2);
  border: 1.5px solid var(--border);
  border-radius: 10px;
  padding: 12px 44px 12px 14px;
  color: var(--text);
  font-family: 'Plus Jakarta Sans', sans-serif;
  font-size: 14.5px;
  transition: border-color .15s, box-shadow .15s;
}
.form-input:focus {
  outline: none;
  border-color: var(--blue-600);
  box-shadow: 0 0 0 3px rgba(37,99,235,.1);
  background: white;
}
.form-input::placeholder { color: var(--text-3); }
.is-valid   .form-input  { border-color: var(--success); }
.is-invalid .form-input  { border-color: var(--error); }

.input-status-icon { position: absolute; right: 14px; display: flex; }
.input-error       { font-size: 12px; color: var(--error); font-weight: 500; }

.checkbox-label {
  display: flex; align-items: flex-start; gap: 10px;
  cursor: pointer;
  margin: 4px 0 8px;
}
.checkbox-input { display: none; }
.checkbox-box {
  width: 18px; height: 18px;
  border-radius: 5px;
  border: 1.5px solid var(--border);
  background: var(--bg-2);
  display: flex; align-items: center; justify-content: center;
  flex-shrink: 0;
  transition: all .15s;
}
.checkbox-box svg { opacity: 0; transform: scale(.5); transition: all .15s; }
.checkbox-input:checked + .checkbox-box { background: var(--blue-600); border-color: var(--blue-600); }
.checkbox-input:checked + .checkbox-box svg { opacity: 1; transform: scale(1); }
.checkbox-text { font-size: 13px; color: var(--text-2); line-height: 1.5; }
.checkbox-text a { color: var(--blue-600); text-decoration: underline; font-weight: 600; }

.spinner {
  width: 18px; height: 18px;
  border: 2px solid rgba(255,255,255,.4);
  border-top-color: white;
  border-radius: 50%;
  animation: spin .8s linear infinite;
}
@keyframes spin { to { transform: rotate(360deg); } }

.form-success { text-align: center; }
.success-shield {
  width: 68px; height: 68px;
  border-radius: 18px;
  background: var(--blue-50);
  border: 1.5px solid var(--blue-200);
  display: flex; align-items: center; justify-content: center;
  margin: 0 auto 20px;
}
.success-title { font-size: 22px; font-weight: 800; color: var(--text); margin-bottom: 10px; }
.success-desc  { font-size: 14px; color: var(--text-2); margin-bottom: 28px; line-height: 1.65; }
.success-card  {
  background: var(--bg-2);
  border: 1.5px solid var(--border);
  border-radius: 12px;
  padding: 18px 20px;
  margin-bottom: 28px;
  display: flex; flex-direction: column; gap: 12px;
}
.sc-row   { display: flex; justify-content: space-between; align-items: center; }
.sc-lbl   { font-size: 12px; color: var(--text-3); font-weight: 600; text-transform: uppercase; letter-spacing: .04em; }
.sc-val   { font-size: 14px; font-weight: 700; color: var(--text); }
.bronze-text { color: #92400E; }

/* ── RESPONSIVE ───────────────────────────────── */
@media (max-width: 1100px) {
  .hero-right   { display: none; }
  .hero-inner   { grid-template-columns: 1fr; }
  .tiers-grid   { grid-template-columns: repeat(3, 1fr); }
}
@media (max-width: 900px) {
  .steps-grid   { grid-template-columns: 1fr; gap: 2px; }
  .tiers-grid   { grid-template-columns: repeat(2, 1fr); }
  .form-wrapper { grid-template-columns: 1fr; gap: 48px; }
  .section-wrap { padding: 0 24px; }
  .hero-inner   { padding: 60px 24px 40px; }
  .hero-nav     { padding: 20px 24px; }
}
@media (max-width: 640px) {
  .tiers-grid { grid-template-columns: 1fr; }
}
</style>
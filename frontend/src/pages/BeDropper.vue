<template>
  <div class="dropper-page">
    <!-- HERO SECTION -->
    <section class="hero">
      <div class="hero-grid-bg"></div>
      <div class="hero-glow"></div>

      <nav class="hero-nav">
        <img :src="LogoDrop" alt="Go Drop" class="nav-logo" />
        <a href="/home" class="nav-back">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M19 12H5M12 5l-7 7 7 7"/></svg>
          Voltar para a loja
        </a>
      </nav>

      <div class="hero-content">
        <div class="hero-eyebrow">
          <div class="eyebrow-badge">
            <span class="eyebrow-dot"></span>
            Go Drop Partners
          </div>
        </div>

        <h1 class="hero-title">
          Escale suas vendas<br />
          <span class="title-gradient">sem investir em estoque.</span>
        </h1>

        <p class="hero-desc">
          O programa definitivo para empreendedores. Cadastre seu CNPJ, conecte-se ao nosso centro de distribuição e transforme seu volume de vendas em até <strong>30% de desconto real</strong>.
        </p>

        <div class="hero-actions">
          <button class="btn-primary" @click="scrollToForm">
            Iniciar meu cadastro
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M5 12h14M12 5l7 7-7 7"/></svg>
          </button>
          <button class="btn-secondary" @click="scrollToTiers">
            Conhecer os Níveis
          </button>
        </div>

        <div class="hero-trust">
          <div class="trust-item">
            <span class="trust-num">2.4k+</span>
            <span class="trust-lbl">Empresas parceiras</span>
          </div>
          <div class="trust-sep"></div>
          <div class="trust-item">
            <span class="trust-num">R$ 0</span>
            <span class="trust-lbl">Custo de adesão</span>
          </div>
          <div class="trust-sep"></div>
          <div class="trust-item">
            <span class="trust-num">Automático</span>
            <span class="trust-lbl">Cálculo de nível</span>
          </div>
        </div>
      </div>

      <div class="hero-visual">
        <div class="visual-wrapper">
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

          <div class="glass-panel floating-stat fs-1">
            <div class="fs-icon blue-bg">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/></svg>
            </div>
            <div class="fs-info">
              <span class="fs-val">100% Sincronizado</span>
              <span class="fs-lbl">Estoque em tempo real</span>
            </div>
          </div>

          <div class="glass-panel floating-stat fs-2">
            <div class="fs-icon green-bg">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="12" y1="1" x2="12" y2="23"/><path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"/></svg>
            </div>
            <div class="fs-info">
              <span class="fs-val">Margem de Lucro</span>
              <span class="fs-lbl">Aumentou 15%</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- COMO FUNCIONA -->
    <section class="how">
      <div class="section-container">
        <div class="section-header">
          <p class="section-eyebrow">Aceleração de negócios</p>
          <h2 class="section-title">Operação otimizada em 4 passos</h2>
        </div>
        <div class="how-steps">
          <div class="step-line"></div>
          <div class="step" v-for="(s, i) in steps" :key="i">
            <div class="step-marker">
              <div class="step-num">0{{ i + 1 }}</div>
            </div>
            <h3 class="step-title">{{ s.title }}</h3>
            <p class="step-desc">{{ s.desc }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- NÍVEIS -->
    <section class="tiers" id="tiers" ref="tiersSection">
      <div class="section-container">
        <div class="section-header">
          <p class="section-eyebrow">Estrutura de benefícios</p>
          <h2 class="section-title">Crescimento escalonável</h2>
          <p class="section-subtitle">Um modelo meritocrático: seu volume de vendas dita sua margem de lucro.</p>
        </div>

        <div class="tiers-grid">
          <div class="tier-card" v-for="tier in tiers" :key="tier.name" :class="['tier-' + tier.key, { 'tier-featured': tier.featured }]">
            <div v-if="tier.featured" class="featured-banner">Plano Recomendado</div>

            <div class="tier-header">
              <div class="tier-icon-wrapper" :style="{ color: tier.color, background: tier.bg }">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/></svg>
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
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 6L9 17l-5-5"/></svg>
                <span>{{ perk }}</span>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </section>

    <!-- FORMULÁRIO -->
    <section class="form-section" id="form" ref="formSection">
      <div class="section-container">
        <div class="form-wrapper">
          <div class="form-left">
            <h2 class="form-title">Inicie sua jornada<br/>como Parceiro</h2>
            <p class="form-subtitle">
              O processo de homologação é instantâneo. Insira os dados da sua empresa abaixo e libere o acesso ao nosso ecossistema de logística e vendas imediatamente.
            </p>

            <div class="benefits-compact">
              <div class="bc-item" v-for="b in benefitsCompact" :key="b.title">
                <div class="bc-icon">
                  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="20 6 9 17 4 12"/></svg>
                </div>
                <div>
                  <h4>{{ b.title }}</h4>
                  <p>{{ b.desc }}</p>
                </div>
              </div>
            </div>
          </div>

          <div class="form-right">
            <div class="premium-form-card">
              <div v-if="!submitted" class="signup-form">

                <div v-if="!auth.isLogged" class="form-not-logged">
                  <div class="lock-icon">
                    <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
                  </div>
                  <h3>Autenticação Necessária</h3>
                  <p>Acesse sua conta corporativa Go Drop para vincular seu CNPJ ao programa de parceiros.</p>
                  <button class="btn-primary full-width" @click="goToLogin">
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
                      <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#10B981" stroke-width="2"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/></svg>
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
                          <svg v-if="cnpjStatus === 'valid'" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#10B981" stroke-width="2"><polyline points="20 6 9 17 4 12"/></svg>
                          <svg v-if="cnpjStatus === 'invalid'" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#EF4444" stroke-width="2"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
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
                        <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3"><polyline points="20 6 9 17 4 12"/></svg>
                      </span>
                      <span class="checkbox-text">Declaro que li e concordo com os <a href="#">Termos e Condições Comerciais</a>.</span>
                    </label>

                    <button
                        class="btn-primary btn-submit full-width"
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
                  <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/><polyline points="9 12 11 14 15 10"/></svg>
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

                <button class="btn-primary full-width" @click="goToLogin">
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
import { useAuthStore } from '../stores/auth' // Mantenha seus imports reais
import LogoDrop from '../assets/LogoDrop.png' // Mantenha seus imports reais
import api from "../api/api.js";
import {useDropperStore} from "../stores/dropper.js";// Mantenha seus imports reais

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
  { title: 'Criação da Conta', desc: 'Registro na plataforma Go Drop com foco em dados corporativos.' },
  { title: 'Homologação de CNPJ', desc: 'Validação fiscal automática e imediata pela nossa API.' },
  { title: 'Seleção de Portfólio', desc: 'Acesso ao catálogo de produtos com preço de custo para revenda.' },
  { title: 'Operação e Escala', desc: 'Venda para seus clientes; nós cuidamos da separação, embalagem e envio.' },
]

const tiers = [
  {
    key: 'bronze', name: 'Bronze', color: '#B08D57', bg: 'rgba(176, 141, 87, 0.1)', discount: 5, threshold: 'R$ 0',
    featured: false,
    perks: ['Desconto base ativo', 'Acesso ao catálogo atacadista', 'Suporte ticket standard']
  },
  {
    key: 'silver', name: 'Silver', color: '#9CA3AF', bg: 'rgba(156, 163, 175, 0.1)', discount: 10, threshold: 'R$ 5.000',
    featured: false,
    perks: ['Selo de parceiro verificado', 'Suporte prioritário (SLA 12h)', 'Dashboard analítico avançado']
  },
  {
    key: 'gold', name: 'Gold', color: '#FBBF24', bg: 'rgba(251, 191, 36, 0.1)', discount: 15, threshold: 'R$ 20.000',
    featured: true,
    perks: ['Acesso early-bird a estoques', 'Canal direto no WhatsApp', 'Comissionamento em cross-sell']
  },
  {
    key: 'platinum', name: 'Platinum', color: '#60A5FA', bg: 'rgba(96, 165, 250, 0.1)', discount: 22, threshold: 'R$ 60.000',
    featured: false,
    perks: ['Account Manager dedicado', 'White-label nas embalagens', 'Integração via API REST']
  },
  {
    key: 'diamond', name: 'Diamond', color: '#A78BFA', bg: 'rgba(167, 139, 250, 0.1)', discount: 30, threshold: 'R$ 150.000',
    featured: false,
    perks: ['Condições B2B customizadas', 'Participação no conselho parceiro', 'Fundo de Co-marketing']
  },
]

const benefitsCompact = [
  { title: 'Zero investimento em infra', desc: 'Armazenamento, picking e packing por nossa conta.' },
  { title: 'Rastreabilidade B2B', desc: 'Logística transparente para você e seu cliente final.' },
  { title: 'Integração tecnológica', desc: 'Sincronização de estoque contra quebras de venda.' },
]

const scrollToForm = () => formSection.value?.scrollIntoView({ behavior: 'smooth' })
const scrollToTiers = () => tiersSection.value?.scrollIntoView({ behavior: 'smooth' })
const goToLogin = () => window.location.href = '/home'

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

    // ✅ Busca o usuário atualizado (agora com role: DROPPER)
    await auth.fetchUser()
    // ✅ Carrega o perfil dropper (level BRONZE, cnpj, etc)
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
@import url('https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@500;600;700;800&family=Inter:wght@400;500;600&display=swap');

.dropper-page {
  --c-bg: #050505;
  --c-surface: #121212;
  --c-surface-hover: #1A1A1A;
  --c-border: rgba(255, 255, 255, 0.1);
  --c-border-strong: rgba(255, 255, 255, 0.2);
  --c-text: #FAFAFA;
  --c-muted: #A1A1AA;
  --c-primary: #3B82F6;
  --c-primary-hover: #2563EB;
  --c-primary-light: #60A5FA;
  --c-success: #10B981;
  --c-error: #EF4444;

  font-family: 'Inter', sans-serif;
  background: var(--c-bg);
  color: var(--c-text);
  min-height: 100vh;
  overflow-x: hidden;
  line-height: 1.5;
}

h1, h2, h3, h4, .font-display {
  font-family: 'Plus Jakarta Sans', sans-serif;
}

.section-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 32px;
}

/* --- HERO SECTION --- */
.hero {
  position: relative;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  padding-bottom: 80px;
  overflow: hidden;
}

.hero-grid-bg {
  position: absolute;
  inset: 0;
  background-image:
      linear-gradient(to right, rgba(255,255,255,0.03) 1px, transparent 1px),
      linear-gradient(to bottom, rgba(255,255,255,0.03) 1px, transparent 1px);
  background-size: 40px 40px;
  mask-image: radial-gradient(circle at center, black, transparent 80%);
  pointer-events: none;
}

.hero-glow {
  position: absolute;
  width: 600px;
  height: 600px;
  background: radial-gradient(circle, rgba(59, 130, 246, 0.15) 0%, transparent 60%);
  top: -100px;
  right: 10%;
  pointer-events: none;
  filter: blur(40px);
}

.hero-nav {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 24px 48px;
  position: relative;
  z-index: 10;
}

.nav-logo { height: 32px; filter: brightness(0) invert(1); }

.nav-back {
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--c-muted);
  text-decoration: none;
  font-size: 14px;
  font-weight: 500;
  transition: color 0.2s;
}
.nav-back:hover { color: var(--c-text); }

.hero-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 40px 48px;
  max-width: 650px;
  position: relative;
  z-index: 10;
}

.hero-eyebrow {
  margin-bottom: 24px;
}

.eyebrow-badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 6px 14px;
  background: rgba(59, 130, 246, 0.1);
  border: 1px solid rgba(59, 130, 246, 0.2);
  border-radius: 100px;
  font-size: 13px;
  font-weight: 600;
  color: var(--c-primary-light);
}

.eyebrow-dot {
  width: 6px; height: 6px;
  background: var(--c-primary-light);
  border-radius: 50%;
  box-shadow: 0 0 8px var(--c-primary-light);
}

.hero-title {
  font-size: clamp(36px, 5vw, 64px);
  font-weight: 800;
  line-height: 1.1;
  letter-spacing: -0.03em;
  margin: 0 0 24px;
}

.title-gradient {
  background: linear-gradient(135deg, #FFFFFF 0%, #A1A1AA 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.hero-desc {
  font-size: 18px;
  color: var(--c-muted);
  margin-bottom: 40px;
  max-width: 540px;
}
.hero-desc strong { color: var(--c-text); font-weight: 600; }

.hero-actions {
  display: flex;
  gap: 16px;
  margin-bottom: 60px;
}

.btn-primary {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  background: var(--c-primary);
  color: #FFFFFF;
  border: none;
  border-radius: 8px;
  padding: 14px 28px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  box-shadow: 0 4px 14px rgba(59, 130, 246, 0.25);
}
.btn-primary:hover:not(:disabled) {
  background: var(--c-primary-hover);
  transform: translateY(-1px);
  box-shadow: 0 6px 20px rgba(59, 130, 246, 0.4);
}
.btn-primary:disabled { opacity: 0.6; cursor: not-allowed; }
.btn-primary.full-width { width: 100%; }

.btn-secondary {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  background: rgba(255,255,255,0.05);
  color: var(--c-text);
  border: 1px solid var(--c-border);
  border-radius: 8px;
  padding: 14px 28px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
}
.btn-secondary:hover { background: rgba(255,255,255,0.08); border-color: var(--c-border-strong); }

.hero-trust { display: flex; align-items: center; gap: 32px; }
.trust-item { display: flex; flex-direction: column; gap: 4px; }
.trust-num { font-family: 'Plus Jakarta Sans', sans-serif; font-size: 24px; font-weight: 700; color: var(--c-text); }
.trust-lbl { font-size: 13px; color: var(--c-muted); }
.trust-sep { width: 1px; height: 32px; background: var(--c-border); }

.hero-visual {
  position: absolute;
  top: 50%;
  right: 5%;
  transform: translateY(-50%);
  width: 500px;
  z-index: 5;
  display: none;
}
@media (min-width: 1100px) { .hero-visual { display: block; } }

.visual-wrapper { position: relative; width: 100%; height: 500px; }

.glass-panel {
  background: rgba(18, 18, 18, 0.6);
  backdrop-filter: blur(24px);
  -webkit-backdrop-filter: blur(24px);
  border: 1px solid rgba(255,255,255,0.08);
  border-radius: 16px;
  box-shadow: 0 24px 48px rgba(0,0,0,0.4);
}

.main-dashboard {
  position: absolute;
  top: 50%; left: 50%;
  transform: translate(-50%, -50%);
  width: 100%;
  padding: 24px;
}

.dash-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
.dash-title { font-size: 14px; font-weight: 500; color: var(--c-muted); }
.dash-badge { font-size: 12px; font-weight: 600; color: #FBBF24; background: rgba(251, 191, 36, 0.1); padding: 4px 10px; border-radius: 6px; }

.dash-body { display: flex; flex-direction: column; gap: 24px; }
.dash-stat { display: flex; flex-direction: column; gap: 4px; }
.stat-lbl { font-size: 13px; color: var(--c-muted); }
.stat-val { font-family: 'Plus Jakarta Sans', sans-serif; font-size: 32px; font-weight: 700; }
.stat-growth { font-size: 13px; color: var(--c-success); font-weight: 500; }

.dash-chart { display: flex; align-items: flex-end; justify-content: space-between; height: 80px; gap: 8px; margin-top: 10px; }
.chart-bar {
  flex: 1;
  background: var(--c-primary);
  border-radius: 4px 4px 0 0;
  opacity: 0.8;
  transform-origin: bottom;
  animation: growBar 1s ease-out forwards;
}
@keyframes growBar { from { transform: scaleY(0); } to { transform: scaleY(1); } }

.floating-stat {
  position: absolute;
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 16px;
  border-radius: 12px;
}
.fs-1 { top: 10%; right: -20px; animation: floatY 6s ease-in-out infinite; }
.fs-2 { bottom: 15%; left: -40px; animation: floatY 5s ease-in-out infinite reverse; }

@keyframes floatY { 0%, 100% { transform: translateY(0); } 50% { transform: translateY(-15px); } }

.fs-icon { width: 40px; height: 40px; border-radius: 10px; display: flex; align-items: center; justify-content: center; color: white; }
.fs-icon.blue-bg { background: linear-gradient(135deg, var(--c-primary-light), var(--c-primary)); }
.fs-icon.green-bg { background: linear-gradient(135deg, #34D399, #10B981); }
.fs-info { display: flex; flex-direction: column; }
.fs-val { font-weight: 600; font-size: 14px; }
.fs-lbl { font-size: 12px; color: var(--c-muted); }


/* --- SECTIONS GLOBALS --- */
section { padding: 120px 0; border-top: 1px solid var(--c-border); }
.section-header { text-align: center; margin-bottom: 72px; }
.section-eyebrow { font-size: 13px; font-weight: 600; text-transform: uppercase; letter-spacing: 0.1em; color: var(--c-primary-light); margin-bottom: 16px; }
.section-title { font-size: clamp(32px, 4vw, 44px); font-weight: 800; letter-spacing: -0.02em; margin-bottom: 16px; }
.section-subtitle { font-size: 18px; color: var(--c-muted); max-width: 600px; margin: 0 auto; }

/* --- COMO FUNCIONA --- */
.how { background: #0A0A0A; }
.how-steps { display: grid; grid-template-columns: repeat(4, 1fr); gap: 32px; position: relative; }
.step-line { position: absolute; top: 24px; left: 50px; right: 50px; height: 1px; background: var(--c-border); z-index: 1; }
@media (max-width: 900px) { .how-steps { grid-template-columns: 1fr; gap: 48px; } .step-line { display: none; } }

.step { position: relative; z-index: 2; display: flex; flex-direction: column; }
.step-marker { background: #0A0A0A; padding-right: 16px; display: inline-block; margin-bottom: 24px; align-self: flex-start; }
.step-num {
  width: 48px; height: 48px;
  background: var(--c-surface);
  border: 1px solid var(--c-border-strong);
  border-radius: 12px;
  display: flex; align-items: center; justify-content: center;
  font-family: 'Plus Jakarta Sans', sans-serif; font-weight: 700; color: var(--c-text);
}
.step-title { font-size: 18px; font-weight: 700; margin-bottom: 12px; }
.step-desc { font-size: 14px; color: var(--c-muted); line-height: 1.6; }

/* --- NÍVEIS --- */
.tiers-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 20px; align-items: end; }

.tier-card {
  background: var(--c-surface);
  border: 1px solid var(--c-border);
  border-radius: 16px;
  padding: 32px 24px;
  transition: all 0.3s ease;
  position: relative;
  display: flex; flex-direction: column;
}
.tier-card:hover { transform: translateY(-8px); border-color: var(--c-border-strong); background: var(--c-surface-hover); }

.tier-featured {
  border-color: rgba(251, 191, 36, 0.4);
  background: linear-gradient(to bottom, rgba(251, 191, 36, 0.03), var(--c-surface));
  transform: translateY(-16px);
}
.tier-featured:hover { transform: translateY(-24px); border-color: rgba(251, 191, 36, 0.6); }

.featured-banner {
  position: absolute; top: -12px; left: 50%; transform: translateX(-50%);
  background: #FBBF24; color: #000; font-size: 11px; font-weight: 700; text-transform: uppercase; letter-spacing: 0.05em;
  padding: 4px 12px; border-radius: 100px; white-space: nowrap;
}

.tier-header { display: flex; align-items: center; gap: 12px; margin-bottom: 24px; }
.tier-icon-wrapper { width: 40px; height: 40px; border-radius: 10px; display: flex; align-items: center; justify-content: center; }
.tier-name { font-size: 20px; font-weight: 700; margin: 0; }

.tier-discount { display: flex; align-items: baseline; gap: 2px; }
.discount-num { font-family: 'Plus Jakarta Sans', sans-serif; font-size: 40px; font-weight: 800; color: var(--c-text); }
.discount-pct { font-size: 20px; font-weight: 700; color: var(--c-muted); }
.tier-lbl { font-size: 13px; color: var(--c-muted); margin-bottom: 24px; }

.tier-threshold { font-size: 13px; color: var(--c-muted); }
.tier-threshold strong { color: var(--c-text); font-weight: 600; }

.tier-divider { height: 1px; background: var(--c-border); margin: 24px 0; }

.tier-perks { list-style: none; padding: 0; margin: 0; display: flex; flex-direction: column; gap: 12px; }
.tier-perks li { display: flex; align-items: flex-start; gap: 12px; font-size: 13px; color: var(--c-muted); line-height: 1.4; }
.tier-perks svg { color: var(--c-text); flex-shrink: 0; margin-top: 2px; opacity: 0.7; }

/* --- FORMULÁRIO --- */
.form-wrapper { display: grid; grid-template-columns: 1fr 1fr; gap: 80px; align-items: center; }
@media (max-width: 900px) { .form-wrapper { grid-template-columns: 1fr; gap: 48px; } }

.form-title { font-size: clamp(32px, 4vw, 44px); font-weight: 800; margin-bottom: 20px; line-height: 1.1; }
.form-subtitle { font-size: 16px; color: var(--c-muted); margin-bottom: 40px; }

.benefits-compact { display: flex; flex-direction: column; gap: 24px; }
.bc-item { display: flex; gap: 16px; }
.bc-icon { width: 32px; height: 32px; border-radius: 8px; background: rgba(255,255,255,0.05); color: var(--c-text); display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
.bc-item h4 { font-size: 15px; font-weight: 600; margin-bottom: 4px; }
.bc-item p { font-size: 14px; color: var(--c-muted); }

.premium-form-card {
  background: var(--c-surface);
  border: 1px solid var(--c-border);
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 20px 40px rgba(0,0,0,0.5);
}

.form-not-logged { text-align: center; }
.lock-icon { width: 64px; height: 64px; border-radius: 16px; background: rgba(255,255,255,0.03); border: 1px solid var(--c-border); display: flex; align-items: center; justify-content: center; margin: 0 auto 24px; color: var(--c-muted); }
.form-not-logged h3 { font-size: 20px; font-weight: 700; margin-bottom: 12px; }
.form-not-logged p { font-size: 14px; color: var(--c-muted); margin-bottom: 32px; }

.active-user-badge { display: flex; align-items: center; gap: 16px; padding: 16px; background: rgba(255,255,255,0.03); border: 1px solid var(--c-border); border-radius: 12px; margin-bottom: 32px; }
.aub-avatar { width: 40px; height: 40px; border-radius: 8px; background: var(--c-primary); color: white; display: flex; align-items: center; justify-content: center; font-weight: 700; font-size: 16px; }
.aub-info { display: flex; flex-direction: column; }
.aub-name { font-weight: 600; font-size: 14px; }
.aub-email { font-size: 13px; color: var(--c-muted); }

.already-dropper { text-align: center; padding: 20px 0; }
.success-ring { width: 56px; height: 56px; border-radius: 50%; background: rgba(16, 185, 129, 0.1); border: 1px solid rgba(16, 185, 129, 0.2); display: flex; align-items: center; justify-content: center; margin: 0 auto 24px; }
.already-dropper h3 { font-size: 20px; font-weight: 700; margin-bottom: 8px; }
.already-dropper p { font-size: 14px; color: var(--c-muted); }

.form-fields { display: flex; flex-direction: column; gap: 20px; }
.input-group { display: flex; flex-direction: column; gap: 8px; }
.input-label { font-size: 13px; font-weight: 500; color: var(--c-muted); }

.input-wrapper { position: relative; display: flex; align-items: center; }
.form-input {
  width: 100%;
  background: rgba(0,0,0,0.2);
  border: 1px solid var(--c-border-strong);
  border-radius: 8px;
  padding: 12px 16px;
  color: var(--c-text);
  font-family: 'Inter', sans-serif;
  font-size: 15px;
  transition: all 0.2s;
}
.form-input:focus { outline: none; border-color: var(--c-primary); box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1); }
.form-input::placeholder { color: #52525B; }

.input-status-icon { position: absolute; right: 16px; display: flex; }
.is-valid .form-input { border-color: var(--c-success); }
.is-invalid .form-input { border-color: var(--c-error); }
.input-error { font-size: 12px; color: var(--c-error); margin-top: 4px; }

.checkbox-label { display: flex; align-items: flex-start; gap: 12px; cursor: pointer; margin: 8px 0 16px; }
.checkbox-input { display: none; }
.checkbox-box {
  width: 20px; height: 20px; border-radius: 6px; border: 1px solid var(--c-border-strong);
  display: flex; align-items: center; justify-content: center; flex-shrink: 0; background: rgba(0,0,0,0.2);
  transition: all 0.2s;
}
.checkbox-box svg { opacity: 0; transform: scale(0.5); transition: all 0.2s; }
.checkbox-input:checked + .checkbox-box { background: var(--c-primary); border-color: var(--c-primary); }
.checkbox-input:checked + .checkbox-box svg { opacity: 1; transform: scale(1); color: white; }
.checkbox-text { font-size: 13px; color: var(--c-muted); line-height: 1.5; }
.checkbox-text a { color: var(--c-text); text-decoration: underline; }

.spinner {
  width: 20px; height: 20px;
  border: 2px solid rgba(255,255,255,0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}
@keyframes spin { to { transform: rotate(360deg); } }

/* FORM SUCCESS STATE */
.form-success { text-align: center; }
.success-shield { width: 72px; height: 72px; border-radius: 20px; background: rgba(59, 130, 246, 0.1); border: 1px solid rgba(59, 130, 246, 0.2); color: var(--c-primary); display: flex; align-items: center; justify-content: center; margin: 0 auto 24px; }
.success-title { font-size: 24px; font-weight: 700; margin-bottom: 12px; }
.success-desc { font-size: 14px; color: var(--c-muted); margin-bottom: 32px; }
.success-card { background: rgba(0,0,0,0.3); border: 1px solid var(--c-border); border-radius: 12px; padding: 20px; margin-bottom: 32px; display: flex; flex-direction: column; gap: 16px; }
.sc-row { display: flex; justify-content: space-between; align-items: center; }
.sc-lbl { font-size: 13px; color: var(--c-muted); }
.sc-val { font-size: 14px; font-weight: 600; font-family: 'Plus Jakarta Sans', sans-serif;}
.bronze-text { color: #B08D57; }
</style>
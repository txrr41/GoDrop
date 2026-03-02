<template>
  <div class="dropper-page">

    <section class="hero">
      <div class="hero-noise"></div>
      <div class="hero-glow"></div>

      <nav class="hero-nav">
        <img :src="LogoDrop" alt="Go Drop" class="nav-logo" />
        <a href="/home" class="nav-back">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M19 12H5M12 5l-7 7 7 7"/></svg>
          Voltar à loja
        </a>
      </nav>

      <div class="hero-content">
        <div class="hero-eyebrow">
          <span class="eyebrow-dot"></span>
          Programa de Parceiros
        </div>

        <h1 class="hero-title">
          Venda sem<br />
          <span class="title-gradient">estocar nada.</span><br />
          Lucre de verdade.
        </h1>

        <p class="hero-desc">
          Cadastre seu CNPJ, escolha os produtos, divulgue — e cada venda sua
          vira desconto real na próxima compra. Quanto mais você vende, mais você economiza.
        </p>

        <div class="hero-actions">
          <button class="btn-primary" @click="scrollToForm">
            Quero ser Dropper
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M5 12h14M12 5l7 7-7 7"/></svg>
          </button>
          <button class="btn-ghost" @click="scrollToTiers">
            Ver os níveis
          </button>
        </div>

        <div class="hero-trust">
          <div class="trust-item">
            <span class="trust-num">+2.400</span>
            <span class="trust-lbl">droppers ativos</span>
          </div>
          <div class="trust-sep"></div>
          <div class="trust-item">
            <span class="trust-num">Até 30%</span>
            <span class="trust-lbl">de desconto</span>
          </div>
          <div class="trust-sep"></div>
          <div class="trust-item">
            <span class="trust-num">0 estoque</span>
            <span class="trust-lbl">necessário</span>
          </div>
        </div>
      </div>

      <div class="hero-visual">
        <div class="floating-card card-a">
          <div class="fc-icon">🚀</div>
          <div class="fc-data">
            <span class="fc-val">R$ 18.400</span>
            <span class="fc-lbl">vendas este mês</span>
          </div>
        </div>
        <div class="floating-card card-b">
          <div class="fc-icon">💎</div>
          <div class="fc-data">
            <span class="fc-val">Nível Diamond</span>
            <span class="fc-lbl">30% de desconto</span>
          </div>
        </div>
        <div class="floating-card card-c">
          <div class="fc-icon">📦</div>
          <div class="fc-data">
            <span class="fc-val">127 pedidos</span>
            <span class="fc-lbl">entregues</span>
          </div>
        </div>
        <div class="dashboard-mock">
          <div class="mock-bar" v-for="(h, i) in [55, 70, 45, 90, 65, 80, 100]" :key="i" :style="{height: h + '%', animationDelay: (i * 0.1) + 's'}"></div>
        </div>
      </div>
    </section>

    <section class="how">
      <div class="section-header">
        <p class="section-eyebrow">Como funciona</p>
        <h2 class="section-title">Simples assim</h2>
      </div>
      <div class="how-steps">
        <div class="step" v-for="(s, i) in steps" :key="i">
          <div class="step-num">0{{ i + 1 }}</div>
          <div class="step-icon">{{ s.icon }}</div>
          <h3 class="step-title">{{ s.title }}</h3>
          <p class="step-desc">{{ s.desc }}</p>
          <div v-if="i < steps.length - 1" class="step-connector"></div>
        </div>
      </div>
    </section>

    <section class="tiers" id="tiers" ref="tiersSection">
      <div class="section-header">
        <p class="section-eyebrow">Programa de níveis</p>
        <h2 class="section-title">Quanto mais você vende,<br/>mais você ganha</h2>
      </div>

      <div class="tiers-grid">
        <div class="tier-card" v-for="tier in tiers" :key="tier.name" :class="['tier-' + tier.key, { 'tier-featured': tier.featured }]">
          <div class="tier-badge">
            <span class="tier-emoji">{{ tier.emoji }}</span>
            <div v-if="tier.featured" class="tier-popular-tag">Mais popular</div>
          </div>
          <h3 class="tier-name">{{ tier.name }}</h3>
          <div class="tier-discount">
            <span class="discount-num">{{ tier.discount }}</span>
            <span class="discount-pct">%</span>
            <span class="discount-lbl">desconto</span>
          </div>
          <div class="tier-threshold">
            A partir de <strong>{{ tier.threshold }}</strong> em vendas
          </div>
          <ul class="tier-perks">
            <li v-for="perk in tier.perks" :key="perk">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M20 6L9 17l-5-5"/></svg>
              {{ perk }}
            </li>
          </ul>
        </div>
      </div>

      <div class="tier-note">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><path d="M12 16v-4M12 8h.01"/></svg>
        Os descontos são aplicados automaticamente nas suas compras. O nível é calculado todo dia 1° do mês com base nas vendas do mês anterior.
      </div>
    </section>

    <section class="benefits">
      <div class="section-header">
        <p class="section-eyebrow">Por que vale a pena</p>
        <h2 class="section-title">Tudo o que você precisa<br/>para crescer</h2>
      </div>
      <div class="benefits-grid">
        <div class="benefit-card" v-for="b in benefits" :key="b.title">
          <div class="benefit-icon">{{ b.icon }}</div>
          <h3 class="benefit-title">{{ b.title }}</h3>
          <p class="benefit-desc">{{ b.desc }}</p>
        </div>
      </div>
    </section>

    <section class="form-section" id="form" ref="formSection">
      <div class="form-wrapper">
        <div class="form-left">
          <p class="section-eyebrow">Comece agora</p>
          <h2 class="form-title">Vire um Dropper<br/>em minutos</h2>
          <p class="form-subtitle">
            Preencha o formulário ao lado com seu CNPJ. Vamos validar automaticamente
            e ativar os benefícios assim que tudo estiver certo.
          </p>

          <div class="form-requirements">
            <p class="req-title">Requisitos</p>
            <div class="req-item" v-for="r in requirements" :key="r">
              <div class="req-dot"></div>
              {{ r }}
            </div>
          </div>
        </div>

        <div class="form-right">
          <div v-if="!submitted" class="signup-form">
            <div v-if="!auth.isLogged" class="form-not-logged">
              <div class="not-logged-icon">🔒</div>
              <h3>Faça login primeiro</h3>
              <p>Você precisa ter uma conta Go Drop para se tornar um Dropper.</p>
              <button class="btn-primary" @click="goToLogin">
                Entrar ou Criar Conta
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M5 12h14M12 5l7 7-7 7"/></svg>
              </button>
            </div>

            <div v-else>
              <div class="form-user-info">
                <div class="fui-avatar">{{ auth.user?.name?.charAt(0) }}</div>
                <div>
                  <p class="fui-name">{{ auth.user?.name }}</p>
                  <p class="fui-email">{{ auth.user?.email }}</p>
                </div>
              </div>

              <div v-if="auth.user?.role === 'DROPPER'" class="already-dropper">
                <span class="ad-icon">🎉</span>
                <div>
                  <p class="ad-title">Você já é um Dropper!</p>
                  <p class="ad-sub">Seus benefícios já estão ativos na sua conta.</p>
                </div>
              </div>

              <div v-else>
                <div class="input-group">
                  <label class="input-label">CNPJ da sua empresa</label>
                  <div class="input-wrapper">
                    <svg class="input-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="2" y="7" width="20" height="14" rx="2"/><path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/></svg>
                    <input
                        :value="cnpj"
                        type="text"
                        placeholder="00.000.000/0001-00"
                        class="form-input"
                        maxlength="18"
                        @input="formatCNPJ"
                    />
                    <div v-if="cnpjStatus === 'valid'" class="input-badge valid">Válido</div>
                    <div v-if="cnpjStatus === 'invalid'" class="input-badge invalid">Inválido</div>
                  </div>
                  <p v-if="cnpjError" class="input-error">{{ cnpjError }}</p>
                </div>

                <div class="input-group">
                  <label class="input-label">Nome da sua loja / marca</label>
                  <div class="input-wrapper">
                    <svg class="input-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/></svg>
                    <input v-model="storeName" type="text" placeholder="Ex: Minha Loja Digital" class="form-input" />
                  </div>
                </div>

                <div class="input-group">
                  <label class="input-label">Seu WhatsApp</label>
                  <div class="input-wrapper">
                    <svg class="input-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07A19.5 19.5 0 0 1 4.5 12.88a19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 3.39 2h3a2 2 0 0 1 2 1.72c.127.96.361 1.903.7 2.81a2 2 0 0 1-.45 2.11L7.91 9.91a16 16 0 0 0 6.1 6.1l1.27-1.27a2 2 0 0 1 2.11-.45c.907.339 1.85.573 2.81.7A2 2 0 0 1 22 16.92z"/></svg>
                    <input v-model="whatsapp" type="text" placeholder="(00) 00000-0000" class="form-input" @input="formatPhone" maxlength="15" />
                  </div>
                </div>

                <label class="checkbox-label">
                  <input type="checkbox" v-model="acceptedTerms" class="checkbox-input" />
                  <span class="checkbox-custom"></span>
                  <span class="checkbox-text">Li e aceito os <a href="#" class="link">Termos do Programa Dropper</a></span>
                </label>

                <button
                    class="btn-primary btn-submit"
                    :disabled="loading || !acceptedTerms || cnpj.length < 18"
                    @click="handleSubmit"
                >
                  <span v-if="loading" class="loading-spinner"></span>
                  <span v-else>Quero ser Dropper</span>
                  <svg v-if="!loading" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M5 12h14M12 5l7 7-7 7"/></svg>
                </button>
              </div>
            </div>
          </div>

          <div v-else class="form-success">
            <div class="success-icon">🎉</div>
            <h3 class="success-title">Solicitação enviada!</h3>
            <p class="success-desc">Seu CNPJ está sendo validado. Em breve você receberá a confirmação por e-mail e seus benefícios serão ativados.</p>
            <div class="success-details">
              <div class="sd-row">
                <span>CNPJ</span>
                <strong>{{ cnpj }}</strong>
              </div>
              <div class="sd-row">
                <span>Nível inicial</span>
                <strong>🥉 Bronze — 5%</strong>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="faq">
      <div class="section-header">
        <p class="section-eyebrow">Dúvidas frequentes</p>
        <h2 class="section-title">Ficou alguma dúvida?</h2>
      </div>
      <div class="faq-list">
        <div
            class="faq-item"
            v-for="(faq, i) in faqs"
            :key="i"
            :class="{ open: openFaq === i }"
            @click="openFaq = openFaq === i ? null : i"
        >
          <div class="faq-question">
            {{ faq.q }}
            <svg class="faq-arrow" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M6 9l6 6 6-6"/></svg>
          </div>
          <div class="faq-answer">{{ faq.a }}</div>
        </div>
      </div>
    </section>

    <section class="cta-final">
      <div class="cta-content">
        <h2 class="cta-title">Pronto para começar?</h2>
        <p class="cta-desc">Junte-se a mais de 2.400 droppers e comece a vender hoje mesmo.</p>
        <button class="btn-primary btn-large" @click="scrollToForm">
          Quero ser Dropper agora
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M5 12h14M12 5l7 7-7 7"/></svg>
        </button>
      </div>
    </section>

  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useAuthStore } from '../stores/auth'
import LogoDrop from '../assets/LogoDrop.png'
import api from "../api/api.js";

const auth = useAuthStore()

const cnpj = ref('')
const storeName = ref('')
const whatsapp = ref('')
const acceptedTerms = ref(false)
const cnpjStatus = ref(null)
const cnpjError = ref('')
const loading = ref(false)
const submitted = ref(false)
const openFaq = ref(null)

const tiersSection = ref(null)
const formSection = ref(null)

const steps = [
  { icon: '📋', title: 'Crie sua conta', desc: 'Se ainda não tem, registre-se gratuitamente na Go Drop. Leva menos de 2 minutos.' },
  { icon: '🏢', title: 'Informe seu CNPJ', desc: 'Acesse a página Seja Dropper e insira o CNPJ da sua empresa. Validamos automaticamente.' },
  { icon: '🛍️', title: 'Escolha produtos', desc: 'Acesse nosso catálogo exclusivo para droppers com preços especiais e selecione o que vai divulgar.' },
  { icon: '💰', title: 'Venda e suba de nível', desc: 'Cada venda acumula pontos. Quanto mais você vende, maior o seu nível e maior o desconto.' },
]

const tiers = [
  {
    key: 'bronze', name: 'Bronze', emoji: '🥉', discount: 5, threshold: 'R$ 0',
    featured: false,
    perks: ['5% de desconto em compras', 'Acesso ao catálogo dropper', 'Suporte por e-mail']
  },
  {
    key: 'silver', name: 'Silver', emoji: '🥈', discount: 10, threshold: 'R$ 5.000 em vendas',
    featured: false,
    perks: ['10% de desconto em compras', 'Badge verificado no perfil', 'Suporte prioritário', 'Relatórios de vendas']
  },
  {
    key: 'gold', name: 'Gold', emoji: '🥇', discount: 15, threshold: 'R$ 20.000 em vendas',
    featured: true,
    perks: ['15% de desconto em compras', 'Acesso antecipado a lançamentos', 'Canal exclusivo no WhatsApp', 'Comissão extra em produtos parceiros']
  },
  {
    key: 'platinum', name: 'Platinum', emoji: '🔷', discount: 22, threshold: 'R$ 60.000 em vendas',
    featured: false,
    perks: ['22% de desconto em compras', 'Gerente de contas dedicado', 'Personalização de marca', 'Acesso à API de estoque']
  },
  {
    key: 'diamond', name: 'Diamond', emoji: '💎', discount: 30, threshold: 'R$ 150.000 em vendas',
    featured: false,
    perks: ['30% de desconto em compras', 'Condições negociadas caso a caso', 'Treinamentos e eventos exclusivos', 'Co-marketing disponível']
  },
]

const benefits = [
  { icon: '📦', title: 'Zero estoque', desc: 'Você divulga, a Go Drop separa e envia diretamente para o seu cliente final.' },
  { icon: '🔄', title: 'Sincronização de estoque', desc: 'Estoque atualizado em tempo real. Sem risco de vender produto fora de estoque.' },
  { icon: '📊', title: 'Dashboard completo', desc: 'Acompanhe suas vendas, comissões e progresso de nível em um painel exclusivo.' },
  { icon: '🚀', title: 'Rastreio automático', desc: 'Seus clientes recebem atualizações de entrega automaticamente, sem precisar te perguntar.' },
  { icon: '💳', title: 'Descontos reais', desc: 'Quanto mais você vende, mais barato fica para você comprar. Desconto real, na prática.' },
  { icon: '🤝', title: 'Suporte dedicado', desc: 'A partir do nível Silver você tem suporte prioritário para resolver tudo rápido.' },
]

const requirements = [
  'CNPJ ativo e em situação regular',
  'Conta Go Drop cadastrada',
  'E-mail verificado',
]

const faqs = [
  { q: 'Precisa ter estoque?', a: 'Não! Você vende como se fosse sua loja, mas quem armazena e envia é a Go Drop. Você só precisa divulgar.' },
  { q: 'Como funciona o desconto?', a: 'A cada compra de produto para revenda, o sistema aplica automaticamente o desconto do seu nível atual.' },
  { q: 'Quando o nível muda?', a: 'O nível é recalculado todo dia 1° do mês com base nas suas vendas do mês anterior.' },
  { q: 'Posso me cadastrar sendo MEI?', a: 'Sim! Aceitamos MEI e demais tipos de CNPJ, desde que estejam ativos na Receita Federal.' },
  { q: 'O cadastro é gratuito?', a: 'Totalmente gratuito. Sem mensalidade, sem taxa de adesão. Você só paga pelos produtos que comprar.' },
  { q: 'Como rastrear minhas vendas?', a: 'Após ativar o cadastro, você terá acesso a um dashboard exclusivo com todos os seus dados em tempo real.' },
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
  // Basic digit validation
  let sum = 0
  let pos = 5
  for (let i = 0; i < 12; i++) {
    sum += parseInt(nums[i]) * pos--
    if (pos < 2) pos = 9
  }
  let r = sum % 11 < 2 ? 0 : 11 - (sum % 11)
  if (r !== parseInt(nums[12])) { cnpjStatus.value = 'invalid'; cnpjError.value = 'CNPJ inválido'; return }
  sum = 0; pos = 6
  for (let i = 0; i < 13; i++) {
    sum += parseInt(nums[i]) * pos--
    if (pos < 2) pos = 9
  }
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
    submitted.value = true
  } catch (err) {
    cnpjError.value = 'Erro ao enviar. Tente novamente.'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Syne:wght@600;700;800&family=Manrope:wght@400;500;600;700&display=swap');

.dropper-page {
  --c-bg: #09090B;
  --c-surface: #111113;
  --c-border: #1E1E22;
  --c-text: #FAFAFA;
  --c-muted: #A1A1AA;
  --c-accent: #22D3EE;
  --c-accent2: #818CF8;
  --c-gold: #F59E0B;
  --c-green: #10B981;
  font-family: 'Manrope', sans-serif;
  background: var(--c-bg);
  color: var(--c-text);
  min-height: 100vh;
  overflow-x: hidden;
}

.hero {
  position: relative;
  min-height: 100vh;
  display: grid;
  grid-template-rows: auto 1fr;
  grid-template-columns: 1fr 1fr;
  grid-template-areas: "nav nav" "content visual";
  padding: 0 60px 80px;
  gap: 0 40px;
  overflow: hidden;
}

.hero-noise {
  position: absolute;
  inset: 0;
  background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 256 256' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noise'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.9' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23noise)' opacity='0.03'/%3E%3C/svg%3E");
  opacity: 0.4;
  pointer-events: none;
}

.hero-glow {
  position: absolute;
  width: 800px;
  height: 800px;
  background: radial-gradient(circle, rgba(34,211,238,0.08) 0%, transparent 70%);
  top: -200px;
  right: -100px;
  pointer-events: none;
}

.hero-nav {
  grid-area: nav;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 32px 0;
}

.nav-logo { height: 36px; filter: brightness(0) invert(1); }

.nav-back {
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--c-muted);
  text-decoration: none;
  font-size: 14px;
  font-weight: 600;
  transition: color 0.2s;
}
.nav-back:hover { color: var(--c-text); }

.hero-content {
  grid-area: content;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding-top: 20px;
}

.hero-eyebrow {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.12em;
  text-transform: uppercase;
  color: var(--c-accent);
  margin-bottom: 28px;
}

.eyebrow-dot {
  width: 6px; height: 6px;
  background: var(--c-accent);
  border-radius: 50%;
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; transform: scale(1); }
  50% { opacity: 0.6; transform: scale(0.8); }
}

.hero-title {
  font-family: 'Syne', sans-serif;
  font-size: clamp(42px, 5vw, 72px);
  font-weight: 800;
  line-height: 1.05;
  letter-spacing: -0.03em;
  margin: 0 0 28px;
}

.title-gradient {
  background: linear-gradient(120deg, var(--c-accent) 0%, var(--c-accent2) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.hero-desc {
  font-size: 17px;
  line-height: 1.7;
  color: var(--c-muted);
  max-width: 480px;
  margin-bottom: 40px;
}

.hero-actions {
  display: flex;
  gap: 14px;
  margin-bottom: 56px;
}

.btn-primary {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  background: var(--c-accent);
  color: #09090B;
  border: none;
  border-radius: 12px;
  padding: 16px 28px;
  font-size: 15px;
  font-weight: 700;
  font-family: 'Manrope', sans-serif;
  cursor: pointer;
  transition: all 0.2s;
  white-space: nowrap;
}
.btn-primary:hover { background: #67E8F9; transform: translateY(-2px); box-shadow: 0 12px 32px rgba(34,211,238,0.25); }
.btn-primary:disabled { opacity: 0.5; cursor: not-allowed; transform: none; box-shadow: none; }

.btn-ghost {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  background: transparent;
  color: var(--c-text);
  border: 1px solid var(--c-border);
  border-radius: 12px;
  padding: 16px 28px;
  font-size: 15px;
  font-weight: 600;
  font-family: 'Manrope', sans-serif;
  cursor: pointer;
  transition: all 0.2s;
}
.btn-ghost:hover { border-color: var(--c-muted); }

.hero-trust {
  display: flex;
  align-items: center;
  gap: 32px;
}

.trust-item { display: flex; flex-direction: column; gap: 2px; }
.trust-num { font-family: 'Syne', sans-serif; font-size: 22px; font-weight: 700; color: var(--c-text); }
.trust-lbl { font-size: 12px; color: var(--c-muted); font-weight: 500; }
.trust-sep { width: 1px; height: 40px; background: var(--c-border); }

.hero-visual {
  grid-area: visual;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.floating-card {
  position: absolute;
  background: rgba(17, 17, 19, 0.9);
  border: 1px solid var(--c-border);
  border-radius: 16px;
  padding: 16px 20px;
  display: flex;
  align-items: center;
  gap: 14px;
  backdrop-filter: blur(20px);
  box-shadow: 0 20px 60px rgba(0,0,0,0.4);
}

.card-a { top: 15%; left: -20px; animation: floatA 4s ease-in-out infinite; }
.card-b { top: 50%; right: -10px; transform: translateY(-50%); animation: floatB 5s ease-in-out infinite; }
.card-c { bottom: 15%; left: 10%; animation: floatC 3.5s ease-in-out infinite; }

@keyframes floatA { 0%,100%{transform:translateY(0)} 50%{transform:translateY(-12px)} }
@keyframes floatB { 0%,100%{transform:translateY(-50%)} 50%{transform:translateY(calc(-50% - 10px))} }
@keyframes floatC { 0%,100%{transform:translateY(0)} 50%{transform:translateY(-8px)} }

.fc-icon { font-size: 28px; }
.fc-val { display: block; font-family: 'Syne', sans-serif; font-size: 15px; font-weight: 700; color: var(--c-text); }
.fc-lbl { display: block; font-size: 11px; color: var(--c-muted); font-weight: 500; }

.dashboard-mock {
  width: 280px; height: 180px;
  background: rgba(17,17,19,0.8);
  border: 1px solid var(--c-border);
  border-radius: 20px;
  display: flex;
  align-items: flex-end;
  justify-content: space-around;
  padding: 24px 20px 20px;
  gap: 8px;
}

.mock-bar {
  flex: 1;
  background: linear-gradient(to top, var(--c-accent), var(--c-accent2));
  border-radius: 6px 6px 0 0;
  opacity: 0.7;
  animation: growBar 1s ease-out forwards;
  transform-origin: bottom;
  transform: scaleY(0);
}

@keyframes growBar {
  to { transform: scaleY(1); }
}

section { padding: 100px 60px; }

.section-header {
  text-align: center;
  margin-bottom: 64px;
}

.section-eyebrow {
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.12em;
  text-transform: uppercase;
  color: var(--c-accent);
  margin-bottom: 16px;
}

.section-title {
  font-family: 'Syne', sans-serif;
  font-size: clamp(32px, 4vw, 52px);
  font-weight: 800;
  line-height: 1.1;
  letter-spacing: -0.03em;
  margin: 0;
}

.how { background: var(--c-surface); border-top: 1px solid var(--c-border); border-bottom: 1px solid var(--c-border); }

.how-steps {
  display: flex;
  gap: 0;
  position: relative;
}

.step {
  flex: 1;
  text-align: center;
  padding: 0 32px;
  position: relative;
}

.step-num {
  font-family: 'Syne', sans-serif;
  font-size: 56px;
  font-weight: 800;
  color: var(--c-border);
  line-height: 1;
  margin-bottom: 20px;
}

.step-icon {
  font-size: 36px;
  margin-bottom: 16px;
}

.step-title {
  font-family: 'Syne', sans-serif;
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 12px;
}

.step-desc {
  font-size: 14px;
  color: var(--c-muted);
  line-height: 1.6;
}

.step-connector {
  position: absolute;
  top: 36px;
  right: -12px;
  width: 24px;
  height: 2px;
  background: var(--c-border);
}

.tiers { background: var(--c-bg); }

.tiers-grid {
  display: flex;
  gap: 16px;
  align-items: stretch;
}

.tier-card {
  flex: 1;
  background: var(--c-surface);
  border: 1px solid var(--c-border);
  border-radius: 20px;
  padding: 28px 24px;
  transition: all 0.2s;
  position: relative;
}
.tier-card:hover { border-color: #333; transform: translateY(-4px); }
.tier-featured { border-color: var(--c-gold) !important; background: rgba(245,158,11,0.04); transform: scale(1.03); }
.tier-featured:hover { transform: scale(1.03) translateY(-4px); }

.tier-badge {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.tier-emoji { font-size: 32px; }

.tier-popular-tag {
  font-size: 10px;
  font-weight: 700;
  background: var(--c-gold);
  color: #09090B;
  padding: 4px 10px;
  border-radius: 100px;
  letter-spacing: 0.06em;
  text-transform: uppercase;
}

.tier-name {
  font-family: 'Syne', sans-serif;
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 16px;
}

.tier-discount {
  display: flex;
  align-items: baseline;
  gap: 4px;
  margin-bottom: 8px;
}

.discount-num { font-family: 'Syne', sans-serif; font-size: 48px; font-weight: 800; color: var(--c-accent); }
.discount-pct { font-size: 24px; font-weight: 700; color: var(--c-accent); }
.discount-lbl { font-size: 14px; color: var(--c-muted); margin-left: 4px; }
.tier-featured .discount-num, .tier-featured .discount-pct { color: var(--c-gold); }

.tier-threshold {
  font-size: 12px;
  color: var(--c-muted);
  margin-bottom: 24px;
  padding-bottom: 24px;
  border-bottom: 1px solid var(--c-border);
}
.tier-threshold strong { color: var(--c-text); }

.tier-perks {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.tier-perks li {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  font-size: 13px;
  color: var(--c-muted);
}

.tier-perks svg { color: var(--c-green); flex-shrink: 0; margin-top: 2px; }

.tier-note {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 32px;
  padding: 16px 20px;
  background: var(--c-surface);
  border: 1px solid var(--c-border);
  border-radius: 12px;
  font-size: 13px;
  color: var(--c-muted);
}
.tier-note svg { color: var(--c-accent); flex-shrink: 0; }

benefits { background: var(--c-surface); border-top: 1px solid var(--c-border); border-bottom: 1px solid var(--c-border); }

.benefits-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.benefit-card {
  background: var(--c-bg);
  border: 1px solid var(--c-border);
  border-radius: 20px;
  padding: 32px;
  transition: all 0.2s;
}
.benefit-card:hover { border-color: #333; transform: translateY(-4px); }

.benefit-icon { font-size: 36px; margin-bottom: 20px; }
.benefit-title { font-family: 'Syne', sans-serif; font-size: 18px; font-weight: 700; margin-bottom: 12px; }
.benefit-desc { font-size: 14px; color: var(--c-muted); line-height: 1.6; }

.form-section { background: var(--c-bg); }

.form-wrapper {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 80px;
  align-items: start;
  max-width: 1100px;
  margin: 0 auto;
}

.form-title {
  font-family: 'Syne', sans-serif;
  font-size: clamp(32px, 3vw, 48px);
  font-weight: 800;
  letter-spacing: -0.03em;
  line-height: 1.1;
  margin-bottom: 20px;
}

.form-subtitle {
  font-size: 15px;
  color: var(--c-muted);
  line-height: 1.7;
  margin-bottom: 40px;
}

.req-title {
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.1em;
  text-transform: uppercase;
  color: var(--c-muted);
  margin-bottom: 16px;
}

.req-item {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 14px;
  color: var(--c-text);
  margin-bottom: 10px;
}

.req-dot {
  width: 8px; height: 8px;
  border-radius: 50%;
  background: var(--c-green);
  flex-shrink: 0;
}

.signup-form {
  background: var(--c-surface);
  border: 1px solid var(--c-border);
  border-radius: 24px;
  padding: 40px;
}

.form-not-logged {
  text-align: center;
  padding: 20px 0;
}
.not-logged-icon { font-size: 48px; margin-bottom: 20px; }
.form-not-logged h3 { font-family: 'Syne', sans-serif; font-size: 22px; font-weight: 700; margin-bottom: 12px; }
.form-not-logged p { color: var(--c-muted); font-size: 14px; margin-bottom: 28px; }

.form-user-info {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 16px;
  background: rgba(34,211,238,0.05);
  border: 1px solid rgba(34,211,238,0.2);
  border-radius: 12px;
  margin-bottom: 28px;
}

.fui-avatar {
  width: 44px; height: 44px;
  background: var(--c-accent);
  color: #09090B;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 800;
  font-size: 18px;
  flex-shrink: 0;
}

.fui-name { font-weight: 700; font-size: 15px; margin-bottom: 2px; }
.fui-email { font-size: 13px; color: var(--c-muted); }

.already-dropper {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  padding: 20px;
  background: rgba(16,185,129,0.08);
  border: 1px solid rgba(16,185,129,0.3);
  border-radius: 16px;
}
.ad-icon { font-size: 32px; }
.ad-title { font-weight: 700; font-size: 16px; margin-bottom: 4px; color: var(--c-green); }
.ad-sub { font-size: 13px; color: var(--c-muted); }

.input-group { margin-bottom: 20px; }
.input-label { display: block; font-size: 13px; font-weight: 600; color: var(--c-muted); margin-bottom: 8px; letter-spacing: 0.04em; }

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.input-icon {
  position: absolute;
  left: 14px;
  color: #555;
  pointer-events: none;
}

.form-input {
  width: 100%;
  height: 52px;
  padding: 0 16px 0 46px;
  background: var(--c-bg);
  border: 1px solid var(--c-border);
  border-radius: 12px;
  color: var(--c-text);
  font-size: 15px;
  font-family: 'Manrope', sans-serif;
  outline: none;
  transition: border-color 0.2s;
}
.form-input:focus { border-color: var(--c-accent); }
.form-input::placeholder { color: #444; }

.input-badge {
  position: absolute;
  right: 12px;
  font-size: 11px;
  font-weight: 700;
  padding: 3px 8px;
  border-radius: 6px;
}
.input-badge.valid { background: rgba(16,185,129,0.15); color: var(--c-green); }
.input-badge.invalid { background: rgba(239,68,68,0.15); color: #F87171; }

.input-error { font-size: 12px; color: #F87171; margin-top: 6px; }

.checkbox-label {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  cursor: pointer;
  margin-bottom: 24px;
}

.checkbox-input { display: none; }

.checkbox-custom {
  width: 20px; height: 20px;
  border: 2px solid var(--c-border);
  border-radius: 6px;
  flex-shrink: 0;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 1px;
}

.checkbox-input:checked + .checkbox-custom {
  background: var(--c-accent);
  border-color: var(--c-accent);
}
.checkbox-input:checked + .checkbox-custom::after {
  content: '✓';
  font-size: 12px;
  color: #09090B;
  font-weight: 800;
}

.checkbox-text { font-size: 13px; color: var(--c-muted); line-height: 1.5; }
.link { color: var(--c-accent); text-decoration: none; }

.btn-submit { width: 100%; justify-content: center; height: 56px; font-size: 16px; }

.loading-spinner {
  width: 18px; height: 18px;
  border: 2px solid rgba(9,9,11,0.3);
  border-top-color: #09090B;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin { to { transform: rotate(360deg); } }

/* Success state */
.form-success {
  background: var(--c-surface);
  border: 1px solid rgba(16,185,129,0.3);
  border-radius: 24px;
  padding: 48px 40px;
  text-align: center;
}

.success-icon { font-size: 64px; margin-bottom: 24px; }
.success-title { font-family: 'Syne', sans-serif; font-size: 26px; font-weight: 800; margin-bottom: 12px; }
.success-desc { font-size: 15px; color: var(--c-muted); line-height: 1.6; margin-bottom: 32px; }

.success-details {
  background: var(--c-bg);
  border: 1px solid var(--c-border);
  border-radius: 16px;
  padding: 20px 24px;
}

.sd-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  font-size: 14px;
}
.sd-row:not(:last-child) { border-bottom: 1px solid var(--c-border); }
.sd-row span { color: var(--c-muted); }
.sd-row strong { color: var(--c-text); }

.faq { background: var(--c-surface); border-top: 1px solid var(--c-border); }

.faq-list {
  max-width: 800px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.faq-item {
  background: var(--c-bg);
  border: 1px solid var(--c-border);
  border-radius: 16px;
  overflow: hidden;
  cursor: pointer;
  transition: border-color 0.2s;
}
.faq-item:hover, .faq-item.open { border-color: #333; }
.faq-item.open { border-color: rgba(34,211,238,0.3); }

.faq-question {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  font-size: 15px;
  font-weight: 600;
}

.faq-arrow {
  flex-shrink: 0;
  transition: transform 0.3s;
  color: var(--c-muted);
}
.faq-item.open .faq-arrow { transform: rotate(180deg); color: var(--c-accent); }

.faq-answer {
  max-height: 0;
  overflow: hidden;
  transition: max-height 0.4s ease, padding 0.3s;
  font-size: 14px;
  color: var(--c-muted);
  line-height: 1.7;
  padding: 0 24px;
}
.faq-item.open .faq-answer {
  max-height: 200px;
  padding: 0 24px 20px;
}

.cta-final {
  background: linear-gradient(135deg, rgba(34,211,238,0.08) 0%, rgba(129,140,248,0.08) 100%);
  border-top: 1px solid var(--c-border);
  text-align: center;
  padding: 100px 60px;
}

.cta-title {
  font-family: 'Syne', sans-serif;
  font-size: clamp(36px, 4vw, 56px);
  font-weight: 800;
  letter-spacing: -0.03em;
  margin-bottom: 20px;
}

.cta-desc {
  font-size: 17px;
  color: var(--c-muted);
  margin-bottom: 40px;
}

.btn-large {
  height: 64px;
  padding: 0 48px;
  font-size: 17px;
  border-radius: 16px;
}

@media (max-width: 1100px) {
  .hero { grid-template-columns: 1fr; grid-template-areas: "nav" "content"; padding: 0 40px 60px; }
  .hero-visual { display: none; }
  .tiers-grid { flex-wrap: wrap; }
  .tier-card { min-width: calc(50% - 8px); }
  .tier-featured { transform: none; }
  .benefits-grid { grid-template-columns: repeat(2, 1fr); }
  .form-wrapper { grid-template-columns: 1fr; gap: 40px; }
}

@media (max-width: 768px) {
  section { padding: 60px 24px; }
  .hero { padding: 0 24px 60px; }
  .hero-actions { flex-direction: column; }
  .btn-primary, .btn-ghost { width: 100%; justify-content: center; }
  .how-steps { flex-direction: column; gap: 32px; }
  .step-connector { display: none; }
  .tier-card { min-width: 100%; }
  .benefits-grid { grid-template-columns: 1fr; }
  .hero-trust { flex-wrap: wrap; gap: 20px; }
}
</style>
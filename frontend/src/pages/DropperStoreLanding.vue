<template>
  <div class="landing">

    <nav class="nav">
      <div class="nav-inner">
        <div class="nav-brand">
          <svg width="28" height="28" viewBox="0 0 32 32" fill="none">
            <rect width="32" height="32" rx="8" fill="#2563EB"/>
            <path d="M8 22L16 10L24 22H8Z" fill="white" fill-opacity=".9"/>
            <circle cx="16" cy="16" r="3" fill="white"/>
          </svg>
          <span class="brand-name">GoDrop</span>
        </div>
        <div class="nav-links">
          <a @click="scrollTo('how')" class="nav-link">Como funciona</a>
          <a @click="scrollTo('levels')" class="nav-link">Níveis</a>
          <a @click="scrollTo('faq')" class="nav-link">FAQ</a>
        </div>
        <button class="nav-cta" @click="$router.push('/minha-loja')">
          Criar minha loja
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M5 12h14M12 5l7 7-7 7"/></svg>
        </button>
      </div>
    </nav>

    <section class="hero">
      <div class="hero-bg-shape"></div>
      <div class="hero-dots"></div>

      <div class="hero-inner">
        <div class="hero-left">
          <div class="hero-pill">
            <span class="pill-dot"></span>
            White Label com IA · Novo
          </div>

          <h1 class="hero-h1">
            Sua marca.<br/>
            <em class="hero-em">Sua loja.</em><br/>
            Seu dinheiro.
          </h1>

          <p class="hero-p">
            Crie uma loja virtual com identidade visual própria em minutos.
            Nossa IA gera nome, cores e slogan — você só escolhe os produtos e vende.
          </p>

          <div class="hero-btns">
            <button class="btn-solid" @click="$router.push('/minha-loja')">
              Criar minha loja grátis
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M5 12h14M12 5l7 7-7 7"/></svg>
            </button>
            <button class="btn-outline" @click="scrollTo('how')">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><polygon points="10 8 16 12 10 16 10 8"/></svg>
              Ver demonstração
            </button>
          </div>

          <div class="hero-trust">
            <div class="trust-stat">
              <span class="ts-num">2min</span>
              <span class="ts-lbl">Para criar sua loja</span>
            </div>
            <div class="trust-sep"></div>
            <div class="trust-stat">
              <span class="ts-num">30%</span>
              <span class="ts-lbl">Desconto Diamond</span>
            </div>
            <div class="trust-sep"></div>
            <div class="trust-stat">
              <span class="ts-num">100%</span>
              <span class="ts-lbl">Identidade visual sua</span>
            </div>
          </div>
        </div>

        <div class="hero-right">
          <div class="browser">
            <div class="browser-top">
              <div class="browser-circles">
                <span class="bc red"></span><span class="bc yellow"></span><span class="bc green"></span>
              </div>
              <div class="browser-bar">
                godrop.com.br/loja/<strong>{{ mockStore.slug }}</strong>
              </div>
              <div class="browser-spacer"></div>
            </div>
            <div class="browser-body" :style="`background:${mockStore.bg}`">
              <div class="mock-nav" :style="`background:${mockStore.primary}`">
                <span class="mock-logo-text">{{ mockStore.name }}</span>
                <span class="mock-tagline">{{ mockStore.slogan }}</span>
              </div>
              <div class="mock-grid">
                <div v-for="n in 4" :key="n" class="mock-card">
                  <div class="mock-thumb" :style="`background:${mockStore.accent}18`"></div>
                  <div class="mock-lines">
                    <div class="mock-line long"></div>
                    <div class="mock-line short" :style="`background:${mockStore.primary}88`"></div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="theme-row">
            <span class="theme-label">Tema:</span>
            <div class="theme-dots">
              <button
                  v-for="t in themes" :key="t.name"
                  class="theme-dot"
                  :class="{ active: mockStore === t }"
                  :style="`background:${t.primary}`"
                  :title="t.name"
                  @click="mockStore = t"
              ></button>
            </div>
            <span class="theme-name">{{ mockStore.name }}</span>
          </div>

          <div class="hero-badge-float">
            <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="#2563EB" stroke-width="2.5"><path d="M20 6L9 17l-5-5"/></svg>
            IA gerou em 3 segundos
          </div>
        </div>
      </div>

      <div class="scroll-hint">
        <div class="scroll-line"></div>
        <span>Role para baixo</span>
      </div>
    </section>

    <section class="how-section" id="how">
      <div class="section-wrap">
        <div class="section-tag">Como funciona</div>
        <h2 class="section-h2">De zero à sua loja em <span class="text-blue">3 passos</span></h2>
        <p class="section-sub">Nossa IA cuida do design. Você cuida das vendas.</p>

        <div class="steps-grid">
          <div v-for="(step, i) in steps" :key="i" class="step-card">
            <div class="step-num">0{{ i + 1 }}</div>
            <div class="step-icon-box">{{ step.icon }}</div>
            <h3 class="step-title">{{ step.title }}</h3>
            <p class="step-desc">{{ step.desc }}</p>
            <svg v-if="i < steps.length - 1" class="step-connector" width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="#BFDBFE" stroke-width="2"><path d="M5 12h14M12 5l7 7-7 7"/></svg>
          </div>
        </div>
      </div>
    </section>

    <section class="ai-section">
      <div class="ai-bg-accent"></div>
      <div class="section-wrap ai-layout">

        <div class="ai-left">
          <div class="section-tag">Inteligência Artificial</div>
          <h2 class="section-h2">Descreva sua loja.<br/><span class="text-blue">A IA cria o resto.</span></h2>
          <p class="section-sub">Digite o nicho em português. Em segundos você recebe nome, slogan, paleta de cores e identidade visual.</p>

          <div class="ai-prompts">
            <button
                v-for="ex in aiExamples" :key="ex.prompt"
                class="ai-prompt-btn"
                :class="{ active: activeExample === ex }"
                @click="activeExample = ex"
            >
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="11" cy="11" r="8"/><path d="m21 21-4.35-4.35"/></svg>
              "{{ ex.prompt }}"
            </button>
          </div>
        </div>

        <div class="ai-right" v-if="activeExample">
          <div class="ai-result-card">
            <div class="ai-result-header">
              <span class="ai-badge">✦ Gerado pela IA</span>
              <span class="ai-time">~3s</span>
            </div>

            <div class="ai-preview" :style="`background: linear-gradient(140deg, ${activeExample.primary} 0%, ${activeExample.secondary} 100%)`">
              <div class="aip-tag">Preview da loja</div>
              <div class="aip-name">{{ activeExample.name }}</div>
              <div class="aip-slogan">{{ activeExample.slogan }}</div>
            </div>

            <div class="ai-fields">
              <div class="ai-field">
                <span class="af-label">Nome gerado</span>
                <span class="af-val">{{ activeExample.name }}</span>
              </div>
              <div class="ai-field">
                <span class="af-label">Slogan</span>
                <span class="af-val">{{ activeExample.slogan }}</span>
              </div>
              <div class="ai-field">
                <span class="af-label">Estilo</span>
                <span class="af-tag">{{ activeExample.style }}</span>
              </div>
              <div class="ai-field">
                <span class="af-label">Paleta</span>
                <div class="af-colors">
                  <span class="af-dot" :style="`background:${activeExample.primary}`"></span>
                  <span class="af-dot" :style="`background:${activeExample.secondary}`"></span>
                  <span class="af-dot" :style="`background:${activeExample.bg}`"></span>
                </div>
              </div>
            </div>

            <button class="ai-use-btn" @click="$router.push('/minha-loja')">
              Usar este tema
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M5 12h14M12 5l7 7-7 7"/></svg>
            </button>
          </div>
        </div>

      </div>
    </section>

    <section class="levels-section" id="levels">
      <div class="section-wrap">
        <div class="section-tag">Programa de Níveis</div>
        <h2 class="section-h2">Quanto mais você vende,<br/><span class="text-blue">mais você ganha</span></h2>
        <p class="section-sub">Suba de nível e desbloqueie descontos maiores. Sua margem cresce com você.</p>

        <div class="levels-grid">
          <div v-for="lv in levels" :key="lv.name" class="level-card" :class="{ 'level-card--featured': lv.featured }">
            <div v-if="lv.featured" class="level-badge">Popular</div>
            <div class="level-gem">{{ lv.gem }}</div>
            <div class="level-name" :style="`color:${lv.color}`">{{ lv.name }}</div>
            <div class="level-pct">{{ lv.discount }}</div>
            <div class="level-pct-label">de desconto</div>
            <div class="level-req">A partir de R$ {{ lv.minSales }}</div>
            <ul class="level-perks">
              <li v-for="p in lv.perks" :key="p" class="perk-item">
                <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="#2563EB" stroke-width="2.5"><polyline points="20 6 9 17 4 12"/></svg>
                {{ p }}
              </li>
            </ul>
          </div>
        </div>
      </div>
    </section>

    <section class="features-section">
      <div class="section-wrap">
        <div class="section-tag">Recursos</div>
        <h2 class="section-h2">Tudo que você precisa<br/><span class="text-blue">para vender online</span></h2>
        <div class="features-grid">
          <div v-for="f in features" :key="f.title" class="feat-card">
            <div class="feat-icon">{{ f.icon }}</div>
            <h3 class="feat-title">{{ f.title }}</h3>
            <p class="feat-desc">{{ f.desc }}</p>
          </div>
        </div>
      </div>
    </section>

    <section class="faq-section" id="faq">
      <div class="section-wrap faq-layout">
        <div class="faq-left">
          <div class="section-tag">Dúvidas</div>
          <h2 class="section-h2">Perguntas<br/><span class="text-blue">frequentes</span></h2>
          <p class="section-sub">Não encontrou o que procura? Fale com o suporte.</p>
          <button class="btn-outline-blue" @click="$router.push('/minha-loja')">Falar com suporte</button>
        </div>
        <div class="faq-right">
          <div v-for="(faq, i) in faqs" :key="i" class="faq-item" :class="{ open: openFaq === i }" @click="toggleFaq(i)">
            <div class="faq-q">
              {{ faq.q }}
              <svg class="faq-chevron" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><polyline points="6 9 12 15 18 9"/></svg>
            </div>
            <div class="faq-a" v-show="openFaq === i">{{ faq.a }}</div>
          </div>
        </div>
      </div>
    </section>

    <section class="cta-section">
      <div class="cta-glow"></div>
      <div class="cta-inner">
        <div class="cta-eyebrow">Comece hoje mesmo · É grátis</div>
        <h2 class="cta-h2">Pronto para ter<br/><span class="text-blue">sua própria loja?</span></h2>
        <p class="cta-sub">Junte-se a centenas de droppers que já vendem com sua marca.</p>
        <button class="btn-solid btn-large" @click="$router.push('/minha-loja')">
          Criar minha loja agora
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M5 12h14M12 5l7 7-7 7"/></svg>
        </button>
        <p class="cta-note">Sem cartão de crédito &nbsp;·&nbsp; Sem contrato &nbsp;·&nbsp; Cancele quando quiser</p>
      </div>
    </section>

  </div>
</template>

<script setup>
import { ref } from 'vue'

const themes = [
  { name: 'Fitness', slug: 'maxforce', primary: '#EF4444', secondary: '#F97316', accent: '#EF4444', bg: '#FFF7F7', slogan: 'Supere seus limites' },
  { name: 'Tech',    slug: 'nextech',  primary: '#2563EB', secondary: '#60A5FA', accent: '#2563EB', bg: '#EFF6FF', slogan: 'O futuro chegou' },
  { name: 'Natural', slug: 'verdepuro',primary: '#16A34A', secondary: '#65A30D', accent: '#16A34A', bg: '#F0FDF4', slogan: 'Natureza e bem-estar' },
  { name: 'Luxo',    slug: 'auralux',  primary: '#92400E', secondary: '#D97706', accent: '#B45309', bg: '#FFFBEB', slogan: 'Sofisticação em cada detalhe' },
]
const mockStore = ref(themes[1])

const steps = [
  { icon: '✍️', title: 'Descreva sua loja', desc: 'Escreva o nicho e estilo em português. A IA entende o contexto e cria tudo automaticamente.' },
  { icon: '🤖', title: 'IA gera o tema',    desc: 'Em segundos você recebe nome, slogan, paleta de cores e identidade visual pronta.' },
  { icon: '🚀', title: 'Publique e venda',  desc: 'Ajuste os detalhes, escolha os produtos e ative. Seu link já está no ar.' },
]

const aiExamples = [
  { prompt: 'Loja de suplementos fitness',   name: 'MaxForce Store',  slogan: 'Potência máxima. Resultados reais.', primary: '#EF4444', secondary: '#F97316', bg: '#FFF7F7', style: 'BOLD' },
  { prompt: 'Produtos de beleza femininos',  name: 'Belle & Glow',   slogan: 'Sua beleza, sua essência.',          primary: '#EC4899', secondary: '#F472B6', bg: '#FDF2F8', style: 'ELEGANT' },
  { prompt: 'Gadgets e tecnologia',          name: 'NexTech Shop',   slogan: 'Inovação no seu bolso.',             primary: '#2563EB', secondary: '#3B82F6', bg: '#EFF6FF', style: 'MODERN' },
  { prompt: 'Produtos naturais e orgânicos', name: 'Verde Puro',     slogan: 'Da natureza para você.',             primary: '#16A34A', secondary: '#65A30D', bg: '#F0FDF4', style: 'MINIMAL' },
]
const activeExample = ref(aiExamples[0])

const levels = [
  { name: 'Bronze',   gem: '🥉', color: '#92400E', discount: '5%',  minSales: '0',       perks: ['Acesso ao catálogo', 'Loja white label', 'Suporte básico'],         featured: false },
  { name: 'Silver',   gem: '🥈', color: '#475569', discount: '10%', minSales: '5.000',   perks: ['Tudo do Bronze', 'Desconto maior', 'Relatórios básicos'],          featured: false },
  { name: 'Gold',     gem: '🥇', color: '#D97706', discount: '15%', minSales: '20.000',  perks: ['Tudo do Silver', 'Suporte prioritário', 'Relatórios avançados'],   featured: true  },
  { name: 'Platinum', gem: '💎', color: '#2563EB', discount: '22%', minSales: '60.000',  perks: ['Tudo do Gold', 'Gerente dedicado', 'Analytics completo'],          featured: false },
  { name: 'Diamond',  gem: '💠', color: '#1D4ED8', discount: '30%', minSales: '150.000', perks: ['Tudo do Platinum', 'Máximo desconto', 'Acesso VIP'],               featured: false },
]

const features = [
  { icon: '🎨', title: 'Design personalizado',  desc: 'Cores, logo e banner 100% seus. Seus clientes nunca saberão que é GoDrop.' },
  { icon: '🔗', title: 'Link exclusivo',         desc: 'godrop.com.br/loja/seu-nome. Compartilhe onde quiser.' },
  { icon: '📦', title: 'Sem estoque',            desc: 'GoDrop cuida do armazenamento, embalagem e entrega.' },
  { icon: '💳', title: 'Pagamento automático',   desc: 'Receba sua margem automaticamente a cada venda.' },
  { icon: '📊', title: 'Dashboard de vendas',    desc: 'Acompanhe vendas e receita em tempo real.' },
  { icon: '🤖', title: 'IA de marca',            desc: 'Nome, slogan e paleta gerados pela IA baseados no seu nicho.' },
  { icon: '📱', title: '100% responsivo',        desc: 'Funciona perfeitamente em celular, tablet e desktop.' },
  { icon: '🔒', title: 'Seguro e confiável',     desc: 'Pagamentos via Stripe com segurança bancária.' },
]

const faqs = [
  { q: 'Preciso ter CNPJ para criar uma loja?', a: 'Sim, para se tornar um dropper oficial é necessário CNPJ. Isso garante segurança jurídica para você e seus clientes.' },
  { q: 'Como funciona o pagamento das minhas comissões?', a: 'A cada venda na sua loja você recebe automaticamente a diferença entre o preço de atacado (com seu desconto de nível) e o preço que você definiu.' },
  { q: 'Posso personalizar os preços dos produtos?', a: 'Sim. Você define o preço final que seus clientes pagam. Quanto maior a diferença, maior sua margem.' },
  { q: 'O cliente sabe que é uma loja GoDrop?', a: 'Não necessariamente. Sua loja tem identidade visual própria. Há um discreto "Powered by GoDrop" no rodapé.' },
  { q: 'Como subo de nível?', a: 'Automaticamente conforme o volume de vendas. O sistema calcula em tempo real e você é notificado ao subir de nível.' },
  { q: 'Posso ter mais de uma loja?', a: 'Atualmente cada dropper pode ter uma loja ativa. Multi-loja está no roadmap.' },
]

const openFaq = ref(null)
const toggleFaq = (i) => { openFaq.value = openFaq.value === i ? null : i }
const scrollTo  = (id) => document.getElementById(id)?.scrollIntoView({ behavior: 'smooth' })
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=DM+Serif+Display:ital@0;1&display=swap');

*, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

/* ── TOKENS ───────────────────────────────────────── */
.landing {
  --blue-50:  #EFF6FF;
  --blue-100: #DBEAFE;
  --blue-200: #BFDBFE;
  --blue-400: #60A5FA;
  --blue-600: #2563EB;
  --blue-700: #1D4ED8;
  --blue-900: #1E3A5F;
  --text:     #0F172A;
  --text-2:   #475569;
  --text-3:   #94A3B8;
  --border:   #E2E8F0;
  --bg:       #FFFFFF;
  --bg-2:     #F8FAFF;

  font-family: 'Plus Jakarta Sans', sans-serif;
  background: var(--bg);
  color: var(--text);
  overflow-x: hidden;
}

.text-blue {
  color: var(--blue-600);
}

/* ── NAV ──────────────────────────────────────────── */
.nav {
  position: fixed;
  top: 0; left: 0; right: 0;
  z-index: 200;
  background: rgba(255,255,255,.92);
  backdrop-filter: blur(16px);
  border-bottom: 1px solid var(--border);
}

.nav-inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 40px;
  height: 60px;
  display: flex;
  align-items: center;
  gap: 48px;
}

.nav-brand {
  display: flex;
  align-items: center;
  gap: 9px;
  flex-shrink: 0;
}

.brand-name {
  font-family: 'DM Serif Display', serif;
  font-size: 20px;
  font-weight: 400;
  color: var(--blue-700);
  letter-spacing: -.01em;
}

.nav-links {
  display: flex;
  gap: 32px;
  flex: 1;
}

.nav-link {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-2);
  cursor: pointer;
  text-decoration: none;
  transition: color .15s;
}

.nav-link:hover { color: var(--blue-600); }

.nav-cta {
  display: flex;
  align-items: center;
  gap: 7px;
  margin-left: auto;
  padding: 9px 20px;
  background: var(--blue-600);
  color: white;
  border: none;
  border-radius: 10px;
  font: 700 13.5px 'Plus Jakarta Sans', sans-serif;
  cursor: pointer;
  transition: background .15s, transform .1s, box-shadow .2s;
  box-shadow: 0 2px 12px rgba(37,99,235,.3);
}
.nav-cta:hover { background: var(--blue-700); transform: translateY(-1px); box-shadow: 0 4px 20px rgba(37,99,235,.4); }

/* ── HERO ─────────────────────────────────────────── */
.hero {
  position: relative;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
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
  opacity: .5;
  pointer-events: none;
}

.hero-inner {
  position: relative;
  z-index: 2;
  max-width: 1200px;
  margin: 0 auto;
  padding: 100px 40px 60px;
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
  font-size: clamp(44px, 5.5vw, 72px);
  font-weight: 400;
  line-height: 1.05;
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

.hero-btns {
  display: flex;
  gap: 12px;
  margin-bottom: 48px;
  flex-wrap: wrap;
}

.btn-solid {
  display: inline-flex;
  align-items: center;
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
.btn-solid:hover { background: var(--blue-700); transform: translateY(-2px); box-shadow: 0 8px 28px rgba(37,99,235,.45); }
.btn-solid.btn-large { padding: 16px 32px; font-size: 16px; border-radius: 14px; }

.btn-outline {
  display: inline-flex;
  align-items: center;
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
.btn-outline:hover { border-color: var(--blue-200); color: var(--blue-600); background: var(--blue-50); }

.btn-outline-blue {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 12px 22px;
  background: var(--blue-50);
  color: var(--blue-600);
  border: 1.5px solid var(--blue-200);
  border-radius: 12px;
  font: 700 14px 'Plus Jakarta Sans', sans-serif;
  cursor: pointer;
  transition: all .15s;
}
.btn-outline-blue:hover { background: var(--blue-100); border-color: var(--blue-400); }

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

.trust-stat { display: flex; flex-direction: column; gap: 2px; }
.ts-num { font-family: 'DM Serif Display', serif; font-size: 26px; font-weight: 400; color: var(--text); letter-spacing: -.02em; }
.ts-lbl { font-size: 11px; font-weight: 600; color: var(--text-3); text-transform: uppercase; letter-spacing: .06em; }
.trust-sep { width: 1px; height: 36px; background: var(--border); }

/* ── BROWSER MOCKUP ───────────────────────────────── */
.hero-right { position: relative; }

.browser {
  background: white;
  border: 1.5px solid var(--border);
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(37,99,235,.1), 0 4px 12px rgba(0,0,0,.05);
  transition: all .3s;
}

.browser-top {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 11px 14px;
  background: var(--bg-2);
  border-bottom: 1px solid var(--border);
}

.browser-circles { display: flex; gap: 5px; }
.bc { width: 10px; height: 10px; border-radius: 50%; }
.bc.red    { background: #FF5F57; }
.bc.yellow { background: #FEBC2E; }
.bc.green  { background: #28C840; }

.browser-bar {
  flex: 1;
  padding: 5px 12px;
  background: white;
  border: 1px solid var(--border);
  border-radius: 6px;
  font-size: 11.5px;
  color: var(--text-3);
  font-weight: 500;
}
.browser-bar strong { color: var(--blue-600); font-weight: 700; }
.browser-spacer { width: 44px; }

.browser-body { transition: background .4s; }

.mock-nav {
  padding: 16px 20px;
  transition: background .4s;
  display: flex;
  flex-direction: column;
  gap: 3px;
}
.mock-logo-text { font-family: 'DM Serif Display', serif; font-size: 18px; color: white; font-weight: 400; }
.mock-tagline { font-size: 11px; color: rgba(255,255,255,.75); font-weight: 500; }

.mock-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
  padding: 14px;
}
.mock-card {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid rgba(0,0,0,.04);
}
.mock-thumb { height: 64px; transition: background .4s; }
.mock-lines { padding: 8px 8px 10px; display: flex; flex-direction: column; gap: 5px; }
.mock-line { height: 7px; border-radius: 4px; background: #E2E8F0; }
.mock-line.long { width: 80%; }
.mock-line.short { width: 45%; }

.theme-row {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 12px;
  padding: 10px 14px;
  background: white;
  border: 1px solid var(--border);
  border-radius: 10px;
}
.theme-label { font-size: 12.5px; font-weight: 600; color: var(--text-3); }
.theme-dots { display: flex; gap: 7px; }
.theme-dot {
  width: 20px; height: 20px;
  border-radius: 50%;
  border: 2px solid transparent;
  cursor: pointer;
  transition: all .15s;
  outline: none;
}
.theme-dot.active { border-color: white; box-shadow: 0 0 0 2px var(--blue-600); transform: scale(1.15); }
.theme-name { font-size: 12px; font-weight: 700; color: var(--text-2); margin-left: auto; }

.hero-badge-float {
  position: absolute;
  bottom: -14px;
  left: -20px;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 9px 14px;
  background: white;
  border: 1.5px solid var(--blue-200);
  border-radius: 50px;
  font-size: 12.5px;
  font-weight: 700;
  color: var(--blue-700);
  box-shadow: 0 4px 16px rgba(37,99,235,.15);
  white-space: nowrap;
  animation: badgeFloat 4s ease-in-out infinite;
}
@keyframes badgeFloat {
  0%,100% { transform: translateY(0); }
  50%     { transform: translateY(-6px); }
}

/* ── SCROLL HINT ──────────────────────────────────── */
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
  width: 1px;
  height: 40px;
  background: linear-gradient(var(--blue-200), transparent);
  animation: scrollPulse 2s ease-in-out infinite;
}
@keyframes scrollPulse {
  0%,100% { opacity: 1; }
  50%     { opacity: .3; }
}

/* ── SECTIONS SHARED ──────────────────────────────── */
.section-wrap {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 40px;
}

.section-tag {
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

.section-h2 {
  font-family: 'DM Serif Display', serif;
  font-size: clamp(30px, 3.5vw, 48px);
  font-weight: 400;
  line-height: 1.1;
  letter-spacing: -.03em;
  color: var(--text);
  margin-bottom: 12px;
}

.section-sub {
  font-size: 16px;
  color: var(--text-2);
  line-height: 1.7;
  margin-bottom: 52px;
}

/* ── HOW IT WORKS ─────────────────────────────────── */
.how-section {
  padding: 120px 0;
  background: var(--bg-2);
  border-top: 1px solid var(--border);
}

.steps-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2px;
  background: var(--border);
  border-radius: 20px;
  overflow: hidden;
  border: 1.5px solid var(--border);
}

.step-card {
  position: relative;
  background: white;
  padding: 48px 36px;
  transition: background .2s;
}
.step-card:hover { background: var(--blue-50); }

.step-num {
  font-family: 'DM Serif Display', serif;
  font-size: 64px;
  font-weight: 400;
  color: var(--blue-100);
  line-height: 1;
  margin-bottom: 16px;
  letter-spacing: -.04em;
}

.step-icon-box {
  font-size: 36px;
  margin-bottom: 18px;
  line-height: 1;
}

.step-title {
  font-size: 17px;
  font-weight: 800;
  color: var(--text);
  margin-bottom: 10px;
  letter-spacing: -.01em;
}

.step-desc {
  font-size: 14px;
  color: var(--text-2);
  line-height: 1.65;
}

.step-connector {
  position: absolute;
  top: 50%;
  right: -16px;
  transform: translateY(-50%);
  z-index: 2;
  background: white;
  border-radius: 50%;
  border: 1px solid var(--border);
  padding: 4px;
  box-sizing: content-box;
}

/* ── AI SECTION ───────────────────────────────────── */
.ai-section {
  padding: 120px 0;
  background: white;
  border-top: 1px solid var(--border);
  position: relative;
  overflow: hidden;
}

.ai-bg-accent {
  position: absolute;
  top: 0; left: 0; right: 0;
  height: 4px;
  background: linear-gradient(90deg, var(--blue-600), var(--blue-400), #818CF8);
}

.ai-layout {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 80px;
  align-items: start;
}

.ai-prompts {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.ai-prompt-btn {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 13px 18px;
  background: var(--bg-2);
  border: 1.5px solid var(--border);
  border-radius: 12px;
  font: 500 14px 'Plus Jakarta Sans', sans-serif;
  color: var(--text-2);
  font-style: italic;
  cursor: pointer;
  text-align: left;
  transition: all .15s;
}
.ai-prompt-btn svg { flex-shrink: 0; color: var(--text-3); transition: color .15s; }
.ai-prompt-btn:hover { border-color: var(--blue-200); color: var(--text); background: var(--blue-50); }
.ai-prompt-btn.active {
  border-color: var(--blue-600);
  background: var(--blue-50);
  color: var(--blue-700);
  font-weight: 700;
}
.ai-prompt-btn.active svg { color: var(--blue-600); }

.ai-result-card {
  background: white;
  border: 1.5px solid var(--border);
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 12px 40px rgba(37,99,235,.08), 0 2px 8px rgba(0,0,0,.04);
}

.ai-result-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 20px;
  background: var(--bg-2);
  border-bottom: 1px solid var(--border);
}

.ai-badge { font-size: 13px; font-weight: 700; color: var(--blue-600); }
.ai-time  { font-size: 11.5px; font-weight: 600; color: var(--text-3); background: var(--blue-50); padding: 3px 10px; border-radius: 20px; }

.ai-preview {
  padding: 32px 24px;
  transition: background .4s;
}

.aip-tag   { font-size: 10px; font-weight: 700; letter-spacing: .12em; text-transform: uppercase; color: rgba(255,255,255,.6); margin-bottom: 10px; }
.aip-name  { font-family: 'DM Serif Display', serif; font-size: 28px; color: white; margin-bottom: 6px; letter-spacing: -.02em; }
.aip-slogan{ font-size: 14px; color: rgba(255,255,255,.85); font-weight: 500; }

.ai-fields {
  display: flex;
  flex-direction: column;
  gap: 0;
}

.ai-field {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 13px 20px;
  border-bottom: 1px solid var(--border);
}
.ai-field:last-child { border-bottom: none; }

.af-label { font-size: 12px; font-weight: 600; color: var(--text-3); text-transform: uppercase; letter-spacing: .08em; }
.af-val   { font-size: 13.5px; font-weight: 700; color: var(--text); max-width: 220px; text-align: right; }
.af-tag   { font-size: 11.5px; font-weight: 800; padding: 3px 10px; background: var(--blue-50); color: var(--blue-700); border-radius: 20px; letter-spacing: .06em; }
.af-colors{ display: flex; gap: 7px; }
.af-dot   { width: 20px; height: 20px; border-radius: 50%; border: 2px solid white; box-shadow: 0 1px 4px rgba(0,0,0,.12); }

.ai-use-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  width: 100%;
  padding: 14px;
  background: var(--blue-600);
  color: white;
  border: none;
  font: 700 14px 'Plus Jakarta Sans', sans-serif;
  cursor: pointer;
  transition: background .15s;
}
.ai-use-btn:hover { background: var(--blue-700); }

/* ── LEVELS ───────────────────────────────────────── */
.levels-section {
  padding: 120px 0;
  background: var(--bg-2);
  border-top: 1px solid var(--border);
}

.levels-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 14px;
}

.level-card {
  background: white;
  border: 1.5px solid var(--border);
  border-radius: 18px;
  padding: 28px 18px;
  text-align: center;
  position: relative;
  transition: transform .2s, box-shadow .2s, border-color .2s;
}
.level-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(37,99,235,.1);
  border-color: var(--blue-200);
}
.level-card--featured {
  border-color: var(--blue-600);
  background: var(--blue-50);
  box-shadow: 0 8px 32px rgba(37,99,235,.12);
}
.level-badge {
  position: absolute;
  top: -12px; left: 50%;
  transform: translateX(-50%);
  background: var(--blue-600);
  color: white;
  font-size: 10px;
  font-weight: 800;
  letter-spacing: .08em;
  text-transform: uppercase;
  padding: 4px 14px;
  border-radius: 100px;
  white-space: nowrap;
  box-shadow: 0 3px 10px rgba(37,99,235,.35);
}

.level-gem   { font-size: 32px; margin-bottom: 8px; }
.level-name  { font-size: 15px; font-weight: 800; margin-bottom: 16px; letter-spacing: -.01em; }
.level-pct   { font-family: 'DM Serif Display', serif; font-size: 38px; font-weight: 400; color: var(--text); line-height: 1; letter-spacing: -.04em; }
.level-pct-label { font-size: 11px; color: var(--text-3); font-weight: 600; margin: 4px 0 14px; }
.level-req   { font-size: 11.5px; font-weight: 600; color: var(--text-2); padding-bottom: 14px; border-bottom: 1px solid var(--border); margin-bottom: 14px; }

.level-perks { list-style: none; display: flex; flex-direction: column; gap: 8px; }
.perk-item { display: flex; align-items: center; gap: 7px; font-size: 12.5px; color: var(--text-2); font-weight: 500; text-align: left; }

/* ── FEATURES ─────────────────────────────────────── */
.features-section {
  padding: 120px 0;
  background: white;
  border-top: 1px solid var(--border);
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.feat-card {
  padding: 28px 24px;
  background: var(--bg-2);
  border: 1.5px solid var(--border);
  border-radius: 16px;
  transition: border-color .2s, transform .2s, box-shadow .2s;
}
.feat-card:hover {
  border-color: var(--blue-200);
  transform: translateY(-3px);
  box-shadow: 0 8px 24px rgba(37,99,235,.08);
  background: white;
}
.feat-icon  { font-size: 28px; margin-bottom: 14px; }
.feat-title { font-size: 15px; font-weight: 800; color: var(--text); margin-bottom: 8px; letter-spacing: -.01em; }
.feat-desc  { font-size: 13.5px; color: var(--text-2); line-height: 1.6; }

/* ── FAQ ──────────────────────────────────────────── */
.faq-section {
  padding: 120px 0;
  background: var(--bg-2);
  border-top: 1px solid var(--border);
}

.faq-layout {
  display: grid;
  grid-template-columns: 340px 1fr;
  gap: 80px;
  align-items: start;
}

.faq-left .section-sub { margin-bottom: 28px; }

.faq-item {
  background: white;
  border: 1.5px solid var(--border);
  border-radius: 14px;
  margin-bottom: 8px;
  cursor: pointer;
  transition: border-color .15s;
  overflow: hidden;
}
.faq-item:hover { border-color: var(--blue-200); }
.faq-item.open  { border-color: var(--blue-600); }

.faq-q {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding: 18px 20px;
  font-size: 14.5px;
  font-weight: 700;
  color: var(--text);
}

.faq-chevron {
  flex-shrink: 0;
  color: var(--text-3);
  transition: transform .25s, color .15s;
}
.faq-item.open .faq-chevron { transform: rotate(180deg); color: var(--blue-600); }

.faq-a {
  padding: 0 20px 18px;
  font-size: 14px;
  color: var(--text-2);
  line-height: 1.7;
}

/* ── CTA ──────────────────────────────────────────── */
.cta-section {
  padding: 120px 0;
  background: white;
  border-top: 1px solid var(--border);
  text-align: center;
  position: relative;
  overflow: hidden;
}

.cta-glow {
  position: absolute;
  top: 50%; left: 50%;
  transform: translate(-50%, -50%);
  width: 700px; height: 400px;
  background: radial-gradient(ellipse, var(--blue-50) 0%, transparent 70%);
  pointer-events: none;
}

.cta-inner { position: relative; z-index: 1; }

.cta-eyebrow {
  font-size: 11px;
  font-weight: 700;
  letter-spacing: .14em;
  text-transform: uppercase;
  color: var(--blue-600);
  margin-bottom: 16px;
}

.cta-h2 {
  font-family: 'DM Serif Display', serif;
  font-size: clamp(36px, 5vw, 64px);
  font-weight: 400;
  line-height: 1.1;
  letter-spacing: -.04em;
  color: var(--text);
  margin-bottom: 18px;
}

.cta-sub {
  font-size: 17px;
  color: var(--text-2);
  margin-bottom: 40px;
  font-weight: 500;
}

.cta-note {
  font-size: 12.5px;
  color: var(--text-3);
  margin-top: 16px;
  font-weight: 500;
  letter-spacing: .02em;
}

/* ── RESPONSIVE ───────────────────────────────────── */
@media (max-width: 1024px) {
  .hero-inner    { grid-template-columns: 1fr; padding: 100px 24px 60px; }
  .hero-right    { display: none; }
  .ai-layout     { grid-template-columns: 1fr; }
  .levels-grid   { grid-template-columns: repeat(3, 1fr); }
  .features-grid { grid-template-columns: repeat(2, 1fr); }
  .steps-grid    { grid-template-columns: 1fr; gap: 2px; }
  .step-connector{ display: none; }
  .faq-layout    { grid-template-columns: 1fr; gap: 40px; }
}

@media (max-width: 640px) {
  .section-wrap  { padding: 0 20px; }
  .hero-inner    { padding: 90px 20px 48px; }
  .levels-grid   { grid-template-columns: repeat(2, 1fr); }
  .features-grid { grid-template-columns: 1fr; }
  .nav-links     { display: none; }
}
</style>
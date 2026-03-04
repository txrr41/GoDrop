<template>
  <div class="landing">
    <nav class="nav">
      <div class="nav-inner">
        <span class="nav-logo">GoDrop</span>
        <button class="nav-cta" @click="$router.push('/minha-loja')">Criar Minha Loja</button>
      </div>
    </nav>

    <section class="hero">
      <div class="hero-bg">
        <div class="orb orb-1"></div>
        <div class="orb orb-2"></div>
        <div class="orb orb-3"></div>
        <div class="grid-lines"></div>
      </div>

      <div class="hero-inner">
        <div class="hero-content">
          <div class="hero-badge">
            <span class="badge-dot"></span>
            Novo · Loja White Label com IA
          </div>
          <h1 class="hero-title">
            Sua marca.<br/>
            <span class="gradient-text">Sua loja.<br/></span>
            Seu dinheiro.
          </h1>
          <p class="hero-sub">
            Crie uma loja virtual personalizada em minutos com inteligência artificial.
            Venda os produtos GoDrop com sua própria identidade visual — sem estoque, sem risco.
          </p>
          <div class="hero-actions">
            <button class="btn-primary" @click="$router.push('/minha-loja')">
              <span>Criar Minha Loja Grátis</span>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
                <path d="M5 12h14M12 5l7 7-7 7"/>
              </svg>
            </button>
            <button class="btn-ghost" @click="scrollTo('how')">Ver como funciona</button>
          </div>
          <div class="hero-stats">
            <div class="stat"><span class="stat-num">2min</span><span class="stat-label">Para criar sua loja</span>
            </div>
            <div class="stat-div"></div>
            <div class="stat"><span class="stat-num">30%</span><span class="stat-label">De desconto Diamond</span></div>
            <div class="stat-div"></div>
            <div class="stat"><span class="stat-num">100%</span><span class="stat-label">Sua identidade visual</span>
            </div>
          </div>
        </div>

        <div class="hero-mockup">
          <div class="mockup-browser">
            <div class="browser-bar">
              <div class="browser-dots"><span></span><span></span><span></span></div>
              <div class="browser-url">godrop.com.br/loja/<strong>sua-loja</strong></div>
            </div>
            <div class="browser-body" :style="`background: ${mockStore.bg}`">
              <div class="mock-header" :style="`background: ${mockStore.primary}`">
                <div class="mock-logo">{{ mockStore.name }}</div>
                <div class="mock-slogan">{{ mockStore.slogan }}</div>
              </div>
              <div class="mock-products">
                <div class="mock-product" v-for="p in 3" :key="p">
                  <div class="mock-img" :style="`background: ${mockStore.secondary}15`"></div>
                  <div class="mock-info">
                    <div class="mock-name"></div>
                    <div class="mock-price" :style="`color: ${mockStore.primary}`"></div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="theme-switcher">
            <span class="ts-label">Escolha um tema:</span>
            <div class="ts-options">
              <button v-for="t in themes" :key="t.name"
                      class="ts-btn"
                      :class="{ active: mockStore === t }"
                      :style="`background: ${t.primary}`"
                      @click="mockStore = t"
              ></button>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="how" id="how">
      <div class="section-inner">
        <div class="section-tag">Como funciona</div>
        <h2 class="section-title">De zero à sua loja em <span class="gradient-text">3 passos</span></h2>
        <p class="section-sub">Nossa IA cuida do design. Você cuida das vendas.</p>

        <div class="steps">
          <div class="step" v-for="(step, i) in steps" :key="i">
            <div class="step-number">0{{ i + 1 }}</div>
            <div class="step-icon">{{ step.icon }}</div>
            <h3 class="step-title">{{ step.title }}</h3>
            <p class="step-desc">{{ step.desc }}</p>
            <div class="step-arrow" v-if="i < steps.length - 1">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M5 12h14M12 5l7 7-7 7"/>
              </svg>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="ai-section">
      <div class="ai-bg">
        <div class="ai-orb"></div>
      </div>
      <div class="section-inner ai-inner">
        <div class="ai-text">
          <div class="section-tag">Inteligência Artificial</div>
          <h2 class="section-title">Descreva sua loja.<br/><span class="gradient-text">A IA cria o resto.</span></h2>
          <p class="section-sub">Digite o nicho da sua loja em português e nossa IA gera automaticamente nome, slogan,
            paleta de cores e identidade visual completa.</p>

          <div class="ai-examples">
            <div class="ai-example" v-for="ex in aiExamples" :key="ex.prompt" @click="activeExample = ex"
                 :class="{ active: activeExample === ex }">
              <span class="ex-prompt">"{{ ex.prompt }}"</span>
            </div>
          </div>
        </div>

        <div class="ai-output" v-if="activeExample">
          <div class="ai-card">
            <div class="ai-card-header">
              <span class="ai-label">✨ Gerado pela IA</span>
            </div>
            <div class="ai-result">
              <div class="ai-store-preview"
                   :style="`background: linear-gradient(135deg, ${activeExample.primary}, ${activeExample.secondary})`">
                <div class="aip-name">{{ activeExample.name }}</div>
                <div class="aip-slogan">{{ activeExample.slogan }}</div>
              </div>
              <div class="ai-details">
                <div class="ai-detail-row">
                  <span class="adr-label">Nome</span>
                  <span class="adr-val">{{ activeExample.name }}</span>
                </div>
                <div class="ai-detail-row">
                  <span class="adr-label">Slogan</span>
                  <span class="adr-val">{{ activeExample.slogan }}</span>
                </div>
                <div class="ai-detail-row">
                  <span class="adr-label">Estilo</span>
                  <span class="adr-val adr-badge">{{ activeExample.style }}</span>
                </div>
                <div class="ai-detail-row">
                  <span class="adr-label">Cores</span>
                  <div class="adr-colors">
                    <span class="color-dot" :style="`background: ${activeExample.primary}`"></span>
                    <span class="color-dot" :style="`background: ${activeExample.secondary}`"></span>
                    <span class="color-dot" :style="`background: ${activeExample.bg}`"></span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="levels-section">
      <div class="section-inner">
        <div class="section-tag">Programa de Níveis</div>
        <h2 class="section-title">Quanto mais você vende,<br/><span class="gradient-text">mais você ganha</span></h2>
        <p class="section-sub">Suba de nível e desbloqueie descontos maiores nos produtos. Sua margem cresce com
          você.</p>

        <div class="levels">
          <div class="level-card" v-for="level in levels" :key="level.name" :class="{ featured: level.featured }">
            <div class="level-gem">{{ level.gem }}</div>
            <div class="level-name" :style="`color: ${level.color}`">{{ level.name }}</div>
            <div class="level-discount">{{ level.discount }}</div>
            <div class="level-discount-label">de desconto</div>
            <div class="level-req">A partir de R$ {{ level.minSales }}</div>
            <div class="level-perks">
              <div class="perk" v-for="p in level.perks" :key="p">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
                  <polyline points="20 6 9 17 4 12"/>
                </svg>
                {{ p }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="features-section">
      <div class="section-inner">
        <div class="section-tag">Recursos</div>
        <h2 class="section-title">Tudo que você precisa<br/><span class="gradient-text">para vender online</span></h2>
        <div class="features-grid">
          <div class="feature-card" v-for="f in features" :key="f.title">
            <div class="feature-icon">{{ f.icon }}</div>
            <h3 class="feature-title">{{ f.title }}</h3>
            <p class="feature-desc">{{ f.desc }}</p>
          </div>
        </div>
      </div>
    </section>

    <section class="faq-section">
      <div class="section-inner">
        <div class="section-tag">Dúvidas</div>
        <h2 class="section-title">Perguntas <span class="gradient-text">frequentes</span></h2>
        <div class="faq-list">
          <div class="faq-item" v-for="(faq, i) in faqs" :key="i" @click="toggleFaq(i)"
               :class="{ open: openFaq === i }">
            <div class="faq-q">
              {{ faq.q }}
              <svg class="faq-arrow" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                   stroke-width="2.5">
                <polyline points="6 9 12 15 18 9"/>
              </svg>
            </div>
            <div class="faq-a" v-show="openFaq === i">{{ faq.a }}</div>
          </div>
        </div>
      </div>
    </section>

    <section class="cta-section">
      <div class="cta-bg">
        <div class="cta-orb"></div>
      </div>
      <div class="cta-inner">
        <h2 class="cta-title">Pronto para ter<br/><span class="gradient-text">sua própria loja?</span></h2>
        <p class="cta-sub">Junte-se a centenas de droppers que já vendem com sua marca. Comece agora, é grátis.</p>
        <button class="btn-primary btn-large" @click="$router.push('/minha-loja')">
          <span>Criar Minha Loja Agora</span>
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
            <path d="M5 12h14M12 5l7 7-7 7"/>
          </svg>
        </button>
        <p class="cta-note">Sem cartão de crédito · Sem contrato · Cancele quando quiser</p>
      </div>
    </section>

  </div>
</template>

<script setup>
import {ref} from 'vue'

const themes = [
  {name: 'Fitness', primary: '#EF4444', secondary: '#F97316', bg: '#FFF7F7', slogan: 'Supere seus limites'},
  {name: 'Tech', primary: '#2563EB', secondary: '#60A5FA', bg: '#EFF6FF', slogan: 'O futuro chegou'},
  {name: 'Natural', primary: '#16A34A', secondary: '#65A30D', bg: '#F0FDF4', slogan: 'Natureza e bem-estar'},
  {name: 'Luxo', primary: '#B45309', secondary: '#D97706', bg: '#FFFBEB', slogan: 'Sofisticação em cada detalhe'},
]

const mockStore = ref(themes[0])

const steps = [
  {
    icon: '✍️',
    title: 'Descreva sua loja',
    desc: 'Escreva em português o nicho e estilo da sua loja. A IA entende o contexto e cria tudo.'
  },
  {
    icon: '🤖',
    title: 'IA gera o tema',
    desc: 'Em segundos, receba nome, slogan, cores e identidade visual gerados automaticamente.'
  },
  {
    icon: '🚀',
    title: 'Publique e venda',
    desc: 'Personalize os detalhes, escolha os produtos e ative sua loja. Seu link já está no ar.'
  },
]

const aiExamples = [
  {
    prompt: 'Loja de suplementos fitness',
    name: 'MaxForce Store',
    slogan: 'Potência máxima. Resultados reais.',
    primary: '#EF4444',
    secondary: '#F97316',
    bg: '#FFF7F7',
    style: 'BOLD'
  },
  {
    prompt: 'Produtos de beleza femininos',
    name: 'Belle & Glow',
    slogan: 'Sua beleza, sua essência.',
    primary: '#EC4899',
    secondary: '#F472B6',
    bg: '#FDF2F8',
    style: 'ELEGANT'
  },
  {
    prompt: 'Gadgets e tecnologia',
    name: 'NexTech Shop',
    slogan: 'Inovação no seu bolso.',
    primary: '#2563EB',
    secondary: '#3B82F6',
    bg: '#EFF6FF',
    style: 'MODERN'
  },
  {
    prompt: 'Produtos naturais e orgânicos',
    name: 'Verde Puro',
    slogan: 'Da natureza para você.',
    primary: '#16A34A',
    secondary: '#65A30D',
    bg: '#F0FDF4',
    style: 'MINIMAL'
  },
]

const activeExample = ref(aiExamples[0])

const levels = [
  {
    name: 'Bronze',
    gem: '🥉',
    color: '#CD7F32',
    discount: '5%',
    minSales: '0',
    perks: ['Acesso ao catálogo', 'Loja white label', 'Suporte básico'],
    featured: false
  },
  {
    name: 'Silver',
    gem: '🥈',
    color: '#64748B',
    discount: '10%',
    minSales: '5.000',
    perks: ['Tudo do Bronze', 'Desconto maior', 'Relatórios básicos'],
    featured: false
  },
  {
    name: 'Gold',
    gem: '🥇',
    color: '#F59E0B',
    discount: '15%',
    minSales: '20.000',
    perks: ['Tudo do Silver', 'Suporte prioritário', 'Relatórios avançados'],
    featured: true
  },
  {
    name: 'Platinum',
    gem: '💎',
    color: '#60A5FA',
    discount: '22%',
    minSales: '60.000',
    perks: ['Tudo do Gold', 'Gerente dedicado', 'Analytics completo'],
    featured: false
  },
  {
    name: 'Diamond',
    gem: '💠',
    color: '#2563EB',
    discount: '30%',
    minSales: '150.000',
    perks: ['Tudo do Platinum', 'Máximo desconto', 'Acesso VIP'],
    featured: false
  },
]

const features = [
  {
    icon: '🎨',
    title: 'Design personalizado',
    desc: 'Cores, logo, banner e identidade visual 100% sua. Seus clientes nunca saberão que é GoDrop.'
  },
  {
    icon: '🔗',
    title: 'Link exclusivo',
    desc: 'Sua loja tem um endereço único: godrop.com.br/loja/seu-nome. Compartilhe onde quiser.'
  },
  {
    icon: '📦',
    title: 'Sem estoque',
    desc: 'Nunca compre produto antecipado. O GoDrop cuida do estoque, embalagem e entrega por você.'
  },
  {
    icon: '💳',
    title: 'Pagamento automático',
    desc: 'Receba automaticamente sua margem a cada venda. Sem burocracia, sem atraso.'
  },
  {
    icon: '📊',
    title: 'Dashboard de vendas',
    desc: 'Acompanhe vendas, clientes e receita em tempo real no seu painel exclusivo de dropper.'
  },
  {
    icon: '🤖',
    title: 'IA de marca',
    desc: 'Nossa IA gera nome, slogan e paleta de cores baseados na descrição do seu negócio.'
  },
  {
    icon: '📱',
    title: '100% responsivo',
    desc: 'Sua loja funciona perfeitamente em celular, tablet e computador. Sem configuração extra.'
  },
  {
    icon: '🔒',
    title: 'Seguro e confiável',
    desc: 'Pagamentos processados pelo Stripe com segurança bancária. Seus clientes estão protegidos.'
  },
]

const faqs = [
  {
    q: 'Preciso ter CNPJ para criar uma loja?',
    a: 'Sim, para se tornar um dropper oficial do GoDrop é necessário ter CNPJ. Isso garante segurança jurídica para você e seus clientes.'
  },
  {
    q: 'Como funciona o pagamento das minhas comissões?',
    a: 'A cada venda realizada na sua loja, você recebe automaticamente a diferença entre o preço de atacado (com seu desconto de nível) e o preço que você definiu para seus clientes.'
  },
  {
    q: 'Posso personalizar os preços dos produtos?',
    a: 'Você define o preço final que seus clientes vão pagar. Quanto maior a diferença entre o preço de atacado e o seu preço de venda, maior é sua margem.'
  },
  {
    q: 'O cliente sabe que é uma loja GoDrop?',
    a: 'Não necessariamente. Sua loja tem sua própria identidade visual. Porém, há um discreto badge "Powered by GoDrop" no rodapé, garantindo transparência.'
  },
  {
    q: 'Como subo de nível?',
    a: 'Seu nível sobe automaticamente conforme o volume total de vendas. O sistema calcula em tempo real e você é notificado ao subir de Bronze para Silver, Gold, Platinum e Diamond.'
  },
  {
    q: 'Posso ter mais de uma loja?',
    a: 'Atualmente cada dropper pode ter uma loja ativa. Estamos trabalhando em funcionalidade multi-loja para uma versão futura.'
  },
]

const openFaq = ref(null)
const toggleFaq = (i) => openFaq.value = openFaq.value === i ? null : i
const scrollTo = (id) => document.getElementById(id)?.scrollIntoView({behavior: 'smooth'})
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Bricolage+Grotesque:opsz,wght@12..96,300;12..96,400;12..96,500;12..96,600;12..96,700;12..96,800&family=DM+Sans:ital,opsz,wght@0,9..40,300;0,9..40,400;0,9..40,500;1,9..40,300&display=swap');

* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

.landing {
  font-family: 'DM Sans', sans-serif;
  background: #FFFFFF;
  color: #0F172A;
  overflow-x: hidden;
}

.nav {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
  padding: 0 24px;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
}

.nav-inner {
  max-width: 1200px;
  margin: 0 auto;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.nav-logo {
  font-family: 'Bricolage Grotesque', sans-serif;
  font-weight: 800;
  font-size: 22px;
  background: linear-gradient(135deg, #1E3A8A, #2563EB);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.nav-cta {
  padding: 10px 22px;
  background: linear-gradient(135deg, #2563EB, #0EA5E9);
  color: white;
  border: none;
  border-radius: 10px;
  font-family: 'DM Sans', sans-serif;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.nav-cta:hover {
  transform: translateY(-1px);
  box-shadow: 0 8px 24px rgba(37, 99, 235, 0.3);
}

.hero {
  position: relative;
  overflow: hidden;
  min-height: 100vh;
  display: flex;
  align-items: center;
}

.hero-inner {
  position: relative;
  z-index: 1;
  display: grid;
  grid-template-columns: 1fr 1fr;
  align-items: center;
  gap: 60px;
  max-width: 1300px;
  width: 100%;
  margin: 0 auto;
  padding: 100px 64px 80px;
}

.hero-bg {
  position: absolute;
  inset: 0;
  z-index: 0;
  pointer-events: none;
  overflow: hidden;
}

.orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.15;
}

.orb-1 {
  width: 500px;
  height: 500px;
  background: #3B82F6;
  top: -100px;
  left: -100px;
  animation: float1 8s ease-in-out infinite;
}

.orb-2 {
  width: 400px;
  height: 400px;
  background: #0EA5E9;
  top: 30%;
  right: -50px;
  animation: float2 10s ease-in-out infinite;
}

.orb-3 {
  width: 300px;
  height: 300px;
  background: #2563EB;
  bottom: 10%;
  left: 30%;
  animation: float3 12s ease-in-out infinite;
}

@keyframes float1 {
  0%, 100% {
    transform: translate(0, 0)
  }
  50% {
    transform: translate(30px, -30px)
  }
}

@keyframes float2 {
  0%, 100% {
    transform: translate(0, 0)
  }
  50% {
    transform: translate(-20px, 20px)
  }
}

@keyframes float3 {
  0%, 100% {
    transform: translate(0, 0)
  }
  50% {
    transform: translate(20px, -20px)
  }
}

.grid-lines {
  position: absolute;
  inset: 0;
  background-image: linear-gradient(rgba(0, 0, 0, 0.03) 1px, transparent 1px),
  linear-gradient(90deg, rgba(0, 0, 0, 0.03) 1px, transparent 1px);
  background-size: 60px 60px;
}

.hero-content {
  position: relative;
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 6px 14px;
  background: rgba(37, 99, 235, 0.1);
  border: 1px solid rgba(37, 99, 235, 0.2);
  border-radius: 100px;
  font-size: 13px;
  color: #2563EB;
  font-weight: 600;
  margin-bottom: 28px;
}

.badge-dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: #2563EB;
  animation: pulse-dot 2s ease-in-out infinite;
}

@keyframes pulse-dot {
  0%, 100% {
    opacity: 1
  }
  50% {
    opacity: 0.3
  }
}

.hero-title {
  font-family: 'Bricolage Grotesque', sans-serif;
  font-size: clamp(42px, 5vw, 72px);
  font-weight: 800;
  line-height: 1.05;
  letter-spacing: -2px;
  margin-bottom: 20px;
  color: #111827;
}

.gradient-text {
  background: linear-gradient(135deg, #2563EB, #0EA5E9);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.hero-sub {
  font-size: 17px;
  color: #4B5563;
  line-height: 1.7;
  max-width: 520px;
  margin-bottom: 36px;
}

.hero-actions {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 48px;
  flex-wrap: wrap;
}

.btn-primary {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  padding: 14px 28px;
  background: linear-gradient(135deg, #2563EB, #0EA5E9);
  color: white;
  border: none;
  border-radius: 12px;
  font-family: 'DM Sans', sans-serif;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.25s;
  box-shadow: 0 4px 20px rgba(37, 99, 235, 0.25);
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 32px rgba(37, 99, 235, 0.4);
}

.btn-primary.btn-large {
  padding: 18px 36px;
  font-size: 17px;
  border-radius: 14px;
}

.btn-ghost {
  padding: 14px 24px;
  background: transparent;
  color: #4B5563;
  border: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: 12px;
  font-family: 'DM Sans', sans-serif;
  font-size: 15px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-ghost:hover {
  border-color: rgba(0, 0, 0, 0.2);
  color: #0F172A;
  background: #F8FAFC;
}

.hero-stats {
  display: flex;
  align-items: center;
  gap: 24px;
}

.stat {
  display: flex;
  flex-direction: column;
}

.stat-num {
  font-family: 'Bricolage Grotesque', sans-serif;
  font-size: 28px;
  font-weight: 800;
  color: #0F172A;
}

.stat-label {
  font-size: 12px;
  color: #64748B;
  font-weight: 500;
}

.stat-div {
  width: 1px;
  height: 36px;
  background: rgba(0, 0, 0, 0.1);
}


.hero-mockup {
  position: relative;
}

.mockup-browser {
  background: #FFFFFF;
  border: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 24px 60px rgba(0, 0, 0, 0.08);
}

.browser-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  background: #F8FAFC;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
}

.browser-dots {
  display: flex;
  gap: 6px;
}

.browser-dots span {
  width: 10px;
  height: 10px;
  border-radius: 50%;
}

.browser-dots span:nth-child(1) {
  background: #EF4444;
}

.browser-dots span:nth-child(2) {
  background: #F59E0B;
}

.browser-dots span:nth-child(3) {
  background: #22C55E;
}

.browser-url {
  flex: 1;
  background: rgba(0, 0, 0, 0.04);
  border-radius: 6px;
  padding: 6px 12px;
  font-size: 12px;
  color: #64748B;
  font-weight: 500;
}

.browser-url strong {
  color: #2563EB;
  font-weight: 700;
}

.browser-body {
  padding: 0;
  transition: background 0.5s;
  min-height: 260px;
}

.mock-header {
  padding: 20px 24px;
  transition: background 0.5s;
}

.mock-logo {
  font-family: 'Bricolage Grotesque', sans-serif;
  font-weight: 800;
  font-size: 20px;
  color: white;
}

.mock-slogan {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.85);
  margin-top: 4px;
}

.mock-products {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
  padding: 16px;
}

.mock-product {
  background: #FFFFFF;
  border: 1px solid rgba(0, 0, 0, 0.05);
  border-radius: 8px;
  overflow: hidden;
}

.mock-img {
  height: 80px;
}

.mock-info {
  padding: 8px;
}

.mock-name {
  height: 8px;
  background: rgba(0, 0, 0, 0.1);
  border-radius: 4px;
  margin-bottom: 6px;
}

.mock-price {
  height: 10px;
  width: 60%;
  background: rgba(0, 0, 0, 0.15);
  border-radius: 4px;
}

.theme-switcher {
  margin-top: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  background: #FFFFFF;
  border: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.03);
}

.ts-label {
  font-size: 13px;
  color: #64748B;
  font-weight: 500;
}

.ts-options {
  display: flex;
  gap: 8px;
}

.ts-btn {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  border: none;
  cursor: pointer;
  transition: all 0.2s;
}

.ts-btn.active {
  box-shadow: 0 0 0 2px #FFFFFF, 0 0 0 3px #2563EB;
  transform: scale(1.1);
}


.section-inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 48px;
}

.section-tag {
  display: inline-flex;
  padding: 5px 14px;
  background: rgba(37, 99, 235, 0.1);
  border: 1px solid rgba(37, 99, 235, 0.2);
  border-radius: 100px;
  font-size: 12px;
  color: #2563EB;
  font-weight: 700;
  letter-spacing: 0.05em;
  text-transform: uppercase;
  margin-bottom: 20px;
}

.section-title {
  font-family: 'Bricolage Grotesque', sans-serif;
  font-size: clamp(32px, 4vw, 52px);
  font-weight: 800;
  line-height: 1.1;
  letter-spacing: -1.5px;
  margin-bottom: 16px;
  color: #0F172A;
}

.section-sub {
  font-size: 17px;
  color: #4B5563;
  line-height: 1.7;
  margin-bottom: 56px;
}

.how {
  padding: 120px 0;
  border-top: 1px solid rgba(0, 0, 0, 0.05);
  background: #FFFFFF;
}

.steps {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 0;
  position: relative;
}

.step {
  padding: 48px 40px;
  border: 1px solid rgba(0, 0, 0, 0.05);
  background: #FFFFFF;
  position: relative;
  transition: all 0.3s;
}

.step:hover {
  background: #F8FAFC;
  border-color: rgba(37, 99, 235, 0.15);
  z-index: 1;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.02);
}

.step:first-child {
  border-radius: 16px 0 0 16px;
}

.step:last-child {
  border-radius: 0 16px 16px 0;
}

.step-number {
  font-family: 'Bricolage Grotesque', sans-serif;
  font-size: 56px;
  font-weight: 800;
  color: rgba(37, 99, 235, 0.06);
  line-height: 1;
  margin-bottom: 16px;
}

.step-icon {
  font-size: 40px;
  margin-bottom: 20px;
}

.step-title {
  font-family: 'Bricolage Grotesque', sans-serif;
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 12px;
  color: #0F172A;
}

.step-desc {
  font-size: 15px;
  color: #64748B;
  line-height: 1.6;
}

.step-arrow {
  position: absolute;
  right: -13px;
  top: 50%;
  transform: translateY(-50%);
  z-index: 2;
  background: #FFFFFF;
  border: 1px solid rgba(0, 0, 0, 0.08);
  border-radius: 50%;
  width: 28px;
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #94A3B8;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.02);
}

.ai-section {
  padding: 120px 0;
  position: relative;
  border-top: 1px solid rgba(0, 0, 0, 0.05);
  background: #F8FAFC;
  overflow: hidden;
}

.ai-bg {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.ai-orb {
  position: absolute;
  width: 600px;
  height: 600px;
  background: radial-gradient(circle, rgba(37, 99, 235, 0.06), transparent 70%);
  right: -100px;
  top: 50%;
  transform: translateY(-50%);
}

.ai-inner {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 80px;
  align-items: center;
}

.ai-examples {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.ai-example {
  padding: 14px 20px;
  background: #FFFFFF;
  border: 1px solid rgba(0, 0, 0, 0.06);
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.02);
}

.ai-example:hover {
  border-color: rgba(37, 99, 235, 0.3);
  background: #F0F9FF;
}

.ai-example.active {
  border-color: #2563EB;
  background: #EFF6FF;
  box-shadow: 0 4px 12px rgba(37, 99, 235, 0.08);
}

.ex-prompt {
  font-size: 14px;
  color: #4B5563;
  font-style: italic;
  font-weight: 500;
}

.ai-card {
  background: #FFFFFF;
  border: 1px solid rgba(0, 0, 0, 0.06);
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.04);
}

.ai-card-header {
  padding: 14px 20px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  background: #F8FAFC;
}

.ai-label {
  font-size: 13px;
  color: #2563EB;
  font-weight: 700;
}

.ai-result {
  padding: 20px;
}

.ai-store-preview {
  border-radius: 12px;
  padding: 28px 24px;
  margin-bottom: 20px;
  transition: all 0.4s;
}

.aip-name {
  font-family: 'Bricolage Grotesque', sans-serif;
  font-size: 24px;
  font-weight: 800;
  color: white;
  margin-bottom: 6px;
}

.aip-slogan {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.85);
  font-weight: 500;
}

.ai-details {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.ai-detail-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-bottom: 12px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.04);
}

.ai-detail-row:last-child {
  border-bottom: none;
  padding-bottom: 0;
}

.adr-label {
  font-size: 13px;
  color: #64748B;
  font-weight: 500;
}

.adr-val {
  font-size: 14px;
  color: #0F172A;
  font-weight: 600;
}

.adr-badge {
  padding: 4px 10px;
  background: rgba(37, 99, 235, 0.1);
  border-radius: 100px;
  color: #2563EB;
  font-size: 12px;
  font-weight: 700;
}

.adr-colors {
  display: flex;
  gap: 8px;
}

.color-dot {
  width: 22px;
  height: 22px;
  border-radius: 50%;
  border: 2px solid #FFFFFF;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.levels-section {
  padding: 120px 0;
  background: #FFFFFF;
  border-top: 1px solid rgba(0, 0, 0, 0.05);
}

.levels {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 16px;
}

.level-card {
  padding: 28px 20px;
  background: #FFFFFF;
  border: 1px solid rgba(0, 0, 0, 0.08);
  border-radius: 16px;
  text-align: center;
  transition: all 0.3s;
  position: relative;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.02);
}

.level-card:hover {
  border-color: rgba(0, 0, 0, 0.15);
  transform: translateY(-4px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.06);
}

.level-card.featured {
  background: #F8FAFC;
  border-color: #2563EB;
  box-shadow: 0 8px 30px rgba(37, 99, 235, 0.1);
}

.level-card.featured::before {
  content: 'Popular';
  position: absolute;
  top: -14px;
  left: 50%;
  transform: translateX(-50%);
  padding: 6px 16px;
  background: linear-gradient(135deg, #2563EB, #0EA5E9);
  border-radius: 100px;
  font-size: 11px;
  font-weight: 700;
  color: white;
  letter-spacing: 0.05em;
  text-transform: uppercase;
  box-shadow: 0 4px 10px rgba(37, 99, 235, 0.3);
}

.level-gem {
  font-size: 36px;
  margin-bottom: 8px;
}

.level-name {
  font-family: 'Bricolage Grotesque', sans-serif;
  font-size: 18px;
  font-weight: 800;
  margin-bottom: 16px;
}

.level-discount {
  font-family: 'Bricolage Grotesque', sans-serif;
  font-size: 40px;
  font-weight: 800;
  color: #0F172A;
  line-height: 1;
}

.level-discount-label {
  font-size: 12px;
  color: #64748B;
  font-weight: 500;
  margin-bottom: 16px;
  margin-top: 4px;
}

.level-req {
  font-size: 12px;
  color: #4B5563;
  font-weight: 600;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
}

.perk {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: #4B5563;
  margin-bottom: 10px;
  font-weight: 500;
}

.perk svg {
  color: #2563EB;
  flex-shrink: 0;
}

.features-section {
  padding: 120px 0;
  border-top: 1px solid rgba(0, 0, 0, 0.05);
  background: #F8FAFC;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.feature-card {
  padding: 28px 24px;
  background: #FFFFFF;
  border: 1px solid rgba(0, 0, 0, 0.06);
  border-radius: 16px;
  transition: all 0.3s;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.02);
}

.feature-card:hover {
  border-color: rgba(37, 99, 235, 0.2);
  transform: translateY(-4px);
  box-shadow: 0 12px 24px rgba(37, 99, 235, 0.08);
}

.feature-icon {
  font-size: 32px;
  margin-bottom: 16px;
}

.feature-title {
  font-family: 'Bricolage Grotesque', sans-serif;
  font-size: 17px;
  font-weight: 700;
  margin-bottom: 10px;
  color: #0F172A;
}

.feature-desc {
  font-size: 14px;
  color: #64748B;
  line-height: 1.6;
}

.faq-section {
  padding: 120px 0;
  border-top: 1px solid rgba(0, 0, 0, 0.05);
  background: #FFFFFF;
}

.faq-list {
  max-width: 760px;
}

.faq-item {
  border-bottom: 1px solid rgba(0, 0, 0, 0.08);
  cursor: pointer;
  overflow: hidden;
}

.faq-q {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 22px 0;
  font-size: 16px;
  font-weight: 600;
  color: #0F172A;
  transition: color 0.2s;
}

.faq-item:hover .faq-q {
  color: #2563EB;
}

.faq-arrow {
  transition: transform 0.3s;
  flex-shrink: 0;
  color: #64748B;
}

.faq-item.open .faq-arrow {
  transform: rotate(180deg);
  color: #2563EB;
}

.faq-a {
  padding: 0 0 22px;
  font-size: 15px;
  color: #4B5563;
  line-height: 1.7;
}

.cta-section {
  padding: 120px 0;
  border-top: 1px solid rgba(0, 0, 0, 0.05);
  background: #F8FAFC;
  position: relative;
  overflow: hidden;
  text-align: center;
}

.cta-bg {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.cta-orb {
  position: absolute;
  width: 600px;
  height: 600px;
  background: radial-gradient(circle, rgba(37, 99, 235, 0.08), transparent 70%);
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}

.cta-inner {
  position: relative;
  z-index: 1;
}

.cta-title {
  font-family: 'Bricolage Grotesque', sans-serif;
  font-size: clamp(36px, 5vw, 64px);
  font-weight: 800;
  line-height: 1.1;
  letter-spacing: -2px;
  margin-bottom: 20px;
  color: #0F172A;
}

.cta-sub {
  font-size: 18px;
  color: #4B5563;
  margin-bottom: 40px;
  font-weight: 500;
}

.cta-note {
  font-size: 13px;
  color: #64748B;
  margin-top: 16px;
  font-weight: 500;
}

@media (max-width: 1024px) {
  .hero-inner {
    grid-template-columns: 1fr;
    padding: 100px 24px 60px;
  }

  .hero-mockup {
    display: none;
  }

  .ai-inner {
    grid-template-columns: 1fr;
  }

  .levels {
    grid-template-columns: repeat(3, 1fr);
  }

  .features-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .steps {
    grid-template-columns: 1fr;
  }

  .step:first-child {
    border-radius: 16px 16px 0 0;
  }

  .step:last-child {
    border-radius: 0 0 16px 16px;
  }

  .step-arrow {
    display: none;
  }
}

@media (max-width: 640px) {
  .section-inner {
    padding: 0 20px;
  }

  .hero-inner {
    padding: 90px 20px 48px;
  }

  .levels {
    grid-template-columns: 1fr 1fr;
  }

  .features-grid {
    grid-template-columns: 1fr;
  }
}
</style>
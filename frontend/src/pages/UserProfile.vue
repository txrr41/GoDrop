<template>
  <div class="profile-root">

    <!-- ════════════════════════════════════════
         DROPPER VIEW (DASHBOARD GAMIFICADO)
    ════════════════════════════════════════ -->
    <template v-if="isDropper">

      <div class="dashboard-container">

        <!-- HEADER & SAUDAÇÃO -->
        <div class="page-header">
          <div class="ph-greet">
            <p class="ph-date">{{ todayLabel }}</p>
            <h1 class="ph-title">Olá, {{ firstName }} 👋</h1>
            <p class="ph-sub">Continue vendendo para subir de nível e desbloquear mais lucros.</p>
          </div>
        </div>

        <!-- HERO CARD DE ASSINATURA (O GRANDE MOTIVADOR) -->
        <div class="tier-hero-card" :class="`tier-${levelKey}`">
          <div class="th-bg-glow"></div>

          <div class="th-content">
            <!-- Info do Nível Atual -->
            <div class="th-current">
              <div class="th-badge">
                <span class="th-icon">{{ levelIcon }}</span>
              </div>
              <div class="th-info">
                <span class="th-eyebrow">Seu Nível Atual</span>
                <h2 class="th-name">{{ levelLabel }}</h2>
                <div class="th-discount-tag">
                  <strong>{{ levelDiscount }}% OFF</strong> em todas as compras
                </div>
              </div>
            </div>

            <!-- Progresso para o Próximo Nível -->
            <div class="th-progress-area" v-if="nextLevel">
              <div class="th-prog-header">
                <div class="th-prog-target">
                  <span class="nxt-lbl">Próximo Nível: <strong>{{ nextLevel.label }}</strong> {{ nextLevelObj?.icon }}</span>
                  <span class="nxt-reward">Desbloqueie {{ nextLevelObj?.discount }}% OFF</span>
                </div>
                <div class="th-prog-amount">
                  Faltam <strong>R$ {{ nextLevel.remaining?.toLocaleString('pt-BR') }}</strong>
                </div>
              </div>

              <div class="th-track-wrap">
                <div class="th-track">
                  <div class="th-fill" :style="{ width: progressPercent + '%' }">
                    <div class="th-fill-shimmer"></div>
                  </div>
                </div>
                <div class="th-pct">{{ progressPercent }}%</div>
              </div>
            </div>

            <!-- Nível Máximo Atingido -->
            <div class="th-progress-area th-max-area" v-else>
              <div class="th-max-content">
                <span class="th-max-icon">🏆</span>
                <div class="th-max-text">
                  <h3>Você atingiu o topo!</h3>
                  <p>Aproveite seu desconto permanente máximo de {{ levelDiscount }}%.</p>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- QUICK STATS (Métricas Rápidas) -->
        <div class="quick-stats-grid">
          <div class="stat-card" v-for="s in statsData" :key="s.label">
            <div class="stat-header">{{ s.label }}</div>
            <div class="stat-value">{{ s.value }}</div>
            <div class="stat-sub">{{ s.sub }}</div>
          </div>
        </div>

        <!-- BODY GRID -->
        <div class="main-layout-grid">

          <!-- COLUNA ESQUERDA: JORNADA (TIMELINE) -->
          <div class="card journey-card">
            <div class="card-header">
              <div class="ch-left">
                <h3 class="card-title">Jornada de Conquistas</h3>
                <p class="card-sub">Acompanhe seu progresso e os próximos benefícios.</p>
              </div>
              <span class="status-badge" :class="`badge-${levelKey}`">{{ levelLabel }}</span>
            </div>

            <div class="roadmap-container">
              <div
                  v-for="(lvl, i) in levels"
                  :key="lvl.key"
                  class="rm-node-row"
                  :class="{
                  'rm-done':    isLevelDone(lvl.key),
                  'rm-current': dropper?.level === lvl.key,
                  'rm-locked':  !isLevelDone(lvl.key) && dropper?.level !== lvl.key
                }"
              >
                <!-- Linha de conexão -->
                <div class="rm-line" v-if="i < levels.length - 1"
                     :class="isLevelDone(lvl.key) ? `rm-line-active rm-bg-${lvl.key.toLowerCase()}` : 'rm-line-inactive'"
                ></div>

                <!-- Círculo do Nível -->
                <div class="rm-circle" :class="(isLevelDone(lvl.key) || dropper?.level === lvl.key) ? `rm-bg-${lvl.key.toLowerCase()}` : ''">
                  <span v-if="isLevelDone(lvl.key)" class="rm-check">✓</span>
                  <span v-else class="rm-emoji">{{ lvl.icon }}</span>
                  <!-- Glow effect pro nível atual -->
                  <div v-if="dropper?.level === lvl.key" class="rm-glow" :class="`rm-glow-${lvl.key.toLowerCase()}`"></div>
                </div>

                <!-- Info do Nível -->
                <div class="rm-content">
                  <div class="rm-header">
                    <span class="rm-name">{{ lvl.label }}</span>
                    <span v-if="dropper?.level === lvl.key" class="rm-tag current-tag" :class="`tag-bg-${lvl.key.toLowerCase()}`">Você está aqui</span>
                    <span v-if="isLevelDone(lvl.key)" class="rm-tag done-tag">Desbloqueado</span>
                    <span v-if="!isLevelDone(lvl.key) && dropper?.level !== lvl.key" class="rm-tag locked-tag">🔒 Bloqueado</span>
                  </div>
                  <span class="rm-threshold">Meta: {{ lvl.threshold }} em vendas</span>
                </div>

                <!-- Recompensa (Desconto) -->
                <div class="rm-reward" :class="(isLevelDone(lvl.key) || dropper?.level === lvl.key) ? `text-${lvl.key.toLowerCase()}` : 'reward-locked'">
                  <div class="rw-val">{{ lvl.discount }}%</div>
                  <div class="rw-lbl">OFF</div>
                </div>
              </div>
            </div>

            <!-- CTA de Motivação Final -->
            <div class="mot-banner" v-if="nextLevel">
              <div class="mot-content">
                <span class="mot-icon">🚀</span>
                <div class="mot-text-wrap">
                  <h4 class="mot-title">Quase lá! Faltam R$ {{ nextLevel.remaining?.toLocaleString('pt-BR') }}</h4>
                  <p class="mot-desc">Alcance o nível {{ nextLevel.label }} e ganhe mais margem de lucro em todas as suas vendas com <strong>{{ nextLevelObj?.discount }}% de desconto</strong>.</p>
                </div>
              </div>
              <button class="mot-btn" :class="`btn-${levelKey}`">Vender Agora</button>
            </div>
          </div>

          <!-- COLUNA DIREITA: DADOS -->
          <div class="side-col">

            <!-- DADOS DA LOJA -->
            <div class="card data-card">
              <div class="card-header slim-header">
                <h3 class="card-title">Dados da Loja</h3>
              </div>
              <div class="data-list">
                <div class="d-row">
                  <span class="d-key">CNPJ</span>
                  <span class="d-val">{{ dropper?.cnpj || '—' }}</span>
                </div>
                <div class="d-row">
                  <span class="d-key">Nome da loja</span>
                  <span class="d-val">{{ dropper?.storeName || '—' }}</span>
                </div>
                <div class="d-row">
                  <span class="d-key">WhatsApp</span>
                  <span class="d-val">{{ dropper?.whatsapp || '—' }}</span>
                </div>
              </div>
            </div>

            <!-- DADOS PESSOAIS -->
            <div class="card data-card">
              <div class="card-header slim-header">
                <h3 class="card-title">Dados Pessoais</h3>
                <button class="ghost-btn" @click="editMode = !editMode">
                  {{ editMode ? 'Cancelar' : 'Editar' }}
                </button>
              </div>

              <div v-if="!editMode" class="data-list">
                <div class="d-row">
                  <span class="d-key">Nome</span>
                  <span class="d-val">{{ user?.name }}</span>
                </div>
                <div class="d-row">
                  <span class="d-key">E-mail</span>
                  <span class="d-val">{{ user?.email }}</span>
                </div>
                <div class="d-row">
                  <span class="d-key">Membro desde</span>
                  <span class="d-val">{{ formatDate(user?.createdAt) }}</span>
                </div>
              </div>

              <div v-else class="edit-form">
                <div class="form-group">
                  <label>Nome Completo</label>
                  <input v-model="editName" type="text" placeholder="Seu nome" />
                </div>
                <div class="form-group">
                  <label>E-mail</label>
                  <input v-model="editEmail" type="email" placeholder="seu@email.com" />
                </div>
                <button class="primary-btn" @click="saveProfile">Salvar Alterações</button>
              </div>
            </div>

          </div>
        </div>

      </div>
    </template>

    <!-- ════════════════════════════════════════
         CUSTOMER VIEW (CONVITE PARA DROPPER)
    ════════════════════════════════════════ -->
    <template v-else>
      <div class="dashboard-container customer-container">
        <div class="page-header">
          <div class="ph-greet">
            <p class="ph-date">{{ todayLabel }}</p>
            <h1 class="ph-title">Olá, {{ firstName }} 👋</h1>
            <p class="ph-sub">Gerencie seu perfil e acompanhe seus pedidos.</p>
          </div>
        </div>

        <div class="customer-layout-grid">

          <!-- SUPER CTA DROPPER -->
          <div class="premium-cta-card">
            <div class="pcta-bg"></div>
            <div class="pcta-content">
              <div class="pcta-header">
                <span class="pcta-badge">Programa de Parceiros</span>
                <h2>Transforme suas vendas em recompensas</h2>
                <p>Torne-se um <strong>Dropper</strong> e acesse nossa jornada de conquistas. Quanto mais você vende, maior o seu desconto em todas as compras.</p>
              </div>

              <div class="pcta-steps">
                <div v-for="lvl in levels" :key="lvl.key" class="pcta-step">
                  <div class="pcta-icon" :class="`icon-${lvl.key.toLowerCase()}`">{{ lvl.icon }}</div>
                  <div class="pcta-info">
                    <span class="pcta-name">{{ lvl.label }}</span>
                    <span class="pcta-thr">{{ lvl.threshold }}</span>
                  </div>
                  <div class="pcta-discount" :class="`text-${lvl.key.toLowerCase()}`">{{ lvl.discount }}%</div>
                </div>
              </div>

              <a href="/seja-dropper" class="pcta-btn">Quero ser um Dropper Agora ➔</a>
            </div>
          </div>

          <div class="customer-side">
            <!-- PEDIDOS -->
            <div class="card data-card">
              <div class="card-header slim-header"><h3 class="card-title">Meus Pedidos</h3></div>
              <div class="stats-mini">
                <div class="s-mini-box">
                  <span class="s-mini-val">{{ dropper?.totalOrders ?? 0 }}</span>
                  <span class="s-mini-lbl">Realizados</span>
                </div>
                <div class="s-mini-div"></div>
                <div class="s-mini-box">
                  <span class="s-mini-val">{{ dropper?.deliveredOrders ?? 0 }}</span>
                  <span class="s-mini-lbl">Entregues</span>
                </div>
              </div>
            </div>

            <!-- PERFIL -->
            <div class="card data-card">
              <div class="card-header slim-header">
                <h3 class="card-title">Dados Pessoais</h3>
                <button class="ghost-btn" @click="editMode = !editMode">{{ editMode ? 'Cancelar' : 'Editar' }}</button>
              </div>
              <div v-if="!editMode" class="data-list">
                <div class="d-row"><span class="d-key">Nome</span><span class="d-val">{{ user?.name }}</span></div>
                <div class="d-row"><span class="d-key">E-mail</span><span class="d-val">{{ user?.email }}</span></div>
              </div>
              <div v-else class="edit-form">
                <div class="form-group"><label>Nome</label><input v-model="editName" type="text" /></div>
                <div class="form-group"><label>E-mail</label><input v-model="editEmail" type="email" /></div>
                <button class="primary-btn" @click="saveProfile">Salvar</button>
              </div>
            </div>
          </div>

        </div>
      </div>
    </template>

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useAuthStore }    from '../stores/auth'
import { useDropperStore } from '../stores/dropper'

const auth         = useAuthStore()
const dropperStore = useDropperStore()

onMounted(async () => {
  await dropperStore.fetchProfile();
})

const user      = computed(() => auth.user)
const dropper   = computed(() => dropperStore.profile)
const isDropper = computed(() => ['DROPPER', 'OWNER'].includes(auth.user?.role))

const editMode = ref(false)
const editName  = ref(user.value?.name  || '')
const editEmail = ref(user.value?.email || '')

const levels = [
  { key: 'BRONZE',   label: 'Bronze',   icon: '🥉', discount: 5,  threshold: 'R$ 0'        },
  { key: 'SILVER',   label: 'Prata',    icon: '🥈', discount: 10, threshold: 'R$ 5.000'    },
  { key: 'GOLD',     label: 'Ouro',     icon: '🏅', discount: 15, threshold: 'R$ 20.000'   },
  { key: 'PLATINUM', label: 'Platina',  icon: '🎖️', discount: 22, threshold: 'R$ 60.000'   },
  { key: 'DIAMOND',  label: 'Diamante', icon: '💎', discount: 30, threshold: 'R$ 150.000'  },
]

const currentLevelIndex = computed(() => levels.findIndex(l => l.key === dropper.value?.level))
const currentLevel  = computed(() => levels[Math.max(0, currentLevelIndex.value)])
const levelKey      = computed(() => currentLevel.value.key.toLowerCase())
const levelIcon     = computed(() => currentLevel.value.icon)
const levelLabel    = computed(() => currentLevel.value.label)
const levelDiscount = computed(() => currentLevel.value.discount)

const nextLevelObj = computed(() => levels[currentLevelIndex.value + 1] ?? null)

const nextLevel = computed(() => {
  const idx = currentLevelIndex.value
  if (idx < 0 || idx === levels.length - 1) return null
  const nxt = levels[idx + 1]
  const nxtVal = parseFloat(nxt.threshold.replace(/\D/g, '')) || 0
  const sales  = parseFloat(dropper.value?.totalSales || 0)
  return { label: nxt.label, remaining: Math.max(0, nxtVal - sales) }
})

const progressPercent = computed(() => {
  const idx = currentLevelIndex.value
  if (idx === levels.length - 1) return 100
  if (idx < 0) return 0
  const cur = parseFloat(levels[idx].threshold.replace(/\D/g, '')) || 0
  const nxt = parseFloat(levels[idx + 1].threshold.replace(/\D/g, '')) || 1
  const sal = parseFloat(dropper.value?.totalSales || 0)
  return Math.min(100, Math.round(((sal - cur) / (nxt - cur)) * 100))
})

const isLevelDone = key => levels.findIndex(l => l.key === key) < currentLevelIndex.value

const firstName = computed(() => user.value?.name?.split(' ')[0] || '')

const todayLabel = computed(() =>
    'Hoje é ' + new Date().toLocaleDateString('pt-BR', { weekday: undefined, day: 'numeric', month: 'long', year: 'numeric' })
)

const statsData = computed(() => [
  { label: 'Saldo disponível',  value: `R$ ${Number(dropper.value?.balance || 0).toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`,        sub: `Pendente: R$ 0,00` },
  { label: 'Vendas aprovadas',  value: dropper.value?.totalOrders ?? 0,                                                                             sub: `Ticket médio: R$ 0,00` },
  { label: 'Jornada de nível',  value: `${progressPercent.value}%`,                                                                                 sub: nextLevel.value ? `Próximo: ${nextLevel.value.label}` : 'Nível máximo 🏆' },
  { label: 'Desconto atual',    value: `${levelDiscount.value}%`,                                                                                   sub: `Nível ${levelLabel.value}` },
])

const formatDate = d => d ? new Date(d).toLocaleDateString('pt-BR', { month: 'long', year: 'numeric' }) : '—'

const saveProfile = async () => { editMode.value = false }
</script>

<style scoped>
/* ══════════════════════════════════════
   DESIGN TOKENS MODERNOS
══════════════════════════════════════ */
.profile-root {
  /* Cores Globais Premium */
  --bg-main:    #F8FAFC;
  --surface:    #FFFFFF;
  --border:     #E2E8F0;
  --border-light: #F1F5F9;
  --text-main:  #0F172A;
  --text-muted: #64748B;
  --text-light: #94A3B8;
  --primary:    #2563EB;
  --primary-hv: #1D4ED8;

  /* Cores por Nível (Gamificação) */
  --bronze:   #D97706; --bronze-light: #FEF3C7; --bronze-grad: linear-gradient(135deg, #F59E0B, #B45309);
  --silver:   #64748B; --silver-light: #F1F5F9; --silver-grad: linear-gradient(135deg, #94A3B8, #475569);
  --gold:     #EAB308; --gold-light:   #FEF08A; --gold-grad:   linear-gradient(135deg, #FACC15, #A16207);
  --platinum: #06B6D4; --platinum-light:#CFFAFE;--platinum-grad:linear-gradient(135deg, #22D3EE, #0369A1);
  --diamond:  #8B5CF6; --diamond-light:#EDE9FE; --diamond-grad: linear-gradient(135deg, #A855F7, #4C1D95);

  /* Sombras e Bordas */
  --shadow-sm: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
  --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.05), 0 2px 4px -1px rgba(0, 0, 0, 0.03);
  --shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.05), 0 4px 6px -2px rgba(0, 0, 0, 0.02);
  --radius-md: 12px;
  --radius-lg: 16px;
  --radius-xl: 24px;

  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
  background-color: var(--bg-main);
  color: var(--text-main);
  min-height: 100vh;
  padding-bottom: 60px;
}

.dashboard-container {
  max-width: 1140px;
  margin: 0 auto;
  padding: 32px 24px;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* ══════════════════════════════════════
   HEADER SIMPLES E LIMPO
══════════════════════════════════════ */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
}
.ph-date { font-size: 13px; color: var(--text-light); font-weight: 500; text-transform: uppercase; letter-spacing: 0.05em; margin: 0 0 8px 0; }
.ph-title { font-size: 28px; font-weight: 800; letter-spacing: -0.02em; color: var(--text-main); margin: 0 0 6px 0; }
.ph-sub { font-size: 15px; color: var(--text-muted); margin: 0; }

/* ══════════════════════════════════════
   HERO CARD (O CARTÃO DE MEMBRO PREMIUM)
══════════════════════════════════════ */
.tier-hero-card {
  position: relative;
  border-radius: var(--radius-xl);
  padding: 32px 40px;
  color: white;
  overflow: hidden;
  box-shadow: var(--shadow-lg);
  transition: transform 0.3s ease;
}
.tier-hero-card:hover { transform: translateY(-2px); }

/* Gradientes de fundo baseados no nível */
.tier-bronze   { background: var(--bronze-grad); }
.tier-silver   { background: var(--silver-grad); }
.tier-gold     { background: var(--gold-grad); }
.tier-platinum { background: var(--platinum-grad); }
.tier-diamond  { background: var(--diamond-grad); }

/* Efeito de brilho de fundo no cartão */
.th-bg-glow {
  position: absolute; top: -50%; right: -10%; width: 60%; height: 200%;
  background: radial-gradient(circle, rgba(255,255,255,0.15) 0%, transparent 60%);
  transform: rotate(30deg); pointer-events: none;
}

.th-content {
  position: relative; z-index: 2;
  display: flex; justify-content: space-between; align-items: center; gap: 40px;
}

/* Lado Esquerdo: Info Atual */
.th-current { display: flex; align-items: center; gap: 24px; }
.th-badge {
  width: 72px; height: 72px; border-radius: 20px;
  background: rgba(255, 255, 255, 0.2); backdrop-filter: blur(10px);
  display: flex; align-items: center; justify-content: center;
  border: 1px solid rgba(255, 255, 255, 0.3); box-shadow: 0 8px 16px rgba(0,0,0,0.1);
}
.th-icon { font-size: 36px; filter: drop-shadow(0 4px 6px rgba(0,0,0,0.2)); }
.th-info { display: flex; flex-direction: column; }
.th-eyebrow { font-size: 12px; font-weight: 600; text-transform: uppercase; letter-spacing: 0.1em; opacity: 0.8; margin-bottom: 4px; }
.th-name { font-size: 28px; font-weight: 800; margin: 0 0 8px 0; line-height: 1.1; letter-spacing: -0.02em; }
.th-discount-tag {
  display: inline-block; background: rgba(0,0,0,0.2); padding: 6px 12px;
  border-radius: 8px; font-size: 13px; font-weight: 500; backdrop-filter: blur(4px);
}

/* Lado Direito: Progresso (Meta) */
.th-progress-area { flex: 1; max-width: 480px; background: rgba(0,0,0,0.15); padding: 20px 24px; border-radius: 16px; border: 1px solid rgba(255,255,255,0.1); }
.th-prog-header { display: flex; justify-content: space-between; align-items: flex-end; margin-bottom: 12px; }
.th-prog-target { display: flex; flex-direction: column; gap: 4px; }
.nxt-lbl { font-size: 14px; opacity: 0.9; }
.nxt-lbl strong { font-weight: 700; color: #fff; font-size: 16px; margin-left: 4px;}
.nxt-reward { font-size: 12px; font-weight: 600; color: #FFD700; text-transform: uppercase; letter-spacing: 0.05em; }
.th-prog-amount { font-size: 13px; opacity: 0.9; text-align: right; }
.th-prog-amount strong { font-size: 16px; font-weight: 800; display: block; margin-top: 2px;}

/* Barra de Progresso do Hero */
.th-track-wrap { display: flex; align-items: center; gap: 16px; }
.th-track { flex: 1; height: 10px; background: rgba(0,0,0,0.3); border-radius: 10px; overflow: hidden; position: relative; }
.th-fill {
  height: 100%; background: #fff; border-radius: 10px;
  transition: width 1s cubic-bezier(0.16, 1, 0.3, 1);
  position: relative; overflow: hidden;
}
.th-fill-shimmer {
  position: absolute; top: 0; left: -100%; width: 50%; height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255,255,255,0.8), transparent);
  animation: shimmer 2s infinite;
}
.th-pct { font-size: 16px; font-weight: 800; width: 45px; text-align: right; }

.th-max-content { display: flex; align-items: center; gap: 16px; }
.th-max-icon { font-size: 40px; }
.th-max-text h3 { margin: 0 0 4px 0; font-size: 20px; font-weight: 800; }
.th-max-text p { margin: 0; font-size: 14px; opacity: 0.9; }

@keyframes shimmer { 100% { left: 200%; } }

/* ══════════════════════════════════════
   QUICK STATS GRID
══════════════════════════════════════ */
.quick-stats-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; }
.stat-card {
  background: var(--surface); border: 1px solid var(--border); border-radius: var(--radius-md);
  padding: 20px; box-shadow: var(--shadow-sm); transition: box-shadow 0.2s;
}
.stat-card:hover { box-shadow: var(--shadow-md); }
.stat-header { font-size: 13px; font-weight: 600; color: var(--text-muted); margin-bottom: 8px; }
.stat-value { font-size: 24px; font-weight: 800; color: var(--text-main); letter-spacing: -0.02em; margin-bottom: 4px; }
.stat-sub { font-size: 12px; color: var(--text-light); }

/* ══════════════════════════════════════
   LAYOUT PRINCIPAL
══════════════════════════════════════ */
.main-layout-grid { display: grid; grid-template-columns: 1fr 340px; gap: 24px; }
.card { background: var(--surface); border: 1px solid var(--border); border-radius: var(--radius-lg); box-shadow: var(--shadow-sm); overflow: hidden; }
.card-header { padding: 24px; border-bottom: 1px solid var(--border-light); display: flex; justify-content: space-between; align-items: center; }
.slim-header { padding: 20px 24px; }
.card-title { font-size: 16px; font-weight: 700; color: var(--text-main); margin: 0 0 4px 0; }
.card-sub { font-size: 13px; color: var(--text-muted); margin: 0; }

.status-badge {
  font-size: 12px; font-weight: 700; padding: 6px 12px; border-radius: 100px; text-transform: uppercase; letter-spacing: 0.05em;
}
.badge-bronze   { background: var(--bronze-light); color: var(--bronze); }
.badge-silver   { background: var(--silver-light); color: var(--silver); }
.badge-gold     { background: var(--gold-light); color: var(--gold); }
.badge-platinum { background: var(--platinum-light); color: var(--platinum); }
.badge-diamond  { background: var(--diamond-light); color: var(--diamond); }

/* ══════════════════════════════════════
   JORNADA (TIMELINE VERTICAL GAMIFICADA)
══════════════════════════════════════ */
.roadmap-container { padding: 12px 24px 24px 24px; }
.rm-node-row { position: relative; display: flex; align-items: center; gap: 20px; padding: 16px 0; }

/* Linha conectora */
.rm-line { position: absolute; left: 23px; top: 48px; width: 2px; height: calc(100% - 16px); z-index: 1; border-radius: 2px; }
.rm-line-inactive { background: var(--border-light); }
.rm-line-active { background: var(--primary); opacity: 0.4; }

/* Dynamic Backgrounds para as linhas conectando níveis prontos */
.rm-bg-bronze   { background-color: var(--bronze) !important; }
.rm-bg-silver   { background-color: var(--silver) !important; }
.rm-bg-gold     { background-color: var(--gold) !important; }
.rm-bg-platinum { background-color: var(--platinum) !important; }
.rm-bg-diamond  { background-color: var(--diamond) !important; }

/* Circulos dos niveis */
.rm-circle {
  width: 48px; height: 48px; border-radius: 14px; background: var(--bg-main); border: 2px solid var(--border);
  display: flex; align-items: center; justify-content: center; font-size: 20px; z-index: 2; position: relative;
  transition: all 0.3s ease; flex-shrink: 0;
}
.rm-check { color: white; font-weight: 800; font-size: 18px; }

/* Glow effect do nível atual */
.rm-glow { position: absolute; width: 100%; height: 100%; border-radius: inherit; z-index: -1; filter: blur(8px); opacity: 0.6; animation: pulseGlow 2s infinite; }
@keyframes pulseGlow { 0% { transform: scale(1); opacity: 0.4; } 50% { transform: scale(1.3); opacity: 0.8; } 100% { transform: scale(1); opacity: 0.4; } }
.rm-glow-bronze   { background: var(--bronze); }
.rm-glow-silver   { background: var(--silver); }
.rm-glow-gold     { background: var(--gold); }
.rm-glow-platinum { background: var(--platinum); }
.rm-glow-diamond  { background: var(--diamond); }

/* Conteúdo da Timeline */
.rm-content { flex: 1; }
.rm-header { display: flex; align-items: center; gap: 12px; margin-bottom: 4px; }
.rm-name { font-size: 16px; font-weight: 700; color: var(--text-main); }
.rm-threshold { font-size: 13px; color: var(--text-muted); font-weight: 500; }

/* Tags */
.rm-tag { font-size: 11px; font-weight: 700; padding: 4px 10px; border-radius: 6px; text-transform: uppercase; letter-spacing: 0.05em; }
.done-tag { background: #ECFDF5; color: #059669; }
.locked-tag { background: var(--bg-main); color: var(--text-light); border: 1px solid var(--border); }
.current-tag { color: white; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
.tag-bg-bronze   { background: var(--bronze); }
.tag-bg-silver   { background: var(--silver); }
.tag-bg-gold     { background: var(--gold); }
.tag-bg-platinum { background: var(--platinum); }
.tag-bg-diamond  { background: var(--diamond); }

/* Recompensa (O desconto gigante na direita) */
.rm-reward { display: flex; flex-direction: column; align-items: flex-end; justify-content: center; min-width: 60px; }
.rw-val { font-size: 24px; font-weight: 900; line-height: 1; letter-spacing: -0.03em; }
.rw-lbl { font-size: 12px; font-weight: 800; text-transform: uppercase; opacity: 0.8; }
.reward-locked { color: var(--border); }
.text-bronze   { color: var(--bronze); }
.text-silver   { color: var(--silver); }
.text-gold     { color: var(--gold); }
.text-platinum { color: var(--platinum); }
.text-diamond  { color: var(--diamond); }

/* Níveis bloqueados */
.rm-locked .rm-circle { background: var(--bg-main); border-color: var(--border-light); opacity: 0.6; }
.rm-locked .rm-emoji { filter: grayscale(100%); opacity: 0.5; }
.rm-locked .rm-name { color: var(--text-light); }

/* ══════════════════════════════════════
   MOTIVATION CTA (FINAL DO CARD JORNADA)
══════════════════════════════════════ */
.mot-banner {
  margin: 0 24px 24px; padding: 20px; border-radius: var(--radius-md);
  background: var(--bg-main); border: 1px dashed var(--border);
  display: flex; align-items: center; justify-content: space-between; gap: 20px;
}
.mot-content { display: flex; align-items: flex-start; gap: 16px; }
.mot-icon { font-size: 28px; }
.mot-text-wrap { display: flex; flex-direction: column; gap: 4px; }
.mot-title { font-size: 15px; font-weight: 700; color: var(--text-main); margin: 0; }
.mot-desc { font-size: 13px; color: var(--text-muted); margin: 0; line-height: 1.5; }
.mot-desc strong { color: var(--text-main); }
.mot-btn {
  padding: 10px 20px; border-radius: 8px; font-weight: 700; font-size: 14px;
  cursor: pointer; transition: opacity 0.2s; color: white; border: none; white-space: nowrap;
}
.mot-btn:hover { opacity: 0.9; transform: translateY(-1px); }
.btn-bronze   { background: var(--bronze); }
.btn-silver   { background: var(--silver); }
.btn-gold     { background: var(--gold); }
.btn-platinum { background: var(--platinum); }
.btn-diamond  { background: var(--diamond); }

/* ══════════════════════════════════════
   COLUNA DIREITA (DADOS)
══════════════════════════════════════ */
.side-col { display: flex; flex-direction: column; gap: 24px; }
.data-list { display: flex; flex-direction: column; }
.d-row {
  display: flex; justify-content: space-between; align-items: center;
  padding: 16px 24px; border-bottom: 1px solid var(--border-light);
}
.d-row:last-child { border-bottom: none; }
.d-key { font-size: 13px; font-weight: 500; color: var(--text-muted); }
.d-val { font-size: 14px; font-weight: 600; color: var(--text-main); text-align: right; max-width: 60%; word-break: break-all; }

/* Botões secundarios */
.ghost-btn {
  background: transparent; border: 1px solid var(--border); color: var(--text-muted);
  font-size: 12px; font-weight: 600; padding: 6px 12px; border-radius: 6px; cursor: pointer; transition: all 0.2s;
}
.ghost-btn:hover { background: var(--bg-main); color: var(--text-main); }

/* Formulário de Edição */
.edit-form { padding: 20px 24px; display: flex; flex-direction: column; gap: 16px; background: var(--bg-main); }
.form-group { display: flex; flex-direction: column; gap: 6px; }
.form-group label { font-size: 12px; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em; }
.form-group input {
  background: var(--surface); border: 1px solid var(--border); padding: 10px 14px; border-radius: 8px;
  font-size: 14px; color: var(--text-main); transition: border-color 0.2s; outline: none;
}
.form-group input:focus { border-color: var(--primary); box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.1); }
.primary-btn {
  background: var(--primary); color: white; border: none; padding: 10px 20px; border-radius: 8px;
  font-size: 14px; font-weight: 600; cursor: pointer; transition: background 0.2s; align-self: flex-start;
}
.primary-btn:hover { background: var(--primary-hv); }

/* ══════════════════════════════════════
   VISÃO CUSTOMER (NÃO-DROPPER)
══════════════════════════════════════ */
.customer-container { max-width: 1000px; }
.customer-layout-grid { display: grid; grid-template-columns: 1fr 340px; gap: 24px; align-items: start; }

.premium-cta-card {
  position: relative; background: #0F172A; border-radius: var(--radius-lg); padding: 40px; color: white;
  overflow: hidden; box-shadow: var(--shadow-lg);
}
.pcta-bg {
  position: absolute; top: 0; left: 0; width: 100%; height: 100%;
  background: radial-gradient(circle at top right, rgba(139, 92, 246, 0.3) 0%, transparent 60%),
  radial-gradient(circle at bottom left, rgba(37, 99, 235, 0.2) 0%, transparent 60%);
  z-index: 1; pointer-events: none;
}
.pcta-content { position: relative; z-index: 2; display: flex; flex-direction: column; gap: 32px; }
.pcta-badge { display: inline-block; background: rgba(139, 92, 246, 0.2); border: 1px solid rgba(139,92,246,0.4); color: #C4B5FD; padding: 6px 14px; border-radius: 100px; font-size: 12px; font-weight: 700; text-transform: uppercase; letter-spacing: 0.1em; align-self: flex-start;}
.pcta-header h2 { font-size: 32px; font-weight: 800; margin: 16px 0 12px 0; letter-spacing: -0.02em; line-height: 1.1; }
.pcta-header p { font-size: 15px; color: #94A3B8; margin: 0; line-height: 1.6; max-width: 90%; }

.pcta-steps { background: rgba(255,255,255,0.05); border: 1px solid rgba(255,255,255,0.1); border-radius: var(--radius-md); display: flex; flex-direction: column; }
.pcta-step { display: flex; align-items: center; gap: 16px; padding: 16px 20px; border-bottom: 1px solid rgba(255,255,255,0.05); transition: background 0.2s; }
.pcta-step:last-child { border-bottom: none; }
.pcta-step:hover { background: rgba(255,255,255,0.1); }
.pcta-icon { width: 40px; height: 40px; border-radius: 10px; display: flex; align-items: center; justify-content: center; font-size: 20px; }
.icon-bronze   { background: var(--bronze-light); }
.icon-silver   { background: var(--silver-light); }
.icon-gold     { background: var(--gold-light); }
.icon-platinum { background: var(--platinum-light); }
.icon-diamond  { background: var(--diamond-light); }
.pcta-info { flex: 1; display: flex; flex-direction: column; gap: 2px; }
.pcta-name { font-size: 15px; font-weight: 700; color: white; }
.pcta-thr { font-size: 12px; color: #94A3B8; }
.pcta-discount { font-size: 20px; font-weight: 900; }

.pcta-btn {
  background: white; color: #0F172A; text-align: center; padding: 16px; border-radius: 12px;
  font-size: 16px; font-weight: 800; text-decoration: none; transition: transform 0.2s, box-shadow 0.2s;
}
.pcta-btn:hover { transform: translateY(-2px); box-shadow: 0 10px 25px -5px rgba(255,255,255,0.3); }

/* Mini Stats Customer */
.stats-mini { display: flex; padding: 24px; }
.s-mini-box { flex: 1; text-align: center; }
.s-mini-val { display: block; font-size: 32px; font-weight: 900; color: var(--text-main); letter-spacing: -0.03em; }
.s-mini-lbl { display: block; font-size: 12px; font-weight: 600; color: var(--text-muted); text-transform: uppercase; margin-top: 4px; }
.s-mini-div { width: 1px; background: var(--border-light); margin: 0 16px; }

/* ══════════════════════════════════════
   RESPONSIVIDADE
══════════════════════════════════════ */
@media (max-width: 1024px) {
  .main-layout-grid, .customer-layout-grid { grid-template-columns: 1fr; }
  .quick-stats-grid { grid-template-columns: repeat(2, 1fr); }
  .th-content { flex-direction: column; align-items: flex-start; gap: 24px; }
  .th-progress-area { max-width: 100%; width: 100%; }
}

@media (max-width: 640px) {
  .dashboard-container { padding: 16px; }
  .tier-hero-card { padding: 24px; }
  .quick-stats-grid { grid-template-columns: 1fr; }
  .th-current { flex-direction: column; align-items: flex-start; gap: 16px; }
  .th-name { font-size: 24px; }
  .rm-node-row { flex-direction: column; align-items: flex-start; gap: 12px; padding-left: 56px; }
  .rm-line { left: 23px; top: 0; height: 100%; }
  .rm-circle { position: absolute; left: 0; top: 16px; }
  .rm-reward { align-items: flex-start; margin-top: 8px; }
  .mot-banner { flex-direction: column; text-align: center; align-items: stretch; }
  .mot-content { flex-direction: column; align-items: center; text-align: center; }
  .pcta-header h2 { font-size: 24px; }
  .premium-cta-card { padding: 24px; }
}
</style>
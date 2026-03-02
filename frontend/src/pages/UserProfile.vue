<template>
  <div class="profile-root">

    <!-- ════════════════════════════════════════
         DROPPER VIEW
    ════════════════════════════════════════ -->
    <template v-if="isDropper">

      <!-- RANK BANNER — estilo Kirvano, muda por nível -->
      <div class="rank-banner" :class="`rank-${levelKey}`">
        <div class="rb-left">
          <span class="rb-icon">{{ levelIcon }}</span>
          <div class="rb-info">
            <span class="rb-eyebrow">Seu nível atual</span>
            <span class="rb-name">{{ levelLabel }}</span>
          </div>
        </div>

        <div class="rb-center">
          <span class="rb-big">{{ levelDiscount }}% OFF</span>
          <span class="rb-center-txt">em todas as suas compras</span>
        </div>

        <div class="rb-right" v-if="nextLevel">
          <span class="rb-next-label">Próximo nível</span>
          <div class="rb-next-row">
            <span class="rb-next-icon">{{ nextLevelObj?.icon }}</span>
            <span class="rb-next-name">{{ nextLevel.label }}</span>
          </div>
          <span class="rb-next-sub">Faltam <strong>R$ {{ nextLevel.remaining?.toLocaleString('pt-BR') }}</strong></span>
        </div>
        <div class="rb-right" v-else>
          <span class="rb-next-label">Nível máximo</span>
          <span class="rb-next-name">🏆 Parabéns!</span>
        </div>
      </div>

      <!-- PAGE HEADER -->
      <div class="page-header">
        <div class="ph-greet">
          <p class="ph-date">{{ todayLabel }}</p>
          <h1 class="ph-title">Olá, {{ firstName }} 👋</h1>
          <p class="ph-sub">Acompanhe sua jornada e suba de nível vendendo mais.</p>
        </div>

        <!-- 4 quick-stat cards estilo Kirvano -->
        <div class="ph-stats">
          <div class="qs" v-for="s in statsData" :key="s.label">
            <span class="qs-label">{{ s.label }}</span>
            <span class="qs-val">{{ s.value }}</span>
            <span class="qs-sub">{{ s.sub }}</span>
          </div>
        </div>
      </div>

      <!-- BODY -->
      <div class="body-grid">

        <!-- JORNADA DE CONQUISTAS -->
        <div class="card journey">
          <div class="card-header">
            <div class="ch-left">
              <span class="card-title">Jornada de conquistas</span>
              <span class="card-sub">Sua jornada começa com a primeira venda.</span>
            </div>
            <a class="saiba-link" href="#">Saiba mais ↗</a>
          </div>

          <!-- PROGRESS -->
          <div class="prog-section">
            <div class="prog-meta">
              <span class="prog-cur">{{ levelLabel }}</span>
              <span class="prog-pct-lbl">{{ progressPercent }}%</span>
              <span class="prog-nxt" v-if="nextLevel">→ {{ nextLevel.label }}</span>
              <span class="prog-nxt" v-else>Máximo ✓</span>
            </div>
            <div class="prog-track">
              <div class="prog-fill" :class="`fill-${levelKey}`" :style="{ width: progressPercent + '%' }"></div>
            </div>
          </div>

          <!-- LEVELS -->
          <div class="levels-list">
            <div
                v-for="(lvl, i) in levels"
                :key="lvl.key"
                class="lv-row"
                :class="{
                'lv-done':    isLevelDone(lvl.key),
                'lv-current': dropper?.level === lvl.key,
                'lv-locked':  !isLevelDone(lvl.key) && dropper?.level !== lvl.key
              }"
            >
              <!-- vertical connector -->
              <div class="lv-line" v-if="i < levels.length - 1"
                   :class="isLevelDone(lvl.key) ? `lv-line-${lvl.key.toLowerCase()}` : ''"
              ></div>

              <div class="lv-node" :class="(isLevelDone(lvl.key) || dropper?.level === lvl.key) ? `lv-node-${lvl.key.toLowerCase()}` : ''">
                <span v-if="isLevelDone(lvl.key)" class="lv-check">✓</span>
                <span v-else class="lv-emoji">{{ lvl.icon }}</span>
              </div>

              <div class="lv-body">
                <div class="lv-name-row">
                  <span class="lv-name">{{ lvl.label }}</span>
                  <span v-if="dropper?.level === lvl.key" class="tag tag-cur" :class="`tag-${lvl.key.toLowerCase()}`">Você está aqui</span>
                  <span v-if="isLevelDone(lvl.key)" class="tag tag-done">Concluído</span>
                  <span v-if="!isLevelDone(lvl.key) && dropper?.level !== lvl.key" class="tag tag-locked">🔒 Bloqueado</span>
                </div>
                <span class="lv-threshold">A partir de {{ lvl.threshold }} em vendas</span>
              </div>

              <div class="lv-pct" :class="(isLevelDone(lvl.key) || dropper?.level === lvl.key) ? `pct-${lvl.key.toLowerCase()}` : 'pct-locked'">
                {{ lvl.discount }}<span class="pct-sym">%</span>
              </div>
            </div>
          </div>

          <!-- CTA motivacional -->
          <div class="mot-banner" :class="`mot-${levelKey}`" v-if="nextLevel">
            <div class="mot-left">
              <span class="mot-icon">{{ nextLevelObj?.icon }}</span>
              <div>
                <p class="mot-title">Faltam R$ {{ nextLevel.remaining?.toLocaleString('pt-BR') }} para o nível {{ nextLevel.label }}</p>
                <p class="mot-sub">Seu desconto vai de {{ levelDiscount }}% para {{ nextLevelObj?.discount }}% — +{{ nextLevelObj?.discount - levelDiscount }}% em todas as compras</p>
              </div>
            </div>
            <span class="mot-arrow">→</span>
          </div>
          <div class="mot-banner mot-max" v-else>
            <span class="mot-icon">💎</span>
            <div>
              <p class="mot-title">Nível máximo atingido!</p>
              <p class="mot-sub">Você tem {{ levelDiscount }}% de desconto permanente em tudo.</p>
            </div>
          </div>
        </div>

        <!-- COLUNA DIREITA -->
        <div class="right-col">

          <!-- DADOS DA LOJA -->
          <div class="card">
            <div class="card-header">
              <span class="card-title">Dados da loja</span>
            </div>
            <div class="field-list">
              <div class="field-row">
                <span class="fk">CNPJ</span>
                <span class="fv">{{ dropper?.cnpj || '—' }}</span>
              </div>
              <div class="field-row">
                <span class="fk">Nome da loja</span>
                <span class="fv">{{ dropper?.storeName || '—' }}</span>
              </div>
              <div class="field-row">
                <span class="fk">WhatsApp</span>
                <span class="fv">{{ dropper?.whatsapp || '—' }}</span>
              </div>
            </div>
          </div>

          <!-- DADOS PESSOAIS -->
          <div class="card">
            <div class="card-header">
              <span class="card-title">Dados pessoais</span>
              <button class="edit-btn" @click="editMode = !editMode">
                {{ editMode ? 'Cancelar' : 'Editar' }}
              </button>
            </div>

            <div v-if="!editMode" class="field-list">
              <div class="field-row">
                <span class="fk">Nome</span>
                <span class="fv">{{ user?.name }}</span>
              </div>
              <div class="field-row">
                <span class="fk">E-mail</span>
                <span class="fv">{{ user?.email }}</span>
              </div>
              <div class="field-row">
                <span class="fk">Membro desde</span>
                <span class="fv">{{ formatDate(user?.createdAt) }}</span>
              </div>
            </div>

            <div v-else class="edit-body">
              <div class="edit-grp">
                <label>Nome</label>
                <input v-model="editName" type="text" />
              </div>
              <div class="edit-grp">
                <label>E-mail</label>
                <input v-model="editEmail" type="email" />
              </div>
              <button class="save-btn" @click="saveProfile">Salvar alterações</button>
            </div>
          </div>

        </div>
      </div>
    </template>

    <!-- ════════════════════════════════════════
         CUSTOMER VIEW
    ════════════════════════════════════════ -->
    <template v-else>
      <div class="page-header">
        <div class="ph-greet">
          <p class="ph-date">{{ todayLabel }}</p>
          <h1 class="ph-title">Olá, {{ firstName }} 👋</h1>
          <p class="ph-sub">Gerencie seu perfil e acompanhe seus pedidos.</p>
        </div>
      </div>

      <div class="customer-grid">

        <!-- CTA DROPPER -->
        <div class="card dropper-cta">
          <div class="card-header">
            <div class="ch-left">
              <span class="card-title">Jornada de conquistas</span>
              <span class="card-sub">Sua jornada começa com a primeira venda.</span>
            </div>
            <span class="tag-programa">Programa de Parceiros</span>
          </div>

          <div class="cta-body">
            <p class="cta-desc">Torne-se um <strong>Dropper</strong> e ganhe descontos progressivos em todas as compras. Quanto mais você vende, mais economiza.</p>

            <div class="cta-levels">
              <div v-for="lvl in levels" :key="lvl.key" class="cta-lv" :class="`cta-lv-${lvl.key.toLowerCase()}`">
                <div class="cta-lv-dot" :class="`dot-${lvl.key.toLowerCase()}`">{{ lvl.icon }}</div>
                <span class="cta-lv-name">{{ lvl.label }}</span>
                <span class="cta-lv-thr">{{ lvl.threshold }}</span>
                <span class="cta-lv-pct" :class="`cpct-${lvl.key.toLowerCase()}`">{{ lvl.discount }}%</span>
              </div>
            </div>

            <a href="/seja-dropper" class="cta-action">Quero ser Dropper →</a>
          </div>
        </div>

        <div class="customer-side">

          <!-- PEDIDOS -->
          <div class="card">
            <div class="card-header"><span class="card-title">Meus pedidos</span></div>
            <div class="cstats">
              <div class="cstat">
                <span class="cstat-n">{{ dropper?.totalOrders ?? 0 }}</span>
                <span class="cstat-l">Realizados</span>
              </div>
              <div class="cstat-sep"></div>
              <div class="cstat">
                <span class="cstat-n">{{ dropper?.deliveredOrders ?? 0 }}</span>
                <span class="cstat-l">Entregues</span>
              </div>
            </div>
          </div>

          <!-- PERFIL -->
          <div class="card">
            <div class="card-header">
              <span class="card-title">Dados pessoais</span>
              <button class="edit-btn" @click="editMode = !editMode">{{ editMode ? 'Cancelar' : 'Editar' }}</button>
            </div>
            <div v-if="!editMode" class="field-list">
              <div class="field-row"><span class="fk">Nome</span><span class="fv">{{ user?.name }}</span></div>
              <div class="field-row"><span class="fk">E-mail</span><span class="fv">{{ user?.email }}</span></div>
            </div>
            <div v-else class="edit-body">
              <div class="edit-grp"><label>Nome</label><input v-model="editName" type="text" /></div>
              <div class="edit-grp"><label>E-mail</label><input v-model="editEmail" type="email" /></div>
              <button class="save-btn" @click="saveProfile">Salvar</button>
            </div>
          </div>

        </div>
      </div>
    </template>

  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useAuthStore }    from '../stores/auth'
import { useDropperStore } from '../stores/dropper'

const auth         = useAuthStore()
const dropperStore = useDropperStore()

const user      = computed(() => auth.user)
const dropper   = computed(() => dropperStore.profile)
const isDropper = computed(() => ['DROPPER', 'OWNER'].includes(auth.user?.role))

const editMode = ref(false)
const editName  = ref(user.value?.name  || '')
const editEmail = ref(user.value?.email || '')

/* ── Levels config ── */
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
   TOKENS — mesma paleta do Kirvano
══════════════════════════════════════ */
.profile-root {
  --bg:      #F7F8FA;
  --white:   #FFFFFF;
  --border:  #E5E7EB;
  --text:    #111827;
  --text2:   #374151;
  --muted:   #6B7280;
  --radius:  8px;
  --fs-base: 13px;

  /* level colors */
  --bronze-c:  #B45309; --bronze-bg:  #FFFBEB; --bronze-bd:  #FDE68A; --bronze-bar:  #F59E0B;
  --silver-c:  #374151; --silver-bg:  #F9FAFB; --silver-bd:  #D1D5DB; --silver-bar:  #9CA3AF;
  --gold-c:    #92400E; --gold-bg:    #FFFBEB; --gold-bd:    #FDE68A; --gold-bar:    #FBBF24;
  --platinum-c:#0E7490; --platinum-bg:#ECFEFF; --platinum-bd:#A5F3FC; --platinum-bar:#06B6D4;
  --diamond-c: #6D28D9; --diamond-bg: #F5F3FF; --diamond-bd: #DDD6FE; --diamond-bar: #8B5CF6;

  font-family: -apple-system, BlinkMacSystemFont, 'Inter', 'Segoe UI', sans-serif;
  font-size: var(--fs-base);
  line-height: 1.5;
  color: var(--text);
  background: var(--bg);
  min-height: 100vh;
  padding-bottom: 80px;
}

/* ══════════════════════════════════════
   RANK BANNER
══════════════════════════════════════ */
.rank-banner {
  display: flex;
  align-items: center;
  padding: 0 40px;
  height: 72px;
  border-bottom: 1px solid var(--border);
  gap: 32px;
}

/* per-level skin */
.rank-bronze   { background: var(--bronze-bg);   border-top: 3px solid var(--bronze-bar);   }
.rank-silver   { background: var(--silver-bg);   border-top: 3px solid var(--silver-bar);   }
.rank-gold     { background: var(--gold-bg);     border-top: 3px solid var(--gold-bar);     }
.rank-platinum { background: var(--platinum-bg); border-top: 3px solid var(--platinum-bar); }
.rank-diamond  { background: var(--diamond-bg);  border-top: 3px solid var(--diamond-bar);  }

.rb-left {
  display: flex;
  align-items: center;
  gap: 12px;
  min-width: 180px;
}
.rb-icon { font-size: 26px; line-height: 1; }
.rb-info { display: flex; flex-direction: column; gap: 1px; }
.rb-eyebrow { font-size: 10px; font-weight: 600; text-transform: uppercase; letter-spacing: .08em; color: var(--muted); }
.rb-name    { font-size: 16px; font-weight: 700; color: var(--text); }

.rb-center {
  flex: 1;
  display: flex;
  align-items: baseline;
  justify-content: center;
  gap: 10px;
}
.rb-big {
  font-size: 26px;
  font-weight: 800;
  letter-spacing: -0.5px;
  color: var(--text);
}
.rb-center-txt { font-size: 13px; color: var(--muted); }

.rb-right {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 3px;
  min-width: 200px;
}
.rb-next-label { font-size: 10px; font-weight: 600; text-transform: uppercase; letter-spacing: .08em; color: var(--muted); }
.rb-next-row   { display: flex; align-items: center; gap: 6px; }
.rb-next-icon  { font-size: 16px; }
.rb-next-name  { font-size: 14px; font-weight: 700; }
.rb-next-sub   { font-size: 12px; color: var(--muted); }

/* ══════════════════════════════════════
   PAGE HEADER
══════════════════════════════════════ */
.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: var(--white);
  border-bottom: 1px solid var(--border);
  padding: 28px 40px;
  gap: 24px;
}

.ph-date  { font-size: 12px; color: var(--muted); margin: 0 0 4px; }
.ph-title { font-size: 20px; font-weight: 700; letter-spacing: -0.3px; margin: 0 0 4px; }
.ph-sub   { font-size: 13px; color: var(--muted); margin: 0; }

/* 4-card stats strip idêntico ao Kirvano */
.ph-stats {
  display: flex;
  border: 1px solid var(--border);
  border-radius: var(--radius);
  overflow: hidden;
  flex-shrink: 0;
}

.qs {
  display: flex;
  flex-direction: column;
  gap: 2px;
  padding: 14px 22px;
  background: var(--white);
  border-right: 1px solid var(--border);
  min-width: 150px;
  transition: background .12s;
}
.qs:last-child { border-right: none; }
.qs:hover { background: var(--bg); }

.qs-label { font-size: 11px; color: var(--muted); font-weight: 500; }
.qs-val   { font-size: 17px; font-weight: 700; color: var(--text); letter-spacing: -.3px; }
.qs-sub   { font-size: 11px; color: var(--muted); }

/* ══════════════════════════════════════
   BODY
══════════════════════════════════════ */
.body-grid {
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: 20px;
  padding: 24px 40px;
  max-width: 1200px;
}

/* ══════════════════════════════════════
   CARD
══════════════════════════════════════ */
.card {
  background: var(--white);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  overflow: hidden;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  border-bottom: 1px solid var(--border);
}

.ch-left { display: flex; flex-direction: column; gap: 2px; }
.card-title { font-size: 14px; font-weight: 600; color: var(--text); }
.card-sub   { font-size: 12px; color: var(--muted); }

.saiba-link {
  font-size: 12px;
  font-weight: 600;
  color: #2563EB;
  text-decoration: none;
  white-space: nowrap;
}
.saiba-link:hover { text-decoration: underline; }

/* ══════════════════════════════════════
   PROGRESS
══════════════════════════════════════ */
.prog-section {
  padding: 14px 20px;
  border-bottom: 1px solid var(--border);
}

.prog-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
  font-size: 12px;
}

.prog-cur      { font-weight: 600; color: var(--text); }
.prog-pct-lbl  { color: var(--muted); }
.prog-nxt      { margin-left: auto; color: var(--muted); }

.prog-track {
  height: 5px;
  background: var(--bg);
  border: 1px solid var(--border);
  border-radius: 100px;
  overflow: hidden;
}

.prog-fill { height: 100%; border-radius: 100px; transition: width .8s cubic-bezier(.16,1,.3,1); }
.fill-bronze   { background: var(--bronze-bar);   }
.fill-silver   { background: var(--silver-bar);   }
.fill-gold     { background: var(--gold-bar);     }
.fill-platinum { background: var(--platinum-bar); }
.fill-diamond  { background: var(--diamond-bar);  }

/* ══════════════════════════════════════
   LEVELS LIST
══════════════════════════════════════ */
.levels-list { padding: 4px 0; }

.lv-row {
  position: relative;
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 11px 20px;
  transition: background .1s;
}
.lv-row:hover { background: var(--bg); }

/* connector line between nodes */
.lv-line {
  position: absolute;
  left: 31px; bottom: -8px;
  width: 2px; height: 16px;
  background: var(--border);
  border-radius: 1px;
  z-index: 1;
}
.lv-line-bronze   { background: var(--bronze-bar);   }
.lv-line-silver   { background: var(--silver-bar);   }
.lv-line-gold     { background: var(--gold-bar);     }
.lv-line-platinum { background: var(--platinum-bar); }
.lv-line-diamond  { background: var(--diamond-bar);  }

/* node circle */
.lv-node {
  width: 26px; height: 26px;
  border-radius: 7px;
  background: var(--bg);
  border: 1.5px solid var(--border);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  flex-shrink: 0;
  z-index: 2;
  transition: all .18s;
}
.lv-node-bronze   { background: var(--bronze-bar);   border-color: transparent; }
.lv-node-silver   { background: var(--silver-bar);   border-color: transparent; }
.lv-node-gold     { background: var(--gold-bar);     border-color: transparent; }
.lv-node-platinum { background: var(--platinum-bar); border-color: transparent; }
.lv-node-diamond  { background: var(--diamond-bar);  border-color: transparent; }

.lv-check { font-size: 11px; font-weight: 800; color: #fff; }
.lv-locked { opacity: .35; }

.lv-body { flex: 1; }
.lv-name-row { display: flex; align-items: center; gap: 7px; margin-bottom: 1px; }
.lv-name     { font-size: 13px; font-weight: 600; color: var(--text); }
.lv-threshold{ font-size: 11px; color: var(--muted); }

/* tags */
.tag {
  font-size: 10px;
  font-weight: 600;
  padding: 1px 7px;
  border-radius: 100px;
  border: 1px solid transparent;
}
.tag-done   { background: #F0FDF4; color: #16A34A; border-color: #BBF7D0; }
.tag-locked { background: var(--bg); color: var(--muted); border-color: var(--border); }

.tag-cur { color: #fff; border-color: transparent; }
.tag-bronze   { background: var(--bronze-bar);   }
.tag-silver   { background: var(--silver-bar);   }
.tag-gold     { background: var(--gold-bar);     }
.tag-platinum { background: var(--platinum-bar); }
.tag-diamond  { background: var(--diamond-bar);  }

/* percent */
.lv-pct { font-size: 20px; font-weight: 800; letter-spacing: -.4px; min-width: 48px; text-align: right; transition: color .18s; }
.pct-sym { font-size: 13px; }
.pct-locked   { color: var(--border); }
.pct-bronze   { color: var(--bronze-c);   }
.pct-silver   { color: var(--silver-c);   }
.pct-gold     { color: var(--gold-c);     }
.pct-platinum { color: var(--platinum-c); }
.pct-diamond  { color: var(--diamond-c);  }

/* ══════════════════════════════════════
   MOTIVATION BANNER
══════════════════════════════════════ */
.mot-banner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: 12px 20px 16px;
  padding: 14px 16px;
  border-radius: 8px;
  border: 1px solid;
}

.mot-left { display: flex; align-items: flex-start; gap: 12px; }
.mot-icon { font-size: 20px; flex-shrink: 0; margin-top: 1px; }
.mot-title{ font-size: 13px; font-weight: 600; margin: 0 0 2px; color: var(--text); }
.mot-sub  { font-size: 12px; color: var(--muted); margin: 0; line-height: 1.5; }
.mot-arrow{ font-size: 18px; color: var(--muted); flex-shrink: 0; }

.mot-bronze   { background: var(--bronze-bg);   border-color: var(--bronze-bd);   }
.mot-silver   { background: var(--silver-bg);   border-color: var(--silver-bd);   }
.mot-gold     { background: var(--gold-bg);     border-color: var(--gold-bd);     }
.mot-platinum { background: var(--platinum-bg); border-color: var(--platinum-bd); }
.mot-diamond  { background: var(--diamond-bg);  border-color: var(--diamond-bd);  }
.mot-max      { background: #F0FDF4; border-color: #BBF7D0; }

/* ══════════════════════════════════════
   RIGHT COL
══════════════════════════════════════ */
.right-col { display: flex; flex-direction: column; gap: 16px; }

.field-list { }
.field-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 11px 20px;
  border-bottom: 1px solid var(--border);
}
.field-row:last-child { border-bottom: none; }
.fk { font-size: 12px; color: var(--muted); font-weight: 500; }
.fv { font-size: 13px; font-weight: 500; color: var(--text2); text-align: right; max-width: 60%; word-break: break-all; }

.edit-btn {
  font-size: 12px; font-weight: 500; color: var(--muted);
  background: none; border: 1px solid var(--border);
  border-radius: 6px; padding: 4px 10px; cursor: pointer;
  font-family: inherit; transition: all .12s;
}
.edit-btn:hover { background: var(--bg); color: var(--text); }

.edit-body {
  padding: 14px 20px;
  display: flex; flex-direction: column; gap: 12px;
}
.edit-grp { display: flex; flex-direction: column; gap: 4px; }
.edit-grp label { font-size: 11px; font-weight: 600; color: var(--muted); text-transform: uppercase; letter-spacing: .07em; }
.edit-grp input {
  background: var(--bg); border: 1px solid var(--border);
  border-radius: 6px; padding: 8px 11px;
  font-size: 13px; font-family: inherit; color: var(--text);
  outline: none; transition: border-color .15s;
}
.edit-grp input:focus { border-color: #9CA3AF; }

.save-btn {
  background: var(--text); color: #fff; border: none;
  border-radius: 6px; padding: 9px 16px;
  font-size: 13px; font-weight: 600; font-family: inherit;
  cursor: pointer; align-self: flex-start; transition: opacity .15s;
}
.save-btn:hover { opacity: .82; }

/* ══════════════════════════════════════
   CUSTOMER VIEW
══════════════════════════════════════ */
.customer-grid {
  display: grid;
  grid-template-columns: 1fr 280px;
  gap: 20px;
  padding: 24px 40px;
  max-width: 1100px;
  align-items: start;
}

.dropper-cta { display: flex; flex-direction: column; }

.tag-programa {
  font-size: 10px; font-weight: 700; text-transform: uppercase;
  letter-spacing: .1em; color: #7C3AED;
  background: #F5F3FF; border: 1px solid #DDD6FE;
  padding: 3px 10px; border-radius: 100px;
}

.cta-body { padding: 20px; display: flex; flex-direction: column; gap: 16px; }
.cta-desc { font-size: 13px; color: var(--text2); line-height: 1.6; margin: 0; }

.cta-levels { display: flex; flex-direction: column; gap: 1px; background: var(--border); border-radius: 6px; overflow: hidden; }

.cta-lv {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 14px;
  background: var(--white);
  border-left: 3px solid transparent;
  transition: background .1s;
}
.cta-lv:hover { background: var(--bg); }

.cta-lv-bronze   { border-left-color: var(--bronze-bar);   }
.cta-lv-silver   { border-left-color: var(--silver-bar);   }
.cta-lv-gold     { border-left-color: var(--gold-bar);     }
.cta-lv-platinum { border-left-color: var(--platinum-bar); }
.cta-lv-diamond  { border-left-color: var(--diamond-bar);  }

.cta-lv-dot {
  width: 26px; height: 26px; border-radius: 7px;
  display: flex; align-items: center; justify-content: center;
  font-size: 13px; flex-shrink: 0;
}
.dot-bronze   { background: var(--bronze-bg);   }
.dot-silver   { background: var(--silver-bg);   }
.dot-gold     { background: var(--gold-bg);     }
.dot-platinum { background: var(--platinum-bg); }
.dot-diamond  { background: var(--diamond-bg);  }

.cta-lv-name { font-size: 13px; font-weight: 600; flex: 1; }
.cta-lv-thr  { font-size: 11px; color: var(--muted); margin-right: 8px; }
.cta-lv-pct  { font-size: 15px; font-weight: 800; }
.cpct-bronze   { color: var(--bronze-c);   }
.cpct-silver   { color: var(--silver-c);   }
.cpct-gold     { color: var(--gold-c);     }
.cpct-platinum { color: var(--platinum-c); }
.cpct-diamond  { color: var(--diamond-c);  }

.cta-action {
  display: block; background: var(--text); color: #fff;
  border-radius: 7px; padding: 11px 18px;
  font-size: 13px; font-weight: 600; text-align: center;
  text-decoration: none; transition: opacity .15s; font-family: inherit;
}
.cta-action:hover { opacity: .82; }

.customer-side { display: flex; flex-direction: column; gap: 14px; }

.cstats { display: flex; align-items: center; padding: 20px; }
.cstat  { flex: 1; text-align: center; }
.cstat-n {
  display: block; font-size: 30px; font-weight: 800;
  letter-spacing: -.5px; color: var(--text);
}
.cstat-l {
  display: block; font-size: 11px; color: var(--muted);
  margin-top: 2px; text-transform: uppercase; letter-spacing: .07em;
}
.cstat-sep { width: 1px; height: 40px; background: var(--border); flex-shrink: 0; }

/* ══════════════════════════════════════
   RESPONSIVE
══════════════════════════════════════ */
@media (max-width: 1024px) {
  .body-grid, .customer-grid { grid-template-columns: 1fr; padding: 20px 24px; }
  .rank-banner { padding: 0 24px; flex-wrap: wrap; height: auto; padding: 12px 24px; gap: 16px; }
  .rb-center   { order: -1; width: 100%; justify-content: flex-start; }
  .rb-right    { display: none; }
  .page-header { padding: 20px 24px; flex-direction: column; align-items: flex-start; }
  .ph-stats    { display: none; }
}

@media (max-width: 640px) {
  .rank-banner { padding: 12px 16px; }
  .body-grid, .customer-grid { padding: 12px 16px; }
  .page-header { padding: 20px 16px; }
}
</style>
<template>
  <div class="profile-page">
    <div class="profile-hero" :class="heroClass">
      <div class="hero-pattern"></div>
      <div class="hero-content">
        <div class="avatar-wrapper">
          <div class="avatar-ring" :style="{ borderColor: levelColor }"></div>
          <div class="avatar">
            <span class="avatar-initials">{{ userInitials }}</span>
          </div>
          <div v-if="isDropper" class="level-badge-avatar" :style="{ background: levelGradient }">
            <span class="level-badge-icon">{{ levelIcon }}</span>
          </div>
        </div>

        <div class="hero-info">
          <div class="hero-name-row">
            <h1 class="hero-name">{{ user.name }}</h1>
            <div v-if="isDropper" class="level-pill" :style="{ background: levelGradient }">
              <span>{{ levelIcon }}</span>
              <span>{{ levelLabel }}</span>
            </div>
            <div v-else class="customer-pill">
              <span>👤</span>
              <span>Cliente</span>
            </div>
          </div>
          <p class="hero-email">{{ user.email }}</p>
          <p v-if="isDropper" class="hero-store">
            <span class="store-icon">🏪</span>
            {{ dropper.storeName }}
          </p>
        </div>
      </div>
    </div>

    <div class="profile-body">

      <template v-if="isDropper">

        <div class="level-card" :class="'level-card--' + dropper.level.toLowerCase()">
          <div class="level-card-bg"></div>
          <div class="level-card-content">

            <div class="level-header">
              <div class="level-icon-big">{{ levelIcon }}</div>
              <div>
                <div class="level-name">{{ levelLabel }}</div>
                <div class="level-discount">{{ levelDiscount }}% de desconto</div>
              </div>
              <div class="level-xp-badge">
                <span class="xp-value">{{ dropper.xp.toLocaleString() }}</span>
                <span class="xp-label">XP</span>
              </div>
            </div>

            <div v-if="nextLevel" class="progress-section">
              <div class="progress-labels">
                <span>{{ levelLabel }}</span>
                <span class="progress-next">{{ nextLevel.label }} em {{ nextLevel.remaining }}</span>
              </div>
              <div class="progress-track">
                <div
                    class="progress-fill"
                    :style="{ width: progressPercent + '%', background: levelGradient }"
                ></div>
              </div>
              <div class="progress-values">
                <span>R$ {{ dropper.totalSales.toLocaleString('pt-BR', { minimumFractionDigits: 2 }) }}</span>
                <span>R$ {{ nextLevel.threshold.toLocaleString('pt-BR') }}</span>
              </div>
            </div>

            <div v-else class="max-level">
              <span class="max-icon">👑</span>
              <span>Nível máximo atingido!</span>
            </div>

          </div>
        </div>

        <div class="stats-grid">
          <div class="stat-card">
            <div class="stat-icon" style="background: #EFF6FF; color: #3B82F6;">💰</div>
            <div class="stat-info">
              <span class="stat-label">Total de Vendas</span>
              <span class="stat-value">R$ {{ dropper.totalSales.toLocaleString('pt-BR', { minimumFractionDigits: 2 }) }}</span>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon" style="background: #F0FDF4; color: #22C55E;">📦</div>
            <div class="stat-info">
              <span class="stat-label">Pedidos Enviados</span>
              <span class="stat-value">{{ dropper.totalOrders }}</span>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon" style="background: #FFF7ED; color: #F97316;">⚡</div>
            <div class="stat-info">
              <span class="stat-label">XP Total</span>
              <span class="stat-value">{{ dropper.xp.toLocaleString() }} xp</span>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon" style="background: #FDF4FF; color: #A855F7;">🏷️</div>
            <div class="stat-info">
              <span class="stat-label">Desconto Atual</span>
              <span class="stat-value">{{ levelDiscount }}%</span>
            </div>
          </div>
        </div>

        <div class="section-card">
          <h3 class="section-title">Jornada de Níveis</h3>
          <div class="levels-timeline">
            <div
                v-for="lvl in levels"
                :key="lvl.key"
                class="timeline-item"
                :class="{
                'timeline-item--active': dropper.level === lvl.key,
                'timeline-item--done': isLevelDone(lvl.key),
                'timeline-item--locked': !isLevelDone(lvl.key) && dropper.level !== lvl.key
              }"
            >
              <div class="timeline-dot" :style="isLevelDone(lvl.key) || dropper.level === lvl.key ? { background: lvl.gradient } : {}">
                <span>{{ isLevelDone(lvl.key) ? '✓' : lvl.icon }}</span>
              </div>
              <div class="timeline-line" v-if="lvl.key !== 'DIAMOND'"></div>
              <div class="timeline-info">
                <div class="timeline-name" :style="dropper.level === lvl.key ? { color: lvl.color } : {}">
                  {{ lvl.label }}
                </div>
                <div class="timeline-threshold">
                  {{ lvl.threshold === 0 ? 'Inicial' : 'R$ ' + lvl.threshold.toLocaleString('pt-BR') }}
                </div>
                <div class="timeline-discount" :style="{ color: lvl.color }">{{ lvl.discount }}% off</div>
              </div>
            </div>
          </div>
        </div>

        <div class="section-card">
          <h3 class="section-title">Dados da Loja</h3>
          <div class="info-list">
            <div class="info-row">
              <span class="info-key">Nome da Loja</span>
              <span class="info-val">{{ dropper.storeName }}</span>
            </div>
            <div class="info-row">
              <span class="info-key">CNPJ</span>
              <span class="info-val info-mono">{{ dropper.cnpj }}</span>
            </div>
            <div class="info-row">
              <span class="info-key">WhatsApp</span>
              <span class="info-val">{{ dropper.whatsapp }}</span>
            </div>
            <div class="info-row">
              <span class="info-key">Status</span>
              <span class="status-badge status-active">● Ativo</span>
            </div>
          </div>
        </div>

      </template>

      <template v-else>

        <div class="dropper-cta">
          <div class="cta-left">
            <div class="cta-icons">
              <span>🥉</span><span>🥈</span><span>🏅</span><span>💎</span>
            </div>
            <h3 class="cta-title">Torne-se um Dropper</h3>
            <p class="cta-desc">Venda nossos produtos com até <strong>30% de desconto</strong> e evolua por níveis.</p>
          </div>
          <button class="cta-btn" @click="$router.push('/dropper')">
            Quero ser Dropper →
          </button>
        </div>

        <div class="stats-grid">
          <div class="stat-card">
            <div class="stat-icon" style="background: #EFF6FF; color: #3B82F6;">🛒</div>
            <div class="stat-info">
              <span class="stat-label">Total de Pedidos</span>
              <span class="stat-value">{{ user.totalOrders ?? 0 }}</span>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon" style="background: #F0FDF4; color: #22C55E;">📍</div>
            <div class="stat-info">
              <span class="stat-label">Pedidos Entregues</span>
              <span class="stat-value">{{ user.deliveredOrders ?? 0 }}</span>
            </div>
          </div>
        </div>

      </template>

      <div class="section-card">
        <div class="section-card-header">
          <h3 class="section-title">Dados Pessoais</h3>
          <button class="edit-btn" @click="editMode = !editMode">
            {{ editMode ? 'Cancelar' : '✏️ Editar' }}
          </button>
        </div>

        <div v-if="!editMode" class="info-list">
          <div class="info-row">
            <span class="info-key">Nome</span>
            <span class="info-val">{{ user.name }}</span>
          </div>
          <div class="info-row">
            <span class="info-key">E-mail</span>
            <span class="info-val">{{ user.email }}</span>
          </div>
          <div class="info-row">
            <span class="info-key">Membro desde</span>
            <span class="info-val">{{ formatDate(user.createdAt) }}</span>
          </div>
        </div>

        <div v-else class="edit-form">
          <div class="form-field">
            <label>Nome</label>
            <input v-model="editName" type="text" class="field-input" />
          </div>
          <div class="form-field">
            <label>E-mail</label>
            <input v-model="editEmail" type="email" class="field-input" />
          </div>
          <div class="edit-actions">
            <button class="btn-save" @click="saveProfile">Salvar</button>
            <button class="btn-cancel" @click="editMode = false">Cancelar</button>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useAuthStore } from '../stores/auth'

const auth = useAuthStore()


const user = computed(() => auth.user)

const isDropper = computed(() =>
    user.value?.role === 'DROPPER' || user.value?.role === 'OWNER'
)

const dropper = ref({
  level: 'GOLD',
  xp: 4350,
  totalSales: 23800.00,
  totalOrders: 47,
  storeName: 'Minha Loja Top',
  cnpj: '12.345.678/0001-99',
  whatsapp: '(41) 99999-8888',
  status: 'ACTIVE'
})

const levels = [
  { key: 'BRONZE',   label: 'Bronze',   icon: '🥉', discount: 5,  threshold: 0,      color: '#92400e', gradient: 'linear-gradient(135deg, #fde68a, #f59e0b)' },
  { key: 'SILVER',   label: 'Prata',    icon: '🥈', discount: 10, threshold: 5000,   color: '#374151', gradient: 'linear-gradient(135deg, #e2e8f0, #94a3b8)' },
  { key: 'GOLD',     label: 'Ouro',     icon: '🏅', discount: 15, threshold: 20000,  color: '#854d0e', gradient: 'linear-gradient(135deg, #fef08a, #f59e0b)' },
  { key: 'PLATINUM', label: 'Platina',  icon: '🎖️', discount: 22, threshold: 60000,  color: '#075985', gradient: 'linear-gradient(135deg, #bae6fd, #0ea5e9)' },
  { key: 'DIAMOND',  label: 'Diamante', icon: '💎', discount: 30, threshold: 150000, color: '#6b21a8', gradient: 'linear-gradient(135deg, #e9d5ff, #a855f7)' },
]

const currentLevel = computed(() =>
    levels.find(l => l.key === dropper.value.level) ?? levels[0]
)


const levelIcon     = computed(() => currentLevel.value.icon)
const levelLabel    = computed(() => currentLevel.value.label)
const levelDiscount = computed(() => currentLevel.value.discount)
const levelColor    = computed(() => currentLevel.value.color)
const levelGradient = computed(() => currentLevel.value.gradient)

const heroClass = computed(() => {
  if (!isDropper.value) return ''
  return 'hero-' + dropper.value.level.toLowerCase()
})

const userInitials = computed(() => {
  const name = user.value?.name ?? ''
  return name
      .split(' ')
      .filter(Boolean)
      .slice(0, 2)
      .map(n => n[0].toUpperCase())
      .join('')
})

const nextLevel = computed(() => {
  const idx = levels.findIndex(l => l.key === dropper.value.level)
  if (idx === -1 || idx === levels.length - 1) return null // já é Diamond

  const next = levels[idx + 1]
  const current = levels[idx]
  const remaining = 'R$ ' + (next.threshold - dropper.value.totalSales)
      .toLocaleString('pt-BR', { minimumFractionDigits: 2 })

  return { label: next.label, threshold: next.threshold, remaining }
})

const progressPercent = computed(() => {
  const idx = levels.findIndex(l => l.key === dropper.value.level)
  if (idx === -1 || idx === levels.length - 1) return 100

  const current = levels[idx]
  const next = levels[idx + 1]
  const range = next.threshold - current.threshold
  const progress = dropper.value.totalSales - current.threshold
  return Math.min(100, Math.max(0, (progress / range) * 100))
})

function isLevelDone(key) {
  const myIdx  = levels.findIndex(l => l.key === dropper.value.level)
  const thisIdx = levels.findIndex(l => l.key === key)
  return thisIdx < myIdx
}

const editMode  = ref(false)
const editName  = ref(user.value?.name ?? '')
const editEmail = ref(user.value?.email ?? '')

function saveProfile() {
  // TODO: chamar PUT /api/user/profile
  console.log('Salvar:', editName.value, editEmail.value)
  editMode.value = false
}

function formatDate(date) {
  if (!date) return '—'
  return new Date(date).toLocaleDateString('pt-BR', {
    day: '2-digit', month: 'long', year: 'numeric'
  })
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Syne:wght@600;700;800&family=DM+Sans:wght@400;500;600&display=swap');

:root {
  --blue-50: #EFF6FF;
  --blue-100: #DBEAFE;
  --blue-500: #3B82F6;
  --blue-600: #2563EB;
  --blue-700: #1D4ED8;
  --gray-50: #F9FAFB;
  --gray-100: #F3F4F6;
  --gray-200: #E5E7EB;
  --gray-400: #9CA3AF;
  --gray-500: #6B7280;
  --gray-700: #374151;
  --gray-900: #111827;
}

.profile-page {
  font-family: 'DM Sans', sans-serif;
  background: #F8FAFC;
  min-height: 100vh;
  color: var(--gray-900);
}

.profile-hero {
  position: relative;
  background: linear-gradient(135deg, #1e3a8a 0%, #2563eb 60%, #3b82f6 100%);
  padding: 48px 32px 64px;
  overflow: hidden;
}

.profile-hero.hero-bronze  { background: linear-gradient(135deg, #451a03 0%, #92400e 60%, #b45309 100%); }
.profile-hero.hero-silver  { background: linear-gradient(135deg, #1f2937 0%, #4b5563 60%, #6b7280 100%); }
.profile-hero.hero-gold    { background: linear-gradient(135deg, #451a03 0%, #b45309 60%, #f59e0b 100%); }
.profile-hero.hero-platinum{ background: linear-gradient(135deg, #0f172a 0%, #1e3a8a 50%, #0891b2 100%); }
.profile-hero.hero-diamond { background: linear-gradient(135deg, #1e1b4b 0%, #4c1d95 50%, #7c3aed 100%); }

.hero-pattern {
  position: absolute;
  inset: 0;
  background-image: radial-gradient(circle at 20% 50%, rgba(255,255,255,0.06) 0%, transparent 50%),
  radial-gradient(circle at 80% 20%, rgba(255,255,255,0.08) 0%, transparent 40%);
}

.hero-content {
  position: relative;
  max-width: 800px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  gap: 28px;
}

.avatar-wrapper {
  position: relative;
  flex-shrink: 0;
}

.avatar-ring {
  position: absolute;
  inset: -4px;
  border-radius: 50%;
  border: 2px solid rgba(255,255,255,0.4);
}

.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: rgba(255,255,255,0.15);
  backdrop-filter: blur(8px);
  border: 2px solid rgba(255,255,255,0.3);
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-initials {
  font-family: 'Syne', sans-serif;
  font-size: 28px;
  font-weight: 700;
  color: white;
}

.level-badge-avatar {
  position: absolute;
  bottom: -4px;
  right: -4px;
  width: 28px;
  height: 28px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  border: 2px solid white;
  box-shadow: 0 2px 8px rgba(0,0,0,0.3);
}

.hero-info {
  flex: 1;
}

.hero-name-row {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
  margin-bottom: 4px;
}

.hero-name {
  font-family: 'Syne', sans-serif;
  font-size: 28px;
  font-weight: 800;
  color: white;
  margin: 0;
}

/* Pills */
.level-pill, .customer-pill {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 12px;
  border-radius: 99px;
  font-size: 12px;
  font-weight: 600;
  color: white;
  letter-spacing: 0.03em;
  box-shadow: 0 2px 8px rgba(0,0,0,0.25);
}

.customer-pill {
  background: rgba(255,255,255,0.2);
  backdrop-filter: blur(8px);
}

.hero-email {
  color: rgba(255,255,255,0.7);
  font-size: 14px;
  margin: 0 0 4px;
}

.hero-store {
  display: flex;
  align-items: center;
  gap: 6px;
  color: rgba(255,255,255,0.9);
  font-size: 14px;
  font-weight: 500;
  margin: 0;
}

.profile-body {
  max-width: 800px;
  margin: -24px auto 0;
  padding: 0 24px 48px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.level-card {
  position: relative;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 4px 24px rgba(0,0,0,0.10);
}

.level-card-bg {
  position: absolute;
  inset: 0;
  opacity: 0.07;
}

.level-card--bronze  { background: linear-gradient(135deg, #fef3c7, #fde68a); }
.level-card--bronze .level-card-bg { background: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23b45309' fill-opacity='0.3'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E"); }

.level-card--silver  { background: linear-gradient(135deg, #f1f5f9, #e2e8f0); }
.level-card--gold    { background: linear-gradient(135deg, #fef9c3, #fef08a); }
.level-card--platinum{ background: linear-gradient(135deg, #e0f2fe, #bae6fd); }
.level-card--diamond { background: linear-gradient(135deg, #f3e8ff, #e9d5ff); }

.level-card-content {
  position: relative;
  padding: 28px;
}

.level-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 24px;
}

.level-icon-big {
  font-size: 48px;
  line-height: 1;
  filter: drop-shadow(0 2px 6px rgba(0,0,0,0.15));
}

.level-name {
  font-family: 'Syne', sans-serif;
  font-size: 22px;
  font-weight: 800;
  color: var(--gray-900);
}

.level-discount {
  font-size: 14px;
  font-weight: 500;
  color: var(--gray-500);
  margin-top: 2px;
}

.level-xp-badge {
  margin-left: auto;
  text-align: right;
}

.xp-value {
  display: block;
  font-family: 'Syne', sans-serif;
  font-size: 28px;
  font-weight: 800;
  color: var(--gray-900);
  line-height: 1;
}

.xp-label {
  font-size: 11px;
  font-weight: 600;
  color: var(--gray-400);
  text-transform: uppercase;
  letter-spacing: 0.1em;
}

.progress-section { margin-top: 4px; }

.progress-labels {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  font-weight: 600;
  color: var(--gray-500);
  margin-bottom: 8px;
}

.progress-next { color: var(--gray-700); }

.progress-track {
  height: 8px;
  background: rgba(0,0,0,0.08);
  border-radius: 99px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  border-radius: 99px;
  transition: width 1s cubic-bezier(0.4, 0, 0.2, 1);
}

.progress-values {
  display: flex;
  justify-content: space-between;
  font-size: 11px;
  color: var(--gray-400);
  margin-top: 6px;
}

.max-level {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  font-weight: 600;
  color: var(--gray-700);
}

.max-icon { font-size: 20px; }

.stats-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

@media (min-width: 600px) {
  .stats-grid { grid-template-columns: repeat(4, 1fr); }
}

.stat-card {
  background: white;
  border-radius: 16px;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.06);
  border: 1px solid var(--gray-100);
}

.stat-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
}

.stat-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.stat-label {
  font-size: 11px;
  color: var(--gray-400);
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.stat-value {
  font-family: 'Syne', sans-serif;
  font-size: 18px;
  font-weight: 700;
  color: var(--gray-900);
}

.section-card {
  background: white;
  border-radius: 20px;
  padding: 28px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.06);
  border: 1px solid var(--gray-100);
}

.section-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-title {
  font-family: 'Syne', sans-serif;
  font-size: 16px;
  font-weight: 700;
  color: var(--gray-900);
  margin: 0 0 20px;
}

.section-card-header .section-title { margin: 0; }

.edit-btn {
  font-size: 13px;
  font-weight: 500;
  color: var(--blue-600);
  background: var(--blue-50);
  border: none;
  padding: 6px 14px;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.15s;
}
.edit-btn:hover { background: var(--blue-100); }

.levels-timeline {
  display: flex;
  justify-content: space-between;
  position: relative;
  padding: 0 8px;
}

.timeline-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  flex: 1;
}

.timeline-dot {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: var(--gray-100);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  position: relative;
  z-index: 1;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  transition: transform 0.2s;
}

.timeline-item--active .timeline-dot {
  transform: scale(1.15);
  box-shadow: 0 4px 16px rgba(0,0,0,0.15);
}

.timeline-item--locked .timeline-dot {
  opacity: 0.35;
  filter: grayscale(0.7);
}

.timeline-line {
  position: absolute;
  top: 22px;
  left: 50%;
  width: 100%;
  height: 2px;
  background: var(--gray-200);
  z-index: 0;
}

.timeline-item--done .timeline-line,
.timeline-item--active .timeline-line {
  background: linear-gradient(90deg, #3b82f6, var(--gray-200));
}

.timeline-info {
  margin-top: 10px;
  text-align: center;
}

.timeline-name {
  font-size: 11px;
  font-weight: 700;
  color: var(--gray-700);
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.timeline-threshold {
  font-size: 10px;
  color: var(--gray-400);
  margin-top: 2px;
}

.timeline-discount {
  font-size: 12px;
  font-weight: 700;
  margin-top: 2px;
}

.info-list { display: flex; flex-direction: column; }

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 14px 0;
  border-bottom: 1px solid var(--gray-100);
}
.info-row:last-child { border-bottom: none; }

.info-key {
  font-size: 13px;
  color: var(--gray-400);
  font-weight: 500;
}

.info-val {
  font-size: 14px;
  font-weight: 600;
  color: var(--gray-900);
}

.info-mono {
  font-family: monospace;
  font-size: 13px;
  letter-spacing: 0.05em;
  background: var(--gray-50);
  padding: 3px 8px;
  border-radius: 6px;
}

.status-badge {
  font-size: 12px;
  font-weight: 600;
  padding: 4px 12px;
  border-radius: 99px;
}

.status-active {
  background: #DCFCE7;
  color: #15803D;
}

.edit-form { display: flex; flex-direction: column; gap: 16px; }

.form-field { display: flex; flex-direction: column; gap: 6px; }

.form-field label {
  font-size: 12px;
  font-weight: 600;
  color: var(--gray-500);
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.field-input {
  padding: 10px 14px;
  border: 1.5px solid var(--gray-200);
  border-radius: 10px;
  font-family: 'DM Sans', sans-serif;
  font-size: 14px;
  color: var(--gray-900);
  outline: none;
  transition: border-color 0.15s;
}
.field-input:focus { border-color: var(--blue-500); }

.edit-actions { display: flex; gap: 10px; }

.btn-save {
  flex: 1;
  padding: 10px;
  background: var(--blue-600);
  color: white;
  border: none;
  border-radius: 10px;
  font-family: 'DM Sans', sans-serif;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.15s;
}
.btn-save:hover { background: var(--blue-700); }

.btn-cancel {
  padding: 10px 20px;
  background: var(--gray-100);
  color: var(--gray-700);
  border: none;
  border-radius: 10px;
  font-family: 'DM Sans', sans-serif;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
}

.dropper-cta {
  background: linear-gradient(135deg, #EFF6FF, #DBEAFE);
  border: 1.5px solid var(--blue-100);
  border-radius: 20px;
  padding: 28px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
  flex-wrap: wrap;
}

.cta-icons {
  font-size: 22px;
  letter-spacing: 2px;
  margin-bottom: 8px;
}

.cta-title {
  font-family: 'Syne', sans-serif;
  font-size: 18px;
  font-weight: 800;
  color: var(--gray-900);
  margin: 0 0 6px;
}

.cta-desc {
  font-size: 13px;
  color: var(--gray-500);
  margin: 0;
}

.cta-btn {
  white-space: nowrap;
  padding: 12px 24px;
  background: var(--blue-600);
  color: white;
  border: none;
  border-radius: 12px;
  font-family: 'DM Sans', sans-serif;
  font-size: 14px;
  font-weight: 700;
  cursor: pointer;
  transition: background 0.15s, transform 0.15s;
}
.cta-btn:hover {
  background: var(--blue-700);
  transform: translateY(-1px);
}
</style>
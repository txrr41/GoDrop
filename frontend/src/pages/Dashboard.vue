<template>
  <div class="dashboard-page">

    <!-- ══════════════════════════════════════════════
         HEADER
    ══════════════════════════════════════════════ -->
    <header class="dash-header">
      <div class="dash-header__left">
        <p class="dash-header__greeting">Bom dia, Admin 👋</p>
        <h1 class="dash-header__title">Dashboard</h1>
      </div>
      <div class="dash-header__right">
        <div class="date-pill">
          <v-icon size="15" class="mr-1" style="color:#0061FF">mdi-calendar-month-outline</v-icon>
          24 de Fevereiro, 2026
        </div>
        <button class="notif-btn">
          <v-icon size="20">mdi-bell-outline</v-icon>
          <span class="notif-dot"></span>
        </button>
        <div class="dash-avatar">
          <v-icon size="18" color="white">mdi-account</v-icon>
        </div>
      </div>
    </header>

    <!-- ══════════════════════════════════════════════
         KPI CARDS — 6 colunas
    ══════════════════════════════════════════════ -->
    <section class="kpi-row">

      <div class="kpi-card" style="--accent:#0061FF">
        <div class="kpi-card__top">
          <div class="kpi-card__icon" style="background:#EFF6FF">
            <v-icon size="20" color="#0061FF">mdi-currency-usd</v-icon>
          </div>
          <span class="kpi-badge kpi-badge--up">▲ 23%</span>
        </div>
        <p class="kpi-card__label">Receita do Mês</p>
        <p class="kpi-card__value">R$ 48.392</p>
        <div class="kpi-progress"><div class="kpi-progress__fill" style="width:74%;background:#0061FF"></div></div>
        <p class="kpi-card__sub">Meta: R$ 65.000 · 74% atingida</p>
      </div>

      <div class="kpi-card" style="--accent:#7C3AED">
        <div class="kpi-card__top">
          <div class="kpi-card__icon" style="background:#F5F3FF">
            <v-icon size="20" color="#7C3AED">mdi-package-variant-closed</v-icon>
          </div>
          <span class="kpi-badge kpi-badge--up">▲ 8%</span>
        </div>
        <p class="kpi-card__label">Pedidos Hoje</p>
        <p class="kpi-card__value">127</p>
        <div class="kpi-progress"><div class="kpi-progress__fill" style="width:51%;background:#7C3AED"></div></div>
        <p class="kpi-card__sub">Meta diária: 250 pedidos</p>
      </div>

      <div class="kpi-card" style="--accent:#0891B2">
        <div class="kpi-card__top">
          <div class="kpi-card__icon" style="background:#ECFEFF">
            <v-icon size="20" color="#0891B2">mdi-tag-multiple-outline</v-icon>
          </div>
          <span class="kpi-badge kpi-badge--neutral">→ 0%</span>
        </div>
        <p class="kpi-card__label">Produtos Ativos</p>
        <p class="kpi-card__value">1.284</p>
        <div class="kpi-progress"><div class="kpi-progress__fill" style="width:88%;background:#0891B2"></div></div>
        <p class="kpi-card__sub">88% do catálogo disponível</p>
      </div>

      <div class="kpi-card" style="--accent:#EF4444">
        <div class="kpi-card__top">
          <div class="kpi-card__icon" style="background:#FEF2F2">
            <v-icon size="20" color="#EF4444">mdi-alert-circle-outline</v-icon>
          </div>
          <span class="kpi-badge kpi-badge--down">▼ 5%</span>
        </div>
        <p class="kpi-card__label">Estoque Crítico</p>
        <p class="kpi-card__value">34</p>
        <div class="kpi-progress"><div class="kpi-progress__fill" style="width:34%;background:#EF4444"></div></div>
        <p class="kpi-card__sub">Produtos abaixo de 10 un.</p>
      </div>

      <div class="kpi-card" style="--accent:#10B981">
        <div class="kpi-card__top">
          <div class="kpi-card__icon" style="background:#ECFDF5">
            <v-icon size="20" color="#10B981">mdi-account-group-outline</v-icon>
          </div>
          <span class="kpi-badge kpi-badge--up">▲ 12%</span>
        </div>
        <p class="kpi-card__label">Novos Clientes</p>
        <p class="kpi-card__value">318</p>
        <div class="kpi-progress"><div class="kpi-progress__fill" style="width:63%;background:#10B981"></div></div>
        <p class="kpi-card__sub">Este mês</p>
      </div>

      <div class="kpi-card" style="--accent:#F59E0B">
        <div class="kpi-card__top">
          <div class="kpi-card__icon" style="background:#FFFBEB">
            <v-icon size="20" color="#F59E0B">mdi-target</v-icon>
          </div>
          <span class="kpi-badge kpi-badge--up">▲ 4%</span>
        </div>
        <p class="kpi-card__label">Ticket Médio</p>
        <p class="kpi-card__value">R$ 381</p>
        <div class="kpi-progress"><div class="kpi-progress__fill" style="width:61%;background:#F59E0B"></div></div>
        <p class="kpi-card__sub">vs R$ 366 no mês anterior</p>
      </div>

    </section>

    <!-- ══════════════════════════════════════════════
         LINHA 2: Gráfico + Donut
    ══════════════════════════════════════════════ -->
    <section class="mid-row">

      <!-- Gráfico de barras (simulado com CSS) -->
      <div class="card chart-card">
        <div class="card__header">
          <div>
            <h2 class="card__title">Vendas por Período</h2>
            <p class="card__sub">Últimos 7 dias</p>
          </div>
          <div class="period-tabs">
            <button class="period-tab period-tab--active">7D</button>
            <button class="period-tab">30D</button>
            <button class="period-tab">90D</button>
          </div>
        </div>

        <div class="bar-chart">
          <div class="bar-chart__y-axis">
            <span>R$12k</span>
            <span>R$9k</span>
            <span>R$6k</span>
            <span>R$3k</span>
            <span>R$0</span>
          </div>
          <div class="bar-chart__body">
            <div class="bar-chart__gridlines">
              <div></div><div></div><div></div><div></div>
            </div>
            <div class="bar-chart__bars">
              <div class="bar-wrap" v-for="(bar, i) in chartData" :key="i">
                <div class="bar-tooltip">{{ bar.value }}</div>
                <div
                    class="bar"
                    :class="{ 'bar--highlight': bar.highlight }"
                    :style="{ height: bar.pct + '%' }"
                ></div>
                <span class="bar-label">{{ bar.day }}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="chart-footer">
          <div class="chart-footer__legend">
            <span class="legend-dot" style="background:#0061FF"></span>
            Receita bruta
          </div>
          <div class="chart-footer__summary">
            <span class="chart-total">R$ 59.400</span>
            <span class="chart-total-label">Total do período</span>
          </div>
        </div>
      </div>

      <!-- Donut de categorias -->
      <div class="card donut-card">
        <div class="card__header">
          <div>
            <h2 class="card__title">Vendas por Categoria</h2>
            <p class="card__sub">Este mês</p>
          </div>
        </div>

        <div class="donut-wrap">
          <svg viewBox="0 0 120 120" class="donut-svg">
            <circle cx="60" cy="60" r="45" fill="none" stroke="#F1F5F9" stroke-width="16"/>
            <circle cx="60" cy="60" r="45" fill="none" stroke="#0061FF" stroke-width="16"
                    stroke-dasharray="107.4 175.8" stroke-dashoffset="0" stroke-linecap="round"/>
            <circle cx="60" cy="60" r="45" fill="none" stroke="#7C3AED" stroke-width="16"
                    stroke-dasharray="67.8 215.4" stroke-dashoffset="-109.4" stroke-linecap="round"/>
            <circle cx="60" cy="60" r="45" fill="none" stroke="#10B981" stroke-width="16"
                    stroke-dasharray="50.9 232.3" stroke-dashoffset="-179.2" stroke-linecap="round"/>
            <circle cx="60" cy="60" r="45" fill="none" stroke="#F59E0B" stroke-width="16"
                    stroke-dasharray="56.5 226.7" stroke-dashoffset="-232.1" stroke-linecap="round"/>
            <text x="60" y="54" text-anchor="middle" class="donut-center-big">38%</text>
            <text x="60" y="66" text-anchor="middle" class="donut-center-sm">Eletrônicos</text>
          </svg>

          <div class="donut-legend">
            <div class="donut-item" v-for="cat in donutCategories" :key="cat.name">
              <span class="donut-dot" :style="{ background: cat.color }"></span>
              <span class="donut-name">{{ cat.name }}</span>
              <span class="donut-pct">{{ cat.pct }}%</span>
            </div>
          </div>
        </div>
      </div>

    </section>

    <!-- ══════════════════════════════════════════════
         LINHA 3: Pedidos + Top Produtos + Alertas
    ══════════════════════════════════════════════ -->
    <section class="bottom-row">

      <!-- Pedidos Recentes -->
      <div class="card orders-card">
        <div class="card__header">
          <div>
            <h2 class="card__title">Pedidos Recentes</h2>
            <p class="card__sub">Últimas 24 horas</p>
          </div>
          <button class="link-btn">Ver todos <v-icon size="14">mdi-arrow-right</v-icon></button>
        </div>

        <div class="orders-table">
          <div class="orders-table__head">
            <span>Pedido</span>
            <span>Cliente</span>
            <span>Produto</span>
            <span>Valor</span>
            <span>Status</span>
          </div>
          <div class="orders-table__row" v-for="order in recentOrders" :key="order.id">
            <span class="order-id">#{{ order.id }}</span>
            <div class="order-client">
              <div class="order-avatar" :style="{ background: order.avatarBg, color: order.avatarColor }">
                {{ order.initials }}
              </div>
              <span>{{ order.name }}</span>
            </div>
            <span class="order-product">{{ order.product }}</span>
            <span class="order-value">{{ order.value }}</span>
            <span :class="['status-pill', 'status-pill--' + order.statusKey]">{{ order.status }}</span>
          </div>
        </div>
      </div>

      <!-- Top Produtos -->
      <div class="card top-products-card">
        <div class="card__header">
          <div>
            <h2 class="card__title">Mais Vendidos</h2>
            <p class="card__sub">Top 5 este mês</p>
          </div>
          <button class="link-btn">Catálogo <v-icon size="14">mdi-arrow-right</v-icon></button>
        </div>

        <div class="top-list">
          <div class="top-item" v-for="(item, i) in topProducts" :key="i">
            <div class="top-rank" :class="'rank-' + (i+1)">{{ i + 1 }}</div>
            <div class="top-thumb" :style="{ background: item.bg }">{{ item.emoji }}</div>
            <div class="top-info">
              <p class="top-name">{{ item.name }}</p>
              <p class="top-cat">{{ item.category }}</p>
            </div>
            <div class="top-bar-wrap">
              <p class="top-units">{{ item.units }} un.</p>
              <div class="top-bar"><div class="top-bar__fill" :style="{ width: item.pct + '%' }"></div></div>
            </div>
            <span class="top-revenue">{{ item.revenue }}</span>
          </div>
        </div>
      </div>

      <!-- Alertas de Estoque -->
      <div class="card alerts-card">
        <div class="card__header">
          <div>
            <h2 class="card__title">Alertas de Estoque</h2>
            <p class="card__sub">Reposição urgente</p>
          </div>
          <span class="alert-count">34</span>
        </div>

        <div class="alerts-list">
          <div class="alert-item" :class="'alert-item--' + alert.level" v-for="alert in stockAlerts" :key="alert.name">
            <v-icon size="14" :color="alert.level === 'critical' ? '#EF4444' : '#F59E0B'">
              {{ alert.level === 'critical' ? 'mdi-circle' : 'mdi-circle' }}
            </v-icon>
            <div class="alert-info">
              <p class="alert-name">{{ alert.name }}</p>
              <p class="alert-detail">Estoque: <strong>{{ alert.stock }} un.</strong> · {{ alert.category }}</p>
            </div>
            <button class="repor-btn">Repor</button>
          </div>
        </div>
      </div>

    </section>

    <section class="mini-stats-bar">
      <div class="mini-stat" v-for="(stat, i) in miniStats" :key="i">
        <span class="mini-stat__icon">{{ stat.icon }}</span>
        <div>
          <p class="mini-stat__value">{{ stat.value }}</p>
          <p class="mini-stat__label">{{ stat.label }}</p>
        </div>
      </div>
    </section>

  </div>
</template>

<script setup>

const chartData = [
  { day: 'Seg', value: 'R$ 7.200', pct: 60,  highlight: false },
  { day: 'Ter', value: 'R$ 9.800', pct: 82,  highlight: false },
  { day: 'Qua', value: 'R$ 6.100', pct: 51,  highlight: false },
  { day: 'Qui', value: 'R$ 11.400', pct: 95, highlight: true  },
  { day: 'Sex', value: 'R$ 8.700', pct: 72,  highlight: false },
  { day: 'Sáb', value: 'R$ 10.300', pct: 86, highlight: false },
  { day: 'Dom', value: 'R$ 5.900', pct: 49,  highlight: false },
]

// Donut
const donutCategories = [
  { name: 'Eletrônicos', color: '#0061FF', pct: 38 },
  { name: 'Moda',        color: '#7C3AED', pct: 24 },
  { name: 'Casa',        color: '#10B981', pct: 18 },
  { name: 'Outros',      color: '#F59E0B', pct: 20 },
]

// Pedidos recentes
const recentOrders = [
  { id: '4821', name: 'Marcos Silva',  initials: 'M', avatarBg: '#DBEAFE', avatarColor: '#1D4ED8', product: 'iPhone 15 Pro',    value: 'R$ 7.299', status: 'Aprovado',   statusKey: 'approved'   },
  { id: '4820', name: 'Ana Souza',     initials: 'A', avatarBg: '#FCE7F3', avatarColor: '#BE185D', product: 'Tênis Nike Air',  value: 'R$ 849',   status: 'Enviado',    statusKey: 'shipped'    },
  { id: '4819', name: 'Roberto Lima',  initials: 'R', avatarBg: '#D1FAE5', avatarColor: '#065F46', product: 'Smart TV 55"',   value: 'R$ 3.199', status: 'Em preparo', statusKey: 'processing' },
  { id: '4818', name: 'Juliana Costa', initials: 'J', avatarBg: '#EDE9FE', avatarColor: '#6D28D9', product: 'Perfume Chanel', value: 'R$ 1.250', status: 'Pendente',   statusKey: 'pending'    },
  { id: '4817', name: 'Felipe Ramos',  initials: 'F', avatarBg: '#FEF3C7', avatarColor: '#92400E', product: 'Notebook Dell',  value: 'R$ 5.499', status: 'Entregue',   statusKey: 'delivered'  },
]

// Top Produtos
const topProducts = [
  { name: 'iPhone 15 Pro Max',  category: 'Eletrônicos', emoji: '📱', bg: '#DBEAFE', units: 234, pct: 94, revenue: 'R$ 1,7M' },
  { name: 'Nike Air Max 270',   category: 'Esportes',    emoji: '👟', bg: '#FCE7F3', units: 198, pct: 79, revenue: 'R$ 168k' },
  { name: 'Samsung Smart TV',   category: 'Eletrônicos', emoji: '📺', bg: '#D1FAE5', units: 143, pct: 57, revenue: 'R$ 457k' },
  { name: 'Kit Perfumes Chanel',category: 'Beleza',      emoji: '💄', bg: '#EDE9FE', units: 117, pct: 47, revenue: 'R$ 146k' },
  { name: 'Dell XPS 15',        category: 'Eletrônicos', emoji: '💻', bg: '#FEF3C7', units: 89,  pct: 36, revenue: 'R$ 489k' },
]

// Alertas de estoque
const stockAlerts = [
  { name: 'AirPods Pro 2ª Geração', stock: 2, category: 'Eletrônicos', level: 'critical' },
  { name: 'Apple Watch Series 9',   stock: 3, category: 'Eletrônicos', level: 'critical' },
  { name: 'Adidas Ultraboost',      stock: 7, category: 'Esportes',    level: 'warning'  },
  { name: 'Sony Alpha A7',          stock: 5, category: 'Eletrônicos', level: 'warning'  },
  { name: 'Dior Sauvage',           stock: 8, category: 'Beleza',      level: 'warning'  },
]

// Mini stats
const miniStats = [
  { icon: '⚡', value: '98.7%',  label: 'Uptime da plataforma' },
  { icon: '🚚', value: '1.8d',   label: 'Tempo médio de envio' },
  { icon: '⭐', value: '4.87',   label: 'Avaliação dos clientes' },
  { icon: '↩️', value: '2.3%',  label: 'Taxa de devolução' },
  { icon: '💳', value: '67%',    label: 'Pagamentos via PIX' },
  { icon: '🔄', value: '34%',    label: 'Clientes recorrentes' },
]
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap');

.dashboard-page {
  font-family: 'Plus Jakarta Sans', sans-serif;
  background: #F0F2F7;
  min-height: 100vh;
  padding: 36px 44px;
  color: #0F172A;
}

.dash-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 28px;
}

.dash-header__greeting {
  font-size: 14px;
  color: #64748B;
  font-weight: 600;
  margin: 0 0 4px;
}

.dash-header__title {
  font-size: 34px;
  font-weight: 800;
  color: #0F172A;
  margin: 0;
  letter-spacing: -0.04em;
}

.dash-header__right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.date-pill {
  display: flex;
  align-items: center;
  background: white;
  padding: 9px 16px;
  border-radius: 100px;
  font-size: 13px;
  font-weight: 600;
  color: #475569;
  box-shadow: 0 1px 3px rgba(0,0,0,0.06);
}

.notif-btn {
  position: relative;
  width: 42px;
  height: 42px;
  background: white;
  border: none;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 1px 3px rgba(0,0,0,0.06);
  color: #475569;
}

.notif-dot {
  position: absolute;
  top: 9px;
  right: 9px;
  width: 9px;
  height: 9px;
  background: #EF4444;
  border-radius: 50%;
  border: 2px solid white;
}

.dash-avatar {
  width: 42px;
  height: 42px;
  background: linear-gradient(135deg, #0061FF, #60A5FA);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.kpi-row {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 16px;
  margin-bottom: 20px;
}

.kpi-card {
  background: white;
  border-radius: 20px;
  padding: 20px;
  border-top: 3px solid var(--accent);
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
  transition: transform 0.2s, box-shadow 0.2s;
}

.kpi-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 24px rgba(0,0,0,0.08);
}

.kpi-card__top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
}

.kpi-card__icon {
  width: 40px;
  height: 40px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.kpi-badge {
  font-size: 11px;
  font-weight: 800;
  padding: 3px 8px;
  border-radius: 100px;
}
.kpi-badge--up      { background: #DCFCE7; color: #15803D; }
.kpi-badge--down    { background: #FEE2E2; color: #DC2626; }
.kpi-badge--neutral { background: #F1F5F9; color: #64748B; }

.kpi-card__label {
  font-size: 11px;
  font-weight: 700;
  color: #94A3B8;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  margin: 0 0 4px;
}

.kpi-card__value {
  font-size: 22px;
  font-weight: 800;
  color: #0F172A;
  margin: 0 0 10px;
  letter-spacing: -0.5px;
}

.kpi-progress {
  height: 4px;
  background: #F1F5F9;
  border-radius: 10px;
  margin-bottom: 8px;
  overflow: hidden;
}
.kpi-progress__fill {
  height: 100%;
  border-radius: 10px;
  transition: width 1s ease;
}

.kpi-card__sub {
  font-size: 11px;
  color: #94A3B8;
  margin: 0;
}

.card {
  background: white;
  border-radius: 20px;
  padding: 24px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
}

.card__header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
}

.card__title {
  font-size: 16px;
  font-weight: 800;
  color: #0F172A;
  margin: 0 0 3px;
  letter-spacing: -0.02em;
}

.card__sub {
  font-size: 13px;
  color: #94A3B8;
  margin: 0;
}

.link-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  font-weight: 700;
  color: #0061FF;
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
  transition: opacity 0.2s;
}
.link-btn:hover { opacity: 0.7; }

.mid-row {
  display: grid;
  grid-template-columns: 1fr 320px;
  gap: 20px;
  margin-bottom: 20px;
}

.period-tabs {
  display: flex;
  background: #F1F5F9;
  border-radius: 10px;
  padding: 3px;
  gap: 2px;
}
.period-tab {
  padding: 5px 14px;
  border: none;
  background: transparent;
  border-radius: 8px;
  font-size: 12px;
  font-weight: 700;
  color: #64748B;
  cursor: pointer;
  transition: all 0.2s;
  font-family: inherit;
}
.period-tab--active {
  background: white;
  color: #0061FF;
  box-shadow: 0 1px 4px rgba(0,0,0,0.08);
}

.bar-chart {
  display: flex;
  gap: 12px;
  height: 190px;
  margin-bottom: 16px;
}

.bar-chart__y-axis {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  font-size: 11px;
  color: #94A3B8;
  font-weight: 600;
  padding-bottom: 22px;
  text-align: right;
  min-width: 38px;
}

.bar-chart__body {
  flex: 1;
  position: relative;
}

.bar-chart__gridlines {
  position: absolute;
  top: 0; left: 0; right: 0;
  height: calc(100% - 22px);
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  pointer-events: none;
}
.bar-chart__gridlines > div {
  height: 1px;
  background: #F1F5F9;
}

.bar-chart__bars {
  display: flex;
  align-items: flex-end;
  justify-content: space-around;
  height: 100%;
  padding-bottom: 22px;
  position: relative;
  z-index: 1;
}

.bar-wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  flex: 1;
  height: 100%;
  justify-content: flex-end;
  position: relative;
}
.bar-wrap:hover .bar-tooltip { opacity: 1; transform: translateY(0); }

.bar-tooltip {
  position: absolute;
  top: -34px;
  background: #0F172A;
  color: white;
  font-size: 11px;
  font-weight: 700;
  padding: 4px 8px;
  border-radius: 6px;
  white-space: nowrap;
  opacity: 0;
  transform: translateY(4px);
  transition: all 0.2s;
  pointer-events: none;
  font-family: inherit;
}
.bar-tooltip::after {
  content: '';
  position: absolute;
  top: 100%; left: 50%;
  transform: translateX(-50%);
  border: 4px solid transparent;
  border-top-color: #0F172A;
}

.bar {
  width: 26px;
  border-radius: 7px 7px 0 0;
  background: linear-gradient(180deg, #93C5FD 0%, #3B82F6 100%);
  transition: opacity 0.2s;
}
.bar--highlight {
  background: linear-gradient(180deg, #60A5FA 0%, #0061FF 100%);
  box-shadow: 0 4px 12px rgba(0,97,255,0.3);
}
.bar-wrap:hover .bar { opacity: 0.8; }

.bar-label {
  font-size: 11px;
  color: #94A3B8;
  font-weight: 600;
}

.chart-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid #F1F5F9;
}
.chart-footer__legend {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: #64748B;
}
.legend-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  display: inline-block;
}
.chart-total {
  display: block;
  font-size: 20px;
  font-weight: 800;
  color: #0F172A;
  letter-spacing: -0.5px;
}
.chart-total-label {
  font-size: 11px;
  color: #94A3B8;
  display: block;
  text-align: right;
}

.donut-wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}
.donut-svg { width: 160px; height: auto; }

.donut-center-big {
  font-family: 'Plus Jakarta Sans', sans-serif;
  font-size: 20px;
  font-weight: 800;
  fill: #0F172A;
}
.donut-center-sm {
  font-size: 8px;
  fill: #94A3B8;
  font-weight: 600;
}

.donut-legend { width: 100%; }
.donut-item {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
}
.donut-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  flex-shrink: 0;
}
.donut-name {
  flex: 1;
  font-size: 13px;
  color: #475569;
  font-weight: 500;
}
.donut-pct {
  font-size: 13px;
  font-weight: 800;
  color: #0F172A;
}

.bottom-row {
  display: grid;
  grid-template-columns: 1fr 360px 260px;
  gap: 20px;
  margin-bottom: 20px;
}

.orders-table__head {
  display: grid;
  grid-template-columns: 65px 1fr 1fr 90px 110px;
  gap: 8px;
  padding: 10px 12px;
  background: #F8FAFC;
  border-radius: 10px;
  font-size: 11px;
  font-weight: 700;
  color: #94A3B8;
  text-transform: uppercase;
  letter-spacing: 0.4px;
  margin-bottom: 4px;
}

.orders-table__row {
  display: grid;
  grid-template-columns: 65px 1fr 1fr 90px 110px;
  gap: 8px;
  align-items: center;
  padding: 12px;
  border-bottom: 1px solid #F8FAFC;
  transition: background 0.15s;
  border-radius: 10px;
}
.orders-table__row:hover { background: #F8FAFC; }
.orders-table__row:last-child { border-bottom: none; }

.order-id {
  font-size: 13px;
  font-weight: 700;
  color: #0061FF;
}
.order-client {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: #1E293B;
  font-weight: 600;
}
.order-avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  font-weight: 800;
  flex-shrink: 0;
}
.order-product { font-size: 13px; color: #475569; }
.order-value { font-size: 13px; font-weight: 700; color: #0F172A; }

.status-pill {
  display: inline-flex;
  align-items: center;
  padding: 4px 10px;
  border-radius: 100px;
  font-size: 11px;
  font-weight: 700;
}
.status-pill--approved   { background: #DCFCE7; color: #15803D; }
.status-pill--shipped    { background: #DBEAFE; color: #1D4ED8; }
.status-pill--processing { background: #EDE9FE; color: #6D28D9; }
.status-pill--pending    { background: #FEF3C7; color: #92400E; }
.status-pill--delivered  { background: #D1FAE5; color: #065F46; }

.top-list { display: flex; flex-direction: column; gap: 10px; }

.top-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px;
  border-radius: 12px;
  transition: background 0.15s;
}
.top-item:hover { background: #F8FAFC; }

.top-rank {
  width: 24px;
  height: 24px;
  border-radius: 8px;
  background: #F1F5F9;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  font-weight: 800;
  color: #64748B;
  flex-shrink: 0;
}
.rank-1 { background: #FEF3C7; color: #D97706; }
.rank-2 { background: #F1F5F9; color: #6B7280; }
.rank-3 { background: #FEF0E7; color: #C2693E; }

.top-thumb {
  width: 38px;
  height: 38px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  flex-shrink: 0;
}

.top-info { flex: 1; min-width: 0; }
.top-name {
  font-size: 12px;
  font-weight: 700;
  color: #1E293B;
  margin: 0 0 2px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.top-cat { font-size: 11px; color: #94A3B8; margin: 0; }

.top-bar-wrap { width: 70px; }
.top-units { font-size: 11px; color: #64748B; font-weight: 600; margin: 0 0 3px; }
.top-bar {
  height: 4px;
  background: #F1F5F9;
  border-radius: 10px;
  overflow: hidden;
}
.top-bar__fill {
  height: 100%;
  background: linear-gradient(90deg, #0061FF, #60A5FA);
  border-radius: 10px;
}

.top-revenue {
  font-size: 11px;
  font-weight: 800;
  color: #0F172A;
  white-space: nowrap;
}

.alert-count {
  background: #FEE2E2;
  color: #DC2626;
  font-size: 12px;
  font-weight: 800;
  padding: 4px 10px;
  border-radius: 100px;
}

.alerts-list { display: flex; flex-direction: column; gap: 8px; }

.alert-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 12px;
  border-radius: 12px;
  border: 1px solid transparent;
}
.alert-item--critical { background: #FFF5F5; border-color: #FECACA; }
.alert-item--warning  { background: #FFFBEB; border-color: #FDE68A; }

.alert-info { flex: 1; min-width: 0; }
.alert-name {
  font-size: 12px;
  font-weight: 700;
  color: #1E293B;
  margin: 0 0 2px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.alert-detail {
  font-size: 11px;
  color: #94A3B8;
  margin: 0;
}
.alert-detail strong { color: #EF4444; }

.repor-btn {
  font-size: 11px;
  font-weight: 700;
  color: #0061FF;
  background: #EFF6FF;
  border: none;
  padding: 4px 10px;
  border-radius: 8px;
  cursor: pointer;
  white-space: nowrap;
  font-family: inherit;
  transition: background 0.2s;
}
.repor-btn:hover { background: #DBEAFE; }

.mini-stats-bar {
  background: white;
  border-radius: 20px;
  padding: 18px 32px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
}

.mini-stat {
  display: flex;
  align-items: center;
  gap: 12px;
}
.mini-stat + .mini-stat {
  padding-left: 24px;
  border-left: 1px solid #F1F5F9;
}
.mini-stat__icon { font-size: 26px; }
.mini-stat__value {
  font-size: 18px;
  font-weight: 800;
  color: #0F172A;
  margin: 0;
  letter-spacing: -0.5px;
}
.mini-stat__label {
  font-size: 12px;
  color: #94A3B8;
  margin: 0;
}

@media (max-width: 1400px) {
  .kpi-row { grid-template-columns: repeat(3, 1fr); }
  .bottom-row { grid-template-columns: 1fr 1fr; }
  .alerts-card { grid-column: span 2; }
}

@media (max-width: 1100px) {
  .dashboard-page { padding: 20px; }
  .mid-row { grid-template-columns: 1fr; }
  .kpi-row { grid-template-columns: repeat(2, 1fr); }
  .bottom-row { grid-template-columns: 1fr; }
  .alerts-card { grid-column: span 1; }
  .mini-stats-bar { flex-wrap: wrap; gap: 20px; justify-content: flex-start; }
  .mini-stat + .mini-stat { border-left: none; padding-left: 0; }
}
</style>
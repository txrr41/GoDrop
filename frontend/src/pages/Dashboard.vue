<template>
  <div class="dashboard-page">

    <!-- ══ PAGE TITLE ══ -->
    <div class="page-title-row">
      <div class="page-title-left">
        <p class="page-breadcrumb">Dashboard / <span>Visão Geral</span></p>
        <h1 class="page-title">Painel de Operações</h1>
      </div>
      <div class="page-title-right">
        <div class="date-range-btn">
          <v-icon size="14">mdi-calendar-range</v-icon>
          <span>Fev 1 — Fev 24, 2026</span>
          <v-icon size="14">mdi-chevron-down</v-icon>
        </div>
        <button class="export-btn">
          <v-icon size="14">mdi-arrow-up-circle-outline</v-icon>
          Exportar
        </button>
      </div>
    </div>

    <!-- ══ KPI STRIP ══ -->
    <div class="kpi-strip">

      <div class="kpi-tile">
        <div class="kpi-tile-head">
          <span class="kpi-label">Receita do Mês</span>
          <span class="kpi-delta positive">▲ 23%</span>
        </div>
        <p class="kpi-value">R$ {{ store.monthRevenue }}</p>
        <div class="kpi-bar-track"><div class="kpi-bar-fill" style="width:74%;background:#059669"></div></div>
        <p class="kpi-sub">Meta: R$ 65.000 · 74%</p>
      </div>

      <div class="kpi-divider"></div>

      <div class="kpi-tile">
        <div class="kpi-tile-head">
          <span class="kpi-label">Total de Pedidos</span>
          <span class="kpi-delta positive">▲ 8%</span>
        </div>
        <p class="kpi-value">{{ store.totalOrders }}</p>
        <div class="kpi-bar-track"><div class="kpi-bar-fill" style="width:51%;background:#6366F1"></div></div>
        <p class="kpi-sub">Meta diária: 250</p>
      </div> 

      <div class="kpi-divider"></div>

      <div class="kpi-tile">
        <div class="kpi-tile-head">
          <span class="kpi-label">Produtos Ativos</span>
          <span class="kpi-delta neutral">→ 0%</span>
        </div>
        <p class="kpi-value">{{ store.totalProducts}}</p>
        <div class="kpi-bar-track"><div class="kpi-bar-fill" style="width:88%;background:#0EA5E9"></div></div>
        <p class="kpi-sub">88% do catálogo</p>
      </div>

      <div class="kpi-divider"></div>

      <div class="kpi-tile">
        <div class="kpi-tile-head">
          <span class="kpi-label">Estoque Crítico</span>
          <span class="kpi-delta negative">▼ 5%</span>
        </div>
        <p class="kpi-value danger">{{store.lowStockCount}}</p>
        <div class="kpi-bar-track"><div class="kpi-bar-fill" style="width:34%;background:#EF4444"></div></div>
        <p class="kpi-sub">Abaixo de 10 un.</p>
      </div>

      <div class="kpi-divider"></div>

      <div class="kpi-tile">
        <div class="kpi-tile-head">
          <span class="kpi-label">Novos Clientes</span>
          <span class="kpi-delta positive">▲ 12%</span>
        </div>
        <p class="kpi-value">{{ store.newCustomers }}</p>
        <div class="kpi-bar-track"><div class="kpi-bar-fill" style="width:63%;background:#059669"></div></div>
        <p class="kpi-sub">Este mês</p>
      </div>

      <div class="kpi-divider"></div>

      <div class="kpi-tile">
        <div class="kpi-tile-head">
          <span class="kpi-label">Ticket Médio</span>
          <span class="kpi-delta positive">▲ 4%</span>
        </div>
        <p class="kpi-value">R$ {{ store.averageTicket.toFixed(2) }}</p>
        <div class="kpi-bar-track"><div class="kpi-bar-fill" style="width:61%;background:#F59E0B"></div></div>
        <p class="kpi-sub">vs R$ 366 anterior</p>
      </div>

    </div>

    <!-- ══ MID ROW ══ -->
    <div class="mid-row">

      <!-- Revenue Chart -->
      <div class="panel chart-panel">
        <div class="panel-header">
          <div>
            <p class="panel-label">Receita Bruta</p>
            <h2 class="panel-title">R$ 59.400 <span class="panel-period">últimos 7 dias</span></h2>
          </div>
          <div class="period-group">
            <button class="period-pill active">7D</button>
            <button class="period-pill">30D</button>
            <button class="period-pill">90D</button>
          </div>
        </div>

        <div class="chart-area">
          <div class="chart-body">
            <div class="chart-grid">
              <div class="grid-line"><span class="grid-label">R$12k</span></div>
              <div class="grid-line"><span class="grid-label">R$9k</span></div>
              <div class="grid-line"><span class="grid-label">R$6k</span></div>
              <div class="grid-line"><span class="grid-label">R$3k</span></div>
            </div>
            <div class="bars-row">
              <div class="bar-col" v-for="(bar, i) in chartData" :key="i">
                <div class="bar-tooltip-text">{{ bar.value }}</div>
                <div class="bar-fill" :class="{ 'bar-highlight': bar.highlight }" :style="{ height: bar.pct + '%' }"></div>
                <span class="bar-day">{{ bar.day }}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="chart-footer-row">
          <div class="chart-legend">
            <span class="legend-swatch" style="background:#059669"></span>
            Receita bruta confirmada
          </div>
          <div class="chart-total-info">
            <span class="chart-total-val">R$ 59.400</span>
            <span class="chart-total-label">Total do período</span>
          </div>
        </div>
      </div>

      <!-- Right column -->
      <div class="panel-col-right">

        <div class="panel donut-panel">
          <div class="panel-header">
            <div>
              <p class="panel-label">Distribuição</p>
              <h2 class="panel-title">Por Categoria</h2>
            </div>
          </div>
          <div class="donut-layout">
            <svg viewBox="0 0 120 120" class="donut-svg">
              <circle cx="60" cy="60" r="48" fill="none" stroke="#F1F5F9" stroke-width="16"/>
              <circle cx="60" cy="60" r="48" fill="none" stroke="#059669" stroke-width="16"
                      stroke-dasharray="115 187" stroke-dashoffset="0" stroke-linecap="butt"/>
              <circle cx="60" cy="60" r="48" fill="none" stroke="#6366F1" stroke-width="16"
                      stroke-dasharray="72 230" stroke-dashoffset="-117" stroke-linecap="butt"/>
              <circle cx="60" cy="60" r="48" fill="none" stroke="#0EA5E9" stroke-width="16"
                      stroke-dasharray="55 247" stroke-dashoffset="-191" stroke-linecap="butt"/>
              <circle cx="60" cy="60" r="48" fill="none" stroke="#F59E0B" stroke-width="16"
                      stroke-dasharray="60 242" stroke-dashoffset="-248" stroke-linecap="butt"/>
              <text x="60" y="56" text-anchor="middle" class="donut-pct-txt">38%</text>
              <text x="60" y="67" text-anchor="middle" class="donut-lbl-txt">Eletrôn.</text>
            </svg>
            <div class="donut-legend-col">
              <div class="dl-row" v-for="cat in donutCategories" :key="cat.name">
                <span class="dl-dot" :style="{background: cat.color}"></span>
                <span class="dl-name">{{ cat.name }}</span>
                <span class="dl-pct">{{ cat.pct }}%</span>
              </div>
            </div>
          </div>
        </div>

        <div class="panel mini-perf-panel">
          <div class="mini-perf-grid">
            <div class="perf-item" v-for="stat in miniStats" :key="stat.label">
              <span class="perf-icon">{{ stat.icon }}</span>
              <div class="perf-data">
                <p class="perf-val">{{ stat.value }}</p>
                <p class="perf-lbl">{{ stat.label }}</p>
              </div>
            </div>
          </div>
        </div>

      </div>
    </div>

    <!-- ══ BOTTOM ROW ══ -->
    <div class="bottom-row">

      <div class="panel orders-panel">
        <div class="panel-header">
          <div>
            <p class="panel-label">Últimas 24 horas</p>
            <h2 class="panel-title">Pedidos Recentes</h2>
          </div>
          <button class="view-all-btn">Ver todos <v-icon size="13">mdi-arrow-right</v-icon></button>
        </div>
        <div class="orders-table">
          <div class="ot-head">
            <span>ID</span>
            <span>Cliente</span>
            <span>Produto</span>
            <span>Valor</span>
            <span>Status</span>
          </div>
          <div class="ot-row" v-for="order in store.lastDayOrdersMapped" :key="order.id">
            <span class="ot-id">#{{ order.id }}</span>
            <div class="ot-client">
              <div class="ot-avatar" :style="{background: order.bg, color: order.tc}">{{ order.initials }}</div>
              <span>{{ order.name }}</span>
            </div>
            <span class="ot-product">{{ order.product }}</span>
            <span class="ot-value">{{ order.value }}</span>
            <div :class="['status-tag', 'status-' + order.statusKey]">{{ order.status }}</div>
          </div>
        </div>
      </div>

      <div class="panel top-panel">
        <div class="panel-header">
          <div>
            <p class="panel-label">Este mês</p>
            <h2 class="panel-title">Mais Vendidos</h2>
          </div>
          <button class="view-all-btn">Catálogo <v-icon size="13">mdi-arrow-right</v-icon></button>
        </div>
        <div class="top-list">
          <div class="top-item" v-for="(store, i) in store.bestProducts" :key="i">
            <div class="top-rank" :class="'rank-' + (i+1)">{{ i+1 }}</div>
            <div class="top-thumb" :style="{background: store.bg}">{{ store.emoji }}</div>
            <div class="top-info">
              <p class="top-name">{{ store.name }}</p>
              <p class="top-cat">{{ store.category }}</p>
            </div>
            <div class="top-right">
              <p class="top-units">{{ store.units }} un.</p>
              <div class="top-bar-track">
                <div class="top-bar-fill" :style="{width: store.pct + '%'}"></div>
              </div>
              <p class="top-revenue">{{ store.revenue }}</p>
            </div>
          </div>
        </div>
      </div>

      <div class="panel alerts-panel">
        <div class="panel-header">
          <div>
            <p class="panel-label">Reposição urgente</p>
            <h2 class="panel-title">Alertas</h2>
          </div>
          <div class="alert-count-badge">{{store.lowStockCount}}</div>
        </div>
        <div class="alerts-list">
          <div class="alert-row alert-critical" v-for="product in store.lowStockProducts" :key="product.id">
            <div class="alert-left">
              <span class="alert-dot"></span>
              <div class="alert-info">
                <p class="alert-name">{{ product.nome }}</p>
                <p class="alert-meta">{{ product.estoque }} un · {{ product.categoria }}</p>
              </div>
            </div>
            <button class="repor-pill">Repor</button>
          </div>
        </div>
      </div>

    </div>

  </div>
</template>

<script setup>
import { useDashboardStore} from "../stores/dashboard.js";
import { onMounted, ref } from "vue";

const store = useDashboardStore()

onMounted(() => {
  store.fetchStats()
})

// Dados temporários para o gráfico não quebrar
const chartData = ref([
  { day: 'Seg', value: 'R$8.2k', pct: 68, highlight: false },
  { day: 'Ter', value: 'R$9.1k', pct: 76, highlight: false },
  { day: 'Qua', value: 'R$7.4k', pct: 62, highlight: false },
  { day: 'Qui', value: 'R$11k',  pct: 92, highlight: true  },
  { day: 'Sex', value: 'R$9.8k', pct: 82, highlight: false },
  { day: 'Sáb', value: 'R$6.1k', pct: 51, highlight: false },
  { day: 'Dom', value: 'R$7.9k', pct: 66, highlight: false },
])

const donutCategories = ref([
  { name: 'Eletrônicos', pct: 38, color: '#059669' },
  { name: 'Moda',        pct: 24, color: '#6366F1' },
  { name: 'Casa',        pct: 18, color: '#0EA5E9' },
  { name: 'Outros',      pct: 20, color: '#F59E0B' },
])

const miniStats = ref([
  { icon: '📦', value: '94%',    label: 'Entrega no prazo' },
  { icon: '↩️', value: '2.1%',   label: 'Taxa devolução'   },
  { icon: '⭐', value: '4.8',    label: 'Avaliação média'  },
  { icon: '⚡', value: '1.2d',   label: 'Tempo preparo'    },
])

const recentOrders = ref([])
const topProducts   = ref([])

</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=IBM+Plex+Mono:wght@400;500;600&family=DM+Sans:wght@300;400;500;600;700&display=swap');

.dashboard-page {
  font-family: 'DM Sans', sans-serif;
  background: #F8FAFC;
  min-height: 100vh;
  padding: 28px 32px 40px;
  color: #0F172A;
}

/* PAGE TITLE */
.page-title-row {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 24px;
}

.page-breadcrumb {
  font-size: 12px;
  color: #94A3B8;
  font-family: 'IBM Plex Mono', monospace;
  margin: 0 0 5px;
}
.page-breadcrumb span { color: #64748B; }

.page-title {
  font-size: 22px;
  font-weight: 700;
  color: #0F172A;
  margin: 0;
  letter-spacing: -0.025em;
}

.page-title-right {
  display: flex;
  align-items: center;
  gap: 10px;
}

.date-range-btn {
  display: flex;
  align-items: center;
  gap: 7px;
  background: #FFFFFF;
  border: 1px solid #E2E8F0;
  border-radius: 8px;
  padding: 7px 14px;
  font-family: 'IBM Plex Mono', monospace;
  font-size: 12px;
  color: #64748B;
  cursor: pointer;
  transition: all 0.15s;
}
.date-range-btn:hover { border-color: #CBD5E1; color: #0F172A; }

.export-btn {
  display: flex;
  align-items: center;
  gap: 7px;
  background: #0F172A;
  color: #FFFFFF;
  border: none;
  border-radius: 8px;
  padding: 8px 16px;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  font-family: 'DM Sans', sans-serif;
  transition: all 0.15s;
}
.export-btn:hover { background: #1E293B; }

/* KPI STRIP */
.kpi-strip {
  display: flex;
  align-items: stretch;
  background: #FFFFFF;
  border: 1px solid #E2E8F0;
  border-radius: 14px;
  overflow: hidden;
  margin-bottom: 20px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
}

.kpi-tile {
  flex: 1;
  padding: 20px 22px;
  min-width: 0;
  transition: background 0.15s;
}
.kpi-tile:hover { background: #F8FAFC; }

.kpi-divider {
  width: 1px;
  background: #F1F5F9;
  margin: 14px 0;
  flex-shrink: 0;
}

.kpi-tile-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.kpi-label {
  font-size: 11px;
  font-weight: 600;
  color: #94A3B8;
  text-transform: uppercase;
  letter-spacing: 0.06em;
}

.kpi-delta {
  font-size: 10px;
  font-weight: 700;
  font-family: 'IBM Plex Mono', monospace;
  padding: 2px 7px;
  border-radius: 5px;
}
.kpi-delta.positive { background: #ECFDF5; color: #059669; }
.kpi-delta.negative { background: #FEF2F2; color: #EF4444; }
.kpi-delta.neutral  { background: #F1F5F9; color: #64748B; }

.kpi-value {
  font-size: 24px;
  font-weight: 700;
  color: #0F172A;
  margin: 0 0 10px;
  letter-spacing: -0.03em;
  font-family: 'IBM Plex Mono', monospace;
}
.kpi-value.danger { color: #EF4444; }

.kpi-bar-track {
  height: 3px;
  background: #F1F5F9;
  border-radius: 10px;
  overflow: hidden;
  margin-bottom: 8px;
}
.kpi-bar-fill { height: 100%; border-radius: 10px; }

.kpi-sub {
  font-size: 11px;
  color: #CBD5E1;
  margin: 0;
  font-family: 'IBM Plex Mono', monospace;
}

/* PANELS */
.panel {
  background: #FFFFFF;
  border: 1px solid #E2E8F0;
  border-radius: 14px;
  padding: 22px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.03);
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
}

.panel-label {
  font-size: 11px;
  font-weight: 600;
  color: #94A3B8;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  margin: 0 0 4px;
}

.panel-title {
  font-size: 17px;
  font-weight: 700;
  color: #0F172A;
  margin: 0;
  letter-spacing: -0.02em;
}

.panel-period {
  font-size: 13px;
  font-weight: 400;
  color: #94A3B8;
}

.view-all-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  font-weight: 600;
  color: #6366F1;
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
  font-family: 'DM Sans', sans-serif;
  transition: opacity 0.15s;
}
.view-all-btn:hover { opacity: 0.7; }

/* MID ROW */
.mid-row {
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: 16px;
  margin-bottom: 16px;
}

.period-group {
  display: flex;
  gap: 2px;
  background: #F8FAFC;
  border: 1px solid #E2E8F0;
  border-radius: 8px;
  padding: 3px;
}

.period-pill {
  padding: 4px 12px;
  border: none;
  background: transparent;
  border-radius: 6px;
  font-size: 11px;
  font-weight: 600;
  color: #94A3B8;
  cursor: pointer;
  font-family: 'IBM Plex Mono', monospace;
  transition: all 0.15s;
}
.period-pill.active {
  background: #FFFFFF;
  color: #0F172A;
  box-shadow: 0 1px 3px rgba(0,0,0,0.08);
}

.chart-panel { display: flex; flex-direction: column; }

.chart-area { flex: 1; margin-bottom: 16px; }

.chart-body {
  position: relative;
  height: 180px;
}

.chart-grid {
  position: absolute;
  top: 0; left: 0; right: 0;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  pointer-events: none;
}

.grid-line {
  position: relative;
  border-top: 1px solid #F1F5F9;
}

.grid-label {
  position: absolute;
  left: -44px;
  top: -8px;
  font-size: 10px;
  color: #CBD5E1;
  font-family: 'IBM Plex Mono', monospace;
}

.bars-row {
  display: flex;
  align-items: flex-end;
  justify-content: space-around;
  height: 100%;
  padding-left: 40px;
  position: relative;
  z-index: 1;
}

.bar-col {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  flex: 1;
  height: 100%;
  justify-content: flex-end;
  position: relative;
}

.bar-col:hover .bar-tooltip-text { opacity: 1; transform: translateY(0); }

.bar-tooltip-text {
  position: absolute;
  top: -32px;
  background: #0F172A;
  color: #F8FAFC;
  font-size: 10px;
  font-weight: 600;
  font-family: 'IBM Plex Mono', monospace;
  padding: 4px 8px;
  border-radius: 5px;
  white-space: nowrap;
  opacity: 0;
  transform: translateY(4px);
  transition: all 0.15s;
  pointer-events: none;
}

.bar-fill {
  width: 26px;
  background: #E2E8F0;
  border-radius: 5px 5px 0 0;
  transition: all 0.2s;
}
.bar-col:hover .bar-fill { background: #CBD5E1; }

.bar-highlight { background: #059669 !important; }
.bar-col:hover .bar-highlight { background: #047857 !important; }

.bar-day {
  font-size: 10px;
  color: #CBD5E1;
  font-family: 'IBM Plex Mono', monospace;
}

.chart-footer-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 14px;
  border-top: 1px solid #F1F5F9;
}

.chart-legend {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
  color: #94A3B8;
}

.legend-swatch {
  width: 12px;
  height: 3px;
  border-radius: 10px;
  display: inline-block;
}

.chart-total-info { text-align: right; }

.chart-total-val {
  display: block;
  font-size: 18px;
  font-weight: 700;
  color: #0F172A;
  font-family: 'IBM Plex Mono', monospace;
  letter-spacing: -0.03em;
}

.chart-total-label { font-size: 11px; color: #CBD5E1; }

/* Right col */
.panel-col-right {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* Donut */
.donut-layout {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 14px;
}

.donut-svg { width: 130px; height: 130px; }

.donut-pct-txt {
  font-size: 17px;
  font-weight: 700;
  fill: #0F172A;
  font-family: 'IBM Plex Mono', monospace;
}

.donut-lbl-txt { font-size: 8px; fill: #94A3B8; }

.donut-legend-col { width: 100%; }

.dl-row {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 5px 0;
  border-bottom: 1px solid #F8FAFC;
}
.dl-row:last-child { border-bottom: none; }

.dl-dot { width: 8px; height: 8px; border-radius: 2px; flex-shrink: 0; }
.dl-name { flex: 1; font-size: 12px; color: #64748B; }
.dl-pct { font-size: 12px; font-weight: 700; color: #0F172A; font-family: 'IBM Plex Mono', monospace; }

/* Mini perf */
.mini-perf-panel { padding: 16px 20px; }

.mini-perf-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px 14px;
}

.perf-item { display: flex; align-items: center; gap: 10px; }
.perf-icon { font-size: 18px; }

.perf-val {
  font-size: 15px;
  font-weight: 700;
  color: #0F172A;
  margin: 0 0 1px;
  font-family: 'IBM Plex Mono', monospace;
  letter-spacing: -0.02em;
}

.perf-lbl {
  font-size: 10px;
  color: #94A3B8;
  margin: 0;
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

/* BOTTOM ROW */
.bottom-row {
  display: grid;
  grid-template-columns: 1fr 280px 220px;
  gap: 16px;
}

/* Orders */
.ot-head {
  display: grid;
  grid-template-columns: 60px 1fr 1fr 90px 100px;
  gap: 8px;
  padding: 8px 10px;
  background: #F8FAFC;
  border-radius: 8px;
  font-size: 10px;
  font-weight: 700;
  color: #94A3B8;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  margin-bottom: 4px;
}

.ot-row {
  display: grid;
  grid-template-columns: 60px 1fr 1fr 90px 100px;
  gap: 8px;
  align-items: center;
  padding: 10px;
  border-radius: 8px;
  transition: background 0.15s;
}
.ot-row:hover { background: #F8FAFC; }

.ot-id { font-size: 12px; font-weight: 700; color: #6366F1; font-family: 'IBM Plex Mono', monospace; }

.ot-client {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: #334155;
  font-weight: 500;
}

.ot-avatar {
  width: 28px;
  height: 28px;
  border-radius: 7px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  font-weight: 800;
  flex-shrink: 0;
}

.ot-product { font-size: 12px; color: #94A3B8; overflow: hidden; white-space: nowrap; text-overflow: ellipsis; }
.ot-value { font-size: 13px; font-weight: 700; color: #0F172A; font-family: 'IBM Plex Mono', monospace; }

.status-tag {
  display: inline-flex;
  align-items: center;
  padding: 3px 9px;
  border-radius: 5px;
  font-size: 11px;
  font-weight: 700;
  width: fit-content;
}

.status-approved   { background: #ECFDF5; color: #059669; }
.status-shipped    { background: #EFF6FF; color: #2563EB; }
.status-processing { background: #F5F3FF; color: #7C3AED; }
.status-pending    { background: #FFFBEB; color: #D97706; }
.status-delivered  { background: #ECFDF5; color: #047857; }

/* Top Products */
.top-list { display: flex; flex-direction: column; gap: 4px; }

.top-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 6px;
  border-radius: 8px;
  transition: background 0.15s;
}
.top-item:hover { background: #F8FAFC; }

.top-rank {
  width: 22px;
  height: 22px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  font-weight: 800;
  font-family: 'IBM Plex Mono', monospace;
  background: #F1F5F9;
  color: #94A3B8;
  flex-shrink: 0;
}
.rank-1 { background: #FFFBEB; color: #D97706; }
.rank-2 { background: #F8FAFC; color: #64748B; }

.top-thumb { width: 32px; height: 32px; border-radius: 8px; display: flex; align-items: center; justify-content: center; font-size: 15px; flex-shrink: 0; }

.top-info { flex: 1; min-width: 0; }
.top-name { font-size: 12px; font-weight: 600; color: #334155; margin: 0 0 2px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.top-cat { font-size: 10px; color: #94A3B8; margin: 0; }

.top-right { text-align: right; min-width: 65px; }
.top-units { font-size: 10px; color: #94A3B8; margin: 0 0 3px; font-family: 'IBM Plex Mono', monospace; }
.top-bar-track { height: 3px; background: #F1F5F9; border-radius: 10px; overflow: hidden; margin-bottom: 3px; }
.top-bar-fill { height: 100%; background: #059669; border-radius: 10px; }
.top-revenue { font-size: 11px; font-weight: 700; color: #059669; font-family: 'IBM Plex Mono', monospace; }

/* Alerts */
.alert-count-badge {
  background: #FEF2F2;
  color: #EF4444;
  font-size: 12px;
  font-weight: 800;
  font-family: 'IBM Plex Mono', monospace;
  padding: 3px 10px;
  border-radius: 6px;
  border: 1px solid #FECACA;
}

.alerts-list { display: flex; flex-direction: column; gap: 6px; }

.alert-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
  padding: 10px 12px;
  border-radius: 9px;
  border: 1px solid transparent;
}
.alert-row.alert-critical { background: #FFF5F5; border-color: #FECACA; }
.alert-row.alert-warning  { background: #FFFBEB; border-color: #FDE68A; }

.alert-left { display: flex; align-items: center; gap: 8px; min-width: 0; }

.alert-dot { width: 7px; height: 7px; border-radius: 50%; flex-shrink: 0; }
.alert-critical .alert-dot { background: #EF4444; }
.alert-warning  .alert-dot { background: #F59E0B; }

.alert-info { min-width: 0; }
.alert-name { font-size: 12px; font-weight: 600; color: #334155; margin: 0 0 2px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.alert-meta { font-size: 10px; color: #94A3B8; margin: 0; font-family: 'IBM Plex Mono', monospace; }

.repor-pill {
  font-size: 11px;
  font-weight: 700;
  color: #0F172A;
  background: #F1F5F9;
  border: 1px solid #E2E8F0;
  padding: 3px 10px;
  border-radius: 6px;
  cursor: pointer;
  white-space: nowrap;
  font-family: 'DM Sans', sans-serif;
  flex-shrink: 0;
  transition: all 0.15s;
}
.repor-pill:hover { background: #E2E8F0; }

/* RESPONSIVE */
@media (max-width: 1300px) {
  .kpi-strip { flex-wrap: wrap; }
  .kpi-tile { min-width: 30%; }
  .kpi-divider { display: none; }
  .bottom-row { grid-template-columns: 1fr 1fr; }
  .alerts-panel { grid-column: span 2; }
}

@media (max-width: 1100px) {
  .mid-row { grid-template-columns: 1fr; }
  .panel-col-right { flex-direction: row; gap: 16px; }
  .donut-panel, .mini-perf-panel { flex: 1; }
}

@media (max-width: 768px) {
  .dashboard-page { padding: 16px; }
  .kpi-tile { min-width: 45%; }
  .bottom-row { grid-template-columns: 1fr; }
  .alerts-panel { grid-column: span 1; }
  .panel-col-right { flex-direction: column; }
  .page-title-row { flex-direction: column; align-items: flex-start; gap: 12px; }
}
</style>
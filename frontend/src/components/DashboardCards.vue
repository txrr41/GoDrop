<template>
  <div class="dashboard-cards">
    <div class="stat-card">
      <div class="stat-content">
        <div class="stat-info">
          <p class="stat-label">Total de Produtos</p>
          <p class="stat-value">{{ totalProducts }}</p>
          <div class="stat-trend trend-up">
            <v-icon size="12">mdi-trending-up</v-icon>
            <span>+12%</span>
            <span class="trend-period">vs mes anterior</span>
          </div>
        </div>
        <div class="stat-icon icon-primary">
          <v-icon>mdi-package-variant</v-icon>
        </div>
      </div>
      <div class="stat-decoration"></div>
    </div>

    <div class="stat-card">
      <div class="stat-content">
        <div class="stat-info">
          <p class="stat-label">Estoque Total</p>
          <p class="stat-value">{{ totalStock.toLocaleString('pt-BR') }}</p>
          <div class="stat-trend trend-up">
            <v-icon size="12">mdi-trending-up</v-icon>
            <span>+8%</span>
            <span class="trend-period">vs mes anterior</span>
          </div>
        </div>
        <div class="stat-icon icon-accent">
          <v-icon>mdi-cube-outline</v-icon>
        </div>
      </div>
      <div class="stat-decoration"></div>
    </div>

    <div class="stat-card">
      <div class="stat-content">
        <div class="stat-info">
          <p class="stat-label">Valor em Estoque</p>
          <p class="stat-value">{{ formatCurrency(totalValue) }}</p>
          <div class="stat-trend trend-up">
            <v-icon size="12">mdi-trending-up</v-icon>
            <span>+23%</span>
            <span class="trend-period">vs mes anterior</span>
          </div>
        </div>
        <div class="stat-icon icon-success">
          <v-icon>mdi-currency-usd</v-icon>
        </div>
      </div>
      <div class="stat-decoration"></div>
    </div>

    <div class="stat-card">
      <div class="stat-content">
        <div class="stat-info">
          <p class="stat-label">Estoque Baixo</p>
          <p class="stat-value">{{ lowStockCount }}</p>
          <div class="stat-trend trend-down">
            <v-icon size="12">mdi-trending-down</v-icon>
            <span>-5%</span>
            <span class="trend-period">vs mes anterior</span>
          </div>
        </div>
        <div class="stat-icon icon-danger">
          <v-icon>mdi-alert-outline</v-icon>
        </div>
      </div>
      <div class="stat-decoration"></div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DashboardCards',
  props: {
    totalProducts: { type: Number, default: 0 },
    totalStock: { type: Number, default: 0 },
    totalValue: { type: Number, default: 0 },
    lowStockCount: { type: Number, default: 0 }
  },
  methods: {
    formatCurrency(value) {
      return value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
    }
  }
}
</script>

<style scoped>
.dashboard-cards {
  display: grid;
  gap: 16px;
  grid-template-columns: 1fr;
}

@media (min-width: 640px) {
  .dashboard-cards {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (min-width: 1024px) {
  .dashboard-cards {
    grid-template-columns: repeat(4, 1fr);
  }
}

.stat-card {
  background: #FFFFFF;
  border-radius: 16px;
  padding: 24px;
  position: relative;
  overflow: hidden;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  border: 1px solid #E3E8EF;
  transition: box-shadow 0.2s ease, transform 0.2s ease;
}

.stat-card:hover {
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -2px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.stat-content {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  position: relative;
  z-index: 1;
}

.stat-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.stat-label {
  font-size: 14px;
  font-weight: 500;
  color: #5E6D82;
  margin: 0;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #1A2332;
  margin: 0;
  line-height: 1.2;
}

.stat-trend {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
}

.stat-trend.trend-up {
  color: #4CAF50;
}

.stat-trend.trend-down {
  color: #F44336;
}

.trend-period {
  color: #9AA5B5;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-icon.icon-primary {
  background: rgba(21, 101, 192, 0.1);
  color: #1565C0;
}

.stat-icon.icon-accent {
  background: rgba(66, 165, 245, 0.1);
  color: #42A5F5;
}

.stat-icon.icon-success {
  background: rgba(76, 175, 80, 0.1);
  color: #4CAF50;
}

.stat-icon.icon-danger {
  background: rgba(244, 67, 54, 0.1);
  color: #F44336;
}

.stat-decoration {
  position: absolute;
  right: -16px;
  bottom: -16px;
  width: 96px;
  height: 96px;
  border-radius: 50%;
  background: rgba(21, 101, 192, 0.1);
  opacity: 0.5;
}
</style>

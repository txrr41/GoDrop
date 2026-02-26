import { defineStore } from 'pinia'
import api from '../api/api'

export const useDashboardStore = defineStore('dashboard', {

    state: () => ({
        stats: null,
        loading: false,
        error: null
    }),

    getters: {
        totalOrders:    (state) => state.stats?.totalOrders    ?? 0,
        totalRevenue:   (state) => state.stats?.totalRevenue   ?? 0,
        totalProducts:  (state) => state.stats?.totalProducts  ?? 0,
        totalCustomers: (state) => state.stats?.totalCustomers ?? 0,
        pendingOrders:  (state) => state.stats?.pendingOrders  ?? 0,
        shippedOrders:  (state) => state.stats?.shippedOrders  ?? 0,
        deliveredOrders:(state) => state.stats?.deliveredOrders ?? 0,
        lowStockProducts:(state) => state.stats?.lowStockProducts ?? [],
        lowStockCount: (state) => state.stats?.lowStockCount?? 0,
    },

    actions: {
        async fetchStats() {
            try {
                this.loading = true
                this.error = null

                const { data } = await api.get('/api/admin/dashboard/stats')
                console.log('✅ Dados recebidos:', data)  // ← adiciona isso
                this.stats = data
                console.log('✅ Store stats:', this.stats)

            } catch (error) {
                console.error('Erro ao buscar stats do dashboard:', error)
                this.error = 'Não foi possível carregar o dashboard'
            } finally {
                this.loading = false
            }
        }
    }
})
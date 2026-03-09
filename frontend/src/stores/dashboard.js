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
        newCustomers: (state) => state.stats?.newCustomers ?? 0,
        averageTicket: (state) => state.stats?.averageTicket ?? 0,
        monthRevenue: (state) => state.stats?.monthRevenue ?? 0,
        lastDayOrdersMapped: (state) => state.stats?.lastDayOrders ?? [],
        bestProducts: (state) => state.stats?.bestProducts ?? [],
        revenueChart: (state) => state.stats?.revenueChart ?? [],
        donutCategories: (state) => state.stats?.donutCategories ?? [],
        miniStats: (state) => state.stats?.miniStats ?? [],
        dateRange: (state) => state.stats?.dateRange ?? '',
    },

    actions: {
        async fetchStats() {
            try {
                this.loading = true
                this.error = null

                const { data } = await api.get('/dashboard/stats')
                console.log('✅ Dados recebidos:', data)  // ← adiciona isso
                this.stats = data
                console.log('✅ Store stats:', this.stats)

            } catch (error) {
                console.error('Erro ao buscar stats do dashboard:', error)
                this.error = 'Não foi possível carregar o dashboard'
            } finally {
                this.loading = false
            }
        },

        async generateCommissionReport (startDate, endDate) {
            const response = await api.get('/dashboard/relatorio/comissoes', {
                params: { startDate, endDate },
                responseType: 'blob'
            })
            const url = window.URL.createObjectURL(response.data)
            const link = document.createElement('a')
            link.href = url
            link.setAttribute('download', 'commission-report.pdf')
            document.body.appendChild(link)
            link.click()
            document.body.removeChild(link)
        },

        async generateTopProductsReport(startDate, endDate) {
            const response = await api.get('/dashboard/relatorio/top-produtos', {
                params: { startDate, endDate },
                responseType: 'blob'
            })
            const url = window.URL.createObjectURL(response.data)
            const link = document.createElement('a')
            link.href = url
            link.setAttribute('download', 'top-produtos.pdf')
            document.body.appendChild(link)
            link.click()
            document.body.removeChild(link)
        },

        async generateCriticalStockReport() {
            const response = await api.get('/dashboard/relatorio/estoque-critico', {
                responseType: 'blob'
            })
            const url = window.URL.createObjectURL(response.data)
            const link = document.createElement('a')
            link.href = url
            link.setAttribute('download', 'estoque-critico.pdf')
            document.body.appendChild(link)
            link.click()
            document.body.removeChild(link)
        }

    }
})
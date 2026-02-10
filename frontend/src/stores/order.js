
import { defineStore } from 'pinia'
import api from '../api/api'

export const useOrderStore = defineStore('order', {
    state: () => ({
        orders: [],
        loading: false,
        error: null
    }),

    getters: {
        // Pedidos pendentes de pagamento
        pendingOrders: (state) =>
            state.orders.filter(o => o.status === 'PENDING'),

        // Pedidos enviados
        shippedOrders: (state) =>
            state.orders.filter(o => o.status === 'SHIPPED'),

        // Pedidos entregues
        deliveredOrders: (state) =>
            state.orders.filter(o => o.status === 'DELIVERED'),

        // Total de pedidos
        totalOrders: (state) => state.orders.length
    },

    actions: {

        async fetchOrders() {
            try {
                this.loading = true
                this.error = null

                const { data } = await api.get('/api/orders')

                this.orders = data
                console.log('✅ Pedidos carregados:', data.length)

            } catch (error) {
                console.error('❌ Erro ao buscar pedidos:', error)
                this.error = 'Erro ao carregar pedidos'
                throw error
            } finally {
                this.loading = false
            }
        },


        async fetchOrderDetails(orderId) {
            try {
                const { data } = await api.get(`/api/orders/${orderId}`)
                console.log('✅ Detalhes do pedido:', data)
                return data
            } catch (error) {
                console.error('❌ Erro ao buscar pedido:', error)
                throw error
            }
        },


        clearOrders() {
            this.orders = []
            this.error = null
        }
    }
})
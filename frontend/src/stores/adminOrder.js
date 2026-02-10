
import { defineStore } from 'pinia'
import api from '../api/api'

export const useAdminOrderStore = defineStore('adminOrder', {
    state: () => ({
        orders: [],
        loading: false,
        error: null,
        stats: {
            total: 0,
            byStatus: {}
        }
    }),

    getters: {
        // Pedidos pendentes
        pendingOrders: (state) =>
            state.orders.filter(o => o.status === 'PENDING'),

        // Pedidos aprovados
        approvedOrders: (state) =>
            state.orders.filter(o => o.status === 'PAYMENT_APPROVED'),

        // Pedidos sendo preparados
        processingOrders: (state) =>
            state.orders.filter(o => o.status === 'PROCESSING'),

        // Pedidos despachados
        shippedOrders: (state) =>
            state.orders.filter(o => o.status === 'SHIPPED'),

        // Total de pedidos
        totalOrders: (state) => state.orders.length
    },

    actions: {

        async fetchAllOrders() {
            try {
                this.loading = true
                this.error = null

                const { data } = await api.get('/api/admin/orders')

                this.orders = data
                console.log('✅ [ADMIN] Pedidos carregados:', data.length)

            } catch (error) {
                console.error('❌ Erro ao buscar pedidos:', error)
                this.error = 'Erro ao carregar pedidos'
                throw error
            } finally {
                this.loading = false
            }
        },

        async updateOrderStatus(orderId, newStatus) {
            try {
                const { data } = await api.patch(
                    `/api/admin/orders/${orderId}/status`,
                    { status: newStatus }
                )

                console.log('✅ Status atualizado:', data)

                // Atualiza pedido na lista local
                const index = this.orders.findIndex(o => o.id === orderId)
                if (index !== -1) {
                    this.orders[index] = data.order
                }

                return data

            } catch (error) {
                console.error('❌ Erro ao atualizar status:', error)
                throw error
            }
        },

        async shipOrder(orderId, trackingCode) {
            try {
                const { data } = await api.post(
                    `/api/admin/orders/${orderId}/ship`,
                    { trackingCode }
                )

                console.log('✅ Pedido despachado:', data)

                const index = this.orders.findIndex(o => o.id === orderId)
                if (index !== -1) {
                    this.orders[index] = data.order
                }

                return data

            } catch (error) {
                console.error('❌ Erro ao despachar pedido:', error)
                throw error
            }
        },


        async fetchStats() {
            try {
                const { data } = await api.get('/api/admin/orders/stats')
                this.stats = data
                return data
            } catch (error) {
                console.error('❌ Erro ao buscar estatísticas:', error)
                throw error
            }
        }
    }
})
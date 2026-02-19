import { defineStore } from 'pinia'
import api from '../api/api'

export const useOfferStore = defineStore('offer', {
    state: () => ({
        offers: [],
        loading: false,
        error: null
    }),

    getters: {
        activeOffers: (state) =>
            state.offers.filter(o => o.status === 'ACTIVE' && o.active),

        scheduledOffers: (state) =>
            state.offers.filter(o => o.status === 'SCHEDULED'),

        expiredOffers: (state) =>
            state.offers.filter(o => o.status === 'EXPIRED')
    },

    actions: {
        async fetchOffers() {
            try {
                this.loading = true
                this.error = null
                const { data } = await api.get('/api/offers')
                this.offers = data
                console.log('✅ Ofertas carregadas:', data.length)
            } catch (error) {
                console.error('❌ Erro ao carregar ofertas:', error)
                this.error = 'Erro ao carregar ofertas'
                throw error
            } finally {
                this.loading = false
            }
        },

        async createOffer(offerData) {
            try {
                this.loading = true
                this.error = null
                const { data } = await api.post('/api/offers', offerData)
                this.offers.push(data)
                console.log('✅ Oferta criada:', data)
                return data
            } catch (error) {
                console.error('❌ Erro ao criar oferta:', error)
                this.error = 'Erro ao criar oferta'
                throw error
            } finally {
                this.loading = false
            }
        },

        async updateOffer(id, offerData) {
            try {
                this.loading = true
                this.error = null
                const { data } = await api.put(`/api/offers/${id}`, offerData)
                const index = this.offers.findIndex(o => o.id === id)
                if (index !== -1) {
                    this.offers[index] = data
                }
                console.log('✅ Oferta atualizada:', data)
                return data
            } catch (error) {
                console.error('❌ Erro ao atualizar oferta:', error)
                this.error = 'Erro ao atualizar oferta'
                throw error
            } finally {
                this.loading = false
            }
        },

        async deleteOffer(id) {
            try {
                this.loading = true
                this.error = null
                await api.delete(`/api/offers/${id}`)
                this.offers = this.offers.filter(o => o.id !== id)
                console.log('✅ Oferta deletada:', id)
            } catch (error) {
                console.error('❌ Erro ao deletar oferta:', error)
                this.error = 'Erro ao deletar oferta'
                throw error
            } finally {
                this.loading = false
            }
        },

        async toggleOffer(id) {
            try {
                const { data } = await api.patch(`/api/offers/${id}/toggle`)
                const index = this.offers.findIndex(o => o.id === id)
                if (index !== -1) {
                    this.offers[index] = data
                }
                console.log('✅ Oferta alternada:', data)
                return data
            } catch (error) {
                console.error('❌ Erro ao alternar oferta:', error)
                throw error
            }
        },

        async getProductFinalPrice(productId) {
            try {
                const { data } = await api.get(`/api/offers/product/${productId}/price`)
                return data
            } catch (error) {
                console.error('❌ Erro ao buscar preço:', error)
                throw error
            }
        },

        async getBestOfferForProduct(productId) {
            try {
                const { data } = await api.get(`/api/offers/product/${productId}/best-offer`)
                return data
            } catch (error) {
                console.error('❌ Erro ao buscar melhor oferta:', error)
                return null
            }
        }
    }
})
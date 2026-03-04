import { defineStore } from 'pinia'
import api from '../api/api'

export const useStoreStore = defineStore('store', {
    state: () => ({
        myStore: null,
        publicStore: null,
        loading: false,
        aiLoading: false,
        error: null,
    }),

    getters: {
        hasStore: ({ myStore }) => !!myStore,
        isActive: ({ myStore }) => myStore?.active === true,
        storeUrl: ({ myStore }) => myStore ? `/loja/${myStore.slug}` : null,
    },

    actions: {
        async generateAiTheme(prompt) {
            try {
                this.aiLoading = true
                this.error = null
                const { data } = await api.post('/api/dropper/store/ai-theme', { prompt })
                this.aiTheme = data
                console.log('🤖 Tema gerado pela IA:', data)
                return data
            } catch (error) {
                console.error('❌ Erro ao gerar tema:', error)
                this.error = 'Erro ao gerar tema com IA'
                throw error
            } finally {
                this.aiLoading = false
            }
        },

        applyAiTheme() {
            if (!this.aiTheme) return
            this.myStore = {
                ...this.myStore,
                storeName: this.aiTheme.storeName,
                slogan: this.aiTheme.slogan,
                primaryColor: this.aiTheme.primaryColor,
                secondaryColor: this.aiTheme.secondaryColor,
                backgroundColor: this.aiTheme.backgroundColor,
                theme: this.aiTheme.theme,
                description: this.aiTheme.description,
            }
        },

        async createStore(storeData) {
            try {
                this.loading = true
                const { data } = await api.post('/api/dropper/store', storeData)
                this.myStore = data
                console.log('✅ Loja criada:', data)
                return data
            } catch (error) {
                this.error = error.response?.data?.message || 'Erro ao criar loja'
                throw error
            } finally {
                this.loading = false
            }
        },

        async fetchMyStore() {
            try {
                this.loading = true
                const { data } = await api.get('/api/dropper/store/me')
                this.myStore = data
            } catch (error) {
                if (error.response?.status === 404 || error.response?.status === 500) {
                    this.myStore = null
                    return
                }
                throw error
            } finally {
                this.loading = false
            }
        },

        async updateStore(storeData) {
            try {
                this.loading = true
                const { data } = await api.put('/api/dropper/store', storeData)
                this.myStore = data
                return data
            } catch (error) {
                this.error = 'Erro ao atualizar loja'
                throw error
            } finally {
                this.loading = false
            }
        },

        async toggleStore() {
            try {
                const { data } = await api.patch('/api/dropper/store/toggle')
                this.myStore = data
                return data
            } catch (error) {
                this.error = 'Erro ao alterar status da loja'
                throw error
            }
        },

        async fetchPublicStore(slug) {
            try {
                this.loading = true
                const { data } = await api.get(`/loja/${slug}`)
                this.publicStore = data
                return data
            } catch (error) {
                this.publicStore = null
                throw error
            } finally {
                this.loading = false
            }
        },
    },
})
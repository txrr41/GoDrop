import { defineStore } from 'pinia'
import api from '../api/api'

export const useAuthStore = defineStore('auth', {
    state: () => ({
        user: null,
        loading: false,
    }),

    getters: {

        isLogged: (state) => !!state.user,
    },

    actions: {
        async login(email, password) {
            try {
                this.loading = true
                const response = await api.post('/auth/login', { email, password })
                this.user = response.data

                console.log('Login bem-sucedido:', this.user)
                return response.data
            } catch (error) {
                console.error('Erro no login:', error)
                this.user = null
                throw error
            } finally {
                this.loading = false
            }
        },

        async register(data) {
            try {
                this.loading = true
                const response = await api.post('/auth/register', data)
                this.user = response.data

                console.log('Registro bem-sucedido:', this.user)
                return response.data
            } catch (error) {
                console.error('Erro no registro:', error)
                this.user = null
                throw error
            } finally {
                this.loading = false
            }
        },

        async fetchUser() {
            try {
                const { data } = await api.get('/auth/me')
                this.user = data
                console.log('Usuário carregado:', this.user)
            } catch (error) {
                console.error('Erro ao buscar usuário:', error)
                this.user = null
            }
        },

        async logout() {
            try {
                await api.post('/auth/logout')
                this.user = null
                console.log('Logout realizado')
            } catch (error) {
                console.error('Erro no logout:', error)
                this.user = null
            }
        }
    }
})
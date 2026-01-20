import { defineStore } from 'pinia'
import api from '../api/api'

export const useAuthStore = defineStore('auth', {
    state: () => ({
        user: null,
        isLoggedIn: false,
        loading: false,
    }),

    getters: {
        isLogged: (state) => !!state.user,
    },

    actions: {
        async login(email, password) {
            this.loading = true
            await api.post('auth/login', { email, password })
            await this.fetchUser()
            this.loading = false
        },
        async register(data) {
            this.loading = true
            await api.post('auth/register', data)
            await this.fetchUser()
            this.loading = false
        },
        async fetchUser() {
            try {
                const { data } = await api.get('auth/me')
                this.user = null
                this.isLogged = false
            } catch  {
                this.user = null
                this.isLogged = false
            }
        },
        async logout() {
            await api.post('auth/logout')
            this.user = null
            this.isLogged = false
        }
    }
})
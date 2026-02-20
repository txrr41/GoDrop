import { defineStore } from 'pinia'
import { computed } from 'vue'
import api from '../api/api'

export const useAuthStore = defineStore('auth', {
    state: () => ({
        user: null,
        loading: false,
        initialized: false,
    }),

    getters: {
        isLogged: ({ user }) => !!user,
        isOwner:     ({ user }) => user?.role === 'OWNER',
        isStaff:     ({ user }) => user?.role === 'STAFF' || user?.role === 'OWNER',
        isCustomer:  ({ user }) => user?.role === 'CUSTOMER',
        isSuspended: ({ user }) => user?.suspended === true,

        permissions: ({ user }) => user?.permissions ?? [],

        can: ({ user }) => (permission) => {
            if (!user) return false
            if (user.role === 'OWNER') return true
            return (user.permissions ?? []).includes(permission)
        },

        menuItems({ user }) {
            if (!user || user.role === 'CUSTOMER') return []

            const isOwner = user.role === 'OWNER'
            const perms   = user.permissions ?? []
            const can     = (p) => isOwner || perms.includes(p)

            const all = [
                {
                    label: 'Dashboard',
                    icon: 'mdi-view-dashboard-outline',
                    route: '/home',
                    show: true,
                },
                {
                    label: 'Produtos',
                    icon: 'mdi-tag-outline',
                    route: '/produtos',
                    show: can('catalog'),
                },
                {
                    label: 'Pedidos',
                    icon: 'mdi-shopping-outline',
                    route: '/admin/pedidos',
                    show: can('orders'),
                },
                {
                    label: 'Clientes',
                    icon: 'mdi-account-group-outline',
                    route: '/clientes',
                    show: can('customers'),
                },
                {
                    label: 'Ofertas',
                    icon: 'mdi-tag-heart-outline',
                    route: '/ofertas',
                    show: can('marketing'),
                },
                {
                    label: 'Controle de Acessos',
                    icon: 'mdi-shield-key-outline',
                    route: '/usuarios',
                    show: isOwner,
                },
            ]

            return all.filter(item => item.show)
        },
    },

    actions: {
        async init() {
            if (this.initialized) return
            try {
                await this.fetchUser()
            } catch {
                // não autenticado — ok
            } finally {
                this.initialized = true
            }
        },

        async login(email, password) {
            try {
                this.loading = true
                const { data } = await api.post('/auth/login', { email, password })
                this.user = data
                return data
            } catch (error) {
                this.user = null
                throw error
            } finally {
                this.loading = false
            }
        },

        async register(data) {
            try {
                this.loading = true
                const res = await api.post('/auth/register', data)
                this.user = res.data
                return res.data
            } catch (error) {
                this.user = null
                throw error
            } finally {
                this.loading = false
            }
        },

        async fetchUser() {
            const { data } = await api.get('/auth/me')
            this.user = data
        },

        async logout() {
            try {
                await api.post('/auth/logout')
            } finally {
                this.user = null
            }
        },
    },
})
import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
    state: () => ({
        user: null,
    }),

    getters: {
        isLogged: (state) => !!state.user,
    },

    actions: {
        login(userDate) {
            this.user = userData
        },

        logout() {
            this.user = null
        }
    }
})
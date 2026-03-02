import { defineStore } from 'pinia'
import api from '../api/api'

export const useDropperStore = defineStore('dropper', {
    state: () => ({
        profile: null,
        loading: false,
    }),

    getters: {
        isDropper: ({ profile }) => profile?.status === 'ACTIVE'

    },

    actions: {
        async fetchProfile () {
            try {
                const { data } = await api.get('/api/dropper/me')
                this.profile = data
            }  catch (error) {
                if (error.response?.status === 401) {
                    this.profile = null
                    return
                }
                throw error
            }
        },
    },

})
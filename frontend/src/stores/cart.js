import { defineStore } from 'pinia'
import api from '../api/api'

export const useCartStore = defineStore('cart', {
    state: () => ({
        items: [],
        coupon: null,
        shippingCost: 15.00,
        dropperDiscount: 0,        // % de desconto do nível dropper (ex: 10)
        dropperLevel: null,        // nível atual (BRONZE, SILVER, etc.)
        loadingDiscount: false,
    }),

    getters: {
        totalItems: (state) => {
            return state.items.reduce((sum, item) => sum + item.quantity, 0)
        },

        subtotal: (state) => {
            return state.items.reduce((sum, item) => sum + (item.preco * item.quantity), 0)
        },

        couponDiscount: (state, getters) => {
            if (!state.coupon) return 0
            // usa subtotal via this dentro de getter com função
            const sub = state.items.reduce((sum, item) => sum + (item.preco * item.quantity), 0)
            return sub * (state.coupon.percentage / 100)
        },

        // Valor absoluto do desconto dropper
        dropperDiscountAmount() {
            if (!this.dropperDiscount || this.dropperDiscount === 0) return 0
            return this.subtotal * (this.dropperDiscount / 100)
        },

        // Subtotal já com desconto dropper aplicado
        subtotalWithDropperDiscount() {
            return this.subtotal - this.dropperDiscountAmount
        },

        shipping() {
            return this.subtotalWithDropperDiscount >= 199 ? 0 : this.shippingCost
        },

        total() {
            return this.subtotalWithDropperDiscount - this.couponDiscount + this.shipping
        },

        // Total em centavos para enviar ao backend (já com desconto aplicado)
        totalInCents() {
            return Math.round(this.total * 100)
        },

        cartItems: (state) => state.items,

        hasDropperDiscount: (state) => state.dropperDiscount > 0,
    },

    actions: {
        // Busca o desconto do dropper autenticado
        async fetchDropperDiscount() {
            try {
                this.loadingDiscount = true
                const { data } = await api.get('/api/dropper/me')
                if (data && data.status === 'ACTIVE') {
                    this.dropperDiscount = data.discountPercent || 0
                    this.dropperLevel = data.level || null
                } else {
                    this.dropperDiscount = 0
                    this.dropperLevel = null
                }
            } catch {
                // Usuário não é dropper ou não está autenticado — sem desconto
                this.dropperDiscount = 0
                this.dropperLevel = null
            } finally {
                this.loadingDiscount = false
            }
        },

        // Limpa desconto dropper (ex: ao fazer logout)
        clearDropperDiscount() {
            this.dropperDiscount = 0
            this.dropperLevel = null
        },

        loadCart() {
            const savedCart = localStorage.getItem('cartItems')
            if (savedCart) {
                this.items = JSON.parse(savedCart)
            }
        },

        saveCart() {
            localStorage.setItem('cartItems', JSON.stringify(this.items))
        },

        addItem(product) {
            const existingItem = this.items.find(item => item.id === product.id)

            if (existingItem) {
                if (existingItem.quantity < product.estoque) {
                    existingItem.quantity++
                } else {
                    throw new Error('Quantidade máxima em estoque atingida')
                }
            } else {
                this.items.push({
                    ...product,
                    quantity: 1
                })
            }

            this.saveCart()
        },

        updateQuantity(productId, change) {
            const item = this.items.find(i => i.id === productId)
            if (!item) return

            const newQuantity = item.quantity + change

            if (newQuantity <= 0) {
                this.removeItem(productId)
            } else if (newQuantity <= item.estoque) {
                item.quantity = newQuantity
                this.saveCart()
            } else {
                throw new Error('Quantidade máxima em estoque atingida')
            }
        },

        removeItem(productId) {
            this.items = this.items.filter(item => item.id !== productId)
            this.saveCart()
        },

        clearCart() {
            this.items = []
            this.coupon = null
            this.saveCart()
        }
    }
})
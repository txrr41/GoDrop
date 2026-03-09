import { defineStore } from 'pinia'
import api from '../api/api'

export const useCartStore = defineStore('cart', {
    state: () => ({
        items: [],
        coupon: null,
        shippingCost: 15.00,
        dropperDiscount: 0,
        dropperLevel: null,
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
            const sub = state.items.reduce((sum, item) => sum + (item.preco * item.quantity), 0)
            return sub * (state.coupon.percentage / 100)
        },

        dropperDiscountAmount() {
            if (!this.dropperDiscount || this.dropperDiscount === 0) return 0
            return this.subtotal * (this.dropperDiscount / 100)
        },


        subtotalWithDropperDiscount() {
            return this.subtotal - this.dropperDiscountAmount
        },

        shipping() {
            return this.subtotalWithDropperDiscount >= 199 ? 0 : this.shippingCost
        },

        total() {
            return this.subtotalWithDropperDiscount - this.couponDiscount + this.shipping
        },


        totalInCents() {
            return Math.round(this.total * 100)
        },

        cartItems: (state) => state.items,

        hasDropperDiscount: (state) => state.dropperDiscount > 0,
    },

    actions: {
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
                this.dropperDiscount = 0
                this.dropperLevel = null
            } finally {
                this.loadingDiscount = false
            }
        },

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
            const preco = product.offerPrice ?? product.preco

            if (existingItem) {
                if (existingItem.quantity < product.estoque) {
                    existingItem.quantity++
                } else {
                    throw new Error('Quantidade máxima em estoque atingida')
                }
            } else {
                this.items.push({
                    ...product,
                    preco,
                    precoOriginal: product.preco,
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
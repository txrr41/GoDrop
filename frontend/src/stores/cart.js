import { defineStore } from 'pinia'

export const useCartStore = defineStore('cart', {
    state: () => ({
        items: [],
        coupon: null,
        shippingCost: 15.00
    }),

    getters: {
        totalItems: (state) => {
            return state.items.reduce((sum, item) => sum + item.quantity, 0)
        },

        subtotal: (state) => {
            return state.items.reduce((sum, item) => sum + (item.preco * item.quantity), 0)
        },

        discount: (state) => {
            if (!state.coupon) return 0
            return state.subtotal * (state.coupon.percentage / 100)
        },

        shipping: (state) => {
            return state.subtotal >= 199 ? 0 : state.shippingCost
        },

        total() {
            return this.subtotal - this.discount + this.shipping
        },

        cartItems: (state) => state.items
    },

    actions: {
        // Carregar carrinho do localStorage
        loadCart() {
            const savedCart = localStorage.getItem('cartItems')
            if (savedCart) {
                this.items = JSON.parse(savedCart)
            }
        },

        // Salvar carrinho
        saveCart() {
            localStorage.setItem('cartItems', JSON.stringify(this.items))
        },

        // Adicionar item ao carrinho
        addItem(product) {
            const existingItem = this.items.find(item => item.id === product.id)

            if (existingItem) {
                // Se já existe, aumenta quantidade
                if (existingItem.quantity < product.estoque) {
                    existingItem.quantity++
                } else {
                    throw new Error('Quantidade máxima em estoque atingida')
                }
            } else {
                // Se não existe, adiciona novo item
                this.items.push({
                    ...product,
                    quantity: 1
                })
            }

            this.saveCart()
        },

        // Atualizar quantidade
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

        // Remover item
        removeItem(productId) {
            this.items = this.items.filter(item => item.id !== productId)
            this.saveCart()
        },

        // Limpar carrinho
        clearCart() {
            this.items = []
            this.coupon = null
            this.saveCart()
        }
    }
})
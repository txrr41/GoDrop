import { defineStore } from 'pinia'
import api from '../api/api'

export const useProductStore = defineStore('product', {
    state: () => ({
        products: [],
        categories: [],
        loading: false,
        error: null
    }),

    getters: {
        totalProducts: ({ products }) => products.length,
        totalStock: ({ products }) => products.reduce((acc, p) => acc + p.estoque, 0),
        totalValue: ({ products }) => products.reduce((acc, p) => acc + (p.preco * p.estoque), 0),
        lowStockCount: ({ products }) => products.filter(p => p.estoque < 20).length,
        activeProducts: ({ products }) => products.filter(p => p.ativo)
    },

    actions: {
        async fetchProducts() {
           try {
               this.loading = true
               this.error = null
               const { data } = await api.get('/produtos')
               this.products = data
               console.log('Produtos carregados:', data)
           } catch (error){
               console.log('Erro ao buscar produtos', error)
               this.error = 'Erro ao carregar produtos'
               throw error
           } finally {
               this.loading = false
           }
        },

        async createProduct(productData){
            try {
                this.loading = true
                this.error = null
                const { data } = await api.post('/produtos', productData)
                this.products.push(data)
                console.log('Produto criado:', data)
            } catch (error){
                console.log('Erro ao criar produto', error)
                this.error = 'Erro ao criar produto'
                throw error
            } finally {
                this.loading = false
            }
        },

        async updateProduct(id, updatedData) {
            try {
                this.loading = true
                this.error = null
                const { data } = await api.put(`/produtos/${id}`, updatedData)
                const index = this.products.findIndex(p => p.id === id)
                if (index !== -1){
                    this.products[index] = data
                }
                console.log('Produto atualizado:', data)
            } catch (error){
                console.log('Erro ao atualizar produto', error)
                this.error = 'Erro ao atualizar produto'
                throw error
            } finally {
                this.loading = false
            }
        },

        async deleteProduct(id) {
            try {
                this.loading = true
                this.error = null
                await api.delete(`/produtos/${id}`)
                this.products = this.products.filter(p => p.id !== id)
                console.log('Produto deletado:', id)
            } catch (error){
                console.log('Erro ao deletar produto', error)
                this.error = 'Erro ao deletar produto'
                throw error
            } finally {
                this.loading = false
            }
        },

        addCategory(categoryName) {
            if (!this.categories.find(c => c.name === categoryName)) {
                this.categories.push({ name: categoryName, value: categoryName })
            }
        }
    }
})
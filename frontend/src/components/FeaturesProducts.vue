<template>
  <section class="featured-section py-12 py-md-16">
    <v-container>
      <div class="text-center mb-10">
        <h2 class="text-h4 text-md-h3 font-weight-bold mb-2">Produtos em Destaque</h2>
        <p class="text-body-1 text-grey-darken-1">Os melhores produtos selecionados especialmente para você</p>
      </div>

      <div v-if="productStore.loading" class="text-center py-16">
        <v-progress-circular indeterminate color="primary" size="64" />
      </div>

      <v-row v-else-if="featuredProducts.length > 0">
        <v-col
            v-for="product in featuredProducts"
            :key="product.id"
            cols="12"
            sm="6"
            md="4"
            lg="3"
        >
          <v-card
              class="product-card h-100"
              elevation="2"
              @click="viewProduct(product)"
          >
            <div class="product-image-container">
              <v-img
                  :src="product.imagem || 'https://via.placeholder.com/400x300'"
                  :alt="product.nome"
                  height="250"
                  cover
                  class="product-image"
              />
              <v-chip
                  v-if="product.estoque < 10"
                  size="small"
                  color="error"
                  class="stock-badge"
              >
                Últimas unidades!
              </v-chip>
            </div>

            <v-card-title class="text-subtitle-1 font-weight-bold">
              {{ product.nome }}
            </v-card-title>

            <v-card-subtitle class="text-caption">
              {{ product.categoria }}
            </v-card-subtitle>

            <v-card-text>
              <p class="text-body-2 mb-2 product-description">
                {{ product.descricao }}
              </p>
              <div class="d-flex align-center justify-space-between">
                <span class="text-h6 font-weight-bold text-primary">
                  {{ formatCurrency(product.preco) }}
                </span>
                <span class="text-caption text-grey">
                  {{ product.estoque }} em estoque
                </span>
              </div>
            </v-card-text>

            <v-card-actions>
              <v-btn
                  color="primary"
                  variant="flat"
                  block
                  @click.stop="addToCart(product)"
                  :disabled="product.estoque === 0"
              >
                <v-icon start>mdi-cart-plus</v-icon>
                {{ product.estoque === 0 ? 'Sem Estoque' : 'Adicionar ao Carrinho' }}
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>

      <div v-else class="text-center py-16">
        <v-icon size="80" color="grey-lighten-2">mdi-package-variant</v-icon>
        <h3 class="text-h6 mt-4 text-grey">Nenhum produto em destaque no momento</h3>
      </div>

      <div v-if="featuredProducts.length > 0" class="text-center mt-10">
        <v-btn
            to="/produtos"
            size="large"
            variant="outlined"
            color="primary"
        >
          Ver Todos os Produtos
          <v-icon end>mdi-arrow-right</v-icon>
        </v-btn>
      </div>
    </v-container>
  </section>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useProductStore } from '../stores/product'
import { useCartStore } from '../stores/cart'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const productStore = useProductStore()
const cartStore = useCartStore()
const authStore = useAuthStore()

const featuredProducts = computed(() => {
  return productStore.products
      .filter(p => p.destaque && p.ativo)
      .slice(0, 8)
})

const formatCurrency = (value) => {
  return value.toLocaleString('pt-BR', {
    style: 'currency',
    currency: 'BRL'
  })
}

const viewProduct = (product) => {
  console.log('Ver produto:', product)
}

const addToCart = (product) => {
  if (!authStore.isLogged) {
    if (confirm('Você precisa estar logado para adicionar itens ao carrinho. Deseja fazer login?')) {
    }
    return
  }

  try {
    cartStore.addItem(product)
  } catch (error) {
    alert(error.message)
  }
}

onMounted(async () => {
  if (productStore.products.length === 0) {
    await productStore.fetchProducts()
  }
})
</script>

<style scoped>
.featured-section {
  background: linear-gradient(180deg, #f8f9fa 0%, #ffffff 100%);
}

.product-card {
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border-radius: 16px !important;
  overflow: hidden;
}

.product-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.15) !important;
}

.product-image-container {
  position: relative;
  overflow: hidden;
}

.product-image {
  transition: transform 0.3s ease;
}

.product-card:hover .product-image {
  transform: scale(1.05);
}

.stock-badge {
  position: absolute;
  top: 12px;
  right: 12px;
  z-index: 1;
}

.product-description {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  min-height: 40px;
}
</style>
<template>
  <section class="ecommerce-showcase py-12 py-md-16">
    <v-container>
      <div class="showcase-header text-center mb-12">
        <h2 class="showcase-title">Produtos em Destaque</h2>
        <p class="showcase-subtitle">Os melhores produtos selecionados especialmente para você</p>
      </div>

      <div v-if="productStore.loading" class="text-center py-16">
        <v-progress-circular indeterminate color="#005EEA" size="64" width="4" />
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
          <div class="product-card" @click="viewProduct(product)">
            <div class="card-image-wrapper">
              <v-img
                  :src="product.imagem || 'https://via.placeholder.com/400x300'"
                  :alt="product.nome"
                  height="220"
                  cover
                  class="product-image"
              />
              <span
                  v-if="product.estoque < 10"
                  class="stock-alert-badge"
              >
                Últimas unidades
              </span>
            </div>

            <div class="card-content">
              <span class="product-category">{{ product.categoria }}</span>
              <h3 class="product-name">{{ product.nome }}</h3>
              <p class="product-description">{{ product.descricao }}</p>

              <div class="price-section">
                <span class="product-price">{{ formatCurrency(product.preco) }}</span>
                <span class="product-stock" :class="{ 'text-error': product.estoque === 0 }">
                  {{ product.estoque > 0 ? `${product.estoque} em estoque` : 'Sem estoque' }}
                </span>
              </div>
            </div>

            <div class="card-actions-wrapper">
              <button
                  class="btn-add-cart"
                  :disabled="product.estoque === 0"
                  @click.stop="addToCart(product)"
              >
                <v-icon start size="20">mdi-cart-outline</v-icon>
                {{ product.estoque === 0 ? 'Indisponível' : 'Adicionar ao Carrinho' }}
              </button>
            </div>
          </div>
        </v-col>
      </v-row>

      <div v-else class="empty-state text-center py-16">
        <div class="empty-icon-wrapper mx-auto mb-4">
          <v-icon size="48" color="#94A3B8">mdi-package-variant</v-icon>
        </div>
        <h3 class="empty-title">Nenhum produto em destaque no momento</h3>
      </div>

      <div v-if="featuredProducts.length > 0" class="text-center mt-12">
        <button class="btn-view-all" @click="$router.push('/produtos')">
          Ver Todos os Produtos
          <v-icon end size="20">mdi-arrow-right</v-icon>
        </button>
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
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap');


.ecommerce-showcase {
  background-color: #FAFAFA;
  font-family: 'Inter', sans-serif;
}

.showcase-title {
  font-size: 2.25rem;
  font-weight: 800;
  color: #0F172A;
  letter-spacing: -0.03em;
  margin-bottom: 8px;
}

.showcase-subtitle {
  font-size: 1.125rem;
  color: #64748B;
}

.product-card {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: #FFFFFF;
  border: 1px solid #E2E8F0;
  border-radius: 16px;
  cursor: pointer;
  overflow: hidden;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  position: relative;
}

.product-card:hover {
  border-color: #005EEA;
  box-shadow: 0 12px 24px -8px rgba(0, 94, 234, 0.15);
  transform: translateY(-4px);
}

/* =========================================
   IMAGEM E BADGES
   ========================================= */
.card-image-wrapper {
  position: relative;
  overflow: hidden;
  background: #F8FAFC;
  padding: 16px;
}

.product-image {
  transition: transform 0.4s ease;
  border-radius: 8px;
}

.product-card:hover .product-image {
  transform: scale(1.05);
}

.stock-alert-badge {
  position: absolute;
  top: 12px;
  left: 12px;
  background: #EF4444;
  color: #FFFFFF;
  padding: 4px 10px;
  border-radius: 6px;
  font-size: 0.75rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  z-index: 2;
  box-shadow: 0 2px 8px rgba(239, 68, 68, 0.3);
}

.card-content {
  padding: 20px 20px 0;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
}

.product-category {
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
  color: #94A3B8;
  letter-spacing: 0.05em;
  margin-bottom: 6px;
}

.product-name {
  font-size: 1.125rem;
  font-weight: 700;
  color: #0F172A;
  line-height: 1.4;
  margin-bottom: 8px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.product-description {
  font-size: 0.875rem;
  color: #64748B;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin-bottom: 16px;
}

.price-section {
  margin-top: auto;
  margin-bottom: 16px;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.product-price {
  font-size: 1.5rem;
  font-weight: 800;
  color: #005EEA;
  letter-spacing: -0.02em;
}

.product-stock {
  font-size: 0.8125rem;
  color: #64748B;
  font-weight: 500;
}
.text-error {
  color: #EF4444;
}

.card-actions-wrapper {
  padding: 0 20px 20px;
}

.btn-add-cart {
  width: 100%;
  padding: 12px;
  background: #005EEA;
  color: #FFFFFF;
  border: none;
  border-radius: 10px;
  font-size: 0.95rem;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-add-cart:hover:not(:disabled) {
  background: #004BCC;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 94, 234, 0.3);
}

.btn-add-cart:active:not(:disabled) {
  transform: translateY(0);
}

.btn-add-cart:disabled {
  background: #E2E8F0;
  color: #94A3B8;
  cursor: not-allowed;
}

.empty-icon-wrapper {
  width: 80px;
  height: 80px;
  background: #F1F5F9;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.empty-title {
  color: #475569;
  font-weight: 600;
}

.btn-view-all {
  padding: 14px 32px;
  background: #FFFFFF;
  color: #005EEA;
  border: 2px solid #005EEA;
  border-radius: 99px; /* Botão em formato "pílula" */
  font-size: 1rem;
  font-weight: 600;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-view-all:hover {
  background: #005EEA;
  color: #FFFFFF;
  box-shadow: 0 8px 20px -6px rgba(0, 94, 234, 0.4);
}
</style>
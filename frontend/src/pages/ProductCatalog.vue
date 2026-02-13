<template>
  <div class="storefront-container">
    <v-container class="mt-8">
      <v-row>
        <v-col cols="12" md="3" class="pr-md-8">
          <div class="filter-sidebar">
            <h2 class="filter-title mb-6">Filtros</h2>

            <div class="filter-section mb-8">
              <label class="section-label">Categorias</label>
              <v-chip-group
                  v-model="activeCategory"
                  column
                  selected-class="active-chip"
              >
                <v-chip
                    v-for="cat in ['Todas', ...productStore.categories]"
                    :key="cat"
                    :value="cat"
                    filter
                    variant="tonal"
                    size="small"
                    class="category-chip"
                >
                  {{ cat }}
                </v-chip>
              </v-chip-group>
            </div>

            <div class="filter-section mb-8">
              <label class="section-label d-flex justify-space-between">
                Preço Máximo
                <span class="price-display">{{ formatCurrency(priceRange) }}</span>
              </label>
              <v-slider
                  v-model="priceRange"
                  :max="maxPriceAvailable"
                  :min="0"
                  step="10"
                  color="primary"
                  track-color="#E2E8F0"
                  hide-details
                  class="mt-2"
              />
            </div>

            <v-btn
                block
                variant="outlined"
                color="grey-darken-1"
                rounded="lg"
                size="small"
                class="text-none"
                @click="resetFilters"
            >
              Limpar Filtros
            </v-btn>
          </div>
        </v-col>

        <v-col cols="12" md="9">
          <div class="d-flex align-center justify-space-between mb-6">
            <p class="results-count">
              Exibindo <strong>{{ filteredProducts.length }}</strong> produtos
            </p>
            <div style="width: 200px">
              <v-select
                  v-model="sortBy"
                  :items="['Novidades', 'Menor Preço', 'Maior Preço']"
                  variant="outlined"
                  density="compact"
                  rounded="lg"
                  hide-details
                  label="Ordernar por"
              />
            </div>
          </div>

          <div v-if="filteredProducts.length === 0" class="empty-state text-center py-16">
            <v-icon size="80" color="grey-lighten-2">mdi-package-variant-closed</v-icon>
            <h3 class="mt-4 text-h6 font-weight-bold">Nenhum produto encontrado</h3>
            <p class="text-grey">Tente ajustar seus filtros ou busca.</p>
          </div>

          <v-row v-else>
            <v-col
                v-for="product in sortedProducts"
                :key="product.id"
                cols="12"
                sm="6"
                lg="4"
            >
              <v-card class="product-item-card" elevation="0">
                <div class="image-container">
                  <v-img
                      :src="product.imagem || 'https://via.placeholder.com/300'"
                      height="240"
                      cover
                      class="rounded-xl"
                  >
                    <template v-slot:placeholder>
                      <v-row class="fill-height ma-0" align="center" justify="center">
                        <v-progress-circular indeterminate color="grey-lighten-4" />
                      </v-row>
                    </template>
                    <div class="card-badges">
                      <v-chip
                          v-if="product.estoque < 5 && product.estoque > 0"
                          color="orange-darken-2"
                          size="x-small"
                          class="font-weight-bold"
                      >
                        ÚLTIMAS UNIDADES
                      </v-chip>
                      <v-chip
                          v-if="product.estoque === 0"
                          color="red"
                          size="x-small"
                          class="font-weight-bold"
                      >
                        ESGOTADO
                      </v-chip>
                    </div>
                  </v-img>
                  <v-btn
                      icon="mdi-cart-plus"
                      color="white"
                      class="quick-add-btn"
                      elevation="4"
                      :disabled="product.estoque === 0"
                      @click="addToCart(product)"
                  />
                </div>

                <div class="pa-4">
                  <div class="text-overline text-grey-darken-1 mb-1">{{ product.categoria }}</div>
                  <h3 class="product-name">{{ product.nome }}</h3>
                  <div class="d-flex align-center justify-space-between mt-3">
                    <span class="product-price">{{ formatCurrency(product.preco) }}</span>
                    <v-rating
                        :model-value="4.5"
                        density="compact"
                        size="x-small"
                        color="amber"
                        half-increments
                        readonly
                    />
                  </div>
                </div>
              </v-card>
            </v-col>
          </v-row>
        </v-col>
      </v-row>
    </v-container>

    <v-snackbar v-model="feedback.show" :color="feedback.color" rounded="pill" elevation="24">
      <div class="d-flex align-center">
        <v-icon start>{{ feedback.icon }}</v-icon>
        {{ feedback.message }}
      </div>
    </v-snackbar>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useProductStore } from '../stores/product'

const productStore = useProductStore()

const search = ref('')
const activeCategory = ref('Todas')
const priceRange = ref(5000)
const sortBy = ref('Novidades')
const cartCount = ref(0)

const feedback = ref({
  show: false,
  message: '',
  color: 'success',
  icon: 'mdi-check-circle'
})

const maxPriceAvailable = computed(() => {
  if (productStore.products.length === 0) return 1000
  return Math.max(...productStore.products.map(p => p.preco)) + 100
})

const filteredProducts = computed(() => {
  return productStore.products.filter(p => {
    const matchesSearch = p.nome.toLowerCase().includes(search.value.toLowerCase()) ||
        p.descricao.toLowerCase().includes(search.value.toLowerCase())
    const matchesCategory = activeCategory.value === 'Todas' || p.categoria === activeCategory.value
    const matchesPrice = p.preco <= priceRange.value

    return matchesSearch && matchesCategory && matchesPrice
  })
})

const sortedProducts = computed(() => {
  const items = [...filteredProducts.value]
  if (sortBy.value === 'Menor Preço') return items.sort((a, b) => a.preco - b.preco)
  if (sortBy.value === 'Maior Preço') return items.sort((a, b) => b.preco - a.preco)
  return items
})

const formatCurrency = (val) => {
  return Number(val).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
}

const resetFilters = () => {
  search.value = ''
  activeCategory.value = 'Todas'
  priceRange.value = maxPriceAvailable.value
}

const addToCart = (product) => {
  cartCount.value++
  feedback.value = {
    show: true,
    message: `${product.nome} adicionado à sacola!`,
    color: '#0F172A',
    icon: 'mdi-shopping'
  }
}

onMounted(async () => {
  await productStore.fetchProducts()
  priceRange.value = maxPriceAvailable.value
})
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');

.storefront-container {
  font-family: 'Inter', sans-serif;
  background-color: #FFFFFF;
  min-height: 100vh;
  color: #1E293B;
}

.store-nav {
  background: white;
  border-bottom: 1px solid #F1F5F9;
  position: sticky;
  top: 0;
  z-index: 100;
}

.brand-name {
  font-size: 20px;
  letter-spacing: -0.5px;
  color: #0F172A;
}

.global-search {
  width: 450px;
}

.cart-btn {
  text-transform: none;
  font-weight: 600;
  letter-spacing: 0;
  height: 44px !important;
}

.filter-sidebar {
  position: sticky;
  top: 100px;
}

.filter-title {
  font-size: 24px;
  font-weight: 700;
  letter-spacing: -1px;
}

.section-label {
  display: block;
  font-size: 14px;
  font-weight: 700;
  color: #64748B;
  text-transform: uppercase;
  letter-spacing: 1px;
  margin-bottom: 12px;
}

.category-chip {
  font-weight: 500;
  border: 1px solid #E2E8F0 !important;
}

.active-chip {
  background-color: #0F172A !important;
  color: white !important;
  border-color: #0F172A !important;
}

.price-display {
  color: #0F172A;
  font-variant-numeric: tabular-nums;
}

.product-item-card {
  transition: all 0.3s ease;
  background: transparent !important;
}

.image-container {
  position: relative;
  overflow: hidden;
  border-radius: 24px;
}

.image-container .v-img {
  transition: transform 0.6s cubic-bezier(0.4, 0, 0.2, 1);
}

.product-item-card:hover .image-container .v-img {
  transform: scale(1.08);
}

.card-badges {
  position: absolute;
  top: 12px;
  left: 12px;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.quick-add-btn {
  position: absolute;
  bottom: 16px;
  right: 16px;
  transform: translateY(60px);
  transition: all 0.3s ease;
  opacity: 0;
}

.product-item-card:hover .quick-add-btn {
  transform: translateY(0);
  opacity: 1;
}

.product-name {
  font-size: 17px;
  font-weight: 600;
  line-height: 1.4;
  color: #1E293B;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  min-height: 48px;
}

.product-price {
  font-size: 19px;
  font-weight: 800;
  color: #0F172A;
}

.results-count {
  color: #64748B;
  font-size: 14px;
}

::-webkit-scrollbar {
  width: 8px;
}
::-webkit-scrollbar-track {
  background: #f1f1f1;
}
::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 10px;
}
::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}
</style>
<template>
  <div class="catalog-root">

    <div class="hero-strip">
      <div class="hero-inner">
        <div class="hero-text">
          <span class="hero-eyebrow">Catálogo</span>
          <h1 class="hero-title">Descubra produtos<br/><em>selecionados para você</em></h1>
        </div>
        <div class="hero-search-wrap">
          <div class="hero-search">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="11" cy="11" r="8"/>
              <path d="m21 21-4.35-4.35"/>
            </svg>
            <input v-model="search" class="hero-search-input" placeholder="Buscar produtos..."/>
            <button v-if="search" class="hero-search-clear" @click="search = ''">✕</button>
          </div>
        </div>
      </div>
    </div>

    <div class="catalog-layout">

      <aside class="filter-panel">
        <div class="fp-header">
          <span class="fp-title">Filtros</span>
          <button class="fp-reset" @click="resetFilters">Limpar</button>
        </div>

        <div class="fp-section">
          <p class="fp-section-label">Categorias</p>
          <div class="fp-cats">
            <button
                v-for="cat in ['Todas', ...productStore.categories]"
                :key="cat"
                class="fp-cat-btn"
                :class="{ 'fp-cat-btn--active': activeCategory === cat }"
                @click="activeCategory = cat"
            >
              {{ cat }}
              <span class="fp-cat-count">{{ getCategoryCount(cat) }}</span>
            </button>
          </div>
        </div>

        <div class="fp-section">
          <p class="fp-section-label">
            Preço máximo
            <strong class="fp-price-val">{{ formatCurrency(priceRange) }}</strong>
          </p>
          <div class="fp-slider-wrap">
            <input
                v-model.number="priceRange"
                type="range"
                :min="0"
                :max="maxPriceAvailable"
                step="10"
                class="fp-slider"
            />
            <div class="fp-slider-labels">
              <span>R$ 0</span>
              <span>{{ formatCurrency(maxPriceAvailable) }}</span>
            </div>
          </div>
        </div>

        <div class="fp-section">
          <p class="fp-section-label">Disponibilidade</p>
          <label class="fp-toggle">
            <input type="checkbox" v-model="onlyInStock" class="fp-toggle-input"/>
            <span class="fp-toggle-track"><span class="fp-toggle-thumb"></span></span>
            Apenas em estoque
          </label>
        </div>

        <div class="fp-results-info">
          <span class="fp-count-num">{{ filteredProducts.length }}</span>
          <span class="fp-count-label">produtos encontrados</span>
        </div>
      </aside>

      <main class="catalog-main">

        <div class="cat-toolbar">
          <div class="cat-active-filters">
            <span v-if="activeCategory !== 'Todas'" class="cat-filter-tag">
              {{ activeCategory }}
              <button @click="activeCategory = 'Todas'">✕</button>
            </span>
            <span v-if="onlyInStock" class="cat-filter-tag">
              Em estoque
              <button @click="onlyInStock = false">✕</button>
            </span>
          </div>
          <div class="cat-sort">
            <span class="cat-sort-label">Ordenar:</span>
            <div class="cat-sort-btns">
              <button v-for="opt in sortOptions" :key="opt.value" class="cat-sort-btn"
                      :class="{ 'cat-sort-btn--active': sortBy === opt.value }" @click="sortBy = opt.value">{{
                  opt.label
                }}
              </button>
            </div>
          </div>
        </div>

        <div v-if="sortedProducts.length === 0" class="cat-empty">
          <div class="cat-empty-icon">
            <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1">
              <path d="M6 2L3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"/>
              <line x1="3" y1="6" x2="21" y2="6"/>
              <path d="M16 10a4 4 0 0 1-8 0"/>
            </svg>
          </div>
          <h3 class="cat-empty-title">Nenhum produto encontrado</h3>
          <p class="cat-empty-sub">Tente ajustar seus filtros ou termos de busca</p>
          <button class="cat-empty-btn" @click="resetFilters">Limpar filtros</button>
        </div>

        <div v-else class="product-grid">
          <article
              v-for="(product, i) in sortedProducts"
              :key="product.id"
              class="pcard"
              :style="{ animationDelay: `${i * 40}ms` }"
              @click="openProduct(product)"
          >
            <div class="pcard-img-wrap">
              <img
                  :src="product.imagem || `https://via.placeholder.com/400x500/EFF6FF/93C5FD?text=${encodeURIComponent(product.nome)}`"
                  :alt="product.nome"
                  class="pcard-img"
                  loading="lazy"
              />
              <div class="pcard-badges">
                <span v-if="product.destaque" class="pbadge pbadge--star">★ Destaque</span>
                <span v-if="product.estoque === 0" class="pbadge pbadge--out">Esgotado</span>
                <span v-else-if="product.estoque < 5" class="pbadge pbadge--low">Últimas unidades</span>
              </div>
              <div class="pcard-actions">
                <button
                    class="pcard-cart-btn"
                    :disabled="product.estoque === 0"
                    @click.stop="addToCart(product)"
                >
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
                    <path d="M6 2 3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"/>
                    <line x1="3" y1="6" x2="21" y2="6"/>
                    <path d="M16 10a4 4 0 0 1-8 0"/>
                  </svg>
                  {{ product.estoque === 0 ? 'Esgotado' : 'Adicionar' }}
                </button>
                <button class="pcard-detail-btn" @click.stop="openProduct(product)">
                  <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                    <circle cx="12" cy="12" r="3"/>
                  </svg>
                </button>
              </div>
            </div>
            <div class="pcard-info">
              <span class="pcard-cat">{{ product.categoria }}</span>
              <h3 class="pcard-name">{{ product.nome }}</h3>

              <div class="pcard-bottom">
                <div class="pcard-price-wrap">
                  <span v-if="product.offerPrice" class="pcard-price-original">
                    {{ formatCurrency(product.preco) }}
                  </span>
                  <span class="pcard-price" :class="{ 'pcard-price--offer': product.offerPrice }">
                      {{ formatCurrency(product.offerPrice ?? product.preco) }}
                   </span>
                </div>
                <span class="pcard-stock" :class="getStockClass(product.estoque)">
                      {{ getStockLabel(product.estoque) }}
                </span>
              </div>
            </div>
          </article>
        </div>

      </main>
    </div>

    <transition name="modal-fade">
      <div v-if="selectedProduct" class="modal-backdrop" @click.self="closeProduct">
        <div class="modal-card">
          <button class="modal-close" @click="closeProduct">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M18 6L6 18M6 6l12 12"/>
            </svg>
          </button>

          <div class="modal-layout">
            <div class="modal-img-side">
              <div class="modal-img-wrap">
                <img
                    :src="selectedProduct.imagem || `https://via.placeholder.com/600x700/EFF6FF/93C5FD?text=${encodeURIComponent(selectedProduct.nome)}`"
                    :alt="selectedProduct.nome"
                    class="modal-img"
                />
                <div class="modal-img-badges">
                  <span v-if="product.offer" class="pbadge pbadge--offer">
                  {{
                      product.offer.type === 'PERCENTAGE' ? `-${product.offer.discountValue}%` : `-R$${product.offer.discountValue}`
                    }}
                  </span>
                  <span v-if="selectedProduct.destaque" class="pbadge pbadge--star">★ Destaque</span>
                  <span v-if="selectedProduct.estoque === 0" class="pbadge pbadge--out">Esgotado</span>
                  <span v-else-if="selectedProduct.estoque < 5"
                        class="pbadge pbadge--low">Últimas {{ selectedProduct.estoque }} unidades</span>
                </div>
              </div>
            </div>

            <div class="modal-info-side">
              <div class="modal-eyebrow">{{ selectedProduct.categoria }}</div>
              <h2 class="modal-name">{{ selectedProduct.nome }}</h2>

              <div class="modal-price-row">
                <div>
                  <span v-if="selectedProduct.offerPrice" class="modal-original-price">
                    {{ formatCurrency(selectedProduct.preco) }}
                  </span>
                  <span class="modal-price">
                    {{ formatCurrency(selectedProduct.offerPrice ?? selectedProduct.preco) }}
                  </span>
                </div>
                <span class="modal-installment">
                  ou 10x de {{ formatCurrency((selectedProduct.offerPrice ?? selectedProduct.preco) / 10) }}
                  </span>
              </div>

              <div v-if="selectedProduct.descricao" class="modal-desc">
                <p class="modal-desc-label">Descrição</p>
                <p class="modal-desc-text">{{ selectedProduct.descricao }}</p>
              </div>

              <div class="modal-meta">
                <div class="modal-meta-item">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M20 7H4a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2z"/>
                    <polyline points="16 3 12 7 8 3"/>
                  </svg>
                  Estoque: <strong>{{ selectedProduct.estoque }} unidades</strong>
                </div>
                <div class="modal-meta-item">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path
                        d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/>
                  </svg>
                  Categoria: <strong>{{ selectedProduct.categoria }}</strong>
                </div>
                <div class="modal-meta-item">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <rect x="1" y="3" width="15" height="13" rx="2"/>
                    <path d="M16 8h4l3 5v3h-7V8z"/>
                    <circle cx="5.5" cy="18.5" r="2.5"/>
                    <circle cx="18.5" cy="18.5" r="2.5"/>
                  </svg>
                  Frete grátis acima de R$ 299,00
                </div>
              </div>

              <div class="modal-qty-row">
                <div class="modal-qty">
                  <button class="qty-btn" @click="qty > 1 && qty--">−</button>
                  <span class="qty-val">{{ qty }}</span>
                  <button class="qty-btn" @click="qty < selectedProduct.estoque && qty++">+</button>
                </div>
              </div>

              <div class="modal-cta">
                <button
                    class="modal-add-btn"
                    :disabled="selectedProduct.estoque === 0"
                    @click="addToCartFromModal(product)"
                >
                  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M6 2 3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"/>
                    <line x1="3" y1="6" x2="21" y2="6"/>
                    <path d="M16 10a4 4 0 0 1-8 0"/>
                  </svg>
                  {{ selectedProduct.estoque === 0 ? 'Produto Esgotado' : 'Adicionar ao Carrinho' }}
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </transition>

    <transition name="cart-slide">
      <div v-if="cartToast.show" class="cart-toast">
        <div class="ct-icon">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
            <path d="M20 6L9 17l-5-5"/>
          </svg>
        </div>
        <div class="ct-text">
          <strong>{{ cartToast.name }}</strong>
          <span>adicionado ao carrinho</span>
        </div>
        <div class="ct-count">
          {{ cartCount }}
          <svg width="14" height="14" viewBox="0 0 24 24" fill="currentColor">
            <path d="M6 2 3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"/>
          </svg>
        </div>
      </div>
    </transition>

  </div>
</template>

<script setup>
import {ref, computed, onMounted, watch} from 'vue'
import {useProductStore} from '../stores/product'
import {useRoute} from 'vue-router'
import {useCartStore} from "../stores/cart.js";
import {useAuthStore} from "../stores/auth.js";

const productStore = useProductStore()
const route = useRoute()
const cartStore = useCartStore()
const authStore = useAuthStore()

const search = ref('')
const activeCategory = ref('Todas')
const priceRange = ref(5000)
const sortBy = ref('newest')
const onlyInStock = ref(false)
const selectedProduct = ref(null)
const qty = ref(1)
const cartCount = ref(0)
const cartToast = ref({show: false, name: ''})

const sortOptions = [
  {value: 'newest', label: 'Novidades'},
  {value: 'asc', label: 'Menor preço'},
  {value: 'desc', label: 'Maior preço'},
]

watch(() => route.query.categoria, (nova) => {
  activeCategory.value = nova || 'Todas'
}, {immediate: true})

const maxPriceAvailable = computed(() => {
  if (!productStore.products.length) return 1000
  return Math.ceil(Math.max(...productStore.products.map(p => p.preco)) / 100) * 100 + 100
})

const getCategoryCount = (cat) => {
  if (cat === 'Todas') return productStore.products.length
  return productStore.products.filter(p => p.categoria === cat).length
}

const filteredProducts = computed(() =>
    productStore.products.filter(p => {
      const matchSearch = !search.value ||
          p.nome?.toLowerCase().includes(search.value.toLowerCase()) ||
          p.descricao?.toLowerCase().includes(search.value.toLowerCase())
      const matchCat = activeCategory.value === 'Todas' || p.categoria === activeCategory.value
      const matchPrice = p.preco <= priceRange.value
      const matchStock = !onlyInStock.value || p.estoque > 0
      return matchSearch && matchCat && matchPrice && matchStock
    })
)

const sortedProducts = computed(() => {
  const list = [...filteredProducts.value]
  if (sortBy.value === 'asc') return list.sort((a, b) => a.preco - b.preco)
  if (sortBy.value === 'desc') return list.sort((a, b) => b.preco - a.preco)
  return list
})

const formatCurrency = v =>
    Number(v).toLocaleString('pt-BR', {style: 'currency', currency: 'BRL'})

const getStockLabel = s => s === 0 ? 'Esgotado' : s < 5 ? `${s} restantes` : `${s} em estoque`
const getStockClass = s => s === 0 ? 'stock--out' : s < 5 ? 'stock--low' : 'stock--ok'

const resetFilters = () => {
  search.value = ''
  activeCategory.value = 'Todas'
  priceRange.value = maxPriceAvailable.value
  onlyInStock.value = false
  sortBy.value = 'newest'
}

const openProduct = (product) => {
  selectedProduct.value = product
  qty.value = 1
  document.body.style.overflow = 'hidden'
}

const closeProduct = () => {
  selectedProduct.value = null
  document.body.style.overflow = ''
}

let toastTimer = null
const showCartToast = (name) => {
  clearTimeout(toastTimer)
  cartToast.value = {show: true, name}
  toastTimer = setTimeout(() => {
    cartToast.value.show = false
  }, 3000)
}

const addToCart = (product) => {
  if (product.estoque === 0) return
  const cartProduct = product.offerPrice
      ? { ...product, preco: product.offerPrice, precoOriginal: product.preco }
      : product
  cartStore.addItem(cartProduct)
  cartCount.value++
  showCartToast(product.nome)
}

const addToCartFromModal = () => {
  if (!selectedProduct.value || selectedProduct.value.estoque === 0) return
  cartCount.value += qty.value
  showCartToast(selectedProduct.value.nome)
  closeProduct()
}

onMounted(async () => {
  await productStore.fetchProductsWithOffers()
  priceRange.value = maxPriceAvailable.value
})
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:ital,wght@0,300;0,400;0,500;0,600;0,700;1,400&family=DM+Serif+Display:ital@0;1&display=swap');

*, *::before, *::after {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

.catalog-root {
  --blue-50: #EFF6FF;
  --blue-100: #DBEAFE;
  --blue-200: #BFDBFE;
  --blue-400: #60A5FA;
  --blue-500: #3B82F6;
  --blue-600: #2563EB;
  --blue-700: #1D4ED8;
  --blue-900: #1E3A5F;

  --c-bg: #F8FAFF;
  --c-surface: #FFFFFF;
  --c-border: #E2EBFF;
  --c-border-2: #CBD5F0;
  --c-text: #0F172A;
  --c-text-2: #475569;
  --c-text-3: #94A3B8;
  --c-accent: var(--blue-600);
  --c-accent-h: var(--blue-700);
  --c-accent-s: var(--blue-50);

  --radius: 12px;
  --radius-lg: 20px;
  --shadow-sm: 0 1px 3px rgba(37, 99, 235, .08), 0 1px 2px rgba(0, 0, 0, .04);
  --shadow-md: 0 4px 16px rgba(37, 99, 235, .12), 0 2px 6px rgba(0, 0, 0, .06);
  --shadow-lg: 0 20px 60px rgba(37, 99, 235, .15), 0 8px 24px rgba(0, 0, 0, .08);
  --shadow-xl: 0 32px 80px rgba(37, 99, 235, .2), 0 12px 32px rgba(0, 0, 0, .1);

  font-family: 'Plus Jakarta Sans', sans-serif;
  background: var(--c-bg);
  color: var(--c-text);
  min-height: 100vh;
}

.hero-strip {
  background: linear-gradient(135deg, var(--blue-700) 0%, var(--blue-600) 50%, #4F86F7 100%);
  color: white;
  padding: 52px 48px 44px;
  position: relative;
  overflow: hidden;
}

.hero-strip::before {
  content: '';
  position: absolute;
  inset: 0;
  background: radial-gradient(ellipse 60% 80% at 80% -10%, rgba(255, 255, 255, .08) 0%, transparent 60%),
  radial-gradient(ellipse 40% 60% at 10% 110%, rgba(255, 255, 255, .05) 0%, transparent 50%);
  pointer-events: none;
}

.hero-strip::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  right: 0;
  height: 40px;
  background: var(--c-bg);
  clip-path: ellipse(55% 100% at 50% 100%);
}

.hero-inner {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 32px;
  flex-wrap: wrap;
  position: relative;
  z-index: 1;
}

.hero-eyebrow {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-size: 11px;
  font-weight: 600;
  letter-spacing: .18em;
  text-transform: uppercase;
  color: rgba(255, 255, 255, .65);
  margin-bottom: 14px;
}

.hero-eyebrow::before {
  content: '';
  display: block;
  width: 20px;
  height: 2px;
  background: rgba(255, 255, 255, .5);
  border-radius: 2px;
}

.hero-title {
  font-family: 'DM Serif Display', serif;
  font-size: clamp(30px, 3.5vw, 48px);
  font-weight: 400;
  line-height: 1.1;
  letter-spacing: -.02em;
}

.modal-original-price {
  font-size: 14px;
  color: var(--c-text-3);
  text-decoration: line-through;
  display: block;
  margin-bottom: 2px;
}

.hero-title em {
  font-style: italic;
  color: #BAD7FF;
}

.hero-search-wrap {
  flex-shrink: 0;
}

.hero-search {
  display: flex;
  align-items: center;
  gap: 10px;
  background: rgba(255, 255, 255, .12);
  border: 1.5px solid rgba(255, 255, 255, .22);
  border-radius: 50px;
  padding: 0 20px;
  height: 50px;
  width: 340px;
  transition: background .2s, border-color .2s, box-shadow .2s;
  color: rgba(255, 255, 255, .6);
  backdrop-filter: blur(8px);
}

.hero-search:focus-within {
  background: rgba(255, 255, 255, .18);
  border-color: rgba(255, 255, 255, .5);
  box-shadow: 0 0 0 4px rgba(255, 255, 255, .1);
}

.hero-search-input {
  flex: 1;
  background: transparent;
  border: none;
  outline: none;
  font: 500 15px 'Plus Jakarta Sans', sans-serif;
  color: white;
}

.hero-search-input::placeholder {
  color: rgba(255, 255, 255, .42);
}

.hero-search-clear {
  background: transparent;
  border: none;
  color: rgba(255, 255, 255, .5);
  cursor: pointer;
  font-size: 15px;
  line-height: 1;
  padding: 0;
  transition: color .15s;
}

.hero-search-clear:hover {
  color: white;
}

.catalog-layout {
  display: grid;
  grid-template-columns: 272px 1fr;
  gap: 0;
  max-width: 1400px;
  margin: 0 auto;
  padding: 36px 48px 60px;
  align-items: start;
}

.filter-panel {
  position: sticky;
  top: 24px;
  background: var(--c-surface);
  border: 1.5px solid var(--c-border);
  border-radius: var(--radius-lg);
  padding: 24px 20px;
  margin-right: 28px;
  box-shadow: var(--shadow-sm);
}

.fp-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1.5px solid var(--c-border);
}

.fp-title {
  font-size: 15px;
  font-weight: 700;
  letter-spacing: -.02em;
  color: var(--c-text);
}

.fp-reset {
  font-size: 12px;
  font-weight: 600;
  color: var(--c-accent);
  background: var(--blue-50);
  border: none;
  border-radius: 20px;
  padding: 4px 12px;
  cursor: pointer;
  transition: background .15s, color .15s;
}

.fp-reset:hover {
  background: var(--blue-100);
  color: var(--blue-700);
}

.fp-section {
  margin-bottom: 24px;
  padding-bottom: 24px;
  border-bottom: 1.5px solid var(--c-border);
}

.fp-section:last-of-type {
  border-bottom: none;
  margin-bottom: 0;
  padding-bottom: 0;
}

.fp-section-label {
  font-size: 10.5px;
  font-weight: 700;
  letter-spacing: .12em;
  text-transform: uppercase;
  color: var(--c-text-3);
  margin-bottom: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.fp-price-val {
  font-size: 12px;
  font-weight: 700;
  color: var(--c-accent);
  letter-spacing: 0;
  text-transform: none;
  background: var(--blue-50);
  padding: 2px 8px;
  border-radius: 20px;
}

.fp-cats {
  display: flex;
  flex-direction: column;
  gap: 3px;
}

.fp-cat-btn {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  background: transparent;
  border: none;
  border-radius: 8px;
  padding: 8px 10px;
  font: 500 13.5px 'Plus Jakarta Sans', sans-serif;
  color: var(--c-text-2);
  cursor: pointer;
  text-align: left;
  transition: all .15s;
}

.fp-cat-btn:hover {
  background: var(--blue-50);
  color: var(--c-accent);
}

.fp-cat-btn--active {
  background: var(--blue-600);
  color: white;
  font-weight: 600;
}

.fp-cat-count {
  font-size: 11px;
  font-weight: 600;
  background: rgba(255, 255, 255, .22);
  padding: 1px 7px;
  border-radius: 10px;
}

.fp-cat-btn:not(.fp-cat-btn--active) .fp-cat-count {
  background: var(--c-border);
  color: var(--c-text-3);
}

.fp-slider-wrap {
  padding: 4px 0;
}

.fp-slider {
  -webkit-appearance: none;
  width: 100%;
  height: 5px;
  border-radius: 3px;
  background: var(--c-border);
  outline: none;
  cursor: pointer;
}

.fp-slider::-webkit-slider-thumb {
  -webkit-appearance: none;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: var(--blue-600);
  cursor: pointer;
  border: 3px solid white;
  box-shadow: 0 0 0 1.5px var(--blue-600), 0 2px 8px rgba(37, 99, 235, .3);
  transition: transform .15s, box-shadow .15s;
}

.fp-slider::-webkit-slider-thumb:hover {
  transform: scale(1.15);
  box-shadow: 0 0 0 1.5px var(--blue-600), 0 4px 12px rgba(37, 99, 235, .4);
}

.fp-slider-labels {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
  font-size: 11.5px;
  color: var(--c-text-3);
  font-weight: 500;
}

.fp-toggle {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 13.5px;
  font-weight: 500;
  color: var(--c-text-2);
  cursor: pointer;
  user-select: none;
}

.fp-toggle-input {
  display: none;
}

.fp-toggle-track {
  width: 40px;
  height: 22px;
  background: var(--c-border);
  border-radius: 11px;
  position: relative;
  transition: background .2s;
  flex-shrink: 0;
}

.fp-toggle-input:checked + .fp-toggle-track {
  background: var(--blue-600);
}

.fp-toggle-thumb {
  position: absolute;
  top: 3px;
  left: 3px;
  width: 16px;
  height: 16px;
  background: white;
  border-radius: 50%;
  transition: left .2s;
  box-shadow: 0 1px 4px rgba(0, 0, 0, .2);
}

.fp-toggle-input:checked + .fp-toggle-track .fp-toggle-thumb {
  left: 21px;
}

.fp-results-info {
  text-align: center;
  margin-top: 20px;
  padding: 16px;
  background: linear-gradient(135deg, var(--blue-50), #EEF2FF);
  border-radius: var(--radius);
  border: 1.5px solid var(--blue-100);
}

.fp-count-num {
  display: block;
  font-family: 'DM Serif Display', serif;
  font-size: 40px;
  font-weight: 400;
  letter-spacing: -.03em;
  color: var(--blue-600);
  line-height: 1;
}

.fp-count-label {
  font-size: 12px;
  font-weight: 500;
  color: var(--c-text-3);
  margin-top: 4px;
  display: block;
}

.catalog-main {
  min-width: 0;
}

.cat-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 24px;
  gap: 16px;
  flex-wrap: wrap;
}

.cat-active-filters {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
}

.cat-filter-tag {
  display: flex;
  align-items: center;
  gap: 6px;
  background: var(--blue-600);
  color: white;
  border-radius: 50px;
  padding: 5px 12px 5px 14px;
  font-size: 12.5px;
  font-weight: 600;
}

.cat-filter-tag button {
  background: rgba(255, 255, 255, .25);
  border: none;
  color: white;
  cursor: pointer;
  font-size: 12px;
  line-height: 1;
  padding: 2px 4px;
  border-radius: 4px;
  transition: background .15s;
}

.cat-filter-tag button:hover {
  background: rgba(255, 255, 255, .4);
}

.cat-sort {
  display: flex;
  align-items: center;
  gap: 10px;
}

.cat-sort-label {
  font-size: 12.5px;
  font-weight: 500;
  color: var(--c-text-3);
  white-space: nowrap;
}

.cat-sort-btns {
  display: flex;
  gap: 4px;
}

.cat-sort-btn {
  border: 1.5px solid var(--c-border);
  background: var(--c-surface);
  border-radius: 50px;
  padding: 6px 14px;
  font: 600 12.5px 'Plus Jakarta Sans', sans-serif;
  color: var(--c-text-2);
  cursor: pointer;
  transition: all .15s;
  white-space: nowrap;
}

.cat-sort-btn:hover {
  color: var(--c-accent);
  background: var(--blue-50);
}

.cat-sort-btn--active {
  background: var(--blue-600);
  color: white;
  border-color: var(--blue-600);
}

.cat-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  padding: 100px 32px;
  text-align: center;
  color: var(--c-text-3);
}

.cat-empty-icon {
  width: 100px;
  height: 100px;
  background: var(--blue-50);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--blue-200);
}

.cat-empty-title {
  font-family: 'DM Serif Display', serif;
  font-size: 22px;
  font-weight: 400;
  color: var(--c-text);
  letter-spacing: -.02em;
}

.cat-empty-sub {
  font-size: 14px;
}

.cat-empty-btn {
  margin-top: 8px;
  background: var(--blue-600);
  color: white;
  border: none;
  border-radius: 50px;
  padding: 12px 28px;
  font: 600 13.5px 'Plus Jakarta Sans', sans-serif;
  cursor: pointer;
  transition: background .15s, transform .1s;
}

.cat-empty-btn:hover {
  background: var(--blue-700);
}

.cat-empty-btn:active {
  transform: scale(.97);
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(230px, 1fr));
  gap: 20px;
}

.pcard {
  background: var(--c-surface);
  border: 1.5px solid var(--c-border);
  border-radius: var(--radius-lg);
  overflow: hidden;
  cursor: pointer;
  animation: fadeUp .45s ease both;
  transition: box-shadow .25s, transform .25s, border-color .25s;
}

.pcard:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-5px);
  border-color: var(--blue-200);
}

@keyframes fadeUp {
  from {
    opacity: 0;
    transform: translateY(18px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.pcard-img-wrap {
  position: relative;
  aspect-ratio: 3/4;
  overflow: hidden;
  background: var(--blue-50);
}

.pcard-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  transition: transform .5s cubic-bezier(.4, 0, .2, 1);
}

.pcard:hover .pcard-img {
  transform: scale(1.06);
}

.pcard-badges {
  position: absolute;
  top: 10px;
  left: 10px;
  display: flex;
  flex-direction: column;
  gap: 5px;
  z-index: 1;
}

.pbadge {
  display: inline-block;
  padding: 4px 10px;
  border-radius: 50px;
  font-size: 10.5px;
  font-weight: 700;
  letter-spacing: .05em;
  text-transform: uppercase;
  backdrop-filter: blur(6px);
}

.pbadge--star {
  background: rgba(37, 99, 235, .9);
  color: white;
}

.pbadge--out {
  background: rgba(220, 38, 38, .9);
  color: white;
}

.pbadge--low {
  background: rgba(234, 88, 12, .9);
  color: white;
}

.pcard-actions {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 14px;
  display: flex;
  gap: 8px;
  background: linear-gradient(transparent, rgba(15, 23, 42, .5));
  transform: translateY(100%);
  transition: transform .28s cubic-bezier(.4, 0, .2, 1);
}

.pcard:hover .pcard-actions {
  transform: translateY(0);
}

.pcard-cart-btn {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 7px;
  background: var(--blue-600);
  border: none;
  border-radius: 8px;
  height: 40px;
  font: 700 12.5px 'Plus Jakarta Sans', sans-serif;
  color: white;
  cursor: pointer;
  transition: background .15s, transform .1s;
  letter-spacing: .01em;
}

.pcard-cart-btn:hover {
  background: var(--blue-700);
}

.pcard-cart-btn:active {
  transform: scale(.97);
}

.pcard-cart-btn:disabled {
  background: rgba(255, 255, 255, .25);
  color: rgba(255, 255, 255, .6);
  cursor: not-allowed;
}

.pcard-detail-btn {
  width: 40px;
  height: 40px;
  background: rgba(255, 255, 255, .15);
  border: 1.5px solid rgba(255, 255, 255, .25);
  border-radius: 8px;
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background .15s;
  flex-shrink: 0;
  backdrop-filter: blur(4px);
}

.pcard-detail-btn:hover {
  background: rgba(255, 255, 255, .28);
}

.pcard-info {
  padding: 14px 16px 16px;
}

.pcard-cat {
  font-size: 10.5px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: .12em;
  color: var(--c-accent);
}

.pcard-name {
  font-family: 'DM Serif Display', serif;
  font-size: 16px;
  font-weight: 400;
  letter-spacing: -.01em;
  margin: 5px 0 12px;
  line-height: 1.35;
  color: var(--c-text);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.pcard-bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.pcard-price {
  font-size: 18px;
  font-weight: 800;
  letter-spacing: -.03em;
  color: var(--c-text);
}

.pcard-stock {
  font-size: 11px;
  font-weight: 700;
  padding: 3px 9px;
  border-radius: 50px;
}

.stock--ok {
  background: #DCFCE7;
  color: #166534;
}

.stock--low {
  background: #FEF3C7;
  color: #92400E;
}

.stock--out {
  background: #FEE2E2;
  color: #991B1B;
}

/* ── MODAL ────────────────────────────────────────── */
.modal-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(15, 23, 42, .55);
  backdrop-filter: blur(10px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9000;
  padding: 24px;
}

.modal-card {
  background: var(--c-surface);
  border-radius: 24px;
  width: 100%;
  max-width: 960px;
  max-height: 90vh;
  overflow: hidden;
  position: relative;
  box-shadow: var(--shadow-xl);
  display: flex;
  flex-direction: column;
  animation: modalIn .32s cubic-bezier(.16, 1, .3, 1);
  border: 1.5px solid var(--c-border);
}

@keyframes modalIn {
  from {
    opacity: 0;
    transform: scale(.95) translateY(16px);
  }
  to {
    opacity: 1;
    transform: scale(1) translateY(0);
  }
}

.modal-close {
  position: absolute;
  top: 18px;
  right: 18px;
  z-index: 10;
  width: 38px;
  height: 38px;
  border-radius: 50%;
  background: white;
  border: 1.5px solid var(--c-border);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--c-text-2);
  box-shadow: var(--shadow-sm);
  transition: border-color .15s, color .15s, transform .15s;
}

.modal-close:hover {
  border-color: var(#93C5FD);
  color: var(--c-accent);
  transform: scale(1.08);
}

.modal-layout {
  display: grid;
  grid-template-columns: 1fr 1fr;
  overflow-y: auto;
  flex: 1;
}

.modal-img-side {
  background: var(--blue-50);
}

.modal-img-wrap {
  position: relative;
  height: 100%;
  min-height: 480px;
}

.modal-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.modal-img-badges {
  position: absolute;
  top: 18px;
  left: 18px;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.modal-info-side {
  padding: 44px 40px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  overflow-y: auto;
}

.modal-eyebrow {
  font-size: 10.5px;
  font-weight: 700;
  letter-spacing: .15em;
  text-transform: uppercase;
  color: var(--c-accent);
}

.modal-name {
  font-family: 'DM Serif Display', serif;
  font-size: 30px;
  font-weight: 400;
  letter-spacing: -.03em;
  line-height: 1.1;
  color: var(--c-text);
}

.modal-price-row {
  display: flex;
  align-items: baseline;
  gap: 12px;
  flex-wrap: wrap;
  padding-bottom: 20px;
  border-bottom: 1.5px solid var(--c-border);
}

.modal-price {
  font-size: 30px;
  font-weight: 800;
  letter-spacing: -.04em;
  color: var(--blue-700);
}

.modal-installment {
  font-size: 13px;
  color: var(--c-text-3);
  font-weight: 500;
}

.modal-desc-label {
  font-size: 11px;
  font-weight: 700;
  letter-spacing: .1em;
  text-transform: uppercase;
  color: var(--c-text-3);
  margin-bottom: 8px;
}

.modal-desc-text {
  font-size: 14px;
  line-height: 1.72;
  color: var(--c-text-2);
}

.modal-meta {
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 16px;
  background: var(--blue-50);
  border-radius: var(--radius);
  border: 1.5px solid var(--blue-100);
}

.modal-meta-item {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 13px;
  color: var(--c-text-2);
  font-weight: 500;
}

.modal-meta-item strong {
  color: var(--c-text);
  font-weight: 700;
}

.modal-meta-item svg {
  flex-shrink: 0;
  color: var(--c-accent);
}

.modal-qty-row {
  display: flex;
  align-items: center;
  gap: 16px;
}

.modal-qty {
  display: flex;
  align-items: center;
  border: 1.5px solid var(--c-border);
  border-radius: 50px;
  overflow: hidden;
  background: var(--c-surface);
}

.qty-btn {
  width: 42px;
  height: 42px;
  background: transparent;
  border: none;
  font-size: 18px;
  cursor: pointer;
  color: var(--c-accent);
  transition: background .15s;
  font-weight: 400;
  display: flex;
  align-items: center;
  justify-content: center;
}

.qty-btn:hover {
  background: var(--blue-50);
}

.qty-val {
  min-width: 40px;
  text-align: center;
  font: 700 16px 'Plus Jakarta Sans', sans-serif;
  color: var(--c-text);
}

.modal-cta {
  margin-top: auto;
}

.modal-add-btn {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  background: linear-gradient(135deg, var(--blue-600), var(--blue-700));
  color: white;
  border: none;
  border-radius: 14px;
  height: 54px;
  font: 700 15px 'Plus Jakarta Sans', sans-serif;
  cursor: pointer;
  letter-spacing: .01em;
  transition: opacity .2s, transform .1s, box-shadow .2s;
  box-shadow: 0 4px 16px rgba(37, 99, 235, .35);
}

.modal-add-btn:hover {
  opacity: .92;
  box-shadow: 0 6px 24px rgba(37, 99, 235, .45);
}

.modal-add-btn:active {
  transform: scale(.98);
}

.modal-add-btn:disabled {
  background: var(--c-border);
  color: var(--c-text-3);
  box-shadow: none;
  cursor: not-allowed;
}

.modal-fade-enter-active, .modal-fade-leave-active {
  transition: opacity .25s;
}

.modal-fade-enter-from, .modal-fade-leave-to {
  opacity: 0;
}

.cart-toast {
  position: fixed;
  bottom: 28px;
  right: 28px;
  z-index: 9999;
  background: var(--blue-700);
  color: white;
  border-radius: 16px;
  padding: 14px 18px;
  display: flex;
  align-items: center;
  gap: 14px;
  box-shadow: 0 8px 32px rgba(37, 99, 235, .4), 0 2px 8px rgba(0, 0, 0, .12);
  max-width: 340px;
  border: 1px solid rgba(255, 255, 255, .12);
}

.ct-icon {
  width: 34px;
  height: 34px;
  background: rgba(255, 255, 255, .18);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.ct-text {
  flex: 1;
  min-width: 0;
}

.ct-text strong {
  display: block;
  font-size: 13.5px;
  font-weight: 700;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.ct-text span {
  font-size: 12px;
  color: rgba(255, 255, 255, .6);
}

.ct-count {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 16px;
  font-weight: 800;
  flex-shrink: 0;
  color: #BAD7FF;
}

.pbadge--offer {
  background: rgba(220, 38, 38, .9);
  color: white;
}

.pcard-price-wrap {
  display: flex;
  flex-direction: column;
}

.pcard-price-original {
  font-size: 12px;
  color: var(--c-text-3);
  text-decoration: line-through;
}

.pcard-price--offer {
  color: #DC2626;
}

.cart-slide-enter-active, .cart-slide-leave-active {
  transition: all .32s cubic-bezier(.16, 1, .3, 1);
}

.cart-slide-enter-from, .cart-slide-leave-to {
  opacity: 0;
  transform: translateY(16px) scale(.95);
}

@media (max-width: 1024px) {
  .catalog-layout {
    grid-template-columns: 230px 1fr;
    padding: 28px 24px;
  }
}

@media (max-width: 768px) {
  .hero-strip {
    padding: 36px 20px 36px;
  }

  .hero-search-wrap {
    width: 100%;
  }

  .hero-search {
    width: 100%;
  }

  .catalog-layout {
    grid-template-columns: 1fr;
    padding: 20px;
  }

  .filter-panel {
    position: static;
    margin-right: 0;
    margin-bottom: 24px;
  }

  .modal-layout {
    grid-template-columns: 1fr;
  }

  .modal-img-wrap {
    min-height: 280px;
  }

  .modal-info-side {
    padding: 28px 24px;
  }

  .modal-name {
    font-size: 24px;
  }
}
</style>
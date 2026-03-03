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
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="11" cy="11" r="8"/><path d="m21 21-4.35-4.35"/></svg>
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
              <button v-for="opt in sortOptions" :key="opt.value" class="cat-sort-btn" :class="{ 'cat-sort-btn--active': sortBy === opt.value }" @click="sortBy = opt.value">{{ opt.label }}</button>
            </div>
          </div>
        </div>

        <div v-if="sortedProducts.length === 0" class="cat-empty">
          <div class="cat-empty-icon">
            <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1"><path d="M6 2L3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"/><line x1="3" y1="6" x2="21" y2="6"/><path d="M16 10a4 4 0 0 1-8 0"/></svg>
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
                  :src="product.imagem || `https://via.placeholder.com/400x500/F8FAFC/CBD5E1?text=${encodeURIComponent(product.nome)}`"
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
                  <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M6 2 3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"/><line x1="3" y1="6" x2="21" y2="6"/><path d="M16 10a4 4 0 0 1-8 0"/></svg>
                  {{ product.estoque === 0 ? 'Esgotado' : 'Adicionar' }}
                </button>
                <button class="pcard-detail-btn" @click.stop="openProduct(product)">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>
                </button>
              </div>
            </div>
            <div class="pcard-info">
              <span class="pcard-cat">{{ product.categoria }}</span>
              <h3 class="pcard-name">{{ product.nome }}</h3>
              <div class="pcard-bottom">
                <span class="pcard-price">{{ formatCurrency(product.preco) }}</span>
                <span class="pcard-stock" :class="getStockClass(product.estoque)">{{ getStockLabel(product.estoque) }}</span>
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
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M18 6L6 18M6 6l12 12"/></svg>
          </button>

          <div class="modal-layout">
            <div class="modal-img-side">
              <div class="modal-img-wrap">
                <img
                    :src="selectedProduct.imagem || `https://via.placeholder.com/600x700/F8FAFC/CBD5E1?text=${encodeURIComponent(selectedProduct.nome)}`"
                    :alt="selectedProduct.nome"
                    class="modal-img"
                />
                <div class="modal-img-badges">
                  <span v-if="selectedProduct.destaque" class="pbadge pbadge--star">★ Destaque</span>
                  <span v-if="selectedProduct.estoque === 0" class="pbadge pbadge--out">Esgotado</span>
                  <span v-else-if="selectedProduct.estoque < 5" class="pbadge pbadge--low">Últimas {{ selectedProduct.estoque }} unidades</span>
                </div>
              </div>
            </div>

            <div class="modal-info-side">
              <div class="modal-eyebrow">{{ selectedProduct.categoria }}</div>
              <h2 class="modal-name">{{ selectedProduct.nome }}</h2>

              <div class="modal-price-row">
                <span class="modal-price">{{ formatCurrency(selectedProduct.preco) }}</span>
                <span class="modal-installment">ou 10x de {{ formatCurrency(selectedProduct.preco / 10) }}</span>
              </div>

              <div v-if="selectedProduct.descricao" class="modal-desc">
                <p class="modal-desc-label">Descrição</p>
                <p class="modal-desc-text">{{ selectedProduct.descricao }}</p>
              </div>

              <div class="modal-meta">
                <div class="modal-meta-item">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 7H4a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2z"/><polyline points="16 3 12 7 8 3"/></svg>
                  Estoque: <strong>{{ selectedProduct.estoque }} unidades</strong>
                </div>
                <div class="modal-meta-item">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/></svg>
                  Categoria: <strong>{{ selectedProduct.categoria }}</strong>
                </div>
                <div class="modal-meta-item">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="1" y="3" width="15" height="13" rx="2"/><path d="M16 8h4l3 5v3h-7V8z"/><circle cx="5.5" cy="18.5" r="2.5"/><circle cx="18.5" cy="18.5" r="2.5"/></svg>
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
                    @click="addToCartFromModal"
                >
                  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M6 2 3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"/><line x1="3" y1="6" x2="21" y2="6"/><path d="M16 10a4 4 0 0 1-8 0"/></svg>
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
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M20 6L9 17l-5-5"/></svg>
        </div>
        <div class="ct-text">
          <strong>{{ cartToast.name }}</strong>
          <span>adicionado ao carrinho</span>
        </div>
        <div class="ct-count">{{ cartCount }} <svg width="14" height="14" viewBox="0 0 24 24" fill="currentColor"><path d="M6 2 3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"/></svg></div>
      </div>
    </transition>

  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useProductStore } from '../stores/product'
import { useRoute } from 'vue-router'

const productStore = useProductStore()
const route = useRoute()

const search = ref('')
const activeCategory = ref('Todas')
const priceRange = ref(5000)
const sortBy = ref('newest')
const onlyInStock = ref(false)
const selectedProduct = ref(null)
const qty = ref(1)
const cartCount = ref(0)
const cartToast = ref({ show: false, name: '' })

const sortOptions = [
  { value: 'newest', label: 'Novidades' },
  { value: 'asc', label: 'Menor preço' },
  { value: 'desc', label: 'Maior preço' },
]

watch(() => route.query.categoria, (nova) => {
  activeCategory.value = nova || 'Todas'
}, { immediate: true })

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
    Number(v).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })

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
  cartToast.value = { show: true, name }
  toastTimer = setTimeout(() => { cartToast.value.show = false }, 3000)
}

const addToCart = (product) => {
  if (product.estoque === 0) return
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
  await productStore.fetchProducts()
  priceRange.value = maxPriceAvailable.value
})
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Fraunces:ital,wght@0,400;0,600;0,700;1,400;1,600&family=Geist:wght@300;400;500;600&display=swap');

*, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

.catalog-root {
  --c-bg:       #FAFAF8;
  --c-surface:  #FFFFFF;
  --c-border:   #E8E8E4;
  --c-text:     #1A1A18;
  --c-text-2:   #5A5A54;
  --c-text-3:   #9A9A92;
  --c-accent:   #1A1A18;
  --c-accent-2: #2D6A4F;
  --c-red:      #C0392B;
  --c-amber:    #D97706;
  --radius:     14px;
  --shadow-sm:  0 1px 4px rgba(0,0,0,.06);
  --shadow-md:  0 8px 24px rgba(0,0,0,.1);
  --shadow-lg:  0 24px 64px rgba(0,0,0,.18);
  font-family: 'Geist', sans-serif;
  background: var(--c-bg);
  color: var(--c-text);
  min-height: 100vh;
}

.hero-strip {
  background: var(--c-text);
  color: white;
  padding: 56px 48px 48px;
}
.hero-inner {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 32px;
  flex-wrap: wrap;
}
.hero-eyebrow {
  display: block;
  font-size: 12px;
  font-weight: 500;
  letter-spacing: .14em;
  text-transform: uppercase;
  color: rgba(255,255,255,.5);
  margin-bottom: 12px;
}
.hero-title {
  font-family: 'Fraunces', serif;
  font-size: clamp(32px, 4vw, 52px);
  font-weight: 600;
  line-height: 1.12;
  letter-spacing: -.03em;
}
.hero-title em { font-style: italic; color: #A8D5BA; }
.hero-search-wrap { flex-shrink: 0; }
.hero-search {
  display: flex;
  align-items: center;
  gap: 10px;
  background: rgba(255,255,255,.1);
  border: 1px solid rgba(255,255,255,.2);
  border-radius: 50px;
  padding: 0 20px;
  height: 50px;
  width: 340px;
  transition: background .2s, border-color .2s;
  color: rgba(255,255,255,.6);
}
.hero-search:focus-within {
  background: rgba(255,255,255,.15);
  border-color: rgba(255,255,255,.4);
}
.hero-search-input {
  flex: 1;
  background: transparent;
  border: none;
  outline: none;
  font: 400 15px 'Geist', sans-serif;
  color: white;
}
.hero-search-input::placeholder { color: rgba(255,255,255,.4); }
.hero-search-clear {
  background: transparent;
  border: none;
  color: rgba(255,255,255,.5);
  cursor: pointer;
  font-size: 16px;
  line-height: 1;
  padding: 0;
}

.catalog-layout {
  display: grid;
  grid-template-columns: 280px 1fr;
  gap: 0;
  max-width: 1400px;
  margin: 0 auto;
  padding: 40px 48px;
  align-items: start;
}

.filter-panel {
  position: sticky;
  top: 24px;
  background: var(--c-surface);
  border: 1px solid var(--c-border);
  border-radius: var(--radius);
  padding: 28px 24px;
  margin-right: 32px;
}
.fp-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 28px;
}
.fp-title { font-size: 16px; font-weight: 600; letter-spacing: -.02em; }
.fp-reset { font-size: 13px; color: var(--c-text-3); background: transparent; border: none; cursor: pointer; text-decoration: underline; text-underline-offset: 3px; transition: color .15s; }
.fp-reset:hover { color: var(--c-text); }

.fp-section { margin-bottom: 28px; padding-bottom: 28px; border-bottom: 1px solid var(--c-border); }
.fp-section:last-of-type { border-bottom: none; }
.fp-section-label { font-size: 11px; font-weight: 600; letter-spacing: .1em; text-transform: uppercase; color: var(--c-text-3); margin-bottom: 14px; display: flex; justify-content: space-between; align-items: center; }
.fp-price-val { font-size: 13px; font-weight: 600; color: var(--c-text); letter-spacing: 0; text-transform: none; }

.fp-cats { display: flex; flex-direction: column; gap: 4px; }
.fp-cat-btn {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  background: transparent;
  border: none;
  border-radius: 8px;
  padding: 9px 12px;
  font: 500 14px 'Geist', sans-serif;
  color: var(--c-text-2);
  cursor: pointer;
  text-align: left;
  transition: all .15s;
}
.fp-cat-btn:hover { background: var(--c-bg); color: var(--c-text); }
.fp-cat-btn--active { background: var(--c-text); color: white; }
.fp-cat-count { font-size: 12px; opacity: .6; }
.fp-cat-btn--active .fp-cat-count { opacity: .6; }

.fp-slider-wrap { padding: 4px 0; }
.fp-slider {
  -webkit-appearance: none;
  width: 100%;
  height: 4px;
  border-radius: 2px;
  background: var(--c-border);
  outline: none;
  cursor: pointer;
}
.fp-slider::-webkit-slider-thumb {
  -webkit-appearance: none;
  width: 20px; height: 20px;
  border-radius: 50%;
  background: var(--c-text);
  cursor: pointer;
  border: 3px solid white;
  box-shadow: 0 0 0 1px var(--c-text), 0 2px 6px rgba(0,0,0,.2);
  transition: transform .15s;
}
.fp-slider::-webkit-slider-thumb:hover { transform: scale(1.2); }
.fp-slider-labels { display: flex; justify-content: space-between; margin-top: 10px; font-size: 12px; color: var(--c-text-3); }

.fp-toggle { display: flex; align-items: center; gap: 10px; font-size: 14px; color: var(--c-text-2); cursor: pointer; user-select: none; }
.fp-toggle-input { display: none; }
.fp-toggle-track { width: 40px; height: 22px; background: var(--c-border); border-radius: 11px; position: relative; transition: background .2s; flex-shrink: 0; }
.fp-toggle-input:checked + .fp-toggle-track { background: var(--c-text); }
.fp-toggle-thumb { position: absolute; top: 3px; left: 3px; width: 16px; height: 16px; background: white; border-radius: 50%; transition: left .2s; box-shadow: 0 1px 3px rgba(0,0,0,.2); }
.fp-toggle-input:checked + .fp-toggle-track .fp-toggle-thumb { left: 21px; }

.fp-results-info { text-align: center; margin-top: 24px; }
.fp-count-num { display: block; font-family: 'Fraunces', serif; font-size: 36px; font-weight: 600; letter-spacing: -.04em; color: var(--c-text); }
.fp-count-label { font-size: 12px; color: var(--c-text-3); }

.catalog-main { min-width: 0; }

.cat-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 28px;
  gap: 16px;
  flex-wrap: wrap;
}
.cat-active-filters { display: flex; gap: 8px; flex-wrap: wrap; }
.cat-filter-tag {
  display: flex;
  align-items: center;
  gap: 6px;
  background: var(--c-text);
  color: white;
  border-radius: 50px;
  padding: 5px 12px 5px 14px;
  font-size: 13px;
  font-weight: 500;
}
.cat-filter-tag button {
  background: transparent;
  border: none;
  color: rgba(255,255,255,.7);
  cursor: pointer;
  font-size: 14px;
  line-height: 1;
  padding: 0;
  transition: color .15s;
}
.cat-filter-tag button:hover { color: white; }

.cat-sort { display: flex; align-items: center; gap: 10px; }
.cat-sort-label { font-size: 13px; color: var(--c-text-3); white-space: nowrap; }
.cat-sort-btns { display: flex; gap: 4px; }
.cat-sort-btn {
  border: 1px solid var(--c-border);
  background: transparent;
  border-radius: 50px;
  padding: 6px 14px;
  font: 500 13px 'Geist', sans-serif;
  color: var(--c-text-2);
  cursor: pointer;
  transition: all .15s;
  white-space: nowrap;
}
.cat-sort-btn:hover { border-color: var(--c-text); color: var(--c-text); }
.cat-sort-btn--active { background: var(--c-text); color: white; border-color: var(--c-text); }


.cat-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  padding: 100px 32px;
  text-align: center;
  color: var(--c-text-3);
}
.cat-empty-icon { opacity: .3; }
.cat-empty-title { font-family: 'Fraunces', serif; font-size: 24px; font-weight: 600; color: var(--c-text); letter-spacing: -.03em; }
.cat-empty-sub { font-size: 14px; }
.cat-empty-btn { margin-top: 8px; background: var(--c-text); color: white; border: none; border-radius: 50px; padding: 12px 28px; font: 600 14px 'Geist', sans-serif; cursor: pointer; transition: opacity .15s; }
.cat-empty-btn:hover { opacity: .8; }

/* PRODUCT GRID */
.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 24px;
}

.pcard {
  background: var(--c-surface);
  border: 1px solid var(--c-border);
  border-radius: var(--radius);
  overflow: hidden;
  cursor: pointer;
  animation: fadeUp .5s ease both;
  transition: box-shadow .25s, transform .25s, border-color .25s;
}
.pcard:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-4px);
  border-color: transparent;
}

@keyframes fadeUp {
  from { opacity: 0; transform: translateY(20px); }
  to   { opacity: 1; transform: translateY(0); }
}

.pcard-img-wrap {
  position: relative;
  aspect-ratio: 3/4;
  overflow: hidden;
  background: #F1F1EE;
}
.pcard-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  transition: transform .5s cubic-bezier(.4,0,.2,1);
}
.pcard:hover .pcard-img { transform: scale(1.07); }

.pcard-badges {
  position: absolute;
  top: 12px;
  left: 12px;
  display: flex;
  flex-direction: column;
  gap: 5px;
  z-index: 1;
}
.pbadge {
  display: inline-block;
  padding: 4px 10px;
  border-radius: 50px;
  font-size: 11px;
  font-weight: 700;
  letter-spacing: .04em;
  text-transform: uppercase;
}
.pbadge--star { background: #1A1A18; color: #A8D5BA; }
.pbadge--out  { background: #C0392B; color: white; }
.pbadge--low  { background: #D97706; color: white; }

.pcard-actions {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 16px;
  display: flex;
  gap: 8px;
  background: linear-gradient(transparent, rgba(0,0,0,.4));
  transform: translateY(100%);
  transition: transform .3s cubic-bezier(.4,0,.2,1);
}
.pcard:hover .pcard-actions { transform: translateY(0); }

.pcard-cart-btn {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  background: white;
  border: none;
  border-radius: 8px;
  height: 42px;
  font: 600 13px 'Geist', sans-serif;
  color: var(--c-text);
  cursor: pointer;
  transition: background .15s, opacity .15s;
}
.pcard-cart-btn:hover { background: #F0F0EC; }
.pcard-cart-btn:disabled { opacity: .5; cursor: not-allowed; }

.pcard-detail-btn {
  width: 42px;
  height: 42px;
  background: rgba(255,255,255,.2);
  border: 1px solid rgba(255,255,255,.3);
  border-radius: 8px;
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background .15s;
  flex-shrink: 0;
}
.pcard-detail-btn:hover { background: rgba(255,255,255,.35); }

.pcard-info { padding: 16px; }
.pcard-cat { font-size: 11px; font-weight: 600; text-transform: uppercase; letter-spacing: .1em; color: var(--c-text-3); }
.pcard-name { font-family: 'Fraunces', serif; font-size: 17px; font-weight: 600; letter-spacing: -.02em; margin: 4px 0 12px; line-height: 1.3; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.pcard-bottom { display: flex; align-items: center; justify-content: space-between; }
.pcard-price { font-size: 18px; font-weight: 700; letter-spacing: -.03em; }
.pcard-stock { font-size: 11px; font-weight: 600; padding: 3px 8px; border-radius: 50px; }
.stock--ok  { background: #ECFDF5; color: #065F46; }
.stock--low { background: #FFFBEB; color: #92400E; }
.stock--out { background: #FEF2F2; color: #991B1B; }

/* ── MODAL ────────────────────────────────────────── */
.modal-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(10,10,8,.6);
  backdrop-filter: blur(8px);
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
  box-shadow: var(--shadow-lg);
  display: flex;
  flex-direction: column;
  animation: modalIn .35s cubic-bezier(.16,1,.3,1);
}
@keyframes modalIn {
  from { opacity: 0; transform: scale(.94) translateY(20px); }
  to   { opacity: 1; transform: scale(1) translateY(0); }
}
.modal-close {
  position: absolute;
  top: 20px;
  right: 20px;
  z-index: 10;
  width: 40px; height: 40px;
  border-radius: 50%;
  background: white;
  border: 1px solid var(--c-border);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--c-text);
  box-shadow: var(--shadow-sm);
  transition: transform .15s;
}
.modal-close:hover { transform: scale(1.1); }

.modal-layout {
  display: grid;
  grid-template-columns: 1fr 1fr;
  overflow-y: auto;
  flex: 1;
}

.modal-img-side { background: #F1F1EE; }
.modal-img-wrap { position: relative; height: 100%; min-height: 480px; }
.modal-img { width: 100%; height: 100%; object-fit: cover; display: block; }
.modal-img-badges { position: absolute; top: 20px; left: 20px; display: flex; flex-direction: column; gap: 6px; }

.modal-info-side {
  padding: 48px 40px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  overflow-y: auto;
}
.modal-eyebrow { font-size: 11px; font-weight: 700; letter-spacing: .12em; text-transform: uppercase; color: var(--c-accent-2); }
.modal-name { font-family: 'Fraunces', serif; font-size: 32px; font-weight: 600; letter-spacing: -.04em; line-height: 1.1; }
.modal-price-row { display: flex; align-items: baseline; gap: 10px; flex-wrap: wrap; padding-bottom: 20px; border-bottom: 1px solid var(--c-border); }
.modal-price { font-size: 32px; font-weight: 700; letter-spacing: -.04em; }
.modal-installment { font-size: 14px; color: var(--c-text-3); }

.modal-desc-label { font-size: 12px; font-weight: 700; letter-spacing: .08em; text-transform: uppercase; color: var(--c-text-3); margin-bottom: 8px; }
.modal-desc-text { font-size: 14px; line-height: 1.7; color: var(--c-text-2); }

.modal-meta { display: flex; flex-direction: column; gap: 10px; padding: 16px; background: var(--c-bg); border-radius: var(--radius); }
.modal-meta-item { display: flex; align-items: center; gap: 10px; font-size: 13px; color: var(--c-text-2); }
.modal-meta-item strong { color: var(--c-text); }
.modal-meta-item svg { flex-shrink: 0; color: var(--c-text-3); }

.modal-qty-row { display: flex; align-items: center; gap: 16px; }
.modal-qty { display: flex; align-items: center; gap: 0; border: 1.5px solid var(--c-border); border-radius: 50px; overflow: hidden; }
.qty-btn { width: 44px; height: 44px; background: transparent; border: none; font-size: 20px; cursor: pointer; color: var(--c-text); transition: background .15s; font-weight: 300; }
.qty-btn:hover { background: var(--c-bg); }
.qty-val { min-width: 40px; text-align: center; font: 600 16px 'Geist', sans-serif; }

.modal-cta { margin-top: auto; }
.modal-add-btn {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  background: var(--c-text);
  color: white;
  border: none;
  border-radius: 50px;
  height: 56px;
  font: 600 15px 'Geist', sans-serif;
  cursor: pointer;
  letter-spacing: .01em;
  transition: opacity .2s, transform .1s;
}
.modal-add-btn:hover { opacity: .85; }
.modal-add-btn:active { transform: scale(.98); }
.modal-add-btn:disabled { opacity: .35; cursor: not-allowed; }

/* TRANSITION MODAL */
.modal-fade-enter-active, .modal-fade-leave-active { transition: opacity .3s; }
.modal-fade-enter-from, .modal-fade-leave-to { opacity: 0; }

/* ── CART TOAST ───────────────────────────────────── */
.cart-toast {
  position: fixed;
  bottom: 32px;
  right: 32px;
  z-index: 9999;
  background: var(--c-text);
  color: white;
  border-radius: 16px;
  padding: 16px 20px;
  display: flex;
  align-items: center;
  gap: 14px;
  box-shadow: var(--shadow-lg);
  max-width: 340px;
}
.ct-icon { width: 36px; height: 36px; background: var(--c-accent-2); border-radius: 50%; display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
.ct-text { flex: 1; min-width: 0; }
.ct-text strong { display: block; font-size: 14px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.ct-text span { font-size: 12px; color: rgba(255,255,255,.6); }
.ct-count { display: flex; align-items: center; gap: 5px; font-size: 16px; font-weight: 700; font-family: 'Fraunces', serif; flex-shrink: 0; color: #A8D5BA; }

.cart-slide-enter-active, .cart-slide-leave-active { transition: all .35s cubic-bezier(.16,1,.3,1); }
.cart-slide-enter-from, .cart-slide-leave-to { opacity: 0; transform: translateY(20px) scale(.95); }

@media (max-width: 1024px) {
  .catalog-layout { grid-template-columns: 240px 1fr; padding: 28px 24px; }
}
@media (max-width: 768px) {
  .hero-strip { padding: 36px 20px 32px; }
  .hero-search-wrap { width: 100%; }
  .hero-search { width: 100%; }
  .catalog-layout { grid-template-columns: 1fr; padding: 20px; }
  .filter-panel { position: static; margin-right: 0; margin-bottom: 24px; }
  .modal-layout { grid-template-columns: 1fr; }
  .modal-img-wrap { min-height: 300px; }
  .modal-info-side { padding: 28px 24px; }
  .modal-name { font-size: 24px; }
}
</style>
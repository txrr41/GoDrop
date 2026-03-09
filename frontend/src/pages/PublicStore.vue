<template>
  <div class="store-root" :style="`
    --p: ${store?.primaryColor   ?? '#2563EB'};
    --s: ${store?.secondaryColor ?? '#0EA5E9'};
    --bg: ${store?.backgroundColor ?? '#F8FAFF'};
    --p-rgb: ${hexToRgb(store?.primaryColor ?? '#2563EB')};
  `">

    <div v-if="storeStore.loading" class="full-center">
      <div class="loader-ring"></div>
    </div>

    <div v-else-if="!store" class="full-center flex-col">
      <span class="nf-emoji">🏪</span>
      <h2 class="nf-title">Loja não encontrada</h2>
      <p class="nf-sub">Esta loja pode estar offline ou não existe.</p>
      <a href="/" class="nf-btn">Voltar ao início</a>
    </div>

    <template v-else>

      <nav class="floatnav" :class="{ scrolled: isScrolled }">
        <div class="fn-inner">
          <div class="fn-brand">
            <div class="fn-logo">
              <img v-if="store.logoUrl" :src="store.logoUrl" class="fn-logo-img" :alt="store.storeName"/>
              <svg v-else width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
                <path d="M6 2L3 6v14a2 2 0 002 2h14a2 2 0 002-2V6l-3-4z"/>
                <line x1="3" y1="6" x2="21" y2="6"/>
              </svg>
            </div>
            <span class="fn-name">{{ store.storeName }}</span>
          </div>

          <div class="fn-search" :class="{ open: searchOpen }">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><circle cx="11" cy="11" r="8"/><path d="m21 21-4.35-4.35"/></svg>
            <input
                v-model="search"
                ref="searchInput"
                class="fn-search-input"
                placeholder="Buscar…"
                @focus="searchOpen = true"
                @blur="searchOpen = false"
            />
          </div>

          <div class="fn-right">
            <button class="fn-search-toggle" @click="toggleSearch">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="11" cy="11" r="8"/><path d="m21 21-4.35-4.35"/></svg>
            </button>
            <button class="fn-cart" @click="cartOpen = true">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="9" cy="21" r="1"/><circle cx="20" cy="21" r="1"/>
                <path d="M1 1h4l2.68 13.39a2 2 0 002 1.61h9.72a2 2 0 001.95-1.57l1.65-8.42H6"/>
              </svg>
              <span v-if="cartStore.totalItems > 0" class="fn-cart-badge">{{ cartStore.totalItems }}</span>
            </button>
          </div>
        </div>
      </nav>

      <section class="hero">

        <div class="hero-visual">
          <div class="hero-img-wrap">
            <img
                v-if="heroProduct"
                :src="heroProduct.imagem || fallbackImg(heroProduct.nome, '1200x900')"
                :alt="heroProduct.nome"
                class="hero-img"
            />
            <div v-else class="hero-img-placeholder">
              <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="rgba(255,255,255,.15)" stroke-width="1">
                <rect x="3" y="3" width="18" height="18" rx="2"/><circle cx="8.5" cy="8.5" r="1.5"/>
                <polyline points="21 15 16 10 5 21"/>
              </svg>
            </div>
          </div>

          <div class="hero-img-vignette"></div>

          <div v-if="heroProduct" class="hero-product-badge" @click="openModal(heroProduct)">
            <div class="hpb-img-wrap">
              <img :src="heroProduct.imagem || fallbackImg(heroProduct.nome, '80x80')" class="hpb-img" :alt="heroProduct.nome"/>
            </div>
            <div class="hpb-info">
              <span class="hpb-label">Destaque</span>
              <span class="hpb-name">{{ heroProduct.nome }}</span>
              <span class="hpb-price">{{ formatPrice(heroProduct.preco) }}</span>
            </div>
            <div class="hpb-arrow">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M5 12h14M12 5l7 7-7 7"/></svg>
            </div>
          </div>

          <div class="hero-chip hero-chip-1">
            <span class="chip-num">{{ store.products?.length ?? 0 }}</span>
            <span class="chip-lbl">produtos</span>
          </div>
          <div class="hero-chip hero-chip-2">
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>
            <span class="chip-lbl">Compra segura</span>
          </div>
        </div>

        <div class="hero-copy">
          <div class="hero-deco-line"></div>

          <div class="hero-eyebrow">
            <span class="eyebrow-dot"></span>
            Loja oficial
          </div>

          <h1 class="hero-title">
            <span class="ht-line ht-line-1">{{ firstWord }}</span>
            <span class="ht-line ht-line-2"><em>{{ restWords }}</em></span>
          </h1>

          <p v-if="store.description" class="hero-desc">{{ store.description }}</p>
          <p v-else class="hero-desc">Produtos selecionados com qualidade e curadoria especial para você.</p>

          <div class="hero-ctas">
            <button class="hcta-primary" @click="scrollToProducts">
              Explorar coleção
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M5 12h14M12 5l7 7-7 7"/></svg>
            </button>
            <button class="hcta-ghost" @click="cartOpen = true">
              Carrinho
              <span v-if="cartStore.totalItems > 0" class="hcta-badge">{{ cartStore.totalItems }}</span>
            </button>
          </div>

          <div class="hero-trust">
            <div class="trust-item">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M5 12h14M12 5l7 7-7 7"/></svg>
              Entrega rápida
            </div>
            <div class="trust-sep"></div>
            <div class="trust-item">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>
              Pagamento seguro
            </div>
            <div class="trust-sep"></div>
            <div class="trust-item">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="23 6 13.5 15.5 8.5 10.5 1 18"/></svg>
              Garantia de qualidade
            </div>
          </div>

          <div class="hero-powered">Powered by <strong>GoDrop</strong></div>
        </div>

        <div class="hero-ticker">
          <div class="ticker-track">
            <span v-for="(p, i) in tickerProducts" :key="i" class="ticker-item">
              <span class="ticker-dot"></span>
              {{ p.nome }}
              <span class="ticker-price">{{ formatPrice(p.preco) }}</span>
            </span>
          </div>
        </div>
      </section>

      <main class="store-main" id="produtos">
        <div class="main-header">
          <div>
            <h2 class="main-title">{{ search ? `Resultados: "${search}"` : 'Nossa coleção' }}</h2>
            <p class="main-sub">{{ sortedProducts.length }} produto{{ sortedProducts.length !== 1 ? 's' : '' }} disponíve{{ sortedProducts.length !== 1 ? 'is' : 'l' }}</p>
          </div>
          <div class="sort-row">
            <button v-for="o in sortOpts" :key="o.v" class="sort-pill" :class="{ active: sortBy === o.v }" @click="sortBy = o.v">{{ o.l }}</button>
          </div>
        </div>

        <div v-if="sortedProducts.length === 0" class="empty-wrap">
          <span class="empty-emoji">📦</span>
          <h3>Nenhum produto encontrado</h3>
          <p>Tente outro termo de busca</p>
          <button @click="search = ''" class="empty-reset">Limpar</button>
        </div>

        <div v-else class="pGrid">
          <article
              v-for="(p, i) in sortedProducts"
              :key="p.id"
              class="pCard"
              :style="`animation-delay:${i*35}ms`"
              @click="openModal(p)"
          >
            <div class="pCard-media">
              <img :src="p.imagem || fallbackImg(p.nome, '400x320')" :alt="p.nome" class="pCard-img" loading="lazy"/>
              <div class="pCard-actions">
                <button class="pca-add" :disabled="p.estoque === 0" @click.stop="addToCart(p)">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><circle cx="9" cy="21" r="1"/><circle cx="20" cy="21" r="1"/><path d="M1 1h4l2.68 13.39a2 2 0 002 1.61h9.72a2 2 0 001.95-1.57l1.65-8.42H6"/></svg>
                  {{ p.estoque === 0 ? 'Esgotado' : 'Adicionar' }}
                </button>
                <button class="pca-view" @click.stop="openModal(p)">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>
                </button>
              </div>
              <span v-if="p.estoque === 0"    class="pBadge pBadge-out">Esgotado</span>
              <span v-else-if="p.estoque < 5" class="pBadge pBadge-low">Últimas {{ p.estoque }}</span>
            </div>
            <div class="pCard-info">
              <p v-if="p.categoria" class="pCard-cat">{{ p.categoria }}</p>
              <h3 class="pCard-name">{{ p.nome }}</h3>
              <div class="pCard-foot">
                <div>
                  <p class="pCard-price">{{ formatPrice(p.preco) }}</p>
                  <p class="pCard-install">10x {{ formatPrice(p.preco/10) }}</p>
                </div>
                <button class="pCard-plus" :class="{ disabled: p.estoque === 0 }" :disabled="p.estoque === 0" @click.stop="addToCart(p)">
                  <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg>
                </button>
              </div>
            </div>
          </article>
        </div>
      </main>

      <transition name="modal-fade">
        <div v-if="selectedProduct" class="modal-back" @click.self="closeModal">
          <div class="modal-box">
            <button class="modal-close" @click="closeModal">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M18 6L6 18M6 6l12 12"/></svg>
            </button>
            <div class="modal-split">
              <div class="modal-img-col">
                <img :src="selectedProduct.imagem || fallbackImg(selectedProduct.nome, '600x500')" :alt="selectedProduct.nome" class="modal-img"/>
                <div v-if="selectedProduct.estoque === 0" class="modal-out-tag">Esgotado</div>
              </div>
              <div class="modal-info-col">
                <p v-if="selectedProduct.categoria" class="mi-cat">{{ selectedProduct.categoria }}</p>
                <h2 class="mi-name">{{ selectedProduct.nome }}</h2>
                <p v-if="selectedProduct.descricao" class="mi-desc">{{ selectedProduct.descricao }}</p>
                <div class="mi-price-block">
                  <span class="mi-price">{{ formatPrice(selectedProduct.preco) }}</span>
                  <span class="mi-install">ou 10x de {{ formatPrice(selectedProduct.preco / 10) }}</span>
                </div>
                <div class="mi-stock" :class="stockClass(selectedProduct.estoque)">
                  <span class="stock-dot"></span>{{ stockLabel(selectedProduct.estoque) }}
                </div>
                <div class="mi-qty-row">
                  <div class="mi-qty">
                    <button class="mi-qty-btn" @click="qty > 1 && qty--">−</button>
                    <span class="mi-qty-val">{{ qty }}</span>
                    <button class="mi-qty-btn" @click="qty < selectedProduct.estoque && qty++">+</button>
                  </div>
                </div>
                <button
                    class="mi-add"
                    :class="{ disabled: selectedProduct.estoque === 0 }"
                    :disabled="selectedProduct.estoque === 0"
                    @click="addToCartQty(selectedProduct)"
                >
                  <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="9" cy="21" r="1"/><circle cx="20" cy="21" r="1"/><path d="M1 1h4l2.68 13.39a2 2 0 002 1.61h9.72a2 2 0 001.95-1.57l1.65-8.42H6"/></svg>
                  {{ selectedProduct.estoque === 0 ? 'Produto esgotado' : 'Adicionar ao carrinho' }}
                </button>
              </div>
            </div>
          </div>
        </div>
      </transition>

      <transition name="fade-back">
        <div v-if="cartOpen" class="cart-back" @click="cartOpen = false"></div>
      </transition>
      <div class="cart-drawer" :class="{ open: cartOpen }">
        <div class="cd-head">
          <h3 class="cd-title">Carrinho</h3>
          <button class="cd-close" @click="cartOpen = false">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M18 6L6 18M6 6l12 12"/></svg>
          </button>
        </div>

        <div v-if="cartStore.totalItems === 0" class="cd-empty">
          <span class="cd-empty-icon">🛒</span>
          <p class="cd-empty-t">Carrinho vazio</p>
          <p class="cd-empty-s">Adicione produtos para continuar</p>
        </div>

        <div v-else class="cd-body">
          <div class="cd-list">
            <div v-for="item in cartStore.cartItems" :key="item.id" class="cd-item">
              <img :src="item.imagem || fallbackImg(item.nome, '56x56')" class="cd-item-img" :alt="item.nome"/>
              <div class="cd-item-info">
                <p class="cd-item-name">{{ item.nome }}</p>
                <p class="cd-item-price">{{ formatPrice(item.preco * item.quantity) }}</p>
              </div>
              <div class="cd-item-qty">
                <button class="cdq-btn" @click="cartStore.updateQuantity(item.id, -1)">−</button>
                <span>{{ item.quantity }}</span>
                <button class="cdq-btn" @click="cartStore.updateQuantity(item.id, 1)">+</button>
              </div>
            </div>
          </div>
          <div class="cd-foot">
            <div class="cd-total-row">
              <span>Total</span>
              <strong class="cd-total">{{ formatPrice(cartStore.subtotal) }}</strong>
            </div>
            <button class="cd-checkout" @click="goToCheckout">
              Finalizar compra
              <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M5 12h14M12 5l7 7-7 7"/></svg>
            </button>
            <p class="cd-secure">🔒 Pagamento 100% seguro</p>
          </div>
        </div>
      </div>

      <transition name="toast-pop">
        <div v-if="toast.show" class="store-toast">
          <div class="t-check">✓</div>
          <div class="t-text">
            <strong>{{ toast.name }}</strong>
            <span>adicionado</span>
          </div>
          <button class="t-view" @click="cartOpen = true; toast.show = false">Ver carrinho →</button>
        </div>
      </transition>

    </template>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useStoreStore } from '../stores/store'
import { useCartStore }  from '../stores/cart'

const route      = useRoute()
const router     = useRouter()
const storeStore = useStoreStore()
const cartStore  = useCartStore()

const search         = ref('')
const searchOpen     = ref(false)
const searchInput    = ref(null)
const cartOpen       = ref(false)
const isScrolled     = ref(false)
const sortBy         = ref('default')
const selectedProduct = ref(null)
const qty            = ref(1)
const toast          = ref({ show: false, name: '' })
let toastTimer       = null

const store = computed(() => storeStore.publicStore)

const heroProduct = computed(() => {
  const list = store.value?.products ?? []
  return list.find(p => p.imagem) ?? list[0] ?? null
})

const tickerProducts = computed(() => {
  const list = store.value?.products ?? []
  return [...list, ...list, ...list].slice(0, 30)
})

const storeTitle = computed(() => store.value?.storeName ?? 'Nossa Loja')
const firstWord  = computed(() => storeTitle.value.split(' ')[0])
const restWords  = computed(() => storeTitle.value.split(' ').slice(1).join(' ') || storeTitle.value)

const sortOpts = [
  { v: 'default', l: 'Padrão'      },
  { v: 'asc',     l: 'Menor preço' },
  { v: 'desc',    l: 'Maior preço' },
]

const filteredProducts = computed(() => {
  const list = store.value?.products ?? []
  if (!search.value) return list
  return list.filter(p => p.nome?.toLowerCase().includes(search.value.toLowerCase()))
})

const sortedProducts = computed(() => {
  const list = [...filteredProducts.value]
  if (sortBy.value === 'asc')  return list.sort((a,b) => a.preco - b.preco)
  if (sortBy.value === 'desc') return list.sort((a,b) => b.preco - a.preco)
  return list
})

function hexToRgb(hex) {
  const r = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex)
  return r ? `${parseInt(r[1],16)},${parseInt(r[2],16)},${parseInt(r[3],16)}` : '37,99,235'
}

function fallbackImg(name, size) {
  return `https://via.placeholder.com/${size}/EEF2FF/6366f1?text=${encodeURIComponent(name ?? 'P')}`
}

const formatPrice = v => Number(v||0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
const stockLabel  = s => s === 0 ? 'Esgotado' : s < 5 ? `Últimas ${s} unidades` : `${s} em estoque`
const stockClass  = s => s === 0 ? 'st-out' : s < 5 ? 'st-low' : 'st-ok'

function scrollToProducts() {
  document.getElementById('produtos')?.scrollIntoView({ behavior: 'smooth' })
}

async function toggleSearch() {
  searchOpen.value = !searchOpen.value
  if (searchOpen.value) {
    await nextTick()
    searchInput.value?.focus()
  }
}

function openModal(p)  { selectedProduct.value = p; qty.value = 1; document.body.style.overflow = 'hidden' }
function closeModal()  { selectedProduct.value = null; document.body.style.overflow = '' }

function showToast(name) {
  clearTimeout(toastTimer)
  toast.value = { show: true, name }
  toastTimer = setTimeout(() => toast.value.show = false, 3000)
}

function addToCart(p) {
  if (!p || p.estoque === 0) return
  cartStore.addItem({ id: p.id, nome: p.nome, preco: p.preco, imagem: p.imagem, storeSlug: store.value.slug })
  showToast(p.nome)
}

function addToCartQty(p) {
  if (!p || p.estoque === 0) return
  for (let i = 0; i < qty.value; i++) addToCart(p)
  closeModal()
}

function goToCheckout() {
  cartOpen.value = false
  router.push({ path: '/checkout', query: { store: store.value.slug } })
}

function onScroll() { isScrolled.value = window.scrollY > 60 }

onMounted(async () => {
  await storeStore.fetchPublicStore(route.params.slug)
  cartStore.loadCart()
  window.addEventListener('scroll', onScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', onScroll)
})
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Syne:wght@700;800&family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=DM+Serif+Display:ital@0;1&display=swap');

*, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

.store-root {
  font-family: 'Plus Jakarta Sans', sans-serif;
  background: var(--bg);
  min-height: 100vh;
  color: #0F172A;
  overflow-x: hidden;
}

.full-center { min-height: 100vh; display: flex; align-items: center; justify-content: center; }
.flex-col    { flex-direction: column; gap: 14px; text-align: center; }
.loader-ring { width: 44px; height: 44px; border: 3px solid #E2E8F0; border-top-color: var(--p); border-radius: 50%; animation: spin .7s linear infinite; }
@keyframes spin { to { transform: rotate(360deg); } }
.nf-emoji { font-size: 56px; }
.nf-title { font-family: 'Syne', sans-serif; font-size: 24px; }
.nf-sub   { font-size: 14px; color: #64748B; }
.nf-btn   { padding: 10px 22px; background: var(--p); color: white; border-radius: 10px; text-decoration: none; font-weight: 700; font-size: 14px; }

.floatnav {
  position: fixed;
  top: 16px; left: 50%;
  transform: translateX(-50%);
  z-index: 999;
  width: calc(100% - 48px);
  max-width: 1180px;
  background: rgba(255,255,255,.08);
  border: 1px solid rgba(255,255,255,.15);
  backdrop-filter: blur(20px) saturate(180%);
  border-radius: 18px;
  transition: background .3s, border-color .3s, box-shadow .3s;
}

.floatnav.scrolled {
  background: rgba(255,255,255,.92);
  border-color: rgba(0,0,0,.06);
  box-shadow: 0 8px 32px rgba(0,0,0,.1);
}

.fn-inner {
  display: flex; align-items: center; gap: 16px;
  padding: 12px 20px;
}

.fn-brand { display: flex; align-items: center; gap: 10px; flex-shrink: 0; }

.fn-logo {
  width: 34px; height: 34px;
  border-radius: 9px;
  background: var(--p);
  display: flex; align-items: center; justify-content: center;
  color: white; overflow: hidden; flex-shrink: 0;
}
.fn-logo-img { width: 100%; height: 100%; object-fit: cover; }

.fn-name {
  font-family: 'Syne', sans-serif;
  font-size: 16px; font-weight: 800;
  color: white; letter-spacing: -.01em;
  transition: color .3s;
}
.floatnav.scrolled .fn-name { color: #0F172A; }

.fn-search {
  flex: 0 0 0;
  overflow: hidden;
  display: flex; align-items: center; gap: 8px;
  background: rgba(255,255,255,.12);
  border: 1px solid rgba(255,255,255,.18);
  border-radius: 50px;
  padding: 0;
  height: 38px;
  width: 0;
  opacity: 0;
  transition: width .3s, opacity .3s, padding .3s;
}
.fn-search.open {
  flex: 0 0 240px; width: 240px;
  opacity: 1; padding: 0 14px;
}
.floatnav.scrolled .fn-search {
  background: rgba(0,0,0,.04);
  border-color: #E2E8F0;
}
.fn-search svg { color: rgba(255,255,255,.5); flex-shrink: 0; }
.floatnav.scrolled .fn-search svg { color: #94A3B8; }
.fn-search-input {
  background: transparent; border: none; outline: none;
  font: 500 14px 'Plus Jakarta Sans', sans-serif;
  color: white; width: 100%;
}
.floatnav.scrolled .fn-search-input { color: #0F172A; }
.fn-search-input::placeholder { color: rgba(255,255,255,.4); }
.floatnav.scrolled .fn-search-input::placeholder { color: #94A3B8; }

.fn-right { display: flex; align-items: center; gap: 8px; margin-left: auto; }

.fn-search-toggle,
.fn-cart {
  position: relative;
  width: 38px; height: 38px; border-radius: 50%;
  background: rgba(255,255,255,.1);
  border: 1px solid rgba(255,255,255,.15);
  color: white; cursor: pointer;
  display: flex; align-items: center; justify-content: center;
  transition: background .2s;
}
.fn-search-toggle:hover, .fn-cart:hover { background: rgba(255,255,255,.2); }
.floatnav.scrolled .fn-search-toggle,
.floatnav.scrolled .fn-cart {
  background: #F8FAFC; border-color: #E2E8F0; color: #0F172A;
}
.floatnav.scrolled .fn-search-toggle:hover,
.floatnav.scrolled .fn-cart:hover { background: #EFF6FF; }

.fn-cart-badge {
  position: absolute; top: -3px; right: -3px;
  width: 16px; height: 16px; border-radius: 50%;
  background: #EF4444; border: 2px solid white;
  font-size: 8px; font-weight: 800; color: white;
  display: flex; align-items: center; justify-content: center;
}


.hero {
  display: grid;
  grid-template-columns: 55% 45%;
  min-height: 100vh;
  position: relative;
  overflow: hidden;
}

.hero-visual {
  position: relative;
  overflow: hidden;
  background: #0B0F1A;
}

.hero-img-wrap {
  position: absolute; inset: 0;
}

.hero-img {
  width: 100%; height: 100%;
  object-fit: cover; display: block;
  transform-origin: center;
  animation: heroImgIn 1.2s cubic-bezier(.16,1,.3,1) both;
}
@keyframes heroImgIn {
  from { transform: scale(1.08); opacity: 0; }
  to   { transform: scale(1);    opacity: 1; }
}

.hero-img-placeholder {
  width: 100%; height: 100%;
  display: flex; align-items: center; justify-content: center;
  background: linear-gradient(135deg, #0B0F1A, #1e293b);
}

.hero-img-vignette {
  position: absolute; inset: 0;
  background: linear-gradient(
      to right,
      transparent 40%,
      rgba(8,12,24,.6) 75%,
      rgba(8,12,24,.95) 100%
  );
  pointer-events: none;
}

.hero-product-badge {
  position: absolute; bottom: 100px; left: 28px;
  display: flex; align-items: center; gap: 12px;
  background: rgba(255,255,255,.1);
  border: 1px solid rgba(255,255,255,.18);
  backdrop-filter: blur(16px);
  border-radius: 16px; padding: 10px 14px;
  cursor: pointer;
  animation: badgeIn .8s .6s cubic-bezier(.16,1,.3,1) both;
  transition: background .2s, transform .2s;
  max-width: 280px;
}
.hero-product-badge:hover { background: rgba(255,255,255,.18); transform: translateY(-2px); }
@keyframes badgeIn { from{opacity:0;transform:translateX(-16px)} to{opacity:1;transform:translateX(0)} }

.hpb-img-wrap { width: 44px; height: 44px; border-radius: 10px; overflow: hidden; flex-shrink: 0; background: rgba(255,255,255,.1); }
.hpb-img      { width: 100%; height: 100%; object-fit: cover; }
.hpb-info     { display: flex; flex-direction: column; gap: 1px; flex: 1; min-width: 0; }
.hpb-label    { font-size: 9.5px; font-weight: 700; text-transform: uppercase; letter-spacing: .1em; color: var(--s); }
.hpb-name     { font-size: 13px; font-weight: 700; color: white; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.hpb-price    { font-size: 13px; font-weight: 800; color: rgba(255,255,255,.7); }
.hpb-arrow    { color: rgba(255,255,255,.5); flex-shrink: 0; }


.hero-chip {
  position: absolute;
  display: flex; align-items: center; gap: 7px;
  background: rgba(255,255,255,.1);
  border: 1px solid rgba(255,255,255,.15);
  backdrop-filter: blur(12px);
  border-radius: 50px; padding: 6px 14px;
  font-size: 12px; font-weight: 700; color: white;
  animation: badgeIn .8s ease both;
}
.hero-chip-1 { top: 120px; left: 28px; animation-delay: .8s; }
.hero-chip-2 { top: 172px; left: 28px; animation-delay: .95s; }
.chip-num    { font-family: 'Syne', sans-serif; font-size: 14px; font-weight: 800; color: var(--s); }
.chip-lbl    { color: rgba(255,255,255,.7); font-size: 11.5px; font-weight: 600; }

.hero-copy {
  position: relative;
  background: #070B16;
  display: flex; flex-direction: column; justify-content: center;
  padding: 120px 52px 100px 48px;
  overflow: hidden;
}

.hero-deco-line {
  position: absolute; top: 0; left: 0; bottom: 0;
  width: 3px;
  background: linear-gradient(to bottom, transparent, var(--p), var(--s), transparent);
  opacity: .6;
}

.hero-copy::before {
  content: '';
  position: absolute; inset: 0; pointer-events: none;
  background:
      radial-gradient(ellipse 80% 60% at 100% 80%, color-mix(in srgb, var(--p) 18%, transparent), transparent 70%),
      radial-gradient(ellipse 60% 50% at 20% 10%,  color-mix(in srgb, var(--s) 12%, transparent), transparent 70%);
}

.hero-eyebrow {
  display: inline-flex; align-items: center; gap: 8px;
  font-size: 10px; font-weight: 700; text-transform: uppercase; letter-spacing: .16em;
  color: var(--s); margin-bottom: 24px; width: fit-content;
  animation: copyIn .7s .1s ease both;
}
.eyebrow-dot {
  width: 6px; height: 6px; border-radius: 50%;
  background: var(--s);
  box-shadow: 0 0 8px var(--s);
  animation: dotBlink 2s ease-in-out infinite;
}
@keyframes dotBlink { 0%,100%{opacity:1} 50%{opacity:.3} }

.hero-title {
  display: flex; flex-direction: column;
  margin-bottom: 24px;
  animation: copyIn .7s .2s ease both;
}
.ht-line {
  font-family: 'Syne', sans-serif;
  font-size: clamp(36px, 4vw, 58px);
  font-weight: 800;
  line-height: 1.0;
  letter-spacing: -.03em;
  color: white;
  display: block;
}
.ht-line-2 em {
  font-style: italic;
  font-family: 'DM Serif Display', serif;
  font-weight: 400;
  color: transparent;
  background: linear-gradient(135deg, var(--p), var(--s));
  -webkit-background-clip: text;
  background-clip: text;
}

.hero-desc {
  font-size: 15px; color: rgba(255,255,255,.5);
  line-height: 1.72; max-width: 380px;
  margin-bottom: 36px;
  animation: copyIn .7s .3s ease both;
}

.hero-ctas {
  display: flex; gap: 12px; flex-wrap: wrap;
  margin-bottom: 40px;
  animation: copyIn .7s .4s ease both;
}

.hcta-primary {
  display: inline-flex; align-items: center; gap: 10px;
  padding: 14px 28px; background: var(--p); color: white;
  border: none; border-radius: 12px;
  font: 700 15px 'Plus Jakarta Sans', sans-serif; cursor: pointer;
  box-shadow: 0 4px 24px rgba(var(--p-rgb), .4);
  transition: transform .15s, box-shadow .2s, opacity .15s;
}
.hcta-primary:hover { opacity: .9; transform: translateY(-2px); box-shadow: 0 8px 32px rgba(var(--p-rgb), .5); }

.hcta-ghost {
  display: inline-flex; align-items: center; gap: 8px;
  padding: 14px 22px;
  background: rgba(255,255,255,.07);
  border: 1px solid rgba(255,255,255,.12);
  color: rgba(255,255,255,.75); border-radius: 12px;
  font: 600 15px 'Plus Jakarta Sans', sans-serif; cursor: pointer;
  backdrop-filter: blur(6px);
  transition: all .2s;
}
.hcta-ghost:hover { background: rgba(255,255,255,.14); border-color: rgba(255,255,255,.25); }
.hcta-badge {
  background: var(--p); color: white; border-radius: 50px;
  padding: 1px 7px; font-size: 11px; font-weight: 800;
}

.hero-trust {
  display: flex; align-items: center; gap: 16px; flex-wrap: wrap;
  animation: copyIn .7s .5s ease both;
}
.trust-item { display: flex; align-items: center; gap: 6px; font-size: 12px; color: rgba(255,255,255,.4); font-weight: 600; }
.trust-item svg { color: var(--s); }
.trust-sep  { width: 1px; height: 18px; background: rgba(255,255,255,.1); }

.hero-powered {
  position: absolute; bottom: 28px; left: 48px;
  font-size: 10.5px; color: rgba(255,255,255,.2); font-weight: 600;
  animation: copyIn .7s .6s ease both;
}
.hero-powered strong { color: rgba(255,255,255,.35); }

@keyframes copyIn { from{opacity:0;transform:translateX(20px)} to{opacity:1;transform:translateX(0)} }

.hero-ticker {
  grid-column: 1 / -1;
  position: absolute; bottom: 0; left: 0; right: 0;
  height: 44px;
  background: var(--p);
  overflow: hidden;
  display: flex; align-items: center;
}

.ticker-track {
  display: flex; gap: 0;
  white-space: nowrap;
  animation: ticker 40s linear infinite;
}
@keyframes ticker { from{transform:translateX(0)} to{transform:translateX(-50%)} }

.ticker-item {
  display: inline-flex; align-items: center; gap: 10px;
  padding: 0 28px;
  font-size: 12.5px; font-weight: 700; color: rgba(255,255,255,.85);
  border-right: 1px solid rgba(255,255,255,.15);
}
.ticker-dot   { width: 5px; height: 5px; border-radius: 50%; background: rgba(255,255,255,.5); flex-shrink: 0; }
.ticker-price { font-weight: 800; color: white; }

.store-main {
  max-width: 1280px; margin: 0 auto;
  padding: 60px 48px 80px;
}

.main-header {
  display: flex; align-items: flex-end; justify-content: space-between;
  margin-bottom: 36px; flex-wrap: wrap; gap: 16px;
}
.main-title { font-family: 'Syne', sans-serif; font-size: 28px; font-weight: 800; color: #0F172A; letter-spacing: -.03em; }
.main-sub   { font-size: 13px; color: #94A3B8; font-weight: 600; margin-top: 3px; }
.sort-row   { display: flex; gap: 4px; }
.sort-pill  { padding: 7px 15px; background: white; border: 1.5px solid #E2E8F0; border-radius: 50px; font: 600 12.5px 'Plus Jakarta Sans', sans-serif; color: #64748B; cursor: pointer; transition: all .15s; }
.sort-pill:hover  { border-color: color-mix(in srgb, var(--p) 40%, transparent); color: var(--p); }
.sort-pill.active { background: var(--p); color: white; border-color: var(--p); }

.empty-wrap { display: flex; flex-direction: column; align-items: center; gap: 10px; padding: 80px 0; text-align: center; }
.empty-emoji { font-size: 52px; }
.empty-wrap h3 { font-family: 'Syne', sans-serif; font-size: 22px; font-weight: 800; }
.empty-wrap p  { font-size: 14px; color: #94A3B8; }
.empty-reset   { padding: 10px 20px; background: var(--p); color: white; border: none; border-radius: 10px; font: 600 13px 'Plus Jakarta Sans', sans-serif; cursor: pointer; margin-top: 4px; }

.pGrid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 20px;
}

.pCard {
  background: white; border: 1.5px solid #E2E8F0;
  border-radius: 18px; overflow: hidden; cursor: pointer;
  animation: fadeUp .45s ease both;
  transition: transform .25s, box-shadow .25s, border-color .25s;
}
.pCard:hover { transform: translateY(-6px); box-shadow: 0 16px 40px rgba(0,0,0,.08); border-color: color-mix(in srgb, var(--p) 30%, transparent); }
@keyframes fadeUp { from{opacity:0;transform:translateY(18px)} to{opacity:1;transform:translateY(0)} }

.pCard-media { position: relative; aspect-ratio: 4/3; overflow: hidden; background: #EEF2FF; }
.pCard-img   { width: 100%; height: 100%; object-fit: cover; display: block; transition: transform .5s; }
.pCard:hover .pCard-img { transform: scale(1.05); }

.pCard-actions {
  position: absolute; inset: 0;
  display: flex; align-items: flex-end; gap: 8px; padding: 12px;
  background: linear-gradient(transparent 40%, rgba(0,0,0,.5));
  opacity: 0; transition: opacity .2s;
}
.pCard:hover .pCard-actions { opacity: 1; }

.pca-add {
  flex: 1; display: flex; align-items: center; justify-content: center; gap: 7px;
  height: 38px; background: white; border: none; border-radius: 9px;
  font: 700 13px 'Plus Jakarta Sans', sans-serif; color: #0F172A; cursor: pointer;
  transition: background .15s;
}
.pca-add:hover    { background: #EEF2FF; }
.pca-add:disabled { opacity: .5; cursor: not-allowed; }
.pca-view {
  width: 38px; height: 38px; background: rgba(255,255,255,.15);
  border: 1px solid rgba(255,255,255,.2); border-radius: 9px;
  color: white; cursor: pointer; display: flex; align-items: center; justify-content: center;
  backdrop-filter: blur(4px); transition: background .15s;
}
.pca-view:hover { background: rgba(255,255,255,.3); }

.pBadge {
  position: absolute; top: 10px; left: 10px;
  padding: 4px 10px; border-radius: 50px; font-size: 10.5px; font-weight: 700; text-transform: uppercase; letter-spacing: .04em;
}
.pBadge-out { background: rgba(220,38,38,.9); color: white; }
.pBadge-low { background: rgba(234,88,12,.9);  color: white; }

.pCard-info { padding: 14px 16px 16px; }
.pCard-cat  { font-size: 10.5px; font-weight: 700; text-transform: uppercase; letter-spacing: .1em; color: var(--p); margin-bottom: 5px; }
.pCard-name { font-family: 'DM Serif Display', serif; font-size: 16px; color: #0F172A; line-height: 1.3; margin-bottom: 14px; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.pCard-foot { display: flex; align-items: flex-end; justify-content: space-between; gap: 8px; }
.pCard-price   { font-size: 18px; font-weight: 800; color: var(--p); letter-spacing: -.02em; line-height: 1; }
.pCard-install { font-size: 10.5px; color: #94A3B8; font-weight: 500; margin-top: 1px; }
.pCard-plus {
  width: 34px; height: 34px; flex-shrink: 0;
  background: var(--p); color: white; border: none; border-radius: 9px;
  display: flex; align-items: center; justify-content: center; cursor: pointer;
  box-shadow: 0 3px 12px rgba(var(--p-rgb), .3);
  transition: opacity .15s, transform .1s;
}
.pCard-plus:hover   { opacity: .88; transform: scale(1.07); }
.pCard-plus.disabled { background: #E2E8F0; box-shadow: none; cursor: not-allowed; }


.modal-back { position: fixed; inset: 0; z-index: 9000; background: rgba(6,12,26,.7); backdrop-filter: blur(14px); display: flex; align-items: center; justify-content: center; padding: 24px; }
.modal-box  { background: white; border-radius: 22px; width: 100%; max-width: 880px; max-height: 88vh; overflow: hidden; position: relative; box-shadow: 0 40px 80px rgba(0,0,0,.3); display: grid; grid-template-columns: 1fr 1fr; animation: modalIn .32s cubic-bezier(.16,1,.3,1); }
@keyframes modalIn { from{opacity:0;transform:scale(.93) translateY(20px)} to{opacity:1;transform:scale(1) translateY(0)} }
.modal-close { position: absolute; top: 16px; right: 16px; z-index: 10; width: 34px; height: 34px; border-radius: 50%; background: white; border: 1.5px solid #E2E8F0; color: #64748B; display: flex; align-items: center; justify-content: center; cursor: pointer; box-shadow: 0 2px 8px rgba(0,0,0,.08); transition: all .15s; }
.modal-close:hover { color: #0F172A; box-shadow: 0 4px 12px rgba(0,0,0,.1); }
.modal-img-col    { background: #EEF2FF; position: relative; }
.modal-img        { width: 100%; height: 100%; object-fit: cover; display: block; min-height: 360px; }
.modal-out-tag    { position: absolute; top: 14px; left: 14px; padding: 5px 12px; border-radius: 50px; background: rgba(220,38,38,.9); color: white; font-size: 11px; font-weight: 700; }
.modal-info-col   { padding: 40px 36px; display: flex; flex-direction: column; gap: 16px; overflow-y: auto; }
.mi-cat    { font-size: 10.5px; font-weight: 700; text-transform: uppercase; letter-spacing: .12em; color: var(--p); }
.mi-name   { font-family: 'DM Serif Display', serif; font-size: 28px; color: #0F172A; letter-spacing: -.03em; line-height: 1.1; }
.mi-desc   { font-size: 14px; color: #64748B; line-height: 1.7; }
.mi-price-block { padding-bottom: 16px; border-bottom: 1.5px solid #E2E8F0; }
.mi-price  { font-size: 32px; font-weight: 800; color: var(--p); letter-spacing: -.04em; display: block; }
.mi-install{ font-size: 12.5px; color: #94A3B8; font-weight: 500; margin-top: 3px; display: block; }
.mi-stock  { display: flex; align-items: center; gap: 7px; font-size: 13px; font-weight: 700; }
.stock-dot { width: 8px; height: 8px; border-radius: 50%; background: currentColor; flex-shrink: 0; }
.st-ok  { color: #16a34a; }
.st-low { color: #ea580c; }
.st-out { color: #dc2626; }
.mi-qty-row { display: flex; }
.mi-qty     { display: flex; align-items: center; border: 1.5px solid #E2E8F0; border-radius: 50px; overflow: hidden; }
.mi-qty-btn { width: 40px; height: 40px; background: transparent; border: none; font-size: 18px; color: var(--p); cursor: pointer; display: flex; align-items: center; justify-content: center; transition: background .15s; }
.mi-qty-btn:hover { background: #EEF2FF; }
.mi-qty-val { min-width: 36px; text-align: center; font: 700 15px 'Plus Jakarta Sans', sans-serif; }
.mi-add { display: flex; align-items: center; justify-content: center; gap: 10px; width: 100%; height: 52px; margin-top: auto; background: var(--p); color: white; border: none; border-radius: 13px; font: 700 15px 'Plus Jakarta Sans', sans-serif; cursor: pointer; box-shadow: 0 4px 20px rgba(var(--p-rgb), .3); transition: opacity .15s, transform .1s; }
.mi-add:hover    { opacity: .9; transform: translateY(-1px); }
.mi-add.disabled { background: #E2E8F0; color: #94A3B8; box-shadow: none; cursor: not-allowed; transform: none; }



.cart-back { position: fixed; inset: 0; z-index: 8000; background: rgba(6,12,26,.5); backdrop-filter: blur(4px); }
.cart-drawer { position: fixed; top: 0; right: 0; bottom: 0; width: 380px; z-index: 8001; background: white; display: flex; flex-direction: column; transform: translateX(100%); transition: transform .32s cubic-bezier(.16,1,.3,1); box-shadow: -20px 0 60px rgba(0,0,0,.15); }
.cart-drawer.open { transform: translateX(0); }
.cd-head { display: flex; align-items: center; justify-content: space-between; padding: 20px 24px; border-bottom: 1.5px solid #E2E8F0; flex-shrink: 0; }
.cd-title { font-family: 'Syne', sans-serif; font-size: 20px; font-weight: 800; color: #0F172A; }
.cd-close { width: 32px; height: 32px; border-radius: 50%; background: #F8FAFC; border: 1px solid #E2E8F0; color: #64748B; display: flex; align-items: center; justify-content: center; cursor: pointer; transition: all .15s; }
.cd-close:hover { background: #F1F5F9; color: #0F172A; }
.cd-empty { flex: 1; display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 8px; }
.cd-empty-icon { font-size: 48px; }
.cd-empty-t { font-size: 16px; font-weight: 700; }
.cd-empty-s { font-size: 13px; color: #94A3B8; }
.cd-body    { flex: 1; display: flex; flex-direction: column; overflow: hidden; }
.cd-list    { flex: 1; overflow-y: auto; padding: 16px 24px; display: flex; flex-direction: column; gap: 10px; }
.cd-item    { display: flex; align-items: center; gap: 12px; padding: 12px; background: #F8FAFC; border: 1px solid #E2E8F0; border-radius: 12px; }
.cd-item-img  { width: 50px; height: 50px; border-radius: 8px; object-fit: cover; flex-shrink: 0; background: #EEF2FF; }
.cd-item-info { flex: 1; min-width: 0; }
.cd-item-name { font-size: 13px; font-weight: 700; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.cd-item-price{ font-size: 12.5px; font-weight: 800; color: var(--p); margin-top: 2px; }
.cd-item-qty  { display: flex; align-items: center; gap: 8px; }
.cdq-btn      { width: 28px; height: 28px; background: white; border: 1px solid #E2E8F0; border-radius: 7px; cursor: pointer; font-size: 15px; display: flex; align-items: center; justify-content: center; color: #64748B; transition: all .15s; }
.cdq-btn:hover { background: #EEF2FF; color: var(--p); }
.cd-item-qty span { font-size: 13px; font-weight: 700; min-width: 16px; text-align: center; }
.cd-foot  { padding: 20px 24px; border-top: 1.5px solid #E2E8F0; flex-shrink: 0; }
.cd-total-row { display: flex; justify-content: space-between; align-items: baseline; margin-bottom: 16px; font-size: 14px; color: #64748B; font-weight: 600; }
.cd-total  { font-family: 'Syne', sans-serif; font-size: 26px; color: #0F172A; font-weight: 800; letter-spacing: -.03em; }
.cd-checkout { display: flex; align-items: center; justify-content: center; gap: 8px; width: 100%; height: 50px; background: var(--p); color: white; border: none; border-radius: 13px; font: 700 15px 'Plus Jakarta Sans', sans-serif; cursor: pointer; box-shadow: 0 4px 16px rgba(var(--p-rgb), .3); margin-bottom: 10px; transition: opacity .15s, transform .1s; }
.cd-checkout:hover { opacity: .9; transform: translateY(-1px); }
.cd-secure { font-size: 11.5px; color: #94A3B8; text-align: center; font-weight: 500; }

.store-toast { position: fixed; bottom: 28px; left: 50%; transform: translateX(-50%); z-index: 9999; display: flex; align-items: center; gap: 12px; background: #0F172A; color: white; padding: 12px 16px 12px 12px; border-radius: 16px; box-shadow: 0 8px 28px rgba(0,0,0,.25); border: 1px solid rgba(255,255,255,.07); white-space: nowrap; }
.t-check { width: 30px; height: 30px; border-radius: 50%; background: var(--p); color: white; display: flex; align-items: center; justify-content: center; font-size: 13px; font-weight: 800; flex-shrink: 0; }
.t-text  { display: flex; align-items: center; gap: 5px; font-size: 13px; }
.t-text strong { font-weight: 700; max-width: 150px; overflow: hidden; text-overflow: ellipsis; }
.t-text span   { color: rgba(255,255,255,.5); }
.t-view  { padding: 6px 13px; background: rgba(255,255,255,.1); border: 1px solid rgba(255,255,255,.15); border-radius: 8px; color: white; font: 700 12px 'Plus Jakarta Sans', sans-serif; cursor: pointer; transition: background .15s; }
.t-view:hover { background: rgba(255,255,255,.2); }

@media (max-width: 1024px) {
  .hero              { grid-template-columns: 1fr; }
  .hero-visual       { min-height: 60vw; }
  .hero-img-vignette { background: linear-gradient(to bottom, transparent 50%, rgba(7,11,22,.98) 100%); }
  .hero-copy         { padding: 40px 32px 80px; }
  .hero-copy::before { display: none; }
  .hero-deco-line    { display: none; }
}
@media (max-width: 768px) {
  .floatnav          { width: calc(100% - 32px); top: 10px; }
  .store-main        { padding: 40px 20px 60px; }
  .modal-box         { grid-template-columns: 1fr; }
  .modal-img-col     { min-height: 240px; }
  .cart-drawer       { width: 100%; }
  .hero-copy         { padding: 32px 20px 60px; }
  .hero-chip-1,
  .hero-chip-2       { display: none; }
  .hero-product-badge{ bottom: 60px; left: 16px; right: 16px; max-width: none; }
}
</style>
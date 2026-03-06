<template>
  <section class="showcase-root py-12 py-md-16">
    <v-container>
      <div class="showcase-header text-center mb-12">
        <span class="showcase-eyebrow">Destaques</span>
        <h2 class="showcase-title">Produtos em Destaque</h2>
        <p class="showcase-subtitle">Os melhores produtos selecionados especialmente para você</p>
      </div>

      <div v-if="productStore.loading" class="text-center py-16">
        <v-progress-circular indeterminate color="#2563EB" size="64" width="4" />
      </div>

      <v-row v-else-if="featuredProducts.length > 0">
        <v-col
            v-for="(product, i) in featuredProducts"
            :key="product.id"
            cols="12"
            sm="6"
            md="4"
            lg="3"
        >
          <div class="pcard" :style="{ animationDelay: `${i * 40}ms` }" @click="viewProduct(product)">
            <div class="pcard-img-wrap">
              <img
                  :src="product.imagem || `https://via.placeholder.com/400x500/EFF6FF/93C5FD?text=${encodeURIComponent(product.nome)}`"
                  :alt="product.nome"
                  class="pcard-img"
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
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M6 2 3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"/><line x1="3" y1="6" x2="21" y2="6"/><path d="M16 10a4 4 0 0 1-8 0"/></svg>
                  {{ product.estoque === 0 ? 'Esgotado' : 'Adicionar' }}
                </button>
                <button class="pcard-detail-btn" @click.stop="viewProduct(product)">
                  <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>
                </button>
              </div>
            </div>
            <div class="pcard-info">
              <span class="pcard-cat">{{ product.categoria }}</span>
              <h3 class="pcard-name">{{ product.nome }}</h3>
              <p class="pcard-desc">{{ product.descricao }}</p>
              <div class="pcard-bottom">
                <span class="pcard-price">{{ formatCurrency(product.preco) }}</span>
                <span class="pcard-stock" :class="getStockClass(product.estoque)">{{ getStockLabel(product.estoque) }}</span>
              </div>
            </div>
          </div>
        </v-col>
      </v-row>

      <div v-else class="cat-empty text-center py-16">
        <div class="cat-empty-icon mx-auto mb-4">
          <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1"><path d="M6 2L3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"/><line x1="3" y1="6" x2="21" y2="6"/><path d="M16 10a4 4 0 0 1-8 0"/></svg>
        </div>
        <h3 class="cat-empty-title">Nenhum produto em destaque no momento</h3>
      </div>

      <div v-if="featuredProducts.length > 0" class="text-center mt-12">
        <button class="btn-view-all" @click="$router.push('/produtos')">
          Ver Todos os Produtos
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M5 12h14M12 5l7 7-7 7"/></svg>
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
  return value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
}

const getStockLabel = s => s === 0 ? 'Esgotado' : s < 5 ? `${s} restantes` : `${s} em estoque`
const getStockClass = s => s === 0 ? 'stock--out' : s < 5 ? 'stock--low' : 'stock--ok'

const viewProduct = (product) => {
  console.log('Ver produto:', product)
}

const addToCart = (product) => {
  if (!authStore.isLogged) {
    if (confirm('Você precisa estar logado para adicionar itens ao carrinho. Deseja fazer login?')) {}
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
@import url('https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:ital,wght@0,300;0,400;0,500;0,600;0,700;1,400&family=DM+Serif+Display:ital@0;1&display=swap');

*, *::before, *::after { box-sizing: border-box; }

.showcase-root {
  --blue-50:    #EFF6FF;
  --blue-100:   #DBEAFE;
  --blue-200:   #BFDBFE;
  --blue-600:   #2563EB;
  --blue-700:   #1D4ED8;

  --c-bg:       #F8FAFF;
  --c-surface:  #FFFFFF;
  --c-border:   #E2EBFF;
  --c-text:     #0F172A;
  --c-text-2:   #475569;
  --c-text-3:   #94A3B8;
  --c-accent:   var(--blue-600);

  --radius:     12px;
  --radius-lg:  20px;
  --shadow-md:  0 4px 16px rgba(37,99,235,.12), 0 2px 6px rgba(0,0,0,.06);

  background: var(--c-bg);
  font-family: 'Plus Jakarta Sans', sans-serif;
}

/* HEADER */
.showcase-header { padding-bottom: 4px; }

.showcase-eyebrow {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-size: 11px;
  font-weight: 700;
  letter-spacing: .18em;
  text-transform: uppercase;
  color: var(--c-accent);
  margin-bottom: 12px;
}

.showcase-eyebrow::before,
.showcase-eyebrow::after {
  content: '';
  display: block;
  width: 24px;
  height: 2px;
  background: var(--blue-200);
  border-radius: 2px;
}

.showcase-title {
  font-family: 'DM Serif Display', serif;
  font-size: clamp(26px, 3vw, 40px);
  font-weight: 400;
  color: var(--c-text);
  letter-spacing: -.03em;
  margin-bottom: 10px;
  line-height: 1.15;
}

.showcase-subtitle {
  font-size: 15px;
  color: var(--c-text-3);
  font-weight: 500;
}

/* CARD */
.pcard {
  background: var(--c-surface);
  border: 1.5px solid var(--c-border);
  border-radius: var(--radius-lg);
  overflow: hidden;
  cursor: pointer;
  height: 100%;
  display: flex;
  flex-direction: column;
  animation: fadeUp .45s ease both;
  transition: box-shadow .25s, transform .25s, border-color .25s;
}

.pcard:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-5px);
  border-color: var(--blue-200);
}

@keyframes fadeUp {
  from { opacity: 0; transform: translateY(18px); }
  to   { opacity: 1; transform: translateY(0); }
}

/* IMAGE */
.pcard-img-wrap {
  position: relative;
  aspect-ratio: 4/3;
  overflow: hidden;
  background: var(--blue-50);
  flex-shrink: 0;
}

.pcard-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  transition: transform .5s cubic-bezier(.4,0,.2,1);
}

.pcard:hover .pcard-img { transform: scale(1.06); }

/* BADGES */
.pcard-badges {
  position: absolute;
  top: 10px; left: 10px;
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

.pbadge--star { background: rgba(37,99,235,.9);  color: white; }
.pbadge--out  { background: rgba(220,38,38,.9);   color: white; }
.pbadge--low  { background: rgba(234,88,12,.9);   color: white; }

/* HOVER ACTIONS */
.pcard-actions {
  position: absolute;
  bottom: 0; left: 0; right: 0;
  padding: 14px;
  display: flex;
  gap: 8px;
  background: linear-gradient(transparent, rgba(15,23,42,.5));
  transform: translateY(100%);
  transition: transform .28s cubic-bezier(.4,0,.2,1);
}

.pcard:hover .pcard-actions { transform: translateY(0); }

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

.pcard-cart-btn:hover { background: var(--blue-700); }
.pcard-cart-btn:active { transform: scale(.97); }
.pcard-cart-btn:disabled {
  background: rgba(255,255,255,.25);
  color: rgba(255,255,255,.6);
  cursor: not-allowed;
}

.pcard-detail-btn {
  width: 40px; height: 40px;
  background: rgba(255,255,255,.15);
  border: 1.5px solid rgba(255,255,255,.25);
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

.pcard-detail-btn:hover { background: rgba(255,255,255,.28); }

/* INFO */
.pcard-info {
  padding: 14px 16px 18px;
  display: flex;
  flex-direction: column;
  flex: 1;
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
  font-size: 17px;
  font-weight: 400;
  letter-spacing: -.01em;
  margin: 5px 0 6px;
  line-height: 1.35;
  color: var(--c-text);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.pcard-desc {
  font-size: 13px;
  color: var(--c-text-3);
  line-height: 1.55;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin-bottom: 12px;
  font-weight: 400;
}

.pcard-bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: auto;
}

.pcard-price {
  font-size: 19px;
  font-weight: 800;
  letter-spacing: -.03em;
  color: var(--blue-700);
}

.pcard-stock {
  font-size: 11px;
  font-weight: 700;
  padding: 3px 9px;
  border-radius: 50px;
}

.stock--ok  { background: #DCFCE7; color: #166534; }
.stock--low { background: #FEF3C7; color: #92400E; }
.stock--out { background: #FEE2E2; color: #991B1B; }

/* EMPTY */
.cat-empty { color: var(--c-text-3); }

.cat-empty-icon {
  width: 90px; height: 90px;
  background: var(--blue-50);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--blue-200);
}

.cat-empty-title {
  font-family: 'DM Serif Display', serif;
  font-size: 20px;
  font-weight: 400;
  color: var(--c-text-2);
  letter-spacing: -.02em;
}

/* VIEW ALL */
.btn-view-all {
  padding: 13px 32px;
  background: var(--c-surface);
  color: var(--c-accent);
  border: 2px solid var(--blue-200);
  border-radius: 50px;
  font: 700 14px 'Plus Jakarta Sans', sans-serif;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  transition: all .2s ease;
  letter-spacing: .01em;
}

.btn-view-all:hover {
  background: var(--blue-600);
  color: white;
  border-color: var(--blue-600);
  box-shadow: 0 8px 20px -6px rgba(37,99,235,.4);
  transform: translateY(-2px);
}

.btn-view-all:active { transform: translateY(0); }
</style>
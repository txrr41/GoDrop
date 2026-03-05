<template>
  <div :style="{ backgroundColor: store?.backgroundColor ?? '#F8FAFC', minHeight: '100vh' }">

    <div v-if="storeStore.loading" class="d-flex align-center justify-center" style="min-height:100vh">
      <v-progress-circular indeterminate color="primary" size="64"/>
    </div>

    <div v-else-if="!store" class="d-flex align-center justify-center flex-column" style="min-height:100vh">
      <v-icon size="80" color="medium-emphasis">mdi-store-off-outline</v-icon>
      <h2 class="text-h5 mt-4">Loja não encontrada</h2>
      <p class="text-medium-emphasis">Esta loja pode estar offline ou não existe</p>
      <v-btn class="mt-4" href="/" variant="outlined">Voltar ao início</v-btn>
    </div>

    <template v-else>
      <header :style="{
        background: `linear-gradient(135deg, ${store.primaryColor}, ${store.secondaryColor})`,
        padding: '60px 24px',
        textAlign: 'center'
      }">
        <v-avatar v-if="store.logoUrl" size="80" class="mb-4 elevation-4">
          <v-img :src="store.logoUrl"/>
        </v-avatar>
        <v-icon v-else size="80" color="white" class="mb-4">mdi-store</v-icon>

        <h1 class="text-h3 font-weight-bold text-white">{{ store.storeName }}</h1>
        <p v-if="store.slogan" class="text-white text-h6 mt-2" style="opacity:0.9">{{ store.slogan }}</p>
        <p v-if="store.description" class="text-white mt-3" style="opacity:0.75; max-width:500px; margin:auto">
          {{ store.description }}
        </p>
        <div class="mt-6">
          <v-chip size="small" color="white" variant="outlined">Powered by GoDrop</v-chip>
        </div>
      </header>

      <div class="pa-4 pb-0" style="max-width:1200px; margin:0 auto">
        <v-text-field
            v-model="search"
            placeholder="Buscar produtos..."
            prepend-inner-icon="mdi-magnify"
            variant="outlined"
            density="compact"
            bg-color="white"
            class="mt-4"
            hide-details
        />
      </div>

      <main style="max-width:1200px; margin:0 auto; padding:24px">
        <p class="text-caption text-medium-emphasis mb-4">
          {{ filteredProducts.length }} produto(s) disponíveis
        </p>

        <v-row v-if="filteredProducts.length > 0">
          <v-col
              v-for="product in filteredProducts"
              :key="product.id"
              cols="12" sm="6" md="4" lg="3"
          >
            <v-card
                elevation="2"
                class="h-100"
                :style="{ borderTop: `3px solid ${store.primaryColor}` }"
                @click="openProduct(product)"
            >
              <v-img
                  :src="product.imagem || 'https://via.placeholder.com/300x200?text=Produto'"
                  height="200"
                  cover
              />
              <v-card-text>
                <p class="font-weight-bold text-truncate">{{ product.nome }}</p>
                <p class="text-caption text-medium-emphasis text-truncate mb-2">{{ product.descricao }}</p>
                <p class="text-h6 font-weight-bold" :style="{ color: store.primaryColor }">
                  {{ formatPrice(product.preco) }}
                </p>
              </v-card-text>
              <v-card-actions class="pa-3 pt-0">
                <v-btn
                    block
                    :style="{ backgroundColor: store.primaryColor, color: 'white' }"
                    @click.stop="addToCart(product)"
                >
                  <v-icon start>mdi-cart-plus</v-icon>
                  Adicionar
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-col>
        </v-row>

        <div v-else class="text-center py-16">
          <v-icon size="64" color="medium-emphasis">mdi-package-variant-remove</v-icon>
          <p class="text-h6 mt-4 text-medium-emphasis">Nenhum produto encontrado</p>
        </div>
      </main>

      <button
          v-if="cartStore.totalItems > 0"
          class="cart-fab"
          :style="{ backgroundColor: store.primaryColor }"
          @click="cartDrawer = true"
      >
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2.5">
          <circle cx="9" cy="21" r="1"/><circle cx="20" cy="21" r="1"/>
          <path d="M1 1h4l2.68 13.39a2 2 0 002 1.61h9.72a2 2 0 001.95-1.57l1.65-8.42H6"/>
        </svg>
        <span class="cart-fab-badge">{{ cartStore.totalItems }}</span>
      </button>

      <div v-if="cartDrawer" class="cart-overlay" @click="cartDrawer = false"/>

      <div class="cart-panel" :class="{ open: cartDrawer }">
        <div class="cart-panel-inner">

          <div class="cart-panel-header">
            <h3>Meu Carrinho</h3>
            <button class="cart-close" @click="cartDrawer = false">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
                <line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/>
              </svg>
            </button>
          </div>

          <div v-if="cartStore.totalItems === 0" class="cart-empty">
            <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="#CBD5E1" stroke-width="1.5">
              <circle cx="9" cy="21" r="1"/><circle cx="20" cy="21" r="1"/>
              <path d="M1 1h4l2.68 13.39a2 2 0 002 1.61h9.72a2 2 0 001.95-1.57l1.65-8.42H6"/>
            </svg>
            <p>Carrinho vazio</p>
          </div>

          <div v-else class="cart-items">
            <div v-for="item in cartStore.cartItems" :key="item.id" class="cart-item">
              <img
                  :src="item.imagem || 'https://via.placeholder.com/50'"
                  class="cart-item-img"
                  :alt="item.nome"
              />
              <div class="cart-item-info">
                <p class="cart-item-name">{{ item.nome }}</p>
                <p class="cart-item-price">{{ formatPrice(item.preco * item.quantity) }}</p>
              </div>
              <div class="cart-item-qty">
                <button class="qty-btn" @click="cartStore.updateQuantity(item.id, -1)">−</button>
                <span>{{ item.quantity }}</span>
                <button class="qty-btn" @click="cartStore.updateQuantity(item.id, 1)">+</button>
              </div>
            </div>

            <div class="cart-divider"/>

            <div class="cart-subtotal">
              <span>Subtotal</span>
              <strong>{{ formatPrice(cartStore.subtotal) }}</strong>
            </div>

            <button
                class="cart-checkout-btn"
                :style="{ backgroundColor: store.primaryColor }"
                @click="goToCheckout"
            >
              Finalizar Compra
            </button>
          </div>
        </div>
      </div>

    </template>

    <v-snackbar v-model="snackbar" timeout="2000" location="top">{{ snackbarMsg }}</v-snackbar>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useStoreStore } from '../stores/store'
import { useCartStore } from '../stores/cart'

const route = useRoute()
const router = useRouter()
const storeStore = useStoreStore()
const cartStore = useCartStore()

const search = ref('')
const cartDrawer = ref(false)
const snackbar = ref(false)
const snackbarMsg = ref('')

const store = computed(() => storeStore.publicStore)

const filteredProducts = computed(() => {
  const products = store.value?.products ?? []
  if (!search.value) return products
  return products.filter(p => p.nome.toLowerCase().includes(search.value.toLowerCase()))
})

function formatPrice(v) {
  return Number(v || 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
}

function addToCart(product) {
  cartStore.addItem({
    id: product.id,
    nome: product.nome,
    preco: product.preco,
    precoCusto: product.precoCusto,
    imagem: product.imagem,
    storeSlug: store.value.slug
  })
  snackbarMsg.value = `${product.nome} adicionado ao carrinho!`
  snackbar.value = true
}

function openProduct(product) {
  addToCart(product)
}

function goToCheckout() {
  cartDrawer.value = false
  router.push({ path: '/checkout', query: { store: store.value.slug } })
}

onMounted(async () => {
  const slug = route.params.slug
  await storeStore.fetchPublicStore(slug)
  cartStore.loadCart()
})
</script>

<style scoped>
.cart-fab {
  position: fixed;
  bottom: 24px;
  right: 24px;
  z-index: 200;
  width: 56px;
  height: 56px;
  border-radius: 50%;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 16px rgba(0,0,0,0.2);
  transition: transform 0.2s, box-shadow 0.2s;
}
.cart-fab:hover {
  transform: scale(1.08);
  box-shadow: 0 6px 20px rgba(0,0,0,0.25);
}
.cart-fab-badge {
  position: absolute;
  top: -4px;
  right: -4px;
  background: #EF4444;
  color: white;
  font-size: 11px;
  font-weight: 700;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px solid white;
}

.cart-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.4);
  z-index: 300;
  backdrop-filter: blur(2px);
}

.cart-panel {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  width: 380px;
  max-width: 100vw;
  background: white;
  z-index: 400;
  transform: translateX(100%);
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: -8px 0 32px rgba(0,0,0,0.12);
}
.cart-panel.open {
  transform: translateX(0);
}

.cart-panel-inner {
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.cart-panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 24px;
  border-bottom: 1px solid #F1F5F9;
  flex-shrink: 0;
}
.cart-panel-header h3 {
  font-size: 18px;
  font-weight: 700;
  color: #0F172A;
  margin: 0;
}
.cart-close {
  background: none;
  border: none;
  cursor: pointer;
  padding: 6px;
  border-radius: 8px;
  display: flex;
  color: #64748B;
  transition: background 0.2s;
}
.cart-close:hover { background: #F1F5F9; color: #0F172A; }

.cart-empty {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 12px;
  color: #94A3B8;
  font-size: 14px;
}

.cart-items {
  flex: 1;
  overflow-y: auto;
  padding: 16px 24px 24px;
  display: flex;
  flex-direction: column;
}

.cart-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 0;
  border-bottom: 1px solid #F8FAFC;
}
.cart-item-img {
  width: 52px;
  height: 52px;
  border-radius: 10px;
  object-fit: cover;
  background: #F1F5F9;
  flex-shrink: 0;
}
.cart-item-info { flex: 1; min-width: 0; }
.cart-item-name {
  font-size: 13px;
  font-weight: 600;
  color: #0F172A;
  margin: 0 0 2px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.cart-item-price { font-size: 13px; color: #64748B; margin: 0; }

.cart-item-qty {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
}
.qty-btn {
  width: 28px;
  height: 28px;
  border-radius: 8px;
  border: 1px solid #E2E8F0;
  background: white;
  cursor: pointer;
  font-size: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #374151;
  transition: all 0.15s;
  line-height: 1;
}
.qty-btn:hover { background: #F1F5F9; border-color: #CBD5E1; }
.cart-item-qty span {
  font-size: 14px;
  font-weight: 600;
  color: #0F172A;
  min-width: 20px;
  text-align: center;
}

.cart-divider { height: 1px; background: #F1F5F9; margin: 16px 0; }

.cart-subtotal {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 15px;
  color: #374151;
  margin-bottom: 16px;
}
.cart-subtotal strong { font-size: 18px; font-weight: 700; color: #0F172A; }

.cart-checkout-btn {
  width: 100%;
  padding: 16px;
  border: none;
  border-radius: 14px;
  color: white;
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
  transition: opacity 0.2s, transform 0.15s;
  margin-top: auto;
}
.cart-checkout-btn:hover { opacity: 0.9; transform: translateY(-1px); }

@media (max-width: 480px) {
  .cart-panel { width: 100vw; }
}
</style>
<template>
  <div :style="{
    backgroundColor: store?.backgroundColor ?? '#F8FAFC', minHeight: '100vh'
  }">

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
      <header
          :style="{
          background: `linear-gradient(135deg, ${store.primaryColor}, ${store.secondaryColor})`,
          padding: '60px 24px',
          textAlign: 'center'
        }"
      >
        <v-avatar v-if="store.logoUrl" size="80" class="mb-4 elevation-4">
          <v-img :src="store.logoUrl"/>
        </v-avatar>
        <v-icon v-else size="80" color="white" class="mb-4">mdi-store</v-icon>

        <h1 class="text-h3 font-weight-bold text-white">{{ store.storeName }}</h1>
        <p v-if="store.slogan" class="text-white text-h6 mt-2" style="opacity:0.9">
          {{ store.slogan }}
        </p>
        <p v-if="store.description" class="text-white mt-3" style="opacity:0.75; max-width:500px; margin:auto">
          {{ store.description }}
        </p>

        <div class="mt-6">
          <v-chip size="small" color="white" variant="outlined">
            Powered by GoDrop
          </v-chip>
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
                <p class="text-caption text-medium-emphasis text-truncate mb-2">
                  {{ product.descricao }}
                </p>
                <p
                    class="text-h6 font-weight-bold"
                    :style="{ color: store.primaryColor }"
                >
                  R$ {{ product.preco?.toFixed(2).replace('.', ',') }}
                </p>
              </v-card-text>
              <v-card-actions class="pa-3 pt-0">
                <v-btn
                    block
                    :style="{
                    backgroundColor: store.primaryColor,
                    color: 'white'
                  }"
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

      <v-btn
          v-if="cartStore.totalItems > 0"
          fab
          :style="{ backgroundColor: store.primaryColor }"
          class="elevation-6"
          style="position:fixed; bottom:24px; right:24px; z-index:100"
          size="large"
          @click="cartDrawer = true"
      >
        <v-badge :content="cartStore.totalItems" color="error">
          <v-icon color="white">mdi-cart</v-icon>
        </v-badge>
      </v-btn>

      <v-navigation-drawer v-model="cartDrawer" location="right" width="380" temporary>
        <div class="pa-4">
          <div class="d-flex align-center justify-space-between mb-4">
            <h3 class="text-h6 font-weight-bold">Meu Carrinho</h3>
            <v-btn icon variant="text" @click="cartDrawer = false">
              <v-icon>mdi-close</v-icon>
            </v-btn>
          </div>

          <div v-if="cartStore.totalItems === 0" class="text-center py-8">
            <v-icon size="48" color="medium-emphasis">mdi-cart-outline</v-icon>
            <p class="text-medium-emphasis mt-2">Carrinho vazio</p>
          </div>

          <div v-else>
            <v-list>
              <v-list-item
                  v-for="item in cartStore.cartItems"
                  :key="item.id"
                  class="px-0"
              >
                <template #prepend>
                  <v-img
                      :src="item.imagem || 'https://via.placeholder.com/50'"
                      width="50"
                      height="50"
                      cover
                      class="rounded mr-3"
                  />
                </template>
                <v-list-item-title class="text-body-2 font-weight-bold">
                  {{ item.nome }}
                </v-list-item-title>
                <v-list-item-subtitle>
                  R$ {{ (item.preco * item.quantity).toFixed(2).replace('.', ',') }}
                </v-list-item-subtitle>
                <template #append>
                  <div class="d-flex align-center gap-1">
                    <v-btn icon size="x-small" variant="outlined" @click="cartStore.updateQuantity(item.id, -1)">
                      <v-icon size="14">mdi-minus</v-icon>
                    </v-btn>
                    <span class="text-body-2 mx-1">{{ item.quantity }}</span>
                    <v-btn icon size="x-small" variant="outlined" @click="cartStore.updateQuantity(item.id, 1)">
                      <v-icon size="14">mdi-plus</v-icon>
                    </v-btn>
                  </div>
                </template>
              </v-list-item>
            </v-list>

            <v-divider class="my-3"/>

            <div class="d-flex justify-space-between mb-2">
              <span class="text-medium-emphasis">Subtotal</span>
              <span class="font-weight-bold">
                R$ {{ cartStore.subtotal.toFixed(2).replace('.', ',') }}
              </span>
            </div>

            <v-btn
                block
                size="large"
                class="mt-3"
                :style="{ backgroundColor: store.primaryColor, color: 'white' }"
                :to="`/checkout?store=${store.slug}`"
            >
              Finalizar Compra
            </v-btn>
          </div>
        </div>
      </v-navigation-drawer>

    </template>

    <v-snackbar v-model="snackbar" timeout="2000" location="top">
      {{ snackbarMsg }}
    </v-snackbar>
  </div>
</template>

<script setup>
import {ref, computed, onMounted} from 'vue'
import {useRoute} from 'vue-router'
import {useStoreStore} from '../stores/store'
import {useCartStore} from '../stores/cart'

const route = useRoute()
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
  return products.filter(p =>
      p.nome.toLowerCase().includes(search.value.toLowerCase())
  )
})

function addToCart(product) {
  cartStore.addItem(product)
  snackbarMsg.value = `${product.nome} adicionado ao carrinho!`
  snackbar.value = true
}

function openProduct(product) {
  addToCart(product)
}

onMounted(async () => {
  const slug = route.params.slug
  await storeStore.fetchPublicStore(slug)
  cartStore.loadCart()
})
</script>
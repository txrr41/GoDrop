<template>
  <div class="products-grid">
    <div v-for="product in products" :key="product.id" class="product-card">
      <div class="card-image">
        <!-- ✅ Adicionado fallback e tratamento de erro -->
        <img
            :src="getImageUrl(product.imagem)"
            :alt="product.nome"
            @error="handleImageError"
            loading="lazy"
        />
      </div>
      <div class="card-content">
        <h3 class="card-title">{{ product.nome }}</h3>
        <p class="card-description">{{ product.descricao }}</p>
        <div class="card-footer">
          <span class="card-price">{{ formatCurrency(product.preco) }}</span>
          <v-chip
              size="small"
              :color="getStockColor(product.estoque)"
              :variant="product.estoque < 10 ? 'flat' : 'outlined'"
          >
            {{ getStockLabel(product.estoque) }}
          </v-chip>
        </div>
        <div class="card-stock">
          <span>Estoque: <strong>{{ product.estoque }} unidades</strong></span>
        </div>
        <v-btn
            color="primary"
            variant="outlined"
            block
            class="cart-btn"
            @click="handleAddToCart(product)"
            :disabled="product.estoque === 0"
        >
          <v-icon start>mdi-cart-plus</v-icon>
          {{ product.estoque === 0 ? 'Sem Estoque' : 'Adicionar ao Carrinho' }}
        </v-btn>
      </div>
    </div>
    <div v-if="products.length === 0" class="empty-grid">
      <p>Nenhum produto encontrado</p>
    </div>

    <v-snackbar
        v-model="snackbar"
        :timeout="3000"
        color="success"
        location="bottom right"
    >
      <div class="snackbar-content">
        <v-icon start>mdi-check-circle</v-icon>
        {{ snackbarMessage }}
      </div>
      <template v-slot:actions>
        <v-btn variant="text" @click="snackbar = false">
          Fechar
        </v-btn>
      </template>
    </v-snackbar>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useCartStore } from '../stores/cart'
import { useAuthStore } from '../stores/auth'

defineProps({
  products: { type: Array, default: () => [] }
})
const emit = defineEmits(['edit', 'delete', 'openLogin'])

const cartStore = useCartStore()
const authStore = useAuthStore()

const snackbar = ref(false)
const snackbarMessage = ref('')

// ✅ NOVA FUNÇÃO: Trata URLs de imagens
const getImageUrl = (imageUrl) => {
  // Se não tem imagem, retorna placeholder
  if (!imageUrl) {
    console.warn('⚠️ Produto sem imagem, usando placeholder')
    return 'https://via.placeholder.com/400x300/e0e0e0/666666?text=Sem+Imagem'
  }

  // Se já é Base64 válido, retorna direto
  if (imageUrl.startsWith('data:image/')) {
    return imageUrl
  }

  // Se é URL válida, retorna direto
  if (imageUrl.startsWith('http://') || imageUrl.startsWith('https://')) {
    return imageUrl
  }

  // Se é caminho relativo, tenta construir URL
  if (imageUrl.startsWith('/')) {
    return imageUrl
  }

  // Caso contrário, assume que está quebrado e usa placeholder
  console.error('❌ Formato de imagem inválido:', imageUrl.substring(0, 50))
  return 'https://via.placeholder.com/400x300/ffcccc/cc0000?text=Erro+na+Imagem'
}

// ✅ NOVA FUNÇÃO: Trata erros de carregamento
const handleImageError = (event) => {
  console.error('❌ Erro ao carregar imagem:', event.target.src)
  event.target.src = 'https://via.placeholder.com/400x300/ffcccc/cc0000?text=Falha+ao+Carregar'
}

const handleAddToCart = (product) => {
  if (!authStore.isLogged) {
    if (confirm('Você precisa estar logado para adicionar itens ao carrinho. Deseja fazer login?')) {
      emit('openLogin')
    }
    return
  }

  try {
    cartStore.addItem(product)
    snackbarMessage.value = `${product.nome} adicionado ao carrinho!`
    snackbar.value = true
  } catch (error) {
    alert(error.message)
  }
}

const formatCurrency = (value) => {
  return value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
}

const getStockColor = (stock) => {
  if (stock === 0) return 'error'
  if (stock < 10) return 'error'
  if (stock < 20) return 'warning'
  return 'success'
}

const getStockLabel = (stock) => {
  if (stock === 0) return 'Esgotado'
  if (stock < 10) return 'Crítico'
  if (stock < 20) return 'Baixo'
  return 'Normal'
}
</script>

<style scoped>
.products-grid {
  margin-top: 24px;
  display: grid;
  gap: 16px;
  grid-template-columns: 1fr;
}

@media (min-width: 640px) {
  .products-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (min-width: 1024px) {
  .products-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (min-width: 1280px) {
  .products-grid {
    grid-template-columns: repeat(4, 1fr);
  }
}

.product-card {
  background: #FFFFFF;
  border-radius: 16px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  border: 1px solid #E3E8EF;
  overflow: hidden;
  transition: box-shadow 0.2s ease, transform 0.2s ease;
}

.product-card:hover {
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -4px rgba(0, 0, 0, 0.1);
  transform: translateY(-4px);
}

.card-image {
  position: relative;
  aspect-ratio: 4 / 3;
  overflow: hidden;
  background: #F5F7FA;
}

.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

/* ✅ NOVO: Estado de loading para imagens */
.card-image img[src*="placeholder"] {
  opacity: 0.7;
}

.product-card:hover .card-image img {
  transform: scale(1.05);
}

.card-overlay {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  background: rgba(0, 0, 0, 0.6);
  opacity: 0;
  transition: opacity 0.2s ease;
}

.product-card:hover .card-overlay {
  opacity: 1;
}

.category-badge {
  position: absolute;
  top: 12px;
  left: 12px;
}

.card-content {
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #1A2332;
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-description {
  font-size: 13px;
  color: #5E6D82;
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.card-price {
  font-size: 18px;
  font-weight: 700;
  color: #1565C0;
}

.card-stock {
  padding-top: 12px;
  border-top: 1px solid #E3E8EF;
  font-size: 13px;
  color: #5E6D82;
}

.card-stock strong {
  color: #1A2332;
}

.cart-btn {
  margin-top: 8px;
  font-weight: 600;
}

.empty-grid {
  grid-column: 1 / -1;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 192px;
  background: #FFFFFF;
  border-radius: 16px;
  color: #9AA5B5;
}

.snackbar-content {
  display: flex;
  align-items: center;
  gap: 8px;
}
</style>
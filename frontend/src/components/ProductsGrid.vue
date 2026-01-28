<template>
  <div class="products-grid">
    <div v-for="product in products" :key="product.id" class="product-card">
      <div class="card-image">
        <img :src="product.imagem" :alt="product.nome" />
        <div class="card-overlay">
          <v-btn icon variant="flat" color="white" size="small" @click="$emit('edit', product)">
            <v-icon>mdi-pencil</v-icon>
          </v-btn>
          <v-btn icon variant="flat" color="error" size="small" @click="$emit('delete', product)" >
            <v-icon>mdi-delete</v-icon>
          </v-btn>
        </div>
        <v-chip class="category-badge" size="small">{{ product.categoria }}</v-chip>
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
            @click="$emit('addToCart', product)"
        >
          <v-icon start>mdi-cart-plus</v-icon>
          Adicionar ao Carrinho
        </v-btn>
      </div>
    </div>
    <div v-if="products.length === 0" class="empty-grid">
      <p>Nenhum produto encontrado</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ProductsGrid',
  props: {
    products: { type: Array, default: () => [] }
  },
  emits: ['edit', 'delete', 'addToCart'],
  methods: {
    formatCurrency(value) {
      return value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
    },
    getStockColor(stock) {
      if (stock < 10) return 'error'
      if (stock < 20) return 'warning'
      return 'success'
    },
    getStockLabel(stock) {
      if (stock < 10) return 'Critico'
      if (stock < 20) return 'Baixo'
      return 'Normal'
    }
  }
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
</style>

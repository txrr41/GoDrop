<template>
  <div class="products-table-container">
    <v-table class="products-table">
      <thead>
      <tr>
        <th class="th-photo">Foto</th>
        <th>Produto</th>
        <th>Categoria</th>
        <th class="text-right">Preco</th>
        <th class="text-center">Estoque</th>
        <th class="text-center">Status</th>
        <th class="text-center th-actions">Acoes</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="product in products" :key="product.id" class="product-row">
        <td>
          <div class="product-thumb">
            <img :src="product.image" :alt="product.name" />
          </div>
        </td>
        <td>
          <div class="product-info">
            <p class="product-name">{{ product.name }}</p>
            <p class="product-description">{{ product.description }}</p>
          </div>
        </td>
        <td>
          <v-chip size="small" variant="outlined">{{ product.category }}</v-chip>
        </td>
        <td class="text-right font-medium">{{ formatCurrency(product.price) }}</td>
        <td class="text-center font-medium">{{ product.stock }}</td>
        <td class="text-center">
          <v-chip
              size="small"
              :color="getStockColor(product.stock)"
              :variant="product.stock < 10 ? 'flat' : 'outlined'"
          >
            {{ getStockLabel(product.stock) }}
          </v-chip>
        </td>
        <td class="text-center">
          <div class="row-actions">
            <v-btn icon variant="text" size="small" class="edit-btn" @click="$emit('edit', product)">
              <v-icon size="18">mdi-pencil</v-icon>
            </v-btn>
            <v-menu>
              <template v-slot:activator="{ props }">
                <v-btn icon variant="text" size="small" v-bind="props">
                  <v-icon size="18">mdi-dots-horizontal</v-icon>
                </v-btn>
              </template>
              <v-list density="compact">
                <v-list-item @click="$emit('edit', product)">
                  <template v-slot:prepend>
                    <v-icon size="18">mdi-pencil</v-icon>
                  </template>
                  <v-list-item-title>Editar</v-list-item-title>
                </v-list-item>
                <v-list-item class="delete-item" @click="$emit('delete', product)">
                  <template v-slot:prepend>
                    <v-icon size="18">mdi-delete</v-icon>
                  </template>
                  <v-list-item-title>Excluir</v-list-item-title>
                </v-list-item>
              </v-list>
            </v-menu>
          </div>
        </td>
      </tr>
      <tr v-if="products.length === 0">
        <td colspan="7" class="empty-state">
          <p>Nenhum produto encontrado</p>
        </td>
      </tr>
      </tbody>
    </v-table>
  </div>
</template>

<script>
export default {
  name: 'ProductsTable',
  props: {
    products: { type: Array, default: () => [] }
  },
  emits: ['edit', 'delete'],
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
.products-table-container {
  margin-top: 24px;
  background: #FFFFFF;
  border-radius: 16px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  border: 1px solid #E3E8EF;
  overflow: hidden;
}

.products-table {
  width: 100%;
}

.products-table thead {
  background: #F5F7FA;
}

.products-table th {
  font-size: 13px;
  font-weight: 600;
  color: #5E6D82;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  padding: 16px;
  border-bottom: 1px solid #E3E8EF;
}

.th-photo {
  width: 80px;
  padding-left: 24px;
}

.th-actions {
  width: 100px;
  padding-right: 24px;
}

.product-row {
  transition: background-color 0.15s ease;
}

.product-row:hover {
  background-color: #F5F7FA;
}

.product-row td {
  padding: 16px;
  vertical-align: middle;
  border-bottom: 1px solid #E3E8EF;
}

.product-thumb {
  width: 48px;
  height: 48px;
  border-radius: 10px;
  overflow: hidden;
  border: 1px solid #E3E8EF;
  background: #F5F7FA;
}

.product-thumb img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.product-name {
  font-size: 14px;
  font-weight: 600;
  color: #1A2332;
  margin: 0;
}

.product-description {
  font-size: 13px;
  color: #5E6D82;
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.font-medium {
  font-weight: 500;
  color: #1A2332;
}

.text-right {
  text-align: right;
}

.text-center {
  text-align: center;
}

.row-actions {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
}

.edit-btn {
  opacity: 0;
  transition: opacity 0.15s ease;
}

.product-row:hover .edit-btn {
  opacity: 1;
}

.delete-item {
  color: #F44336 !important;
}

.empty-state {
  height: 128px;
  text-align: center;
  color: #9AA5B5;
  font-size: 14px;
}
</style>

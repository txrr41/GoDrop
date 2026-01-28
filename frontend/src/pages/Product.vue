<template>
  <div class="products-page">

    <DashboardCards
        :total-products="totalProducts"
        :total-stock="totalStock"
        :total-value="totalValue"
        :low-stock-count="lowStockCount"
    />


    <ActionsBar
        v-model:search-term="searchTerm"
        v-model:selected-category="selectedCategory"
        v-model:view-mode="viewMode"
        :category-options="categoryOptions"
        @open-category-modal="categoryModal = true"
        @open-product-modal="openNewProduct"
    />


    <ProductsTable
        v-if="viewMode === 'list'"
        :products="filteredProducts"
        @edit="editProduct"
        @delete="confirmDelete"
    />


    <ProductsGrid
        v-else
        :products="filteredProducts"
        @edit="editProduct"
        @delete="confirmDelete"
        @add-to-cart="addToCart"
    />


    <ProductModal
        v-model="productModal"
        :editing-product="editingProduct"
        :categories="categories"
        @save="saveProduct"
    />


    <CategoryModal
        v-model="categoryModal"
        @save="saveCategory"
    />


    <DeleteDialog
        v-model="deleteDialog"
        :product-name="productToDelete?.name || ''"
        @confirm="deleteProduct"
    />


    <v-snackbar v-model="snackbar" :color="snackbarColor" timeout="3000" location="top">
      {{ snackbarMessage }}
      <template v-slot:actions>
        <v-btn variant="text" @click="snackbar = false">Fechar</v-btn>
      </template>
    </v-snackbar>
  </div>
</template>

<script>
import DashboardCards from '../components/DashboardCards.vue'
import ActionsBar from '../components/ActionsBar.vue'
import ProductsTable from '../components/ProductsTable.vue'
import ProductsGrid from '../components/ProductsGrid.vue'
import ProductModal from '../components/ProductModal.vue'
import CategoryModal from '../components/CategoryModal.vue'
import DeleteDialog from '../components/DeleteDialog.vue'

export default {
  name: 'ProductsPage',
  components: {
    DashboardCards,
    ActionsBar,
    ProductsTable,
    ProductsGrid,
    ProductModal,
    CategoryModal,
    DeleteDialog
  },
  data() {
    return {
      viewMode: 'list',
      searchTerm: '',
      selectedCategory: 'all',

      productModal: false,
      categoryModal: false,
      deleteDialog: false,

      snackbar: false,
      snackbarMessage: '',
      snackbarColor: 'success',

      editingProduct: null,
      productToDelete: null,

      products: [],
      categories: []
    }
  },
  computed: {
    categoryOptions() {
      return [{ name: 'Todas as categorias', value: 'all' }, ...this.categories]
    },
    filteredProducts() {
      return this.products.filter(product => {
        const matchesSearch = product.name.toLowerCase().includes(this.searchTerm.toLowerCase())
        const matchesCategory = this.selectedCategory === 'all' || product.category === this.selectedCategory
        return matchesSearch && matchesCategory
      })
    },
    totalProducts() {
      return this.products.length
    },
    totalStock() {
      return this.products.reduce((acc, p) => acc + p.stock, 0)
    },
    totalValue() {
      return this.products.reduce((acc, p) => acc + (p.price * p.stock), 0)
    },
    lowStockCount() {
      return this.products.filter(p => p.stock < 20).length
    }
  },
  methods: {
    openNewProduct() {
      this.editingProduct = null
      this.productModal = true
    },
    editProduct(product) {
      this.editingProduct = product
      this.productModal = true
    },
    saveProduct(formData) {
      console.log('Salvando produto:', formData)
    },

    confirmDelete(product) {
      this.productToDelete = product
      this.deleteDialog = true
    },
    deleteProduct() {
      console.log('Excluindo produto:', this.productToDelete)
      this.showSnackbar('Produto excluido com sucesso!', 'success')
      this.productToDelete = null
    },


    saveCategory(categoryName) {
      console.log('Salvando categoria:', categoryName)
    },

    addToCart(product) {
      console.log('Adicionando ao carrinho:', product)
      this.showSnackbar(`${product.name} adicionado ao carrinho!`, 'success')
    },

    showSnackbar(message, color = 'success') {
      this.snackbarMessage = message
      this.snackbarColor = color
      this.snackbar = true
    }
  }
}
</script>

<style scoped>
.products-page {
  min-height: 100vh;
  background-color: #F5F7FA;
  padding: 24px;
}

@media (min-width: 1024px) {
  .products-page {
    padding: 32px;
  }
}
</style>

<template>
  <div class="products-page">

    <DashboardCards
        :total-products="productStore.totalProducts"
        :total-stock="productStore.totalStock"
        :total-value="productStore.totalValue"
        :low-stock-count="productStore.lowStockCount"
    />


    <ActionsBar
        v-model:search-term="searchTerm"
        v-model:selected-category="selectedCategory"
        v-model:view-mode="viewMode"
        :category-options="categoryOptions"
        @open-category-modal="categoryModal = true"
        @open-product-modal="openNewProduct"
        :disabled="productStore.loading"
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
        :categories="productStore.categories"
        @save="saveProduct"
    />

    <CategoryModal
        v-model="categoryModal"
        @save="saveCategory"
    />

    <DeleteDialog
        v-model="deleteDialog"
        :product-name="productToDelete?.nome || ''"
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

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useProductStore } from '../stores/product'
import DashboardCards from '../components/DashboardCards.vue'
import ActionsBar from '../components/ActionsBar.vue'
import ProductsTable from '../components/ProductsTable.vue'
import ProductsGrid from '../components/ProductsGrid.vue'
import ProductModal from '../components/ProductModal.vue'
import CategoryModal from '../components/CategoryModal.vue'
import DeleteDialog from '../components/DeleteDialog.vue'


const route = useRoute()
const productStore = useProductStore()

const viewMode = ref('list')
const searchTerm = ref('')
const selectedCategory = ref('all')
const productModal = ref(false)
const categoryModal = ref(false)
const deleteDialog = ref(false)
const snackbar = ref(false)
const snackbarMessage = ref('')
const snackbarColor = ref('success')
const editingProduct = ref(null)
const productToDelete = ref(null)

watch(() => route.query.categoria, (newCategoria) => {
  if (newCategoria) {
    selectedCategory.value = newCategoria
  }
}, { immediate: true })

const categoryOptions = computed(() => {
  return [
    { name: 'Todas as categorias', value: 'all' },
    ...productStore.categories
  ]
})

const filteredProducts = computed(() => {
  return productStore.products.filter(product => {
    const matchesSearch = product.nome.toLowerCase().includes(searchTerm.value.toLowerCase())
    const matchesCategory = selectedCategory.value === 'all' || product.categoria === selectedCategory.value
    return matchesSearch && matchesCategory
  })
})


const openNewProduct = () => {
  editingProduct.value = null
  productModal.value = true
}

const editProduct = (product) => {
  editingProduct.value = { ...product }
  productModal.value = true
}

const saveProduct = async (formData) => {
  try {
    if (editingProduct.value) {
      await productStore.updateProduct(editingProduct.value.id, formData)
      showSnackbar('Produto atualizado com sucesso!', 'success')
    } else {
      await productStore.createProduct(formData)
      showSnackbar('Produto criado com sucesso!', 'success')
    }
    productModal.value = false
  } catch (error) {
    showSnackbar('Erro ao salvar produto', 'error')
  }
}

const confirmDelete = (product) => {
  productToDelete.value = product
  deleteDialog.value = true
}

const deleteProduct = async () => {
  try {
    await productStore.deleteProduct(productToDelete.value.id)
    showSnackbar('Produto excluído com sucesso!', 'success')
    productToDelete.value = null
  } catch (error) {
    showSnackbar('Erro ao excluir produto', 'error')
  }
}

const saveCategory = (categoryName) => {
  productStore.addCategory(categoryName)
  showSnackbar('Categoria adicionada com sucesso!', 'success')
}

const addToCart = (product) => {
  console.log('Adicionando ao carrinho:', product)
  showSnackbar(`${product.nome} adicionado ao carrinho!`, 'success')
}

const showSnackbar = (message, color = 'success') => {
  snackbarMessage.value = message
  snackbarColor.value = color
  snackbar.value = true
}

onMounted(async () => {
  try {
    await productStore.fetchProducts()
  } catch (error) {
    showSnackbar('Erro ao carregar produtos', 'error')
  }

})
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
<template>
  <div class="product-selector">
    <!-- Header -->
    <div class="selector-header">
      <div>
        <h3 class="selector-title">Selecionar Produtos</h3>
        <p class="selector-subtitle">Escolha produtos do catálogo e defina seu preço de venda</p>
      </div>
      <div class="header-actions">
        <v-btn v-if="hasChanges" color="primary" @click="saveSelection" :loading="saving">
          <v-icon start>mdi-content-save</v-icon>
          Salvar ({{ selectedItems.length }})
        </v-btn>
        <v-btn variant="outlined" @click="$emit('cancel')">Cancelar</v-btn>
      </div>
    </div>

    <!-- Search + Filter -->
    <div class="selector-filters">
      <v-text-field
          v-model="search"
          placeholder="Buscar produtos..."
          prepend-inner-icon="mdi-magnify"
          variant="outlined"
          density="compact"
          hide-details
          class="search-field"
      />
      <v-chip-group v-model="activeCategory" mandatory>
        <v-chip value="all" filter>Todos</v-chip>
        <v-chip v-for="cat in categories" :key="cat" :value="cat" filter>{{ cat }}</v-chip>
      </v-chip-group>
    </div>

    <!-- Product Grid -->
    <div class="products-grid">
      <div
          v-for="product in filteredProducts"
          :key="product.id"
          :class="['product-card', { selected: isSelected(product.id) }]"
      >
        <!-- Checkbox overlay -->
        <div class="product-checkbox" @click="toggleProduct(product)">
          <v-icon v-if="isSelected(product.id)" color="primary" size="22">mdi-checkbox-marked-circle</v-icon>
          <v-icon v-else color="grey-lighten-1" size="22">mdi-checkbox-blank-circle-outline</v-icon>
        </div>

        <!-- Image -->
        <div class="product-image" @click="toggleProduct(product)">
          <img :src="product.imagem || 'https://via.placeholder.com/200x150?text=Produto'" :alt="product.nome" />
        </div>

        <!-- Info -->
        <div class="product-info">
          <p class="product-name">{{ product.nome }}</p>
          <p class="product-category">{{ product.categoria }}</p>
          <p class="product-cost">
            <v-icon size="12" color="grey">mdi-tag</v-icon>
            Custo: {{ formatCurrency(product.preco) }}
          </p>

          <!-- Custom price input (only when selected) -->
          <div v-if="isSelected(product.id)" class="price-input-wrapper" @click.stop>
            <v-text-field
                :model-value="getCustomPrice(product.id)"
                @update:model-value="setCustomPrice(product.id, $event)"
                label="Seu preço de venda"
                prefix="R$"
                type="number"
                :min="product.preco"
                step="0.01"
                density="compact"
                variant="outlined"
                hide-details
                color="primary"
                :rules="[v => Number(v) >= product.preco || `Mínimo: ${formatCurrency(product.preco)}`]"
            />
            <div v-if="getMargin(product) > 0" class="margin-badge">
              <v-icon size="12">mdi-trending-up</v-icon>
              Margem: {{ formatCurrency(getMargin(product)) }}
            </div>
          </div>

          <!-- Preview price when not selected -->
          <div v-else class="price-preview" @click="toggleProduct(product)">
            <span class="price-hint">Clique para adicionar e definir preço</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Empty state -->
    <div v-if="filteredProducts.length === 0" class="empty-state">
      <v-icon size="64" color="grey-lighten-2">mdi-package-variant-remove</v-icon>
      <p>Nenhum produto encontrado</p>
    </div>

    <!-- Footer summary -->
    <div v-if="selectedItems.length > 0" class="selection-summary">
      <div class="summary-chips">
        <v-chip
            v-for="item in selectedItems"
            :key="item.productId"
            closable
            size="small"
            color="primary"
            variant="tonal"
            @click:close="removeById(item.productId)"
        >
          {{ getProductName(item.productId) }}
          <span v-if="item.customPrice" class="ml-1">· {{ formatCurrency(item.customPrice) }}</span>
        </v-chip>
      </div>
      <v-btn color="primary" @click="saveSelection" :loading="saving" class="save-btn">
        <v-icon start>mdi-content-save</v-icon>
        Salvar seleção
      </v-btn>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useProductStore } from '../stores/product'
import { useStoreStore } from '../stores/store'

const props = defineProps({
  // Array de { productId, customPrice } já salvos na loja
  initialProducts: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['updated', 'cancel'])

const productStore = useProductStore()
const storeStore = useStoreStore()

const search = ref('')
const activeCategory = ref('all')
const saving = ref(false)

// selectedItems: [{ productId, customPrice }]
const selectedItems = ref([])

onMounted(async () => {
  await productStore.fetchProducts()
  // Inicializa com os produtos já selecionados
  selectedItems.value = props.initialProducts.map(p => ({
    productId: p.productId || p.id,
    customPrice: p.customPrice || p.preco || null
  }))
})

const categories = computed(() => {
  const cats = productStore.activeProducts.map(p => p.categoria).filter(Boolean)
  return [...new Set(cats)]
})

const filteredProducts = computed(() => {
  let list = productStore.activeProducts
  if (activeCategory.value !== 'all') {
    list = list.filter(p => p.categoria === activeCategory.value)
  }
  if (search.value) {
    const q = search.value.toLowerCase()
    list = list.filter(p => p.nome.toLowerCase().includes(q))
  }
  return list
})

const hasChanges = computed(() => {
  const initial = JSON.stringify(
      props.initialProducts.map(p => ({ productId: p.productId || p.id, customPrice: p.customPrice || p.preco })).sort((a,b) => a.productId - b.productId)
  )
  const current = JSON.stringify(
      [...selectedItems.value].sort((a,b) => a.productId - b.productId)
  )
  return initial !== current
})

function isSelected(productId) {
  return selectedItems.value.some(i => i.productId === productId)
}

function toggleProduct(product) {
  const idx = selectedItems.value.findIndex(i => i.productId === product.id)
  if (idx >= 0) {
    selectedItems.value.splice(idx, 1)
  } else {
    // Default customPrice = preço de custo (dropper define depois)
    selectedItems.value.push({ productId: product.id, customPrice: product.preco })
  }
}

function getCustomPrice(productId) {
  const item = selectedItems.value.find(i => i.productId === productId)
  return item?.customPrice ?? null
}

function setCustomPrice(productId, value) {
  const item = selectedItems.value.find(i => i.productId === productId)
  if (item) {
    item.customPrice = value ? Number(Number(value).toFixed(2)) : null
  }
}

function getMargin(product) {
  const customPrice = getCustomPrice(product.id)
  if (!customPrice) return 0
  return Number(customPrice) - Number(product.preco)
}

function removeById(productId) {
  selectedItems.value = selectedItems.value.filter(i => i.productId !== productId)
}

function getProductName(productId) {
  return productStore.activeProducts.find(p => p.id === productId)?.nome ?? `Produto #${productId}`
}

function formatCurrency(v) {
  return Number(v || 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
}

async function saveSelection() {
  saving.value = true
  try {
    await storeStore.updateStore({ products: selectedItems.value })
    emit('updated', selectedItems.value)
  } catch (e) {
    console.error('Erro ao salvar:', e)
  } finally {
    saving.value = false
  }
}
</script>

<style scoped>
.product-selector {
  padding: 24px 0;
}

.selector-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: 20px;
  gap: 16px;
  flex-wrap: wrap;
}

.selector-title {
  font-size: 18px;
  font-weight: 700;
  color: #0F172A;
  margin: 0 0 4px;
}

.selector-subtitle {
  font-size: 13px;
  color: #64748B;
  margin: 0;
}

.header-actions {
  display: flex;
  gap: 8px;
}

.selector-filters {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 20px;
}

.search-field {
  max-width: 400px;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}

.product-card {
  background: #F8FAFC;
  border: 2px solid #E2E8F0;
  border-radius: 16px;
  overflow: hidden;
  position: relative;
  cursor: pointer;
  transition: all 0.2s ease;
}

.product-card:hover {
  border-color: #CBD5E1;
  box-shadow: 0 4px 12px rgba(0,0,0,0.06);
}

.product-card.selected {
  border-color: #2563EB;
  background: #EFF6FF;
}

.product-checkbox {
  position: absolute;
  top: 10px;
  right: 10px;
  z-index: 2;
  background: white;
  border-radius: 50%;
  line-height: 0;
}

.product-image {
  width: 100%;
  height: 130px;
  overflow: hidden;
  background: #E2E8F0;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info {
  padding: 12px;
}

.product-name {
  font-size: 14px;
  font-weight: 600;
  color: #1E293B;
  margin: 0 0 2px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-category {
  font-size: 11px;
  color: #94A3B8;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  margin: 0 0 6px;
}

.product-cost {
  font-size: 12px;
  color: #64748B;
  margin: 0 0 8px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.price-input-wrapper {
  margin-top: 8px;
}

.margin-badge {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 11px;
  font-weight: 600;
  color: #059669;
  margin-top: 4px;
}

.price-preview {
  margin-top: 8px;
}

.price-hint {
  font-size: 11px;
  color: #94A3B8;
  font-style: italic;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 48px;
  color: #94A3B8;
}

.selection-summary {
  background: #F1F5F9;
  border-radius: 16px;
  padding: 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  flex-wrap: wrap;
  border: 1px solid #E2E8F0;
}

.summary-chips {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  flex: 1;
}

.save-btn {
  flex-shrink: 0;
}
</style>
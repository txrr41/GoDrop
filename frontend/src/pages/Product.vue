<template>
  <div class="pm-root">

    <header class="pm-topbar">
      <div class="tb-left">
        <h1 class="tb-title">Produtos</h1>
        <div class="tb-breadcrumb">
          <span>GoDrop</span><span class="tb-sep">/</span><span class="tb-current">Catálogo de Produtos</span>
        </div>
      </div>
      <div class="tb-right">
        <div class="tb-search">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="11" cy="11" r="8"/><path d="m21 21-4.35-4.35"/></svg>
          <input v-model="searchTerm" placeholder="Buscar produto..." class="tb-search-input"/>
        </div>
        <button class="tb-btn-new" @click="openModal()">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M12 5v14M5 12h14"/></svg>
          Novo Produto
        </button>
      </div>
    </header>

    <section class="pm-stats">
      <div class="stat-card">
        <div class="stat-icon stat-icon--blue"><svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor"><path d="M20 7H4a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2z"/></svg></div>
        <div class="stat-body"><span class="stat-value">{{ products.length }}</span><span class="stat-label">Total de Produtos</span></div>
        <span class="stat-trend trend-up">+12%</span>
      </div>
      <div class="stat-card">
        <div class="stat-icon stat-icon--emerald"><svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor"><path d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-5 14H7v-2h7v2zm3-4H7v-2h10v2zm0-4H7V7h10v2z"/></svg></div>
        <div class="stat-body"><span class="stat-value">{{ totalStock.toLocaleString('pt-BR') }}</span><span class="stat-label">Estoque Total</span></div>
        <span class="stat-trend trend-up">+8%</span>
      </div>
      <div class="stat-card">
        <div class="stat-icon stat-icon--amber"><svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor"><path d="M11.8 10.9c-2.27-.59-3-1.2-3-2.15 0-1.09 1.01-1.85 2.7-1.85 1.78 0 2.44.85 2.5 2.1h2.21c-.07-1.72-1.12-3.3-3.21-3.81V3h-3v2.16c-1.94.42-3.5 1.68-3.5 3.61 0 2.31 1.91 3.46 4.7 4.13 2.5.6 3 1.48 3 2.41 0 .69-.49 1.79-2.7 1.79-2.06 0-2.87-.92-2.98-2.1h-2.2c.12 2.19 1.76 3.42 3.68 3.83V21h3v-2.15c1.95-.37 3.5-1.5 3.5-3.55 0-2.84-2.43-3.81-4.7-4.4z"/></svg></div>
        <div class="stat-body"><span class="stat-value">{{ formatCurrency(totalValue) }}</span><span class="stat-label">Valor em Estoque</span></div>
        <span class="stat-trend trend-up">+23%</span>
      </div>
      <div class="stat-card">
        <div class="stat-icon stat-icon--red"><svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor"><path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z"/></svg></div>
        <div class="stat-body"><span class="stat-value">{{ lowStockCount }}</span><span class="stat-label">Estoque Crítico</span></div>
        <span class="stat-trend trend-down">Atenção</span>
      </div>
    </section>

    <section class="pm-toolbar">
      <div class="pt-filters">
        <button v-for="cat in categoryTabs" :key="cat" class="pt-tab" :class="{ 'pt-tab--active': selectedCategory === cat }" @click="selectedCategory = cat">
          {{ cat === 'all' ? 'Todos' : cat }}
        </button>
      </div>
      <div class="pt-actions">
        <button class="pt-view" :class="{ 'pt-view--active': viewMode === 'grid' }" @click="viewMode = 'grid'">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor"><path d="M3 3h7v7H3zm11 0h7v7h-7zM3 14h7v7H3zm11 0h7v7h-7z"/></svg>
        </button>
        <button class="pt-view" :class="{ 'pt-view--active': viewMode === 'list' }" @click="viewMode = 'list'">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor"><path d="M3 18h18v-2H3v2zm0-5h18v-2H3v2zm0-7v2h18V6H3z"/></svg>
        </button>
        <button class="pt-btn-cat" @click="showCatModal = true">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 5v14M5 12h14"/></svg>
          Categoria
        </button>
      </div>
    </section>

    <!-- GRID -->
    <section v-if="viewMode === 'grid'" class="pm-grid">
      <div v-for="product in filteredProducts" :key="product.id" class="pg-card" :class="{ 'pg-card--featured': product.destaque }">
        <div class="pg-image-wrap">
          <img :src="product.imagem || 'https://via.placeholder.com/400x300/f1f5f9/94a3b8?text=Sem+Foto'" :alt="product.nome" class="pg-image"/>
          <div class="pg-badge-wrap">
            <span v-if="product.destaque" class="pg-badge pg-badge--star">★ Destaque</span>
            <span class="pg-badge" :class="getStockBadgeClass(product.estoque)">{{ getStockLabel(product.estoque) }}</span>
          </div>
          <div class="pg-overlay">
            <button class="pg-ov-btn" @click="openModal(product)">✎ Editar</button>
            <button class="pg-ov-btn pg-ov-btn--danger" @click="confirmDelete(product)">✕ Excluir</button>
          </div>
        </div>
        <div class="pg-body">
          <span class="pg-category">{{ product.categoria }}</span>
          <h3 class="pg-name">{{ product.nome }}</h3>
          <p class="pg-desc">{{ product.descricao }}</p>
          <div class="pg-footer">
            <span class="pg-price">{{ formatCurrency(product.preco) }}</span>
            <span class="pg-stock">{{ product.estoque }} un.</span>
          </div>
        </div>
      </div>
      <div v-if="filteredProducts.length === 0" class="pm-empty">
        <p class="empty-text">Nenhum produto encontrado</p>
        <button class="tb-btn-new" @click="openModal()">Adicionar primeiro produto</button>
      </div>
    </section>

    <!-- LIST -->
    <section v-else class="pm-list">
      <table class="pl-table">
        <thead><tr><th>Produto</th><th>Categoria</th><th>Preço</th><th>Estoque</th><th>Status</th><th>Ações</th></tr></thead>
        <tbody>
        <tr v-for="product in filteredProducts" :key="product.id" class="pl-row">
          <td>
            <div class="pl-product">
              <img :src="product.imagem || 'https://via.placeholder.com/48x48/f1f5f9/94a3b8?text=?'" class="pl-thumb"/>
              <div><div class="pl-name">{{ product.nome }}</div><div class="pl-desc-sm">{{ product.descricao }}</div></div>
            </div>
          </td>
          <td><span class="pl-chip">{{ product.categoria }}</span></td>
          <td class="pl-price-col">{{ formatCurrency(product.preco) }}</td>
          <td class="pl-stock-col">{{ product.estoque }}</td>
          <td><span class="pl-status" :class="getStockBadgeClass(product.estoque)">{{ getStockLabel(product.estoque) }}</span></td>
          <td>
            <div class="pl-actions">
              <button class="pl-btn" @click="openModal(product)">✎</button>
              <button class="pl-btn pl-btn--danger" @click="confirmDelete(product)">✕</button>
            </div>
          </td>
        </tr>
        <tr v-if="filteredProducts.length === 0"><td colspan="6" class="pl-empty">Nenhum produto encontrado</td></tr>
        </tbody>
      </table>
    </section>

    <!-- MODAL PRODUTO -->
    <div v-if="showModal" class="modal-backdrop" @click.self="closeModal">
      <div class="modal-panel">
        <div class="modal-head">
          <div>
            <h2 class="modal-title">{{ editingProduct ? 'Editar Produto' : 'Novo Produto' }}</h2>
            <p class="modal-sub">{{ editingProduct ? 'Atualize as informações do produto' : 'Preencha os dados para cadastrar' }}</p>
          </div>
          <button class="modal-close" @click="closeModal">✕</button>
        </div>
        <div class="modal-body">
          <div class="modal-col-img">
            <p class="mf-label">Imagem do Produto</p>
            <div class="img-drop" :class="{ 'img-drop--has': formData.image }" @click="triggerFileInput" @dragover.prevent @drop.prevent="handleDrop">
              <img v-if="formData.image" :src="formData.image" class="img-preview" alt="preview"/>
              <div v-else class="img-placeholder">
                <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="#94a3b8" stroke-width="1.5"><rect x="3" y="3" width="18" height="18" rx="2"/><circle cx="8.5" cy="8.5" r="1.5"/><polyline points="21 15 16 10 5 21"/></svg>
                <p class="img-hint">Arraste ou <span>clique para upload</span></p>
                <p class="img-sub">PNG, JPG, WEBP · até 5MB</p>
              </div>
              <input ref="fileInput" type="file" accept="image/*" style="display:none" @change="handleFileChange"/>
              <button v-if="formData.image" class="img-remove" @click.stop="formData.image = ''">✕</button>
            </div>
            <div class="destaque-toggle" @click="formData.destaque = !formData.destaque">
              <div class="dt-info">
                <span class="dt-icon">★</span>
                <div><p class="dt-title">Produto em Destaque</p><p class="dt-sub">Aparece na página inicial</p></div>
              </div>
              <div class="dt-switch" :class="{ 'dt-switch--on': formData.destaque }"><div class="dt-thumb"></div></div>
            </div>
          </div>
          <div class="modal-col-form">
            <div class="mf-group">
              <label class="mf-label">Nome do Produto <span class="mf-req">*</span></label>
              <input v-model="formData.name" class="mf-input" :class="{ 'mf-input--error': errors.name }" placeholder="Ex: Notebook Pro 15"/>
              <span v-if="errors.name" class="mf-error">{{ errors.name }}</span>
            </div>
            <div class="mf-group">
              <label class="mf-label">Descrição</label>
              <textarea v-model="formData.description" class="mf-input mf-textarea" placeholder="Descreva o produto..." rows="4"></textarea>
            </div>
            <div class="mf-row">
              <div class="mf-group">
                <label class="mf-label">Preço (R$) <span class="mf-req">*</span></label>
                <div class="mf-input-wrap">
                  <span class="mf-prefix">R$</span>
                  <input v-model.number="formData.price" type="number" step="0.01" min="0" class="mf-input mf-input--prefixed" :class="{ 'mf-input--error': errors.price }" placeholder="0,00"/>
                </div>
                <span v-if="errors.price" class="mf-error">{{ errors.price }}</span>
              </div>
              <div class="mf-group">
                <label class="mf-label">Estoque <span class="mf-req">*</span></label>
                <input v-model.number="formData.stock" type="number" min="0" class="mf-input" :class="{ 'mf-input--error': errors.stock }" placeholder="0"/>
                <span v-if="errors.stock" class="mf-error">{{ errors.stock }}</span>
              </div>
            </div>
            <div class="mf-group">
              <label class="mf-label">Categoria</label>
              <select v-model="formData.category" class="mf-input mf-select">
                <option value="">Selecione uma categoria</option>
                <option v-for="cat in categories" :key="cat.name" :value="cat.name">{{ cat.name }}</option>
              </select>
            </div>
            <div v-if="formData.price > 0" class="mf-preview">
              <div class="mfp-row"><span>Preço de venda</span><strong>{{ formatCurrency(formData.price) }}</strong></div>
              <div v-if="formData.stock > 0" class="mfp-row"><span>Valor total em estoque</span><strong>{{ formatCurrency(formData.price * formData.stock) }}</strong></div>
            </div>
          </div>
        </div>
        <div class="modal-foot">
          <button class="mf-btn-cancel" @click="closeModal">Cancelar</button>
          <button class="mf-btn-save" @click="saveProduct" :disabled="saving">
            <span v-if="saving" class="mf-spinner"></span>
            <span v-else>{{ editingProduct ? 'Salvar Alterações' : 'Cadastrar Produto' }}</span>
          </button>
        </div>
      </div>
    </div>

    <!-- MODAL CATEGORIA -->
    <div v-if="showCatModal" class="modal-backdrop" @click.self="showCatModal = false">
      <div class="modal-panel modal-panel--sm">
        <div class="modal-head">
          <div><h2 class="modal-title">Nova Categoria</h2><p class="modal-sub">Organize seus produtos por categoria</p></div>
          <button class="modal-close" @click="showCatModal = false">✕</button>
        </div>
        <div class="modal-cat-body">
          <div class="mf-group">
            <label class="mf-label">Nome da Categoria</label>
            <input v-model="newCategoryName" class="mf-input" placeholder="Ex: Eletrônicos, Roupas..."/>
          </div>
        </div>
        <div class="modal-foot">
          <button class="mf-btn-cancel" @click="showCatModal = false">Cancelar</button>
          <button class="mf-btn-save" @click="saveCategory">Criar Categoria</button>
        </div>
      </div>
    </div>

    <!-- MODAL DELETAR -->
    <div v-if="showDeleteModal" class="modal-backdrop" @click.self="showDeleteModal = false">
      <div class="modal-panel modal-panel--sm">
        <div class="modal-del-body">
          <div class="del-icon">⚠</div>
          <h2 class="modal-title" style="margin-top:16px">Excluir Produto?</h2>
          <p class="modal-sub" style="margin-top:8px">Tem certeza que deseja excluir <strong>{{ deletingProduct?.nome }}</strong>? Esta ação não pode ser desfeita.</p>
        </div>
        <div class="modal-foot">
          <button class="mf-btn-cancel" @click="showDeleteModal = false">Cancelar</button>
          <button class="mf-btn-delete" @click="deleteProduct">Excluir</button>
        </div>
      </div>
    </div>

    <transition name="toast-slide">
      <div v-if="toast.show" class="pm-toast" :class="`pm-toast--${toast.type}`">{{ toast.message }}</div>
    </transition>

  </div>
</template>

<script setup>
import { ref, computed, reactive } from 'vue'
import { useProductStore } from '../stores/product'

const productStore = useProductStore()
const searchTerm = ref('')
const selectedCategory = ref('all')
const viewMode = ref('grid')
const showModal = ref(false)
const showCatModal = ref(false)
const showDeleteModal = ref(false)
const editingProduct = ref(null)
const deletingProduct = ref(null)
const saving = ref(false)
const newCategoryName = ref('')
const fileInput = ref(null)
const formData = reactive({ name: '', description: '', price: 0, stock: 0, category: '', image: '', destaque: false })
const errors = reactive({ name: '', price: '', stock: '' })
const toast = reactive({ show: false, message: '', type: 'success' })
const products = ref([])
const categories = ref([
  { name: 'Eletrônicos' }, { name: 'Moda' }, { name: 'Casa' }, { name: 'Esportes' }, { name: 'Beleza' }
])

const categoryTabs = computed(() => ['all', ...categories.value.map(c => c.name)])
const filteredProducts = computed(() =>
    products.value.filter(p => {
      const matchSearch = p.nome?.toLowerCase().includes(searchTerm.value.toLowerCase())
      const matchCat = selectedCategory.value === 'all' || p.categoria === selectedCategory.value
      return matchSearch && matchCat
    })
)
const totalStock = computed(() => products.value.reduce((s, p) => s + (p.estoque || 0), 0))
const totalValue = computed(() => products.value.reduce((s, p) => s + ((p.preco || 0) * (p.estoque || 0)), 0))
const lowStockCount = computed(() => products.value.filter(p => p.estoque < 10).length)
const formatCurrency = v => v.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
const getStockLabel = s => s === 0 ? 'Esgotado' : s < 10 ? 'Crítico' : s < 20 ? 'Baixo' : 'Normal'
const getStockBadgeClass = s => s < 10 ? 'badge--red' : s < 20 ? 'badge--amber' : 'badge--green'

const showToast = (message, type = 'success') => {
  Object.assign(toast, { show: true, message, type })
  setTimeout(() => { toast.show = false }, 3000)
}
const openModal = (product = null) => {
  editingProduct.value = product
  if (product) {
    Object.assign(formData, { name: product.nome || '', description: product.descricao || '', price: product.preco || 0, stock: product.estoque || 0, category: product.categoria || '', image: product.imagem || '', destaque: product.destaque || false })
  } else {
    Object.assign(formData, { name: '', description: '', price: 0, stock: 0, category: '', image: '', destaque: false })
  }
  errors.name = errors.price = errors.stock = ''
  showModal.value = true
}
const closeModal = () => { showModal.value = false; editingProduct.value = null }
const validate = () => {
  errors.name = formData.name.trim() ? '' : 'Nome é obrigatório'
  errors.price = formData.price > 0 ? '' : 'Informe um preço válido'
  errors.stock = formData.stock >= 0 ? '' : 'Estoque inválido'
  return !errors.name && !errors.price && !errors.stock
}
const saveProduct = async () => {
  if (!validate()) return
  saving.value = true
  const payload = { nome: formData.name, descricao: formData.description, preco: parseFloat(formData.price), estoque: parseInt(formData.stock), categoria: formData.category || 'Sem categoria', imagem: formData.image, ativo: true, destaque: formData.destaque }
  try {
    if (editingProduct.value) {
      await productStore.updateProduct(editingProduct.value.id, payload)
      const idx = products.value.findIndex(p => p.id === editingProduct.value.id)
      if (idx !== -1) products.value[idx] = { ...products.value[idx], ...payload }
      showToast('Produto atualizado!')
    } else {
      const created = await productStore.createProduct(payload)
      products.value.unshift(created || { id: Date.now(), ...payload })
      showToast('Produto cadastrado!')
    }
    closeModal()
  } catch { showToast('Erro ao salvar produto.', 'error') }
  finally { saving.value = false }
}
const confirmDelete = p => { deletingProduct.value = p; showDeleteModal.value = true }
const deleteProduct = async () => {
  try {
    await productStore.deleteProduct(deletingProduct.value.id)
    products.value = products.value.filter(p => p.id !== deletingProduct.value.id)
    showToast('Produto excluído.')
    showDeleteModal.value = false
  } catch { showToast('Erro ao excluir.', 'error') }
}
const saveCategory = () => {
  if (!newCategoryName.value.trim()) return
  categories.value.push({ name: newCategoryName.value.trim() })
  newCategoryName.value = ''
  showCatModal.value = false
  showToast('Categoria criada!')
}
const triggerFileInput = () => fileInput.value?.click()
const handleFileChange = e => {
  const file = e.target.files[0]
  if (!file) return
  const r = new FileReader()
  r.onload = ev => { formData.image = ev.target.result }
  r.readAsDataURL(file)
}
const handleDrop = e => {
  const file = e.dataTransfer.files[0]
  if (!file || !file.type.startsWith('image/')) return
  const r = new FileReader()
  r.onload = ev => { formData.image = ev.target.result }
  r.readAsDataURL(file)
}
const init = async () => {
  await productStore.fetchProducts()
  products.value = productStore.products || []
}
init()
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=DM+Mono:wght@500&display=swap');
*, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

.pm-root {
  --bg: #F7F8FA; --surface: #FFFFFF; --border: #E8ECF0; --border-md: #D1D9E0;
  --text-1: #0D1117; --text-2: #4A5568; --text-3: #8A95A3;
  --accent: #2563EB; --accent-hv: #1D4ED8; --accent-lt: #EFF6FF; --red: #DC2626;
  --radius: 12px; --radius-lg: 16px;
  --shadow-sm: 0 1px 3px rgba(0,0,0,.06); --shadow-md: 0 4px 12px rgba(0,0,0,.08);
  --shadow-lg: 0 20px 48px rgba(0,0,0,.18);
  font-family: 'DM Sans', sans-serif; background: var(--bg); color: var(--text-1);
  padding: 32px; display: flex; flex-direction: column; gap: 24px; position: relative;
}

.pm-topbar { display: flex; align-items: flex-start; justify-content: space-between; gap: 16px; flex-wrap: wrap; }
.tb-title { font-size: 24px; font-weight: 700; letter-spacing: -.03em; }
.tb-breadcrumb { display: flex; align-items: center; gap: 6px; font-size: 13px; color: var(--text-3); margin-top: 4px; }
.tb-sep { color: var(--border-md); }
.tb-current { color: var(--text-2); }
.tb-right { display: flex; align-items: center; gap: 12px; }
.tb-search { display: flex; align-items: center; gap: 8px; background: var(--surface); border: 1px solid var(--border); border-radius: 10px; padding: 0 14px; height: 40px; color: var(--text-3); transition: border-color .2s; }
.tb-search:focus-within { border-color: var(--accent); }
.tb-search-input { border: none; outline: none; font: 400 14px 'DM Sans',sans-serif; color: var(--text-1); background: transparent; width: 200px; }
.tb-search-input::placeholder { color: var(--text-3); }
.tb-btn-new { display: flex; align-items: center; gap: 8px; background: var(--accent); color: white; border: none; border-radius: 10px; padding: 0 18px; height: 40px; font: 600 14px 'DM Sans',sans-serif; cursor: pointer; transition: background .2s, transform .1s; white-space: nowrap; }
.tb-btn-new:hover { background: var(--accent-hv); transform: translateY(-1px); }

.pm-stats { display: grid; grid-template-columns: repeat(4,1fr); gap: 16px; }
.stat-card { background: var(--surface); border: 1px solid var(--border); border-radius: var(--radius-lg); padding: 20px; display: flex; align-items: center; gap: 16px; box-shadow: var(--shadow-sm); transition: box-shadow .2s, transform .2s; }
.stat-card:hover { box-shadow: var(--shadow-md); transform: translateY(-2px); }
.stat-icon { width: 44px; height: 44px; border-radius: 12px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
.stat-icon--blue { background: #EFF6FF; color: #2563EB; }
.stat-icon--emerald { background: #ECFDF5; color: #059669; }
.stat-icon--amber { background: #FFFBEB; color: #D97706; }
.stat-icon--red { background: #FEF2F2; color: #DC2626; }
.stat-body { flex: 1; }
.stat-value { display: block; font-size: 20px; font-weight: 700; letter-spacing: -.02em; }
.stat-label { display: block; font-size: 12px; color: var(--text-3); font-weight: 500; margin-top: 2px; }
.stat-trend { font-size: 12px; font-weight: 600; padding: 3px 8px; border-radius: 6px; white-space: nowrap; }
.trend-up { background: #ECFDF5; color: #059669; }
.trend-down { background: #FEF2F2; color: #DC2626; }

.pm-toolbar { display: flex; align-items: center; justify-content: space-between; background: var(--surface); border: 1px solid var(--border); border-radius: var(--radius); padding: 12px 16px; }
.pt-filters { display: flex; gap: 4px; flex-wrap: wrap; }
.pt-tab { border: none; background: transparent; padding: 6px 14px; border-radius: 7px; font: 500 13px 'DM Sans',sans-serif; color: var(--text-2); cursor: pointer; transition: all .15s; }
.pt-tab:hover { background: var(--bg); }
.pt-tab--active { background: var(--accent-lt); color: var(--accent); font-weight: 600; }
.pt-actions { display: flex; align-items: center; gap: 8px; }
.pt-view { width: 34px; height: 34px; border: 1px solid var(--border); border-radius: 8px; background: transparent; color: var(--text-3); cursor: pointer; display: flex; align-items: center; justify-content: center; transition: all .15s; }
.pt-view:hover { color: var(--text-1); border-color: var(--border-md); }
.pt-view--active { background: var(--accent); color: white; border-color: var(--accent); }
.pt-btn-cat { display: flex; align-items: center; gap: 6px; border: 1px solid var(--border); background: transparent; border-radius: 8px; padding: 0 14px; height: 34px; font: 500 13px 'DM Sans',sans-serif; color: var(--text-2); cursor: pointer; transition: all .15s; }
.pt-btn-cat:hover { border-color: var(--border-md); color: var(--text-1); }

.pm-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(240px,1fr)); gap: 16px; }
.pg-card { background: var(--surface); border: 1px solid var(--border); border-radius: var(--radius-lg); overflow: hidden; box-shadow: var(--shadow-sm); transition: box-shadow .25s, transform .25s; }
.pg-card:hover { box-shadow: var(--shadow-md); transform: translateY(-3px); }
.pg-card--featured { border-color: #FBBF24; }
.pg-image-wrap { position: relative; aspect-ratio: 4/3; overflow: hidden; background: var(--bg); }
.pg-image { width: 100%; height: 100%; object-fit: cover; transition: transform .35s; display: block; }
.pg-card:hover .pg-image { transform: scale(1.06); }
.pg-badge-wrap { position: absolute; top: 10px; left: 10px; display: flex; flex-direction: column; gap: 4px; }
.pg-badge { display: inline-block; font-size: 11px; font-weight: 600; padding: 3px 8px; border-radius: 6px; }
.pg-badge--star { background: #FFFBEB; color: #92400E; }
.badge--green { background: #ECFDF5; color: #065F46; }
.badge--amber { background: #FFFBEB; color: #92400E; }
.badge--red { background: #FEF2F2; color: #991B1B; }
.pg-overlay { position: absolute; inset: 0; background: rgba(0,0,0,.5); display: flex; align-items: center; justify-content: center; gap: 10px; opacity: 0; transition: opacity .2s; }
.pg-card:hover .pg-overlay { opacity: 1; }
.pg-ov-btn { display: flex; align-items: center; gap: 6px; background: white; border: none; border-radius: 8px; padding: 8px 14px; font: 600 13px 'DM Sans',sans-serif; color: var(--text-1); cursor: pointer; transition: transform .1s; }
.pg-ov-btn:hover { transform: scale(1.05); }
.pg-ov-btn--danger { color: var(--red); }
.pg-body { padding: 16px; }
.pg-category { font-size: 11px; font-weight: 600; text-transform: uppercase; letter-spacing: .06em; color: var(--accent); }
.pg-name { font-size: 15px; font-weight: 600; margin: 4px 0 6px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.pg-desc { font-size: 12.5px; color: var(--text-2); line-height: 1.5; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; min-height: 38px; }
.pg-footer { display: flex; justify-content: space-between; align-items: center; margin-top: 12px; padding-top: 12px; border-top: 1px solid var(--border); }
.pg-price { font-size: 16px; font-weight: 700; color: var(--accent); }
.pg-stock { font-size: 12px; font-weight: 500; color: var(--text-3); font-family: 'DM Mono',monospace; }

.pm-list { background: var(--surface); border: 1px solid var(--border); border-radius: var(--radius-lg); overflow: hidden; }
.pl-table { width: 100%; border-collapse: collapse; }
.pl-table thead tr { background: var(--bg); }
.pl-table th { text-align: left; padding: 14px 16px; font-size: 12px; font-weight: 600; text-transform: uppercase; letter-spacing: .06em; color: var(--text-3); border-bottom: 1px solid var(--border); }
.pl-row { border-bottom: 1px solid var(--border); transition: background .12s; }
.pl-row:last-child { border-bottom: none; }
.pl-row:hover { background: var(--bg); }
.pl-table td { padding: 14px 16px; vertical-align: middle; }
.pl-product { display: flex; align-items: center; gap: 12px; }
.pl-thumb { width: 44px; height: 44px; border-radius: 8px; object-fit: cover; border: 1px solid var(--border); flex-shrink: 0; }
.pl-name { font-size: 14px; font-weight: 600; }
.pl-desc-sm { font-size: 12px; color: var(--text-3); white-space: nowrap; overflow: hidden; text-overflow: ellipsis; max-width: 220px; }
.pl-chip { font-size: 12px; font-weight: 500; padding: 3px 10px; border-radius: 6px; border: 1px solid var(--border); color: var(--text-2); }
.pl-price-col { font-weight: 600; font-size: 14px; font-family: 'DM Mono',monospace; }
.pl-stock-col { font-family: 'DM Mono',monospace; font-weight: 500; font-size: 14px; }
.pl-status { font-size: 12px; font-weight: 600; padding: 4px 10px; border-radius: 6px; }
.pl-actions { display: flex; gap: 6px; }
.pl-btn { width: 32px; height: 32px; border: 1px solid var(--border); border-radius: 8px; background: transparent; color: var(--text-2); cursor: pointer; display: flex; align-items: center; justify-content: center; transition: all .15s; font-size: 14px; }
.pl-btn:hover { border-color: var(--accent); color: var(--accent); }
.pl-btn--danger:hover { border-color: var(--red); color: var(--red); }
.pl-empty { text-align: center; padding: 48px; color: var(--text-3); }
.pm-empty { grid-column: 1/-1; display: flex; flex-direction: column; align-items: center; gap: 16px; padding: 80px; color: var(--text-3); }
.empty-text { font-size: 15px; }

/* MODAIS - position fixed garante que aparecem sobre tudo */
.modal-backdrop { position: fixed; inset: 0; background: rgba(0,0,0,.55); backdrop-filter: blur(4px); display: flex; align-items: center; justify-content: center; z-index: 9000; padding: 24px; }
.modal-panel { background: var(--surface); border-radius: 20px; width: 100%; max-width: 860px; max-height: 90vh; overflow: hidden; display: flex; flex-direction: column; box-shadow: var(--shadow-lg); animation: modalIn .25s cubic-bezier(.16,1,.3,1); }
.modal-panel--sm { max-width: 440px; }
@keyframes modalIn { from { opacity: 0; transform: translateY(20px) scale(.97); } to { opacity: 1; transform: translateY(0) scale(1); } }
.modal-head { display: flex; justify-content: space-between; align-items: flex-start; padding: 28px 32px; border-bottom: 1px solid var(--border); flex-shrink: 0; }
.modal-title { font-size: 18px; font-weight: 700; letter-spacing: -.02em; }
.modal-sub { font-size: 13px; color: var(--text-2); margin-top: 4px; }
.modal-close { width: 36px; height: 36px; border: 1px solid var(--border); border-radius: 10px; background: transparent; color: var(--text-2); cursor: pointer; display: flex; align-items: center; justify-content: center; transition: all .15s; flex-shrink: 0; font-size: 16px; }
.modal-close:hover { background: var(--bg); color: var(--text-1); }
.modal-body { display: grid; grid-template-columns: 280px 1fr; overflow-y: auto; flex: 1; }
.modal-col-img { padding: 28px 24px 28px 32px; border-right: 1px solid var(--border); display: flex; flex-direction: column; gap: 20px; }
.modal-col-form { padding: 28px 32px; display: flex; flex-direction: column; gap: 20px; }
.modal-cat-body { padding: 24px 32px; }
.modal-del-body { padding: 48px 32px 24px; text-align: center; }
.modal-foot { display: flex; justify-content: flex-end; gap: 12px; padding: 20px 32px; border-top: 1px solid var(--border); flex-shrink: 0; }

.img-drop { position: relative; border: 2px dashed var(--border-md); border-radius: var(--radius); aspect-ratio: 4/3; cursor: pointer; overflow: hidden; transition: border-color .2s, background .2s; background: var(--bg); }
.img-drop:hover { border-color: var(--accent); background: var(--accent-lt); }
.img-drop--has { border-style: solid; border-color: var(--border); }
.img-preview { width: 100%; height: 100%; object-fit: cover; display: block; }
.img-placeholder { position: absolute; inset: 0; display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 8px; }
.img-hint { font-size: 13px; color: var(--text-2); font-weight: 500; }
.img-hint span { color: var(--accent); }
.img-sub { font-size: 11px; color: var(--text-3); }
.img-remove { position: absolute; top: 8px; right: 8px; width: 28px; height: 28px; background: rgba(0,0,0,.6); color: white; border: none; border-radius: 7px; cursor: pointer; font-size: 13px; display: flex; align-items: center; justify-content: center; }

.destaque-toggle { display: flex; align-items: center; justify-content: space-between; background: var(--bg); border: 1px solid var(--border); border-radius: var(--radius); padding: 14px; cursor: pointer; transition: border-color .2s; user-select: none; }
.destaque-toggle:hover { border-color: var(--accent); }
.dt-info { display: flex; align-items: center; gap: 10px; }
.dt-icon { font-size: 18px; color: #FBBF24; }
.dt-title { font-size: 13px; font-weight: 600; }
.dt-sub { font-size: 11px; color: var(--text-3); margin-top: 2px; }
.dt-switch { width: 40px; height: 22px; background: var(--border-md); border-radius: 11px; position: relative; transition: background .2s; flex-shrink: 0; }
.dt-switch--on { background: var(--accent); }
.dt-thumb { position: absolute; top: 3px; left: 3px; width: 16px; height: 16px; background: white; border-radius: 50%; transition: left .2s; box-shadow: 0 1px 3px rgba(0,0,0,.2); }
.dt-switch--on .dt-thumb { left: 21px; }

.mf-label { font-size: 13px; font-weight: 600; color: var(--text-1); margin-bottom: 8px; display: block; }
.mf-req { color: var(--red); margin-left: 2px; }
.mf-group { display: flex; flex-direction: column; }
.mf-row { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
.mf-input { border: 1.5px solid var(--border); border-radius: 10px; padding: 10px 14px; font: 500 14px 'DM Sans',sans-serif; color: var(--text-1); background: var(--surface); transition: border-color .2s, box-shadow .2s; outline: none; width: 100%; }
.mf-input:focus { border-color: var(--accent); box-shadow: 0 0 0 3px rgba(37,99,235,.1); }
.mf-input--error { border-color: var(--red) !important; }
.mf-textarea { resize: vertical; min-height: 100px; font-family: inherit; }
.mf-select { appearance: none; background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' viewBox='0 0 24 24' fill='none' stroke='%238A95A3' stroke-width='2'%3E%3Cpath d='M6 9l6 6 6-6'/%3E%3C/svg%3E"); background-repeat: no-repeat; background-position: right 14px center; padding-right: 40px; cursor: pointer; }
.mf-input-wrap { position: relative; }
.mf-prefix { position: absolute; left: 14px; top: 50%; transform: translateY(-50%); font-size: 13px; font-weight: 600; color: var(--text-3); pointer-events: none; }
.mf-input--prefixed { padding-left: 36px; }
.mf-error { font-size: 12px; color: var(--red); margin-top: 5px; font-weight: 500; }
.mf-preview { background: var(--bg); border: 1px solid var(--border); border-radius: 10px; padding: 14px; display: flex; flex-direction: column; gap: 8px; }
.mfp-row { display: flex; justify-content: space-between; font-size: 13px; color: var(--text-2); }
.mfp-row strong { color: var(--text-1); font-weight: 600; }

.mf-btn-cancel { height: 40px; padding: 0 20px; border: 1.5px solid var(--border); border-radius: 10px; background: transparent; font: 600 14px 'DM Sans',sans-serif; color: var(--text-2); cursor: pointer; transition: all .15s; }
.mf-btn-cancel:hover { border-color: var(--border-md); color: var(--text-1); }
.mf-btn-save { height: 40px; padding: 0 24px; background: var(--accent); color: white; border: none; border-radius: 10px; font: 600 14px 'DM Sans',sans-serif; cursor: pointer; transition: background .15s; display: flex; align-items: center; gap: 8px; }
.mf-btn-save:hover { background: var(--accent-hv); }
.mf-btn-save:disabled { opacity: .65; cursor: not-allowed; }
.mf-btn-delete { height: 40px; padding: 0 24px; background: var(--red); color: white; border: none; border-radius: 10px; font: 600 14px 'DM Sans',sans-serif; cursor: pointer; }
.mf-btn-delete:hover { opacity: .9; }
.mf-spinner { width: 16px; height: 16px; border: 2px solid rgba(255,255,255,.3); border-top-color: white; border-radius: 50%; animation: spin .7s linear infinite; }
@keyframes spin { to { transform: rotate(360deg); } }
.del-icon { font-size: 48px; color: var(--red); }

.pm-toast { position: fixed; bottom: 28px; right: 28px; z-index: 9999; padding: 14px 20px; border-radius: 12px; font: 600 14px 'DM Sans',sans-serif; box-shadow: var(--shadow-lg); color: white; }
.pm-toast--success { background: #065F46; }
.pm-toast--error { background: #991B1B; }
.toast-slide-enter-active, .toast-slide-leave-active { transition: all .3s; }
.toast-slide-enter-from, .toast-slide-leave-to { opacity: 0; transform: translateY(12px); }

@media (max-width: 1024px) { .pm-stats { grid-template-columns: repeat(2,1fr); } }
@media (max-width: 768px) {
  .pm-root { padding: 16px; }
  .modal-body { grid-template-columns: 1fr; }
  .modal-col-img { border-right: none; border-bottom: 1px solid var(--border); }
  .pm-stats { grid-template-columns: 1fr 1fr; }
}
</style>
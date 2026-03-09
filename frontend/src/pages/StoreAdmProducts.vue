<template>
  <div class="admin-products">
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">Catálogo de Produtos</h1>
        <p class="page-subtitle">{{ productStore.totalProducts }} produtos cadastrados</p>
      </div>
      <button class="btn-new" @click="openCreate">
        <span class="btn-icon">＋</span>
        Novo Produto
      </button>
    </div>

    <div class="filters-bar">
      <div class="search-wrap">
        <svg class="search-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="11" cy="11" r="8"/><path d="m21 21-4.35-4.35"/>
        </svg>
        <input
            v-model="search"
            class="search-input"
            placeholder="Buscar produto..."
        />
      </div>

      <div class="filter-tabs">
        <button
            v-for="tab in tabs"
            :key="tab.value"
            class="tab-btn"
            :class="{ active: activeTab === tab.value }"
            @click="activeTab = tab.value"
        >
          {{ tab.label }}
        </button>
      </div>
    </div>

    <div v-if="productStore.loading" class="loading-grid">
      <div v-for="i in 6" :key="i" class="skeleton-card" />
    </div>

    <div v-else-if="filtered.length === 0" class="empty-state">
      <div class="empty-icon">📦</div>
      <p class="empty-title">Nenhum produto encontrado</p>
      <p class="empty-sub">Cadastre seu primeiro produto clicando em "Novo Produto"</p>
    </div>

    <div v-else class="products-grid">
      <div
          v-for="product in filtered"
          :key="product.id"
          class="product-card"
          :class="{ inactive: !product.ativo }"
      >
        <div class="card-img-wrap">
          <img
              v-if="product.imagem"
              :src="product.imagem"
              :alt="product.nome"
              class="card-img"
              @error="e => e.target.style.display='none'"
          />
          <div v-else class="card-img-placeholder">
            <span>📷</span>
          </div>

          <div v-if="product.destaque" class="badge-destaque">Destaque</div>
          <div v-if="!product.ativo" class="badge-inactive">Inativo</div>

          <div class="card-actions-overlay">
            <button class="action-btn edit" @click="openEdit(product)" title="Editar">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
                <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
              </svg>
            </button>
            <button class="action-btn delete" @click="confirmDelete(product)" title="Excluir">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <polyline points="3,6 5,6 21,6"/><path d="M19,6l-1,14H6L5,6"/>
                <path d="M10,11v6"/><path d="M14,11v6"/>
                <path d="M9,6V4h6v2"/>
              </svg>
            </button>
          </div>
        </div>

        <div class="card-body">
          <div class="card-category">{{ product.categoria || 'Sem categoria' }}</div>
          <h3 class="card-name">{{ product.nome }}</h3>
          <p class="card-desc">{{ product.descricao || 'Sem descrição' }}</p>

          <div class="card-footer">
            <div class="card-price">R$ {{ formatPrice(product.preco) }}</div>
            <div class="card-stock" :class="stockClass(product.estoque)">
              {{ product.estoque }} un.
            </div>
          </div>
        </div>
      </div>
    </div>

    <Teleport to="body">
      <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
        <div class="modal-box">
          <div class="modal-header">
            <h2 class="modal-title">{{ editing ? 'Editar Produto' : 'Novo Produto' }}</h2>
            <button class="modal-close" @click="closeModal">✕</button>
          </div>

          <form class="modal-form" @submit.prevent="handleSubmit">

            <div class="field">
              <label class="label">Nome do produto *</label>
              <input v-model="form.nome" class="input" placeholder="Ex: Camiseta Premium" required />
            </div>

            <div class="field-row">
              <div class="field">
                <label class="label">Preço (R$) *</label>
                <input v-model="form.preco" type="number" step="0.01" min="0" class="input" placeholder="0,00" required />
              </div>
              <div class="field">
                <label class="label">Estoque *</label>
                <input v-model="form.estoque" type="number" min="0" class="input" placeholder="0" required />
              </div>
            </div>

            <div class="field">
              <label class="label">Categoria</label>
              <input v-model="form.categoria" class="input" placeholder="Ex: Roupas, Eletrônicos..." />
            </div>

            <div class="field">
              <label class="label">Descrição</label>
              <textarea v-model="form.descricao" class="input textarea" rows="3" placeholder="Descreva o produto..." />
            </div>

            <div class="field">
              <label class="label">URL da Imagem</label>
              <input v-model="form.imagem" class="input" placeholder="https://..." />
              <div v-if="form.imagem" class="img-preview">
                <img :src="form.imagem" alt="preview" @error="e => e.target.style.display='none'" />
              </div>
            </div>

            <div class="field-row toggles">
              <label class="toggle-label">
                <div class="toggle-switch" :class="{ on: form.ativo }" @click="form.ativo = !form.ativo" />
                <span>Produto ativo</span>
              </label>
              <label class="toggle-label">
                <div class="toggle-switch" :class="{ on: form.destaque }" @click="form.destaque = !form.destaque" />
                <span>Em destaque</span>
              </label>
            </div>

            <p v-if="formError" class="form-error">{{ formError }}</p>

            <div class="modal-actions">
              <button type="button" class="btn-cancel" @click="closeModal">Cancelar</button>
              <button type="submit" class="btn-save" :disabled="productStore.loading">
                <span v-if="productStore.loading" class="spinner" />
                <span v-else>{{ editing ? 'Salvar alterações' : 'Cadastrar produto' }}</span>
              </button>
            </div>
          </form>
        </div>
      </div>
    </Teleport>

    <Teleport to="body">
      <div v-if="showDelete" class="modal-overlay" @click.self="showDelete = false">
        <div class="modal-box modal-delete">
          <div class="delete-icon">🗑️</div>
          <h3 class="delete-title">Excluir produto?</h3>
          <p class="delete-text">
            <strong>{{ deleteTarget?.nome }}</strong> será desativado do catálogo.
            Os droppers que já têm este produto nas lojas não serão afetados.
          </p>
          <div class="modal-actions">
            <button class="btn-cancel" @click="showDelete = false">Cancelar</button>
            <button class="btn-delete" :disabled="productStore.loading" @click="handleDelete">
              <span v-if="productStore.loading" class="spinner" />
              <span v-else>Confirmar exclusão</span>
            </button>
          </div>
        </div>
      </div>
    </Teleport>

    <!-- Toast -->
    <Teleport to="body">
      <transition name="toast">
        <div v-if="toast.show" class="toast" :class="toast.type">
          {{ toast.message }}
        </div>
      </transition>
    </Teleport>

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useProductStore } from '@/stores/product'

const productStore = useProductStore()

const search    = ref('')
const activeTab = ref('all')
const showModal = ref(false)
const showDelete = ref(false)
const editing   = ref(null)   // product object when editing
const deleteTarget = ref(null)
const formError = ref('')

const toast = ref({ show: false, type: 'success', message: '' })

const emptyForm = () => ({
  nome: '',
  preco: '',
  estoque: '',
  descricao: '',
  imagem: '',
  categoria: '',
  ativo: true,
  destaque: false,
})

const form = ref(emptyForm())

const tabs = [
  { label: 'Todos',    value: 'all' },
  { label: 'Ativos',   value: 'active' },
  { label: 'Inativos', value: 'inactive' },
  { label: 'Destaque', value: 'destaque' },
]

const filtered = computed(() => {
  let list = productStore.products

  if (activeTab.value === 'active')   list = list.filter(p => p.ativo)
  if (activeTab.value === 'inactive') list = list.filter(p => !p.ativo)
  if (activeTab.value === 'destaque') list = list.filter(p => p.destaque)

  if (search.value.trim()) {
    const q = search.value.toLowerCase()
    list = list.filter(p =>
        p.nome?.toLowerCase().includes(q) ||
        p.categoria?.toLowerCase().includes(q) ||
        p.descricao?.toLowerCase().includes(q)
    )
  }
  return list
})

const formatPrice = (v) => Number(v).toFixed(2).replace('.', ',')

const stockClass = (estoque) => {
  if (estoque === 0) return 'stock-zero'
  if (estoque < 10)  return 'stock-low'
  return 'stock-ok'
}

const showToast = (message, type = 'success') => {
  toast.value = { show: true, type, message }
  setTimeout(() => toast.value.show = false, 3000)
}

const openCreate = () => {
  editing.value = null
  form.value = emptyForm()
  formError.value = ''
  showModal.value = true
}

const openEdit = (product) => {
  editing.value = product
  form.value = { ...product }
  formError.value = ''
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
  editing.value = null
  formError.value = ''
}

const confirmDelete = (product) => {
  deleteTarget.value = product
  showDelete.value = true
}

const handleSubmit = async () => {
  formError.value = ''
  try {
    const payload = {
      nome:      form.value.nome,
      preco:     Number(form.value.preco),
      estoque:   Number(form.value.estoque),
      descricao: form.value.descricao,
      imagem:    form.value.imagem,
      categoria: form.value.categoria,
      ativo:     form.value.ativo,
      destaque:  form.value.destaque,
    }

    if (editing.value) {
      await productStore.updateProduct(editing.value.id, payload)
      showToast('Produto atualizado com sucesso!')
    } else {
      await productStore.createProduct(payload)
      showToast('Produto cadastrado com sucesso!')
    }
    closeModal()
  } catch (err) {
    formError.value = err.response?.data?.message || 'Erro ao salvar produto. Tente novamente.'
  }
}

const handleDelete = async () => {
  try {
    await productStore.deleteProduct(deleteTarget.value.id)
    showDelete.value = false
    showToast('Produto removido do catálogo.', 'info')
  } catch {
    showToast('Erro ao excluir produto.', 'error')
  }
}

onMounted(() => productStore.fetchProducts())
</script>

<style scoped>
@import url('[https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap](https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap)');

.admin-products {
  padding: 40px 32px;
  max-width: 1280px;
  margin: 0 auto;
  font-family: 'Inter', system-ui, sans-serif;
  color: #0F172A;
  background: #F8FAFC;
  min-height: 100vh;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 40px;
}

.page-title {
  font-size: 36px;
  font-weight: 800;
  letter-spacing: -1px;
  margin: 0 0 6px;
  color: #0F172A;
}

.page-subtitle {
  color: #64748B;
  font-size: 16px;
  font-weight: 500;
  margin: 0;
}

.btn-new {
  background: #0F172A;
  color: #FFFFFF;
  border: none;
  padding: 14px 28px;
  border-radius: 16px;
  font-weight: 700;
  font-size: 15px;
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 8px 20px rgba(15, 23, 42, 0.1);
}

.btn-new:hover {
  background: #1E293B;
  transform: translateY(-3px);
  box-shadow: 0 12px 28px rgba(15, 23, 42, 0.15);
}

.btn-icon {
  background: rgba(255, 255, 255, 0.15);
  border-radius: 50%;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
}

.filters-bar {
  display: flex;
  gap: 20px;
  margin-bottom: 32px;
  align-items: center;
  flex-wrap: wrap;
  background: #FFFFFF;
  padding: 16px;
  border-radius: 24px;
  box-shadow: 0 10px 30px -10px rgba(15, 23, 42, 0.05);
  border: 1px solid #F1F5F9;
}

.search-wrap {
  display: flex;
  align-items: center;
  background: #F8FAFC;
  border-radius: 16px;
  padding: 0 20px;
  flex: 1;
  min-width: 280px;
  border: 1px solid #E2E8F0;
  transition: all 0.2s;
}

.search-wrap:focus-within {
  border-color: #3B82F6;
  box-shadow: 0 0 0 4px rgba(59, 130, 246, 0.1);
  background: #FFFFFF;
}

.search-icon {
  width: 20px;
  height: 20px;
  color: #94A3B8;
  margin-right: 12px;
}

.search-input {
  border: none;
  background: transparent;
  padding: 16px 0;
  width: 100%;
  outline: none;
  font-size: 15px;
  font-weight: 500;
  color: #0F172A;
}

.filter-tabs {
  display: flex;
  gap: 6px;
  background: #F1F5F9;
  padding: 6px;
  border-radius: 16px;
}

.tab-btn {
  padding: 10px 20px;
  border: none;
  background: transparent;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 600;
  color: #64748B;
  cursor: pointer;
  transition: all 0.2s;
}

.tab-btn.active {
  background: #FFFFFF;
  color: #0F172A;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.tab-btn:hover:not(.active) {
  color: #0F172A;
}

.delete-title { font-size: 22px; font-weight: 700; margin-bottom: 12px; color: #0F172A; }
.delete-text { font-size: 15px; color: #64748B; line-height: 1.6; margin-bottom: 24px; }
.btn-delete {
  padding: 14px 28px;
  background: #EF4444;
  color: #fff;
  border: none;
  border-radius: 14px;
  font-family: inherit;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}
.btn-delete:hover { background: #DC2626; transform: translateY(-2px); box-shadow: 0 8px 16px rgba(239, 68, 68, 0.2); }

::v-deep(.product-card), ::v-deep(tr) {
  background: #FFFFFF;
  border-radius: 20px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid #F1F5F9;
}
::v-deep(.product-card:hover), ::v-deep(tr:hover) {
  box-shadow: 0 16px 32px -12px rgba(15, 23, 42, 0.08);
  transform: translateY(-2px);
  border-color: #E2E8F0;
  z-index: 2;
}
</style>
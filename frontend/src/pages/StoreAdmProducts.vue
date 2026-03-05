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
@import url('https://fonts.googleapis.com/css2?family=Geist:wght@300;400;500;600;700&family=Instrument+Serif:ital@0;1&display=swap');

* { box-sizing: border-box; margin: 0; padding: 0; }

.admin-products {
  font-family: 'Geist', sans-serif;
  padding: 32px;
  min-height: 100vh;
  background: #F8FAFC;
  color: #0F172A;
}

/* ── Header ──────────────────────────────────────────────────────────── */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 28px;
}

.page-title {
  font-family: 'Instrument Serif', serif;
  font-size: 32px;
  font-weight: 400;
  color: #0F172A;
  line-height: 1.1;
}

.page-subtitle {
  margin-top: 4px;
  font-size: 14px;
  color: #64748B;
}

.btn-new {
  display: flex;
  align-items: center;
  gap: 8px;
  background: #0F172A;
  color: #fff;
  border: none;
  border-radius: 10px;
  padding: 11px 20px;
  font-family: 'Geist', sans-serif;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: background .2s, transform .15s;
}
.btn-new:hover { background: #1E293B; transform: translateY(-1px); }
.btn-icon { font-size: 18px; line-height: 1; }

/* ── Filters ──────────────────────────────────────────────────────────── */
.filters-bar {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}

.search-wrap {
  position: relative;
  flex: 1;
  min-width: 200px;
}
.search-icon {
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  width: 16px;
  height: 16px;
  color: #94A3B8;
}
.search-input {
  width: 100%;
  padding: 10px 12px 10px 36px;
  border: 1px solid #E2E8F0;
  border-radius: 10px;
  background: #fff;
  font-family: 'Geist', sans-serif;
  font-size: 14px;
  color: #0F172A;
  outline: none;
  transition: border-color .2s;
}
.search-input:focus { border-color: #2563EB; }

.filter-tabs {
  display: flex;
  gap: 4px;
  background: #E2E8F0;
  border-radius: 10px;
  padding: 3px;
}
.tab-btn {
  padding: 6px 14px;
  border: none;
  border-radius: 8px;
  font-family: 'Geist', sans-serif;
  font-size: 13px;
  cursor: pointer;
  background: transparent;
  color: #64748B;
  transition: all .2s;
  font-weight: 500;
}
.tab-btn.active {
  background: #fff;
  color: #0F172A;
  box-shadow: 0 1px 3px rgba(0,0,0,.1);
}

/* ── Loading Skeleton ──────────────────────────────────────────────────── */
.loading-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 20px;
}
.skeleton-card {
  height: 340px;
  border-radius: 14px;
  background: linear-gradient(90deg, #E2E8F0 25%, #F1F5F9 50%, #E2E8F0 75%);
  background-size: 200% 100%;
  animation: shimmer 1.4s infinite;
}
@keyframes shimmer {
  0% { background-position: 200% 0; }
  100% { background-position: -200% 0; }
}

/* ── Empty ──────────────────────────────────────────────────────────── */
.empty-state {
  text-align: center;
  padding: 80px 20px;
}
.empty-icon { font-size: 56px; margin-bottom: 16px; }
.empty-title { font-size: 20px; font-weight: 600; color: #0F172A; margin-bottom: 8px; }
.empty-sub { color: #64748B; font-size: 14px; }

/* ── Products Grid ──────────────────────────────────────────────────── */
.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 20px;
}

.product-card {
  background: #fff;
  border-radius: 14px;
  overflow: hidden;
  border: 1px solid #E2E8F0;
  transition: box-shadow .2s, transform .2s;
}
.product-card:hover {
  box-shadow: 0 8px 24px rgba(0,0,0,.09);
  transform: translateY(-2px);
}
.product-card.inactive { opacity: .6; }

.card-img-wrap {
  position: relative;
  height: 200px;
  background: #F1F5F9;
  overflow: hidden;
}
.card-img {
  width: 100%; height: 100%;
  object-fit: cover;
  transition: transform .3s;
}
.product-card:hover .card-img { transform: scale(1.03); }

.card-img-placeholder {
  width: 100%; height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40px;
  color: #CBD5E1;
}

.badge-destaque {
  position: absolute;
  top: 10px; left: 10px;
  background: #F59E0B;
  color: #fff;
  font-size: 11px;
  font-weight: 600;
  padding: 3px 8px;
  border-radius: 20px;
}
.badge-inactive {
  position: absolute;
  top: 10px; right: 10px;
  background: #64748B;
  color: #fff;
  font-size: 11px;
  font-weight: 600;
  padding: 3px 8px;
  border-radius: 20px;
}

.card-actions-overlay {
  position: absolute;
  inset: 0;
  background: rgba(15,23,42,.45);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  opacity: 0;
  transition: opacity .2s;
}
.product-card:hover .card-actions-overlay { opacity: 1; }

.action-btn {
  width: 40px; height: 40px;
  border-radius: 50%;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform .15s;
}
.action-btn svg { width: 18px; height: 18px; }
.action-btn:hover { transform: scale(1.1); }
.action-btn.edit { background: #fff; color: #2563EB; }
.action-btn.delete { background: #FEE2E2; color: #DC2626; }

.card-body { padding: 16px; }
.card-category {
  font-size: 11px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: .06em;
  color: #2563EB;
  margin-bottom: 6px;
}
.card-name {
  font-size: 16px;
  font-weight: 600;
  color: #0F172A;
  margin-bottom: 6px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.card-desc {
  font-size: 13px;
  color: #64748B;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin-bottom: 12px;
}
.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.card-price {
  font-size: 18px;
  font-weight: 700;
  color: #0F172A;
}
.card-stock {
  font-size: 12px;
  font-weight: 600;
  padding: 3px 10px;
  border-radius: 20px;
}
.stock-ok   { background: #DCFCE7; color: #16A34A; }
.stock-low  { background: #FEF9C3; color: #CA8A04; }
.stock-zero { background: #FEE2E2; color: #DC2626; }

.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,.5);
  backdrop-filter: blur(4px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 20px;
}

.modal-box {
  background: #fff;
  border-radius: 16px;
  width: 100%;
  max-width: 540px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 24px 64px rgba(0,0,0,.18);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px 24px 0;
  margin-bottom: 20px;
}
.modal-title {
  font-family: 'Instrument Serif', serif;
  font-size: 22px;
  font-weight: 400;
}
.modal-close {
  background: #F1F5F9;
  border: none;
  width: 32px; height: 32px;
  border-radius: 50%;
  cursor: pointer;
  font-size: 14px;
  color: #64748B;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background .2s;
}
.modal-close:hover { background: #E2E8F0; }

.modal-form { padding: 0 24px 24px; }

.field { margin-bottom: 16px; }
.field-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
  margin-bottom: 16px;
}
.toggles { align-items: center; margin-top: 4px; }

.label {
  display: block;
  font-size: 13px;
  font-weight: 500;
  color: #374151;
  margin-bottom: 6px;
}

.input {
  width: 100%;
  padding: 10px 14px;
  border: 1px solid #E2E8F0;
  border-radius: 10px;
  font-family: 'Geist', sans-serif;
  font-size: 14px;
  color: #0F172A;
  background: #F8FAFC;
  outline: none;
  transition: border-color .2s, background .2s;
}
.input:focus {
  border-color: #2563EB;
  background: #fff;
}
.textarea { resize: vertical; min-height: 80px; }

.img-preview {
  margin-top: 8px;
  border-radius: 8px;
  overflow: hidden;
  height: 100px;
  background: #F1F5F9;
}
.img-preview img {
  width: 100%; height: 100%;
  object-fit: cover;
}

.toggle-label {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  font-size: 13px;
  color: #374151;
  user-select: none;
}
.toggle-switch {
  width: 40px; height: 22px;
  background: #E2E8F0;
  border-radius: 11px;
  position: relative;
  transition: background .2s;
  cursor: pointer;
}
.toggle-switch::after {
  content: '';
  position: absolute;
  top: 3px; left: 3px;
  width: 16px; height: 16px;
  background: #fff;
  border-radius: 50%;
  transition: transform .2s;
  box-shadow: 0 1px 3px rgba(0,0,0,.2);
}
.toggle-switch.on { background: #2563EB; }
.toggle-switch.on::after { transform: translateX(18px); }

.form-error {
  background: #FEF2F2;
  border: 1px solid #FECACA;
  color: #DC2626;
  border-radius: 8px;
  padding: 10px 14px;
  font-size: 13px;
  margin-bottom: 16px;
}

.modal-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  margin-top: 8px;
}

.btn-cancel {
  padding: 10px 20px;
  border: 1px solid #E2E8F0;
  border-radius: 10px;
  background: transparent;
  font-family: 'Geist', sans-serif;
  font-size: 14px;
  cursor: pointer;
  color: #64748B;
  transition: background .2s;
}
.btn-cancel:hover { background: #F8FAFC; }

.btn-save {
  padding: 10px 24px;
  background: #0F172A;
  color: #fff;
  border: none;
  border-radius: 10px;
  font-family: 'Geist', sans-serif;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: background .2s;
  display: flex;
  align-items: center;
  gap: 8px;
}
.btn-save:hover { background: #1E293B; }
.btn-save:disabled { opacity: .6; cursor: not-allowed; }

/* Delete Modal */
.modal-delete {
  max-width: 400px;
  padding: 32px;
  text-align: center;
}
.delete-icon { font-size: 48px; margin-bottom: 16px; }
.delete-title { font-size: 20px; font-weight: 600; margin-bottom: 12px; }
.delete-text { font-size: 14px; color: #64748B; line-height: 1.6; margin-bottom: 24px; }
.btn-delete {
  padding: 10px 24px;
  background: #DC2626;
  color: #fff;
  border: none;
  border-radius: 10px;
  font-family: 'Geist', sans-serif;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: background .2s;
}
.btn-delete:hover { background: #B91C1C; }
.btn-delete:disabled { opacity: .6; cursor: not-allowed; }

/* ── Spinner ──────────────────────────────────────────────────────────── */
.spinner {
  width: 16px; height: 16px;
  border: 2px solid rgba(255,255,255,.3);
  border-top-color: #fff;
  border-radius: 50%;
  animation: spin .6s linear infinite;
}
@keyframes spin { to { transform: rotate(360deg); } }

/* ── Toast ──────────────────────────────────────────────────────────── */
.toast {
  position: fixed;
  bottom: 24px; right: 24px;
  padding: 14px 20px;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 500;
  z-index: 9999;
  box-shadow: 0 8px 24px rgba(0,0,0,.15);
}
.toast.success { background: #0F172A; color: #fff; }
.toast.error   { background: #DC2626; color: #fff; }
.toast.info    { background: #2563EB; color: #fff; }

.toast-enter-active, .toast-leave-active { transition: all .3s; }
.toast-enter-from { opacity: 0; transform: translateY(12px); }
.toast-leave-to   { opacity: 0; transform: translateY(12px); }
</style>
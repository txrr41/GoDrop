<template>
  <div class="page-root">

    <header class="page-topbar">
      <div class="tb-left">
        <p class="tb-breadcrumb">Admin <span class="sep">/</span> <span class="current">Permissões</span></p>
        <h1 class="tb-title">Controle de Acessos</h1>
      </div>
      <div class="tb-actions">
        <button class="tb-btn-secondary" @click="loadUsers" :disabled="loading">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
            <path d="M21 2v6h-6M3 12a9 9 0 0 1 15-6.7L21 8M3 22v-6h6M21 12a9 9 0 0 1-15 6.7L3 16"/>
          </svg>
          Sincronizar
        </button>
        <button class="tb-btn-primary" @click="openEditModal(null)">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
            <path d="M12 5v14M5 12h14"/>
          </svg>
          Novo Usuário
        </button>
      </div>
    </header>

    <section class="table-card">
      <div class="table-topbar">
        <div class="search-wrap">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="11" cy="11" r="8"/>
            <path d="m21 21-4.35-4.35"/>
          </svg>
          <input v-model="search" placeholder="Buscar por nome ou e-mail..." class="search-input"/>
        </div>
        <div class="filter-tabs">
          <button v-for="f in filters" :key="f.val" class="filter-tab" :class="{ active: filterStatus === f.val }"
                  @click="filterStatus = f.val">
            {{ f.label }}
          </button>
        </div>
      </div>

      <v-data-table :headers="headers" :items="filteredUsers" class="data-table" hover>

        <template v-slot:item.user="{ item }">
          <div class="user-cell">
            <div class="user-avatar">{{ item.name.charAt(0) }}</div>
            <div>
              <div class="user-name-row">
                <span class="user-name">{{ item.name }}</span>
                <span :class="['type-tag', item.type === 'owner' ? 'tag-amber' : 'tag-indigo']">{{
                    item.type.toUpperCase()
                  }}</span>
              </div>
              <span class="user-email">{{ item.email }}</span>
            </div>
          </div>
        </template>

        <template v-slot:item.permissions="{ item }">
          <div class="perm-row">
            <span v-for="p in item.permissions.slice(0, 2)" :key="p" class="perm-tag">{{ translatePerm(p) }}</span>
            <span v-if="item.permissions.length > 2" class="perm-tag perm-more">+{{
                item.permissions.length - 2
              }}</span>
            <span v-if="item.permissions.length === 0 && item.type === 'owner'"
                  class="perm-tag perm-owner">Acesso Total</span>
          </div>
        </template>

        <template v-slot:item.status="{ item }">
          <span :class="['status-pill', item.suspended ? 'status-suspended' : 'status-active']">
            <span class="status-dot-sm"></span>
            {{ item.suspended ? 'Suspenso' : 'Ativo' }}
          </span>
        </template>

        <template v-slot:item.actions="{ item }">
          <div class="row-actions">
            <button class="act-btn act-edit" @click="openEditModal(item)">
              <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
                <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
              </svg>
              Editar
            </button>
            <button
                :class="['act-btn', item.suspended ? 'act-reactivate' : 'act-suspend']"
                :disabled="suspendingId === item.id"
                @click="toggleSuspension(item)"
            >
              {{ item.suspended ? 'Reativar' : 'Suspender' }}
            </button>
          </div>
        </template>
      </v-data-table>
    </section>

    <!-- MODAL EDITAR/CRIAR -->
    <v-dialog v-model="editModal" max-width="640" scrollable>
      <div class="modal-card">
        <div class="modal-hd">
          <div>
            <p class="modal-pre">{{ isEditing ? 'Editar' : 'Novo' }}</p>
            <h2 class="modal-title">{{ isEditing ? 'Editar Perfil' : 'Cadastrar Membro' }}</h2>
          </div>
          <button class="modal-close-btn" @click="editModal = false">
            <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M18 6 6 18M6 6l12 12"/>
            </svg>
          </button>
        </div>

        <div class="modal-body">
          <div class="form-section">
            <p class="form-section-label">Informações Principais</p>
            <div class="form-row">
              <div class="field-group">
                <label class="field-label">Nome</label>
                <input v-model="form.name" class="field-input" placeholder="Nome completo"/>
              </div>
              <div class="field-group">
                <label class="field-label">E-mail</label>
                <input v-model="form.email" class="field-input" placeholder="email@exemplo.com"/>
              </div>
            </div>
            <div v-if="!isEditing" class="field-group">
              <label class="field-label">Senha inicial</label>
              <input v-model="form.password" type="password" class="field-input"
                     placeholder="Deixe vazio para usar Temp@1234"/>
            </div>
          </div>

          <div class="form-section">
            <p class="form-section-label">Permissões de Acesso</p>
            <p class="form-section-hint">Selecione as áreas que este membro pode acessar.</p>
            <div class="perms-grid">
              <div
                  v-for="perm in availablePermissions"
                  :key="perm.id"
                  class="perm-card"
                  :class="{ active: form.permissions.includes(perm.id) }"
                  @click="togglePerm(perm.id)"
              >
                <div class="perm-card-icon">
                  <v-icon size="18">{{ perm.icon }}</v-icon>
                </div>
                <div class="perm-card-text">
                  <span class="pc-title">{{ perm.label }}</span>
                  <span class="pc-desc">{{ perm.desc }}</span>
                </div>
                <div class="perm-check" :class="{ checked: form.permissions.includes(perm.id) }">
                  <svg v-if="form.permissions.includes(perm.id)" width="10" height="10" viewBox="0 0 24 24" fill="none"
                       stroke="currentColor" stroke-width="3">
                    <polyline points="20 6 9 17 4 12"/>
                  </svg>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="modal-ft">
          <button class="ft-cancel" @click="editModal = false">Cancelar</button>
          <button class="ft-save" :disabled="saving" @click="saveUser">
            <span v-if="saving" class="spin-sm"></span>
            <span v-else>{{ isEditing ? 'Salvar Alterações' : 'Criar Perfil' }}</span>
          </button>
        </div>
      </div>
    </v-dialog>

    <!-- MODAL SUSPENDER -->
    <v-dialog v-model="suspendModal" max-width="420">
      <div class="modal-card">
        <div class="modal-hd">
          <div>
            <p class="modal-pre">Atenção</p>
            <h2 class="modal-title">Confirmar Suspensão</h2>
          </div>
          <button class="modal-close-btn" @click="suspendModal = false">
            <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M18 6 6 18M6 6l12 12"/>
            </svg>
          </button>
        </div>
        <div class="modal-body">
          <p class="suspend-desc">Ao suspender <strong>{{ selectedUser?.name }}</strong>, o acesso será bloqueado
            imediatamente.</p>
          <div class="field-group">
            <label class="field-label">Motivo</label>
            <select v-model="suspensionForm.reason" class="field-input field-select">
              <option value="">Selecione um motivo</option>
              <option>Tentativa de Fraude</option>
              <option>Comportamento Indevido</option>
              <option>Atraso em Pagamentos</option>
              <option>Fim de Vínculo</option>
              <option>Outro</option>
            </select>
          </div>
          <div class="field-group">
            <label class="field-label">Observações</label>
            <textarea v-model="suspensionForm.details" class="field-input field-textarea" rows="3"
                      placeholder="Observações internas..."></textarea>
          </div>
        </div>
        <div class="modal-ft">
          <button class="ft-cancel" @click="suspendModal = false">Voltar</button>
          <button class="ft-danger" :disabled="saving" @click="confirmSuspension">
            <span v-if="saving" class="spin-sm"></span>
            <span v-else>Suspender Agora</span>
          </button>
        </div>
      </div>
    </v-dialog>

    <transition name="toast-slide">
      <div v-if="snackbar.show" class="pm-toast" :class="`pm-toast--${snackbar.color}`">
        {{ snackbar.text }}
      </div>
    </transition>
  </div>
</template>

<script setup>
import {ref, computed, onMounted} from 'vue'
import api from '../api/api.js'

const loading = ref(false)
const saving = ref(false)
const suspendingId = ref(null)
const search = ref('')
const filterStatus = ref('all')
const editModal = ref(false)
const suspendModal = ref(false)
const isEditing = ref(false)
const snackbar = ref({show: false, text: '', color: 'success'})
const users = ref([])
const selectedUser = ref(null)
const suspensionForm = ref({reason: '', details: ''})
const form = ref({id: null, name: '', email: '', password: '', permissions: []})

const filters = [
  {val: 'all', label: 'Todos'},
  {val: 'staff', label: 'Staff'},
  {val: 'owner', label: 'Owner'},
  {val: 'suspended', label: 'Suspensos'},
]

const availablePermissions = [
  {id: 'catalog', label: 'Produtos', desc: 'Gerenciar catálogo — /produtos', icon: 'mdi-tag-outline'},
  {id: 'orders', label: 'Pedidos', desc: 'Painel admin — /admin/pedidos', icon: 'mdi-shopping-outline'},
  {id: 'customers', label: 'Clientes', desc: 'Visualizar dados e histórico', icon: 'mdi-account-group-outline'},
  {id: 'marketing', label: 'Ofertas', desc: 'Criar e gerenciar ofertas', icon: 'mdi-tag-heart-outline'},
]

const headers = [
  {title: 'IDENTIFICAÇÃO', key: 'user', align: 'start'},
  {title: 'PERMISSÕES', key: 'permissions', align: 'start'},
  {title: 'STATUS', key: 'status', align: 'center'},
  {title: '', key: 'actions', align: 'end', sortable: false},
]

const filteredUsers = computed(() => {
  const term = search.value.toLowerCase()
  return users.value.filter(u => {
    const match = u.name.toLowerCase().includes(term) || u.email.toLowerCase().includes(term)
    if (filterStatus.value === 'suspended') return match && u.suspended
    if (filterStatus.value === 'staff') return match && u.type === 'staff'
    if (filterStatus.value === 'owner') return match && u.type === 'owner'
    return match
  })
})

async function loadUsers() {
  loading.value = true
  try {
    const {data} = await api.get('/api/admin/users');
    users.value = data
  } catch {
    notify('Erro ao carregar usuários.', 'error')
  } finally {
    loading.value = false
  }
}

async function saveUser() {
  saving.value = true
  try {
    const payload = {
      name: form.value.name,
      email: form.value.email,
      password: form.value.password,
      permissions: form.value.permissions
    }
    if (isEditing.value) {
      const {data} = await api.put(`/api/admin/users/${form.value.id}`, payload)
      const idx = users.value.findIndex(u => u.id === form.value.id)
      if (idx !== -1) users.value[idx] = data
      notify('Perfil atualizado!', 'success')
    } else {
      const {data} = await api.post('/api/admin/users', payload)
      users.value.push(data)
      notify('Usuário criado!', 'success')
    }
    editModal.value = false
  } catch (e) {
    notify(e.response?.data?.message ?? 'Erro ao salvar.', 'error')
  } finally {
    saving.value = false
  }
}

async function confirmSuspension() {
  saving.value = true
  try {
    const {data} = await api.patch(`/api/admin/users/${selectedUser.value.id}/suspend`, {
      suspend: 'true',
      reason: suspensionForm.value.reason
    })
    const idx = users.value.findIndex(u => u.id === selectedUser.value.id)
    if (idx !== -1) users.value[idx] = data
    suspendModal.value = false
    notify('Acesso suspenso.', 'warning')
  } catch {
    notify('Erro ao suspender.', 'error')
  } finally {
    saving.value = false
  }
}

async function toggleSuspension(user) {
  selectedUser.value = user
  if (user.suspended) {
    suspendingId.value = user.id
    try {
      const {data} = await api.patch(`/api/admin/users/${user.id}/suspend`, {suspend: 'false'})
      const idx = users.value.findIndex(u => u.id === user.id)
      if (idx !== -1) users.value[idx] = data
      notify('Usuário reativado!', 'success')
    } catch {
      notify('Erro ao reativar.', 'error')
    } finally {
      suspendingId.value = null
    }
  } else {
    suspensionForm.value = {reason: '', details: ''}
    suspendModal.value = true
  }
}

function openEditModal(user) {
  if (user) {
    isEditing.value = true
    form.value = {id: user.id, name: user.name, email: user.email, password: '', permissions: [...user.permissions]}
  } else {
    isEditing.value = false
    form.value = {id: null, name: '', email: '', password: '', permissions: []}
  }
  editModal.value = true
}

function togglePerm(id) {
  const idx = form.value.permissions.indexOf(id)
  if (idx > -1) form.value.permissions.splice(idx, 1)
  else form.value.permissions.push(id)
}

function translatePerm(id) {
  return availablePermissions.find(p => p.id === id)?.label ?? id
}

function notify(text, color) {
  snackbar.value = {show: true, text, color};
  setTimeout(() => {
    snackbar.value.show = false
  }, 3000)
}

onMounted(loadUsers)
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=DM+Mono:wght@500&display=swap');

*, *::before, *::after {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

.page-root {
  font-family: 'DM Sans', sans-serif;
  background: #F8FAFC;
  min-height: 100vh;
  padding: 28px 32px 40px;
  color: #0F172A;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* TOPBAR */
.page-topbar {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
}

.tb-breadcrumb {
  font-size: 12px;
  color: #94A3B8;
  font-family: 'DM Mono', monospace;
  margin-bottom: 4px;
}

.tb-breadcrumb .sep {
  color: #CBD5E1;
  margin: 0 4px;
}

.tb-breadcrumb .current {
  color: #64748B;
}

.tb-title {
  font-size: 22px;
  font-weight: 700;
  letter-spacing: -0.025em;
  color: #0F172A;
}

.tb-actions {
  display: flex;
  gap: 10px;
}

.tb-btn-secondary {
  display: flex;
  align-items: center;
  gap: 7px;
  background: #fff;
  border: 1px solid #E2E8F0;
  border-radius: 10px;
  padding: 0 16px;
  height: 38px;
  font: 600 13px 'DM Sans', sans-serif;
  color: #475569;
  cursor: pointer;
  transition: all .15s;
}

.tb-btn-secondary:hover {
  border-color: #CBD5E1;
  color: #0F172A;
}

.tb-btn-primary {
  display: flex;
  align-items: center;
  gap: 7px;
  background: #0F172A;
  border: none;
  border-radius: 10px;
  padding: 0 16px;
  height: 38px;
  font: 600 13px 'DM Sans', sans-serif;
  color: #fff;
  cursor: pointer;
  transition: background .15s;
}

.tb-btn-primary:hover {
  background: #1E293B;
}

/* TABLE CARD */
.table-card {
  background: #fff;
  border: 1px solid #E2E8F0;
  border-radius: 14px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, .03);
}

.table-topbar {
  padding: 16px 20px;
  border-bottom: 1px solid #F1F5F9;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}

.search-wrap {
  display: flex;
  align-items: center;
  gap: 8px;
  background: #F8FAFC;
  border: 1px solid #E2E8F0;
  border-radius: 9px;
  padding: 0 12px;
  height: 36px;
  color: #94A3B8;
  transition: border-color .2s;
}

.search-wrap:focus-within {
  border-color: #6366F1;
}

.search-input {
  border: none;
  background: transparent;
  outline: none;
  font: 400 13px 'DM Sans', sans-serif;
  color: #0F172A;
  width: 220px;
}

.search-input::placeholder {
  color: #94A3B8;
}

.filter-tabs {
  display: flex;
  gap: 2px;
  background: #F8FAFC;
  border: 1px solid #E2E8F0;
  border-radius: 9px;
  padding: 3px;
}

.filter-tab {
  border: none;
  background: transparent;
  padding: 5px 12px;
  border-radius: 6px;
  font: 500 12px 'DM Sans', sans-serif;
  color: #94A3B8;
  cursor: pointer;
  transition: all .15s;
}

.filter-tab.active {
  background: #fff;
  color: #0F172A;
  box-shadow: 0 1px 3px rgba(0, 0, 0, .08);
  font-weight: 600;
}

:deep(.v-data-table-header th) {
  background: #F8FAFC !important;
  font-size: 11px !important;
  font-weight: 700 !important;
  text-transform: uppercase;
  color: #94A3B8 !important;
  letter-spacing: .06em;
}

:deep(.v-data-table__td) {
  height: 68px !important;
  border-bottom: 1px solid #F8FAFC !important;
}

/* USER CELL */
.user-cell {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 0;
}

.user-avatar {
  width: 36px;
  height: 36px;
  background: #EEF2FF;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 800;
  font-size: 14px;
  color: #6366F1;
  flex-shrink: 0;
}

.user-name-row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-name {
  font-size: 13px;
  font-weight: 600;
  color: #0F172A;
}

.user-email {
  font-size: 11px;
  color: #94A3B8;
  display: block;
  margin-top: 1px;
}

.type-tag {
  font-size: 10px;
  font-weight: 700;
  padding: 2px 7px;
  border-radius: 5px;
  letter-spacing: .05em;
}

.tag-amber {
  background: #FEF3C7;
  color: #92400E;
}

.tag-indigo {
  background: #EEF2FF;
  color: #4338CA;
}

/* PERM TAGS */
.perm-row {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
}

.perm-tag {
  background: #F1F5F9;
  color: #475569;
  font-size: 11px;
  font-weight: 600;
  padding: 3px 9px;
  border-radius: 6px;
  text-transform: uppercase;
  letter-spacing: .04em;
}

.perm-more {
  background: #E0E7FF;
  color: #4338CA;
}

.perm-owner {
  background: #FEF3C7;
  color: #92400E;
}

/* STATUS */
.status-pill {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 10px;
  border-radius: 100px;
  font-size: 11px;
  font-weight: 700;
}

.status-dot-sm {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: currentColor;
}

.status-active {
  background: rgba(16, 185, 129, .1);
  color: #059669;
  border: 1px solid rgba(16, 185, 129, .2);
}

.status-suspended {
  background: rgba(244, 63, 94, .1);
  color: #E11D48;
  border: 1px solid rgba(244, 63, 94, .2);
}

/* ROW ACTIONS */
.row-actions {
  display: flex;
  gap: 6px;
  justify-content: flex-end;
}

.act-btn {
  border-radius: 8px;
  border: 1px solid;
  padding: 5px 11px;
  font: 600 11px 'DM Sans', sans-serif;
  cursor: pointer;
  transition: all .15s;
  display: flex;
  align-items: center;
  gap: 5px;
  height: 30px;
}

.act-edit {
  background: #F8FAFC;
  border-color: #E2E8F0;
  color: #475569;
}

.act-edit:hover {
  border-color: #CBD5E1;
  color: #0F172A;
}

.act-suspend {
  background: #FEF2F2;
  border-color: #FECACA;
  color: #DC2626;
}

.act-suspend:hover {
  background: #FEE2E2;
}

.act-reactivate {
  background: #F0FDF4;
  border-color: #BBF7D0;
  color: #059669;
}

.act-reactivate:hover {
  background: #DCFCE7;
}

/* MODAIS */
.modal-card {
  background: #fff;
  border-radius: 18px;
  box-shadow: 0 20px 48px rgba(0, 0, 0, .12);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  font-family: 'DM Sans', sans-serif;
}

.modal-hd {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 22px 26px;
  border-bottom: 1px solid #F1F5F9;
}

.modal-pre {
  font-size: 10px;
  color: #94A3B8;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: .1em;
  margin-bottom: 3px;
}

.modal-title {
  font-size: 17px;
  font-weight: 700;
  color: #0F172A;
  letter-spacing: -0.02em;
}

.modal-close-btn {
  width: 30px;
  height: 30px;
  border: 1px solid #E2E8F0;
  border-radius: 8px;
  background: transparent;
  color: #94A3B8;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all .15s;
}

.modal-close-btn:hover {
  background: #F8FAFC;
  color: #0F172A;
}

.modal-body {
  padding: 22px 26px;
  max-height: 60vh;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.form-section {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.form-section-label {
  font-size: 11px;
  font-weight: 700;
  color: #94A3B8;
  text-transform: uppercase;
  letter-spacing: .1em;
}

.form-section-hint {
  font-size: 12px;
  color: #64748B;
  margin-top: -8px;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.field-group {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.field-label {
  font-size: 12px;
  font-weight: 600;
  color: #475569;
}

.field-input {
  border: 1.5px solid #E2E8F0;
  border-radius: 9px;
  padding: 9px 12px;
  font: 500 13px 'DM Sans', sans-serif;
  color: #0F172A;
  background: #fff;
  outline: none;
  transition: border-color .2s, box-shadow .2s;
  width: 100%;
}

.field-input:focus {
  border-color: #6366F1;
  box-shadow: 0 0 0 3px rgba(99, 102, 241, .1);
}

.field-select {
  appearance: none;
  cursor: pointer;
}

.field-textarea {
  resize: vertical;
  min-height: 80px;
  font-family: inherit;
}

.perms-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
}

.perm-card {
  display: flex;
  align-items: center;
  gap: 12px;
  border: 1.5px solid #E2E8F0;
  border-radius: 12px;
  padding: 14px;
  cursor: pointer;
  transition: all .2s;
}

.perm-card:hover {
  background: #F8FAFC;
  border-color: #CBD5E1;
}

.perm-card.active {
  border-color: #6366F1;
  background: #F5F3FF;
}

.perm-card-icon {
  width: 38px;
  height: 38px;
  background: #F1F5F9;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #64748B;
  flex-shrink: 0;
  transition: all .2s;
}

.perm-card.active .perm-card-icon {
  background: #6366F1;
  color: #fff;
}

.perm-card-text {
  flex: 1;
  min-width: 0;
}

.pc-title {
  font-size: 13px;
  font-weight: 700;
  color: #0F172A;
  display: block;
}

.pc-desc {
  font-size: 11px;
  color: #94A3B8;
  display: block;
  margin-top: 1px;
}

.perm-check {
  width: 18px;
  height: 18px;
  border: 1.5px solid #E2E8F0;
  border-radius: 5px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: all .2s;
}

.perm-check.checked {
  background: #6366F1;
  border-color: #6366F1;
  color: #fff;
}

.suspend-desc {
  font-size: 14px;
  color: #64748B;
  line-height: 1.6;
}

.modal-ft {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding: 16px 26px;
  border-top: 1px solid #F1F5F9;
  background: #FAFAFA;
}

.ft-cancel {
  background: transparent;
  border: 1px solid #E2E8F0;
  border-radius: 8px;
  padding: 8px 16px;
  font: 600 13px 'DM Sans', sans-serif;
  color: #64748B;
  cursor: pointer;
}

.ft-save {
  background: #0F172A;
  border: none;
  border-radius: 8px;
  padding: 8px 20px;
  font: 700 13px 'DM Sans', sans-serif;
  color: #fff;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 6px;
  transition: background .15s;
}

.ft-save:hover:not(:disabled) {
  background: #1E293B;
}

.ft-save:disabled {
  background: #E2E8F0;
  color: #94A3B8;
  cursor: not-allowed;
}

.ft-danger {
  background: #DC2626;
  border: none;
  border-radius: 8px;
  padding: 8px 20px;
  font: 700 13px 'DM Sans', sans-serif;
  color: #fff;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 6px;
  transition: opacity .15s;
}

.ft-danger:hover:not(:disabled) {
  opacity: .9;
}

.ft-danger:disabled {
  opacity: .5;
  cursor: not-allowed;
}

.spin-sm {
  width: 14px;
  height: 14px;
  border: 2px solid rgba(255, 255, 255, .3);
  border-top-color: #fff;
  border-radius: 50%;
  animation: spin .7s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.pm-toast {
  position: fixed;
  bottom: 28px;
  right: 28px;
  z-index: 9999;
  padding: 12px 18px;
  border-radius: 10px;
  font: 600 13px 'DM Sans', sans-serif;
  box-shadow: 0 8px 24px rgba(0, 0, 0, .15);
  color: #fff;
}

.pm-toast--success {
  background: #065F46;
}

.pm-toast--error {
  background: #991B1B;
}

.pm-toast--warning {
  background: #92400E;
}

.toast-slide-enter-active, .toast-slide-leave-active {
  transition: all .25s;
}

.toast-slide-enter-from, .toast-slide-leave-to {
  opacity: 0;
  transform: translateY(8px);
}

@media (max-width: 768px) {
  .page-root {
    padding: 16px;
  }

  .table-topbar {
    flex-direction: column;
    align-items: stretch;
  }

  .form-row {
    grid-template-columns: 1fr;
  }

  .perms-grid {
    grid-template-columns: 1fr;
  }
}
</style>
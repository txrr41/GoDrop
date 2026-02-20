<template>
  <div class="admin-panel">
    <header class="page-header">
      <div class="header-content">
        <div class="title-section">
          <div class="crown-wrapper">
            <v-icon size="40" color="white">mdi-shield-key</v-icon>
          </div>
          <div class="title-text">
            <h1>Controle de Acessos</h1>
            <div class="subtitle-badge">
              <span class="dot"></span>
              <p>Gerenciando equipe e permissões do e-commerce</p>
            </div>
          </div>
        </div>
        <div class="header-actions">
          <v-btn class="action-btn secondary" elevation="0" @click="loadUsers" :loading="loading">
            <v-icon start>mdi-sync</v-icon>
            Sincronizar
          </v-btn>
          <v-btn class="action-btn primary" elevation="0" @click="openEditModal(null)">
            <v-icon start>mdi-account-plus</v-icon>
            Novo Usuário
          </v-btn>
        </div>
      </div>
    </header>

    <main class="panel-content">
      <v-card class="neo-table-card" elevation="0">
        <div class="table-tools">
          <v-text-field
              v-model="search"
              prepend-inner-icon="mdi-magnify"
              placeholder="Buscar por nome ou e-mail..."
              variant="outlined"
              density="compact"
              hide-details
              class="neo-search"
          />
          <v-chip-group v-model="filterStatus" mandatory class="status-filters">
            <v-chip value="all" variant="text" selected-class="active-filter">Todos</v-chip>
            <v-chip value="staff" variant="text" selected-class="active-filter">Staff</v-chip>
            <v-chip value="owner" variant="text" selected-class="active-filter">Owner</v-chip>
            <v-chip value="suspended" variant="text" selected-class="active-filter">Suspensos</v-chip>
          </v-chip-group>
        </div>

        <v-data-table :headers="headers" :items="filteredUsers" class="custom-grid" hover>

          <template v-slot:item.user="{ item }">
            <div class="user-info-cell">
              <v-avatar size="44" class="user-avatar-premium">
                <span>{{ item.name.charAt(0) }}</span>
              </v-avatar>
              <div class="user-details">
                <div class="d-flex align-center gap-2">
                  <span class="user-name">{{ item.name }}</span>
                  <v-chip
                      :color="item.type === 'owner' ? 'amber' : 'indigo'"
                      size="x-small"
                      variant="flat"
                      class="type-tag"
                  >
                    {{ item.type.toUpperCase() }}
                  </v-chip>
                </div>
                <span class="user-email">{{ item.email }}</span>
              </div>
            </div>
          </template>

          <template v-slot:item.permissions="{ item }">
            <div class="perm-wrap">
              <span v-for="p in item.permissions.slice(0, 2)" :key="p" class="neo-perm-tag">
                {{ translatePerm(p) }}
              </span>
              <span v-if="item.permissions.length > 2" class="neo-perm-tag more">
                +{{ item.permissions.length - 2 }}
              </span>
              <span v-if="item.permissions.length === 0 && item.type === 'owner'" class="neo-perm-tag owner">
                Acesso Total
              </span>
            </div>
          </template>

          <template v-slot:item.status="{ item }">
            <div :class="['status-badge', item.suspended ? 'suspended' : 'active']">
              <span class="status-dot"></span>
              {{ item.suspended ? 'Suspenso' : 'Ativo' }}
            </div>
          </template>

          <template v-slot:item.actions="{ item }">
            <div class="actions-group">
              <v-btn icon="mdi-pencil-outline" variant="text" size="small" @click="openEditModal(item)"/>
              <v-btn
                  :icon="item.suspended ? 'mdi-account-check' : 'mdi-account-off-outline'"
                  variant="text"
                  size="small"
                  :color="item.suspended ? 'success' : 'error'"
                  :loading="suspendingId === item.id"
                  @click="toggleSuspension(item)"
              />
            </div>
          </template>
        </v-data-table>
      </v-card>
    </main>

    <v-dialog v-model="editModal" max-width="700px" scrollable>
      <v-card class="neo-modal">
        <v-card-title class="neo-modal-header">
          <div class="header-title">
            <v-icon color="indigo-accent-2" class="mr-2">mdi-account-cog</v-icon>
            {{ isEditing ? 'Editar Perfil' : 'Cadastrar Membro' }}
          </div>
          <v-btn icon="mdi-close" variant="text" size="small" @click="editModal = false"/>
        </v-card-title>

        <v-card-text class="pa-8">
          <div class="form-section">
            <div class="section-title">Informações Principais</div>
            <v-row>
              <v-col cols="12" md="6">
                <v-text-field v-model="form.name" label="Nome" variant="outlined" density="comfortable"
                              class="neo-input"/>
              </v-col>
              <v-col cols="12" md="6">
                <v-text-field v-model="form.email" label="E-mail" variant="outlined" density="comfortable"
                              class="neo-input"/>
              </v-col>
              <v-col cols="12" v-if="!isEditing">
                <v-text-field
                    v-model="form.password"
                    label="Senha inicial"
                    type="password"
                    variant="outlined"
                    density="comfortable"
                    class="neo-input"
                    hint="Deixe vazio para usar senha padrão: Temp@1234"
                    persistent-hint
                />
              </v-col>
            </v-row>
          </div>

          <div class="form-section mt-8">
            <div class="section-title">Permissões de Acesso</div>
            <p class="section-hint">Selecione as áreas que este membro pode acessar.</p>
            <div class="permissions-matrix">
              <div
                  v-for="perm in availablePermissions"
                  :key="perm.id"
                  class="perm-card"
                  :class="{ active: form.permissions.includes(perm.id) }"
                  @click="togglePerm(perm.id)"
              >
                <div class="perm-icon">
                  <v-icon size="20">{{ perm.icon }}</v-icon>
                </div>
                <div class="perm-text">
                  <span class="p-title">{{ perm.label }}</span>
                  <span class="p-desc">{{ perm.desc }}</span>
                </div>
                <v-checkbox-btn v-model="form.permissions" :value="perm.id" color="indigo-accent-4" hide-details/>
              </div>
            </div>
          </div>
        </v-card-text>

        <v-card-actions class="pa-8 pt-0">
          <v-spacer/>
          <v-btn variant="text" @click="editModal = false" class="cancel-btn">Cancelar</v-btn>
          <v-btn color="indigo-accent-4" variant="flat" @click="saveUser" :loading="saving" class="save-btn px-8">
            {{ isEditing ? 'Atualizar Perfil' : 'Criar Perfil' }}
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="suspendModal" max-width="450px">
      <v-card class="neo-modal suspension-modal">
        <v-card-title class="d-flex align-center pa-6">
          <div class="warning-icon-circle mr-4">
            <v-icon color="red-accent-3">mdi-alert-outline</v-icon>
          </div>
          <span class="text-h6 font-weight-bold">Confirmar Suspensão</span>
        </v-card-title>
        <v-card-text class="px-6 pb-6">
          <p class="mb-6">
            Ao suspender <strong>{{ selectedUser?.name }}</strong>, o acesso será bloqueado imediatamente.
          </p>
          <v-select
              v-model="suspensionForm.reason"
              :items="['Tentativa de Fraude', 'Comportamento Indevido', 'Atraso em Pagamentos', 'Fim de Vínculo', 'Outro']"
              label="Motivo da Suspensão"
              variant="outlined"
              class="neo-input"
          />
          <v-textarea v-model="suspensionForm.details" label="Observações Internas" variant="outlined" rows="2"
                      class="neo-input"/>
        </v-card-text>
        <v-card-actions class="px-6 pb-6 pt-0">
          <v-btn variant="text" @click="suspendModal = false">Voltar</v-btn>
          <v-spacer/>
          <v-btn color="red-accent-3" variant="flat" @click="confirmSuspension" :loading="saving"
                 class="confirm-suspend-btn">
            Suspender Agora
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-snackbar v-model="snackbar.show" :color="snackbar.color" location="top right" elevation="20">
      {{ snackbar.text }}
    </v-snackbar>
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
const snackbar = ref({show: false, text: '', color: ''})
const users = ref([])
const selectedUser = ref(null)
const suspensionForm = ref({reason: '', details: ''})

const form = ref({
  id: null, name: '', email: '', password: '', permissions: []
})

const availablePermissions = [
  {id: 'catalog', label: 'Produtos', desc: 'Gerenciar produtos — /produtos', icon: 'mdi-tag-outline'},
  {id: 'orders', label: 'Pedidos', desc: 'Painel admin — /admin/pedidos', icon: 'mdi-shopping-outline'},
  {id: 'customers', label: 'Clientes', desc: 'Visualizar dados e histórico', icon: 'mdi-account-group-outline'},
  {id: 'marketing', label: 'Ofertas', desc: 'Criar e gerenciar ofertas — /ofertas', icon: 'mdi-tag-heart-outline'},
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
    const matchSearch = u.name.toLowerCase().includes(term) || u.email.toLowerCase().includes(term)
    if (filterStatus.value === 'suspended') return matchSearch && u.suspended
    if (filterStatus.value === 'staff') return matchSearch && u.type === 'staff'
    if (filterStatus.value === 'owner') return matchSearch && u.type === 'owner'
    return matchSearch
  })
})

async function loadUsers() {
  loading.value = true
  try {
    const {data} = await api.get('/api/admin/users')
    users.value = data
  } catch (e) {
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
      permissions: form.value.permissions,
    }

    if (isEditing.value) {
      const {data} = await api.put(`/api/admin/users/${form.value.id}`, payload)
      const idx = users.value.findIndex(u => u.id === form.value.id)
      if (idx !== -1) users.value[idx] = data
      notify('Perfil atualizado com sucesso!', 'success')
    } else {
      const {data} = await api.post('/api/admin/users', payload)
      users.value.push(data)
      notify('Usuário criado com sucesso!', 'success')
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
      reason: suspensionForm.value.reason,
    })
    const idx = users.value.findIndex(u => u.id === selectedUser.value.id)
    if (idx !== -1) users.value[idx] = data
    suspendModal.value = false
    notify('Acesso suspenso.', 'warning')
  } catch (e) {
    notify('Erro ao suspender usuário.', 'error')
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
  snackbar.value = {show: true, text, color}
}

onMounted(loadUsers)
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap');

.admin-panel {
  font-family: 'Plus Jakarta Sans', sans-serif;
  background-color: #f8fafc;
  min-height: 100vh;
  padding: 48px;
  color: #0f172a;
}

.page-header {
  margin-bottom: 40px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title-section {
  display: flex;
  align-items: center;
  gap: 20px;
}

.crown-wrapper {
  background: linear-gradient(135deg, #6366f1 0%, #4338ca 100%);
  width: 64px;
  height: 64px;
  border-radius: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 12px 24px -6px rgba(99, 102, 241, 0.4);
}

.title-text h1 {
  font-size: 28px;
  font-weight: 800;
  letter-spacing: -0.03em;
  margin-bottom: 4px;
}

.subtitle-badge {
  display: flex;
  align-items: center;
  gap: 8px;
  background: #fff;
  padding: 4px 12px;
  border-radius: 100px;
  border: 1px solid #e2e8f0;
  width: fit-content;
}

.subtitle-badge .dot {
  width: 8px;
  height: 8px;
  background: #10b981;
  border-radius: 50%;
  box-shadow: 0 0 10px #10b981;
}

.subtitle-badge p {
  font-size: 13px;
  color: #64748b;
  font-weight: 600;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.action-btn {
  height: 48px !important;
  border-radius: 14px !important;
  text-transform: none !important;
  font-weight: 700 !important;
  font-size: 15px !important;
}

.action-btn.primary {
  background: #0f172a !important;
  color: white !important;
}

.action-btn.secondary {
  background: white !important;
  color: #475569 !important;
  border: 1px solid #e2e8f0 !important;
}

.neo-table-card {
  border-radius: 24px !important;
  border: 1px solid #e2e8f0 !important;
  overflow: hidden;
}

.table-tools {
  padding: 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  border-bottom: 1px solid #f1f5f9;
}

.neo-search {
  max-width: 320px;
}

:deep(.neo-search .v-field) {
  border-radius: 12px !important;
  background: #f8fafc !important;
}

.active-filter {
  background: #f1f5f9 !important;
  color: #6366f1 !important;
  font-weight: 700 !important;
}

.user-info-cell {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 12px 0;
}

.user-avatar-premium {
  background: #f1f5f9;
  border: 2px solid white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  font-weight: 800;
  color: #6366f1;
}

.user-name {
  font-weight: 700;
  color: #1e293b;
  font-size: 15px;
}

.user-email {
  display: block;
  font-size: 13px;
  color: #94a3b8;
}

.type-tag {
  font-weight: 800 !important;
  letter-spacing: 0.05em;
}

.perm-wrap {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.neo-perm-tag {
  background: #f1f5f9;
  color: #475569;
  font-size: 11px;
  font-weight: 700;
  padding: 4px 10px;
  border-radius: 8px;
  text-transform: uppercase;
}

.neo-perm-tag.more {
  background: #e0e7ff;
  color: #4338ca;
}

.neo-perm-tag.owner {
  background: #fef3c7;
  color: #92400e;
}

.status-badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 6px 16px;
  border-radius: 100px;
  font-size: 13px;
  font-weight: 700;
  border: 1px solid transparent;
}

.status-badge.active {
  background: rgba(16, 185, 129, 0.1);
  color: #059669;
  border-color: rgba(16, 185, 129, 0.2);
}

.status-badge.active .status-dot {
  background: #10b981;
  box-shadow: 0 0 8px #10b981;
}

.status-badge.suspended {
  background: rgba(244, 63, 94, 0.1);
  color: #e11d48;
  border-color: rgba(244, 63, 94, 0.2);
}

.status-badge.suspended .status-dot {
  background: #f43f5e;
  box-shadow: 0 0 8px #f43f5e;
}

.status-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
}

.actions-group {
  display: flex;
  gap: 4px;
  justify-content: flex-end;
}

.neo-modal {
  border-radius: 32px !important;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.15) !important;
}

.neo-modal-header {
  padding: 24px 32px !important;
  border-bottom: 1px solid #f1f5f9;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-title {
  font-weight: 800;
  font-size: 20px;
  letter-spacing: -0.02em;
}

.section-title {
  font-size: 12px;
  font-weight: 800;
  text-transform: uppercase;
  color: #94a3b8;
  letter-spacing: 0.1em;
  margin-bottom: 20px;
}

.section-hint {
  font-size: 14px;
  color: #64748b;
  margin-bottom: 24px;
}

.permissions-matrix {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.perm-card {
  border: 1px solid #e2e8f0;
  border-radius: 20px;
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 16px;
  cursor: pointer;
  transition: all 0.2s;
}

.perm-card:hover {
  background: #f8fafc;
  border-color: #cbd5e1;
}

.perm-card.active {
  border-color: #6366f1;
  background: #f5f3ff;
  box-shadow: 0 4px 12px rgba(99, 102, 241, 0.1);
}

.perm-icon {
  width: 44px;
  height: 44px;
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #64748b;
}

.perm-card.active .perm-icon {
  background: #6366f1;
  color: white;
  border: none;
}

.p-title {
  display: block;
  font-weight: 700;
  font-size: 14px;
  color: #1e293b;
}

.p-desc {
  font-size: 12px;
  color: #94a3b8;
  display: block;
}

.save-btn {
  height: 52px !important;
  border-radius: 16px !important;
  font-weight: 800 !important;
  text-transform: none !important;
}

.cancel-btn {
  font-weight: 700 !important;
  text-transform: none !important;
  color: #64748b !important;
}

.warning-icon-circle {
  width: 56px;
  height: 56px;
  background: #fff1f2;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

@media (max-width: 768px) {
  .permissions-matrix {
    grid-template-columns: 1fr;
  }

  .admin-panel {
    padding: 24px;
  }

  .header-content {
    flex-direction: column;
    align-items: flex-start;
    gap: 24px;
  }
}
</style>
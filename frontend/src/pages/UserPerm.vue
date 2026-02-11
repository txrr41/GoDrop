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
          <v-btn
              class="action-btn secondary"
              elevation="0"
              @click="loadUsers"
              :loading="loading"
          >
            <v-icon start>mdi-sync</v-icon>
            Sincronizar
          </v-btn>

          <v-btn
              class="action-btn primary"
              elevation="0"
              @click="openEditModal(null)"
          >
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
              placeholder="Buscar por nome, e-mail ou CPF..."
              variant="outlined"
              density="compact"
              hide-details
              class="neo-search"
          ></v-text-field>

          <v-chip-group v-model="filterStatus" mandatory class="status-filters">
            <v-chip value="all" variant="text" selected-class="active-filter">Todos</v-chip>
            <v-chip value="staff" variant="text" selected-class="active-filter">Funcionários</v-chip>
            <v-chip value="customer" variant="text" selected-class="active-filter">Clientes</v-chip>
            <v-chip value="suspended" variant="text" selected-class="active-filter">Suspensos</v-chip>
          </v-chip-group>
        </div>

        <v-data-table
            :headers="headers"
            :items="filteredUsers"
            class="custom-grid"
            hover
        >

          <template v-slot:item.user="{ item }">
            <div class="user-info-cell">
              <v-avatar size="44" class="user-avatar-premium">
                <v-img v-if="item.avatar" :src="item.avatar"></v-img>
                <span v-else>{{ item.name.charAt(0) }}</span>
              </v-avatar>
              <div class="user-details">
                <div class="d-flex align-center gap-2">
                  <span class="user-name">{{ item.name }}</span>
                  <v-chip v-if="item.type === 'staff'" size="x-small" color="indigo" variant="flat" class="type-tag">STAFF</v-chip>
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
              <v-btn icon="mdi-pencil-outline" variant="text" size="small" color="slate-600" @click="openEditModal(item)"></v-btn>
              <v-btn
                  :icon="item.suspended ? 'mdi-account-check' : 'mdi-account-off-outline'"
                  variant="text"
                  size="small"
                  :color="item.suspended ? 'emerald' : 'rose'"
                  @click="toggleSuspension(item)"
              ></v-btn>
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
            {{ isEditing ? 'Configurar Perfil' : 'Cadastrar Membro' }}
          </div>
          <v-btn icon="mdi-close" variant="text" size="small" @click="editModal = false"></v-btn>
        </v-card-title>

        <v-card-text class="pa-8">
          <div class="form-section">
            <div class="section-title">Informações Principais</div>
            <v-row>
              <v-col cols="12" md="6">
                <v-text-field v-model="form.name" label="Nome" variant="outlined" density="comfortable" class="neo-input"></v-text-field>
              </v-col>
              <v-col cols="12" md="6">
                <v-text-field v-model="form.email" label="E-mail" variant="outlined" density="comfortable" class="neo-input"></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-select
                    v-model="form.type"
                    :items="[{title: 'Funcionário / Staff', value: 'staff'}, {title: 'Cliente / Comprador', value: 'customer'}]"
                    label="Tipo de Perfil"
                    variant="outlined"
                    class="neo-input"
                ></v-select>
              </v-col>
            </v-row>
          </div>

          <div class="form-section mt-8">
            <div class="section-title">Permissões de Acesso</div>
            <p class="section-hint">Selecione granularmente as áreas que este perfil pode acessar ou gerenciar.</p>

            <div class="permissions-matrix">
              <div v-for="perm in availablePermissions" :key="perm.id"
                   class="perm-card"
                   :class="{ active: form.permissions.includes(perm.id) }"
                   @click="togglePerm(perm.id)">
                <div class="perm-icon">
                  <v-icon size="20">{{ perm.icon }}</v-icon>
                </div>
                <div class="perm-text">
                  <span class="p-title">{{ perm.label }}</span>
                  <span class="p-desc">{{ perm.desc }}</span>
                </div>
                <v-checkbox-btn v-model="form.permissions" :value="perm.id" color="indigo-accent-4" hide-details></v-checkbox-btn>
              </div>
            </div>
          </div>
        </v-card-text>

        <v-card-actions class="pa-8 pt-0">
          <v-spacer></v-spacer>
          <v-btn variant="text" @click="editModal = false" class="cancel-btn">Cancelar</v-btn>
          <v-btn color="indigo-accent-4" variant="flat" @click="saveUser" class="save-btn px-8">
            {{ isEditing ? 'Atualizar Perfil' : 'Criar Perfil' }}
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="suspendModal" max-width="450px">
      <v-card class="neo-modal suspension-modal">
        <v-card-title class="d-flex align-center pa-6">
          <div class="warning-icon-circle mr-4">
            <v-icon color="rose-accent-3">mdi-alert-outline</v-icon>
          </div>
          <span class="text-h6 font-weight-bold">Confirmar Suspensão</span>
        </v-card-title>

        <v-card-text class="px-6 pb-6">
          <p class="text-slate-600 mb-6">Ao suspender <strong>{{ selectedUser?.name }}</strong>, ele perderá acesso imediato a todas as funções do painel.</p>

          <v-select
              v-model="suspensionForm.reason"
              :items="['Tentativa de Fraude', 'Comportamento Indevido', 'Atraso em Pagamentos', 'Fim de Vínculo', 'Outro']"
              label="Motivo da Suspensão"
              variant="outlined"
              class="neo-input"
          ></v-select>

          <v-textarea
              v-model="suspensionForm.details"
              label="Observações Internas"
              variant="outlined"
              rows="2"
              class="neo-input"
          ></v-textarea>
        </v-card-text>

        <v-card-actions class="px-6 pb-6 pt-0">
          <v-btn variant="text" @click="suspendModal = false">Voltar</v-btn>
          <v-spacer></v-spacer>
          <v-btn color="rose-accent-3" variant="flat" @click="confirmSuspension" class="confirm-suspend-btn">
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
import { ref, computed } from 'vue'

const loading = ref(false)
const search = ref('')
const filterStatus = ref('all')
const editModal = ref(false)
const suspendModal = ref(false)
const isEditing = ref(false)
const snackbar = ref({ show: false, text: '', color: '' })

const users = ref([
  { id: 1, name: 'Ricardo Oliveira', email: 'ricardo@ecommerce.com', type: 'staff', permissions: ['catalog', 'orders'], suspended: false },
  { id: 2, name: 'Mariana Costa', email: 'mari@marketing.com', type: 'staff', permissions: ['marketing', 'customers'], suspended: false },
  { id: 3, name: 'Juliano Silva', email: 'juliano.user@gmail.com', type: 'customer', permissions: [], suspended: true },
  { id: 4, name: 'Alice Santos', email: 'alice@logistica.com', type: 'staff', permissions: ['logistics', 'orders'], suspended: false },
])

const availablePermissions = [
  { id: 'catalog', label: 'Catálogo', desc: 'Gerenciar produtos e categorias', icon: 'mdi-tag-outline' },
  { id: 'orders', label: 'Pedidos', desc: 'Processar e gerenciar vendas', icon: 'mdi-shopping-outline' },
  { id: 'logistics', label: 'Logística', desc: 'Controle de fretes e entregas', icon: 'mdi-truck-delivery-outline' },
  { id: 'marketing', label: 'Marketing', desc: 'Cupons e campanhas de e-mail', icon: 'mdi-bullhorn-outline' },
  { id: 'customers', label: 'Clientes', desc: 'Visualizar dados e histórico', icon: 'mdi-account-group-outline' },
  { id: 'finances', label: 'Financeiro', desc: 'Saques e relatórios de lucro', icon: 'mdi-cash-multiple' },
]

const headers = [
  { title: 'IDENTIFICAÇÃO', key: 'user', align: 'start' },
  { title: 'PERMISSÕES', key: 'permissions', align: 'start' },
  { title: 'STATUS', key: 'status', align: 'center' },
  { title: '', key: 'actions', align: 'end', sortable: false }
]

const form = ref({ id: null, name: '', email: '', type: 'staff', permissions: [] })
const suspensionForm = ref({ reason: '', details: '' })
const selectedUser = ref(null)

const filteredUsers = computed(() => {
  return users.value.filter(u => {
    const term = search.value.toLowerCase()
    const matchesSearch = u.name.toLowerCase().includes(term) || u.email.toLowerCase().includes(term)

    if (filterStatus.value === 'suspended') return matchesSearch && u.suspended
    if (filterStatus.value === 'staff') return matchesSearch && u.type === 'staff'
    if (filterStatus.value === 'customer') return matchesSearch && u.type === 'customer'

    return matchesSearch
  })
})

const openEditModal = (user) => {
  if (user) {
    isEditing.value = true
    form.value = { ...user, permissions: [...user.permissions] }
  } else {
    isEditing.value = false
    form.value = { id: Date.now(), name: '', email: '', type: 'staff', permissions: [] }
  }
  editModal.value = true
}

const togglePerm = (id) => {
  const index = form.value.permissions.indexOf(id)
  if (index > -1) form.value.permissions.splice(index, 1)
  else form.value.permissions.push(id)
}

const toggleSuspension = (user) => {
  selectedUser.value = user
  if (user.suspended) {
    user.suspended = false
    notify('Usuário reativado!', 'success')
  } else {
    suspensionForm.value = { reason: '', details: '' }
    suspendModal.value = true
  }
}

const confirmSuspension = () => {
  selectedUser.value.suspended = true
  suspendModal.value = false
  notify('Acesso revogado.', 'error')
}

const saveUser = () => {
  if (isEditing.value) {
    const idx = users.value.findIndex(u => u.id === form.value.id)
    users.value[idx] = { ...form.value }
  } else {
    users.value.push({ ...form.value, suspended: false })
  }
  editModal.value = false
  notify('Configurações salvas com sucesso.', 'success')
}

const notify = (text, color) => {
  snackbar.value = { show: true, text, color }
}

const translatePerm = (id) => availablePermissions.find(p => p.id === id)?.label || id
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

/* Header Estilo AdminOrder */
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

/* Botões Modernos */
.action-btn {
  height: 48px !important;
  border-radius: 14px !important;
  text-transform: none !important;
  font-weight: 700 !important;
  font-size: 15px !important;
  letter-spacing: 0;
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

/* Tabela Card */
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
  border: none !important;
}

.active-filter {
  background: #f1f5f9 !important;
  color: #6366f1 !important;
  font-weight: 700 !important;
}

/* Slots da Tabela */
.user-info-cell {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 12px 0;
}

.user-avatar-premium {
  background: #f1f5f9;
  border: 2px solid white;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
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

/* Permissões UI */
.perm-wrap {
  display: flex;
  gap: 8px;
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

/* STATUS BADGES (Estilo AdminOrder) */
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

/* Modal UI */
.neo-modal {
  border-radius: 32px !important;
  border: none !important;
  box-shadow: 0 25px 50px -12px rgba(0,0,0,0.15) !important;
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

/* Matrix de Permissões */
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
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
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
  .permissions-matrix { grid-template-columns: 1fr; }
  .admin-panel { padding: 24px; }
  .header-content { flex-direction: column; align-items: flex-start; gap: 24px; }
}
</style>
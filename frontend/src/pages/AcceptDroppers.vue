<template>
  <div class="page">
    <div class="page-header">
      <div>
        <h1 class="page-title">Solicitações de Parceiros</h1>
        <p class="page-sub">Gerencie as solicitações para o programa Go Drop Partners.</p>
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
          <span class="tab-count" :class="'count-' + tab.value">{{ countByStatus(tab.value) }}</span>
        </button>
      </div>
    </div>

    <!-- Loading -->
    <div v-if="loading" class="loading-state">
      <div class="spinner"></div>
      <p>Carregando solicitações...</p>
    </div>

    <!-- Empty -->
    <div v-else-if="filtered.length === 0" class="empty-state">
      <div class="empty-icon">📋</div>
      <h3>Nenhuma solicitação {{ activeTab !== 'ALL' ? statusLabel(activeTab).toLowerCase() : '' }}</h3>
      <p>Quando usuários solicitarem o programa de parceiros, aparecerão aqui.</p>
    </div>

    <!-- Table -->
    <div v-else class="table-card">
      <table class="table">
        <thead>
        <tr>
          <th>Solicitante</th>
          <th>Empresa</th>
          <th>CNPJ</th>
          <th>WhatsApp</th>
          <th>Data</th>
          <th>Status</th>
          <th>Ações</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="d in filtered" :key="d.id" class="table-row">
          <td>
            <div class="user-cell">
              <div class="avatar">{{ d.userName?.charAt(0)?.toUpperCase() }}</div>
              <div>
                <div class="user-name">{{ d.userName }}</div>
                <div class="user-email">{{ d.userEmail }}</div>
              </div>
            </div>
          </td>
          <td class="td-store">{{ d.storeName || '—' }}</td>
          <td class="td-cnpj">{{ d.cnpj }}</td>
          <td class="td-phone">{{ d.whatsapp }}</td>
          <td class="td-date">{{ formatDate(d.createdAt) }}</td>
          <td>
              <span class="status-badge" :class="'badge-' + d.status">
                {{ statusLabel(d.status) }}
              </span>
          </td>
          <td>
            <div class="action-btns" v-if="d.status === 'PENDING'">
              <button class="btn-approve" @click="handleApprove(d)" :disabled="actionLoading === d.id">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><polyline points="20 6 9 17 4 12"/></svg>
                Aprovar
              </button>
              <button class="btn-reject" @click="handleReject(d)" :disabled="actionLoading === d.id">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
                Recusar
              </button>
            </div>
            <span v-else class="no-action">—</span>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- Toast -->
    <transition name="toast">
      <div v-if="toast.show" class="toast" :class="'toast-' + toast.type">
        {{ toast.message }}
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import api from '../api/api.js'

const droppers = ref([])
const loading = ref(true)
const actionLoading = ref(null)
const activeTab = ref('PENDING')
const toast = ref({ show: false, message: '', type: 'success' })

const tabs = [
  { label: 'Pendentes',  value: 'PENDING'  },
  { label: 'Aprovados',  value: 'ACTIVE'   },
  { label: 'Recusados',  value: 'REJECTED' },
  { label: 'Todos',      value: 'ALL'      },
]

const filtered = computed(() => {
  if (activeTab.value === 'ALL') return droppers.value
  return droppers.value.filter(d => d.status === activeTab.value)
})

const countByStatus = (status) => {
  if (status === 'ALL') return droppers.value.length
  return droppers.value.filter(d => d.status === status).length
}

const statusLabel = (status) => {
  const map = { PENDING: 'Pendente', ACTIVE: 'Aprovado', REJECTED: 'Recusado' }
  return map[status] || status
}

const formatDate = (d) => {
  if (!d) return '—'
  return new Date(d).toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit', year: 'numeric' })
}

const fetchDroppers = async () => {
  loading.value = true
  try {
    const { data } = await api.get('/api/admin/droppers')
    droppers.value = data
  } catch (e) {
    showToast('Erro ao carregar solicitações.', 'error')
  } finally {
    loading.value = false
  }
}

const handleApprove = async (dropper) => {
  actionLoading.value = dropper.id
  try {
    await api.patch(`/api/admin/droppers/${dropper.id}/approve`)
    dropper.status = 'ACTIVE'
    showToast(`${dropper.userName} aprovado com sucesso!`, 'success')
  } catch (e) {
    showToast('Erro ao aprovar solicitação.', 'error')
  } finally {
    actionLoading.value = null
  }
}

const handleReject = async (dropper) => {
  actionLoading.value = dropper.id
  try {
    await api.patch(`/api/admin/droppers/${dropper.id}/reject`)
    dropper.status = 'REJECTED'
    showToast(`Solicitação de ${dropper.userName} recusada.`, 'error')
  } catch (e) {
    showToast('Erro ao recusar solicitação.', 'error')
  } finally {
    actionLoading.value = null
  }
}

const showToast = (message, type = 'success') => {
  toast.value = { show: true, message, type }
  setTimeout(() => { toast.value.show = false }, 3500)
}

onMounted(fetchDroppers)
</script>

<style scoped>
.page {
  padding: 32px;
  max-width: 1200px;
  margin: 0 auto;
  font-family: 'Inter', sans-serif;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 32px;
  gap: 24px;
  flex-wrap: wrap;
}

.page-title {
  font-size: 24px;
  font-weight: 800;
  color: #0F172A;
  margin: 0 0 6px 0;
}

.page-sub {
  font-size: 14px;
  color: #64748B;
  margin: 0;
}

/* TABS */
.filter-tabs {
  display: flex;
  gap: 8px;
  background: #F1F5F9;
  padding: 6px;
  border-radius: 12px;
}

.tab-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  border: none;
  border-radius: 8px;
  background: transparent;
  color: #64748B;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.tab-btn.active {
  background: white;
  color: #0F172A;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
}

.tab-count {
  font-size: 11px;
  font-weight: 700;
  padding: 2px 7px;
  border-radius: 100px;
  background: #E2E8F0;
  color: #475569;
}

.tab-btn.active .count-PENDING  { background: #FEF3C7; color: #D97706; }
.tab-btn.active .count-ACTIVE   { background: #DCFCE7; color: #16A34A; }
.tab-btn.active .count-REJECTED { background: #FEE2E2; color: #DC2626; }
.tab-btn.active .count-ALL      { background: #EEF2FF; color: #4F46E5; }

/* STATES */
.loading-state, .empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
  color: #64748B;
  gap: 12px;
}

.empty-icon { font-size: 48px; }
.empty-state h3 { font-size: 18px; font-weight: 700; color: #0F172A; margin: 0; }
.empty-state p { font-size: 14px; margin: 0; }

.spinner {
  width: 32px; height: 32px;
  border: 3px solid #E2E8F0;
  border-top-color: #3B82F6;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}
@keyframes spin { to { transform: rotate(360deg); } }

/* TABLE */
.table-card {
  background: white;
  border: 1px solid #E2E8F0;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
}

.table {
  width: 100%;
  border-collapse: collapse;
}

.table thead tr {
  background: #F8FAFC;
  border-bottom: 1px solid #E2E8F0;
}

.table th {
  padding: 14px 20px;
  font-size: 12px;
  font-weight: 700;
  color: #64748B;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  text-align: left;
  white-space: nowrap;
}

.table-row {
  border-bottom: 1px solid #F1F5F9;
  transition: background 0.15s;
}
.table-row:last-child { border-bottom: none; }
.table-row:hover { background: #FAFAFA; }

.table td {
  padding: 16px 20px;
  font-size: 14px;
  color: #374151;
  vertical-align: middle;
}

.user-cell { display: flex; align-items: center; gap: 12px; }

.avatar {
  width: 36px; height: 36px;
  border-radius: 10px;
  background: #EEF2FF;
  color: #4F46E5;
  display: flex; align-items: center; justify-content: center;
  font-weight: 700; font-size: 14px;
  flex-shrink: 0;
}

.user-name { font-weight: 600; color: #0F172A; font-size: 14px; }
.user-email { font-size: 12px; color: #64748B; margin-top: 2px; }

.td-store, .td-cnpj { font-family: monospace; font-size: 13px; }
.td-cnpj { color: #475569; }
.td-phone { font-size: 13px; }
.td-date { font-size: 13px; color: #64748B; white-space: nowrap; }

/* STATUS BADGES */
.status-badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 100px;
  font-size: 12px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

.badge-PENDING  { background: #FEF3C7; color: #D97706; }
.badge-ACTIVE   { background: #DCFCE7; color: #16A34A; }
.badge-REJECTED { background: #FEE2E2; color: #DC2626; }

/* ACTION BUTTONS */
.action-btns { display: flex; gap: 8px; }

.btn-approve, .btn-reject {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 7px 14px;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  border: none;
  transition: all 0.2s;
}

.btn-approve {
  background: #DCFCE7;
  color: #16A34A;
}
.btn-approve:hover:not(:disabled) {
  background: #16A34A;
  color: white;
}

.btn-reject {
  background: #FEE2E2;
  color: #DC2626;
}
.btn-reject:hover:not(:disabled) {
  background: #DC2626;
  color: white;
}

.btn-approve:disabled, .btn-reject:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.no-action { color: #CBD5E1; font-size: 18px; }

/* TOAST */
.toast {
  position: fixed;
  bottom: 32px;
  right: 32px;
  padding: 14px 24px;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 600;
  color: white;
  z-index: 9999;
  box-shadow: 0 8px 24px rgba(0,0,0,0.15);
}

.toast-success { background: #16A34A; }
.toast-error   { background: #DC2626; }

.toast-enter-active, .toast-leave-active { transition: all 0.3s ease; }
.toast-enter-from, .toast-leave-to { opacity: 0; transform: translateY(16px); }
</style>
<template>
  <div class="denied-wrapper">
    <div class="denied-card">

      <!-- Ícone animado -->
      <div class="icon-ring">
        <div class="icon-inner">
          <v-icon size="48" color="white">mdi-shield-lock-outline</v-icon>
        </div>
      </div>

      <!-- Texto -->
      <h1 class="denied-title">Acesso Restrito</h1>
      <p class="denied-subtitle">
        Você não tem permissão para acessar esta área.<br>
        Entre em contato com o administrador para solicitar acesso.
      </p>

      <!-- Quais permissões o usuário tem -->
      <div class="perms-box" v-if="auth.permissions.length > 0">
        <span class="perms-label">Suas permissões atuais:</span>
        <div class="perms-list">
          <span v-for="p in auth.permissions" :key="p" class="perm-chip">
            <v-icon size="13" class="mr-1">{{ permIcon(p) }}</v-icon>
            {{ permLabel(p) }}
          </span>
        </div>
      </div>
      <div class="perms-box empty" v-else>
        <v-icon size="16" class="mr-2" color="#94a3b8">mdi-information-outline</v-icon>
        <span>Nenhuma permissão atribuída ao seu perfil.</span>
      </div>

      <!-- Ações -->
      <div class="denied-actions">
        <v-btn
            variant="flat"
            class="back-btn"
            prepend-icon="mdi-arrow-left"
            @click="$router.back()"
        >
          Voltar
        </v-btn>
        <v-btn
            variant="flat"
            class="dashboard-btn"
            prepend-icon="mdi-view-dashboard-outline"
            :to="'/admin'"
        >
          Ir para o Dashboard
        </v-btn>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useAuthStore } from '../stores/auth.js'

const auth = useAuthStore()

const allPerms = {
  catalog:   { label: 'Produtos',  icon: 'mdi-tag-outline' },
  orders:    { label: 'Pedidos',   icon: 'mdi-shopping-outline' },
  customers: { label: 'Clientes',  icon: 'mdi-account-group-outline' },
}

function permLabel(id) {
  return allPerms[id]?.label ?? id
}
function permIcon(id) {
  return allPerms[id]?.icon ?? 'mdi-circle-small'
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap');

.denied-wrapper {
  font-family: 'Plus Jakarta Sans', sans-serif;
  min-height: 100vh;
  background: #f8fafc;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
}

.denied-card {
  background: white;
  border-radius: 32px;
  border: 1px solid #e2e8f0;
  padding: 56px 48px;
  max-width: 520px;
  width: 100%;
  text-align: center;
  box-shadow: 0 20px 60px -12px rgba(0, 0, 0, 0.08);
}

/* Ícone com anel pulsante */
.icon-ring {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: rgba(239, 68, 68, 0.08);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 32px;
  animation: pulse-ring 2.5s ease-in-out infinite;
}

.icon-inner {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 12px 28px -6px rgba(239, 68, 68, 0.45);
}

@keyframes pulse-ring {
  0%, 100% { box-shadow: 0 0 0 0 rgba(239, 68, 68, 0.15); }
  50%       { box-shadow: 0 0 0 14px rgba(239, 68, 68, 0); }
}

.denied-title {
  font-size: 26px;
  font-weight: 800;
  color: #0f172a;
  letter-spacing: -0.03em;
  margin-bottom: 12px;
}

.denied-subtitle {
  font-size: 15px;
  color: #64748b;
  line-height: 1.7;
  margin-bottom: 32px;
}

/* Caixinha de permissões atuais */
.perms-box {
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 16px;
  padding: 16px 20px;
  margin-bottom: 36px;
  text-align: left;
}

.perms-box.empty {
  display: flex;
  align-items: center;
  color: #94a3b8;
  font-size: 14px;
  font-weight: 500;
}

.perms-label {
  display: block;
  font-size: 11px;
  font-weight: 800;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  color: #94a3b8;
  margin-bottom: 12px;
}

.perms-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.perm-chip {
  display: inline-flex;
  align-items: center;
  background: #e0e7ff;
  color: #4338ca;
  font-size: 12px;
  font-weight: 700;
  padding: 5px 12px;
  border-radius: 100px;
}

/* Botões */
.denied-actions {
  display: flex;
  gap: 12px;
  justify-content: center;
}

.back-btn {
  background: #f1f5f9 !important;
  color: #475569 !important;
  border-radius: 14px !important;
  height: 48px !important;
  text-transform: none !important;
  font-weight: 700 !important;
  font-size: 15px !important;
  padding: 0 24px !important;
}

.dashboard-btn {
  background: #0f172a !important;
  color: white !important;
  border-radius: 14px !important;
  height: 48px !important;
  text-transform: none !important;
  font-weight: 700 !important;
  font-size: 15px !important;
  padding: 0 24px !important;
}

@media (max-width: 480px) {
  .denied-card { padding: 40px 24px; }
  .denied-actions { flex-direction: column; }
  .back-btn, .dashboard-btn { width: 100%; justify-content: center; }
}
</style>
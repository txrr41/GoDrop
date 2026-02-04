<template>
  <v-app>
    <!-- Header -->
    <v-app-bar flat class="header-bar">
      <v-container class="d-flex align-center justify-space-between py-0">
        <a href="/" class="logo-link d-flex align-center text-decoration-none">
          <div class="logo-icon">
            <v-icon size="20" color="white">mdi-package-variant-closed</v-icon>
          </div>
          <span class="logo-text">Go <span class="text-primary">Drop</span></span>
        </a>
      </v-container>
    </v-app-bar>

    <!-- Main Content -->
    <v-main class="main-content">
      <v-container class="fill-height d-flex align-center justify-center py-12">
        <div class="status-wrapper">
          <!-- Status Card -->
          <v-card
              class="status-card pa-8 text-center"
              :class="{ 'card-visible': mounted }"
              elevation="0"
          >
            <!-- Animated Icon -->
            <div class="icon-container mb-6">
              <div
                  class="icon-circle"
                  :class="[
                  isSuccess ? 'icon-success' : 'icon-error',
                  { 'icon-visible': showContent }
                ]"
              >
                <v-icon
                    :icon="isSuccess ? 'mdi-check-circle' : 'mdi-close-circle'"
                    size="56"
                    :class="isSuccess ? 'icon-bounce' : 'icon-shake'"
                />
              </div>

              <!-- Pulse Ring for Success -->
              <div v-if="isSuccess" class="pulse-ring"></div>
            </div>

            <!-- Status Text -->
            <div class="status-text" :class="{ 'content-visible': showContent }">
              <h1 class="text-h5 font-weight-bold mb-2">
                {{ isSuccess ? 'Pagamento Realizado!' : 'Pagamento Cancelado' }}
              </h1>
              <p class="text-body-1 text-medium-emphasis mb-1">
                {{ isSuccess ? 'Seu pedido foi confirmado com sucesso' : 'O pagamento não foi concluído' }}
              </p>
              <p v-if="orderId" class="text-body-2 text-medium-emphasis">
                Pedido: <span class="font-weight-medium text-high-emphasis">{{ orderId }}</span>
              </p>
            </div>

            <!-- Email Notification Section -->
            <div
                class="email-section mt-8 pa-4"
                :class="{ 'email-visible': showEmail }"
            >
              <div class="d-flex align-center justify-center ga-3 mb-3">
                <div class="email-icon-wrapper">
                  <v-icon icon="mdi-email-outline" size="20" color="primary" />
                </div>
                <div class="text-left">
                  <p class="text-body-2 font-weight-medium">
                    {{ isSuccess ? 'Confirmação enviada' : 'Notificação enviada' }}
                  </p>
                  <p class="text-caption text-medium-emphasis">para seu e-mail</p>
                </div>
              </div>

              <div class="email-display d-flex align-center justify-center ga-2 py-2 px-4">
                <v-icon icon="mdi-email" size="16" color="grey-darken-1" />
                <span class="text-body-2 font-weight-medium text-truncate">{{ auth.user.email }}</span>
              </div>

              <p class="text-caption text-medium-emphasis mt-3">
                {{ isSuccess
                  ? 'Você receberá todos os detalhes do pedido e informações de rastreamento'
                  : 'Enviamos os detalhes sobre o cancelamento para seu e-mail'
                }}
              </p>
            </div>

            <!-- Progress Indicator for Success -->
            <div
                v-if="isSuccess && showEmail"
                class="d-flex align-center justify-center ga-2 mt-6 text-body-2 text-medium-emphasis"
            >
              <v-progress-circular indeterminate size="16" width="2" color="primary" />
              <span>Preparando seu pedido...</span>
            </div>

            <!-- Action Buttons -->
            <div
                class="actions-container mt-8 d-flex flex-column ga-3"
                :class="{ 'actions-visible': showEmail }"
            >
              <template v-if="isSuccess">
                <v-btn
                    href="/"
                    color="primary"
                    size="large"
                    block
                    class="btn-action"
                >
                  Acompanhar Pedido
                </v-btn>
                <v-btn
                    href="/"
                    variant="outlined"
                    size="large"
                    block
                    class="btn-action"
                    prepend-icon="mdi-arrow-left"
                >
                  Voltar às Compras
                </v-btn>
              </template>
              <template v-else>
                <v-btn
                    href="/"
                    color="primary"
                    size="large"
                    block
                    class="btn-action"
                >
                  Tentar Novamente
                </v-btn>
                <v-btn
                    href="/"
                    variant="outlined"
                    size="large"
                    block
                    class="btn-action"
                    prepend-icon="mdi-arrow-left"
                >
                  Voltar à Loja
                </v-btn>
              </template>
            </div>
          </v-card>

          <!-- Support Text -->
          <p class="text-center text-body-2 text-medium-emphasis mt-6">
            Precisa de ajuda?
            <a href="/" class="text-primary font-weight-medium text-decoration-none support-link">
              Fale conosco
            </a>
          </p>
        </div>
      </v-container>
    </v-main>

    <!-- Footer -->
    <v-footer class="footer-bar">
      <v-container class="d-flex flex-column flex-sm-row align-center justify-space-between ga-4">
        <span class="text-body-2 text-medium-emphasis">
          © 2024 Go Drop. Todos os direitos reservados.
        </span>
        <div class="d-flex align-center ga-4">
          <a href="/" class="footer-link">Termos</a>
          <a href="/" class="footer-link">Privacidade</a>
          <a href="/" class="footer-link">Suporte</a>
        </div>
      </v-container>
    </v-footer>
  </v-app>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import {useRoute} from "vue-router";
import { useAuthStore } from "../stores/auth.js";

const auth = useAuthStore()
const route = useRoute()
const orderId = ref(route.query.orderId || '12345')



const props = defineProps({
  status: {
    type: String,
    default: 'success',
    validator: (value) => ['success', 'cancelled'].includes(value)
  },
  orderId: {
    type: String,
    default: '#GD2024001'
  }
})

const mounted = ref(false)
const showContent = ref(false)
const showEmail = ref(false)

const isSuccess = computed(() => props.status === 'success')

onMounted(() => {
  mounted.value = true
  setTimeout(() => {
    showContent.value = true
  }, 300)
  setTimeout(() => {
    showEmail.value = true
  }, 800)
})
</script>

<style scoped>
.header-bar {
  border-bottom: 1px solid rgba(0, 0, 0, 0.08) !important;
  background: rgba(255, 255, 255, 0.9) !important;
  backdrop-filter: blur(8px);
}

.logo-link {
  gap: 8px;
}

.logo-icon {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  background: #2563eb;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo-text {
  font-size: 1.25rem;
  font-weight: 700;
  color: #1e293b;
  letter-spacing: -0.025em;
}

.main-content {
  background: #f8fafc;
  min-height: 100vh;
}

.status-wrapper {
  width: 100%;
  max-width: 420px;
}

.status-card {
  border-radius: 16px !important;
  border: 1px solid rgba(0, 0, 0, 0.08);
  opacity: 0;
  transform: translateY(32px);
  transition: all 0.7s cubic-bezier(0.4, 0, 0.2, 1);
}

.status-card.card-visible {
  opacity: 1;
  transform: translateY(0);
}

.icon-container {
  position: relative;
  display: flex;
  justify-content: center;
}

.icon-circle {
  width: 96px;
  height: 96px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  transform: scale(0);
  transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  z-index: 1;
}

.icon-circle.icon-visible {
  transform: scale(1);
}

.icon-circle.icon-success {
  background: #ecfdf5;
  color: #10b981;
}

.icon-circle.icon-error {
  background: #fef2f2;
  color: #ef4444;
}

.icon-bounce {
  animation: bounce 1s ease-in-out;
}

.icon-shake {
  animation: shake 0.5s ease-in-out;
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% { transform: translateY(0); }
  40% { transform: translateY(-8px); }
  60% { transform: translateY(-4px); }
}

@keyframes shake {
  0%, 100% { transform: translateX(0); }
  20%, 60% { transform: translateX(-4px); }
  40%, 80% { transform: translateX(4px); }
}

.pulse-ring {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 96px;
  height: 96px;
  border-radius: 50%;
  background: rgba(16, 185, 129, 0.2);
  animation: pulse 2s ease-out infinite;
}

@keyframes pulse {
  0% { transform: translate(-50%, -50%) scale(1); opacity: 1; }
  100% { transform: translate(-50%, -50%) scale(1.5); opacity: 0; }
}

.status-text {
  opacity: 0;
  transform: translateY(16px);
  transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1) 0.2s;
}

.status-text.content-visible {
  opacity: 1;
  transform: translateY(0);
}

.email-section {
  background: #f8fafc;
  border-radius: 12px;
  border: 1px solid rgba(0, 0, 0, 0.08);
  opacity: 0;
  transform: translateY(16px);
  transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1) 0.5s;
}

.email-section.email-visible {
  opacity: 1;
  transform: translateY(0);
}

.email-icon-wrapper {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: rgba(37, 99, 235, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
}

.email-display {
  background: white;
  border-radius: 8px;
  border: 1px solid rgba(0, 0, 0, 0.08);
}

.actions-container {
  opacity: 0;
  transform: translateY(16px);
  transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1) 0.7s;
}

.actions-container.actions-visible {
  opacity: 1;
  transform: translateY(0);
}

.btn-action {
  height: 48px !important;
  font-weight: 500;
  letter-spacing: 0;
  text-transform: none;
  border-radius: 12px !important;
}

.support-link:hover {
  text-decoration: underline !important;
}

.footer-bar {
  border-top: 1px solid rgba(0, 0, 0, 0.08);
  background: white;
}

.footer-link {
  color: #64748b;
  text-decoration: none;
  font-size: 0.875rem;
  transition: color 0.2s;
}

.footer-link:hover {
  color: #1e293b;
}
</style>

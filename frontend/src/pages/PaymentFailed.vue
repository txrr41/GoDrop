<template>
  <div class="payment-failed-page">
    <v-container class="failed-container">
      <div class="failed-content" :class="{ 'show': mounted }">
        <!-- Animação de X -->
        <div class="failed-animation">
          <div class="circle-border error">
            <div class="circle">
              <div class="error-icon">
                <div class="error-line line-left"></div>
                <div class="error-line line-right"></div>
                <div class="error-circle"></div>
              </div>
            </div>
          </div>
          <div class="sad-particles">
            <div v-for="i in 20" :key="i" class="particle" :style="particleStyle()"></div>
          </div>
        </div>

        <!-- Mensagem de Erro -->
        <h1 class="failed-title">Pagamento Não Aprovado</h1>
        <p class="failed-subtitle">Não foi possível processar seu pagamento</p>

        <!-- Informações do Erro -->
        <div class="error-info-card">
          <div class="error-header">
            <v-icon color="error" size="24">mdi-alert-circle</v-icon>
            <span>Detalhes do Problema</span>
          </div>

          <div class="error-message">
            <p>{{ errorMessage }}</p>
          </div>

          <div class="error-code">
            <span class="code-label">Código de Referência:</span>
            <span class="code-value">#{{ errorCode }}</span>
          </div>
        </div>

        <!-- Possíveis Causas -->
        <div class="possible-causes">
          <h3>Possíveis Causas</h3>
          <div class="causes-list">
            <div class="cause-item">
              <v-icon color="warning" size="20">mdi-credit-card-off</v-icon>
              <span>Saldo insuficiente ou limite excedido</span>
            </div>
            <div class="cause-item">
              <v-icon color="warning" size="20">mdi-lock-alert</v-icon>
              <span>Dados do cartão incorretos</span>
            </div>
            <div class="cause-item">
              <v-icon color="warning" size="20">mdi-shield-alert</v-icon>
              <span>Transação bloqueada pelo banco</span>
            </div>
            <div class="cause-item">
              <v-icon color="warning" size="20">mdi-calendar-remove</v-icon>
              <span>Cartão vencido ou inválido</span>
            </div>
          </div>
        </div>

        <!-- Próximos Passos -->
        <div class="next-actions">
          <h3>O que você pode fazer?</h3>
          <div class="actions-grid">
            <div class="action-card" @click="tryAgain">
              <div class="action-icon">
                <v-icon color="primary" size="32">mdi-refresh</v-icon>
              </div>
              <h4>Tentar Novamente</h4>
              <p>Revise os dados e tente processar o pagamento novamente</p>
            </div>

            <div class="action-card" @click="changePaymentMethod">
              <div class="action-icon">
                <v-icon color="primary" size="32">mdi-credit-card-multiple</v-icon>
              </div>
              <h4>Outro Método</h4>
              <p>Escolha uma forma de pagamento diferente</p>
            </div>

            <div class="action-card" @click="contactSupport">
              <div class="action-icon">
                <v-icon color="primary" size="32">mdi-headset</v-icon>
              </div>
              <h4>Suporte</h4>
              <p>Fale com nossa equipe para ajudar você</p>
            </div>
          </div>
        </div>

        <!-- Resumo do Pedido -->
        <div class="order-summary-card">
          <div class="summary-header">
            <v-icon color="grey" size="20">mdi-cart</v-icon>
            <span>Seus itens ainda estão salvos</span>
          </div>
          <div class="summary-details">
            <div class="summary-row">
              <span>Total do Pedido</span>
              <span class="summary-value">{{ formatCurrency(orderTotal) }}</span>
            </div>
            <div class="summary-row">
              <span>Itens no Carrinho</span>
              <span class="summary-value">{{ itemCount }} {{ itemCount === 1 ? 'item' : 'itens' }}</span>
            </div>
          </div>
        </div>

        <!-- Botões de Ação -->
        <div class="action-buttons">
          <v-btn
              color="error"
              size="large"
              variant="flat"
              @click="tryAgain"
              class="action-btn"
          >
            <v-icon start>mdi-refresh</v-icon>
            Tentar Novamente
          </v-btn>
          <v-btn
              color="grey"
              size="large"
              variant="outlined"
              @click="goToHome"
              class="action-btn"
          >
            <v-icon start>mdi-home</v-icon>
            Voltar ao Início
          </v-btn>
        </div>

        <!-- Ajuda -->
        <div class="help-section">
          <v-icon color="primary" size="20">mdi-information</v-icon>
          <p>Precisa de ajuda? <a href="#" @click.prevent="contactSupport">Entre em contato conosco</a></p>
        </div>
      </div>
    </v-container>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useCartStore } from '../stores/cart'

const router = useRouter()
const route = useRoute()
const cartStore = useCartStore()

const mounted = ref(false)
const errorCode = ref(route.query.errorCode || 'ERR' + Math.random().toString(36).substr(2, 9).toUpperCase())
const orderTotal = ref(parseFloat(route.query.amount) || cartStore.total || 0)
const itemCount = ref(cartStore.totalItems || 0)

const errorMessage = computed(() => {
  const defaultMessage = 'O pagamento não pôde ser processado. Verifique os dados do seu cartão e tente novamente.'
  return route.query.message || defaultMessage
})

const formatCurrency = (value) => {
  return value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
}

const particleStyle = () => {
  return {
    left: `${Math.random() * 100}%`,
    animationDelay: `${Math.random() * 2}s`,
    animationDuration: `${2 + Math.random() * 2}s`
  }
}

const tryAgain = () => {
  router.push('/checkout')
}

const changePaymentMethod = () => {
  router.push('/checkout?step=payment')
}

const contactSupport = () => {
  // Implementar chat ou redirecionar para suporte
  alert('Redirecionando para suporte...')
}

const goToHome = () => {
  router.push('/home')
}

onMounted(() => {
  setTimeout(() => {
    mounted.value = true
  }, 100)
})
</script>

<style scoped>
.payment-failed-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  padding: 40px 0;
}

.failed-container {
  max-width: 700px;
}

.failed-content {
  opacity: 0;
  transform: translateY(30px);
  transition: all 0.6s cubic-bezier(0.4, 0, 0.2, 1);
}

.failed-content.show {
  opacity: 1;
  transform: translateY(0);
}

/* Animação de X */
.failed-animation {
  position: relative;
  margin: 0 auto 40px;
  width: 150px;
  height: 150px;
}

.circle-border {
  width: 150px;
  height: 150px;
  padding: 3px;
  background: linear-gradient(135deg, #F44336 0%, #E91E63 100%);
  border-radius: 50%;
  animation: borderShake 0.8s ease-out;
}

.circle {
  width: 100%;
  height: 100%;
  background: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.error-icon {
  position: relative;
  width: 80px;
  height: 80px;
}

.error-line {
  position: absolute;
  width: 60px;
  height: 5px;
  background-color: #F44336;
  border-radius: 3px;
  top: 50%;
  left: 50%;
  transform-origin: center;
}

.line-left {
  transform: translate(-50%, -50%) rotate(45deg);
  animation: errorLineLeft 0.5s 0.3s cubic-bezier(0.4, 0, 0.2, 1) forwards;
  opacity: 0;
  width: 0;
}

.line-right {
  transform: translate(-50%, -50%) rotate(-45deg);
  animation: errorLineRight 0.5s 0.5s cubic-bezier(0.4, 0, 0.2, 1) forwards;
  opacity: 0;
  width: 0;
}

.error-circle {
  position: absolute;
  top: -3px;
  left: -3px;
  width: 86px;
  height: 86px;
  border-radius: 50%;
  border: 5px solid #F44336;
  animation: scaleCircle 0.5s cubic-bezier(0.4, 0, 0.2, 1) forwards;
  transform: scale(0);
}

/* Partículas */
.sad-particles {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: visible;
  pointer-events: none;
}

.particle {
  position: absolute;
  width: 6px;
  height: 6px;
  background: rgba(244, 67, 54, 0.6);
  border-radius: 50%;
  top: 50%;
  left: 50%;
  opacity: 0;
  animation: particleFall 3s ease-out forwards;
}

/* Título e Subtítulo */
.failed-title {
  font-size: 42px;
  font-weight: 700;
  color: white;
  margin: 0 0 12px 0;
  text-align: center;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.failed-subtitle {
  font-size: 18px;
  color: rgba(255, 255, 255, 0.9);
  text-align: center;
  margin: 0 0 40px 0;
}

/* Card de Informações de Erro */
.error-info-card {
  background: white;
  border-radius: 20px;
  padding: 32px;
  margin-bottom: 32px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  border-left: 5px solid #F44336;
}

.error-header {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 18px;
  font-weight: 600;
  color: #1A2332;
  margin-bottom: 20px;
}

.error-message {
  background: rgba(244, 67, 54, 0.05);
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 16px;
}

.error-message p {
  font-size: 15px;
  color: #5E6D82;
  margin: 0;
  line-height: 1.6;
}

.error-code {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 16px;
  border-top: 1px solid #E3E8EF;
}

.code-label {
  font-size: 13px;
  color: #5E6D82;
}

.code-value {
  font-size: 14px;
  font-weight: 600;
  color: #F44336;
  font-family: monospace;
}

/* Possíveis Causas */
.possible-causes {
  background: white;
  border-radius: 20px;
  padding: 32px;
  margin-bottom: 32px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.possible-causes h3 {
  font-size: 20px;
  font-weight: 600;
  color: #1A2332;
  margin: 0 0 20px 0;
}

.causes-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.cause-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: #FFF9E6;
  border-radius: 10px;
  font-size: 14px;
  color: #5E6D82;
}

/* Próximas Ações */
.next-actions {
  background: white;
  border-radius: 20px;
  padding: 32px;
  margin-bottom: 32px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.next-actions h3 {
  font-size: 20px;
  font-weight: 600;
  color: #1A2332;
  margin: 0 0 24px 0;
}

.actions-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 16px;
}

.action-card {
  padding: 24px;
  background: #F5F7FA;
  border-radius: 12px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.action-card:hover {
  background: white;
  border-color: #1565C0;
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(21, 101, 192, 0.2);
}

.action-icon {
  width: 64px;
  height: 64px;
  margin: 0 auto 16px;
  background: white;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.action-card h4 {
  font-size: 16px;
  font-weight: 600;
  color: #1A2332;
  margin: 0 0 8px 0;
}

.action-card p {
  font-size: 13px;
  color: #5E6D82;
  margin: 0;
  line-height: 1.4;
}

/* Resumo do Pedido */
.order-summary-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 32px;
  backdrop-filter: blur(10px);
}

.summary-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  font-weight: 600;
  color: #5E6D82;
  margin-bottom: 16px;
}

.summary-details {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  color: #5E6D82;
}

.summary-value {
  font-weight: 600;
  color: #1A2332;
}

/* Botões */
.action-buttons {
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
}

.action-btn {
  flex: 1;
  font-weight: 600;
  letter-spacing: 0.5px;
}

/* Ajuda */
.help-section {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 16px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 12px;
  backdrop-filter: blur(10px);
}

.help-section p {
  font-size: 14px;
  color: white;
  margin: 0;
}

.help-section a {
  color: white;
  font-weight: 600;
  text-decoration: underline;
}

/* Animações */
@keyframes borderShake {
  0%, 100% {
    transform: translateX(0);
  }
  10%, 30%, 50%, 70%, 90% {
    transform: translateX(-5px);
  }
  20%, 40%, 60%, 80% {
    transform: translateX(5px);
  }
}

@keyframes scaleCircle {
  0% {
    transform: scale(0);
    opacity: 0;
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}

@keyframes errorLineLeft {
  0% {
    width: 0;
    opacity: 0;
  }
  100% {
    width: 60px;
    opacity: 1;
  }
}

@keyframes errorLineRight {
  0% {
    width: 0;
    opacity: 0;
  }
  100% {
    width: 60px;
    opacity: 1;
  }
}



/* Responsivo */
@media (max-width: 600px) {
  .failed-title {
    font-size: 32px;
  }

  .action-buttons {
    flex-direction: column;
  }

  .actions-grid {
    grid-template-columns: 1fr;
  }

  .error-info-card,
  .possible-causes,
  .next-actions {
    padding: 24px;
  }
}
</style>
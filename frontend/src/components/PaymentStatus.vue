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
                {{ getPaymentTitle() }}
              </h1>
              <p class="text-body-1 text-medium-emphasis mb-1">
                {{ getPaymentMessage() }}
              </p>
              <p v-if="orderId" class="text-body-2 text-medium-emphasis">
                Pedido: <span class="font-weight-medium text-high-emphasis">{{ orderId }}</span>
              </p>
            </div>

            <!-- PIX QR Code Section -->
            <div v-if="isPixPayment && pixQrCode" class="pix-section mt-8 pa-4" :class="{ 'pix-visible': showPix }">
              <div class="d-flex align-center justify-center ga-3 mb-3">
                <div class="pix-icon-wrapper">
                  <v-icon icon="mdi-qrcode" size="24" color="#32BCAD" />
                </div>
                <div class="text-left">
                  <p class="text-body-2 font-weight-medium">QR Code PIX</p>
                  <p class="text-caption text-medium-emphasis">Escaneie para pagar</p>
                </div>
              </div>

              <div class="qr-code-container mb-4">
                <img :src="`https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=${encodeURIComponent(pixQrCode)}`" 
                     alt="QR Code PIX" 
                     class="qr-code-image" />
              </div>

              <v-text-field
                  :model-value="pixCopyPaste"
                  label="Código PIX (Copie e Cole)"
                  readonly
                  variant="outlined"
                  density="compact"
                  class="pix-copy-paste"
              >
                <template v-slot:append>
                  <v-btn
                      icon="mdi-content-copy"
                      size="small"
                      variant="text"
                      @click="copyPixCode"
                  />
                </template>
              </v-text-field>

              <p class="text-caption text-medium-emphasis mt-3">
                O pagamento será confirmado automaticamente após a transferência.
              </p>
            </div>

            <!-- Boleto Section -->
            <div v-if="isBoletoPayment" class="boleto-section mt-8 pa-4" :class="{ 'boleto-visible': showBoleto }">
              <div class="d-flex align-center justify-center ga-3 mb-3">
                <div class="boleto-icon-wrapper">
                  <v-icon icon="mdi-barcode" size="24" color="primary" />
                </div>
                <div class="text-left">
                  <p class="text-body-2 font-weight-medium">Boleto Bancário</p>
                  <p class="text-caption text-medium-emphasis">Vencimento em {{ boletoDueDate }}</p>
                </div>
              </div>

              <v-btn
                  @click="downloadBoleto"
                  color="primary"
                  size="large"
                  block
                  class="btn-boleto mb-3"
              >
                <v-icon start>mdi-download</v-icon>
                Baixar Boleto
              </v-btn>

              <p class="text-caption text-medium-emphasis">
                A compensação pode levar até 48h úteis após o pagamento.
              </p>
            </div>

            <!-- Email Notification Section -->
            <div
                v-if="!isPixPayment && !isBoletoPayment"
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
                v-if="isSuccess && !isPixPayment && !isBoletoPayment && showEmail"
                class="d-flex align-center justify-center ga-2 mt-6 text-body-2 text-medium-emphasis"
            >
              <v-progress-circular indeterminate size="16" width="2" color="primary" />
              <span>Preparando seu pedido...</span>
            </div>

            <!-- Action Buttons -->
            <div
                class="actions-container mt-8 d-flex flex-column ga-3"
                :class="{ 'actions-visible': showActions }"
            >
              <template v-if="isSuccess">
                <v-btn
                    href="/orders"
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
                    href="/checkout"
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

    <!-- Snackbar for copy -->
    <v-snackbar v-model="copySnackbar" :timeout="2000" color="success">
      Código PIX copiado!
    </v-snackbar>
  </v-app>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import {useRoute} from "vue-router";
import { useAuthStore } from "../stores/auth.js";
import LogoDrop from '../assets/LogoDrop.png'

const auth = useAuthStore()
const route = useRoute()
const orderId = ref(route.query.orderId || '12345')
const paymentMethod = ref(route.query.method || 'CREDIT_CARD')

// PIX data
const pixQrCode = ref(route.query.pixQrCode || '')
const pixCopyPaste = ref(route.query.pixCopyPaste || '')

// Boleto data
const boletoUrl = ref(route.query.boletoUrl || '')
const boletoBarcode = ref(route.query.boletoBarcode || '')
const boletoDueDate = ref(route.query.boletoDueDate || '')

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
const showPix = ref(false)
const showBoleto = ref(false)
const showEmail = ref(false)
const showActions = ref(false)
const copySnackbar = ref(false)

const isSuccess = computed(() => props.status === 'success')
const isPixPayment = computed(() => paymentMethod.value === 'PIX')
const isBoletoPayment = computed(() => paymentMethod.value === 'BOLETO')

const getPaymentTitle = () => {
  if (isPixPayment.value) return 'Aguardando Pagamento PIX'
  if (isBoletoPayment.value) return 'Aguardando Pagamento Boleto'
  return isSuccess.value ? 'Pagamento Realizado!' : 'Pagamento Cancelado'
}

const getPaymentMessage = () => {
  if (isPixPayment.value) return 'Escaneie o QR Code ou copie o código para pagar'
  if (isBoletoPayment.value) return 'Baixe o boleto e pague até a data de vencimento'
  return isSuccess.value ? 'Seu pedido foi confirmado com sucesso' : 'O pagamento não foi concluído'
}

const copyPixCode = () => {
  navigator.clipboard.writeText(pixCopyPaste.value)
  copySnackbar.value = true
}

const downloadBoleto = () => {
  // Generate professional boleto HTML
  const boletoHtml = `
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Boleto Bancário - GoDrop</title>
  <style>
    * { margin: 0; padding: 0; box-sizing: border-box; }
    body {
      font-family: 'Segoe UI', 'Helvetica Neue', Arial, sans-serif;
      background: #f5f7fa;
      padding: 40px 20px;
      color: #2c3e50;
    }
    .boleto-container {
      max-width: 800px;
      margin: 0 auto;
      background: #ffffff;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
      border-radius: 4px;
      overflow: hidden;
    }
    .header {
      background: #1a365d;
      color: #ffffff;
      padding: 30px 40px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      border-bottom: 3px solid #c5a059;
    }
    .header-left { display: flex; align-items: center; gap: 20px; }
    .logo { height: 50px; width: auto; }
    .company-info h1 {
      font-size: 24px;
      font-weight: 600;
      letter-spacing: 0.5px;
      margin-bottom: 4px;
    }
    .company-info p {
      font-size: 12px;
      opacity: 0.85;
      font-weight: 400;
    }
    .header-right { text-align: right; }
    .document-type {
      font-size: 11px;
      text-transform: uppercase;
      letter-spacing: 1px;
      opacity: 0.8;
      margin-bottom: 4px;
    }
    .document-number {
      font-size: 16px;
      font-weight: 600;
      letter-spacing: 1px;
    }
    .notice-bar {
      background: #fff3cd;
      border-left: 4px solid #ffc107;
      padding: 12px 40px;
      font-size: 13px;
      color: #856404;
      font-weight: 500;
    }
    .content { padding: 40px; }
    .amount-section {
      text-align: center;
      padding: 25px;
      background: #f8f9fa;
      border: 1px solid #e9ecef;
      margin-bottom: 30px;
    }
    .amount-label {
      font-size: 12px;
      color: #6c757d;
      text-transform: uppercase;
      letter-spacing: 1px;
      margin-bottom: 8px;
    }
    .amount {
      font-size: 42px;
      font-weight: 700;
      color: #1a365d;
      margin: 0;
    }
    .section-title {
      font-size: 14px;
      font-weight: 600;
      color: #1a365d;
      text-transform: uppercase;
      letter-spacing: 0.5px;
      margin-bottom: 15px;
      padding-bottom: 8px;
      border-bottom: 2px solid #e9ecef;
    }
    .info-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 20px;
      margin-bottom: 30px;
    }
    .info-item {
      display: flex;
      flex-direction: column;
    }
    .info-label {
      font-size: 11px;
      color: #6c757d;
      text-transform: uppercase;
      letter-spacing: 0.5px;
      margin-bottom: 4px;
    }
    .info-value {
      font-size: 14px;
      font-weight: 500;
      color: #2c3e50;
    }
    .info-value.highlight {
      font-weight: 600;
      color: #1a365d;
    }
    .barcode-section {
      background: #1a365d;
      padding: 30px;
      margin-top: 30px;
      text-align: center;
      border-radius: 4px;
    }
    .barcode-label {
      font-size: 11px;
      color: rgba(255, 255, 255, 0.7);
      text-transform: uppercase;
      letter-spacing: 1px;
      margin-bottom: 15px;
    }
    .barcode {
      font-size: 18px;
      letter-spacing: 4px;
      font-family: 'Courier New', monospace;
      font-weight: 600;
      color: #ffffff;
      margin: 0 0 20px 0;
      word-break: break-all;
    }
    .barcode-visual {
      display: flex;
      justify-content: center;
      align-items: center;
      height: 50px;
      gap: 3px;
    }
    .barcode-bar {
      height: 100%;
      background: #ffffff;
      border-radius: 1px;
    }
    .barcode-bar.thin { width: 2px; }
    .barcode-bar.medium { width: 4px; }
    .barcode-bar.thick { width: 6px; }
    .barcode-bar.space { width: 2px; background: transparent; }
    .barcode-bar.space-wide { width: 4px; background: transparent; }
    .instructions {
      background: #f8f9fa;
      padding: 20px;
      margin-top: 30px;
      border-left: 3px solid #c5a059;
    }
    .instructions h3 {
      font-size: 13px;
      font-weight: 600;
      color: #1a365d;
      margin-bottom: 10px;
      text-transform: uppercase;
      letter-spacing: 0.5px;
    }
    .instructions ul {
      list-style: none;
      padding: 0;
    }
    .instructions li {
      font-size: 13px;
      color: #495057;
      margin-bottom: 8px;
      padding-left: 20px;
      position: relative;
    }
    .instructions li:before {
      content: "•";
      position: absolute;
      left: 0;
      color: #c5a059;
      font-weight: bold;
    }
    .footer {
      background: #2c3e50;
      color: #ffffff;
      text-align: center;
      padding: 25px 40px;
      font-size: 12px;
    }
    .footer p {
      margin: 4px 0;
      opacity: 0.85;
    }
    .footer .separator {
      width: 60px;
      height: 2px;
      background: #c5a059;
      margin: 15px auto;
    }
    .footer strong {
      font-weight: 600;
      color: #ffffff;
    }
    @media print {
      body { background: #ffffff; padding: 0; }
      .boleto-container { box-shadow: none; }
    }
  </style>
</head>
<body>
  <div class="boleto-container">
    <div class="header">
      <div class="header-left">
        <img src="${LogoDrop}" alt="GoDrop Logo" class="logo" />
        <div class="company-info">
          <h1>GoDrop</h1>
          <p>Plataforma de E-commerce Corporativa</p>
        </div>
      </div>
      <div class="header-right">
        <div class="document-type">Boleto Bancário</div>
        <div class="document-number">${orderId.value}</div>
      </div>
    </div>
    
    <div class="notice-bar">
      Documento para fins de demonstração - Não possui valor fiscal
    </div>
    
    <div class="content">
      <div class="amount-section">
        <div class="amount-label">Valor a Pagar</div>
        <div class="amount">R$ ${route.query.amount || '0,00'}</div>
      </div>
      
      <div class="section-title">Informações do Pagamento</div>
      <div class="info-grid">
        <div class="info-item">
          <span class="info-label">Número do Pedido</span>
          <span class="info-value highlight">${orderId.value}</span>
        </div>
        <div class="info-item">
          <span class="info-label">Data de Vencimento</span>
          <span class="info-value highlight">${boletoDueDate.value}</span>
        </div>
        <div class="info-item">
          <span class="info-label">Pagador</span>
          <span class="info-value">${auth.user?.name || 'Cliente Teste'}</span>
        </div>
        <div class="info-item">
          <span class="info-label">CNPJ do Beneficiário</span>
          <span class="info-value">00.000.000/0001-00</span>
        </div>
      </div>
      
      <div class="section-title">Dados Bancários</div>
      <div class="info-grid">
        <div class="info-item">
          <span class="info-label">Banco</span>
          <span class="info-value">Banco Teste (000)</span>
        </div>
        <div class="info-item">
          <span class="info-label">Agência / Conta</span>
          <span class="info-value">0001 / 12345-6</span>
        </div>
      </div>
      
      <div class="barcode-section">
        <div class="barcode-label">Código de Barras</div>
        <div class="barcode">12345.67890 12345.678901 12345.678906 1 23456789000001</div>
        <div class="barcode-visual">
          <div class="barcode-bar thick"></div>
          <div class="barcode-bar space"></div>
          <div class="barcode-bar thin"></div>
          <div class="barcode-bar medium"></div>
          <div class="barcode-bar thin"></div>
          <div class="barcode-bar space-wide"></div>
          <div class="barcode-bar thick"></div>
          <div class="barcode-bar medium"></div>
          <div class="barcode-bar thin"></div>
          <div class="barcode-bar space"></div>
          <div class="barcode-bar medium"></div>
          <div class="barcode-bar thick"></div>
          <div class="barcode-bar thin"></div>
          <div class="barcode-bar space-wide"></div>
          <div class="barcode-bar thin"></div>
          <div class="barcode-bar medium"></div>
          <div class="barcode-bar thick"></div>
          <div class="barcode-bar medium"></div>
          <div class="barcode-bar space"></div>
          <div class="barcode-bar thick"></div>
          <div class="barcode-bar thin"></div>
          <div class="barcode-bar medium"></div>
          <div class="barcode-bar space-wide"></div>
          <div class="barcode-bar medium"></div>
          <div class="barcode-bar thin"></div>
          <div class="barcode-bar thick"></div>
          <div class="barcode-bar medium"></div>
          <div class="barcode-bar space"></div>
          <div class="barcode-bar thin"></div>
          <div class="barcode-bar thick"></div>
          <div class="barcode-bar medium"></div>
          <div class="barcode-bar thin"></div>
          <div class="barcode-bar space-wide"></div>
          <div class="barcode-bar thick"></div>
          <div class="barcode-bar medium"></div>
          <div class="barcode-bar thin"></div>
          <div class="barcode-bar space"></div>
          <div class="barcode-bar medium"></div>
          <div class="barcode-bar thick"></div>
          <div class="barcode-bar thin"></div>
          <div class="barcode-bar space-wide"></div>
          <div class="barcode-bar thin"></div>
          <div class="barcode-bar medium"></div>
          <div class="barcode-bar thick"></div>
          <div class="barcode-bar medium"></div>
          <div class="barcode-bar space"></div>
          <div class="barcode-bar thick"></div>
          <div class="barcode-bar thin"></div>
          <div class="barcode-bar medium"></div>
          <div class="barcode-bar space-wide"></div>
          <div class="barcode-bar medium"></div>
          <div class="barcode-bar thin"></div>
          <div class="barcode-bar thick"></div>
          <div class="barcode-bar medium"></div>
          <div class="barcode-bar space"></div>
          <div class="barcode-bar thin"></div>
          <div class="barcode-bar thick"></div>
          <div class="barcode-bar medium"></div>
          <div class="barcode-bar thin"></div>
          <div class="barcode-bar space-wide"></div>
          <div class="barcode-bar thick"></div>
          <div class="barcode-bar medium"></div>
          <div class="barcode-bar thin"></div>
          <div class="barcode-bar space"></div>
          <div class="barcode-bar medium"></div>
          <div class="barcode-bar thick"></div>
          <div class="barcode-bar thin"></div>
          <div class="barcode-bar space-wide"></div>
          <div class="barcode-bar thin"></div>
          <div class="barcode-bar medium"></div>
          <div class="barcode-bar thick"></div>
          <div class="barcode-bar medium"></div>
          <div class="barcode-bar space"></div>
          <div class="barcode-bar thick"></div>
          <div class="barcode-bar thin"></div>
          <div class="barcode-bar medium"></div>
          <div class="barcode-bar space-wide"></div>
          <div class="barcode-bar medium"></div>
          <div class="barcode-bar thin"></div>
          <div class="barcode-bar thick"></div>
          <div class="barcode-bar medium"></div>
          <div class="barcode-bar space"></div>
          <div class="barcode-bar thin"></div>
          <div class="barcode-bar thick"></div>
          <div class="barcode-bar medium"></div>
          <div class="barcode-bar thin"></div>
          <div class="barcode-bar space-wide"></div>
          <div class="barcode-bar thick"></div>
          <div class="barcode-bar medium"></div>
          <div class="barcode-bar thin"></div>
        </div>
      </div>
      
      <div class="instructions">
        <h3>Instruções de Pagamento</h3>
        <ul>
          <li>Este boleto pode ser pago em qualquer agência bancária ou internet banking</li>
          <li>O pagamento será compensado em até 48 horas úteis após a liquidação</li>
          <li>Após o pagamento, você receberá uma confirmação por e-mail</li>
          <li>Mantenha o comprovante de pagamento para futuras consultas</li>
        </ul>
      </div>
    </div>
    
    <div class="footer">
      <p><strong>GoDrop</strong> - Soluções em E-commerce</p>
      <div class="separator"></div>
      <p>Documento gerado em ${new Date().toLocaleDateString('pt-BR')} às ${new Date().toLocaleTimeString('pt-BR')}</p>
      <p>© 2024 GoDrop. Todos os direitos reservados.</p>
    </div>
  </div>
</body>
</html>
  `
  
  // Create a blob and download the file
  const blob = new Blob([boletoHtml], { type: 'text/html' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `boleto_${orderId.value}.html`
  document.body.appendChild(a)
  a.click()
  document.body.removeChild(a)
  URL.revokeObjectURL(url)
}

onMounted(() => {
  mounted.value = true
  setTimeout(() => {
    showContent.value = true
  }, 300)
  
  if (isPixPayment.value) {
    setTimeout(() => {
      showPix.value = true
    }, 500)
  } else if (isBoletoPayment.value) {
    setTimeout(() => {
      showBoleto.value = true
    }, 500)
  } else {
    setTimeout(() => {
      showEmail.value = true
    }, 800)
  }
  
  setTimeout(() => {
    showActions.value = true
  }, 1000)
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

/* PIX Section Styles */
.pix-section {
  background: #f0fdf4;
  border-radius: 12px;
  border: 1px solid rgba(50, 188, 173, 0.2);
  opacity: 0;
  transform: translateY(16px);
  transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

.pix-section.pix-visible {
  opacity: 1;
  transform: translateY(0);
}

.pix-icon-wrapper {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: rgba(50, 188, 173, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
}

.qr-code-container {
  display: flex;
  justify-content: center;
  padding: 16px;
  background: white;
  border-radius: 8px;
  border: 1px solid rgba(0, 0, 0, 0.08);
}

.qr-code-image {
  width: 200px;
  height: 200px;
  object-fit: contain;
}

.pix-copy-paste {
  background: white;
}

/* Boleto Section Styles */
.boleto-section {
  background: #eff6ff;
  border-radius: 12px;
  border: 1px solid rgba(37, 99, 235, 0.2);
  opacity: 0;
  transform: translateY(16px);
  transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

.boleto-section.boleto-visible {
  opacity: 1;
  transform: translateY(0);
}

.boleto-icon-wrapper {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: rgba(37, 99, 235, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-boleto {
  height: 48px !important;
  font-weight: 500;
  letter-spacing: 0;
  text-transform: none;
  border-radius: 12px !important;
}
</style>

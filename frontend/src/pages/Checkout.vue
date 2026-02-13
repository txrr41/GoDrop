<template>
  <div class="checkout-premium">
    <!-- Navbar de Checkout (Segura) -->
    <nav class="checkout-nav">
      <v-container class="d-flex align-center justify-space-between py-4">
        <div class="brand-secure">
          <v-icon color="primary" size="28">mdi-shield-check</v-icon>
          <span class="brand-text">Checkout Seguro</span>
        </div>
        <v-btn variant="text" color="grey-darken-1" @click="router.push('/produtos')" class="text-none">
          <v-icon start size="18">mdi-arrow-left</v-icon>
          Continuar Comprando
        </v-btn>
      </v-container>
    </nav>

    <v-container class="mt-8 pb-16">
      <!-- Indicador de Etapas -->
      <div class="steps-indicator mb-12">
        <div v-for="(step, index) in ['Entrega', 'Pagamento', 'Revisão']" :key="index"
             :class="['step-item', { active: currentStep >= index }]">
          <div class="step-number">{{ index + 1 }}</div>
          <span class="step-label">{{ step }}</span>
          <div v-if="index < 2" class="step-line"></div>
        </div>
      </div>

      <v-row>
        <!-- Coluna Principal (Formulários) -->
        <v-col cols="12" lg="8">
          <v-form ref="formRef">
            <!-- Seção 1: Entrega -->
            <section class="checkout-section active mb-8">
              <div class="section-header">
                <v-icon color="primary" class="mr-3">mdi-map-marker-outline</v-icon>
                <h2>Endereço de Entrega</h2>
              </div>

              <div class="section-body">
                <v-row>
                  <v-col cols="12" md="4">
                    <v-text-field
                        v-model="formData.cep"
                        label="CEP"
                        placeholder="00000-000"
                        variant="outlined"
                        bg-color="grey-lighten-5"
                        @input="formatCEP"
                        @blur="buscarCEP"
                        maxlength="9"
                        :loading="loadingCEP"
                        :rules="[rules.required]"
                    />
                  </v-col>
                  <v-col cols="12" md="8">
                    <v-text-field
                        v-model="formData.rua"
                        label="Endereço (Rua/Avenida)"
                        variant="outlined"
                        bg-color="grey-lighten-5"
                        :rules="[rules.required]"
                    />
                  </v-col>
                  <v-col cols="12" md="3">
                    <v-text-field
                        v-model="formData.numero"
                        label="Número"
                        variant="outlined"
                        bg-color="grey-lighten-5"
                        :rules="[rules.required]"
                    />
                  </v-col>
                  <v-col cols="12" md="9">
                    <v-text-field
                        v-model="formData.complemento"
                        label="Complemento (Opcional)"
                        variant="outlined"
                        bg-color="grey-lighten-5"
                    />
                  </v-col>
                  <v-col cols="12" md="5">
                    <v-text-field
                        v-model="formData.bairro"
                        label="Bairro"
                        variant="outlined"
                        bg-color="grey-lighten-5"
                        :rules="[rules.required]"
                    />
                  </v-col>
                  <v-col cols="12" md="5">
                    <v-text-field
                        v-model="formData.cidade"
                        label="Cidade"
                        variant="outlined"
                        bg-color="grey-lighten-5"
                        :rules="[rules.required]"
                    />
                  </v-col>
                  <v-col cols="12" md="2">
                    <v-text-field
                        v-model="formData.estado"
                        label="UF"
                        variant="outlined"
                        bg-color="grey-lighten-5"
                        maxlength="2"
                        :rules="[rules.required]"
                    />
                  </v-col>
                </v-row>
              </div>
            </section>

            <!-- Seção 2: Dados Pessoais -->
            <section class="checkout-section mb-8">
              <div class="section-header">
                <v-icon color="primary" class="mr-3">mdi-account-outline</v-icon>
                <h2>Informações do Comprador</h2>
              </div>
              <div class="section-body">
                <v-row>
                  <v-col cols="12" md="6">
                    <v-text-field v-model="formData.nome" label="Nome Completo" variant="outlined" bg-color="grey-lighten-5" :rules="[rules.required]" />
                  </v-col>
                  <v-col cols="12" md="6">
                    <v-text-field v-model="formData.email" label="E-mail para notificações" variant="outlined" bg-color="grey-lighten-5" :rules="[rules.required, rules.email]" />
                  </v-col>
                  <v-col cols="12" md="6">
                    <v-text-field v-model="formData.cpf" label="CPF" placeholder="000.000.000-00" variant="outlined" bg-color="grey-lighten-5" @input="formatCPF" maxlength="14" :rules="[rules.required]" />
                  </v-col>
                  <v-col cols="12" md="6">
                    <v-text-field v-model="formData.telefone" label="Celular/WhatsApp" placeholder="(00) 00000-0000" variant="outlined" bg-color="grey-lighten-5" @input="formatTelefone" maxlength="15" :rules="[rules.required]" />
                  </v-col>
                </v-row>
              </div>
            </section>

            <!-- Seção 3: Pagamento -->
            <section class="checkout-section mb-8">
              <div class="section-header">
                <v-icon color="primary" class="mr-3">mdi-credit-card-outline</v-icon>
                <h2>Método de Pagamento</h2>
              </div>

              <div class="payment-grid mt-4">
                <!-- Opção Cartão -->
                <div
                    class="payment-card-option"
                    :class="{ active: formData.pagamento === 'CREDIT_CARD' }"
                    @click="formData.pagamento = 'CREDIT_CARD'"
                >
                  <v-icon size="24">mdi-credit-card</v-icon>
                  <span>Cartão</span>
                  <v-icon v-if="formData.pagamento === 'CREDIT_CARD'" class="check-icon">mdi-check-circle</v-icon>
                </div>

                <!-- Opção PIX -->
                <div
                    class="payment-card-option"
                    :class="{ active: formData.pagamento === 'PIX' }"
                    @click="formData.pagamento = 'PIX'"
                >
                  <v-icon size="24">mdi-qrcode-scan</v-icon>
                  <span>PIX</span>
                  <v-chip size="x-small" color="success" class="promo-badge">5% OFF</v-chip>
                  <v-icon v-if="formData.pagamento === 'PIX'" class="check-icon">mdi-check-circle</v-icon>
                </div>

                <!-- Opção Boleto -->
                <div
                    class="payment-card-option"
                    :class="{ active: formData.pagamento === 'BOLETO' }"
                    @click="formData.pagamento = 'BOLETO'"
                >
                  <v-icon size="24">mdi-barcode</v-icon>
                  <span>Boleto</span>
                  <v-icon v-if="formData.pagamento === 'BOLETO'" class="check-icon">mdi-check-circle</v-icon>
                </div>
              </div>

              <div class="payment-details-container mt-6">
                <!-- Detalhes do Cartão (Stripe) -->
                <div v-show="formData.pagamento === 'CREDIT_CARD'" class="stripe-container">
                  <p class="text-caption text-grey-darken-1 mb-3">Insira os dados do seu cartão de forma segura:</p>
                  <div id="card-element" class="stripe-card-element"></div>
                  <div id="card-errors" class="stripe-card-errors"></div>
                  <v-img src="https://vignette.wikia.nocookie.net/p__/images/3/30/Stripe_logo.png/revision/latest?cb=20190521101515&path-prefix=protagonist" width="60" class="mt-4 opacity-50" />
                </div>

                <!-- Preview PIX -->
                <div v-if="formData.pagamento === 'PIX'" class="pix-preview text-center py-4">
                  <v-icon size="48" color="primary">mdi-lightning-bolt</v-icon>
                  <p class="mt-2 font-weight-bold">Aprovação imediata</p>
                  <p class="text-caption">O QR Code será gerado após clicar em finalizar.</p>
                </div>

                <!-- Preview Boleto -->
                <div v-if="formData.pagamento === 'BOLETO'" class="boleto-preview text-center py-4">
                  <v-icon size="48" color="grey-darken-2">mdi-file-document-outline</v-icon>
                  <p class="mt-2 font-weight-bold">Vencimento em 3 dias</p>
                  <p class="text-caption">A compensação pode levar até 48h úteis.</p>
                </div>
              </div>
            </section>
          </v-form>
        </v-col>

        <!-- Coluna Lateral (Resumo) -->
        <v-col cols="12" lg="4">
          <aside class="order-summary-sticky">
            <v-card class="summary-card-v2" elevation="0">
              <div class="summary-header">
                <h3>Resumo do Pedido</h3>
              </div>

              <div class="summary-items-list">
                <div v-for="item in cartStore.cartItems" :key="item.id" class="mini-product">
                  <div class="mini-thumb">
                    <img :src="item.imagem || '/placeholder.png'" />
                    <span class="mini-qty">{{ item.quantity }}</span>
                  </div>
                  <div class="mini-details">
                    <p class="mini-name">{{ item.nome }}</p>
                    <p class="mini-price">{{ formatCurrency(item.preco * item.quantity) }}</p>
                  </div>
                </div>
              </div>

              <v-divider class="my-4" />

              <div class="summary-totals-v2">
                <div class="total-row">
                  <span>Subtotal</span>
                  <span>{{ formatCurrency(cartStore.subtotal) }}</span>
                </div>
                <div class="total-row">
                  <span>Entrega Estimada</span>
                  <span class="text-success font-weight-bold">Grátis</span>
                </div>
                <div v-if="formData.pagamento === 'PIX'" class="total-row discount">
                  <span>Desconto PIX (5%)</span>
                  <span>- {{ formatCurrency(cartStore.subtotal * 0.05) }}</span>
                </div>

                <div class="grand-total-box mt-6">
                  <span class="total-label">Total a pagar</span>
                  <span class="total-value">{{ formatCurrency(formData.pagamento === 'PIX' ? cartStore.total * 0.95 : cartStore.total) }}</span>
                </div>
              </div>

              <v-btn
                  block
                  color="primary"
                  height="64"
                  class="checkout-final-btn mt-6"
                  @click="finalizarCompra"
                  :loading="loading"
              >
                <v-icon start size="20">mdi-lock-outline</v-icon>
                Finalizar Compra
              </v-btn>

              <div class="secure-badges mt-6">
                <v-icon size="14">mdi-shield-check</v-icon>
                <span>Ambiente 100% criptografado e seguro</span>
              </div>
            </v-card>
          </aside>
        </v-col>
      </v-row>
    </v-container>

    <v-snackbar v-model="snackbar" :color="snackbarColor" location="top" elevation="24">
      {{ snackbarMessage }}
    </v-snackbar>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '../stores/cart'
import paymentService, { getStripe } from '../services/paymentService'

const router = useRouter()
const cartStore = useCartStore()
const loading = ref(false)
const loadingCEP = ref(false)
const formRef = ref(null)
const snackbar = ref(false)
const snackbarMessage = ref('')
const snackbarColor = ref('success')
const currentStep = ref(1)

let cardElement = null
let stripe = null

const formData = reactive({
  nome: '', email: '', telefone: '', cpf: '',
  cep: '', rua: '', numero: '', complemento: '',
  bairro: '', cidade: '', estado: '', pagamento: 'CREDIT_CARD'
})

const rules = {
  required: v => !!v || 'Campo obrigatório',
  email: v => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v) || 'E-mail inválido'
}

const formatTelefone = (e) => {
  const val = String(e.target.value || '')
  let v = val.replace(/\D/g, '')
  formData.telefone = v.length <= 10
      ? v.replace(/(\d{2})(\d{4})(\d{4})/, '($1) $2-$3')
      : v.replace(/(\d{2})(\d{5})(\d{4})/, '($1) $2-$3')
}

const formatCPF = (e) => {
  const val = String(e.target.value || '')
  formData.cpf = val.replace(/\D/g, '').replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4')
}

const formatCEP = (e) => {
  const val = String(e.target.value || '')
  formData.cep = val.replace(/\D/g, '').replace(/(\d{5})(\d{3})/, '$1-$2')
}

const formatCurrency = (v) => {
  const value = Number(v) || 0
  return value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
}

const buscarCEP = async () => {
  const cepValue = String(formData.cep || '')
  const cleanCep = cepValue.replace(/\D/g, '')
  if (cleanCep.length === 8) {
    loadingCEP.value = true
    try {
      const res = await fetch(`https://viacep.com.br/ws/${cleanCep}/json/`)
      const data = await res.json()
      if (data && !data.erro) {
        formData.rua = data.logradouro || ''
        formData.bairro = data.bairro || ''
        formData.cidade = data.localidade || ''
        formData.estado = data.uf || ''
      }
    } catch (err) {
      console.error("Erro ao buscar CEP:", err)
    } finally {
      loadingCEP.value = false
    }
  }
}

const initializeStripe = async () => {
  stripe = await getStripe()
  const elements = stripe.elements()
  cardElement = elements.create('card', {
    style: {
      base: {
        fontSize: '16px',
        fontFamily: '"Inter", sans-serif',
        color: '#1E293B',
        '::placeholder': { color: '#94A3B8' }
      }
    },
    hidePostalCode: true
  })
  setTimeout(() => {
    const el = document.getElementById('card-element')
    if (el) cardElement.mount('#card-element')
  }, 500)
}

onMounted(() => {
  if (cartStore.totalItems === 0) router.push('/produtos')
  else initializeStripe()
})

const showSnackbar = (m, c) => {
  snackbarMessage.value = m;
  snackbarColor.value = c;
  snackbar.value = true
}

const finalizarCompra = async () => {
  const { valid } = await formRef.value.validate()
  if (!valid) return showSnackbar('Preencha os campos obrigatórios', 'error')

  loading.value = true
  try {
    const cartItems = cartStore.cartItems.map(item => ({
      productId: item.id,
      quantity: item.quantity,
      unitPrice: item.preco
    }))

    const paymentData = {
      paymentMethod: formData.pagamento,
      amountInCents: Math.round(cartStore.total * 100),
      customerEmail: formData.email,
      buyerName: formData.nome,
      buyerEmail: formData.email,
      buyerPhone: String(formData.telefone).replace(/\D/g, ''),
      buyerCpf: String(formData.cpf).replace(/\D/g, ''),
      shippingCep: String(formData.cep).replace(/\D/g, ''),
      shippingStreet: formData.rua,
      shippingNumber: formData.numero,
      shippingComplement: formData.complemento,
      shippingNeighborhood: formData.bairro,
      shippingCity: formData.cidade,
      shippingState: formData.estado,
      items: cartItems
    }

    const { clientSecret } = await paymentService.createPaymentIntent(paymentData)

    if (formData.pagamento === 'CREDIT_CARD') {
      const result = await paymentService.confirmCardPayment(clientSecret, cardElement, {
        name: formData.nome,
        email: formData.email,
        address: {
          line1: formData.rua,
          city: formData.cidade,
          postal_code: formData.cep,
          country: 'BR'
        }
      })

      if (result.success) {
        cartStore.clearCart()
        await router.push({ path: '/payment-success', query: { orderId: result.paymentIntent?.metadata?.order_id || 'N/A', amount: cartStore.total, email: formData.email, method: formData.pagamento } })
      } else {
        await router.push({ path: '/payment-failed', query: { message: result.error || 'Erro ao processar pagamento', amount: cartStore.total, errorCode: 'PAY_' + Date.now() } })
      }
    } else {
      // Simulação PIX/Boleto
      setTimeout(() => {
        cartStore.clearCart()
        router.push({ path: '/payment-success', query: { orderId: 'PED' + Date.now(), amount: cartStore.total, email: formData.email, method: formData.pagamento } })
            , 2000})
    }
  } catch (e) {
    console.error('❌ Erro:', e)
    await router.push({ path: '/payment-failed', query: { message: 'Erro ao processar pedido. Tente novamente.', amount: cartStore.total, errorCode: 'SYS_' + Date.now() } })
  } finally {
    loading.value = false
  }
}

watch(() => formData.pagamento, (val) => {
  if (val === 'CREDIT_CARD') initializeStripe()
})
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap');

.checkout-premium {
  background-color: #FAFAFB;
  min-height: 100vh;
  font-family: 'Plus Jakarta Sans', sans-serif;
  color: #1E293B;
}

/* NAVBAR */
.checkout-nav {
  background: white;
  border-bottom: 1px solid #F1F5F9;
}

.brand-secure {
  display: flex;
  align-items: center;
  gap: 10px;
}

.brand-text {
  font-weight: 800;
  font-size: 18px;
  color: #0F172A;
  letter-spacing: -0.5px;
}

/* STEPPER */
.steps-indicator {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 40px;
}

.step-item {
  display: flex;
  align-items: center;
  gap: 12px;
  opacity: 0.4;
  position: relative;
  transition: all 0.3s;
}

.step-item.active {
  opacity: 1;
}

.step-number {
  width: 32px;
  height: 32px;
  background: #E2E8F0;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 800;
  font-size: 14px;
}

.active .step-number {
  background: #0061FF;
  color: white;
  box-shadow: 0 4px 12px rgba(0, 97, 255, 0.3);
}

.step-label {
  font-weight: 700;
  font-size: 15px;
}

.step-line {
  width: 100px;
  height: 2px;
  background: #E2E8F0;
  margin-left: 20px;
}

/* SEÇÕES */
.checkout-section {
  background: white;
  border: 1px solid #F1F5F9;
  border-radius: 24px;
  padding: 32px;
  transition: transform 0.3s, box-shadow 0.3s;
}

.checkout-section:hover {
  box-shadow: 0 10px 30px rgba(0,0,0,0.02);
}

.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 24px;
}

.section-header h2 {
  font-size: 20px;
  font-weight: 800;
  color: #0F172A;
}

/* PAYMENT OPTIONS */
.payment-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.payment-card-option {
  border: 2px solid #F1F5F9;
  border-radius: 16px;
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  position: relative;
  transition: all 0.2s;
  background: #F8FAFC;
}

.payment-card-option:hover {
  background: #F1F5F9;
  border-color: #E2E8F0;
}

.payment-card-option.active {
  border-color: #0061FF;
  background: #EFF6FF;
  color: #0061FF;
}

.payment-card-option span {
  font-weight: 700;
  font-size: 14px;
}

.check-icon {
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 18px;
}

.promo-badge {
  position: absolute;
  top: -10px;
  left: 50%;
  transform: translateX(-50%);
}

.stripe-card-element {
  background: white;
  padding: 16px;
  border: 1px solid #E2E8F0;
  border-radius: 12px;
}

/* SUMMARY SIDEBAR */
.order-summary-sticky {
  position: sticky;
  top: 32px;
}

.summary-card-v2 {
  background: white;
  border: 1px solid #F1F5F9;
  border-radius: 24px;
  padding: 32px;
}

.summary-header h3 {
  font-size: 18px;
  font-weight: 800;
  margin-bottom: 24px;
}

.mini-product {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
}

.mini-thumb {
  width: 64px;
  height: 64px;
  background: #F8FAFC;
  border-radius: 12px;
  position: relative;
  padding: 8px;
}

.mini-thumb img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.mini-qty {
  position: absolute;
  top: -8px;
  right: -8px;
  background: #0F172A;
  color: white;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  font-size: 11px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 800;
}

.mini-name {
  font-size: 14px;
  font-weight: 600;
  color: #1E293B;
  margin: 0;
}

.mini-price {
  font-size: 13px;
  color: #64748B;
  font-weight: 500;
}

.summary-totals-v2 .total-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;
  font-size: 14px;
  font-weight: 600;
  color: #64748B;
}

.total-row.discount span {
  color: #059669;
}

.grand-total-box {
  background: #F8FAFC;
  padding: 20px;
  border-radius: 16px;
  display: flex;
  flex-direction: column;
}

.grand-total-box .total-label {
  font-size: 12px;
  font-weight: 700;
  text-transform: uppercase;
  color: #94A3B8;
  margin-bottom: 4px;
}

.grand-total-box .total-value {
  font-size: 32px;
  font-weight: 800;
  color: #0061FF;
  letter-spacing: -1px;
}

.checkout-final-btn {
  border-radius: 16px !important;
  font-weight: 800 !important;
  text-transform: none !important;
  font-size: 18px !important;
}

.secure-badges {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  font-size: 11px;
  font-weight: 600;
  color: #94A3B8;
}

@media (max-width: 960px) {
  .payment-grid { grid-template-columns: 1fr; }
  .steps-indicator { gap: 10px; }
  .step-line { display: none; }
}
</style>
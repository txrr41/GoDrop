<template>
  <div class="checkout-page">
    <v-container>
      <div class="checkout-header">
        <h1>Finalizar Compra</h1>
        <v-btn variant="text" to="/produtos">
          <v-icon start>mdi-arrow-left</v-icon>
          Voltar para produtos
        </v-btn>
      </div>

      <div class="checkout-content">
        <div class="checkout-main">

          <v-card class="checkout-card">
            <v-card-title>
              <v-icon start>mdi-truck</v-icon>
              Informações de Entrega
            </v-card-title>
            <v-card-text>
              <v-form ref="formRef">
                <v-row>
                  <v-col cols="12" md="6">
                    <v-text-field
                        v-model="formData.nome"
                        label="Nome Completo *"
                        variant="outlined"
                        density="compact"
                        :rules="[rules.required]"
                    />
                  </v-col>
                  <v-col cols="12" md="6">
                    <v-text-field
                        v-model="formData.email"
                        label="E-mail *"
                        variant="outlined"
                        density="compact"
                        :rules="[rules.required, rules.email]"
                    />
                  </v-col>
                  <v-col cols="12" md="6">
                    <v-text-field
                        v-model="formData.telefone"
                        label="Telefone *"
                        variant="outlined"
                        density="compact"
                        :rules="[rules.required]"
                        placeholder="(00) 00000-0000"
                        @input="formatTelefone"
                        maxlength="15"
                    />
                  </v-col>
                  <v-col cols="12" md="6">
                    <v-text-field
                        v-model="formData.cpf"
                        label="CPF *"
                        variant="outlined"
                        density="compact"
                        :rules="[rules.required]"
                        placeholder="000.000.000-00"
                        @input="formatCPF"
                        maxlength="14"
                    />
                  </v-col>
                  <v-col cols="12" md="4">
                    <v-text-field
                        v-model="formData.cep"
                        label="CEP *"
                        variant="outlined"
                        density="compact"
                        :rules="[rules.required]"
                        placeholder="00000-000"
                        @input="formatCEP"
                        @blur="buscarCEP"
                        maxlength="9"
                        :loading="loadingCEP"
                    />
                  </v-col>
                  <v-col cols="12" md="6">
                    <v-text-field
                        v-model="formData.rua"
                        label="Rua *"
                        variant="outlined"
                        density="compact"
                        :rules="[rules.required]"
                    />
                  </v-col>
                  <v-col cols="12" md="2">
                    <v-text-field
                        v-model="formData.numero"
                        label="Número *"
                        variant="outlined"
                        density="compact"
                        :rules="[rules.required]"
                    />
                  </v-col>
                  <v-col cols="12" md="6">
                    <v-text-field
                        v-model="formData.complemento"
                        label="Complemento"
                        variant="outlined"
                        density="compact"
                    />
                  </v-col>
                  <v-col cols="12" md="6">
                    <v-text-field
                        v-model="formData.bairro"
                        label="Bairro *"
                        variant="outlined"
                        density="compact"
                        :rules="[rules.required]"
                    />
                  </v-col>
                  <v-col cols="12" md="8">
                    <v-text-field
                        v-model="formData.cidade"
                        label="Cidade *"
                        variant="outlined"
                        density="compact"
                        :rules="[rules.required]"
                    />
                  </v-col>
                  <v-col cols="12" md="4">
                    <v-text-field
                        v-model="formData.estado"
                        label="Estado *"
                        variant="outlined"
                        density="compact"
                        :rules="[rules.required]"
                        maxlength="2"
                        placeholder="UF"
                    />
                  </v-col>
                </v-row>
              </v-form>
            </v-card-text>
          </v-card>

          <v-card class="checkout-card mt-6">
            <v-card-title>
              <v-icon start>mdi-credit-card</v-icon>
              Forma de Pagamento
            </v-card-title>
            <v-card-text>
              <v-radio-group v-model="formData.pagamento">
                <v-radio value="CREDIT_CARD">
                  <template v-slot:label>
                    <div class="payment-option">
                      <v-icon color="primary" class="mr-2">mdi-credit-card</v-icon>
                      <div>
                        <strong>Cartão de Crédito</strong>
                        <p class="text-caption mb-0">Até 12x no cartão</p>
                      </div>
                    </div>
                  </template>
                </v-radio>

                <div v-if="formData.pagamento === 'CREDIT_CARD'" class="mt-4">
                  <div id="card-element" class="stripe-card-element"></div>
                  <div id="card-errors" class="stripe-card-errors"></div>
                </div>

                <v-radio value="PIX" disabled>
                  <template v-slot:label>
                    <div class="payment-option">
                      <v-icon color="primary" class="mr-2">mdi-qrcode</v-icon>
                      <div><strong>PIX</strong> (Em breve)</div>
                    </div>
                  </template>
                </v-radio>
              </v-radio-group>
            </v-card-text>
          </v-card>
        </div>

        <div class="checkout-sidebar">
          <v-card class="summary-card sticky">
            <v-card-title>Resumo do Pedido</v-card-title>
            <v-card-text>
              <div class="summary-items">
                <div v-for="item in cartStore.cartItems" :key="item.id" class="summary-item">
                  <div class="item-info">
                    <img :src="item.imagem || '/placeholder.png'" class="item-thumbnail" alt="item thumbnail"/>
                    <div>
                      <p class="item-name">{{ item.nome }}</p>
                      <p class="item-quantity">Qtd: {{ item.quantity }}</p>
                    </div>
                  </div>
                  <p class="item-price">{{ formatCurrency(item.preco * item.quantity) }}</p>
                </div>
              </div>

              <v-divider class="my-4" />

              <div class="summary-totals">
                <div class="total-row"><span>Subtotal</span><span>{{ formatCurrency(cartStore.subtotal) }}</span></div>
                <div class="total-row"><span>Frete</span><span>Grátis</span></div>
                <v-divider class="my-2" />
                <div class="total-row total"><span>Total</span><span>{{ formatCurrency(cartStore.total) }}</span></div>
              </div>

              <v-btn color="primary" size="large" block class="mt-4" @click="finalizarCompra" :loading="loading" :disabled="!formData.pagamento">
                <v-icon start>mdi-lock</v-icon> Finalizar Pedido
              </v-btn>
            </v-card-text>
          </v-card>
        </div>
      </div>
    </v-container>

    <v-snackbar v-model="snackbar" :color="snackbarColor" location="top">{{ snackbarMessage }}</v-snackbar>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
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
  cardElement = elements.create('card', { style: { base: { fontSize: '16px' } }, hidePostalCode: true })
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

// ADICIONE ESTAS LINHAS NO SEU Checkout.vue

// Na função finalizarCompra, após o resultado do pagamento:

const finalizarCompra = async () => {
  const { valid } = await formRef.value.validate()
  if (!valid) return showSnackbar('Preencha os campos obrigatórios', 'error')

  loading.value = true
  try {
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
      shippingState: formData.estado
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
        // ✅ REDIRECIONA PARA PÁGINA DE SUCESSO
        router.push({
          path: '/payment-success',
          query: {
            orderId: result.paymentIntent?.metadata?.order_id || 'N/A',
            amount: cartStore.total,
            email: formData.email,
            method: formData.pagamento
          }
        })
      } else {
        // ❌ REDIRECIONA PARA PÁGINA DE FALHA
        router.push({
          path: '/payment-failed',
          query: {
            message: result.error || 'Erro ao processar pagamento',
            amount: cartStore.total,
            errorCode: 'PAY_' + Date.now()
          }
        })
      }
    }
  } catch (e) {
    console.error('Erro:', e)
    // ❌ REDIRECIONA PARA PÁGINA DE FALHA
    router.push({
      path: '/payment-failed',
      query: {
        message: 'Erro ao processar pedido. Tente novamente.',
        amount: cartStore.total,
        errorCode: 'SYS_' + Date.now()
      }
    })
  } finally {
    loading.value = false
  }
}

watch(() => formData.pagamento, (val) => {
  if (val === 'CREDIT_CARD') initializeStripe()
})
</script>

<style scoped>
.checkout-page {
  min-height: 100vh; background-color: #F5F7FA; padding: 24px 0;
}
.checkout-content {
  display: grid; grid-template-columns: 1fr 400px; gap: 24px;
}
.stripe-card-element {
  border: 1px solid #E3E8EF; border-radius: 8px; padding: 12px; background: white;
}
.summary-item {
  display: flex; justify-content: space-between; margin-bottom: 10px;
}
.item-thumbnail {
  width: 50px; height: 50px; border-radius: 8px; margin-right: 10px; object-fit: cover;
}
.total-row.total {
  font-size: 20px; font-weight: bold;
}
@media (max-width: 960px) {
  .checkout-content { grid-template-columns: 1fr;
  } }
</style>
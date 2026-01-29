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
        <!-- Order Summary -->
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
                <v-radio value="pix">
                  <template v-slot:label>
                    <div class="payment-option">
                      <v-icon color="primary" class="mr-2">mdi-qrcode</v-icon>
                      <div>
                        <strong>PIX</strong>
                        <p class="text-caption mb-0">Aprovação Instantânea</p>
                      </div>
                    </div>
                  </template>
                </v-radio>
                <v-radio value="cartao">
                  <template v-slot:label>
                    <div class="payment-option">
                      <v-icon color="primary" class="mr-2">mdi-credit-card</v-icon>
                      <div>
                        <strong>Cartão de Crédito</strong>
                        <p class="text-caption mb-0">Parcelamento em até 12x sem juros</p>
                      </div>
                    </div>
                  </template>
                </v-radio>
                <v-radio value="boleto">
                  <template v-slot:label>
                    <div class="payment-option">
                      <v-icon color="primary" class="mr-2">mdi-barcode</v-icon>
                      <div>
                        <strong>Boleto Bancário</strong>
                        <p class="text-caption mb-0">5% de desconto à vista</p>
                      </div>
                    </div>
                  </template>
                </v-radio>
              </v-radio-group>
            </v-card-text>
          </v-card>
        </div>

        <!-- Order Summary Sidebar -->
        <div class="checkout-sidebar">
          <v-card class="summary-card sticky">
            <v-card-title>
              Resumo do Pedido
            </v-card-title>
            <v-card-text>
              <!-- Items -->
              <div class="summary-items">
                <div v-for="item in cartStore.cartItems" :key="item.id" class="summary-item">
                  <div class="item-info">
                    <img :src="item.imagem || '/placeholder.png'" :alt="item.nome" class="item-thumbnail" />
                    <div>
                      <p class="item-name">{{ item.nome }}</p>
                      <p class="item-quantity">Qtd: {{ item.quantity }}</p>
                    </div>
                  </div>
                  <p class="item-price">{{ formatCurrency(item.preco * item.quantity) }}</p>
                </div>
              </div>

              <v-divider class="my-4" />

              <!-- Totals -->
              <div class="summary-totals">
                <div class="total-row">
                  <span>Subtotal</span>
                  <span>{{ formatCurrency(cartStore.subtotal) }}</span>
                </div>
                <div class="total-row">
                  <span>Frete</span>
                  <span>{{ cartStore.shipping > 0 ? formatCurrency(cartStore.shipping) : 'Grátis' }}</span>
                </div>
                <div v-if="cartStore.discount > 0" class="total-row discount">
                  <span>Desconto</span>
                  <span>- {{ formatCurrency(cartStore.discount) }}</span>
                </div>
                <v-divider class="my-2" />
                <div class="total-row total">
                  <span>Total</span>
                  <span>{{ formatCurrency(cartStore.total) }}</span>
                </div>
              </div>

              <v-btn
                  color="primary"
                  size="large"
                  block
                  class="mt-4"
                  @click="finalizarCompra"
                  :loading="loading"
              >
                <v-icon start>mdi-lock</v-icon>
                Finalizar Pedido
              </v-btn>

              <div class="security-badges">
                <v-icon size="20" color="success">mdi-shield-check</v-icon>
                <span>Pagamento 100% Seguro</span>
              </div>
            </v-card-text>
          </v-card>
        </div>
      </div>
    </v-container>

    <!-- Snackbar -->
    <v-snackbar v-model="snackbar" :color="snackbarColor" :timeout="3000" location="top">
      {{ snackbarMessage }}
      <template v-slot:actions>
        <v-btn variant="text" @click="snackbar = false">Fechar</v-btn>
      </template>
    </v-snackbar>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '../stores/cart'

const router = useRouter()
const cartStore = useCartStore()

const loading = ref(false)
const loadingCEP = ref(false)
const formRef = ref(null)
const snackbar = ref(false)
const snackbarMessage = ref('')
const snackbarColor = ref('success')

const formData = reactive({
  nome: '',
  email: '',
  telefone: '',
  cpf: '',
  cep: '',
  rua: '',
  numero: '',
  complemento: '',
  bairro: '',
  cidade: '',
  estado: '',
  pagamento: 'pix'
})

const rules = {
  required: value => !!value || 'Campo obrigatório',
  email: value => {
    const pattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    return pattern.test(value) || 'E-mail inválido'
  }
}

const formatCurrency = (value) => {
  if (!value) return 'R$ 0,00'
  return value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
}

const formatTelefone = (event) => {
  let value = event.target.value.replace(/\D/g, '')
  if (value.length > 11) value = value.slice(0, 11)

  if (value.length <= 10) {
    value = value.replace(/(\d{2})(\d{0,4})(\d{0,4})/, '($1) $2-$3')
  } else {
    value = value.replace(/(\d{2})(\d{5})(\d{0,4})/, '($1) $2-$3')
  }

  formData.telefone = value
}

const formatCPF = (event) => {
  let value = event.target.value.replace(/\D/g, '')
  if (value.length > 11) value = value.slice(0, 11)

  value = value.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4')
  formData.cpf = value
}

const formatCEP = (event) => {
  let value = event.target.value.replace(/\D/g, '')
  if (value.length > 8) value = value.slice(0, 8)

  value = value.replace(/(\d{5})(\d{0,3})/, '$1-$2')
  formData.cep = value
}

/**
 * Busca dados do CEP na API ViaCEP
 * @see
 */
const buscarCEP = async () => {
  const cepLimpo = formData.cep.replace(/\D/g, '')

  if (cepLimpo.length === 8) {
    loadingCEP.value = true
    try {
      const response = await fetch(`https://viacep.com.br/ws/${cepLimpo}/json/`)
      const data = await response.json()

      if (data && !data.erro) {
        formData.rua = data.logradouro || ''
        formData.bairro = data.bairro || ''
        formData.cidade = data.localidade || ''
        formData.estado = data.uf || ''

        snackbarMessage.value = 'CEP encontrado!'
        snackbarColor.value = 'success'
        snackbar.value = true
      } else {
        snackbarMessage.value = 'CEP não encontrado'
        snackbarColor.value = 'error'
        snackbar.value = true
      }
    } catch (error) {
      console.error('Erro ao buscar CEP:', error)
      snackbarMessage.value = 'Erro ao buscar CEP'
      snackbarColor.value = 'error'
      snackbar.value = true
    } finally {
      loadingCEP.value = false
    }
  }
}

const finalizarCompra = async () => {
  if (!formRef.value) return

  const { valid } = await formRef.value.validate()

  if (!valid) {
    snackbarMessage.value = 'Por favor, preencha todos os campos obrigatórios'
    snackbarColor.value = 'error'
    snackbar.value = true
    return
  }

  loading.value = true

  try {
    await new Promise(resolve => setTimeout(resolve, 2000))

    snackbarMessage.value = 'Pedido realizado com sucesso!'
    snackbarColor.value = 'success'
    snackbar.value = true

    setTimeout(() => {
      cartStore.clearCart()
      router.push('/produtos')
    }, 1500)
  } catch (error) {
    console.error('Erro ao finalizar compra:', error)
    snackbarMessage.value = 'Erro ao processar pedido. Tente novamente.'
    snackbarColor.value = 'error'
    snackbar.value = true
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  if (cartStore.totalItems === 0) {
    router.push('/produtos')
  }
})
</script>

<style scoped>
.checkout-page {
  min-height: 100vh;
  background-color: #F5F7FA;
  padding: 24px 0;
}

.checkout-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
}

.checkout-header h1 {
  font-size: 32px;
  font-weight: 700;
  color: #1A2332;
}

.checkout-content {
  display: grid;
  grid-template-columns: 1fr 400px;
  gap: 24px;
}

@media (max-width: 960px) {
  .checkout-content {
    grid-template-columns: 1fr;
  }

  .checkout-sidebar {
    order: -1;
  }
}

.checkout-card {
  border-radius: 16px !important;
}

.payment-option {
  display: flex;
  align-items: center;
  padding: 8px 0;
}

.summary-card {
  border-radius: 16px !important;
}

.sticky {
  position: sticky;
  top: 24px;
}

.summary-items {
  display: flex;
  flex-direction: column;
  gap: 16px;
  max-height: 300px;
  overflow-y: auto;
}

.summary-items::-webkit-scrollbar {
  width: 6px;
}

.summary-items::-webkit-scrollbar-track {
  background: #F5F7FA;
}

.summary-items::-webkit-scrollbar-thumb {
  background: #9AA5B5;
  border-radius: 3px;
}

.summary-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
}

.item-info {
  display: flex;
  align-items: center;
  gap: 12px;
  flex: 1;
}

.item-thumbnail {
  width: 50px;
  height: 50px;
  object-fit: cover;
  border-radius: 8px;
  background: #F5F7FA;
}

.item-name {
  font-size: 14px;
  font-weight: 600;
  color: #1A2332;
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.item-quantity {
  font-size: 12px;
  color: #5E6D82;
  margin: 0;
}

.item-price {
  font-size: 14px;
  font-weight: 600;
  color: #1A2332;
  margin: 0;
}

.summary-totals {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.total-row {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  color: #5E6D82;
}

.total-row.discount {
  color: #4CAF50;
  font-weight: 600;
}

.total-row.total {
  font-size: 20px;
  font-weight: 700;
  color: #1A2332;
  margin-top: 8px;
}

.security-badges {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #E3E8EF;
  font-size: 13px;
  color: #5E6D82;
}
</style>
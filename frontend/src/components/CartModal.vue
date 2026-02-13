<template>
  <v-navigation-drawer
      :model-value="modelValue"
      @update:model-value="$emit('update:modelValue', $event)"
      location="right"
      temporary
      width="450"
      class="sidebar-cart"
      elevation="20"
  >
    <div class="cart-wrapper">
      <header class="cart-header">
        <div class="header-content">
          <div class="brand-line"></div>
          <div class="title-group">
            <h2>Minha Sacola</h2>
            <span class="count-badge">{{ items.length }} {{ items.length === 1 ? 'item' : 'itens' }}</span>
          </div>
        </div>
        <v-btn
            icon="mdi-close"
            variant="text"
            color="#64748B"
            class="close-drawer-btn"
            @click="$emit('update:modelValue', false)"
        />
      </header>

      <main class="cart-main-content">
        <div v-if="items.length === 0" class="empty-state">
          <div class="empty-icon-wrapper">
            <v-icon size="80" color="#F1F5F9">mdi-shopping-outline</v-icon>
          </div>
          <h3>Sua sacola está vazia</h3>
          <p>Que tal explorar nossas novidades e encontrar algo especial?</p>
          <v-btn
              color="#1E293B"
              block
              rounded="xl"
              height="50"
              variant="flat"
              class="mt-6"
              @click="$emit('update:modelValue', false)"
          >
            Começar a Comprar
          </v-btn>
        </div>

        <div v-else class="product-list">
          <div v-for="item in items" :key="item.id" class="product-item">
            <div class="product-thumb">
              <img :src="item.imagem || '/placeholder.png'" :alt="item.nome" />
            </div>

            <div class="product-info">
              <div class="info-top">
                <span class="p-category">{{ item.categoria }}</span>
                <h4 class="p-name">{{ item.nome }}</h4>
              </div>

              <div class="info-bottom">
                <div class="qty-selector">
                  <button @click="$emit('updateQuantity', item.id, -1)" :disabled="item.quantity <= 1">
                    <v-icon size="14">mdi-minus</v-icon>
                  </button>
                  <span class="qty-num">{{ item.quantity }}</span>
                  <button @click="$emit('updateQuantity', item.id, 1)" :disabled="item.quantity >= item.estoque">
                    <v-icon size="14">mdi-plus</v-icon>
                  </button>
                </div>
                <div class="p-pricing">
                  <span class="total-p">{{ formatCurrency(item.preco * item.quantity) }}</span>
                </div>
              </div>

              <v-btn
                  icon="mdi-trash-can-outline"
                  variant="text"
                  color="#EF4444"
                  size="x-small"
                  class="remove-p-btn"
                  @click="$emit('removeItem', item.id)"
              />
            </div>
          </div>
        </div>
      </main>

      <footer v-if="items.length > 0" class="cart-footer">
        <div class="shipping-section">
          <div class="section-label">
            <v-icon size="16" class="mr-1">mdi-truck-outline</v-icon>
            Cálculo de Entrega
          </div>
          <div class="shipping-box">
            <input
                type="text"
                v-model="zipCode"
                placeholder="00000-000"
                maxlength="9"
                @input="handleZipInput"
            />
            <button
                @click="calculateShipping"
                :disabled="zipCode.length < 8 || loadingShipping"
            >
              <v-progress-circular v-if="loadingShipping" indeterminate size="16" width="2" color="white" />
              <span v-else>Calcular</span>
            </button>
          </div>
          <p v-if="shippingMessage" class="ship-msg">{{ shippingMessage }}</p>
        </div>

        <div class="free-shipping-progress">
          <div class="progress-info">
            <span v-if="subtotal < 199">Faltam <strong>{{ formatCurrency(199 - subtotal) }}</strong> para frete grátis</span>
            <span v-else class="success-text">Você ganhou frete grátis!</span>
          </div>
          <div class="progress-track">
            <div class="progress-bar" :style="{ width: freeShippingProgress + '%' }"></div>
          </div>
        </div>

        <div class="summary-details">
          <div class="summary-line">
            <span>Subtotal</span>
            <span>{{ formatCurrency(subtotal) }}</span>
          </div>
          <div class="summary-line">
            <span>Frete</span>
            <span :class="{'free-text': subtotal >= 199}">
              {{ subtotal >= 199 ? 'Grátis' : (shippingCalculated ? formatCurrency(shippingCost) : '--') }}
            </span>
          </div>
          <v-divider class="my-3" />
          <div class="summary-line total-line">
            <span>Total Estimado</span>
            <span class="final-price">{{ formatCurrency(total) }}</span>
          </div>
        </div>

        <v-btn
            block
            color="#0061FF"
            height="60"
            rounded="xl"
            elevation="8"
            class="checkout-btn"
            @click="$emit('checkout')"
        >
          Finalizar Pedido
          <v-icon end>mdi-arrow-right</v-icon>
        </v-btn>

        <p class="security-note">
          <v-icon size="12">mdi-shield-lock</v-icon>
          Pagamento 100% seguro e criptografado
        </p>
      </footer>
    </div>
  </v-navigation-drawer>
</template>

<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
  modelValue: { type: Boolean, default: false },
  items: { type: Array, default: () => [] }
})
defineEmits(['update:modelValue', 'updateQuantity', 'removeItem', 'checkout']);

const zipCode = ref('')
const shippingCost = ref(0)
const loadingShipping = ref(false)
const shippingCalculated = ref(false)
const shippingMessage = ref('')

const subtotal = computed(() => {
  return props.items.reduce((sum, item) => sum + (item.preco * item.quantity), 0)
})

const freeShippingProgress = computed(() => {
  return Math.min((subtotal.value / 199) * 100, 100)
})

const total = computed(() => {
  const shipping = subtotal.value >= 199 ? 0 : shippingCost.value
  return subtotal.value + shipping
})

const formatCurrency = (value) => {
  return value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
}

const handleZipInput = (e) => {
  let val = e.target.value.replace(/\D/g, '')
  if (val.length > 5) {
    val = val.substring(0, 5) + '-' + val.substring(5, 8)
  }
  zipCode.value = val
}

const calculateShipping = () => {
  if (zipCode.value.length < 9) return
  loadingShipping.value = true

  setTimeout(() => {
    loadingShipping.value = false
    shippingCalculated.value = true
    shippingCost.value = zipCode.value.startsWith('0') ? 45.00 : 25.00
    shippingMessage.value = `Entrega em até ${zipCode.value.startsWith('0') ? '2' : '4'} dias úteis.`
  }, 1000)
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap');

.sidebar-cart {
  font-family: 'Plus Jakarta Sans', sans-serif !important;
  z-index: 2000 !important;
}

.cart-wrapper {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: white;
}

.cart-header {
  padding: 32px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #F1F5F9;
}

.brand-line {
  width: 3px;
  height: 24px;
  background: #0061FF;
  border-radius: 4px;
  margin-right: 12px;
}

.header-content {
  display: flex;
  align-items: center;
}

.title-group h2 {
  font-size: 20px;
  font-weight: 800;
  color: #0F172A;
  margin: 0;
  line-height: 1.2;
}

.count-badge {
  font-size: 12px;
  font-weight: 600;
  color: #64748B;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.cart-main-content {
  flex-grow: 1;
  overflow-y: auto;
  padding: 24px;
}

.product-item {
  display: flex;
  gap: 16px;
  padding-bottom: 24px;
  margin-bottom: 24px;
  border-bottom: 1px solid #F8FAFC;
  position: relative;
}

.product-thumb {
  width: 90px;
  height: 110px;
  background: #F8FAFC;
  border-radius: 16px;
  overflow: hidden;
  flex-shrink: 0;
}

.product-thumb img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.product-info {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.p-category {
  font-size: 10px;
  font-weight: 700;
  color: #0061FF;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.p-name {
  font-size: 15px;
  font-weight: 700;
  color: #1E293B;
  margin: 2px 0;
  line-height: 1.4;
  padding-right: 24px;
}

.info-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.qty-selector {
  display: flex;
  align-items: center;
  background: #F1F5F9;
  padding: 4px;
  border-radius: 10px;
  gap: 12px;
}

.qty-selector button {
  width: 24px;
  height: 24px;
  background: white;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #1E293B;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

.qty-num {
  font-size: 13px;
  font-weight: 700;
  color: #1E293B;
}

.total-p {
  font-weight: 800;
  color: #0F172A;
  font-size: 16px;
}

.remove-p-btn {
  position: absolute;
  top: 0;
  right: -8px;
}

/* FOOTER SECTION */
.cart-footer {
  padding: 24px;
  background: #F8FAFC;
  border-top: 1px solid #F1F5F9;
}

.section-label {
  font-size: 11px;
  font-weight: 800;
  color: #64748B;
  text-transform: uppercase;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
}

.shipping-box {
  display: flex;
  background: white;
  border: 1.5px solid #E2E8F0;
  border-radius: 12px;
  padding: 4px;
  margin-bottom: 8px;
}

.shipping-box input {
  flex-grow: 1;
  border: none;
  outline: none;
  padding: 0 12px;
  font-size: 13px;
  font-weight: 600;
  color: #1E293B;
}

.shipping-box button {
  background: #0F172A;
  color: white;
  padding: 8px 16px;
  border-radius: 10px;
  font-size: 12px;
  font-weight: 700;
  transition: all 0.2s;
}

.shipping-box button:disabled {
  opacity: 0.6;
}

.ship-msg {
  font-size: 11px;
  color: #0061FF;
  font-weight: 600;
  margin-bottom: 16px;
}

.free-shipping-progress {
  margin-bottom: 24px;
}

.progress-info {
  font-size: 12px;
  color: #475569;
  margin-bottom: 8px;
}

.success-text {
  color: #059669;
  font-weight: 700;
}

.progress-track {
  height: 6px;
  background: #E2E8F0;
  border-radius: 10px;
  overflow: hidden;
}

.progress-bar {
  height: 100%;
  background: #0061FF;
  transition: width 0.6s cubic-bezier(0.4, 0, 0.2, 1);
}

.summary-details {
  margin-bottom: 24px;
}

.summary-line {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  font-weight: 600;
  color: #64748B;
  margin-bottom: 8px;
}

.free-text {
  color: #059669;
}

.total-line {
  color: #0F172A;
  font-size: 16px;
}

.final-price {
  font-size: 24px;
  font-weight: 800;
  color: #0061FF;
}

.checkout-btn {
  text-transform: none !important;
  font-weight: 800 !important;
  font-size: 16px !important;
}

.security-note {
  font-size: 10px;
  color: #94A3B8;
  text-align: center;
  margin-top: 16px;
  font-weight: 600;
}

/* EMPTY STATE STYLE */
.empty-state {
  text-align: center;
  padding: 60px 0;
}

.empty-icon-wrapper {
  margin-bottom: 20px;
}

.empty-state h3 {
  font-size: 18px;
  font-weight: 800;
  color: #1E293B;
}

.empty-state p {
  font-size: 14px;
  color: #64748B;
  margin-top: 8px;
}
</style>
<template>
  <v-dialog :model-value="modelValue" @update:model-value="$emit('update:modelValue', $event)" max-width="900" persistent>
    <v-card class="cart-modal">
      <!-- Header -->
      <div class="cart-header">
        <div class="header-content">
          <v-icon size="28" color="primary">mdi-cart</v-icon>
          <div class="header-text">
            <h2>Seu Carrinho</h2>
            <p>{{ items.length }} {{ items.length === 1 ? 'item' : 'itens' }}</p>
          </div>
        </div>
        <v-btn icon variant="text" @click="$emit('update:modelValue', false)">
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </div>

      <v-divider />

      <!-- Empty State -->
      <div v-if="items.length === 0" class="empty-cart">
        <div class="empty-icon">
          <v-icon size="80" color="grey-lighten-1">mdi-cart-outline</v-icon>
        </div>
        <h3>Seu carrinho está vazio</h3>
        <p>Adicione produtos para começar suas compras</p>
        <v-btn color="primary" size="large" @click="$emit('update:modelValue', false)">
          Continuar Comprando
        </v-btn>
      </div>

      <!-- Cart Items -->
      <div v-else class="cart-content">
        <div class="cart-items">
          <div v-for="item in items" :key="item.id" class="cart-item">
            <div class="item-image">
              <img :src="item.imagem || '/placeholder.png'" :alt="item.nome" />
              <v-chip size="x-small" class="item-badge" color="primary">
                {{ item.quantity }}
              </v-chip>
            </div>

            <div class="item-details">
              <h4 class="item-name">{{ item.nome }}</h4>
              <p class="item-category">{{ item.categoria }}</p>
              <p class="item-price">{{ formatCurrency(item.preco) }} cada</p>
            </div>

            <div class="item-actions">
              <div class="quantity-control">
                <v-btn
                    icon
                    size="small"
                    variant="outlined"
                    @click="$emit('updateQuantity', item.id, -1)"
                    :disabled="item.quantity <= 1"
                >
                  <v-icon size="16">mdi-minus</v-icon>
                </v-btn>
                <span class="quantity">{{ item.quantity }}</span>
                <v-btn
                    icon
                    size="small"
                    variant="outlined"
                    @click="$emit('updateQuantity', item.id, 1)"
                    :disabled="item.quantity >= item.estoque"
                >
                  <v-icon size="16">mdi-plus</v-icon>
                </v-btn>
              </div>

              <div class="item-total">
                <p class="total-label">Total</p>
                <p class="total-value">{{ formatCurrency(item.preco * item.quantity) }}</p>
              </div>

              <v-btn
                  icon
                  variant="text"
                  color="error"
                  size="small"
                  @click="$emit('removeItem', item.id)"
              >
                <v-icon>mdi-delete-outline</v-icon>
              </v-btn>
            </div>
          </div>
        </div>

        <!-- Summary Sidebar -->
        <div class="cart-summary">
          <h3>Resumo do Pedido</h3>

          <div class="summary-section">
            <div class="summary-row">
              <span>Subtotal ({{ totalItems }} {{ totalItems === 1 ? 'item' : 'itens' }})</span>
              <span class="value">{{ formatCurrency(subtotal) }}</span>
            </div>
            <div class="summary-row">
              <span>Desconto</span>
              <span class="value success">- {{ formatCurrency(discount) }}</span>
            </div>
            <div class="summary-row">
              <span>Frete</span>
              <span class="value">{{ shippingCost > 0 ? formatCurrency(shippingCost) : 'Grátis' }}</span>
            </div>
          </div>

          <v-divider class="my-4" />

          <div class="summary-total">
            <span>Total</span>
            <span class="total-amount">{{ formatCurrency(total) }}</span>
          </div>

          <!-- Coupon -->
          <div class="coupon-section">
            <v-text-field
                v-model="couponCode"
                placeholder="Código de desconto"
                variant="outlined"
                density="compact"
                hide-details
                append-inner-icon="mdi-tag-outline"
            >
              <template v-slot:append>
                <v-btn
                    size="small"
                    color="primary"
                    variant="text"
                    @click="applyCoupon"
                    :disabled="!couponCode"
                >
                  Aplicar
                </v-btn>
              </template>
            </v-text-field>
          </div>

          <!-- Free Shipping Progress -->
          <div v-if="freeShippingProgress < 100" class="free-shipping">
            <div class="shipping-header">
              <v-icon size="20" color="primary">mdi-truck-fast</v-icon>
              <span>Frete grátis em compras acima de R$ 199</span>
            </div>
            <v-progress-linear
                :model-value="freeShippingProgress"
                color="primary"
                height="8"
                rounded
                class="mt-2"
            />
            <p class="shipping-text">
              Faltam {{ formatCurrency(199 - subtotal) }} para ganhar frete grátis!
            </p>
          </div>

          <div v-else class="free-shipping-achieved">
            <v-icon color="success">mdi-check-circle</v-icon>
            <span>Você ganhou frete grátis!</span>
          </div>

          <!-- Action Buttons -->
          <v-btn
              color="primary"
              size="large"
              block
              class="checkout-btn"
              @click="$emit('checkout')"
          >
            <v-icon start>mdi-lock</v-icon>
            Finalizar Compra
          </v-btn>

          <v-btn
              variant="outlined"
              size="large"
              block
              class="mt-3"
              @click="$emit('update:modelValue', false)"
          >
            Continuar Comprando
          </v-btn>

          <!-- Trust Badges -->
          <div class="trust-badges">
            <div class="badge">
              <v-icon size="20" color="success">mdi-shield-check</v-icon>
              <span>Compra Segura</span>
            </div>
            <div class="badge">
              <v-icon size="20" color="primary">mdi-truck</v-icon>
              <span>Entrega Rápida</span>
            </div>
            <div class="badge">
              <v-icon size="20" color="warning">mdi-arrow-u-left-top</v-icon>
              <span>Devolução Fácil</span>
            </div>
          </div>
        </div>
      </div>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
  modelValue: { type: Boolean, default: false },
  items: { type: Array, default: () => [] }
})
defineEmits(['update:modelValue', 'updateQuantity', 'removeItem', 'checkout']);
const couponCode = ref('')
const discount = ref(0)
const shippingCost = ref(0)
const totalItems = computed(() => {
  return props.items.reduce((sum, item) => sum + item.quantity, 0)
})

const subtotal = computed(() => {
  return props.items.reduce((sum, item) => sum + (item.preco * item.quantity), 0)
})

const freeShippingProgress = computed(() => {
  return Math.min((subtotal.value / 199) * 100, 100)
})

const total = computed(() => {
  const shipping = subtotal.value >= 199 ? 0 : shippingCost.value
  return subtotal.value - discount.value + shipping
})

const formatCurrency = (value) => {
  return value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
}

const applyCoupon = () => {
  if (couponCode.value.toUpperCase() === 'DESCONTO10') {
    discount.value = subtotal.value * 0.1
    alert('Cupom aplicado! 10% de desconto')
  } else {
    alert('Cupom inválido')
  }
}
</script>

<style scoped>
.cart-modal {
  border-radius: 16px !important;
  max-height: 90vh;
  display: flex;
  flex-direction: column;
}

.cart-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 24px;
  background: linear-gradient(135deg, rgba(21, 101, 192, 0.05) 0%, rgba(21, 101, 192, 0.02) 100%);
}

.header-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-text h2 {
  font-size: 24px;
  font-weight: 700;
  color: #1A2332;
  margin: 0;
}

.header-text p {
  font-size: 14px;
  color: #5E6D82;
  margin: 0;
}

.empty-cart {
  padding: 80px 40px;
  text-align: center;
}

.empty-icon {
  margin-bottom: 24px;
}

.empty-cart h3 {
  font-size: 24px;
  font-weight: 600;
  color: #1A2332;
  margin-bottom: 12px;
}

.empty-cart p {
  font-size: 16px;
  color: #5E6D82;
  margin-bottom: 32px;
}

.cart-content {
  display: grid;
  grid-template-columns: 1fr 380px;
  gap: 24px;
  padding: 24px;
  max-height: calc(90vh - 200px);
  overflow: hidden;
}

@media (max-width: 768px) {
  .cart-content {
    grid-template-columns: 1fr;
  }
}

.cart-items {
  overflow-y: auto;
  padding-right: 12px;
}

.cart-item {
  display: flex;
  gap: 16px;
  padding: 20px;
  background: #F5F7FA;
  border-radius: 12px;
  margin-bottom: 16px;
  transition: all 0.2s ease;
}

.cart-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.item-image {
  position: relative;
  width: 100px;
  height: 100px;
  border-radius: 8px;
  overflow: hidden;
  background: #FFFFFF;
  flex-shrink: 0;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-badge {
  position: absolute;
  top: -8px;
  right: -8px;
  font-weight: 700;
}

.item-details {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.item-name {
  font-size: 16px;
  font-weight: 600;
  color: #1A2332;
  margin: 0;
}

.item-category {
  font-size: 13px;
  color: #5E6D82;
  margin: 0;
}

.item-price {
  font-size: 14px;
  font-weight: 500;
  color: #1565C0;
  margin: 0;
}

.item-actions {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 12px;
}

.quantity-control {
  display: flex;
  align-items: center;
  gap: 12px;
  background: #FFFFFF;
  padding: 4px;
  border-radius: 8px;
}

.quantity {
  font-size: 16px;
  font-weight: 600;
  min-width: 30px;
  text-align: center;
}

.item-total {
  text-align: right;
}

.total-label {
  font-size: 12px;
  color: #5E6D82;
  margin: 0;
}

.total-value {
  font-size: 18px;
  font-weight: 700;
  color: #1A2332;
  margin: 0;
}

.cart-summary {
  background: #F5F7FA;
  border-radius: 12px;
  padding: 24px;
  height: fit-content;
  position: sticky;
  top: 0;
}

.cart-summary h3 {
  font-size: 20px;
  font-weight: 700;
  color: #1A2332;
  margin: 0 0 20px 0;
}

.summary-section {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
}

.summary-row span:first-child {
  color: #5E6D82;
}

.summary-row .value {
  font-weight: 600;
  color: #1A2332;
}

.summary-row .value.success {
  color: #4CAF50;
}

.summary-total {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 0;
  font-size: 16px;
  font-weight: 600;
}

.total-amount {
  font-size: 28px;
  font-weight: 700;
  color: #1565C0;
}

.coupon-section {
  margin: 20px 0;
}

.free-shipping {
  background: rgba(21, 101, 192, 0.05);
  border: 1px solid rgba(21, 101, 192, 0.2);
  border-radius: 8px;
  padding: 16px;
  margin: 20px 0;
}

.shipping-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  font-weight: 600;
  color: #1A2332;
  margin-bottom: 8px;
}

.shipping-text {
  font-size: 12px;
  color: #5E6D82;
  margin: 8px 0 0 0;
}

.free-shipping-achieved {
  display: flex;
  align-items: center;
  gap: 8px;
  background: rgba(76, 175, 80, 0.1);
  border: 1px solid rgba(76, 175, 80, 0.3);
  border-radius: 8px;
  padding: 12px;
  margin: 20px 0;
  font-size: 14px;
  font-weight: 600;
  color: #2E7D32;
}

.checkout-btn {
  margin-top: 20px;
  font-weight: 600;
  letter-spacing: 0.5px;
}

.trust-badges {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 8px;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #E3E8EF;
}

.badge {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  text-align: center;
}

.badge span {
  font-size: 11px;
  color: #5E6D82;
  font-weight: 500;
}

.cart-items::-webkit-scrollbar {
  width: 8px;
}

.cart-items::-webkit-scrollbar-track {
  background: #F5F7FA;
  border-radius: 4px;
}

.cart-items::-webkit-scrollbar-thumb {
  background: #9AA5B5;
  border-radius: 4px;
}

.cart-items::-webkit-scrollbar-thumb:hover {
  background: #5E6D82;
}
</style>
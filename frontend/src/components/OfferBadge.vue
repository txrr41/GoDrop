<template>
  <div v-if="offer" class="offer-badge-wrapper">
    <!-- Badge de Desconto -->
    <v-chip
        class="offer-badge"
        :class="badgeClass"
        size="small"
        variant="flat"
    >
      <v-icon start size="14">mdi-tag</v-icon>
      <span class="discount-text">
        {{ offer.type === 'PERCENTAGE'
          ? `-${offer.discountValue}%`
          : `-R$ ${offer.discountValue}`
        }}
      </span>
    </v-chip>

    <!-- Preço com Desconto -->
    <div v-if="showPrice" class="price-display">
      <span class="original-price">{{ formatCurrency(originalPrice) }}</span>
      <span class="final-price">{{ formatCurrency(finalPrice) }}</span>
      <span class="savings">
        Economize {{ formatCurrency(savings) }}
      </span>
    </div>

    <!-- Timer de Expiração -->
    <div v-if="showTimer && timeRemaining" class="offer-timer">
      <v-icon size="12">mdi-clock-outline</v-icon>
      <span>{{ timeRemaining }}</span>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'

const props = defineProps({
  offer: {
    type: Object,
    default: null
  },
  originalPrice: {
    type: Number,
    required: true
  },
  showPrice: {
    type: Boolean,
    default: false
  },
  showTimer: {
    type: Boolean,
    default: false
  },
  badgePosition: {
    type: String,
    default: 'top-left'
  }
})

const now = ref(new Date())
let timerInterval = null

const finalPrice = computed(() => {
  if (!props.offer) return props.originalPrice

  if (props.offer.type === 'PERCENTAGE') {
    const discount = props.originalPrice * (props.offer.discountValue / 100)
    return props.originalPrice - discount
  } else {
    return props.originalPrice - props.offer.discountValue
  }
})

const savings = computed(() => {
  return props.originalPrice - finalPrice.value
})

const badgeClass = computed(() => {
  return `position-${props.badgePosition}`
})

const timeRemaining = computed(() => {
  if (!props.offer || !props.offer.endDate) return null

  const end = new Date(props.offer.endDate)
  const diff = end - now.value

  if (diff <= 0) return 'Oferta expirada'

  const hours = Math.floor(diff / (1000 * 60 * 60))
  const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60))
  const seconds = Math.floor((diff % (1000 * 60)) / 1000)

  if (hours > 24) {
    const days = Math.floor(hours / 24)
    return `${days}d ${hours % 24}h restantes`
  } else if (hours > 0) {
    return `${hours}h ${minutes}m restantes`
  } else {
    return `${minutes}m ${seconds}s restantes`
  }
})

const formatCurrency = (value) => {
  return value.toLocaleString('pt-BR', {
    style: 'currency',
    currency: 'BRL'
  })
}

onMounted(() => {
  if (props.showTimer) {
    timerInterval = setInterval(() => {
      now.value = new Date()
    }, 1000)
  }
})

onUnmounted(() => {
  if (timerInterval) {
    clearInterval(timerInterval)
  }
})
</script>

<style scoped>
.offer-badge-wrapper {
  position: relative;
}

.offer-badge {
  background: linear-gradient(135deg, #7c3aed 0%, #a855f7 100%) !important;
  color: white !important;
  font-weight: 700;
  padding: 6px 12px;
  box-shadow: 0 2px 8px rgba(124, 58, 237, 0.3);
}

.offer-badge.position-top-left {
  position: absolute;
  top: 12px;
  left: 12px;
  z-index: 1;
}

.offer-badge.position-top-right {
  position: absolute;
  top: 12px;
  right: 12px;
  z-index: 1;
}

.discount-text {
  font-size: 13px;
  letter-spacing: 0.5px;
}

.price-display {
  display: flex;
  flex-direction: column;
  gap: 4px;
  margin-top: 8px;
}

.original-price {
  font-size: 14px;
  color: #94a3b8;
  text-decoration: line-through;
}

.final-price {
  font-size: 24px;
  font-weight: 800;
  color: #7c3aed;
}

.savings {
  font-size: 12px;
  color: #22c55e;
  font-weight: 600;
}

.offer-timer {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  background: #fef3c7;
  color: #92400e;
  padding: 4px 8px;
  border-radius: 6px;
  font-size: 11px;
  font-weight: 700;
  margin-top: 8px;
}
</style>
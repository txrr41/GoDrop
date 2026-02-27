<template>
  <div v-if="isDropper" class="dropper-badge" :class="'badge--' + level.toLowerCase()">
    <span class="badge-icon">{{ icon }}</span>
    <span class="badge-label">{{ label }}</span>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  isDropper: {
    type: Boolean,
    default: false
  },
  level: {
    type: String,
    default: 'BRONZE'
  }
})

const levelMap = {
  BRONZE:   { icon: '🥉', label: 'Bronze' },
  SILVER:   { icon: '🥈', label: 'Prata' },
  GOLD:     { icon: '🏅', label: 'Ouro' },
  PLATINUM: { icon: '🎖️', label: 'Platina' },
  DIAMOND:  { icon: '💎', label: 'Diamante' },
}

const icon  = computed(() => levelMap[props.level]?.icon  ?? '🥉')
const label = computed(() => levelMap[props.level]?.label ?? 'Bronze')
</script>

<style scoped>
.dropper-badge {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  padding: 4px 10px;
  border-radius: 99px;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.02em;
  cursor: default;
  transition: transform 0.15s;
}

.dropper-badge:hover { transform: scale(1.04); }

.badge--bronze {
  background: linear-gradient(135deg, #fef3c7, #fde68a);
  color: #92400e;
  border: 1px solid #fcd34d;
}

.badge--silver {
  background: linear-gradient(135deg, #f1f5f9, #e2e8f0);
  color: #374151;
  border: 1px solid #cbd5e1;
}

.badge--gold {
  background: linear-gradient(135deg, #fefce8, #fef08a);
  color: #854d0e;
  border: 1px solid #facc15;
  box-shadow: 0 0 8px rgba(250, 204, 21, 0.4);
}

.badge--platinum {
  background: linear-gradient(135deg, #e0f2fe, #bae6fd);
  color: #075985;
  border: 1px solid #7dd3fc;
}

.badge--diamond {
  background: linear-gradient(135deg, #f3e8ff, #e9d5ff);
  color: #6b21a8;
  border: 1px solid #c084fc;
  box-shadow: 0 0 10px rgba(192, 132, 252, 0.4);
}

.badge-icon { font-size: 14px; }
.badge-label { font-size: 11px; text-transform: uppercase; letter-spacing: 0.08em; }
</style>
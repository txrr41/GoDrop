<template>
  <v-dialog :model-value="modelValue" @update:model-value="$emit('update:modelValue', $event)" max-width="400">
    <v-card class="delete-modal">
      <v-card-text class="text-center">
        <div class="delete-icon">
          <v-icon size="48" color="error">mdi-alert-circle-outline</v-icon>
        </div>
        <h3 class="delete-title">Confirmar Exclusao</h3>
        <p class="delete-message">
          Tem certeza que deseja excluir o produto <strong>"{{ productName }}"</strong>? Esta acao nao pode ser desfeita.
        </p>
      </v-card-text>
      <v-card-actions class="modal-actions">
        <v-btn variant="outlined" @click="$emit('update:modelValue', false)">Cancelar</v-btn>
        <v-btn color="error" @click="confirmDelete">Excluir</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  name: 'DeleteDialog',
  props: {
    modelValue: { type: Boolean, default: false },
    productName: { type: String, default: '' }
  },
  emits: ['update:modelValue', 'confirm'],
  methods: {
    confirmDelete() {
      this.$emit('confirm')
      this.$emit('update:modelValue', false)
    }
  }
}
</script>

<style scoped>
.delete-modal {
  border-radius: 16px !important;
}

.delete-icon {
  margin-bottom: 16px;
}

.delete-title {
  font-size: 20px;
  font-weight: 600;
  color: #1A2332;
  margin: 0 0 12px;
}

.delete-message {
  font-size: 14px;
  color: #5E6D82;
  margin: 0;
  line-height: 1.6;
}

.modal-actions {
  padding: 16px 24px 24px;
  gap: 12px;
  justify-content: flex-end;
}
</style>

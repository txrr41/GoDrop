<template>
  <v-dialog :model-value="modelValue" @update:model-value="$emit('update:modelValue', $event)" max-width="400" persistent>
    <v-card class="category-modal">
      <v-card-text class="text-center">
        <div class="modal-icon">
          <v-icon size="28" color="primary">mdi-folder-plus</v-icon>
        </div>
        <h3 class="modal-heading">Nova Categoria</h3>
        <p class="modal-subheading">Adicione uma nova categoria para organizar seus produtos</p>

        <div class="form-group">
          <label class="form-label">Nome da Categoria</label>
          <v-text-field
              v-model="categoryName"
              placeholder="Ex: Eletronicos, Roupas, Acessorios..."
              variant="outlined"
              density="compact"
              hide-details
          />
        </div>
      </v-card-text>
      <v-card-actions class="modal-actions">
        <v-btn variant="outlined" @click="closeModal">Cancelar</v-btn>
        <v-btn color="primary" @click="saveCategory">Criar Categoria</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  name: 'CategoryModal',
  props: {
    modelValue: { type: Boolean, default: false }
  },
  emits: ['update:modelValue', 'save'],
  data() {
    return {
      categoryName: ''
    }
  },
  watch: {
    modelValue(val) {
      if (!val) {
        this.categoryName = ''
      }
    }
  },
  methods: {
    closeModal() {
      this.$emit('update:modelValue', false)
    },
    saveCategory() {
      if (this.categoryName.trim()) {
        this.$emit('save', this.categoryName.trim())
        this.closeModal()
      }
    }
  }
}
</script>

<style scoped>
.category-modal {
  border-radius: 16px !important;
}

.modal-icon {
  width: 48px;
  height: 48px;
  margin: 0 auto 16px;
  border-radius: 50%;
  background: rgba(21, 101, 192, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-heading {
  font-size: 20px;
  font-weight: 600;
  color: #1A2332;
  margin: 0 0 8px;
}

.modal-subheading {
  font-size: 14px;
  color: #5E6D82;
  margin: 0 0 24px;
}

.form-group {
  margin-bottom: 0;
  text-align: left;
}

.form-label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: #1A2332;
  margin-bottom: 8px;
}

.modal-actions {
  padding: 16px 24px 24px;
  gap: 12px;
  justify-content: flex-end;
}
</style>

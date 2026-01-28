<template>
  <v-dialog :model-value="modelValue" @update:model-value="$emit('update:modelValue', $event)" max-width="500" persistent>
    <v-card class="product-modal">
      <v-card-title class="modal-title">
        {{ editingProduct ? 'Editar Produto' : 'Novo Produto' }}
      </v-card-title>
      <v-card-text>
        <div class="form-group">
          <label class="form-label">Imagem do Produto</label>
          <div class="image-upload-area">
            <div v-if="formData.imagem" class="image-preview">
              <img :src="formData.imagem" alt="Preview" />
              <v-btn icon variant="flat" color="error" size="x-small" class="remove-image" @click="removeImage">
                <v-icon size="16">mdi-close</v-icon>
              </v-btn>
            </div>
            <label v-else class="upload-placeholder">
              <v-icon size="32" color="grey">mdi-image-plus</v-icon>
              <span>Upload</span>
              <input type="file" accept="image/*" hidden @change="handleImageUpload" />
            </label>
            <div class="upload-info">
              <p>Clique para fazer upload ou arraste uma imagem</p>
              <p class="upload-hint">PNG, JPG ou WEBP (max. 5MB)</p>
            </div>
          </div>
        </div>

        <div class="form-group">
          <label class="form-label">Nome do Produto</label>
          <v-text-field
              v-model="formData.name"
              placeholder="Ex: Notebook Pro 15"
              variant="outlined"
              density="compact"
              hide-details
          />
        </div>

        <div class="form-group">
          <label class="form-label">Descricao</label>
          <v-textarea
              v-model="formData.description"
              placeholder="Descreva o produto..."
              variant="outlined"
              density="compact"
              rows="3"
              hide-details
          />
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label">Preco (R$)</label>
            <v-text-field
                v-model.number="formData.price"
                type="number"
                step="0.01"
                min="0"
                placeholder="0,00"
                variant="outlined"
                density="compact"
                hide-details
            />
          </div>
          <div class="form-group">
            <label class="form-label">Estoque</label>
            <v-text-field
                v-model.number="formData.stock"
                type="number"
                min="0"
                placeholder="0"
                variant="outlined"
                density="compact"
                hide-details
            />
          </div>
        </div>

        <div class="form-group">
          <label class="form-label">Categoria</label>
          <v-select
              v-model="formData.category"
              :items="categories"
              item-title="name"
              item-value="name"
              placeholder="Selecione uma categoria"
              variant="outlined"
              density="compact"
              hide-details
          />
        </div>
      </v-card-text>
      <v-card-actions class="modal-actions">
        <v-btn variant="outlined" @click="closeModal">Cancelar</v-btn>
        <v-btn color="primary" @click="saveProduct">
          {{ editingProduct ? 'Salvar Alteracoes' : 'Adicionar Produto' }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>

export default {
  name: 'ProductModal',
  props: {
    modelValue: { type: Boolean, default: false },
    editingProduct: { type: Object, default: null },
    categories: { type: Array, default: () => [] }
  },
  emits: ['update:modelValue', 'save'],
  data() {
    return {
      formData: {
        name: '',
        description: '',
        price: 0,
        stock: 0,
        category: '',
        image: ''
      }
    }
  },
  watch: {
    editingProduct: {
      immediate: true,
      handler(product) {
        if (product) {
          this.formData = { ...product }
        } else {
          this.resetForm()
        }
      }
    },
    modelValue(val) {
      if (!val) {
        this.resetForm()
      }
    }
  },
  methods: {
    resetForm() {
      this.formData = { name: '', description: '', price: 0, stock: 0, category: '', image: '' }
    },

    closeModal() {
      this.$emit('update:modelValue', false)
    },

    saveProduct() {
      if (!this.formData.name || !this.formData.price || !this.formData.stock) {
        alert('Preencha todos os campos obrigatórios')
        return
      }

      const productData = {
        nome: this.formData.name,
        preco: parseFloat(this.formData.price),
        descricao: this.formData.description || '',
        estoque: parseInt(this.formData.stock),
        imagem: this.formData.image || '',
        categoria: this.formData.category || 'Uncategorized',
        ativo: true

      }

      this.$emit('save', productData)
      console.log(productData)
    },

    handleImageUpload(event) {
      const file = event.target.files[0]
      if (file) {
        const reader = new FileReader()
        reader.onloadend = () => {
          this.formData.image = reader.result
        }
        reader.readAsDataURL(file)
      }
    },
    removeImage() {
      this.formData.image = ''
    }
  }
}

</script>

<style scoped>
.product-modal {
  border-radius: 16px !important;
}

.modal-title {
  font-size: 20px;
  font-weight: 600;
  color: #1A2332;
  padding: 24px 24px 16px;
  border-bottom: 1px solid #E3E8EF;
}

.modal-actions {
  padding: 16px 24px 24px;
  gap: 12px;
  justify-content: flex-end;
}

.form-group {
  margin-bottom: 20px;
}

.form-label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: #1A2332;
  margin-bottom: 8px;
}

.form-row {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.image-upload-area {
  display: flex;
  align-items: flex-start;
  gap: 16px;
}

.image-preview {
  position: relative;
  width: 128px;
  height: 128px;
  border-radius: 10px;
  overflow: hidden;
  border: 1px solid #E3E8EF;
  background: #F5F7FA;
}

.image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.remove-image {
  position: absolute;
  top: 4px;
  right: 4px;
}

.upload-placeholder {
  width: 128px;
  height: 128px;
  border: 2px dashed #E3E8EF;
  border-radius: 10px;
  background: #F5F7FA;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  cursor: pointer;
  transition: border-color 0.2s ease, background-color 0.2s ease;
}

.upload-placeholder:hover {
  border-color: #1565C0;
  background: rgba(21, 101, 192, 0.1);
}

.upload-placeholder span {
  font-size: 12px;
  color: #9AA5B5;
}

.upload-info {
  flex: 1;
}

.upload-info p {
  font-size: 14px;
  color: #5E6D82;
  margin: 0;
}

.upload-hint {
  font-size: 12px !important;
  color: #9AA5B5 !important;
  margin-top: 4px !important;
}
</style>

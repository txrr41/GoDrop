<template>
  <div class="actions-bar">
    <div class="search-filter">
      <v-text-field
          :model-value="searchTerm"
          @update:model-value="$emit('update:searchTerm', $event)"
          placeholder="Buscar produtos..."
          prepend-inner-icon="mdi-magnify"
          variant="outlined"
          density="compact"
          hide-details
          class="search-input"
      />
      <v-select
          :model-value="selectedCategory"
          @update:model-value="$emit('update:selectedCategory', $event)"
          :items="categoryOptions"
          item-title="name"
          item-value="name"
          variant="outlined"
          density="compact"
          hide-details
          class="category-select"
      />
    </div>

    <div class="action-buttons">
      <div class="view-toggle">
        <v-btn
            :variant="viewMode === 'list' ? 'flat' : 'text'"
            :color="viewMode === 'list' ? 'primary' : 'default'"
            icon
            size="small"
            @click="$emit('update:viewMode', 'list')"
        >
          <v-icon>mdi-format-list-bulleted</v-icon>
        </v-btn>
        <v-btn
            :variant="viewMode === 'grid' ? 'flat' : 'text'"
            :color="viewMode === 'grid' ? 'primary' : 'default'"
            icon
            size="small"
            @click="$emit('update:viewMode', 'grid')"
        >
          <v-icon>mdi-view-grid</v-icon>
        </v-btn>
      </div>
      <v-btn variant="outlined" @click="$emit('openCategoryModal')">
        <v-icon start>mdi-folder-plus</v-icon>
        <span class="btn-text">Categoria</span>
      </v-btn>
      <v-btn color="primary" @click="$emit('openProductModal')">
        <v-icon start>mdi-plus</v-icon>
        <span class="btn-text">Novo Produto</span>
      </v-btn>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ActionsBar',
  props: {
    searchTerm: { type: String, default: '' },
    selectedCategory: { type: String, default: 'all' },
    viewMode: { type: String, default: 'list' },
    categoryOptions: { type: Array, default: () => [] }
  },
  emits: ['update:searchTerm', 'update:selectedCategory', 'update:viewMode', 'openCategoryModal', 'openProductModal']
}
</script>

<style scoped>
.actions-bar {
  margin-top: 32px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

@media (min-width: 768px) {
  .actions-bar {
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
  }
}

.search-filter {
  display: flex;
  flex-direction: column;
  gap: 12px;
  flex: 1;
}

@media (min-width: 640px) {
  .search-filter {
    flex-direction: row;
    align-items: center;
  }
}

.search-input {
  flex: 1;
  max-width: 400px;
}

.category-select {
  width: 100%;
}

@media (min-width: 640px) {
  .category-select {
    width: 200px;
  }
}

.action-buttons {
  display: flex;
  align-items: center;
  gap: 8px;
}

.view-toggle {
  display: flex;
  align-items: center;
  background: #FFFFFF;
  border: 1px solid #E3E8EF;
  border-radius: 10px;
  padding: 4px;
}

.btn-text {
  display: none;
}

@media (min-width: 640px) {
  .btn-text {
    display: inline;
  }
}
</style>

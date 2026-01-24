<template>
  <section class="py-12 py-md-16">
    <v-container>
      <div class="text-center mb-10">
        <h2 class="text-h4 text-md-h3 font-weight-bold mb-2">Categorias</h2>
        <p class="text-body-1 text-grey-darken-1">Explore nossos produtos por categoria</p>
      </div>

      <v-row>
        <v-col
            v-for="category in categories"
            :key="category.name"
            cols="6"
            sm="6"
            md="2"
        >
          <v-card
              class="category-card pa-10 ptext-center rounded-xl cursor-pointer"
              variant="flat"
              @click="selectCategory(category.name)"
          >
            <div class="category-icon-wrapper mx-auto mb-3">
              <v-icon :color="category.color" size="32">{{ category.icon }}</v-icon>
            </div>
            <h3 class="text-subtitle-2 font-weight-bold">{{ category.name }}</h3>
            <p class="text-caption text-grey">{{ category.count }} produtos</p>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </section>
</template>

<script setup lang="ts">
const categories = [
  { name: 'Eletrônicos', icon: 'mdi-cellphone', count: 234, color: 'primary' },
  { name: 'Moda', icon: 'mdi-tshirt-crew', count: 567, color: 'pink' },
  { name: 'Casa', icon: 'mdi-sofa', count: 189, color: 'orange' },
  { name: 'Esportes', icon: 'mdi-basketball', count: 145, color: 'green' },
  { name: 'Beleza', icon: 'mdi-lipstick', count: 312, color: 'purple' },
  { name: 'Games', icon: 'mdi-gamepad-variant', count: 98, color: 'red' }
]

const selectCategory = (name: string) => {
  console.log('Selected category:', name)
}
</script>

<style scoped>
.category-card {
  background-color: #f8f9fa;
  transition: all 0.3s ease;
}

.category-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.category-icon-wrapper {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  background-color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.cursor-pointer {
  cursor: pointer;
}
</style>

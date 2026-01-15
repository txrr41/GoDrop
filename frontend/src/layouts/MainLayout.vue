<template>
  <div class="layout">
    <!-- Menu lateral fixo -->
    <Menu />

    <!-- Conteúdo principal -->
    <div class="main-content">
      <!-- Header fixo -->
      <Header />

      <!-- Página atual -->
      <slot />
    </div>
  </div>
</template>

<script>
import Menu from '../components/Menu.vue'
import Header from '../components/Header.vue'

export default {
  components: { Menu, Header }
}
</script>

<style scoped>
.layout {
  display: flex;
  height: 100vh;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
}
</style>

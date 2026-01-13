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
  display: flex;       /* Menu + conteúdo lado a lado */
  height: 100vh;       /* Ocupa a tela inteira */
}

.main-content {
  flex: 1;             /* Ocupa todo o espaço restante */
  display: flex;
  flex-direction: column; /* Header no topo e página abaixo */
  overflow-y: auto;       /* Permite scroll se o conteúdo crescer */
}
</style>

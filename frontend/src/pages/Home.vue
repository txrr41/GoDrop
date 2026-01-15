<template>
  <div class="home">
    <h1>Bem-vindo a GoHome!</h1>
    <p>Esta é a tela inicial da aplicação.</p>
  </div>
</template>

<script>
export default {
  layout: () => import('../layouts/MainLayout.vue') // define que esta página usa o layout principal
}
</script>

<style scoped>
.home {
  padding: 20px;
}
</style>

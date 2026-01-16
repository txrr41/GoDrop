<template>
  <div class="layout">
    <Sidebar />

    <div class="main">
      <!-- HEADER SEM PADDING -->
      <Header />

      <!-- CONTEÚDO COM PADDING -->
      <main class="content">
        <RouterView />
      </main>
    </div>
  </div>
</template>

<script setup>
import Sidebar from '../components/Sidebar.vue'
import Header from '../components/Header.vue'
</script>

<style scoped>
.layout {
  display: flex;
  min-height: 100vh;
  background: #eeefef;
}

/* coluna direita */
.main {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
}

/* SOMENTE O CONTEÚDO TEM PADDING */
.content {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
}
</style>

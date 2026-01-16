<template>
  <div class="layout">
    <Sidebar />

    <div class="right">
      <Header />
      <main class="content">
        <router-view />
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
}

/* tudo que NÃO é sidebar */
.right {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
}

/* área do conteúdo */
.content {
  flex: 1;
  padding: 24px;
  background: #e5e5e5;
}
</style>

<template>
  <v-app>
    <Sidebar ref="sidebarRef" />

    <Header @toggle-sidebar="openSidebar" />

    <v-main>
      <RouterView />
    </v-main>
  </v-app>
</template>

<script setup>
import { ref } from 'vue'
import Header from '../components/Header.vue'
import Sidebar from '../components/Sidebar.vue'

const sidebarRef = ref(null)
sidebarRef.value = undefined;

function openSidebar() {
  sidebarRef.value.drawer = true
}
</script>

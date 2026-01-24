<template>
  <v-app>
    <Sidebar ref="sidebarRef" />

    <Header @toggle-sidebar="openSidebar" />

    <v-main>

      <HeroSection />
      <RouterView />
    </v-main>
  </v-app>
</template>

<script setup>
import { ref } from 'vue'
import Header from '../components/Header.vue'
import Sidebar from '../components/Sidebar.vue'
import HeroSection from "../components/HeroSection.vue";

const sidebarRef = ref(null)

function openSidebar() {
  sidebarRef.value.drawer = true
}
</script>

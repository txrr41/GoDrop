<template>
  <!-- Navigation Drawer / Sidebar -->
  <v-navigation-drawer
      v-model="drawer"
      temporary
      class="sidebar"
      width="280"
  >
    <div class="sidebar-header px-3 py-3">
      <div class="d-flex align-center justify-space-between">

          <img :src="LogoDrop" alt="" style="width: 120px" >

        <v-btn
            icon
            variant="text"
            size="small"
            @click="drawer = false"
        >
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </div>
    </div>

    <v-divider></v-divider>

    <div class="pa-4">
      <div class="section-label text-grey text-caption font-weight-medium mb-2">
        PRINCIPAL
      </div>

      <v-list density="compact" nav class="pa-0">
        <v-list-item
            v-for="item in principalItems"
            :key="item.title"
            :prepend-icon="item.icon"
            :title="item.title"
            :active="item.active"
            :class="{ 'active-item': item.active }"
            rounded="lg"
            class="mb-1"
            @click="selectItem(item)"
        ></v-list-item>
      </v-list>
    </div>

    <div class="pa-4 pt-0">
      <div class="section-label text-grey text-caption font-weight-medium mb-2">
        CONTA
      </div>

      <v-list density="compact" nav class="pa-0">
        <v-list-item
            v-for="item in contaItems"
            :key="item.title"
            :prepend-icon="item.icon"
            :title="item.title"
            :active="item.active"
            :class="{ 'active-item': item.active }"
            rounded="lg"
            class="mb-1"
            @click="selectItem(item)"
        ></v-list-item>
      </v-list>
    </div>

    <template v-slot:append>
      <div class="pa-4">
        <v-divider class="mb-4"></v-divider>
        <div class="d-flex align-center ga-3">
          <v-avatar color="cyan-lighten-4" size="40">
            <v-icon color="cyan">mdi-account</v-icon>
          </v-avatar>
          <div>
            <div class="text-body-2 font-weight-medium">Visitante</div>
            <div class="text-caption text-grey">Fazer login</div>
          </div>
        </div>
      </div>
    </template>
  </v-navigation-drawer>
</template>

<script setup>
import { ref, reactive } from 'vue'
import LogoDrop from '../assets/LogoDrop.png'

const drawer = ref(false)

const principalItems = reactive([
  { title: 'Home', icon: 'mdi-home', active: true },
  { title: 'Dashboard', icon: 'mdi-finance', active: false },
  { title: 'Produtos', icon: 'mdi-package', active: false },
  { title: 'Etiquetas', icon: 'mdi-tag', active: false },
  { title: 'Envios', icon: 'mdi-truck', active: false },
  { title: 'Usuários', icon: 'mdi-account', active: false},
])

const contaItems = reactive([
  { title: 'Ajuda', icon: 'mdi-help-circle', active: false },
  { title: 'Configurações', icon: 'mdi-cog', active: false },
])

const emit = defineEmits(['page-change'])

function selectItem(item) {

  principalItems.forEach(i => i.active = false)
  contaItems.forEach(i => i.active = false)

  item.active = true

  emit('page-change', item.title)

  drawer.value = false
}

defineExpose({
  drawer
})
</script>

<style scoped>


.sidebar
{
  border-radius: 0 16px 16px 0 !important;
}

.sidebar-header
{
  background: linear-gradient(135deg, #f8f9fa 0%, #ffffff 100%);
}

.section-label
{
  letter-spacing: 0.5px;
}

.active-item {
  background: linear-gradient(135deg, #00BCD4 0%, #0097b2 100%) !important;
  color: white !important;
}

.active-item :deep(.v-icon) {
  color: white !important;
}

.active-item :deep(.v-list-item-title) {
  color: white !important;
}

:deep(.v-list-item):not(.active-item):hover {
  background: #f5f5f5 !important;
}

:deep(.v-navigation-drawer) {
  box-shadow: 4px 0 24px rgba(0, 0, 0, 0.12) !important;
}
</style>
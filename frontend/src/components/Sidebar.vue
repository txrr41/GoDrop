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
import { useRouter } from 'vue-router'
import LogoDrop from '../assets/LogoDrop.png'

const drawer = ref(false)
const router = useRouter()

const principalItems = reactive([
  { title: 'Home', path: '/home', icon: 'mdi-home', active: true },
  { title: 'Dashboard', path: '/dashboard', icon: 'mdi-finance', active: false },
  { title: 'Produtos', path: '/produtos', icon: 'mdi-package', active: false },
  { title: 'Etiquetas', path: '/etiquetas', icon: 'mdi-tag', active: false },
  { title: 'Envios', path: '/envios', icon: 'mdi-truck', active: false },
  { title: 'Usuários', path: '/usuarios', icon: 'mdi-account', active: false},
])

const contaItems = reactive([
  { title: 'Ajuda', path: '/ajuda', icon: 'mdi-help-circle', active: false },
  { title: 'Configurações', path: '/configuracoes', icon: 'mdi-cog', active: false },
])

const emit = defineEmits(['page-change'])

function selectItem(item) {
  // Desativa todos os itens
  principalItems.forEach(i => i.active = false)
  contaItems.forEach(i => i.active = false)

  // Ativa o item selecionado
  item.active = true

  // Emite o evento
  emit('page-change', item.title)

  // Navega para a rota se existir
  if (item.path) {
    router.push(item.path)
  }

  // Fecha o drawer
  drawer.value = false
}

defineExpose({
  drawer
})
</script>
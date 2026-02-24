<template>
  <!-- Navigation Drawer / Sidebar -->
  <v-navigation-drawer
      v-model="drawer"
      temporary
      class="admin-sidebar"
      width="280"
      elevation="1"
  >
    <!-- Header -->
    <div class="sidebar-header px-4 py-4 d-flex align-center justify-space-between">
      <img :src="LogoDrop" alt="Logo" style="width: 120px; object-fit: contain;" >
      <v-btn
          icon
          variant="text"
          size="small"
          color="grey-darken-1"
          class="close-btn"
          @click="drawer = false"
      >
        <v-icon>mdi-close</v-icon>
      </v-btn>
    </div>

    <v-divider class="border-opacity-50 mx-4"></v-divider>

    <!-- Principal Section -->
    <div class="px-3 py-4">
      <div class="section-label mb-3">
        Principal
      </div>

      <v-list density="compact" nav class="pa-0">
        <v-list-item
            v-for="item in principalItems"
            :key="item.title"
            :title="item.title"
            :class="['nav-item', { 'active-item': item.active }]"
            rounded="md"
            class="mb-1"
            @click="selectItem(item)"
        >
          <template v-slot:prepend>
            <v-icon :color="item.active ? 'cyan-darken-2' : 'grey-darken-1'" size="small">
              {{ item.icon }}
            </v-icon>
          </template>
        </v-list-item>
      </v-list>
    </div>

    <v-divider class="border-opacity-50 mx-4"></v-divider>

    <!-- Conta Section -->
    <div class="px-3 py-4">
      <div class="section-label mb-3">
        Conta
      </div>

      <v-list density="compact" nav class="pa-0">
        <v-list-item
            v-for="item in contaItems"
            :key="item.title"
            :title="item.title"
            :class="['nav-item', { 'active-item': item.active }]"
            rounded="md"
            class="mb-1"
            @click="selectItem(item)"
        >
          <template v-slot:prepend>
            <v-icon :color="item.active ? 'cyan-darken-2' : 'grey-darken-1'" size="small">
              {{ item.icon }}
            </v-icon>
          </template>
        </v-list-item>
      </v-list>
    </div>

    <!-- Footer / User Info -->
    <template v-slot:append>
      <div class="pa-4 bg-footer">
        <div class="user-profile-card pa-3 d-flex align-center ga-3 rounded-lg">
          <v-avatar color="cyan-lighten-5" size="42" class="border-avatar">
            <v-icon color="cyan-darken-1">mdi-account-tie</v-icon>
          </v-avatar>
          <div class="user-info">
            <div class="text-body-2 font-weight-bold text-blue-grey-darken-4">Visitante</div>
            <div class="text-caption font-weight-medium text-cyan-darken-2 cursor-pointer login-text">
              Fazer login
            </div>
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
  { title: 'Dashboard', path: '/metricas', icon: 'mdi-finance', active: false },
  { title: 'Produtos', path: '/produtos', icon: 'mdi-package', active: false },
  { title: 'Etiquetas', path: '/etiquetas', icon: 'mdi-tag', active: false },
  { title: 'Pedidos', path: '/admin/pedidos', icon: 'mdi-truck', active: false },
  { title: 'Usuários', path: '/usuarios', icon: 'mdi-account', active: false},
  { title: 'Cadastar Oferta', path: '/oferta/cadastro', icon: 'mdi-sale', active: false},
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

<style scoped>
/* Estilo geral da sidebar para um tom mais limpo */
.admin-sidebar {
  background-color: #fafbfc !important;
  border-right: 1px solid #eceff1 !important;
}

.sidebar-header {
  background-color: transparent;
}

/* Labels de sessão padronizados (estilo uppercase espaçado) */
.section-label {
  font-size: 0.7rem;
  font-weight: 700;
  letter-spacing: 0.08em;
  color: #90a4ae;
  text-transform: uppercase;
  padding-left: 12px;
}

/* Itens de navegação */
.nav-item {
  transition: all 0.2s ease;
  color: #546e7a !important;
  font-weight: 500;
  font-size: 0.9rem;
  overflow: hidden;
}

.nav-item:hover {
  background-color: #f1f5f9 !important;
  color: #263238 !important;
}

/* Item Ativo - Design moderno com barra lateral e cor primária */
.active-item {
  background-color: #e0f7fa !important; /* Fundo cyan bem claro */
  color: #00838f !important; /* Texto cyan escuro corporativo */
  font-weight: 600;
  position: relative;
}

/* Barra indicadora esquerda no item ativo */
.active-item::before {
  content: '';
  position: absolute;
  left: 0;
  top: 10%;
  height: 80%;
  width: 4px;
  background-color: #00acc1;
  border-radius: 0 4px 4px 0;
}

/* Rodapé e Card de Usuário */
.bg-footer {
  background: linear-gradient(to top, #ffffff, #fafbfc);
  border-top: 1px solid #eceff1;
}

.user-profile-card {
  background-color: #ffffff;
  border: 1px solid #e2e8f0;
  box-shadow: 0 1px 3px rgba(0,0,0,0.02);
  transition: box-shadow 0.2s ease, transform 0.2s ease;
}

.user-profile-card:hover {
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
  transform: translateY(-1px);
}

.border-avatar {
  border: 1px solid #b2ebf2 !important;
}

.login-text {
  transition: color 0.2s;
}

.login-text:hover {
  color: #006064 !important;
  text-decoration: underline;
}

.close-btn {
  transition: background-color 0.2s, transform 0.2s;
}

.close-btn:hover {
  background-color: #f1f5f9;
  transform: scale(1.05);
}
</style>
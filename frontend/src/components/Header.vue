<template>
  <v-app>

    <v-app-bar elevation="1">
      <div class="d-flex align-center" style="margin: 20px" >
        <img :src="logo" alt="" style="width: 200px" >

        <v-app-bar-nav-icon style="position: relative; left: -70px;"></v-app-bar-nav-icon>
      </div>



      <v-spacer />

      <div v-if="!auth.isLogged">
        <v-btn variant="outlined" color="primary" @click="openLogin">
          Login
        </v-btn>

        <v-btn color="primary" class="ml-2" @click="openRegister">
          Cadastrar
        </v-btn>
      </div>

      <v-menu v-else>
        <template #activator="{ props }">
          <v-btn v-bind="props" variant="text">
            <v-avatar size="32" class="mr-2">
              <v-img
                  :src="auth.user?.avatarUrl || '/avatar-default.png'"
              />
            </v-avatar>
            {{ auth.user?.name }}
          </v-btn>
        </template>

        <v-list>
          <v-list-item to="/profile" title="Meu perfil" />
          <v-list-item to="/settings" title="Configurações" />

          <v-divider />

          <v-list-item
              title="Sair"
              @click="logout"
              class="text-red"
          />
        </v-list>
      </v-menu>
    </v-app-bar>

    <v-main>
      <RouterView />
    </v-main>

    <AuthModal
        v-if="showAuth"
        :start-sign-up="startSignUp"
        @close="closeAuth"
    />

  </v-app>
</template>
<script setup>
import { ref } from 'vue'
import { useAuthStore } from '../stores/auth'
import AuthModal from '../components/Auth.vue'
import logo from '../assets/logo.png'


const auth = useAuthStore()


const showAuth = ref(false)
const startSignUp = ref(false)


function openLogin() {
  startSignUp.value = false
  showAuth.value = true
}

function openRegister() {
  startSignUp.value = true
  showAuth.value = true
}

function closeAuth() {
  showAuth.value = false
}

function logout() {
  auth.logout()
}
</script>

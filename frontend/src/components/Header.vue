<template>
  <header class="header">

    <!-- ESQUERDA -->
    <div class="header-left"></div>

    <!-- DIREITA -->
    <div class="header-right">

      <!-- NÃO LOGADO -->
      <div v-if="!auth.isLogged" class="auth-actions">
        <button class="btn-outline" @click="openLogin">
          Login
        </button>

        <button class="btn-primary" @click="openRegister">
          Cadastrar
        </button>
      </div>

      <!-- LOGADO -->
      <div v-else class="user-area">
        <button class="user-button" @click="toggleMenu">
          <img
              class="avatar"
              :src="auth.user?.avatarUrl || '/avatar-default.png'"
              alt="Avatar"
          />
          <span class="name">{{ auth.user?.name }}</span>
        </button>

        <!-- DROPDOWN -->
        <div v-if="menuOpen" class="dropdown">
          <RouterLink to="/profile">Meu perfil</RouterLink>
          <RouterLink to="/settings">Configurações</RouterLink>

          <hr />

          <button class="logout" @click="logout">
            Sair
          </button>
        </div>
      </div>

    </div>

    <!-- MODAL AUTH -->
    <AuthModal
        v-if="showAuth"
        :start-sign-up="startSignUp"
        @close="closeAuth"
    />
  </header>
</template>

<script setup>
import { ref } from 'vue'
import { useAuthStore } from '../stores/auth'
import AuthModal from '../components/Auth.vue'

const auth = useAuthStore()

const menuOpen = ref(false)
const showAuth = ref(false)
const startSignUp = ref(false)

function toggleMenu() {
  menuOpen.value = !menuOpen.value
}

function logout() {
  auth.logout()
  menuOpen.value = false
}

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
</script>

<style scoped>
* {
  font-family: "Roboto";
}

.header {
  height: 64px;
  background: rgba(255, 255, 255, 0.49);
  border-bottom: 1px solid #e5e7eb;
  display: flex;
  align-items: center;
  padding: 0 24px;
  justify-content: space-between;
}

.header-left {
  display: flex;
  align-items: center;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

/* ===== NÃO LOGADO ===== */

.auth-actions {
  display: flex;
  gap: 12px;
}

.btn-outline {
  padding: 8px 14px;
  border: 1px solid #309cf4;
  border-radius: 6px;
  color: #309cf4;
  font-size: 14px;
  background: transparent;
  cursor: pointer;
}

.btn-outline:hover {
  background: rgba(48, 156, 244, 0.1);
}

.btn-primary {
  padding: 8px 14px;
  background: #309cf4;
  border-radius: 6px;
  color: white;
  font-size: 14px;
  border: none;
  cursor: pointer;
}

/* ===== LOGADO ===== */

.user-area {
  position: relative;
}

.user-button {
  display: flex;
  align-items: center;
  gap: 10px;
  background: transparent;
  border: none;
  cursor: pointer;
  padding: 6px 10px;
  border-radius: 8px;
}

.user-button:hover {
  background: #f3f4f6;
}

.avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
}

.name {
  font-size: 14px;
  font-weight: 500;
  color: #374151;
}

/* ===== DROPDOWN ===== */

.dropdown {
  position: absolute;
  right: 0;
  top: 48px;
  background: white;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  min-width: 180px;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.08);
  display: flex;
  flex-direction: column;
  padding: 8px;
  z-index: 50;
}

.dropdown a {
  padding: 8px 10px;
  font-size: 14px;
  color: #374151;
  text-decoration: none;
  border-radius: 6px;
}

.dropdown a:hover {
  background: #f3f4f6;
}

.dropdown hr {
  margin: 8px 0;
  border-top: 1px solid #e5e7eb;
}

.logout {
  padding: 8px 10px;
  background: none;
  border: none;
  text-align: left;
  font-size: 14px;
  color: #dc2626;
  cursor: pointer;
  border-radius: 6px;
}

.logout:hover {
  background: #fee2e2;
}
</style>
s
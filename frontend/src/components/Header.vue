<template>
  <v-app-bar elevation="1" fixed>
    <v-app-bar-nav-icon @click="emit('toggle-sidebar')" ></v-app-bar-nav-icon>
    <div class="d-flex align-center"  >
      <img class="ml-0" :src="LogoDrop" alt="" style="width: 150px" >
    </div>

    <v-spacer />

    <v-icon class="ma-1">mdi-magnify</v-icon>

    <!-- Cart Button with Badge -->
    <v-badge
        :content="cartStore.totalItems"
        :model-value="cartStore.totalItems > 0"
        color="error"
        overlap
        class="mr-4"
    >
      <v-btn icon variant="text" @click="showCart = true">
        <v-icon>mdi-cart</v-icon>
      </v-btn>
    </v-badge>

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
        <v-list-item to="/orders" title="Meus Pedidos" />

        <v-divider />

        <v-list-item
            title="Sair"
            @click="logout"
            class="text-red"
        />
      </v-list>
    </v-menu>
  </v-app-bar>

  <AuthModal
      v-if="showAuth"
      :start-sign-up="startSignUp"
      @close="closeAuth"
  />

  <CartModal
      v-model="showCart"
      :items="cartStore.cartItems"
      @updateQuantity="handleUpdateQuantity"
      @removeItem="handleRemoveItem"
      @checkout="handleCheckout"
  />
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { useCartStore } from '../stores/cart'
import AuthModal from '../components/Auth.vue'
import CartModal from '../components/CartModal.vue'
import LogoDrop from '../assets/LogoDrop.png'

const router = useRouter()
const auth = useAuthStore()
const cartStore = useCartStore()

const emit = defineEmits(['toggle-sidebar'])

const showAuth = ref(false)
const startSignUp = ref(false)
const showCart = ref(false)

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
  cartStore.clearCart()
}

function handleUpdateQuantity(productId, change) {
  try {
    cartStore.updateQuantity(productId, change)
  } catch (error) {
    alert(error.message)
  }
}

function handleRemoveItem(productId) {
  if (confirm('Deseja remover este item do carrinho?')) {
    cartStore.removeItem(productId)
  }
}

function handleCheckout() {
  if (!auth.isLogged) {
    showCart.value = false
    openLogin()
    return
  }

  showCart.value = false
  router.push('/checkout')
}

cartStore.loadCart()
</script>
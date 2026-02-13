<template>
  <v-app-bar
      flat
      class="custom-header"
      height="120"
  >
    <div class="w-100 h-100 d-flex flex-column">

      <div class="top-row d-flex align-center px-md-10 px-4 flex-grow-1">
        <v-btn
            icon
            variant="text"
            class="nav-toggle-btn mr-2"
            @click="emit('toggle-sidebar')"
        >
          <v-icon size="24">mdi-menu-variant</v-icon>
        </v-btn>

        <div class="logo-wrapper d-flex align-center cursor-pointer">
          <img :src="LogoDrop" alt="Logo" class="main-logo">
        </div>

        <v-spacer />

        <div class="search-container d-none d-md-flex align-center mr-6">
          <v-icon size="20" class="search-icon">mdi-magnify</v-icon>
          <input type="text" placeholder="O que você está procurando?" class="search-input">
        </div>

        <div class="actions-wrapper d-flex align-center">
          <v-btn icon variant="text" class="d-md-none">
            <v-icon size="22">mdi-magnify</v-icon>
          </v-btn>

          <v-badge
              :content="cartStore.totalItems"
              :model-value="cartStore.totalItems > 0"
              color="black"
              offset-x="4"
              offset-y="4"
              class="custom-cart-badge mr-2 mr-md-4"
          >
            <v-btn icon variant="text" class="cart-btn" @click="showCart = true">
              <v-icon size="24">mdi-shopping-outline</v-icon>
            </v-btn>
          </v-badge>

          <v-divider vertical inset class="mx-2 d-none d-sm-flex opacity-10" />

          <div v-if="!auth.isLogged" class="auth-actions d-flex align-center ml-2">
            <v-btn
                variant="text"
                class="login-btn text-none font-weight-medium d-none d-sm-flex"
                @click="openLogin"
            >
              Entrar
            </v-btn>

            <v-btn
                flat
                color="black"
                class="register-btn text-none font-weight-bold px-6 ml-2"
                rounded="lg"
                @click="openRegister"
            >
              Cadastrar
            </v-btn>
          </div>

          <v-menu v-else transition="slide-y-transition">
            <template #activator="{ props }">
              <v-btn v-bind="props" variant="text" class="user-profile-btn text-none ml-2" rounded="xl">
                <v-avatar size="32" class="mr-2">
                  <v-img :src="auth.user?.avatarUrl || '/avatar-default.png'" />
                </v-avatar>
                <span class="d-none d-sm-inline font-weight-bold">{{ auth.user?.name }}</span>
                <v-icon size="16" class="ml-1">mdi-chevron-down</v-icon>
              </v-btn>
            </template>
            <v-list class="custom-menu-list mt-2 pa-2" elevation="10" rounded="xl" min-width="200">
              <v-list-item to="/profile" prepend-icon="mdi-account-outline" title="Meu perfil" />
              <v-list-item to="/settings" prepend-icon="mdi-cog-outline" title="Configurações" />
              <v-list-item to="/orders" prepend-icon="mdi-package-variant-closed" title="Meus Pedidos" />
              <v-divider class="my-2 opacity-10" />
              <v-list-item title="Sair" prepend-icon="mdi-logout" @click="logout" class="text-red" />
            </v-list>
          </v-menu>
        </div>
      </div>

      <div class="categories-row d-none d-md-flex align-center justify-center">
        <nav class="nav-links d-flex align-center">
          <a href="../pages/ProductCatalog.vue" class="nav-link">Catalogo</a>
          <a href="#" class="nav-link">Masculino</a>
          <a href="#" class="nav-link">Feminino</a>
          <a href="#" class="nav-link">Acessórios</a>
          <a href="#" class="nav-link">Ofertas</a>
          <v-menu open-on-hover transition="slide-y-transition">
            <template #activator="{ props }">
              <a v-bind="props" class="nav-link more-link">
                Mais <v-icon size="14">mdi-chevron-down</v-icon>
              </a>
            </template>
            <v-list class="pa-2" rounded="lg">
              <v-list-item title="Eletrônicos" />
              <v-list-item title="Casa & Decoração" />
              <v-list-item title="Esportes" />
            </v-list>
          </v-menu>
        </nav>
      </div>
    </div>
  </v-app-bar>

  <AuthModal v-if="showAuth" :start-sign-up="startSignUp" @close="closeAuth" />
  <CartModal v-model="showCart" :items="cartStore.cartItems" @updateQuantity="handleUpdateQuantity" @removeItem="handleRemoveItem" @checkout="handleCheckout" />
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

function openLogin() { startSignUp.value = false; showAuth.value = true; }
function openRegister() { startSignUp.value = true; showAuth.value = true; }
function closeAuth() { showAuth.value = false; }
function logout() { auth.logout(); cartStore.clearCart(); }

function handleUpdateQuantity(productId, change) {
  try { cartStore.updateQuantity(productId, change); } catch (error) { alert(error.message); }
}
function handleRemoveItem(productId) {
  if (confirm('Deseja remover este item do carrinho?')) { cartStore.removeItem(productId); }
}
function handleCheckout() {
  if (!auth.isLogged) { showCart.value = false; openLogin(); return; }
  showCart.value = false; router.push('/checkout');
}
cartStore.loadCart()
</script>

<style scoped lang="scss">
.custom-header {
  background-color: #fff !important;
  border-bottom: 1px solid #f0f0f0 !important;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.top-row {
  max-width: 1600px;
  width: 100%;
  margin: 0 auto;
}

.main-logo {
  height: 42px;
  width: auto;
  max-width: 180px;
  transition: transform 0.3s ease;
  &:hover { transform: scale(1.03); }
}

.search-container {
  background: #f5f5f7;
  border-radius: 50px;
  padding: 8px 16px;
  width: 320px;
  transition: all 0.2s ease;
  &:focus-within {
    background: #fff;
    box-shadow: 0 0 0 2px #000;
  }
}

.search-icon { color: #888; margin-right: 8px; }
.search-input {
  border: none;
  background: transparent;
  outline: none;
  font-size: 0.85rem;
  width: 100%;
  color: #1d1d1f;
  &::placeholder { color: #86868b; }
}


.categories-row {
  height: 48px;
  border-top: 1px solid #f5f5f7;
  background-color: #fff;
}

.nav-links {
  gap: 32px;
}

.nav-link {
  text-decoration: none;
  color: #1d1d1f;
  font-size: 0.85rem;
  font-weight: 500;
  letter-spacing: 0.3px;
  text-transform: uppercase;
  padding: 12px 0;
  border-bottom: 2px solid transparent;
  transition: all 0.2s ease;

  &:hover {
    color: #000;
    border-bottom: 2px solid #000;
  }
}

.more-link {
  display: flex;
  align-center: center;
  cursor: pointer;
}

.register-btn {
  text-transform: none;
  letter-spacing: 0;
  height: 44px !important;
  font-size: 0.9rem;
}

.user-profile-btn {
  background: #f5f5f7 !important;
  &:hover { background: #ebebeb !important; }
}

:deep(.custom-cart-badge .v-badge__badge) {
  font-size: 9px;
  height: 16px;
  min-width: 16px;
}

.custom-menu-list {
  border: 1px solid #f0f0f0;
}

@media (max-width: 960px) {
  .custom-header {
    height: 72px !important;
  }
}
</style>
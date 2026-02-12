<template>
  <footer class="main-footer">
    <v-container>
      <v-row class="footer-content">
        <v-col cols="12" md="4">
          <div class="footer-brand">
            <div class="logo-wrapper mb-4">
              <img :src="LogoDrop" alt="Go Drop" style="width: 150px" />
            </div>
            <p class="text-body-2 text-grey-lighten-1 mb-4">
              Sua loja online completa com os melhores produtos e as melhores ofertas.
              Qualidade garantida e entrega rápida para todo o Brasil.
            </p>
            <div class="social-links">
              <v-btn
                  icon
                  variant="text"
                  size="small"
                  href="https://facebook.com"
                  target="_blank"
              >
                <v-icon>mdi-facebook</v-icon>
              </v-btn>
              <v-btn
                  icon
                  variant="text"
                  size="small"
                  href="https://instagram.com"
                  target="_blank"
              >
                <v-icon>mdi-instagram</v-icon>
              </v-btn>
              <v-btn
                  icon
                  variant="text"
                  size="small"
                  href="https://twitter.com"
                  target="_blank"
              >
                <v-icon>mdi-twitter</v-icon>
              </v-btn>
              <v-btn
                  icon
                  variant="text"
                  size="small"
                  href="https://linkedin.com"
                  target="_blank"
              >
                <v-icon>mdi-linkedin</v-icon>
              </v-btn>
            </div>
          </div>
        </v-col>

        <v-col cols="6" md="2">
          <h3 class="footer-heading">Navegação</h3>
          <ul class="footer-links">
            <li><a href="/home">Home</a></li>
            <li><a href="/produtos">Produtos</a></li>
            <li><a href="/orders">Meus Pedidos</a></li>
            <li><a href="/">Sobre Nós</a></li>
          </ul>
        </v-col>

        <v-col cols="6" md="2">
          <h3 class="footer-heading">Categorias</h3>
          <ul class="footer-links">
            <li><a @click="filterByCategory('Eletrônicos')">Eletrônicos</a></li>
            <li><a @click="filterByCategory('Moda')">Moda</a></li>
            <li><a @click="filterByCategory('Casa')">Casa</a></li>
            <li><a @click="filterByCategory('Esportes')">Esportes</a></li>
            <li><a @click="filterByCategory('Beleza')">Beleza</a></li>
          </ul>
        </v-col>

        <v-col cols="6" md="2">
          <h3 class="footer-heading">Ajuda</h3>
          <ul class="footer-links">
            <li><a href="/">FAQ</a></li>
            <li><a href="/">Políticas de Troca</a></li>
            <li><a href="/">Política de Privacidade</a></li>
            <li><a href="/">Termos de Uso</a></li>
            <li><a href="/">Rastreamento</a></li>
          </ul>
        </v-col>

        <v-col cols="6" md="2">
          <h3 class="footer-heading">Contato</h3>
          <ul class="footer-contact">
            <li>
              <v-icon size="18" class="mr-2">mdi-email-outline</v-icon>
              contato@godrop.com
            </li>
            <li>
              <v-icon size="18" class="mr-2">mdi-phone-outline</v-icon>
              (11) 9999-9999
            </li>
            <li>
              <v-icon size="18" class="mr-2">mdi-whatsapp</v-icon>
              (11) 98888-8888
            </li>
            <li>
              <v-icon size="18" class="mr-2">mdi-clock-outline</v-icon>
              Seg-Sex 9h-18h
            </li>
          </ul>
        </v-col>
      </v-row>

      <v-divider class="my-6 border-opacity-25"></v-divider>

      <div class="payment-section mb-6">
        <h4 class="text-subtitle-2 text-grey-lighten-1 mb-3">Formas de Pagamento</h4>
        <div class="payment-icons">
          <v-icon size="40" class="payment-icon">mdi-credit-card</v-icon>
          <v-icon size="40" class="payment-icon">mdi-credit-card-outline</v-icon>
          <v-icon size="40" class="payment-icon">mdi-bank</v-icon>
          <v-icon size="40" class="payment-icon">mdi-cash</v-icon>
          <v-icon size="40" class="payment-icon">mdi-qrcode</v-icon>
        </div>
      </div>

      <div class="footer-bottom">
        <p class="text-body-2 text-grey mb-0">
          © {{ currentYear }} Go Drop. Todos os direitos reservados.
        </p>
        <p class="text-caption text-grey mb-0">
          CNPJ: 00.000.000/0001-00 | Desenvolvido com ❤️
        </p>
      </div>
    </v-container>

    <v-btn
        v-show="showScrollTop"
        class="scroll-top-btn"
        icon
        color="primary"
        size="large"
        elevation="4"
        @click="scrollToTop"
    >
      <v-icon>mdi-chevron-up</v-icon>
    </v-btn>
  </footer>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import LogoDrop from '../assets/LogoDrop.png'

const router = useRouter()
const showScrollTop = ref(false)

const currentYear = computed(() => new Date().getFullYear())

const handleScroll = () => {
  showScrollTop.value = window.scrollY > 300
}

const scrollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  })
}

const filterByCategory = (category) => {
  router.push({
    path: '/produtos',
    query: { categoria: category }
  })
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
.main-footer {
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
  color: #fff;
  padding: 64px 0 24px;
  position: relative;
  margin-top: 80px;
}

.footer-content {
  margin-bottom: 32px;
}

.footer-brand {
  max-width: 300px;
}

.logo-wrapper {
  filter: brightness(0) invert(1);
}

.social-links {
  display: flex;
  gap: 8px;
}

.footer-heading {
  font-size: 16px;
  font-weight: 700;
  margin-bottom: 20px;
  color: #fff;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.footer-links {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-links li {
  margin-bottom: 12px;
}

.footer-links a {
  color: #b0b3b8;
  text-decoration: none;
  font-size: 14px;
  transition: color 0.3s ease;
  cursor: pointer;
}

.footer-links a:hover {
  color: #fff;
  padding-left: 4px;
}

.footer-contact {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-contact li {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  font-size: 14px;
  color: #b0b3b8;
}

.payment-section {
  text-align: center;
}

.payment-icons {
  display: flex;
  justify-content: center;
  gap: 16px;
  flex-wrap: wrap;
}

.payment-icon {
  color: #b0b3b8;
  opacity: 0.7;
  transition: opacity 0.3s ease;
}

.payment-icon:hover {
  opacity: 1;
}

.footer-bottom {
  text-align: center;
  padding-top: 24px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.scroll-top-btn {
  position: fixed;
  bottom: 32px;
  right: 32px;
  z-index: 999;
  transition: all 0.3s ease;
}

.scroll-top-btn:hover {
  transform: translateY(-4px);
}

@media (max-width: 960px) {
  .main-footer {
    padding: 48px 0 24px;
  }

  .footer-brand {
    max-width: 100%;
    margin-bottom: 32px;
  }

  .footer-heading {
    font-size: 14px;
  }
}
</style>
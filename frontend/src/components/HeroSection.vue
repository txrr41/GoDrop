<template>
  <section class="hero-section py-12 py-md-16">
    <v-container>
      <v-row align="center">
        <v-col cols="12" lg="6">
          <div class="mb-6">
            <v-chip
                color="primary"
                variant="tonal"
                class="font-weight-medium"
            >
              <span class="pulse-dot mr-2"></span>
              Frete Grátis em pedidos acima de R$ 199
            </v-chip>
          </div>

          <h1 class="text-h3 text-md-h2 font-weight-bold mb-4 hero-title">
            Encontre os <span class="text-primary">melhores produtos</span> com preços incríveis
          </h1>

          <p class="text-body-1 text-grey-darken-1 mb-8" style="max-width: 500px; line-height: 1.7;">
            Descubra milhares de produtos selecionados com qualidade garantida e entrega rápida para todo o Brasil.
          </p>

          <div class="d-flex flex-column flex-sm-row ga-4">
            <v-btn
                size="large"
                color="primary"
                class="text-white"
            >
              Explorar Produtos
              <v-icon end>mdi-arrow-right</v-icon>
            </v-btn>
            <v-btn
                size="large"
                variant="outlined"
                class="text-primary border-primary"
            >
              Ver Ofertas
            </v-btn>
          </div>
        </v-col>

        <v-col cols="12" lg="6" class="d-none d-lg-block">
          <div class="hero-cards-container">
            <v-row>
              <v-col cols="6">
                <v-card class="pa-5 rounded-xl" elevation="4">
                  <div class="icon-wrapper mb-3">
                    <v-icon color="primary" size="28">mdi-truck-fast</v-icon>
                  </div>
                  <h3 class="text-subtitle-1 font-weight-bold mb-1">Entrega Rápida</h3>
                  <p class="text-body-2 text-grey">Em todo Brasil</p>
                </v-card>

                <v-card class="pa-5 rounded-xl mt-4" elevation="4">
                  <div class="icon-wrapper mb-3">
                    <v-icon color="primary" size="28">mdi-credit-card</v-icon>
                  </div>
                  <h3 class="text-subtitle-1 font-weight-bold mb-1">Parcelamento</h3>
                  <p class="text-body-2 text-grey">Em até 12x</p>
                </v-card>
              </v-col>

              <v-col cols="6" class="pt-10">
                <v-card class="pa-5 rounded-xl" elevation="4">
                  <div class="icon-wrapper mb-3">
                    <v-icon color="primary" size="28">mdi-shield-check</v-icon>
                  </div>
                  <h3 class="text-subtitle-1 font-weight-bold mb-1">Compra Segura</h3>
                  <p class="text-body-2 text-grey">100% protegido</p>
                </v-card>

                <v-card class="pa-5 rounded-xl mt-4 bg-primary" elevation="4">
                  <p class="text-h3 font-weight-bold text-white">50%</p>
                  <p class="text-body-2 text-white" style="opacity: 0.8;">de desconto</p>
                </v-card>
              </v-col>
            </v-row>
          </div>
        </v-col>
      </v-row>
    </v-container>
  </section>
</template>

<script setup lang="ts">
</script>

<style scoped>
.hero-section {
  background: linear-gradient(135deg, rgba(0, 184, 212, 0.05) 0%, white 50%, rgba(0, 184, 212, 0.08) 100%);
}

.hero-title {
  line-height: 1.2;
}

.pulse-dot {
  width: 8px;
  height: 8px;
  background-color: var(--primary);
  border-radius: 50%;
  display: inline-block;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% {
    box-shadow: 0 0 0 0 rgba(0, 184, 212, 0.7);
  }
  70% {
    box-shadow: 0 0 0 8px rgba(0, 184, 212, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(0, 184, 212, 0);
  }
}

.icon-wrapper {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background-color: rgba(0, 184, 212, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
}

.hero-cards-container {
  background: linear-gradient(135deg, rgba(0, 184, 212, 0.15) 0%, rgba(0, 184, 212, 0.05) 100%);
  border-radius: 24px;
  padding: 32px;
}
</style>

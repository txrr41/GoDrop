<template>
  <section class="hero-wrapper">
    <v-container>
      <v-row align="center" class="min-vh-80">
        <v-col cols="12" lg="6" class="text-left">
          <div class="tag-label mb-4">
            <span class="dot"></span>
            LOGÍSTICA INTEGRADA E INTELIGENTE
          </div>

          <h1 class="display-title mb-6">
            Escalone sua operação com <br />
            <span class="text-gradient">Venda sem Estoque</span>
          </h1>

          <p class="description-text mb-10">
            Conecte sua loja aos melhores fornecedores globais. Nós cuidamos do
            armazenamento, separação e envio direto para o seu cliente final com
            rastreamento em tempo real.
          </p>

          <div class="action-group d-flex flex-wrap ga-4">
            <v-btn
                to ="/api"
                height="56"
                color="primary"
                elevation="0"
                class="px-8 rounded-lg text-none font-weight-bold"
             >
              Começar agora
            </v-btn>

            <v-btn
                height="56"
                variant="outlined"
                color="secondary"
                class="px-8 rounded-lg text-none font-weight-bold border-opacity-25"
            >
              Ver demonstração
            </v-btn>
          </div>

          <div class="mt-12 d-flex align-center ga-6 trust-section">
            <div class="trust-item">
              <span class="trust-value">+10k</span>
              <span class="trust-label">Lojistas ativos</span>
            </div>
            <v-divider vertical inset class="mx-2"></v-divider>
            <div class="trust-item">
              <span class="trust-value">24h</span>
              <span class="trust-label">Média de despacho</span>
            </div>
          </div>
        </v-col>

        <v-col cols="12" lg="6" class="position-relative d-none d-lg-block">
          <div class="image-composition">
            <v-img
                src="https://images.unsplash.com/photo-1586528116311-ad8dd3c8310d?auto=format&fit=crop&q=80&w=1000"
                alt="Logística de Dropshipping"
                cover
                class="main-hero-image rounded-xl elevation-24"
            >
              <template v-slot:placeholder>
                <v-row class="fill-height ma-0" align="center" justify="center">
                  <v-progress-circular indeterminate color="primary"></v-progress-circular>
                </v-row>
              </template>
            </v-img>

            <div class="floating-card status-card p-4 rounded-lg elevation-12">
              <div class="d-flex align-center mb-2">
                <v-icon color="success" size="20" class="mr-2">mdi-check-decagram</v-icon>
                <span class="text-caption font-weight-bold">PEDIDO ENVIADO</span>
              </div>
              <div class="text-h6 font-weight-bold">#SH-99283</div>
              <div class="text-caption text-grey">Destino: São Paulo, BR</div>
            </div>

            <div class="floating-card integration-card p-4 rounded-lg elevation-12">
              <div class="d-flex ga-2">
                <v-icon color="primary">mdi-sync</v-icon>
                <span class="text-caption font-weight-bold">ESTOQUE SINCRONIZADO</span>
              </div>
            </div>
          </div>
        </v-col>
      </v-row>
    </v-container>

    <div class="bg-pattern"></div>
  </section>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700;800&display=swap');

.hero-wrapper {
  font-family: 'Inter', sans-serif;
  position: relative;
  background-color: #ffffff;
  overflow: hidden;
  padding: 100px 0;
}

.min-vh-80 {
  min-height: 80vh;
}

.tag-label {
  display: inline-flex;
  align-items: center;
  background: #f0f7ff;
  color: #0061ff;
  padding: 8px 16px;
  border-radius: 100px;
  font-size: 0.75rem;
  letter-spacing: 1px;
  font-weight: 700;
  border: 1px solid rgba(0, 97, 255, 0.1);
}

.dot {
  width: 6px;
  height: 6px;
  background-color: #0061ff;
  border-radius: 50%;
  margin-right: 10px;
  box-shadow: 0 0 10px rgba(0, 97, 255, 0.5);
}


.display-title {
  font-size: 3.5rem;
  line-height: 1.1;
  font-weight: 800;
  color: #1a1a1a;
  letter-spacing: -0.02em;
}

.text-gradient {
  background: linear-gradient(90deg, #0061ff 0%, #00c6ff 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.description-text {
  font-size: 1.15rem;
  line-height: 1.6;
  color: #4a4a4a;
  max-width: 540px;
}

/* Trust Marks */
.trust-value {
  display: block;
  font-size: 1.25rem;
  font-weight: 800;
  color: #1a1a1a;
}

.trust-label {
  font-size: 0.85rem;
  color: #7a7a7a;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

/* Imagem e Cards Flutuantes */
.image-composition {
  position: relative;
  padding: 20px;
}

.main-hero-image {
  width: 100%;
  max-width: 500px;
  height: 600px;
  margin-left: auto;
  z-index: 2;
}

.floating-card {
  position: absolute;
  background: white;
  z-index: 10;
  padding: 16px;
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.status-card {
  bottom: 10%;
  left: -5%;
  width: 220px;
  animation: float 4s ease-in-out infinite;
}

.integration-card {
  top: 15%;
  right: -5%;
  animation: float 5s ease-in-out infinite reverse;
}

@keyframes float {
  0% { transform: translateY(0px); }
  50% { transform: translateY(-15px); }
  100% { transform: translateY(0px); }
}

/* Background Pattern sutil */
.bg-pattern {
  position: absolute;
  top: 0;
  right: 0;
  width: 50%;
  height: 100%;
  background-image: radial-gradient(#0061ff 0.5px, transparent 0.5px);
  background-size: 30px 30px;
  opacity: 0.05;
  z-index: 0;
}

/* Responsividade */
@media (max-width: 960px) {
  .display-title {
    font-size: 2.5rem;
  }
  .hero-wrapper {
    padding: 60px 0;
    text-align: center;
  }
  .description-text {
    margin: 0 auto 40px;
  }
  .action-group {
    justify-content: center;
  }
  .trust-section {
    justify-content: center;
  }
}
</style>
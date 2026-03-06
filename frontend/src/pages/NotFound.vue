<template>
  <div class="notfound-root">

    <div class="bg-pattern"></div>

    <div class="shape shape-1"></div>
    <div class="shape shape-2"></div>

    <header class="page-header">
      <div class="brand-container">
        <img :src="LogoDrop" alt="Logo" class="brand-name" style="width: 100px">
      </div>
    </header>

    <main class="page-content">

      <div class="text-section">
        <div class="error-badge">Erro 404</div>

        <h1 class="title">Página não encontrada</h1>

        <p class="subtitle">
          O link que você clicou pode estar quebrado, a página pode ter sido removida ou você digitou um endereço incorreto. Vamos ajudar você a voltar para a loja.
        </p>

        <div class="action-group">
          <button class="back-btn" @click="goHome">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
              <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"></path>
              <polyline points="9 22 9 12 15 12 15 22"></polyline>
            </svg>
            Voltar para a Home
          </button>

          <button class="support-btn">
            Falar com Suporte
          </button>
        </div>
      </div>

      <div class="visual-section">
        <div class="giant-404">404</div>
      </div>

    </main>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import LogoDrop from "../assets/LogoDrop.png";
const router = useRouter()
function goHome() { router.push('/home') }
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=Plus+Jakarta+Sans:wght@700;800&display=swap');

.notfound-root {
  min-height: 100vh;
  background-color: #F8FAFC;
  display: flex;
  flex-direction: column;
  font-family: 'Inter', sans-serif;
  position: relative;
  overflow: hidden;
  color: #1A1C21;
}

.page-header {
  width: 100%;
  padding: 40px 5%;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  position: relative;
  z-index: 10;
}

.brand-container {
  display: flex;
  align-items: center;
  gap: 12px;
  opacity: 0.8;
  user-select: none;
  transition: opacity 0.3s;
  cursor: pointer;
}

.brand-container:hover {
  opacity: 1;
}

.brand-logo-placeholder {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #0052FF, #4facfe);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-family: 'Plus Jakarta Sans', sans-serif;
  font-weight: 800;
  font-size: 20px;
  box-shadow: 0 4px 12px rgba(0, 82, 255, 0.2);
}

.brand-name {
  font-family: 'Plus Jakarta Sans', sans-serif;
  font-weight: 700;
  font-size: 22px;
  letter-spacing: -0.5px;
  color: #1A1C21;
}

.page-content {
  flex: 1;
  display: grid;
  grid-template-columns: 1fr 1fr;
  align-items: center;
  gap: 40px;
  max-width: 1400px;
  width: 100%;
  margin: 0 auto;
  padding: 0 5% 60px;
  position: relative;
  z-index: 1;
}

.text-section {
  max-width: 580px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.error-badge {
  background-color: rgba(0, 82, 255, 0.1);
  color: #0052FF;
  padding: 8px 16px;
  border-radius: 100px;
  font-weight: 600;
  font-size: 14px;
  letter-spacing: 0.5px;
  text-transform: uppercase;
  margin-bottom: 24px;
}

.title {
  font-family: 'Plus Jakarta Sans', sans-serif;
  font-size: clamp(36px, 5vw, 56px);
  font-weight: 800;
  color: #0F172A;
  margin: 0 0 20px;
  line-height: 1.1;
  letter-spacing: -0.03em;
}

.subtitle {
  font-size: 18px;
  color: #64748B;
  line-height: 1.6;
  margin: 0 0 48px;
}

.action-group {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
}

.back-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  padding: 16px 32px;
  background-color: #0052FF;
  color: white;
  border: none;
  border-radius: 12px;
  font-family: 'Inter', sans-serif;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 10px 25px rgba(0, 82, 255, 0.25);
}

.back-btn:hover {
  transform: translateY(-2px);
  background-color: #0041CC;
  box-shadow: 0 15px 35px rgba(0, 82, 255, 0.35);
}

.support-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 16px 32px;
  background-color: white;
  color: #0F172A;
  border: 1px solid #E2E8F0;
  border-radius: 12px;
  font-family: 'Inter', sans-serif;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.support-btn:hover {
  background-color: #F1F5F9;
  border-color: #CBD5E1;
}

.visual-section {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  position: relative;
}

.giant-404 {
  font-family: 'Plus Jakarta Sans', sans-serif;
  font-size: clamp(150px, 20vw, 280px);
  font-weight: 800;
  line-height: 0.8;
  background: linear-gradient(180deg, rgba(0, 82, 255, 0.15) 0%, rgba(79, 172, 254, 0.05) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  letter-spacing: -10px;
  user-select: none;
  transform: rotate(-2deg);
}

.bg-pattern {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: radial-gradient(#0052FF 0.7px, transparent 0.7px);
  background-size: 36px 36px;
  opacity: 0.04;
  z-index: 0;
}

.shape {
  position: absolute;
  z-index: 0;
  border-radius: 50%;
  filter: blur(100px);
  opacity: 0.12;
}
.shape-1 {
  width: 400px;
  height: 400px;
  background: #0052FF;
  top: -10%;
  left: -10%;
}
.shape-2 {
  width: 500px;
  height: 500px;
  background: #4facfe;
  bottom: -20%;
  right: -10%;
}

@media (max-width: 968px) {
  .page-content {
    grid-template-columns: 1fr;
    text-align: center;
    justify-content: center;
    padding-top: 40px;
  }

  .text-section {
    margin: 0 auto;
    align-items: center;
  }

  .action-group {
    justify-content: center;
  }

  .visual-section {
    justify-content: center;
    margin-top: 20px;
  }

  .giant-404 {
    font-size: clamp(120px, 25vw, 180px);
    background: linear-gradient(180deg, rgba(0, 82, 255, 0.1) 0%, rgba(79, 172, 254, 0.02) 100%);
  }
}

@media (max-width: 480px) {
  .action-group {
    flex-direction: column;
    width: 100%;
  }

  .back-btn, .support-btn {
    width: 100%;
  }

  .page-header {
    justify-content: center;
  }
}
</style>
<template>
  <section class="categories-section">
    <div class="section-container">

      <!-- HEADER -->
      <header class="section-header">
        <div class="header-left">
          <span class="section-eyebrow">Descubra</span>
          <h2 class="section-title">Categorias</h2>
        </div>
        <a href="/catalogo" class="view-all-link">
          <span>Ver catálogo completo</span>
          <svg class="arrow-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M5 12h14M12 5l7 7-7 7"/></svg>
        </a>
      </header>

      <!-- GRID -->
      <div class="categories-grid">
        <div
            v-for="category in categories"
            :key="category.name"
            class="cat-card"
            :style="{ '--cat-color': category.color, '--cat-bg': category.bg }"
            @click="selectCategory(category.name)"
        >
          <!-- Ícone -->
          <div class="cat-icon-wrap">
            <v-icon size="24" :color="category.color">{{ category.icon }}</v-icon>
          </div>

          <!-- Conteúdo -->
          <div class="cat-content">
            <h3 class="cat-name">{{ category.name }}</h3>
            <p class="cat-count">{{ category.count }} produtos</p>
          </div>

          <!-- Seta indicativa (aparece no hover) -->
          <div class="cat-action">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="var(--cat-color)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M5 12h14M12 5l7 7-7 7"/>
            </svg>
          </div>
        </div>
      </div>

    </div>
  </section>
</template>

<script setup>
const emit = defineEmits(['category-selected'])

const categories = [
  { name: 'Eletrônicos', icon: 'mdi-cellphone',        count: 234, color: '#0F172A', bg: '#F1F5F9' },
  { name: 'Moda',        icon: 'mdi-tshirt-crew',       count: 567, color: '#0F172A', bg: '#F1F5F9' },
  { name: 'Casa & Decor',icon: 'mdi-sofa',              count: 189, color: '#0F172A', bg: '#F1F5F9' },
  { name: 'Esportes',    icon: 'mdi-basketball',        count: 145, color: '#0F172A', bg: '#F1F5F9' },
  { name: 'Beleza',      icon: 'mdi-lipstick',          count: 312, color: '#0F172A', bg: '#F1F5F9' },
  { name: 'Games',       icon: 'mdi-gamepad-variant',   count: 98,  color: '#0F172A', bg: '#F1F5F9' },
]

// Nota: No script alterei as cores para um padrão mais neutro e premium (tons de cinza/preto),
// mas você pode voltar para as coloridas se for a identidade da sua marca!

const selectCategory = (name) => emit('category-selected', name)
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');

.categories-section {
  padding: 64px 0;
  font-family: 'Inter', sans-serif;
  background-color: #FAFAFA; /* Fundo levemente cinza para destacar os cards brancos */
}

.section-container {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 24px;
}

/* =========================================
   HEADER
   ========================================= */
.section-header {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  margin-bottom: 40px;
}

.header-left {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.section-eyebrow {
  font-size: 12px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.15em;
  color: #64748B;
}

.section-title {
  font-size: 28px;
  font-weight: 700;
  color: #0F172A;
  letter-spacing: -0.03em;
  margin: 0;
}

.view-all-link {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  font-weight: 500;
  color: #0F172A;
  text-decoration: none;
  padding: 8px 16px;
  border-radius: 99px;
  border: 1px solid #E2E8F0;
  background: #FFF;
  transition: all 0.2s ease;
}

.view-all-link:hover {
  background: #0F172A;
  color: #FFF;
  border-color: #0F172A;
}

.view-all-link:hover .arrow-icon {
  transform: translateX(3px);
  transition: transform 0.2s ease;
}

/* =========================================
   GRID
   ========================================= */
.categories-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 20px;
}

/* =========================================
   CARDS PREMIUM
   ========================================= */
.cat-card {
  position: relative;
  background: #FFFFFF;
  border: 1px solid #F1F5F9;
  border-radius: 20px;
  padding: 24px 20px;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  gap: 20px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.02);
  overflow: hidden;
}

.cat-card:hover {
  transform: translateY(-6px);
  border-color: #E2E8F0;
  box-shadow: 0 16px 32px -8px rgba(0, 0, 0, 0.08);
}

/* ICON BACKGROUND */
.cat-icon-wrap {
  width: 52px;
  height: 52px;
  border-radius: 14px;
  background: var(--cat-bg);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: all 0.3s ease;
}

.cat-card:hover .cat-icon-wrap {
  transform: scale(1.05);
  background: #F8FAFC;
}

/* TEXT CONTENT */
.cat-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
  z-index: 2;
}

.cat-name {
  font-size: 16px;
  font-weight: 600;
  color: #0F172A;
  margin: 0;
  letter-spacing: -0.01em;
}

.cat-count {
  font-size: 13px;
  font-weight: 500;
  color: #64748B;
  margin: 0;
}

/* INTERAÇÃO DA SETA (SLIDE IN) */
.cat-action {
  position: absolute;
  bottom: 24px;
  right: 20px;
  opacity: 0;
  transform: translateX(-15px);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.cat-card:hover .cat-action {
  opacity: 1;
  transform: translateX(0);
}

/* =========================================
   RESPONSIVO
   ========================================= */
@media (max-width: 1024px) {
  .categories-grid { grid-template-columns: repeat(3, 1fr); }
  .section-title { font-size: 24px; }
}

@media (max-width: 640px) {
  .categories-section { padding: 40px 0; }
  .section-container { padding: 0 16px; }
  .categories-grid { grid-template-columns: repeat(2, 1fr); gap: 12px; }

  .section-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
    margin-bottom: 28px;
  }

  .cat-card { padding: 20px 16px; border-radius: 16px; }
  .cat-name { font-size: 15px; }
  .cat-icon-wrap { width: 44px; height: 44px; }

  /* Mantém a seta levemente visível no mobile já que não há hover */
  .cat-action {
    opacity: 0.3;
    transform: translateX(0);
    bottom: 20px;
    right: 16px;
  }
}
</style>
<template>
  <section class="categories-root py-12 py-md-16">
    <v-container>

      <div class="showcase-header text-center mb-12">
        <span class="showcase-eyebrow">Descubra</span>
        <h2 class="showcase-title">Categorias</h2>
        <p class="showcase-subtitle">Explore nossa seleção de produtos por categoria</p>
      </div>

      <div class="categories-grid">
        <div
            v-for="category in categories"
            :key="category.name"
            class="cat-card"
            @click="selectCategory(category.name)"
        >
          <div class="cat-icon-wrap">
            <v-icon size="26" color="#2563EB">{{ category.icon }}</v-icon>
          </div>
          <div class="cat-content">
            <h3 class="cat-name">{{ category.name }}</h3>
            <p class="cat-count">{{ category.count }} produtos</p>
          </div>
          <div class="cat-action">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#2563EB" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
              <path d="M5 12h14M12 5l7 7-7 7"/>
            </svg>
          </div>
        </div>
      </div>

      <div class="text-center mt-12">
        <button class="btn-view-all" @click="$router.push('/catalogo')">
          Ver Catálogo Completo
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
            <path d="M5 12h14M12 5l7 7-7 7"/>
          </svg>
        </button>
      </div>

    </v-container>
  </section>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()
const emit = defineEmits(['category-selected'])

const categories = [
  { name: 'Eletrônicos', icon: 'mdi-cellphone', count: 234 },
  { name: 'Moda', icon: 'mdi-tshirt-crew', count: 567 },
  { name: 'Casa & Decor', icon: 'mdi-sofa', count: 189 },
  { name: 'Esportes', icon: 'mdi-basketball', count: 145 },
  { name: 'Beleza', icon: 'mdi-lipstick', count: 312 },
  { name: 'Games', icon: 'mdi-gamepad-variant', count: 98 },
]

const selectCategory = (name) => {
  emit('category-selected', name)
  router.push({ path: '/produtos', query: { categoria: name } })
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:ital,wght@0,300;0,400;0,500;0,600;0,700;1,400&family=DM+Serif+Display:ital@0;1&display=swap');

*, *::before, *::after { box-sizing: border-box; }

.categories-root {
  --blue-50:   #EFF6FF;
  --blue-100:  #DBEAFE;
  --blue-200:  #BFDBFE;
  --blue-600:  #2563EB;
  --blue-700:  #1D4ED8;
  --c-bg:      #F8FAFF;
  --c-surface: #FFFFFF;
  --c-border:  #E2EBFF;
  --c-text:    #0F172A;
  --c-text-2:  #475569;
  --c-text-3:  #94A3B8;
  --c-accent:  var(--blue-600);
  --radius-lg: 20px;
  --shadow-md: 0 4px 16px rgba(37,99,235,.12), 0 2px 6px rgba(0,0,0,.06);

  background: var(--c-bg);
  font-family: 'Plus Jakarta Sans', sans-serif;
}

.showcase-header { padding-bottom: 4px; }

.showcase-eyebrow {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-size: 11px;
  font-weight: 700;
  letter-spacing: .18em;
  text-transform: uppercase;
  color: var(--c-accent);
  margin-bottom: 12px;
}

.showcase-eyebrow::before,
.showcase-eyebrow::after {
  content: '';
  display: block;
  width: 24px;
  height: 2px;
  background: var(--blue-200);
  border-radius: 2px;
}

.showcase-title {
  font-family: 'DM Serif Display', serif;
  font-size: clamp(26px, 3vw, 40px);
  font-weight: 400;
  color: var(--c-text);
  letter-spacing: -.03em;
  margin-bottom: 10px;
  line-height: 1.15;
}

.showcase-subtitle {
  font-size: 15px;
  color: var(--c-text-3);
  font-weight: 500;
}

.categories-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 16px;
}

.cat-card {
  background: var(--c-surface);
  border: 1.5px solid var(--c-border);
  border-radius: var(--radius-lg);
  padding: 24px 20px;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  gap: 16px;
  position: relative;
  overflow: hidden;
  animation: fadeUp .45s ease both;
  transition: box-shadow .25s, transform .25s, border-color .25s;
}

.cat-card:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-5px);
  border-color: var(--blue-200);
}

@keyframes fadeUp {
  from { opacity: 0; transform: translateY(18px); }
  to   { opacity: 1; transform: translateY(0); }
}

.cat-icon-wrap {
  width: 52px;
  height: 52px;
  border-radius: 14px;
  background: var(--blue-50);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: transform .25s, background .25s;
  border: 1.5px solid var(--blue-100);
}

.cat-card:hover .cat-icon-wrap {
  transform: scale(1.08);
  background: var(--blue-100);
}

.cat-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.cat-name {
  font-size: 15px;
  font-weight: 700;
  color: var(--c-text);
  margin: 0;
  letter-spacing: -.01em;
}

.cat-count {
  font-size: 12px;
  font-weight: 500;
  color: var(--c-text-3);
  margin: 0;
}

.cat-action {
  position: absolute;
  bottom: 20px;
  right: 18px;
  opacity: 0;
  transform: translateX(-10px);
  transition: opacity .25s, transform .25s;
}

.cat-card:hover .cat-action {
  opacity: 1;
  transform: translateX(0);
}

.btn-view-all {
  padding: 13px 32px;
  background: var(--c-surface);
  color: var(--c-accent);
  border: 2px solid var(--blue-200);
  border-radius: 50px;
  font: 700 14px 'Plus Jakarta Sans', sans-serif;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  transition: all .2s ease;
  letter-spacing: .01em;
}

.btn-view-all:hover {
  background: var(--blue-600);
  color: white;
  border-color: var(--blue-600);
  box-shadow: 0 8px 20px -6px rgba(37,99,235,.4);
  transform: translateY(-2px);
}

.btn-view-all:active { transform: translateY(0); }

@media (max-width: 1024px) {
  .categories-grid { grid-template-columns: repeat(3, 1fr); }
}

@media (max-width: 640px) {
  .categories-grid { grid-template-columns: repeat(2, 1fr); gap: 12px; }
  .cat-card { padding: 18px 14px; border-radius: 16px; }
  .cat-action { opacity: 0.4; transform: translateX(0); }
}
</style>
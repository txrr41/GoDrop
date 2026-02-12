<template>
  <div class="home-page">
    <HeroSection />
    <CategoriesSection @category-selected="handleCategoryClick" />
    <FeaturedProducts />
    <Footer />
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import HeroSection from "../components/HeroSection.vue"
import CategoriesSection from "../components/CategoriesSection.vue"
import FeaturedProducts from "../components/FeaturesProducts.vue"
import Footer from "../components/Footer.vue"

const router = useRouter()

const handleCategoryClick = (categoryName) => {
  router.push({
    path: '/produtos',
    query: { categoria: categoryName }
  })
}
</script>

<style scoped>
.home-page {
  background: #fff;
}
</style>
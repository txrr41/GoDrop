<template>
  <div class="menu">
    <div class="logo">ShipHub</div>
    <ul>
      <li><a href="/" @click.prevent="goHome">Home</a></li>
    </ul>
  </div>
</template>

<script>
import { Inertia } from '@inertiajs/inertia'

export default {
  methods: {
    goHome() {
      Inertia.visit('/')
    }
  }
}
</script>

<style scoped>
.menu {
  width: 200px;
  background-color: #1e1e2f;
  color: white;
  padding: 20px;
  height: 100vh;
}

.logo {
  font-size: 1.5em;
  font-weight: bold;
  margin-bottom: 30px;
}

ul {
  list-style: none;
  padding: 0;
}

li a {
  color: white;
  text-decoration: none;
  display: block;
  padding: 10px 0;
}

li a:hover {
  background-color: #2e2e4f;
}
</style>

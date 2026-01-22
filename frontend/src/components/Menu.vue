<template>
  <div class="menu">
    <div class="logo"><img src="../assets/logo.png" style="width: 180px; padding: 0 0 0 0"></div>
    <ul>
      <li>
        <router-link to="/">Home</router-link>
      </li>
    </ul>
  </div>
</template>

<script>
export default {}
</script>

<style scoped>
.menu {
  width: 200px;
  background-color: #ffffff;
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

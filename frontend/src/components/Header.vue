<template>
  <header class="header">
    <div class="logo">ShipHub</div>
    <div class="buttons">
      <button @click="openLogin">Login</button>
      <button @click="openRegister">Cadastro</button>
    </div>

    <!-- Modais -->
    <div v-if="showLogin" class="modal-overlay" @click.self="closeLogin">
      <div class="modal">
        <h2>Login</h2>
        <!-- aqui você coloca o form de login -->
        <button @click="closeLogin">Fechar</button>
      </div>
    </div>

    <div v-if="showRegister" class="modal-overlay" @click.self="closeRegister">
      <div class="modal">
        <h2>Cadastro</h2>
        <!-- aqui você coloca o form de cadastro -->
        <button @click="closeRegister">Fechar</button>
      </div>
    </div>
  </header>
</template>

<script>
export default {
  data() {
    return {
      showLogin: false,
      showRegister: false
    }
  },
  methods: {
    openLogin() {
      this.showLogin = true
    },
    closeLogin() {
      this.showLogin = false
    },
    openRegister() {
      this.showRegister = true
    },
    closeRegister() {
      this.showRegister = false
    }
  }
}
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #2e2e4f;
  padding: 10px 20px;
  color: white;
}

.buttons button {
  margin-left: 10px;
  padding: 5px 15px;
  border: none;
  background-color: #4b4b7f;
  color: white;
  cursor: pointer;
}

.buttons button:hover {
  background-color: #6b6bbf;
}

/* Overlay dos modais */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.5); /* fundo borrado */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

/* Conteúdo do modal */
.modal {
  background: white;
  padding: 20px;
  border-radius: 5px;
}
</style>

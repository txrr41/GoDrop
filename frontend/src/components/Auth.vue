<template>
  <!-- OVERLAY -->
  <div class="overlay" @click.self="$emit('close')">
    <div class="auth-container">
      <div class="auth-card">
        <!-- Logo e Header -->
        <div class="auth-header">
          <div class="logo-container">
            <img :src="LogoDrop" alt="Go Drop" class="auth-logo" />
          </div>
          <button @click="$emit('close')" class="close-button" type="button">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M18 6L6 18M6 6l12 12"/>
            </svg>
          </button>
        </div>

        <!-- Tabs -->
        <div class="auth-tabs">
          <button
              @click="isSignUp = false"
              :class="['tab-button', !isSignUp && 'active']"
              type="button"
          >
            Entrar
          </button>
          <button
              @click="isSignUp = true"
              :class="['tab-button', isSignUp && 'active']"
              type="button"
          >
            Criar Conta
          </button>
          <div class="tab-indicator" :class="{ 'sign-up': isSignUp }"></div>
        </div>

        <!-- Title -->
        <div class="auth-title">
          <h1>{{ isSignUp ? 'Comece sua jornada' : 'Bem-vindo de volta' }}</h1>
          <p>{{ isSignUp ? 'Crie sua conta para continuar' : 'Entre com suas credenciais' }}</p>
        </div>

        <!-- Form -->
        <form @submit.prevent="handleSubmit" class="auth-form">
          <!-- Nome (apenas no cadastro) -->
          <div v-if="isSignUp" class="form-group">
            <label class="form-label">Nome completo</label>
            <div class="input-wrapper">
              <svg class="input-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                <circle cx="12" cy="7" r="4"/>
              </svg>
              <input
                  v-model="name"
                  type="text"
                  placeholder="João Silva"
                  class="form-input"
                  required
              />
            </div>
          </div>

          <!-- Email -->
          <div class="form-group">
            <label class="form-label">E-mail</label>
            <div class="input-wrapper">
              <svg class="input-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
                <polyline points="22,6 12,13 2,6"/>
              </svg>
              <input
                  v-model="email"
                  type="email"
                  placeholder="seu@email.com"
                  class="form-input"
                  required
              />
            </div>
          </div>

          <!-- Senha -->
          <div class="form-group">
            <label class="form-label">Senha</label>
            <div class="input-wrapper">
              <svg class="input-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
              </svg>
              <input
                  v-model="password"
                  :type="showPassword ? 'text' : 'password'"
                  placeholder="••••••••"
                  class="form-input"
                  required
              />
              <button
                  type="button"
                  @click="showPassword = !showPassword"
                  class="password-toggle"
              >
                <svg v-if="!showPassword" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                  <circle cx="12" cy="12" r="3"/>
                </svg>
                <svg v-else width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"/>
                  <line x1="1" y1="1" x2="23" y2="23"/>
                </svg>
              </button>
            </div>
          </div>

          <!-- Password Strength (apenas no cadastro) -->
          <div v-if="isSignUp && password" class="password-strength">
            <div class="strength-bars">
              <div
                  v-for="i in 4"
                  :key="i"
                  :class="['strength-bar', i <= str ? strengthColors[str - 1] : '']"
              />
            </div>
            <p :class="['strength-text', strengthTextColors[str - 1] || '']">
              {{ strengthLabels[str] || 'Digite uma senha' }}
            </p>
          </div>

          <!-- Forgot Password (apenas no login) -->
          <div v-if="!isSignUp" class="forgot-password">
            <button type="button" class="forgot-link">
              Esqueceu a senha?
            </button>
          </div>

          <!-- Submit Button -->
          <button type="submit" class="submit-button">
            {{ isSignUp ? 'Criar minha conta' : 'Entrar na plataforma' }}
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="5" y1="12" x2="19" y2="12"/>
              <polyline points="12 5 19 12 12 19"/>
            </svg>
          </button>
        </form>

        <!-- Footer -->
        <div class="auth-footer">
          <div class="divider">
            <span>ou continue com</span>
          </div>

          <div class="social-buttons">
            <button
                type="button"
                class="social-btn google-btn"
                @click="loginWithGoogle"
            >
              <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                <path d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z" fill="#4285F4"/>
                <path d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z" fill="#34A853"/>
                <path d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z" fill="#FBBC05"/>
                <path d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z" fill="#EA4335"/>
              </svg>
              Google
            </button>

            <button type="button" class="social-button">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                <path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z"/>
              </svg>
              Facebook
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useAuthStore } from "../stores/auth.js"
import LogoDrop from '../assets/LogoDrop.png'

const emit = defineEmits(['close'])
const auth = useAuthStore()

const name = ref('')
const email = ref('')
const password = ref('')
const showPassword = ref(false)
const loginWithGoogle = () => {
  window.location.href = 'http://localhost:8080/oauth2/authorization/google'
}

const strengthColors = ['red', 'orange', 'yellow', 'green']
const strengthTextColors = ['text-red', 'text-orange', 'text-yellow', 'text-green']
const strengthLabels = ['', 'Fraca', 'Razoável', 'Boa', 'Forte']

const str = computed(() => {
  const p = password.value
  if (!p) return 0
  let s = 0
  if (p.length >= 8) s++
  if (/[a-z]/.test(p) && /[A-Z]/.test(p)) s++
  if (/\d/.test(p)) s++
  if (/[^a-zA-Z0-9]/.test(p)) s++
  return s
})

const handleSubmit = async () => {
  try {
    if (isSignUp.value) {
      await auth.register({
        name: name.value,
        email: email.value,
        password: password.value
      })
    } else {
      await auth.login(email.value, password.value)
    }
    emit('close')
  } catch (err) {
    console.error('Erro', err)
    alert('Erro ao realizar operação: ' + (err.message || 'Erro desconhecido'))
  }
}

const props = defineProps({
  startSignUp: {
    type: Boolean,
    default: false
  }
})

const isSignUp = ref(props.startSignUp)

watch(
    () => props.startSignUp,
    (val) => {
      isSignUp.value = val
    }
)
</script>

<style scoped>
/* Overlay */
.overlay {
  position: fixed;
  inset: 0;
  background: rgba(15, 23, 42, 0.75);
  backdrop-filter: blur(8px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
  padding: 20px;
}

/* Container */
.auth-container {
  width: 100%;
  max-width: 480px;
  animation: slideUp 0.3s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Card */
.auth-card {
  background: #ffffff;
  border-radius: 24px;
  box-shadow: 0 24px 48px rgba(0, 0, 0, 0.12);
  overflow: hidden;
}

/* Header */
.auth-header {
  padding: 32px 32px 24px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  border-bottom: 1px solid #f1f5f9;
}

.logo-container {
  flex: 1;
}

.auth-logo {
  height: 32px;
  width: auto;
}

.close-button {
  width: 40px;
  height: 40px;
  border-radius: 12px;
  border: none;
  background: #f8fafc;
  color: #64748b;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s;
}

.close-button:hover {
  background: #f1f5f9;
  color: #334155;
}

/* Tabs */
.auth-tabs {
  position: relative;
  display: flex;
  padding: 0 32px;
  margin-top: 16px;
  gap: 8px;
}

.tab-button {
  flex: 1;
  padding: 12px 24px;
  border: none;
  background: transparent;
  font-size: 15px;
  font-weight: 600;
  color: #64748b;
  cursor: pointer;
  transition: color 0.2s;
  position: relative;
  z-index: 1;
}

.tab-button.active {
  color: #0f172a;
}

.tab-indicator {
  position: absolute;
  bottom: 0;
  left: 32px;
  width: calc(50% - 36px);
  height: 3px;
  background: #0f172a;
  border-radius: 3px 3px 0 0;
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.tab-indicator.sign-up {
  transform: translateX(calc(100% + 8px));
}

/* Title */
.auth-title {
  padding: 32px 32px 24px;
  text-align: center;
}

.auth-title h1 {
  font-size: 28px;
  font-weight: 700;
  color: #0f172a;
  margin: 0 0 8px 0;
  letter-spacing: -0.02em;
}

.auth-title p {
  font-size: 15px;
  color: #64748b;
  margin: 0;
}

/* Form */
.auth-form {
  padding: 0 32px 32px;
}

.form-group {
  margin-bottom: 20px;
}

.form-label {
  display: block;
  font-size: 14px;
  font-weight: 600;
  color: #334155;
  margin-bottom: 8px;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.input-icon {
  position: absolute;
  left: 16px;
  color: #94a3b8;
  pointer-events: none;
}

.form-input {
  width: 100%;
  height: 52px;
  padding: 0 48px;
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  font-size: 15px;
  color: #0f172a;
  background: #ffffff;
  transition: all 0.2s;
}

.form-input:focus {
  outline: none;
  border-color: #0f172a;
  background: #fafafa;
}

.form-input::placeholder {
  color: #cbd5e1;
}

.password-toggle {
  position: absolute;
  right: 16px;
  width: 36px;
  height: 36px;
  border: none;
  background: transparent;
  color: #94a3b8;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  transition: all 0.2s;
}

.password-toggle:hover {
  background: #f8fafc;
  color: #64748b;
}

/* Password Strength */
.password-strength {
  margin-top: 12px;
}

.strength-bars {
  display: flex;
  gap: 6px;
  margin-bottom: 8px;
}

.strength-bar {
  flex: 1;
  height: 4px;
  background: #e2e8f0;
  border-radius: 2px;
  transition: all 0.3s;
}

.strength-bar.red { background: #ef4444; }
.strength-bar.orange { background: #f97316; }
.strength-bar.yellow { background: #eab308; }
.strength-bar.green { background: #22c55e; }

.strength-text {
  font-size: 13px;
  font-weight: 600;
  margin: 0;
}

.text-red { color: #ef4444; }
.text-orange { color: #f97316; }
.text-yellow { color: #eab308; }
.text-green { color: #22c55e; }

/* Forgot Password */
.forgot-password {
  text-align: right;
  margin-bottom: 24px;
}

.forgot-link {
  border: none;
  background: none;
  color: #64748b;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: color 0.2s;
}

.forgot-link:hover {
  color: #0f172a;
}

/* Submit Button */
.submit-button {
  width: 100%;
  height: 56px;
  border: none;
  border-radius: 12px;
  background: #0f172a;
  color: #ffffff;
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition: all 0.2s;
}

.submit-button:hover {
  background: #1e293b;
  transform: translateY(-1px);
  box-shadow: 0 8px 16px rgba(15, 23, 42, 0.24);
}

.submit-button:active {
  transform: translateY(0);
}

/* Footer */
.auth-footer {
  padding: 24px 32px 32px;
  border-top: 1px solid #f1f5f9;
}

.divider {
  position: relative;
  text-align: center;
  margin-bottom: 20px;
}

.divider::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 1px;
  background: #e2e8f0;
}

.divider span {
  position: relative;
  display: inline-block;
  padding: 0 16px;
  background: #ffffff;
  font-size: 13px;
  color: #94a3b8;
  font-weight: 500;
}

.social-buttons {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.social-button {
  height: 48px;
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  background: #ffffff;
  color: #64748b;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition: all 0.2s;
}

.social-button:hover {
  border-color: #cbd5e1;
  background: #f8fafc;
}

/* Responsive */
@media (max-width: 640px) {
  .auth-header,
  .auth-title,
  .auth-form,
  .auth-footer {
    padding-left: 24px;
    padding-right: 24px;
  }

  .auth-tabs {
    padding: 0 24px;
  }

  .tab-indicator {
    left: 24px;
    width: calc(50% - 28px);
  }

  .auth-title h1 {
    font-size: 24px;
  }

  .social-buttons {
    grid-template-columns: 1fr;
  }
}
</style>
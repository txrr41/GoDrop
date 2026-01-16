<template>
  <!-- OVERLAY -->
  <div class="overlay" @click.self="$emit('close')">
    <div class="auth">
      <div class="card-wrap">
        <div class="card">
          <div class="head">
            <h1 class="title">{{ isSignUp ? 'Create Account' : 'Welcome Back' }}</h1>
            <p class="sub">
              {{ isSignUp ? 'Sign up to get started' : 'Sign in to your account' }}
            </p>
          </div>

          <div class="tabs">
            <button @click="isSignUp = false" :class="['tab', !isSignUp && 'active']">
              Sign In
            </button>
            <button @click="isSignUp = true" :class="['tab', isSignUp && 'active']">
              Sign Up
            </button>
          </div>

          <form @submit.prevent="handleSubmit" class="form">
            <div v-if="isSignUp" class="field">
              <label class="label">Full Name</label>
              <input v-model="name" type="text" placeholder="John Doe" class="input" />
            </div>

            <div class="field">
              <label class="label">Email</label>
              <input v-model="email" type="email" placeholder="you@example.com" class="input" />
            </div>

            <div class="field">
              <label class="label">Password</label>
              <div class="pw-wrap">
                <input
                    v-model="password"
                    :type="showPassword ? 'text' : 'password'"
                    placeholder="••••••••"
                    class="input pw-input"
                />
                <button type="button" @click="showPassword = !showPassword" class="pw-btn">
                  👁
                </button>
              </div>
            </div>

            <div v-if="isSignUp && password" class="strength">
              <div class="bars">
                <div
                    v-for="i in 4"
                    :key="i"
                    :class="['bar', i <= str ? colors[str - 1] : '']"
                />
              </div>
              <p :class="['msg', txtColors[str - 1] || '']">
                {{ labels[str] || 'Enter a password' }}
              </p>
            </div>

            <div v-if="!isSignUp" class="forgot">
              <button type="button" class="link">Forgot password?</button>
            </div>

            <button type="submit" class="btn">
              {{ isSignUp ? 'Create Account' : 'Sign In' }}
            </button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

defineEmits(['close'])

const isSignUp = ref(false)
const name = ref('')
const email = ref('')
const password = ref('')
const showPassword = ref(false)

const colors = ['red', 'orange', 'yellow', 'green']
const txtColors = ['red', 'orange', 'yellow', 'green']
const labels = ['', 'Weak', 'Fair', 'Good', 'Strong']

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

const handleSubmit = () => {
  console.log(isSignUp.value ? 'Sign up' : 'Sign in')
}
</script>

<style scoped>
/* OVERLAY */
.overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.65);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

/* AUTH CARD */
.auth {
  width: 100%;
  max-width: 28rem;
  padding: 1rem;
}

.card {
  backdrop-filter: blur(24px);
  background: rgba(255, 255, 255, 0.1);
  border-radius: 1rem;
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
  padding: 2rem;
}

.head {
  text-align: center;
  margin-bottom: 2rem;
}

.title {
  font-size: 1.875rem;
  font-weight: 700;
  color: #fff;
}

.sub {
  color: rgba(255, 255, 255, 0.6);
}

* { box-sizing: border-box; margin: 0; padding: 0; }

.page {
  min-height: 100vh;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 1rem;
  position: relative;
  overflow: hidden;
}

.bg {
  position: absolute;
  inset: 0;
  background: linear-gradient(to bottom right, #0f172a, #581c87, #0f172a);
}

.bg-img {
  position: absolute;
  inset: 0;

  opacity: 0.4;
}

.card-wrap {
  position: relative;
  width: 100%;
  max-width: 28rem;
}

.card {
  backdrop-filter: blur(24px);
  background: rgba(255,255,255,0.1);
  border-radius: 1rem;
  border: 1px solid rgba(255,255,255,0.2);
  box-shadow: 0 25px 50px -12px rgba(0,0,0,0.25);
  padding: 2rem;
}

.head { text-align: center; margin-bottom: 2rem; }
.title { font-size: 1.875rem; font-weight: 700; color: #fff; margin-bottom: 0.5rem; }
.sub { color: rgba(255,255,255,0.6); }

.tabs {
  display: flex;
  background: rgba(255,255,255,0.05);
  border-radius: 0.5rem;
  padding: 0.25rem;
  margin-bottom: 1.5rem;
}

.tab {
  flex: 1;
  padding: 0.5rem 1rem;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  font-weight: 500;
  color: rgba(255,255,255,0.6);
  background: transparent;
  border: none;
  cursor: pointer;
  transition: all 0.2s;
}

.tab:hover { color: #fff; }
.tab.active { background: rgba(255,255,255,0.2); color: #fff; }

.form { display: flex; flex-direction: column; gap: 1rem; }
.field { display: flex; flex-direction: column; gap: 0.5rem; }
.label { font-size: 0.875rem; font-weight: 500; color: rgba(255,255,255,0.8); }

.input {
  width: 100%;
  padding: 0.75rem 1rem;
  background: rgba(255,255,255,0.05);
  border: 1px solid rgba(255,255,255,0.1);
  border-radius: 0.5rem;
  color: #fff;
  font-size: 1rem;
  outline: none;
  transition: all 0.2s;
}

.input::placeholder { color: rgba(255,255,255,0.4); }
.input:focus { box-shadow: 0 0 0 2px rgba(168,85,247,0.5); border-color: transparent; }

.pw-wrap { position: relative; }
.pw-input { padding-right: 3rem; }

.pw-btn {
  position: absolute;
  right: 0.75rem;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: rgba(255,255,255,0.4);
  cursor: pointer;
  transition: color 0.2s;
}

.pw-btn:hover { color: rgba(255,255,255,0.8); }

.icon { width: 1.25rem; height: 1.25rem; }

.strength { display: flex; flex-direction: column; gap: 0.5rem; }
.bars { display: flex; gap: 0.25rem; }

.bar {
  height: 0.25rem;
  flex: 1;
  border-radius: 9999px;
  background: rgba(255,255,255,0.1);
  transition: all 0.3s;
}

.bar.red { background: #ef4444; }
.bar.orange { background: #f97316; }
.bar.yellow { background: #eab308; }
.bar.green { background: #22c55e; }

.msg { font-size: 0.75rem; color: rgba(255,255,255,0.4); }
.msg.red { color: #f87171; }
.msg.orange { color: #fb923c; }
.msg.yellow { color: #facc15; }
.msg.green { color: #4ade80; }

.forgot { display: flex; justify-content: flex-end; }

.link {
  font-size: 0.875rem;
  color: #c084fc;
  background: none;
  border: none;
  cursor: pointer;
  transition: color 0.2s;
}

.link:hover { color: #d8b4fe; }

.btn {
  width: 100%;
  padding: 0.75rem;
  background: linear-gradient(to right, #9333ea, #db2777);
  color: #fff;
  font-weight: 500;
  border: none;
  border-radius: 0.5rem;
  cursor: pointer;
  transition: all 0.2s;
}

.btn:hover { background: linear-gradient(to right, #a855f7, #ec4899); transform: scale(1.02); }
.btn:active { transform: scale(0.98); }

.divider { display: flex; align-items: center; gap: 1rem; margin: 1.5rem 0; }
.line { flex: 1; height: 1px; background: rgba(255,255,255,0.1); }
.txt { color: rgba(255,255,255,0.4); font-size: 0.875rem; }

.social { display: flex; gap: 0.75rem; }

.soc-btn {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  padding: 0.75rem;
  background: rgba(255,255,255,0.05);
  border: 1px solid rgba(255,255,255,0.1);
  border-radius: 0.5rem;
  cursor: pointer;
  transition: all 0.2s;
  color: #fff;
}

.soc-btn:hover { background: rgba(255,255,255,0.1); }
</style>

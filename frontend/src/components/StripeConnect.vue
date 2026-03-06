<template>
  <v-card rounded="xl" elevation="0" border>
    <v-card-text class="pa-6">

      <div v-if="status === 'NOT_CONNECTED'" class="text-center py-4">
        <div class="stripe-icon mb-4">
          <svg width="48" height="48" viewBox="0 0 48 48" fill="none">
            <rect width="48" height="48" rx="12" fill="#635BFF"/>
            <path d="M22.5 18.5c0-1.1.9-1.5 2.3-1.5 2.1 0 4.7.6 6.7 1.7v-6.3c-2.2-.9-4.5-1.4-6.7-1.4-5.5 0-9.2 2.9-9.2 7.7 0 7.5 10.3 6.3 10.3 9.5 0 1.3-1.1 1.7-2.6 1.7-2.3 0-5.2-.9-7.5-2.2v6.4c2.5 1.1 5.1 1.6 7.5 1.6 5.7 0 9.6-2.8 9.6-7.7-.1-8.1-10.4-6.6-10.4-9.5z" fill="white"/>
          </svg>
        </div>
        <h3 class="text-h6 font-weight-bold mb-2">Conecte sua conta Stripe</h3>
        <p class="text-medium-emphasis text-body-2 mb-6" style="max-width:360px; margin:auto">
          Para receber automaticamente sua parte nas vendas, conecte ou crie uma conta Stripe.
          É rápido e seguro — a Stripe cuida de tudo.
        </p>

        <v-card color="grey-lighten-5" rounded="lg" border class="mb-6 text-left">
          <v-list density="compact" bg-color="transparent">
            <v-list-item prepend-icon="mdi-check-circle" color="success">
              <v-list-item-title class="text-body-2">Split automático no momento da venda</v-list-item-title>
            </v-list-item>
            <v-list-item prepend-icon="mdi-check-circle" color="success">
              <v-list-item-title class="text-body-2">Saque direto para sua conta bancária</v-list-item-title>
            </v-list-item>
            <v-list-item prepend-icon="mdi-check-circle" color="success">
              <v-list-item-title class="text-body-2">Dashboard de vendas na Stripe</v-list-item-title>
            </v-list-item>
            <v-list-item prepend-icon="mdi-check-circle" color="success">
              <v-list-item-title class="text-body-2">Sem taxas extras da GoDrop</v-list-item-title>
            </v-list-item>
          </v-list>
        </v-card>

        <v-btn
            color="#635BFF"
            size="large"
            rounded="lg"
            :loading="loading"
            @click="startOnboarding"
            style="color:white; min-width:220px"
        >
          <v-icon start>mdi-open-in-new</v-icon>
          Conectar com Stripe
        </v-btn>
      </div>

      <div v-else-if="status === 'PENDING'" class="text-center py-4">
        <v-icon size="56" color="warning" class="mb-4">mdi-clock-outline</v-icon>
        <h3 class="text-h6 font-weight-bold mb-2">Cadastro incompleto</h3>
        <p class="text-medium-emphasis text-body-2 mb-6" style="max-width:340px; margin:auto">
          Você iniciou o cadastro mas não terminou. Clique abaixo para continuar de onde parou.
        </p>
        <div class="d-flex gap-3 justify-center">
          <v-btn
              color="#635BFF"
              rounded="lg"
              :loading="loading"
              @click="startOnboarding"
              style="color:white"
          >
            <v-icon start>mdi-arrow-right</v-icon>
            Continuar cadastro
          </v-btn>
          <v-btn variant="outlined" rounded="lg" :loading="syncing" @click="syncAccount">
            <v-icon start>mdi-refresh</v-icon>
            Verificar status
          </v-btn>
        </div>
      </div>

      <div v-else-if="status === 'RESTRICTED'" class="text-center py-4">
        <v-icon size="56" color="orange" class="mb-4">mdi-alert-circle-outline</v-icon>
        <h3 class="text-h6 font-weight-bold mb-2">Informações adicionais necessárias</h3>
        <p class="text-medium-emphasis text-body-2 mb-6" style="max-width:340px; margin:auto">
          A Stripe precisa de mais informações para liberar sua conta. Acesse o painel para resolver.
        </p>
        <div class="d-flex gap-3 justify-center">
          <v-btn color="orange" rounded="lg" :loading="loading" @click="startOnboarding" style="color:white">
            <v-icon start>mdi-open-in-new</v-icon>
            Resolver pendências
          </v-btn>
          <v-btn variant="outlined" rounded="lg" :loading="syncing" @click="syncAccount">
            <v-icon start>mdi-refresh</v-icon>
            Atualizar
          </v-btn>
        </div>
      </div>

      <div v-else-if="status === 'ACTIVE'">
        <div class="d-flex align-center gap-3 mb-4">
          <div class="stripe-icon-sm">
            <svg width="32" height="32" viewBox="0 0 48 48" fill="none">
              <rect width="48" height="48" rx="12" fill="#635BFF"/>
              <path d="M22.5 18.5c0-1.1.9-1.5 2.3-1.5 2.1 0 4.7.6 6.7 1.7v-6.3c-2.2-.9-4.5-1.4-6.7-1.4-5.5 0-9.2 2.9-9.2 7.7 0 7.5 10.3 6.3 10.3 9.5 0 1.3-1.1 1.7-2.6 1.7-2.3 0-5.2-.9-7.5-2.2v6.4c2.5 1.1 5.1 1.6 7.5 1.6 5.7 0 9.6-2.8 9.6-7.7-.1-8.1-10.4-6.6-10.4-9.5z" fill="white"/>
            </svg>
          </div>
          <div>
            <p class="font-weight-bold mb-0">Stripe conectada</p>
            <p class="text-caption text-medium-emphasis mb-0">Conta verificada e pronta para receber</p>
          </div>
          <v-spacer/>
          <v-chip color="success" size="small" prepend-icon="mdi-check-circle">Ativa</v-chip>
        </div>

        <v-divider class="mb-4"/>

        <v-row dense>
          <v-col cols="12" sm="4">
            <v-card color="success-lighten-5" rounded="lg" border elevation="0" class="pa-3 text-center">
              <v-icon color="success" class="mb-1">mdi-lightning-bolt</v-icon>
              <p class="text-caption text-medium-emphasis mb-0">Split automático</p>
              <p class="font-weight-bold text-body-2">Ativado</p>
            </v-card>
          </v-col>
          <v-col cols="12" sm="4">
            <v-card color="blue-lighten-5" rounded="lg" border elevation="0" class="pa-3 text-center">
              <v-icon color="blue" class="mb-1">mdi-bank-outline</v-icon>
              <p class="text-caption text-medium-emphasis mb-0">Saques</p>
              <p class="font-weight-bold text-body-2">Habilitados</p>
            </v-card>
          </v-col>
          <v-col cols="12" sm="4">
            <v-card color="purple-lighten-5" rounded="lg" border elevation="0" class="pa-3 text-center">
              <v-icon color="purple" class="mb-1">mdi-shield-check</v-icon>
              <p class="text-caption text-medium-emphasis mb-0">Verificação</p>
              <p class="font-weight-bold text-body-2">Completa</p>
            </v-card>
          </v-col>
        </v-row>

        <div class="d-flex justify-end mt-4">
          <v-btn variant="text" size="small" color="medium-emphasis" :loading="syncing" @click="syncAccount">
            <v-icon start size="14">mdi-refresh</v-icon>
            Sincronizar status
          </v-btn>
        </div>
      </div>

    </v-card-text>
  </v-card>

  <v-snackbar v-model="snackbar" :color="snackbarColor" timeout="3000" location="top">
    {{ snackbarMsg }}
  </v-snackbar>
</template>

<script setup>
import { ref } from 'vue'
import api from '../api/api'

const props = defineProps({
  initialStatus: {
    type: String,
    default: 'NOT_CONNECTED'
  }
})

const emit = defineEmits(['status-changed'])

const status = ref(props.initialStatus)
const loading = ref(false)
const syncing = ref(false)
const snackbar = ref(false)
const snackbarMsg = ref('')
const snackbarColor = ref('success')


async function startOnboarding() {
  try {
    loading.value = true
    const { data } = await api.post('/api/dropper/stripe/onboarding')
    window.location.href = data.url
  } catch (e) {
    showSnackbar('Erro ao conectar com a Stripe', 'error')
  } finally {
    loading.value = false
  }
}

async function syncAccount() {
  try {
    syncing.value = true
    const { data } = await api.post('/api/dropper/stripe/sync')
    status.value = data.status
    emit('status-changed', data)

    if (data.status === 'ACTIVE') {
      showSnackbar('Conta Stripe verificada e ativa!', 'success')
    } else if (data.status === 'PENDING') {
      showSnackbar('Cadastro ainda incompleto na Stripe', 'warning')
    }
  } catch (e) {
    showSnackbar('Erro ao verificar status da Stripe', 'error')
  } finally {
    syncing.value = false
  }
}

function showSnackbar(msg, color = 'success') {
  snackbarMsg.value = msg
  snackbarColor.value = color
  snackbar.value = true
}
</script>
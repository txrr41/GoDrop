<template>
  <div>
    <v-card class="mb-6 overflow-hidden" elevation="0" border>
      <div
          class="pa-8"
          :style="{ background: `linear-gradient(135deg, ${store.primaryColor}, ${store.secondaryColor})` }"
      >
        <div class="d-flex align-center justify-space-between">
          <div>
            <h2 class="text-h4 font-weight-bold text-white">{{ store.storeName }}</h2>
            <p class="text-white mt-1 opacity-80">{{ store.slogan }}</p>
            <v-chip
                class="mt-3"
                :color="store.active ? 'success' : 'error'"
                variant="flat"
                size="small"
            >
              <v-icon start size="12">mdi-circle</v-icon>
              {{ store.active ? 'Loja Online' : 'Loja Offline' }}
            </v-chip>
          </div>
          <div class="d-flex flex-column gap-2">
            <v-btn
                variant="flat"
                color="white"
                :href="`/loja/${store.slug}`"
                target="_blank"
            >
              <v-icon start>mdi-open-in-new</v-icon>
              Ver Loja
            </v-btn>
            <v-btn
                variant="outlined"
                color="white"
                @click="copyStoreLink"
            >
              <v-icon start>mdi-link</v-icon>
              Copiar Link
            </v-btn>
          </div>
        </div>
      </div>

      <v-card-text class="d-flex align-center justify-space-between">
        <div class="d-flex align-center gap-2">
          <v-icon color="medium-emphasis" size="18">mdi-link-variant</v-icon>
          <span class="text-body-2 text-medium-emphasis">
            godrop.com.br/loja/<strong>{{ store.slug }}</strong>
          </span>
        </div>
        <v-switch
            :model-value="store.active"
            @update:model-value="toggleStore"
            color="success"
            density="compact"
            hide-details
            :label="store.active ? 'Online' : 'Offline'"
        />
      </v-card-text>
    </v-card>

    <v-row class="mb-6">
      <v-col cols="12" sm="4">
        <v-card elevation="0" border class="pa-4 text-center">
          <v-icon size="32" color="primary" class="mb-2">mdi-package-variant</v-icon>
          <p class="text-h5 font-weight-bold">{{ store.products?.length ?? 0 }}</p>
          <p class="text-caption text-medium-emphasis">Produtos na loja</p>
        </v-card>
      </v-col>
      <v-col cols="12" sm="4">
        <v-card elevation="0" border class="pa-4 text-center">
          <v-icon size="32" color="success" class="mb-2">mdi-currency-brl</v-icon>
          <p class="text-h5 font-weight-bold">R$ {{ formatMoney(dropperProfile?.totalSales) }}</p>
          <p class="text-caption text-medium-emphasis">Total vendido</p>
        </v-card>
      </v-col>
      <v-col cols="12" sm="4">
        <v-card elevation="0" border class="pa-4 text-center">
          <v-icon size="32" color="warning" class="mb-2">mdi-trophy</v-icon>
          <p class="text-h5 font-weight-bold">{{ dropperProfile?.level }}</p>
          <p class="text-caption text-medium-emphasis">Seu nível atual</p>
        </v-card>
      </v-col>
    </v-row>

    <v-tabs v-model="tab" class="mb-4">
      <v-tab value="products">
        <v-icon start>mdi-package-variant</v-icon>
        Produtos
      </v-tab>
      <v-tab value="appearance">
        <v-icon start>mdi-palette</v-icon>
        Aparência
      </v-tab>
    </v-tabs>

    <v-tabs-window v-model="tab">

      <v-tabs-window-item value="products">
        <v-card elevation="0" border class="pa-4">
          <div class="d-flex align-center justify-space-between mb-4">
            <p class="text-subtitle-1 font-weight-bold">Produtos na sua loja</p>
            <v-btn size="small" variant="outlined" @click="editProductsDialog = true">
              <v-icon start>mdi-pencil</v-icon>
              Gerenciar
            </v-btn>
          </div>

          <v-row>
            <v-col
                v-for="product in store.products"
                :key="product.id"
                cols="6" sm="4" md="3"
            >
              <v-card elevation="1" class="text-center pa-3">
                <v-img
                    :src="product.imagem || 'https://via.placeholder.com/150'"
                    height="80"
                    cover
                    class="rounded mb-2"
                />
                <p class="text-caption font-weight-bold text-truncate">{{ product.nome }}</p>
                <p class="text-caption" :style="{ color: store.primaryColor }">
                  R$ {{ product.preco?.toFixed(2) }}
                </p>
              </v-card>
            </v-col>
          </v-row>
        </v-card>
      </v-tabs-window-item>

      <v-tabs-window-item value="appearance">
        <v-card elevation="0" border class="pa-6">
          <p class="text-subtitle-1 font-weight-bold mb-4">Personalizar visual</p>

          <v-row>
            <v-col cols="12" md="6">
              <v-text-field
                  v-model="editForm.storeName"
                  label="Nome da loja"
                  variant="outlined"
              />
            </v-col>
            <v-col cols="12" md="6">
              <v-text-field
                  v-model="editForm.slogan"
                  label="Slogan"
                  variant="outlined"
              />
            </v-col>
            <v-col cols="12">
              <p class="text-caption text-medium-emphasis mb-2">CORES</p>
              <div class="d-flex gap-4">
                <div>
                  <p class="text-caption mb-1">Primária</p>
                  <input type="color" v-model="editForm.primaryColor"
                         style="width:48px;height:48px;border:none;cursor:pointer;border-radius:8px;" />
                </div>
                <div>
                  <p class="text-caption mb-1">Secundária</p>
                  <input type="color" v-model="editForm.secondaryColor"
                         style="width:48px;height:48px;border:none;cursor:pointer;border-radius:8px;" />
                </div>
                <div>
                  <p class="text-caption mb-1">Fundo</p>
                  <input type="color" v-model="editForm.backgroundColor"
                         style="width:48px;height:48px;border:none;cursor:pointer;border-radius:8px;" />
                </div>
              </div>
            </v-col>

            <v-col cols="12">
              <p class="text-caption text-medium-emphasis mb-2">PREVIEW AO VIVO</p>
              <v-card
                  class="pa-6 text-center"
                  :style="{ backgroundColor: editForm.backgroundColor }"
                  elevation="1"
              >
                <div
                    class="pa-4 rounded mb-4"
                    :style="{ background: `linear-gradient(135deg, ${editForm.primaryColor}, ${editForm.secondaryColor})` }"
                >
                  <p class="text-h6 font-weight-bold text-white">{{ editForm.storeName }}</p>
                  <p class="text-white text-caption">{{ editForm.slogan }}</p>
                </div>
                <v-btn :style="{ backgroundColor: editForm.primaryColor, color: 'white' }">
                  Comprar Agora
                </v-btn>
              </v-card>
            </v-col>
          </v-row>

          <v-btn
              color="primary"
              :loading="storeStore.loading"
              @click="saveAppearance"
              class="mt-4"
          >
            <v-icon start>mdi-check</v-icon>
            Salvar Alterações
          </v-btn>
        </v-card>
      </v-tabs-window-item>

    </v-tabs-window>

    <v-snackbar v-model="snackbar" :color="snackbarColor" timeout="3000">
      {{ snackbarMessage }}
    </v-snackbar>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useStoreStore } from '../stores/store'
import { useDropperStore } from '../stores/dropper'

const storeStore = useStoreStore()
const dropperStore = useDropperStore()

const tab = ref('products')
const snackbar = ref(false)
const snackbarMessage = ref('')
const snackbarColor = ref('success')
const editProductsDialog = ref(false)

const store = computed(() => storeStore.myStore)
const dropperProfile = computed(() => dropperStore.profile)

const editForm = ref({
  storeName: '',
  slogan: '',
  primaryColor: '#6366F1',
  secondaryColor: '#818CF8',
  backgroundColor: '#F8FAFC',
})

function formatMoney(value) {
  return (value ?? 0).toLocaleString('pt-BR', { minimumFractionDigits: 2 })
}

async function toggleStore() {
  await storeStore.toggleStore()
  notify(
      store.value.active ? 'Loja online! 🟢' : 'Loja offline 🔴',
      store.value.active ? 'success' : 'warning'
  )
}

async function saveAppearance() {
  await storeStore.updateStore(editForm.value)
  notify('Aparência salva com sucesso!')
}

function copyStoreLink() {
  const url = `${window.location.origin}/loja/${store.value.slug}`
  navigator.clipboard.writeText(url)
  notify('Link copiado! 📋')
}

function notify(message, color = 'success') {
  snackbarMessage.value = message
  snackbarColor.value = color
  snackbar.value = true
}

onMounted(() => {
  if (store.value) {
    editForm.value = {
      storeName: store.value.storeName,
      slogan: store.value.slogan,
      primaryColor: store.value.primaryColor,
      secondaryColor: store.value.secondaryColor,
      backgroundColor: store.value.backgroundColor,
    }
  }
})
</script>
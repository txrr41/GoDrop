<template>
  <v-container max-width="900" class="py-8">
    <div class="mb-8">
      <h1 class="text-h4 font-weight-bold">🏪 Minha Loja</h1>
      <p class="text-medium-emphasis mt-1">
        Configure sua loja white label e comece a vender com sua identidade
      </p>
    </div>

    <template v-if="!storeStore.hasStore">

      <v-stepper v-model="step" flat>
        <v-stepper-header>
          <v-stepper-item title="Descreva sua loja" :value="1" :complete="step > 1"/>
          <v-divider/>
          <v-stepper-item title="Tema gerado pela IA" :value="2" :complete="step > 2"/>
          <v-divider/>
          <v-stepper-item title="Ajuste e finalize" :value="3"/>
        </v-stepper-header>

        <v-stepper-window>

          <v-stepper-window-item :value="1">
            <v-card flat class="pa-6">
              <div class="text-center mb-6">
                <v-icon size="64" color="primary">mdi-robot-excited-outline</v-icon>
                <h2 class="text-h5 font-weight-bold mt-4">Descreva sua loja para a IA</h2>
                <p class="text-medium-emphasis mt-2">
                  Fale sobre seu nicho, público e estilo. A IA vai criar um tema personalizado.
                </p>
              </div>

              <div class="mb-4">
                <p class="text-caption text-medium-emphasis mb-2">EXEMPLOS DE PROMPTS</p>
                <div class="d-flex flex-wrap gap-2">
                  <v-chip
                      v-for="example in promptExamples"
                      :key="example"
                      variant="outlined"
                      size="small"
                      @click="aiPrompt = example"
                      class="cursor-pointer"
                  >
                    {{ example }}
                  </v-chip>
                </div>
              </div>

              <v-textarea
                  v-model="aiPrompt"
                  label="Descreva sua loja..."
                  placeholder="Ex: Quero uma loja de suplementos fitness com visual moderno e vibrante, focada em jovens atletas..."
                  rows="4"
                  variant="outlined"
                  counter="300"
                  maxlength="300"
              />

              <v-btn
                  color="primary"
                  size="large"
                  block
                  :loading="storeStore.aiLoading"
                  :disabled="aiPrompt.trim().length < 20"
                  @click="generateTheme"
                  class="mt-2"
              >
                <v-icon start>mdi-auto-fix</v-icon>
                Gerar Tema com IA
              </v-btn>
            </v-card>
          </v-stepper-window-item>

          <v-stepper-window-item :value="2">
            <v-card flat class="pa-6" v-if="storeStore.aiTheme">

              <h2 class="text-h5 font-weight-bold mb-1">✨ Tema Gerado!</h2>
              <p class="text-medium-emphasis mb-6">
                {{ storeStore.aiTheme.reasoning }}
              </p>

              <v-card
                  class="mb-6 overflow-hidden"
                  :style="{ backgroundColor: storeStore.aiTheme.backgroundColor }"
                  elevation="2"
              >
                <div
                    class="pa-8 text-center"
                    :style="{ background: `linear-gradient(135deg, ${storeStore.aiTheme.primaryColor}, ${storeStore.aiTheme.secondaryColor})` }"
                >
                  <h2 class="text-h4 font-weight-bold text-white">
                    {{ storeStore.aiTheme.storeName }}
                  </h2>
                  <p class="text-white mt-2 text-subtitle-1">
                    {{ storeStore.aiTheme.slogan }}
                  </p>
                </div>

                <div class="pa-4 d-flex gap-3">
                  <v-card
                      v-for="i in 3"
                      :key="i"
                      class="flex-1"
                      elevation="1"
                  >
                    <div
                        class="pa-6"
                        :style="{ backgroundColor: storeStore.aiTheme.primaryColor + '20' }"
                    />
                    <v-card-text>
                      <p class="font-weight-bold">Produto {{ i }}</p>
                      <p class="text-caption text-medium-emphasis">R$ 99,90</p>
                      <v-btn
                          size="small"
                          block
                          class="mt-2"
                          :style="{ backgroundColor: storeStore.aiTheme.primaryColor, color: 'white' }"
                      >
                        Comprar
                      </v-btn>
                    </v-card-text>
                  </v-card>
                </div>
              </v-card>

              <v-row>
                <v-col cols="6">
                  <p class="text-caption text-medium-emphasis">NOME SUGERIDO</p>
                  <p class="font-weight-bold">{{ storeStore.aiTheme.storeName }}</p>
                </v-col>
                <v-col cols="6">
                  <p class="text-caption text-medium-emphasis">ESTILO</p>
                  <v-chip size="small" :color="themeColor(storeStore.aiTheme.theme)">
                    {{ storeStore.aiTheme.theme }}
                  </v-chip>
                </v-col>
                <v-col cols="12">
                  <p class="text-caption text-medium-emphasis">SLOGAN</p>
                  <p>{{ storeStore.aiTheme.slogan }}</p>
                </v-col>
                <v-col cols="12">
                  <p class="text-caption text-medium-emphasis mb-2">PALETA DE CORES</p>
                  <div class="d-flex gap-3">
                    <div
                        v-for="(color, label) in colorPalette"
                        :key="label"
                        class="text-center"
                    >
                      <div
                          class="rounded-lg mb-1"
                          style="width: 48px; height: 48px;"
                          :style="{ backgroundColor: color }"
                      />
                      <p class="text-caption">{{ label }}</p>
                    </div>
                  </div>
                </v-col>
              </v-row>

              <div class="d-flex gap-3 mt-4">
                <v-btn variant="outlined" @click="step = 1">
                  <v-icon start>mdi-refresh</v-icon>
                  Gerar outro
                </v-btn>
                <v-btn color="primary" flex="1" @click="applyAndNext">
                  Usar esse tema
                  <v-icon end>mdi-arrow-right</v-icon>
                </v-btn>
              </div>
            </v-card>
          </v-stepper-window-item>

          <v-stepper-window-item :value="3">
            <v-card flat class="pa-6">
              <h2 class="text-h5 font-weight-bold mb-6">Ajuste os detalhes</h2>

              <v-row>
                <v-col cols="12" md="6">
                  <v-text-field
                      v-model="form.storeName"
                      label="Nome da Loja *"
                      variant="outlined"
                  />
                </v-col>
                <v-col cols="12" md="6">
                  <v-text-field
                      v-model="form.slogan"
                      label="Slogan"
                      variant="outlined"
                  />
                </v-col>
                <v-col cols="12">
                  <v-textarea
                      v-model="form.description"
                      label="Descrição da loja"
                      rows="3"
                      variant="outlined"
                  />
                </v-col>
                <v-col cols="12">
                  <p class="text-caption text-medium-emphasis mb-2">CORES (clique para alterar)</p>
                  <div class="d-flex gap-4">
                    <div>
                      <p class="text-caption mb-1">Primária</p>
                      <input type="color" v-model="form.primaryColor"
                             style="width:48px; height:48px; border:none; cursor:pointer; border-radius:8px;"/>
                    </div>
                    <div>
                      <p class="text-caption mb-1">Secundária</p>
                      <input type="color" v-model="form.secondaryColor"
                             style="width:48px; height:48px; border:none; cursor:pointer; border-radius:8px;"/>
                    </div>
                    <div>
                      <p class="text-caption mb-1">Fundo</p>
                      <input type="color" v-model="form.backgroundColor"
                             style="width:48px; height:48px; border:none; cursor:pointer; border-radius:8px;"/>
                    </div>
                  </div>
                </v-col>

                <v-col cols="12">
                  <p class="text-subtitle-2 font-weight-bold mb-2">Produtos na sua loja</p>
                  <p class="text-caption text-medium-emphasis mb-3">
                    Selecione quais produtos você quer vender
                  </p>
                  <div class="d-flex flex-wrap gap-2">
                    <v-chip
                        v-for="product in productStore.products"
                        :key="product.id"
                        :variant="form.productIds.includes(product.id) ? 'flat' : 'outlined'"
                        :color="form.productIds.includes(product.id) ? 'primary' : undefined"
                        @click="toggleProduct(product.id)"
                        class="cursor-pointer"
                    >
                      {{ product.nome }}
                    </v-chip>
                  </div>
                </v-col>
              </v-row>

              <v-btn
                  color="primary"
                  size="large"
                  block
                  :loading="storeStore.loading"
                  :disabled="!form.storeName"
                  @click="createStore"
                  class="mt-4"
              >
                <v-icon start>mdi-check</v-icon>
                Criar Minha Loja
              </v-btn>
            </v-card>
          </v-stepper-window-item>

        </v-stepper-window>
      </v-stepper>
    </template>

    <template v-else>
      <DropperStoreDashboard/>
    </template>

  </v-container>
</template>

<script setup>
import {ref, computed, onMounted} from 'vue'
import {useStoreStore} from '../stores/store'
import {useProductStore} from '../stores/product'
import DropperStoreDashboard from '../pages/DropperStoreDashboard.vue'

const storeStore = useStoreStore()
const productStore = useProductStore()

const step = ref(1)
const aiPrompt = ref('')

const form = ref({
  storeName: '',
  slogan: '',
  description: '',
  primaryColor: '#6366F1',
  secondaryColor: '#818CF8',
  backgroundColor: '#F8FAFC',
  theme: 'MODERN',
  productIds: [],
})

const promptExamples = [
  'Loja de roupas femininas elegante e sofisticada',
  'Produtos fitness e suplementos com visual vibrante',
  'Eletrônicos e gadgets com estilo tech moderno',
  'Cosméticos naturais e orgânicos com visual clean',
  'Loja infantil colorida e divertida',
]

const colorPalette = computed(() => ({
  'Primária': storeStore.aiTheme?.primaryColor,
  'Secundária': storeStore.aiTheme?.secondaryColor,
  'Fundo': storeStore.aiTheme?.backgroundColor,
}))

function themeColor(theme) {
  const map = {MODERN: 'primary', MINIMAL: 'grey', BOLD: 'error', ELEGANT: 'purple'}
  return map[theme] || 'primary'
}

function toggleProduct(id) {
  const idx = form.value.productIds.indexOf(id)
  if (idx === -1) form.value.productIds.push(id)
  else form.value.productIds.splice(idx, 1)
}

async function generateTheme() {
  await storeStore.generateAiTheme(aiPrompt.value)
  step.value = 2
}

function applyAndNext() {
  const ai = storeStore.aiTheme
  form.value.storeName = ai.storeName
  form.value.slogan = ai.slogan
  form.value.primaryColor = ai.primaryColor
  form.value.secondaryColor = ai.secondaryColor
  form.value.backgroundColor = ai.backgroundColor
  form.value.theme = ai.theme
  form.value.description = ai.description
  step.value = 3
}

async function createStore() {
  await storeStore.createStore({
    ...form.value,
    aiPrompt: aiPrompt.value,
  })
}

onMounted(async () => {
  await storeStore.fetchMyStore()
  await productStore.fetchProducts()
})
</script>
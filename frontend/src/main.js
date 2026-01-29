import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { createVuetify } from 'vuetify'
import 'vuetify/styles'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import '@mdi/font/css/materialdesignicons.css'
import App from './App.vue'
import router from './router'
import { useAuthStore } from './stores/auth'
import { vMaska} from "maska/vue";

const vuetify = createVuetify({
    components,
    directives,
})

const pinia = createPinia()
const app = createApp(App)

app.use(vuetify)
app.use(pinia)
app.use(router)
app.directive('maska', vMaska)

const authStore = useAuthStore()
authStore.init().finally(() => {
    app.mount('#app')
})
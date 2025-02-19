import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from '@/router'
import { createVuetify } from 'vuetify'
import { aliases, mdi } from 'vuetify/iconsets/mdi'
import { mdiIconAliases } from '@jsonforms/vue-vuetify'
import '@mdi/font/css/materialdesignicons.css';
import 'vuetify/styles'

const app = createApp(App)

app.use(createPinia())
app.use(createVuetify({
  icons: {
    defaultSet: 'mdi',
    sets: {
      mdi,
    },
    aliases: { ...aliases, ...mdiIconAliases },
  },
}))
app.use(router)

app.mount('#app')

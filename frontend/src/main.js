import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios'
import './tailwind.css'
//import './assets/styles/admin.css'
//import './assets/styles/buttons.css'
//import './assets/styles/forms.css'
//import './assets/styles/tables.css'
//import './assets/styles/badges.css'
//import './assets/styles/cards.css'
//import './assets/styles/animations.css'
//import './assets/styles/layout.css'

axios.defaults.baseURL = 'http://localhost:8080'

const app = createApp(App)
app.use(router)
app.mount('#app')

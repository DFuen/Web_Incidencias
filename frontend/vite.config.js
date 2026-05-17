import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import tailwindcss from '@tailwindcss/vite'

export default defineConfig({
  plugins: [vue()],
  preview: {
    allowedHosts: [
      'webincidencias-production.up.railway.app'
    ]
  }
})

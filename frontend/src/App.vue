<template>
  <div>
    <header
      v-if="isAuthenticated && route.path !== '/'"
      class="flex items-center justify-end gap-3 border-b border-slate-200 bg-white px-5 py-3"
    >
      <span class="text-sm text-slate-700">{{ currentUser }}</span>
      <LogoutButton @click="logout" />
    </header>

    <router-view />
  </div>
</template>

<script setup>
import { onBeforeUnmount, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import LogoutButton from './components/auth/LogoutButton.vue'

const route = useRoute()
const router = useRouter()

const isAuthenticated = ref(!!localStorage.getItem('auth'))
const currentUser = ref(localStorage.getItem('nombre') || '')

const syncSessionState = () => {
  isAuthenticated.value = !!localStorage.getItem('auth')
  currentUser.value = localStorage.getItem('nombre') || ''
}

const onAuthChanged = () => {
  syncSessionState()
}

onMounted(() => {
  syncSessionState()
  window.addEventListener('auth-changed', onAuthChanged)
  window.addEventListener('storage', onAuthChanged)
})

onBeforeUnmount(() => {
  window.removeEventListener('auth-changed', onAuthChanged)
  window.removeEventListener('storage', onAuthChanged)
})

const logout = () => {
  localStorage.removeItem('auth')
  localStorage.removeItem('user')
  localStorage.removeItem('nombre')
  localStorage.removeItem('rol')
  window.dispatchEvent(new Event('auth-changed'))
  router.push('/')
}
</script>

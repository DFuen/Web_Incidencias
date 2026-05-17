<template>
  <div>
    <header
      v-if="mostrarHeader"
      class="flex items-center justify-end gap-3 border-b border-slate-200 bg-white px-5 py-3"
    >
      <span class="text-sm font-medium text-slate-700">
        {{ currentUser }}
      </span>

      <LogoutButton @click="logout" />
    </header>

    <router-view />
  </div>
</template>

<script setup>
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import LogoutButton from './components/auth/LogoutButton.vue'

const route = useRoute()
const router = useRouter()

const isAuthenticated = ref(false)
const currentUser = ref('')

const mostrarHeader = computed(() => {
  return isAuthenticated.value && route.path !== '/'
})

const syncSessionState = () => {
  isAuthenticated.value = !!localStorage.getItem('auth')
  currentUser.value = localStorage.getItem('nombre') || localStorage.getItem('user') || ''
}

const clearSession = () => {
  localStorage.removeItem('auth')
  localStorage.removeItem('user')
  localStorage.removeItem('nombre')
  localStorage.removeItem('rol')
}

const logout = () => {
  clearSession()
  syncSessionState()
  window.dispatchEvent(new Event('auth-changed'))
  router.push('/')
}

onMounted(() => {
  syncSessionState()
  window.addEventListener('auth-changed', syncSessionState)
  window.addEventListener('storage', syncSessionState)
})

onBeforeUnmount(() => {
  window.removeEventListener('auth-changed', syncSessionState)
  window.removeEventListener('storage', syncSessionState)
})
</script>
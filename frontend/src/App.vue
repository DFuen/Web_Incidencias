<template>
  <div>
    <header v-if="isAuthenticated && route.path !== '/'" class="topbar">
      <span class="user-label">{{ currentUser }}</span>
      <button class="logout-btn" @click="logout">Cerrar sesion</button>
    </header>

    <router-view />
  </div>
</template>

<script setup>
import { onBeforeUnmount, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const isAuthenticated = ref(!!localStorage.getItem('auth'))
const currentUser = ref(localStorage.getItem('user') || '')

const syncSessionState = () => {
  isAuthenticated.value = !!localStorage.getItem('auth')
  currentUser.value = localStorage.getItem('user') || ''
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
  localStorage.removeItem('rol')
  window.dispatchEvent(new Event('auth-changed'))
  router.push('/')
}
</script>

<style scoped>
.topbar {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 12px;
  padding: 12px 20px;
  border-bottom: 1px solid #e5e7eb;
  background: #ffffff;
}

.user-label {
  color: #374151;
  font-size: 0.95rem;
}

.logout-btn {
  border: none;
  background: #dc2626;
  color: #fff;
  padding: 8px 14px;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
}

.logout-btn:hover {
  background: #b91c1c;
}
</style>

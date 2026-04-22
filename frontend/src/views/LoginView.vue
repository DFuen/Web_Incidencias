<template>
  <div class="min-h-screen bg-[radial-gradient(circle_at_30%_10%,#dbeafe,transparent_35%),radial-gradient(circle_at_80%_20%,#bbf7d0,transparent_40%),linear-gradient(180deg,#f8fafc,#e2e8f0)] px-4 py-10">
    <div class="mx-auto max-w-md rounded-3xl border border-slate-200 bg-white/95 p-8 shadow-xl backdrop-blur-sm">
      <h2 class="mb-6 text-center text-3xl font-bold tracking-tight text-slate-900">Iniciar sesion</h2>
      <form @submit.prevent="login" class="space-y-4">
        <div>
          <input
            v-model="email"
            type="email"
            placeholder="Email"
            @blur="validateEmail"
            class="w-full rounded-xl border border-slate-300 px-4 py-2.5 text-sm text-slate-800 outline-none transition focus:border-sky-500 focus:ring-2 focus:ring-sky-200"
          />
          <span v-if="emailError" class="mt-1 block text-xs font-medium text-rose-600">{{ emailError }}</span>
        </div>

        <div>
          <input
            v-model="password"
            type="password"
            placeholder="Contrasena"
            @blur="validatePassword"
            class="w-full rounded-xl border border-slate-300 px-4 py-2.5 text-sm text-slate-800 outline-none transition focus:border-sky-500 focus:ring-2 focus:ring-sky-200"
          />
          <span v-if="passwordError" class="mt-1 block text-xs font-medium text-rose-600">{{ passwordError }}</span>
        </div>

        <button
          type="submit"
          class="w-full rounded-xl bg-sky-600 px-4 py-2.5 text-sm font-semibold text-white shadow-sm transition hover:bg-sky-700"
        >
          Entrar
        </button>

        <p v-if="error" class="rounded-lg border border-rose-200 bg-rose-50 px-3 py-2 text-sm text-rose-700">{{ error }}</p>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const email = ref('')
const password = ref('')
const error = ref('')
const emailError = ref('')
const passwordError = ref('')
const router = useRouter()

const validateEmail = () => {
  if (!email.value) {
    emailError.value = 'El email es obligatorio'
  } else if (!/^\S+@\S+\.\S+$/.test(email.value)) {
    emailError.value = 'El email no es válido'
  } else {
    emailError.value = ''
  }
}

const validatePassword = () => {
  if (!password.value) {
    passwordError.value = 'La contraseña es obligatoria'
  } else if (password.value.length < 4) {
    passwordError.value = 'La contraseña debe tener al menos 4 caracteres'
  } else {
    passwordError.value = ''
  }
}

const login = async () => {
  error.value = ''
  validateEmail()
  validatePassword()
  if (emailError.value || passwordError.value) return
  try {
    // Construir la cabecera Authorization básica
    const basicAuth = 'Basic ' + btoa(email.value + ':' + password.value)
    const response = await axios.get('/api/usuarios/email/' + email.value, {
      headers: {
        'Authorization': basicAuth
      }
    })
    const usuario = response.data
    localStorage.setItem('user', email.value)
    localStorage.setItem('auth', btoa(email.value + ':' + password.value))
    localStorage.setItem('rol', usuario.rol)
    window.dispatchEvent(new Event('auth-changed'))
    if (usuario.rol === 'administrador') {
      router.push('/admin')
    } else {
      router.push('/incidencias')
    }
  } catch (e) {
    error.value = 'Credenciales incorrectas'
  }
}
</script>

<template>
  <div class="flex min-h-screen items-center justify-center bg-gradient-to-br from-sky-100 via-white to-emerald-100 px-4">
    
    <LoginForm
      :email="email"
      :password="password"
      :error="error"
      :email-error="emailError"
      :password-error="passwordError"
      @update:email="email = $event"
      @update:password="password = $event"
      @submit="login"
    />

  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import LoginForm from '../components/auth/LoginForm.vue'

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
    localStorage.setItem('nombre', usuario.nombre)
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

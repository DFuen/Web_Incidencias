<template>
  <div class="login-container">
    <h2>Iniciar sesión</h2>
    <form @submit.prevent="login">
      <input v-model="email" type="email" placeholder="Email" @blur="validateEmail" />
      <span v-if="emailError" class="error">{{ emailError }}</span>
      <input v-model="password" type="password" placeholder="Contraseña" @blur="validatePassword" />
      <span v-if="passwordError" class="error">{{ passwordError }}</span>
      <button type="submit">Entrar</button>
      <p v-if="error" class="error">{{ error }}</p>
    </form>
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

<style scoped>
.login-container { max-width: 350px; margin: 60px auto; padding: 2em; border: 1px solid #ccc; border-radius: 8px; }
input { display: block; width: 100%; margin-bottom: 1em; padding: 0.5em; }
button { width: 100%; padding: 0.7em; }
.error { color: red; font-size: 0.95em; }
</style>

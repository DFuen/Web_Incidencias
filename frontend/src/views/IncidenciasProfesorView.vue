<template>
  <div class="min-h-screen bg-gradient-to-b from-slate-50 via-white to-cyan-50 px-4 py-6 sm:px-6">
    <div class="mx-auto max-w-6xl space-y-6">
      <header>
        <h1 class="border-b-4 border-emerald-600 pb-3 text-xl font-bold tracking-tight text-slate-900 sm:text-2xl">
          Mis Incidencias
        </h1>

        <p class="mt-2 text-sm text-slate-600">
          Crea nuevas incidencias y consulta el historial de las que has registrado.
        </p>
      </header>

      <p
        v-if="formError"
        class="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700"
      >
        {{ formError }}
      </p>

      <CreateIncidenciaForm
        :ubicaciones="ubicaciones"
        :categorias="categorias"
        @created="recargarIncidencias"
      />

      <HistoricoTable
        :key="historialKey"
        :incidencias="incidencias"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

import HistoricoTable from '../components/incidencias/HistoricoTable.vue'
import CreateIncidenciaForm from '../components/incidencias/CreateIncidenciaForm.vue'

const incidencias = ref([])
const ubicaciones = ref([])
const categorias = ref([])
const formError = ref('')
const loading = ref(false)
const historialKey = ref(0)

const getAuthHeaders = () => {
  const auth = localStorage.getItem('auth')

  return {
    Authorization: 'Basic ' + auth
  }
}

const normalizarRespuestaArray = (data) => {
  if (Array.isArray(data)) return data

  if (Array.isArray(data?.content)) return data.content

  return []
}

const recargarIncidencias = async () => {
  await fetchData()
  historialKey.value++
}

const fetchData = async () => {
  loading.value = true
  formError.value = ''

  try {
    const email = localStorage.getItem('user')

    const user = await axios.get('/api/usuarios/email/' + email, {
      headers: getAuthHeaders()
    })

    const [
      incidenciasRes,
      ubicacionesRes,
      categoriasRes
    ] = await Promise.all([
      axios.get('/api/incidencias?usuarioId=' + user.data.id, {
        headers: getAuthHeaders()
      }),

      axios.get('/api/ubicaciones', {
        headers: getAuthHeaders()
      }),

      axios.get('/api/categorias', {
        headers: getAuthHeaders()
      })
    ])

    incidencias.value = [
      ...normalizarRespuestaArray(incidenciasRes.data)
    ]

    ubicaciones.value = [
      ...normalizarRespuestaArray(ubicacionesRes.data)
    ]

    categorias.value = [
      ...normalizarRespuestaArray(categoriasRes.data)
    ]
  } catch (error) {
    console.error('ERROR:', error)
    formError.value = 'Error al cargar datos'
  } finally {
    loading.value = false
  }
}

onMounted(fetchData)
</script>
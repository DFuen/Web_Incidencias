<template>
  <div class="min-h-screen bg-gradient-to-b from-slate-50 via-white to-cyan-50 px-4 py-6 sm:px-6">
    <div class="mx-auto max-w-6xl space-y-6">
      <h1 class="border-b-4 border-emerald-600 pb-3 text-xl font-bold tracking-tight text-slate-900 sm:text-2xl">
        Mis Incidencias
      </h1>

      <CreateIncidenciaForm
        :ubicaciones="ubicaciones"
        :categorias="categorias"
        @created="fetchData"
      />

      <HistoricoTable :incidencias="incidencias" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import LogoutButton from "../components/auth/LogoutButton.vue";
import EstadoBadge from '../components/incidencias/EstadoBadge.vue'
import HistoricoTable from '../components/incidencias/HistoricoTable.vue'
import CreateIncidenciaForm from '../components/incidencias/CreateIncidenciaForm.vue'

const incidencias = ref([])
const ubicaciones = ref([])
const categorias = ref([])
const formError = ref('')

const fetchData = async () => {
  try {
    const auth = localStorage.getItem('auth')
    const email = localStorage.getItem('user')

    // USUARIO
    const user = await axios.get('/api/usuarios/email/' + email, {
      headers: {
        Authorization: 'Basic ' + auth
      }
    })

    // INCIDENCIAS
    const incidenciasRes = await axios.get(
      '/api/incidencias?usuarioId=' + user.data.id,
      {
        headers: {
          Authorization: 'Basic ' + auth
        }
      }
    )

    incidencias.value =
      incidenciasRes.data.content || incidenciasRes.data

    // UBICACIONES
    const ubicacionesRes = await axios.get('/api/ubicaciones')
    ubicaciones.value = ubicacionesRes.data

    // CATEGORIAS
    const categoriasRes = await axios.get('/api/categorias')
    categorias.value = categoriasRes.data

  } catch (error) {
    console.error('ERROR:', error)
    formError.value = 'Error al cargar datos'
  }
}

onMounted(fetchData)
</script>
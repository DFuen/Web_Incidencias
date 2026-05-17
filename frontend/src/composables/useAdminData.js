import { ref } from 'vue'
import axios from 'axios'

export const useAdminData = () => {
  const todasIncidencias = ref([])
  const categorias = ref([])
  const ubicaciones = ref([])
  const profesores = ref([])

  const dashboard = ref({
    abiertas: 0,
    resueltas: 0,
    tiempoMedioHoras: 0,
    topUbicaciones: [],
    topCategorias: []
  })

  const fetchData = async () => {
    try {
      const auth = localStorage.getItem('auth')

      const res = await axios.get('/api/incidencias?size=1000', {
        headers: {
          Authorization: 'Basic ' + auth
        }
      })

      todasIncidencias.value = res.data.content || res.data

      const categoriasRes = await axios.get('/api/categorias', {
        headers: {
          Authorization: 'Basic ' + auth
        }
      })

      categorias.value = categoriasRes.data

      const dashboardRes = await axios.get('/api/incidencias/dashboard', {
        headers: {
          Authorization: 'Basic ' + auth
        }
      })

      dashboard.value = dashboardRes.data

      const ubicacionesRes = await axios.get('/api/ubicaciones', {
        headers: {
          Authorization: 'Basic ' + auth
        }
      })

      ubicaciones.value = ubicacionesRes.data

      const profesoresRes = await axios.get('/api/usuarios', {
        headers: {
          Authorization: 'Basic ' + auth
        }
      })

      profesores.value = profesoresRes.data
    } catch (error) {
      console.error('Error al cargar datos:', error)
    }
  }

  return {
    todasIncidencias,
    categorias,
    ubicaciones,
    profesores,
    dashboard,
    fetchData
  }
}
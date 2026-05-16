<template>
  <div class="min-h-[calc(100vh-80px)] bg-gradient-to-b from-slate-50 via-white to-slate-100 px-4 py-6 sm:px-6">
    <div class="mx-auto flex max-w-[1800px] flex-col gap-6">
      <aside class="lg:h-fit">
        <AdminMenu
          :menu-expanded="menuExpanded"
          :active-admin-view="activeAdminView"
          :admin-menu="adminMenu"
          @toggle-menu="menuExpanded = !menuExpanded"
          @change-view="activeAdminView = $event"
        />
      </aside>

      <main class="min-w-0 flex-1 space-y-8">
        <DashboardView
          v-if="activeAdminView === 'dashboard'"
          :dashboard="dashboard"
          :ultimas-incidencias="ultimasIncidencias"
          :todas-incidencias="todasIncidencias"
          :foto-error-map="fotoErrorMap"
          :get-foto-url="getFotoUrl"
          :abrir-foto="abrirFoto"
          :abrir-descripcion="abrirDescripcion"
          :cambiar-estado="cambiarEstado"
          :format-fecha="formatFecha"
          :tiempo-medio-horas="tiempoMedioHoras"
          :tiempo-medio-arco="tiempoMedioArco"
          :tiempo-medio-circunferencia="tiempoMedioCircunferencia"
          :tiempo-medio-angulo="tiempoMedioAngulo"
          :category-stats="categoryStats"
          :category-gradient="categoryGradient"
        />

        <GestionView
          v-else-if="activeAdminView === 'gestion'"
          :ubicaciones="ubicaciones"
          :profesores="profesores"
          @refresh="fetchData"
        />

        <section
          v-else-if="activeAdminView === 'incidencias'"
          class="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm sm:p-6"
        >
          <h2 class="text-xl font-semibold text-slate-800">
            Listado de Incidencias
          </h2>

          <IncidenciaFilters
            :filtros="filtros"
            :categorias="categorias"
            @limpiar="limpiarFiltros"
          />

          <div
            v-if="incidenciasFilteradas.length === 0"
            class="mt-4 rounded-xl border border-dashed border-slate-300 bg-slate-50 p-10 text-center text-slate-600"
          >
            <p>No hay incidencias que coincidan con los filtros aplicados.</p>
          </div>

          <div v-else class="mt-4">
            <div class="block space-y-4 md:hidden">
              <IncidenciaCard
                v-for="inc in incidenciasFilteradas"
                :key="inc.id"
                :incidencia="inc"
                :foto-error-map="fotoErrorMap"
                :get-foto-url="getFotoUrl"
                :abrir-foto="abrirFoto"
                :abrir-descripcion="abrirDescripcion"
                :cambiar-estado="cambiarEstado"
                :format-fecha="formatFecha"
              />
            </div>

            <IncidenciaTable
              :incidencias="incidenciasFilteradas"
              :items-por-pagina="itemsPorPagina"
              :foto-error-map="fotoErrorMap"
              :get-foto-url="getFotoUrl"
              :abrir-foto="abrirFoto"
              :abrir-descripcion="abrirDescripcion"
              :cambiar-estado="cambiarEstado"
              :format-fecha="formatFecha"
            />
          </div>
        </section>

        <section
          v-else-if="activeAdminView === 'estadisticas'"
          class="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm sm:p-6"
        >
          <MetricasDashboard
            :dashboard="dashboard"
            :total-incidencias="todasIncidencias.length"
            :tiempo-medio-horas="tiempoMedioHoras"
            :tiempo-medio-arco="tiempoMedioArco"
            :tiempo-medio-circunferencia="tiempoMedioCircunferencia"
            :tiempo-medio-angulo="tiempoMedioAngulo"
            :category-stats="categoryStats"
            :category-gradient="categoryGradient"
            :ubicacion-stats="ubicacionStats"
            :mostrar-ubicaciones="true"
          />
        </section>
      </main>
    </div>
  </div>

  <VistaAmpliadaModal
    :visible="vistaAmpliadaVisible"
    :tipo="vistaAmpliadaTipo"
    :contenido="vistaAmpliadaContenido"
    :error="vistaAmpliadaError"
    @close="cerrarVistaAmpliada"
    @image-error="vistaAmpliadaError = true"
  />

  <CerrarIncidenciaModal
    :visible="modalCerrarVisible"
    @close="modalCerrarVisible = false"
    @confirm="confirmarCerrarIncidencia"
  />
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

import AdminMenu from '../components/incidencias/AdminMenu.vue'
import IncidenciaTable from '../components/incidencias/IncidenciaTable.vue'
import IncidenciaFilters from '../components/incidencias/IncidenciaFilters.vue'
import VistaAmpliadaModal from '../components/incidencias/VistaAmpliadaModal.vue'
import IncidenciaCard from '../components/incidencias/IncidenciaCard.vue'
import CerrarIncidenciaModal from '../components/incidencias/CerrarIncidenciaModal.vue'

import DashboardView from '../components/admin/DashboardView.vue'
import GestionView from '../components/admin/GestionView.vue'
import MetricasDashboard from '../components/admin/MetricasDashboard.vue'

const todasIncidencias = ref([])
const categorias = ref([])
const ubicaciones = ref([])
const profesores = ref([])

const filtros = ref({
  categoriaId: '',
  estado: '',
  fechaDesde: '',
  fechaHasta: ''
})

const dashboard = ref({
  abiertas: 0,
  resueltas: 0,
  tiempoMedioHoras: 0,
  topUbicaciones: [],
  topCategorias: []
})

const activeAdminView = ref('dashboard')
const menuExpanded = ref(true)

const vistaAmpliadaVisible = ref(false)
const vistaAmpliadaTipo = ref('foto')
const vistaAmpliadaContenido = ref('')
const vistaAmpliadaError = ref(false)

const fotoErrorMap = ref({})
const itemsPorPagina = ref(5)

const modalCerrarVisible = ref(false)
const incidenciaSeleccionada = ref(null)

const adminMenu = [
  { id: 'dashboard', label: 'Inicio' },
  { id: 'incidencias', label: 'Todas las incidencias' },
  { id: 'estadisticas', label: 'Métricas' },
  { id: 'gestion', label: 'Gestión' }
]

const incidenciasFilteradas = computed(() => {
  return todasIncidencias.value.filter((inc) => {
    let cumple = true

    if (filtros.value.categoriaId) {
      cumple = cumple && inc.categoria.id == filtros.value.categoriaId
    }

    if (filtros.value.estado) {
      cumple = cumple && inc.estado === filtros.value.estado
    }

    if (filtros.value.fechaDesde) {
      const fechaIncidencia = new Date(inc.fechaCreacion)
      const fechaDesde = new Date(filtros.value.fechaDesde)

      cumple = cumple && fechaIncidencia >= fechaDesde
    }

    if (filtros.value.fechaHasta) {
      const fechaIncidencia = new Date(inc.fechaCreacion)
      const fechaHasta = new Date(filtros.value.fechaHasta)

      fechaHasta.setHours(23, 59, 59, 999)

      cumple = cumple && fechaIncidencia <= fechaHasta
    }

    return cumple
  })
})

const ultimasIncidencias = computed(() => {
  return [...todasIncidencias.value]
    .sort((a, b) => new Date(b.fechaCreacion) - new Date(a.fechaCreacion))
    .slice(0, 3)
})

const tiempoMedioHoras = computed(() => {
  const raw = Number(dashboard.value.tiempoMedioHoras)
  return Number.isFinite(raw) && raw >= 0 ? raw : 0
})

const maxHorasReloj = 72
const radioReloj = 92
const tiempoMedioCircunferencia = 2 * Math.PI * radioReloj

const tiempoMedioHorasEscaladas = computed(() => {
  return Math.min(Math.max(tiempoMedioHoras.value, 0), maxHorasReloj)
})

const tiempoMedioArco = computed(() => {
  return (tiempoMedioHorasEscaladas.value / maxHorasReloj) * tiempoMedioCircunferencia
})

const tiempoMedioAngulo = computed(() => {
  const horasEscaladas = tiempoMedioHorasEscaladas.value
  return horasEscaladas * (360 / maxHorasReloj)
})

const categoryPalette = [
  '#0ea5e9',
  '#22c55e',
  '#f59e0b',
  '#a855f7',
  '#ef4444',
  '#14b8a6',
  '#f97316',
  '#6366f1'
]

const categoryStats = computed(() => {
  const total = todasIncidencias.value.length

  if (!total) return []

  const countByCategory = todasIncidencias.value.reduce((acc, inc) => {
    const nombre = inc?.categoria?.nombre || 'Sin categoria'
    acc[nombre] = (acc[nombre] || 0) + 1
    return acc
  }, {})

  return Object.entries(countByCategory)
    .sort((a, b) => b[1] - a[1])
    .map(([nombre, count], index) => {
      const percent = (count / total) * 100

      return {
        nombre,
        count,
        percent,
        percentLabel: `${percent.toFixed(1)}%`,
        color: categoryPalette[index % categoryPalette.length]
      }
    })
})

const ubicacionStats = computed(() => {
  const total = todasIncidencias.value.length

  if (!total) return []

  const countByUbicacion = todasIncidencias.value.reduce((acc, inc) => {
    const codigo = inc?.ubicacion?.codigo || 'Sin código'
    const nombre = inc?.ubicacion?.nombre || 'Sin ubicación'
    const key = `${codigo} - ${nombre}`

    acc[key] = (acc[key] || 0) + 1

    return acc
  }, {})

  return Object.entries(countByUbicacion)
    .sort((a, b) => b[1] - a[1])
    .map(([nombre, count]) => ({
      nombre,
      count,
      percentLabel: `${((count / total) * 100).toFixed(1)}%`
    }))
})

const categoryGradient = computed(() => {
  if (!categoryStats.value.length) {
    return '#e2e8f0 0 100%'
  }

  let acumulado = 0

  const segmentos = categoryStats.value.map((item) => {
    const inicio = acumulado
    acumulado += item.percent
    return `${item.color} ${inicio.toFixed(2)}% ${acumulado.toFixed(2)}%`
  })

  return segmentos.join(', ')
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

onMounted(fetchData)

const limpiarFiltros = () => {
  filtros.value = {
    categoriaId: '',
    estado: '',
    fechaDesde: '',
    fechaHasta: ''
  }
}

const cambiarEstado = async (incidencia, nuevoEstado) => {
  if (incidencia.estado === 'REALIZADA') {
    alert('No se puede modificar una incidencia ya realizada')
    return
  }

  if (nuevoEstado === 'REALIZADA') {
    incidenciaSeleccionada.value = incidencia
    modalCerrarVisible.value = true
    return
  }

  await actualizarEstado(incidencia, nuevoEstado)
}

const actualizarEstado = async (
  incidencia,
  nuevoEstado,
  descripcionSolucion = null
) => {
  try {
    const auth = localStorage.getItem('auth')

    await axios.put(
      `/api/incidencias/${incidencia.id}/estado`,
      {
        estado: nuevoEstado,
        descripcionSolucion
      },
      {
        headers: {
          Authorization: 'Basic ' + auth
        }
      }
    )

    await fetchData()
  } catch (error) {
    console.error('Error al cambiar estado:', error)
    alert('Error al cambiar el estado de la incidencia')
  }
}

const confirmarCerrarIncidencia = async (descripcion) => {
  await actualizarEstado(
    incidenciaSeleccionada.value,
    'REALIZADA',
    descripcion
  )

  modalCerrarVisible.value = false
  incidenciaSeleccionada.value = null
}

const formatFecha = (fecha) => {
  return new Date(fecha).toLocaleString('es-ES')
}

const getFotoUrl = (fotoPath) => {
  if (!fotoPath) return '#'

  if (fotoPath.startsWith('http://') || fotoPath.startsWith('https://')) {
    return fotoPath
  }

  return fotoPath
}

const abrirFoto = (fotoPath) => {
  vistaAmpliadaTipo.value = 'foto'
  vistaAmpliadaContenido.value = getFotoUrl(fotoPath)
  vistaAmpliadaError.value = false
  vistaAmpliadaVisible.value = true
}

const abrirDescripcion = (descripcion) => {
  vistaAmpliadaTipo.value = 'descripcion'
  vistaAmpliadaContenido.value = descripcion
  vistaAmpliadaVisible.value = true
}

const cerrarVistaAmpliada = () => {
  vistaAmpliadaVisible.value = false
  vistaAmpliadaContenido.value = ''
  vistaAmpliadaError.value = false
}
</script>
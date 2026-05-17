<template>
  <div class="min-h-[calc(100vh-80px)] bg-gradient-to-b from-slate-50 via-white to-slate-100 px-4 py-6 sm:px-6">
    <div class="mx-auto flex max-w-[1800px] flex-col gap-6">
      <aside class="lg:h-fit">
        <!-- Menú lateral de administración -->
        <AdminMenu
          :menu-expanded="menuExpanded"
          :active-admin-view="activeAdminView"
          :admin-menu="adminMenu"
          @toggle-menu="menuExpanded = !menuExpanded"
          @change-view="activeAdminView = $event"
        />
      </aside>

      <main class="min-w-0 flex-1 space-y-8">
        <!-- Vista de inicio con métricas clave y últimas incidencias -->
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
        <!-- Vista de inicio con métricas clave y últimas incidencias --> 
        <GestionView
          v-else-if="activeAdminView === 'gestion'"
          :ubicaciones="ubicaciones"
          :profesores="profesores"
          @refresh="fetchData"
        />
        <!-- Vista de gestión de ubicaciones y profesores -->
        <ListadoIncidenciasAdmin
          v-else-if="activeAdminView === 'incidencias'"
          :incidencias="incidenciasFilteradas"
          :categorias="categorias"
          :filtros="filtros"
          :items-por-pagina="itemsPorPagina"
          :foto-error-map="fotoErrorMap"
          :get-foto-url="getFotoUrl"
          :abrir-foto="abrirFoto"
          :abrir-descripcion="abrirDescripcion"
          :cambiar-estado="cambiarEstado"
          :format-fecha="formatFecha"
          @limpiar="limpiarFiltros"
        />

        <section
          v-else-if="activeAdminView === 'estadisticas'"
          class="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm sm:p-6"
        >
          <!-- Vista de métricas y estadísticas -->
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

  <!-- Modales para vista ampliada de foto/descripcion y cierre de incidencia -->
  <AdminModals
    :vista-ampliada-visible="vistaAmpliadaVisible"
    :vista-ampliada-tipo="vistaAmpliadaTipo"
    :vista-ampliada-contenido="vistaAmpliadaContenido"
    :vista-ampliada-error="vistaAmpliadaError"
    :modal-cerrar-visible="modalCerrarVisible"
    @cerrar-vista-ampliada="cerrarVistaAmpliada"
    @image-error="vistaAmpliadaError = true"
    @cerrar-modal-cierre="modalCerrarVisible = false"
    @confirmar-cierre="confirmarCerrarIncidencia"
  />
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

//COMPOSABLES
import { useAdminMetricas } from '../composables/useAdminMetricas'
import { useAdminData } from '../composables/useAdminData'
import { useIncidenciaActions } from '../composables/useIncidenciaActions'
import { useIncidenciaFilters } from '../composables/useIncidenciaFilters'

//COMPONENTES
import AdminMenu from '../components/incidencias/AdminMenu.vue'
import DashboardView from '../components/admin/DashboardView.vue'
import GestionView from '../components/admin/GestionView.vue'
import MetricasDashboard from '../components/admin/MetricasDashboard.vue'
import ListadoIncidenciasAdmin from '../components/admin/ListadoIncidenciasAdmin.vue'
import AdminModals from '../components/admin/AdminModals.vue'

//UTILS
const {
  todasIncidencias,
  categorias,
  ubicaciones,
  profesores,
  dashboard,
  fetchData
} = useAdminData()

//Filtros y acciones de incidencias
const {
  filtros,
  incidenciasFilteradas,
  limpiarFiltros
} = useIncidenciaFilters(todasIncidencias)

//Acciones para vista ampliada y cierre de incidencia
const {
  vistaAmpliadaVisible,
  vistaAmpliadaTipo,
  vistaAmpliadaContenido,
  vistaAmpliadaError,
  modalCerrarVisible,
  fotoErrorMap,
  cambiarEstado,
  confirmarCerrarIncidencia,
  getFotoUrl,
  abrirFoto,
  abrirDescripcion,
  cerrarVistaAmpliada
} = useIncidenciaActions(fetchData)

//Métricas y estadísticas
const {
  tiempoMedioHoras,
  tiempoMedioArco,
  tiempoMedioCircunferencia,
  tiempoMedioAngulo,
  categoryStats,
  categoryGradient,
  ubicacionStats
} = useAdminMetricas(todasIncidencias, dashboard)

//Estado de la vista
const activeAdminView = ref('dashboard')
const menuExpanded = ref(true)
const itemsPorPagina = ref(5)

//Definición del menú de administración
const adminMenu = [
  { id: 'dashboard', label: 'Inicio' },
  { id: 'incidencias', label: 'Todas las incidencias' },
  { id: 'estadisticas', label: 'Métricas' },
  { id: 'gestion', label: 'Gestión' }
]

//Incidencias más recientes para mostrar en el dashboard
const ultimasIncidencias = computed(() => {
  return [...todasIncidencias.value]
    .sort((a, b) => new Date(b.fechaCreacion) - new Date(a.fechaCreacion))
    .slice(0, 3)
})

//Función para formatear fechas en español
const formatFecha = (fecha) => {
  return new Date(fecha).toLocaleString('es-ES')
}

onMounted(fetchData)

</script>
<template>
  <div class="min-h-screen bg-gradient-to-b from-slate-50 via-white to-slate-100 px-4 py-6 sm:px-6">
    <div class="mx-auto max-w-7xl space-y-8">
      <h1 class="border-b-4 border-sky-600 pb-3 text-2xl font-bold tracking-tight text-slate-900 sm:text-3xl">
        Gestión de Incidencias del Centro
      </h1>

      <section class="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm sm:p-6">
        <h2 class="text-xl font-semibold text-slate-800">Filtros</h2>
        <div class="mt-4 grid grid-cols-1 gap-4 md:grid-cols-3 md:items-end">
          <div class="flex flex-col gap-2">
            <label class="text-sm font-semibold text-slate-700">Categoría</label>
            <select
              v-model="filtros.categoriaId"
              @change="aplicarFiltros"
              class="rounded-lg border border-slate-300 bg-white px-3 py-2 text-sm text-slate-800 shadow-sm outline-none transition focus:border-sky-500 focus:ring-2 focus:ring-sky-200"
            >
              <option value="">Todas</option>
              <option v-for="c in categorias" :key="c.id" :value="c.id">{{ c.nombre }}</option>
            </select>
          </div>

          <div class="flex flex-col gap-2">
            <label class="text-sm font-semibold text-slate-700">Estado</label>
            <select
              v-model="filtros.estado"
              @change="aplicarFiltros"
              class="rounded-lg border border-slate-300 bg-white px-3 py-2 text-sm text-slate-800 shadow-sm outline-none transition focus:border-sky-500 focus:ring-2 focus:ring-sky-200"
            >
              <option value="">Todos</option>
              <option value="PENDIENTE">Abierto (Pendiente)</option>
              <option value="EN_PROCESO">En Proceso</option>
              <option value="REALIZADA">Cerrado (Realizada)</option>
            </select>
          </div>

          <button
            @click="limpiarFiltros"
            class="h-fit rounded-lg bg-slate-700 px-4 py-2 text-sm font-semibold text-white transition hover:bg-slate-800"
          >
            Limpiar Filtros
          </button>
        </div>
      </section>

      <section class="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm sm:p-6">
        <h2 class="text-xl font-semibold text-slate-800">Listado de Incidencias</h2>

        <div
          v-if="incidenciasFilteradas.length === 0"
          class="mt-4 rounded-xl border border-dashed border-slate-300 bg-slate-50 p-10 text-center text-slate-600"
        >
          <p>No hay incidencias que coincidan con los filtros aplicados.</p>
        </div>

        <div v-else class="mt-4 overflow-hidden rounded-xl border border-slate-200">
          <div class="overflow-x-auto">
            <table class="min-w-full divide-y divide-slate-200 text-sm">
              <thead class="bg-slate-50">
                <tr>
                  <th class="px-4 py-3 text-left font-semibold text-slate-700">Ubicación</th>
                  <th class="px-4 py-3 text-left font-semibold text-slate-700">Categoría</th>
                  <th class="px-4 py-3 text-left font-semibold text-slate-700">Usuario</th>
                  <th class="px-4 py-3 text-left font-semibold text-slate-700">Descripción</th>
                  <th class="px-4 py-3 text-left font-semibold text-slate-700">Estado</th>
                  <th class="px-4 py-3 text-left font-semibold text-slate-700">Fecha Creación</th>
                  <th class="px-4 py-3 text-left font-semibold text-slate-700">Fecha Cierre</th>
                  <th class="px-4 py-3 text-left font-semibold text-slate-700">Acciones</th>
                </tr>
              </thead>
              <tbody class="divide-y divide-slate-100 bg-white">
                <tr v-for="inc in incidenciasFilteradas" :key="inc.id" class="hover:bg-slate-50">
                  <td class="px-4 py-3 text-slate-700">{{ inc.ubicacion.codigo }} - {{ inc.ubicacion.descripcion }}</td>
                  <td class="px-4 py-3 text-slate-700">{{ inc.categoria.nombre }}</td>
                  <td class="px-4 py-3 text-slate-700">{{ inc.usuarioCreador.nombre }}</td>
                  <td class="max-w-xs truncate px-4 py-3 text-slate-700">{{ inc.descripcion }}</td>
                  <td class="px-4 py-3">
                    <span :class="estadoBadgeClass(inc.estado)">{{ inc.estado }}</span>
                  </td>
                  <td class="px-4 py-3 text-slate-600">{{ formatFecha(inc.fechaCreacion) }}</td>
                  <td class="px-4 py-3 text-slate-600">{{ inc.fechaResolucion ? formatFecha(inc.fechaResolucion) : '-' }}</td>
                  <td class="px-4 py-3">
                    <button
                      v-if="inc.estado === 'PENDIENTE'"
                      @click="cambiarEstado(inc, 'EN_PROCESO')"
                      class="rounded-lg bg-sky-600 px-3 py-1.5 text-xs font-semibold text-white transition hover:bg-sky-700"
                    >
                      En Proceso
                    </button>
                    <template v-else-if="inc.estado === 'EN_PROCESO'">
                      <div class="flex min-w-[210px] gap-2">
                        <button
                          @click="cambiarEstado(inc, 'PENDIENTE', true)"
                          class="w-1/2 rounded-lg bg-sky-600 px-2 py-1.5 text-xs font-semibold text-white transition hover:bg-sky-700"
                        >
                          Volver
                        </button>
                        <button
                          @click="cambiarEstado(inc, 'REALIZADA')"
                          class="w-1/2 rounded-lg bg-emerald-600 px-2 py-1.5 text-xs font-semibold text-white transition hover:bg-emerald-700"
                        >
                          Cerrar
                        </button>
                      </div>
                    </template>
                    <span v-else class="text-xs font-semibold text-slate-500">Cerrada</span>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </section>

      <section class="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm sm:p-6">
        <h2 class="text-xl font-semibold text-slate-800">Dashboard de Métricas</h2>

        <div class="mt-5 grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-2">
          <article class="rounded-xl border border-slate-200 bg-slate-50 p-5 text-center">
            <div class="text-3xl font-bold text-sky-600">{{ dashboard.abiertas }}</div>
            <div class="mt-1 text-sm text-slate-600">Incidencias Abiertas</div>
          </article>
          <article class="rounded-xl border border-slate-200 bg-slate-50 p-5 text-center">
            <div class="text-3xl font-bold text-sky-600">{{ dashboard.resueltas }}</div>
            <div class="mt-1 text-sm text-slate-600">Incidencias Resueltas</div>
          </article>
        </div>

        <div class="mt-6 grid grid-cols-1 gap-4 lg:grid-cols-2">
          <div class="rounded-xl border border-slate-200 bg-white p-4">
            <h3 class="mb-3 text-base font-semibold text-slate-700">Porcentaje de Incidencias por Categoria</h3>

            <div v-if="categoryStats.length > 0" class="grid grid-cols-1 items-center gap-5 md:grid-cols-[220px_1fr]">
              <div class="mx-auto">
                <div
                  class="flex h-52 w-52 items-center justify-center rounded-full"
                  :style="{ background: `conic-gradient(${categoryGradient})` }"
                >
                  <div class="flex h-32 w-32 flex-col items-center justify-center rounded-full bg-white text-center shadow-inner">
                    <div class="text-2xl font-bold text-slate-800">{{ todasIncidencias.length }}</div>
                    <div class="text-xs font-medium text-slate-500">Incidencias</div>
                  </div>
                </div>
              </div>

              <ul class="space-y-2">
                <li
                  v-for="item in categoryStats"
                  :key="item.nombre"
                  class="flex items-center justify-between rounded-lg border border-slate-200 px-3 py-2"
                >
                  <div class="flex items-center gap-2">
                    <span class="h-3 w-3 rounded-full" :style="{ backgroundColor: item.color }"></span>
                    <span class="text-sm text-slate-700">{{ item.nombre }}</span>
                  </div>
                  <div class="text-right">
                    <div class="text-sm font-semibold text-slate-800">{{ item.percentLabel }}</div>
                    <div class="text-xs text-slate-500">{{ item.count }} incidencias</div>
                  </div>
                </li>
              </ul>
            </div>

            <p v-else class="rounded-lg border border-dashed border-slate-300 bg-slate-50 px-3 py-8 text-center text-sm text-slate-500">
              No hay datos de categorias para mostrar.
            </p>
          </div>
          <div class="rounded-xl border border-slate-200 bg-white p-4">
            <h3 class="mb-3 text-base font-semibold text-slate-700">Reloj de Tiempo Medio de Resolucion</h3>

            <div class="flex flex-col items-center gap-4">
              <svg viewBox="0 0 220 220" class="h-52 w-52">
                <circle cx="110" cy="110" r="92" fill="#f1f5f9" stroke="#cbd5e1" stroke-width="12" />
                <circle
                  cx="110"
                  cy="110"
                  r="92"
                  fill="none"
                  stroke="#fdba74"
                  stroke-width="12"
                  stroke-linecap="round"
                  :stroke-dasharray="`${tiempoMedioArco} ${tiempoMedioCircunferencia}`"
                  transform="rotate(-90 110 110)"
                />
                <circle cx="110" cy="110" r="58" fill="white" />

                <line
                  x1="110"
                  y1="110"
                  x2="110"
                  y2="42"
                  stroke="#0284c7"
                  stroke-width="5"
                  stroke-linecap="round"
                  :transform="`rotate(${tiempoMedioAngulo} 110 110)`"
                />
                <circle cx="110" cy="110" r="8" fill="#334155" />

                <text x="110" y="28" text-anchor="middle" class="fill-slate-500 text-[12px] font-semibold">0h / 72h</text>
                <text x="110" y="204" text-anchor="middle" class="fill-slate-500 text-[12px] font-semibold">36h</text>
                <text x="26" y="114" text-anchor="middle" class="fill-slate-500 text-[12px] font-semibold">54h</text>
                <text x="194" y="114" text-anchor="middle" class="fill-slate-500 text-[12px] font-semibold">18h</text>
              </svg>

              <div class="text-center">
                <div class="text-2xl font-bold text-sky-600">{{ tiempoMedioHoras.toFixed(1) }}h</div>
                <p class="text-sm text-slate-600">Tiempo medio actual de resolucion (escala reloj 72h)</p>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

const todasIncidencias = ref([])
const categorias = ref([])
const filtros = ref({ categoriaId: '', estado: '' })
const dashboard = ref({ abiertas: 0, resueltas: 0, tiempoMedioHoras: 0, topUbicaciones: [], topCategorias: [] })

const incidenciasFilteradas = computed(() => {
  return todasIncidencias.value.filter(inc => {
    let cumple = true
    if (filtros.value.categoriaId) {
      cumple = cumple && inc.categoria.id == filtros.value.categoriaId
    }
    if (filtros.value.estado) {
      cumple = cumple && inc.estado === filtros.value.estado
    }
    return cumple
  })
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
  // 0h arriba, 18h derecha, 36h abajo, 54h izquierda, 72h arriba
  return horasEscaladas * (360 / maxHorasReloj)
})

const categoryPalette = ['#0ea5e9', '#22c55e', '#f59e0b', '#a855f7', '#ef4444', '#14b8a6', '#f97316', '#6366f1']

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
    
    // Cargar todas las incidencias
    const res = await axios.get('/api/incidencias?size=1000', {
      headers: { Authorization: 'Basic ' + auth }
    })
    todasIncidencias.value = res.data.content || res.data
    
    // Cargar categorías
    categorias.value = (await axios.get('/api/categorias', {
      headers: { Authorization: 'Basic ' + auth }
    })).data
    
    // Cargar dashboard
    const dashboardRes = await axios.get('/api/incidencias/dashboard', {
      headers: { Authorization: 'Basic ' + auth }
    })
    dashboard.value = dashboardRes.data
  } catch (error) {
    console.error('Error al cargar datos:', error)
  }
}

onMounted(fetchData)

const aplicarFiltros = () => {
  // Los filtros se aplican automáticamente con el computed
}

const limpiarFiltros = () => {
  filtros.value = { categoriaId: '', estado: '' }
}

const cambiarEstado = async (incidencia, nuevoEstado, confirmarRegreso = false) => {
  try {
    if (incidencia.estado === 'REALIZADA') {
      alert('No se puede modificar una incidencia ya realizada')
      return
    }

    if (confirmarRegreso) {
      const confirmed = window.confirm('¿Seguro que quieres cambiar a un estado anterior?')
      if (!confirmed) return
    }

    const auth = localStorage.getItem('auth')
    await axios.put(`/api/incidencias/${incidencia.id}/estado`, 
      { estado: nuevoEstado },
      { headers: { Authorization: 'Basic ' + auth } }
    )
    // Recargar datos
    fetchData()
  } catch (error) {
    console.error('Error al cambiar estado:', error)
    alert('Error al cambiar el estado de la incidencia')
  }
}

const formatFecha = (fecha) => {
  return new Date(fecha).toLocaleString('es-ES')
}

const estadoBadgeClass = (estado) => {
  const base = 'inline-flex rounded-md px-2.5 py-1 text-xs font-semibold'

  if (estado === 'PENDIENTE') return `${base} bg-amber-200 text-amber-900`
  if (estado === 'EN_PROCESO') return `${base} bg-cyan-200 text-cyan-900`
  if (estado === 'REALIZADA') return `${base} bg-emerald-200 text-emerald-900`

  return `${base} bg-slate-200 text-slate-800`
}
</script>

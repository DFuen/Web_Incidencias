<template>
  <div class="hidden overflow-hidden rounded-xl border border-slate-200 md:block">
    
    <div class="overflow-x-auto">
        <div class="flex items-center justify-end border-b border-slate-200 bg-white px-4 py-3">
    
    <div class="flex items-center gap-2">
        
        <label class="text-sm text-slate-600">
        Mostrar:
        </label>

        <select
        v-model="itemsPerPage"
        class="rounded-lg border border-slate-300 px-3 py-2 text-sm"
        >
        <option :value="5">5</option>
        <option :value="10">10</option>
        </select>

    </div>

    </div>
      
      <table class="min-w-full divide-y divide-slate-200 text-sm">
        
        <thead class="bg-slate-50">
          <tr>
            <th class="px-4 py-3 text-left font-semibold text-slate-700">
              Ubicación
            </th>

            <th class="px-4 py-3 text-left font-semibold text-slate-700">
              Categoría
            </th>

            <th class="px-4 py-3 text-left font-semibold text-slate-700">
              Profesor
            </th>

            <th class="px-4 py-3 text-left font-semibold text-slate-700">
              Descripción
            </th>

            <th class="px-4 py-3 text-left font-semibold text-slate-700">
              Imagen
            </th>

            <th class="px-4 py-3 text-left font-semibold text-slate-700">
              Estado
            </th>

            <th class="px-4 py-3 text-left font-semibold text-slate-700">
              Encargado
            </th>

            <th class="px-4 py-3 text-left font-semibold text-slate-700">
              Fecha Creación
            </th>

            <th class="px-4 py-3 text-left font-semibold text-slate-700">
              Fecha Cierre
            </th>

            <th class="px-4 py-3 text-left font-semibold text-slate-700">
              Solución
            </th>

            <th class="px-4 py-3 text-left font-semibold text-slate-700">
              Acciones
            </th>
          </tr>
        </thead>

        <tbody class="divide-y divide-slate-100 bg-white">
          
          <tr
            v-for="inc in paginatedIncidencias"
            :key="inc.id"
            class="hover:bg-slate-50"
          >
            
            <td class="px-4 py-3 text-slate-700">
              {{ inc.ubicacion.codigo }} -
              {{ inc.ubicacion.nombre }}
            </td>

            <td class="px-4 py-3 text-slate-700">
              {{ inc.categoria.nombre }}
            </td>

            <td class="px-4 py-3 text-slate-700">
              {{ inc.usuarioCreador?.nombre || '-' }}
            </td>

            <td class="px-4 py-3 align-top text-slate-700">
              <div>
                <button
                  type="button"
                  @click="abrirDescripcion(inc.descripcion)"
                  class="max-w-3xs truncate text-left hover:underline"
                >
                  {{ inc.descripcion }}
                </button>

                <p class="mt-1 text-xs text-slate-500">
                  Haz clic para ver la descripción completa
                </p>
              </div>
            </td>

            <td class="px-4 py-3 text-slate-700">
              <div>
                
                <button
                  v-if="inc.foto && !fotoErrorMap[inc.id]"
                  type="button"
                  @click="abrirFoto(inc.foto)"
                >
                  <img
                    :src="getFotoUrl(inc.foto)"
                    alt="Foto incidencia"
                    class="h-15 w-15 rounded object-cover shadow-sm transition hover:scale-105"
                  />
                </button>

                <p
                  v-if="inc.foto"
                  class="mt-1 text-xs text-slate-500"
                >
                  Haz clic para ampliar la foto
                </p>

                <span
                  v-else
                  class="text-xs text-slate-500"
                >
                  No proporcionado
                </span>

              </div>
            </td>

            <td class="px-4 py-3">
              <EstadoBadge :estado="inc.estado" />
            </td>

            <td class="px-4 py-3 text-slate-700">
              {{ inc.usuarioEncargado?.nombre || '-' }}
            </td>

            <td class="px-4 py-3 text-slate-600">
              {{ formatFecha(inc.fechaCreacion) }}
            </td>

            <td class="px-4 py-3 text-slate-600">
              {{
                inc.fechaResolucion
                  ? formatFecha(inc.fechaResolucion)
                  : '-'
              }}
            </td>

            <td class="px-4 py-3 align-top text-slate-700">
              
              <div v-if="inc.descripcionSolucion">
                
                <button
                  type="button"
                  @click="abrirDescripcion(inc.descripcionSolucion)"
                  class="max-w-3xs truncate text-left hover:underline"
                >
                  {{ inc.descripcionSolucion }}
                </button>

                <p class="mt-1 text-xs text-slate-500">
                  Haz clic para ver la solución completa
                </p>

              </div>

              <span
                v-else
                class="text-xs text-slate-500"
              >
                -
              </span>

            </td>

            <td class="px-4 py-3">
              
              <button
                v-if="inc.estado === 'PENDIENTE'"
                @click="cambiarEstado(inc, 'EN_PROCESO')"
                class="rounded-lg bg-sky-600 px-3 py-1.5 text-xs font-semibold text-white transition hover:bg-sky-700"
              >
                En Proceso
              </button>

              <template v-else-if="inc.estado === 'EN_PROCESO'">
                
                <button
                  @click="cambiarEstado(inc, 'REALIZADA')"
                  class="rounded-lg bg-emerald-600 px-3 py-1.5 text-xs font-semibold text-white transition hover:bg-emerald-700"
                >
                  Finalizar
                </button>

              </template>

              <span
                v-else
                class="text-xs font-semibold text-slate-500"
              >
                Finalizada
              </span>

            </td>

          </tr>

        </tbody>

      </table>

    </div>

    <!-- PAGINACIÓN -->

    <!-- PAGINACIÓN -->

    <div v-if="!hidePagination" class="flex flex-col items-center justify-between gap-4 border-t border-slate-200 bg-white px-4 py-4 sm:flex-row">

    <p class="text-sm text-slate-600">
        Mostrando {{ paginatedIncidencias.length }}
        de {{ incidencias.length }} incidencias
    </p>

    <div class="flex items-center gap-3">

        <button
        @click="prevPage"
        :disabled="currentPage === 1"
        class="rounded-lg border border-slate-300 px-4 py-2 text-sm transition hover:bg-slate-100 disabled:cursor-not-allowed disabled:opacity-50"
        >
        Anterior
        </button>

        <span class="text-sm text-slate-600">
        Página {{ currentPage }} de {{ totalPages }}
        </span>

        <button
        @click="nextPage"
        :disabled="currentPage === totalPages"
        class="rounded-lg border border-slate-300 px-4 py-2 text-sm transition hover:bg-slate-100 disabled:cursor-not-allowed disabled:opacity-50"
        >
        Siguiente
        </button>

    </div>

    </div>

  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import EstadoBadge from './EstadoBadge.vue'

const props = defineProps({
  incidencias: {
    type: Array,
    required: true
  },

  fotoErrorMap: {
    type: Object,
    required: true
  },

  getFotoUrl: {
    type: Function,
    required: true
  },

  abrirFoto: {
    type: Function,
    required: true
  },

  abrirDescripcion: {
    type: Function,
    required: true
  },

  cambiarEstado: {
    type: Function,
    required: true
  },

  formatFecha: {
    type: Function,
    required: true
  },

  itemsPorPagina: {
    type: Number,
    default: 5
  },
  hidePagination: {
  type: Boolean,
  default: false
  }
})

const currentPage = ref(1)

const itemsPerPage = ref(props.itemsPorPagina)

watch(itemsPerPage, () => {
  currentPage.value = 1
})

watch(
  () => props.incidencias,
  () => {
    currentPage.value = 1
  }
)

const totalPages = computed(() => {
  if (!props.incidencias.length) return 1

  return Math.ceil(
    props.incidencias.length / itemsPerPage.value
  )
})

const paginatedIncidencias = computed(() => {
  const start =
    (currentPage.value - 1) * itemsPerPage.value

  const end = start + itemsPerPage.value

  return props.incidencias.slice(start, end)
})

const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++
  }
}

const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--
  }
}
</script>
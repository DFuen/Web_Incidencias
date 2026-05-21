<template>
  <section class="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm sm:p-6">
    <h2 class="text-lg font-semibold text-slate-800 sm:text-xl">
      Histórico de Mis Incidencias
    </h2>

    <div
      v-if="incidencias.length === 0"
      class="mt-4 rounded-xl border border-dashed border-slate-300 bg-slate-50 p-10 text-center text-slate-600"
    >
      <p>No tienes incidencias reportadas aún.</p>
    </div>

    <template v-else>
      <!-- VISTA ESCRITORIO: TABLA -->
      <div class="mt-4 hidden overflow-hidden rounded-xl border border-slate-200 md:block">
        <div class="overflow-x-auto">
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
              </tr>
            </thead>

            <tbody class="divide-y divide-slate-100 bg-white">
              <tr
                v-for="inc in incidencias"
                :key="inc.id"
                class="hover:bg-slate-50"
              >
                <td class="px-4 py-3 text-slate-700">
                  {{ formatUbicacion(inc.ubicacion) }}
                </td>

                <td class="px-4 py-3 text-slate-700">
                  {{ inc.categoria?.nombre || '-' }}
                </td>

                <td class="max-w-xs px-4 py-3 text-slate-700">
                  <button
                    type="button"
                    @click="abrirDescripcion(inc.descripcion)"
                    class="max-w-xs truncate text-left hover:underline"
                  >
                    {{ inc.descripcion || 'Sin descripción' }}
                  </button>
                </td>

                <td class="px-4 py-3 text-slate-700">
                  <button
                    v-if="inc.foto"
                    type="button"
                    @click="abrirFoto(inc.foto)"
                  >
                    <img
                      :src="getFotoUrl(inc.foto)"
                      alt="Foto incidencia"
                      class="h-14 w-14 rounded object-cover shadow-sm transition hover:scale-105"
                    />
                  </button>

                  <span
                    v-else
                    class="text-xs text-slate-500"
                  >
                    No proporcionada
                  </span>
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
                  {{ formatFecha(inc.fechaResolucion) }}
                </td>

                <td class="max-w-xs px-4 py-3 text-slate-700">
                  <button
                    v-if="inc.descripcionSolucion"
                    type="button"
                    @click="abrirDescripcion(inc.descripcionSolucion)"
                    class="max-w-xs truncate text-left hover:underline"
                  >
                    {{ inc.descripcionSolucion }}
                  </button>

                  <span
                    v-else
                    class="text-xs text-slate-500"
                  >
                    -
                  </span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- VISTA MÓVIL: TARJETAS -->
      <div class="mt-4 grid grid-cols-1 gap-4 md:hidden">
        <IncidenciaCard
          v-for="inc in incidencias"
          :key="inc.id"
          :incidencia="inc"
          :foto-error-map="fotoErrorMap"
          :get-foto-url="getFotoUrl"
          :abrir-foto="abrirFoto"
          :abrir-descripcion="abrirDescripcion"
          :cambiar-estado="() => {}"
          :mostrar-acciones="false"
        />
      </div>
    </template>

    <VistaAmpliadaModal
      :visible="vistaAmpliadaVisible"
      :tipo="vistaAmpliadaTipo"
      :contenido="vistaAmpliadaContenido"
      :error="vistaAmpliadaError"
      @close="cerrarVistaAmpliada"
      @image-error="vistaAmpliadaError = true"
    />
  </section>
</template>

<script setup>
import { ref } from 'vue'

import EstadoBadge from './EstadoBadge.vue'
import IncidenciaCard from './IncidenciaCard.vue'
import VistaAmpliadaModal from './VistaAmpliadaModal.vue'
import { useIncidenciaUtils } from '../../composables/useIncidenciaUtils'

defineProps({
  incidencias: {
    type: Array,
    required: true
  }
})

const {
  getFotoUrl,
  formatFecha,
  formatUbicacion
} = useIncidenciaUtils()

const fotoErrorMap = ref({})

const vistaAmpliadaVisible = ref(false)
const vistaAmpliadaTipo = ref('foto')
const vistaAmpliadaContenido = ref('')
const vistaAmpliadaError = ref(false)

const abrirFoto = (fotoPath) => {
  vistaAmpliadaTipo.value = 'foto'
  vistaAmpliadaContenido.value = getFotoUrl(fotoPath)
  vistaAmpliadaError.value = false
  vistaAmpliadaVisible.value = true
}

const abrirDescripcion = (descripcion) => {
  vistaAmpliadaTipo.value = 'descripcion'
  vistaAmpliadaContenido.value =
    descripcion?.trim() || 'Sin descripción'
  vistaAmpliadaVisible.value = true
}

const cerrarVistaAmpliada = () => {
  vistaAmpliadaVisible.value = false
  vistaAmpliadaContenido.value = ''
  vistaAmpliadaError.value = false
}
</script>
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

    <div
      v-else
      class="mt-4 overflow-hidden rounded-xl border border-slate-200"
    >
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
                Estado
              </th>

              <th class="px-4 py-3 text-left font-semibold text-slate-700">
                Fecha Creación
              </th>

              <th class="px-4 py-3 text-left font-semibold text-slate-700">
                Fecha Resolución
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
                {{ inc.ubicacion.codigo }} -
                {{ inc.ubicacion.descripcion }}
              </td>

              <td class="px-4 py-3 text-slate-700">
                {{ inc.categoria.nombre }}
              </td>

              <td class="max-w-xs truncate px-4 py-3 text-slate-700">
                {{ inc.descripcion }}
              </td>

              <td class="px-4 py-3">
                <EstadoBadge :estado="inc.estado" />
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
            </tr>

          </tbody>
        </table>

      </div>
    </div>

  </section>
</template>

<script setup>
import EstadoBadge from './EstadoBadge.vue'

defineProps({
  incidencias: {
    type: Array,
    required: true
  }
})

const formatFecha = (fecha) => {
  return new Date(fecha).toLocaleString('es-ES')
}
</script>
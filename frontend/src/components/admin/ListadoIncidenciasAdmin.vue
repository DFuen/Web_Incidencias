<template>
  <section class="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm sm:p-6">
    <h2 class="text-xl font-semibold text-slate-800">
      Listado de Incidencias
    </h2>

    <IncidenciaFilters
      :filtros="filtros"
      :categorias="categorias"
      @limpiar="$emit('limpiar')"
    />

    <div
      v-if="incidencias.length === 0"
      class="mt-4 rounded-xl border border-dashed border-slate-300 bg-slate-50 p-10 text-center text-slate-600"
    >
      <p>No hay incidencias que coincidan con los filtros aplicados.</p>
    </div>

    <div v-else class="mt-4">
      <div class="block space-y-4 md:hidden">
        <IncidenciaCard
          v-for="inc in incidencias"
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
        :incidencias="incidencias"
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
</template>

<script setup>
import IncidenciaFilters from '../incidencias/IncidenciaFilters.vue'
import IncidenciaCard from '../incidencias/IncidenciaCard.vue'
import IncidenciaTable from '../incidencias/IncidenciaTable.vue'

defineProps({
  incidencias: {
    type: Array,
    required: true
  },

  categorias: {
    type: Array,
    required: true
  },

  filtros: {
    type: Object,
    required: true
  },

  itemsPorPagina: {
    type: Number,
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
  }
})

defineEmits(['limpiar'])
</script>
<template>
  <section class="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm sm:p-6">

    <div class="rounded-xl border border-slate-200 bg-slate-50 p-4">

      <h1 class="border-b-4 border-sky-600 pb-3 text-2xl font-bold tracking-tight text-slate-900 sm:text-3xl">
        Últimas incidencias
      </h1>

      <!-- Tarjetas móvil -->
      <div class="grid gap-4 p-4 md:hidden">
        <IncidenciaCard
          v-for="inc in ultimasIncidencias.slice(0, 3)"
          :key="inc.id"
          :incidencia="inc"
          :foto-error-map="fotoErrorMap"
          :get-foto-url="getFotoUrl"
          :abrir-foto="abrirFoto"
          :abrir-descripcion="abrirDescripcion"
          :cambiar-estado="cambiarEstado"
        />
      </div>

      <div class="mt-4 overflow-hidden rounded-lg border border-slate-200 bg-white">

        <IncidenciaTable
          v-if="ultimasIncidencias.length > 0"
          :incidencias="ultimasIncidencias.slice(0, 3)"
          :items-por-pagina="3"
          :hide-pagination="true"
          :foto-error-map="fotoErrorMap"
          :get-foto-url="getFotoUrl"
          :abrir-foto="abrirFoto"
          :abrir-descripcion="abrirDescripcion"
          :cambiar-estado="cambiarEstado"
          :format-fecha="formatFecha"
        />

        <p
          v-else
          class="px-4 py-8 text-center text-sm text-slate-500"
        >
          No hay incidencias recientes para mostrar.
        </p>

      </div>

    </div>
    <MetricasDashboard
      :dashboard="dashboard"
      :total-incidencias="todasIncidencias.length"
      :tiempo-medio-horas="tiempoMedioHoras"
      :tiempo-medio-arco="tiempoMedioArco"
      :tiempo-medio-circunferencia="tiempoMedioCircunferencia"
      :tiempo-medio-angulo="tiempoMedioAngulo"
      :category-stats="categoryStats"
      :category-gradient="categoryGradient"
    />
  </section>
</template>

<script setup>
import IncidenciaCard from '../incidencias/IncidenciaCard.vue'
import IncidenciaTable from '../incidencias/IncidenciaTable.vue'
import MetricasDashboard from './MetricasDashboard.vue'

defineProps({
  dashboard: {
    type: Object,
    required: true
  },

  ultimasIncidencias: {
    type: Array,
    required: true
  },

  todasIncidencias: {
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

  tiempoMedioHoras: {
    type: Number,
    required: true
  },

  tiempoMedioArco: {
    type: Number,
    required: true
  },

  tiempoMedioCircunferencia: {
    type: Number,
    required: true
  },

  tiempoMedioAngulo: {
    type: Number,
    required: true
  },

  categoryStats: {
    type: Array,
    required: true
  },

  categoryGradient: {
    type: String,
    required: true
  }
})
</script>
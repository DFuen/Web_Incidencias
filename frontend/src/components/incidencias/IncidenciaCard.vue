<template>
  <div
    class="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm"
  >
    <div class="flex items-start justify-between gap-3">
      <div>
        <h3 class="text-base font-semibold text-slate-800">
          {{ incidencia.ubicacion.codigo }} -
          {{ incidencia.ubicacion.nombre }}
        </h3>

        <p class="mt-1 text-sm text-slate-500">
          {{ incidencia.categoria.nombre }}
        </p>
      </div>

      <EstadoBadge :estado="incidencia.estado" />
    </div>

    <div class="mt-4 space-y-2 text-sm text-slate-700">
      <p>
        <span class="font-semibold">Profesor:</span>
        {{ incidencia.usuarioCreador?.nombre || '-' }}
      </p>

      <p>
        <span class="font-semibold">Encargado:</span>
        {{ incidencia.usuarioEncargado?.nombre || '-' }}
      </p>

      <p>
        <span class="font-semibold">Fecha creación:</span>
        {{ formatFecha(incidencia.fechaCreacion) }}
      </p>

      <p>
        <span class="font-semibold">Fecha cierre:</span>
        {{
          incidencia.fechaResolucion
            ? formatFecha(incidencia.fechaResolucion)
            : '-'
        }}
      </p>
    </div>

    <div class="mt-4">
      <p class="mb-1 text-sm font-semibold text-slate-700">
        Descripción
      </p>

      <button
        type="button"
        @click="abrirDescripcion(incidencia.descripcion)"
        class="line-clamp-3 text-left text-sm text-slate-600 hover:underline"
      >
        {{ incidencia.descripcion }}
      </button>
    </div>

    <div
      v-if="incidencia.descripcionSolucion"
      class="mt-4"
    >
      <p class="mb-1 text-sm font-semibold text-slate-700">
        Solución
      </p>

      <button
        type="button"
        @click="abrirDescripcion(incidencia.descripcionSolucion)"
        class="line-clamp-3 text-left text-sm text-slate-600 hover:underline"
      >
        {{ incidencia.descripcionSolucion }}
      </button>
    </div>

    <div
      v-if="incidencia.foto && !fotoErrorMap[incidencia.id]"
      class="mt-4"
    >
      <button
        type="button"
        @click="abrirFoto(incidencia.foto)"
      >
        <img
          :src="getFotoUrl(incidencia.foto)"
          alt="Foto incidencia"
          class="h-40 w-full rounded-xl object-cover transition hover:scale-[1.02]"
        />
      </button>
    </div>

    <div class="mt-5">
      <button
        v-if="incidencia.estado === 'PENDIENTE'"
        @click="cambiarEstado(incidencia, 'EN_PROCESO')"
        class="w-full rounded-xl bg-sky-600 px-4 py-2 text-sm font-semibold text-white hover:bg-sky-700"
      >
        En Proceso
      </button>

      <button
        v-else-if="incidencia.estado === 'EN_PROCESO'"
        @click="cambiarEstado(incidencia, 'REALIZADA')"
        class="w-full rounded-xl bg-emerald-600 px-4 py-2 text-sm font-semibold text-white hover:bg-emerald-700"
      >
        Finalizar
      </button>

      <div
        v-else
        class="rounded-xl bg-slate-100 px-4 py-2 text-center text-sm font-semibold text-slate-500"
      >
        Finalizada
      </div>
    </div>
  </div>
</template>

<script setup>
import EstadoBadge from './EstadoBadge.vue'

defineProps({
  incidencia: {
    type: Object,
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
  }
})

const formatFecha = (fecha) => {
  return new Date(fecha).toLocaleString('es-ES')
}
</script>
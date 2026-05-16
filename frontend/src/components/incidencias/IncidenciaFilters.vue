<template>
  <div class="mt-4 rounded-xl border border-slate-200 bg-slate-50 p-4">

    <div class="grid grid-cols-1 gap-4 md:grid-cols-12 md:items-end">

      <!-- Categoría -->

      <div class="flex flex-col gap-2 md:col-span-3">
        <label class="text-sm font-semibold text-slate-700">
          Categoría
        </label>

        <select
          v-model="filtros.categoriaId"
          class="rounded-lg border border-slate-300 bg-white px-3 py-2 text-sm text-slate-800 shadow-sm outline-none transition focus:border-sky-500 focus:ring-2 focus:ring-sky-200"
        >
          <option value="">Todas</option>

          <option
            v-for="c in categorias"
            :key="c.id"
            :value="c.id"
          >
            {{ c.nombre }}
          </option>
        </select>
      </div>

      <!-- Estado -->

      <div class="flex flex-col gap-2 md:col-span-3">
        <label class="text-sm font-semibold text-slate-700">
          Estado
        </label>

        <select
          v-model="filtros.estado"
          class="rounded-lg border border-slate-300 bg-white px-3 py-2 text-sm text-slate-800 shadow-sm outline-none transition focus:border-sky-500 focus:ring-2 focus:ring-sky-200"
        >
          <option value="">Todos</option>

          <option value="PENDIENTE">
            Abierto (Pendiente)
          </option>

          <option value="EN_PROCESO">
            En Proceso
          </option>

          <option value="REALIZADA">
            Cerrado (Realizada)
          </option>
        </select>
      </div>

      <!-- Desde -->

      <div class="flex flex-col gap-2 md:col-span-2">
        <label class="text-sm font-semibold text-slate-700">
          Desde
        </label>

        <input
          v-model="filtros.fechaDesde"
          type="date"
          class="rounded-lg border border-slate-300 bg-white px-3 py-2 text-sm text-slate-800 shadow-sm outline-none transition focus:border-sky-500 focus:ring-2 focus:ring-sky-200"
        />
      </div>

      <!-- Hasta -->

      <div class="flex flex-col gap-2 md:col-span-2">
        <label class="text-sm font-semibold text-slate-700">
          Hasta
        </label>

        <input
          v-model="filtros.fechaHasta"
          type="date"
          class="rounded-lg border border-slate-300 bg-white px-3 py-2 text-sm text-slate-800 shadow-sm outline-none transition focus:border-sky-500 focus:ring-2 focus:ring-sky-200"
        />
      </div>

      <!-- Botón -->

      <button
        @click="$emit('limpiar')"
        class="h-fit rounded-lg bg-slate-700 px-4 py-2 text-sm font-semibold text-white transition hover:bg-slate-800 md:col-span-2"
      >
        Limpiar Filtros
      </button>

    </div>

  </div>
</template>

<script setup>
defineProps({
  filtros: {
    type: Object,
    required: true
  },

  categorias: {
    type: Array,
    required: true
  }
})

defineEmits(['limpiar'])
</script>
<template>
  <div>
    <h2 class="mt-6 text-xl font-semibold text-slate-800">
      Métricas Generales
    </h2>

    <div class="mt-5 grid grid-cols-1 gap-4 sm:grid-cols-2">
      <article class="rounded-xl border border-slate-200 bg-slate-50 p-5 text-center">
        <div class="text-3xl font-bold text-sky-600">
          {{ dashboard.abiertas }}
        </div>
        <div class="mt-1 text-sm text-slate-600">
          Incidencias Abiertas
        </div>
      </article>

      <article class="rounded-xl border border-slate-200 bg-slate-50 p-5 text-center">
        <div class="text-3xl font-bold text-sky-600">
          {{ dashboard.resueltas }}
        </div>
        <div class="mt-1 text-sm text-slate-600">
          Incidencias Resueltas
        </div>
      </article>
    </div>

    <div class="mt-6 grid grid-cols-1 gap-4 lg:grid-cols-2">
      <div class="rounded-xl border border-slate-200 bg-white p-4">
        <h3 class="mb-3 text-base font-semibold text-slate-700">
          Porcentaje de Incidencias por Categoría
        </h3>

        <div
          v-if="categoryStats.length > 0"
          class="grid grid-cols-1 items-center gap-5 md:grid-cols-[220px_1fr]"
        >
          <div class="mx-auto">
            <div
              class="flex h-52 w-52 items-center justify-center rounded-full"
              :style="{ background: `conic-gradient(${categoryGradient})` }"
            >
              <div class="flex h-32 w-32 flex-col items-center justify-center rounded-full bg-white text-center shadow-inner">
                <div class="text-2xl font-bold text-slate-800">
                  {{ totalIncidencias }}
                </div>
                <div class="text-xs font-medium text-slate-500">
                  Incidencias
                </div>
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
                <span
                  class="h-3 w-3 rounded-full"
                  :style="{ backgroundColor: item.color }"
                />
                <span class="text-sm text-slate-700">
                  {{ item.nombre }}
                </span>
              </div>

              <div class="text-right">
                <div class="text-sm font-semibold text-slate-800">
                  {{ item.percentLabel }}
                </div>
                <div class="text-xs text-slate-500">
                  {{ item.count }} incidencias
                </div>
              </div>
            </li>
          </ul>
        </div>

        <p
          v-else
          class="rounded-lg border border-dashed border-slate-300 bg-slate-50 px-3 py-8 text-center text-sm text-slate-500"
        >
          No hay datos de categorías para mostrar.
        </p>
      </div>

      <div class="rounded-xl border border-slate-200 bg-white p-4">
        <h3 class="mb-3 text-base font-semibold text-slate-700">
          Tiempo Medio de Resolución
        </h3>

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

            <text x="110" y="28" text-anchor="middle" class="fill-slate-500 text-[12px] font-semibold">
              0h / 72h
            </text>
            <text x="110" y="204" text-anchor="middle" class="fill-slate-500 text-[12px] font-semibold">
              36h
            </text>
            <text x="26" y="114" text-anchor="middle" class="fill-slate-500 text-[12px] font-semibold">
              54h
            </text>
            <text x="194" y="114" text-anchor="middle" class="fill-slate-500 text-[12px] font-semibold">
              18h
            </text>
          </svg>

          <div class="text-center">
            <div class="text-2xl font-bold text-sky-600">
              {{ tiempoMedioHoras.toFixed(1) }}h
            </div>
            <p class="text-sm text-slate-600">
              Tiempo medio actual de resolución
            </p>
          </div>
        </div>
      </div>
    </div>

    <div
      v-if="mostrarUbicaciones"
      class="mt-6 rounded-xl border border-slate-200 bg-white p-4"
    >
      <h3 class="mb-3 text-base font-semibold text-slate-700">
        Incidencias por Ubicación
      </h3>

      <div
        v-if="ubicacionStats.length > 0"
        class="space-y-3"
      >
        <div
          v-for="item in ubicacionStats"
          :key="item.nombre"
          :class="[
                    'rounded-lg border p-3 transition-all',
                    item === ubicacionStats[0]
                      ? 'border-red-300 bg-red-50 shadow-sm'
                      : 'border-slate-200'
                  ]"
        >
          <div class="flex items-center justify-between gap-3">
            <span class="text-sm font-medium text-slate-700">
              {{ item.nombre }}
            </span>

            <span
              :class="[
                'text-sm font-bold',
                item === ubicacionStats[0]
                  ? 'text-red-600'
                  : 'text-sky-600'
              ]"
            >
              {{ item.count }}
            </span>
          </div>

          <div class="mt-2 h-2 rounded-full bg-slate-100">
            <div
              :class="[
                      'h-2 rounded-full',
                      item === ubicacionStats[0]
                        ? 'bg-red-500'
                        : 'bg-sky-500'
                    ]"
              :style="{ width: item.percentLabel }"
            />
          </div>

          <p class="mt-1 text-xs text-slate-500">
            {{ item.percentLabel }} del total
          </p>
        </div>
      </div>

      <p
        v-else
        class="rounded-lg border border-dashed border-slate-300 bg-slate-50 px-3 py-8 text-center text-sm text-slate-500"
      >
        No hay datos de ubicaciones para mostrar.
      </p>
    </div>
  </div>
</template>

<script setup>
defineProps({
  dashboard: {
    type: Object,
    required: true
  },

  totalIncidencias: {
    type: Number,
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
  },

ubicacionStats: {
  type: Array,
  default: () => []
},

mostrarUbicaciones: {
  type: Boolean,
  default: false
}
})
</script>
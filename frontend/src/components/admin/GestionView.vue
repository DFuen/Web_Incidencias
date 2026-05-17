<template>
  <section class="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm sm:p-6">
    <div class="flex flex-wrap gap-3 border-b border-slate-200 pb-4">
      <button
        @click="activeGestionView = 'ubicaciones'"
        :class="buttonClass('ubicaciones')"
      >
        Ubicaciones
      </button>

      <button
        @click="activeGestionView = 'profesores'"
        :class="buttonClass('profesores')"
      >
        Profesores
      </button>
    </div>

    <div class="mt-6">
      <UbicacionesCrud
        v-if="activeGestionView === 'ubicaciones'"
        :ubicaciones="ubicaciones"
        @refresh="$emit('refresh')"
      />

      <ProfesoresCrud
        v-else-if="activeGestionView === 'profesores'"
        :profesores="profesores"
        @refresh="$emit('refresh')"
      />
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue'
import UbicacionesCrud from './UbicacionesCrud.vue'
import ProfesoresCrud from './ProfesoresCrud.vue'

defineProps({
  ubicaciones: {
    type: Array,
    required: true
  },

  profesores: {
    type: Array,
    required: true
  }
})

defineEmits(['refresh'])

const activeGestionView = ref('ubicaciones')

const buttonClass = (view) => [
  'rounded-lg px-4 py-2 text-sm font-semibold transition',
  activeGestionView.value === view
    ? 'bg-sky-600 text-white'
    : 'bg-slate-100 text-slate-700 hover:bg-slate-200'
]
</script>
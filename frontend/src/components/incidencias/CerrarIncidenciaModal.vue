<template>
  <div
    v-if="visible"
    class="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4"
  >
    <div class="w-full max-w-lg rounded-2xl bg-white p-6 shadow-2xl">

      <h2 class="text-xl font-bold text-slate-800">
        Cerrar incidencia
      </h2>

      <p class="mt-2 text-sm text-slate-600">
        Añade una descripción de la solución aplicada.
      </p>

      <div class="mt-4 rounded-xl bg-slate-50 p-3 text-sm">
        <p class="font-semibold text-slate-700">
          {{ incidencia?.ubicacion?.codigo }}
        </p>

        <p class="mt-1 text-slate-600">
          {{ incidencia?.descripcion }}
        </p>
      </div>

      <textarea
        v-model="descripcion"
        rows="5"
        placeholder="Describe la solución..."
        class="mt-4 w-full rounded-xl border border-slate-300 p-3 text-sm outline-none focus:border-sky-500"
      />

      <div class="mt-6 flex justify-end gap-3">
        <button
          @click="$emit('close')"
          class="rounded-xl border border-slate-300 px-4 py-2 text-sm font-medium text-slate-700 hover:bg-slate-100"
        >
          Cancelar
        </button>

        <button
          @click="confirmar"
          class="rounded-xl bg-sky-600 px-4 py-2 text-sm font-semibold text-white hover:bg-sky-700"
        >
          Confirmar cierre
        </button>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  visible: Boolean,
  incidencia: Object
})

const emit = defineEmits(['close', 'confirm'])

const descripcion = ref('')

watch(
  () => props.visible,
  (nuevo) => {
    if (nuevo) {
      descripcion.value =
        props.incidencia?.descripcionSolucion || ''
    }
  }
)

const confirmar = () => {
  emit('confirm', descripcion.value?.trim() || '')
}
</script>
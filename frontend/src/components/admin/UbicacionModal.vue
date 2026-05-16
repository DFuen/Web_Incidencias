<template>
  <div
    v-if="visible"
    class="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4"
  >
    <div class="w-full max-w-lg rounded-2xl bg-white p-6 shadow-xl">
      <div class="flex items-center justify-between">
        <h2 class="text-xl font-bold text-slate-800">
          {{ modoEdicion ? 'Editar Ubicación' : 'Nueva Ubicación' }}
        </h2>

        <button
          type="button"
          @click="$emit('close')"
          class="text-slate-500 hover:text-slate-700"
        >
          ✕
        </button>
      </div>

      <form
        class="mt-6 space-y-4"
        @submit.prevent="guardar"
      >
        <div>
          <label class="mb-1 block text-sm font-medium text-slate-700">
            Código
          </label>

          <input
            v-model="form.codigo"
            type="text"
            maxlength="10"
            class="w-full rounded-lg border border-slate-300 px-3 py-2"
            required
          />
        </div>

        <div>
          <label class="mb-1 block text-sm font-medium text-slate-700">
            Nombre
          </label>

          <input
            v-model="form.nombre"
            type="text"
            maxlength="100"
            class="w-full rounded-lg border border-slate-300 px-3 py-2"
            required
          />
        </div>

        <div class="flex justify-end gap-3 pt-4">
          <button
            type="button"
            @click="$emit('close')"
            class="rounded-lg border border-slate-300 px-4 py-2 text-sm"
          >
            Cancelar
          </button>

          <button
            type="submit"
            class="rounded-lg bg-sky-600 px-4 py-2 text-sm font-semibold text-white hover:bg-sky-700"
          >
            {{ modoEdicion ? 'Guardar cambios' : 'Crear ubicación' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },

  ubicacion: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['close', 'save'])

const form = ref({
  codigo: '',
  nombre: ''
})

const modoEdicion = ref(false)

watch(
  () => [props.ubicacion, props.visible],
  ([value, visible]) => {
    if (!visible) return

    if (value) {
      modoEdicion.value = true

      form.value = {
        codigo: value.codigo || '',
        nombre: value.nombre || ''
      }
    } else {
      modoEdicion.value = false

      form.value = {
        codigo: '',
        nombre: ''
      }
    }
  },
  { immediate: true }
)

const guardar = () => {
  emit('save', {
    ...form.value,
    id: props.ubicacion?.id || null
  })
}
</script>
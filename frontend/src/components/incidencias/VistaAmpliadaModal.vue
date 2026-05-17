<template>
  <div
    v-if="visible"
    class="fixed inset-0 z-50 flex items-center justify-center bg-slate-900/80 px-4 py-6"
    @click.self="$emit('close')"
  >
    <!-- FOTO -->
    <div
      v-if="tipo === 'foto'"
      class="max-h-[90vh] max-w-[90vw] overflow-auto rounded-2xl bg-white p-3 shadow-2xl"
    >
      <img
        v-if="!error"
        :src="contenido"
        alt="Foto ampliada"
        class="max-h-[85vh] max-w-[85vw] rounded-xl object-contain"
        @error="$emit('image-error')"
      />

      <p
        v-else
        class="px-6 py-10 text-center text-sm text-slate-600"
      >
        No proporcionado
      </p>
    </div>

    <!-- DESCRIPCIÓN -->
    <div
      v-else
      class="w-full max-w-2xl rounded-2xl bg-white p-5 shadow-2xl"
    >
      <div class="border-b border-slate-200 pb-3">
        <h3 class="text-lg font-semibold text-slate-800">
          Descripción completa
        </h3>
      </div>

      <p class="mt-4 whitespace-pre-wrap text-lg text-slate-700">
        {{ contenido }}
      </p>
    </div>
  </div>
</template>

<script setup>
defineProps({
  visible: {
    type: Boolean,
    required: true
  },

  tipo: {
    type: String,
    required: true
  },

  contenido: {
    type: String,
    default: ''
  },

  error: {
    type: Boolean,
    default: false
  }
})

defineEmits([
  'close',
  'image-error'
])
</script>

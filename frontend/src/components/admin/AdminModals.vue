<template>
  <div>
    <VistaAmpliadaModal
      :visible="vistaAmpliadaVisible"
      :tipo="vistaAmpliadaTipo"
      :contenido="vistaAmpliadaContenido"
      :error="vistaAmpliadaError"
      @close="$emit('cerrar-vista-ampliada')"
      @image-error="$emit('image-error')"
    />

    <CerrarIncidenciaModal
      :visible="modalCerrarVisible"
      @close="$emit('cerrar-modal-cierre')"
      @confirm="$emit('confirmar-cierre', $event)"
    />
  </div>
</template>

<script setup>
import VistaAmpliadaModal from '../incidencias/VistaAmpliadaModal.vue'
import CerrarIncidenciaModal from '../incidencias/CerrarIncidenciaModal.vue'

defineProps({
  vistaAmpliadaVisible: {
    type: Boolean,
    required: true
  },

  vistaAmpliadaTipo: {
    type: String,
    required: true
  },

  vistaAmpliadaContenido: {
    type: String,
    required: true
  },

  vistaAmpliadaError: {
    type: Boolean,
    required: true
  },

  modalCerrarVisible: {
    type: Boolean,
    required: true
  }
})

defineEmits([
  'cerrar-vista-ampliada',
  'image-error',
  'cerrar-modal-cierre',
  'confirmar-cierre'
])
</script>
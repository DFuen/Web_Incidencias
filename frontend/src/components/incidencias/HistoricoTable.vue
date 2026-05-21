<template>
  <section class="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm sm:p-6">

    <h2 class="text-lg font-semibold text-slate-800 sm:text-xl">
      Histórico de Mis Incidencias
    </h2>

    <div
      v-if="incidencias.length === 0"
      class="mt-4 rounded-xl border border-dashed border-slate-300 bg-slate-50 p-10 text-center text-slate-600"
    >
      <p>No tienes incidencias reportadas aún.</p>
    </div>

    <div
      v-else
      class="mt-4 grid grid-cols-1 gap-4 md:grid-cols-2 xl:grid-cols-3"
    >
      <IncidenciaCard
        v-for="inc in incidencias"
        :key="inc.id"
        :incidencia="inc"
        :foto-error-map="fotoErrorMap"
        :get-foto-url="getFotoUrl"
        :abrir-foto="abrirFoto"
        :abrir-descripcion="abrirDescripcion"
        :cambiar-estado="() => {}"
        :mostrar-acciones="false"
      />
    </div>

    <!-- MODAL FOTO / DESCRIPCIÓN -->

    <VistaAmpliadaModal
      :visible="vistaAmpliadaVisible"
      :tipo="vistaAmpliadaTipo"
      :contenido="vistaAmpliadaContenido"
      :error="vistaAmpliadaError"
      @close="cerrarVistaAmpliada"
      @image-error="vistaAmpliadaError = true"
    />

  </section>
</template>

<script setup>
import { ref } from 'vue'

import IncidenciaCard from './IncidenciaCard.vue'
import VistaAmpliadaModal from './VistaAmpliadaModal.vue'

defineProps({
  incidencias: {
    type: Array,
    required: true
  }
})

const fotoErrorMap = ref({})

const vistaAmpliadaVisible = ref(false)
const vistaAmpliadaTipo = ref('foto')
const vistaAmpliadaContenido = ref('')
const vistaAmpliadaError = ref(false)

const getFotoUrl = (fotoPath) => {
  if (!fotoPath) return ''

  if (
    fotoPath.startsWith('http://') ||
    fotoPath.startsWith('https://')
  ) {
    return fotoPath
  }

  return fotoPath
}

const abrirFoto = (fotoPath) => {
  vistaAmpliadaTipo.value = 'foto'
  vistaAmpliadaContenido.value = getFotoUrl(fotoPath)
  vistaAmpliadaError.value = false
  vistaAmpliadaVisible.value = true
}

const abrirDescripcion = (descripcion) => {
  vistaAmpliadaTipo.value = 'descripcion'
  vistaAmpliadaContenido.value =
    descripcion?.trim() || 'Sin descripción'
  vistaAmpliadaVisible.value = true
}

const cerrarVistaAmpliada = () => {
  vistaAmpliadaVisible.value = false
  vistaAmpliadaContenido.value = ''
  vistaAmpliadaError.value = false
}
</script>
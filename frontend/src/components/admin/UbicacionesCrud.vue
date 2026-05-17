<template>
  <section class="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm sm:p-5">
    <div class="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
      <h2 class="text-xl font-bold text-slate-800 sm:text-2xl">
        Ubicaciones
      </h2>

      <button
        @click="abrirCrear"
        class="w-full rounded-lg bg-sky-600 px-4 py-2 text-sm font-semibold text-white hover:bg-sky-700 sm:w-auto"
      >
        Nueva Ubicación
      </button>
    </div>

    <div
      v-if="ubicaciones.length"
      class="mt-6 grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-3"
    >
      <article
        v-for="ubicacion in ubicaciones"
        :key="ubicacion.id"
        class="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm transition hover:shadow-md"
      >
        <div class="flex items-start justify-between gap-3">
          <div class="min-w-0">
            <h3 class="text-base font-bold text-slate-800">
              {{ ubicacion.nombre }}
            </h3>

            <p class="mt-1 text-sm text-slate-600">
              Código:
              <span class="font-semibold text-slate-800">
                {{ ubicacion.codigo }}
              </span>
            </p>
          </div>

          <span class="shrink-0 rounded-full bg-sky-100 px-3 py-1 text-xs font-semibold text-sky-700">
            {{ ubicacion.codigo }}
          </span>
        </div>

        <div class="mt-5 grid grid-cols-2 gap-2">
          <button
            @click="editarUbicacion(ubicacion)"
            class="rounded-lg bg-amber-500 px-3 py-2 text-xs font-semibold text-white hover:bg-amber-600"
          >
            Editar
          </button>

          <button
            @click="eliminarUbicacion(ubicacion.id)"
            class="rounded-lg bg-red-600 px-3 py-2 text-xs font-semibold text-white hover:bg-red-700"
          >
            Eliminar
          </button>
        </div>
      </article>
    </div>

    <div
      v-else
      class="mt-6 rounded-xl border border-dashed border-slate-300 p-6 text-center text-sm text-slate-500"
    >
      No hay ubicaciones para mostrar.
    </div>

    <UbicacionModal
      :visible="modalVisible"
      :ubicacion="ubicacionSeleccionada"
      @close="modalVisible = false"
      @save="guardarUbicacion"
    />
  </section>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import UbicacionModal from './UbicacionModal.vue'

defineProps({
  ubicaciones: {
    type: Array,
    required: true
  }
})

const emit = defineEmits(['refresh'])

const modalVisible = ref(false)
const ubicacionSeleccionada = ref(null)

const abrirCrear = () => {
  ubicacionSeleccionada.value = null
  modalVisible.value = true
}

const editarUbicacion = (ubicacion) => {
  ubicacionSeleccionada.value = ubicacion
  modalVisible.value = true
}

const eliminarUbicacion = async (id) => {
  if (!confirm('¿Seguro que deseas eliminar esta ubicación?')) return

  try {
    const auth = localStorage.getItem('auth')

    await axios.delete(`/api/ubicaciones/${id}`, {
      headers: {
        Authorization: 'Basic ' + auth
      }
    })

    emit('refresh')
  } catch (error) {
    console.error(error)
    alert('Error al eliminar ubicación')
  }
}

const guardarUbicacion = async (data) => {
  try {
    const auth = localStorage.getItem('auth')

    const payload = {
      codigo: data.codigo,
      nombre: data.nombre
    }

    if (data.id) {
      await axios.put(`/api/ubicaciones/${data.id}`, payload, {
        headers: {
          Authorization: 'Basic ' + auth
        }
      })
    } else {
      await axios.post('/api/ubicaciones', payload, {
        headers: {
          Authorization: 'Basic ' + auth
        }
      })
    }

    modalVisible.value = false
    emit('refresh')
  } catch (error) {
    console.error(error)
    alert('Error al guardar ubicación')
  }
}
</script>
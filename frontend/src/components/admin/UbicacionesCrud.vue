<template>
  <div>
    <section class="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
      <div class="flex items-center justify-between">
        <h2 class="text-2xl font-bold text-slate-800">
          Ubicaciones
        </h2>

        <button
          @click="abrirCrear"
          class="rounded-lg bg-sky-600 px-4 py-2 text-sm font-semibold text-white hover:bg-sky-700"
        >
          Nueva Ubicación
        </button>
      </div>

      <div class="mt-6 overflow-hidden rounded-xl border border-slate-200">
        <table class="min-w-full divide-y divide-slate-200">
          <thead class="bg-slate-50">
            <tr>
              <th class="px-4 py-3 text-left text-sm font-semibold text-slate-700">
                Código
              </th>
              <th class="px-4 py-3 text-left text-sm font-semibold text-slate-700">
                Nombre
              </th>
              <th class="px-4 py-3 text-left text-sm font-semibold text-slate-700">
                Acciones
              </th>
            </tr>
          </thead>

          <tbody class="divide-y divide-slate-100 bg-white">
            <tr
              v-for="ubicacion in ubicaciones"
              :key="ubicacion.id"
            >
              <td class="px-4 py-3 text-slate-700">
                {{ ubicacion.codigo }}
              </td>

              <td class="px-4 py-3 text-slate-700">
                {{ ubicacion.nombre }}
              </td>

              <td class="px-4 py-3">
                <div class="flex gap-2">
                  <button
                    @click="editarUbicacion(ubicacion)"
                    class="rounded-lg bg-amber-500 px-3 py-1.5 text-xs font-semibold text-white hover:bg-amber-600"
                  >
                    Editar
                  </button>

                  <button
                    @click="eliminarUbicacion(ubicacion.id)"
                    class="rounded-lg bg-red-600 px-3 py-1.5 text-xs font-semibold text-white hover:bg-red-700"
                  >
                    Eliminar
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>

    <UbicacionModal
      :visible="modalVisible"
      :ubicacion="ubicacionSeleccionada"
      @close="modalVisible = false"
      @save="guardarUbicacion"
    />
  </div>
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
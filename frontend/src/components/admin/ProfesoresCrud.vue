<template>
  <section class="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
    <div class="flex items-center justify-between">
      <h2 class="text-2xl font-bold text-slate-800">
        Profesores
      </h2>

      <div class="flex items-center gap-3">
        <span class="text-sm font-medium text-slate-700">
          Filtro:
        </span>

        <select
          v-model="filtroRol"
          class="rounded-lg border border-slate-300 px-3 py-2 text-sm"
        >
          <option value="">
            Todos los roles
          </option>

          <option value="administrador">
            Administradores
          </option>

          <option value="profesor">
            Profesores
          </option>
        </select>

        <button
          @click="abrirCrear"
          class="rounded-lg bg-sky-600 px-4 py-2 text-sm font-semibold text-white hover:bg-sky-700"
        >
          Nuevo Profesor
        </button>
      </div>
    </div>

    <div class="mt-6 overflow-hidden rounded-xl border border-slate-200">
      <table class="min-w-full divide-y divide-slate-200">
        <thead class="bg-slate-50">
          <tr>
            <th class="px-4 py-3 text-left text-sm font-semibold text-slate-700">
              Nombre
            </th>
            <th class="px-4 py-3 text-left text-sm font-semibold text-slate-700">
              Email
            </th>
            <th class="px-4 py-3 text-left text-sm font-semibold text-slate-700">
              Rol
            </th>
            <th class="px-4 py-3 text-left text-sm font-semibold text-slate-700">
              Acciones
            </th>
          </tr>
        </thead>

        <tbody class="divide-y divide-slate-100 bg-white">
          <tr
            v-for="profesor in usuariosFiltrados"
            :key="profesor.id"
          >
            <td class="px-4 py-3 text-slate-700">
              {{ profesor.nombre }}
            </td>

            <td class="px-4 py-3 text-slate-700">
              {{ profesor.email }}
            </td>

            <td class="px-4 py-3 text-slate-700">
              {{ profesor.rol }}
            </td>

            <td class="px-4 py-3">
              <div class="flex gap-2">
                <button
                  @click="editarProfesor(profesor)"
                  class="rounded-lg bg-amber-500 px-3 py-1.5 text-xs font-semibold text-white hover:bg-amber-600"
                >
                  Editar
                </button>

                <button
                  @click="eliminarProfesor(profesor.id)"
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

    <ProfesorModal
      :visible="modalVisible"
      :profesor="profesorSeleccionado"
      @close="modalVisible = false"
      @save="guardarProfesor"
    />
  </section>
</template>

<script setup>
import { ref, computed } from 'vue'
import axios from 'axios'
import ProfesorModal from './ProfesorModal.vue'

const props = defineProps({
  profesores: {
    type: Array,
    required: true
  }
})

const emit = defineEmits(['refresh'])

const filtroRol = ref('')
const modalVisible = ref(false)
const profesorSeleccionado = ref(null)

const usuariosFiltrados = computed(() => {
  if (!filtroRol.value) {
    return props.profesores
  }

  return props.profesores.filter((usuario) =>
    usuario.rol === filtroRol.value
  )
})

const abrirCrear = () => {
  profesorSeleccionado.value = null
  modalVisible.value = true
}

const editarProfesor = (profesor) => {
  profesorSeleccionado.value = profesor
  modalVisible.value = true
}

const eliminarProfesor = async (id) => {
  if (!confirm('¿Seguro que deseas eliminar este profesor?')) return

  try {
    const auth = localStorage.getItem('auth')

    await axios.delete(`/api/usuarios/${id}`, {
      headers: {
        Authorization: 'Basic ' + auth
      }
    })

    emit('refresh')
  } catch (error) {
    console.error(error)
    alert('Error al eliminar profesor')
  }
}

const guardarProfesor = async (data) => {
  try {
    const auth = localStorage.getItem('auth')

    if (data.id) {
      await axios.put(`/api/usuarios/${data.id}`, data, {
        headers: {
          Authorization: 'Basic ' + auth
        }
      })
    } else {
      await axios.post('/api/usuarios', data, {
        headers: {
          Authorization: 'Basic ' + auth
        }
      })
    }

    modalVisible.value = false
    emit('refresh')
  } catch (error) {
    console.error(error)
    alert('Error al guardar profesor')
  }
}
</script>
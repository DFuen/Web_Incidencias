<template>
  <section class="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm sm:p-5">
    <div class="flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">
      <h2 class="text-xl font-bold text-slate-800 sm:text-2xl">
        Profesores
      </h2>

      <div class="flex flex-col gap-3 sm:flex-row sm:items-center">
        <div class="flex flex-col gap-1 sm:flex-row sm:items-center sm:gap-3">
          <span class="text-sm font-medium text-slate-700">
            Filtro:
          </span>

          <select
            v-model="filtroRol"
            class="w-full rounded-lg border border-slate-300 px-3 py-2 text-sm sm:w-auto"
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
        </div>

        <button
          @click="abrirCrear"
          class="w-full rounded-lg bg-sky-600 px-4 py-2 text-sm font-semibold text-white hover:bg-sky-700 sm:w-auto"
        >
          Nuevo Profesor
        </button>
      </div>
    </div>

    <div
      v-if="usuariosFiltrados.length"
      class="mt-6 grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-3"
    >
      <article
        v-for="profesor in usuariosFiltrados"
        :key="profesor.id"
        class="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm transition hover:shadow-md"
      >
        <div class="flex items-start justify-between gap-3">
          <div class="min-w-0">
            <h3 class="truncate text-base font-bold text-slate-800">
              {{ profesor.nombre }}
            </h3>

            <p class="mt-1 break-all text-sm text-slate-600">
              {{ profesor.email }}
            </p>
          </div>

          <span
            class="shrink-0 rounded-full px-3 py-1 text-xs font-semibold"
            :class="rolClass(profesor.rol)"
          >
            {{ profesor.rol }}
          </span>
        </div>

        <div class="mt-5 grid grid-cols-2 gap-2">
          <button
            @click="editarProfesor(profesor)"
            class="rounded-lg bg-amber-500 px-3 py-2 text-xs font-semibold text-white hover:bg-amber-600"
          >
            Editar
          </button>

          <button
            @click="eliminarProfesor(profesor.id)"
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
      No hay profesores para mostrar.
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

const rolClass = (rol) => {
  if (rol === 'administrador') {
    return 'bg-purple-100 text-purple-700'
  }

  return 'bg-sky-100 text-sky-700'
}

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
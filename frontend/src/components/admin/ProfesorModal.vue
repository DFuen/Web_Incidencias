<template>
  <div
    v-if="visible"
    class="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4"
  >
    <div class="w-full max-w-lg rounded-2xl bg-white p-6 shadow-xl">
      <div class="flex items-center justify-between">
        <h2 class="text-xl font-bold text-slate-800">
          {{ modoEdicion ? 'Editar Usuario' : 'Nuevo Usuario' }}
        </h2>

        <button
          type="button"
          @click="$emit('close')"
          class="text-slate-500 hover:text-slate-700"
        >
          ✕
        </button>
      </div>

      <form class="mt-6 space-y-4" @submit.prevent="guardar">
        <div>
          <label class="mb-1 block text-sm font-medium text-slate-700">
            Nombre
          </label>

          <input
            v-model.trim="form.nombre"
            type="text"
            class="w-full rounded-lg border border-slate-300 px-3 py-2"
            required
          />
        </div>

        <div>
          <label class="mb-1 block text-sm font-medium text-slate-700">
            Email
          </label>

          <input
            v-model.trim="form.email"
            type="email"
            autocomplete="email"
            class="w-full rounded-lg border border-slate-300 px-3 py-2"
            required
          />
        </div>

        <div>
          <label class="mb-1 block text-sm font-medium text-slate-700">
            Rol
          </label>

          <select
            v-model="form.rol"
            class="w-full rounded-lg border border-slate-300 px-3 py-2"
            required
          >
            <option value="profesor">
              Profesor
            </option>

            <option value="administrador">
              Administrador
            </option>
          </select>
        </div>

        <div v-if="!modoEdicion">
          <label class="mb-1 block text-sm font-medium text-slate-700">
            Contraseña
          </label>

          <input
            v-model="form.password"
            type="password"
            autocomplete="new-password"
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
            {{ modoEdicion ? 'Guardar cambios' : 'Crear usuario' }}
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

  profesor: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['close', 'save'])

const modoEdicion = ref(false)

const form = ref({
  nombre: '',
  email: '',
  rol: 'profesor',
  password: ''
})

watch(
  () => [props.profesor, props.visible],
  ([value, visible]) => {
    if (!visible) return

    if (value) {
      modoEdicion.value = true

      form.value = {
        id: value.id,
        nombre: value.nombre || '',
        email: value.email || '',
        rol: value.rol || 'profesor',
        password: ''
      }
    } else {
      modoEdicion.value = false

      form.value = {
        nombre: '',
        email: '',
        rol: 'profesor',
        password: ''
      }
    }
  },
  { immediate: true }
)

const guardar = () => {
  const data = {
    ...form.value,
    rol: form.value.rol.toLowerCase()
  }

  if (modoEdicion.value || !data.password) {
    delete data.password
  }

  emit('save', data)
}
</script>
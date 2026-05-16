<template>
  <section class="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm sm:p-6">
    
    <button
      @click="mostrarFormulario = !mostrarFormulario"
      class="w-full rounded-xl bg-emerald-600 px-4 py-2 text-sm font-semibold text-white transition hover:bg-emerald-700 sm:w-auto"
    >
      {{ mostrarFormulario ? 'Cancelar' : '+ Crear Nueva Incidencia' }}
    </button>

    <div
      v-if="mostrarFormulario"
      class="mt-5 rounded-xl border border-slate-200 bg-slate-50 p-4 sm:p-5"
    >
      <h2 class="text-lg font-semibold text-slate-800">
        Reportar Nueva Incidencia
      </h2>

      <form
        @submit.prevent="crearIncidencia"
        class="mt-4 space-y-4"
      >
        
        <!-- UBICACION -->
        <div>
          <label class="mb-1 block text-sm font-semibold text-slate-700">
            Ubicación (Aula) *
          </label>

          <select
            v-model="nueva.ubicacionId"
            class="w-full rounded-lg border border-slate-300 bg-white px-3 py-2"
          >
            <option value="">
              Selecciona una ubicación
            </option>

            <option
              v-for="u in ubicaciones"
              :key="u.id"
              :value="u.id"
            >
              {{ u.codigo }} - {{ u.descripcion }}
            </option>
          </select>

          <span
            v-if="ubicacionError"
            class="mt-1 block text-xs font-medium text-rose-600"
          >
            {{ ubicacionError }}
          </span>
        </div>

        <!-- CATEGORIA -->
        <div>
          <label class="mb-1 block text-sm font-semibold text-slate-700">
            Categoría *
          </label>

          <select
            v-model="nueva.categoriaId"
            class="w-full rounded-lg border border-slate-300 bg-white px-3 py-2"
          >
            <option value="">
              Selecciona una categoría
            </option>

            <option
              v-for="c in categorias"
              :key="c.id"
              :value="c.id"
            >
              {{ c.nombre }}
            </option>
          </select>

          <span
            v-if="categoriaError"
            class="mt-1 block text-xs font-medium text-rose-600"
          >
            {{ categoriaError }}
          </span>
        </div>

        <!-- DESCRIPCION -->
        <div>
          <label class="mb-1 block text-sm font-semibold text-slate-700">
            Descripción del Problema *
          </label>

          <textarea
            v-model="nueva.descripcion"
            placeholder="Describe el problema"
            class="min-h-[110px] w-full resize-y rounded-lg border border-slate-300 bg-white px-3 py-2"
          ></textarea>

          <span
            v-if="descripcionError"
            class="mt-1 block text-xs font-medium text-rose-600"
          >
            {{ descripcionError }}
          </span>
        </div>

        <!-- FOTO -->
        <div>
          <label class="mb-1 block text-sm font-semibold text-slate-700">
            Foto (Opcional)
          </label>

          <input
            type="file"
            accept="image/png, image/jpeg"
            @change="onFileChange"
            class="block w-full rounded-lg border border-slate-300 bg-white px-3 py-2 text-sm"
          />
        </div>

        <!-- BOTON -->
        <button
          type="submit"
          class="w-full rounded-xl bg-sky-600 px-5 py-2.5 text-sm font-semibold text-white transition hover:bg-sky-700 sm:w-auto"
        >
          Enviar Incidencia
        </button>

        <!-- MENSAJES -->
        <span
          v-if="formError"
          class="block rounded-lg border border-rose-200 bg-rose-50 px-3 py-2 text-sm text-rose-700"
        >
          {{ formError }}
        </span>

        <span
          v-if="formSuccess"
          class="block rounded-lg border border-emerald-200 bg-emerald-50 px-3 py-2 text-sm text-emerald-700"
        >
          {{ formSuccess }}
        </span>

      </form>
    </div>

  </section>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

const emit = defineEmits(['created'])

defineProps({
  ubicaciones: Array,
  categorias: Array
})

const mostrarFormulario = ref(false)

const nueva = ref({
  ubicacionId: '',
  categoriaId: '',
  descripcion: '',
  foto: null
})

const ubicacionError = ref('')
const categoriaError = ref('')
const descripcionError = ref('')
const formError = ref('')
const formSuccess = ref('')

const onFileChange = (e) => {
  nueva.value.foto = e.target.files[0]
}

const validar = () => {
  ubicacionError.value = !nueva.value.ubicacionId
    ? 'La ubicación es obligatoria'
    : ''

  categoriaError.value = !nueva.value.categoriaId
    ? 'La categoría es obligatoria'
    : ''

  descripcionError.value = !nueva.value.descripcion
    ? 'La descripción es obligatoria'
    : ''

  return !(
    ubicacionError.value ||
    categoriaError.value ||
    descripcionError.value
  )
}

const crearIncidencia = async () => {
  formError.value = ''
  formSuccess.value = ''

  if (!validar()) {
    formError.value = 'Corrige los errores del formulario'
    return
  }

  try {
    const auth = localStorage.getItem('auth')
    const email = localStorage.getItem('user')

    const user = await axios.get(
      '/api/usuarios/email/' + email,
      {
        headers: {
          Authorization: 'Basic ' + auth
        }
      }
    )

    let fotoPath = null

    if (nueva.value.foto) {
      const formData = new FormData()

      formData.append('file', nueva.value.foto)

      const uploadRes = await axios.post(
        '/api/files/upload',
        formData,
        {
          headers: {
            Authorization: 'Basic ' + auth,
            'Content-Type': 'multipart/form-data'
          }
        }
      )

      fotoPath =
        typeof uploadRes.data === 'string'
          ? uploadRes.data
          : null
    }

    const incidencia = {
      ubicacion: {
        id: parseInt(nueva.value.ubicacionId)
      },

      categoria: {
        id: parseInt(nueva.value.categoriaId)
      },

      descripcion: nueva.value.descripcion,

      usuarioCreador: {
        id: user.data.id
      },

      foto: fotoPath,

      estado: 'PENDIENTE'
    }

    await axios.post(
      '/api/incidencias',
      incidencia,
      {
        headers: {
          Authorization: 'Basic ' + auth
        }
      }
    )

    formSuccess.value =
      '¡Incidencia creada correctamente!'

    nueva.value = {
      ubicacionId: '',
      categoriaId: '',
      descripcion: '',
      foto: null
    }

    mostrarFormulario.value = false

    emit('created')

  } catch (error) {
    console.error(error)

    formError.value =
      'Error al crear la incidencia'
  }
}
</script>
<template>
  <div class="min-h-screen bg-gradient-to-b from-slate-50 via-white to-cyan-50 px-4 py-6 sm:px-6">
    <div class="mx-auto max-w-6xl space-y-6">
      <h1 class="border-b-4 border-emerald-600 pb-3 text-xl font-bold tracking-tight text-slate-900 sm:text-2xl">
        Mis Incidencias
      </h1>

      <section class="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm sm:p-6">
        <button
          @click="mostrarFormulario = !mostrarFormulario"
          class="w-full rounded-xl bg-emerald-600 px-4 py-2 text-sm font-semibold text-white transition hover:bg-emerald-700 sm:w-auto"
        >
          {{ mostrarFormulario ? 'Cancelar' : '+ Crear Nueva Incidencia' }}
        </button>

        <div v-if="mostrarFormulario" class="mt-5 rounded-xl border border-slate-200 bg-slate-50 p-4 sm:p-5">
          <h2 class="text-lg font-semibold text-slate-800">Reportar Nueva Incidencia</h2>
          <form @submit.prevent="crearIncidencia" class="mt-4 space-y-4">
            <div>
              <label class="mb-1 block text-sm font-semibold text-slate-700">Ubicacion (Aula) *</label>
              <select
                v-model="nueva.ubicacionId"
                class="w-full rounded-lg border border-slate-300 bg-white px-3 py-2">
                <option value="" disabled selected>Selecciona una ubicacion</option>
                <option v-for="u in ubicaciones" :key="u.id" :value="u.id">
                  {{ u.codigo }} - {{ u.descripcion }}
                </option>
              </select>
              <span v-if="ubicacionError" class="mt-1 block text-xs font-medium text-rose-600">{{ ubicacionError }}</span>
            </div>

            <div>
              <label class="mb-1 block text-sm font-semibold text-slate-700">Categoria *</label>
              <select
                v-model="nueva.categoriaId"
                @blur="validarCategoria"
                class="w-full rounded-lg border border-slate-300 bg-white px-3 py-2 text-sm text-slate-800 shadow-sm outline-none transition focus:border-sky-500 focus:ring-2 focus:ring-sky-200">
                <option value="" disabled selected>Selecciona una categoria</option>
                <option v-for="c in categorias" :key="c.id" :value="c.id">
                  {{ c.nombre }}
                </option>
              </select>
              <span v-if="categoriaError" class="mt-1 block text-xs font-medium text-rose-600">{{ categoriaError }}</span>
            </div>

            <div>
              <label class="mb-1 block text-sm font-semibold text-slate-700">Descripcion del Problema *</label>
              <textarea
                v-model="nueva.descripcion"
                @blur="validarDescripcion"
                placeholder="Describe el problema en detalle"
                class="min-h-[110px] w-full resize-y rounded-lg border border-slate-300 bg-white px-3 py-2 text-sm text-slate-800 shadow-sm outline-none transition focus:border-sky-500 focus:ring-2 focus:ring-sky-200"
              ></textarea>
              <span v-if="descripcionError" class="mt-1 block text-xs font-medium text-rose-600">{{ descripcionError }}</span>
            </div>

            <div>
              <label class="mb-1 block text-sm font-semibold text-slate-700">Foto (Opcional)</label>
              <input
                type="file"
                @change="onFileChange"
                accept="image/png, image/jpeg"
                class="block w-full rounded-lg border border-slate-300 bg-white px-3 py-2 text-sm text-slate-700 file:mr-3 file:rounded-md file:border-0 file:bg-slate-200 file:px-3 file:py-1.5 file:text-sm file:font-semibold file:text-slate-700 hover:file:bg-slate-300"
              />
            </div>

            <button
              type="submit"
              class="w-full rounded-xl bg-sky-600 px-5 py-2.5 text-sm font-semibold text-white transition hover:bg-sky-700 sm:w-auto"
            >
              Enviar Incidencia
            </button>

            <span v-if="formError" class="block rounded-lg border border-rose-200 bg-rose-50 px-3 py-2 text-sm text-rose-700">{{ formError }}</span>
            <span
              v-if="formSuccess"
              class="block rounded-lg border border-emerald-200 bg-emerald-50 px-3 py-2 text-sm font-medium text-emerald-700"
            >
              {{ formSuccess }}
            </span>
          </form>
        </div>
      </section>

      <section class="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm sm:p-6">
        <h2 class="text-lg font-semibold text-slate-800 sm:text-xl">Historico de Mis Incidencias</h2>
        <div
          v-if="incidencias.length === 0"
          class="mt-4 rounded-xl border border-dashed border-slate-300 bg-slate-50 p-10 text-center text-slate-600"
        >
          <p>No tienes incidencias reportadas aun.</p>
        </div>

        <div v-else class="mt-4 overflow-hidden rounded-xl border border-slate-200">
          <div class="overflow-x-auto">
            <table class="min-w-full divide-y divide-slate-200 text-sm">
              <thead class="bg-slate-50">
                <tr>
                  <th class="px-4 py-3 text-left font-semibold text-slate-700">Ubicacion</th>
                  <th class="px-4 py-3 text-left font-semibold text-slate-700">Categoria</th>
                  <th class="px-4 py-3 text-left font-semibold text-slate-700">Descripcion</th>
                  <th class="px-4 py-3 text-left font-semibold text-slate-700">Estado</th>
                  <th class="px-4 py-3 text-left font-semibold text-slate-700">Fecha Creacion</th>
                  <th class="px-4 py-3 text-left font-semibold text-slate-700">Fecha Resolucion</th>
                </tr>
              </thead>
              <tbody class="divide-y divide-slate-100 bg-white">
                <tr v-for="inc in incidencias" :key="inc.id" class="hover:bg-slate-50">
                  <td class="px-4 py-3 text-slate-700">{{ inc.ubicacion.codigo }} - {{ inc.ubicacion.descripcion }}</td>
                  <td class="px-4 py-3 text-slate-700">{{ inc.categoria.nombre }}</td>
                  <td class="max-w-xs truncate px-4 py-3 text-slate-700">{{ inc.descripcion }}</td>
                  <td class="px-4 py-3"><span :class="estadoBadgeClass(inc.estado)">{{ inc.estado }}</span></td>
                  <td class="px-4 py-3 text-slate-600">{{ formatFecha(inc.fechaCreacion) }}</td>
                  <td class="px-4 py-3 text-slate-600">{{ inc.fechaResolucion ? formatFecha(inc.fechaResolucion) : '-' }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import LogoutButton from "../components/LogoutButton.vue";

const incidencias = ref([])
const ubicaciones = ref([])
const categorias = ref([])
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

const fetchData = async () => {
  try {
    const auth = localStorage.getItem('auth')
    const email = localStorage.getItem('user')

    // USUARIO
    const user = await axios.get('/api/usuarios/email/' + email, {
      headers: {
        Authorization: 'Basic ' + auth
      }
    })

    // INCIDENCIAS
    const incidenciasRes = await axios.get(
      '/api/incidencias?usuarioId=' + user.data.id,
      {
        headers: {
          Authorization: 'Basic ' + auth
        }
      }
    )

    incidencias.value =
      incidenciasRes.data.content || incidenciasRes.data

    // UBICACIONES
    const ubicacionesRes = await axios.get('/api/ubicaciones')
    ubicaciones.value = ubicacionesRes.data

    // CATEGORIAS
    const categoriasRes = await axios.get('/api/categorias')
    categorias.value = categoriasRes.data

  } catch (error) {
    console.error('ERROR:', error)
    formError.value = 'Error al cargar datos'
  }
}

onMounted(fetchData)

const onFileChange = e => {
  nueva.value.foto = e.target.files[0]
}

const validarUbicacion = () => {
  ubicacionError.value = !nueva.value.ubicacionId ? 'La ubicación es obligatoria' : ''
}

const validarCategoria = () => {
  categoriaError.value = !nueva.value.categoriaId ? 'La categoría es obligatoria' : ''
}

const validarDescripcion = () => {
  descripcionError.value = !nueva.value.descripcion ? 'La descripción es obligatoria' : ''
}

const crearIncidencia = async () => {
  formError.value = ''
  formSuccess.value = ''
  validarUbicacion()
  validarCategoria()
  validarDescripcion()
  
  if (ubicacionError.value || categoriaError.value || descripcionError.value) {
    formError.value = 'Por favor, corrige los errores antes de enviar.'
    return
  }

  try {
    const auth = localStorage.getItem('auth')
    const email = localStorage.getItem('user')

    const user = await axios.get('/api/usuarios/email/' + email, {
    headers: { Authorization: 'Basic ' + auth }
    })

    if (!user.data) {
      formError.value = 'Usuario no encontrado'
      return
    }


    let fotoPath = null
    if (nueva.value.foto) {
      const formData = new FormData()
      formData.append('file', nueva.value.foto)

      const uploadRes = await axios.post('/api/files/upload', formData, {
        headers: {
          Authorization: 'Basic ' + auth,
          'Content-Type': 'multipart/form-data'
        }
      })
      fotoPath = typeof uploadRes.data === 'string' ? uploadRes.data : null
    }

    const incidencia = {
      ubicacion: { id: parseInt(nueva.value.ubicacionId) },
      categoria: { id: parseInt(nueva.value.categoriaId) },
      descripcion: nueva.value.descripcion,
      usuarioCreador: { id: user.data.id },
      foto: fotoPath,
      estado: 'PENDIENTE'
    }

    await axios.post('/api/incidencias', incidencia, {
      headers: { Authorization: 'Basic ' + auth }
    })

    formSuccess.value = '¡Incidencia creada exitosamente!'
    nueva.value = { ubicacionId: '', categoriaId: '', descripcion: '', foto: null }
    mostrarFormulario.value = false
    setTimeout(fetchData, 1000)
  } catch (error) {
    formError.value = 'Error al crear la incidencia. Intenta de nuevo.'
    console.error(error)
  }
}

const formatFecha = (fecha) => {
  return new Date(fecha).toLocaleString('es-ES')
}

const estadoBadgeClass = (estado) => {
  const base = 'inline-flex rounded-md px-2.5 py-1 text-xs font-semibold'

  if (estado === 'PENDIENTE') return `${base} bg-amber-200 text-amber-900`
  if (estado === 'EN_PROCESO') return `${base} bg-cyan-200 text-cyan-900`
  if (estado === 'REALIZADA') return `${base} bg-emerald-200 text-emerald-900`

  return `${base} bg-slate-200 text-slate-800`
}
</script>

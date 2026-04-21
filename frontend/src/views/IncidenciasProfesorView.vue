<template>
  <div class="profesor-container">
    <h1>Mis Incidencias</h1>
    
    <div class="form-section">
      <button @click="mostrarFormulario = !mostrarFormulario" class="btn-crear">
        {{ mostrarFormulario ? 'Cancelar' : '+ Crear Nueva Incidencia' }}
      </button>
      
      <div v-if="mostrarFormulario" class="formulario">
        <h2>Reportar Nueva Incidencia</h2>
        <form @submit.prevent="crearIncidencia">
          <div class="form-group">
            <label>Ubicación (Aula) *</label>
            <select v-model="nueva.ubicacionId" @blur="validarUbicacion" class="form-input">
              <option value="">Selecciona una ubicación</option>
              <option v-for="u in ubicaciones" :key="u.id" :value="u.id">
                {{ u.codigo }} - {{ u.descripcion }}
              </option>
            </select>
            <span v-if="ubicacionError" class="error">{{ ubicacionError }}</span>
          </div>

          <div class="form-group">
            <label>Categoría *</label>
            <select v-model="nueva.categoriaId" @blur="validarCategoria" class="form-input">
              <option value="">Selecciona una categoría</option>
              <option v-for="c in categorias" :key="c.id" :value="c.id">{{ c.nombre }}</option>
            </select>
            <span v-if="categoriaError" class="error">{{ categoriaError }}</span>
          </div>

          <div class="form-group">
            <label>Descripción del Problema *</label>
            <textarea 
              v-model="nueva.descripcion" 
              @blur="validarDescripcion"
              placeholder="Describe el problema en detalle"
              class="form-textarea"
            ></textarea>
            <span v-if="descripcionError" class="error">{{ descripcionError }}</span>
          </div>

          <div class="form-group">
            <label>Foto (Opcional)</label>
            <input type="file" @change="onFileChange" accept="image/png, image/jpeg" class="form-input" />
          </div>

          <button type="submit" class="btn-submit">Enviar Incidencia</button>
          <span v-if="formError" class="error">{{ formError }}</span>
          <span v-if="formSuccess" class="success">{{ formSuccess }}</span>
        </form>
      </div>
    </div>

    <div class="historico-section">
      <h2>Histórico de Mis Incidencias</h2>
      <div v-if="incidencias.length === 0" class="empty-state">
        <p>No tienes incidencias reportadas aún.</p>
      </div>
      <table v-else class="incidencias-table">
        <thead>
          <tr>
            <th>Ubicación</th>
            <th>Categoría</th>
            <th>Descripción</th>
            <th>Estado</th>
            <th>Fecha Creación</th>
            <th>Fecha Resolución</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="inc in incidencias" :key="inc.id">
            <td>{{ inc.ubicacion.codigo }} - {{ inc.ubicacion.descripcion }}</td>
            <td>{{ inc.categoria.nombre }}</td>
            <td>{{ inc.descripcion.substring(0, 50) }}...</td>
            <td><span class="estado-badge" :class="inc.estado">{{ inc.estado }}</span></td>
            <td>{{ formatFecha(inc.fechaCreacion) }}</td>
            <td>{{ inc.fechaResolucion ? formatFecha(inc.fechaResolucion) : '-' }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const incidencias = ref([])
const ubicaciones = ref([])
const categorias = ref([])
const mostrarFormulario = ref(false)
const nueva = ref({ ubicacionId: '', categoriaId: '', descripcion: '', foto: null })
const ubicacionError = ref('')
const categoriaError = ref('')
const descripcionError = ref('')
const formError = ref('')
const formSuccess = ref('')

const fetchData = async () => {
  try {
    const auth = localStorage.getItem('auth')
    const email = localStorage.getItem('user')
    
    const user = await axios.get('/api/usuarios/email/' + email, {
      headers: { Authorization: 'Basic ' + auth }
    })
    
    const res = await axios.get('/api/incidencias?usuarioId=' + user.data.id, {
      headers: { Authorization: 'Basic ' + auth }
    })
    incidencias.value = res.data.content || res.data
    
    ubicaciones.value = (await axios.get('/api/ubicaciones', {
      headers: { Authorization: 'Basic ' + auth }
    })).data
    
    categorias.value = (await axios.get('/api/categorias', {
      headers: { Authorization: 'Basic ' + auth }
    })).data
  } catch (error) {
    console.error('Error al cargar datos:', error)
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

    const incidencia = {
      ubicacion: { id: parseInt(nueva.value.ubicacionId) },
      categoria: { id: parseInt(nueva.value.categoriaId) },
      descripcion: nueva.value.descripcion,
      usuarioCreador: { id: user.data.id },
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
</script>

<style scoped>
.profesor-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

h1 {
  color: #333;
  border-bottom: 3px solid #007bff;
  padding-bottom: 10px;
  margin-bottom: 20px;
}

h2 {
  color: #555;
  margin-top: 30px;
  margin-bottom: 15px;
}

.form-section {
  margin: 20px 0;
}

.btn-crear {
  background-color: #28a745;
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  font-weight: bold;
}

.btn-crear:hover {
  background-color: #218838;
}

.formulario {
  background-color: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  margin-top: 15px;
  border: 1px solid #dee2e6;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  font-weight: bold;
  margin-bottom: 5px;
  color: #333;
}

.form-input, .form-textarea {
  display: block;
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-family: inherit;
  font-size: 14px;
}

.form-textarea {
  resize: vertical;
  min-height: 100px;
}

.form-input:focus, .form-textarea:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 5px rgba(0, 123, 255, 0.3);
}

.error {
  color: #dc3545;
  font-size: 12px;
  margin-top: 3px;
  display: block;
}

.success {
  color: #28a745;
  font-size: 14px;
  margin-top: 5px;
  display: block;
  padding: 10px;
  background-color: #d4edda;
  border: 1px solid #c3e6cb;
  border-radius: 4px;
}

.btn-submit {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  font-weight: bold;
}

.btn-submit:hover {
  background-color: #0056b3;
}

.historico-section {
  margin-top: 40px;
}

.empty-state {
  background-color: #f8f9fa;
  padding: 30px;
  border-radius: 4px;
  text-align: center;
  color: #666;
}

.incidencias-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 15px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.incidencias-table thead {
  background-color: #f8f9fa;
}

.incidencias-table th {
  padding: 12px;
  text-align: left;
  border-bottom: 2px solid #dee2e6;
  font-weight: bold;
  color: #333;
}

.incidencias-table td {
  padding: 12px;
  border-bottom: 1px solid #dee2e6;
}

.incidencias-table tbody tr:hover {
  background-color: #f9f9f9;
}

.estado-badge {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: bold;
  color: white;
  display: inline-block;
}

.estado-badge.PENDIENTE {
  background-color: #ffc107;
  color: #333;
}

.estado-badge.EN_PROCESO {
  background-color: #17a2b8;
}

.estado-badge.REALIZADA {
  background-color: #28a745;
}

@media (max-width: 768px) {
  .profesor-container {
    padding: 10px;
  }

  .incidencias-table {
    font-size: 12px;
  }

  .incidencias-table th, .incidencias-table td {
    padding: 8px;
  }

  .btn-crear, .btn-submit {
    width: 100%;
  }
}
</style>

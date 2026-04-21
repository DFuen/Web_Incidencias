<template>
  <div class="admin-container">
    <h1>Gestión de Incidencias del Centro</h1>
    
    <!-- Sección de Filtros -->
    <div class="filtros-section">
      <h2>Filtros</h2>
      <div class="filtros-grid">
        <div class="filtro-group">
          <label>Categoría:</label>
          <select v-model="filtros.categoriaId" @change="aplicarFiltros" class="form-input">
            <option value="">Todas</option>
            <option v-for="c in categorias" :key="c.id" :value="c.id">{{ c.nombre }}</option>
          </select>
        </div>

        <div class="filtro-group">
          <label>Estado:</label>
          <select v-model="filtros.estado" @change="aplicarFiltros" class="form-input">
            <option value="">Todos</option>
            <option value="PENDIENTE">Abierto (Pendiente)</option>
            <option value="EN_PROCESO">En Proceso</option>
            <option value="REALIZADA">Cerrado (Realizada)</option>
          </select>
        </div>

        <div class="filtro-group">
          <button @click="limpiarFiltros" class="btn-limpiar">Limpiar Filtros</button>
        </div>
      </div>
    </div>

    <!-- Sección de Incidencias -->
    <div class="incidencias-section">
      <h2>Listado de Incidencias</h2>
      <div v-if="incidenciasFilteradas.length === 0" class="empty-state">
        <p>No hay incidencias que coincidan con los filtros aplicados.</p>
      </div>
      <table v-else class="incidencias-table">
        <thead>
          <tr>
            <th>Ubicación</th>
            <th>Categoría</th>
            <th>Usuario</th>
            <th>Descripción</th>
            <th>Estado</th>
            <th>Fecha Creación</th>
            <th>Fecha Cierre</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="inc in incidenciasFilteradas" :key="inc.id">
            <td>{{ inc.ubicacion.codigo }} - {{ inc.ubicacion.descripcion }}</td>
            <td>{{ inc.categoria.nombre }}</td>
            <td>{{ inc.usuarioCreador.nombre }}</td>
            <td>{{ inc.descripcion.substring(0, 40) }}...</td>
            <td><span class="estado-badge" :class="inc.estado">{{ inc.estado }}</span></td>
            <td>{{ formatFecha(inc.fechaCreacion) }}</td>
            <td>{{ inc.fechaResolucion ? formatFecha(inc.fechaResolucion) : '-' }}</td>
            <td>
              <button 
                v-if="inc.estado === 'PENDIENTE'" 
                @click="cambiarEstado(inc, 'EN_PROCESO')"
                class="btn-avanzar"
              >
                → En Proceso
              </button>
              <template v-else-if="inc.estado === 'EN_PROCESO'">
                <div class="acciones-estado">
                  <button 
                    @click="cambiarEstado(inc, 'PENDIENTE', true)"
                    class="btn-avanzar"
                  >
                    ↩ Volver a Pendiente
                  </button>
                  <button 
                    @click="cambiarEstado(inc, 'REALIZADA')"
                    class="btn-cerrar-estado"
                  >
                    ✓ Cerrar
                  </button>
                </div>
              </template>
              <span v-else class="estado-final">Cerrada</span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Sección de Dashboard -->
    <div class="dashboard-section">
      <h2>Dashboard de Métricas</h2>
      <div class="metricas-grid">
        <div class="metrica-card">
          <div class="metrica-valor">{{ dashboard.abiertas }}</div>
          <div class="metrica-label">Incidencias Abiertas</div>
        </div>
        <div class="metrica-card">
          <div class="metrica-valor">{{ dashboard.resueltas }}</div>
          <div class="metrica-label">Incidencias Resueltas</div>
        </div>
        <div class="metrica-card">
          <div class="metrica-valor">{{ dashboard.tiempoMedioHoras.toFixed(1) }}h</div>
          <div class="metrica-label">Tiempo Medio Resolución</div>
        </div>
      </div>

      <div class="top-items">
        <div class="top-section">
          <h3>Top 5 Ubicaciones</h3>
          <ul>
            <li v-for="(item, idx) in dashboard.topUbicaciones" :key="idx">
              {{ item[0] }}: {{ item[1] }} incidencias
            </li>
          </ul>
        </div>
        <div class="top-section">
          <h3>Top 5 Categorías</h3>
          <ul>
            <li v-for="(item, idx) in dashboard.topCategorias" :key="idx">
              {{ item[0] }}: {{ item[1] }} incidencias
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

const todasIncidencias = ref([])
const categorias = ref([])
const filtros = ref({ categoriaId: '', estado: '' })
const dashboard = ref({ abiertas: 0, resueltas: 0, tiempoMedioHoras: 0, topUbicaciones: [], topCategorias: [] })

const incidenciasFilteradas = computed(() => {
  return todasIncidencias.value.filter(inc => {
    let cumple = true
    if (filtros.value.categoriaId) {
      cumple = cumple && inc.categoria.id == filtros.value.categoriaId
    }
    if (filtros.value.estado) {
      cumple = cumple && inc.estado === filtros.value.estado
    }
    return cumple
  })
})

const fetchData = async () => {
  try {
    const auth = localStorage.getItem('auth')
    
    // Cargar todas las incidencias
    const res = await axios.get('/api/incidencias?size=1000', {
      headers: { Authorization: 'Basic ' + auth }
    })
    todasIncidencias.value = res.data.content || res.data
    
    // Cargar categorías
    categorias.value = (await axios.get('/api/categorias', {
      headers: { Authorization: 'Basic ' + auth }
    })).data
    
    // Cargar dashboard
    const dashboardRes = await axios.get('/api/incidencias/dashboard', {
      headers: { Authorization: 'Basic ' + auth }
    })
    dashboard.value = dashboardRes.data
  } catch (error) {
    console.error('Error al cargar datos:', error)
  }
}

onMounted(fetchData)

const aplicarFiltros = () => {
  // Los filtros se aplican automáticamente con el computed
}

const limpiarFiltros = () => {
  filtros.value = { categoriaId: '', estado: '' }
}

const cambiarEstado = async (incidencia, nuevoEstado, confirmarRegreso = false) => {
  try {
    if (incidencia.estado === 'REALIZADA') {
      alert('No se puede modificar una incidencia ya realizada')
      return
    }

    if (confirmarRegreso) {
      const confirmed = window.confirm('¿Seguro que quieres cambiar a un estado anterior?')
      if (!confirmed) return
    }

    const auth = localStorage.getItem('auth')
    await axios.put(`/api/incidencias/${incidencia.id}/estado`, 
      { estado: nuevoEstado },
      { headers: { Authorization: 'Basic ' + auth } }
    )
    // Recargar datos
    fetchData()
  } catch (error) {
    console.error('Error al cambiar estado:', error)
    alert('Error al cambiar el estado de la incidencia')
  }
}

const formatFecha = (fecha) => {
  return new Date(fecha).toLocaleString('es-ES')
}
</script>

<style scoped>
.admin-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

h1 {
  color: #333;
  border-bottom: 3px solid #007bff;
  padding-bottom: 10px;
  margin-bottom: 30px;
}

h2 {
  color: #555;
  margin-top: 30px;
  margin-bottom: 15px;
  font-size: 22px;
}

h3 {
  color: #666;
  font-size: 16px;
  margin-bottom: 10px;
}

.filtros-section {
  background-color: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 30px;
}

.filtros-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 15px;
  margin-top: 15px;
}

.filtro-group {
  display: flex;
  flex-direction: column;
}

.filtro-group label {
  font-weight: bold;
  margin-bottom: 5px;
  color: #333;
}

.form-input {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  background-color: white;
}

.form-input:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 5px rgba(0, 123, 255, 0.3);
}

.btn-limpiar {
  background-color: #6c757d;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
  align-self: flex-end;
  margin-top: 20px;
}

.btn-limpiar:hover {
  background-color: #5a6268;
}

.incidencias-section {
  margin: 30px 0;
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

.btn-avanzar, .btn-cerrar-estado {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  font-weight: bold;
}

.btn-cerrar-estado {
  background-color: #28a745;
}

.btn-avanzar:hover {
  background-color: #0056b3;
}

.btn-cerrar-estado:hover {
  background-color: #218838;
}

.estado-final {
  color: #666;
  font-weight: bold;
}

.acciones-estado {
  display: flex;
  gap: 10px;
  justify-content: space-between;
  align-items: center;
}

.acciones-estado .btn-avanzar,
.acciones-estado .btn-cerrar-estado {
  flex: 1;
  width: 50%;
  min-width: 0;
}

.dashboard-section {
  background-color: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  margin-top: 40px;
}

.metricas-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin: 20px 0;
}

.metrica-card {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  text-align: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.metrica-valor {
  font-size: 32px;
  font-weight: bold;
  color: #007bff;
  margin-bottom: 10px;
}

.metrica-label {
  color: #666;
  font-size: 14px;
}

.top-items {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.top-section {
  background-color: white;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.top-section ul {
  list-style: none;
  padding: 0;
}

.top-section li {
  padding: 8px 0;
  border-bottom: 1px solid #eee;
  color: #555;
}

.top-section li:last-child {
  border-bottom: none;
}

@media (max-width: 768px) {
  .admin-container {
    padding: 10px;
  }

  .filtros-grid {
    grid-template-columns: 1fr;
  }

  .incidencias-table {
    font-size: 12px;
  }

  .incidencias-table th, .incidencias-table td {
    padding: 8px;
  }

  .metricas-grid {
    grid-template-columns: 1fr;
  }

  .top-items {
    grid-template-columns: 1fr;
  }
}
</style>

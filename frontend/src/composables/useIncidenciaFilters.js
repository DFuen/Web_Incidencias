import { ref, computed } from 'vue'

export const useIncidenciaFilters = (todasIncidencias) => {
  const filtros = ref({
    categoriaId: '',
    estado: '',
    fechaDesde: '',
    fechaHasta: ''
  })

  const incidenciasFilteradas = computed(() => {
    return todasIncidencias.value.filter((inc) => {
      let cumple = true

      if (filtros.value.categoriaId) {
        cumple = cumple && inc.categoria.id == filtros.value.categoriaId
      }

      if (filtros.value.estado) {
        cumple = cumple && inc.estado === filtros.value.estado
      }

      if (filtros.value.fechaDesde) {
        const fechaIncidencia = new Date(inc.fechaCreacion)
        const fechaDesde = new Date(filtros.value.fechaDesde)

        cumple = cumple && fechaIncidencia >= fechaDesde
      }

      if (filtros.value.fechaHasta) {
        const fechaIncidencia = new Date(inc.fechaCreacion)
        const fechaHasta = new Date(filtros.value.fechaHasta)

        fechaHasta.setHours(23, 59, 59, 999)

        cumple = cumple && fechaIncidencia <= fechaHasta
      }

      return cumple
    })
  })

  const limpiarFiltros = () => {
    filtros.value = {
      categoriaId: '',
      estado: '',
      fechaDesde: '',
      fechaHasta: ''
    }
  }

  return {
    filtros,
    incidenciasFilteradas,
    limpiarFiltros
  }
}
export const useIncidenciaUtils = () => {
  const getFotoUrl = (fotoPath) => {
    if (!fotoPath) return ''

    if (
      fotoPath.startsWith('http://') ||
      fotoPath.startsWith('https://')
    ) {
      return fotoPath
    }

    return fotoPath
  }

  const formatFecha = (fecha) => {
    if (!fecha) return '-'

    return new Date(fecha).toLocaleString('es-ES')
  }

  const formatUbicacion = (ubicacion) => {
    if (!ubicacion) return '-'

    const codigo = ubicacion.codigo || '-'
    const nombre = ubicacion.nombre || 'Sin nombre'

    return `${codigo} - ${nombre}`
  }

  return {
    getFotoUrl,
    formatFecha,
    formatUbicacion
  }
}
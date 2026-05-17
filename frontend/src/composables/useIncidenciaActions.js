import { ref } from 'vue'
import axios from 'axios'

export const useIncidenciaActions = (fetchData) => {
  const vistaAmpliadaVisible = ref(false)
  const vistaAmpliadaTipo = ref('foto')
  const vistaAmpliadaContenido = ref('')
  const vistaAmpliadaError = ref(false)

  const modalCerrarVisible = ref(false)
  const incidenciaSeleccionada = ref(null)

  const fotoErrorMap = ref({})

  const cambiarEstado = async (incidencia, nuevoEstado) => {
    if (incidencia.estado === 'REALIZADA') {
      alert('No se puede modificar una incidencia ya realizada')
      return
    }

    if (nuevoEstado === 'REALIZADA') {
      incidenciaSeleccionada.value = incidencia
      modalCerrarVisible.value = true
      return
    }

    await actualizarEstado(incidencia, nuevoEstado)
  }

  const actualizarEstado = async (
    incidencia,
    nuevoEstado,
    descripcionSolucion = null
  ) => {
    try {
      const auth = localStorage.getItem('auth')

      await axios.put(
        `/api/incidencias/${incidencia.id}/estado`,
        {
          estado: nuevoEstado,
          descripcionSolucion
        },
        {
          headers: {
            Authorization: 'Basic ' + auth
          }
        }
      )

      await fetchData()
    } catch (error) {
      console.error('Error al cambiar estado:', error)
      alert('Error al cambiar el estado de la incidencia')
    }
  }

  const confirmarCerrarIncidencia = async (descripcion) => {
    await actualizarEstado(
      incidenciaSeleccionada.value,
      'REALIZADA',
      descripcion
    )

    modalCerrarVisible.value = false
    incidenciaSeleccionada.value = null
  }

  const getFotoUrl = (fotoPath) => {
    if (!fotoPath) return '#'

    if (
      fotoPath.startsWith('http://') ||
      fotoPath.startsWith('https://')
    ) {
      return fotoPath
    }

    return fotoPath
  }

  const abrirFoto = (fotoPath) => {
    vistaAmpliadaTipo.value = 'foto'
    vistaAmpliadaContenido.value = getFotoUrl(fotoPath)
    vistaAmpliadaError.value = false
    vistaAmpliadaVisible.value = true
  }

  const abrirDescripcion = (descripcion) => {
    vistaAmpliadaTipo.value = 'descripcion'
    vistaAmpliadaContenido.value = descripcion
    vistaAmpliadaVisible.value = true
  }

  const cerrarVistaAmpliada = () => {
    vistaAmpliadaVisible.value = false
    vistaAmpliadaContenido.value = ''
    vistaAmpliadaError.value = false
  }

  return {
    vistaAmpliadaVisible,
    vistaAmpliadaTipo,
    vistaAmpliadaContenido,
    vistaAmpliadaError,
    modalCerrarVisible,
    incidenciaSeleccionada,
    fotoErrorMap,
    cambiarEstado,
    actualizarEstado,
    confirmarCerrarIncidencia,
    getFotoUrl,
    abrirFoto,
    abrirDescripcion,
    cerrarVistaAmpliada
  }
}
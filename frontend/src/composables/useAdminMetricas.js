import { computed } from 'vue'

export const useAdminMetricas = (todasIncidencias, dashboard) => {
  const tiempoMedioHoras = computed(() => {
    const raw = Number(dashboard.value.tiempoMedioHoras)
    return Number.isFinite(raw) && raw >= 0 ? raw : 0
  })

  const maxHorasReloj = 72
  const radioReloj = 92
  const tiempoMedioCircunferencia = 2 * Math.PI * radioReloj

  const tiempoMedioHorasEscaladas = computed(() => {
    return Math.min(Math.max(tiempoMedioHoras.value, 0), maxHorasReloj)
  })

  const tiempoMedioArco = computed(() => {
    return (tiempoMedioHorasEscaladas.value / maxHorasReloj) * tiempoMedioCircunferencia
  })

  const tiempoMedioAngulo = computed(() => {
    return tiempoMedioHorasEscaladas.value * (360 / maxHorasReloj)
  })

  const categoryPalette = [
    '#0ea5e9',
    '#22c55e',
    '#f59e0b',
    '#a855f7',
    '#ef4444',
    '#14b8a6',
    '#f97316',
    '#6366f1'
  ]

  const categoryStats = computed(() => {
    const total = todasIncidencias.value.length
    if (!total) return []

    const countByCategory = todasIncidencias.value.reduce((acc, inc) => {
      const nombre = inc?.categoria?.nombre || 'Sin categoria'
      acc[nombre] = (acc[nombre] || 0) + 1
      return acc
    }, {})

    return Object.entries(countByCategory)
      .sort((a, b) => b[1] - a[1])
      .map(([nombre, count], index) => {
        const percent = (count / total) * 100

        return {
          nombre,
          count,
          percent,
          percentLabel: `${percent.toFixed(1)}%`,
          color: categoryPalette[index % categoryPalette.length]
        }
      })
  })

  const categoryGradient = computed(() => {
    if (!categoryStats.value.length) {
      return '#e2e8f0 0 100%'
    }

    let acumulado = 0

    const segmentos = categoryStats.value.map((item) => {
      const inicio = acumulado
      acumulado += item.percent

      return `${item.color} ${inicio.toFixed(2)}% ${acumulado.toFixed(2)}%`
    })

    return segmentos.join(', ')
  })

  const ubicacionStats = computed(() => {
    const total = todasIncidencias.value.length
    if (!total) return []

    const countByUbicacion = todasIncidencias.value.reduce((acc, inc) => {
      const codigo = inc?.ubicacion?.codigo || 'Sin código'
      const nombre = inc?.ubicacion?.nombre || 'Sin ubicación'
      const key = `${codigo} - ${nombre}`

      acc[key] = (acc[key] || 0) + 1

      return acc
    }, {})

    return Object.entries(countByUbicacion)
      .sort((a, b) => b[1] - a[1])
      .map(([nombre, count]) => ({
        nombre,
        count,
        percentLabel: `${((count / total) * 100).toFixed(1)}%`
      }))
  })

  return {
    tiempoMedioHoras,
    tiempoMedioArco,
    tiempoMedioCircunferencia,
    tiempoMedioAngulo,
    categoryStats,
    categoryGradient,
    ubicacionStats
  }
}
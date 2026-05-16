<template>
  <span :class="badgeClass">
    {{ estadoTexto }}
  </span>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  estado: {
    type: String,
    required: true
  }
})

const estadoTexto = computed(() => {
  const estados = {
    PENDIENTE: 'PENDIENTE',
    EN_PROCESO: 'EN PROCESO',
    REALIZADA: 'REALIZADA'
  }

  return estados[props.estado] || props.estado
})

const badgeClass = computed(() => {
  const base =
    'inline-flex rounded-md px-2.5 py-1 text-xs font-semibold'

  if (props.estado === 'PENDIENTE') {
    return `${base} bg-amber-200 text-amber-900`
  }

  if (props.estado === 'EN_PROCESO') {
    return `${base} bg-cyan-200 text-cyan-900`
  }

  if (props.estado === 'REALIZADA') {
    return `${base} bg-emerald-200 text-emerald-900`
  }

  return `${base} bg-slate-200 text-slate-800`
})
</script>
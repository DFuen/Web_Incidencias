<template>
  <button
    type="button"
    class="group relative cursor-pointer overflow-hidden border-4 border-rose-600 bg-white px-6 py-2 text-sm font-semibold text-rose-600 transition-colors duration-300 hover:text-white max-md:text-white"
    style="clip-path: polygon(0 0, 100% 0, 100% 78%, 92% 100%, 0 100%, 0 22%)"
    @click="$emit('click')"
  >
    <span class="relative z-10">
      Cerrar sesión
    </span>

    <span
      class="absolute left-1/2 top-1/2 z-0 h-[500%] w-[8%] -translate-x-1/2 -translate-y-1/2 rotate-[-60deg] bg-white transition-all duration-300 group-hover:w-full group-hover:rotate-[-90deg] group-hover:bg-rose-600 max-md:w-full max-md:rotate-[-90deg] max-md:bg-rose-600"
    />
  </button>
</template>

<script setup>
defineEmits(['click'])
</script>
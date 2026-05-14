<template>

  <!-- ===== MÓVIL ===== -->
  <div class="flex flex-col gap-3 lg:hidden">

    <button
      @click="$emit('toggle-menu')"
      class="flex items-center justify-between rounded-xl border border-slate-200 bg-white px-4 py-3 shadow-sm"
    >
      <span class="font-semibold text-slate-700">
        Menú
      </span>

      <svg
        class="h-5 w-5 text-slate-700 transition-transform duration-300"
        :class="menuExpanded ? 'rotate-90' : ''"
        fill="none"
        stroke="currentColor"
        viewBox="0 0 24 24"
        stroke-width="2"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          d="M9 5l7 7-7 7"
        />
      </svg>
    </button>

    <transition
      enter-active-class="transition-all duration-300 ease-out"
      enter-from-class="opacity-0 -translate-y-2"
      enter-to-class="opacity-100 translate-y-0"
      leave-active-class="transition-all duration-200 ease-in"
      leave-from-class="opacity-100 translate-y-0"
      leave-to-class="opacity-0 -translate-y-2"
    >
      <div
        v-if="menuExpanded"
        class="flex flex-col gap-2 rounded-2xl border border-slate-200 bg-white p-3 shadow-sm"
      >
        <button
          v-for="item in adminMenu"
          :key="item.id"
          @click="$emit('change-view', item.id)"
          class="rounded-xl px-4 py-3 text-left text-sm font-semibold transition"
          :class="
            activeAdminView === item.id
              ? 'bg-sky-100 text-sky-700'
              : 'text-slate-600 hover:bg-slate-100'
          "
        >
          {{ item.label }}
        </button>
      </div>
    </transition>
  </div>

  <!-- ===== ESCRITORIO ===== -->
  <div class="hidden lg:flex items-center gap-3">

    <button
      @click="$emit('toggle-menu')"
      class="flex items-center gap-2 rounded-xl border border-slate-200 bg-white px-4 py-3 shadow-sm transition hover:bg-slate-50"
    >
      <svg
        class="h-5 w-5 text-slate-700 transition-transform duration-300"
        :class="menuExpanded ? 'rotate-180' : ''"
        fill="none"
        stroke="currentColor"
        viewBox="0 0 24 24"
        stroke-width="2"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          d="M9 5l7 7-7 7"
        />
      </svg>

      <span class="font-semibold text-slate-700">
        Menú
      </span>
    </button>

    <transition
      enter-active-class="transition-all duration-300 ease-out"
      enter-from-class="opacity-0 -translate-x-5"
      enter-to-class="opacity-100 translate-x-0"
      leave-active-class="transition-all duration-200 ease-in"
      leave-from-class="opacity-100 translate-x-0"
      leave-to-class="opacity-0 -translate-x-5"
    >
      <div
        v-if="menuExpanded"
        class="flex items-center gap-2 rounded-2xl border border-slate-200 bg-white p-2 shadow-lg"
      >
        <button
          v-for="item in adminMenu"
          :key="item.id"
          @click="$emit('change-view', item.id)"
          class="rounded-xl px-4 py-2 text-sm font-semibold transition"
          :class="
            activeAdminView === item.id
              ? 'bg-sky-100 text-sky-700'
              : 'text-slate-600 hover:bg-slate-100'
          "
        >
          {{ item.label }}
        </button>
      </div>
    </transition>

  </div>

</template>
<script setup>
defineProps({
  menuExpanded: Boolean,
  activeAdminView: String,
  adminMenu: Array
})

defineEmits(['toggle-menu', 'change-view'])
</script>
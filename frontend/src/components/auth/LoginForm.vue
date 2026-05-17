<template>
  <div class="w-full max-w-md rounded-3xl border border-slate-200 bg-white p-8 shadow-2xl">

    <!-- HEADER -->
    <div class="mb-8 text-center">
      <h1 class="text-3xl font-bold tracking-tight text-slate-800">
        Iniciar sesión
      </h1>

      <p class="mt-2 text-sm text-slate-500">
        Accede a la plataforma de incidencias
      </p>
    </div>

    <!-- FORM -->
    <form @submit.prevent="emit('submit')" class="space-y-5">

      <!-- EMAIL -->
      <div>
        <label
          for="email"
          class="mb-2 block text-sm font-semibold text-slate-700"
        >
          Correo electrónico
        </label>

        <input
          id="email"
          :value="email"
          @input="emit('update:email', $event.target.value)"
          type="email"
          placeholder="correo@ejemplo.com"
          autocomplete="email"
          class="w-full rounded-xl border border-slate-300 px-4 py-3 text-sm outline-none transition focus:border-sky-500 focus:ring-4 focus:ring-sky-100"
        />

        <p
          v-if="emailError"
          class="mt-2 text-sm font-medium text-red-500"
        >
          {{ emailError }}
        </p>
      </div>

      <!-- PASSWORD -->
      <div>
        <label
          for="password"
          class="mb-2 block text-sm font-semibold text-slate-700"
        >
          Contraseña
        </label>

        <input
          id="password"
          :value="password"
          @input="emit('update:password', $event.target.value)"
          type="password"
          placeholder="********"
          autocomplete="current-password"
          class="w-full rounded-xl border border-slate-300 px-4 py-3 text-sm outline-none transition focus:border-sky-500 focus:ring-4 focus:ring-sky-100"
        />

        <p
          v-if="passwordError"
          class="mt-2 text-sm font-medium text-red-500"
        >
          {{ passwordError }}
        </p>
      </div>

      <!-- ERROR -->
      <div
        v-if="error"
        class="rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm font-medium text-red-600"
      >
        {{ error }}
      </div>

      <!-- BUTTON -->
      <button
        type="submit"
        class="w-full rounded-xl bg-sky-600 px-4 py-3 font-semibold text-white shadow-md transition hover:bg-sky-700 hover:shadow-lg active:scale-[0.98]"
      >
        Entrar
      </button>

    </form>
  </div>
</template>

<script setup>
defineProps({
  email: String,
  password: String,
  error: String,
  emailError: String,
  passwordError: String
})

const emit = defineEmits([
  'update:email',
  'update:password',
  'submit'
])
</script>
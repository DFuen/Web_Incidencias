import { createRouter, createWebHistory } from 'vue-router'
import LoginView from './views/LoginView.vue'
import IncidenciasProfesorView from './views/IncidenciasProfesorView.vue'
import IncidenciasAdminView from './views/IncidenciasAdminView.vue'

const routes = [
  { path: '/', component: LoginView, meta: { requiresAuth: false } },
  { path: '/incidencias', component: IncidenciasProfesorView, meta: { requiresAuth: true } },
  { path: '/admin', component: IncidenciasAdminView, meta: { requiresAuth: true, requiredRole: 'administrador' } }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from) => {
  const auth = localStorage.getItem('auth')
  const rol = localStorage.getItem('rol')

  // Si la ruta requiere autenticación
  if (to.meta.requiresAuth) {
    if (!auth) {
      // No autenticado, redirigir a login
      return '/'
    }

    // Verificar rol si es requerido
    if (to.meta.requiredRole && rol !== to.meta.requiredRole) {
      // Rol incorrecto, redirigir según rol
      if (rol === 'administrador') {
        return '/admin'
      } else {
        return '/incidencias'
      }
    }
  } else if (to.path === '/' && auth) {
    // Si está autenticado y trata de ir al login, redirigir según rol
    if (rol === 'administrador') {
      return '/admin'
    } else {
      return '/incidencias'
    }
  }

  return true
})

export default router

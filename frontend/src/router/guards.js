import { useAuthStore } from '../stores/auth'

export async function authGuard(to, from, next) {
    const auth = useAuthStore()

    // Carrega o usuário do servidor na primeira visita
    if (!auth.initialized) {
        await auth.fetchMe()
    }

    const requiresAuth = to.meta?.requiresAuth
    const requiredPerm = to.meta?.permission  // ex: 'catalog'
    const ownerOnly    = to.meta?.ownerOnly

    // Rota pública
    if (!requiresAuth) return next()

    // Não autenticado
    if (!auth.isLoggedIn) return next('/login')

    // Suspenso: desloga e avisa
    if (auth.isSuspended) {
        await auth.logout()
        return next('/login?suspended=1')
    }

    // Rota de admin acessada por customer
    if (to.path.startsWith('/admin') && !auth.isStaff) {
        return next('/')
    }

    // Rota exclusiva do owner
    if (ownerOnly && !auth.isOwner) {
        return next('/admin/acesso-negado')
    }

    // Rota com permissão específica
    if (requiredPerm && !auth.can(requiredPerm)) {
        return next('/admin/acesso-negado')
    }

    next()
}
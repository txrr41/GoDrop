import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../layouts/MainLayout.vue'
import Home from '../pages/Home.vue'
import Auth from '../components/Auth.vue'
import Product from '../pages/Product.vue'
import Checkout from '../pages/Checkout.vue'
import PaymentSuccess from '../pages/PaymentSuccess.vue'
import PaymentFailed from '../pages/PaymentFailed.vue'
import Order from '../pages/Order.vue'
import UserPerm from '../pages/UserPerm.vue'
import ProductCatalog from '../pages/ProductCatalog.vue'
import ApiDocumentation from '../pages/ApiDocumentation.vue'
import Dashboard from '../pages/Dashboard.vue'
import BeDropper from '../pages/BeDropper.vue'
import UserProfile from '../pages/UserProfile.vue'
import AcceptDroppers from '../pages/AcceptDroppers.vue'
import DropperStoreLanding from '../pages/DropperStoreLanding.vue'
import NotFound from '../pages/NotFound.vue'

async function permissionGuard(to, _from, next) {
    const { useAuthStore } = await import('../stores/auth.js')
    const auth = useAuthStore()

    if (!auth.initialized) await auth.init()

    const requiresAuth = to.meta?.requiresAuth
    const requiredPerm = to.meta?.permission
    const ownerOnly    = to.meta?.ownerOnly
    const staffOnly    = to.meta?.staffOnly  // rotas que precisam ser pelo menos staff

    if (!requiresAuth) return next()

    if (!auth.isLogged) return next('/login')

    if (auth.isSuspended) {
        await auth.logout()
        return next('/login?suspended=1')
    }

    if (ownerOnly && !auth.isOwner) return next('/not-found')


    if (staffOnly && !auth.isStaff && !auth.isOwner) return next('/not-found')

    if (requiredPerm && !auth.can(requiredPerm)) return next('/not-found')

    next()
}

const routes = [
    { path: '/', redirect: '/home' },

    { path: '/home',     component: MainLayout, children: [{ path: '', component: Home }] },
    { path: '/login',    component: Auth },
    { path: '/catalogo', component: MainLayout, children: [{ path: '', component: ProductCatalog }] },
    { path: '/api',      component: ApiDocumentation },
    { path: '/checkout', component: Checkout },
    { path: '/payment-success', component: PaymentSuccess },
    { path: '/payment-failed',  component: PaymentFailed },
    { path: '/perfil',   component: UserProfile },
    { path: '/loja-info', component: DropperStoreLanding },

    { path: '/orders', component: Order, meta: { requiresAuth: true } },

    {
        path: '/produtos',
        component: MainLayout,
        meta: { requiresAuth: true, staffOnly: true, permission: 'catalog' },
        children: [{ path: '', component: Product }],
    },
    {
        path: '/admin',
        component: MainLayout,
        children: [
            {
                path: 'pedidos',
                component: () => import('../pages/AdminOrder.vue'),
                meta: { requiresAuth: true, staffOnly: true, permission: 'orders' },
            },
        ],
    },
    {
        path: '/oferta/cadastro',
        component: MainLayout,
        meta: { requiresAuth: true, staffOnly: true, permission: 'marketing' },
        children: [{ path: '', component: () => import('../pages/OffersPage.vue') }],
    },
    {
        path: '/usuarios',
        component: MainLayout,
        meta: { requiresAuth: true, ownerOnly: true },
        children: [{ path: '', component: UserPerm }],
    },
    {
        path: '/metricas',
        component: MainLayout,
        meta: { requiresAuth: true, ownerOnly: true },
        children: [{ path: '', component: Dashboard }],
    },
    {
        path: '/pedidos/droppers',
        component: MainLayout,
        meta: { requiresAuth: true, staffOnly: true },
        children: [{ path: '', component: AcceptDroppers }],
    },
    {
        path: '/dropper',
        component: BeDropper,
        meta: { requiresAuth: false },
    },
    {
        path: '/minha-loja',
        component: MainLayout,
        meta: { requiresAuth: true },
        children: [{ path: '', component: () => import('../pages/DropperStoreSetup.vue') }],
    },
    {
        path: '/dropper/dashboard',
        component: MainLayout,
        meta: { requiresAuth: true },
        children: [{ path: '', component: () => import('../pages/DropperStoreDashboard.vue') }],
    },
    {
        path: '/dropper/stripe/return',
        redirect: '/dropper/dashboard?stripe=return',
    },
    {
        path: '/dropper/stripe/refresh',
        redirect: '/dropper/dashboard?stripe=refresh',
    },
    { path: '/loja/:slug', component: () => import('../pages/PublicStore.vue')
    },
    { path: '/not-found', component: NotFound
    },
    { path: '/:pathMatch(.*)*', component: NotFound
    },
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

router.beforeEach(permissionGuard)

export default router
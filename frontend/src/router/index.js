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

async function permissionGuard(to, _from, next) {
    // Lazy import do store (evita problemas de inicialização)
    const {useAuthStore} = await import('../stores/auth.js')
    const auth = useAuthStore()

    if (!auth.initialized) {
        await auth.init()
    }

    const requiresAuth = to.meta?.requiresAuth
    const requiredPerm = to.meta?.permission   // ex: 'catalog'
    const ownerOnly = to.meta?.ownerOnly

    if (!requiresAuth) return next()

    if (!auth.isLogged) return next('/login')

    if (auth.isSuspended) {
        await auth.logout()
        return next('/login?suspended=1')
    }

    if (ownerOnly && !auth.isOwner) {
        return next('/acesso-negado')
    }

    if (requiredPerm && !auth.can(requiredPerm)) {
        return next('/acesso-negado')
    }

    next()
}

const routes = [
    {path: '/', redirect: '/home'},

    {
        path: '/home',
        component: MainLayout,
        children: [{path: '', component: Home}],
    },
    {
        path: '/login',
        component: Auth,
    },
    {
        path: '/catalogo',
        component: MainLayout,
        children: [{path: '', component: ProductCatalog}],
    },
    {
        path: '/api',
        component: ApiDocumentation,
    },
    {
        path: '/checkout',
        component: Checkout,
    },
    {
        path: '/payment-success',
        component: PaymentSuccess,
    },
    {
        path: '/payment-failed',
        component: PaymentFailed,
    },
    {
        path: '/orders',
        component: Order,
        meta: {requiresAuth: true},
    },

    {
        path: '/produtos',
        component: MainLayout,
        meta: {requiresAuth: true, permission: 'catalog'},
        children: [{path: '', component: Product}],
    },

    {
        path: '/admin',
        component: MainLayout,
        children: [
            {
                path: 'pedidos',
                component: () => import('../pages/AdminOrder.vue'),
                meta: {requiresAuth: true, permission: 'orders'},
            },
        ],
    },

    {
        path: '/ofertas',
        component: MainLayout,
        meta: {requiresAuth: true, permission: 'marketing'},
        children: [
            {
                path: '',
                component: () => import('../pages/OffersPage.vue'),
            },
        ],
    },

    {
        path: '/usuarios',
        component: MainLayout,
        meta: {requiresAuth: true, ownerOnly: true},
        children: [{path: '', component: UserPerm}],
    },

    {
        path: '/acesso-negado',
        component: MainLayout,
        children: [
            {
                path: '',
                component: () => import('../pages/AcessDenied.vue'),
                meta: {requiresAuth: true},
            },
        ],
    },
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

router.beforeEach(permissionGuard)

export default router
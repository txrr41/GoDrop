import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../layouts/MainLayout.vue'
import Home from '../pages/Home.vue'
import Auth from "../components/Auth.vue"
import Product from "../pages/Product.vue";
import Checkout from "../pages/Checkout.vue";
import PaymentSuccess from "../pages/PaymentSuccess.vue";
import PaymentFailed from "../pages/PaymentFailed.vue";
import Order from "../pages/Order.vue";
import UserPerm from "../pages/UserPerm.vue";
import ApiDocumentation from "../pages/ApiDocumentation.vue";
import ProductCatalog from "../pages/ProductCatalog.vue";

const routes = [
    {
        path: '/',
        redirect: '/home'
    },
    {
        path: '/home',
        component: MainLayout,
        children: [
            {
                path: '',
                component: Home
            }
        ]
    },
    {
        path: '/login',
        component: Auth
    },
    {
        path: '/produtos',
        component: MainLayout,
        children: [
            {
                path: '',
                component: Product
            }
        ]
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
        path: '/admin',
        component: MainLayout,
        children: [
            {
                path: 'pedidos',
                component: () => import('../pages/AdminOrder.vue')
            }
        ]
    },
    {
        path: '/orders',
        component: Order
    },
    {
        path: '/usuarios',
        component: MainLayout,
        children: [
            {
                path: '',
                component: UserPerm
            }
        ]
    },
    {
        path: '/api',
        component: ApiDocumentation
    },
    {
        path: '/catalogo',
        component: MainLayout,
        children: [
            {
                path: '',
                component: ProductCatalog
            }
        ]
    },




];

export default createRouter({
    history: createWebHistory(),
    routes
})
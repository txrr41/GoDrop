import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../layouts/MainLayout.vue'
import Home from '../pages/Home.vue'
import Auth from "../components/Auth.vue"
import Product from "../pages/Product.vue";

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
    }
];

export default createRouter({
    history: createWebHistory(),
    routes
})
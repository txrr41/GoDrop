import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../layouts/MainLayout.vue'
import Home from '../pages/Home.vue'
import Auth from "../components/Auth.vue"

const routes = [
    {
        path: '/',
        redirect: '/home'  // Redireciona / para /home
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
    }
]

export default createRouter({
    history: createWebHistory(),
    routes
})
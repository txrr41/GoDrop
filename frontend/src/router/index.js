import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../layouts/MainLayout.vue'
import Home from '../pages/Home.vue'
import Login from '../components/auth.vue'
import Auth from "../components/Auth.vue";

const routes = [
    {
        path: '/',
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

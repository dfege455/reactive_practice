import { createWebHistory, createRouter } from "vue-router";
import Layout from '../layout/index.vue'
export const constantRouter = [
    {
        path: '/redirect',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '/redirect/:path(.*)',
                component: () => import('../views/redirect/index.vue')
            }
        ]
    },
    {
        path: '/login',
        component: () => import('../views/login.vue'),
        hidden: true
    },
    {
        path: '/register',
        component: () => import('../views/register.vue'),
        name: 'Register'
    },
    {
        path: '',
        component: Layout,
        redirect: '/index',
        children: [
            {
                path: '/index',
                component: () => import('../views/index.vue'),
                name: 'Index',
            },
            {
                path: '/invitation',
                component: () => import('../views/invitation/index.vue'),
                name: 'Invitation'
            }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes: constantRouter,

})

export default router
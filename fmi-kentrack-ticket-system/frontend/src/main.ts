import {createApp} from 'vue'
import 'material-icons/iconfont/material-icons.css';
import "quasar/dist/quasar.prod.css";
import App from './App.vue'
import {Dialog, Notify, Quasar} from "quasar";
import {createRouter, createWebHistory} from "vue-router";
import Home from "./pages/Home.vue"
import LoggedInPage from "./pages/logged-in-page.vue"
import DashboardsPage from "./pages/dashboards-page.vue"
import BoardPage from "./pages/board-page.vue"
import IssuesPage from "./pages/issues-page.vue"
import IssuePage from "./pages/issue-page.vue"
import LoginPage from "./pages/login-page.vue"
import {userIsLoggedIn} from "./services/UserService";

export const routes = [
    {
        path: '/',
        component: LoggedInPage,
        beforeEnter: (to, from) => {
            if (!userIsLoggedIn()) return '/login'
            return true
        },
        redirect: to => ({path: `/home`}),
        children: [
            {path: '/home', component: Home},
            {path: '/dashboards', component: DashboardsPage},
            {path: 'dashboard/:id(\\d+)', component: BoardPage, props: true},
            {path: '/issues', component: IssuesPage},
            {path: '/issue/:id(\\d+)', component: IssuePage, props: true},
        ]
    },
    {path: '/login', component: LoginPage, props: false},
]

export const router = createRouter({
    history: createWebHistory(),
    routes,
})

createApp(App)
    .use(Quasar, {
        plugins: {
            Dialog,
            Notify
        },
        config: {
            loadingBar: {
                color: 'purple',
                size: '200px',
                position: 'bottom'
            }
        }
    })
    .use(router)
    .mount("#app");

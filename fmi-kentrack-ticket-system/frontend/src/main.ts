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
import {currentUserHasRole, userIsLoggedIn} from "./services/UserService";
import UnauthorizedPage from "./pages/unauthorized-page.vue"
import PageNotFound from "./pages/page-not-found.vue";
import ProfileSettings from "./pages/profile-settings.vue";
import AdminUsersPage from "./pages/admin-users-page.vue";
import AdminManageUserPage from "./pages/admin-manage-user-page.vue";
import {Roles} from "./model/Roles";

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
            {path: '/profile-settings', component: ProfileSettings, props: true},
            {
                path: '/users',
                component: AdminUsersPage,
                name: 'users',
                beforeEnter: (to, from) => currentUserHasRole(Roles.MAINTAINER) ? true : '/unauthorized'
            },
            {
                path: '/users/user/:username',
                name: 'user',
                props: true,
                component: AdminManageUserPage,
                beforeEnter: (to, from) => currentUserHasRole(Roles.MAINTAINER) ? true : '/unauthorized'
            },
        ]
    },
    {path: '/login', component: LoginPage, props: false},
    {
        path: `/:pathMatch(.*)`,
        component: PageNotFound,
        beforeEnter: (to, from) => {
            if (!userIsLoggedIn()) return '/login'
            return true
        }
    },
    {
        path: `/unauthorized`,
        component: UnauthorizedPage
    }
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

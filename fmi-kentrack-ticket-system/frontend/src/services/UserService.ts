import axios, {AxiosResponse} from "axios";
import {useLocalStorage} from "@vueuse/core";
import {UserView} from "../model/UserView";
import {Roles} from "../model/Roles";


export const login = async (username: string | null, password: string | null): Promise<AxiosResponse> =>
    await axios.post<string>(`/login`, {username: username, password: password}, {
        baseURL: "/auth",
    })

export const logout = async () =>
    await axios.post(`/logout`, null, {
        baseURL: "/auth",
    })

export const storeUser = (user: UserView) => useLocalStorage('kentrack-user', user)
export const clearUserStorage = () => localStorage.clear()

export const getCurrentUser = (): UserView => JSON.parse(<string>localStorage.getItem('kentrack-user')) as UserView

export const userIsLoggedIn = (): boolean => localStorage.getItem('kentrack-user') != null

export const currentUserHasRole = (role: Roles): boolean => {
    const currentUser: UserView = getCurrentUser()
    return currentUser != null && currentUser.roles.includes(role.toString());
}
export const updateUserInLocalStorage = (user: UserView) => localStorage.setItem('kentrack-user', JSON.stringify(user));

import axios, {AxiosResponse} from "axios";
import {useLocalStorage} from "@vueuse/core";
import {User} from "../model/User";
import {users} from "../model/mocked-date";


export const login = async (email: string, password: string): Promise<AxiosResponse> =>
        await axios.post<string>(`/login`, {username: email, password}, {
            baseURL: "/auth",
        })

export const logout = async () =>
        await axios.post(`/logout`, null, {
            baseURL: "/auth",
        })

export const storeUser = (user: User) => useLocalStorage('kentrack-user', user)
export const clearUserStorage = () => localStorage.clear()

export const getCurrentUser = (): User => JSON.parse(<string>localStorage.getItem('kentrack-user')) as User

export const userIsLoggedIn = (): boolean => localStorage.getItem('kentrack-user') != null

export const currentUserHasRole = (role: string): boolean => {
    const currentUser: User = getCurrentUser()
    return currentUser != null && currentUser.roles.includes(role);
}
export const updateUserInLocalStorage = (user: User) => localStorage.setItem('kentrack-user', JSON.stringify(user));



export const login1 = async (username: string, password: string): Promise<User | undefined> => users.value.find(u => u.username == username)

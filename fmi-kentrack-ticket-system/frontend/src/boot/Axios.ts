import axios from 'axios'
import {setupApiInterceptorsTo} from "../interceptors/ApiInterceptors";


const api = axios.create({baseURL: '/api'})
export const setUpAxios = quasar => setupApiInterceptorsTo(api, quasar);

export {axios, api}
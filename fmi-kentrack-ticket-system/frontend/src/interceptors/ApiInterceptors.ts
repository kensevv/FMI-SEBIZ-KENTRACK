import {AxiosError, AxiosInstance, AxiosRequestConfig, AxiosResponse} from "axios";
import {QVueGlobals} from 'quasar'
import {Queue} from "./Queue";
import {router} from "../main";

// Interceptors for '/api' URL prefix
const notificationQueue: Map<string, Queue<Function>> = new Map<string, Queue<Function>>()

export const setupApiInterceptorsTo = (axiosInstance: AxiosInstance, quasar: QVueGlobals): AxiosInstance => {
    axiosInstance.interceptors.request.use(onRequest(quasar), onRequestError(quasar));
    axiosInstance.interceptors.response.use(onResponse(), onResponseError(quasar));
    return axiosInstance;
};

const excludedUrlsFromNotifications = ['/get-latest-rvm-change-times',]

const onRequest = (quasar: QVueGlobals) => (config: AxiosRequestConfig): AxiosRequestConfig => {
    if (config.method != 'get' && config.url && !excludedUrlsFromNotifications.includes(config.url)) {
        if (!notificationQueue.get(config.url)) {
            notificationQueue.set(config.url, new Queue<Function>())
        }
        notificationQueue.get(config.url)?.push(quasar.notify({
            progress: true,
            type: 'ongoing',
            position: 'top-right',
            message: config['notificationMessages']?.progressMessage ?? "Please wait...",
        }))
    }
    return config;
};

const onResponse = () => (response: AxiosResponse): AxiosResponse => {
    if (response.config.url && notificationQueue.get(response.config.url) && notificationQueue.get(response.config.url)?.isNotEmpty()) {
        // @ts-ignore
        notificationQueue.get(response.config.url).pop()(successNotification(response))
    }
    return response;
};

const onRequestError = (quasar: QVueGlobals) => (error: AxiosError): Promise<AxiosError> => {
    return onErrorAction(error, quasar);
};

const onResponseError = (quasar: QVueGlobals) => (error: AxiosError): Promise<AxiosError> => {
    return onErrorAction(error, quasar)
};

const onErrorAction = (error: AxiosError, quasar: QVueGlobals) => {
    if (error.config.url && notificationQueue.get(error.config.url) && notificationQueue.get(error.config.url)?.isNotEmpty()) {
        // @ts-ignore
        notificationQueue.get(error.config.url).pop()(failedNotification(error))
    }
    if (error.response?.status == 401) {
        router.push("/login");
    } else if (error.response?.status == 404) {
        router.push("/404");
    } else {
        quasar.dialog({
            title: error.response?.statusText ?? "Something went wrong",
            message: JSON.stringify(error.response),
            persistent: true,
            position: "right",
            ok: {
                push: true,
                color: 'negative'
            },
        })
    }

    return Promise.reject(error);
};

const successNotification = (response: AxiosResponse) => ({
    type: 'positive',
    progress: true,
    position: 'top-right',
    timeout: 1000,
    message: response.config['notificationMessages']?.successMessage ?? "Successful operation"
});

const failedNotification = (error: AxiosError) => ({
    type: 'negative',
    progress: true,
    position: 'top-right',
    timeout: 1000,
    message: error.config['notificationMessages']?.errorMessage ?? "Operation Failed"
});
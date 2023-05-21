import {Dialog} from "quasar";

export const dateTimeToGermanLocaleString = (date) => {
    if (!date) return ''

    let dateToConvert: Date = date

    if (typeof date === 'string') {
        dateToConvert = new Date(date)
    }

    return dateToConvert.toLocaleDateString('de-DE', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
    })
}

export const promiseDialog = <T>(dialogComponent, props) => new Promise<T>((resolve) => {
    Dialog.create({
        component: dialogComponent,
        componentProps: props,
        cancel: true,
        ok: {
            color: 'primary'
        },
        persistent: true
    })
        .onOk((response) => resolve(response))
        .onCancel(() => {
        })
});


export const commentPromiseDialog = (): Promise<string> => new Promise<string>((resolve) => {
    Dialog.create({
        title: "Add new Comment",
        message: "Enter a text for your new comment!",
        prompt: {
            model: '',
            isValid: val => val.length > 2,
            type: 'text'
        },
        cancel: {
            label: "Cancel",
            color: 'negative'
        },
        ok: {
            color: 'primary'
        },
        persistent: true,
    })
        .onOk((comment) => resolve(comment))
        .onCancel(() => {
        })
});

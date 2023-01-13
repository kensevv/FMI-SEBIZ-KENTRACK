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

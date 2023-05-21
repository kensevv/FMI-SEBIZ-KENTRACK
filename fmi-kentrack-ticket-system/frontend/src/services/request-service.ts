import {UserView} from "../model/UserView";
import {NotificationMessages} from "../model/NotificationMessages";
import {api} from "../boot/Axios";
import {Board} from "../model/Board";
import {Ticket} from "../model/Ticket";

export const getUserByUsername = async (username: string): Promise<UserView> => await api.get<UserView>(`/user/`, {
    params: {username}
}).then(r => r.data)

export const checkIfUsernameExists = async (username: string): Promise<boolean> => await api.get<boolean>(`/user/exists`, {
    params: {username}
}).then(r => r.data)

export const createUser = async (user: UserView): Promise<Awaited<any> | UserView[]> =>
    await api.post<UserView>(`/user/create`,
        JSON.stringify(user),
        {
            headers: {'Content-Type': 'application/json'},
            // @ts-ignore
            notificationMessages: <NotificationMessages>{
                progressMessage: "Creating new user...",
                successMessage: "Successfully created the user.",
                errorMessage: "Something went wrong while creating the new user."
            },
        }).then(r => r.data)

export const changeCurrentUserPassword = async (currentPassword: string, newPassword: string): Promise<Awaited<any> | boolean[]> =>
    await api.get<boolean>(`/user/change-password`,
        {
            params: {
                currentPassword: currentPassword,
                newPassword: newPassword
            }
        }).then(r => r.data)
export const getUsers = async (): Promise<UserView[]> => await api.get<UserView[]>(`/user/all`).then(r => r.data)

export const updateUser = async (user: UserView): Promise<Awaited<any> | UserView[]> =>
    await api.post<UserView>(`/user/update`, JSON.stringify(user), {
        headers: {'Content-Type': 'application/json'},
        // @ts-ignore
        notificationMessages: <NotificationMessages>{
            progressMessage: "Updating the user...",
            successMessage: "Successfully updated the user.",
            errorMessage: "Something went wrong while updating the user."
        },
    }).then(r => r.data)

export const deleteUser = async (username: string): Promise<string> => await api.delete<string>(`/user/delete`, {
    params: {username}
}).then(r => r.data)

export const getAllBoards = async (): Promise<Board[]> => await api.get<Board[]>(`/get-all-boards`).then(r => r.data)
export const getAllTickets = async (): Promise<Ticket[]> => await api.get<Ticket[]>(`/get-all-tickets`).then(r => r.data)
export const createBoard = async (board: Board): Promise<Awaited<any> | Board[]> =>
    await api.post<Board>(`/create-new-board`,
        JSON.stringify(board),
        {
            headers: {'Content-Type': 'application/json'},
            // @ts-ignore
            notificationMessages: <NotificationMessages>{
                progressMessage: "Creating new board...",
                successMessage: "Successfully created the board.",
                errorMessage: "Something went wrong while creating the new board."
            },
        }).then(r => r.data)

export const updateBoard = async (board: Board): Promise<Awaited<any> | Board[]> =>
    await api.post<Board>(`/update-board`,
        JSON.stringify(board),
        {
            headers: {'Content-Type': 'application/json'},
            // @ts-ignore
            notificationMessages: <NotificationMessages>{
                progressMessage: "Updating board...",
                successMessage: "Successfully updated the board.",
                errorMessage: "Something went wrong while updating the board."
            },
        }).then(r => r.data)

export const createTicket = async (newTicket: Ticket): Promise<Awaited<any> | Ticket> =>
    await api.post<Ticket>(`/create-new-ticket`,
        JSON.stringify(newTicket),
        {
            headers: {'Content-Type': 'application/json'},
            // @ts-ignore
            notificationMessages: <NotificationMessages>{
                progressMessage: "Creating new ticket...",
                successMessage: "Successfully created the new ticket.",
                errorMessage: "Something went wrong while creating the new ticket."
            },
        }).then(r => r.data)

export const updateTicket = async (ticket: Ticket) =>
    await api.post(`/update-ticket`,
        JSON.stringify(ticket),
        {
            headers: {'Content-Type': 'application/json'},
            // @ts-ignore
            notificationMessages: <NotificationMessages>{
                progressMessage: "Updating ticket...",
                successMessage: "Successfully updated the ticket.",
                errorMessage: "Something went wrong while updating the ticket."
            },
        })

export const createNewCommentForTicket = async (commentContent: string, ticketId): Promise<Awaited<any> | Comment> =>
    await api.post<Comment>(`/create-new-comment-for-ticket`,
        null,
        {
            params: {ticketId, commentContent},
            headers: {'Content-Type': 'application/json'},
            // @ts-ignore
            notificationMessages: <NotificationMessages>{
                progressMessage: "Adding new comment to ticket...",
                successMessage: "Successfully added your comment the ticket.",
                errorMessage: "Something went wrong while adding the comment."
            },
        }).then(r => r.data)

export const deleteTicketById = async (ticketId: number) => {
    await api.delete(`/delete-ticket-by-id`, {
        params: {ticketId},
        // @ts-ignore
        notificationMessages: <NotificationMessages>{
            progressMessage: "Deleting ticket..",
            successMessage: "Successfully deleted the ticket.",
            errorMessage: "Something went wrong while deleting the ticket."
        }
    })
}

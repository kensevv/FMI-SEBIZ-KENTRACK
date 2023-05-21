import {UserView} from "./UserView";

export interface Board {
    id: number,
    title: string,
    owner: UserView,
    participants: UserView[],
    createdDate: Date,
    updatedDate: Date
}

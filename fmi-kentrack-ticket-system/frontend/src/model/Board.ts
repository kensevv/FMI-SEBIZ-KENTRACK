import {User} from "./User";

export interface Board {
    id: number,
    title: string,
    owner: User,
    participants: User[],
    createdDate: Date,
    updatedDate: Date
}

import {UserView} from "./UserView";

export interface Comment {
    id: number,
    content: string,
    author: UserView,
    createdDate: Date,
    updatedDate: Date,
}

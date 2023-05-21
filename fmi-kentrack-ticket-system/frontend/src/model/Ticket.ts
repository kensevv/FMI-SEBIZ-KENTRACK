import {Board} from "./Board";
import {Comment} from "./Comment";
import {Section} from "./Section";
import {UserView} from "./UserView";

export interface Ticket {
    id: number,
    title: string,
    description: string,
    owner: UserView,
    assignee: UserView,
    comments: Comment[],
    section: Section,
    board: Board,
    createdDate: Date,
    updatedDate: Date,
}
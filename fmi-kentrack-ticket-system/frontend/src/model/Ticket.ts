import {Board} from "./Board";
import {Comment} from "./Comment";
import {Section} from "./Section";
import {User} from "./User";

export interface Ticket {
    id: number,
    title: string,
    description: string,
    owner: User,
    assignee: User,
    comments: Comment[],
    section: Section,
    board: Board,
    createdDate: Date,
    updatedDate: Date,
}
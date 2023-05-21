import {ref} from "vue";
import {Board} from "./Board";
import {UserView} from "./UserView";
import {getAllBoards, getAllSections, getAllTickets, getUsers} from "../services/request-service";
import {Section} from "./Section";
import {Ticket} from "./Ticket";

export const sections = ref<Section[]>([
    <Section>{sectionName: "Open"},
    <Section>{sectionName: "Ready For Dev"},
    <Section>{sectionName: "In Development"},
    <Section>{sectionName: "UAT"},
    <Section>{sectionName: "Done"},
])
export let allBoards = ref<Board[]>([])
export let allUsers = ref<UserView[]>([])
export let allTickets = ref<Ticket[]>([])
export const loadServerConstants = async () => {
    [
        sections.value,
        allBoards.value,
        allUsers.value,
        allTickets.value,
    ] = await Promise.all([
        getAllSections(),
        getAllBoards(),
        getUsers(),
        getAllTickets(),
    ])
};
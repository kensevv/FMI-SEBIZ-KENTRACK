import {User} from "./User";
import {Section} from "./Section";
import {Board} from "./Board";
import {Ticket} from "./Ticket";
import {Comment} from "./Comment";
import {ref} from "vue";

export const users = ref([
    <User>{username: "kensev", firstName: "Kenan", lastName: "Yusein", email: "kensev@gmail.com"},
    <User>{username: "michael", firstName: "Michael", lastName: "Avery", email: "michael@gmail.com"},
    <User>{username: "rachel", firstName: "Rachel", lastName: "Elliott", email: "rachel@gmail.com"},
    <User>{username: "jessica", firstName: "Jessica", lastName: "Wade", email: "jessica@gmail.com"},
    <User>{username: "nancy", firstName: "Nancy", lastName: "Baxter", email: "nancy@gmail.com"},
    <User>{username: "cassandra", firstName: "Cassandra", lastName: "Jones", email: "cassandra@gmail.com"},
    <User>{username: "nicole", firstName: "Nicole", lastName: "Lopez", email: "nicole@gmail.com"},
    <User>{username: "ian", firstName: "Ian", lastName: "Davis", email: "ian@gmail.com"},
    <User>{username: "victoria", firstName: "Victoria", lastName: "Lewis", email: "victoria@gmail.com"},
])

export const sections = ref([
    <Section>{title: "Open"},
    <Section>{title: "Ready For Dev"},
    <Section>{title: "In Development"},
    <Section>{title: "UAT"},
    <Section>{title: "Done"},
])

export const boards = ref([
    <Board>{
        id: 1,
        title: "First Example Project",
        owner: users.value[0],
        participants: [users.value[0], users.value[1], users.value[2]],
        createdDate: new Date(),
        updatedDate: new Date()
    },
    <Board>{
        id: 2,
        title: "Second Example Project",
        owner: users.value[3],
        participants: [users.value[3], users.value[4], users.value[5]],
        createdDate: new Date(),
        updatedDate: new Date()
    },
    <Board>{
        id: 3,
        title: "Third Example Project",
        owner: users.value[6],
        participants: [users.value[6], users.value[7], users.value[8]],
        createdDate: new Date(),
        updatedDate: new Date()
    },
])

export const tickets = ref([
    <Ticket>{
        id: 1,
        title: "Create the login page",
        description: "The login page must be created with the following criterias: <br> It should following the base applicaiton layout. <br> It should have a box in the middle with two text input fields - one for username and one for password. <br> A login button must be placed underneath them. <br> Validations for both input fields must be implemented. <br> Clicking login must send login request to the LDAP and if the user is authenticated it should proceed to the home page. IF not an error message must be displayed.",
        owner: users.value[0],
        assignee: users.value[1],
        board: boards.value[0],
        comments: [<Comment>{
            id: 0,
            author: users.value[0],
            updatedDate: new Date(),
            createdDate: new Date(),
            content: "Test comment 1"
        }, <Comment>{
            id: 1,
            author: users.value[0],
            updatedDate: new Date(),
            createdDate: new Date(),
            content: "Test comment 2"
        }],
        section: sections.value[0],
        createdDate: new Date(),
        updatedDate: new Date()
    },
    <Ticket>{
        id: 2,
        title: "Create overall application layout",
        description: "The overall application layout must be created. It should define the main application primary and secondary colors and also it should implement the user header menu on the top. The colors should be black, gray and white. The menu must have the kentrack logo as a button for home page. Two extra buttons for accessing the dashboards and issues pages. A search bar. A notification icon and profile management menu must be added. A plus icon for creating new dashboard / issue must be present.",
        owner: users.value[0],
        assignee: users.value[2],
        board: boards.value[0],
        comments: [],
        section: sections.value[1],
        createdDate: new Date(),
        updatedDate: new Date()
    },
    <Ticket>{
        id: 3,
        title: "Create the home page",
        description: "The home page is the page that the user should be redirected to right after login in. It should provide the main information about the KenTrack web application - its purposes, use cases and more.",
        owner: users.value[0],
        assignee: users.value[0],
        board: boards.value[0],
        comments: [],
        section: sections.value[2],
        createdDate: new Date(),
        updatedDate: new Date()
    },
    <Ticket>{
        id: 4,
        title: "Create the Dashboards page",
        description: "The dashboards page is opened clicking the Dashboards icon on the header menu. It should show all available kanban boards in the application database. Additional information for each one of them must be displayed. Two buttons must be present - one for editing the board and the second one must open the board for more information.",
        owner: users.value[0],
        assignee: users.value[1],
        board: boards.value[0],
        comments: [],
        section: sections.value[2],
        createdDate: new Date(),
        updatedDate: new Date()
    },
    <Ticket>{
        id: 5,
        title: "Create the Issues page",
        description: "The issues page is opened clicking the Issues icon on the header menu. It should show all available issues in the application database. Additional information for each one of them must be displayed. Two buttons must be present - one for editing the issue and the second one must open the issue for more information.",
        owner: users.value[0],
        assignee: users.value[1],
        board: boards.value[0],
        comments: [],
        section: sections.value[1],
        createdDate: new Date(),
        updatedDate: new Date()
    },
    <Ticket>{
        id: 6,
        title: "Create single board-view page",
        description: "",
        owner: users.value[0],
        assignee: users.value[1],
        board: boards.value[0],
        comments: [],
        section: sections.value[4],
        createdDate: new Date(),
        updatedDate: new Date()
    },
    <Ticket>{
        id: 7,
        title: "Create single ticket-view page",
        description: "",
        owner: users.value[0],
        assignee: users.value[1],
        board: boards.value[0],
        comments: [],
        section: sections.value[4],
        createdDate: new Date(),
        updatedDate: new Date()
    },
    <Ticket>{
        id: 8,
        title: "Implement user menu",
        description: "",
        owner: users.value[0],
        assignee: users.value[2],
        board: boards.value[0],
        comments: [],
        section: sections.value[0],
        createdDate: new Date(),
        updatedDate: new Date()
    },
    <Ticket>{
        id: 9,
        title: "Implement creating new board",
        description: "",
        owner: users.value[0],
        assignee: users.value[0],
        board: boards.value[0],
        comments: [],
        section: sections.value[3],
        createdDate: new Date(),
        updatedDate: new Date()
    },
    <Ticket>{
        id: 10,
        title: "Implement creating new tickets",
        description: "",
        owner: users.value[0],
        assignee: users.value[1],
        board: boards.value[0],
        comments: [],
        section: sections.value[4],
        createdDate: new Date(),
        updatedDate: new Date()
    },
    <Ticket>{
        id: 11,
        title: "Implement editing existing tickets",
        description: "",
        owner: users.value[0],
        assignee: users.value[1],
        board: boards.value[0],
        comments: [],
        section: sections.value[3],
        createdDate: new Date(),
        updatedDate: new Date()
    },
    <Ticket>{
        id: 12,
        title: "Implement editing existing boards",
        description: "",
        owner: users.value[0],
        assignee: users.value[1],
        board: boards.value[0],
        comments: [],
        section: sections.value[3],
        createdDate: new Date(),
        updatedDate: new Date()
    },

])
import {UserView} from "./UserView";

export type AuthenticationResponse = Success | Error

export class Success {
    user: UserView

    constructor(user: UserView) {
        this.user = user;
    }
}

export class Error {
    result: AuthenticationResult

    constructor(result: AuthenticationResult) {
        this.result = result;
    }
}

export enum AuthenticationResult {
    UNKNOWN_USER = "UNKNOWN_USER",
    WRONG_PASSWORD = "WRONG_PASSWORD",
    UNSUCCESSFUL_ATTEMPT = "UNSUCCESSFUL_ATTEMPT"
}

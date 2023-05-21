<template>
  <q-layout class="bg-grey-1">
    <q-header class="text-white" elevated height-hint="61.59" style="background: #24292e">
      <q-toolbar class="q-py-sm q-px-md">
        <q-btn :ripple="false" class="q-mr-sm" color="white" dense flat no-caps size="19px"
               to="/home">
          <img height="50" src="/src/assets/logo-white.png">
        </q-btn>

        <div v-if="$q.screen.gt.sm"
             class="GL__toolbar-link q-ml-xs q-gutter-md text-body2 text-weight-bold row items-center no-wrap">
          <q-btn :ripple="false" class="q-mr-sm" color="white" dense flat icon="dashboard" label="Dashboards" no-caps
                 to="/dashboards"/>
          <q-btn :ripple="false" class="q-mr-sm" color="white" dense flat icon="task" label="Issues" no-caps
                 to="/issues"/>
          <q-btn v-show="currentUserHasRole(Roles.MAINTAINER)" :ripple="false" class="q-mr-sm" color="white" dense
                 flat icon="manage_accounts" label="Manage Users" no-caps
                 to="/users"/>
        </div>

        <q-space/>

        <div class="q-pl-sm q-gutter-sm row items-center no-wrap">
          <q-btn v-if="$q.screen.gt.xs" dense flat icon="notifications" round size="sm">
            <q-tooltip>Notifications</q-tooltip>
          </q-btn>
          <q-btn v-if="$q.screen.gt.xs" dense flat>
            <div class="row items-center no-wrap">
              <q-icon name="add" size="20px"/>
              <q-icon name="arrow_drop_down" size="16px" style="margin-left: -2px"/>
            </div>
            <q-menu auto-close>
              <q-list dense style="min-width: 100px">
                <q-item class="GL__menu-link" clickable @click="createNewBoard()">
                  <q-item-section>New dashboard</q-item-section>
                </q-item>
                <q-item class="GL__menu-link" clickable @click="createNewTicket()">
                  <q-item-section>New issue</q-item-section>
                </q-item>
              </q-list>
            </q-menu>
          </q-btn>

          <q-btn dense flat no-wrap>
            <q-avatar class="text-black" rounded size="20px" style="background-color: white">
              {{ getCurrentUser().firstName[0] }}{{ getCurrentUser().lastName[0] }}
            </q-avatar>
            <q-icon name="arrow_drop_down" size="16px"/>

            <q-menu auto-close>
              <q-list dense>
                <q-item class="GL__menu-link-signed-in">
                  <q-item-section>
                    <div>Signed in as <strong>{{ getCurrentUser().firstName }} {{ getCurrentUser().lastName }}</strong>
                    </div>
                  </q-item-section>
                </q-item>
                <q-separator/>
                <q-item class="GL__menu-link" clickable @click="router.push('/profile-settings')">
                  <q-item-section>Your profile</q-item-section>
                </q-item>
                <q-item class="GL__menu-link" clickable @click="router.push('/dashboards')">
                  <q-item-section>Your dashboards</q-item-section>
                </q-item>
                <q-item class="GL__menu-link" clickable @click="router.push('/issues')">
                  <q-item-section>Your issues</q-item-section>
                </q-item>
                <q-separator/>
                <q-item class="GL__menu-link" clickable @click="clearUserStorage(); router.push('/login')">
                  <q-item-section>Sign out</q-item-section>
                </q-item>
              </q-list>
            </q-menu>
          </q-btn>
        </div>
      </q-toolbar>
    </q-header>

    <q-page-container style="background-color: #e0e0e0">
      <router-view v-slot="{ Component }">
        <template v-if="Component">
          <suspense>
            <component :is="Component"></component>
            <template #fallback>
              <div class="fixed-center">
                <q-spinner
                    :thickness="2"
                    color="primary"
                    size="5.5em"
                />
              </div>
            </template>
          </suspense>
        </template>
      </router-view>
    </q-page-container>
  </q-layout>
</template>

<script lang="ts" setup>

import {useQuasar} from "quasar";
import {Board} from "../model/Board";
import EditBoardDialog from "../dialogs/edit-board-dialog.vue";
import EditIssueDialog from "../dialogs/edit-issue-dialog.vue";
import {Ticket} from "../model/Ticket";
import {clearUserStorage, currentUserHasRole, getCurrentUser} from "../services/UserService";
import {useRouter} from "vue-router";
import {createBoard, createTicket} from "../services/request-service";
import {onBeforeMount} from "vue";
import {allBoards, allTickets, loadServerConstants} from "../model/constants";
import {Roles} from "../model/Roles";

onBeforeMount(() => {
  loadServerConstants()
})

const quasar = useQuasar()
const router = useRouter()

const createNewBoard = () => {
  quasar.dialog({
    component: EditBoardDialog,
    componentProps: {
      'item': <Board><unknown>{
        id: null,
        title: '',
        owner: null,
        createdDate: new Date(),
        updatedDate: new Date(),
        participants: [],
      },
    }
  }).onOk(async (newBoard: Board) => {
    await createBoard(newBoard).then(response => {
      allBoards.value.push(response)
      router.push(`/dashboard/${response.id}`)
    })
  })
}

const createNewTicket = () => {
  quasar.dialog({
    component: EditIssueDialog,
    componentProps: {
      'item': <Ticket><unknown>{
        id: null,
        title: null,
        description: null,
        comments: [],
        board: null,
        section: null,
        assignee: null,
        owner: null,
        createdDate: new Date(),
        updatedDate: new Date(),
      },
    }
  }).onOk(async (newTicket: Ticket) => {
    await createTicket(newTicket).then(response => {
      allTickets.value.push(response)
    })
  })
}

</script>

<style lang="sass">
.GL
  &__select-GL__menu-link
    .default-type
    visibility: hidden

    &:hover
      background: #0366d6
      color: white

      .q-item__section--side
        color: white

      .default-type
        visibility: visible

  &__toolbar-link
    a
      color: white
      text-decoration: none

      &:hover
        opacity: 0.7

  &__menu-link:hover
    background: #0366d6
    color: white

  &__menu-link-signed-in,
  &__menu-link-status
    &:hover
      & > div
        background: white !important

  &__menu-link-status
    color: grey

    &:hover
      color: lightblue

  &__toolbar-select.q-field--focused
    width: 450px !important

    .q-field__append
      display: none
</style>
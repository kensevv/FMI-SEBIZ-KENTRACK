<template>
  <q-layout class="bg-grey-1">
    <q-header elevated class="text-white" style="background: #24292e" height-hint="61.59">
      <q-toolbar class="q-py-sm q-px-md">
        <q-btn dense flat :ripple="false" size="19px" color="white" class="q-mr-sm" no-caps
               to="/home">
          <img height="50" src="/src/assets/logo-white.png">
        </q-btn>

        <div v-if="$q.screen.gt.sm"
             class="GL__toolbar-link q-ml-xs q-gutter-md text-body2 text-weight-bold row items-center no-wrap">
          <q-btn dense flat :ripple="false" icon="dashboard" label="Dashboards" color="white" class="q-mr-sm" no-caps
                 to="/dashboards"/>
          <q-btn dense flat :ripple="false" icon="task" label="Issues" color="white" class="q-mr-sm" no-caps
                 to="/issues"/>
        </div>

        <q-space/>

        <q-select
            ref="search" dark dense standout use-input hide-selected
            class="GL__toolbar-select"
            color="black" :stack-label="false" label="Search issues..."
            v-model="text" :options="filteredOptions" @filter="filter"
            style="width: 300px"
        >

          <template v-slot:append>
            <img src="https://cdn.quasar.dev/img/layout-gallery/img-github-search-key-slash.svg">
          </template>

          <template v-slot:no-option>
            <q-item>
              <q-item-section>
                <div class="text-center">
                  <q-spinner-pie
                      color="grey-5"
                      size="24px"
                  />
                </div>
              </q-item-section>
            </q-item>
          </template>

          <template v-slot:option="scope">
            <q-item
                v-bind="scope.itemProps"
                class="GL__select-GL__menu-link"
            >
              <q-item-section side>
                <q-icon name="collections_bookmark"/>
              </q-item-section>
              <q-item-section>
                <q-item-label v-html="scope.opt.label"/>
              </q-item-section>
              <q-item-section side :class="{ 'default-type': !scope.opt.type }">
                <q-btn outline dense no-caps text-color="blue-grey-5" size="12px" class="bg-grey-1 q-px-sm">
                  {{ scope.opt.type || 'Jump to' }}
                  <q-icon name="subdirectory_arrow_left" size="14px"/>
                </q-btn>
              </q-item-section>
            </q-item>
          </template>
        </q-select>

        <div class="q-pl-sm q-gutter-sm row items-center no-wrap">
          <q-btn v-if="$q.screen.gt.xs" dense flat round size="sm" icon="notifications">
            <q-tooltip>Notifications</q-tooltip>
          </q-btn>
          <q-btn v-if="$q.screen.gt.xs" dense flat>
            <div class="row items-center no-wrap">
              <q-icon name="add" size="20px"/>
              <q-icon name="arrow_drop_down" size="16px" style="margin-left: -2px"/>
            </div>
            <q-menu auto-close>
              <q-list dense style="min-width: 100px">
                <q-item clickable class="GL__menu-link" @click="createNewBoard()">
                  <q-item-section>New dashboard</q-item-section>
                </q-item>
                <q-item clickable class="GL__menu-link" @click="createNewTicket()">
                  <q-item-section>New issue</q-item-section>
                </q-item>
              </q-list>
            </q-menu>
          </q-btn>

          <q-btn dense flat no-wrap>
            <q-avatar rounded size="20px">
              <img src="https://cdn.quasar.dev/img/avatar3.jpg">
            </q-avatar>
            <q-icon name="arrow_drop_down" size="16px"/>

            <q-menu auto-close>
              <q-list dense>
                <q-item class="GL__menu-link-signed-in">
                  <q-item-section>
                    <div>Signed in as <strong>Kenan Yusein</strong></div>
                  </q-item-section>
                </q-item>
                <q-separator/>
                <q-item clickable class="GL__menu-link-status">
                  <q-item-section>
                    <div>
                      <q-icon name="tag_faces" color="blue-9" size="18px"/>
                      Set your status
                    </div>
                  </q-item-section>
                </q-item>
                <q-separator/>
                <q-item clickable class="GL__menu-link">
                  <q-item-section>Your profile</q-item-section>
                </q-item>
                <q-item clickable class="GL__menu-link">
                  <q-item-section>Your dashboards</q-item-section>
                </q-item>
                <q-item clickable class="GL__menu-link">
                  <q-item-section>Your issues</q-item-section>
                </q-item>
                <q-separator/>
                <q-item clickable class="GL__menu-link">
                  <q-item-section>Help</q-item-section>
                </q-item>
                <q-item clickable class="GL__menu-link">
                  <q-item-section>Settings</q-item-section>
                </q-item>
                <q-item clickable class="GL__menu-link" @click="clearUserStorage(); router.push('/login')">
                  <q-item-section>Sign out</q-item-section>
                </q-item>
              </q-list>
            </q-menu>
          </q-btn>
        </div>
      </q-toolbar>
    </q-header>

    <q-page-container style="background-color: #e0e0e0">
      <router-view/>
    </q-page-container>
  </q-layout>
</template>

<script lang="ts" setup>

import {useQuasar} from "quasar";
import {Board} from "../model/Board";
import {boards, tickets} from "../model/mocked-date";
import EditBoardDialog from "../dialogs/edit-board-dialog.vue";
import EditIssueDialog from "../dialogs/edit-issue-dialog.vue";
import {Ticket} from "../model/Ticket";
import {clearUserStorage} from "../services/UserService";
import {useRouter} from "vue-router";

const quasar = useQuasar()
const router = useRouter()

const createNewBoard = () => {
  quasar.dialog({
    component: EditBoardDialog,
    componentProps: {
      'item': <Board><unknown>{
        id: Math.max(...boards.value.map(b => b.id)) + 1,
        title: null,
        owner: null,
        createdDate: new Date(),
        updatedDate: new Date(),
        participants: [],
      },
    }
  }).onOk(async (newBoard: Board) => boards.value.push(newBoard))
}

const createNewTicket = () => {
  quasar.dialog({
    component: EditIssueDialog,
    componentProps: {
      'item': <Ticket><unknown>{
        id: Math.max(...tickets.value.map(b => b.id)) + 1,
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
  }).onOk(async (newTicket: Ticket) => tickets.value.push(newTicket))
}


</script>

<script lang="ts">
import {ref} from 'vue'

const stringOptions = [
  'quasarframework/quasar',
  'quasarframework/quasar-awesome'
]
export default {
  name: 'MyLayout',
  setup() {
    const text = ref('')
    const options = ref(null)
    const filteredOptions = ref([])
    const search = ref(null) // $refs.search
    function filter(val, update) {
      if (options.value === null) {
        // load data
        setTimeout(() => {
          options.value = stringOptions
          search.value.filter('')
        }, 2000)
        update()
        return
      }
      if (val === '') {
        update(() => {
          filteredOptions.value = options.value.map(op => ({label: op}))
        })
        return
      }
      update(() => {
        filteredOptions.value = [
          {
            label: val,
            type: 'In this repository'
          },
          {
            label: val,
            type: 'All GitHub'
          },
          ...options.value
              .filter(op => op.toLowerCase().includes(val.toLowerCase()))
              .map(op => ({label: op}))
        ]
      })
    }

    return {
      text,
      options,
      filteredOptions,
      search,
      filter
    }
  }
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
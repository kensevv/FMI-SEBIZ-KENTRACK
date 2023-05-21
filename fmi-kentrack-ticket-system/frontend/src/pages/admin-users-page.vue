<template>
  <q-page padding>
    <div class="row">
      <div class="col-2"/>
      <div class="col-8 q-col-gutter-lg">
        <div class="col">
          <q-table :columns="columns"
                   flat
                   :pagination="{rowsPerPage: 0}"
                   :rows="users"
                   title="Users"
                   :filter="filter"
                   hide-pagination
                   @row-click="openUser">
            <template v-slot:top-right>
              <q-input v-model="filter" clearable debounce="300" dense placeholder="Search">
                <template v-slot:append>
                  <q-icon name="search"/>
                </template>
              </q-input>
            </template>
            <template v-slot:body-cell-roles="props">
              <td style="vertical-align: center">
                <div style="float: right">
                  {{ props.row.roles.join(", ") }}
                </div>
              </td>
            </template>
          </q-table>
          <q-page-sticky :offset="[18, 18]" position="top">
            <q-btn color="primary" fab icon="add" @click="onAddUserClick"/>
          </q-page-sticky>
        </div>
      </div>
      <div class="col-2"/>
    </div>
  </q-page>
</template>

<script lang="ts" setup>
import {$ref} from "vue/macros";
import {useRouter} from "vue-router";
import {UserView} from "../model/UserView";
import {promiseDialog} from "../utils";
import {createUser, getUsers} from "../services/request-service";
import CreateUserDialog from "../dialogs/create-user-dialog.vue";
import {allUsers} from "../model/constants";

let users = $ref<UserView[]>(await getUsers());

const router = useRouter();
const openUser = (evt, row: UserView, idx) => {
  router.push({
    name: "user",
    params: {
      username: row.username
    }
  })
}

const onAddUserClick = async () => {
  const response = await promiseDialog<UserView>(CreateUserDialog, null);
  const newUser = await createUser(response);
  allUsers.value.push(newUser)
  await router.push({
    name: "user",
    params: {
      username: newUser.username
    }
  })
}

const filter = $ref('')

const columns = [
  {
    name: 'firstName',
    label: 'First name',
    field: (row: UserView) => row.firstName,
    sortable: true
  },
  {
    name: 'lastName',
    label: 'Last name',
    field: (row: UserView) => row.lastName,
    sortable: true
  },
  {
    name: 'email',
    label: 'E-mail',
    field: (row: UserView) => row.email,
    sortable: true
  },
  {name: "roles", label: "Roles"},
]

</script>
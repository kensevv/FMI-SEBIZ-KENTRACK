<template>
  <q-page>
    <div class="row">
      <div class="col-6 offset-3">
        <q-card class="q-pa-lg">
          <h3>User details for <span style="color: #0e6be7">{{ user.username }}</span></h3>
          <q-input v-model="user.username"
                   class="q-mb-md"
                   dense
                   disable
                   label="Username"
          />

          <q-input v-model="user.firstName"
                   class="q-mb-md"
                   dense
                   label="First Name"
          />

          <q-input v-model="user.lastName"
                   class="q-mb-md"
                   dense
                   label="Last Name"
          />

          <q-input v-model="user.email"
                   class="q-mb-md"
                   dense
                   label="Email"
                   type="email"/>

          <q-select
              v-model="user.roles"
              label="Roles"
              :options="Object.values(Roles)"
              class="q-mb-md"
              dense
              multiple
              stack-label
              use-chips
          />
          <div>
            <q-btn @click="onBackToUsers">Back to users</q-btn>
            <div class="float-right">
              <q-btn :disabled="cannotDelete" class="q-mr-md" color="negative" @click="onDeleteUser">
                Delete User
              </q-btn>
              <q-btn :disabled="saveDisabled" class="q-mr-md" color="primary" @click="onSaveUser">
                Save
              </q-btn>
            </div>
          </div>

        </q-card>
      </div>
    </div>
  </q-page>
</template>

<script lang="ts" setup>
import {$computed, $ref} from "vue/macros";
import {isEqual} from "lodash-es";
import {useRouter} from "vue-router";
import {useQuasar} from "quasar";
import {deleteUser, getUserByUsername, updateUser} from "../services/request-service";
import {UserView} from "../model/UserView";
import {getCurrentUser} from "../services/UserService";
import {Roles} from "../model/Roles";

const props = defineProps<{
  username: string
}>();

let serverUser = $ref<UserView>(await getUserByUsername(props.username));
let user = $ref({...serverUser});
const saveDisabled = $computed(() => isEqual({...serverUser}, {...user}))
console.log(user)
const onSaveUser = async () => {
  await updateUser(user).then(response => {
    serverUser = response
    user = {...serverUser}
  })
}
const currentUserUsername = getCurrentUser().username;
const cannotDelete = user.username == currentUserUsername
const quasar = useQuasar();
const router = useRouter();

const onDeleteUser = async () => {
  await deleteUser(user.username!!).then(async r => {
    await router.push({name: 'users'})
  })
}

const onBackToUsers = async () => await router.push({name: 'users'})

</script>
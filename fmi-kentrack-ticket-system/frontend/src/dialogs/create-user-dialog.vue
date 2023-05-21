<script lang="ts" setup>

import {useDialogPluginComponent} from "quasar";
import {$ref} from "vue/macros";
import {useMagicKeys, whenever} from "@vueuse/core";
import {UserView} from "../model/UserView";
import {checkIfUsernameExists} from "../services/request-service";

let {dialogRef, onDialogCancel, onDialogHide, onDialogOK} = useDialogPluginComponent();
defineEmits([...useDialogPluginComponent.emits])

const userItem = $ref<Partial<UserView>>({})

let userAlreadyExists = $ref(false);
let loading = $ref(false);
const onAccept = async () => {
  userAlreadyExists = false
  const dbUser = await checkIfUsernameExists(userItem.username!);
  if (!dbUser) {
    onDialogOK(userItem)
  } else {
    userAlreadyExists = true
  }
}
const {enter} = useMagicKeys();
whenever(enter, () => {
  onAccept()
})
</script>

<template>
  <q-dialog ref="dialogRef" @hide="onDialogHide">
    <q-card class="q-dialog-plugin" style="width: 60vh">
      <q-form greedy @submit="onAccept">
        <q-card-section class="dialog-header">
          <div class="text-h6">Add new user</div>
          <q-btn v-close-popup dense flat icon="close" round/>
        </q-card-section>
        <q-card-section v-if="!loading">
          <q-input v-model="userItem.username" :rules="[ val => val !== '' && val != null || 'Required']" class="test-blue-9" dense label="Username"></q-input>
          <q-input v-model="userItem.firstName" :rules="[ val => val !== '' && val != null || 'Required']" class="test-blue-9" dense label="First Name"></q-input>
          <q-input v-model="userItem.lastName" :rules="[ val => val !== '' && val != null || 'Required']" class="test-blue-9" dense label="Last Name"></q-input>
          <q-input v-model="userItem.email" :rules="[ val => val !== '' && val != null || 'Required']" class="test-blue-9" dense label="E-mail"></q-input>
          <div>Initial Password: <span class="text-blue-9">easyTrackTest</span></div>
          <div v-if="!loading && userAlreadyExists" class="text-negative">This username already exists, please choose
            another one
          </div>
        </q-card-section>
        <q-card-section v-else>
          <q-spinner></q-spinner>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn color="negative" label="Cancel" @click="onDialogCancel"/>
          <q-btn type="update" :disable="loading || userAlreadyExists" color="primary" label="Create user"/>
        </q-card-actions>
      </q-form>
    </q-card>
  </q-dialog>
</template>


<style scoped>
.dialog-header {
  display: flex;
  justify-content: space-between;
}
</style>

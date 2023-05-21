<template>
  <div class="row flex flex-center layout_bg">
    <q-page class="row q-pa-lg">
      <div class="col">
        <q-card bordered class="card-bg no-shadow qpas">
          <q-card-section class="text-h6 ">
            <div class="text-h6">Edit Profile
              <q-btn color="primary" flat
                     icon="save" round type="a"
                     @click="changePassword()">
                <q-tooltip>
                  Update user info
                </q-tooltip>
              </q-btn>
            </div>
          </q-card-section>
          <q-card-section class="q-pa-sm">
            <q-list class="row">
              <q-item class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                <q-item-section avatar>
                  <q-avatar class="text-white" size="50px" style="background-color: #0d2b49">
                    {{ getCurrentUser().firstName[0] }}{{ getCurrentUser().lastName[0] }}
                  </q-avatar>
                </q-item-section>
                <div class="text-h4" style="color: #0e6be7">{{ userDetails.username }}</div>
                <div class="text-h4" style="color: #0e6be7"> {{ userDetails.roles }}</div>
              </q-item>
              <q-item class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
                <q-item-section>
                  <q-input v-model="userDetails.firstName" dense label="First Name"/>
                </q-item-section>
              </q-item>
              <q-item class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
                <q-item-section>
                  <q-input v-model="userDetails.lastName" dense label="Last Name"/>
                </q-item-section>
              </q-item>
              <q-item class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
                <q-item-section>
                  <q-input v-model="userDetails.email" dense label="Email Address"/>
                </q-item-section>
              </q-item>
            </q-list>
          </q-card-section>
        </q-card>
        <q-form greedy @submit="changePassword()">
          <q-card bordered class="card-bg no-shadow">
            <q-card-section class="text-h6 q-pa-sm">


              <div class="text-h6">Change Password
                <q-btn :disable="!newPassword || !currentPassword || !confirmNewPassword"
                       color="primary" flat
                       icon="save" round type="a"
                       @click="changePassword()">
                  <q-tooltip v-if="!newPassword || !currentPassword || !confirmNewPassword">
                    Fill out all inputs
                  </q-tooltip>
                  <q-tooltip v-else>
                    Save new password
                  </q-tooltip>
                </q-btn>
                <div v-if="passwordStatus.message && passwordStatus.message !== ''"
                      :class="passwordStatus.color">{{ passwordStatus.message }}</div>
              </div>
            </q-card-section>
            <q-card-section class="q-pa-sm row">
              <q-item class="col-lg-8 col-md-8 col-sm-12 col-xs-12">
                <q-item-section>
                  <q-input v-model="currentPassword" :rules="[ val => val !== null && val !== '' || 'Required']" dense
                           label="Current Password"
                           round
                           type="password"/>
                </q-item-section>
              </q-item>
              <q-item class="col-lg-8 col-md-8 col-sm-12 col-xs-12">
                <q-item-section>
                  <q-input v-model="newPassword" :rules="[ val => val !== null && val !== '' || 'Required']" dense
                           label="New Password"
                           round
                           type="password"/>
                </q-item-section>
              </q-item>
              <q-item class="col-lg-8 col-md-8 col-sm-12 col-xs-12">
                <q-item-section>
                  <q-input v-model="confirmNewPassword" :rules="[ val => val !== null && val !== '' && newPassword === confirmNewPassword || 'Does not match the new password']"
                           dense
                           label="Confirm New Password"
                           round
                           type="password"/>
                </q-item-section>
              </q-item>
            </q-card-section>
          </q-card>
        </q-form>
      </div>
    </q-page>
  </div>
</template>

<script lang="ts" setup>

import {$ref} from "vue/macros";
import {getCurrentUser} from "../services/UserService";
import {changeCurrentUserPassword} from "../services/request-service";

const userDetails = $ref(getCurrentUser())
const currentPassword = $ref('')
const newPassword = $ref('')
const confirmNewPassword = $ref('')
let passwordStatus = $ref({message: '', color: 'primary'})

const changePassword = async () => {
  await changeCurrentUserPassword(currentPassword, newPassword).then(response => {
    if (response) {
      passwordStatus = {
        message: "Your password has been successfully changed!",
        color: 'text-positive'
      }
    } else {
      passwordStatus = {
        message: "Wrong current password! Please try again.",
        color: 'text-negative'
      }
    }
  }).catch(error => {
    passwordStatus = {
      message: "Something went wrong while changing your password, please try again later or contact an administrator for more information",
      color: 'text-negative'
    }
  })
}
</script>

<style scoped>

</style>
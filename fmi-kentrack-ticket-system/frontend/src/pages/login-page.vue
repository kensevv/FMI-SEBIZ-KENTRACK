<template>
  <div class="window-height window-width" style="background: #24292e">
    <q-card class="fixed-center q-pa-md" style="width: 25rem; ">
      <q-form @submit="onLoginClick">
        <q-card-section class="q-col-gutter-lg">
          <img src="/src/assets/logo.png" width="350">
          <q-input v-model="username"
                   :rules="[ val => val !== null && val !== '' || 'required']"
                   class="full-width"
                   dense
                   filled
                   label="username"
                   standout
                   type="text"/>
          <q-input v-model="password"
                   :rules="[ val => val !== null && val !== '' || 'required']"
                   class="full-width"
                   dense
                   filled
                   label="password"
                   standout
                   type="password"/>
        </q-card-section>
        <q-card-actions>
          <q-btn :loading="loading" class="full-width" color="grey" label="login" type="submit">
          </q-btn>
        </q-card-actions>
        <div class="text-center" style="color: red">{{ failMessage }}</div>
      </q-form>
    </q-card>
  </div>
</template>
<script lang="ts" setup>

import {login, storeUser} from "../services/UserService";
import {useRouter} from "vue-router";
import {useQuasar} from "quasar";
import {$ref} from "vue/macros";
import {AuthenticationResponse, Error, Success} from "../model/AuthenticationResponse";


const router = useRouter()
const quasar = useQuasar()

const username = $ref(null);
const password = $ref(null);

let loading = $ref(false);
let failMessage = $ref('')

const onLoginClick = async () => {
  loading = true
  await login(username, password)
      .then(r => {
        const authResponse: AuthenticationResponse = <Success>r.data
        storeUser(authResponse.user)
        loading = false
        router.push({path: '/'})
      }).catch(e => {
        const authResponse: AuthenticationResponse = <Error>e.response.data
        loading = false
        failMessage = `${e.response.statusText}: ${authResponse.result}`
        Promise.reject("Unauthenticated");
      })
}

</script>

<script lang="ts">
import {defineComponent} from "vue";

export default defineComponent({
  name: 'LoginPage'
})
</script>
<style scoped>

</style>
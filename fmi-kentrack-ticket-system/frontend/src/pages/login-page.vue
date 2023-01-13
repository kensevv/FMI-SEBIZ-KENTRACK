<template>
  <div class="window-height window-width" style="background: #24292e">
    <q-card class="fixed-center q-pa-md" style="width: 25rem; ">
      <q-form @submit="onLoginClick">
        <q-card-section class="q-col-gutter-lg">
          <img width="350" src="/src/assets/logo.png">
          <q-input v-model="username"
                   :rules="[ val => val !== null && val !== '' || 'required']"
                   :reactive-rules="true"
                   dense
                   filled
                   label="username"
                   standout
                   type="text"
                   class="full-width"/>
          <q-input v-model="password"
                   :rules="[ val => val !== null && val !== '' || 'required']"
                   :reactive-rules="true"
                   dense
                   filled
                   label="password"
                   standout
                   type="password"
                   class="full-width"/>
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

import {login, login1, storeUser} from "../services/UserService";
import {useRouter} from "vue-router";
import {useQuasar} from "quasar";
import {$ref} from "vue/macros";


const router = useRouter()
const quasar = useQuasar()

const username = $ref(null);
const password = $ref(null);

let loading = $ref(false);
let failMessage = $ref('')

const onLoginClick = async () => {
  loading = true
  const user = await login1(username, password)
      if(user) {
        storeUser(user)
        loading = false
        router.push({path: '/'})
      } else {
        loading = false
      }
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


<template>
  <q-dialog ref="dialogRef" @hide="onDialogHide">
    <q-card class="q-dialog-plugin">
      <q-card-section class="dialog-header">
        <div class="text-h6">Manage Board</div>
        <q-btn v-close-popup dense flat icon="close" round/>
      </q-card-section>
      <q-card-section>
        <q-form class="q-gutter-md" @submit="submit">
          <q-input v-model="issueItem.id"
                   dense
                   filled
                   label="ID"/>
          <q-input v-model="issueItem.title"
                   dense
                   filled
                   label="Title"/>
          <q-select v-model="issueItem.owner"
                    :options="users"
                    filled
                    label="Owner"
                    option-label="firstName"
          />

          <q-select
              v-model="issueItem.participants"
              :option-label="user => `${user.firstName} ${user.lastName}`"
              :options="users"
              class="q-banner--top-padding"
              dense
              filled
              label="Participants"
              multiple
              stack-label
              use-chips
          />
          <input-date-picker v-model="issueItem.createdDate" label="Created"/>
          <input-date-picker v-model="issueItem.updatedDate" label="Updated"/>

          <q-card-actions align="right">
            <q-btn label="Submit" type="update"/>
            <q-btn class="q-ml-sm" flat label="Cancel" @click="onDialogCancel"/>
          </q-card-actions>
        </q-form>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script lang="ts" setup>
import {useDialogPluginComponent} from 'quasar'
import {$ref} from "vue/macros";
import {users} from "../model/mocked-date";
import {Board} from "../model/Board";
import InputDatePicker from "../components/input-date-picker.vue";

const {dialogRef, onDialogHide, onDialogOK, onDialogCancel} = useDialogPluginComponent()
const props = defineProps<{
  item: Board,
}>();

defineEmits([...useDialogPluginComponent.emits])

const issueItem = $ref<Board>(props.item)
const submit = async () => {
  onDialogOK(issueItem)
}
</script>
<style scoped>
.dialog-header {
  display: flex;
  justify-content: space-between;
}
</style>
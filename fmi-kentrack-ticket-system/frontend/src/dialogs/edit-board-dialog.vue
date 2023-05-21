<template>
  <q-dialog ref="dialogRef" @hide="onDialogHide">
    <q-card class="q-dialog-plugin">
      <q-card-section class="dialog-header">
        <div class="text-h6">Manage Board</div>
        <q-btn v-close-popup dense flat icon="close" round/>
      </q-card-section>
      <q-card-section>
        <q-form class="q-gutter-md" @submit="submit">
          <q-input v-model="boardItem.title"
                   dense
                   filled
                   label="Title"/>
          <q-select v-model="boardItem.owner"
                    :options="allUsers"
                    filled
                    label="Owner"
                    option-label="firstName"
          />

          <q-select
              v-model="boardItem.participants"
              :option-label="user => `${user.firstName} ${user.lastName}`"
              :options="allUsers"
              class="q-banner--top-padding"
              dense
              filled
              label="Participants"
              multiple
              stack-label
              use-chips
          />
          <input-date-picker v-model="boardItem.createdDate" label="Created" read-only/>
          <input-date-picker v-model="boardItem.updatedDate" label="Updated" read-only/>

          <q-card-actions align="right">
            <q-btn label="Submit" type="update" color="primary"/>
            <q-btn class="q-ml-sm" flat label="Cancel" color="negative" @click="onDialogCancel"/>
          </q-card-actions>
        </q-form>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script lang="ts" setup>
import {useDialogPluginComponent} from 'quasar'
import {$ref} from "vue/macros";
import {Board} from "../model/Board";
import InputDatePicker from "../components/input-date-picker.vue";
import {allUsers} from "../model/constants";

const {dialogRef, onDialogHide, onDialogOK, onDialogCancel} = useDialogPluginComponent()
const props = defineProps<{
  item: Board,
}>();

defineEmits([...useDialogPluginComponent.emits])

const boardItem = $ref<Board>(props.item)
const submit = async () => {
  onDialogOK(boardItem)
}
</script>
<style scoped>
.dialog-header {
  display: flex;
  justify-content: space-between;
}
</style>
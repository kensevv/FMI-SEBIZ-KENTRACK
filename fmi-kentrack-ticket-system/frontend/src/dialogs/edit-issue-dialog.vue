<template>
  <q-dialog ref="dialogRef" @hide="onDialogHide" full-width>
    <q-card class="q-dialog-plugin">
      <q-card-section class="dialog-header">
        <div class="text-h6">Manage Issue</div>
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
          <q-editor
              v-model="issueItem.description"
              :dense="$q.screen.lt.md"
              :fonts="{
        arial: 'Arial',
        arial_black: 'Arial Black',
        comic_sans: 'Comic Sans MS',
        courier_new: 'Courier New',
        impact: 'Impact',
        lucida_grande: 'Lucida Grande',
        times_new_roman: 'Times New Roman',
        verdana: 'Verdana'
      }"
              :toolbar="[
        ['bold', 'italic', 'strike', 'underline'],
          ['unordered', 'ordered', 'outdent', 'indent'],
                [
          {
            label: $q.lang.editor.align,
            icon: $q.iconSet.editor.align,
            fixedLabel: true,
            list: 'only-icons',
            options: ['left', 'center', 'right', 'justify']
          },
          {
            label: $q.lang.editor.formatting,
            icon: $q.iconSet.editor.formatting,
            options: [
              'p',
              'code',
              'h1',
              'h2',
              'h3',
              'h4',
            ]
          },
          {
            label: $q.lang.editor.fontSize,
            icon: $q.iconSet.editor.fontSize,
            fixedLabel: true,
            fixedIcon: true,
            list: 'no-icons',
            options: [
              'size-1',
              'size-2',
              'size-3',
              'size-4',
              'size-5',
              'size-6',
              'size-7'
            ]
          },
          {
            label: $q.lang.editor.defaultFont,
            icon: $q.iconSet.editor.font,
            fixedIcon: true,
            list: 'no-icons',
            options: [
              'default_font',
              'arial',
              'arial_black',
              'comic_sans',
              'courier_new',
              'impact',
              'lucida_grande',
              'times_new_roman',
              'verdana'
            ]
          },
        ],
         ['subscript', 'superscript'],
        ['token', 'hr', 'link'],
        ['undo', 'redo'],
        ['fullscreen']
      ]"
          />

          <q-select v-model="issueItem.owner"
                    :options="users"
                    filled
                    label="Owner"
                    option-label="firstName"
          />
          <q-select v-model="issueItem.assignee"
                    :options="users"
                    filled
                    label="Assignee"
                    option-label="firstName"
          />

          <q-select v-model="issueItem.section"
                    :options="sections"
                    filled
                    label="Section"
                    option-label="title"
          />

          <q-select v-model="issueItem.board"
                    :options="boards"
                    filled
                    label="Board"
                    option-label="title"
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
import {Ticket} from "../model/Ticket";
import {boards, sections, users} from "../model/mocked-date";
import InputDatePicker from "../components/input-date-picker.vue";

const {dialogRef, onDialogHide, onDialogOK, onDialogCancel} = useDialogPluginComponent()
const props = defineProps<{
  item: Ticket,
}>();

defineEmits([...useDialogPluginComponent.emits])

const issueItem = $ref<Ticket>(props.item)
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
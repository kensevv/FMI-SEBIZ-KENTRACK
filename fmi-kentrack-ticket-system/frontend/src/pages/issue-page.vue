<template>
  <q-page padding>
    <div class="row ">
      <div class="col-3"></div>
      <q-card class="col">
        <q-card-section>
          <div class="row">
            <div class="text-h5">Issue {{ issue.id }} - {{ issue.title }}</div>
            <q-space/>
            <q-btn flat icon="edit" no-caps
                   round
                   dense
                   size="s"
                   @click="()=> editIssue(issue)"
            >
              <q-tooltip>Click to edit</q-tooltip>
            </q-btn>
          </div>
        </q-card-section>
        <q-separator/>

        <q-card-section>
          <q-toolbar-title>Description:</q-toolbar-title>
          <div v-html="issue.description"></div>
        </q-card-section>

        <q-separator/>
        <q-card-section>
          <q-toolbar-title>Comments:</q-toolbar-title>
          <div class="q-px-lg q-pb-md">
            <q-timeline color="grey">
              <q-timeline-entry v-for="comment in issue.comments"
                                :title="`${comment.author.firstName} ${comment.author.lastName}`"
                                :subtitle="dateTimeToGermanLocaleString(comment.updatedDate)"
                                icon="chat"
                                :body="comment.content"
              />
            </q-timeline>
          </div>
          <q-btn  color="secondary" flat
                 icon="add_circle_outline"
                 label="Add comment"
          />
        </q-card-section>
      </q-card>
      <div class="col-3"></div>
    </div>
  </q-page>
</template>

<script lang="ts" setup>
import {$ref} from "vue/macros";
import {tickets} from "../model/mocked-date";
import {dateTimeToGermanLocaleString} from "../utils";
import {Ticket} from "../model/Ticket";
import EditIssueDialog from "../dialogs/edit-issue-dialog.vue";
import {useQuasar} from "quasar";

const quasar = useQuasar()

const props = defineProps<{
  id: number
}>()

const issue = $ref(tickets.value.find(ticket => ticket.id == props.id))

const editIssue = (issue: Ticket) => {
  quasar.dialog({
    component: EditIssueDialog,
    componentProps: {
      'item': {...issue},
    }
  }).onOk(async (editedItem: Ticket) => {
    tickets.value = tickets.value.map(ticket => {
      if (ticket.id == editedItem.id) return editedItem
      else return ticket
    })
  })
}

</script>

<style scoped>

</style>
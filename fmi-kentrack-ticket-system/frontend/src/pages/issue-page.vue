<template>
  <q-page padding>
    <div class="row ">
      <div class="col-3"></div>
      <q-card class="col">
        <q-card-section>
          <div class="row">
            <div class="text-h5">Issue {{ issue?.id }} - {{ issue?.title }}</div>
            <q-space/>
            <q-btn dense flat icon="edit"
                   no-caps
                   round
                   size="s"
                   @click="()=> editIssue(issue)"
            >
              <q-tooltip>Click to edit</q-tooltip>
            </q-btn>
          </div>
        </q-card-section>
        <q-separator/>
        <div class="row">
          <div class="col-10">
            <q-card-section>
              <q-toolbar-title>Description:</q-toolbar-title>
              <div v-html="issue?.description"></div>
            </q-card-section>
          </div>
          <div class="col-2 q-pa-sm">
            <q-input :model-value="`${issue?.assignee?.firstName} ${issue?.assignee?.lastName}`" class="col-12"
                     label="Assignee" readonly
                     stack-label></q-input>
            <q-input :model-value="`${issue?.owner?.firstName} ${issue?.owner?.lastName}`" class="col-12"
                     label="Reporter"
                     readonly
                     stack-label></q-input>
          </div>
        </div>
        <q-separator/>
        <q-card-section>
          <q-toolbar-title>Comments: <q-btn color="secondary" flat
                                            icon="add_circle_outline"
                                            @click="addCommentToTicket()">
            <q-tooltip>Add new comment</q-tooltip>
          </q-btn></q-toolbar-title>
          <div class="q-px-lg q-pb-md">
            <q-timeline color="grey">
              <q-timeline-entry v-for="comment in issue?.comments"
                                :body="comment?.content"
                                :subtitle="dateTimeToGermanLocaleString(comment?.updatedDate)"
                                :title="`${comment?.author?.firstName} ${comment?.author?.lastName}`"
                                icon="chat"
              />
            </q-timeline>
          </div>
        </q-card-section>
      </q-card>
      <div class="col-3">
      </div>
    </div>
  </q-page>
</template>

<script lang="ts" setup>
import {$computed} from "vue/macros";
import {commentPromiseDialog, dateTimeToGermanLocaleString} from "../utils";
import {Ticket} from "../model/Ticket";
import EditIssueDialog from "../dialogs/edit-issue-dialog.vue";
import {useQuasar} from "quasar";
import {allTickets} from "../model/constants";
import {createNewCommentForTicket, updateTicket} from "../services/request-service";

const quasar = useQuasar()

const props = defineProps<{
  id: number
}>()

const issue = $computed(() => allTickets.value.find(ticket => ticket.id == props.id))

const editIssue = (issue: Ticket) => {
  quasar.dialog({
    component: EditIssueDialog,
    componentProps: {
      'item': {...issue},
    }
  }).onOk(async (editedItem: Ticket) => {
    await updateTicket(editedItem).then(r => {
      allTickets.value = allTickets.value.map(ticket => {
        if (ticket.id == editedItem.id) return editedItem
        else return ticket
      })
    })
  })
}

const addCommentToTicket = async () => {
  const commentContent = await commentPromiseDialog()
  await createNewCommentForTicket(commentContent, +props.id).then(response => {
    allTickets.value = allTickets.value.map(ticket => {
      if (ticket.id == props.id) return {...issue, comments: [...ticket.comments, response]}
      else return ticket
    })
  })


}

</script>

<style scoped>

</style>
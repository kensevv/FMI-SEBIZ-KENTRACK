<template>
  <q-page padding>
    <div class="row">
      <div class="col-1"></div>
      <div class="col">

        <q-table :columns="columns" :rows="tickets" :pagination="{rowsPerPage: 0}" hide-pagination virtual-scroll>
          <template v-slot:body-cell-edit="props">
            <q-td>
              <q-btn color="primary" flat
                     icon="edit" no-caps round
                     size="s"
                     @click="()=>editIssue(props.row)"
              />
              <q-btn
                  color="negative"
                  flat
                  icon="delete"
                  round
                  size="s"
                  style="margin-left: 10px;"
                  @click="onDeleteClick(props.row)"

              />
              <q-btn flat
                     icon="open_in_new" no-caps round
                     size="s"
                     @click="()=>router.push(`/issue/${props.row.id}`)"
              />
            </q-td>
          </template>
        </q-table>
      </div>
      <div class="col-1"></div>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import {tickets} from "../model/mocked-date";

import {Ticket} from "../model/Ticket";
import {dateTimeToGermanLocaleString} from "../utils";
import {useRouter} from "vue-router";
import EditIssueDialog from "../dialogs/edit-issue-dialog.vue";
import {useQuasar} from "quasar";

const quasar = useQuasar()
const router = useRouter()

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

const columns = [
  {
    label: 'ID',
    name: 'id',
    field: (row: Ticket) => row.id,
    sortable: true,
    align: 'left'
  }, {
    label: 'Title',
    name: 'title',
    field: (row: Ticket) => row.title,
    sortable: true,
    align: 'left'
  }, {
    label: 'Owner',
    name: 'owner',
    field: (row: Ticket) => row.owner.firstName,
    sortable: true,
    align: 'left'
  }, {
    label: 'Assignee',
    name: 'assignee',
    field: (row: Ticket) => row.assignee.firstName,
    sortable: true,
    align: 'left'
  }, {
    label: 'Status',
    name: 'status',
    field: (row: Ticket) => row.section.title,
    sortable: true,
    align: 'left'
  }, {
    label: 'Board',
    name: 'board',
    field: (row: Ticket) => row.board.title,
    sortable: true,
    align: 'left'
  }, {
    label: 'Created',
    name: 'created',
    field: (row: Ticket) => dateTimeToGermanLocaleString(row.createdDate),
    sortable: true,
    align: 'left'
  }, {
    label: 'Updated',
    name: 'updated',
    field: (row: Ticket) => dateTimeToGermanLocaleString(row.updatedDate),
    sortable: true,
    align: 'left'
  },
  {
    name: 'edit',
    headerClasses: 'q-table--col-auto-width'
  },
]
</script>

<style scoped>

</style>
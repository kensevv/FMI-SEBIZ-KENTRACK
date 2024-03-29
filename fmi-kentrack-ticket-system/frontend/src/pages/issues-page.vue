<template>
  <q-page padding>
    <div class="row">
      <div class="col-1"></div>
      <div class="col">

        <q-table :columns="columns"
                 :filter="filter"
                 :pagination="{rowsPerPage: 0}"
                 :rows="filteredTickets"
                 hide-pagination
                 virtual-scroll>
          <template v-slot:top class="full-width bg-green">
            <div class="row full-width">
              <div class="col-1 q-table__title">All issues</div>
              <div class="col-7 offset-1 row q-gutter-sm">
                <q-select v-model="boardFilter" :options="allBoards" class="col" dense emit-value label="Board"
                          map-options
                          option-label="title"
                          option-value="id"
                          options-dense/>
                <q-select v-model="sectionFilter" :options="sections" class="col" dense emit-value
                          label="Status"
                          map-options
                          option-label="sectionName"
                          option-value="sectionName"
                          options-dense/>
                <q-select v-model="assigneeFilter" :option-label="user => `${user.firstName} ${user.lastName}`" :options="allUsers" class="col" dense emit-value
                          label="Assignee"
                          map-options
                          option-value="username"
                          options-dense/>
              </div>
              <q-input v-model="filter" class="col offset-1" clearable debounce="300" dense placeholder="Search">
                <template v-slot:append>
                  <q-icon name="search"/>
                </template>
              </q-input>
            </div>
          </template>
          <template v-slot:top-right>

          </template>
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

<script lang="ts" setup>
import {Ticket} from "../model/Ticket";
import {dateTimeToGermanLocaleString} from "../utils";
import {useRouter} from "vue-router";
import EditIssueDialog from "../dialogs/edit-issue-dialog.vue";
import {useQuasar} from "quasar";
import {allBoards, allTickets, allUsers, sections} from "../model/constants";
import {deleteTicketById, updateTicket} from "../services/request-service";
import {$computed, $ref} from "vue/macros";

const quasar = useQuasar()
const router = useRouter()
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
const onDeleteClick = async (issue: Ticket) => {
  await deleteTicketById(issue.id).then(r => {
    allTickets.value = allTickets.value.filter(ticket => ticket.id != issue.id)
  })
}
const filter = $ref('')
const boardFilter = $ref<number | null>(null)
const assigneeFilter = $ref<string | null>(null)
const sectionFilter = $ref<string>('')
const filteredTickets = $computed(() => {
  let result = allTickets.value
  if (boardFilter) {
    result = result.filter(ticket => ticket.board.id == boardFilter)
  }
  if (assigneeFilter && assigneeFilter !== '') {
    result = result.filter(ticket => ticket.assignee.username == assigneeFilter)
  }
  if (sectionFilter && sectionFilter !== '') {
    result = result.filter(ticket => ticket.section.sectionName == sectionFilter)
  }
  return result
})

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
    field: (row: Ticket) => row.section.sectionName,
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
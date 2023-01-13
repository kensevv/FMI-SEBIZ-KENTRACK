<template>
  <q-page padding>
    <div class="row items-start q-gutter-md wrap">
      <div v-for="board in boards" class="col">
        <q-card>
          <q-toolbar class="text-white shadow-4" style="background: #24292e">
            <q-icon name="dashboard" size="sm"/>
            <q-toolbar-title style="font-size: 3vh">
              {{ board.title }}
            </q-toolbar-title>
          </q-toolbar>
          <q-card-section>
            <div class="q-px-md">
              Board Owner: <span class="text-primary">{{ `${board.owner.firstName} ${board.owner.lastName}` }}</span>
              <br>
              Participants: <span class="text-primary"><span
                v-for="participant in board.participants">{{ participant?.firstName ?? "" }}, </span></span> <br>
              Created: <span class="text-primary">{{ dateTimeToGermanLocaleString(board.createdDate) }}</span> <br>
              Last Updated: <span class="text-primary">{{ dateTimeToGermanLocaleString(board.updatedDate) }}</span> <br>
            </div>
          </q-card-section>

          <q-separator/>

          <q-card-actions>
            <q-space/>
            <q-btn flat icon="edit" no-caps
                   round
                   size="s"
                   @click="()=> editBoard(board)"
            >
              <q-tooltip>Edit Board</q-tooltip>
            </q-btn>
            <q-btn flat icon="open_in_new" no-caps
                   round
                   size="s"
                   @click="()=> router.push(`/dashboard/${board.id}`)"
            >
              <q-tooltip>Open Board</q-tooltip>
            </q-btn>
          </q-card-actions>
        </q-card>

      </div>

    </div>
  </q-page>
</template>

<script setup lang="ts">
import {boards} from "../model/mocked-date";
import {dateTimeToGermanLocaleString} from "../utils";
import {useRouter} from "vue-router";
import {useQuasar} from "quasar";
import {Board} from "../model/Board";
import EditBoardDialog from "../dialogs/edit-board-dialog.vue";

const router = useRouter()
const quasar = useQuasar()

const editBoard = (issue: Board) => {
  quasar.dialog({
    component: EditBoardDialog,
    componentProps: {
      'item': {...issue},
    }
  }).onOk(async (editedItem: Board) => {
    boards.value = boards.value.map(board => {
      if (board.id == editedItem.id) return editedItem
      else return board
    })
  })
}


</script>

<style scoped>
.center {
  margin: auto;
  width: 50%;
  padding: 10px;
}
</style>
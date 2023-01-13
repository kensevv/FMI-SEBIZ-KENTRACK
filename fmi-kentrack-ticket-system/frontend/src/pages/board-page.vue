<template>
  <q-page>
    <div class="row" style="height: 100vh">
      <div v-for="section in sections" style="height: 100vh" class="col q-pa-sm">
        <q-card style="height: 100vh">
          <q-card-section class="bg-grey-8 text-white">
            <div class="text-h6">{{ section.title }}</div>
          </q-card-section>

          <q-card-section vertical align="left">
            <q-list>
              <div v-for="issue in issues.filter(it => it.section?.title === section?.title)">
                <q-item>
                  <q-card class="full-width bg-grey text-white">
                    <q-card-section>
                      <div>{{ issue.id }} {{ issue.title }}</div>
                      <q-separator color="white"/>
                      <div>
                        <q-icon size="sm" name="engineering"/>
                        {{ issue.assignee.firstName }}
                      </div>
                      <q-btn flat icon="open_in_new" no-caps
                             round
                             class="float-right"
                             size="s"
                             @click="()=> router.push(`/issue/${issue.id}`)"
                      >
                        <q-tooltip>Open Ticket</q-tooltip>
                      </q-btn>
                    </q-card-section>

                  </q-card>
                </q-item>
              </div>
            </q-list>
          </q-card-section>
        </q-card>
      </div>
    </div>

  </q-page>
  <q-footer reveal style="background-color: #24292e" elevated>
    <div class="row q-layout-padding">
      <div class="col text-h6">
        {{ board.title }} <br>
      </div>
      <div class="col text-h6">
        Board Owner: {{ board.owner.firstName }} {{ board.owner.lastName }}
      </div>
      <div class="col text-h6">
        Participants: {{ board.participants.map(p => p.firstName).join(", ") }}

      </div>
      <div class="col text-h8">
        Created: {{ dateTimeToGermanLocaleString(board.createdDate) }}<br>
        Last Updated: {{ dateTimeToGermanLocaleString(board.updatedDate) }}
      </div>
    </div>
  </q-footer>
</template>

<script setup lang="ts">

import {useQuasar} from "quasar";
import {useRouter} from "vue-router";
import {$ref} from "vue/macros";
import {boards, sections, tickets} from "../model/mocked-date";
import {dateTimeToGermanLocaleString} from "../utils";
import {Board} from "../model/Board";
import EditIssueDialog from "../dialogs/edit-issue-dialog.vue";

const quasar = useQuasar()
const router = useRouter()

const props = defineProps<{
  id: number
}>()
const board = $ref(boards.value.find(board => board.id == props.id))
const issues = $ref(tickets.value.filter(ticket => ticket.board.id === board?.id))

</script>

<style scoped>

</style>
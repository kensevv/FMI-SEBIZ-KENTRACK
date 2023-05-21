<template>
  <q-page>
    <div class="row" style="height: 100vh">
      <div v-for="section in sections" class="col q-pa-sm" style="height: 100vh">
        <q-card style="height: 100vh">
          <q-card-section class="bg-grey-8 text-white">
            <div class="text-h6">{{ section?.sectionName }}</div>
          </q-card-section>

          <q-card-section align="left" vertical>
            <q-list>
              <div v-for="issue in issues.filter(it => it.section?.sectionName === section?.sectionName)">
                <q-item>
                  <q-card class="full-width bg-grey text-white">
                    <q-card-section>
                      <div>{{ issue?.id }} {{ issue?.title }}</div>
                      <q-separator color="white"/>
                      <div>
                        <q-icon name="engineering" size="sm"/>
                        {{ issue?.assignee?.firstName }}
                      </div>
                      <q-btn class="float-right" flat icon="open_in_new"
                             no-caps
                             round
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
  <q-footer elevated reveal style="background-color: #24292e">
    <div class="row q-layout-padding">
      <div class="col text-h6">
        {{ board?.title }} <br>
      </div>
      <div class="col text-h6">
        Board Owner: {{ board?.owner?.firstName }} {{ board?.owner?.lastName }}
      </div>
      <div class="col text-h6">
        Participants: {{ board?.participants.map(p => p.firstName).join(", ") }}

      </div>
      <div class="col text-h8">
        Created: {{ dateTimeToGermanLocaleString(board?.createdDate) }}<br>
        Last Updated: {{ dateTimeToGermanLocaleString(board?.updatedDate) }}
      </div>
    </div>
  </q-footer>
</template>

<script lang="ts" setup>

import {useQuasar} from "quasar";
import {useRouter} from "vue-router";
import {$computed} from "vue/macros";
import {dateTimeToGermanLocaleString} from "../utils";
import {allBoards, allTickets, sections} from "../model/constants";

const quasar = useQuasar()
const router = useRouter()

const props = defineProps<{
  id: number
}>()
const board = $computed(() => allBoards.value.find(board => board.id == props.id))
const issues = $computed(() => allTickets.value.filter(ticket => ticket.board.id === board?.id))

</script>

<style scoped>

</style>
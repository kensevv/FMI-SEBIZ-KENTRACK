<template>
  <q-input :label="label"
           :model-value="formatToGerman(modelValue)"
           dense
           fill-mask
           filled
           mask="##.##.####"
           @update:model-value="updateModelValue($event)">
    <template v-slot:append>
      <q-icon class="cursor-pointer" name="event">
        <q-popup-proxy ref="qDateProxy"
                       transition-hide="scale"
                       transition-show="scale">
          <q-date :model-value="formatToGerman(modelValue)"
                  mask="DD.MM.YYYY"
                  @update:model-value="updateModelValue($event)">
            <div class="row items-center justify-end">
              <q-btn v-close-popup color="primary" flat label="Close"/>
            </div>
          </q-date>
        </q-popup-proxy>
      </q-icon>
    </template>
  </q-input>
</template>
<script lang="ts" setup>
import {date} from "quasar";
import {useI18n} from "vue-i18n";

const props = defineProps<{
  modelValue: string | null
  label: string
}>();

const emits = defineEmits<{
  (e: 'update:modelValue', value: string | null): void
}>();

const validDateInputCheck = new RegExp(/^\d{2}\.\d{2}\.\d{4}$/)
const updateModelValue = (ev) => {
  if (validDateInputCheck.test(ev)) {
    emits("update:modelValue", formatToDate(ev))
  }
}

const formatToGerman = (dateToFormat: string | null) => {
  if (dateToFormat == null || dateToFormat == '') {
    return null
  }
  return date.formatDate(new Date(dateToFormat), 'DD.MM.YYYY');
};

const formatToDate = (ev) => {
  if (ev == null || ev == '') {
    return null
  }
  return date.formatDate(date.extractDate(ev, 'DD.MM.YYYY'), "YYYY-MM-DD");
};


</script>
<style scoped>
input {
  margin-bottom: 20px;
}
</style>
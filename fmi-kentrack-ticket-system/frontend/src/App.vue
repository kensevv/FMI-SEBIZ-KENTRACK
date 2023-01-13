<template>
  <router-view/>
</template>

<script lang="ts" setup>
import {useQuasar} from "quasar";
import {setUpAxios} from "./boot/Axios";

const quasar = useQuasar()
setUpAxios(quasar)

</script>

<style>
</style>

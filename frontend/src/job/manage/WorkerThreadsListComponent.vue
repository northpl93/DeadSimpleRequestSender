<script setup lang="ts">
import { onMounted, onUnmounted, ref, type Ref } from 'vue';
import { useManageJobStore } from './ManageJobStore';
import type { WorkerThread } from '@/job';
import { getJobThreads } from '@/job/JobAPI';

const headers = [
  {
    key: "threadId",
    title: "Thread ID"
  },
  {
    key: "name",
    title: "Thread name"
  },
  {
    key: "terminationRequested",
    title: "Is terminating"
  }
]

const manageJobStore = useManageJobStore()

const threadList: Ref<WorkerThread[]> = ref([])
function fetchThreadsList() {
  getJobThreads(manageJobStore.jobId).then(response => threadList.value = response.data)
}

let autoRefreshTimer: number
onMounted(() => {
  fetchThreadsList()
  autoRefreshTimer = setInterval(fetchThreadsList, 5000);
})
onUnmounted(() => {
  clearInterval(autoRefreshTimer)
})
</script>

<template>
  <h2>Worker threads</h2>
  <v-data-table :headers="headers" :items="threadList" disable-sort>
  </v-data-table>
</template>

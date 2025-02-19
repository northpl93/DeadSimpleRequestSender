<script setup lang="ts">
import { onMounted, onUnmounted, ref, type Ref } from 'vue';
import { useRouter } from 'vue-router';
import { getJobList } from '../JobAPI';
import type { Job } from '@/job';

const router = useRouter()

const jobList: Ref<Job[]> = ref([])
function fetchJobList() {
  getJobList().then(response => {
    jobList.value = response.data
  })
}

const headers = [
  {
    key: "displayName",
    title: "Job name"
  },
  {
    key: "jobConfig.request.url",
    title: "URL"
  },
  {
    key: "targetWorkerThreads",
    title: "Worker threads"
  }
]

// eslint-disable-next-line @typescript-eslint/no-explicit-any
function rowClick(event: MouseEvent, row: any) {
  router.push({ name: 'manageJob', params: { id: row.item.jobId } })
}

let autoRefreshTimer: number
onMounted(() => {
  fetchJobList()
  autoRefreshTimer = setInterval(fetchJobList, 1000);
})
onUnmounted(() => {
  clearInterval(autoRefreshTimer)
})
</script>

<template>
  <v-container>
    <v-row>
      <v-col cols="8">
        <h1>List of running jobs</h1>
      </v-col>
      <v-col align="right" class="align-self-center" cols="4">
        <v-btn to="/jobs/create">Create a new job</v-btn>
      </v-col>
    </v-row>

    <v-row>
      <v-data-table :headers="headers" :items="jobList" disable-sort @click:row="rowClick">
      </v-data-table>
    </v-row>
  </v-container>
</template>

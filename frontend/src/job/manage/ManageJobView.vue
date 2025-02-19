<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router';
import { killJob } from '@/job/JobAPI';
import JobDefinitionPreviewComponent from './JobDefinitionPreviewComponent.vue';
import { useManageJobStore } from './ManageJobStore';
import JobBaseInfoComponent from './JobBaseInfoComponent.vue';
import WorkerThreadsListComponent from './WorkerThreadsListComponent.vue';

const route = useRoute()
const router = useRouter()

const manageJobStore = useManageJobStore()
manageJobStore.loadJobIntoStore(route.params.id as string)

function handleKillJobButton() {
  killJob(manageJobStore.jobId).then(() => {
    router.push({ name: 'jobList' })
  })
}
</script>

<template>
  <v-container>
    <v-row>
      <v-col cols="8">
        <h1 v-if="manageJobStore.jobData != null">
          {{ manageJobStore.jobData.displayName }} <v-chip v-if="manageJobStore.jobData?.terminationRequested" color="red">Terminating</v-chip>
        </h1>
      </v-col>
      <v-col align="right" class="align-self-center" cols="4">
        <v-btn @click="handleKillJobButton">Kill</v-btn>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="6">
        <JobBaseInfoComponent />
      </v-col>
      <v-col cols="6">
        <JobDefinitionPreviewComponent :job-id="route.params.id as string" />
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12">
        <WorkerThreadsListComponent />
      </v-col>
    </v-row>

  </v-container>
</template>

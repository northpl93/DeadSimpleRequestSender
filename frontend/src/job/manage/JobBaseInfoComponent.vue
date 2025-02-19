<script setup lang="ts">
import { useManageJobStore } from './ManageJobStore';
import SetTargetWorkerThreadsDialog from './SetTargetWorkerThreadsDialog.vue';

const manageJobStore = useManageJobStore()
</script>

<template>
  <v-table v-if="manageJobStore.jobData != null">
    <tbody>
      <tr>
        <td>Job ID</td>
        <td>{{ manageJobStore.jobData.jobId }}</td>
      </tr>
      <tr>
        <td>Work directory</td>
        <td>{{ manageJobStore.jobData.workDir }}</td>
      </tr>
      <tr>
        <td>Target worker threads</td>
        <td>{{ manageJobStore.jobData.targetWorkerThreads }} <SetTargetWorkerThreadsDialog /></td>
      </tr>
    </tbody>
  </v-table>
  <v-skeleton-loader v-else type="article"></v-skeleton-loader>
</template>

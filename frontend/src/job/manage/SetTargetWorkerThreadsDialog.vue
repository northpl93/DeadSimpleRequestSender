<script setup lang="ts">
import { updateJobTargetWorkerThreads } from '@/job/JobAPI';
import { useManageJobStore } from './ManageJobStore';

const manageJobStore = useManageJobStore()

const dialogModel = defineModel<boolean>("dialog")
const textInputModel = defineModel<string>("newTargetWorkerThreads")

manageJobStore.$subscribe((mutation, state) => {
  textInputModel.value = String(state.jobData?.targetWorkerThreads)
})

function handleUpdateWorkerThreadsButton() {
  const newWorkerThreads = parseInt(textInputModel.value as string)
  updateJobTargetWorkerThreads(manageJobStore.jobData!.jobId, newWorkerThreads).then(() => {
    manageJobStore.updateWorkerThreadsState(newWorkerThreads)
  })
  dialogModel.value = false
}
</script>

<template>
  <v-btn icon="mdi-pencil" variant="plain" @click="dialogModel = true"></v-btn>
  <v-dialog v-model="dialogModel" max-width="500">
    <v-card title="New target worker threads">
      <v-card-text>
        <v-text-field type="number" v-model="textInputModel" />
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn text="Update" @click="handleUpdateWorkerThreadsButton"></v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { ref, type Ref } from 'vue';
import { submitJob } from '@/job/JobAPI';
import { useCreateJobStore } from '@/job/create/CreateJobStore';
import { useRouter } from 'vue-router';

const router = useRouter()
const createJobStore = useCreateJobStore()
const submittingJob: Ref<boolean> = ref(false)

function handleSubmitJobButton() {
  submittingJob.value = true
  submitJob(createJobStore.jobDefinition).then(response => {
    router.push({ name: 'manageJob', params: { id: response.data.jobId } })
  })
}
</script>

<template>
  <v-btn :loading="submittingJob" class="flex-grow-1" height="48" variant="tonal" @click="handleSubmitJobButton">Submit</v-btn>
</template>

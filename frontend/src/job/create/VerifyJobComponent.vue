<script setup lang="ts">
import VCodeBlock from '@wdns/vue-code-block';
import { onMounted, onUnmounted, ref } from 'vue';
import { useCreateJobStore } from './CreateJobStore';
import { getJobYamlPreview } from '@/job/JobAPI';
import SubmitJobButtonComponent from '@/job/create/SubmitJobButtonComponent.vue';

const createJobStore = useCreateJobStore()
const verificationResult = ref<string | null>(null)

onMounted(() => {
  getJobYamlPreview(createJobStore.jobDefinition).then(response => {
    verificationResult.value = response.data
  })
})

onUnmounted(() => {
  verificationResult.value = null
})

function onGoBackButton() {
  createJobStore.updateStepperState(2)
}
</script>

<template>
  <v-card v-if="verificationResult != null" title="Validate job definition" flat>
    <v-container>
      <v-row>
        <v-col cols="12">
          <VCodeBlock :code="verificationResult" highlightjs lang="yaml" theme="default" />
        </v-col>
      </v-row>

      <v-row>
        <v-col align="left" class="align-self-center" cols="2">
          <v-btn class="flex-grow-1" height="48" variant="tonal" @click="onGoBackButton">Back</v-btn>
        </v-col>
        <v-col cols="8"></v-col>
        <v-col align="right" class="align-self-center" cols="2">
          <SubmitJobButtonComponent />
        </v-col>
      </v-row>
    </v-container>
  </v-card>
  <v-skeleton-loader v-else type="article"></v-skeleton-loader>
</template>

<script setup lang="ts">
import { ref, type Ref } from 'vue';
import { useCreateJobStore } from '@/job/create/CreateJobStore';
import { getJobTemplates } from '../JobAPI';
import type { JobTemplate } from '..';

const createJobStore = useCreateJobStore()

const jobTemplates: Ref<JobTemplate[] | null> = ref(null);
getJobTemplates().then(response => {
  jobTemplates.value = response.data
})

const selected: Ref<string | null> = ref(null);
function onTemplateSelected(jobTemplate: JobTemplate) {
  selected.value = jobTemplate.name
  createJobStore.updateJobDefinition(jobTemplate.definition)
}

function onGoNextButton() {
  createJobStore.updateStepperState(2)
}
</script>

<template>
  <v-container v-if="jobTemplates != null">
    <v-row>
      <v-col cols="12">
        <v-list lines="two">
          <v-list-item v-for="jobTemplate in jobTemplates" :key="jobTemplate.name" :active="jobTemplate.name === selected" @click="onTemplateSelected(jobTemplate)">
            <v-list-item-title>{{ jobTemplate.name }}</v-list-item-title>
            <v-list-item-subtitle>{{ jobTemplate.description }}</v-list-item-subtitle>
          </v-list-item>
        </v-list>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="8"></v-col>
      <v-col align="right" class="align-self-center" cols="4">
        <v-btn class="flex-grow-1" height="48" variant="tonal" @click="onGoNextButton">
          {{ selected == null ? "Continue without template" : "Continue with template" }}
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
  <v-skeleton-loader v-else type="article"></v-skeleton-loader>
</template>

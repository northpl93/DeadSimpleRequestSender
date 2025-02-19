<script setup lang="ts">
import CreateJobForm from './CreateJobFormComponent.vue';
import VerifyJob from './VerifyJobComponent.vue';
import SelectTemplate from './SelectTemplateComponent.vue';
import { useCreateJobStore } from './CreateJobStore';
import { onBeforeUnmount } from 'vue';

const steps = ['Select template', 'Configure job', 'Validate']
const createJobStore = useCreateJobStore()
onBeforeUnmount(() => createJobStore.reset())
</script>

<template>
  <v-container class="bg-surface-variant">
    <v-stepper :items="steps" :model-value="createJobStore.stepperState" @update:model-value="createJobStore.updateStepperState" hide-actions>
      <template v-slot:item.1>
        <SelectTemplate v-if="createJobStore.stepperState == 1" />
      </template>

      <template v-slot:item.2>
        <CreateJobForm />
      </template>

      <template v-slot:item.3>
        <VerifyJob v-if="createJobStore.stepperState == 3" />
      </template>
    </v-stepper>
  </v-container>
</template>

<script setup lang="ts">
import { type JobDefinition } from '@/job'
import uiSchema from '@/job/create/CreateJobFormLayout.json'
import { createAjv } from '@jsonforms/core';
import { JsonForms, type JsonFormsChangeEvent } from '@jsonforms/vue';
import { extendedVuetifyRenderers } from '@jsonforms/vue-vuetify';
import { markRaw, ref, type Ref } from 'vue';
import { useCreateJobStore } from '@/job/create/CreateJobStore';
import { getJobDefinitionSchema } from '@/job/JobAPI';
import { customLayouts } from '@/job/create/CreateJobFormCustomLayouts';

const createJobStore = useCreateJobStore()

const customAjv = createAjv({ useDefaults: true });
const renderers = markRaw([
  ...extendedVuetifyRenderers,
]);

const jobDefinitionSchema: Ref<object | null> = ref(null);
getJobDefinitionSchema().then(response => {
  jobDefinitionSchema.value = response.data
})

function onJsonFormChange(event: JsonFormsChangeEvent) {
  createJobStore.jobDefinition = event.data as JobDefinition
}

function onGoBackButton() {
  createJobStore.reset()
}

function onGoNextButton() {
  createJobStore.updateStepperState(3)
}
</script>

<template>
  <v-container v-if="jobDefinitionSchema != null">
    <v-row>
      <v-col cols="12">
        <JsonForms
          :ajv="customAjv"
          :renderers="renderers"
          :data="createJobStore.jobDefinition"
          :schema="jobDefinitionSchema"
          :uischema="uiSchema"
          :uischemas="customLayouts"
          @change="onJsonFormChange"
        />
      </v-col>
    </v-row>
    <v-row>
      <v-col align="left" class="align-self-center" cols="2">
          <v-btn class="flex-grow-1" height="48" variant="tonal" @click="onGoBackButton">Clear and go back</v-btn>
        </v-col>
      <v-col cols="8"></v-col>
      <v-col align="right" class="align-self-center" cols="2">
        <v-btn class="flex-grow-1" height="48" variant="tonal" @click="onGoNextButton">Next</v-btn>
      </v-col>
    </v-row>
  </v-container>
  <v-skeleton-loader v-else type="article"></v-skeleton-loader>
</template>

<style>
@import '@jsonforms/vue-vuetify/lib/jsonforms-vue-vuetify.css';
</style>

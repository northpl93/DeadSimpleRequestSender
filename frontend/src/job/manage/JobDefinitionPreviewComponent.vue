<script setup lang="ts">
import VCodeBlock from '@wdns/vue-code-block';
import { useManageJobStore } from './ManageJobStore';

const manageJobStore = useManageJobStore()
</script>

<template>
  <VCodeBlock v-if="manageJobStore.jobYaml != null" :code="manageJobStore.jobYaml" highlightjs lang="yaml" theme="default" />
  <v-skeleton-loader v-else type="article"></v-skeleton-loader>
</template>

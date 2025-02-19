<script setup lang="ts">
import { RouterView } from 'vue-router'
</script>

<template>
  <v-layout class="rounded rounded-md">
    <v-app-bar color="surface-variant">
      <template #prepend>
        <v-app-bar-title>DeadSimple*RequestSender</v-app-bar-title>

        <v-tabs>
          <v-tab to="/">Jobs</v-tab>
        </v-tabs>
      </template>
    </v-app-bar>

    <v-main class="d-flex align-center justify-center" style="min-height: 300px; min-width: 500px;">
      <RouterView />
    </v-main>
  </v-layout>
</template>

<style scoped>
.v-app-bar-title {
  margin-left: 20px;
  margin-right: 20px;
}
</style>

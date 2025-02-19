import { defineStore } from "pinia";
import { ref, type Ref } from "vue";
import type { Job } from "@/job";
import { getJob, getJobYaml } from "@/job/JobAPI";

export const useManageJobStore = defineStore('manageJob', () => {
  const jobId: Ref<string> = ref("")
  const jobData: Ref<Job | null> = ref(null)
  const jobYaml: Ref<string | null> = ref(null)

  function loadJobIntoStore(requestedJobId: string) {
    jobId.value = requestedJobId
    getJob(requestedJobId).then(response => jobData.value = response.data)
    getJobYaml(requestedJobId).then(response => jobYaml.value = response.data)
  }

  function updateWorkerThreadsState(newWorkerThreads: number) {
    jobData.value!.targetWorkerThreads = newWorkerThreads
  }

  return { jobId, jobData, jobYaml, loadJobIntoStore, updateWorkerThreadsState }
})

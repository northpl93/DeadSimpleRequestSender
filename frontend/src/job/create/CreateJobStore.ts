import { ref, type Ref } from 'vue'
import { defineStore } from 'pinia'
import type { JobDefinition } from '@/job'

export const useCreateJobStore = defineStore('createJob', () => {
  const stepperState: Ref<number> = ref(1)
  function updateStepperState(newStepperState: unknown) {
    stepperState.value = newStepperState as number
  }

  const jobDefinition: Ref<JobDefinition> = ref(emptyJobDefinition())
  function updateJobDefinition(newJobDefinition: JobDefinition) {
    jobDefinition.value = newJobDefinition;
  }

  function reset() {
    jobDefinition.value = emptyJobDefinition()
    stepperState.value = 1
  }

  return { stepperState, updateStepperState, jobDefinition, updateJobDefinition, reset }
})

function emptyJobDefinition(): JobDefinition {
  return {
    executor: {
      threads: 0
    },
    request: {
      url: "http://",
      verb: "POST",
      headers: {
        "Content-Type": "application/json"
      }
    },
  }
}

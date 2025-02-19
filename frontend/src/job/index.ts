export interface JobTemplate {
  name: string,
  description: string,
  definition: JobDefinition
}

export type JobDefinition = object

export interface Job {
  jobId: string,
  displayName: string,
  workDir: string,
  jobConfig: JobDefinition,
  targetWorkerThreads: number,
  terminationRequested: boolean
}

export interface WorkerThread {
  threadId: number,
  threadName: string,
  terminationRequested: boolean
}

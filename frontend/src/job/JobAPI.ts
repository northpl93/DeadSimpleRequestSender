import apiClient from "@/API";
import type { AxiosResponse } from "axios";
import type { Job, JobDefinition, JobTemplate, WorkerThread } from "@/job";

export function getJobTemplates(): Promise<AxiosResponse<JobTemplate[]>> {
  return apiClient.get("/new-job/templates")
}

export function getJobDefinitionSchema(): Promise<AxiosResponse<object>> {
  return apiClient.get("/new-job/schema")
}

export function getJobYamlPreview(jobDefinition: JobDefinition): Promise<AxiosResponse<string>> {
  return apiClient.post("/new-job/preview", jobDefinition)
}

export function submitJob(jobDefinition: JobDefinition): Promise<AxiosResponse<Job>> {
  return apiClient.post("/jobs", jobDefinition)
}

export function getJobList(): Promise<AxiosResponse<Job[]>> {
  return apiClient.get(`/jobs`)
}

export function getJob(jobId: string): Promise<AxiosResponse<Job>> {
  return apiClient.get(`/jobs/${jobId}`)
}

export function getJobYaml(jobId: string): Promise<AxiosResponse<string>> {
  return apiClient.get(`/jobs/${jobId}/yaml`)
}

export function getJobThreads(jobId: string): Promise<AxiosResponse<WorkerThread[]>> {
  return apiClient.get(`/jobs/${jobId}/threads`)
}

export function updateJobTargetWorkerThreads(jobId: string, targetWorkerThreads: number): Promise<AxiosResponse<unknown>> {
  return apiClient.patch(`/jobs/${jobId}`, { targetWorkerThreads: targetWorkerThreads })
}

export function killJob(jobId: string): Promise<AxiosResponse<unknown>> {
  return apiClient.delete(`/jobs/${jobId}`)
}

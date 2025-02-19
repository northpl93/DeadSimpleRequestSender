import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      redirect: { path: "/jobs" },
    },
    {
      path: '/jobs',
      name: 'jobList',
      component: () => import('@/job/list/JobListView.vue'),
    },
    {
      path: '/jobs/:id',
      name: 'manageJob',
      component: () => import('@/job/manage/ManageJobView.vue'),
    },
    {
      path: '/jobs/create',
      name: 'createJob',
      component: () => import('@/job/create/CreateJobView.vue'),
    },
  ],
})

export default router

import axios, { type AxiosInstance } from 'axios'

const apiClient: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_DSRS_API,
  headers: {
    'Content-type': 'application/json',
  },
})

export default apiClient

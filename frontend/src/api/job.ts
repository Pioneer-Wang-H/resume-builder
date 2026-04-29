import request from './request'

export const getApplications = () => request.get('/job-application')
export const getApplicationStats = () => request.get('/job-application/stats')
export const saveApplication = (data: any) => request.post('/job-application', data)
export const updateApplication = (id: number, data: any) => request.put(`/job-application/${id}`, data)
export const deleteApplication = (id: number) => request.delete(`/job-application/${id}`)

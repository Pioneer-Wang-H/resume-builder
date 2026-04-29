import request from './request'

export const getResumes = () => request.get('/resume')
export const getResume = (id: number) => request.get(`/resume/${id}`)
export const createResume = (data: { title: string; templateId?: number }) => request.post('/resume', data)
export const deleteResume = (id: number) => request.delete(`/resume/${id}`)
export const updateResumeTitle = (id: number, data: { title: string }) => request.put(`/resume/${id}/title`, data)
export const setDefaultResume = (id: number) => request.put(`/resume/${id}/default`)
export const configureSections = (id: number, sections: any[]) => request.put(`/resume/${id}/sections`, sections)
export const assembleResume = (id: number) => request.get(`/resume/${id}/assemble`)

export const exportResumePdf = (id: number) => request.get(`/resume/${id}/export-pdf`, { responseType: 'blob' })

// Templates
export const getTemplates = () => request.get('/template')
export const getTemplate = (id: number) => request.get(`/template/${id}`)

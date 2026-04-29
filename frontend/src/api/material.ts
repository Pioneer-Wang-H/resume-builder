import request from './request'

// Basic Info
export const getBasicInfo = () => request.get('/basic-info')
export const saveBasicInfo = (data: any) => request.put('/basic-info', data)

// Education
export const getEducations = () => request.get('/education')
export const saveEducation = (data: any) => request.post('/education', data)
export const updateEducation = (id: number, data: any) => request.put(`/education/${id}`, data)
export const deleteEducation = (id: number) => request.delete(`/education/${id}`)

// Project
export const getProjects = () => request.get('/project-experience')
export const saveProject = (data: any) => request.post('/project-experience', data)
export const updateProject = (id: number, data: any) => request.put(`/project-experience/${id}`, data)
export const deleteProject = (id: number) => request.delete(`/project-experience/${id}`)

// Work
export const getWorks = () => request.get('/work-experience')
export const saveWork = (data: any) => request.post('/work-experience', data)
export const updateWork = (id: number, data: any) => request.put(`/work-experience/${id}`, data)
export const deleteWork = (id: number) => request.delete(`/work-experience/${id}`)

// Skills
export const getSkills = () => request.get('/skill-tag')
export const saveSkill = (data: any) => request.post('/skill-tag', data)
export const batchSaveSkills = (data: any[]) => request.post('/skill-tag/batch', data)
export const updateSkill = (id: number, data: any) => request.put(`/skill-tag/${id}`, data)
export const deleteSkill = (id: number) => request.delete(`/skill-tag/${id}`)

// Certificates
export const getCertificates = () => request.get('/certificate')
export const saveCertificate = (data: any) => request.post('/certificate', data)
export const updateCertificate = (id: number, data: any) => request.put(`/certificate/${id}`, data)
export const deleteCertificate = (id: number) => request.delete(`/certificate/${id}`)

// Self Evaluation
export const getSelfEvaluation = () => request.get('/self-evaluation')
export const saveSelfEvaluation = (data: any) => request.put('/self-evaluation', data)

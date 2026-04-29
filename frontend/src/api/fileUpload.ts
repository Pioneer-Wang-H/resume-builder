import request from './request'

export const uploadResumeFile = (file: File) => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/file/upload-resume', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })
}

export const importResumeData = (data: any) => request.post('/file/import-resume', data)

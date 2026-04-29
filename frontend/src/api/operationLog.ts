import request from './request'

export const getOperationLogs = (params: { page: number; pageSize: number; module?: string }) =>
  request.get('/operation-log', { params })

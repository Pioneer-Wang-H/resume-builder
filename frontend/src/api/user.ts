import request from './request'

export const login = (data: { username: string; password: string }) =>
  request.post('/user/login', data)

export const register = (data: { username: string; password: string; nickname?: string; email?: string }) =>
  request.post('/user/register', data)

export const logout = () => request.post('/user/logout')

export const getUserInfo = () => request.get('/user/info')

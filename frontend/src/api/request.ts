import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

const request = axios.create({
  baseURL: '/api',
  timeout: 15000,
})

request.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = token
  }
  return config
})

request.interceptors.response.use(
  (response) => {
    // 二进制响应（如 PDF 导出）直接返回 blob，不走 JSON code 检查
    if (response.config.responseType === 'blob') {
      return response.data
    }
    const res = response.data
    if (res.code === 401) {
      localStorage.removeItem('token')
      router.push('/login')
      return Promise.reject(new Error(res.message))
    }
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message))
    }
    return res
  },
  (error) => {
    ElMessage.error('网络异常，请稍后重试')
    return Promise.reject(error)
  }
)

export default request

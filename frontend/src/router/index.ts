import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/Login.vue'),
    },
    {
      path: '/',
      component: () => import('@/layouts/MainLayout.vue'),
      redirect: '/dashboard',
      children: [
        {
          path: 'dashboard',
          name: 'Dashboard',
          component: () => import('@/views/Dashboard.vue'),
          meta: { title: '工作台' },
        },
        {
          path: 'material/basic',
          name: 'BasicInfo',
          component: () => import('@/views/material/BasicInfo.vue'),
          meta: { title: '基本信息' },
        },
        {
          path: 'material/education',
          name: 'Education',
          component: () => import('@/views/material/Education.vue'),
          meta: { title: '教育经历' },
        },
        {
          path: 'material/project',
          name: 'ProjectExperience',
          component: () => import('@/views/material/ProjectExperience.vue'),
          meta: { title: '项目经历' },
        },
        {
          path: 'material/work',
          name: 'WorkExperience',
          component: () => import('@/views/material/WorkExperience.vue'),
          meta: { title: '工作经历' },
        },
        {
          path: 'material/skills',
          name: 'Skills',
          component: () => import('@/views/material/Skills.vue'),
          meta: { title: '技能标签' },
        },
        {
          path: 'material/certificates',
          name: 'Certificates',
          component: () => import('@/views/material/Certificates.vue'),
          meta: { title: '证书奖项' },
        },
        {
          path: 'material/evaluation',
          name: 'SelfEvaluation',
          component: () => import('@/views/material/SelfEvaluation.vue'),
          meta: { title: '自我评价' },
        },
        {
          path: 'material/import',
          name: 'ResumeImport',
          component: () => import('@/views/material/ResumeImport.vue'),
          meta: { title: '文件导入' },
        },
        {
          path: 'resume/list',
          name: 'ResumeList',
          component: () => import('@/views/resume/ResumeList.vue'),
          meta: { title: '简历管理' },
        },
        {
          path: 'resume/:id/assemble',
          name: 'ResumeAssemble',
          component: () => import('@/views/resume/ResumeAssemble.vue'),
          meta: { title: '简历组装' },
        },
        {
          path: 'resume/:id/preview',
          name: 'ResumePreview',
          component: () => import('@/views/resume/ResumePreview.vue'),
          meta: { title: '简历预览' },
        },
        {
          path: 'job/list',
          name: 'JobList',
          component: () => import('@/views/job/JobList.vue'),
          meta: { title: '投递管理' },
        },
        {
          path: 'job/stats',
          name: 'JobStats',
          component: () => import('@/views/job/JobStats.vue'),
          meta: { title: '投递统计' },
        },
        {
          path: 'system/log',
          name: 'OperationLog',
          component: () => import('@/views/system/OperationLog.vue'),
          meta: { title: '操作日志' },
        },
      ],
    },
  ],
})

router.beforeEach((to, _from) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    return '/login'
  } else if (to.path === '/login' && token) {
    return '/'
  }
})

export default router

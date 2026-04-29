<template>
  <div class="dashboard">
    <h2>工作台</h2>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>📄 我的简历</template>
          <div class="stat-number">{{ stats.resumeCount }}</div>
          <el-link type="primary" href="/resume/list">管理简历 →</el-link>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>📬 投递记录</template>
          <div class="stat-number">{{ stats.applicationCount }}</div>
          <el-link type="primary" href="/job/list">查看记录 →</el-link>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>📊 面试邀约</template>
          <div class="stat-number">{{ stats.interviewCount }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>快捷入口</template>
          <el-space>
            <el-button type="primary" @click="$router.push('/material/basic')">完善基本信息</el-button>
            <el-button type="primary" @click="$router.push('/material/project')">添加项目经历</el-button>
            <el-button type="success" @click="$router.push('/resume/list')">新建简历</el-button>
          </el-space>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>使用说明</template>
          <ol>
            <li>在<strong>素材仓库</strong>中填写所有经历</li>
            <li>在<strong>简历管理</strong>中创建多份简历</li>
            <li>每份简历<strong>勾选不同素材</strong>适配不同岗位</li>
            <li>一键<strong>导出 PDF</strong></li>
          </ol>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { reactive, onMounted } from 'vue'
import { getResumes } from '@/api/resume'
import { getApplications } from '@/api/job'

const stats = reactive({ resumeCount: 0, applicationCount: 0, interviewCount: 0 })

onMounted(async () => {
  try {
    const r: any = await getResumes()
    stats.resumeCount = r.data?.length || 0
    const a: any = await getApplications()
    stats.applicationCount = a.data?.length || 0
    stats.interviewCount = a.data?.filter((item: any) => item.status === '面试').length || 0
  } catch {}
})
</script>

<style scoped>
.stat-number { font-size: 36px; font-weight: 700; color: #409EFF; margin: 12px 0; }
ol { padding-left: 20px; line-height: 2; }
</style>

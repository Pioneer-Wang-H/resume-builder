<template>
  <div class="page">
    <div class="preview-toolbar">
      <h2>简历预览</h2>
      <el-space>
        <el-button @click="$router.push(`/resume/${resumeId}/assemble`)">返回编辑</el-button>
        <el-button type="primary" @click="handleExportPDF" :loading="exporting">导出 PDF</el-button>
        <el-button @click="window.print()">打印</el-button>
      </el-space>
    </div>

    <div class="preview-container">
      <!-- Classic Template -->
      <div class="resume-paper template-classic" v-if="resume?.templateId === 1 || !resume?.templateId">
        <!-- Sidebar -->
        <div class="sidebar">
          <div class="avatar-circle">{{ basicInfo?.name?.charAt(0) || '?' }}</div>
          <div class="contact-info">
            <div v-if="basicInfo?.phone">📱 {{ basicInfo.phone }}</div>
            <div v-if="basicInfo?.email">📧 {{ basicInfo.email }}</div>
            <div v-if="basicInfo?.city">📍 {{ basicInfo.city }}</div>
            <div v-if="basicInfo?.github">🔗 {{ basicInfo.github }}</div>
          </div>

          <div class="section" v-if="data.skills?.length">
            <h3>技能</h3>
            <div class="skill-tags">
              <span v-for="s in data.skills" :key="s.id" class="skill-tag">{{ s.skillName }}</span>
            </div>
          </div>
        </div>

        <!-- Main Content -->
        <div class="main">
          <div class="header">
            <h1>{{ basicInfo?.name || '未填写姓名' }}</h1>
            <p class="position">{{ basicInfo?.intendedPosition }}</p>
          </div>

          <div class="section" v-if="data.education?.length">
            <h3>教育经历</h3>
            <div v-for="e in data.education" :key="e.id" class="item">
              <div class="item-header">
                <strong>{{ e.school }}</strong>
                <span>{{ e.startDate }} ~ {{ e.endDate }}</span>
              </div>
              <div class="item-sub">{{ e.major }} · {{ e.degree }}<span v-if="e.gpa"> · GPA {{ e.gpa }}</span></div>
              <p v-if="e.description">{{ e.description }}</p>
            </div>
          </div>

          <div class="section" v-if="data.workExperience?.length">
            <h3>工作经历</h3>
            <div v-for="w in data.workExperience" :key="w.id" class="item">
              <div class="item-header">
                <strong>{{ w.company }}</strong>
                <span>{{ w.startDate }} ~ {{ w.endDate }}</span>
              </div>
              <div class="item-sub">{{ w.position }}</div>
              <p v-if="w.description">{{ w.description }}</p>
            </div>
          </div>

          <div class="section" v-if="data.projectExperience?.length">
            <h3>项目经历</h3>
            <div v-for="p in data.projectExperience" :key="p.id" class="item">
              <div class="item-header">
                <strong>{{ p.projectName }}</strong>
                <span>{{ p.startDate }} ~ {{ p.endDate }}</span>
              </div>
              <div class="item-sub">{{ p.role }}</div>
              <p v-if="p.description">{{ p.description }}</p>
            </div>
          </div>

          <div class="section" v-if="data.certificates?.length">
            <h3>证书奖项</h3>
            <div v-for="c in data.certificates" :key="c.id" class="item">
              <strong>{{ c.name }}</strong>
              <span v-if="c.issuingAuthority"> · {{ c.issuingAuthority }}</span>
              <span v-if="c.obtainDate"> · {{ c.obtainDate }}</span>
            </div>
          </div>

          <div class="section" v-if="data.selfEvaluation?.content">
            <h3>自我评价</h3>
            <p>{{ data.selfEvaluation.content }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { assembleResume, exportResumePdf } from '@/api/resume'
import { ElMessage } from 'element-plus'

const route = useRoute()
const resumeId = Number(route.params.id)

const resume = ref<any>(null)
const data = ref<any>({})
const basicInfo = ref<any>(null)
const exporting = ref(false)

onMounted(async () => {
  try {
    const res: any = await assembleResume(resumeId)
    resume.value = res.data?.resume
    basicInfo.value = res.data?.basicInfo
    data.value = res.data
  } catch {}
})

async function handleExportPDF() {
  exporting.value = true
  try {
    const blob: any = await exportResumePdf(resumeId)
    const url = URL.createObjectURL(new Blob([blob], { type: 'application/pdf' }))
    const link = document.createElement('a')
    link.href = url
    link.download = `resume-${resumeId}.pdf`
    link.click()
    URL.revokeObjectURL(url)
    ElMessage.success('PDF 导出成功')
  } catch {
    ElMessage.error('PDF 导出失败，请重试')
  } finally { exporting.value = false }
}
</script>

<style scoped>
.preview-toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.preview-container { display: flex; justify-content: center; }

.resume-paper {
  width: 210mm;
  min-height: 297mm;
  background: #fff;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
  display: flex;
}

.template-classic .sidebar {
  width: 220px;
  background: #1a365d;
  color: #e2e8f0;
  padding: 32px 20px;
}

.template-classic .main {
  flex: 1;
  padding: 32px 40px;
}

.avatar-circle {
  width: 80px; height: 80px; border-radius: 50%;
  background: #4299e1; display: flex; align-items: center; justify-content: center;
  font-size: 32px; color: #fff; margin: 0 auto 20px;
}

.contact-info { font-size: 13px; line-height: 2; margin-bottom: 24px; }

.skill-tags { display: flex; flex-wrap: wrap; gap: 6px; }
.skill-tag { background: rgba(255,255,255,0.15); padding: 3px 10px; border-radius: 12px; font-size: 12px; }

.main h1 { font-size: 28px; color: #1a202c; margin-bottom: 4px; }
.main .position { color: #4a5568; font-size: 16px; margin-bottom: 24px; }

.section { margin-bottom: 20px; }
.section h3 { font-size: 16px; color: #1a365d; border-bottom: 2px solid #1a365d; padding-bottom: 4px; margin-bottom: 12px; }

.item { margin-bottom: 12px; }
.item-header { display: flex; justify-content: space-between; font-size: 14px; }
.item-sub { font-size: 13px; color: #4a5568; }
.item p { font-size: 13px; color: #4a5568; margin-top: 4px; line-height: 1.6; }

@media print {
  .preview-toolbar { display: none; }
  .resume-paper { box-shadow: none; }
}
</style>

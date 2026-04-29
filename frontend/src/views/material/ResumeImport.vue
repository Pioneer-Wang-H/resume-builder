<template>
  <div class="page">
    <h2>文件导入</h2>
    <p class="desc">上传 PDF 或 Word 简历文件，系统将自动解析并填充到素材池。</p>

    <el-card style="margin-top: 16px">
      <el-upload
        ref="uploadRef"
        :auto-upload="false"
        :limit="1"
        accept=".pdf,.docx,.doc"
        :on-change="handleFileChange"
        drag
      >
        <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip">支持 PDF、DOCX、DOC 格式</div>
        </template>
      </el-upload>

      <el-button
        type="primary"
        :loading="parsing"
        :disabled="!selectedFile"
        @click="handleParse"
        style="margin-top: 16px"
      >
        开始解析
      </el-button>
    </el-card>

    <!-- Preview -->
    <el-card v-if="parsedData" style="margin-top: 20px">
      <template #header>解析结果预览</template>

      <div class="preview-section">
        <h4>基本信息</h4>
        <el-descriptions :column="3" border size="small">
          <el-descriptions-item label="姓名">{{ parsedData.name || '-' }}</el-descriptions-item>
          <el-descriptions-item label="手机">{{ parsedData.phone || '-' }}</el-descriptions-item>
          <el-descriptions-item label="邮箱">{{ parsedData.email || '-' }}</el-descriptions-item>
          <el-descriptions-item label="城市">{{ parsedData.city || '-' }}</el-descriptions-item>
          <el-descriptions-item label="意向岗位">{{ parsedData.intendedPosition || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>

      <div class="preview-section" v-if="parsedData.educationList?.length">
        <h4>教育经历 ({{ parsedData.educationList.length }} 条)</h4>
        <el-table :data="parsedData.educationList" border size="small">
          <el-table-column prop="school" label="学校" />
          <el-table-column prop="major" label="专业" />
          <el-table-column prop="degree" label="学历" />
          <el-table-column prop="startDate" label="开始" width="90" />
          <el-table-column prop="endDate" label="结束" width="90" />
        </el-table>
      </div>

      <div class="preview-section" v-if="parsedData.workExperienceList?.length">
        <h4>工作经历 ({{ parsedData.workExperienceList.length }} 条)</h4>
        <el-table :data="parsedData.workExperienceList" border size="small">
          <el-table-column prop="company" label="公司" />
          <el-table-column prop="position" label="职位" />
          <el-table-column prop="startDate" label="开始" width="90" />
          <el-table-column prop="endDate" label="结束" width="90" />
        </el-table>
      </div>

      <div class="preview-section" v-if="parsedData.skillNames?.length">
        <h4>技能标签</h4>
        <el-tag v-for="(skill, i) in parsedData.skillNames" :key="i" style="margin: 0 8px 8px 0">{{ skill }}</el-tag>
      </div>

      <div class="preview-section" v-if="parsedData.certificateList?.length">
        <h4>证书奖项</h4>
        <el-table :data="parsedData.certificateList" border size="small">
          <el-table-column prop="name" label="名称" />
          <el-table-column prop="issuingAuthority" label="颁发机构" />
          <el-table-column prop="obtainDate" label="获得日期" width="120" />
        </el-table>
      </div>

      <div class="preview-section" v-if="parsedData.selfEvaluationContent">
        <h4>自我评价</h4>
        <p class="eval-text">{{ parsedData.selfEvaluationContent }}</p>
      </div>

      <el-button
        type="primary"
        :loading="importing"
        @click="handleImport"
        style="margin-top: 16px"
      >
        确认导入到素材池
      </el-button>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { uploadResumeFile, importResumeData } from '@/api/fileUpload'
import { ElMessage } from 'element-plus'
import { UploadFilled } from '@element-plus/icons-vue'

const selectedFile = ref<File | null>(null)
const parsing = ref(false)
const importing = ref(false)
const parsedData = ref<any>(null)

function handleFileChange(file: any) {
  selectedFile.value = file.raw
}

async function handleParse() {
  if (!selectedFile.value) return
  parsing.value = true
  try {
    const res: any = await uploadResumeFile(selectedFile.value)
    parsedData.value = res.data
    ElMessage.success('解析完成，请核对结果后确认导入')
  } catch {
    ElMessage.error('解析失败，请检查文件格式')
  } finally {
    parsing.value = false
  }
}

async function handleImport() {
  importing.value = true
  try {
    await importResumeData(parsedData.value)
    ElMessage.success('导入成功')
  } catch {
    ElMessage.error('导入失败')
  } finally {
    importing.value = false
  }
}
</script>

<style scoped>
.page { padding: 0; }
.desc { color: #909399; margin-top: 8px; }
.preview-section { margin-bottom: 16px; }
.preview-section h4 { margin-bottom: 8px; color: #303133; }
.eval-text { white-space: pre-wrap; color: #606266; font-size: 14px; line-height: 1.6; padding: 8px; background: #f5f7fa; border-radius: 4px; }
</style>

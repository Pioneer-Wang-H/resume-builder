<template>
  <div class="page">
    <div class="page-header">
      <h2>我的简历</h2>
      <el-button type="primary" @click="showCreate = true">新建简历</el-button>
    </div>

    <el-row :gutter="20">
      <el-col :span="8" v-for="r in list" :key="r.id">
        <el-card shadow="hover" class="resume-card">
          <div class="resume-title">{{ r.title }}</div>
          <div class="resume-meta">
            <el-tag v-if="r.isDefault" type="success" size="small">默认</el-tag>
            创建于 {{ r.createTime?.substring(0, 10) }}
          </div>
          <div class="resume-actions">
            <el-button size="small" type="primary" @click="$router.push(`/resume/${r.id}/assemble`)">组装</el-button>
            <el-button size="small" @click="$router.push(`/resume/${r.id}/preview`)">预览</el-button>
            <el-dropdown @command="(cmd: string) => handleCommand(cmd, r)">
              <el-button size="small">更多</el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="default">设为默认</el-dropdown-item>
                  <el-dropdown-item command="delete" style="color: #f56c6c">删除</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog v-model="showCreate" title="新建简历" width="400px">
      <el-form :model="createForm" label-width="80px">
        <el-form-item label="简历标题"><el-input v-model="createForm.title" placeholder="如：Java后端岗" /></el-form-item>
        <el-form-item label="模板">
          <el-select v-model="createForm.templateId">
            <el-option v-for="t in templates" :key="t.id" :label="t.name" :value="t.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreate = false">取消</el-button>
        <el-button type="primary" @click="handleCreate">创建</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getResumes, createResume, deleteResume, setDefaultResume, getTemplates } from '@/api/resume'
import { ElMessage, ElMessageBox } from 'element-plus'

const list = ref<any[]>([])
const templates = ref<any[]>([])
const showCreate = ref(false)
const createForm = reactive({ title: '', templateId: 1 })

onMounted(async () => {
  const [r, t]: any[] = await Promise.all([getResumes(), getTemplates()])
  list.value = r.data || []
  templates.value = t.data || []
})

async function handleCreate() {
  if (!createForm.title) return
  await createResume(createForm)
  ElMessage.success('创建成功')
  showCreate.value = false
  const r: any = await getResumes()
  list.value = r.data || []
}

async function handleCommand(cmd: string, row: any) {
  if (cmd === 'default') {
    await setDefaultResume(row.id)
    ElMessage.success('已设为默认')
    const r: any = await getResumes()
    list.value = r.data || []
  } else if (cmd === 'delete') {
    await ElMessageBox.confirm('确定删除？', '提示', { type: 'warning' })
    await deleteResume(row.id)
    ElMessage.success('已删除')
    const r: any = await getResumes()
    list.value = r.data || []
  }
}
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.resume-card { margin-bottom: 16px; }
.resume-title { font-size: 18px; font-weight: 600; margin-bottom: 8px; }
.resume-meta { font-size: 13px; color: #999; margin-bottom: 12px; display: flex; align-items: center; gap: 8px; }
.resume-actions { display: flex; gap: 8px; }
</style>

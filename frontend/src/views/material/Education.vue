<template>
  <div class="page">
    <h2>教育经历 <el-button type="primary" size="small" @click="openDialog()">添加</el-button></h2>
    <el-table :data="list" stripe>
      <el-table-column prop="school" label="学校" />
      <el-table-column prop="major" label="专业" />
      <el-table-column prop="degree" label="学位" width="100" />
      <el-table-column prop="gpa" label="GPA" width="80" />
      <el-table-column label="时间" width="200">
        <template #default="{ row }">{{ row.startDate }} ~ {{ row.endDate }}</template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button size="small" @click="openDialog(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="visible" :title="isEdit ? '编辑' : '添加'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="学校"><el-input v-model="form.school" /></el-form-item>
        <el-form-item label="专业"><el-input v-model="form.major" /></el-form-item>
        <el-form-item label="学位">
          <el-select v-model="form.degree">
            <el-option label="本科" value="本科" />
            <el-option label="硕士" value="硕士" />
            <el-option label="博士" value="博士" />
            <el-option label="大专" value="大专" />
          </el-select>
        </el-form-item>
        <el-form-item label="GPA"><el-input v-model="form.gpa" /></el-form-item>
        <el-form-item label="开始时间"><el-date-picker v-model="form.startDate" type="month" value-format="YYYY-MM-DD" /></el-form-item>
        <el-form-item label="结束时间"><el-date-picker v-model="form.endDate" type="month" value-format="YYYY-MM-DD" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getEducations, saveEducation, updateEducation, deleteEducation } from '@/api/material'
import { ElMessage, ElMessageBox } from 'element-plus'

const list = ref([])
const visible = ref(false)
const isEdit = ref(false)
const form = reactive<any>({ school: '', major: '', degree: '本科', gpa: '', startDate: '', endDate: '', description: '' })
let editId: number | null = null

onMounted(async () => { await load() })
async function load() { const res: any = await getEducations(); list.value = res.data || [] }

function openDialog(row?: any) {
  if (row) { isEdit.value = true; editId = row.id; Object.assign(form, row) }
  else { isEdit.value = false; editId = null; Object.keys(form).forEach(k => (form as any)[k] = k === 'degree' ? '本科' : '') }
  visible.value = true
}

async function handleSave() {
  try {
    if (isEdit.value && editId) await updateEducation(editId, { ...form })
    else await saveEducation({ ...form })
    ElMessage.success('保存成功')
    visible.value = false
    await load()
  } catch {}
}

async function handleDelete(id: number) {
  await ElMessageBox.confirm('确定删除？', '提示', { type: 'warning' })
  await deleteEducation(id)
  ElMessage.success('已删除')
  await load()
}
</script>

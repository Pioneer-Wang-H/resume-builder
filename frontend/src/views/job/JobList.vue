<template>
  <div class="page">
    <h2>投递记录 <el-button type="primary" size="small" @click="openDialog()">添加</el-button></h2>
    <el-table :data="list" stripe>
      <el-table-column prop="company" label="公司" />
      <el-table-column prop="position" label="岗位" width="150" />
      <el-table-column prop="salaryRange" label="薪资范围" width="120" />
      <el-table-column prop="location" label="地点" width="100" />
      <el-table-column prop="channel" label="渠道" width="100" />
      <el-table-column prop="applyDate" label="投递日期" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)">{{ row.status }}</el-tag>
        </template>
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
        <el-form-item label="公司"><el-input v-model="form.company" /></el-form-item>
        <el-form-item label="岗位"><el-input v-model="form.position" /></el-form-item>
        <el-form-item label="薪资"><el-input v-model="form.salaryRange" placeholder="如：15K-25K" /></el-form-item>
        <el-form-item label="地点"><el-input v-model="form.location" /></el-form-item>
        <el-form-item label="渠道">
          <el-select v-model="form.channel">
            <el-option label="BOSS直聘" value="BOSS直聘" />
            <el-option label="拉勾" value="拉勾" />
            <el-option label="猎聘" value="猎聘" />
            <el-option label="官网" value="官网" />
            <el-option label="内推" value="内推" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="已投递" value="已投递" />
            <el-option label="初筛通过" value="初筛通过" />
            <el-option label="面试" value="面试" />
            <el-option label="已拿offer" value="已拿offer" />
            <el-option label="已拒绝" value="已拒绝" />
          </el-select>
        </el-form-item>
        <el-form-item label="投递日期"><el-date-picker v-model="form.applyDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.notes" type="textarea" /></el-form-item>
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
import { getApplications, saveApplication, updateApplication, deleteApplication } from '@/api/job'
import { ElMessage, ElMessageBox } from 'element-plus'

const list = ref([])
const visible = ref(false)
const isEdit = ref(false)
const form = reactive<any>({ company: '', position: '', salaryRange: '', location: '', channel: '', status: '已投递', applyDate: '', notes: '' })
let editId: number | null = null

onMounted(async () => { await load() })
async function load() { const res: any = await getApplications(); list.value = res.data || [] }

function statusType(s: string) {
  const map: Record<string, string> = { '已投递': 'info', '初筛通过': 'warning', '面试': '', '已拿offer': 'success', '已拒绝': 'danger' }
  return map[s] || 'info'
}

function openDialog(row?: any) {
  if (row) { isEdit.value = true; editId = row.id; Object.assign(form, row) }
  else { isEdit.value = false; editId = null; Object.keys(form).forEach(k => (form as any)[k] = k === 'status' ? '已投递' : '') }
  visible.value = true
}

async function handleSave() {
  try {
    if (isEdit.value && editId) await updateApplication(editId, { ...form })
    else await saveApplication({ ...form })
    ElMessage.success('保存成功')
    visible.value = false
    await load()
  } catch {}
}

async function handleDelete(id: number) {
  await ElMessageBox.confirm('确定删除？', '提示', { type: 'warning' })
  await deleteApplication(id)
  ElMessage.success('已删除')
  await load()
}
</script>

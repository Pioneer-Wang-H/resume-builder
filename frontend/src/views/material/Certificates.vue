<template>
  <div class="page">
    <h2>证书奖项 <el-button type="primary" size="small" @click="openDialog()">添加</el-button></h2>
    <el-table :data="list" stripe>
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="issuingAuthority" label="颁发机构" width="200" />
      <el-table-column prop="obtainDate" label="获得时间" width="130" />
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button size="small" @click="openDialog(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="visible" :title="isEdit ? '编辑' : '添加'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="颁发机构"><el-input v-model="form.issuingAuthority" /></el-form-item>
        <el-form-item label="获得时间"><el-date-picker v-model="form.obtainDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" /></el-form-item>
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
import { getCertificates, saveCertificate, updateCertificate, deleteCertificate } from '@/api/material'
import { ElMessage, ElMessageBox } from 'element-plus'

const list = ref([])
const visible = ref(false)
const isEdit = ref(false)
const form = reactive<any>({ name: '', issuingAuthority: '', obtainDate: '', description: '' })
let editId: number | null = null

onMounted(async () => { await load() })
async function load() { const res: any = await getCertificates(); list.value = res.data || [] }

function openDialog(row?: any) {
  if (row) { isEdit.value = true; editId = row.id; Object.assign(form, row) }
  else { isEdit.value = false; editId = null; Object.keys(form).forEach(k => (form as any)[k] = '') }
  visible.value = true
}

async function handleSave() {
  try {
    if (isEdit.value && editId) await updateCertificate(editId, { ...form })
    else await saveCertificate({ ...form })
    ElMessage.success('保存成功')
    visible.value = false
    await load()
  } catch {}
}

async function handleDelete(id: number) {
  await ElMessageBox.confirm('确定删除？', '提示', { type: 'warning' })
  await deleteCertificate(id)
  ElMessage.success('已删除')
  await load()
}
</script>

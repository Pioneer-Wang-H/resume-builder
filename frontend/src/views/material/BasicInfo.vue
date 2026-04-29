<template>
  <div class="page">
    <h2>基本信息</h2>
    <el-card>
      <el-form :model="form" label-width="100px" style="max-width: 600px">
        <el-form-item label="姓名"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="手机号码"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item>
        <el-form-item label="意向岗位"><el-input v-model="form.intendedPosition" /></el-form-item>
        <el-form-item label="所在城市"><el-input v-model="form.city" /></el-form-item>
        <el-form-item label="个人网站"><el-input v-model="form.website" /></el-form-item>
        <el-form-item label="GitHub"><el-input v-model="form.github" /></el-form-item>
        <el-form-item label="LinkedIn"><el-input v-model="form.linkedin" /></el-form-item>
        <el-form-item label="出生日期">
          <el-date-picker v-model="form.birthDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSave" :loading="loading">保存</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, onMounted, ref } from 'vue'
import { getBasicInfo, saveBasicInfo } from '@/api/material'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const form = reactive<any>({
  name: '', phone: '', email: '', intendedPosition: '',
  city: '', website: '', github: '', linkedin: '', birthDate: null,
})

onMounted(async () => {
  try {
    const res: any = await getBasicInfo()
    if (res.data) Object.assign(form, res.data)
  } catch {}
})

async function handleSave() {
  loading.value = true
  try {
    await saveBasicInfo({ ...form })
    ElMessage.success('保存成功')
  } finally { loading.value = false }
}
</script>

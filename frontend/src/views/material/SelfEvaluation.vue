<template>
  <div class="page">
    <h2>自我评价</h2>
    <el-card>
      <el-input v-model="content" type="textarea" :rows="8" placeholder="介绍自己的优势、特长、职业规划..." />
      <el-button type="primary" @click="handleSave" :loading="loading" style="margin-top: 16px">保存</el-button>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getSelfEvaluation, saveSelfEvaluation } from '@/api/material'
import { ElMessage } from 'element-plus'

const content = ref('')
const loading = ref(false)

onMounted(async () => {
  try {
    const res: any = await getSelfEvaluation()
    content.value = res.data?.content || ''
  } catch {}
})

async function handleSave() {
  loading.value = true
  try {
    await saveSelfEvaluation({ content: content.value })
    ElMessage.success('保存成功')
  } finally { loading.value = false }
}
</script>

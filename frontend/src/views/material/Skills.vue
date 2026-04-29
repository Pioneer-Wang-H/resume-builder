<template>
  <div class="page">
    <h2>技能标签</h2>
    <el-card>
      <div style="margin-bottom: 16px">
        <el-input v-model="newSkill" placeholder="输入技能名称，回车添加" @keyup.enter="addSkill" style="width: 300px; margin-right: 8px" />
        <el-select v-model="newCategory" placeholder="分类" style="width: 150px; margin-right: 8px">
          <el-option label="编程语言" value="编程语言" />
          <el-option label="框架" value="框架" />
          <el-option label="数据库" value="数据库" />
          <el-option label="工具" value="工具" />
          <el-option label="其他" value="其他" />
        </el-select>
        <el-rate v-model="newProficiency" style="display: inline-flex; vertical-align: middle" />
      </div>
      <el-tag
        v-for="skill in list" :key="skill.id"
        closable
        :disable-transitions="false"
        @close="handleDelete(skill.id)"
        style="margin: 0 8px 8px 0; padding: 8px 12px"
        size="large"
      >
        {{ skill.skillName }}
        <el-rate :model-value="skill.proficiency" disabled size="small" style="display: inline-flex; margin-left: 8px" />
      </el-tag>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getSkills, saveSkill, deleteSkill } from '@/api/material'
import { ElMessage } from 'element-plus'

const list = ref<any[]>([])
const newSkill = ref('')
const newCategory = ref('编程语言')
const newProficiency = ref(3)

onMounted(async () => {
  const res: any = await getSkills()
  list.value = res.data || []
})

async function addSkill() {
  if (!newSkill.value.trim()) return
  await saveSkill({ skillName: newSkill.value.trim(), category: newCategory.value, proficiency: newProficiency.value })
  newSkill.value = ''
  const res: any = await getSkills()
  list.value = res.data || []
}

async function handleDelete(id: number) {
  await deleteSkill(id)
  ElMessage.success('已删除')
  const res: any = await getSkills()
  list.value = res.data || []
}
</script>

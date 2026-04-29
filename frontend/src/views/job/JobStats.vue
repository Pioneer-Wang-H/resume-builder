<template>
  <div class="page">
    <h2>投递统计</h2>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover"><template #header>总投递</template><div class="stat">{{ stats.total }}</div></el-card>
      </el-col>
      <el-col :span="6" v-for="(v, k) in stats.byStatus" :key="k">
        <el-card shadow="hover"><template #header>{{ k }}</template><div class="stat">{{ v }}</div></el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card header="渠道分布">
          <div v-for="(v, k) in stats.byChannel" :key="k" style="margin-bottom: 12px">
            <span style="display: inline-block; width: 100px">{{ k }}</span>
            <el-progress :percentage="stats.total ? Math.round((v as number) / stats.total * 100) : 0" />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { reactive, onMounted } from 'vue'
import { getApplicationStats } from '@/api/job'

const stats = reactive<{ total: number; byStatus: Record<string, number>; byChannel: Record<string, number> }>({
  total: 0, byStatus: {}, byChannel: {},
})

onMounted(async () => {
  try {
    const res: any = await getApplicationStats()
    Object.assign(stats, res.data)
  } catch {}
})
</script>

<style scoped>
.stat { font-size: 36px; font-weight: 700; color: #409EFF; }
</style>

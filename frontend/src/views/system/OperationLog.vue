<template>
  <div class="page">
    <h2>操作日志</h2>

    <el-form :inline="true" style="margin-bottom: 16px">
      <el-form-item label="操作模块">
        <el-select v-model="filterModule" placeholder="全部" clearable @change="loadData">
          <el-option label="素材管理" value="素材管理" />
          <el-option label="简历管理" value="简历管理" />
          <el-option label="投递管理" value="投递管理" />
        </el-select>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="userId" label="用户ID" width="80" />
      <el-table-column prop="module" label="操作模块" width="100" />
      <el-table-column prop="operation" label="操作" width="160" />
      <el-table-column prop="params" label="请求参数" min-width="200">
        <template #default="{ row }">
          <el-tooltip :content="row.params" placement="top" :disabled="!row.params">
            <span class="params-text">{{ row.params || '-' }}</span>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column prop="ip" label="IP" width="140" />
      <el-table-column prop="createTime" label="操作时间" width="170" />
    </el-table>

    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :total="total"
      layout="total, prev, pager, next"
      style="margin-top: 16px; justify-content: flex-end"
      @current-change="loadData"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getOperationLogs } from '@/api/operationLog'

const tableData = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)
const filterModule = ref('')

async function loadData() {
  loading.value = true
  try {
    const res: any = await getOperationLogs({
      page: currentPage.value,
      pageSize: pageSize.value,
      module: filterModule.value || undefined,
    })
    tableData.value = res.data?.records || []
    total.value = res.data?.total || 0
  } finally {
    loading.value = false
  }
}

onMounted(loadData)
</script>

<style scoped>
.page { padding: 0; }
.params-text {
  display: inline-block;
  max-width: 300px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: 12px;
  color: #909399;
}
</style>

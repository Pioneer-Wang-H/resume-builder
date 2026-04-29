<template>
  <el-container class="layout">
    <el-aside width="220px">
      <div class="logo">📄 简历工具</div>
      <el-menu
        :default-active="activeMenu"
        :default-openeds="['material', 'resume', 'job']"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/dashboard">
          <el-icon><HomeFilled /></el-icon> 工作台
        </el-menu-item>

        <el-sub-menu index="material">
          <template #title>
            <el-icon><Collection /></el-icon> 素材仓库
          </template>
          <el-menu-item index="/material/basic">基本信息</el-menu-item>
          <el-menu-item index="/material/education">教育经历</el-menu-item>
          <el-menu-item index="/material/project">项目经历</el-menu-item>
          <el-menu-item index="/material/work">工作经历</el-menu-item>
          <el-menu-item index="/material/skills">技能标签</el-menu-item>
          <el-menu-item index="/material/certificates">证书奖项</el-menu-item>
          <el-menu-item index="/material/evaluation">自我评价</el-menu-item>
          <el-menu-item index="/material/import">文件导入</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="resume">
          <template #title>
            <el-icon><Document /></el-icon> 简历管理
          </template>
          <el-menu-item index="/resume/list">我的简历</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="job">
          <template #title>
            <el-icon><Briefcase /></el-icon> 投递管理
          </template>
          <el-menu-item index="/job/list">投递记录</el-menu-item>
          <el-menu-item index="/job/stats">投递统计</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="system">
          <template #title>
            <el-icon><Setting /></el-icon> 系统管理
          </template>
          <el-menu-item index="/system/log">操作日志</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header>
        <span class="breadcrumb">{{ pageTitle }}</span>
        <el-dropdown @command="handleCommand">
          <span class="user-avatar">
            <el-avatar size="small" /> 用户
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from '@/store/auth'
import { HomeFilled, Collection, Document, Briefcase, Setting } from '@element-plus/icons-vue'

const route = useRoute()
const auth = useAuthStore()

const activeMenu = computed(() => route.path)
const pageTitle = computed(() => route.meta?.title || '')

function handleCommand(cmd: string) {
  if (cmd === 'logout') auth.logout()
}
</script>

<style scoped>
.layout {
  min-height: 100vh;
}

.el-aside {
  background: #304156;
  overflow-x: hidden;
}

.logo {
  color: #fff;
  font-size: 18px;
  font-weight: 600;
  text-align: center;
  padding: 20px 0;
  border-bottom: 1px solid rgba(255,255,255,0.1);
}

.el-header {
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid #e6e6e6;
  padding: 0 24px;
}

.breadcrumb {
  font-size: 16px;
  font-weight: 500;
}

.user-avatar {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.el-main {
  background: #f5f7fa;
  padding: 24px;
}
</style>

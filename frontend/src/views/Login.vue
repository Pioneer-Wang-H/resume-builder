<template>
  <div class="login-container">
    <div class="login-card">
      <h1>📄 简历生成器</h1>
      <p class="subtitle">管理多份简历，高效投递</p>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="0">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名" size="large" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" size="large" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" :loading="loading" @click="handleLogin" style="width: 100%">
            登录
          </el-button>
        </el-form-item>
      </el-form>
      <p class="switch">
        没有账号？<el-link type="primary" @click="showRegister = true">立即注册</el-link>
      </p>
    </div>

    <el-dialog v-model="showRegister" title="注册" width="400px">
      <el-form :model="regForm" :rules="regRules" ref="regFormRef">
        <el-form-item prop="username">
          <el-input v-model="regForm.username" placeholder="用户名" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="regForm.password" type="password" placeholder="密码" show-password />
        </el-form-item>
        <el-form-item prop="confirm">
          <el-input v-model="regForm.confirm" type="password" placeholder="确认密码" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="regLoading" @click="handleRegister" style="width: 100%">
            注册
          </el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/store/auth'
import { register } from '@/api/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const auth = useAuthStore()

const loading = ref(false)
const formRef = ref()
const form = reactive({ username: 'test', password: '123456' })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
}

async function handleLogin() {
  await formRef.value.validate()
  loading.value = true
  try {
    await auth.login(form.username, form.password)
    router.push('/')
  } finally {
    loading.value = false
  }
}

const showRegister = ref(false)
const regLoading = ref(false)
const regFormRef = ref()
const regForm = reactive({ username: '', password: '', confirm: '' })
const regRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }, { min: 3, max: 20, message: '3-20位', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }, { min: 6, message: '至少6位', trigger: 'blur' }],
  confirm: [{ required: true, message: '请确认密码', trigger: 'blur' },
    { validator: (_: any, v: string, cb: any) => cb(v !== regForm.password ? new Error('两次密码不一致') : undefined), trigger: 'blur' }],
}

async function handleRegister() {
  await regFormRef.value.validate()
  regLoading.value = true
  try {
    await register({ username: regForm.username, password: regForm.password, nickname: regForm.username })
    ElMessage.success('注册成功，请登录')
    showRegister.value = false
  } finally {
    regLoading.value = false
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  background: #fff;
  padding: 48px 40px;
  border-radius: 8px;
  width: 400px;
  box-shadow: 0 8px 32px rgba(0,0,0,0.15);
}

h1 { text-align: center; font-size: 28px; margin-bottom: 8px; }
.subtitle { text-align: center; color: #999; margin-bottom: 32px; }
.switch { text-align: center; color: #999; font-size: 14px; }
</style>

<template>
  <AuthShell>
    <h2 class="auth-title">管理员登录</h2>
    <p class="auth-desc">使用管理账户进入后台。</p>

    <div v-if="error" class="auth-notice" role="alert" tabindex="-1" ref="errorRef">{{ error }}</div>

    <form class="auth-form" @submit.prevent="handleLogin">
      <div class="auth-field">
        <label class="auth-label" for="admin-account">账号 <span class="req">*</span></label>
        <input
          id="admin-account"
          name="name"
          class="auth-input"
          type="text"
          v-model="form.name"
          required
          minlength="5"
          maxlength="30"
          autocomplete="username"
          placeholder="请输入管理员账号"
        >
      </div>

      <div class="auth-field">
        <label class="auth-label" for="admin-password">密码 <span class="req">*</span></label>
        <div class="auth-input-wrap">
          <input
            id="admin-password"
            name="password"
            class="auth-input auth-input--password"
            :type="showPassword ? 'text' : 'password'"
            v-model="form.password"
            required
            minlength="5"
            maxlength="30"
            autocomplete="current-password"
            placeholder="请输入密码"
          >
          <button type="button" class="auth-eye" :aria-label="showPassword ? '隐藏密码' : '显示密码'" @click="showPassword = !showPassword">
            <svg v-if="showPassword" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round">
              <path d="M3 3l18 18" />
              <path d="M10.6 10.7A3 3 0 0 0 13.3 13.4" />
              <path d="M9.9 5.2A9.8 9.8 0 0 1 12 5c5.5 0 9 7 9 7a15.7 15.7 0 0 1-2.3 3.2M6.6 6.7A15.9 15.9 0 0 0 3 12s3.5 7 9 7a9.6 9.6 0 0 0 4.2-.9" />
            </svg>
            <svg v-else viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round">
              <path d="M3 12s3.5-7 9-7 9 7 9 7-3.5 7-9 7-9-7-9-7z" />
              <circle cx="12" cy="12" r="3" />
            </svg>
          </button>
        </div>
      </div>

      <button type="submit" class="auth-submit" :disabled="submitting">
        {{ submitting ? '登录中...' : '登录管理端' }}
      </button>
    </form>

    <p class="auth-switch">学生用户？<router-link to="/login">前往学生登录</router-link></p>
  </AuthShell>
</template>

<script setup>
import { ref, reactive, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { api } from '../../api/request'
import { useUserStore } from '../../stores/user'
import AuthShell from '../../components/AuthShell.vue'

const router = useRouter()
const userStore = useUserStore()

const form = reactive({ name: '', password: '' })
const error = ref('')
const submitting = ref(false)
const showPassword = ref(false)
const errorRef = ref(null)

async function handleLogin() {
  error.value = ''
  submitting.value = true
  try {
    const r = await api('/admin/login', form)
    userStore.login('admin', r)
    router.push('/admin')
  } catch (e) {
    error.value = e.message
    await nextTick()
    errorRef.value?.focus()
  } finally {
    submitting.value = false
  }
}
</script>
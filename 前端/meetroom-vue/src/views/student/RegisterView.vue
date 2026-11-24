<template>
  <AuthShell>
    <h2 class="auth-title">创建校园账户</h2>
    <p class="auth-desc">完善基本信息，即可使用预约服务。</p>

    <div v-if="error" class="auth-notice" role="alert" tabindex="-1" ref="errorRef">{{ error }}</div>

    <form class="auth-form" @submit.prevent="handleRegister">
      <div class="auth-grid">
        <div class="auth-field">
          <label class="auth-label" for="account">用户名 <span class="req">*</span></label>
          <input
            id="account"
            name="account"
            class="auth-input"
            type="text"
            v-model="form.account"
            required
            minlength="3"
            maxlength="30"
            autocomplete="username"
            placeholder="3-30 位字符"
          >
        </div>
        <div class="auth-field">
          <label class="auth-label" for="name">昵称 <span class="req">*</span></label>
          <input
            id="name"
            name="name"
            class="auth-input"
            type="text"
            v-model="form.name"
            required
            maxlength="30"
            autocomplete="nickname"
            placeholder="如何称呼你"
          >
        </div>

        <div class="auth-field auth-field--full">
          <label class="auth-label" for="password">设置密码 <span class="req">*</span></label>
          <div class="auth-input-wrap">
            <input
              id="password"
              name="password"
              class="auth-input auth-input--password"
              :type="showPassword ? 'text' : 'password'"
              v-model="form.password"
              required
              minlength="6"
              maxlength="30"
              autocomplete="new-password"
              placeholder="6-30 位字符"
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

        <div
          v-for="f in userFields"
          :key="f[0]"
          class="auth-field"
          :class="{ 'auth-field--full': f[0] === 'no' }"
        >
          <label class="auth-label" :for="'f-' + f[0]">{{ f[1] }} <span class="req">*</span></label>
          <select v-if="f[2] === 'select'" :id="'f-' + f[0]" class="auth-select" v-model="form[f[0]]" required>
            <option value="" disabled>请选择</option>
            <option v-for="opt in f[3]" :key="opt" :value="opt">{{ opt }}</option>
          </select>
          <input
            v-else
            :id="'f-' + f[0]"
            class="auth-input"
            :type="f[2]"
            v-model="form[f[0]]"
            required
            :min="f[2] === 'number' ? 1900 : undefined"
            :max="f[2] === 'number' ? 2100 : undefined"
            :minlength="['college', 'major', 'item'].includes(f[0]) ? 2 : undefined"
            :maxlength="f[2] === 'number' ? undefined : 200"
            :placeholder="f[1]"
          >
        </div>
      </div>

      <label class="auth-check auth-agree">
        <input type="checkbox" v-model="agreed" required>
        <span>我已阅读并同意 <router-link to="/agreement" target="_blank">用户使用协议</router-link></span>
      </label>

      <button type="submit" class="auth-submit" :disabled="submitting">
        {{ submitting ? '注册中...' : '注册并登录' }}
      </button>
    </form>

    <p class="auth-switch">已有账户？<router-link to="/login">去登录</router-link></p>
  </AuthShell>
</template>

<script setup>
import { ref, reactive, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { api } from '../../api/request'
import { useUserStore } from '../../stores/user'
import { userFields, buildPayload } from '../../composables/useHelpers'
import AuthShell from '../../components/AuthShell.vue'

const router = useRouter()
const userStore = useUserStore()

const form = reactive({
  account: '', name: '', password: '',
  sex: '', birth: '', college: '', major: '', item: '', year: '', no: ''
})
const agreed = ref(false)
const error = ref('')
const submitting = ref(false)
const showPassword = ref(false)
const errorRef = ref(null)

async function handleRegister() {
  error.value = ''
  submitting.value = true
  try {
    const payloadData = buildPayload(userFields, form)
    const result = await api('/passport/register', {
      account: form.account,
      name: form.name,
      password: form.password,
      ...payloadData
    })
    userStore.login('cust', result.token)
    router.push('/profile')
  } catch (e) {
    error.value = e.message
    await nextTick()
    errorRef.value?.focus()
  } finally {
    submitting.value = false
  }
}
</script>

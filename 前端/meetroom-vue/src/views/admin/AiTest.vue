<template>
  <div class="aitest">
    <div class="eyebrow">AI QUALITY / TURBOROOM</div>
    <h1>AI 测试中心</h1>
    <p class="subtitle">用 AI 把需求自动转化为接口测试用例，并一键回归执行，保障预约系统接口质量。</p>

    <div class="panel">
      <div class="panel-head">
        <h2>第一步 · 用 AI 生成测试用例</h2>
        <span class="hint">描述你要测试的需求或接口，AI 会输出结构化用例</span>
      </div>
      <div v-if="error" class="notice error" role="alert">{{ error }}</div>
      <div class="field">
        <label>需求 / 接口描述 *</label>
        <textarea v-model="requirement" rows="4" placeholder="例如：验证用户登录接口，正常账号密码可以登录，密码错误要返回 1600，缺少 token 访问个人信息要返回 2401..."></textarea>
      </div>
      <div class="row">
        <button class="btn primary" type="button" :disabled="generating" @click="handleGenerate">
          <span v-if="generating" class="spin" aria-hidden="true"></span>
          {{ generating ? 'AI 生成中，请稍候…' : 'AI 生成用例' }}
        </button>
        <button class="btn ghost" type="button" :disabled="generating" @click="loadRegression">加载内置回归套件</button>
        <button class="btn ghost" type="button" @click="toggleAll">
          {{ allSelected ? '取消全选' : '全选用例' }}
        </button>
      </div>
    </div>

    <div v-if="cases.length" class="panel">
      <div class="panel-head">
        <h2>第二步 · 勾选并执行用例</h2>
        <span class="hint">共 {{ cases.length }} 条，已选 {{ selectedCount }} 条</span>
      </div>
      <div class="case-list">
        <label v-for="(item, index) in cases" :key="index" class="case-item" :class="{ checked: item.selected }">
          <input v-model="item.selected" type="checkbox">
          <div class="case-meta">
            <strong>{{ item.name }}</strong>
            <code>{{ item.method || 'POST' }} {{ item.path }}</code>
            <small>期望：{{ item.expect }}</small>
          </div>
        </label>
      </div>
      <div class="row">
        <button class="btn primary" type="button" :disabled="running || selectedCount === 0" @click="runSelected">
          <span v-if="running" class="spin" aria-hidden="true"></span>
          {{ running ? '执行中…' : `执行选中用例（${selectedCount}）` }}
        </button>
        <button class="btn ghost" type="button" :disabled="running" @click="runRegression">一键执行内置回归</button>
      </div>
    </div>

    <div v-if="results.length" class="panel">
      <div class="panel-head">
        <h2>执行结果</h2>
        <div class="summary">
          <span class="chip ok">通过 {{ summary.pass }}</span>
          <span class="chip bad">失败 {{ summary.fail }}</span>
          <span class="chip">总计 {{ summary.total }}</span>
        </div>
      </div>
      <table class="result-table">
        <thead>
          <tr>
            <th>用例</th>
            <th>请求</th>
            <th>期望</th>
            <th>实际</th>
            <th>耗时</th>
            <th>结果</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(row, index) in results" :key="index">
            <td class="td-name">{{ row.name }}</td>
            <td><code>{{ row.method }} {{ row.path }}</code></td>
            <td class="td-expect">{{ row.expect }}</td>
            <td>HTTP {{ row.status }} · code {{ row.code }}</td>
            <td>{{ row.duration }} ms</td>
            <td>
              <span class="badge" :class="row.pass ? 'pass' : 'fail'">{{ row.pass ? '通过' : '失败' }}</span>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-for="(row, index) in results.filter(r => !r.pass)" :key="'m' + index" class="notice error">
        [{{ row.name }}] {{ row.msg }}
      </div>
    </div>

    <div v-if="!cases.length && !results.length" class="panel empty">
      <p>还没有测试用例。输入需求让 AI 生成，或加载内置回归套件后执行。</p>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { api } from '../../api/request'
import { useToast } from '../../composables/useToast'

const { toast } = useToast()

const requirement = ref('')
const cases = ref([])
const results = ref([])
const summary = ref({ total: 0, pass: 0, fail: 0 })
const generating = ref(false)
const running = ref(false)
const error = ref('')

const selectedCount = computed(() => cases.value.filter(c => c.selected).length)
const allSelected = computed(() => cases.value.length > 0 && cases.value.every(c => c.selected))

function decorateCases(list) {
  return (list || []).map(c => ({
    name: c.name || '未命名用例',
    method: (c.method || 'POST').toUpperCase(),
    path: c.path || '',
    body: c.body || {},
    expect: c.expect || '',
    selected: true
  }))
}

async function handleGenerate() {
  error.value = ''
  if (!requirement.value.trim()) {
    error.value = '请先填写需求描述。'
    return
  }
  generating.value = true
  try {
    const data = await api('/admin/aitest/generate', { requirement: requirement.value.trim() }, 'admin', 120000)
    cases.value = decorateCases(data.cases)
    toast(`AI 已生成 ${cases.value.length} 条用例`)
  } catch (e) {
    error.value = e.message
  } finally {
    generating.value = false
  }
}

async function loadRegression() {
  error.value = ''
  try {
    const data = await api('/admin/aitest/regression', {}, 'admin')
    cases.value = decorateCases(data.cases)
    toast(`已加载 ${cases.value.length} 条内置回归用例`)
  } catch (e) {
    error.value = e.message
  }
}

function toggleAll() {
  if (allSelected.value) {
    cases.value.forEach(c => { c.selected = false })
  } else {
    cases.value.forEach(c => { c.selected = true })
  }
}

async function runCases(payloadCases, label) {
  error.value = ''
  running.value = true
  results.value = []
  try {
    const data = await api('/admin/aitest/run', { cases: payloadCases }, 'admin')
    results.value = data.results || []
    summary.value = data.summary || { total: results.value.length, pass: 0, fail: results.value.length }
    toast(`${label}执行完成：通过 ${summary.value.pass} / 失败 ${summary.value.fail}`)
  } catch (e) {
    error.value = e.message
  } finally {
    running.value = false
  }
}

function runSelected() {
  const picked = cases.value.filter(c => c.selected).map(c => ({
    name: c.name, method: c.method, path: c.path, body: c.body, expect: c.expect
  }))
  runCases(picked, '选中用例')
}

function runRegression() {
  runCases([], '内置回归套件')
}
</script>

<style scoped>
.aitest {
  color: #143b30;
}

.eyebrow {
  color: #8a998c;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: .18em;
}

h1 {
  margin: 8px 0 6px;
  font-size: 26px;
  letter-spacing: -.02em;
}

.subtitle {
  margin: 0 0 22px;
  color: #75877a;
  font-size: 13.5px;
}

.panel {
  margin-bottom: 18px;
  padding: 22px;
  border: 1px solid rgba(28, 74, 59, .12);
  border-radius: 18px;
  background: linear-gradient(160deg, #fffdf7, #f4f8f1);
  box-shadow: 0 16px 34px -30px rgba(20, 59, 48, .55);
}

.panel-head {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 14px;
}

.panel-head h2 {
  margin: 0;
  font-size: 16.5px;
}

.hint {
  color: #93a296;
  font-size: 12px;
}

.field {
  margin-bottom: 14px;
}

.field label {
  display: block;
  margin-bottom: 7px;
  color: #4d6559;
  font-size: 13px;
  font-weight: 600;
}

textarea {
  box-sizing: border-box;
  width: 100%;
  padding: 12px 14px;
  border: 1px solid rgba(28, 74, 59, .16);
  border-radius: 12px;
  background: rgba(255, 255, 255, .8);
  color: #143b30;
  font: inherit;
  font-size: 13.5px;
  line-height: 1.7;
  resize: vertical;
  outline: none;
}

textarea:focus {
  border-color: rgba(28, 74, 59, .4);
  box-shadow: 0 0 0 3px rgba(143, 182, 161, .2);
}

.row {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  min-height: 40px;
  padding: 10px 18px;
  border: 1px solid rgba(28, 74, 59, .2);
  border-radius: 12px;
  background: #fff;
  color: #1c4a3b;
  font-size: 13.5px;
  font-weight: 600;
  cursor: pointer;
  transition: transform .15s ease, box-shadow .15s ease, background .15s ease;
}

.btn:hover:not(:disabled) {
  background: #eef3e6;
  transform: translateY(-1px);
}

.btn:disabled {
  opacity: .55;
  cursor: not-allowed;
}

.btn.primary {
  border-color: transparent;
  background: linear-gradient(135deg, #215c48, #143d31);
  color: #faf6ea;
  box-shadow: 0 14px 26px -18px rgba(20, 61, 49, .9);
}

.btn.primary:hover:not(:disabled) {
  background: linear-gradient(135deg, #256450, #164439);
}

.spin {
  width: 14px;
  height: 14px;
  border: 2px solid rgba(250, 246, 234, .35);
  border-top-color: #faf6ea;
  border-radius: 50%;
  animation: rotate .8s linear infinite;
}

@keyframes rotate {
  to { transform: rotate(360deg); }
}

.notice {
  margin: 0 0 14px;
  padding: 11px 14px;
  border-radius: 12px;
  font-size: 13px;
  line-height: 1.6;
}

.notice.error {
  border: 1px solid rgba(164, 82, 63, .25);
  background: rgba(164, 82, 63, .08);
  color: #a4523f;
}

.case-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 16px;
}

.case-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 12px 14px;
  border: 1px solid rgba(28, 74, 59, .12);
  border-radius: 12px;
  background: rgba(255, 255, 255, .65);
  cursor: pointer;
  transition: border-color .15s ease, background .15s ease;
}

.case-item:hover {
  border-color: rgba(28, 74, 59, .3);
}

.case-item.checked {
  border-color: rgba(33, 92, 72, .45);
  background: #eef4ea;
}

.case-item input {
  margin-top: 3px;
  accent-color: #1c4a3b;
}

.case-meta {
  display: flex;
  flex-direction: column;
  gap: 3px;
  min-width: 0;
}

.case-meta strong {
  font-size: 14px;
}

.case-meta code {
  color: #4d7c6c;
  font-size: 12.5px;
  overflow-wrap: anywhere;
}

.case-meta small {
  color: #8a998c;
  font-size: 12px;
  overflow-wrap: anywhere;
}

.summary {
  display: flex;
  gap: 8px;
}

.chip {
  padding: 5px 12px;
  border: 1px solid rgba(28, 74, 59, .14);
  border-radius: 999px;
  background: #fff;
  color: #5c7267;
  font-size: 12px;
  font-weight: 700;
}

.chip.ok {
  border-color: rgba(33, 92, 72, .35);
  background: #e7f1e5;
  color: #1c4a3b;
}

.chip.bad {
  border-color: rgba(164, 82, 63, .3);
  background: #f7e9e3;
  color: #a4523f;
}

.result-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 13px;
}

.result-table th {
  padding: 10px 12px;
  border-bottom: 1px solid rgba(28, 74, 59, .16);
  color: #75877a;
  font-size: 12px;
  text-align: left;
  letter-spacing: .04em;
}

.result-table td {
  padding: 11px 12px;
  border-bottom: 1px solid rgba(28, 74, 59, .08);
  color: #3c5348;
  vertical-align: top;
}

.result-table code {
  color: #4d7c6c;
  font-size: 12px;
  overflow-wrap: anywhere;
}

.td-name {
  font-weight: 600;
}

.td-expect {
  max-width: 260px;
  overflow-wrap: anywhere;
}

.badge {
  display: inline-block;
  padding: 4px 11px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 700;
}

.badge.pass {
  background: #e7f1e5;
  color: #1c4a3b;
}

.badge.fail {
  background: #f7e9e3;
  color: #a4523f;
}

.empty p {
  margin: 0;
  color: #93a296;
  font-size: 13.5px;
}
</style>

<template>
  <div>
    <h1>预约订单管理</h1>
    <p class="muted">选择场地进入预约名单，完成订单审核、签到核销与 Excel 导出。</p>

    <div v-if="showExportTip" class="notice">
      进入场地订单页后，在底部「导出名单」选择日期范围，即可导出 Excel 预约报表。
    </div>

    <form class="toolbar" @submit.prevent="handleSearch">
      <input v-model="search" placeholder="搜索场地名称或关键词" aria-label="搜索场地">
      <button class="primary" type="submit">搜索</button>
    </form>

    <div class="table-wrap">
      <table>
        <thead><tr><th>场地</th><th>分类</th><th>状态</th><th>订单入口</th></tr></thead>
        <tbody>
          <tr v-for="x in list" :key="x.meetId">
            <td>{{ x.meetTitle }}<br><small>#{{ x.meetId }}</small></td>
            <td>{{ x.meetCateName }}<br><small>每时段 {{ x.meetMaxCnt }} 人</small></td>
            <td>{{ Number(x.meetStatus) === 1 ? '开放中' : '已停用' }}</td>
            <td>
              <button @click="router.push(`/admin/joins?id=${x.meetId}`)">预约名单 / 核销 / 导出</button>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-if="loading" class="empty">正在加载场地…</div>
      <div v-else-if="!list.length" class="empty">暂无场地，请先在「自习场地管理」中新增。</div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { api } from '../../api/request'

const route = useRoute()
const router = useRouter()
const list = ref([])
const loading = ref(true)
const search = ref('')

const showExportTip = computed(() => route.query.export === '1')

async function loadData() {
  loading.value = true
  try {
    const r = await api('/admin/meet/list', { page: 1, size: 100, isTotal: false, search: search.value })
    list.value = r.list || []
  } catch {
    list.value = []
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  loadData()
}

onMounted(loadData)
</script>

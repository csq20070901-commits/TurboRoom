<template>
  <div>
    <div class="eyebrow">CAMPUS STUDY / TURBOROOM</div>
    <h1>找到适合你的自习空间</h1>
    <TextLoop
      text="找到适合你的自习空间 ✦ TurboRoom ✦ Campus Study ✦ 自习预约 ✦ 专注空间"
      shape="wave"
      :speed="60"
      direction="forward"
      separator="✦"
      :curviness="30"
      :tilt="-2"
      :fontSize="24"
      :fontWeight="700"
      :letterSpacing="3"
      :uppercase="false"
      :ribbon="true"
      ribbonColor="#1a7a6b"
      :ribbonWidth="42"
      :edgeFade="0.5"
      :pauseOnHover="true"
    />
    <p class="muted">按场地分类、教学楼和开放日期查找，点击分类直达目标空间。</p>

    <nav class="cate-nav" aria-label="场地分类">
      <button
        type="button"
        class="cate-tile"
        :class="{ active: !filters.roomType }"
        @click="selectCategory('')"
      >
        <span class="cate-icon">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
            <path d="M3 3h7v7H3z" />
            <path d="M14 3h7v7h-7z" />
            <path d="M3 14h7v7H3z" />
            <path d="M14 14h7v7h-7z" />
          </svg>
        </span>
        <span class="cate-label">全部场地</span>
        <span class="cate-sub">ALL SPACES</span>
      </button>
      <button
        v-for="c in categoryItems"
        :key="c.name"
        type="button"
        class="cate-tile"
        :class="{ active: filters.roomType === c.name }"
        @click="selectCategory(c.name)"
      >
        <span class="cate-icon">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
            <path v-for="d in c.paths" :key="d" :d="d" />
          </svg>
        </span>
        <span class="cate-label">{{ c.name }}</span>
        <span class="cate-sub">{{ c.en }}</span>
      </button>
    </nav>

    <form class="toolbar" @submit.prevent="handleSearch">
      <input v-model="filters.search" aria-label="搜索名称或关键词" placeholder="搜索名称或关键词">
      <select v-model="filters.building" aria-label="教学楼">
        <option value="">全部楼宇</option>
        <option v-for="b in buildings" :key="b" :value="b">{{ b }}</option>
      </select>
      <select v-model="filters.roomType" aria-label="场地分类">
        <option value="">全部分类</option>
        <option v-for="c in categories" :key="c" :value="c">{{ c }}</option>
      </select>
      <input v-model="filters.day" type="date" aria-label="开放日期">
      <SpecularButton
        size="md"
        :radius="10"
        tint="#1a7a6b"
        :tintOpacity="0.2"
        textColor="#ffffff"
        lineColor="#ffffff"
        baseColor="#1a7a6b"
        :intensity="0.9"
        :idleIntensity="0.4"
        type="submit"
      >
        查询
      </SpecularButton>
      <SpecularButton
        size="md"
        :radius="10"
        tint="#6b7280"
        :tintOpacity="0.1"
        textColor="#374151"
        lineColor="#6b7280"
        baseColor="#6b7280"
        :intensity="0.7"
        :idleIntensity="0.3"
        @click="router.push('/meets')"
      >
        重置
      </SpecularButton>
    </form>

    <AppSkeleton v-if="loading" />
    <template v-else>
      <div class="grid">
        <AppCard v-for="item in list" :key="item.meetId" :item="item" type="meet" />
      </div>
      <div v-if="!list.length" class="empty">暂无开放场地，请稍后再来。</div>
      <AppPagination
        v-if="total > 0"
        :total="total"
        :page="page"
        :size="size"
        @navigate="handlePage"
      />
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { api } from '../../api/request'
import { categories, buildings } from '../../composables/useHelpers'
import AppCard from '../../components/AppCard.vue'
import AppPagination from '../../components/AppPagination.vue'
import AppSkeleton from '../../components/AppSkeleton.vue'
import TextLoop from '../../components/TextLoop.vue'

const route = useRoute()
const router = useRouter()

const loading = ref(true)
const list = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(12)

const filters = ref({
  search: '',
  building: '',
  roomType: '',
  day: ''
})

const categoryItems = [
  { name: '图书馆', en: 'LIBRARY', paths: ['M12 6.5C10.6 5.2 8.6 4.5 6.5 4.5v15c2.1 0 4.1.7 5.5 2 1.4-1.3 3.4-2 5.5-2v-15c-2.1 0-4.1.7-5.5 2Z', 'M12 6.5v15'] },
  { name: '教学楼', en: 'TEACHING', paths: ['M4 21V5l8-3v19', 'M12 21V8l8 3v10', 'M8 10h.01', 'M8 14h.01', 'M8 18h.01', 'M16 14h.01', 'M16 18h.01'] },
  { name: '实验室', en: 'LAB', paths: ['M9 3h6', 'M10 3v5.5L4.5 17a3.5 3.5 0 0 0 3 5.5h9a3.5 3.5 0 0 0 3-5.5L14 8.5V3', 'M7.5 15h9'] },
  { name: '院系楼', en: 'DEPARTMENT', paths: ['M12 3 2 8h20L12 3Z', 'M5 10v8', 'M9.5 10v8', 'M14.5 10v8', 'M19 10v8', 'M3 21h18', 'M7 21v-3', 'M17 21v-3'] },
  { name: '考研自习室', en: 'POSTGRAD', paths: ['M22 9 12 4 2 9l10 5 10-5Z', 'M6 11.5V17c0 1.5 2.7 3 6 3s6-1.5 6-3v-5.5', 'M22 9v5'] },
  { name: '其他', en: 'MORE', paths: ['M5 12h.01', 'M12 12h.01', 'M19 12h.01'] }
]

function readQuery() {
  const q = route.query
  page.value = Number(q.page) || 1
  size.value = Number(q.size) || 12
  filters.value.search = q.search || ''
  filters.value.building = q.building || ''
  filters.value.roomType = q.roomType || ''
  filters.value.day = q.day || ''
}

async function loadData() {
  loading.value = true
  try {
    const r = await api('/meet/list', {
      page: page.value,
      size: size.value,
      isTotal: true,
      ...filters.value
    })
    list.value = r.list || []
    total.value = Number(r.total) || 0
  } catch { list.value = []; total.value = 0 }
  finally { loading.value = false }
}

function selectCategory(name) {
  router.push({ path: '/meets', query: { ...filters.value, roomType: name, page: 1, size: size.value } })
}

function handleSearch() {
  router.push({ path: '/meets', query: { ...filters.value, page: 1, size: size.value } })
}

function handlePage(p, s) {
  router.push({ path: '/meets', query: { ...filters.value, page: p, size: s } })
}

onMounted(() => { readQuery(); loadData() })
watch(() => route.query, () => { readQuery(); loadData() })
</script>

<style scoped>
.cate-nav {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(136px, 1fr));
  gap: 12px;
  margin: 20px 0 18px;
}

.cate-tile {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 7px;
  padding: 16px 10px 13px;
  border: 1px solid rgba(26, 122, 107, .16);
  border-radius: 18px;
  background: linear-gradient(165deg, #fffdf8 0%, #f2f7f0 100%);
  color: #2f6b58;
  cursor: pointer;
  transition: transform .2s ease, box-shadow .2s ease, border-color .2s ease, background .2s ease, color .2s ease;
}

.cate-tile:hover {
  transform: translateY(-2px);
  border-color: rgba(26, 122, 107, .34);
  box-shadow: 0 14px 26px -18px rgba(20, 59, 48, .55);
}

.cate-tile.active {
  background: linear-gradient(150deg, #1a7a6b 0%, #145c50 100%);
  border-color: #1a7a6b;
  color: #f7fbf4;
  box-shadow: 0 16px 28px -16px rgba(26, 122, 107, .65), inset 0 1px 0 rgba(255, 255, 255, .18);
}

.cate-icon {
  display: grid;
  place-items: center;
  width: 42px;
  height: 42px;
  border-radius: 13px;
  background: rgba(26, 122, 107, .09);
  transition: background .2s ease;
}

.cate-tile.active .cate-icon {
  background: rgba(255, 255, 255, .16);
}

.cate-icon svg {
  width: 22px;
  height: 22px;
}

.cate-label {
  font-size: 14.5px;
  font-weight: 700;
  letter-spacing: .02em;
}

.cate-sub {
  font-size: 9.5px;
  font-weight: 700;
  letter-spacing: 2px;
  opacity: .5;
}

.cate-tile.active .cate-sub {
  opacity: .75;
}
</style>

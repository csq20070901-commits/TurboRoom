<template>
  <router-view v-if="isLogin" />
  <div v-else class="admin-shell">
    <div class="admin-atmos" aria-hidden="true">
      <span class="admin-atmos__grid"></span>
      <svg class="admin-atmos__flow" viewBox="0 0 1400 1000" preserveAspectRatio="none" fill="none">
        <g stroke="#8fbaa4" stroke-width="1.2" stroke-linecap="round">
          <path d="M-40 130C260 70 460 190 740 128S1180 30 1460 96" opacity=".26" />
          <path d="M-40 300C280 232 500 348 800 276S1220 170 1460 232" opacity=".16" />
          <path d="M-40 620C300 548 560 680 880 598S1260 490 1460 552" opacity=".12" />
          <path d="M-40 880C320 800 620 920 960 840S1300 750 1460 800" opacity=".09" />
        </g>
      </svg>
    </div>

    <button v-if="sidebarOpen" class="admin-backdrop" aria-label="关闭导航" @click="sidebarOpen = false"></button>

    <aside class="admin-sidebar" :class="{ 'is-open': sidebarOpen }" aria-label="后台侧边导航">
      <router-link class="admin-brand" to="/admin" @click="closeSidebar">
        <img class="admin-brand__logo" src="/logo.png" alt="">
        <span class="admin-brand__text">
          <strong>TurboRoom</strong>
          <small>校园自习预约</small>
        </span>
      </router-link>

      <button class="admin-create" type="button" @click="goCreate">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" aria-hidden="true">
          <path d="M12 5v14M5 12h14" />
        </svg>
        新建预约
      </button>

      <nav class="admin-menu" aria-label="管理菜单">
        <router-link
          v-for="item in navItems"
          :key="item.label"
          :to="item.to"
          class="admin-menu__item"
          :class="{ 'is-active': isActive(item) }"
          :aria-current="isActive(item) ? 'page' : undefined"
          @click="closeSidebar"
        >
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
            <path :d="item.icon" />
          </svg>
          <span>{{ item.label }}</span>
          <svg v-if="item.external" class="admin-menu__external" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
            <path d="M14 5h5v5M19 5l-8 8M18 13v5a1 1 0 0 1-1 1H6a1 1 0 0 1-1-1V7a1 1 0 0 1 1-1h5" />
          </svg>
        </router-link>
      </nav>

      <section class="admin-activity" aria-label="最近操作记录">
        <div class="admin-activity__head">
          <span>最近操作</span>
          <span class="admin-activity__pulse" aria-hidden="true"></span>
        </div>
        <ul v-if="activities.length" class="admin-activity__list">
          <li v-for="entry in activities.slice(0, 4)" :key="entry.path">
            <router-link :to="entry.path" @click="closeSidebar">
              <span class="admin-activity__label">{{ entry.label }}</span>
              <time>{{ formatTime(entry.time) }}</time>
            </router-link>
          </li>
        </ul>
        <p v-else class="admin-activity__empty">暂无记录，从上方菜单开始管理。</p>
      </section>

      <p class="admin-sidebar__foot">TurboRoom · 管理工作台</p>
    </aside>

    <div class="admin-body">
      <header class="admin-topbar">
        <button class="admin-iconbtn admin-topbar__menu" type="button" aria-label="打开导航" @click="sidebarOpen = true">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" aria-hidden="true">
            <path d="M4 6h16M4 12h16M4 18h16" />
          </svg>
        </button>

        <nav class="admin-crumbs" aria-label="面包屑">
          <router-link to="/admin">后台首页</router-link>
          <template v-for="(crumb, index) in breadcrumbs" :key="crumb">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
              <path d="m9 5 7 7-7 7" />
            </svg>
            <span :aria-current="index === breadcrumbs.length - 1 ? 'page' : undefined">{{ crumb }}</span>
          </template>
        </nav>

        <form class="admin-search" role="search" @submit.prevent="handleSearch">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" aria-hidden="true">
            <circle cx="10.8" cy="10.8" r="6.5" />
            <path d="m16 16 4.5 4.5" />
          </svg>
          <input v-model.trim="search" type="search" placeholder="搜索自习场地…" aria-label="搜索自习场地">
        </form>

        <button class="admin-iconbtn admin-bell" type="button" title="预约订单提醒" aria-label="查看预约订单" @click="goOrders">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
            <path d="M18 9a6 6 0 1 0-12 0c0 5-2 6-2 6h16s-2-1-2-6ZM10 19a2.2 2.2 0 0 0 4 0" />
          </svg>
          <span class="admin-bell__dot" aria-hidden="true"></span>
        </button>

        <div ref="accountRef" class="admin-account">
          <button class="admin-avatar" type="button" aria-haspopup="menu" :aria-expanded="accountOpen" @click="accountOpen = !accountOpen">
            <span class="admin-avatar__circle" aria-hidden="true">{{ avatarText }}</span>
            <span class="admin-avatar__meta">
              <strong>{{ adminName }}</strong>
              <small>系统管理员</small>
            </span>
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
              <path d="m6 9 6 6 6-6" />
            </svg>
          </button>
          <div v-if="accountOpen" class="admin-account__menu" role="menu">
            <router-link role="menuitem" to="/admin/setup" @click="accountOpen = false">系统设置</router-link>
            <router-link role="menuitem" to="/admin/managers" @click="accountOpen = false">管理员管理</router-link>
            <router-link role="menuitem" to="/admin/password" @click="accountOpen = false">修改密码</router-link>
            <router-link role="menuitem" to="/home" @click="accountOpen = false">学生端首页</router-link>
            <button role="menuitem" type="button" @click="handleLogout">退出登录</button>
          </div>
        </div>
      </header>

      <main id="main" class="admin-main" tabindex="-1">
        <div class="admin-content">
          <router-view />
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import { computed, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { useAdminActivity } from '../composables/useAdminActivity'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const { items: activities, record, formatTime } = useAdminActivity()

const sidebarOpen = ref(false)
const accountOpen = ref(false)
const accountRef = ref(null)
const search = ref('')

const isLogin = computed(() => route.name === 'AdminLogin')
const adminName = computed(() => userStore.admin?.name || '管理员')
const avatarText = computed(() => adminName.value.slice(0, 1).toUpperCase())

const navItems = [
  {
    label: '自习场地管理',
    to: '/admin/meets',
    matches: ['AdminMeets', 'AdminDays'],
    icon: 'M12 6.6C10.4 5.3 8.2 4.7 5.8 5v12.5c2.4-.3 4.6.3 6.2 1.6 1.6-1.3 3.8-1.9 6.2-1.6V5c-2.4-.3-4.6.3-6.2 1.6ZM12 6.6v12.5'
  },
  {
    label: '预约订单管理',
    to: '/admin/orders',
    matches: ['AdminOrders', 'AdminJoins'],
    icon: 'M7 3v3M17 3v3M4.5 8.5h15M5.5 5h13A1.5 1.5 0 0 1 20 6.5v12a1.5 1.5 0 0 1-1.5 1.5h-13A1.5 1.5 0 0 1 4 18.5v-12A1.5 1.5 0 0 1 5.5 5ZM9 14l2 2 4-4'
  },
  {
    label: '校园公告管理',
    to: '/admin/news',
    matches: ['AdminNews'],
    icon: 'M4.5 10.2v3.6c0 .6.5 1.1 1.1 1.1h1.7l5.2 3.3V5.8L7.3 9.1H5.6c-.6 0-1.1.5-1.1 1.1ZM15.3 8.4a4.7 4.7 0 0 1 0 7.2M17.8 5.9a8.2 8.2 0 0 1 0 12.2'
  },
  {
    label: 'AI 助手',
    to: '/ai',
    matches: [],
    external: true,
    icon: 'M12 4l1.6 4.9 4.9 1.6-4.9 1.6L12 17l-1.6-4.9-4.9-1.6 4.9-1.6L12 4ZM18.5 15.5l.8 2.2 2.2.8-2.2.8-.8 2.2-.8-2.2-2.2-.8 2.2-.8.8-2.2Z'
  },
  {
    label: '用户管理',
    to: '/admin/users',
    matches: ['AdminUsers', 'AdminUserDetail'],
    icon: 'M16 19v-1.5A3.5 3.5 0 0 0 12.5 14h-5A3.5 3.5 0 0 0 4 17.5V19M10 10.5a3 3 0 1 0 0-6 3 3 0 0 0 0 6ZM17 11a2.6 2.6 0 1 0-1.2-4.9M20 19v-1.4a3.4 3.4 0 0 0-2.5-3.3'
  },
  {
    label: '数据统计看板',
    to: '/admin',
    matches: ['AdminDashboard'],
    icon: 'M4 4v15a1 1 0 0 0 1 1h15M8.5 15v-4M13 15V8M17.5 15v-6'
  },
  {
    label: 'AI 测试中心',
    to: '/admin/aitest',
    matches: ['AdminAiTest'],
    icon: 'M9 3h6M10 3v5.3L4.7 17a2 2 0 0 0 2.4 2.9L12 17.5l4.9 2.4a2 2 0 0 0 2.4-2.9L14 8.3V3M7.5 14h9'
  },
  {
    label: '系统设置',
    to: '/admin/setup',
    matches: ['AdminSetup', 'AdminManagers', 'AdminPassword'],
    icon: 'M12 15a3 3 0 1 0 0-6 3 3 0 0 0 0 6ZM19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 1 1-2.83 2.83l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 1 1-4 0v-.09a1.65 1.65 0 0 0-1-1.51 1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 1 1-2.83-2.83l.06-.06A1.65 1.65 0 0 0 4.6 15a1.65 1.65 0 0 0-1.51-1H3a2 2 0 1 1 0-4h.09a1.65 1.65 0 0 0 1.51-1 1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 1 1 2.83-2.83l.06.06A1.65 1.65 0 0 0 9 4.6a1.65 1.65 0 0 0 1-1.51V3a2 2 0 1 1 4 0v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 1 1 2.83 2.83l-.06.06A1.65 1.65 0 0 0 19.4 9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 1 1 0 4h-.09a1.65 1.65 0 0 0-1.51 1Z'
  }
]

const breadcrumbs = computed(() => {
  switch (route.name) {
    case 'AdminDashboard': return ['数据统计看板']
    case 'AdminMeets': return ['自习场地管理']
    case 'AdminEdit': return route.query.type === 'news' ? ['校园公告管理', '编辑公告'] : ['自习场地管理', '编辑场地']
    case 'AdminDays': return ['自习场地管理', '开放日期']
    case 'AdminOrders': return ['预约订单管理']
    case 'AdminJoins': return ['预约订单管理', '预约名单']
    case 'AdminNews': return ['校园公告管理']
    case 'AdminUsers': return ['用户管理']
    case 'AdminUserDetail': return ['用户管理', '用户详情']
    case 'AdminManagers': return ['系统设置', '管理员管理']
    case 'AdminSetup': return ['系统设置']
    case 'AdminAiTest': return ['AI 测试中心']
    case 'AdminPassword': return ['系统设置', '修改密码']
    default: return []
  }
})

function isActive(item) {
  if (route.name === 'AdminEdit') {
    return route.query.type === 'news' ? item.to === '/admin/news' : item.to === '/admin/meets'
  }
  return item.matches.includes(route.name)
}

function closeSidebar() {
  sidebarOpen.value = false
}

function goCreate() {
  closeSidebar()
  router.push('/admin/edit?type=meet')
}

function goOrders() {
  router.push('/admin/orders')
}

function handleSearch() {
  router.push({ path: '/admin/meets', query: { page: 1, search: search.value } })
}

function handleLogout() {
  accountOpen.value = false
  userStore.logout('admin')
  router.push('/admin/login')
}

function onDocumentClick(event) {
  if (accountRef.value && !accountRef.value.contains(event.target)) accountOpen.value = false
}

watch(() => route.fullPath, () => {
  accountOpen.value = false
  closeSidebar()
  if (!isLogin.value && userStore.admin && breadcrumbs.value.length) {
    record(breadcrumbs.value.join(' · '), route.fullPath)
  }
}, { immediate: true })

onMounted(() => document.addEventListener('click', onDocumentClick))
onBeforeUnmount(() => document.removeEventListener('click', onDocumentClick))
</script>

<style scoped>
.admin-shell {
  --forest: #1c4a3b;
  --forest-deep: #143b30;
  --sidebar-w: 252px;
  position: relative;
  min-height: 100vh;
  color: var(--forest-deep);
}

.admin-atmos {
  position: fixed;
  inset: 0;
  z-index: 0;
  pointer-events: none;
  background:
    radial-gradient(52% 34% at 88% 0%, rgba(206, 228, 211, .55), transparent 70%),
    radial-gradient(42% 30% at 30% 100%, rgba(240, 232, 209, .42), transparent 72%),
    #f4f7f2;
}

.admin-atmos__grid {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(rgba(28, 74, 59, .05) 1px, transparent 1px),
    linear-gradient(90deg, rgba(28, 74, 59, .05) 1px, transparent 1px);
  background-size: 38px 38px;
  -webkit-mask-image: radial-gradient(ellipse 86% 80% at 60% 24%, #000 24%, transparent 86%);
  mask-image: radial-gradient(ellipse 86% 80% at 60% 24%, #000 24%, transparent 86%);
}

.admin-atmos__flow {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
}

.admin-sidebar {
  position: fixed;
  inset: 0 auto 0 0;
  z-index: 40;
  display: flex;
  flex-direction: column;
  width: var(--sidebar-w);
  height: 100dvh;
  padding: 24px 18px 16px;
  overflow-y: auto;
  border-right: 1px solid rgba(28, 74, 59, .12);
  background: linear-gradient(180deg, #fffdf7 0%, #f5f8ef 100%);
}

.admin-brand {
  display: flex;
  align-items: center;
  gap: 12px;
  color: var(--forest-deep);
}

.admin-brand:hover {
  text-decoration: none;
}

.admin-brand__logo {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: #fff;
  object-fit: contain;
  box-shadow: 0 6px 16px -10px rgba(20, 59, 48, .5);
}

.admin-brand__text strong {
  display: block;
  font-size: 21px;
  font-weight: 800;
  letter-spacing: -.02em;
  line-height: 1.2;
}

.admin-brand__text small {
  display: block;
  margin-top: 5px;
  color: #75877a;
  font-size: 11px;
  letter-spacing: 3px;
}

.admin-create {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  width: 100%;
  margin: 24px 0 20px;
  padding: 13px 16px;
  border: 0;
  border-radius: 16px;
  background: linear-gradient(135deg, #215c48, #143d31);
  color: #faf6ea;
  font-weight: 600;
  box-shadow: 0 14px 26px -18px rgba(20, 61, 49, .9), inset 0 1px 0 rgba(255, 255, 255, .16);
  transition: transform .2s ease, box-shadow .2s ease;
}

.admin-create:hover {
  background: linear-gradient(135deg, #256450, #164439);
  box-shadow: 0 18px 30px -18px rgba(20, 61, 49, .9);
  transform: translateY(-1px);
}

.admin-create svg {
  width: 17px;
  height: 17px;
}

.admin-menu {
  display: flex;
  flex: 0 0 auto;
  flex-direction: column;
  order: 0;
  gap: 4px;
}

.admin-menu__item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 14px;
  border: 1px solid transparent;
  border-radius: 14px;
  color: #5c7267;
  font-size: 14.5px;
  font-weight: 500;
  transition: background .2s ease, color .2s ease, border-color .2s ease;
}

.admin-menu__item > svg:first-child {
  flex: 0 0 auto;
  width: 20px;
  height: 20px;
}

.admin-menu__item:hover {
  background: rgba(232, 240, 227, .85);
  color: var(--forest);
  text-decoration: none;
}

.admin-menu__item.is-active {
  border-color: rgba(28, 74, 59, .1);
  background: linear-gradient(135deg, #e2eddf, #f3f1e3);
  color: var(--forest);
  font-weight: 700;
  box-shadow: 0 8px 18px -16px rgba(20, 59, 48, .7);
}

.admin-menu__external {
  width: 14px;
  height: 14px;
  margin-left: auto;
  opacity: .45;
}

.admin-activity {
  margin-top: auto;
  padding-top: 22px;
}

.admin-activity__head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 4px 10px;
  border-top: 1px solid rgba(28, 74, 59, .1);
  color: #75877a;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: .12em;
}

.admin-activity__pulse {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: #8fb6a1;
  box-shadow: 0 0 0 4px rgba(143, 182, 161, .18);
}

.admin-activity__list {
  display: flex;
  flex-direction: column;
  gap: 2px;
  margin: 0;
  padding: 0;
  list-style: none;
}

.admin-activity__list a {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
  padding: 8px 10px;
  border-radius: 10px;
  color: #5c7267;
  font-size: 12.5px;
}

.admin-activity__list a:hover {
  background: rgba(232, 240, 227, .8);
  color: var(--forest);
  text-decoration: none;
}

.admin-activity__label {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.admin-activity__list time {
  flex: 0 0 auto;
  color: #93a296;
  font-size: 11px;
  font-variant-numeric: tabular-nums;
}

.admin-activity__empty {
  margin: 0;
  padding: 0 4px;
  color: #93a296;
  font-size: 12px;
  line-height: 1.7;
}

.admin-sidebar__foot {
  margin: 18px 0 0;
  padding: 0 4px;
  color: #9aa99c;
  font-size: 11px;
  letter-spacing: .08em;
}

.admin-body {
  position: relative;
  z-index: 1;
  min-height: 100vh;
  margin-left: var(--sidebar-w);
}

.admin-topbar {
  position: sticky;
  top: 0;
  z-index: 30;
  display: flex;
  align-items: center;
  gap: 18px;
  min-height: 74px;
  padding: 14px 30px;
  border-bottom: 1px solid rgba(28, 74, 59, .1);
  background: rgba(250, 251, 246, .86);
  -webkit-backdrop-filter: blur(14px);
  backdrop-filter: blur(14px);
}

.admin-iconbtn.admin-topbar__menu {
  display: none;
}

.admin-crumbs {
  display: flex;
  flex: 1 1 auto;
  align-items: center;
  order: 0;
  gap: 7px;
  min-width: 0;
  color: #8a998c;
  font-size: 13px;
}

.admin-crumbs a {
  color: #75877a;
  white-space: nowrap;
}

.admin-crumbs a:hover {
  color: var(--forest);
  text-decoration: none;
}

.admin-crumbs svg {
  flex: 0 0 auto;
  width: 13px;
  height: 13px;
  opacity: .55;
}

.admin-crumbs span {
  overflow: hidden;
  color: var(--forest-deep);
  font-weight: 600;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.admin-search {
  display: flex;
  flex: 0 1 280px;
  align-items: center;
  gap: 8px;
  min-width: 170px;
  padding: 0 14px;
  border: 1px solid rgba(28, 74, 59, .14);
  border-radius: 999px;
  background: rgba(255, 253, 247, .9);
  transition: border-color .2s ease, box-shadow .2s ease;
}

.admin-search:focus-within {
  border-color: rgba(28, 74, 59, .34);
  box-shadow: 0 0 0 3px rgba(143, 182, 161, .18);
}

.admin-search svg {
  flex: 0 0 auto;
  width: 17px;
  height: 17px;
  color: #8a998c;
}

.admin-search input {
  min-height: 40px;
  padding: 8px 0;
  border: 0;
  background: transparent;
  color: var(--forest-deep);
  font-size: 13px;
  outline: none;
}

.admin-search input:focus-visible {
  outline: none;
}

.admin-iconbtn {
  position: relative;
  display: inline-grid;
  flex: 0 0 auto;
  place-items: center;
  width: 42px;
  height: 42px;
  min-height: 42px;
  padding: 0;
  border: 1px solid rgba(28, 74, 59, .14);
  border-radius: 13px;
  background: rgba(255, 253, 247, .9);
  color: #5c7267;
  transition: background .2s ease, color .2s ease;
}

.admin-iconbtn:hover {
  background: #eef3e6;
  color: var(--forest);
}

.admin-iconbtn svg {
  width: 19px;
  height: 19px;
}

.admin-bell__dot {
  position: absolute;
  top: 10px;
  right: 11px;
  width: 6px;
  height: 6px;
  border: 1.5px solid #fffdf7;
  border-radius: 50%;
  background: #b39a5f;
}

.admin-account {
  position: relative;
  flex: 0 0 auto;
}

.admin-avatar {
  display: flex;
  align-items: center;
  gap: 10px;
  min-height: 46px;
  padding: 4px 10px 4px 5px;
  border: 1px solid rgba(28, 74, 59, .14);
  border-radius: 999px;
  background: rgba(255, 253, 247, .9);
  color: var(--forest-deep);
}

.admin-avatar:hover {
  background: #eef3e6;
}

.admin-avatar__circle {
  display: grid;
  place-items: center;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: linear-gradient(135deg, #2f6b58, #1c4a3b);
  color: #f8f5e9;
  font-size: 15px;
  font-weight: 700;
}

.admin-avatar__meta {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  line-height: 1.3;
}

.admin-avatar__meta strong {
  font-size: 13px;
  font-weight: 700;
}

.admin-avatar__meta small {
  color: #8a998c;
  font-size: 11px;
}

.admin-avatar > svg {
  width: 14px;
  height: 14px;
  color: #8a998c;
}

.admin-account__menu {
  position: absolute;
  top: calc(100% + 10px);
  right: 0;
  z-index: 50;
  display: flex;
  flex-direction: column;
  min-width: 168px;
  padding: 7px;
  border: 1px solid rgba(28, 74, 59, .14);
  border-radius: 16px;
  background: #fffdf7;
  box-shadow: 0 22px 44px -24px rgba(20, 59, 48, .45);
}

.admin-account__menu a,
.admin-account__menu button {
  display: flex;
  align-items: center;
  width: 100%;
  min-height: 38px;
  padding: 8px 12px;
  border: 0;
  border-radius: 10px;
  background: transparent;
  color: #5c7267;
  font-size: 13px;
  text-align: left;
}

.admin-account__menu a:hover,
.admin-account__menu button:hover {
  background: #eef3e6;
  color: var(--forest);
  text-decoration: none;
}

.admin-account__menu button {
  border-top: 1px solid rgba(28, 74, 59, .1);
  border-radius: 0 0 10px 10px;
  color: #a4523f;
}

.admin-main {
  max-width: none;
  min-height: calc(100vh - 74px);
  margin: 0;
  padding: 30px 32px 60px;
}

.admin-content {
  max-width: 1280px;
  margin: 0 auto;
}

.admin-backdrop {
  display: none;
}

@media (max-width: 1200px) {
  .admin-shell {
    --sidebar-w: 232px;
  }

  .admin-topbar {
    gap: 14px;
    padding: 14px 22px;
  }

  .admin-search {
    flex-basis: 220px;
  }
}

@media (max-width: 960px) {
  .admin-sidebar {
    width: 276px;
    transform: translateX(-100%);
    transition: transform .28s ease;
    box-shadow: 24px 0 60px -40px rgba(20, 59, 48, .5);
  }

  .admin-sidebar.is-open {
    transform: translateX(0);
  }

  .admin-backdrop {
    position: fixed;
    inset: 0;
    z-index: 35;
    display: block;
    width: 100%;
    height: 100%;
    padding: 0;
    border: 0;
    border-radius: 0;
    background: rgba(20, 59, 48, .28);
    -webkit-backdrop-filter: blur(2px);
    backdrop-filter: blur(2px);
  }

  .admin-body {
    margin-left: 0;
  }

  .admin-iconbtn.admin-topbar__menu {
    display: inline-grid;
  }

  .admin-main {
    padding: 24px 22px 48px;
  }
}

@media (max-width: 640px) {
  .admin-topbar {
    flex-wrap: wrap;
    gap: 10px;
    padding: 12px 16px;
  }

  .admin-crumbs {
    order: 2;
    flex-basis: 100%;
    font-size: 12px;
  }

  .admin-search {
    order: 1;
    flex: 1 1 auto;
    min-width: 0;
  }

  .admin-topbar__menu {
    order: 0;
  }

  .admin-bell {
    order: 1;
  }

  .admin-account {
    order: 1;
  }

  .admin-avatar__meta,
  .admin-avatar > svg {
    display: none;
  }

  .admin-avatar {
    padding: 4px;
  }

  .admin-main {
    padding: 20px 16px 40px;
  }
}
</style>

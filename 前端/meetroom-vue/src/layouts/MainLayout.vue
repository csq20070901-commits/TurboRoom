<template>
  <router-view v-if="isBare" />
  <div v-else class="stu-shell">
    <div class="stu-atmos" aria-hidden="true">
      <span class="stu-atmos__grid"></span>
      <svg class="stu-atmos__flow" viewBox="0 0 1400 1000" preserveAspectRatio="none" fill="none">
        <g stroke="#8fbaa4" stroke-width="1.2" stroke-linecap="round">
          <path d="M-40 130C260 70 460 190 740 128S1180 30 1460 96" opacity=".26" />
          <path d="M-40 300C280 232 500 348 800 276S1220 170 1460 232" opacity=".16" />
          <path d="M-40 620C300 548 560 680 880 598S1260 490 1460 552" opacity=".12" />
          <path d="M-40 880C320 800 620 920 960 840S1300 750 1460 800" opacity=".09" />
        </g>
      </svg>
    </div>

    <button v-if="sidebarOpen" class="stu-backdrop" aria-label="关闭导航" @click="sidebarOpen = false"></button>

    <aside class="stu-sidebar" :class="{ 'is-open': sidebarOpen }" aria-label="学生端侧边导航">
      <router-link class="stu-brand" to="/home" @click="closeSidebar">
        <img class="stu-brand__logo" src="/logo.png" alt="">
        <span class="stu-brand__text">
          <strong>TurboRoom</strong>
          <small>校园自习预约</small>
        </span>
      </router-link>

      <button class="stu-book" type="button" @click="goBook">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
          <path d="M7 3v3M17 3v3M4.5 8.5h15M5.5 5h13A1.5 1.5 0 0 1 20 6.5v12a1.5 1.5 0 0 1-1.5 1.5h-13A1.5 1.5 0 0 1 4 18.5v-12A1.5 1.5 0 0 1 5.5 5Z" />
          <path d="M12 12.5v5M9.5 15h5" />
        </svg>
        预约自习
      </button>

      <nav class="stu-menu" aria-label="学生端菜单">
        <router-link
          v-for="item in navItems"
          :key="item.label"
          :to="item.to"
          class="stu-menu__item"
          :class="{ 'is-active': isActive(item) }"
          :aria-current="isActive(item) ? 'page' : undefined"
          @click="closeSidebar"
        >
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
            <path :d="item.icon" />
          </svg>
          <span>{{ item.label }}</span>
          <svg v-if="item.external" class="stu-menu__external" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
            <path d="M14 5h5v5M19 5l-8 8M18 13v5a1 1 0 0 1-1 1H6a1 1 0 0 1-1-1V7a1 1 0 0 1 1-1h5" />
          </svg>
        </router-link>
      </nav>

      <section class="stu-user" aria-label="当前登录用户">
        <div class="stu-user__head">
          <span class="stu-user__circle" aria-hidden="true">{{ avatarText }}</span>
          <span class="stu-user__meta">
            <strong>{{ studentName }}</strong>
            <small>{{ userStore.cust?.account || '学生账号' }}</small>
          </span>
        </div>
        <button class="stu-user__logout" type="button" @click="handleLogout">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
            <path d="M9 21H6a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h3M16 17l5-5-5-5M21 12H9" />
          </svg>
          退出登录
        </button>
      </section>

      <p class="stu-sidebar__foot">
        TurboRoom · 学生端
        <router-link to="/about" @click="closeSidebar">关于我们</router-link>
      </p>
    </aside>

    <div class="stu-body">
      <header class="stu-topbar">
        <button class="stu-iconbtn stu-topbar__menu" type="button" aria-label="打开导航" @click="sidebarOpen = true">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" aria-hidden="true">
            <path d="M4 6h16M4 12h16M4 18h16" />
          </svg>
        </button>

        <nav class="stu-crumbs" aria-label="面包屑">
          <template v-if="route.name === 'Home'">
            <span aria-current="page">首页</span>
          </template>
          <template v-else>
            <router-link to="/home">首页</router-link>
            <template v-for="(crumb, index) in breadcrumbs" :key="crumb">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                <path d="m9 5 7 7-7 7" />
              </svg>
              <span :aria-current="index === breadcrumbs.length - 1 ? 'page' : undefined">{{ crumb }}</span>
            </template>
          </template>
        </nav>

        <form class="stu-search" role="search" @submit.prevent="handleSearch">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" aria-hidden="true">
            <circle cx="10.8" cy="10.8" r="6.5" />
            <path d="m16 16 4.5 4.5" />
          </svg>
          <input v-model.trim="search" type="search" placeholder="搜索自习场地…" aria-label="搜索自习场地">
        </form>

        <button class="stu-iconbtn stu-bell" type="button" title="我的预约" aria-label="查看我的预约" @click="goBookings">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
            <path d="M18 9a6 6 0 1 0-12 0c0 5-2 6-2 6h16s-2-1-2-6ZM10 19a2.2 2.2 0 0 0 4 0" />
          </svg>
          <span class="stu-bell__dot" aria-hidden="true"></span>
        </button>

        <div ref="accountRef" class="stu-account">
          <button class="stu-avatar" type="button" aria-haspopup="menu" :aria-expanded="accountOpen" @click="accountOpen = !accountOpen">
            <span class="stu-avatar__circle" aria-hidden="true">{{ avatarText }}</span>
            <span class="stu-avatar__meta">
              <strong>{{ studentName }}</strong>
              <small>学生用户</small>
            </span>
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
              <path d="m6 9 6 6 6-6" />
            </svg>
          </button>
          <div v-if="accountOpen" class="stu-account__menu" role="menu">
            <router-link role="menuitem" to="/profile" @click="accountOpen = false">个人中心</router-link>
            <router-link role="menuitem" to="/bookings" @click="accountOpen = false">我的预约</router-link>
            <router-link role="menuitem" to="/password" @click="accountOpen = false">修改密码</router-link>
            <router-link role="menuitem" to="/admin" @click="accountOpen = false">管理入口</router-link>
            <button role="menuitem" type="button" @click="handleLogout">退出登录</button>
          </div>
        </div>
      </header>

      <main id="main" class="stu-main" tabindex="-1">
        <div class="stu-content">
          <router-view />
        </div>
      </main>
    </div>
  </div>

  <AiRobotFab v-if="route.name !== 'AiAssistant' && userStore.cust" />
</template>

<script setup>
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import AiRobotFab from '../components/AiRobotFab.vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const sidebarOpen = ref(false)
const accountOpen = ref(false)
const accountRef = ref(null)
const search = ref('')

const isBare = computed(() => ['Login', 'Register'].includes(route.name))
const studentName = computed(() => userStore.cust?.name || '同学')
const avatarText = computed(() => (userStore.cust?.name || userStore.cust?.account || 'T').slice(0, 1).toUpperCase())

const navItems = [
  {
    label: '首页',
    to: '/home',
    matches: ['Home'],
    icon: 'M3 10.2 12 3l9 7.2M5.5 9.8V20h5v-5.5h3V20h5V9.8'
  },
  {
    label: '自习场地',
    to: '/meets',
    matches: ['Meets', 'MeetDetail', 'JoinBooking'],
    icon: 'M12 6.6C10.4 5.3 8.2 4.7 5.8 5v12.5c2.4-.3 4.6.3 6.2 1.6 1.6-1.3 3.8-1.9 6.2-1.6V5c-2.4-.3-4.6.3-6.2 1.6ZM12 6.6v12.5'
  },
  {
    label: '校园公告',
    to: '/news-list',
    matches: ['NewsList', 'NewsDetail'],
    icon: 'M4.5 10.2v3.6c0 .6.5 1.1 1.1 1.1h1.7l5.2 3.3V5.8L7.3 9.1H5.6c-.6 0-1.1.5-1.1 1.1ZM15.3 8.4a4.7 4.7 0 0 1 0 7.2M17.8 5.9a8.2 8.2 0 0 1 0 12.2'
  },
  {
    label: 'AI 助手',
    to: '/ai',
    matches: ['AiAssistant'],
    icon: 'M7 5h10a3 3 0 0 1 3 3v6a3 3 0 0 1-3 3H7a3 3 0 0 1-3-3V8a3 3 0 0 1 3-3ZM12 3v2M12 15v2M9.5 10.5h.01M14.5 10.5h.01M9.5 13.5c1.5 1.2 3.5 1.2 5 0'
  },
  {
    label: '我的预约',
    to: '/bookings',
    matches: ['Bookings', 'BookingDetail'],
    icon: 'M7 3v3M17 3v3M4.5 8.5h15M5.5 5h13A1.5 1.5 0 0 1 20 6.5v12a1.5 1.5 0 0 1-1.5 1.5h-13A1.5 1.5 0 0 1 4 18.5v-12A1.5 1.5 0 0 1 5.5 5ZM9 14l2 2 4-4'
  },
  {
    label: '个人中心',
    to: '/profile',
    matches: ['Profile', 'Password', 'History', 'Favourites'],
    icon: 'M16 19v-1.5A3.5 3.5 0 0 0 12.5 14h-5A3.5 3.5 0 0 0 4 17.5V19M10 10.5a3 3 0 1 0 0-6 3 3 0 0 0 0 6Z'
  }
]

const breadcrumbs = computed(() => {
  switch (route.name) {
    case 'Meets': return ['自习场地']
    case 'MeetDetail': return ['自习场地', '场地详情']
    case 'JoinBooking': return ['自习场地', '预约确认']
    case 'NewsList': return ['校园公告']
    case 'NewsDetail': return ['校园公告', '公告详情']
    case 'AiAssistant': return ['AI 助手']
    case 'Bookings': return ['我的预约']
    case 'BookingDetail': return ['我的预约', '预约详情']
    case 'Profile': return ['个人中心']
    case 'Password': return ['个人中心', '修改密码']
    case 'History': return ['个人中心', '浏览记录']
    case 'Favourites': return ['个人中心', '我的收藏']
    case 'About': return ['关于我们']
    case 'Agreement': return ['用户使用协议']
    default: return []
  }
})

function isActive(item) {
  return item.matches.includes(route.name)
}

function closeSidebar() {
  sidebarOpen.value = false
}

function goBook() {
  closeSidebar()
  router.push('/meets')
}

function goBookings() {
  router.push('/bookings')
}

function handleSearch() {
  router.push({ path: '/meets', query: { page: 1, search: search.value } })
}

function handleLogout() {
  accountOpen.value = false
  userStore.logout('cust')
  router.push('/login')
}

function onDocumentClick(event) {
  if (accountRef.value && !accountRef.value.contains(event.target)) accountOpen.value = false
}

onMounted(() => document.addEventListener('click', onDocumentClick))
onBeforeUnmount(() => document.removeEventListener('click', onDocumentClick))
</script>

<style scoped>
.stu-shell {
  --forest: #1c4a3b;
  --forest-deep: #143b30;
  --sidebar-w: 252px;
  position: relative;
  min-height: 100vh;
  color: var(--forest-deep);
}

.stu-atmos {
  position: fixed;
  inset: 0;
  z-index: 0;
  pointer-events: none;
  background:
    radial-gradient(52% 34% at 88% 0%, rgba(206, 228, 211, .55), transparent 70%),
    radial-gradient(42% 30% at 30% 100%, rgba(240, 232, 209, .42), transparent 72%),
    #f4f7f2;
}

.stu-atmos__grid {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(rgba(28, 74, 59, .05) 1px, transparent 1px),
    linear-gradient(90deg, rgba(28, 74, 59, .05) 1px, transparent 1px);
  background-size: 38px 38px;
  -webkit-mask-image: radial-gradient(ellipse 86% 80% at 60% 24%, #000 24%, transparent 86%);
  mask-image: radial-gradient(ellipse 86% 80% at 60% 24%, #000 24%, transparent 86%);
}

.stu-atmos__flow {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
}

.stu-sidebar {
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

.stu-brand {
  display: flex;
  align-items: center;
  gap: 12px;
  color: var(--forest-deep);
}

.stu-brand:hover {
  text-decoration: none;
}

.stu-brand__logo {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: #fff;
  object-fit: contain;
  box-shadow: 0 6px 16px -10px rgba(20, 59, 48, .5);
}

.stu-brand__text strong {
  display: block;
  font-size: 21px;
  font-weight: 800;
  letter-spacing: -.02em;
  line-height: 1.2;
}

.stu-brand__text small {
  display: block;
  margin-top: 5px;
  color: #75877a;
  font-size: 11px;
  letter-spacing: 3px;
}

.stu-book {
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

.stu-book:hover {
  background: linear-gradient(135deg, #256450, #164439);
  box-shadow: 0 18px 30px -18px rgba(20, 61, 49, .9);
  transform: translateY(-1px);
}

.stu-book svg {
  width: 17px;
  height: 17px;
}

.stu-menu {
  display: flex;
  flex: 0 0 auto;
  flex-direction: column;
  order: 0;
  gap: 4px;
}

.stu-menu__item {
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

.stu-menu__item > svg:first-child {
  flex: 0 0 auto;
  width: 20px;
  height: 20px;
}

.stu-menu__item:hover {
  background: rgba(232, 240, 227, .85);
  color: var(--forest);
  text-decoration: none;
}

.stu-menu__item.is-active {
  border-color: rgba(28, 74, 59, .1);
  background: linear-gradient(135deg, #e2eddf, #f3f1e3);
  color: var(--forest);
  font-weight: 700;
  box-shadow: 0 8px 18px -16px rgba(20, 59, 48, .7);
}

.stu-menu__external {
  width: 14px;
  height: 14px;
  margin-left: auto;
  opacity: .45;
}

.stu-user {
  margin-top: auto;
  padding: 18px 6px 4px;
  border-top: 1px solid rgba(28, 74, 59, .1);
}

.stu-user__head {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 4px;
}

.stu-user__circle {
  display: grid;
  flex: 0 0 auto;
  place-items: center;
  width: 38px;
  height: 38px;
  border-radius: 50%;
  background: linear-gradient(135deg, #2f6b58, #1c4a3b);
  color: #f8f5e9;
  font-size: 15px;
  font-weight: 700;
}

.stu-user__meta {
  display: flex;
  flex-direction: column;
  min-width: 0;
  line-height: 1.35;
}

.stu-user__meta strong {
  overflow: hidden;
  font-size: 13.5px;
  font-weight: 700;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.stu-user__meta small {
  overflow: hidden;
  color: #8a998c;
  font-size: 11px;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.stu-user__logout {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 7px;
  width: 100%;
  min-height: 38px;
  margin-top: 10px;
  padding: 8px 12px;
  border: 1px solid rgba(28, 74, 59, .14);
  border-radius: 12px;
  background: rgba(255, 253, 247, .8);
  color: #5c7267;
  font-size: 12.5px;
  transition: background .2s ease, color .2s ease;
}

.stu-user__logout:hover {
  background: #eef3e6;
  color: var(--forest);
}

.stu-user__logout svg {
  width: 15px;
  height: 15px;
}

.stu-sidebar__foot {
  display: flex;
  gap: 8px;
  margin: 14px 0 0;
  padding: 0 4px;
  color: #9aa99c;
  font-size: 11px;
  letter-spacing: .08em;
}

.stu-sidebar__foot a {
  color: #7d9183;
}

.stu-sidebar__foot a:hover {
  color: var(--forest);
}

.stu-body {
  position: relative;
  z-index: 1;
  min-height: 100vh;
  margin-left: var(--sidebar-w);
}

.stu-topbar {
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

.stu-iconbtn.stu-topbar__menu {
  display: none;
}

.stu-crumbs {
  display: flex;
  flex: 1 1 auto;
  align-items: center;
  order: 0;
  gap: 7px;
  min-width: 0;
  color: #8a998c;
  font-size: 13px;
}

.stu-crumbs a {
  color: #75877a;
  white-space: nowrap;
}

.stu-crumbs a:hover {
  color: var(--forest);
  text-decoration: none;
}

.stu-crumbs svg {
  flex: 0 0 auto;
  width: 13px;
  height: 13px;
  opacity: .55;
}

.stu-crumbs span {
  overflow: hidden;
  color: var(--forest-deep);
  font-weight: 600;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.stu-search {
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

.stu-search:focus-within {
  border-color: rgba(28, 74, 59, .34);
  box-shadow: 0 0 0 3px rgba(143, 182, 161, .18);
}

.stu-search svg {
  flex: 0 0 auto;
  width: 17px;
  height: 17px;
  color: #8a998c;
}

.stu-search input {
  min-height: 40px;
  padding: 8px 0;
  border: 0;
  background: transparent;
  color: var(--forest-deep);
  font-size: 13px;
  outline: none;
}

.stu-search input:focus-visible {
  outline: none;
}

.stu-iconbtn {
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

.stu-iconbtn:hover {
  background: #eef3e6;
  color: var(--forest);
}

.stu-iconbtn svg {
  width: 19px;
  height: 19px;
}

.stu-bell__dot {
  position: absolute;
  top: 10px;
  right: 11px;
  width: 6px;
  height: 6px;
  border: 1.5px solid #fffdf7;
  border-radius: 50%;
  background: #b39a5f;
}

.stu-account {
  position: relative;
  flex: 0 0 auto;
}

.stu-avatar {
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

.stu-avatar:hover {
  background: #eef3e6;
}

.stu-avatar__circle {
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

.stu-avatar__meta {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  line-height: 1.3;
}

.stu-avatar__meta strong {
  font-size: 13px;
  font-weight: 700;
}

.stu-avatar__meta small {
  color: #8a998c;
  font-size: 11px;
}

.stu-avatar > svg {
  width: 14px;
  height: 14px;
  color: #8a998c;
}

.stu-account__menu {
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

.stu-account__menu a,
.stu-account__menu button {
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

.stu-account__menu a:hover,
.stu-account__menu button:hover {
  background: #eef3e6;
  color: var(--forest);
  text-decoration: none;
}

.stu-main {
  padding: 28px 30px 64px;
}

.stu-content {
  width: min(100%, 1160px);
  margin: 0 auto;
}

.stu-backdrop {
  position: fixed;
  inset: 0;
  z-index: 35;
  border: 0;
  border-radius: 0;
  background: rgba(20, 59, 48, .32);
  cursor: default;
}

@media (max-width: 1200px) {
  .stu-topbar {
    gap: 12px;
  }
  .stu-search {
    flex-basis: 210px;
  }
}

@media (max-width: 960px) {
  .stu-sidebar {
    transform: translateX(-100%);
    transition: transform .24s ease;
  }
  .stu-sidebar.is-open {
    transform: translateX(0);
    box-shadow: 24px 0 60px -30px rgba(20, 59, 48, .5);
  }
  .stu-body {
    margin-left: 0;
  }
  .stu-iconbtn.stu-topbar__menu {
    display: inline-grid;
  }
}

@media (max-width: 640px) {
  .stu-topbar {
    flex-wrap: wrap;
    gap: 10px 12px;
    padding: 12px 16px;
  }
  .stu-crumbs {
    order: 2;
    flex-basis: 100%;
  }
  .stu-search {
    flex: 1 1 auto;
    min-width: 0;
  }
  .stu-avatar__meta {
    display: none;
  }
  .stu-avatar > svg {
    display: none;
  }
  .stu-main {
    padding: 20px 16px 48px;
  }
}
</style>

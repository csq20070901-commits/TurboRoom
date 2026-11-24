<template>
  <div class="home-page">
    <div class="home-atmos" aria-hidden="true">
      <span class="home-atmos__wash"></span>
      <span class="home-atmos__grid"></span>
      <svg class="home-atmos__flow" viewBox="0 0 1200 1200" preserveAspectRatio="none" fill="none">
        <g stroke="#8fbaa4" stroke-width="1.2" stroke-linecap="round">
          <path d="M-40 150C190 96 330 200 560 148S960 50 1260 108" opacity=".34" />
          <path d="M-40 262C220 200 380 310 640 244S1020 140 1260 198" opacity=".2" />
          <path d="M-40 486C230 420 430 548 690 470S1080 356 1260 424" opacity=".16" />
          <path d="M-40 726C250 654 480 782 760 700S1100 590 1260 652" opacity=".13" />
          <path d="M-40 980C260 900 520 1028 840 944S1140 846 1280 902" opacity=".1" />
        </g>
        <g fill="#9ec4ae">
          <circle cx="352" cy="176" r="2.6" opacity=".28" />
          <circle cx="842" cy="392" r="2.2" opacity=".22" />
          <circle cx="512" cy="660" r="2.4" opacity=".2" />
          <circle cx="1012" cy="868" r="2" opacity=".18" />
        </g>
      </svg>
    </div>

    <div class="home-content">
      <section class="home-hero" aria-labelledby="home-hero-title">
        <div class="home-hero__photo" aria-hidden="true"></div>
        <div class="home-hero__glow" aria-hidden="true"></div>

        <div class="home-hero__body">
          <span class="home-hero__eyebrow">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
              <path d="M5 19c6.5-.4 11-4.4 13-13-8 .8-12.4 4.8-13 13Z" />
              <path d="M5 19c1.6-4 4.2-7 8-9" />
            </svg>
            MAKE ROOM FOR FOCUS
          </span>
          <h1 id="home-hero-title">找到你的空间，<br>开启一段专注时光。</h1>
          <p class="home-hero__desc">从图书馆到校园自修室，按自己的节奏安排学习。<br>选择场地、预约时段，让每一次自习都有计划。</p>
          <div class="home-hero__actions">
            <router-link class="home-cta" to="/meets">
              寻找自习场地 <span aria-hidden="true">&rarr;</span>
            </router-link>
            <span class="home-hero__hint">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.4" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                <path d="M12 3.6l1.7 5.1 5.1 1.7-5.1 1.7L12 17.2l-1.7-5.1-5.1-1.7 5.1-1.7L12 3.6Z" />
              </svg>
              安静 · 专注 · 有序
            </span>
          </div>
        </div>

        <div class="home-hero__art" aria-hidden="true">
          <div class="home-hero__paper">
            <svg class="home-hero__sketch" viewBox="0 0 220 150" fill="none">
              <path d="M110 52C94 42 72 38 52 41v66c20-3 42 1 58 11 16-10 38-14 58-11V41c-20-3-42 1-58 11Z" stroke="currentColor" stroke-width="1.6" stroke-linejoin="round" opacity=".85" />
              <path d="M110 52v66" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" opacity=".7" />
              <path d="M68 56c12-1 24 1 32 5M68 70c12-1 24 1 32 5M68 84c12-1 24 1 32 5" stroke="currentColor" stroke-width="1.2" stroke-linecap="round" opacity=".4" />
              <path d="M120 61c8-3 20-5 32-4M120 75c8-3 20-5 32-4M120 89c8-3 20-5 32-4" stroke="currentColor" stroke-width="1.2" stroke-linecap="round" opacity=".4" />
              <path d="M110 40V26" stroke="currentColor" stroke-width="1.4" stroke-linecap="round" opacity=".8" />
              <path d="M110 36c0-10 7-18 17-21-1 11-8 19-17 21Z" fill="#dfeade" stroke="currentColor" stroke-width="1.2" opacity=".9" />
              <path d="M110 36c-1-8-8-14-16-15 1 9 8 14 16 15Z" fill="#e9f0df" stroke="currentColor" stroke-width="1.2" opacity=".85" />
              <path d="M45 30l2.2 6.6L54 39l-6.8 2.4L45 48l-2.2-6.6L36 39l6.8-2.4L45 30Z" fill="#cbb27a" opacity=".55" />
              <path d="M176 98l1.8 5.2 5.2 1.8-5.2 1.8-1.8 5.2-1.8-5.2-5.2-1.8 5.2-1.8L176 98Z" fill="#9cbba6" opacity=".6" />
              <path d="M37 96c9 5 15 12 17 22" stroke="currentColor" stroke-width="1.1" stroke-linecap="round" stroke-dasharray="2 7" opacity=".35" />
              <path d="M184 30c-7 5-11 12-12 20" stroke="currentColor" stroke-width="1.1" stroke-linecap="round" stroke-dasharray="2 7" opacity=".35" />
            </svg>
            <span class="home-hero__tag">YOUR STUDY SPACE</span>
            <p class="home-hero__quote">留一点时间<br>给更好的自己。</p>
            <p class="home-hero__places">图书馆 / 教学楼 / 自修室</p>
          </div>
          <svg class="home-hero__leaf" viewBox="0 0 48 48" fill="none" aria-hidden="true">
            <path d="M8 41C22 34 33 22 40 7" stroke="currentColor" stroke-width="1.4" stroke-linecap="round" />
            <path d="M40 7c6-1 9-3 11-6-1 5-5 8-11 8Z" fill="#dbe8d7" stroke="currentColor" stroke-width="1" />
            <path d="M28 20c-5-1-9-4-10-8 5-1 9 2 10 8Z" fill="#e7efe0" stroke="currentColor" stroke-width="1" />
            <path d="M18 31c-4-1-7-3-9-7 4-1 8 2 9 7Z" fill="#eef2e6" stroke="currentColor" stroke-width="1" />
          </svg>
          <svg class="home-hero__star" viewBox="0 0 24 24" fill="none" aria-hidden="true">
            <path d="M12 4l1.6 5 5 1.6-5 1.6-1.6 5-1.6-5-5-1.6 5-1.6L12 4Z" stroke="currentColor" stroke-width="1.3" stroke-linejoin="round" />
          </svg>
        </div>
      </section>

      <div v-if="error" class="notice error" role="alert">
        {{ error }} <button @click="loadData">重试</button>
      </div>

      <section class="home-section home-section--rooms" aria-labelledby="home-rooms-title">
        <svg class="home-spark home-spark--rooms" viewBox="0 0 24 24" fill="none" aria-hidden="true">
          <path d="M12 4l1.6 5 5 1.6-5 1.6-1.6 5-1.6-5-5-1.6 5-1.6L12 4Z" stroke="currentColor" stroke-width="1.3" stroke-linejoin="round" />
        </svg>
        <div class="home-head">
          <div class="home-head__left">
            <span class="home-head__icon">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                <path d="M12 6.6C10.4 5.3 8.2 4.7 5.8 5v12.5c2.4-.3 4.6.3 6.2 1.6 1.6-1.3 3.8-1.9 6.2-1.6V5c-2.4-.3-4.6.3-6.2 1.6Z" />
                <path d="M12 6.6v12.5" />
              </svg>
            </span>
            <div class="home-head__text">
              <h2 id="home-rooms-title">发现自习场地</h2>
              <p>挑一处安静角落，让专注自然发生。</p>
            </div>
          </div>
          <router-link class="home-more" to="/meets">查看全部 <span aria-hidden="true">&rarr;</span></router-link>
        </div>
        <AppSkeleton v-if="loading" />
        <div v-else class="grid">
          <AppCard v-for="item in meetList" :key="item.meetId" :item="item" type="meet" />
        </div>
      </section>

      <div class="home-divider" aria-hidden="true">
        <span></span>
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.4" stroke-linecap="round" stroke-linejoin="round">
          <path d="M5 19c7-.5 11.5-4.6 13.5-13C10.5 6.8 6 10.9 5 19Z" />
          <path d="M5 19c1.8-4.5 4.6-7.7 8.5-10" />
        </svg>
        <span></span>
      </div>

      <section class="home-section home-section--news" aria-labelledby="home-news-title">
        <svg class="home-book-deco" viewBox="0 0 48 48" fill="none" aria-hidden="true">
          <path d="M24 15c-4-3-9-4-14-3v21c5-1 10 0 14 3 4-3 9-4 14-3V12c-5-1-10 0-14 3Z" stroke="currentColor" stroke-width="1.4" stroke-linejoin="round" />
          <path d="M24 15v21" stroke="currentColor" stroke-width="1.3" stroke-linecap="round" />
          <path d="M16 20c2 0 4 .2 5 .7M16 26c2 0 4 .2 5 .7M32 20c-2 0-4 .2-5 .7M32 26c-2 0-4 .2-5 .7" stroke="currentColor" stroke-width="1" stroke-linecap="round" opacity=".6" />
        </svg>
        <div class="home-head">
          <div class="home-head__left">
            <span class="home-head__icon">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                <path d="M4.5 10.2v3.6c0 .6.5 1.1 1.1 1.1h1.7l5.2 3.3V5.8L7.3 9.1H5.6c-.6 0-1.1.5-1.1 1.1Z" />
                <path d="M15.3 8.4a4.7 4.7 0 0 1 0 7.2" />
                <path d="M17.8 5.9a8.2 8.2 0 0 1 0 12.2" />
              </svg>
            </span>
            <div class="home-head__text">
              <h2 id="home-news-title">校园公告</h2>
              <p>校园里的新鲜事，及时了解。</p>
            </div>
          </div>
          <router-link class="home-more" to="/news-list">全部公告 <span aria-hidden="true">&rarr;</span></router-link>
        </div>
        <div class="grid">
          <AppCard v-for="item in newsList" :key="item.newsId" :item="item" type="news" />
        </div>
        <div v-if="!loading && !newsList.length" class="empty">暂无公告</div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { api } from '../../api/request'
import AppCard from '../../components/AppCard.vue'
import AppSkeleton from '../../components/AppSkeleton.vue'

const loading = ref(true)
const error = ref('')
const meetList = ref([])
const newsList = ref([])

async function loadData() {
  loading.value = true
  error.value = ''
  try {
    const data = await api('/home/list') || {}
    meetList.value = data.meetList || []
  } catch (e) {
    error.value = e.message
  }
  try {
    const newsData = await api('/news/list', { page: 1, size: 6, isTotal: false })
    newsList.value = newsData.list || []
  } catch {}
  finally { loading.value = false }
}

onMounted(loadData)
</script>

<style scoped>
.home-page {
  position: relative;
  min-height: 100vh;
  --forest: #1c4a3b;
  --forest-deep: #143b30;
  --cream: #fbf7ec;
  --khaki: #d9cfb4;
  --sage: #8fb6a1;
}

.home-atmos {
  position: absolute;
  inset: 0;
  z-index: 0;
  pointer-events: none;
}

.home-atmos__wash {
  position: absolute;
  inset: -40px 0 0;
  background:
    radial-gradient(58% 34% at 10% 3%, rgba(206, 228, 211, .62), transparent 70%),
    radial-gradient(44% 28% at 94% 12%, rgba(240, 232, 209, .55), transparent 72%),
    radial-gradient(50% 30% at 50% 100%, rgba(228, 238, 225, .5), transparent 75%);
}

.home-atmos__grid {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(rgba(28, 74, 59, .055) 1px, transparent 1px),
    linear-gradient(90deg, rgba(28, 74, 59, .055) 1px, transparent 1px);
  background-size: 38px 38px;
  opacity: .8;
  -webkit-mask-image: radial-gradient(ellipse 82% 74% at 50% 30%, #000 20%, transparent 82%);
  mask-image: radial-gradient(ellipse 82% 74% at 50% 30%, #000 20%, transparent 82%);
}

.home-atmos__flow {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  opacity: .85;
}

.home-content {
  position: relative;
  z-index: 1;
}

.home-hero {
  position: relative;
  display: grid;
  grid-template-columns: minmax(0, 1.12fr) minmax(300px, .88fr);
  align-items: center;
  gap: 40px;
  margin-bottom: 34px;
  padding: 52px 56px;
  border: 1px solid rgba(28, 74, 59, .14);
  border-radius: 30px;
  overflow: hidden;
  isolation: isolate;
  background: #eef3e9;
  box-shadow: 0 30px 60px -48px rgba(20, 59, 48, .65);
}

.home-hero__photo {
  position: absolute;
  inset: -18px;
  z-index: -3;
  background: url('/hero-campus.jpg?v=2') center 42% / cover no-repeat;
  filter: blur(3.2px) saturate(.82) brightness(1.04);
  transform: scale(1.045);
}

.home-hero__glow {
  position: absolute;
  inset: 0;
  z-index: -2;
  background:
    radial-gradient(ellipse 34% 52% at 74% 22%, rgba(255, 255, 255, .72), transparent 70%),
    radial-gradient(ellipse 42% 46% at 96% 88%, rgba(214, 232, 216, .5), transparent 72%),
    linear-gradient(96deg, rgba(252, 249, 241, .95) 0%, rgba(250, 247, 238, .86) 38%, rgba(242, 247, 238, .55) 68%, rgba(236, 244, 237, .38) 100%);
  box-shadow: inset 0 0 110px 24px rgba(255, 250, 240, .5);
}

.home-hero::after {
  content: '';
  position: absolute;
  inset: 0;
  z-index: -1;
  pointer-events: none;
  background: linear-gradient(115deg, rgba(255, 255, 255, 0) 42%, rgba(255, 255, 255, .26) 50%, rgba(255, 255, 255, 0) 58%);
}

.home-hero__body {
  max-width: 560px;
}

.home-hero__eyebrow {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 7px 14px;
  border: 1px solid rgba(28, 74, 59, .16);
  border-radius: 999px;
  background: rgba(255, 252, 244, .62);
  color: #2f6b58;
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 2.6px;
}

.home-hero__eyebrow svg {
  width: 14px;
  height: 14px;
}

.home-hero h1 {
  margin: 18px 0 16px;
  color: var(--forest-deep);
  font-size: clamp(34px, 3.6vw, 48px);
  font-weight: 800;
  line-height: 1.26;
  letter-spacing: -1.2px;
}

.home-hero__desc {
  margin: 0 0 28px;
  color: #4d6a5d;
  font-size: 16px;
  line-height: 1.85;
}

.home-hero__actions {
  display: flex;
  align-items: center;
  gap: 18px;
  flex-wrap: wrap;
}

.home-cta {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  padding: 14px 26px;
  border-radius: 999px;
  background: linear-gradient(135deg, #215c48, #143d31);
  color: #faf6ea;
  font-weight: 600;
  letter-spacing: .02em;
  box-shadow: 0 16px 30px -18px rgba(20, 61, 49, .9), inset 0 1px 0 rgba(255, 255, 255, .16);
  transition: transform .25s ease, box-shadow .25s ease, background .25s ease;
}

.home-cta:hover {
  color: #fffdf6;
  text-decoration: none;
  background: linear-gradient(135deg, #256450, #164439);
  box-shadow: 0 20px 34px -18px rgba(20, 61, 49, .9), inset 0 1px 0 rgba(255, 255, 255, .2);
  transform: translateY(-2px);
}

.home-cta span {
  transition: transform .25s ease;
}

.home-cta:hover span {
  transform: translateX(3px);
}

.home-hero__hint {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  color: #7a8779;
  font-size: 13px;
  letter-spacing: .08em;
}

.home-hero__hint svg {
  width: 16px;
  height: 16px;
  color: #a08d58;
}

.home-hero__art {
  position: relative;
  display: flex;
  justify-content: flex-end;
}

.home-hero__paper {
  position: relative;
  width: min(100%, 320px);
  padding: 26px 28px 24px;
  border: 1px solid rgba(28, 74, 59, .16);
  border-radius: 24px;
  background: linear-gradient(165deg, rgba(255, 253, 246, .78), rgba(247, 243, 232, .55));
  -webkit-backdrop-filter: blur(10px);
  backdrop-filter: blur(10px);
  box-shadow: 0 26px 50px -36px rgba(20, 59, 48, .65), inset 0 1px 0 rgba(255, 255, 255, .7);
  transform: rotate(-1.2deg);
}

.home-hero__paper::before {
  content: '';
  position: absolute;
  inset: 8px;
  border: 1px dashed rgba(28, 74, 59, .12);
  border-radius: 18px;
  pointer-events: none;
}

.home-hero__sketch {
  display: block;
  width: 170px;
  height: auto;
  margin: 0 auto 6px;
  color: #2f6b58;
}

.home-hero__tag {
  display: block;
  color: #8c7a4f;
  font-size: 10px;
  font-weight: 700;
  letter-spacing: 2.6px;
  text-align: center;
}

.home-hero__quote {
  margin: 10px 0 8px;
  color: #244d3f;
  font-size: 23px;
  font-weight: 700;
  line-height: 1.5;
  letter-spacing: .01em;
  text-align: center;
}

.home-hero__places {
  margin: 0;
  color: #7d8b7c;
  font-size: 12.5px;
  letter-spacing: .14em;
  text-align: center;
}

.home-hero__leaf {
  position: absolute;
  right: 4px;
  bottom: -12px;
  width: 44px;
  height: 44px;
  color: #4d7a62;
  opacity: .55;
  transform: rotate(18deg);
}

.home-hero__star {
  position: absolute;
  top: -6px;
  right: -4px;
  width: 20px;
  height: 20px;
  color: #b39a5f;
  opacity: .65;
}

.home-page .notice.error {
  margin: 0 0 22px;
  border-radius: 14px;
  background: #fff8f1;
}

.home-section {
  position: relative;
  padding: 26px 28px 30px;
  border: 1px solid rgba(28, 74, 59, .1);
  border-radius: 28px;
  box-shadow: 0 26px 56px -46px rgba(20, 59, 48, .5);
}

.home-section--rooms {
  background: linear-gradient(132deg, rgba(255, 252, 243, .94) 0%, rgba(233, 242, 233, .86) 52%, rgba(249, 245, 233, .92) 100%);
}

.home-section--news {
  background: linear-gradient(132deg, rgba(250, 246, 235, .94) 0%, rgba(238, 243, 231, .88) 48%, rgba(255, 253, 246, .94) 100%);
}

.home-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
  flex-wrap: wrap;
  margin: 0 0 22px;
}

.home-head__left {
  display: flex;
  align-items: center;
  gap: 14px;
  min-width: 0;
}

.home-head__icon {
  flex: 0 0 auto;
  display: grid;
  place-items: center;
  width: 46px;
  height: 46px;
  border: 1px solid rgba(28, 74, 59, .14);
  border-radius: 14px;
  background: linear-gradient(150deg, #fffdf6, #eaf2e6);
  color: #2f6b58;
  box-shadow: 0 8px 18px -14px rgba(20, 59, 48, .6), inset 0 1px 0 rgba(255, 255, 255, .8);
}

.home-head__icon svg {
  width: 22px;
  height: 22px;
}

.home-head h2 {
  margin: 0;
  color: #1b4335;
  font-size: 22px;
  line-height: 1.4;
  letter-spacing: -.01em;
}

.home-head__text p {
  margin: 3px 0 0;
  color: #7a887a;
  font-size: 13px;
  letter-spacing: .02em;
}

.home-more {
  display: inline-flex;
  align-items: center;
  gap: 7px;
  padding: 9px 16px;
  border: 1px solid rgba(28, 74, 59, .16);
  border-radius: 999px;
  background: rgba(255, 253, 246, .7);
  color: #2f6b58;
  font-size: 13.5px;
  font-weight: 600;
  white-space: nowrap;
  transition: background .2s ease, border-color .2s ease, transform .2s ease;
}

.home-more:hover {
  border-color: rgba(28, 74, 59, .28);
  background: #fffdf7;
  text-decoration: none;
  transform: translateY(-1px);
}

.home-more span {
  transition: transform .2s ease;
}

.home-more:hover span {
  transform: translateX(2px);
}

.home-divider {
  display: flex;
  align-items: center;
  gap: 16px;
  margin: 24px 4px;
}

.home-divider span {
  flex: 1;
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(47, 107, 88, .24) 30%, rgba(176, 158, 112, .28) 70%, transparent);
}

.home-divider svg {
  flex: 0 0 auto;
  width: 18px;
  height: 18px;
  color: #7f9c86;
}

.home-spark {
  position: absolute;
  z-index: 0;
  color: #b29a61;
  opacity: .55;
  pointer-events: none;
}

.home-spark--rooms {
  top: -13px;
  right: 32px;
  width: 18px;
  height: 18px;
  transform: rotate(12deg);
}

.home-book-deco {
  position: absolute;
  right: 24px;
  bottom: 12px;
  z-index: 0;
  width: 42px;
  height: 42px;
  color: #4f7a62;
  opacity: .28;
  pointer-events: none;
}

.home-head,
.home-section :deep(.grid),
.home-section :deep(.skeleton-grid),
.home-section .empty {
  position: relative;
  z-index: 1;
}

.home-page :deep(.card) {
  position: relative;
  border: 1px solid rgba(28, 74, 59, .12);
  border-radius: 22px;
  background: linear-gradient(180deg, #fffdf8 0%, #fbfaf3 100%);
  box-shadow: 0 22px 40px -36px rgba(20, 59, 48, .55), 0 2px 6px rgba(20, 59, 48, .04);
}

.home-page :deep(.card:hover) {
  transform: translateY(-4px);
  box-shadow: 0 28px 46px -34px rgba(20, 59, 48, .5), 0 4px 12px rgba(20, 59, 48, .06);
}

.home-page :deep(.card .cover),
.home-page :deep(.card .cover-placeholder) {
  width: calc(100% - 20px);
  margin: 10px 10px 0;
  border-radius: 15px;
  aspect-ratio: 16 / 10;
}

.home-page :deep(.card .cover-placeholder) {
  background: linear-gradient(140deg, #eef3e6, #f6f1e2);
  color: #577c67;
  font-size: 18px;
  letter-spacing: 4px;
}

.home-page :deep(.card .card-body) {
  padding: 20px 22px 24px;
}

.home-page :deep(.card .chip) {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 11px;
  border: 1px solid rgba(28, 74, 59, .1);
  border-radius: 999px;
  background: rgba(232, 240, 227, .9);
  color: #466b56;
  font-size: 12px;
  letter-spacing: .04em;
}

.home-page :deep(.card h3) {
  margin: 10px 0 8px;
  color: #173f33;
  font-size: 19px;
  line-height: 1.45;
  letter-spacing: -.01em;
}

.home-page :deep(.card p.muted) {
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
  margin: 0 0 18px;
  color: #66786b;
  font-size: 14px;
  line-height: 1.75;
}

.home-page :deep(.card .specular-button) {
  padding: 12px 20px;
  background: linear-gradient(135deg, #f7f3e7, #eef1e2);
  color: #1f4a3a !important;
  font-size: 14px;
  box-shadow: 0 8px 16px -12px rgba(20, 59, 48, .55);
}

.home-page :deep(.card .specular-button__border) {
  border-color: rgba(31, 74, 58, .22);
}

.home-page :deep(.card .specular-button:hover) {
  background: linear-gradient(135deg, #fbf7eb, #f2f4e8);
  box-shadow: 0 12px 20px -12px rgba(20, 59, 48, .5);
}

.home-page :deep(.card)::before,
.home-page :deep(.card)::after {
  content: '';
  position: absolute;
  z-index: 3;
  width: 46px;
  height: 46px;
  pointer-events: none;
  background: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 64 64' fill='none'%3E%3Cpath d='M8 58C24 48 38 32 50 10' stroke='%232f6b58' stroke-width='1.3' stroke-linecap='round' opacity='.55'/%3E%3Cpath d='M50 10c8-2 12-5 14-9-1 7-6 11-14 11z' fill='%23dbe8d7' stroke='%232f6b58' stroke-width='1.1' opacity='.9'/%3E%3Cpath d='M38 26c-7-1-12-5-14-10 7-1 12 3 14 10z' fill='%23e6efe1' stroke='%232f6b58' stroke-width='1.1' opacity='.85'/%3E%3Cpath d='M26 40c-6-1-10-4-12-9 6-1 10 2 12 9z' fill='%23eef2e7' stroke='%232f6b58' stroke-width='1.1' opacity='.8'/%3E%3Cpath d='M44 16c6-1 10-4 12-8-6-1-10 2-12 8z' fill='%23e6efe1' stroke='%232f6b58' stroke-width='1.1' opacity='.85'/%3E%3C/svg%3E") center / contain no-repeat;
  transition: opacity .3s ease, transform .3s ease;
}

.home-page :deep(.card)::before {
  top: 13px;
  right: 12px;
  opacity: .7;
  transform: rotate(-10deg);
}

.home-page :deep(.card)::after {
  right: 10px;
  bottom: 10px;
  width: 38px;
  height: 38px;
  opacity: .4;
  transform: rotate(166deg);
}

.home-page :deep(.card:hover)::before {
  opacity: .92;
  transform: rotate(-4deg) translateY(-1px);
}

.home-page :deep(.card:hover)::after {
  opacity: .55;
}

.home-page :deep(.skeleton-card) {
  border-color: rgba(28, 74, 59, .1);
  border-radius: 22px;
  background: rgba(255, 253, 248, .9);
}

.home-page :deep(.skeleton-cover) {
  width: calc(100% - 20px);
  margin: 10px 10px 0;
  border-radius: 15px;
}

.home-section .empty {
  border-color: rgba(28, 74, 59, .18);
  border-radius: 18px;
  background: rgba(255, 253, 246, .5);
  color: #7a887a;
}

@media (max-width: 1024px) {
  .home-hero {
    grid-template-columns: minmax(0, 1fr) minmax(260px, .8fr);
    gap: 28px;
    padding: 42px 40px;
  }

  .home-hero h1 {
    font-size: 38px;
  }
}

@media (max-width: 900px) {
  .home-hero {
    grid-template-columns: 1fr;
    padding: 40px 34px;
  }

  .home-hero__art {
    display: none;
  }

  .home-hero__body {
    max-width: 640px;
  }

  .home-section {
    padding: 24px 22px 26px;
    border-radius: 24px;
  }
}

@media (max-width: 600px) {
  .home-hero {
    padding: 32px 22px 34px;
    border-radius: 24px;
  }

  .home-hero h1 {
    margin: 16px 0 14px;
    font-size: 30px;
    letter-spacing: -.6px;
  }

  .home-hero__desc {
    margin-bottom: 22px;
    font-size: 14.5px;
    line-height: 1.8;
  }

  .home-hero__eyebrow {
    padding: 6px 12px;
    font-size: 10px;
    letter-spacing: 2px;
  }

  .home-cta {
    width: 100%;
    justify-content: center;
  }

  .home-hero__actions {
    gap: 14px;
  }

  .home-hero__hint {
    font-size: 12px;
  }

  .home-section {
    padding: 20px 16px 22px;
    border-radius: 20px;
  }

  .home-head {
    gap: 14px;
  }

  .home-head__icon {
    width: 40px;
    height: 40px;
    border-radius: 12px;
  }

  .home-head__icon svg {
    width: 19px;
    height: 19px;
  }

  .home-head h2 {
    font-size: 19px;
  }

  .home-head__text p {
    display: none;
  }

  .home-more {
    padding: 8px 13px;
    font-size: 12.5px;
  }

  .home-divider {
    margin: 18px 0;
  }

  .home-spark--rooms {
    right: 18px;
  }

  .home-book-deco {
    display: none;
  }

  .home-page :deep(.card .card-body) {
    padding: 18px 18px 20px;
  }

  .home-page :deep(.card h3) {
    font-size: 18px;
  }
}
</style>

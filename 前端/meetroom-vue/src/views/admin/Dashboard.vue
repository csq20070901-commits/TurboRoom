<template>
  <div class="dash">
    <section class="dash-hero" aria-labelledby="dash-welcome">
      <div class="dash-hero__text">
        <span class="dash-hero__eyebrow">TURBOROOM ADMIN</span>
        <h1 id="dash-welcome">你好，管理员，<br>欢迎来到 TurboRoom 自习管理后台</h1>
        <p>管理自习场地、审核预约订单、发布校园公告、查看预约数据</p>
      </div>
      <div class="dash-hero__robot" aria-hidden="true">
        <span class="dash-hero__halo"></span>
        <img src="/admin-robot.png" alt="">
        <span class="dash-hero__shadow"></span>
      </div>
    </section>

    <div class="dash-actions" aria-label="快捷功能">
      <button v-for="action in quickActions" :key="action.label" class="dash-action" type="button" @click="action.run">
        <span class="dash-action__icon">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
            <path :d="action.icon" />
          </svg>
        </span>
        <span>{{ action.label }}</span>
        <svg class="dash-action__arrow" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
          <path d="M5 12h14m-6-6 6 6-6 6" />
        </svg>
      </button>
    </div>

    <section class="dash-cards" aria-label="管理功能">
      <button v-for="card in cards" :key="card.title" class="dash-card" type="button" @click="card.run">
        <span class="dash-card__top">
          <span class="dash-card__icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
              <path :d="card.icon" />
            </svg>
          </span>
          <span class="dash-card__arrow" aria-hidden="true">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round">
              <path d="M6 18 18 6M8 6h10v10" />
            </svg>
          </span>
        </span>
        <span class="dash-card__body">
          <h2>{{ card.title }}</h2>
          <p>{{ card.desc }}</p>
        </span>
        <span v-if="card.stat && statMap[card.stat] !== undefined" class="dash-card__stat">
          <strong>{{ statMap[card.stat] }}</strong>{{ card.statLabel }}
        </span>
      </button>
    </section>

    <section v-if="stats.length" ref="statsRef" class="dash-stats" aria-label="数据概览">
      <article v-for="item in stats" :key="item.title" class="dash-stat">
        <span class="dash-stat__label">{{ item.title }}</span>
        <strong>{{ item.cnt }}</strong>
        <small>{{ statCaption(item.title) }}</small>
      </article>
    </section>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { api } from '../../api/request'

const router = useRouter()
const stats = ref([])
const statsRef = ref(null)

const quickActions = [
  {
    label: '新增自习场地',
    icon: 'M12 5v14M5 12h14',
    run: () => router.push('/admin/edit?type=meet')
  },
  {
    label: '预约审核',
    icon: 'M7 3v3M17 3v3M4.5 8.5h15M5.5 5h13A1.5 1.5 0 0 1 20 6.5v12a1.5 1.5 0 0 1-1.5 1.5h-13A1.5 1.5 0 0 1 4 18.5v-12A1.5 1.5 0 0 1 5.5 5ZM9 14l2 2 4-4',
    run: () => router.push('/admin/orders')
  },
  {
    label: '发布公告',
    icon: 'M14 5l5 5M4 20l4-.8L19 8a2.1 2.1 0 0 0-3-3L5 16l-1 4Z',
    run: () => router.push('/admin/edit?type=news')
  },
  {
    label: '导出预约报表',
    icon: 'M12 4v11M7.5 11l4.5 4.5L16.5 11M5 19h14',
    run: () => router.push('/admin/orders?export=1')
  }
]

const cards = [
  {
    title: '场地管理',
    desc: '维护场地信息、开放日期与每时段容量，让自习资源清晰可用。',
    stat: '预约项目数',
    statLabel: '个开放场地',
    icon: 'M12 6.6C10.4 5.3 8.2 4.7 5.8 5v12.5c2.4-.3 4.6.3 6.2 1.6 1.6-1.3 3.8-1.9 6.2-1.6V5c-2.4-.3-4.6.3-6.2 1.6ZM12 6.6v12.5',
    run: () => router.push('/admin/meets')
  },
  {
    title: '预约审核',
    desc: '查看预约名单，处理订单状态，完成到场签到与扫码核销。',
    statLabel: '',
    icon: 'M7 3v3M17 3v3M4.5 8.5h15M5.5 5h13A1.5 1.5 0 0 1 20 6.5v12a1.5 1.5 0 0 1-1.5 1.5h-13A1.5 1.5 0 0 1 4 18.5v-12A1.5 1.5 0 0 1 5.5 5ZM9 14l2 2 4-4',
    run: () => router.push('/admin/orders')
  },
  {
    title: '公告管理',
    desc: '发布校园公告，同步自习安排、开放通知与文明使用倡议。',
    stat: '文章数',
    statLabel: '篇校园公告',
    icon: 'M4.5 10.2v3.6c0 .6.5 1.1 1.1 1.1h1.7l5.2 3.3V5.8L7.3 9.1H5.6c-.6 0-1.1.5-1.1 1.1ZM15.3 8.4a4.7 4.7 0 0 1 0 7.2M17.8 5.9a8.2 8.2 0 0 1 0 12.2',
    run: () => router.push('/admin/news')
  },
  {
    title: '数据看板',
    desc: '汇总用户、场地与公告数据，快速掌握校园自习运营情况。',
    statLabel: '',
    icon: 'M4 4v15a1 1 0 0 0 1 1h15M8.5 15v-4M13 15V8M17.5 15v-6',
    run: () => statsRef.value?.scrollIntoView({ behavior: 'smooth', block: 'start' })
  }
]

const statMap = computed(() => Object.fromEntries(stats.value.map((item) => [item.title, item.cnt])))

function statCaption(title) {
  if (title === '用户数') return '已注册学生'
  if (title === '文章数') return '已发布公告'
  if (title === '预约项目数') return '开放预约场地'
  return '实时统计数据'
}

onMounted(async () => {
  try {
    const r = await api('/admin/home')
    stats.value = r || []
  } catch {}
})
</script>

<style scoped>
.dash {
  display: flex;
  flex-direction: column;
  gap: 26px;
}

.dash-hero {
  position: relative;
  min-height: 236px;
  padding: 38px 300px 38px 40px;
  overflow: hidden;
  border: 1px solid rgba(28, 74, 59, .13);
  border-radius: 28px;
  background:
    radial-gradient(ellipse 42% 90% at 92% 12%, rgba(214, 232, 216, .85), transparent 70%),
    linear-gradient(118deg, rgba(255, 252, 243, .96) 0%, rgba(237, 244, 233, .9) 56%, rgba(229, 240, 228, .92) 100%);
  box-shadow: 0 28px 56px -46px rgba(20, 59, 48, .55);
}

.dash-hero::after {
  content: '';
  position: absolute;
  inset: 0;
  pointer-events: none;
  background: linear-gradient(115deg, rgba(255, 255, 255, 0) 42%, rgba(255, 255, 255, .3) 50%, rgba(255, 255, 255, 0) 58%);
}

.dash-hero__eyebrow {
  display: inline-flex;
  align-items: center;
  padding: 6px 13px;
  border: 1px solid rgba(28, 74, 59, .15);
  border-radius: 999px;
  background: rgba(255, 253, 246, .65);
  color: #6f856f;
  font-size: 10.5px;
  font-weight: 700;
  letter-spacing: 2.6px;
}

.dash-hero h1 {
  margin: 16px 0 12px;
  color: #143b30;
  font-size: clamp(24px, 2.4vw, 32px);
  font-weight: 800;
  line-height: 1.42;
  letter-spacing: -.5px;
}

.dash-hero p {
  margin: 0;
  color: #63796a;
  font-size: 14.5px;
  line-height: 1.8;
}

.dash-hero__robot {
  position: absolute;
  top: 50%;
  right: 46px;
  width: 196px;
  height: 196px;
  transform: translateY(-50%);
}

.dash-hero__halo {
  position: absolute;
  inset: -30px -34px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(255, 255, 255, .85) 0%, rgba(199, 224, 199, .45) 46%, transparent 72%);
}

.dash-hero__robot img {
  position: relative;
  width: 100%;
  height: 100%;
  object-fit: contain;
  mix-blend-mode: multiply;
  -webkit-mask-image: radial-gradient(ellipse 58% 56% at 50% 48%, #000 64%, transparent 88%);
  mask-image: radial-gradient(ellipse 58% 56% at 50% 48%, #000 64%, transparent 88%);
  animation: robot-float 5.5s ease-in-out infinite;
}

.dash-hero__shadow {
  position: absolute;
  right: 16%;
  bottom: -16px;
  left: 16%;
  height: 16px;
  border-radius: 50%;
  background: radial-gradient(ellipse, rgba(20, 59, 48, .2), transparent 70%);
}

@keyframes robot-float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-9px); }
}

.dash-actions {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 16px;
}

.dash-action {
  display: flex;
  align-items: center;
  gap: 11px;
  min-width: 0;
  padding: 13px 16px;
  border: 1px solid rgba(28, 74, 59, .13);
  border-radius: 17px;
  background: rgba(255, 253, 247, .92);
  color: #2b5444;
  font-size: 14px;
  font-weight: 600;
  box-shadow: 0 16px 30px -28px rgba(20, 59, 48, .55);
  transition: transform .2s ease, border-color .2s ease, background .2s ease;
}

.dash-action:hover {
  border-color: rgba(28, 74, 59, .28);
  background: #fffdf7;
  transform: translateY(-2px);
}

.dash-action__icon {
  display: grid;
  flex: 0 0 auto;
  place-items: center;
  width: 34px;
  height: 34px;
  border-radius: 11px;
  background: linear-gradient(150deg, #eef3e6, #e3edde);
  color: #2f6b58;
}

.dash-action__icon svg {
  width: 18px;
  height: 18px;
}

.dash-action > span:nth-child(2) {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.dash-action__arrow {
  flex: 0 0 auto;
  width: 15px;
  height: 15px;
  margin-left: auto;
  color: #9aab97;
  transition: transform .2s ease;
}

.dash-action:hover .dash-action__arrow {
  transform: translateX(3px);
}

.dash-cards {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 18px;
}

.dash-card {
  display: flex;
  flex-direction: column;
  align-items: stretch;
  gap: 18px;
  min-width: 0;
  padding: 22px;
  border: 1px solid rgba(28, 74, 59, .12);
  border-radius: 22px;
  background: linear-gradient(180deg, #fffdf8, #fafaf1);
  color: #143b30;
  text-align: left;
  box-shadow: 0 22px 40px -36px rgba(20, 59, 48, .5);
  transition: transform .22s ease, box-shadow .22s ease, border-color .22s ease;
}

.dash-card:hover {
  border-color: rgba(28, 74, 59, .26);
  background: linear-gradient(180deg, #fffef9, #f6f8ee);
  box-shadow: 0 28px 46px -36px rgba(20, 59, 48, .55);
  transform: translateY(-4px);
}

.dash-card__top {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.dash-card__icon {
  display: grid;
  place-items: center;
  width: 46px;
  height: 46px;
  border: 1px solid rgba(28, 74, 59, .12);
  border-radius: 15px;
  background: linear-gradient(150deg, #fffdf6, #e7f0e2);
  color: #2f6b58;
}

.dash-card__icon svg {
  width: 22px;
  height: 22px;
}

.dash-card__arrow {
  display: grid;
  place-items: center;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background: rgba(232, 240, 227, .8);
  color: #6d8a74;
  transition: transform .22s ease, background .22s ease;
}

.dash-card__arrow svg {
  width: 15px;
  height: 15px;
}

.dash-card:hover .dash-card__arrow {
  background: #dce9d6;
  transform: translate(2px, -2px);
}

.dash-card__body h2 {
  margin: 0 0 8px;
  color: #173f33;
  font-size: 19px;
  letter-spacing: -.01em;
}

.dash-card__body p {
  margin: 0;
  color: #6c7f70;
  font-size: 13px;
  line-height: 1.8;
}

.dash-card__stat {
  display: flex;
  align-items: baseline;
  gap: 6px;
  margin-top: auto;
  padding-top: 14px;
  border-top: 1px dashed rgba(28, 74, 59, .14);
  color: #8a998c;
  font-size: 12px;
}

.dash-card__stat strong {
  color: #1c4a3b;
  font-size: 24px;
  font-variant-numeric: tabular-nums;
  line-height: 1;
}

.dash-stats {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 18px;
  scroll-margin-top: 100px;
}

.dash-stat {
  padding: 22px 26px;
  border: 1px solid rgba(28, 74, 59, .11);
  border-radius: 20px;
  background: rgba(255, 253, 247, .85);
}

.dash-stat__label {
  display: block;
  color: #75877a;
  font-size: 13px;
}

.dash-stat strong {
  display: block;
  margin: 6px 0 2px;
  color: #143b30;
  font-size: 36px;
  font-variant-numeric: tabular-nums;
  line-height: 1.2;
}

.dash-stat small {
  color: #93a296;
  font-size: 12px;
}

@media (max-width: 1200px) {
  .dash-hero {
    padding-right: 250px;
  }

  .dash-hero__robot {
    right: 30px;
    width: 170px;
    height: 170px;
  }

  .dash-actions {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .dash-cards {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 720px) {
  .dash {
    gap: 20px;
  }

  .dash-hero {
    min-height: 0;
    padding: 28px 24px;
    border-radius: 22px;
  }

  .dash-hero h1 {
    font-size: 24px;
  }

  .dash-hero p {
    font-size: 13.5px;
  }

  .dash-hero__robot {
    display: none;
  }

  .dash-actions {
    gap: 12px;
  }

  .dash-action {
    padding: 12px;
    font-size: 13px;
  }

  .dash-action__arrow {
    display: none;
  }

  .dash-cards {
    grid-template-columns: 1fr;
    gap: 14px;
  }

  .dash-stats {
    grid-template-columns: 1fr;
    gap: 14px;
  }
}
</style>

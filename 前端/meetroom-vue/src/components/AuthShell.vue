<template>
  <div class="auth-page">
    <div class="auth-bg" aria-hidden="true">
      <span class="auth-blob auth-blob--1" />
      <span class="auth-blob auth-blob--2" />
      <span class="auth-blob auth-blob--3" />
      <span class="auth-waves" />
    </div>

    <div class="auth-shell">
      <section class="auth-art" aria-hidden="true">
        <div class="auth-brand">
          <img class="auth-brand__logo" src="/logo.png" alt="">
          <div>
            <strong>TurboRoom</strong>
            <span>校园自习预约</span>
          </div>
        </div>
        <h1 class="auth-headline">智慧自习室预约平台</h1>
        <p class="auth-subline">线上选座、预约签到、高效自习</p>

        <div class="auth-stage">
          <span class="auth-stage__glow" />
          <span class="auth-ring auth-ring--outer" />
          <span class="auth-ring auth-ring--inner" />
          <img class="auth-stage__img" src="/auth-hero.png?v=1" alt="">
          <div class="auth-float auth-float--study">
            <span class="auth-float__icon">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round">
                <path d="M4 5.5A1.5 1.5 0 0 1 5.5 4H11v16H5.5A1.5 1.5 0 0 1 4 18.5z" />
                <path d="M20 5.5A1.5 1.5 0 0 0 18.5 4H13v16h5.5a1.5 1.5 0 0 0 1.5-1.5z" />
              </svg>
            </span>
            <span class="auth-float__text"><strong>自习空间</strong><small>安静 · 专注</small></span>
          </div>
          <div class="auth-float auth-float--seat">
            <span class="auth-float__icon">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round">
                <rect x="4" y="4.5" width="6" height="6" rx="1.4" />
                <rect x="14" y="4.5" width="6" height="6" rx="1.4" />
                <rect x="4" y="13.5" width="6" height="6" rx="1.4" />
                <rect x="14" y="13.5" width="6" height="6" rx="1.4" fill="currentColor" fill-opacity="0.16" />
              </svg>
            </span>
            <span class="auth-float__text"><strong>在线选座</strong><small>实时余位</small></span>
          </div>
          <div class="auth-float auth-float--calendar">
            <span class="auth-float__icon">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round">
                <rect x="4" y="5.5" width="16" height="14" rx="2" />
                <path d="M4 10h16M8 4v3M16 4v3" />
                <path d="M8.5 14h2M13.5 14h2M8.5 16.5h2" />
              </svg>
            </span>
            <span class="auth-float__text"><strong>预约签到</strong><small>扫码入场</small></span>
          </div>
        </div>
      </section>

      <section class="auth-side">
        <div class="auth-mobile-brand">
          <img src="/logo.png" alt="">
          <strong>智慧自习室预约平台</strong>
        </div>

        <div class="auth-card">
          <div class="auth-tabs" role="group" aria-label="账户类型">
            <router-link to="/login" class="auth-tab" :class="{ 'auth-tab--active': activeTab === 'cust' }" :aria-current="activeTab === 'cust' ? 'page' : undefined">普通用户</router-link>
            <router-link to="/admin/login" class="auth-tab" :class="{ 'auth-tab--active': activeTab === 'admin' }" :aria-current="activeTab === 'admin' ? 'page' : undefined">管理员</router-link>
          </div>
          <slot />
        </div>

        <p class="auth-foot">TurboRoom · 为专注留出空间</p>
      </section>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const activeTab = computed(() => (route.name === 'AdminLogin' ? 'admin' : 'cust'))
</script>

<style>
.auth-page {
  position: fixed;
  inset: 0;
  z-index: 10000;
  overflow-x: hidden;
  overflow-y: auto;
  color: #0f2557;
  background: linear-gradient(168deg, #f7fbff 0%, #edf5ff 42%, #e6f0ff 72%, #e9f4ff 100%);
  font-family: "Microsoft YaHei", "PingFang SC", system-ui, sans-serif;
  -webkit-font-smoothing: antialiased;
}

.auth-bg {
  position: fixed;
  inset: 0;
  overflow: hidden;
  pointer-events: none;
}

.auth-blob {
  position: absolute;
  border-radius: 50%;
  filter: blur(70px);
  animation: auth-drift 26s ease-in-out infinite alternate;
}

.auth-blob--1 {
  top: -20vw;
  left: -14vw;
  width: 54vw;
  height: 54vw;
  background: radial-gradient(circle at 38% 38%, rgba(191, 222, 255, 0.9), rgba(191, 222, 255, 0));
}

.auth-blob--2 {
  right: -14vw;
  bottom: -18vw;
  width: 46vw;
  height: 46vw;
  background: radial-gradient(circle at 60% 40%, rgba(214, 236, 255, 0.95), rgba(214, 236, 255, 0));
  animation-delay: -8s;
}

.auth-blob--3 {
  top: 26vh;
  left: 34vw;
  width: 36vw;
  height: 36vw;
  background: radial-gradient(circle, rgba(233, 244, 255, 0.9), rgba(233, 244, 255, 0));
  animation-delay: -16s;
}

.auth-waves {
  position: absolute;
  inset: 0;
  background:
    repeating-radial-gradient(ellipse 72% 58% at 16% 12%, rgba(147, 197, 253, 0.2) 0 1px, transparent 1px 40px),
    repeating-radial-gradient(ellipse 62% 52% at 90% 84%, rgba(96, 165, 250, 0.16) 0 1px, transparent 1px 32px);
  opacity: 0.55;
  -webkit-mask-image: linear-gradient(180deg, rgba(0, 0, 0, 0.9), rgba(0, 0, 0, 0.2));
  mask-image: linear-gradient(180deg, rgba(0, 0, 0, 0.9), rgba(0, 0, 0, 0.2));
}

@keyframes auth-drift {
  from { transform: translate3d(0, 0, 0) scale(1); }
  to { transform: translate3d(3vw, -2vh, 0) scale(1.08); }
}

.auth-shell {
  position: relative;
  z-index: 1;
  display: grid;
  grid-template-columns: minmax(0, 1.08fr) minmax(0, 0.92fr);
  align-items: center;
  gap: clamp(36px, 5vw, 88px);
  width: 100%;
  max-width: 1240px;
  min-height: 100vh;
  margin: 0 auto;
  padding: clamp(28px, 4vw, 56px) clamp(24px, 5vw, 64px);
}

.auth-art {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  min-width: 0;
}

.auth-brand {
  display: flex;
  align-items: center;
  gap: 12px;
}

.auth-brand__logo {
  width: 46px;
  height: 46px;
  padding: 4px;
  border-radius: 14px;
  background: #fff;
  object-fit: contain;
  box-shadow: 0 8px 20px rgba(37, 99, 235, 0.16);
}

.auth-brand strong {
  display: block;
  font-size: 17px;
  line-height: 1.2;
  letter-spacing: 0.4px;
  color: #0b2a5b;
}

.auth-brand span {
  display: block;
  margin-top: 3px;
  font-size: 12px;
  letter-spacing: 2px;
  color: #7188a8;
}

.auth-headline {
  margin: 32px 0 10px;
  font-size: clamp(30px, 3.3vw, 44px);
  line-height: 1.24;
  font-weight: 800;
  letter-spacing: -1px;
  color: #0a2a5e;
}

.auth-subline {
  margin: 0;
  font-size: clamp(15px, 1.2vw, 17px);
  letter-spacing: 1px;
  color: #58718f;
}

.auth-stage {
  position: relative;
  width: min(100%, 520px);
  margin: 6px 0 0 -6px;
  aspect-ratio: 1 / 1;
  isolation: isolate;
}

.auth-stage::before {
  content: "";
  position: absolute;
  inset: 7% 5%;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(226, 237, 249, 0.85) 0%, rgba(230, 240, 251, 0.5) 55%, rgba(233, 243, 253, 0) 76%);
  filter: blur(18px);
  z-index: 0;
}

.auth-stage__glow {
  position: absolute;
  left: 13%;
  bottom: 10%;
  width: 74%;
  height: 50%;
  border-radius: 50%;
  background: radial-gradient(ellipse at center, rgba(96, 165, 250, 0.38), rgba(147, 197, 253, 0.12) 55%, transparent 76%);
  filter: blur(30px);
  z-index: 0;
}

.auth-stage__img {
  position: relative;
  z-index: 2;
  display: block;
  width: 100%;
  height: auto;
  -webkit-mask-image: radial-gradient(ellipse 56% 58% at 50% 47%, #000 62%, rgba(0, 0, 0, 0.65) 76%, transparent 88%);
  mask-image: radial-gradient(ellipse 56% 58% at 50% 47%, #000 62%, rgba(0, 0, 0, 0.65) 76%, transparent 88%);
}

.auth-ring {
  position: absolute;
  top: 50%;
  left: 50%;
  border: 1px dashed rgba(59, 130, 246, 0.32);
  border-radius: 50%;
  z-index: 1;
}

.auth-ring--outer {
  width: 94%;
  aspect-ratio: 1 / 1;
  animation: auth-spin-outer 52s linear infinite;
}

.auth-ring--inner {
  width: 68%;
  aspect-ratio: 1 / 1;
  border-color: rgba(147, 197, 253, 0.5);
  animation: auth-spin-inner 34s linear infinite;
}

@keyframes auth-spin-outer {
  from { transform: translate(-50%, -50%) rotate(0deg); }
  to { transform: translate(-50%, -50%) rotate(360deg); }
}

@keyframes auth-spin-inner {
  from { transform: translate(-50%, -50%) rotate(360deg); }
  to { transform: translate(-50%, -50%) rotate(0deg); }
}

.auth-float {
  position: absolute;
  z-index: 3;
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 14px 10px 10px;
  border: 1px solid rgba(255, 255, 255, 0.85);
  border-radius: 14px;
  background: rgba(255, 255, 255, 0.6);
  box-shadow: 0 16px 34px rgba(37, 99, 235, 0.16);
  -webkit-backdrop-filter: blur(16px) saturate(1.3);
  backdrop-filter: blur(16px) saturate(1.3);
  animation: auth-float 6.5s ease-in-out infinite;
}

.auth-float--study {
  top: 13%;
  left: -2%;
  animation-delay: -1s;
}

.auth-float--seat {
  top: 41%;
  right: -4%;
  animation-delay: -3.2s;
}

.auth-float--calendar {
  bottom: 11%;
  left: 3%;
  animation-delay: -5.4s;
}

.auth-float__icon {
  display: grid;
  place-items: center;
  flex: 0 0 auto;
  width: 38px;
  height: 38px;
  border-radius: 11px;
  background: linear-gradient(150deg, #eaf3ff, #d5e7ff);
  color: #1d4ed8;
}

.auth-float__icon svg {
  width: 21px;
  height: 21px;
}

.auth-float__text strong {
  display: block;
  font-size: 13.5px;
  line-height: 1.3;
  color: #0f2c5c;
}

.auth-float__text small {
  display: block;
  margin-top: 1px;
  font-size: 11px;
  letter-spacing: 0.5px;
  color: #6b83a4;
}

@keyframes auth-float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

.auth-side {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 18px;
  min-width: 0;
}

.auth-mobile-brand {
  display: none;
  align-items: center;
  gap: 10px;
}

.auth-mobile-brand img {
  width: 38px;
  height: 38px;
  padding: 3px;
  border-radius: 11px;
  background: #fff;
  object-fit: contain;
  box-shadow: 0 6px 16px rgba(37, 99, 235, 0.16);
}

.auth-mobile-brand strong {
  font-size: 16px;
  color: #0b2a5b;
}

.auth-card {
  position: relative;
  width: 100%;
  max-width: 480px;
  padding: clamp(24px, 2.4vw, 34px) clamp(22px, 2.6vw, 36px);
  border: 1px solid rgba(255, 255, 255, 0.9);
  border-radius: 26px;
  background: #fff;
  box-shadow: 0 30px 80px rgba(37, 99, 235, 0.14), 0 6px 20px rgba(15, 37, 87, 0.06);
}

.auth-tabs {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 4px;
  margin-bottom: 24px;
  padding: 4px;
  border-radius: 12px;
  background: #eef4fd;
}

.auth-tab {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 38px;
  padding: 0 10px;
  border-radius: 9px;
  font-size: 14px;
  font-weight: 600;
  letter-spacing: 0.5px;
  color: #6a7f9c;
  text-decoration: none;
  transition: color 0.18s ease, background-color 0.18s ease, box-shadow 0.18s ease;
}

.auth-tab:hover {
  color: #1d4ed8;
  text-decoration: none;
}

.auth-tab--active {
  background: #fff;
  color: #1d4ed8;
  box-shadow: 0 2px 8px rgba(37, 99, 235, 0.16);
}

.auth-title {
  margin: 0 0 6px;
  font-size: clamp(24px, 2vw, 28px);
  line-height: 1.3;
  font-weight: 800;
  letter-spacing: -0.4px;
  color: #0b2a5b;
}

.auth-desc {
  margin: 0 0 22px;
  font-size: 14px;
  color: #6b83a4;
}

.auth-notice {
  margin: 0 0 16px;
  padding: 11px 14px;
  border: 1px solid #fecdd3;
  border-radius: 10px;
  background: #fff1f2;
  color: #b42336;
  font-size: 13.5px;
  line-height: 1.55;
}

.auth-notice:focus {
  outline: 2px solid #fda4af;
  outline-offset: 2px;
}

.auth-form {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.auth-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14px;
}

.auth-field {
  display: flex;
  flex-direction: column;
  gap: 7px;
  min-width: 0;
}

.auth-field--full {
  grid-column: 1 / -1;
}

.auth-label {
  font-size: 13px;
  font-weight: 650;
  letter-spacing: 0.2px;
  color: #33507a;
}

.auth-label .req {
  color: #e25555;
}

.auth-input-wrap {
  position: relative;
  display: flex;
  align-items: center;
}

.auth-input,
.auth-select {
  width: 100%;
  height: 46px;
  padding: 0 13px;
  border: 1px solid #d9e4f5;
  border-radius: 10px;
  background: #fbfdff;
  color: #102a54;
  font-size: 14px;
  transition: border-color 0.18s ease, box-shadow 0.18s ease, background-color 0.18s ease;
}

.auth-input::placeholder {
  color: #a2b4cc;
}

.auth-input:focus,
.auth-select:focus,
.auth-input:focus-visible,
.auth-select:focus-visible {
  outline: none;
  border-color: #3b82f6;
  background: #fff;
  box-shadow: 0 0 0 3.5px rgba(59, 130, 246, 0.14);
}

.auth-input--password {
  padding-right: 46px;
}

.auth-select {
  padding-right: 34px;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='8' viewBox='0 0 12 8'%3E%3Cpath d='M1 1.5 6 6.5l5-5' fill='none' stroke='%237b90ad' stroke-width='1.6' stroke-linecap='round' stroke-linejoin='round'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right 12px center;
  background-size: 12px;
  appearance: none;
  -webkit-appearance: none;
  cursor: pointer;
}

.auth-eye {
  position: absolute;
  right: 6px;
  display: grid;
  place-items: center;
  width: 34px;
  height: 34px;
  min-height: 0;
  padding: 0;
  border: 0;
  border-radius: 8px;
  background: transparent;
  color: #8ba0bb;
  cursor: pointer;
}

.auth-eye:hover {
  background: #eef4fd;
  color: #1d4ed8;
}

.auth-eye:active {
  background: #e3edfc;
}

.auth-eye svg {
  width: 18px;
  height: 18px;
}

.auth-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.auth-check {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: #4a6486;
  cursor: pointer;
  user-select: none;
  -webkit-user-select: none;
}

.auth-check input {
  flex: 0 0 auto;
  width: 16px;
  height: 16px;
  min-height: 0;
  margin: 0;
  accent-color: #2563eb;
  cursor: pointer;
}

.auth-agree {
  align-items: flex-start;
  font-size: 12.5px;
  line-height: 1.6;
  color: #5c7697;
}

.auth-agree input {
  margin-top: 3px;
}

.auth-agree a {
  color: #1d4ed8;
  font-weight: 600;
}

.auth-submit {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  width: 100%;
  height: 50px;
  min-height: 50px;
  padding: 0;
  border: 0;
  border-radius: 12px;
  background: linear-gradient(135deg, #1d4ed8, #2563eb 55%, #3b82f6);
  color: #fff;
  font-size: 15.5px;
  font-weight: 700;
  letter-spacing: 1px;
  cursor: pointer;
  box-shadow: 0 10px 24px rgba(37, 99, 235, 0.28);
  transition: transform 0.16s ease, box-shadow 0.16s ease, filter 0.16s ease;
}

.auth-submit:hover {
  background: linear-gradient(135deg, #1e40af, #1d4ed8 55%, #2563eb);
  box-shadow: 0 14px 30px rgba(37, 99, 235, 0.34);
}

.auth-submit:active {
  transform: translateY(1px);
}

.auth-submit:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  box-shadow: none;
}

.auth-switch {
  margin: 18px 0 0;
  text-align: center;
  font-size: 13.5px;
  color: #6b83a4;
}

.auth-switch a {
  margin-left: 4px;
  color: #1d4ed8;
  font-weight: 650;
}

.auth-switch a:hover {
  text-decoration: underline;
}

.auth-foot {
  margin: 0;
  font-size: 12px;
  letter-spacing: 1.5px;
  color: #7d93b1;
}

@media (max-width: 1024px) {
  .auth-art {
    display: none;
  }

  .auth-shell {
    grid-template-columns: 1fr;
    min-height: 100vh;
    padding: 40px 24px;
  }

  .auth-mobile-brand {
    display: flex;
  }

  .auth-side {
    gap: 14px;
  }
}

@media (max-width: 560px) {
  .auth-shell {
    padding: 24px 16px;
  }

  .auth-card {
    padding: 22px 20px;
    border-radius: 22px;
  }

  .auth-grid {
    grid-template-columns: 1fr;
  }

  .auth-tabs {
    margin-bottom: 20px;
  }
}
</style>

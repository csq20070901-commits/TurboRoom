<template>
  <button class="robot-fab" type="button" aria-label="打开 TurboRoom AI 助手" @click="goAi">
    <span class="robot-bubble">我是 Turbo 小助手，点我直接问～</span>
    <span class="robot-sparks" aria-hidden="true"><i></i><i></i><i></i></span>
    <span class="robot-body" aria-hidden="true">
      <svg class="robot-svg" viewBox="0 0 96 120" aria-hidden="true" focusable="false">
        <path d="M48 12v6" stroke="#c08f3e" stroke-width="3.4" stroke-linecap="round" />
        <circle class="robot-ant" cx="48" cy="7.4" r="5" fill="#f6b84b" />
        <circle cx="9" cy="44" r="7" fill="#2f9d7f" />
        <circle cx="87" cy="44" r="7" fill="#2f9d7f" />
        <rect x="16" y="15" width="64" height="57" rx="18" fill="url(#robotFabGrad)" />
        <rect x="23" y="23" width="50" height="38" rx="12" fill="#faf6ea" />
        <g class="robot-eyes">
          <ellipse cx="37" cy="40" rx="6" ry="7.2" fill="#1c4a3b" />
          <ellipse cx="59" cy="40" rx="6" ry="7.2" fill="#1c4a3b" />
          <circle cx="39.2" cy="37.6" r="2.1" fill="#fff" />
          <circle cx="61.2" cy="37.6" r="2.1" fill="#fff" />
        </g>
        <path d="M40 52c2.6 2.4 5.4 2.4 8 0" stroke="#1c4a3b" stroke-width="2.6" stroke-linecap="round" fill="none" />
        <circle cx="29" cy="48" r="3.4" fill="#f3a25b" opacity=".92" />
        <circle cx="67" cy="48" r="3.4" fill="#f3a25b" opacity=".92" />
        <rect x="17" y="79" width="11" height="23" rx="5.5" fill="#2f9d7f" />
        <g class="robot-arm">
          <rect x="68" y="79" width="11" height="23" rx="5.5" fill="#2f9d7f" />
        </g>
        <rect x="26" y="77" width="44" height="35" rx="14" fill="url(#robotFabGrad)" />
        <rect x="33" y="84" width="30" height="19" rx="8" fill="#faf6ea" />
        <path d="M48 98c-2.7-3.1-7-1.6-7 1.4 0 2.1 1.8 3.4 7 5.6 5.2-2.2 7-3.5 7-5.6 0-3-4.3-4.5-7-1.4Z" fill="#e2574c" />
        <rect x="30" y="112" width="15" height="8" rx="4" fill="#f3a25b" />
        <rect x="51" y="112" width="15" height="8" rx="4" fill="#f3a25b" />
        <defs>
          <linearGradient id="robotFabGrad" x1="0" y1="0" x2="1" y2="1">
            <stop offset="0" stop-color="#2f9d7f" />
            <stop offset="1" stop-color="#1c5c48" />
          </linearGradient>
        </defs>
      </svg>
    </span>
    <span class="robot-shadow" aria-hidden="true"></span>
  </button>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

function goAi() {
  router.push('/ai')
}
</script>

<style scoped>
.robot-fab {
  position: fixed;
  right: 24px;
  bottom: 22px;
  z-index: 10001;
  display: block;
  width: 92px;
  height: 92px;
  padding: 0;
  border: 0;
  background: transparent;
  cursor: pointer;
  -webkit-tap-highlight-color: transparent;
  animation: fab-in .5s ease-out .35s backwards;
}

.robot-fab:focus-visible {
  outline: 3px solid rgba(111, 165, 140, .4);
  outline-offset: 4px;
  border-radius: 24px;
}

.robot-body {
  position: absolute;
  inset: 2px 4px 10px;
  animation: fab-float 3.2s ease-in-out infinite;
}

.robot-svg {
  display: block;
  width: 100%;
  height: 100%;
  filter: drop-shadow(0 8px 14px rgba(20, 59, 48, .22));
}

.robot-fab:hover .robot-body {
  animation-duration: 1.6s;
}

.robot-shadow {
  position: absolute;
  left: 16px;
  right: 16px;
  bottom: 2px;
  height: 10px;
  border-radius: 50%;
  background: rgba(20, 59, 48, .3);
  filter: blur(2px);
  animation: fab-shadow 3.2s ease-in-out infinite;
}

.robot-ant {
  animation: ant-glow 2.2s ease-in-out infinite;
}

.robot-eyes {
  transform-origin: 48px 40px;
  animation: blink 4.8s infinite;
}

.robot-arm {
  transform-origin: 73.5px 81px;
  animation: wave 5s ease-in-out infinite;
}

.robot-sparks {
  position: absolute;
  top: -6px;
  right: -4px;
  width: 44px;
  height: 40px;
  pointer-events: none;
}

.robot-sparks i {
  position: absolute;
  border-radius: 50%;
}

.robot-sparks i:nth-child(1) {
  top: 6px;
  right: 8px;
  width: 9px;
  height: 9px;
  background: #f6b84b;
  animation: twinkle 2.4s ease-in-out infinite;
}

.robot-sparks i:nth-child(2) {
  top: 18px;
  right: 0;
  width: 6px;
  height: 6px;
  background: #2f9d7f;
  animation: twinkle 2.4s ease-in-out .5s infinite;
}

.robot-sparks i:nth-child(3) {
  top: 2px;
  right: 22px;
  width: 5px;
  height: 5px;
  background: #f3a25b;
  animation: twinkle 2.4s ease-in-out 1s infinite;
}

.robot-bubble {
  position: absolute;
  right: 100%;
  bottom: 20px;
  z-index: 61;
  width: max-content;
  max-width: 220px;
  margin-right: 14px;
  padding: 10px 14px;
  border: 1px solid rgba(28, 74, 59, .16);
  border-radius: 14px 14px 4px 14px;
  background: #fffdf7;
  color: #1c4a3b;
  font-size: .84rem;
  font-weight: 600;
  line-height: 1.5;
  white-space: normal;
  box-shadow: 0 10px 26px rgba(20, 59, 48, .16);
  animation: bubble-cycle 26s ease-in-out infinite;
}

.robot-bubble::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 100%;
  width: 8px;
  height: 8px;
  margin-top: -4px;
  border-right: 1px solid rgba(28, 74, 59, .16);
  border-bottom: 1px solid rgba(28, 74, 59, .16);
  background: #fffdf7;
  transform: rotate(-45deg) translateY(-4px);
}

.robot-fab:hover .robot-bubble {
  opacity: 1;
  transform: none;
  animation: none;
}

@keyframes fab-in {
  from { opacity: 0; transform: translateY(16px) scale(.86); }
  to { opacity: 1; transform: translateY(0) scale(1); }
}

@keyframes fab-float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-7px); }
}

@keyframes fab-shadow {
  0%, 100% { transform: scaleX(1); opacity: .3; }
  50% { transform: scaleX(.78); opacity: .16; }
}

@keyframes ant-glow {
  0%, 100% { filter: drop-shadow(0 0 2px rgba(246, 184, 75, .6)); }
  50% { filter: drop-shadow(0 0 8px rgba(255, 200, 97, .95)); }
}

@keyframes blink {
  0%, 91%, 100% { transform: scaleY(1); }
  94%, 97% { transform: scaleY(.08); }
}

@keyframes wave {
  0%, 10%, 100% { transform: rotate(0); }
  14% { transform: rotate(-26deg); }
  18% { transform: rotate(12deg); }
  22% { transform: rotate(-16deg); }
  26% { transform: rotate(0); }
}

@keyframes twinkle {
  0%, 100% { opacity: 0; transform: scale(.4); }
  50% { opacity: 1; transform: scale(1); }
}

@keyframes bubble-cycle {
  0%, 5% { opacity: 0; transform: translateY(10px) scale(.92); }
  11%, 38% { opacity: 1; transform: translateY(0) scale(1); }
  46%, 100% { opacity: 0; transform: translateY(10px) scale(.92); }
}

@media (max-width: 760px) {
  .robot-fab {
    right: 14px;
    bottom: 14px;
    width: 76px;
    height: 76px;
  }
  .robot-bubble {
    bottom: 12px;
    max-width: 180px;
    font-size: .78rem;
  }
}

@media (prefers-reduced-motion: reduce) {
  .robot-body,
  .robot-shadow,
  .robot-ant,
  .robot-eyes,
  .robot-arm,
  .robot-sparks i,
  .robot-bubble,
  .robot-fab {
    animation-duration: .01ms;
    animation-iteration-count: 1;
  }
}
</style>

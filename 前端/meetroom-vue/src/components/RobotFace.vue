<template>
  <svg class="robot-face" viewBox="0 0 64 64" aria-hidden="true" focusable="false">
    <path d="M32 6.5V10" stroke="#c08f3e" stroke-width="2.6" stroke-linecap="round" />
    <circle cx="32" cy="4.4" r="3.2" fill="#f6b84b" />
    <circle cx="7.6" cy="30" r="4.6" fill="#2f9d7f" />
    <circle cx="56.4" cy="30" r="4.6" fill="#2f9d7f" />
    <rect x="12" y="10" width="40" height="40" rx="13" fill="url(#robotFaceGrad)" />
    <rect x="17.5" y="16.5" width="29" height="24" rx="8" fill="#faf6ea" />
    <ellipse cx="26" cy="27.5" rx="3.6" ry="4.2" fill="#1c4a3b" />
    <ellipse cx="38" cy="27.5" rx="3.6" ry="4.2" fill="#1c4a3b" />
    <circle cx="27.3" cy="26" r="1.2" fill="#fff" />
    <circle cx="39.3" cy="26" r="1.2" fill="#fff" />
    <path d="M26.5 34.5c1.6 1.4 3.4 1.4 5 0" stroke="#1c4a3b" stroke-width="2" stroke-linecap="round" fill="none" />
    <circle cx="20.5" cy="33.5" r="2" fill="#f3a25b" opacity=".9" />
    <circle cx="43.5" cy="33.5" r="2" fill="#f3a25b" opacity=".9" />
    <defs>
      <linearGradient id="robotFaceGrad" x1="0" y1="0" x2="1" y2="1">
        <stop offset="0" stop-color="#2f9d7f" />
        <stop offset="1" stop-color="#1c5c48" />
      </linearGradient>
    </defs>
  </svg>
</template>

<script setup>
</script>

<style scoped>
.robot-face {
  display: block;
  width: 100%;
  height: 100%;
}
</style>

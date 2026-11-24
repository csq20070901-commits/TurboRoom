<template>
  <nav
    class="line-sidebar"
    :class="{
      'line-sidebar--markers': showMarker,
      'line-sidebar--scale-tick': scaleTick,
      [className]: className
    }"
    :style="navStyle"
  >
    <ul ref="listRef" class="line-sidebar__list" @pointermove="handlePointerMove" @pointerleave="handlePointerLeave">
      <li
        v-for="(label, index) in items"
        :key="`${label}-${index}`"
        ref="itemRefs"
        class="line-sidebar__item"
        :aria-current="activeIndex === index ? 'true' : undefined"
        @click="handleClick(index, label)"
      >
        <span v-if="showMarker" class="line-sidebar__marker" aria-hidden="true" />
        <span class="line-sidebar__label">
          <span v-if="showIndex" class="line-sidebar__index">{{ String(index + 1).padStart(2, '0') }}</span>
          <span class="line-sidebar__text">{{ label }}</span>
        </span>
      </li>
    </ul>
  </nav>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'

const props = defineProps({
  items: { type: Array, default: () => ['Overview', 'Components', 'Animations', 'Backgrounds', 'Showcase'] },
  accentColor: { type: String, default: '#A855F7' },
  textColor: { type: String, default: '#c4c4c4' },
  markerColor: { type: String, default: '#6c6c6c' },
  showIndex: { type: Boolean, default: true },
  showMarker: { type: Boolean, default: true },
  proximityRadius: { type: Number, default: 100 },
  maxShift: { type: Number, default: 30 },
  falloff: { type: String, default: 'smooth', validator: v => ['linear', 'smooth', 'sharp'].includes(v) },
  markerLength: { type: Number, default: 60 },
  markerGap: { type: Number, default: 0 },
  tickScale: { type: Number, default: 0.5 },
  scaleTick: { type: Boolean, default: true },
  itemGap: { type: Number, default: 20 },
  fontSize: { type: Number, default: 1.1 },
  smoothing: { type: Number, default: 100 },
  defaultActive: { type: Number, default: null },
  className: { type: String, default: '' }
})

const emit = defineEmits(['itemClick'])

const FALLOFF_CURVES = {
  linear: p => p,
  smooth: p => p * p * (3 - 2 * p),
  sharp: p => p * p * p
}

function parseHex(hex) {
  const h = hex.replace('#', '')
  return [parseInt(h.slice(0, 2), 16), parseInt(h.slice(2, 4), 16), parseInt(h.slice(4, 6), 16)]
}

function lerpColor(a, b, t) {
  const ar = parseHex(a), br = parseHex(b)
  const r = Math.round(ar[0] + (br[0] - ar[0]) * t)
  const g = Math.round(ar[1] + (br[1] - ar[1]) * t)
  const bl = Math.round(ar[2] + (br[2] - ar[2]) * t)
  return `rgb(${r},${g},${bl})`
}

const listRef = ref(null)
const itemRefs = ref([])
const activeIndex = ref(props.defaultActive)

let rafRef = null
let lastRef = 0
let targets = []
let currents = []
let itemCenters = []
let needsLayout = true

const navStyle = computed(() => ({
  '--accent-color': props.accentColor,
  '--text-color': props.textColor,
  '--marker-color': props.markerColor,
  '--marker-length': `${props.markerLength}px`,
  '--marker-gap': `${props.markerGap}px`,
  '--tick-scale': props.tickScale,
  '--max-shift': `${props.maxShift}px`,
  '--item-gap': `${props.itemGap}px`,
  '--font-size': `${props.fontSize}rem`,
  '--smoothing': `${props.smoothing}ms`
}))

const cacheLayout = () => {
  const list = listRef.value
  if (!list) return
  const rect = list.getBoundingClientRect()
  const items = itemRefs.value
  itemCenters = items.map(el => {
    if (!el) return 0
    return el.offsetTop + el.offsetHeight / 2 - rect.top
  })
  needsLayout = false
}

const runFrame = (now) => {
  const dt = Math.min((now - lastRef) / 1000, 0.05)
  lastRef = now
  const tau = Math.max(props.smoothing, 1) / 1000
  const k = 1 - Math.exp(-dt / tau)
  const accent = props.accentColor
  const text = props.textColor
  const marker = props.markerColor
  const active = activeIndex.value
  const items = itemRefs.value

  let moving = false
  for (let i = 0; i < items.length; i++) {
    const el = items[i]
    if (!el) continue
    const target = Math.max(targets[i] || 0, active === i ? 1 : 0)
    const cur = currents[i] || 0
    const next = cur + (target - cur) * k
    const settled = Math.abs(target - next) < 0.0015
    const value = settled ? target : next
    currents[i] = value
    if (!settled) moving = true

    el.style.setProperty('--effect', value.toFixed(4))
    const label = el.querySelector('.line-sidebar__label')
    if (label) label.style.color = lerpColor(text, accent, value)
    const markerEl = el.querySelector('.line-sidebar__marker')
    if (markerEl) markerEl.style.backgroundColor = lerpColor(marker, accent, value)
  }

  rafRef = moving ? requestAnimationFrame(runFrame) : null
}

const startLoop = () => {
  if (rafRef != null) cancelAnimationFrame(rafRef)
  lastRef = performance.now()
  rafRef = requestAnimationFrame(runFrame)
}

let moveThrottle = false
const handlePointerMove = (e) => {
  if (moveThrottle) return
  moveThrottle = true
  requestAnimationFrame(() => { moveThrottle = false })

  if (needsLayout) cacheLayout()
  const ease = FALLOFF_CURVES[props.falloff] ?? FALLOFF_CURVES.linear
  const pointerY = e.clientY - listRef.value.getBoundingClientRect().top
  const radius = props.proximityRadius
  const items = itemRefs.value
  for (let i = 0; i < items.length; i++) {
    if (!items[i]) continue
    const distance = Math.abs(pointerY - itemCenters[i])
    targets[i] = ease(Math.max(0, 1 - distance / radius))
  }
  startLoop()
}

const handlePointerLeave = () => {
  targets = targets.map(() => 0)
  startLoop()
}

const handleClick = (index, label) => {
  activeIndex.value = index
  emit('itemClick', index, label)
}

onMounted(() => {
  cacheLayout()
  targets = new Array(props.items.length).fill(0)
  currents = new Array(props.items.length).fill(0)
  startLoop()
})

onUnmounted(() => {
  if (rafRef != null) cancelAnimationFrame(rafRef)
  rafRef = null
})
</script>

<style scoped>
.line-sidebar {
  --accent-color: #a855f7;
  --text-color: #c4c4c4;
  --marker-color: #6c6c6c;
  --marker-length: 60px;
  --marker-gap: 0px;
  --tick-scale: 0.5;
  --max-shift: 30px;
  --item-gap: 20px;
  --font-size: 1.1rem;
  --smoothing: 100ms;

  position: relative;
  display: flex;
  justify-content: flex-start;
}

.line-sidebar--markers {
  padding-left: calc(var(--marker-length) + var(--marker-gap));
}

.line-sidebar__list {
  list-style: none;
  margin: 0;
  padding: 1rem 0;
  display: flex;
  flex-direction: column;
  gap: var(--item-gap);
}

.line-sidebar__item {
  position: relative;
  cursor: pointer;
}

.line-sidebar__item::before {
  content: '';
  position: absolute;
  inset: -6px -48px;
}

.line-sidebar__label {
  position: relative;
  display: inline-flex;
  align-items: baseline;
  font-size: var(--font-size);
  line-height: 1.2;
  will-change: transform, color;
}

.line-sidebar__index {
  font-family: ui-monospace, SFMono-Regular, Menlo, monospace;
  margin-right: 0.6rem;
  font-size: 0.85em;
  will-change: opacity;
}

.line-sidebar__marker {
  position: absolute;
  top: 50%;
  left: calc(-1 * var(--marker-length) - var(--marker-gap));
  height: 1px;
  width: var(--marker-length);
  transform-origin: left center;
  will-change: transform, background-color;
}

.line-sidebar--markers .line-sidebar__item:not(:last-child)::after {
  content: '';
  position: absolute;
  top: calc(100% + var(--item-gap) / 2);
  left: calc(-1 * var(--marker-length) - var(--marker-gap));
  height: 1px;
  width: calc(var(--marker-length) * var(--tick-scale));
  background-color: var(--marker-color);
  opacity: 0.5;
  transform: translateY(-50%);
}

.line-sidebar--scale-tick .line-sidebar__item:not(:last-child)::after {
  transform-origin: left center;
  transform: translateY(-50%) scaleX(calc(0.7 + var(--effect, 0) * 0.6));
}
</style>

<template>
  <div
    ref="rootRef"
    class="text-loop"
    :class="{ 'text-loop--draggable': draggable }"
    :style="wrapperStyle"
  >
    <svg
      ref="svgRef"
      class="text-loop-svg"
      :viewBox="`0 0 ${VIEW_W} ${VIEW_H}`"
      preserveAspectRatio="xMidYMid meet"
      role="img"
      :aria-label="text"
    >
      <defs>
        <linearGradient v-if="fadeStops.length" :id="fadeId" x1="0" y1="0" x2="1" y2="0">
          <stop
            v-for="(stop, i) in fadeStops"
            :key="i"
            :offset="stop.offset"
            stop-color="#ffffff"
            :stop-opacity="stop.opacity"
          />
        </linearGradient>
        <mask v-if="fadeStops.length" :id="maskId" maskUnits="userSpaceOnUse" :x="0" :y="0" :width="VIEW_W" :height="VIEW_H">
          <rect :width="VIEW_W" :height="VIEW_H" :fill="`url(#${fadeId})`" />
        </mask>
      </defs>

      <text ref="measureRef" class="text-loop-measure" :style="textStyle" aria-hidden="true">
        {{ unit }}
      </text>

      <g :mask="fadeStops.length ? `url(#${maskId})` : undefined">
        <g :transform="mainTransform">
          <path
            ref="pathRef"
            :id="pathId"
            :d="pathD"
            fill="none"
            :stroke="ribbon ? ribbonColor : 'none'"
            :stroke-width="ribbon ? ribbonWidth : 0"
            stroke-linecap="round"
            stroke-linejoin="round"
          />

          <text
            class="text-loop-text"
            :style="textStyle"
            :fill="textFill"
            dominant-baseline="central"
            aria-hidden="true"
            :textLength="metrics.length || undefined"
            length-adjust="spacing"
          >
            <textPath ref="headRef" :href="`#${pathId}`" start-offset="0">
              {{ loopText }}
            </textPath>
          </text>

          <text
            class="text-loop-text"
            :style="textStyle"
            :fill="textFill"
            dominant-baseline="central"
            aria-hidden="true"
            :textLength="metrics.length || undefined"
            length-adjust="spacing"
          >
            <textPath ref="tailRef" :href="`#${pathId}`" start-offset="0">
              {{ loopText }}
            </textPath>
          </text>
        </g>
      </g>
    </svg>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import { gsap } from 'gsap'

const props = defineProps({
  text: { type: String, default: 'TurboRoom ✦ Campus Study' },
  shape: { type: String, default: 'wave', validator: v => ['wave', 'circle', 'infinity', 'arch', 'line'].includes(v) },
  path: { type: String, default: undefined },
  speed: { type: Number, default: 90 },
  direction: { type: String, default: 'forward', validator: v => ['forward', 'reverse'].includes(v) },
  separator: { type: String, default: '✦' },
  curviness: { type: Number, default: 90 },
  tilt: { type: Number, default: 0 },
  fontSize: { type: Number, default: 46 },
  fontWeight: { type: Number, default: 800 },
  letterSpacing: { type: Number, default: 2 },
  uppercase: { type: Boolean, default: true },
  color: { type: String, default: undefined },
  ribbon: { type: Boolean, default: true },
  ribbonColor: { type: String, default: '#1a7a6b' },
  ribbonWidth: { type: Number, default: 86 },
  edgeFade: { type: Number, default: 0 },
  pauseOnHover: { type: Boolean, default: true },
  draggable: { type: Boolean, default: false },
  outline: { type: Boolean, default: false },
  outlineWidth: { type: Number, default: 1.5 }
})

const VIEW_W = 1200
const VIEW_H = 200
const CX = VIEW_W / 2
const CY = VIEW_H / 2
const EDGE_PAD = 6
const FADE_STEPS = 8

const rootRef = ref(null)
const svgRef = ref(null)
const pathRef = ref(null)
const measureRef = ref(null)
const headRef = ref(null)
const tailRef = ref(null)

const progressRef = ref(0)
const metrics = ref({ length: 0, reps: 1 })
const travelRef = ref(0)

const rawId = `text-loop-${Math.random().toString(36).slice(2, 9)}`
const pathId = `${rawId}`
const fadeId = `${rawId}-fade`
const maskId = `${rawId}-mask`

const custom = computed(() => Boolean(props.path))

const pathD = computed(() => {
  if (props.path) return props.path
  return buildPath(props.shape, props.curviness, props.ribbonWidth)
})

const fadeStops = computed(() => buildFade(props.edgeFade))

const unit = computed(() => {
  const base = props.uppercase ? String(props.text).toUpperCase() : String(props.text)
  const gap = props.separator ? `\u00A0${props.separator}\u00A0` : '\u00A0\u00A0\u00A0'
  return `${base}${gap}`
})

const textStyle = computed(() => ({
  fontSize: `${props.fontSize}px`,
  fontWeight: props.fontWeight,
  letterSpacing: `${props.letterSpacing}px`
}))

const textFill = computed(() => props.color || (props.ribbon ? '#ffffff' : 'currentColor'))

const mainAngle = computed(() => props.tilt)
const mainTransform = computed(() => {
  const angle = mainAngle.value
  return angle ? `rotate(${angle} ${CX} ${CY})` : undefined
})

const loopText = computed(() => unit.value.repeat(metrics.value.reps))

const wrapperStyle = computed(() => ({}))

function smootherstep(t) {
  return t * t * t * (t * (t * 6 - 15) + 10)
}

function buildPath(shape, curviness, ribbonWidth) {
  const c = Math.max(0, curviness)
  const room = Math.max(20, CY - Math.max(0, ribbonWidth) / 2 - EDGE_PAD)

  switch (shape) {
    case 'circle': {
      const r = Math.min(90 + Math.max(0, curviness) * 0.95, Math.max(20, CY - Math.max(0, ribbonWidth) / 2 - EDGE_PAD))
      return `M ${CX - r} ${CY} A ${r} ${r} 0 1 1 ${CX + r} ${CY} A ${r} ${r} 0 1 1 ${CX - r} ${CY} Z`
    }
    case 'infinity': {
      const r = 150 + c * 1.4
      const h = Math.min(60 + c * 0.95, room)
      return [
        `M ${CX} ${CY}`,
        `C ${CX + r * 0.55} ${CY - h} ${CX + r} ${CY - h} ${CX + r} ${CY}`,
        `C ${CX + r} ${CY + h} ${CX + r * 0.55} ${CY + h} ${CX} ${CY}`,
        `C ${CX - r * 0.55} ${CY - h} ${CX - r} ${CY - h} ${CX - r} ${CY}`,
        `C ${CX - r} ${CY + h} ${CX - r * 0.55} ${CY + h} ${CX} ${CY}`,
        'Z'
      ].join(' ')
    }
    case 'arch': {
      const rise = Math.min(120 + c * 1.1, room * 2)
      return `M 120 ${CY + rise / 2} Q ${CX} ${CY - rise * 1.5} ${VIEW_W - 120} ${CY + rise / 2}`
    }
    case 'line':
      return `M -320 ${CY} L ${VIEW_W + 320} ${CY}`
    case 'wave':
    default: {
      const a = Math.min(c * 2.2, room * 2)
      return `M -320 ${CY} Q -160 ${CY - a} 0 ${CY} T 320 ${CY} T 640 ${CY} T 960 ${CY} T 1280 ${CY} T ${VIEW_W + 320} ${CY}`
    }
  }
}

function buildFade(edgeFade) {
  const width = Math.min(1, Math.max(0, edgeFade)) * 0.3
  if (width <= 0) return []
  const stops = []
  for (let i = 0; i <= FADE_STEPS; i++) {
    const t = i / FADE_STEPS
    stops.push({ offset: t * width, opacity: smootherstep(t) })
  }
  for (let i = FADE_STEPS; i >= 0; i--) {
    const t = i / FADE_STEPS
    stops.push({ offset: 1 - t * width, opacity: smootherstep(t) })
  }
  return stops
}

function wrap(v, length) {
  return ((v % length) + length) % length
}

let gsapTicker = null
let running = false
let hoverScale = 1
let observer = null

function measure() {
  const pathEl = pathRef.value
  const measureEl = measureRef.value
  if (!pathEl || !measureEl) return

  let length = 0
  let unitWidth = 0
  try {
    length = pathEl.getTotalLength()
    unitWidth = measureEl.getComputedTextLength()
  } catch {
    return
  }
  if (!length) return

  const reps = unitWidth > 0 ? Math.max(1, Math.round(length / unitWidth)) : 1
  metrics.value = { length, reps }
}

function applyOffset() {
  const { length } = metrics.value
  if (!length) return
  const offset = wrap(travelRef.value, length)
  progressRef.value = offset / length
  if (headRef.value) headRef.value.setAttribute('startOffset', String(offset))
  if (tailRef.value) tailRef.value.setAttribute('startOffset', String(offset - length))
}

function tick(_, deltaMs) {
  const dt = Math.min(0.05, deltaMs / 1000)
  const reduced = window.matchMedia?.('(prefers-reduced-motion: reduce)')?.matches
  if (reduced) return

  const sign = props.direction === 'reverse' ? -1 : 1
  const base = Math.max(0, props.speed) * sign
  const v = base * hoverScale
  if (Math.abs(v) < 0.01) return

  travelRef.value += v * dt
  applyOffset()
}

function start() {
  if (running) return
  running = true
  gsapTicker = gsap.ticker.add(tick)
}

function stop() {
  if (!running) return
  running = false
  if (gsapTicker) gsap.ticker.remove(gsapTicker)
}

function onEnter() {
  if (props.pauseOnHover) {
    gsap.to({ scale: hoverScale }, {
      scale: 0,
      duration: 0.6,
      ease: 'power2.out',
      overwrite: true,
      onUpdate: function() {
        hoverScale = this.targets()[0].scale
      }
    })
  }
}

function onLeave() {
  if (props.pauseOnHover) {
    gsap.to({ scale: hoverScale }, {
      scale: 1,
      duration: 0.9,
      ease: 'power2.inOut',
      overwrite: true,
      onUpdate: function() {
        hoverScale = this.targets()[0].scale
      }
    })
  }
}

onMounted(() => {
  measure()
  if (document.fonts?.ready) {
    document.fonts.ready.then(measure).catch(() => {})
  }

  applyOffset()
  start()

  if (typeof IntersectionObserver !== 'undefined') {
    observer = new IntersectionObserver(([entry]) => {
      if (entry.isIntersecting) start()
      else stop()
    })
    if (rootRef.value) observer.observe(rootRef.value)
  }

  if (rootRef.value && props.pauseOnHover) {
    rootRef.value.addEventListener('pointerenter', onEnter)
    rootRef.value.addEventListener('pointerleave', onLeave)
  }
})

onUnmounted(() => {
  stop()
  observer?.disconnect()
  if (rootRef.value && props.pauseOnHover) {
    rootRef.value.removeEventListener('pointerenter', onEnter)
    rootRef.value.removeEventListener('pointerleave', onLeave)
  }
})

watch([() => props.shape, () => props.curviness, () => props.ribbonWidth, () => props.path], () => {
  measure()
  applyOffset()
})
</script>

<style scoped>
.text-loop {
  position: relative;
  width: 100%;
  overflow: hidden;
  margin: -40px 0;
}

.text-loop--draggable {
  cursor: grab;
  touch-action: pan-y;
}

.text-loop-svg {
  display: block;
  width: 100%;
  height: auto;
}

.text-loop-text {
  user-select: none;
}

.text-loop-measure {
  visibility: hidden;
  pointer-events: none;
}
</style>

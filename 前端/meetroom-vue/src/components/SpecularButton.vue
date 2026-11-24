<template>
  <button
    ref="buttonRef"
    :type="type"
    :disabled="disabled"
    :class="['specular-button', `specular-button--${size}`, className]"
    :style="buttonStyle"
    @click="handleClick"
    @pointermove="onPointerMove"
    @pointerleave="onPointerLeave"
    @pointerdown="onPointerDown"
    @pointerup="onPointerUp"
  >
    <span class="specular-button__border" />
    <span class="specular-button__shine" :style="shineStyle" />
    <span class="specular-button__label">
      <slot />
    </span>
  </button>
</template>

<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
  size: { type: String, default: 'lg', validator: v => ['sm', 'md', 'lg'].includes(v) },
  radius: { type: Number, default: 18 },
  tint: { type: String, default: '#ffffff' },
  tintOpacity: { type: Number, default: 0.04 },
  blur: { type: Number, default: 0 },
  textColor: { type: String, default: '#f5f5f5' },
  lineColor: { type: String, default: '#ffffff' },
  baseColor: { type: String, default: '#525252' },
  intensity: { type: Number, default: 1 },
  idleIntensity: { type: Number, default: 0.35 },
  thickness: { type: Number, default: 1.2 },
  glow: { type: Number, default: 1 },
  speed: { type: Number, default: 0.35 },
  followMouse: { type: Boolean, default: true },
  proximity: { type: Number, default: 250 },
  autoAnimate: { type: Boolean, default: false },
  disabled: { type: Boolean, default: false },
  type: { type: String, default: 'button', validator: v => ['button', 'submit', 'reset'].includes(v) },
  className: { type: String, default: '' }
})

const emit = defineEmits(['click'])

const buttonRef = ref(null)
const shinePos = ref({ x: 50, y: 0 })
const shineOpacity = ref(0)
const isPressed = ref(false)

const buttonStyle = computed(() => ({
  '--sb-radius': `${props.radius}px`,
  '--sb-tint': props.tint,
  '--sb-tint-opacity': props.tintOpacity,
  '--sb-blur': `${props.blur}px`,
  '--sb-text-color': props.textColor,
  '--sb-line-color': props.lineColor,
  '--sb-base-color': props.baseColor
}))

const shineStyle = computed(() => ({
  '--shine-x': `${shinePos.value.x}%`,
  '--shine-y': `${shinePos.value.y}%`,
  '--shine-opacity': shineOpacity.value,
  '--shine-glow': props.glow
}))

function updateShine(clientX, clientY) {
  if (!props.followMouse || !buttonRef.value) return
  const rect = buttonRef.value.getBoundingClientRect()
  const x = ((clientX - rect.left) / rect.width) * 100
  const y = ((clientY - rect.top) / rect.height) * 100
  shinePos.value = { x: Math.max(0, Math.min(100, x)), y: Math.max(0, Math.min(100, y)) }
  shineOpacity.value = props.intensity
}

function onPointerMove(e) {
  updateShine(e.clientX, e.clientY)
}

function onPointerLeave() {
  shineOpacity.value = props.idleIntensity
  shinePos.value = { x: 50, y: 0 }
}

function onPointerDown() {
  isPressed.value = true
}

function onPointerUp() {
  isPressed.value = false
}

function handleClick(e) {
  if (!props.disabled) emit('click', e)
}
</script>

<style scoped>
.specular-button {
  --sb-radius: 18px;
  --sb-tint: #ffffff;
  --sb-tint-opacity: 0.04;
  --sb-blur: 0px;
  --sb-text-color: #f5f5f5;
  --sb-line-color: #ffffff;
  --sb-base-color: #525252;
  --shine-x: 50%;
  --shine-y: 0%;
  --shine-opacity: 0.35;
  --shine-glow: 1;

  position: relative;
  isolation: isolate;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 0.5em;
  margin: 0;
  border: none;
  border-radius: var(--sb-radius);
  font: inherit;
  font-weight: 500;
  line-height: 1;
  letter-spacing: 0.01em;
  color: var(--sb-text-color);
  background: color-mix(in srgb, var(--sb-tint) calc(var(--sb-tint-opacity) * 100%), transparent);
  box-shadow: 0 10px 24px -14px rgba(0, 0, 0, 0.45);
  -webkit-backdrop-filter: blur(var(--sb-blur));
  backdrop-filter: blur(var(--sb-blur));
  cursor: pointer;
  outline: none;
  -webkit-tap-highlight-color: transparent;
  transition: transform 0.25s cubic-bezier(0.2, 0.8, 0.2, 1), box-shadow 0.3s ease;
  overflow: hidden;
}

.specular-button__border {
  position: absolute;
  inset: 0;
  border-radius: inherit;
  pointer-events: none;
  border: 1.2px solid color-mix(in srgb, var(--sb-base-color) 35%, transparent);
  transition: border-color 0.3s ease;
  z-index: 1;
}

.specular-button__shine {
  position: absolute;
  inset: -2px;
  border-radius: inherit;
  pointer-events: none;
  z-index: 0;
  opacity: var(--shine-opacity);
  background: radial-gradient(
    ellipse 80% 60% at var(--shine-x) var(--shine-y),
    color-mix(in srgb, var(--sb-line-color) 30%, transparent),
    transparent 60%
  );
  transition: opacity 0.4s ease;
  filter: blur(calc(2px * var(--shine-glow)));
}

.specular-button:hover .specular-button__border {
  border-color: color-mix(in srgb, var(--sb-line-color) 60%, transparent);
}

.specular-button:hover {
  box-shadow:
    0 0 calc(16px * var(--shine-glow)) color-mix(in srgb, var(--sb-line-color) 20%, transparent),
    0 8px 20px -10px rgba(0, 0, 0, 0.3);
}

.specular-button:active {
  transform: scale(0.97);
}

.specular-button:active .specular-button__shine {
  opacity: 1;
  background: radial-gradient(
    ellipse 100% 80% at var(--shine-x) var(--shine-y),
    color-mix(in srgb, var(--sb-line-color) 40%, transparent),
    transparent 60%
  );
}

.specular-button:focus-visible {
  outline: 2px solid color-mix(in srgb, var(--sb-text-color) 55%, transparent);
  outline-offset: 3px;
}

.specular-button:disabled {
  opacity: 0.5;
  cursor: default;
}

.specular-button:disabled:active {
  transform: none;
}

.specular-button--sm {
  padding: 10px 20px;
  font-size: 0.875rem;
}

.specular-button--md {
  padding: 14px 28px;
  font-size: 1rem;
}

.specular-button--lg {
  padding: 18px 38px;
  font-size: 1.125rem;
}

.specular-button__label {
  position: relative;
  z-index: 2;
  display: inline-flex;
  align-items: center;
  gap: inherit;
}
</style>

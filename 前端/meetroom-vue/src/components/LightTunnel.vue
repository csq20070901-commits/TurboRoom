<template>
  <div ref="containerRef" class="light-tunnel-container" :class="className" :style="containerStyle">
    <canvas ref="canvasRef" class="light-tunnel-canvas" aria-hidden="true" />
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const props = defineProps({
  cableColor: { type: String, default: '#ffffff' },
  pulseColor: { type: String, default: '#ffffff' },
  tunnelColor: { type: String, default: '#ffffff' },
  tunnelOpacity: { type: Number, default: 0 },
  speed: { type: Number, default: 0.1 },
  flowDirection: { type: String, default: 'inward', validator: v => ['inward', 'outward'].includes(v) },
  pulseSpeed: { type: Number, default: 2 },
  pulseLength: { type: Number, default: 0.28 },
  pulseBlend: { type: Number, default: 0.6 },
  pulseWidth: { type: Number, default: 1 },
  pulseDensity: { type: Number, default: 0.5 },
  cableCount: { type: Number, default: 56 },
  thickness: { type: Number, default: 0.35 },
  rimWidth: { type: Number, default: 0.15 },
  waviness: { type: Number, default: 0.3 },
  sway: { type: Number, default: 0.5 },
  bend: { type: Number, default: 0.4 },
  size: { type: Number, default: 1 },
  centerX: { type: Number, default: 0 },
  centerY: { type: Number, default: 0 },
  glow: { type: Number, default: 1 },
  fadeNear: { type: Number, default: 0.5 },
  fadeFar: { type: Number, default: 2 },
  brightness: { type: Number, default: 1 },
  colorVariance: { type: Boolean, default: true },
  grain: { type: Boolean, default: false },
  grainIntensity: { type: Number, default: 0.05 },
  opacity: { type: Number, default: 1 },
  mouseInteraction: { type: Boolean, default: true },
  mouseStrength: { type: Number, default: 0.1 },
  clickSurge: { type: Boolean, default: true },
  intro: { type: Boolean, default: true },
  variation: { type: Number, default: 0 },
  lightMode: { type: Boolean, default: false },
  dpr: { type: Number, default: undefined },
  className: { type: String, default: '' },
  style: { type: Object, default: undefined }
})

const containerRef = ref(null)
const canvasRef = ref(null)

const INTRO_SECONDS = 1.8
const TAU = 6.2831853

const VERTEX = `
attribute vec2 aPosition;
void main() {
  gl_Position = vec4(aPosition, 0.0, 1.0);
}`

const FRAGMENT = `
precision highp float;
uniform vec2 uResolution;
uniform vec2 uCenter;
uniform float uScale;
uniform float uRatio;
uniform float uTime;
uniform float uDrift;
uniform vec2 uSteer;
uniform float uBend;
uniform float uCount;
uniform float uSwing;
uniform float uWave;
uniform float uWidth;
uniform float uPulseWidth;
uniform float uHead;
uniform float uTail;
uniform float uChance;
uniform float uSpacing;
uniform float uPulseSpeed;
uniform float uFlow;
uniform float uHalo;
uniform float uGlow;
uniform float uBrightness;
uniform float uVariance;
uniform float uBody;
uniform float uFadeNear;
uniform float uFadeFar;
uniform vec3 uCable;
uniform vec3 uPulse;
uniform vec3 uTunnel;
uniform vec3 uSurge;
uniform float uReveal;
uniform float uGrain;
uniform float uSeed;
uniform float uLight;
uniform float uOpacity;

const float TAU = 6.2831853;

vec3 inkOf(vec3 color, float depthOfInk) {
  float luma = dot(color, vec3(0.2126, 0.7152, 0.0722));
  float peak = max(color.r, max(color.g, color.b));
  float chroma = (peak - min(color.r, min(color.g, color.b))) / max(peak, 0.001);
  return color * min(1.0, mix(depthOfInk, depthOfInk * 1.7, chroma) / max(luma, 0.001));
}

float hash(float n, float seed) {
  return fract(sin(n * 127.1 + seed * 311.7) * 43758.5453);
}

vec2 bendAt(float depth, float drift, float bend, vec2 steer) {
  float reach = 1.0 - exp(-max(depth, 0.0) * 0.55);
  vec2 wobble = vec2(sin(drift * 0.37 + depth * 0.31), cos(drift * 0.29 + depth * 0.23)) * bend;
  return (wobble + steer) * reach;
}

void main() {
  vec2 p = (gl_FragCoord.xy - 0.5 * uResolution - uCenter) / uScale;
  float depth = -log(max(length(p), 1e-5));
  vec2 q = p;
  for (int i = 0; i < 4; i++) {
    q = p - bendAt(depth, uDrift, uBend, uSteer);
    depth = -log(max(length(q), 1e-5));
  }
  float r = length(q);
  float px = r * uScale;
  float turn = atan(q.y, q.x) / TAU;
  float s = turn - uSwing - uWave * sin(depth * 1.2 + uDrift * 0.5);
  float cell = floor(s * uCount);
  float spacing = TAU * px / uCount;
  float near = smoothstep(0.55, 1.25, r);
  float width = uWidth * (1.0 + near * 1.2);
  float soft = uRatio * (0.55 + near * 0.9);
  float tight = uRatio * uHalo * (1.0 + near);
  float wide = tight * 4.5;

  float lines = 0.0;
  float body = 0.0;
  vec3 glow = vec3(0.0);
  float strokes = 0.0;
  for (int k = -3; k <= 3; k++) {
    float i = cell + float(k);
    float id = mod(i, uCount);
    float h1 = hash(id, uSeed);
    float h2 = hash(id + 17.0, uSeed);
    float h3 = hash(id + 41.0, uSeed);
    float h4 = hash(id + 73.0, uSeed);
    float center = (i + 0.5 + (h1 - 0.5) * 0.55) / uCount;
    center += 0.12 * sin(depth * (0.8 + h2 * 0.9) + uDrift * (0.4 + h3 * 0.5) + h4 * TAU) / uCount;
    float d = abs(s - center) * TAU * px;
    float shade = 1.0 + uVariance * (h3 - 0.5) * 0.9;
    float reach = smoothstep(spacing * 2.4, spacing * 1.4, d);

    lines += smoothstep(width + soft, width - soft, d) * shade;
    body += exp(-d / max(spacing * 0.22, 0.001)) * shade * reach;

    float x = depth * uSpacing + uTime * uPulseSpeed * (0.55 + h2 * 0.6) * uFlow + h4 * 7.0;
    float lead = (fract(x) - 0.5) / uSpacing * uFlow;
    float lit = step(hash(id * 3.7 + floor(x) * 13.1, uSeed), uChance);
    float limit = 0.5 / uSpacing;
    float head = exp(-pow(max(-lead, 0.0) / uHead, 2.0)) * smoothstep(-limit, -limit * 0.8, lead);
    float tail = exp(-max(lead, 0.0) / uTail) * smoothstep(limit, limit * 0.55, lead);
    float pulse = (lead < 0.0 ? head : tail) * lit;
    float ring = uSurge.y * exp(-pow((depth - uSurge.x) / uSurge.z, 2.0));
    pulse = max(pulse, ring);
    float hot = pulse * (lead < 0.0 ? 1.0 : exp(-max(lead, 0.0) / (uTail * 0.35)));

    float core = uPulseWidth * width + 0.35 * uRatio;
    float stroke = smoothstep(core + soft, core - soft, d);
    float bloom = (exp(-d / tight) * 0.7 + exp(-d / wide) * 0.18) * uGlow * reach;
    vec3 tint = mix(uPulse, vec3(1.0), hot * 0.55 * (1.0 - uLight));
    glow += tint * pulse * (stroke * 1.5 + bloom) * shade;
    strokes += pulse * stroke * shade;
  }

  float lod = smoothstep(4.0, 1.2, spacing);
  float average = min(2.0 * uWidth / max(spacing, 0.001), 1.0);
  lines = mix(lines, average, lod);
  glow = mix(glow, uPulse * average * 0.6 * uChance, lod);
  strokes = mix(strokes, average * 0.6 * uChance, lod);
  body = mix(body, 1.0, lod) * uBody;

  float rr = r * 0.25;
  float fade = smoothstep(0.0, uFadeNear, rr) * smoothstep(uFadeFar, uFadeFar * 0.55, rr);
  fade *= smoothstep(uReveal - 0.6, uReveal, depth);

  float grainVal = 1.0;
  if (uGrain > 0.0) {
    grainVal += (fract(sin(dot(gl_FragCoord.xy, vec2(12.9898, 78.233)) + uTime * 7.0) * 43758.5453) - 0.5) * uGrain * 4.0;
  }

  if (uLight > 0.5) {
    float hairline = clamp(lines * 0.26 + body * 0.2, 0.0, 1.0);
    float strokeVal = clamp(strokes * 1.6, 0.0, 1.0);
    float haze = clamp(length(glow) * 0.12, 0.0, 0.25);
    float ink = clamp(hairline + strokeVal + haze, 0.0, 1.0) * fade * uOpacity * grainVal;
    vec3 tone = mix(inkOf(uCable, 0.3), inkOf(uPulse, 0.16), clamp(strokeVal + haze * 2.0, 0.0, 1.0));
    tone = mix(tone, inkOf(uTunnel, 0.3), clamp(body * uBody, 0.0, 1.0) * 0.5);
    gl_FragColor = vec4(tone * ink, ink);
    return;
  }

  vec3 light = (uCable * lines * 0.12 + uTunnel * body * 0.35 + glow) * uBrightness * fade * grainVal;
  vec3 mapped = (1.0 - exp(-light * 1.4)) * uOpacity;
  gl_FragColor = vec4(mapped, max(mapped.r, max(mapped.g, mapped.b)));
}`

const clamp = (v, lo, hi) => Math.min(hi, Math.max(lo, v))

const hexToRgb = hex => {
  const m = hex.replace('#', '').match(/.{2}/g)
  return m ? m.map(c => parseInt(c, 16) / 255) : [1, 1, 1]
}

const containerStyle = ref({})

onMounted(() => {
  const container = containerRef.value
  const canvas = canvasRef.value
  if (!container || !canvas) return

  // 先确保 canvas 尺寸正确，再创建 WebGL 上下文
  const initWebGL = () => {
    const width = Math.max(1, container.clientWidth)
    const height = Math.max(1, container.clientHeight)
    const ratio = Math.min(props.dpr || window.devicePixelRatio || 1, 2)
    canvas.width = Math.max(1, Math.round(width * ratio))
    canvas.height = Math.max(1, Math.round(height * ratio))

    const gl = canvas.getContext('webgl', {
      alpha: true, premultipliedAlpha: true, antialias: false, depth: false, stencil: false
    })
    if (!gl) return null

    const compile = (type, src) => {
      const sh = gl.createShader(type)
      if (!sh) return null
      gl.shaderSource(sh, src)
      gl.compileShader(sh)
      if (gl.getShaderParameter(sh, gl.COMPILE_STATUS)) return sh
      const log = gl.getShaderInfoLog(sh)
      console.error('LightTunnel shader compile error:', type === gl.VERTEX_SHADER ? 'VERTEX' : 'FRAGMENT', log)
      gl.deleteShader(sh)
      return null
    }

    const vs = compile(gl.VERTEX_SHADER, VERTEX)
    const fs = compile(gl.FRAGMENT_SHADER, FRAGMENT)
    if (!vs || !fs) {
      console.error('LightTunnel shader compilation failed:', {
        vs: vs ? 'ok' : gl.getShaderInfoLog(gl.createShader(gl.VERTEX_SHADER)),
        fs: fs ? 'ok' : 'failed'
      })
      return null
    }

    const program = gl.createProgram()
    if (!program) return null
    gl.attachShader(program, vs)
    gl.attachShader(program, fs)
    gl.bindAttribLocation(program, 0, 'aPosition')
    gl.linkProgram(program)
    const linkStatus = gl.getProgramParameter(program, gl.LINK_STATUS)
    if (!linkStatus) {
      console.error('LightTunnel program link failed:', gl.getProgramInfoLog(program))
      gl.deleteProgram(program)
      return null
    }
    gl.deleteShader(vs)
    gl.deleteShader(fs)

    const u = {}
    const count = gl.getProgramParameter(program, gl.ACTIVE_UNIFORMS)
    for (let i = 0; i < count; i++) {
      const info = gl.getActiveUniform(program, i)
      if (info) u[info.name] = gl.getUniformLocation(program, info.name)
    }

    const buffer = gl.createBuffer()
    gl.bindBuffer(gl.ARRAY_BUFFER, buffer)
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array([-1,-1, 3,-1, -1,3]), gl.STATIC_DRAW)
    gl.enableVertexAttribArray(0)
    gl.vertexAttribPointer(0, 2, gl.FLOAT, false, 0, 0)

    return { gl, program, u, buffer }
  }

  const webgl = initWebGL()
  if (!webgl) return
  const { gl, program, u, buffer } = webgl

  const s = {
    cableColor: props.cableColor,
    pulseColor: props.pulseColor,
    tunnelColor: props.tunnelColor,
    tunnelOpacity: clamp(props.tunnelOpacity, 0, 1),
    speed: Math.max(0, props.speed),
    flow: props.flowDirection === 'outward' ? 1 : -1,
    pulseSpeed: Math.max(0, props.pulseSpeed),
    pulseLength: clamp(props.pulseLength, 0.02, 1),
    pulseBlend: clamp(props.pulseBlend, 0, 1),
    pulseWidth: clamp(props.pulseWidth, 0.1, 3),
    pulseDensity: clamp(props.pulseDensity, 0, 1),
    cableCount: clamp(Math.round(props.cableCount), 4, 200),
    thickness: clamp(props.thickness, 0.05, 2),
    rimWidth: clamp(props.rimWidth, 0, 1),
    waviness: Math.max(0, props.waviness),
    sway: Math.max(0, props.sway),
    bend: Math.max(0, props.bend),
    size: Math.max(0.05, props.size),
    centerX: props.centerX,
    centerY: props.centerY,
    glow: Math.max(0, props.glow),
    fadeNear: Math.max(0.001, props.fadeNear),
    fadeFar: Math.max(0.01, props.fadeFar),
    brightness: Math.max(0, props.brightness),
    colorVariance: props.colorVariance,
    grain: props.grain,
    grainIntensity: Math.max(0, props.grainIntensity),
    opacity: clamp(props.opacity, 0, 1),
    mouseInteraction: props.mouseInteraction,
    mouseStrength: Math.max(0, props.mouseStrength),
    clickSurge: props.clickSurge,
    intro: props.intro,
    variation: props.variation,
    lightMode: props.lightMode,
    dpr: props.dpr
  }

  const reduce = window.matchMedia?.('(prefers-reduced-motion: reduce)').matches ?? false
  const pointer = { x: 0, y: 0, inside: false }
  const steer = { x: 0, y: 0 }
  const surge = { age: 10 }
  let time = 0, drift = 0
  let reveal = s.intro && !reduce ? 0 : 1
  let raf = 0, last = 0, visible = true

  const render = () => {
    const width = canvas.width, height = canvas.height
    const ratio = width / Math.max(1, container.clientWidth)
    const minSide = Math.min(width, height)
    const flow = s.flow
    const travel = surge.age * 2.6
    const surgeDepth = flow > 0 ? 3.4 - travel : -0.6 + travel
    const surgeLevel = surge.age < 2.4 ? Math.min(1, surge.age / 0.12) * Math.exp(-surge.age * 1.1) : 0
    const eased = reveal * reveal * (3 - 2 * reveal)

    gl.viewport(0, 0, width, height)
    gl.useProgram(program)
    gl.uniform2f(u.uResolution, width, height)
    gl.uniform2f(u.uCenter, s.centerX * minSide, s.centerY * minSide)
    gl.uniform1f(u.uScale, minSide * 0.5 * s.size)
    gl.uniform1f(u.uRatio, ratio)
    gl.uniform1f(u.uTime, time)
    gl.uniform1f(u.uDrift, drift)
    gl.uniform2f(u.uSteer, steer.x, steer.y)
    gl.uniform1f(u.uBend, s.bend * 0.45)
    gl.uniform1f(u.uCount, s.cableCount)
    gl.uniform1f(u.uSwing, (Math.sin(drift * 0.9) * s.sway * 0.5) / (Math.PI * 2))
    gl.uniform1f(u.uWave, s.waviness * 0.15)
    gl.uniform1f(u.uWidth, (0.25 + s.thickness * 1.5) * ratio)
    gl.uniform1f(u.uPulseWidth, 0.6 + s.pulseWidth * 0.9)
    const length = s.pulseLength * 1.2
    gl.uniform1f(u.uHead, length * (0.08 + s.pulseBlend * 0.22))
    gl.uniform1f(u.uTail, length * (0.5 + s.pulseBlend * 0.7))
    gl.uniform1f(u.uChance, s.pulseDensity)
    gl.uniform1f(u.uSpacing, 0.4)
    gl.uniform1f(u.uPulseSpeed, s.pulseSpeed * 4)
    gl.uniform1f(u.uFlow, flow)
    gl.uniform1f(u.uHalo, 1.2 + s.rimWidth * 8)
    gl.uniform1f(u.uGlow, s.glow)
    gl.uniform1f(u.uBrightness, s.brightness)
    gl.uniform1f(u.uVariance, s.colorVariance ? 1 : 0)
    gl.uniform1f(u.uBody, s.tunnelOpacity)
    gl.uniform1f(u.uFadeNear, s.fadeNear)
    gl.uniform1f(u.uFadeFar, s.fadeFar)
    gl.uniform3fv(u.uCable, hexToRgb(s.cableColor))
    gl.uniform3fv(u.uPulse, hexToRgb(s.pulseColor))
    gl.uniform3fv(u.uTunnel, hexToRgb(s.tunnelColor))
    gl.uniform3f(u.uSurge, surgeDepth, surgeLevel, 0.22)
    gl.uniform1f(u.uReveal, 1.7 - eased * 3.6)
    gl.uniform1f(u.uGrain, s.grain ? s.grainIntensity : 0)
    gl.uniform1f(u.uSeed, s.variation)
    gl.uniform1f(u.uLight, s.lightMode ? 1 : 0)
    gl.uniform1f(u.uOpacity, s.opacity)
    gl.drawArrays(gl.TRIANGLES, 0, 3)
  }

  const frame = now => {
    raf = 0
    if (!visible || document.hidden) return
    const dt = last ? clamp((now - last) / 1000, 0.0005, 0.05) : 1 / 60
    last = now
    const motion = reduce ? 0 : 1
    time += dt * s.speed * motion
    drift += dt * (s.speed + 0.2) * motion
    surge.age += dt
    if (s.intro && !reduce && reveal < 1) reveal = Math.min(1, reveal + dt / INTRO_SECONDS)
    else if (!s.intro || reduce) reveal = 1

    const engaged = s.mouseInteraction && pointer.inside && !reduce
    const reach = s.mouseStrength * 2.5
    const target = engaged ? [pointer.x * reach, pointer.y * reach] : [0, 0]
    const follow = 1 - Math.exp(-dt / 0.6)
    steer.x += (target[0] - steer.x) * follow
    steer.y += (target[1] - steer.y) * follow

    render()
    const settled = Math.abs(target[0] - steer.x) < 0.0005 && Math.abs(target[1] - steer.y) < 0.0005
    const busy = motion > 0 || !settled || reveal < 1 || surge.age < 2.4
    if (busy) raf = requestAnimationFrame(frame)
    else last = 0
  }

  const wake = () => { if (!raf && visible && !document.hidden) raf = requestAnimationFrame(frame) }

  const resize = () => {
    const width = Math.max(1, container.clientWidth)
    const height = Math.max(1, container.clientHeight)
    const ratio = Math.min(s.dpr || window.devicePixelRatio || 1, 2)
    canvas.width = Math.max(1, Math.round(width * ratio))
    canvas.height = Math.max(1, Math.round(height * ratio))
    render()
    wake()
  }

  const locate = event => {
    const rect = container.getBoundingClientRect()
    const x = event.clientX - rect.left, y = event.clientY - rect.top
    pointer.inside = x >= 0 && y >= 0 && x <= rect.width && y <= rect.height
    const scale = Math.min(rect.width, rect.height) * 0.5
    pointer.x = (x - rect.width / 2) / Math.max(1, scale)
    pointer.y = (rect.height / 2 - y) / Math.max(1, scale)
  }

  const onPointerMove = event => { locate(event); wake() }
  const onPointerDown = event => {
    locate(event)
    if (!pointer.inside || !s.clickSurge || reduce) return
    surge.age = 0; wake()
  }
  const onPointerUp = event => { if (event.pointerType !== 'mouse') pointer.inside = false }
  const onPointerLeave = () => { pointer.inside = false }
  const onVisibility = () => { last = 0; wake() }

  const resizeObs = new ResizeObserver(resize)
  resizeObs.observe(container)
  const intersectObs = new IntersectionObserver(entries => {
    visible = entries.some(e => e.isIntersecting)
    if (visible) { last = 0; wake() }
  })
  intersectObs.observe(container)
  window.addEventListener('pointermove', onPointerMove, { passive: true })
  window.addEventListener('pointerdown', onPointerDown, { passive: true })
  window.addEventListener('pointerup', onPointerUp, { passive: true })
  window.addEventListener('pointercancel', onPointerUp, { passive: true })
  document.documentElement.addEventListener('pointerleave', onPointerLeave)
  document.addEventListener('visibilitychange', onVisibility)

  // 延迟确保布局完成后再 resize
  setTimeout(() => {
    resize()
    // 再次确保
    setTimeout(resize, 100)
  }, 50)

  onUnmounted(() => {
    cancelAnimationFrame(raf)
    resizeObs.disconnect()
    intersectObs.disconnect()
    window.removeEventListener('pointermove', onPointerMove)
    window.removeEventListener('pointerdown', onPointerDown)
    window.removeEventListener('pointerup', onPointerUp)
    window.removeEventListener('pointercancel', onPointerUp)
    document.documentElement.removeEventListener('pointerleave', onPointerLeave)
    document.removeEventListener('visibilitychange', onVisibility)
    gl.deleteProgram(program)
    gl.deleteBuffer(buffer)
    gl.getExtension('WEBGL_lose_context')?.loseContext()
  })
})
</script>

<style scoped>
.light-tunnel-container {
  position: relative;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.light-tunnel-canvas {
  position: absolute;
  inset: 0;
  display: block;
  width: 100%;
  height: 100%;
}
</style>

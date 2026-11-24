import { ref } from 'vue'

const KEY = 'turboroom.admin.activity'
const items = ref(read())

function read() {
  try {
    const value = JSON.parse(sessionStorage.getItem(KEY) || '[]')
    return Array.isArray(value) ? value : []
  } catch {
    return []
  }
}

export function useAdminActivity() {
  function record(label, path) {
    if (!label || !path) return
    items.value = [
      { label, path, time: Date.now() },
      ...items.value.filter((entry) => entry.path !== path)
    ].slice(0, 6)
    try {
      sessionStorage.setItem(KEY, JSON.stringify(items.value))
    } catch {}
  }

  function formatTime(time) {
    const date = new Date(time)
    if (Number.isNaN(date.getTime())) return ''
    const hm = `${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
    const today = new Date()
    if (date.toDateString() === today.toDateString()) return hm
    return `${date.getMonth() + 1}/${date.getDate()} ${hm}`
  }

  return { items, record, formatTime }
}

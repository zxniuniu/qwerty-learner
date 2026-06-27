<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref } from 'vue'
import type { Article } from '../api/typing'
import { getArticle, listArticles, saveSpeed } from '../api/typing'

const ZH_SKIP = /[\s，。！？；：、""''（）《》【】—…·,.!?;:'"()-]/

function isAutoSkip(ch: string, lang: string): boolean {
  if (lang === 'zh') return ZH_SKIP.test(ch)
  return ch === '\n'
}
function skipForward(text: string, idx: number, lang: string): number {
  let i = idx
  while (i < text.length && isAutoSkip(text[i], lang)) i++
  return i
}

const articles = ref<Article[]>([])
const current = ref<Article | null>(null)
const content = ref('')
const language = ref('zh')

const inputIndex = ref(0)
const correctCount = ref(0)
const wrongCount = ref(0)
const hasError = ref(false)
const startTime = ref<number | null>(null)
const elapsed = ref(0)
const finished = ref(false)
const inputRef = ref<HTMLTextAreaElement | null>(null)

let timer: number | undefined

const chars = computed(() => Array.from(content.value))
const stats = computed(() => {
  const total = correctCount.value + wrongCount.value
  const minutes = elapsed.value / 60
  const cpm = minutes > 0 ? Math.round(correctCount.value / minutes) : 0
  const accuracy = total > 0 ? Math.round((correctCount.value / total) * 100) : 100
  const progress = content.value.length > 0 ? Math.round((inputIndex.value / content.value.length) * 100) : 0
  const sec = Math.floor(elapsed.value % 60)
  const min = Math.floor(elapsed.value / 60)
  const timeStr = `${min < 10 ? '0' + min : min}:${sec < 10 ? '0' + sec : sec}`
  return { cpm, accuracy, progress, timeStr }
})

function charClass(i: number): string {
  if (i < inputIndex.value) return 'ch-done'
  if (i === inputIndex.value) return hasError.value ? 'ch-error' : 'ch-current'
  return 'ch-todo'
}

function reset() {
  inputIndex.value = skipForward(content.value, 0, language.value)
  correctCount.value = 0
  wrongCount.value = 0
  hasError.value = false
  startTime.value = null
  elapsed.value = 0
  finished.value = false
  focusInput()
}

async function selectArticle(a: Article) {
  // 列表不带正文，按需拉取详情
  const full = a.content ? a : await getArticle(a.id as string)
  current.value = full
  content.value = full.content ?? ''
  language.value = full.language
  reset()
}

function inputChars(typedChars: string[]) {
  if (finished.value) return
  if (startTime.value === null) startTime.value = Date.now()
  let idx = inputIndex.value
  for (const typed of typedChars) {
    if (idx >= content.value.length) break
    const expected = content.value[idx]
    const match = language.value === 'en' ? typed.toLowerCase() === expected.toLowerCase() : typed === expected
    if (match) {
      correctCount.value++
      hasError.value = false
      idx = skipForward(content.value, idx + 1, language.value)
      if (idx >= content.value.length) {
        finished.value = true
        break
      }
    } else {
      wrongCount.value++
      hasError.value = true
      break
    }
  }
  inputIndex.value = idx
  if (finished.value) uploadResult()
}

function onKeydown(e: KeyboardEvent) {
  if (e.isComposing || e.keyCode === 229) return
  if (language.value !== 'en') return
  if (e.key.length === 1) {
    inputChars([e.key])
    e.preventDefault()
  }
}
function onCompositionEnd(e: CompositionEvent) {
  if (language.value !== 'zh') return
  if (e.data) inputChars(Array.from(e.data))
  if (inputRef.value) inputRef.value.value = ''
}
function onInput() {
  if (inputRef.value) inputRef.value.value = ''
}
function focusInput() {
  setTimeout(() => inputRef.value?.focus(), 0)
}

function uploadResult() {
  saveSpeed({
    mode: 'article',
    refId: current.value?.id,
    refName: current.value?.title,
    cpm: stats.value.cpm,
    accuracy: stats.value.accuracy,
    duration: Math.round(elapsed.value),
    charCount: correctCount.value + wrongCount.value,
    wrongCount: wrongCount.value,
    userName: localStorage.getItem('typing_user_name') || '匿名用户',
  }).catch(() => undefined)
}

onMounted(async () => {
  try {
    articles.value = await listArticles()
    if (articles.value.length > 0) await selectArticle(articles.value[0])
  } catch {
    // 后端未启动时给一段本地占位文本
    current.value = { id: 'local', title: '示例', language: 'zh', content: '请先启动后端服务以加载文章。这里是一段本地占位文本，可直接练习。' }
    content.value = current.value.content as string
    language.value = 'zh'
    reset()
  }
  timer = window.setInterval(() => {
    if (startTime.value !== null && !finished.value) elapsed.value = (Date.now() - startTime.value) / 1000
  }, 200)
})
onUnmounted(() => clearInterval(timer))
</script>

<template>
  <h2>打字练习</h2>

  <div class="card">
    <div style="margin-bottom: 12px">
      <span
        v-for="a in articles"
        :key="a.id"
        class="chip"
        :class="{ active: current && a.id === current.id }"
        @click="selectArticle(a)"
      >
        {{ a.title }} <small>{{ a.language === 'zh' ? '中' : 'EN' }}</small>
      </span>
    </div>

    <div class="stat-row" style="margin: 16px 0">
      <div class="stat"><div class="v">{{ stats.timeStr }}</div><div class="l">时间</div></div>
      <div class="stat"><div class="v">{{ stats.cpm }}</div><div class="l">字符/分</div></div>
      <div class="stat"><div class="v">{{ stats.accuracy }}%</div><div class="l">正确率</div></div>
      <div class="stat"><div class="v">{{ stats.progress }}%</div><div class="l">进度</div></div>
    </div>
  </div>

  <div class="card" @click="focusInput">
    <p class="article-text">
      <span v-for="(ch, i) in chars" :key="i" :class="charClass(i)">{{ ch }}</span>
    </p>

    <div v-if="finished" style="margin-top: 16px; display: flex; align-items: center; justify-content: space-between">
      <span class="ch-done">完成！字符/分 {{ stats.cpm }}，正确率 {{ stats.accuracy }}%</span>
      <button class="btn" @click.stop="reset">再来一遍</button>
    </div>

    <p v-if="language === 'zh'" style="color: var(--muted); font-size: 12px; margin-top: 10px">
      中文请开启系统输入法（拼音 / 五笔），标点与空格会自动跳过
    </p>

    <textarea
      ref="inputRef"
      class="hidden-input"
      autofocus
      spellcheck="false"
      @keydown="onKeydown"
      @input="onInput"
      @compositionend="onCompositionEnd"
      @blur="focusInput"
    ></textarea>
  </div>
</template>

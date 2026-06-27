import VirtualKeyboard from '../VirtualKeyboard'
import type { Article } from '@/resources/article'
import { articles } from '@/resources/article'
import { isIgnoreCaseAtom, nextExpectedKeyAtom } from '@/store'
import { useAtomValue, useSetAtom } from 'jotai'
import type React from 'react'
import { useCallback, useEffect, useMemo, useReducer, useRef, useState } from 'react'

// 中文标点与空白：文章模式下自动跳过，用户只需输入汉字
const ZH_SKIP = /[\s，。！？；：、""''（）《》【】—…·,.!?;:'"()-]/

function isAutoSkip(ch: string, language: Article['language']): boolean {
  if (language === 'zh') return ZH_SKIP.test(ch)
  // 拉丁文：仅跳过换行，空格与标点都需要真实输入
  return ch === '\n'
}

function skipForward(text: string, idx: number, language: Article['language']): number {
  let i = idx
  while (i < text.length && isAutoSkip(text[i], language)) i++
  return i
}

type TypingState = {
  inputIndex: number
  correctCount: number
  wrongCount: number
  hasError: boolean
  startTime: number | null
  isFinished: boolean
}

type TypingAction =
  | { type: 'reset'; content: string; language: Article['language'] }
  | { type: 'input'; chars: string[]; content: string; language: Article['language']; isIgnoreCase: boolean; now: number }

function initState(article: Article): TypingState {
  return {
    inputIndex: skipForward(article.content, 0, article.language),
    correctCount: 0,
    wrongCount: 0,
    hasError: false,
    startTime: null,
    isFinished: false,
  }
}

// 纯 reducer：一次处理可能的多个字符（中文输入法一次组合可产生多字），StrictMode 下也安全
function reducer(state: TypingState, action: TypingAction): TypingState {
  switch (action.type) {
    case 'reset':
      return {
        inputIndex: skipForward(action.content, 0, action.language),
        correctCount: 0,
        wrongCount: 0,
        hasError: false,
        startTime: null,
        isFinished: false,
      }
    case 'input': {
      if (state.isFinished) return state
      let { inputIndex, correctCount, wrongCount, hasError } = state
      let isFinished = false
      const startTime = state.startTime ?? action.now
      for (const typed of action.chars) {
        if (inputIndex >= action.content.length) break
        const expected = action.content[inputIndex]
        const match = action.isIgnoreCase ? typed.toLowerCase() === expected.toLowerCase() : typed === expected
        if (match) {
          correctCount += 1
          hasError = false
          inputIndex = skipForward(action.content, inputIndex + 1, action.language)
          if (inputIndex >= action.content.length) {
            isFinished = true
            break
          }
        } else {
          wrongCount += 1
          hasError = true
          break
        }
      }
      return { inputIndex, correctCount, wrongCount, hasError, startTime, isFinished }
    }
    default:
      return state
  }
}

export default function ArticleTyping() {
  const isIgnoreCase = useAtomValue(isIgnoreCaseAtom)
  const setNextExpectedKey = useSetAtom(nextExpectedKeyAtom)

  const [article, setArticle] = useState<Article>(articles[0])
  const content = article.content

  const [state, dispatch] = useReducer(reducer, article, initState)
  const { inputIndex, correctCount, wrongCount, hasError, startTime, isFinished } = state

  const [elapsed, setElapsed] = useState(0)
  const textareaRef = useRef<HTMLTextAreaElement>(null)

  const selectArticle = useCallback((next: Article) => {
    setArticle(next)
    dispatch({ type: 'reset', content: next.content, language: next.language })
    setElapsed(0)
    textareaRef.current?.focus()
  }, [])

  // 计时
  useEffect(() => {
    if (startTime === null || isFinished) return
    const timer = window.setInterval(() => setElapsed((Date.now() - startTime) / 1000), 200)
    return () => window.clearInterval(timer)
  }, [startTime, isFinished])

  // 维护虚拟键盘的下一键高亮：仅拉丁文可预测击键
  useEffect(() => {
    if (isFinished || article.language !== 'en') {
      setNextExpectedKey('')
      return
    }
    setNextExpectedKey(content[inputIndex] ?? '')
  }, [content, inputIndex, isFinished, article.language, setNextExpectedKey])

  useEffect(() => () => setNextExpectedKey(''), [setNextExpectedKey])

  // 自动聚焦
  useEffect(() => {
    textareaRef.current?.focus()
  }, [article])

  const inputChars = useCallback(
    (chars: string[]) => {
      dispatch({ type: 'input', chars, content, language: article.language, isIgnoreCase, now: Date.now() })
    },
    [content, article.language, isIgnoreCase],
  )

  const onKeyDown = useCallback(
    (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
      const ne = e.nativeEvent as KeyboardEvent
      if (ne.isComposing || ne.keyCode === 229) return // 输入法组合中，交给 compositionend
      if (article.language !== 'en') return
      if (e.key.length === 1) {
        inputChars([e.key])
        e.preventDefault()
      }
    },
    [article.language, inputChars],
  )

  const onCompositionEnd = useCallback(
    (e: React.CompositionEvent<HTMLTextAreaElement>) => {
      if (article.language !== 'zh') return
      const data = (e.nativeEvent as CompositionEvent).data
      if (data) inputChars(Array.from(data))
      if (textareaRef.current) textareaRef.current.value = ''
    },
    [article.language, inputChars],
  )

  const onInput = useCallback(() => {
    // 输入内容已逐字符处理，清空隐藏输入框防止累积
    if (textareaRef.current) textareaRef.current.value = ''
  }, [])

  const stats = useMemo(() => {
    const total = correctCount + wrongCount
    const minutes = elapsed / 60
    const cpm = minutes > 0 ? Math.round(correctCount / minutes) : 0
    const accuracy = total > 0 ? Math.round((correctCount / total) * 100) : 100
    const progress = content.length > 0 ? Math.round((inputIndex / content.length) * 100) : 0
    const seconds = Math.floor(elapsed % 60)
    const mins = Math.floor(elapsed / 60)
    const timeStr = `${mins < 10 ? '0' + mins : mins}:${seconds < 10 ? '0' + seconds : seconds}`
    return { cpm, accuracy, progress, timeStr }
  }, [correctCount, wrongCount, elapsed, inputIndex, content.length])

  return (
    <div className="flex w-full flex-col items-center gap-5" onClick={() => textareaRef.current?.focus()}>
      {/* 文章选择 */}
      <div className="flex flex-wrap justify-center gap-2">
        {articles.map((a) => (
          <button
            key={a.id}
            type="button"
            onClick={(e) => {
              e.stopPropagation()
              selectArticle(a)
            }}
            className={`rounded-md px-3 py-1 text-sm transition-colors ${
              a.id === article.id
                ? 'bg-indigo-500 text-white'
                : 'bg-gray-100 text-gray-600 hover:bg-gray-200 dark:bg-gray-700 dark:text-gray-300'
            }`}
          >
            {a.title}
            <span className="ml-1 text-xs opacity-70">{a.language === 'zh' ? '中' : 'EN'}</span>
          </button>
        ))}
      </div>

      {/* 统计仪表盘 */}
      <div className="flex w-4/5 max-w-3xl justify-around rounded-xl bg-white py-4 shadow-md dark:bg-gray-800">
        <Stat label="时间" value={stats.timeStr} />
        <Stat label="字符/分" value={stats.cpm + ''} />
        <Stat label="正确率" value={stats.accuracy + '%'} />
        <Stat label="进度" value={stats.progress + '%'} />
      </div>

      {/* 文章正文 */}
      <div className="relative w-4/5 max-w-3xl rounded-xl bg-white p-6 shadow-sm dark:bg-gray-800">
        <p className="whitespace-pre-wrap break-words text-2xl leading-relaxed tracking-wide" lang={article.language}>
          {Array.from(content).map((ch, i) => {
            let cls = 'text-gray-400 dark:text-gray-500'
            if (i < inputIndex) cls = 'text-green-600 dark:text-green-400'
            else if (i === inputIndex) cls = hasError ? 'rounded bg-red-400 text-white' : 'rounded bg-indigo-200 dark:bg-indigo-700'
            return (
              <span key={i} className={cls}>
                {ch}
              </span>
            )
          })}
        </p>
        {isFinished && (
          <div className="mt-4 flex items-center justify-between border-t border-gray-100 pt-4 dark:border-gray-700">
            <span className="text-sm text-green-600 dark:text-green-400">
              完成！字符/分 {stats.cpm}，正确率 {stats.accuracy}%
            </span>
            <button type="button" onClick={() => selectArticle(article)} className="my-btn-primary">
              再来一遍
            </button>
          </div>
        )}

        {/* 隐藏输入框：承接键盘与输入法事件 */}
        <textarea
          className="absolute left-0 top-0 h-0 w-0 appearance-none overflow-hidden border-0 p-0 opacity-0 focus:outline-none"
          ref={textareaRef}
          autoFocus
          spellCheck={false}
          onKeyDown={onKeyDown}
          onInput={onInput}
          onCompositionEnd={onCompositionEnd}
          onBlur={() => {
            if (!isFinished) setTimeout(() => textareaRef.current?.focus(), 0)
          }}
        />
      </div>

      {article.language === 'zh' && <p className="-mt-2 text-xs text-gray-400">请开启系统输入法（拼音 / 五笔），标点与空格会自动跳过</p>}

      {/* 虚拟键盘 */}
      <VirtualKeyboard />
    </div>
  )
}

function Stat({ label, value }: { label: string; value: string }) {
  return (
    <div className="flex flex-col items-center">
      <span className="text-2xl font-bold text-indigo-500">{value}</span>
      <span className="mt-1 text-xs text-gray-500 dark:text-gray-400">{label}</span>
    </div>
  )
}

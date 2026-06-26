import type { WordUpdateAction } from '../InputHandler'
import { TypingContext } from '@/pages/Typing/store'
import type { CompositionEvent, FormEvent } from 'react'
import { useCallback, useContext, useEffect, useRef } from 'react'

/**
 * 中文输入处理器
 *
 * 与英文 TextAreaHandler 不同，中文需要依赖输入法（IME）将拼音组合为汉字，
 * 因此这里允许 composition（输入法候选）流程，并在 compositionend 时
 * 取出最终组合好的汉字，逐字符交给上层做匹配。
 */
export default function ChineseInputHandler({ updateInput }: { updateInput: (updateObj: WordUpdateAction) => void }) {
  const textareaRef = useRef<HTMLTextAreaElement>(null)
  // 是否正处于输入法组合过程中
  const isComposingRef = useRef(false)
  // compositionend 刚刚触发，用于忽略部分浏览器（如 Firefox）在 compositionend 之后
  // 紧跟着补发的一次 input 事件，避免重复输入
  const justComposedRef = useRef(false)
  // eslint-disable-next-line  @typescript-eslint/no-non-null-assertion
  const { state } = useContext(TypingContext)!

  useEffect(() => {
    if (!textareaRef.current) return

    if (state.isTyping) {
      textareaRef.current.focus()
    } else {
      textareaRef.current.blur()
    }
  }, [state.isTyping])

  const clearTextarea = useCallback(() => {
    if (textareaRef.current) {
      textareaRef.current.value = ''
    }
  }, [])

  const emit = useCallback(
    (text: string, event: FormEvent<HTMLTextAreaElement> | CompositionEvent<HTMLTextAreaElement>) => {
      // 一次组合可能产生多个汉字（如 "nihao" -> "你好"），逐字符上报
      for (const char of Array.from(text)) {
        updateInput({ type: 'add', value: char, event: event as FormEvent<HTMLTextAreaElement> })
      }
    },
    [updateInput],
  )

  const onInput = useCallback(
    (e: FormEvent<HTMLTextAreaElement>) => {
      const nativeEvent = e.nativeEvent as InputEvent
      // 组合过程中产生的 input 事件直接忽略，等待 compositionend
      if (nativeEvent.isComposing || isComposingRef.current) return
      // 忽略 compositionend 之后紧跟的补发 input 事件
      if (justComposedRef.current) {
        clearTextarea()
        return
      }
      if (nativeEvent.data !== null) {
        emit(nativeEvent.data, e)
      }
      clearTextarea()
    },
    [clearTextarea, emit],
  )

  const onCompositionStart = useCallback(() => {
    isComposingRef.current = true
  }, [])

  const onCompositionEnd = useCallback(
    (e: CompositionEvent<HTMLTextAreaElement>) => {
      isComposingRef.current = false
      const data = (e.nativeEvent as globalThis.CompositionEvent).data
      if (data) {
        emit(data, e)
      }
      clearTextarea()
      // 标记刚结束组合，下一拍清除，用于吞掉浏览器补发的 input 事件
      justComposedRef.current = true
      setTimeout(() => {
        justComposedRef.current = false
      }, 0)
    },
    [clearTextarea, emit],
  )

  const onBlur = useCallback(() => {
    if (!textareaRef.current) return

    if (state.isTyping) {
      textareaRef.current.focus()
    }
  }, [state.isTyping])

  return (
    <textarea
      className="absolute left-0 top-0 m-0 h-0 w-0 appearance-none overflow-hidden border-0 p-0 focus:outline-none"
      ref={textareaRef}
      autoFocus
      spellCheck="false"
      onInput={onInput}
      onBlur={onBlur}
      onCompositionStart={onCompositionStart}
      onCompositionEnd={onCompositionEnd}
    ></textarea>
  )
}

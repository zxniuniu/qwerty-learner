type PinyinNotationProps = {
  // 以空格分隔、与汉字一一对应的拼音，如 "nǐ hǎo"
  pinyin: string
}

/**
 * 中文拼音标注：在汉字上方整体展示拼音，方便练习时确认读音与输入。
 */
export default function PinyinNotation({ pinyin }: PinyinNotationProps) {
  return (
    <div className="mx-auto flex h-12 items-end justify-center">
      <span className="font-sans text-2xl tracking-wide text-gray-600 dark:text-gray-400">{pinyin}</span>
    </div>
  )
}

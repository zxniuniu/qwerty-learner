type WubiNotationProps = {
  // 与汉字一一对应的五笔编码，如 ["WQIY", "VBG"]
  codes: string[]
}

/**
 * 五笔编码标注：在汉字上方展示每个字的五笔（86）编码，方便练习五笔打字。
 */
export default function WubiNotation({ codes }: WubiNotationProps) {
  return (
    <div className="mx-auto flex h-12 items-end justify-center gap-3">
      {codes.map((code, index) => (
        <span key={`${index}-${code}`} className="font-mono text-2xl uppercase tracking-widest text-gray-600 dark:text-gray-400">
          {code || '·'}
        </span>
      ))}
    </div>
  )
}

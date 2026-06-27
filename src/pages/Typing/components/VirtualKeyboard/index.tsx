import { FINGER_COLOR, FINGER_NAME, FINGER_OF_KEY, KEYBOARD_ROWS, WUBI_86_MNEMONIC, WUBI_KEY_NAME } from '@/constants/keyboard'
import { chineseHintTypeAtom, currentDictInfoAtom, nextExpectedKeyAtom, wubiVersionAtom } from '@/store'
import { useAtomValue } from 'jotai'
import { useMemo } from 'react'

export default function VirtualKeyboard() {
  const nextKey = useAtomValue(nextExpectedKeyAtom)
  const currentDictInfo = useAtomValue(currentDictInfoAtom)
  const chineseHintType = useAtomValue(chineseHintTypeAtom)
  const wubiVersion = useAtomValue(wubiVersionAtom)

  // 是否在键位上叠加五笔字根（中文 + 五笔提示时）
  const showWubiRadical = currentDictInfo.language === 'zh' && chineseHintType === 'wubi'

  const normalizedNextKey = useMemo(() => (nextKey ? nextKey.toUpperCase() : ''), [nextKey])

  return (
    <div className="flex select-none flex-col items-center gap-1.5">
      {KEYBOARD_ROWS.map((row, rowIndex) => (
        <div
          key={rowIndex}
          className="flex justify-center gap-1.5"
          // 错落排列，模拟真实键盘的行偏移
          style={{ paddingLeft: `${rowIndex * 1.4}rem` }}
        >
          {row.map((key) => {
            const finger = FINGER_OF_KEY[key]
            const isNext = normalizedNextKey === key
            const radical = WUBI_KEY_NAME[key]
            const fingerColor = finger ? FINGER_COLOR[finger] : 'bg-gray-200 dark:bg-gray-700'
            const tooltip = [
              finger ? FINGER_NAME[finger] : '',
              showWubiRadical && WUBI_86_MNEMONIC[key] ? `五笔字根：${WUBI_86_MNEMONIC[key]}` : '',
            ]
              .filter(Boolean)
              .join('\n')

            return (
              <div
                key={key}
                title={tooltip || undefined}
                className={`relative flex h-11 w-11 flex-col items-center justify-center rounded-md text-sm font-medium shadow-sm transition-all duration-150 ${
                  isNext
                    ? 'scale-110 bg-indigo-500 text-white ring-2 ring-indigo-400 ring-offset-1 dark:ring-offset-gray-900'
                    : `${fingerColor} text-gray-700 dark:text-gray-200`
                }`}
              >
                <span className="leading-none">{key}</span>
                {showWubiRadical && radical && (
                  <span className={`mt-0.5 text-[11px] leading-none ${isNext ? 'text-indigo-100' : 'text-gray-500 dark:text-gray-400'}`}>
                    {radical}
                  </span>
                )}
              </div>
            )
          })}
        </div>
      ))}

      {/* 空格键 */}
      <div className="mt-0.5 flex justify-center">
        <div
          className={`flex h-9 w-72 items-center justify-center rounded-md text-xs font-medium shadow-sm ${
            normalizedNextKey === ' '
              ? 'scale-105 bg-indigo-500 text-white ring-2 ring-indigo-400'
              : 'bg-gray-200 text-gray-500 dark:bg-gray-700 dark:text-gray-400'
          }`}
        >
          space
        </div>
      </div>

      {showWubiRadical && (
        <p className="mt-1 text-xs text-gray-400 dark:text-gray-500">键位下方为五笔{wubiVersion}版键名字根，悬停查看完整字根口诀</p>
      )}
    </div>
  )
}

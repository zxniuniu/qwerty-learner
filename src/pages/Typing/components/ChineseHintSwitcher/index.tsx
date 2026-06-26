import Tooltip from '@/components/Tooltip'
import { chineseHintTypeAtom, currentDictInfoAtom } from '@/store'
import type { ChineseHintType } from '@/typings'
import { useAtom, useAtomValue } from 'jotai'
import { useCallback } from 'react'

const options: { type: ChineseHintType; name: string }[] = [
  { type: 'pinyin', name: '拼音' },
  { type: 'wubi', name: '五笔' },
]

export default function ChineseHintSwitcher() {
  const currentDictInfo = useAtomValue(currentDictInfoAtom)
  const [hintType, setHintType] = useAtom(chineseHintTypeAtom)

  const onSelect = useCallback(
    (type: ChineseHintType) => {
      setHintType(type)
    },
    [setHintType],
  )

  // 仅在中文词库下显示提示类型切换
  if (currentDictInfo.language !== 'zh') return null

  return (
    <Tooltip content="切换中文练习提示（拼音 / 五笔）">
      <div className="flex items-center overflow-hidden rounded-md border border-gray-300 dark:border-gray-600">
        {options.map((option) => (
          <button
            key={option.type}
            type="button"
            onClick={(e) => {
              onSelect(option.type)
              e.currentTarget.blur()
            }}
            className={`px-2 py-1 text-sm font-medium transition-colors focus:outline-none ${
              hintType === option.type
                ? 'bg-indigo-500 text-white'
                : 'bg-transparent text-gray-600 hover:bg-gray-100 dark:text-gray-300 dark:hover:bg-gray-700'
            }`}
            aria-label={`使用${option.name}提示`}
          >
            {option.name}
          </button>
        ))}
      </div>
    </Tooltip>
  )
}

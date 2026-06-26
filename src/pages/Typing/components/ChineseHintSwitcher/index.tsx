import Tooltip from '@/components/Tooltip'
import { chineseHintTypeAtom, currentDictInfoAtom, wubiVersionAtom } from '@/store'
import type { ChineseHintType, WubiVersion } from '@/typings'
import { useAtom, useAtomValue } from 'jotai'
import { useCallback } from 'react'

type HintOption = {
  key: string
  name: string
  hintType: ChineseHintType
  // 仅五笔提示需要区分版本
  wubiVersion?: WubiVersion
}

const options: HintOption[] = [
  { key: 'pinyin', name: '拼音', hintType: 'pinyin' },
  { key: 'wubi86', name: '五笔86', hintType: 'wubi', wubiVersion: '86' },
  { key: 'wubi98', name: '五笔98', hintType: 'wubi', wubiVersion: '98' },
]

export default function ChineseHintSwitcher() {
  const currentDictInfo = useAtomValue(currentDictInfoAtom)
  const [hintType, setHintType] = useAtom(chineseHintTypeAtom)
  const [wubiVersion, setWubiVersion] = useAtom(wubiVersionAtom)

  const onSelect = useCallback(
    (option: HintOption) => {
      setHintType(option.hintType)
      if (option.wubiVersion) {
        setWubiVersion(option.wubiVersion)
      }
    },
    [setHintType, setWubiVersion],
  )

  const isActive = useCallback(
    (option: HintOption) => {
      if (option.hintType === 'pinyin') return hintType === 'pinyin'
      return hintType === 'wubi' && wubiVersion === option.wubiVersion
    },
    [hintType, wubiVersion],
  )

  // 仅在中文词库下显示提示类型切换
  if (currentDictInfo.language !== 'zh') return null

  return (
    <Tooltip content="切换中文练习提示（拼音 / 五笔 86 / 五笔 98）">
      <div className="flex items-center overflow-hidden rounded-md border border-gray-300 dark:border-gray-600">
        {options.map((option) => (
          <button
            key={option.key}
            type="button"
            onClick={(e) => {
              onSelect(option)
              e.currentTarget.blur()
            }}
            className={`px-2 py-1 text-sm font-medium transition-colors focus:outline-none ${
              isActive(option)
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

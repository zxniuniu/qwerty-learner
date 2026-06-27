import styles from './index.module.css'
import { defaultFontSizeConfig } from '@/constants'
import { fontSizeConfigAtom, typingLayoutModeAtom } from '@/store'
import type { TypingLayoutMode } from '@/typings'
import * as ScrollArea from '@radix-ui/react-scroll-area'
import * as Slider from '@radix-ui/react-slider'
import { useAtom } from 'jotai'
import { useCallback } from 'react'

const layoutOptions: { mode: TypingLayoutMode; name: string; description: string }[] = [
  { mode: 'classic', name: '经典模式', description: '原版单词卡片练习' },
  { mode: 'enhanced', name: '增强模式', description: '卡片练习 + 屏幕虚拟键盘（指法/五笔字根）+ 醒目统计' },
  { mode: 'article', name: '文章模式', description: '整段文章连续打字，逐字高亮 + 底部虚拟键盘' },
]

export default function ViewSetting() {
  const [fontSizeConfig, setFontsizeConfig] = useAtom(fontSizeConfigAtom)
  const [layoutMode, setLayoutMode] = useAtom(typingLayoutModeAtom)

  const onChangeForeignFontSize = useCallback(
    (value: [number]) => {
      setFontsizeConfig((prev) => ({
        ...prev,
        foreignFont: value[0],
      }))
    },
    [setFontsizeConfig],
  )

  const onChangeTranslateFontSize = useCallback(
    (value: [number]) => {
      setFontsizeConfig((prev) => ({
        ...prev,
        translateFont: value[0],
      }))
    },
    [setFontsizeConfig],
  )

  const onResetFontSize = useCallback(() => {
    setFontsizeConfig({ ...defaultFontSizeConfig })
  }, [setFontsizeConfig])

  return (
    <ScrollArea.Root className="flex-1 select-none overflow-y-auto ">
      <ScrollArea.Viewport className="h-full w-full px-3">
        <div className={styles.tabContent}>
          <div className={styles.section}>
            <span className={styles.sectionLabel}>打字界面布局</span>
            <span className={styles.sectionDescription}>选择练习界面，设置即时生效并自动记忆，可随时切换回原版</span>
            <div className="mt-2 flex flex-col gap-2">
              {layoutOptions.map((option) => (
                <button
                  key={option.mode}
                  type="button"
                  onClick={() => setLayoutMode(option.mode)}
                  className={`flex flex-col items-start rounded-lg border p-3 text-left transition-colors ${
                    layoutMode === option.mode
                      ? 'border-indigo-500 bg-indigo-50 dark:bg-indigo-900/30'
                      : 'border-gray-200 hover:border-indigo-300 dark:border-gray-700'
                  }`}
                >
                  <span className="text-sm font-medium text-gray-800 dark:text-gray-100">{option.name}</span>
                  <span className="mt-0.5 text-xs font-normal text-gray-500 dark:text-gray-400">{option.description}</span>
                </button>
              ))}
            </div>
          </div>
          <div className={styles.section}>
            <span className={styles.sectionLabel}>字体设置</span>
            <div className={styles.block}>
              <span className={styles.blockLabel}>外语字体</span>
              <div className="flex h-5 w-full items-center justify-between">
                <Slider.Root
                  value={[fontSizeConfig.foreignFont]}
                  min={20}
                  max={96}
                  step={4}
                  className="slider"
                  onValueChange={onChangeForeignFontSize}
                >
                  <Slider.Track>
                    <Slider.Range />
                  </Slider.Track>
                  <Slider.Thumb />
                </Slider.Root>
                <span className="ml-4 w-10 text-xs font-normal text-gray-600">{fontSizeConfig.foreignFont}px</span>
              </div>
            </div>

            <div className={styles.block}>
              <span className={styles.blockLabel}>中文字体</span>
              <div className="flex h-5 w-full items-center justify-between">
                <Slider.Root
                  value={[fontSizeConfig.translateFont]}
                  max={60}
                  min={14}
                  step={4}
                  className="slider"
                  onValueChange={onChangeTranslateFontSize}
                >
                  <Slider.Track>
                    <Slider.Range />
                  </Slider.Track>
                  <Slider.Thumb />
                </Slider.Root>
                <span className="ml-4 w-10 text-xs font-normal text-gray-600">{fontSizeConfig.translateFont}px</span>
              </div>
            </div>
          </div>
          <button className="my-btn-primary ml-4 disabled:bg-gray-300" type="button" onClick={onResetFontSize} title="重置字体设置">
            重置字体设置
          </button>
        </div>
      </ScrollArea.Viewport>
      <ScrollArea.Scrollbar className="flex touch-none select-none bg-transparent " orientation="vertical"></ScrollArea.Scrollbar>
    </ScrollArea.Root>
  )
}

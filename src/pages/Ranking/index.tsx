import { getRanking, isTypingBackendEnabled } from '@/api/typingApi'
import type { RankingItem } from '@/api/typingApi'
import Layout from '@/components/Layout'
import { useCallback, useEffect, useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'

const modeOptions: { value: string; label: string }[] = [
  { value: '', label: '全部' },
  { value: 'word', label: '单词练习' },
  { value: 'article', label: '文章练习' },
]

export default function RankingPage() {
  const navigate = useNavigate()
  const [mode, setMode] = useState('')
  const [list, setList] = useState<RankingItem[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const load = useCallback((m: string) => {
    if (!isTypingBackendEnabled) return
    setLoading(true)
    setError(null)
    getRanking(m, 50)
      .then((data) => setList(data))
      .catch((e) => setError(e instanceof Error ? e.message : '加载失败'))
      .finally(() => setLoading(false))
  }, [])

  useEffect(() => {
    load(mode)
  }, [mode, load])

  const maxCpm = useMemo(() => list.reduce((max, item) => Math.max(max, item.cpm ?? 0), 0) || 1, [list])

  return (
    <Layout>
      <div className="flex w-full flex-1 flex-col items-center overflow-y-auto px-4 pt-6">
        <div className="mb-6 flex w-full max-w-3xl items-center justify-between">
          <h1 className="text-2xl font-bold text-indigo-500">速度排行榜</h1>
          <button type="button" className="my-btn-primary" onClick={() => navigate('/')}>
            返回练习
          </button>
        </div>

        {!isTypingBackendEnabled ? (
          <p className="mt-10 text-gray-500 dark:text-gray-400">
            尚未配置打字后端，排行榜不可用。请设置环境变量{' '}
            <code className="rounded bg-gray-200 px-1 dark:bg-gray-700">VITE_TYPING_API_BASE</code> 指向 jeesite-typing-service 后端地址。
          </p>
        ) : (
          <div className="w-full max-w-3xl">
            {/* 模式筛选 */}
            <div className="mb-4 flex gap-2">
              {modeOptions.map((opt) => (
                <button
                  key={opt.value}
                  type="button"
                  onClick={() => setMode(opt.value)}
                  className={`rounded-md px-3 py-1 text-sm transition-colors ${
                    mode === opt.value
                      ? 'bg-indigo-500 text-white'
                      : 'bg-gray-100 text-gray-600 hover:bg-gray-200 dark:bg-gray-700 dark:text-gray-300'
                  }`}
                >
                  {opt.label}
                </button>
              ))}
            </div>

            {loading && <p className="py-10 text-center text-gray-400">加载中…</p>}
            {error && <p className="py-10 text-center text-red-500">加载失败：{error}</p>}
            {!loading && !error && list.length === 0 && <p className="py-10 text-center text-gray-400">暂无记录</p>}

            {!loading && !error && list.length > 0 && (
              <div className="overflow-hidden rounded-xl bg-white shadow-sm dark:bg-gray-800">
                {list.map((item, index) => (
                  <div
                    key={item.userCode || index}
                    className="flex items-center gap-3 border-b border-gray-100 px-4 py-3 last:border-b-0 dark:border-gray-700"
                  >
                    <span
                      className={`flex h-7 w-7 flex-shrink-0 items-center justify-center rounded-full text-sm font-bold ${
                        index === 0
                          ? 'bg-amber-400 text-white'
                          : index === 1
                          ? 'bg-gray-400 text-white'
                          : index === 2
                          ? 'bg-orange-400 text-white'
                          : 'bg-gray-100 text-gray-500 dark:bg-gray-700 dark:text-gray-300'
                      }`}
                    >
                      {index + 1}
                    </span>
                    <span className="w-28 flex-shrink-0 truncate text-sm font-medium text-gray-700 dark:text-gray-200">
                      {item.userName || '匿名用户'}
                    </span>
                    {/* CPM 可视化条 */}
                    <div className="relative h-5 flex-1 overflow-hidden rounded bg-gray-100 dark:bg-gray-700">
                      <div
                        className="h-full rounded bg-indigo-400 transition-all"
                        style={{ width: `${Math.round(((item.cpm ?? 0) / maxCpm) * 100)}%` }}
                      />
                    </div>
                    <span className="w-20 flex-shrink-0 text-right text-sm font-semibold text-indigo-500">{item.cpm} 字/分</span>
                    <span className="w-16 flex-shrink-0 text-right text-xs text-gray-400">{item.accuracy}%</span>
                    <span className="hidden w-16 flex-shrink-0 text-right text-xs text-gray-400 sm:inline">{item.recordCount} 次</span>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}
      </div>
    </Layout>
  )
}

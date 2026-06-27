/**
 * 打字练习后端（JeeSite 微服务 jeesite-typing-service）API 客户端。
 *
 * 通过环境变量 VITE_TYPING_API_BASE 配置后端地址来「开启」后端功能：
 *   - 未配置：视为无后端（公共部署默认），不上报、不请求，App 行为不变。
 *   - 配置为后端根地址（如 http://localhost:8090 或网关地址）：开启速度上报与排名等功能。
 *
 * 接口契约见 jeesite-typing-service/README.md。
 */

const API_BASE = (import.meta.env.VITE_TYPING_API_BASE as string | undefined) ?? ''

/** 是否已配置后端（决定前端是否启用速度上报/排名等功能） */
export const isTypingBackendEnabled = API_BASE.length > 0

function url(path: string): string {
  return `${API_BASE}/api/typing${path}`
}

export type SpeedRecordPayload = {
  mode: string // word / article / pinyin / wubi / en
  refId?: string
  refName?: string
  cpm: number
  wpm?: number
  accuracy: number
  duration: number
  charCount: number
  wrongCount?: number
  userCode?: string
  userName?: string
}

export type RankingItem = {
  userCode: string
  userName: string
  cpm: number
  wpm: number
  accuracy: number
  recordCount: number
}

export type CompetitionItem = {
  id: string
  title: string
  articleId?: string
  description?: string
  startTime?: string
  endTime?: string
  compStatus?: string
}

export type LeaderboardItem = {
  rankNo: number
  userCode: string
  userName: string
  cpm: number
  accuracy: number
  duration: number
  charCount: number
}

async function getJson<T>(path: string): Promise<T> {
  const res = await fetch(url(path))
  if (!res.ok) throw new Error(`请求失败：${res.status}`)
  return res.json() as Promise<T>
}

async function postJson<T>(path: string, body: unknown): Promise<T> {
  const res = await fetch(url(path), {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
  })
  if (!res.ok) throw new Error(`请求失败：${res.status}`)
  return res.json() as Promise<T>
}

/** 上报一条速度记录。后端未配置时静默跳过，且不抛出异常以免影响练习流程。 */
export async function saveSpeedRecord(payload: SpeedRecordPayload): Promise<void> {
  if (!isTypingBackendEnabled) return
  try {
    await postJson('/speed/save', payload)
  } catch (e) {
    // 上报失败不应影响练习，吞掉异常仅记录日志
    console.warn('上报打字速度失败', e)
  }
}

/** 速度排名 */
export function getRanking(mode?: string, top = 50): Promise<RankingItem[]> {
  const query = new URLSearchParams()
  if (mode) query.set('mode', mode)
  query.set('top', String(top))
  return getJson<RankingItem[]>(`/speed/ranking?${query.toString()}`)
}

/** 比赛列表 */
export function getCompetitions(compStatus?: string): Promise<CompetitionItem[]> {
  const query = new URLSearchParams()
  if (compStatus) query.set('compStatus', compStatus)
  return getJson<CompetitionItem[]>(`/competition/list?${query.toString()}`)
}

/** 比赛排行榜 */
export function getLeaderboard(competitionId: string, top = 50): Promise<LeaderboardItem[]> {
  return getJson<LeaderboardItem[]>(`/competition/leaderboard?competitionId=${encodeURIComponent(competitionId)}&top=${top}`)
}

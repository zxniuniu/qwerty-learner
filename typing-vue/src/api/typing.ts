// 打字后端 API 客户端（对接 typing-server / jeesite-typing-service）
// 后端地址通过 VITE_API_BASE 配置，默认本地 8090

const API_BASE = (import.meta.env.VITE_API_BASE as string | undefined) ?? 'http://localhost:8090'

export interface Article {
  id?: string
  title: string
  language: string
  category?: string
  content?: string
  wordCount?: number
  difficulty?: string
  sort?: number
}

export interface SpeedRecordPayload {
  mode: string
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

export interface RankingItem {
  userCode: string
  userName: string
  cpm: number
  wpm: number
  accuracy: number
  recordCount: number
}

export interface Competition {
  id: string
  title: string
  articleId?: string
  description?: string
  compStatus?: string
  articleTitle?: string
}

export interface LeaderboardItem {
  rankNo: number
  userCode: string
  userName: string
  cpm: number
  accuracy: number
  duration: number
}

function get<T>(path: string): Promise<T> {
  return fetch(`${API_BASE}${path}`).then((r) => {
    if (!r.ok) throw new Error(`请求失败 ${r.status}`)
    return r.json() as Promise<T>
  })
}

function post<T>(path: string, body: unknown): Promise<T> {
  return fetch(`${API_BASE}${path}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
  }).then((r) => {
    if (!r.ok) throw new Error(`请求失败 ${r.status}`)
    return r.json() as Promise<T>
  })
}

// 玩家端
export const listArticles = (language?: string) =>
  get<Article[]>(`/api/typing/article/list${language ? `?language=${language}` : ''}`)
export const getArticle = (id: string) => get<Article>(`/api/typing/article/get?id=${encodeURIComponent(id)}`)
export const saveSpeed = (payload: SpeedRecordPayload) => post<{ result: string; id: string }>('/api/typing/speed/save', payload)
export const getRanking = (mode = '', top = 50) => get<RankingItem[]>(`/api/typing/speed/ranking?mode=${mode}&top=${top}`)
export const listCompetitions = (compStatus = '') => get<Competition[]>(`/api/typing/competition/list?compStatus=${compStatus}`)
export const submitResult = (payload: { competitionId: string; cpm: number; accuracy: number; duration: number; charCount: number; userCode?: string; userName?: string }) =>
  post<{ result: string }>('/api/typing/competition/submit', payload)
export const getLeaderboard = (competitionId: string, top = 50) =>
  get<LeaderboardItem[]>(`/api/typing/competition/leaderboard?competitionId=${encodeURIComponent(competitionId)}&top=${top}`)

// 管理端
export const adminListArticles = () => get<Article[]>('/api/admin/article/list')
export const adminSaveArticle = (a: Article) => post<{ result: string; id: string }>('/api/admin/article/save', a)
export const adminDeleteArticle = (id: string) => post<{ result: string }>(`/api/admin/article/delete?id=${encodeURIComponent(id)}`, {})
export const adminSaveCompetition = (c: Partial<Competition>) => post<{ result: string; id: string }>('/api/admin/competition/save', c)
export const adminDeleteCompetition = (id: string) => post<{ result: string }>(`/api/admin/competition/delete?id=${encodeURIComponent(id)}`, {})

import type { WubiVersion } from '@/typings'
import { useMemo } from 'react'
import useSWRImmutable from 'swr/immutable'

const WUBI_DICT_URL: Record<WubiVersion, string> = {
  '86': '/dicts/wubi_86.json',
  '98': '/dicts/wubi_98.json',
}

type WubiDict = Record<string, string>

async function wubiDictFetcher(url: string): Promise<WubiDict> {
  const URL_PREFIX: string = REACT_APP_DEPLOY_ENV === 'pages' ? '/qwerty-learner' : ''
  const response = await fetch(URL_PREFIX + url)
  return response.json()
}

/**
 * 按需懒加载五笔码表。仅在 enabled 为 true 时发起请求，并按版本（86 / 98）
 * 加载对应的全量汉字→五笔编码映射，只在用户切换到五笔提示时加载一次（按版本缓存）。
 */
export function useWubiCodes(word: string, enabled: boolean, version: WubiVersion): { codes: string[] | null; isLoading: boolean } {
  const { data, isLoading } = useSWRImmutable<WubiDict>(enabled ? WUBI_DICT_URL[version] : null, wubiDictFetcher)

  const codes = useMemo(() => {
    if (!enabled || !data) return null
    return Array.from(word).map((char) => data[char] ?? '')
  }, [word, enabled, data])

  return { codes, isLoading }
}

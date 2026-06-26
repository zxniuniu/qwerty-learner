import { useMemo } from 'react'
import useSWRImmutable from 'swr/immutable'

const WUBI_DICT_URL = '/dicts/wubi_86.json'

type WubiDict = Record<string, string>

async function wubiDictFetcher(url: string): Promise<WubiDict> {
  const URL_PREFIX: string = REACT_APP_DEPLOY_ENV === 'pages' ? '/qwerty-learner' : ''
  const response = await fetch(URL_PREFIX + url)
  return response.json()
}

/**
 * 按需懒加载五笔（86 版）码表。仅在 enabled 为 true 时发起请求，
 * 码表为全量汉字→五笔编码映射，约 270KB，只在用户切换到五笔提示时加载一次。
 */
export function useWubiCodes(word: string, enabled: boolean): { codes: string[] | null; isLoading: boolean } {
  const { data, isLoading } = useSWRImmutable<WubiDict>(enabled ? WUBI_DICT_URL : null, wubiDictFetcher)

  const codes = useMemo(() => {
    if (!enabled || !data) return null
    return Array.from(word).map((char) => data[char] ?? '')
  }, [word, enabled, data])

  return { codes, isLoading }
}

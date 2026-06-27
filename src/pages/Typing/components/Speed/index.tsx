import { TypingContext } from '../../store'
import InfoBox from './InfoBox'
import { useContext } from 'react'

export default function Speed({ prominent = false }: { prominent?: boolean }) {
  // eslint-disable-next-line  @typescript-eslint/no-non-null-assertion
  const { state } = useContext(TypingContext)!
  const seconds = state.timerData.time % 60
  const minutes = Math.floor(state.timerData.time / 60)
  const secondsString = seconds < 10 ? '0' + seconds : seconds + ''
  const minutesString = minutes < 10 ? '0' + minutes : minutes + ''
  const inputNumber = state.chapterData.correctCount + state.chapterData.wrongCount

  return (
    <div
      className={`my-card flex w-3/5 rounded-xl bg-white p-4 transition-all duration-300 dark:bg-gray-800 ${
        prominent ? 'py-6 opacity-100 shadow-lg' : 'py-10 opacity-50'
      }`}
    >
      <InfoBox info={`${minutesString}:${secondsString}`} description="时间" />
      <InfoBox info={inputNumber + ''} description="输入数" />
      <InfoBox info={state.timerData.wpm + ''} description="WPM" />
      <InfoBox info={state.chapterData.correctCount + ''} description="正确数" />
      <InfoBox info={state.timerData.accuracy + ''} description="正确率" />
    </div>
  )
}

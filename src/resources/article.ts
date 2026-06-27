// 文章模式（连续打字）的内置文章
// language 决定输入方式：'zh' 走输入法组合，'en' 走逐字母直接输入

export type Article = {
  id: string
  title: string
  language: 'zh' | 'en'
  content: string
}

export const articles: Article[] = [
  {
    id: 'zh-typing',
    title: '练习打字',
    language: 'zh',
    content:
      '熟练的打字是一项非常实用的技能。无论是写文章还是写代码，稳定而准确的输入都能让我们把注意力放在思考本身，而不是寻找按键上。坚持每天练习，速度自然会一点一点提高。',
  },
  {
    id: 'zh-learn',
    title: '关于学习',
    language: 'zh',
    content:
      '学习是一个不断积累的过程。今天比昨天进步一点，明天又比今天多懂一些，时间久了就会有明显的变化。不要害怕犯错，每一次纠正都是一次真正的成长。',
  },
  {
    id: 'en-pangram',
    title: 'The Quick Fox',
    language: 'en',
    content:
      'The quick brown fox jumps over the lazy dog. Packing my box with five dozen liquor jugs is a classic typing drill. Practice these sentences and your fingers will learn where every key lives.',
  },
  {
    id: 'en-habit',
    title: 'Small Steps',
    language: 'en',
    content:
      'Good habits are built one small step at a time. Type a little every day, keep your eyes on the screen, and trust your hands to remember the rest. Speed follows accuracy, so slow down to go fast.',
  },
]

// 虚拟键盘布局与指法 / 五笔字根数据

// 键盘按键分行（仅主键区，足够打字练习使用）
export const KEYBOARD_ROWS: string[][] = [
  ['1', '2', '3', '4', '5', '6', '7', '8', '9', '0'],
  ['Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P'],
  ['A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L'],
  ['Z', 'X', 'C', 'V', 'B', 'N', 'M'],
]

// 八个手指分区（左右手 + 拇指）
export type Finger = 'lPinky' | 'lRing' | 'lMiddle' | 'lIndex' | 'rIndex' | 'rMiddle' | 'rRing' | 'rPinky' | 'thumb'

// 标准指法：每个按键由哪个手指负责
export const FINGER_OF_KEY: Record<string, Finger> = {
  '1': 'lPinky',
  Q: 'lPinky',
  A: 'lPinky',
  Z: 'lPinky',
  '2': 'lRing',
  W: 'lRing',
  S: 'lRing',
  X: 'lRing',
  '3': 'lMiddle',
  E: 'lMiddle',
  D: 'lMiddle',
  C: 'lMiddle',
  '4': 'lIndex',
  '5': 'lIndex',
  R: 'lIndex',
  T: 'lIndex',
  F: 'lIndex',
  G: 'lIndex',
  V: 'lIndex',
  B: 'lIndex',
  '6': 'rIndex',
  '7': 'rIndex',
  Y: 'rIndex',
  U: 'rIndex',
  H: 'rIndex',
  J: 'rIndex',
  N: 'rIndex',
  M: 'rIndex',
  '8': 'rMiddle',
  I: 'rMiddle',
  K: 'rMiddle',
  '9': 'rRing',
  O: 'rRing',
  L: 'rRing',
  '0': 'rPinky',
  P: 'rPinky',
  ' ': 'thumb',
}

// 指法分区配色（左右手同名手指共用一种颜色，符合常见指法图）
export const FINGER_COLOR: Record<Finger, string> = {
  lPinky: 'bg-rose-200 dark:bg-rose-900/50',
  lRing: 'bg-amber-200 dark:bg-amber-900/50',
  lMiddle: 'bg-emerald-200 dark:bg-emerald-900/50',
  lIndex: 'bg-sky-200 dark:bg-sky-900/50',
  rIndex: 'bg-sky-200 dark:bg-sky-900/50',
  rMiddle: 'bg-emerald-200 dark:bg-emerald-900/50',
  rRing: 'bg-amber-200 dark:bg-amber-900/50',
  rPinky: 'bg-rose-200 dark:bg-rose-900/50',
  thumb: 'bg-gray-200 dark:bg-gray-700',
}

export const FINGER_NAME: Record<Finger, string> = {
  lPinky: '左手小指',
  lRing: '左手无名指',
  lMiddle: '左手中指',
  lIndex: '左手食指',
  rIndex: '右手食指',
  rMiddle: '右手中指',
  rRing: '右手无名指',
  rPinky: '右手小指',
  thumb: '拇指',
}

// 五笔键名汉字（每个键位的代表字根）。86 版与 98 版的键名汉字一致，
// 因此可同时用于两个版本，无版本差异风险。
export const WUBI_KEY_NAME: Record<string, string> = {
  G: '王',
  F: '土',
  D: '大',
  S: '木',
  A: '工',
  H: '目',
  J: '日',
  K: '口',
  L: '田',
  M: '山',
  T: '禾',
  R: '白',
  E: '月',
  W: '人',
  Q: '金',
  Y: '言',
  U: '立',
  I: '水',
  O: '火',
  P: '之',
  N: '已',
  B: '子',
  V: '女',
  C: '又',
  X: '纟',
}

// 86 版五笔字根助记口诀（用于悬浮提示，帮助记忆每个键位的字根）
export const WUBI_86_MNEMONIC: Record<string, string> = {
  G: '王旁青头戋（兼）五一',
  F: '土士二干十寸雨',
  D: '大犬三羊古石厂',
  S: '木丁西',
  A: '工戈草头右框七',
  H: '目具上止卜虎皮',
  J: '日早两竖与虫依',
  K: '口与川，字根稀',
  L: '田甲方框四车力',
  M: '山由贝，下框几',
  T: '禾竹一撇双人立，反文条头共三一',
  R: '白手看头三二斤',
  E: '月彡（衫）乃用家衣底',
  W: '人八登祭取字头',
  Q: '金勺缺点无尾鱼，犬旁留叉儿一点夕，氏无七（妻）',
  Y: '言文方广在四一，高头一捺谁人去',
  U: '立辛两点六门疒（病）',
  I: '水旁兴头小倒立',
  O: '火业头，四点米',
  P: '之宝盖，摘礻（示）衤（衣）',
  N: '已半巳满不出己，左框折尸心和羽',
  B: '子耳了也框向上',
  V: '女刀九臼山朝西',
  C: '又巴马，丢矢矣',
  X: '慈母无心弓和匕，幼无力',
}

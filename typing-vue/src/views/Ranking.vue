<script setup lang="ts">
import * as echarts from 'echarts'
import { nextTick, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import type { RankingItem } from '../api/typing'
import { getRanking } from '../api/typing'

const mode = ref('')
const list = ref<RankingItem[]>([])
const loading = ref(false)
const error = ref('')
const chartEl = ref<HTMLDivElement | null>(null)
let chart: echarts.ECharts | null = null

const modes = [
  { v: '', l: '全部' },
  { v: 'word', l: '单词练习' },
  { v: 'article', l: '文章练习' },
]

async function load() {
  loading.value = true
  error.value = ''
  try {
    list.value = await getRanking(mode.value, 20)
    await nextTick()
    renderChart()
  } catch (e) {
    error.value = e instanceof Error ? e.message : '加载失败'
  } finally {
    loading.value = false
  }
}

function renderChart() {
  if (!chartEl.value) return
  if (!chart) chart = echarts.init(chartEl.value)
  const top = list.value.slice(0, 10)
  chart.setOption({
    grid: { left: 80, right: 24, top: 16, bottom: 24 },
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'value', name: '字符/分' },
    yAxis: {
      type: 'category',
      inverse: true,
      data: top.map((i) => i.userName || '匿名'),
    },
    series: [
      {
        type: 'bar',
        data: top.map((i) => i.cpm),
        itemStyle: { color: '#4f46e5', borderRadius: [0, 4, 4, 0] },
        label: { show: true, position: 'right', formatter: '{c}' },
      },
    ],
  })
}

watch(mode, load)
onMounted(load)
onBeforeUnmount(() => chart?.dispose())
window.addEventListener('resize', () => chart?.resize())
</script>

<template>
  <h2>速度排行</h2>

  <div class="card">
    <div style="margin-bottom: 8px">
      <span v-for="m in modes" :key="m.v" class="chip" :class="{ active: mode === m.v }" @click="mode = m.v">{{ m.l }}</span>
    </div>
    <div v-if="error" style="color: #ef4444">加载失败：{{ error }}（请确认后端已启动并配置 VITE_API_BASE）</div>
    <div ref="chartEl" style="width: 100%; height: 360px"></div>
  </div>

  <div class="card">
    <table>
      <thead>
        <tr><th>名次</th><th>用户</th><th>最好成绩(字符/分)</th><th>平均正确率</th><th>记录数</th></tr>
      </thead>
      <tbody>
        <tr v-for="(item, idx) in list" :key="item.userCode || idx">
          <td>{{ idx + 1 }}</td>
          <td>{{ item.userName || '匿名用户' }}</td>
          <td style="color: var(--primary); font-weight: 600">{{ item.cpm }}</td>
          <td>{{ item.accuracy }}%</td>
          <td>{{ item.recordCount }}</td>
        </tr>
        <tr v-if="!loading && list.length === 0"><td colspan="5" style="color: var(--muted)">暂无记录</td></tr>
      </tbody>
    </table>
  </div>
</template>

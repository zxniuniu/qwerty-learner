<script setup lang="ts">
import { onMounted, ref } from 'vue'
import type { Competition, LeaderboardItem } from '../api/typing'
import { getLeaderboard, listCompetitions } from '../api/typing'

const list = ref<Competition[]>([])
const selected = ref<Competition | null>(null)
const board = ref<LeaderboardItem[]>([])
const loading = ref(false)

const statusText: Record<string, string> = { '0': '未开始', '1': '进行中', '2': '已结束' }

async function load() {
  loading.value = true
  try {
    list.value = await listCompetitions()
    if (list.value.length > 0) await view(list.value[0])
  } finally {
    loading.value = false
  }
}
async function view(c: Competition) {
  selected.value = c
  board.value = await getLeaderboard(c.id, 50)
}

onMounted(load)
</script>

<template>
  <h2>打字比赛</h2>

  <div class="card">
    <table>
      <thead>
        <tr><th>比赛</th><th>说明</th><th>状态</th><th>操作</th></tr>
      </thead>
      <tbody>
        <tr v-for="c in list" :key="c.id">
          <td style="font-weight: 600">{{ c.title }}</td>
          <td style="color: var(--muted)">{{ c.description }}</td>
          <td>{{ statusText[c.compStatus || '0'] }}</td>
          <td><button class="btn secondary" style="padding: 4px 10px" @click="view(c)">查看排行</button></td>
        </tr>
        <tr v-if="!loading && list.length === 0"><td colspan="4" style="color: var(--muted)">暂无比赛</td></tr>
      </tbody>
    </table>
  </div>

  <div v-if="selected" class="card">
    <h3 style="margin-top: 0">{{ selected.title }} · 排行榜</h3>
    <table>
      <thead>
        <tr><th>名次</th><th>用户</th><th>字符/分</th><th>正确率</th><th>用时(秒)</th></tr>
      </thead>
      <tbody>
        <tr v-for="r in board" :key="r.rankNo">
          <td>{{ r.rankNo }}</td>
          <td>{{ r.userName || '匿名用户' }}</td>
          <td style="color: var(--primary); font-weight: 600">{{ r.cpm }}</td>
          <td>{{ r.accuracy }}%</td>
          <td>{{ r.duration }}</td>
        </tr>
        <tr v-if="board.length === 0"><td colspan="5" style="color: var(--muted)">暂无成绩</td></tr>
      </tbody>
    </table>
  </div>
</template>

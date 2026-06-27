<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import type { Article } from '../api/typing'
import { adminDeleteArticle, adminListArticles, adminSaveArticle } from '../api/typing'

const list = ref<Article[]>([])
const loading = ref(false)
const editing = ref(false)
const form = reactive<Article>({ title: '', language: 'zh', category: '', content: '', difficulty: '1', sort: 30 })

async function load() {
  loading.value = true
  try {
    list.value = await adminListArticles()
  } finally {
    loading.value = false
  }
}

function newArticle() {
  Object.assign(form, { id: undefined, title: '', language: 'zh', category: '', content: '', difficulty: '1', sort: 30 })
  editing.value = true
}
function edit(a: Article) {
  Object.assign(form, a)
  editing.value = true
}
async function save() {
  if (!form.title || !form.content) {
    alert('标题与正文不能为空')
    return
  }
  await adminSaveArticle({ ...form })
  editing.value = false
  await load()
}
async function remove(a: Article) {
  if (!confirm(`确定删除「${a.title}」？`)) return
  await adminDeleteArticle(a.id as string)
  await load()
}

onMounted(load)
</script>

<template>
  <div style="display: flex; align-items: center; justify-content: space-between">
    <h2>文章管理</h2>
    <button class="btn" @click="newArticle">+ 新增文章</button>
  </div>

  <div v-if="editing" class="card">
    <h3 style="margin-top: 0">{{ form.id ? '编辑文章' : '新增文章' }}</h3>
    <div class="form-row"><label>标题</label><input v-model="form.title" /></div>
    <div style="display: flex; gap: 16px">
      <div class="form-row" style="flex: 1">
        <label>语言</label>
        <select v-model="form.language"><option value="zh">中文</option><option value="en">英文</option></select>
      </div>
      <div class="form-row" style="flex: 1"><label>分类</label><input v-model="form.category" /></div>
      <div class="form-row" style="flex: 1">
        <label>难度</label>
        <select v-model="form.difficulty"><option value="1">易</option><option value="2">中</option><option value="3">难</option></select>
      </div>
      <div class="form-row" style="width: 100px"><label>排序</label><input v-model.number="form.sort" type="number" /></div>
    </div>
    <div class="form-row"><label>正文</label><textarea v-model="form.content" rows="5"></textarea></div>
    <div style="display: flex; gap: 10px">
      <button class="btn" @click="save">保存</button>
      <button class="btn ghost" @click="editing = false">取消</button>
    </div>
  </div>

  <div class="card">
    <table>
      <thead>
        <tr><th>标题</th><th>语言</th><th>分类</th><th>难度</th><th>字数</th><th>操作</th></tr>
      </thead>
      <tbody>
        <tr v-for="a in list" :key="a.id">
          <td>{{ a.title }}</td>
          <td>{{ a.language === 'zh' ? '中文' : '英文' }}</td>
          <td>{{ a.category }}</td>
          <td>{{ { '1': '易', '2': '中', '3': '难' }[a.difficulty || '1'] }}</td>
          <td>{{ a.wordCount }}</td>
          <td>
            <button class="btn secondary" style="padding: 4px 10px" @click="edit(a)">编辑</button>
            <button class="btn danger" style="padding: 4px 10px; margin-left: 6px" @click="remove(a)">删除</button>
          </td>
        </tr>
        <tr v-if="!loading && list.length === 0"><td colspan="6" style="color: var(--muted)">暂无文章</td></tr>
      </tbody>
    </table>
  </div>
</template>

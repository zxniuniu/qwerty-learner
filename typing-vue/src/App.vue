<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <div class="app-layout">
    <aside class="sidebar">
      <div class="logo">⌨️ 打字练习</div>
      <nav>
        <RouterLink to="/practice">打字练习</RouterLink>
        <RouterLink to="/ranking">速度排行</RouterLink>
        <RouterLink to="/competitions">打字比赛</RouterLink>
        <RouterLink to="/admin/articles">文章管理</RouterLink>
      </nav>
    </aside>
    <main class="main">
      <RouterView />
    </main>
  </div>
</template>

import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  { path: '/', redirect: '/practice' },
  { path: '/practice', name: 'practice', component: () => import('../views/Practice.vue'), meta: { title: '打字练习' } },
  { path: '/ranking', name: 'ranking', component: () => import('../views/Ranking.vue'), meta: { title: '速度排行' } },
  { path: '/competitions', name: 'competitions', component: () => import('../views/Competitions.vue'), meta: { title: '打字比赛' } },
  { path: '/admin/articles', name: 'articles', component: () => import('../views/Articles.vue'), meta: { title: '文章管理' } },
]

export default createRouter({
  history: createWebHashHistory(),
  routes,
})

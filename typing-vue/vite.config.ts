import vue from '@vitejs/plugin-vue'
import { defineConfig } from 'vite'

// 通过 VITE_API_BASE 指向 typing-server 后端（默认 http://localhost:8090）
export default defineConfig({
  base: './',
  plugins: [vue()],
  server: {
    port: 5180,
  },
})

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://github.com/vuetifyjs/vuetify-loader/tree/next/packages/vite-plugin
// @ts-ignore
import vuetify from 'vite-plugin-vuetify'

// https://vitejs.dev/config/
export default defineConfig({
	plugins: [
		vue(),
		vuetify({ autoImport: true }),
	],
	server: {
		port: 8089,
		host: true,
		open: true,
		proxy: {
			'/api': {
				target: 'http://localhost:8082',
				changeOrigin: true,
				rewrite: (p: string) => p.replace(/^\/api/, '')
			}
		}
	}
})

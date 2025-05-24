import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig } from 'vite';

export default defineConfig({
	plugins: [sveltekit()],
	build: {
		sourcemap: true, // Enables debugging the frontend code in the browser (with developer tools)
	},
  server: {
    proxy: {
      '/api': 'http://localhost:8080',
      '/auth': 'http://localhost:8080'
    }
  }
});

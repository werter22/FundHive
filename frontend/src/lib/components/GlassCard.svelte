<script>
  export let className = "";
</script>

<div class={`glass-card p-4 rounded-xl shadow-md ${className}`}>
  <slot />
</div>

<style>
  .glass-card {
    background: rgba(70, 75, 80, 0.4);
    backdrop-filter: blur(12px);
    border: 1px solid rgba(255, 255, 255, 0.15);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
    border-radius: 1rem;
  }
</style>

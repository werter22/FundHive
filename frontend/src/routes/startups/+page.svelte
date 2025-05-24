<script>
  import axios from "axios";
  import { page } from "$app/state";
  import { onMount } from "svelte";
  import { jwt_token, isAuthenticated } from "../../store";
  import GlassCard from "$lib/components/GlassCard.svelte";
  import StartupCard from "$lib/components/StartupCard.svelte";

  const API_ROOT = page.url.origin;

  let name = $state();
  let industry = $state();
  let fundingStatus = $state();
  let minValuation = $state();
  let maxValuation = $state();
  let aiRating = $state();

  let startups = $state([]);

  let showChat = $state(false);
  let chatInput = $state("");
  let chatLog = $state([]);
  let chatError = $state("");
  let inputBox = $state();

  onMount(getStartups);

  function toggleChat() {
    showChat = !showChat;
  }

  function askStartupChat() {
    if (!chatInput) return;

    const prompt = chatInput;
    chatInput = "";

    var config = {
      method: "get",
      url: `${API_ROOT}/api/ai/startups/chat`,
      headers: { Authorization: "Bearer " + $jwt_token },
      params: {
        message: prompt,
      },
    };

    axios(config)
      .then(function (response) {
        chatLog = [...chatLog, { prompt, response: response.data }];
        chatError = "";
        inputBox.focus();
      })
      .catch(function (err) {
        if (err.response?.status === 403) {
          chatError = "Not authorized to use this feature.";
        } else {
          chatError = "Chat failed, try again later.";
          console.error("Chat error:", err);
        }
      });
  }

  function getStartups() {
    var params = {};

    if (name) params.name = name;
    if (industry) params.industry = industry;
    if (fundingStatus) params.fundingStatus = fundingStatus;
    if (minValuation) params.minValuation = +minValuation;
    if (maxValuation) params.maxValuation = +maxValuation;
    if (aiRating) params.aiRating = +aiRating;

    var config = {
      method: "get",
      url: `${API_ROOT}/api/startups`,
      // public available API, no need for auth token
      headers: {},
      params: params,
    };

    axios(config)
      .then(function (response) {
        startups = response.data;
      })
      .catch(function (error) {
        console.error("Failed to load startups:", error);
        alert("Could not load startups");
      });
  }

  function stripHtml(html) {
    if (typeof html !== "string" || !html.trim()) return "";
    try {
      const div = document.createElement("div");
      div.innerHTML = html;
      return div.textContent || div.innerText || "";
    } catch {
      return html;
    }
  }
</script>

<GlassCard className="mt-4">
  <h1 class="text-3xl font-bold text-orange-600">Startups</h1>
  {#if $isAuthenticated}
    <div class="flex items-center justify-between mb-4">
      <button class="btn btn-secondary" onclick={toggleChat}>
        {showChat ? "Hide Chat" : "Chat Assistant"}
      </button>
    </div>
  {/if}

  {#if $isAuthenticated && showChat}
    <div class="chat-area mb-3">
      <h5 class="text-lg font-semibold">Startup Finder Assistant</h5>

 {#if chatError}
        <p class="text-danger mt-2">{chatError}</p>
      {:else if chatLog.length}
        <div class="chat-log">
          {#each chatLog as { prompt, response }}
            <div class="message user">
              <strong>You:</strong>
              {prompt}
            </div>
            <div class="message ai">
              <strong>AI:</strong>
              {@html response}
            </div>
          {/each}
        </div>
      {/if}

      <textarea
        bind:this={inputBox}
        bind:value={chatInput}
        class="form-control mb-2"
        placeholder="Need help to find interesting startups? Ask here…"
        onkeydown={(e) => {
          if (e.key === "Enter" && !e.shiftKey) {
            e.preventDefault();
            askStartupChat();
          }
        }}
      ></textarea>
      <button class="btn btn-primary" onclick={askStartupChat}>Ask AI</button>

    
    </div>
  {/if}
</GlassCard>

<GlassCard className="mt-4">
  <div class="filters">
    <input
      class="form-control"
      type="text"
      placeholder="Name…"
      bind:value={name}
      oninput={getStartups}
    />

    <select class="form-select" bind:value={industry} onchange={getStartups}>
      <option value="">All industries</option>
      <option value="TECH">Tech</option>
      <option value="HEALTHCARE">Healthcare</option>
      <option value="ECOMMERCE">Ecommerce</option>
      <option value="EDUCATION">Education</option>
      <option value="ENERGY">Energy</option>
      <option value="AUTOMOTIVE">Automotive</option>
      <option value="REAL_ESTATE">Real Estate</option>
      <option value="MEDIA">Media</option>
      <option value="OTHERS">Others</option>
    </select>

    <select
      class="form-select"
      bind:value={fundingStatus}
      onchange={getStartups}
    >
      <option value="">All funding statuses</option>
      <option value="PRE_SEED">Pre-Seed</option>
      <option value="SEED">Seed</option>
      <option value="SERIES_A">Series A</option>
      <option value="SERIES_B">Series B</option>
      <option value="SERIES_C">Series C</option>
      <option value="ACQUIRED">Acquired</option>
      <option value="IPO">IPO</option>
      <option value="BOOTSTRAPPED">Bootstrapped</option>
    </select>

    <input
      class="form-control"
      type="number"
      placeholder="Min Valuation"
      bind:value={minValuation}
      oninput={getStartups}
    />
    <input
      class="form-control"
      type="number"
      placeholder="Max Valuation"
      bind:value={maxValuation}
      oninput={getStartups}
    />
    <input
      class="form-control"
      type="number"
      placeholder="Min AI-rating"
      bind:value={aiRating}
      oninput={getStartups}
    />
    <div class="filters_action">
      <button class="btn btn-primary" onclick={getStartups}> Search </button>
    </div>
  </div>

  {#if startups.length > 0}
    <div class="startup-grid">
      {#each startups as s}
        <StartupCard startup={s} />
      {/each}
    </div>
  {:else}
    <p class="text-muted text-center">No startups found.</p>
  {/if}
</GlassCard>

<style>
  .filters {
    display: flex;
    gap: 1rem;
    margin-bottom: 1.5rem;
    flex-wrap: wrap;
  }
  .filters input,
  .filters select {
    width: 150px;
  }

  .startup-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 2rem;
    margin-top: 2rem;
  }

  /* Startup Finder Assistant styling */
  .chat-area {
    background: var(--form-bg);
    border: 1px solid var(--form-border);
    border-radius: var(--form-radius);
    padding: 1rem;
    backdrop-filter: blur(10px);
  }

  .chat-area textarea.form-control {
    background: var(--form-bg);
    color: var(--form-text);
    border: 1px solid var(--form-border);
    border-radius: var(--form-radius);
  }
  .chat-area textarea::placeholder {
    color: rgba(255, 255, 255, 0.6);
  }

  .chat-area button.btn-primary {
    background: var(--btn-primary-bg);
    color: var(--btn-primary-color);
    border-radius: var(--form-radius);
    padding: 0.75rem 1rem;
  }

  /* Chat messages styling */
  .chat-area .chat-log {
    margin-top: 1rem;
  }

  .chat-area .chat-log .message {
    background: rgba(255, 255, 255, 0.2);
    border: 1px solid var(--form-border);
    border-radius: var(--form-radius);
    padding: 0.75rem 1rem;
    margin-bottom: 0.75rem;
    color: #000; /* darker text for legibility */
    line-height: 1.4;
  }

  /* User vs AI alignment */
  .chat-area .chat-log .message.user {
    text-align: right;
    background: rgba(0, 212, 255, 0.1);
  }

  .chat-area .chat-log .message.ai {
    text-align: left;
    background: rgba(255, 255, 255, 0.2);
  }

  /* Bold prefixes */
  .chat-area .chat-log .message strong {
    font-weight: 600;
  }
</style>

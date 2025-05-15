<script>
  import axios from "axios";
  import { page } from "$app/state";
  import { onMount } from "svelte";

  const API_ROOT = page.url.origin;

  let name = $state();
  let industry = $state();
  let fundingStatus = $state();
  let minValuation = $state();
  let maxValuation = $state();
  let aiRating = $state();

  let startups = $state([]);

  onMount(getStartups);

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

<h1 class="mt-4">Startups</h1>

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

  <select class="form-select" bind:value={fundingStatus} onchange={getStartups}>
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
    step="0.01"
    placeholder="Min AI-rating"
    bind:value={aiRating}
    oninput={getStartups}
  />

  <button class="btn btn-primary" onclick={getStartups}> Search </button>
</div>

<table class="table table-striped">
  <thead>
    <tr>
      <th>Name</th>
      <th>Description</th>
      <th>Industry</th>
      <th>Valuation</th>
      <th>Funding Status</th>
      <th>AI Rating</th>
    </tr>
  </thead>
  <tbody>
    {#if startups.length > 0}
      {#each startups as s}
        <tr>
          <td
            ><a class="link-primary" href={`/startups/${s.id}`}>{s.name}</a></td
          >
          <td>{stripHtml(s.description).slice(0, 100)}...</td>
          <td>{s.industry}</td>
          <td>{Number(s.valuation).toLocaleString()}</td>
          <td>{s.fundingStatus}</td>
          <td>{s.aiRating}</td>
        </tr>
      {/each}
    {:else}
      <tr>
        <td colspan="6" class="text-center text-muted"> No startups found. </td>
      </tr>
    {/if}
  </tbody>
</table>

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
</style>

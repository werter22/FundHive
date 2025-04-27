<script>
  import axios from "axios";
  import { page } from "$app/stores";
  import { onMount } from "svelte";
  import { goto } from "$app/navigation";

  const API_ROOT = $page.url.origin;
  const id = $page.params.id;

  let startup = $state(null);
  let overview = $state(null);
  let error = $state(null);

  onMount(() => {
    getStartup();
    getFundingOverview();
  });

  function getStartup() {
    var config = {
      method: "get",
      url: `${API_ROOT}/api/startups/${id}`,
      headers: {},
    };

    axios(config)
      .then(function (response) {
        startup = response.data;
      })
      .catch(function (error) {
        console.error("Failed to load startup:", error);
        error = "Startup not found.";
      });
  }

  function getFundingOverview() {
    var config = {
      method: "get",
      url: `${API_ROOT}/api/startups/${id}/funding-overview`,
      headers: {},
    };

    axios(config)
      .then(function (response) {
        overview = response.data;
      })
      .catch(function (error) {
        console.error("Failed to load funding overview:", error);
      });
  }

  function saveChanges() {
    var config = {
      method: "put",
      url: `${API_ROOT}/api/startups/${id}`,
      headers: {
        "Content-Type": "application/json",
      },
      data: startup,
    };

    axios(config)
      .then(function (response) {
        alert("Changes saved successfully!");
        // Optional: reload data again if you want
        // getStartup();
      })
      .catch(function (error) {
        console.error("Failed to save changes:", error);
        alert("Could not save changes.");
      });
  }
</script>

{#if error}
  <div class="alert alert-danger mt-4">{error}</div>
{:else if !startup}
  <div class="text-center mt-4">Loading...</div>
{:else}
  <h1 class="mt-4">
    {startup.name || "Untitled Startup"}
    <input class="form-control" bind:value={startup.name} placeholder="Name" />
  </h1>

  <h2 class="mt-3">Funding Overview</h2>
  {#if overview}
    <div class="card mb-4 p-3">
      <p><strong>Total Rounds:</strong> {overview.roundCount}</p>
      <p><strong>Total Raised:</strong> ${Number(overview.totalRaised).toLocaleString()}</p>
    </div>
  {:else}
    <p class="text-muted">No funding overview available.</p>
  {/if}

  <div class="card p-4 mb-4">
    <h3 class="mb-3">Startup Details</h3>

    <div class="mb-3">
      <label for="description"><strong>Description:</strong></label>
      <textarea id="description" class="form-control" bind:value={startup.description} rows="3"></textarea>
    </div>

    <div class="mb-3">
      <label for="industry"><strong>Industry:</strong></label>
      <select id="industry" class="form-select" bind:value={startup.industry}>
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
    </div>

    <div class="mb-3">
      <label for="valuation"><strong>Valuation:</strong></label>
      <input id="valuation" class="form-control" type="number" bind:value={startup.valuation} />
    </div>

    <div class="mb-3">
      <label for="funding-status"><strong>Funding Status:</strong></label>
      <select id="funding-status" class="form-select" bind:value={startup.fundingStatus}>
        <option value="PRE_SEED">Pre-Seed</option>
        <option value="SEED">Seed</option>
        <option value="SERIES_A">Series A</option>
        <option value="SERIES_B">Series B</option>
        <option value="SERIES_C">Series C</option>
        <option value="ACQUIRED">Acquired</option>
        <option value="IPO">IPO</option>
        <option value="BOOTSTRAPPED">Bootstrapped</option>
      </select>
    </div>

    <div class="mb-3">
      <label for="ai-rating"><strong>AI Rating:</strong></label>
      <input id="ai-rating" class="form-control" type="number" step="0.01" min="0" max="5" bind:value={startup.aiRating} />
    </div>

    <button class="btn btn-success mt-3" onclick={saveChanges}>Save Changes</button>
  </div>
{/if}

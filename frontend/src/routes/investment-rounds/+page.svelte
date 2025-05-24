<script>
  import axios from "axios";
  import { page } from "$app/state";
  import { onMount } from "svelte";
  import { jwt_token } from "../../store";
  import { fade } from "svelte/transition";
  import GlassCard from "$lib/components/GlassCard.svelte";

  const API_ROOT = page.url.origin;

  let showAdvanced = $state(false);

  let minAmountRaised = $state();
  let maxAmountRaised = $state();
  let startDateFrom = $state();
  let startDateTo = $state();
  let endDateFrom = $state();
  let endDateTo = $state();
  let status = $state();

  let investmentRounds = $state([]);

  onMount(getInvestmentRounds);

  function getInvestmentRounds() {
    var params = {};

    if (minAmountRaised) params.minAmountRaised = +minAmountRaised;
    if (maxAmountRaised) params.maxAmountRaised = +maxAmountRaised;
    if (startDateFrom) params.startDateFrom = startDateFrom;
    if (startDateTo) params.startDateTo = startDateTo;
    if (endDateFrom) params.endDateFrom = endDateFrom;
    if (endDateTo) params.endDateTo = endDateTo;
    if (status) params.status = status;

    var config = {
      method: "get",
      url: `${API_ROOT}/api/investment-rounds`,
      headers: { Authorization: "Bearer " + $jwt_token },
      params: params,
    };

    axios(config)
      .then(function (response) {
        investmentRounds = response.data;
      })
      .catch(function (error) {
        console.error("Failed to load investment rounds:", error);
        alert("Could not load investment rounds");
      });
  }
</script>

<GlassCard className="mt-4">
  <h1 class="mt-4">Investment Rounds</h1>

  <div class="filters-wrapper">
    <button
      type="button"
      class="btn btn-link toggle-advanced"
      onclick={() => (showAdvanced = !showAdvanced)}
    >
      {#if showAdvanced}Hide Advanced Filters{:else}Show Advanced Filters{/if}
    </button>
  </div>

  <div class="filters">
    <input
      class="form-control"
      type="number"
      placeholder="Min Amount Raised"
      bind:value={minAmountRaised}
      oninput={getInvestmentRounds}
    />
    <input
      class="form-control"
      type="number"
      placeholder="Max Amount Raised"
      bind:value={maxAmountRaised}
      oninput={getInvestmentRounds}
    />
    <input
      class="form-control"
      type="date"
      placeholder="Start Date From"
      bind:value={startDateFrom}
      oninput={getInvestmentRounds}
    />
    <input
      class="form-control"
      type="date"
      placeholder="Start Date"
      bind:value={startDateTo}
      oninput={getInvestmentRounds}
    />
    {#if showAdvanced}
      <div in:fade out:fade>
        <input
          class="form-control"
          type="date"
          placeholder="End Date From"
          bind:value={endDateFrom}
          oninput={getInvestmentRounds}
        />
        <input
          class="form-control"
          type="date"
          placeholder="End Date To"
          bind:value={endDateTo}
          oninput={getInvestmentRounds}
        />
      </div>
    {/if}

    <select
      class="form-select"
      bind:value={status}
      onchange={getInvestmentRounds}
    >
      <option value="">All statuses</option>
      <option value="OPEN">Open</option>
      <option value="CLOSED">Closed</option>
      <option value="CANCELLED">Cancelled</option>
      <option value="EXPIRED">Expired</option>
    </select>
    <div class="filters__action">
      <button class="btn btn-primary" onclick={getInvestmentRounds}
        >Search</button
      >
    </div>
  </div>

  <table class="uniform-table">
    <thead>
      <tr>
        <th>Round Name</th>
        <th>Amount Raised</th>
        <th>Goal Amount</th>
        <th>Start Date</th>
        <th>End Date</th>
        <th>Status</th>
      </tr>
    </thead>
    <tbody>
      {#if investmentRounds.length > 0}
        {#each investmentRounds as r}
          <tr>
            <td>{r.round_name}</td>
            <td>${Number(r.amount_raised).toLocaleString()}</td>
            <td>${Number(r.goal_amount).toLocaleString()}</td>
            <td>{r.date}</td>
            <td>{r.endDate}</td>
            <td>{r.status}</td>
          </tr>
        {/each}
      {:else}
        <tr>
          <td colspan="6" class="text-center text-muted"
            >No investment rounds found.</td
          >
        </tr>
      {/if}
    </tbody>
  </table>
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
  .filters-wrapper {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 1rem;
  }
  /* Style the toggle like a link but with spacing */
  .toggle-advanced {
    font-size: 0.9rem;
    color: var(--accent-cyan);
    text-decoration: none;
    padding: 0; /* remove default btn-link padding */
    margin-right: 1rem;
  }
  .toggle-advanced:hover {
    text-decoration: underline;
  }

  .filters__action {
  flex: 0 0 auto;
  align-self: baseline;
}
</style>

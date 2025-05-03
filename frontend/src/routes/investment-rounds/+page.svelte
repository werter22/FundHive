<script>
    import axios from "axios";
    import { page } from "$app/state";
    import { onMount } from "svelte";
    import { jwt_token } from "../../store"; 
  
    const API_ROOT = page.url.origin;
  
    let minAmountRaised = $state();
    let maxAmountRaised = $state();
    let startDate = $state();
    let endDate = $state();
    let status = $state();
  
    let investmentRounds = $state([]);
  
    onMount(getInvestmentRounds);
  
    function getInvestmentRounds() {
      var params = {};
  
      if (minAmountRaised) params.minAmountRaised = +minAmountRaised;
      if (maxAmountRaised) params.maxAmountRaised = +maxAmountRaised;
      if (startDate) params.startDate = startDate;
      if (endDate) params.endDate = endDate;
      if (status) params.status = status;
  
      var config = {
        method: "get",
        url: `${API_ROOT}/api/investment-rounds`,
        headers: {Authorization: "Bearer "+$jwt_token},
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
  
  <h1 class="mt-4">Investment Rounds</h1>
  
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
      placeholder="Start Date"
      bind:value={startDate}
      oninput={getInvestmentRounds}
    />
    <input
      class="form-control"
      type="date"
      placeholder="End Date"
      bind:value={endDate}
      oninput={getInvestmentRounds}
    />
  
    <select class="form-select" bind:value={status} onchange={getInvestmentRounds}>
      <option value="">All statuses</option>
      <option value="OPEN">Open</option>
      <option value="CLOSED">Closed</option>
      <option value="CANCELLED">Cancelled</option>
      <option value="EXPIRED">Expired</option>
    </select>
  
    <button class="btn btn-primary" onclick={getInvestmentRounds}>Search</button>
  </div>
  
  <table class="table table-striped">
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
          <td colspan="6" class="text-center text-muted">No investment rounds found.</td>
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
  
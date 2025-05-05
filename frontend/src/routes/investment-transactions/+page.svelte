<script>
  import axios from "axios";
  import { page } from "$app/state";
  import { onMount } from "svelte";
  import { jwt_token, user, isAuthenticated } from "../../store";

  const API_ROOT = page.url.origin;

  let portfolio = $state(null);
  let loadError = $state(false);

  let investorId = $state();
  let startupId = $state();
  let roundId = $state();
  let minAmount = $state();
  let maxAmount = $state();
  let startDate = $state();
  let endDate = $state();

  let transactions = $state([]);

  onMount(() => {
    getTransactions();
    if ($isAuthenticated && $user.user_roles.includes("investor")) {
      getPortfolio();
    }
  });

  function getPortfolio() {
    const me = $user.sub;
    var config = {
      method: "get",
      url: `${API_ROOT}/api/investment-transactions/${me}/portfolio`,
      headers: { Authorization: "Bearer " + $jwt_token },
    };

    axios(config)
      .then(function (response) {
        portfolio = response.data;
        loadError = false;
      })
      .catch(function (error) {
        if (!(error.response && error.response.status === 403)) {
          console.error("Portfolio load failed:", error);
          loadError = true;
        }
      });
  }

  function getTransactions() {
    var params = {};

    if (investorId) params.investorId = investorId;
    if (startupId) params.startupId = startupId;
    if (roundId) params.roundId = roundId;
    if (minAmount) params.minAmount = +minAmount;
    if (maxAmount) params.maxAmount = +maxAmount;
    if (startDate) params.startDate = startDate;
    if (endDate) params.endDate = endDate;

    var config = {
      method: "get",
      url: `${API_ROOT}/api/investment-transactions`,
      headers: { Authorization: "Bearer " + $jwt_token },
      params: params,
    };

    axios(config)
      .then(function (response) {
        transactions = response.data;
      })
      .catch(function (error) {
        console.error("Failed to load transactions:", error);
      });
  }
</script>

{#if $isAuthenticated && $user.user_roles && $user.user_roles.includes("investor")}
  <h1 class="mt-4">Investor Portfolio</h1>

  {#if loadError}
    <p class="text-danger">
      Oops, something went wrong loading your portfolio. Please try again later.
    </p>
  {:else if portfolio}
    <div class="card mb-4 p-4">
      <h5>Summary</h5>
      <p>
        <strong>Total Investment:</strong> ${Number(
          portfolio.summary.totalAmount ?? 0,
        ).toLocaleString()}
      </p>
      <p>
        <strong>Transactions Made:</strong>
        {portfolio.summary.transactionCount}
      </p>
    </div>

    <h5>Investments</h5>

    {#if portfolio.transactions.length > 0}
      <table class="table table-striped">
        <thead>
          <tr>
            <th>Startup</th>
            <th>Round</th>
            <th>Amount</th>
            <th>Date</th>
          </tr>
        </thead>
        <tbody>
          {#each portfolio.transactions as tx}
            <tr>
              <td>{tx.startupName}</td>
              <td>{tx.roundName}</td>
              <td>${Number(tx.amount ?? 0).toLocaleString()}</td>
              <td>{tx.date}</td>
            </tr>
          {/each}
        </tbody>
      </table>
    {:else}
      <p class="text-muted">You haven’t made any investments yet.</p>
    {/if}
  {:else}
    <p class="text-muted">Loading portfolio...</p>
  {/if}
{/if}

{#if $isAuthenticated && $user.user_roles && $user.user_roles.includes("admin")}
  <h1 class="mt-4">Investment Transactions</h1>

  <div class="filters mb-4">
    <input
      class="form-control"
      type="text"
      placeholder="Investor ID"
      bind:value={investorId}
      oninput={getTransactions}
    />
    <input
      class="form-control"
      type="text"
      placeholder="Startup ID"
      bind:value={startupId}
      oninput={getTransactions}
    />
    <input
      class="form-control"
      type="text"
      placeholder="Round ID"
      bind:value={roundId}
      oninput={getTransactions}
    />
    <input
      class="form-control"
      type="number"
      placeholder="Min Amount"
      bind:value={minAmount}
      oninput={getTransactions}
    />
    <input
      class="form-control"
      type="number"
      placeholder="Max Amount"
      bind:value={maxAmount}
      oninput={getTransactions}
    />
    <input
      class="form-control"
      type="date"
      placeholder="Start Date"
      bind:value={startDate}
      oninput={getTransactions}
    />
    <input
      class="form-control"
      type="date"
      placeholder="End Date"
      bind:value={endDate}
      oninput={getTransactions}
    />

    <button class="btn btn-primary" onclick={getTransactions}>Search</button>
  </div>

  <table class="table table-striped">
    <thead>
      <tr>
        <th>Txn ID</th>
        <th>Investor</th>
        <th>Startup</th>
        <th>Round</th>
        <th class="text-end">Amount</th>
        <th>Date</th>
      </tr>
    </thead>
    <tbody>
      {#if transactions.length > 0}
        {#each transactions as t}
          <tr>
            <td>{t.transactionId}</td>

            <td>
              {t.investorName}
              <small class="text-muted">({t.investorId})</small>
            </td>

            <td>
              {t.startupName}
              <small class="text-muted">({t.startupId})</small>
            </td>

            <td>
              {t.roundName}
              <small class="text-muted">({t.roundId})</small>
            </td>

            <td class="text-end">
              ${Number(t.amount || 0).toLocaleString()}
            </td>

            <td>
              {new Date(t.date).toLocaleDateString()}
            </td>
          </tr>
        {/each}
      {:else}
        <tr>
          <td colspan="6" class="text-center text-muted">
            No transactions found.
          </td>
        </tr>
      {/if}
    </tbody>
  </table>
{/if}

<style>
  .filters {
    display: flex;
    flex-wrap: wrap;
    gap: 1rem;
  }
  .filters input {
    width: 150px;
  }
</style>

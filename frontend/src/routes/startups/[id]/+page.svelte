<script>
  import axios from "axios";
  import { page } from "$app/stores";
  import { onMount } from "svelte";
  import { goto } from "$app/navigation";
  import { get } from "svelte/store";
  import { jwt_token, user, isAuthenticated } from "../../../store";

  const API_ROOT = $page.url.origin;
  const id = $page.params.id;

  let startup = $state(null);
  let overview = $state(null);
  let error = $state(null);

  let creatingRound = $state(false);

  let newRound = $state({
    round_name: "",
    goal_amount: null,
    date: "",
  });

  let investmentRounds = $state([]);
  let investingRoundId = $state(null); // ID of the round we are investing into
  let newInvestmentAmount = $state(null);

  onMount(async () => {
    //  if not logged in, bounce right back to the list
    if (!get(isAuthenticated)) {
      goto("/startups");
      return;
    }
    //  otherwise carry on as before
    getStartup();
    getFundingOverview();
    getInvestmentRounds();
  });

  function getFundingOverview() {
    var config = {
      method: "get",
      url: `${API_ROOT}/api/startups/${id}/funding-overview`,
      headers: { Authorization: "Bearer " + $jwt_token },
    };

    axios(config)
      .then(function (response) {
        overview = response.data;
      })
      .catch(function (error) {
        console.error("Failed to load funding overview:", error);
      });
  }

  function getStartup() {
    var config = {
      method: "get",
      url: `${API_ROOT}/api/startups/${id}`,
      headers: { Authorization: "Bearer " + $jwt_token },
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

  function saveChanges() {
    var config = {
      method: "put",
      url: `${API_ROOT}/api/startups/${id}`,
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + $jwt_token,
      },
      data: startup,
    };

    axios(config)
      .then(function (response) {
        alert("Changes saved successfully!");
        // reload data again
        getStartup();
      })
      .catch(function (error) {
        console.error("Failed to save changes:", error);
        alert("Could not save changes.");
      });
  }

  function createFundingRound() {
    var config = {
      method: "post",
      url: `${API_ROOT}/api/investment-rounds`,
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + $jwt_token,
      },
      data: {
        round_name: newRound.round_name,
        goal_amount: newRound.goal_amount,
        date: newRound.date,
        startupId: id, // important!
      },
    };

    axios(config)
      .then(function () {
        alert("Funding Round created!");
        creatingRound = false;
        newRound = { round_name: "", goal_amount: null, date: "" };
        getInvestmentRounds(); // reload fresh
      })
      .catch(function (error) {
        console.error("Failed to create funding round:", error);
        alert("Could not create funding round.");
      });
  }

  function getInvestmentRounds() {
    var config = {
      method: "get",
      url: `${API_ROOT}/api/investment-rounds/${id}`,
      headers: { Authorization: "Bearer " + $jwt_token },
    };

    axios(config)
      .then(function (response) {
        investmentRounds = response.data;
      })
      .catch(function (error) {
        console.error("Failed to load investment rounds:", error);
      });
  }
  function publishRound(roundId) {
    var config = {
      method: "put",
      url: `${API_ROOT}/api/investment-rounds/${roundId}/open`,
      headers: { Authorization: "Bearer " + $jwt_token },
    };

    axios(config)
      .then(function (response) {
        const round = response.data;
        alert(`Round "${round.round_name}" is now OPEN.`);
        getInvestmentRounds(); // reload updated list
      })
      .catch(function (error) {
        console.error("Failed to publish round:", error);
        alert("Could not publish round.");
      });
  }

  function cancelRound(roundId) {
    var config = {
      method: "put",
      url: `${API_ROOT}/api/investment-rounds/${roundId}/cancel`,
      headers: { Authorization: "Bearer " + $jwt_token },
    };

    axios(config)
      .then(function (response) {
        const round = response.data;
        alert(`Round "${round.round_name}" was successfully cancelled.`);
        getInvestmentRounds(); // reload updated list
      })
      .catch(function (error) {
        console.error("Failed to cancel round:", error);
        alert("Could not cancel round.");
      });
  }

  function startInvesting(roundId) {
    investingRoundId = roundId;
    newInvestmentAmount = null;
  }

  function cancelInvesting() {
    investingRoundId = null;
    newInvestmentAmount = null;
  }

  function submitInvestment() {
    if (!newInvestmentAmount || newInvestmentAmount <= 0) {
      alert("Please enter a valid investment amount.");
      return;
    }

    var config = {
      method: "post",
      url: `${API_ROOT}/api/investment-transactions`,
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + $jwt_token,
      },
      data: {
        investmentRoundId: investingRoundId,
        amount: newInvestmentAmount,
      },
    };

    axios(config)
      .then(function () {
        alert("Investment successful!");
        investingRoundId = null;
        newInvestmentAmount = null;
        getStartup();
        getInvestmentRounds();
        getFundingOverview();
      })
      .catch(function (error) {
        console.error("Failed to invest:", error);
        alert("Could not complete investment.");
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
    {#if $isAuthenticated && $user.user_roles && $user.user_roles.includes("entrepreneur")}
      <input
        class="form-control"
        bind:value={startup.name}
        placeholder="Name"
      />
    {/if}
  </h1>

  <h2 class="mt-3">Funding Overview</h2>
  {#if overview}
    <div class="card mb-4 p-3">
      <p><strong>Total Rounds:</strong> {overview.roundCount}</p>
      <p>
        <strong>Total Raised:</strong> ${Number(
          overview.totalRaised,
        ).toLocaleString()}
      </p>
    </div>
  {:else}
    <p class="text-muted">No funding overview available.</p>
  {/if}

  <div class="card p-4 mb-4">
    <h3 class="mb-3">Startup Details</h3>

    {#if $isAuthenticated && $user.user_roles && $user.user_roles.includes("entrepreneur") && $user.sub === startup.ownerId}
      <div class="mb-3">
        <label for="description"><strong>Description:</strong></label>
        <textarea
          id="description"
          class="form-control"
          bind:value={startup.description}
          rows="3"
        ></textarea>
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
        <input
          id="valuation"
          class="form-control"
          type="number"
          bind:value={startup.valuation}
        />
      </div>

      <div class="mb-3">
        <label for="funding-status"><strong>Funding Status:</strong></label>
        <select
          id="funding-status"
          class="form-select"
          bind:value={startup.fundingStatus}
        >
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
        <input
          id="ai-rating"
          class="form-control"
          type="number"
          step="0.01"
          min="0"
          max="5"
          bind:value={startup.aiRating}
        />
      </div>

      <button class="btn btn-success mt-3" onclick={saveChanges}
        >Save Changes</button
      >
    {/if}
  </div>

  {#if $isAuthenticated && $user.user_roles && $user.sub !== startup.ownerId}
    <div class="mb-3">
      <label for="description" class="form-label"
        ><strong>Description:</strong></label
      >
      <p class="form-control-plaintext">{startup.description || "—"}</p>
    </div>

    <div class="mb-3">
      <label for="industry-display" class="form-label"
        ><strong>Industry:</strong></label
      >
      <p id="industry-display" class="form-control-plaintext">
        {startup.industry}
      </p>
    </div>

    <div class="mb-3">
      <label for="valuation-display" class="form-label"
        ><strong>Valuation:</strong></label
      >
      <p id="valuation-display" class="form-control-plaintext">
        ${Number(startup.valuation ?? 0).toLocaleString()}
      </p>
    </div>

    <div class="mb-3">
      <label for="funding-status-display" class="form-label"
        ><strong>Funding Status:</strong></label
      >
      <p id="funding-status-display" class="form-control-plaintext">
        {startup.fundingStatus}
      </p>
    </div>

    <div class="mb-3">
      <label for="ai-rating-display" class="form-label"
        ><strong>AI Rating:</strong></label
      >
      <p id="ai-rating-display" class="form-control-plaintext">
        {startup.aiRating}
      </p>
    </div>
  {/if}

  <h2 class="mt-4">Investment Rounds</h2>

  {#if $isAuthenticated && $user.user_roles && $user.user_roles.includes("entrepreneur") && $user.sub === startup.ownerId}
    {#if !creatingRound}
      <button
        class="btn btn-primary mb-3"
        onclick={() => (creatingRound = true)}
      >
        + Create New Funding Round
      </button>
    {:else}
      <div class="card p-4 mb-4">
        <h5>Create New Funding Round</h5>

        <div class="mb-3">
          <label for="round-name">Round Name</label>
          <input
            id="round-name"
            class="form-control"
            bind:value={newRound.round_name}
          />
        </div>

        <div class="mb-3">
          <label for="goal-amount">Goal Amount</label>
          <input
            id="goal-amount"
            class="form-control"
            type="number"
            bind:value={newRound.goal_amount}
          />
        </div>

        <div class="mb-3">
          <label for="start-date">Start Date</label>
          <input
            id="start-date"
            class="form-control"
            type="date"
            bind:value={newRound.date}
          />
        </div>

        <div class="d-flex gap-2">
          <button class="btn btn-success" onclick={createFundingRound}
            >Save</button
          >
          <button
            class="btn btn-secondary"
            onclick={() => (creatingRound = false)}>Cancel</button
          >
        </div>
      </div>
    {/if}
  {/if}

  {#if investmentRounds.length > 0}
    <table class="table table-striped">
      <thead>
        <tr>
          <th>Round Name</th>
          <th>Amount Raised</th>
          <th>Goal Amount</th>
          <th>Start Date</th>
          <th>End Date</th>
          <th>Status</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        {#each investmentRounds as round}
          <tr>
            <td>{round.round_name}</td>
            <td>${Number(round.amount_raised ?? 0).toLocaleString()}</td>
            <td>${Number(round.goal_amount ?? 0).toLocaleString()}</td>
            <td>{round.date}</td>
            <td>{round.endDate}</td>
            <td>{round.status}</td>
            <td>
              {#if round.status === "UPCOMING"}
                {#if $isAuthenticated && $user.user_roles && $user.user_roles.includes("entrepreneur") && $user.sub === startup.ownerId}
                  <div class="d-flex gap-2">
                    <button
                      class="btn btn-success btn-sm"
                      onclick={() => publishRound(round.id)}>Publish</button
                    >
                    <button
                      class="btn btn-danger btn-sm"
                      onclick={() => cancelRound(round.id)}>Cancel</button
                    >
                  </div>
                {/if}
              {:else if round.status === "OPEN"}
                <div class="d-flex flex-column gap-2">
                  {#if $isAuthenticated && $user.user_roles && $user.user_roles.includes("entrepreneur") && $user.sub === startup.ownerId}
                    <button
                      class="btn btn-danger btn-sm"
                      onclick={() => cancelRound(round.id)}>Cancel</button
                    >
                  {/if}

                  {#if $isAuthenticated && $user.user_roles && $user.user_roles.includes("investor")}
                    {#if investingRoundId !== round.id}
                      <button
                        class="btn btn-primary btn-sm"
                        onclick={() => startInvesting(round.id)}>Invest</button
                      >
                    {:else}
                      <div class="d-flex gap-2">
                        <input
                          type="number"
                          class="form-control form-control-sm"
                          placeholder="Amount"
                          bind:value={newInvestmentAmount}
                        />
                        <button
                          class="btn btn-success btn-sm"
                          onclick={submitInvestment}>Confirm</button
                        >
                        <button
                          class="btn btn-secondary btn-sm"
                          onclick={cancelInvesting}>Cancel</button
                        >
                      </div>
                    {/if}
                  {/if}
                </div>
              {:else}
                <span class="text-muted">-</span>
              {/if}
            </td>
          </tr>
        {/each}
      </tbody>
    </table>
  {:else}
    <p class="text-muted">No investment rounds found for this startup.</p>
  {/if}
{/if}

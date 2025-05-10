<script>
  import axios from "axios";
  import { page } from "$app/state";
  import { onMount } from "svelte";
  import { jwt_token, user, isAuthenticated } from "../../store";

  const API_ROOT = page.url.origin;

  let startup = $state({
    name: null,
    description: null,
    industry: null,
    valuation: null,
    fundingStatus: null,
  });

  function createStartup() {
    var config = {
      method: "post",
      url: API_ROOT + "/api/startups",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + $jwt_token,
      },
      data: startup,
    };

    axios(config)
      .then(function (response) {
        alert("Startup created successfully");
        console.log(response.data);

        startup = {
          name: null,
          description: null,
          industry: null,
          valuation: null,
          fundingStatus: null,
        };
      })
      .catch(function (error) {
        alert("Could not create Startup");
        console.log(error);
      });
  }
</script>

<h1 class="mt-4">Account</h1>

<div class="d-flex align-items-center mb-4">
  <div
    class="rounded-square d-flex align-items-center justify-content-center me-4"
    style="width: 100px; height: 100px; background-color: #e9a8c6; color: white; font-size: 2rem; font-weight: bold;"
  ></div>

  <div>
    <p><strong>Name:</strong> {$user.name}</p>
    <p><strong>Nickname:</strong> {$user.nickname}</p>
    <p><strong>First Name:</strong> {$user.given_name}</p>
    <p><strong>Last Name:</strong> {$user.family_name}</p>
    <p><strong>Email:</strong> {$user.email}</p>
  </div>
</div>

{#if $isAuthenticated && $user.user_roles  && $user.user_roles.includes("entrepreneur")}
  <h2 class="mt-5">Create New Startup</h2>
  <form class="mt-4" onsubmit={createStartup}>
    <div class="mb-3">
      <label class="form-label" for="name">Name</label>
      <input
        class="form-control"
        id="name"
        type="text"
        bind:value={startup.name}
        required
      />
    </div>

    <div class="mb-3">
      <label class="form-label" for="description">Description</label>
      <textarea
        class="form-control"
        id="description"
        rows="3"
        bind:value={startup.description}
        required
      ></textarea>
    </div>

    <div class="mb-3">
      <label class="form-label" for="industry">Industry</label>
      <select
        class="form-select"
        id="industry"
        bind:value={startup.industry}
        required
      >
        <option value="">Select industry</option>
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
      <label class="form-label" for="valuation">Valuation</label>
      <input
        class="form-control"
        id="valuation"
        type="number"
        bind:value={startup.valuation}
        required
      />
    </div>

    <div class="mb-3">
      <label class="form-label" for="fundingStatus">Funding Status</label>
      <select
        class="form-select"
        id="fundingStatus"
        bind:value={startup.fundingStatus}
        required
      >
        <option value="">Select funding status</option>
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

    <button class="btn btn-success" type="submit">Create Startup</button>
  </form>
{/if}

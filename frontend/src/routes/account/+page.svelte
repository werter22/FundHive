<script>
  import axios from "axios";
  import { page } from "$app/state";
  import { onMount } from "svelte";

  const API_ROOT = page.url.origin;

  let startup = $state({
    name: null,
    description: null,
    industry: null,
    valuation: null,
    fundingStatus: null,
    aiRating: null,
  });

  function createStartup() {
    var config = {
      method: "post",
      url: API_ROOT + "/api/startups",
      headers: {
        "Content-Type": "application/json",
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
        aiRating: null,
      };
    })
    .catch(function (error) {
      alert("Could not create Startup");
      console.log(error);
    });
  }
</script>

<h1 class="mt-4">Account</h1>

<!-- Future: user info here -->

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
    <select class="form-select" id="industry" bind:value={startup.industry} required>
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

  <div class="mb-3">
    <label class="form-label" for="aiRating">AI Rating</label>
    <input
      class="form-control"
      id="aiRating"
      type="number"
      step="0.01"
      min="0"
      max="5"
      bind:value={startup.aiRating}
      required
    />
  </div>

  <button class="btn btn-success" type="submit">Create Startup</button>
</form>

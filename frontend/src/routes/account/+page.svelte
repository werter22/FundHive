<script>
  import axios from "axios";
  import { page } from "$app/state";
  import { onMount } from "svelte";
  import { jwt_token, user, isAuthenticated } from "../../store";
  import GlassCard from "$lib/components/GlassCard.svelte";

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

<GlassCard className="mt-4">

<div class="profile-header">
  <h1 class="account-title">Account</h1>
  <button class="btn btn-secondary edit-btn"> Edit Profile</button>
</div>

<!-- NEW: avatar + name/email on its own centered row -->
<div class="profile-top">
  <div class="avatar">
    <div class="avatar-fallback">{$user.nickname?.[0] || 'U'}</div>
  </div>
  <div class="profile-identity">
    <div class="username">{$user.nickname}</div>
    <div class="email">{$user.email}</div>
  </div>
</div>

   <div class="profile-details">
      <div class="detail">
        <span class="label">First Name</span>
        <span class="value">{$user.given_name}</span>
      </div>
      <div class="detail">
        <span class="label">Last Name</span>
        <span class="value">{$user.family_name}</span>
      </div>
      <div class="detail">
        <span class="label">Nickname</span>
        <span class="value">{$user.nickname}</span>
      </div>
      <div class="detail">
        <span class="label">Email</span>
        <span class="value">{$user.email}</span>
      </div>
    </div>


{#if $isAuthenticated && $user.user_roles && $user.user_roles.includes("entrepreneur")}
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
</GlassCard>

<style>

  
.profile-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 0.5rem;
  margin-bottom: 1.5rem;
}
  .avatar {
    flex-shrink: 0;
    width: 96px;
    height: 96px;
    border-radius: 50%;
    overflow: hidden;
    border: 2px solid var(--focus-border);
    background: var(--form-bg);
  }
  .avatar-fallback {
    width: 100%;
    height: 100%;
    display: flex;
    background: var(--form-bg);
    color: var(--form-text);
    font-size: 2rem;
    font-weight: bold;
    align-items: center;
    justify-content: center;
  }

  .username {
    margin: 0;
    font-size: 1.75rem;
    font-weight: 600;
    color: var(--form-text);
  }
  .email {
    margin: 0.25rem 0 0;
    font-size: 0.95rem;
    color: rgba(255,255,255,0.8);
  }

.edit-btn {
  background: transparent;
  border: 2px solid var(--accent-cyan);
  color: var(--accent-cyan);
  padding: 0.5rem 1rem;
  border-radius: var(--form-radius);
  font-size: 0.9rem;
  transition: background 0.2s, color 0.2s;
}

.edit-btn:hover {
  background: var(--accent-cyan);
  color: #fff;
}

.profile-top {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1.5rem;
  margin-bottom: 2rem;
}

  .profile-details {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
    gap: 1.5rem;
  }

  .detail {
    display: flex;
    flex-direction: column;
  }
  .label {
    font-size: 0.75rem;
    color: rgba(255,255,255,0.6);
    text-transform: uppercase;
    letter-spacing: 0.03em;
  }
  .value {
    margin-top: 0.25rem;
    font-size: 1rem;
    color: var(--form-text);
  }
</style>
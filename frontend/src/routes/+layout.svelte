<script>
  import "./styles.css";
  import { isAuthenticated, user } from "../store";
  import auth from "../auth.service";

  const logoSrc = "images/FundHive_Logo.png";
</script>

<nav class="navbar navbar-expand-lg bg-light">
  <div class="container-fluid">
    <a class="navbar-brand" href="/">
      <img
        src={logoSrc}
        alt="FundHive"
        height="50"
        class="d-inline-block align-text-top"
      />
      <!-- for screen-readers: -->
      <span class="visually-hidden">FundHive</span>
    </a>

    <button
      class="navbar-toggler"
      type="button"
      data-bs-toggle="collapse"
      data-bs-target="#navbarNav"
      aria-controls="navbarNav"
      aria-expanded="false"
      aria-label="Toggle navigation"
    >
      <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav me-auto">
        <li class="nav-item">
          <a class="nav-link" href="/startups">Startups</a>
        </li>

        {#if $isAuthenticated}
          {#if $isAuthenticated && $user.user_roles && $user.user_roles.includes("admin")}
            <li class="nav-item">
              <a class="nav-link" href="/investment-rounds">Investment Rounds</a
              >
            </li>
          {/if}
          {#if $isAuthenticated && $user.user_roles && ($user.user_roles.includes("investor")|| $user.user_roles.includes("admin"))}
            <li class="nav-item">
              <a class="nav-link" href="/investment-transactions"
                >Transactions</a
              >
            </li>
          {/if}
          <li class="nav-item">
            <a class="nav-link" href="/account">Account</a>
          </li>
        {/if}
      </ul>

      <div class="d-flex align-items-center">
        {#if $isAuthenticated}
          <span class="navbar-text me-3">{$user.name}</span>
          <button class="btn btn-outline-primary" on:click={auth.logout}>
            Log Out
          </button>
        {:else}
          <button class="btn btn-primary" on:click={auth.login}>
            Log In
          </button>
        {/if}
      </div>
    </div>
  </div>
</nav>
<div class="container mt-3">
  <slot />
</div>

<style>
  .navbar-brand img {
    height: 2.5rem;
    width: auto;
  }
</style>

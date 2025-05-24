<script>
  import "./styles.css";
  import { isAuthenticated, user } from "../store";
  import auth from "../auth.service";

</script>

<nav class="navbar navbar-expand-lg glass-navbar fixed-top">
  <div class="container-fluid">
    <a class="navbar-brand" href="/">
      <img
        src="images/home.png"
        alt="FundHive"
        height="50"
        class="d-inline-block align-text-top"
      />
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
          {#if $user.user_roles?.includes("admin")}
            <li class="nav-item">
              <a class="nav-link" href="/investment-rounds">Investment Rounds</a>
            </li>
          {/if}
          {#if $user.user_roles?.some(r => ["investor", "admin"].includes(r))}
            <li class="nav-item">
              <a class="nav-link" href="/investment-transactions">Transactions</a>
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
          <button class="btn btn-outline-primary" on:click={auth.logout}>Log Out</button>
        {/if}
      </div>
    </div>
  </div>
</nav>

<!-- ✅ Offset for fixed navbar -->
<div class="container page-content">
  <slot />
</div>

<style>
  .glass-navbar {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    z-index: 1030;
    background: linear-gradient(145deg, #3a6d82, #5a9bb0);
    backdrop-filter: blur(16px);
    -webkit-backdrop-filter: blur(16px);
    border-bottom: 1px solid rgba(255, 255, 255, 0.15);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.25);
    border-radius: 0 0 1rem 1rem;
    padding: 0.5rem 1rem;
    color: white;
  }

  .navbar-nav .nav-link {
    color: #e8f6fb !important;
    font-weight: 500;
    margin-right: 1rem;
    transition: all 0.2s ease-in-out;
  }

  .navbar-nav .nav-link:hover {
    text-shadow: 0 0 6px rgba(255, 255, 255, 0.6);
    transform: translateY(-1px);
  }

  .navbar-brand img {
    height: 2.5rem;
    border-radius: 0.5rem;
  }

  .navbar-text {
    color: #f2f2f2;
    font-size: 0.95rem;
    font-weight: 400;
  }

  .btn-outline-primary {
    border: 1px solid #00d4ff;
    color: #00d4ff;
    border-radius: 999px;
    padding: 0.4rem 1rem;
    font-weight: 500;
    transition: all 0.2s ease-in-out;
  }

  .btn-outline-primary:hover {
    background-color: #00d4ff;
    color: #000;
    box-shadow: 0 0 12px rgba(0, 212, 255, 0.6);
  }

  /* ✅ Add spacing below fixed navbar */
  .page-content {
    padding-top: 6rem; /* adjust as needed for your navbar height */
  }

  
</style>

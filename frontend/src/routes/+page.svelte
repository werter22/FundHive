<script>
    import { isAuthenticated } from "../store";
    import auth from "../auth.service";
    import GlassCard from "$lib/components/GlassCard.svelte";

    let showLogin = $state(false);

    let username = $state("");
    let password = $state("");
    let loginForm = $state();

    function loginWithUsernameAndPassword(event) {
        event.preventDefault();
        // form validation with bootstrap: see https://getbootstrap.com/docs/5.3/forms/validation/
        if (loginForm.checkValidity()) {
            console.log("login");
            auth.login(username, password);
        }
        loginForm.classList.add("was-validated");
    }
</script>

{#if $isAuthenticated}
    <GlassCard class="mt-4">
        <div class="welcome-container">
            <img
                src="images/FundHive_Logo.png"
                alt="FundHive Logo"
                class="hero-logo"
            />
            <h1>Welcome to FundHive</h1>
            <p>
                Your gateway to the world of startups and investment
                opportunities.
            </p>
            <a href="/startups" class="btn btn-accent mt-3">Explore Startups</a>
        </div>
    </GlassCard>
{:else if showLogin}
    <div class="d-flex justify-content-center align-items-center login-screen">
        <div class="glass-card login-card">
            <img
                src="/images/FundHive_Logo.png"
                alt="FundHive logo"
                class="login-logo"
            />
            <h3 class="mb-4 text-white text-center">Log in</h3>

            <form
                onsubmit={loginWithUsernameAndPassword}
                bind:this={loginForm}
                class="needs-validation"
                novalidate
            >
                <div class="mb-3">
                    <label for="username" class="form-label text-white"
                        >E-Mail</label
                    >
                    <input
                        bind:value={username}
                        type="email"
                        class="form-control frosted-input"
                        id="username"
                        required
                    />
                    <div class="invalid-feedback">
                        Please provide your e-mail address.
                    </div>
                </div>

                <div class="mb-4">
                    <label for="password" class="form-label text-white"
                        >Password</label
                    >
                    <input
                        bind:value={password}
                        type="password"
                        class="form-control frosted-input"
                        id="password"
                        required
                    />
                    <div class="invalid-feedback">
                        Please provide your password.
                    </div>
                </div>

                <div class="d-flex justify-content-between align-items-center">
                    <button type="submit" class="btn btn-accent">Log in</button>
                    <a
                        href="/signup"
                        class="text-light text-decoration-underline">Sign up</a
                    >
                </div>
            </form>
        </div>
    </div>
{:else}
    <div class="landing-wrapper">
        <GlassCard class="mt-4">
            <div class="hero-container text-center">
                <img
                    src="images/FundHive_Logo.png"
                    alt="FundHive Logo"
                    class="hero-logo"
                />

                <h1 class="display-5 fw-bold mt-3">
                    Invest in Tomorrow’s Startups, Today
                </h1>
                <p class="lead text-muted mt-2 mb-4">
                    Join FundHive — the secure, stylish, and smart way to
                    connect startups with investors.
                </p>

                <div class="d-flex justify-content-center gap-3">
                    <button
                        class="btn btn-accent"
                        onclick={() => (showLogin = true)}>Log In</button
                    >
                    <a href="/signup" class="btn btn-outline-light">Sign Up</a>
                </div>
            </div>
        </GlassCard>
    </div>
{/if}

<style>
/* === GENERAL === */
.hero-logo,
.login-logo {
  max-width: 250px;
  height: auto;
  margin-bottom: 1rem;
  display: block;
  margin-left: auto;
  margin-right: auto;
  object-fit: contain;
}

.form-control {
  background-color: rgba(255, 255, 255, 0.1);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 0.5rem;
}

.form-control:focus {
  border-color: #00d4ff;
  box-shadow: 0 0 0 0.2rem rgba(0, 212, 255, 0.3);
  background-color: rgba(255, 255, 255, 0.12);
}

.form-label {
  font-weight: 500;
}

/* === BUTTONS === */
.btn-accent {
  background-color: #ffd94f;
  border: none;
  color: black;
  font-weight: 600;
  border-radius: 999px;
  padding: 0.6rem 1.4rem;
  transition: 0.2s ease-in-out;
}

.btn-accent:hover {
  background-color: #ffe878;
  box-shadow: 0 0 12px rgba(255, 217, 79, 0.6);
}

.btn-outline-light {
  border: 1px solid white;
  color: white;
  border-radius: 999px;
  padding: 0.6rem 1.4rem;
}

.btn-outline-light:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

/* === LAYOUT === */

.hero-container {
  max-width: 720px;
  width: 100%;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.welcome-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding: 2rem;
}

.welcome-container h1 {
  margin-top: 1rem;
}

/* === LOGIN === */
.login-screen {
  min-height: 60vh;
  padding-top: 5vh;
  padding-bottom: 5vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-card {
  max-width: 420px;
  width: 100%;
  padding: 2rem;
  border-radius: 1rem;
  background: rgba(70, 75, 80, 0.4);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(0, 0, 0, 0.1);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
  align-items: center;
}

</style>

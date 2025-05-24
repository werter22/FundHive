<script>
  import { isAuthenticated } from "../../store";
  import auth from "../../auth.service";

  let email = $state("");
  let password = $state("");
  let firstName = $state("");
  let lastName = $state("");

  const userTypes = [
    { value: "entrepreneur", label: "to get my startup funded" },
    { value: "investor", label: "to invest in a startup" },
  ];
  let selectedUserType = $state("");
  let signupForm;

  function signup(event) {
    event.preventDefault();
    if (signupForm.checkValidity()) {
      console.log("signup");
      auth.signup(email, password, firstName, lastName, selectedUserType);
    }
    signupForm.classList.add("was-validated");
  }
</script>

<div class="auth-wrapper">
  <div class="auth-card">
    <img src="/images/FundHive_Logo.png" alt="FundHive Logo" class="auth-logo" />
    <h2 class="text-center text-white mb-4">Sign up</h2>

    <form
      onsubmit={signup}
      bind:this={signupForm}
      class="needs-validation"
      novalidate
    >
      <div class="mb-3">
        <label for="username" class="form-label">E-Mail</label>
        <input
          bind:value={email}
          type="email"
          class="form-control"
          id="username"
          name="username"
          required
        />
        <div class="invalid-feedback">Please provide an e-mail address.</div>
      </div>

      <div class="mb-3">
        <label for="first-name" class="form-label">First Name</label>
        <input
          bind:value={firstName}
          type="text"
          class="form-control"
          id="first-name"
          name="first-name"
        />
      </div>

      <div class="mb-3">
        <label for="last-name" class="form-label">Last Name</label>
        <input
          bind:value={lastName}
          type="text"
          class="form-control"
          id="last-name"
          name="last-name"
        />
      </div>

      <div class="mb-3">
        <label for="password" class="form-label">Password</label>
        <input
          bind:value={password}
          type="password"
          class="form-control"
          id="password"
          name="password"
          required
        />
        <div class="invalid-feedback">Please choose a password.</div>
      </div>

      <div class="mb-4">
        <label class="form-label" for="user-type">I want:</label>
        <div id="user-type">
          {#each userTypes as { value, label }}
            <div class="form-check">
              <input
                bind:group={selectedUserType}
                class="form-check-input"
                type="radio"
                id={value}
                {value}
                required
              />
              <label class="form-check-label" for={value}>{label}</label>
            </div>
          {/each}
        </div>
        <div class="invalid-feedback">
          Please select whether you want to found a startup or invest.
        </div>
      </div>

      <div class="d-flex justify-content-between align-items-center">
        <button type="submit" class="btn btn-primary">Sign up</button>
        <a href="/">Log in</a>
      </div>
    </form>
  </div>
</div>

<style>
  .auth-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 90vh;
  }

  .auth-card {
    width: 100%;
    max-width: 460px;
    background: rgba(70, 75, 80, 0.4);
    backdrop-filter: blur(16px);
    -webkit-backdrop-filter: blur(16px);
    border-radius: 1rem;
    padding: 2.5rem;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.25);
    color: white;
  }

  .auth-logo {
    display: block;
    max-width: 325px;
    margin: 0 auto 1rem;
  }

  .form-control {
    background-color: rgba(255, 255, 255, 0.1);
    color: white;
    border: 1px solid rgba(255, 255, 255, 0.2);
  }

  .form-control:focus {
    border-color: var(--accent-cyan, #00d4ff);
    box-shadow: 0 0 0 0.2rem rgba(0, 212, 255, 0.3);
  }

  .form-label, .form-check-label {
    color: white;
    font-weight: 500;
  }

  .invalid-feedback {
    color: #ffbaba;
  }

  .btn-primary {
    background-color: var(--accent-yellow, #ffd94f);
    color: #000;
    border: none;
    border-radius: 999px;
    padding: 0.5rem 1.5rem;
    font-weight: 500;
  }

  .btn-primary:hover {
    background-color: #ffeb88;
  }

  a {
    color: white;
  }
</style>

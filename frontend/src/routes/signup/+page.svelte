<script>
  import { isAuthenticated } from "../../store";
  import auth from "../../auth.service";

  let email = $state("");
  let password = $state("");
  let firstName = $state("");
  let lastName = $state("");
  // ─────────── New state for user intent ───────────
  const userTypes = [
    { value: "entrepreneur", label: "I want to get my startup funded" },
    { value: "investor", label: "I want to invest in a startup" },
  ];
  let selectedUserType = $state(""); // holds "entrepreneur" or "investor"

  let signupForm;

  function signup(event) {
    event.preventDefault();
    // form validation with bootstrap: see https://getbootstrap.com/docs/5.3/forms/validation/
    if (signupForm.checkValidity()) {
      console.log("signup");
      auth.signup(email, password, firstName, lastName, selectedUserType);
    }
    signupForm.classList.add("was-validated");
  }
</script>

<h1>Sign up</h1>
<div class="container mt-5">
  <div class="row justify-content-center">
    <div class="col-md-6">
      <div class="card">
        <div class="card-header">Sign up</div>
        <div class="card-body">
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
              <div class="invalid-feedback">
                Please provide an e-mail address.
              </div>
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
              <label class="form-label" for="user-type">I am:</label>
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

            <div class="row align-items-end">
              <div class="col">
                <button type="submit" class="btn btn-primary">Sign up</button>
              </div>
              <div class="col-auto">
                <a href="/">Log in</a>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>

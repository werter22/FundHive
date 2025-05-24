<script>
  const { startup } = $props();

  // Strips HTML tags
  function stripHtml(html) {
    if (typeof html !== "string" || !html.trim()) return "";
    try {
      const div = document.createElement("div");
      div.innerHTML = html;
      return div.textContent || div.innerText || "";
    } catch {
      return html;
    }
  }

  // Background images by industry
  const industryVisuals = {
    TECH: "/images/bg-tech.png",
    FINTECH: "/images/bg-fintech.png",
    HEALTHCARE: "/images/bg-healthcare.png",
    ECOMMERCE: "/images/bg-ecommerce.png",
    EDUCATION: "/images/bg-education.png",
    ENERGY: "/images/bg-energy.png",
    AUTOMOTIVE: "/images/bg-automotive.png",
    REAL_ESTATE: "/images/bg-realestate.png",
    MEDIA: "/images/bg-media.png",
    OTHERS: "/images/bg-generic.png",
  };

  // Accent colors by industry
  const industryAccents = {
    TECH:        "#4f94ff",
    FINTECH:     "#ff6a00",
    HEALTHCARE:  "#9b59b6",
    ECOMMERCE:   "#27ae60",
    EDUCATION:   "#f1c40f",
    ENERGY:      "#e67e22",
    AUTOMOTIVE:  "#3498db",
    REAL_ESTATE: "#2ecc71",
    MEDIA:       "#e74c3c",
    OTHERS:      "#7f8c8d",
  };

  // HEX → RGBA helper
  function hexToRGBA(hex, alpha = 0.4) {
    hex = hex.replace(/^#/, "");
    const [r, g, b] = hex.match(/.{2}/g).map(pair => parseInt(pair, 16));
    return `rgba(${r}, ${g}, ${b}, ${alpha})`;
  }

  // Normalize the industry key
  const raw = $state(startup?.industry || "OTHERS");
  const key = $state(raw.toUpperCase().replace(/\s+/g, "_"));
  const bgImage = industryVisuals[key] || industryVisuals.OTHERS;
  const accent  = industryAccents[key]  || industryAccents.OTHERS;

  // Create a muted (semi-transparent) version of that accent
  const accentMuted = hexToRGBA(accent, 0.4);

  // Bee fill clip
  function getFillClip(i) {
    const rating = parseFloat(startup.aiRating) || 0;
    const fraction = Math.min(1, Math.max(0, rating - i));
    return `inset(0 ${100 - fraction * 100}% 0 0)`;
  }
</script>

<a
  class="startup-card"
  href={`/startups/${startup.id}`}
  style="--card-accent: {accentMuted};"
>
  <div class="card-bg" style="background-image: url('{bgImage}');"></div>

  <div class="card-body">
    <span class="industry-tag">{startup.industry}</span>
    <h3 class="startup-name">{startup.name}</h3>
    <p class="startup-description">
      {stripHtml(startup.description).slice(0, 150)}…
    </p>

    <div class="startup-details">
      <p><strong>Valuation:</strong> {Number(startup.valuation).toLocaleString()}</p>
      <p><strong>Status:</strong> {startup.fundingStatus}</p>
    </div>

    <div class="startup-rating">
      <p class="rating-text">Rating:</p>
      <div class="bee-rating">
        {#each Array(5) as _, i}
          <div class="bee-wrapper" aria-hidden="true">
            <img src="/images/bee-icon.png" class="bee bee-bg" alt="" />
            <img
              src="/images/bee-icon.png"
              class="bee bee-fill"
              alt=""
              style="clip-path: {getFillClip(i)}"
            />
          </div>
        {/each}
      </div>
    </div>
  </div>
</a>

<style>
  .startup-card {
    --accent: var(--card-accent);
    position: relative;
    overflow: hidden;
    display: block;
    padding: 1.6rem;
    border-radius: 1rem;
    color: white;
    text-decoration: none;
    background: rgba(255, 255, 255, 0.05);
    backdrop-filter: blur(10px);
    border: 2px solid var(--accent, rgba(255,255,255,0.2));
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.25);
    transition: transform 0.25s, box-shadow 0.25s;
  }
  .startup-card:hover {
    transform: translateY(-3px);
    box-shadow: 0 0 20px var(--accent);
  }

  .card-bg {
    position: absolute;
    inset: 0;
    background-size: cover;
    background-position: center;
    filter: grayscale(1) brightness(0.7);
    opacity: 0.8;
    z-index: 0;
  }

  .card-body {
    position: relative;
    z-index: 1;
  }

  .industry-tag {
    display: inline-block;
    font-size: 0.65rem;
    text-transform: uppercase;
    letter-spacing: 0.03em;
    color: #fff;
    background: var(--accent, rgba(255,255,255,0.1));
    border: 1px solid var(--accent);
    padding: 0.25rem 0.6rem;
    border-radius: 999px;
    margin-bottom: 0.5rem;
  }

  .startup-name {
    margin: 0;
    font-size: 1.25rem;
    font-weight: bold;
  }

  .startup-description {
    margin: 0.5rem 0 1rem;
    font-size: 0.9rem;
    color: #ddd;
  }

  .startup-details p {
    margin: 0;
    font-size: 0.85rem;
    color: #bbb;
  }

  .startup-rating {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-top: 1rem;
    gap: 0.5rem;
  }

  .rating-text {
    font-size: 1rem;
    font-weight: 600;
    color: var(--accent);
  }

  .bee-rating { display: flex; gap: 4px; }
  .bee-wrapper { position: relative; width: 24px; height: 24px; }
  .bee       { position: absolute; top: 0; left: 0; width: 100%; height: 100%; }
  .bee-bg    { opacity: 0.2; filter: grayscale(1); }
  .bee-fill  { filter: drop-shadow(0 0 2px var(--accent)); }
</style>

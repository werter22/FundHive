describe('Entrepreneur creates a startup and multiple funding rounds', () => {
  beforeEach(() => {
    // Login as entrepreneur
    cy.visit('http://localhost:8080');
    cy.get('#username').type(Cypress.env('entrepreneur').email);
    cy.get('#password').type(Cypress.env('entrepreneur').password);
    cy.contains('button', 'Log in').click();
    cy.contains('h1', 'Welcome').should('be.visible');

    // Stub alert globally
    cy.window().then((win) => {
      cy.stub(win, 'alert').as('alertStub');
    });

  });

  it('creates startup and handles 3 funding rounds', () => {

    // Navigate to account page
    cy.contains('a', 'Account').click();
    cy.contains('h2', 'Create New Startup').should('exist');

    // ==== CREATE STARTUP ====
    cy.get('input').eq(0).clear().type('MooMentum');
    cy.get('textarea').eq(0).clear().type(
      'Livestreamed emotional support cows for remote workers, powered by Web3.'
    );
    cy.get('select').eq(0).select('Tech');
    cy.get('input[type="number"]').clear().type('3500000');
    cy.get('select').eq(1).select('Pre-Seed');

    cy.contains('button', 'Create Startup').click();
    cy.get('@alertStub').should('have.been.calledWith', 'Startup created successfully');

    cy.wait(1000);

    cy.contains('a', 'Startups').click();
    cy.contains('a', 'MooMentum').click();
    cy.contains('h1', 'MooMentum').should('exist');

    // === ROUND CREATION FUNCTION ===
    const createRound = (name: string, amount: number, date: string) => {
      cy.contains('button', 'Create New Funding Round').click();
      cy.get('#round-name').clear().type(name);
      cy.get('#goal-amount').clear().type(amount.toString());
      cy.get('#start-date').type(date);

      cy.contains('h5', 'Create New Funding Round')
        .parent()
        .within(() => {
          cy.contains('button', 'Save').click();
        });

      cy.get('@alertStub').should('have.been.calledWith', 'Funding Round created!');
    };

    // === ROUND 1: UPCOMING ===
    const round1 = { name: 'Round One', amount: 500000, date: '2025-06-01' };
    createRound(round1.name, round1.amount, round1.date);

    cy.contains('tr', round1.name).within(() => {
      cy.contains('td', 'UPCOMING').should('exist');
      cy.contains('button', 'Publish').should('be.visible');
      cy.contains('button', 'Cancel').should('be.visible');
    });

    // === ROUND 2: CANCEL ===
    const round2 = { name: 'Round Two', amount: 250000, date: '2025-06-05' };
    createRound(round2.name, round2.amount, round2.date);

    cy.contains('tr', round2.name).within(() => {
      cy.contains('button', 'Cancel').click();
    });

    cy.contains('tr', round2.name).within(() => {
      cy.contains('td', 'CANCELLED').should('exist');
      cy.contains('button', 'Publish').should('not.exist');
      cy.contains('button', 'Cancel').should('not.exist');
    });

    // === ROUND 3: PUBLISH ===
    const round3 = { name: 'Round Three', amount: 100000, date: '2025-06-10' };
    createRound(round3.name, round3.amount, round3.date);

    cy.contains('tr', round3.name).within(() => {
      cy.contains('button', 'Publish').click();
    });

    cy.contains('tr', round3.name).within(() => {
      cy.contains('td', 'OPEN').should('exist');
      cy.contains('button', 'Publish').should('not.exist');
    });
  });

  it('creates 3 additional creative startups for filter testing', () => {
    // Navigate to Account page
    cy.contains('a', 'Account').click();
    cy.contains('h2', 'Create New Startup').should('exist');
    // Create startups
    createStartup(
      'Barkchain',
      'Decentralized social media for dogs, powered by blockchain.',
      'Media',
      500000,
      'Pre-Seed'
    );

    createStartup(
      'HealthHolo',
      'Holographic doctors available on-demand for remote diagnostics.',
      'Healthcare',
      4500000,
      'Series A'
    );

    createStartup(
      'EduVerse',
      'Metaverse-powered education platform for gamified learning.',
      'Education',
      3200000,
      'Series B'
    );
  });

  function createStartup(
    name: string,
    description: string,
    industry: string,
    valuation: number,
    funding: string
  ) {
    cy.get('input').eq(0).clear().type(name);
    cy.get('textarea').eq(0).clear().type(description);
    cy.get('select').eq(0).select(industry);
    cy.get('input[type="number"]').clear().type(valuation.toString());
    cy.get('select').eq(1).select(funding);

    cy.contains('button', 'Create Startup').click();
    cy.get('@alertStub').should('be.calledWith', 'Startup created successfully');
    cy.wait(1000); // Optional: see next note
  }

});

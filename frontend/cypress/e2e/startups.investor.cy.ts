describe('Investor views a startup with open funding round', () => {
    beforeEach(() => {
        // === Login as Investor ===
        cy.visit('http://localhost:8080');
        cy.contains('button', 'Log In').click();
        cy.get('#username').type(Cypress.env('investor').email);
        cy.get('#password').type(Cypress.env('investor').password);
        cy.contains('button', 'Log in').click();
        cy.contains('h1', 'Welcome').should('be.visible');
    });

    it('navigates to the startup and verifies no entrepreneur controls are visible', () => {
        // === Navigate to MooMentum startup page ===
        cy.contains('a', 'Startups').click();
        cy.contains('a', 'MooMentum').click();
        cy.contains('h1', 'MooMentum').should('exist');

        // === Entrepreneur-only UI elements should NOT be visible ===
        cy.contains('button', 'Create New Funding Round').should('not.exist');
        cy.contains('button', 'Save Changes').should('not.exist');
        cy.contains('button', 'Cancel').should('not.exist');
        cy.contains('button', 'Publish').should('not.exist');
        cy.contains('h5', 'Create New Funding Round').should('not.exist');

        // === Invest buttons should only be visible for OPEN rounds ===
        cy.get('table').contains('td', 'OPEN').parents('tr').within(() => {
            cy.contains('button', 'Invest').should('be.visible');
        });

        // === No Invest button for CANCELLED and UPCOMING rounds ===
        cy.get('table').contains('td', 'CANCELLED').parents('tr').within(() => {
            cy.contains('button', 'Invest').should('not.exist');
        });
        cy.get('table').contains('td', 'UPCOMING').parents('tr').within(() => {
            cy.contains('button', 'Invest').should('not.exist');
        });

        // === Click "Invest" on Round Three (which must be OPEN) ===
        cy.get('table').contains('tr', 'Round Three').within(() => {
            cy.contains('button', 'Invest').click();

            // === Confirm the investment form appears ===
            cy.get('input[placeholder="Amount"]').should('be.visible');
            cy.contains('button', 'Confirm').should('be.visible');
            cy.contains('button', 'Cancel').should('be.visible');

            cy.get('input[placeholder="Amount"]').type('50000');
            cy.contains('button', 'Confirm').click();
            cy.wait(1000);
        });

        // === Handle success alert ===
        cy.on('window:alert', (text) => {
            expect(text).to.contain('Investment successful!');
        });

        // === Validate that investment data updated ===
        cy.contains('strong', 'Total Raised:')
            .parent()
            .should('contain.text', '$50.000');

        cy.get('table').contains('tr', 'Round Three').within(() => {
            cy.contains('$50.000').should('exist');
        });

        cy.wait(1000);

        // === Invest again with 30,000 ===
        cy.get('table').contains('tr', 'Round Three').within(() => {
            cy.contains('button', 'Invest').click();
            cy.get('input[placeholder="Amount"]').type('30000');
            cy.contains('button', 'Confirm').click();
        });

        cy.wait(1000);

        // === Invest again with 40,000 ===
        cy.get('table').contains('tr', 'Round Three').within(() => {
            cy.contains('button', 'Invest').click();
            cy.get('input[placeholder="Amount"]').type('40000');
            cy.contains('button', 'Confirm').click();
        });

        cy.wait(1000);

        // === Validate total amount has reached goal, status is CLOSED ===
        cy.get('table').contains('tr', 'Round Three').within(() => {
            cy.contains('CLOSED').should('exist');
            cy.contains('button', 'Invest').should('not.exist');
            cy.contains('$120.000').should('exist'); // Amount Raised
        });

        // === Validate total raised in overview ===
        cy.contains('strong', 'Total Raised:')
            .parent()
            .should('contain.text', '$120.000');

        // === Navigate to Transactions tab ===
        cy.contains('a', 'Transactions').click();

        const expectedAmounts = ['$50.000', '$30.000', '$40.000'];
        const today = new Date().toISOString().split('T')[0]; // Format: YYYY-MM-DD

        // Navigate to Transactions page
        cy.contains('a', 'Transactions').click();
        cy.contains('h1', 'Investor Portfolio').should('exist');

        // Summary check
        cy.contains('p', 'Total Investment:').should('contain', '$120.000');
        cy.contains('p', 'Transactions Made:').should('contain', '3');

        // Transactions list check
        cy.get('table tbody tr').should('have.length', 3);

        // Validate each transaction
        cy.get('table tbody tr').each(($row, index) => {
            cy.wrap($row).within(() => {
                cy.contains('MooMentum').should('exist');
                cy.contains('Round Three').should('exist');
                cy.contains(expectedAmounts[index]).should('exist');
                cy.contains(today).should('exist');
            });
        });
    });

    it('filters startups by industry and funding status', () => {
        // === Go to Startups page ===
        cy.contains('a', 'Startups').click();
        cy.contains('h1', 'Startups').should('exist');

        // === Filter by Industry: Media ===
        cy.get('select').eq(0).select('Media'); // Industry
        cy.get('button').contains('Search').click();

        // === Only Barkchain should appear ===
        cy.get('.startup-card').should('have.length', 1);
        cy.get('.startup-card').contains('Barkchain').should('exist');
        cy.get('.startup-card').contains('MEDIA').should('exist');

        // === Clear filters ===
        cy.get('select').eq(0).select('All industries');

        // === Filter by Funding: Series A ===
        cy.get('select').eq(1).select('Series A'); // Funding
        cy.get('button').contains('Search').click();

        cy.get('.startup-card').should('have.length', 1);
        cy.get('.startup-card').contains('HealthHolo').should('exist');
        cy.get('.startup-card').contains('SERIES_A').should('exist');

        // === Combined filter: Education + Series B ===
        cy.get('select').eq(0).select('Education');
        cy.get('select').eq(1).select('Series B');
        cy.get('button').contains('Search').click();

        cy.get('.startup-card').should('have.length', 1);
        cy.get('.startup-card').contains('EduVerse').should('exist');

        // === No results case ===
        cy.get('select').eq(0).select('Automotive');
        cy.get('select').eq(1).select('Seed');
        cy.get('button').contains('Search').click();

        cy.contains('No startups found.').should('exist');

        // === Reset filters ===
        cy.get('select').eq(0).select('All industries');
        cy.get('select').eq(1).select('All funding statuses');
        cy.get('button').contains('Search').click();

        cy.get('.startup-card').should('have.length.at.least', 4);
    });
});
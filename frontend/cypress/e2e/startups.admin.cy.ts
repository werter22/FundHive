describe('Manage startups as admin', () => {
    before(() => {
        cy.clearAllSessionStorage();

        cy.visit('http://localhost:8080');

        cy.contains('button', 'Log In').click();

        cy.get("#username").type(Cypress.env()["admin"].email);
        cy.get("#password").type(Cypress.env()["admin"].password);

        cy.contains("button", "Log in").click();

        cy.contains('h1', 'Welcome').should('be.visible')

        cy.window().then((win) => {
            const token = win.sessionStorage.getItem("jwt_token");

            cy.request({
                method: "DELETE",
                url: "http://localhost:8080/api/deleteAll",
                headers: {
                    Authorization: `Bearer ${token}`,
                }
            });
        });
    });

    it('navigates to the Startups page and shows it empty', () => {
        cy.contains('a', 'Startups').click();
        cy.location('pathname').should('eq', '/startups');
        cy.contains('h1', 'Startups').should('be.visible');
        cy.contains('No startups found').should('exist');
    });
});
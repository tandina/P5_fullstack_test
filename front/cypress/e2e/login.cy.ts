describe("Login spec", () => {
  it("register user ", () => {
    cy.visit("/register");

    cy.get("input[formControlName=firstName]").type("tandina");
    cy.get("input[formControlName=lastName]").type("toure");
    cy.get("input[formControlName=email]").type("toure@toure.com");
    cy.get("input[formControlName=password]").type(
      `${"tourepass"}{enter}{enter}`
    );
  });
  it('submit button sould be disabled when email input is empty', () => {
    cy.visit('/login')

    cy.get('input[formControlName=password]').type(`${"tourepass"}`)
    cy.get('button[type=submit]').should('be.disabled')
  })

  it('submit button sould be disabled when password input is empty', () => {
    cy.visit('/login')

    cy.get('input[formControlName=email]').type("toure@toure.com")
    cy.get('button[type=submit]').should('be.disabled')
  })
});
  it("Login successfull", () => {
    cy.visit("/login");

    cy.intercept(
      {
        method: "GET",
        url: "/api/session",
      },
      []
    ).as("session");

    cy.get("input[formControlName=email]").type("toure@toure.com");
    cy.get("input[formControlName=password]").type(
      `${"tourepass"}{enter}{enter}`
    );

    cy.url().should("include", "/sessions");
  });

  it("display user information", () => {
    cy.visit("/login");
    cy.intercept(
      {
        method: "GET",
        url: "/api/session",
      },
      []
    ).as("session");

    cy.get("input[formControlName=email]").type("toure@toure.com");
    cy.get("input[formControlName=password]").type(
      `${"tourepass"}{enter}{enter}`
    );

    cy.url().should("include", "/sessions");

    cy.contains("Account").click();
  });
});

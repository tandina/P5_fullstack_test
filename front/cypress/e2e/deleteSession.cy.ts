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

  it("delete user session", () => {
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
    cy.contains("Detail").click();
  });
});

describe("Login spec", () => {
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
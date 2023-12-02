describe("register spec", () => {
  it("register user ", () => {
    cy.visit("/register");

    cy.get("input[formControlName=firstName]").type("tandina");
    cy.get("input[formControlName=lastName]").type("toure");
    cy.get("input[formControlName=email]").type("toure@toure.com");
    cy.get("input[formControlName=password]").type(
      `${"tourepass"}{enter}{enter}`
    );
  });
});

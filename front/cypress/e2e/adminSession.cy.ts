describe("Admin session spec", () => {
  const sessionName = "Cours de Chuck";
  const newSessionName = "Karaté Session";
  const sessionDate = "2023-12-20";
  const sessionTeacher_id = 1;
  const sessionTeacherFistName = "DELAHAYE";
  const sessionTeacherLastName = "Margot";
  const sessionDescription = "personne ne supprime la session de Chuck Norris";

  const teacherData = [
    {
      id: 1,
      lastName: sessionTeacherLastName,
      firstName: sessionTeacherFistName,
      createdAt: new Date(),
      updatedAt: new Date(),
    },
  ];

  const sessionData = {
    id: 1,
    name: sessionName,
    date: sessionDate,
    teacher_id: sessionTeacher_id,
    description: sessionDescription,
    users: [],
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  const interceptApi = (method, url, status, body) => {
    cy.intercept(method, url, { status, body });
  };

  const typeInFormControl = (controlName, value) => {
    cy.get(`input[formControlName=${controlName}]`).type(value);
  };

  const clickButton = (text) => {
    cy.get("button span").contains(text).click();
  };

  const checkUrl = (url) => {
    cy.url().should("include", url);
  };

  it("Login successfull", () => {
    cy.visit("/login");
    cy.intercept(
      {
        method: "GET",
        url: "/api/session",
      },
      []
    ).as("session");

    cy.get("input[formControlName=email]").type("yoga@studio.com");
    cy.get("input[formControlName=password]").type(
      `${"test!1234"}{enter}{enter}`
    );

    cy.url().should("include", "/sessions");
  });

  it("create a session", () => {
    interceptApi("GET", "/api/teacher", 200, teacherData);
    interceptApi("POST", "/api/session", 200, {});
    interceptApi("GET", "/api/session", 200, [sessionData]);

    cy.contains("Create").click();

    checkUrl("/sessions/create");

    typeInFormControl("name", sessionName);
    typeInFormControl("date", sessionDate);

    cy.get("mat-select[formControlName=teacher_id]").click();
    cy.get("mat-option")
      .contains(`${sessionTeacherFistName} ${sessionTeacherLastName}`)
      .click();

    cy.get("textarea[formControlName=description]").type(sessionDescription);
    cy.contains("Save").click();
  });
});

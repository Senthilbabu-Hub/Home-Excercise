Feature: SauceDemo Login and Cart functionalities

@Incorrect
  Scenario: Log in with incorrect password and detect error message
    Given User navigate to SauceDemo login page
    When User log in with incorrect credentials
    Then User should see an error message
@Correct
  Scenario: Log in with correct password
    Given User navigate to SauceDemo login page
    When User log in with valid credentials
    Then User should be redirected to the inventory page
    Then Add the 4 cheapest items to the cart and the total price should be below $50
    And User navigate to the cart page
    
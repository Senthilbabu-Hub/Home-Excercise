Feature: SauceDemo Login and Cart functionalities

@Incorrect
  Scenario Outline: Log in with incorrect password and detect error message
    Given User navigate to SauceDemo URL "<url>"
    When User log in with incorrect username "<username>" and password "<password>"
    Then User should see an error message
    
    Examples: 
    |url                        | username      | password      | 
    |https://www.saucedemo.com  | standard_user | secret_sauce1 | 
    
    
@Correct
  Scenario Outline: Log in with correct password
    Given User navigate to SauceDemo URL "<url>"
    When User log in with valid username1 "<username>" and password1 "<password>"
    Then User should be redirected to the inventory page
    Then Add the 4 cheapest items to the cart and the total price should be below $50
    And User navigate to the cart page
    
    Examples: 
    |url                        | username      | password      | 
    |https://www.saucedemo.com  | standard_user | secret_sauce  | 
    
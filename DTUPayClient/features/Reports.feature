Feature: Fetch DTUPay reports
  Scenario: Get all reports from DTUPay successfully
    Given There are 4 customers and merchants in the system
    And 2 payments have been done by 2 separate customers
    When Fetching all reports, it returns a list of payments for all customers and merchants
    Then The payments made beforehand should match the fetched payments

  Scenario: Get all reports for a specific customer from DTUPay successfully
    Given There are 4 customers and merchants in the system
    And 2 payments have been done by 2 separate customers
    When Fetching all reports from 1 customer
    When Fetching all reports, it returns a list of payments for all customers and merchants
    Then A list of payments for that customer are returned and contained within all DTPPay reports

  Scenario: Get all reports for a specific merchant from DTUPay successfully
    Given There are 4 customers and merchants in the system
    And 2 payments have been done by 2 separate customers
    When Fetching all reports from 1 merchant
    When Fetching all reports, it returns a list of payments for all customers and merchants
    Then A list of payments for that merchant are returned

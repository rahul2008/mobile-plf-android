@automated
@android
Feature: BleStrategy component
  
  Scenario: Strategy is not available
    Given the BleStrategy is initialized with id "x"
    Then the BleStrategy is not available

  Scenario: Strategy is available
    Given a mock device is found with id "x"
    And the BleStrategy is initialized with id "x"
    Then the BleStrategy is available

  Scenario:


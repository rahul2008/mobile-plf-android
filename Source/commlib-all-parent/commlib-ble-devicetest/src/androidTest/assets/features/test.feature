Feature: DIComm BLE communication on device
  DIComm BLE communication must behave according the requirements on a real device.

  Background:
    Given distance between phone and BLE Reference Node is 50 cm

  Scenario: Get value from Time Port
    Given a BLE Reference Node is discovered and selected
    And "Stay connected" is disabled
    And "Subscribe to time port" is disabled
    When application requests time value from time port
    Then time value is received without errors
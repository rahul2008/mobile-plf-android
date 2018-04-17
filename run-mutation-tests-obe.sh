#!/bin/bash

./gradlew :mya-catk:testDebug  \
  :mya-catk:pitestDebug \
  :mya-csw:testDebug \
  :mya-csw:pitestDebug \
  :dataServices:testDebug \
  :dataServices:pitestDebug \
  :dataServicesUApp:testDebug \
  :dataServicesUApp:pitestDebug

# order-service (integration tests demo)

## The challenge
A common challenge when doing integration tests is that if they are overdone to validate too much logic, it can slow down the test suite delaying feedback and increasing CI time. 

## What this repo does
This repo demonstrates how to implement integration tests the right way and avoid overdoing it.

The approach this repo recommends/demonstrates is to add just the bare minimum integration tests to validate the integration of the service with external components and cover all the other logic in the Unit test layer for faster feedback and reduced CI time.

Appropriate number of integration tests are added for each external component that the order-service integrates with.

## External components order-service integrates with
1. API services (notification-Service, loyalty-service)
2. Database (postgres)
3. Queue (rabbit-mq)
4. OAuth (okta)

## Notes
- Uses wiremock for mocking APIs services and oauth api calls
- Uses docker containers for spinning up postgres and rabbitmq components for integration tests
- Uses flyway for db migration scripts
- Uses dummy public/private key pair (pem files) in test/resources for generating JWT tokens for oauth integration tests
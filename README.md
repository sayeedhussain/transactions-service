# transactions-service (Layered Architecture Pattern demo)

## What this repo does
This repo demonstrates how to implement Layered Architecture Pattern.

## Running the app
Spin up postgres
```bash
docker-compose down && docker-compose up
```
Run the application
```bash
./gradlew bootRun 
```
Hit the POST endpoint
```bash
curl -v -X POST http://localhost:8080/api/transactions \
-H "Content-Type: application/json" \
-d '{
  "sourceAccountId": 1,
  "destinationAccountId": 2,
  "amount": 500.00
}'
```

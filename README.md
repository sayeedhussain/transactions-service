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
  "customerId": 123,
  "items": [
    {
      "name": "Laptop",
      "qty": 1,
      "price": 50000.00
    },
    {
      "name": "Mouse",
      "qty": 2,
      "price": 1500.00
    }
  ]
}'
```

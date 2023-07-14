

# CURRENCY RATE SERVICE


## Features
- Docker compose file for MongoDB
- MongoExpress
- Connection to external api for getting actual rate -> https://apilayer.com/marketplace/exchangerates_data-api

Api posibility:
- get all available rates
- get rate for distinct currency
- convert one currency to another


## API Reference

```http
http://localhost:8080/api/rates/all
```
```http
http://localhost:8080/api/rates?currency=CZK
```
```http
http://localhost:8080/api/rates/convert?sourceCurrency=UAH&targetCurrency=CZK&amount=1
```
#### MONGO EXPRESS

```http
http://localhost:8081
```



## Run Locally

Maven install

```bash
  mvn clean install
```

Run docker container

```bash
  docker-compose up -d 
```

Let's go

```bash
  run project
```




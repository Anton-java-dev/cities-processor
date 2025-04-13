# Cities Data Processor

A Spring Boot application to process city data (population, area) from provided JSON/CSV files.

## Prerequisites
- Java 17
- Gradle 8.x

## How to Run
1. Clone this repository:
   ```bash
   git clone https://github.com/Anton-java-dev/cities-processor.git 

2. Build and run the application:
    ```bash
   ./gradlew bootRun  # Linux/Mac
   .\gradlew.bat bootRun  # Windows
   
## API

### Endpoints
Get cities data

#### `GET /api/cities`
- **Description**: Get city data from a specified file format with optional sorting.
- **Query Parameters**:
    - `dataFormat` (required): `CSV` or `JSON`
    - `sortField` (optional default: NAME): one of the following:
        - `NAME`
        - `POPULATION`
        - `AREA`
    - `isAsc` (optional default:true): `true` for ascending, `false` for descending
    - `nameContains` (optional): filter cities that contain the given substring in their name (case-insensitive)

- **Request Examples**:

Get cities from JSON file:

curl "http://localhost:8080/api/cities?dataFormat=JSON"

Sort cities by population in descending order:

curl "http://localhost:8080/api/cities?dataFormat=CSV&sortField=POPULATION&isAsc=false"

Filter cities that contain "ham" in the name

curl "http://localhost:8080/api/cities?dataFormat=CSV&nameContains=ham"

- **Responses**:

`200 OK`
```json
[
{
"name": "Berlin",
"population": 1000,
"area": 10.0,
"density": 100.0
},
{
"name": "Hamburg",
"population": 800,
"area": 8.0,
"density": 100.0
}
]
```
`400 Bad Request`:
```
Data loading failed: JSON file not found at path: data/citis.json
```

#### `POST /api/cities`

- **Description**: Add a new city at runtime
- **Request Body** (JSON):
```json
{
  "name": "Munich",
  "population": 1000,
  "area": 12.3
}
```
- **Validation**

name: non-empty string

population: positive integer

area: positive double

- **Request Examples**:

curl -X POST "http://localhost:8080/api/cities" \
-H "Content-Type: application/json" \
-d '{"name":"Tokyo","population":13960000,"area":2194.0}'

- **Responses**:

`200 OK` - Successfully added city

`400 BAD REQUEST` - Validation failure
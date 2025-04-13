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

- **Examples**:

Get cities from JSON file:

curl "http://localhost:8080/api/cities?dataFormat=JSON"

Sort cities by population in descending order:

curl "http://localhost:8080/api/cities?dataFormat=CSV&sortField=POPULATION&isAsc=false"

- **Response Examples**:

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
```json
Data loading failed: JSON file not found at path: data/citis.json
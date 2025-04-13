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
- **Description**: Get city data from specified file format
- **Parameters**:
   - `dataFormat` (required): `CSV` or `JSON`
- **Example**:
  ```bash
  curl "http://localhost:8080/api/cities?dataFormat=JSON"
- **Response Examples**:

`200 OK`:
```json
[
  {
    "name": "Berlin",
    "population": 3769000,
    "area": 891.7
  }
]
```
`400 Bad Request`:
```json
Data loading failed: JSON file not found at path: data/citis.json
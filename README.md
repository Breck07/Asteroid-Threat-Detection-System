# Asteroid-Threat-Detection-System

A multi-service desktop application that retrieves real-time Near-Earth Object (NEO) data from NASA and presents asteroid threat information through a Java Swing interface.

The system uses a **Python FastAPI service** as an API layer between NASA's NEO API and a **Java application**, demonstrating REST communication, JSON processing, external API integration, and service separation.

## Architecture- Yes I used ai for the text display below of the architecture!

                    NASA NEO API
                         │
                         │ HTTPS
                         ▼
              ┌─────────────────────┐
              │    Python FastAPI   │
              │     API Service     │
              └──────────┬──────────┘
                         │
                         │ HTTP / JSON
                         ▼
              ┌─────────────────────┐
              │    Java Backend     │
              │      HttpClient     │
              │       Gson          │
              └──────────┬──────────┘
                         │
                         ▼
              ┌─────────────────────┐
              │    Java Swing UI    │
              │  Threat Information │
              └─────────────────────┘

## Features

* Retrieves Near-Earth Object data from NASA's API
* Displays asteroid names and threat information
* Identifies potentially hazardous asteroids
* Displays estimated miss distance
* Displays asteroid velocity
* Processes multiple asteroids across multiple dates
* Separates external API communication from the desktop application
* Uses environment variables for API credentials
* Handles upstream API/request failures
* Provides a graphical desktop interface

## Technologies

### Java

* Java
* Maven
* Java `HttpClient`
* Gson
* Java Swing
* FlatLaf

### Python

* Python
* FastAPI
* Requests
* python-dotenv
* Uvicorn

### External API

* NASA Near Earth Object Web Service (NeoWs)

## How It Works

### 1. NASA Data Retrieval

The FastAPI service communicates with NASA's Near Earth Object API and requests asteroid data for a specified date range.

### 2. API Service

FastAPI exposes an endpoint that acts as an abstraction layer between the NASA API and the Java application.

The service handles:

* Request parameters
* NASA API communication
* API authentication
* JSON responses
* Upstream request errors

### 3. Java Backend

The Java application communicates with the FastAPI service using Java's built-in `HttpClient`.

The response is parsed using Gson and the relevant asteroid information is extracted from NASA's nested JSON structure.

The application processes information such as:

* Asteroid name
* Potentially hazardous classification
* Relative velocity
* Miss distance

### 4. Desktop Interface

The processed information is presented through a Java Swing graphical interface, allowing users to view asteroid threat information without interacting directly with the APIs.

## Configuration

The FastAPI service requires a NASA API key.

Create a `.env` file inside the `api-services` directory:

```env
API_KEY=your_nasa_api_key
```

The `.env` file should **not** be committed to Git.

You can obtain a NASA API key through NASA's API services.

## Running the Project

### Start the FastAPI Service

Navigate to the API service directory:

```bash
cd ATDS-APP/api-services
```

Install the Python dependencies:
FastAPI-Uvicorn-Pydantic-Dotenv


Start the FastAPI server:

```bash
uvicorn main:app --reload
```

The API service will run locally and provide the endpoint used by the Java application.

### Run the Java Application

Navigate to the Maven project:

```bash
cd ATDS-APP/App/atds
```

Build the project:

```bash
mvn clean package
```

Then run the generated application according to the project's Maven configuration.

The FastAPI service must be running before using the Java application.

## Error Handling

The API service handles failures when communicating with NASA and returns an appropriate HTTP error response to the Java application.

The Java application also checks the HTTP response status before attempting to process the returned JSON.

This prevents the application from treating failed API responses as valid asteroid data.

## Why This Project?

This project was built to practice real-world software engineering concepts rather than relying on a single application layer.

The project demonstrates:

* Designing multiple services with separate responsibilities
* Building and consuming REST APIs
* HTTP communication between applications
* Working with nested JSON data
* Integrating external APIs
* Environment-based configuration
* Error handling
* Java application development
* Python backend development

## Future Improvements

Potential future improvements include:

* Introduce strongly typed Java model classes for asteroid data
* Add automated unit and integration tests
* Improve API validation
* Add request timeouts and retry handling
* Improve data visualization
* Add additional NASA datasets
* Package the application as a standalone executable
* Improve logging and diagnostics

## License

This project is for educational and portfolio purposes.

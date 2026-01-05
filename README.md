# arguesEye API

arguesEye API is a backend service that integrates with **urlscan.io** to retrieve website analysis details.  
It allows clients to submit URLs and receive scan-related information for security analysis and inspection.

## 🚀 Features

- Token-based access with daily usage limits
- Each user receives a limited number of API calls per day
- Send URLs for scanning
- Retrieve scan details from **urlscan.io**
- RESTful API with JSON responses
- Built with scalability and extensibility in mind

## Tech Stack

- Java
- Spring Boot
- REST APIs
- urlscan.io API

## API Overview

The API exposes endpoints that allow clients to:

- Submit a URL for scanning
- Fetch scan-related details from **urlscan.io**

> This service acts as a middleware between client applications and the urlscan.io API.

## ⚙️ Setup & Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/LakshanWC/ArguesEyeAPI.git

2. Create a .env file or configure your environment variables for your API keys:

```bash
   URL_SCAN_API_KEY=your_urlscan_api_key_here
   DEBUG_API_KEY=your_debug_api_key_here

Docker Deployment

This project includes a Dockerfile located in the service folder, allowing you to build and run the API in a container.

Build and run locally
# Navigate to the service folder
cd service

# Build the Docker image
docker build -t argueseye-api .

# Run the container
docker run -p 8088:8088 argueseye-api

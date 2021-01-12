# Read Me First

API Doc can be found:

https://app.swaggerhub.com/apis/milosnkb/tender-demo_api/v0
http://localhost:8080/swagger-ui.html

# Getting Started

1. mvn clean package - This will also run the tests
2. docker build -t tender-demo .
3. docker run -it --expose 8080 -p 8080:8080 tender-demo - it will run app on port defined in Dockerfile in case port is in already in use





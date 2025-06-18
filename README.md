# Charging Session Service

Kotlin, Springboot Microservice for managing EV charging sessions with async Kafka-based authorization.

## Quick Start

### Prerequisites
- Docker 20+
- Kafka (included in Docker)

### Run Locally

# 1. Clone repo
git clone https://github.com/your-repo/charging-service.git
cd $Project-Folder

# 2. Start services (Kafka + Apps)
docker-compose up --build

# 3. Verify services
docker-compose ps

# 4. Verify Api 

Through swagger "http://localhost:8082/swagger-ui/index.html#/"
Or through Postman, http://localhost:8082/api/v1/charging/start-charging

# 5. Curl for Success Scenario

curl -X 'POST' \
  'http://localhost:8082/api/v1/charging/start-charging' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "stationId": "123e4567-e89b-12d3-a456-426614174000",
    "driverToken": "validDriverToken-new-Token-1234",
    "callbackUrl": "http://localhost:8082/api/v1/callback/get-callback"
}'

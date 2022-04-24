# logcollection
Log Collection Take Home Project for Cribl for Yves do Régo interviews

## Required

* Java 11+
* Maven 3.6 (tested)
* Docker 19+ (for running integration tests)
* Docker compose (For running a local deployment)

## Build log-collector-server:latest 

```

cd logcollection
docker build -f docker/Dockerfile -t log-collector-server:latest .

```

## Start a local deployment of log-collector-server using docker-compose

```
docker-compose -f docker/docker-compose.yml up
```

You can check the status of the application with the following command. The returned status should be "status": "UP"

```
curl http://localhost:8090/logCollector/health
```

# logcollection

Log Collection Take Home Project for Yves do Régo Cribl interviews

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

The log collector server is currently listening at port 8090.

You can check the status of the application with the following command. The returned status should be "status": "UP"

```
curl http://localhost:8090/logCollector/health
```

## Using the log-collector-server API

### Get the list of files present in the server /var/log directory

You can get the list of files present in a speficied directory.

```
GET http://<target-server>:8090/logCollector/get-files?directoryPath=/var/log&matchingExtensions=txt,log
```

List of supported request parameters:

- `target-server` = localhost
- `directoryPath` = Directory path to search
- `matchingExtensions` = Optional parameter to be used to return only matching files extensions. This is a comma separated list. For example matchingExtensions=txt.log,out

#### Curl Command
```
curl http://localhost:8090/logCollector/get-files?directoryPath=/var/log&matchingExtensions=txt,log
```


### Get the list of events from a specified file.

<TBD>






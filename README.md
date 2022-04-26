# Log Collection

Log Collection Take Home Project


## Software Required on your system...

* Java 11+
* Maven 3.6 (tested)
* Docker 19+ (for running integration tests)
* Docker compose (For running a local deployment)

</br>

## Step 1: Get the project from Github

Git clone repository https://github.com/ydorego/logcollection.git

</br>
</br>

## Step 2: Build log-collector-server docker image 


```
cd logcollection
docker build -f docker/Dockerfile -t log-collector-server:latest .
```

</br>
</br>

## Step 3: Start a local deployment of log-collector-server using docker-compose

```
docker-compose -f docker/docker-compose.yml up
```

The log collector server is currently listening at port 8090. 

To check the status of the application with the following command. The returned status should be "status": "UP"

```
curl http://localhost:8090/logCollector/health
```

</br>
</br>

## Using the log-collector-server API

The API currently offers the ability to list the content of a directory and the ability to collect logs from a specified file. We took the
liberty to adjust some of the take home requirements to provide a flebiel solution to the end-user. 

Changes from the initial requirements:
* Addition of a get-files API
* Log files events retrieval are no longer limited to /var/log. The full path of the file is provided by the user.

</br>

### Get the list of files present in the server /var/log directory

You can get the list of files present in a speficied directory.

```
GET http://<target-server>:8090/logCollector/get-files?directoryPath=/var/log&matchingExtensions=txt, log
```

List of supported request parameters:

- `target-server` = localhost
- `directoryPath` = Directory path to search
- `matchingExtensions` = Optional parameter to be used to return only matching files extensions. This is a comma separated list. For example matchingExtensions=txt.log,out

</br>

```
curl http://localhost:8090/logCollector/get-files?directoryPath=/var/log&matchingExtensions=txt,log
```

**Returned Payload:**

```
{
    "directoryPath": "/var/log",
    "matchingExtensions": "txt, log",
    "files": [
        {
            "fileName": "/var/log/alternatives.log",
            "length": 502,
            "lastModified": "2022-04-20T10:54:19.000+00:00",
            "directory": false
        },
        {
            "fileName": "/var/log/dpkg.log",
            "length": 24254,
            "lastModified": "2022-04-20T10:54:19.000+00:00",
            "directory": false
        }
    ]
}
```

</br>
</br>

### Get the list of events from a specified file.

You can get x number of evetns from a specified log file. As per the requirement the evetns are listed in a reverse time order.

```
GET http://<target-server>:8090/logCollector/get-events?fileName=/var/log/dpkg.log&numberOfEvents=20&filter=contains-all:test
```

List of supported request parameters:

- `target-server` = localhost
- `fileName` = Full path name of the log file to process
- `numberOfEvents` = Maximum number of events to return.
- `filter` = Optional filter.


</br>

```
curl http://localhost:8090/logCollector/get-events?fileName=/var/log/dpkg.log&numberOfEvents=20&filter=contains-all:test
```

**Returned Payload:**

```
{
    "fileName": "/var/log/dpkg.log",
    "numberOfEvents": 20,
    "filter": "contains-all:test",
    "timeRequested": "2022-04-25T02:19:56.093+00:00",
    "timeCompleted": "2022-04-25T02:19:56.251+00:00",
    "events": [
        "",
        "2022-04-20 10:54:19 startup packages configure",
        "2022-04-20 10:54:19 status not-installed readline-common:all <none>",
        "2022-04-20 10:54:19 status config-files readline-common:all 8.1-1",
        "2022-04-20 10:54:19 purge readline-common:all 8.1-1 <none>",
        "2022-04-20 10:54:19 status not-installed dirmngr:amd64 <none>",
        "2022-04-20 10:54:19 status config-files dirmngr:amd64 2.2.27-2+deb11u1",
        "2022-04-20 10:54:19 purge dirmngr:amd64 2.2.27-2+deb11u1 <none>",
        "2022-04-20 10:54:19 status not-installed wget:amd64 <none>",
        "2022-04-20 10:54:19 status config-files wget:amd64 1.21-1+deb11u1",
        "2022-04-20 10:54:19 purge wget:amd64 1.21-1+deb11u1 <none>",
        "2022-04-20 10:54:19 status not-installed gpg-agent:amd64 <none>",
        "2022-04-20 10:54:19 status config-files gpg-agent:amd64 2.2.27-2+deb11u1",
        "2022-04-20 10:54:19 purge gpg-agent:amd64 2.2.27-2+deb11u1 <none>",
        "2022-04-20 10:54:19 startup packages purge",
        "2022-04-20 10:54:19 status installed libc-bin:amd64 2.31-13+deb11u3",
        "2022-04-20 10:54:19 status half-configured libc-bin:amd64 2.31-13+deb11u3",
        "2022-04-20 10:54:19 trigproc libc-bin:amd64 2.31-13+deb11u3 <none>",
        "2022-04-20 10:54:19 startup packages configure",
        "2022-04-20 10:54:19 status config-files readline-common:all 8.1-1"
    ]
}  
```


## Bonus Requirement Cluster master-workers

</br>

### Docker compose command  for the 3 nodes cluster.

```
docker-compose -f docker/docker-compose-cluster.yml up
```

### Get the list of events from a server delegating to workers

You can get x number of events from a specified log file. As per the requirement the evetns are listed in a reverse time order.

```
GET http://localhost:8090/logCollector/get-events-from-servers?fileName=/var/log/dpkg.log&numberOfEvents=20&filter=contains-all:test&serverList=log-collector-worker1,log-collector-worker2
```

List of supported request parameters:

- `target-server` = localhost
- `fileName` = Full path name of the log file to process
- `numberOfEvents` = Maximum number of events to return.
- `serverList` = List of servers to retrieve events. In the above example log-collector-worker1 and log-collector-worker2
- `filter` = Optional filter.

</br>

```
curl http://localhost:8090/logCollector/get-events-from-servers?fileName=/var/log/dpkg.log&numberOfEvents=20&filter=contains-all:test&serverList=log-collector-worker1,log-collector-worker2
```

**Returned Payload:**

```
[
    {
        "serverName": "log-collector-worker1",
        "logEventsResponse": {
            "fileName": "/var/log/dpkg.log",
            "numberOfEvents": 20,
            "filter": "contains-all:test",
            "timeRequested": "2022-04-25T04:26:52.774+00:00",
            "timeCompleted": "2022-04-25T04:26:52.938+00:00",
            "events": [
                "",
                "2022-04-20 10:54:19 startup packages configure",
                "2022-04-20 10:54:19 status not-installed readline-common:all <none>",
                "2022-04-20 10:54:19 status config-files readline-common:all 8.1-1",
                "2022-04-20 10:54:19 purge readline-common:all 8.1-1 <none>",
                "2022-04-20 10:54:19 status not-installed dirmngr:amd64 <none>",
                "2022-04-20 10:54:19 status config-files dirmngr:amd64 2.2.27-2+deb11u1",
                "2022-04-20 10:54:19 purge dirmngr:amd64 2.2.27-2+deb11u1 <none>",
                "2022-04-20 10:54:19 status not-installed wget:amd64 <none>",
                "2022-04-20 10:54:19 status config-files wget:amd64 1.21-1+deb11u1",
                "2022-04-20 10:54:19 purge wget:amd64 1.21-1+deb11u1 <none>",
                "2022-04-20 10:54:19 status not-installed gpg-agent:amd64 <none>",
                "2022-04-20 10:54:19 status config-files gpg-agent:amd64 2.2.27-2+deb11u1",
                "2022-04-20 10:54:19 purge gpg-agent:amd64 2.2.27-2+deb11u1 <none>",
                "2022-04-20 10:54:19 startup packages purge",
                "2022-04-20 10:54:19 status installed libc-bin:amd64 2.31-13+deb11u3",
                "2022-04-20 10:54:19 status half-configured libc-bin:amd64 2.31-13+deb11u3",
                "2022-04-20 10:54:19 trigproc libc-bin:amd64 2.31-13+deb11u3 <none>",
                "2022-04-20 10:54:19 startup packages configure",
                "2022-04-20 10:54:19 status config-files readline-common:all 8.1-1"
            ]
        }
    },
    {
        "serverName": "log-collector-worker2",
        "logEventsResponse": {
            "fileName": "/var/log/dpkg.log",
            "numberOfEvents": 20,
            "filter": "contains-all:test",
            "timeRequested": "2022-04-25T04:26:55.541+00:00",
            "timeCompleted": "2022-04-25T04:26:55.727+00:00",
            "events": [
                "",
                "2022-04-20 10:54:19 startup packages configure",
                "2022-04-20 10:54:19 status not-installed readline-common:all <none>",
                "2022-04-20 10:54:19 status config-files readline-common:all 8.1-1",
                "2022-04-20 10:54:19 purge readline-common:all 8.1-1 <none>",
                "2022-04-20 10:54:19 status not-installed dirmngr:amd64 <none>",
                "2022-04-20 10:54:19 status config-files dirmngr:amd64 2.2.27-2+deb11u1",
                "2022-04-20 10:54:19 purge dirmngr:amd64 2.2.27-2+deb11u1 <none>",
                "2022-04-20 10:54:19 status not-installed wget:amd64 <none>",
                "2022-04-20 10:54:19 status config-files wget:amd64 1.21-1+deb11u1",
                "2022-04-20 10:54:19 purge wget:amd64 1.21-1+deb11u1 <none>",
                "2022-04-20 10:54:19 status not-installed gpg-agent:amd64 <none>",
                "2022-04-20 10:54:19 status config-files gpg-agent:amd64 2.2.27-2+deb11u1",
                "2022-04-20 10:54:19 purge gpg-agent:amd64 2.2.27-2+deb11u1 <none>",
                "2022-04-20 10:54:19 startup packages purge",
                "2022-04-20 10:54:19 status installed libc-bin:amd64 2.31-13+deb11u3",
                "2022-04-20 10:54:19 status half-configured libc-bin:amd64 2.31-13+deb11u3",
                "2022-04-20 10:54:19 trigproc libc-bin:amd64 2.31-13+deb11u3 <none>",
                "2022-04-20 10:54:19 startup packages configure",
                "2022-04-20 10:54:19 status config-files readline-common:all 8.1-1"
            ]
        }
    }
] 
```




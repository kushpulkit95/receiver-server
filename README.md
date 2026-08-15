# Receiver Server

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1-brightgreen)
![Maven](https://img.shields.io/badge/Maven-Build-blue)
![Docker](https://img.shields.io/badge/Docker-Containerization-blue)
![TCP%2FUDP](https://img.shields.io/badge/Networking-TCP%2FUDP-informational)
![Status](https://img.shields.io/badge/Status-Completed-success)

A lightweight **Java and Spring Boot receiver application** developed as part of the Data Refinery Engine Simulator project.

The Receiver accepts CDR and NAT records transmitted by the Data Refinery Simulator using **TCP and UDP**, respectively. Received records are persisted into flat CSV files while runtime logs provide visibility into the number of records received.

The Receiver is designed as a **long-running server** and can continue accepting records from multiple simulator executions without requiring a restart.

---

## Features

- TCP receiver for CDR records
- UDP receiver for NAT records
- Flat-file persistence
- Separate output files for CDR and NAT records
- Runtime received-record counters
- Continuous listening for incoming records
- Spring Boot application startup
- Docker containerization
- Compatible with Docker Compose and Kubernetes deployments

---

## Tech Stack

- Java 21
- Spring Boot 4.1
- Maven
- TCP Socket Programming
- UDP Socket Programming
- Java File I/O
- Docker

---

## Project Structure

```text
src
└── main
    └── java
        └── com.pk.receiver_server
            ├── ReceiverApplication.java
            ├── TcpServer.java
            ├── UdpServer.java
            └── FlatFileWriter.java
```

### Components

| Component | Responsibility |
|---|---|
| `ReceiverApplication` | Starts the Spring Boot application and receiver server threads |
| `TcpServer` | Listens for CDR records over TCP |
| `UdpServer` | Listens for NAT records over UDP |
| `FlatFileWriter` | Writes received records to CSV files |

---

## Architecture

The Receiver operates as the receiving endpoint of the Data Refinery system.

```text
             Data Refinery Simulator
                       │
              ┌────────┴────────┐
              │                 │
          CDR / TCP         NAT / UDP
              │                 │
              ▼                 ▼
       ┌────────────┐    ┌────────────┐
       │ TcpServer  │    │ UdpServer  │
       │  Port 5000 │    │  Port 5001 │
       └──────┬─────┘    └──────┬─────┘
              │                 │
              └────────┬────────┘
                       ▼
                FlatFileWriter
                       │
              ┌────────┴────────┐
              ▼                 ▼
      received-cdr.csv   received-nat.csv
```

---

## Communication

The Receiver exposes two listening endpoints:

| Data Type | Protocol | Port |
|---|---|---:|
| CDR | TCP | `5000` |
| NAT | UDP | `5001` |

The Simulator sends:

```text
CDR ── TCP : 5000 ──► TcpServer
NAT ── UDP : 5001 ──► UdpServer
```

---

## Receiver Lifecycle

Unlike the Simulator, which performs a finite generation run, the Receiver is designed to remain active and wait for incoming records.

```text
Start Receiver
      │
      ├── Start TCP server
      │
      └── Start UDP server
              │
              ▼
       Wait for records
              │
              ▼
       Receive record
              │
              ▼
       Write to CSV
              │
              ▼
       Update counter
              │
              ▼
       Continue listening
              │
              └──────────────► ...
```

The Receiver does not need to be restarted between simulator executions.

For example:

```text
Simulator Run 1
    │
    ├── 5 CDR
    └── 5 NAT
            │
            ▼
        Receiver
            │
        CDR Total: 5
        NAT Total: 5

Simulator Run 2
    │
    ├── 5 CDR
    └── 5 NAT
            │
            ▼
        Receiver
            │
        CDR Total: 10
        NAT Total: 10
```

---

## Logging

The Receiver logs its listening state when the application starts:

```text
TCP Server listening on port 5000...
UDP server listening on port 5001...
```

When CDR records are received:

```text
TCP: CDR record received | Total: 1
TCP: CDR record received | Total: 2
TCP: CDR record received | Total: 3
```

When NAT records are received:

```text
UDP: NAT record received | Total: 1
UDP: NAT record received | Total: 2
UDP: NAT record received | Total: 3
```

The counters are cumulative for the lifetime of the Receiver process.

This provides a simple way to verify that records from multiple simulator executions are being received successfully.

---

## Flat File Output

The Receiver stores records in separate CSV files.

### CDR Output

```text
received-cdr.csv
```

Header:

```text
IMSI,MSISDN,IMEI,APN,RATType,Action,Timestamp
```

### NAT Output

```text
received-nat.csv
```

Header:

```text
Private_IP,Private_Port,Public_IP,Public_Port,Destination_IP,Destination_Port,Protocol,Timestamp
```

The Receiver creates the required output directory if it does not already exist.

Records are appended to the files rather than replacing previously received records.

---

## Running Locally

Clone the repository:

```bash
git clone https://github.com/kushpulkit95/receiver-server.git
```

Navigate to the project:

```bash
cd receiver-server
```

Build the application:

```bash
mvn clean package
```

Run the application:

```bash
mvn spring-boot:run
```

Alternatively, run the generated JAR:

```bash
java -jar target/receiver-server-0.0.1-SNAPSHOT.jar
```

Once started, the Receiver listens on:

```text
TCP : 5000
UDP : 5001
```

The Simulator can then be configured to send records to the Receiver.

---

## Docker

The Receiver is packaged as a Docker image and published to Docker Hub.

```text
pkistrying/receiver-server:latest
```

Build the image:

```bash
docker build -t pkistrying/receiver-server:latest .
```

Run the container:

```bash
docker run -p 5000:5000 -p 5001:5001 pkistrying/receiver-server:latest
```

The container exposes:

```text
5000/TCP
5001/UDP
```

---

## Docker Compose

When used with the Data Refinery Simulator through Docker Compose, the Receiver acts as the receiving service.

The Simulator communicates with the Receiver using the Docker service name rather than `localhost`.

```text
Simulator
    │
    ├── TCP ──► Receiver : 5000
    │
    └── UDP ──► Receiver : 5001
```

---

## Kubernetes

The Receiver is also deployed as a Kubernetes Deployment as part of the overall Data Refinery project.

The Kubernetes deployment uses:

- Deployment
- Service
- Namespace
- Container image from Docker Hub

The Receiver Service exposes:

```text
5000/TCP
5001/UDP
```

The Simulator communicates with the Receiver through the Kubernetes Service:

```text
refinery-receiver-service
```

The Kubernetes deployment is maintained in the separate `refinery-deployment` repository.

Kubernetes and Helm were validated locally using Minikube.

---

## Kubernetes Commands

View the Receiver Pod:

```bash
kubectl get pods -n refinery
```

View Receiver logs:

```bash
kubectl logs deployment/refinery-receiver -n refinery
```

Describe the Receiver Deployment:

```bash
kubectl describe deployment refinery-receiver -n refinery
```

Open a shell inside the Receiver Pod:

```bash
kubectl exec -it deployment/refinery-receiver -n refinery -- sh
```

Check the Receiver Service:

```bash
kubectl get service refinery-receiver-service -n refinery
```

Restart the Receiver Deployment:

```bash
kubectl rollout restart deployment/refinery-receiver -n refinery
```

---

## Testing

The Receiver was tested as part of the complete Simulator → Receiver pipeline.

Testing validates:

- TCP CDR reception
- UDP NAT reception
- Sent versus received record counts
- Flat-file output
- Multiple simulator executions
- Continuous Receiver operation
- Kubernetes Service communication
- Containerized deployment

A typical test flow is:

```text
Configure Simulator
       │
       ▼
Start Receiver
       │
       ▼
Run Simulator
       │
       ├── CDR ── TCP ──► Receiver
       │
       └── NAT ── UDP ──► Receiver
                            │
                            ▼
                         CSV files
```

The Receiver can remain running while the Simulator is executed multiple times.

---

## Example Validation

For a simulator configuration of:

```text
recordCount = 5
datatype    = cdr,nat
```

the expected result is:

```text
CDR Sent      = 5
CDR Received  = 5

NAT Sent      = 5
NAT Received  = 5
```

After running the Simulator again without restarting the Receiver:

```text
CDR Total = 10
NAT Total = 10
```

The corresponding CSV files should contain:

```text
1 header + 10 records = 11 lines
```

---

## Related Project

The Receiver is the receiving component of the **Data Refinery Engine Simulator** project.

The main Simulator application generates CDR and NAT records and transmits them to this application.

**Data Refinery Simulator:**

https://github.com/kushpulkit95/data-refinery-simulator

The deployment configuration for Docker Compose, Kubernetes, and Helm is maintained separately in the deployment repository.

---

## Project Status

The Receiver implementation and deployment workflow have been completed and validated.

Completed:

- [x] TCP receiver
- [x] UDP receiver
- [x] CDR flat-file output
- [x] NAT flat-file output
- [x] Continuous listening
- [x] Cumulative reception counters
- [x] Runtime logging
- [x] Docker containerization
- [x] Docker Compose integration
- [x] Kubernetes deployment
- [x] End-to-end validation

---

## Learning Objectives

This project provided hands-on experience with:

- TCP server development
- UDP server development
- Java Socket Programming
- Spring Boot
- Java File I/O
- Long-running server applications
- Docker containerization
- Kubernetes deployment
- Network service communication
- Runtime logging and monitoring

---

## Author

**Pulkit Kush**

B.Tech Computer Science — Data Science

Java • Spring Boot • TCP/UDP • Docker • Kubernetes

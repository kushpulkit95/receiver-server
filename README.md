# Receiver Server

![Java](https://img.shields.io/badge/Java-21-orange)

A lightweight Java application developed to receive records transmitted by the **Data Refinery Simulator**.

The receiver supports both **TCP** and **UDP** communication, writes all received records into a flat file, and maintains runtime reception statistics. It serves as a standalone testing utility for validating simulator output and network communication.

---

## Features

- TCP receiver implementation
- UDP receiver implementation
- Flat file writer for received records
- Runtime received-record counter
- Interface-based server architecture
- Configurable communication protocol
- Independent Java application (No Spring Boot)

---

## Tech Stack

- Java 21
- Spring Boot 4.1
- TCP Socket Programming
- UDP Socket Programming
- Java File I/O

---

## Project Structure

```text
src
├── ReceiverApplication.java
├── TcpServer.java
├── UdpServer.java
└── FlatFileWriter.java
```

---

## Purpose

This project acts as a lightweight receiving endpoint for the **Data Refinery Simulator**.

It is intentionally kept separate from the simulator to emulate communication between two independent applications over a network. This separation allows the simulator to be tested using different transport protocols while keeping the receiver focused solely on accepting, processing, and storing incoming records.

---

## Learning Objectives

This project was built to gain hands-on experience with:

- TCP server development
- UDP server development
- Java Socket Programming
- Flat file handling
- Long-running server applications
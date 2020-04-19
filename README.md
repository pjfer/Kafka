# Pratical Assignment II

The project is about a centralized control platform where it's necessary to supervise the status of cars.
Periodically, each car sends information, which is stored in a remote file to be processed by the centralized platform.

## Getting Started

To smoothly run this project, please make sure you have the following prerequisites and software installed.

### Prerequisites

Since the project was built to run with [Java 8](https://www.oracle.com/java/technologies/javase-jre8-downloads.html), you must have [Java 8](https://www.oracle.com/java/technologies/javase-jre8-downloads.html) installed on your computer.

### Installing

It is advised to install the [Apache Netbeans IDE](https://netbeans.apache.org/download/index.html) to run our project.

## Deployment

Inside of the folder PA2_P2G01/src/Scripts, there's 3 scripts responsible for the data construction and kafka deployment.

- plates_gen: Generates a file, inside the Data folder, with random car plate numbers. The number of generated plate number is inserted by the user.
- messages_gen: Generates a file, inside the Data folder, with messages of type status, speed and heartbeat. The number of generated messages of each type is inserted by the user.
- kafka: Deploys zookeeper and kafka, and creates the necessary topics.

To start zookeeper and kafka, inside the Scripts folder, simply run on the command line:

`./kafka -s`

And, after the start command is complete, run this command to create the necessary topics:

`./kafka -c`

To shutdown the zookeeper and kafka, run:

`./kafka -d`

After starting the zookeeper and kafka, creating the topics and opening our project (PA2_P2G01) in the IDE, firstly, the AlarmEntity.java, BatchEntity.java and ReportEntity.java should be run, and afterwards, the CollectEntity.java.

## Built With

* Java
* Bash

## Authors

* **Pedro Ferreira**
* **Rafael Teixeira**

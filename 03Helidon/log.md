```
[opc@bastion-instance-20230609 03Helidon]$ ls
helidon
[opc@bastion-instance-20230609 03Helidon]$ 
[opc@bastion-instance-20230609 03Helidon]$ 
[opc@bastion-instance-20230609 03Helidon]$ sudo mv ./helidon /usr/local/bin/
[opc@bastion-instance-20230609 03Helidon]$ helidon --version
Looking up default Helidon version
build.date               2023-08-03 16:22:57 UTC
build.version            3.0.6
build.revision           0c0d6790
latest.helidon.version   4.0.0-M2
default.helidon.version  3.2.2
```

```
[opc@bastion-instance-20230609 03Helidon]$ helidon init
Helidon versions
  (1) 3.2.2 
  (2) 2.6.3 
  (3) 4.0.0-M2 
  (4) Show all versions 
Enter selection (default: 1): 1

Helidon version: 3.2.2

| Helidon Flavor

Select a Flavor
  (1) se | Helidon SE
  (2) mp | Helidon MP
Enter selection (default: 1): 

11
| Application Type

Select an Application Type
  (1) quickstart | Quickstart
  (2) database   | Database
  (3) custom     | Custom
Enter selection (default: 1): 2
Invalid response: 112
  (1) quickstart | Quickstart
  (2) database   | Database
  (3) custom     | Custom
Enter selection (default: 1): 2

| Media Support

Select a JSON library
  (1) jsonp   | JSON-P
  (2) jackson | Jackson
  (3) jsonb   | JSON-B
Enter selection (default: 1): 

| Database

Select a Database Server
  (1) h2       | H2
  (2) mysql    | MySQL
  (3) oracledb | Oracle DB
  (4) mongodb  | MongoDB
Enter selection (default: 1): 

| Customize Project

Project groupId (default: me.opc-helidon): 
Project artifactId (default: database-se): 
Project version (default: 1.0-SNAPSHOT): 
Java package name (default: me.opc.se.database): 

Switch directory to /home/opc/FY24-cnstudy/FY24-CN-Study/03Helidon/database-se to use CLI

Start development loop? (default: n): 
```

```
[opc@bastion-instance-20230609 03Helidon]$ ls
database-se  log.md
[opc@bastion-instance-20230609 03Helidon]$ tree
.
|-- database-se
|   |-- Dockerfile
|   |-- Dockerfile.jlink
|   |-- Dockerfile.native
|   |-- README.md
|   |-- pom.xml
|   `-- src
|       |-- main
|       |   |-- java
|       |   |   `-- me
|       |   |       `-- opc
|       |   |           `-- se
|       |   |               `-- database
|       |   |                   |-- InitializeDb.java
|       |   |                   |-- Main.java
|       |   |                   |-- Pokemon.java
|       |   |                   |-- PokemonMapperProvider.java
|       |   |                   |-- PokemonService.java
|       |   |                   |-- PokemonType.java
|       |   |                   |-- PokemonTypeMapperProvider.java
|       |   |                   |-- SimpleGreetService.java
|       |   |                   `-- package-info.java
|       |   `-- resources
|       |       |-- META-INF
|       |       |   |-- native-image
|       |       |   |   |-- me.opc-helidon
|       |       |   |   |   `-- database-se
|       |       |   |   |       `-- native-image.properties
|       |       |   |   |-- native-image.properties
|       |       |   |   `-- reflect-config.json
|       |       |   `-- services
|       |       |       `-- io.helidon.dbclient.spi.DbMapperProvider
|       |       |-- Pokemons.json
|       |       |-- Types.json
|       |       |-- application.yaml
|       |       `-- logging.properties
|       `-- test
|           |-- java
|           |   `-- me
|           |       `-- opc
|           |           `-- se
|           |               `-- database
|           |                   `-- MainTest.java
|           `-- resources
|               `-- application.yaml
`-- log.md

21 directories, 25 files
```

```
[opc@bastion-instance-20230609 03Helidon]$ mvn package
[INFO] Scanning for projects...
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  0.160 s
[INFO] Finished at: 2023-09-14T06:52:37Z
[INFO] ------------------------------------------------------------------------
[ERROR] The goal you specified requires a project to execute but there is no POM in this directory (/home/opc/FY24-cnstudy/FY24-CN-Study/03Helidon). Please verify you invoked Maven from the correct directory. -> [Help 1]
[ERROR] 
[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
[ERROR] Re-run Maven using the -X switch to enable full debug logging.
[ERROR] 
[ERROR] For more information about the errors and possible solutions, please read the following articles:
[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MissingProjectException
[opc@bastion-instance-20230609 03Helidon]$ mvn --version
Apache Maven 3.9.4 (dfbb324ad4a7c8fb0bf182e6d91b0ae20e3d2dd9)
Maven home: /usr/local/bin/apache-maven-3.9.4
Java version: 20.0.2, vendor: Oracle Corporation, runtime: /usr/java/jdk-20.0.2
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "5.15.0-101.103.2.1.el8uek.x86_64", arch: "amd64", family: "unix"
```

```
$ cd database-se
$ mvn package
・・・
Downloaded from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus-io/2.7.1/plexus-io-2.7.1.jar (86 kB at 4.1 MB/s)
Downloaded from central: https://repo.maven.apache.org/maven2/org/tukaani/xz/1.5/xz-1.5.jar (100 kB at 9.1 MB/s)
Downloaded from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus-utils/3.0.24/plexus-utils-3.0.24.jar (247 kB at 14 MB/s)
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/commons/commons-compress/1.11/commons-compress-1.11.jar (426 kB at 16 MB/s)
[INFO] Building jar: /home/opc/FY24-cnstudy/FY24-CN-Study/03Helidon/database-se/target/database-se.jar
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  21.836 s
[INFO] Finished at: 2023-09-14T06:54:32Z
[INFO] ------------------------------------------------------------------------
[opc@bastion-instance-20230609 database-se]$ tree 
.
|-- Dockerfile
|-- Dockerfile.jlink
|-- Dockerfile.native
|-- README.md
|-- pom.xml
|-- src
|   |-- main
|   |   |-- java
|   |   |   `-- me
|   |   |       `-- opc
|   |   |           `-- se
|   |   |               `-- database
|   |   |                   |-- InitializeDb.java
|   |   |                   |-- Main.java
|   |   |                   |-- Pokemon.java
|   |   |                   |-- PokemonMapperProvider.java
|   |   |                   |-- PokemonService.java
|   |   |                   |-- PokemonType.java
|   |   |                   |-- PokemonTypeMapperProvider.java
|   |   |                   |-- SimpleGreetService.java
|   |   |                   `-- package-info.java
|   |   `-- resources
|   |       |-- META-INF
|   |       |   |-- native-image
|   |       |   |   |-- me.opc-helidon
|   |       |   |   |   `-- database-se
|   |       |   |   |       `-- native-image.properties
|   |       |   |   |-- native-image.properties
|   |       |   |   `-- reflect-config.json
|   |       |   `-- services
|   |       |       `-- io.helidon.dbclient.spi.DbMapperProvider
|   |       |-- Pokemons.json
|   |       |-- Types.json
|   |       |-- application.yaml
|   |       `-- logging.properties
|   `-- test
|       |-- java
|       |   `-- me
|       |       `-- opc
|       |           `-- se
|       |               `-- database
|       |                   `-- MainTest.java
|       `-- resources
|           `-- application.yaml
`-- target
    |-- classes
    |   |-- META-INF
    |   |   |-- native-image
    |   |   |   |-- me.opc-helidon
    |   |   |   |   `-- database-se
    |   |   |   |       `-- native-image.properties
    |   |   |   |-- native-image.properties
    |   |   |   `-- reflect-config.json
    |   |   `-- services
    |   |       `-- io.helidon.dbclient.spi.DbMapperProvider
    |   |-- Pokemons.json
    |   |-- Types.json
    |   |-- application.yaml
    |   |-- logging.properties
    |   `-- me
    |       `-- opc
    |           `-- se
    |               `-- database
    |                   |-- InitializeDb.class
    |                   |-- Main.class
    |                   |-- Pokemon.class
    |                   |-- PokemonMapperProvider$PokemonMapper.class
    |                   |-- PokemonMapperProvider.class
    |                   |-- PokemonService.class
    |                   |-- PokemonType.class
    |                   |-- PokemonTypeMapperProvider$PokemonTypeMapper.class
    |                   |-- PokemonTypeMapperProvider.class
    |                   `-- SimpleGreetService.class
    |-- database-se.jar
    |-- generated-sources
    |   `-- annotations
    |-- generated-test-sources
    |   `-- test-annotations
    |-- libs
    |   |-- HikariCP-5.0.1.jar
    |   |-- h2-2.1.212.jar
    |   |-- helidon-common-3.2.2.jar
    |   |-- helidon-common-configurable-3.2.2.jar
    |   |-- helidon-common-context-3.2.2.jar
    |   |-- helidon-common-http-3.2.2.jar
    |   |-- helidon-common-key-util-3.2.2.jar
    |   |-- helidon-common-mapper-3.2.2.jar
    |   |-- helidon-common-media-type-3.2.2.jar
    |   |-- helidon-common-reactive-3.2.2.jar
    |   |-- helidon-common-service-loader-3.2.2.jar
    |   |-- helidon-config-3.2.2.jar
    |   |-- helidon-config-yaml-3.2.2.jar
    |   |-- helidon-dbclient-3.2.2.jar
    |   |-- helidon-dbclient-common-3.2.2.jar
    |   |-- helidon-dbclient-health-3.2.2.jar
    |   |-- helidon-dbclient-jdbc-3.2.2.jar
    |   |-- helidon-dbclient-jsonp-3.2.2.jar
    |   |-- helidon-dbclient-metrics-3.2.2.jar
    |   |-- helidon-dbclient-metrics-jdbc-3.2.2.jar
    |   |-- helidon-fault-tolerance-3.2.2.jar
    |   |-- helidon-health-3.2.2.jar
    |   |-- helidon-health-checks-3.2.2.jar
    |   |-- helidon-health-common-3.2.2.jar
    |   |-- helidon-logging-common-3.2.2.jar
    |   |-- helidon-media-common-3.2.2.jar
    |   |-- helidon-media-jsonb-3.2.2.jar
    |   |-- helidon-media-jsonp-3.2.2.jar
    |   |-- helidon-metrics-3.2.2.jar
    |   |-- helidon-metrics-api-3.2.2.jar
    |   |-- helidon-metrics-service-api-3.2.2.jar
    |   |-- helidon-service-common-rest-3.2.2.jar
    |   |-- helidon-tracing-3.2.2.jar
    |   |-- helidon-tracing-config-3.2.2.jar
    |   |-- helidon-webserver-3.2.2.jar
    |   |-- helidon-webserver-cors-3.2.2.jar
    |   |-- jakarta.annotation-api-2.0.0.jar
    |   |-- jakarta.json-2.0.0-module.jar
    |   |-- jakarta.json-api-2.0.1.jar
    |   |-- jakarta.json.bind-api-2.0.0.jar
    |   |-- metrics-core-4.1.2.jar
    |   |-- microprofile-health-api-4.0.jar
    |   |-- microprofile-metrics-api-4.0.jar
    |   |-- netty-buffer-4.1.94.Final.jar
    |   |-- netty-codec-4.1.94.Final.jar
    |   |-- netty-codec-http-4.1.94.Final.jar
    |   |-- netty-common-4.1.94.Final.jar
    |   |-- netty-handler-4.1.94.Final.jar
    |   |-- netty-resolver-4.1.94.Final.jar
    |   |-- netty-transport-4.1.94.Final.jar
    |   |-- netty-transport-native-unix-common-4.1.94.Final.jar
    |   |-- parsson-1.0.2.jar
    |   |-- slf4j-api-1.7.32.jar
    |   |-- slf4j-jdk14-1.7.32.jar
    |   |-- snakeyaml-2.0.jar
    |   `-- yasson-2.0.4.jar
    |-- maven-archiver
    |   `-- pom.properties
    |-- maven-status
    |   `-- maven-compiler-plugin
    |       |-- compile
    |       |   `-- default-compile
    |       |       |-- createdFiles.lst
    |       |       `-- inputFiles.lst
    |       `-- testCompile
    |           `-- default-testCompile
    |               |-- createdFiles.lst
    |               `-- inputFiles.lst
    |-- surefire-reports
    |   |-- TEST-me.opc.se.database.MainTest.xml
    |   `-- me.opc.se.database.MainTest.txt
    `-- test-classes
        |-- application.yaml
        `-- me
            `-- opc
                `-- se
                    `-- database
                        `-- MainTest.class

49 directories, 108 files
```

```
[opc@bastion-instance-20230609 database-se]$ java -jar target/database-se.jar
2023.09.14 06:57:23 INFO io.helidon.common.LogConfig Thread[#1,main,5,main]: Logging at initialization configured using classpath: /logging.properties
2023.09.14 06:57:24 INFO com.zaxxer.hikari.HikariDataSource Thread[#1,main,5,main]: h2 - Starting...
2023.09.14 06:57:24 INFO com.zaxxer.hikari.HikariDataSource Thread[#1,main,5,main]: h2 - Start completed.
2023.09.14 06:57:25 INFO io.helidon.common.HelidonFeatures Thread[#33,features-thread,5,main]: Helidon SE 3.2.2 features: [Config, Db Client, Fault Tolerance, Health, Metrics, Tracing, WebServer]
2023.09.14 06:57:25 INFO io.helidon.webserver.NettyWebServer Thread[#34,nioEventLoopGroup-2-1,10,main]: Channel '@default' started: [id: 0x18c1c259, L:/[0:0:0:0:0:0:0:0]:8080]
Database here : http://localhost:8080/pokemon

```

```
[opc@bastion-instance-20230609 database-se]$ curl -X GET http://localhost:8080/simple-greet
{"message":"Hello World!"}

[opc@bastion-instance-20230609 database-se]$ curl -X GET http://localhost:8080/pokemon
[{"id":1,"idType":12,"name":"Bulbasaur"},{"id":2,"idType":10,"name":"Charmander"},{"id":3,"idType":11,"name":"Squirtle"},{"id":4,"idType":7,"name":"Caterpie"},{"id":5,"idType":7,"name":"Weedle"},{"id":6,"idType":3,"name":"Pidgey"}
```
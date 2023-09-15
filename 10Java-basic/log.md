```
[opc@bastion-instance-20230609 10Java-basic]$ mvn archetype:generate \
> -DarchetypeArtifactId=maven-archetype-quickstart \
> -DinteractiveMode=false \
> -DgroupId=com.oracle.training.data \
> -DartifactId=com.oracle.training
[INFO] Scanning for projects...
[INFO] 
[INFO] ------------------< org.apache.maven:standalone-pom >-------------------
[INFO] Building Maven Stub Project (No POM) 1
[INFO] --------------------------------[ pom ]---------------------------------
[INFO] 
[INFO] >>> archetype:3.2.1:generate (default-cli) > generate-sources @ standalone-pom >>>
[INFO] 
[INFO] <<< archetype:3.2.1:generate (default-cli) < generate-sources @ standalone-pom <<<
[INFO] 
[INFO] 
[INFO] --- archetype:3.2.1:generate (default-cli) @ standalone-pom ---
[INFO] Generating project in Batch mode
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/archetypes/maven-archetype-quickstart/1.0/maven-archetype-quickstart-1.0.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/archetypes/maven-archetype-quickstart/1.0/maven-archetype-quickstart-1.0.pom (703 B at 13 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/archetypes/maven-archetype-bundles/2/maven-archetype-bundles-2.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/archetypes/maven-archetype-bundles/2/maven-archetype-bundles-2.pom (1.5 kB at 78 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/archetype/maven-archetype-parent/1/maven-archetype-parent-1.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/archetype/maven-archetype-parent/1/maven-archetype-parent-1.pom (1.3 kB at 77 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/maven-parent/4/maven-parent-4.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/maven-parent/4/maven-parent-4.pom (10.0 kB at 400 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/archetypes/maven-archetype-quickstart/1.0/maven-archetype-quickstart-1.0.jar
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/archetypes/maven-archetype-quickstart/1.0/maven-archetype-quickstart-1.0.jar (4.3 kB at 269 kB/s)
[INFO] ----------------------------------------------------------------------------
[INFO] Using following parameters for creating project from Old (1.x) Archetype: maven-archetype-quickstart:1.0
[INFO] ----------------------------------------------------------------------------
[INFO] Parameter: basedir, Value: /home/opc/FY24-cnstudy/FY24-CN-Study/10Java-basic
[INFO] Parameter: package, Value: com.oracle.training.data
[INFO] Parameter: groupId, Value: com.oracle.training.data
[INFO] Parameter: artifactId, Value: com.oracle.training
[INFO] Parameter: packageName, Value: com.oracle.training.data
[INFO] Parameter: version, Value: 1.0-SNAPSHOT
[INFO] project created from Old (1.x) Archetype in dir: /home/opc/FY24-cnstudy/FY24-CN-Study/10Java-basic/com.oracle.training
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  3.276 s
[INFO] Finished at: 2023-09-15T06:21:40Z
[INFO] ------------------------------------------------------------------------
[opc@bastion-instance-20230609 10Java-basic]$ ls
com.oracle.training
[opc@bastion-instance-20230609 10Java-basic]$ tree com.oracle.training/
com.oracle.training/
|-- pom.xml
`-- src
    |-- main
    |   `-- java
    |       `-- com
    |           `-- oracle
    |               `-- training
    |                   `-- data
    |                       `-- App.java
    `-- test
        `-- java
            `-- com
                `-- oracle
                    `-- training
                        `-- data
                            `-- AppTest.java

13 directories, 3 files
[opc@bastion-instance-20230609 10Java-basic]$ 
[opc@bastion-instance-20230609 10Java-basic]$ 
[opc@bastion-instance-20230609 10Java-basic]$ ls
com.oracle.training
```
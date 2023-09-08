# #2-1で開発したFunctionをAPI Gatewayと連携し、実行してください

```
[opc@bastion-instance-20230609 FY24-cnstudy]$ git clone https://github.com/oracle-japan/apigw-functions-handson.git
Cloning into 'apigw-functions-handson'...
remote: Enumerating objects: 48, done.
remote: Counting objects: 100% (48/48), done.
remote: Compressing objects: 100% (40/40), done.
remote: Total 48 (delta 3), reused 38 (delta 1), pack-reused 0
Receiving objects: 100% (48/48), 1.51 MiB | 1.45 MiB/s, done.
Resolving deltas: 100% (3/3), done.
[opc@bastion-instance-20230609 FY24-cnstudy]$ cd apigw-functions-handson
[opc@bastion-instance-20230609 apigw-functions-handson]$ cd fn-generate-qrcode/
[opc@bastion-instance-20230609 fn-generate-qrcode]$ fn list a
NAME            ID
helloworld      ocid1.fnapp.oc1.ap-osaka-1.aaaaaaaatsewf2ucrr2h2k2ftam3egokoclwz3vbhj5ngmsrneyg4vwjl5dq
[opc@bastion-instance-20230609 fn-generate-qrcode]$ fn list f helloworld
NAME            IMAGE                                                           ID
helloworld      kix.ocir.io/orasejapan/kix-cnstudy-repo/helloworld:0.0.2        ocid1.fnfunc.oc1.ap-osaka-1.aaaaaaaa7d5ygxnj3lz2cft5szp5pxu4gjrhkn53j2l3aohapad2itvuwxuq
[opc@bastion-instance-20230609 fn-generate-qrcode]$ fn -v deploy --app helloworld
Deploying fn-generate-qrcode to app: helloworld
Bumped to version 0.0.2
Using Container engine docker
Building image kix.ocir.io/orasejapan/kix-cnstudy-repo/fn-generate-qrcode:0.0.2 
Dockerfile content
-----------------------------------
FROM delabassee/fn-cache:latest as cache-stage

FROM openjdk:13 as build-stage
WORKDIR /function
#RUN curl https://www.apache.org/dist/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz -o apache-maven-3.6.3-bin.tar.gz
RUN curl ftp://ftp.meisei-u.ac.jp/mirror/apache/dist/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz -o apache-maven-3.6.3-bin.tar.gz
RUN tar -zxvf apache-maven-3.6.3-bin.tar.gz

ENV PATH="/function/apache-maven-3.6.3/bin:${PATH}"

ENV MAVEN_OPTS -Dhttp.proxyHost= -Dhttp.proxyPort= -Dhttps.proxyHost= -Dhttps.proxyPort= -Dhttp.nonProxyHosts= -Dmaven.repo.local=/usr/share/maven/ref/repository
ADD pom.xml /function/pom.xml

RUN ["mvn", "package"]

ADD src /function/src

RUN ["mvn", "package", \
    "dependency:copy-dependencies", \
    "-DincludeScope=runtime", \
    "-Dmdep.prependGroupId=true", \
    "-DoutputDirectory=target" ]

RUN /usr/java/openjdk-13/bin/jlink --no-header-files --no-man-pages --strip-java-debug-attributes --output /function/fnjre --add-modules $(/usr/java/openjdk-13/bin/jdeps --print-module-deps --ignore-missing-deps -cp /function/target/*.jar /function/target/function.jar)

FROM oraclelinux:8-slim
WORKDIR /function

COPY --from=build-stage /function/target/*.jar /function/
COPY --from=build-stage /function/fnjre/ /function/fnjre/
COPY --from=cache-stage /libfnunixsocket.so /lib

ENTRYPOINT [ "/function/fnjre/bin/java", \
    "--enable-preview", \
    "-cp", "/function/*", \
    "com.fnproject.fn.runtime.EntryPoint" ]

CMD ["com.example.fn.HelloFunction::handleRequest"]
#CMD /bin/ash
-----------------------------------
FN_REGISTRY:  kix.ocir.io/orasejapan/kix-cnstudy-repo
Current Context:  kix-helloworld
[+] Building 130.3s (12/21)                                                                                                                                                                   
 => [internal] load .dockerignore                                                                                                                                                        0.0s
 => => transferring context: 2B                                                                                                                                                          0.0s
 => [internal] load build definition from Dockerfile                                                                                                                                     0.0s
 => => transferring dockerfile: 1.66kB                                                                                                                                                   0.0s
 => [internal] load metadata for docker.io/delabassee/fn-cache:latest                                                                                                                    1.3s
 => [internal] load metadata for docker.io/library/openjdk:13                                                                                                                            1.3s
 => [internal] load metadata for docker.io/library/oraclelinux:8-slim                                                                                                                    1.3s
 => [build-stage 1/9] FROM docker.io/library/openjdk:13@sha256:08ab736e5b08665c76dc8b16d8b06e42fca5a065e1203229bf39af71c2605d83                                                          0.0s
 => [internal] load build context                                                                                                                                                        0.0s
 => => transferring context: 11.85kB                                                                                                                                                     0.0s
 => CACHED [cache-stage 1/1] FROM docker.io/delabassee/fn-cache:latest@sha256:82c7e0a6e0937210c1ad29458ff332acac99666bcd678960b053643d4cc8bf15                                           0.0s
 => [stage-2 1/5] FROM docker.io/library/oraclelinux:8-slim@sha256:91b8fbafa37719b6b6cd231c113ab9b435025c94f3054dd220914822b4141b42                                                      0.0s
 => CACHED [build-stage 2/9] WORKDIR /function                                                                                                                                           0.0s
 => CACHED [stage-2 2/5] WORKDIR /function                                                                                                                                               0.0s
 => ERROR [build-stage 3/9] RUN curl ftp://ftp.meisei-u.ac.jp/mirror/apache/dist/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz -o apache-maven-3.6.3-bin.tar.gz           129.0s
------                                                                                                                                                                                        
 > [build-stage 3/9] RUN curl ftp://ftp.meisei-u.ac.jp/mirror/apache/dist/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz -o apache-maven-3.6.3-bin.tar.gz:                        
#0 0.210   % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current                                                                                                      
#0 0.211                                  Dload  Upload   Total   Spent    Left  Speed
  0     0    0     0    0     0      0      0 --:--:--  0:02:08 --:--:--     0curl: (7) Failed connect to ftp.meisei-u.ac.jp:21; Connection timed out
------
Dockerfile:6
--------------------
   4 |     WORKDIR /function
   5 |     #RUN curl https://www.apache.org/dist/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz -o apache-maven-3.6.3-bin.tar.gz
   6 | >>> RUN curl ftp://ftp.meisei-u.ac.jp/mirror/apache/dist/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz -o apache-maven-3.6.3-bin.tar.gz
   7 |     RUN tar -zxvf apache-maven-3.6.3-bin.tar.gz
   8 |     
--------------------
ERROR: failed to solve: process "/bin/sh -c curl ftp://ftp.meisei-u.ac.jp/mirror/apache/dist/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz -o apache-maven-3.6.3-bin.tar.gz" did not complete successfully: exit code: 7


Fn: error running docker build: exit status 1

See 'fn <command> --help' for more information. Client version: 0.6.26
```

## やったこと
### FTPをインストールする

```
$ sudo su -
# yum -y install ftp
This system is receiving updates from OSMS server.
Last metadata expiration check: 2:55:12 ago on Fri 08 Sep 2023 01:09:36 AM GMT.
Dependencies resolved.
==============================================================================================================================================================================================
 Package                               Architecture                             Version                                          Repository                                              Size
==============================================================================================================================================================================================
Installing:
 ftp                                   x86_64                                   0.17-78.el8                                      ol8_appstream-x86_64                                    70 k

Transaction Summary
==============================================================================================================================================================================================
Install  1 Package

Total download size: 70 k
Installed size: 112 k
Downloading Packages:
ftp-0.17-78.el8.x86_64.rpm                                                                                                                                    597 kB/s |  70 kB     00:00    
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Total                                                                                                                                                         587 kB/s |  70 kB     00:00     
Running transaction check
Transaction check succeeded.
Running transaction test
Transaction test succeeded.
Running transaction
  Preparing        :                                                                                                                                                                      1/1 
  Installing       : ftp-0.17-78.el8.x86_64                                                                                                                                               1/1 
  Running scriptlet: ftp-0.17-78.el8.x86_64                                                                                                                                               1/1 
  Verifying        : ftp-0.17-78.el8.x86_64                                                                                                                                               1/1 

Installed:
  ftp-0.17-78.el8.x86_64                                                                                                                                                                      

Complete!
```

→結果変わらず

### セキュリティリストの20、21を開ける

→結果変わらず

### DockerfileのMavenのURLを変える

https://archive.apache.org/dist/maven/maven-3/3.6.3/binaries/

```
$ fn -v deploy --app helloworld
Deploying fn-generate-qrcode to app: helloworld
Bumped to version 0.0.5
Using Container engine docker
Building image kix.ocir.io/orasejapan/kix-cnstudy-repo/fn-generate-qrcode:0.0.5 
Dockerfile content
-----------------------------------
FROM delabassee/fn-cache:latest as cache-stage

FROM openjdk:13 as build-stage
WORKDIR /function
RUN curl https://archive.apache.org/dist/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz -o apache-maven-3.6.3-bin.tar.gz
#RUN curl https://www.apache.org/dist/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz -o apache-maven-3.6.3-bin.tar.gz
#RUN curl ftp://ftp.meisei-u.ac.jp/mirror/apache/dist/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz -o apache-maven-3.6.3-bin.tar.gz
RUN tar -zxvf apache-maven-3.6.3-bin.tar.gz

ENV PATH="/function/apache-maven-3.6.3/bin:${PATH}"

ENV MAVEN_OPTS -Dhttp.proxyHost= -Dhttp.proxyPort= -Dhttps.proxyHost= -Dhttps.proxyPort= -Dhttp.nonProxyHosts= -Dmaven.repo.local=/usr/share/maven/ref/repository
ADD pom.xml /function/pom.xml

RUN ["mvn", "package"]

ADD src /function/src

RUN ["mvn", "package", \
    "dependency:copy-dependencies", \
    "-DincludeScope=runtime", \
    "-Dmdep.prependGroupId=true", \
    "-DoutputDirectory=target" ]

RUN /usr/java/openjdk-13/bin/jlink --no-header-files --no-man-pages --strip-java-debug-attributes --output /function/fnjre --add-modules $(/usr/java/openjdk-13/bin/jdeps --print-module-deps --ignore-missing-deps -cp /function/target/*.jar /function/target/function.jar)

FROM oraclelinux:8-slim
WORKDIR /function

COPY --from=build-stage /function/target/*.jar /function/
COPY --from=build-stage /function/fnjre/ /function/fnjre/
COPY --from=cache-stage /libfnunixsocket.so /lib

ENTRYPOINT [ "/function/fnjre/bin/java", \
    "--enable-preview", \
    "-cp", "/function/*", \
    "com.fnproject.fn.runtime.EntryPoint" ]

CMD ["com.example.fn.HelloFunction::handleRequest"]
#CMD /bin/ash
-----------------------------------
FN_REGISTRY:  kix.ocir.io/orasejapan/kix-cnstudy-repo
Current Context:  kix-helloworld
[+] Building 52.5s (15/21)                                                                                                                                                                    
 => [internal] load build definition from Dockerfile                                                                                                                                     0.0s
 => => transferring dockerfile: 1.79kB                                                                                                                                                   0.0s
 => [internal] load .dockerignore                                                                                                                                                        0.0s
 => => transferring context: 2B                                                                                                                                                          0.0s
 => [internal] load metadata for docker.io/delabassee/fn-cache:latest                                                                                                                    1.3s
 => [internal] load metadata for docker.io/library/openjdk:13                                                                                                                            1.3s
 => [internal] load metadata for docker.io/library/oraclelinux:8-slim                                                                                                                    1.3s
 => [build-stage 1/9] FROM docker.io/library/openjdk:13@sha256:08ab736e5b08665c76dc8b16d8b06e42fca5a065e1203229bf39af71c2605d83                                                          0.0s
 => CACHED [cache-stage 1/1] FROM docker.io/delabassee/fn-cache:latest@sha256:82c7e0a6e0937210c1ad29458ff332acac99666bcd678960b053643d4cc8bf15                                           0.0s
 => [internal] load build context                                                                                                                                                        0.0s
 => => transferring context: 913B                                                                                                                                                        0.0s
 => [stage-2 1/5] FROM docker.io/library/oraclelinux:8-slim@sha256:91b8fbafa37719b6b6cd231c113ab9b435025c94f3054dd220914822b4141b42                                                      0.0s
 => CACHED [build-stage 2/9] WORKDIR /function                                                                                                                                           0.0s
 => CACHED [stage-2 2/5] WORKDIR /function                                                                                                                                               0.0s
 => [build-stage 3/9] RUN curl https://archive.apache.org/dist/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz -o apache-maven-3.6.3-bin.tar.gz                               4.9s
 => [build-stage 4/9] RUN tar -zxvf apache-maven-3.6.3-bin.tar.gz                                                                                                                        1.2s
 => [build-stage 5/9] ADD pom.xml /function/pom.xml                                                                                                                                      0.1s 
 => ERROR [build-stage 6/9] RUN ["mvn", "package"]                                                                                                                                      44.8s 
------                                                                                                                                                                                        
 > [build-stage 6/9] RUN ["mvn", "package"]:                                                                                                                                                  
#0 3.203 [INFO] Scanning for projects...                                                                                                                                                      
#0 3.271 [WARNING]                                                                                                                                                                            
#0 3.275 [WARNING] Some problems were encountered while building the effective model for com.example.fn:hello:jar:1.0.0                                                                       
#0 3.297 [WARNING] 'dependencies.dependency.(groupId:artifactId:type:classifier)' must be unique: junit:junit:jar -> duplicate declaration of version 4.13.1 @ line 92, column 21             
#0 3.313 [WARNING] 'dependencies.dependency.(groupId:artifactId:type:classifier)' must be unique: com.fnproject.fn:testing-junit4:jar -> version ${fdk.version} vs 1.0.99 @ line 98, column 21
#0 3.331 [WARNING] 
#0 3.333 [WARNING] It is highly recommended to fix these problems because they threaten the stability of your build.
#0 3.336 [WARNING] 
#0 3.336 [WARNING] For this reason, future Maven versions might no longer support building such malformed projects.
#0 3.348 [WARNING] 
#0 3.360 [INFO] 
#0 3.363 [INFO] ------------------------< com.example.fn:hello >------------------------
#0 3.372 [INFO] Building hello 1.0.0
#0 3.372 [INFO] --------------------------------[ jar ]---------------------------------
#0 3.733 Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-resources-plugin/2.6/maven-resources-plugin-2.6.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-resources-plugin/2.6/maven-resources-plugin-2.6.pom (8.1 kB at 15 kB/s)
#0 4.303 Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-plugins/23/maven-plugins-23.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-plugins/23/maven-plugins-23.pom (9.2 kB at 128 kB/s)
#0 4.392 Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/maven-parent/22/maven-parent-22.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/maven-parent/22/maven-parent-22.pom (30 kB at 268 kB/s)
#0 4.530 Downloading from central: https://repo.maven.apache.org/maven2/org/apache/apache/11/apache-11.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/apache/11/apache-11.pom (15 kB at 290 kB/s)
#0 4.628 Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-resources-plugin/2.6/maven-resources-plugin-2.6.jar
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-resources-plugin/2.6/maven-resources-plugin-2.6.jar (30 kB at 615 kB/s)
#0 4.727 Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-compiler-plugin/3.8.0/maven-compiler-plugin-3.8.0.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-compiler-plugin/3.8.0/maven-compiler-plugin-3.8.0.pom (12 kB at 259 kB/s)
#0 4.794 Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-plugins/32/maven-plugins-32.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-plugins/32/maven-plugins-32.pom (11 kB at 194 kB/s)
#0 4.864 Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/maven-parent/32/maven-parent-32.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/maven-parent/32/maven-parent-32.pom (43 kB at 563 kB/s)
#0 4.955 Downloading from central: https://repo.maven.apache.org/maven2/org/apache/apache/20/apache-20.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/apache/20/apache-20.pom (16 kB at 256 kB/s)
#0 5.046 Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-compiler-plugin/3.8.0/maven-compiler-plugin-3.8.0.jar
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-compiler-plugin/3.8.0/maven-compiler-plugin-3.8.0.jar (62 kB at 826 kB/s)
#0 5.147 Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-surefire-plugin/2.12.4/maven-surefire-plugin-2.12.4.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-surefire-plugin/2.12.4/maven-surefire-plugin-2.12.4.pom (10 kB at 349 kB/s)
#0 5.185 Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/surefire/surefire/2.12.4/surefire-2.12.4.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/surefire/surefire/2.12.4/surefire-2.12.4.pom (14 kB at 373 kB/s)
#0 5.243 Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-surefire-plugin/2.12.4/maven-surefire-plugin-2.12.4.jar
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-surefire-plugin/2.12.4/maven-surefire-plugin-2.12.4.jar (30 kB at 708 kB/s)
#0 5.307 Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-jar-plugin/2.4/maven-jar-plugin-2.4.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-jar-plugin/2.4/maven-jar-plugin-2.4.pom (5.8 kB at 94 kB/s)
#0 5.377 Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-plugins/22/maven-plugins-22.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-plugins/22/maven-plugins-22.pom (13 kB at 334 kB/s)
#0 5.437 Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/maven-parent/21/maven-parent-21.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/maven-parent/21/maven-parent-21.pom (26 kB at 447 kB/s)
#0 5.507 Downloading from central: https://repo.maven.apache.org/maven2/org/apache/apache/10/apache-10.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/apache/10/apache-10.pom (15 kB at 592 kB/s)
#0 5.543 Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-jar-plugin/2.4/maven-jar-plugin-2.4.jar
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-jar-plugin/2.4/maven-jar-plugin-2.4.jar (34 kB at 1.4 MB/s)
#0 5.588 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/com/fnproject/fn/api/1.0.99/api-1.0.99.pom
#0 6.653 Downloading from jitpack.io: https://jitpack.io/com/fnproject/fn/api/1.0.99/api-1.0.99.pom
#0 7.545 Downloading from central: https://repo.maven.apache.org/maven2/com/fnproject/fn/api/1.0.99/api-1.0.99.pom
Downloaded from central: https://repo.maven.apache.org/maven2/com/fnproject/fn/api/1.0.99/api-1.0.99.pom (2.0 kB at 4.3 kB/s)
#0 8.032 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/com/fnproject/fn/fdk/1.0.99/fdk-1.0.99.pom
#0 8.727 Downloading from jitpack.io: https://jitpack.io/com/fnproject/fn/fdk/1.0.99/fdk-1.0.99.pom
#0 9.506 Downloading from central: https://repo.maven.apache.org/maven2/com/fnproject/fn/fdk/1.0.99/fdk-1.0.99.pom
Downloaded from central: https://repo.maven.apache.org/maven2/com/fnproject/fn/fdk/1.0.99/fdk-1.0.99.pom (13 kB at 59 kB/s)
#0 9.735 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/com/fnproject/fn/runtime/1.0.99/runtime-1.0.99.pom
#0 10.42 Downloading from jitpack.io: https://jitpack.io/com/fnproject/fn/runtime/1.0.99/runtime-1.0.99.pom
#0 10.64 Downloading from central: https://repo.maven.apache.org/maven2/com/fnproject/fn/runtime/1.0.99/runtime-1.0.99.pom
Downloaded from central: https://repo.maven.apache.org/maven2/com/fnproject/fn/runtime/1.0.99/runtime-1.0.99.pom (3.2 kB at 7.2 kB/s)
#0 11.09 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/com/fasterxml/jackson/core/jackson-databind/2.9.9/jackson-databind-2.9.9.pom
#0 11.79 Downloading from jitpack.io: https://jitpack.io/com/fasterxml/jackson/core/jackson-databind/2.9.9/jackson-databind-2.9.9.pom
#0 12.63 Downloading from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.9.9/jackson-databind-2.9.9.pom
Downloaded from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.9.9/jackson-databind-2.9.9.pom (6.4 kB at 190 kB/s)
#0 12.67 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/com/fasterxml/jackson/jackson-base/2.9.9/jackson-base-2.9.9.pom
#0 13.39 Downloading from jitpack.io: https://jitpack.io/com/fasterxml/jackson/jackson-base/2.9.9/jackson-base-2.9.9.pom
#0 13.62 Downloading from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/jackson-base/2.9.9/jackson-base-2.9.9.pom
Downloaded from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/jackson-base/2.9.9/jackson-base-2.9.9.pom (5.5 kB at 157 kB/s)
#0 13.67 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/com/fasterxml/jackson/jackson-bom/2.9.9/jackson-bom-2.9.9.pom
#0 14.34 Downloading from jitpack.io: https://jitpack.io/com/fasterxml/jackson/jackson-bom/2.9.9/jackson-bom-2.9.9.pom
#0 14.58 Downloading from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/jackson-bom/2.9.9/jackson-bom-2.9.9.pom
Downloaded from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/jackson-bom/2.9.9/jackson-bom-2.9.9.pom (12 kB at 496 kB/s)
#0 14.62 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/com/fasterxml/jackson/jackson-parent/2.9.1.2/jackson-parent-2.9.1.2.pom
#0 15.32 Downloading from jitpack.io: https://jitpack.io/com/fasterxml/jackson/jackson-parent/2.9.1.2/jackson-parent-2.9.1.2.pom
#0 15.54 Downloading from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/jackson-parent/2.9.1.2/jackson-parent-2.9.1.2.pom
Downloaded from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/jackson-parent/2.9.1.2/jackson-parent-2.9.1.2.pom (7.9 kB at 247 kB/s)
#0 15.58 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/com/fasterxml/oss-parent/34/oss-parent-34.pom
#0 16.26 Downloading from jitpack.io: https://jitpack.io/com/fasterxml/oss-parent/34/oss-parent-34.pom
#0 17.06 Downloading from central: https://repo.maven.apache.org/maven2/com/fasterxml/oss-parent/34/oss-parent-34.pom
Downloaded from central: https://repo.maven.apache.org/maven2/com/fasterxml/oss-parent/34/oss-parent-34.pom (23 kB at 757 kB/s)
#0 17.10 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/com/fasterxml/jackson/core/jackson-annotations/2.9.0/jackson-annotations-2.9.0.pom
#0 17.81 Downloading from jitpack.io: https://jitpack.io/com/fasterxml/jackson/core/jackson-annotations/2.9.0/jackson-annotations-2.9.0.pom
#0 18.61 Downloading from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.9.0/jackson-annotations-2.9.0.pom
Downloaded from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.9.0/jackson-annotations-2.9.0.pom (1.9 kB at 78 kB/s)
#0 18.64 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/com/fasterxml/jackson/jackson-parent/2.9.0/jackson-parent-2.9.0.pom
#0 19.32 Downloading from jitpack.io: https://jitpack.io/com/fasterxml/jackson/jackson-parent/2.9.0/jackson-parent-2.9.0.pom
#0 19.55 Downloading from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/jackson-parent/2.9.0/jackson-parent-2.9.0.pom
Downloaded from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/jackson-parent/2.9.0/jackson-parent-2.9.0.pom (7.8 kB at 326 kB/s)
#0 19.58 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/com/fasterxml/oss-parent/28/oss-parent-28.pom
#0 20.27 Downloading from jitpack.io: https://jitpack.io/com/fasterxml/oss-parent/28/oss-parent-28.pom
#0 20.50 Downloading from central: https://repo.maven.apache.org/maven2/com/fasterxml/oss-parent/28/oss-parent-28.pom
Downloaded from central: https://repo.maven.apache.org/maven2/com/fasterxml/oss-parent/28/oss-parent-28.pom (20 kB at 801 kB/s)
#0 20.54 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/com/fasterxml/jackson/core/jackson-core/2.9.9/jackson-core-2.9.9.pom
#0 21.22 Downloading from jitpack.io: https://jitpack.io/com/fasterxml/jackson/core/jackson-core/2.9.9/jackson-core-2.9.9.pom
#0 22.01 Downloading from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/core/jackson-core/2.9.9/jackson-core-2.9.9.pom
Downloaded from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/core/jackson-core/2.9.9/jackson-core-2.9.9.pom (4.1 kB at 156 kB/s)
#0 22.06 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/org/apache/httpcomponents/httpcore/4.4.10/httpcore-4.4.10.pom
#0 22.76 Downloading from jitpack.io: https://jitpack.io/org/apache/httpcomponents/httpcore/4.4.10/httpcore-4.4.10.pom
#0 22.99 Downloading from central: https://repo.maven.apache.org/maven2/org/apache/httpcomponents/httpcore/4.4.10/httpcore-4.4.10.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/httpcomponents/httpcore/4.4.10/httpcore-4.4.10.pom (5.4 kB at 223 kB/s)
#0 23.02 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/org/apache/httpcomponents/httpcomponents-core/4.4.10/httpcomponents-core-4.4.10.pom
#0 23.71 Downloading from jitpack.io: https://jitpack.io/org/apache/httpcomponents/httpcomponents-core/4.4.10/httpcomponents-core-4.4.10.pom
#0 24.49 Downloading from central: https://repo.maven.apache.org/maven2/org/apache/httpcomponents/httpcomponents-core/4.4.10/httpcomponents-core-4.4.10.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/httpcomponents/httpcomponents-core/4.4.10/httpcomponents-core-4.4.10.pom (13 kB at 275 kB/s)
#0 24.55 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/org/apache/httpcomponents/httpcomponents-parent/10/httpcomponents-parent-10.pom
#0 25.26 Downloading from jitpack.io: https://jitpack.io/org/apache/httpcomponents/httpcomponents-parent/10/httpcomponents-parent-10.pom
#0 25.53 Downloading from central: https://repo.maven.apache.org/maven2/org/apache/httpcomponents/httpcomponents-parent/10/httpcomponents-parent-10.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/httpcomponents/httpcomponents-parent/10/httpcomponents-parent-10.pom (34 kB at 863 kB/s)
#0 25.59 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/org/apache/apache/18/apache-18.pom
#0 26.30 Downloading from jitpack.io: https://jitpack.io/org/apache/apache/18/apache-18.pom
#0 27.11 Downloading from central: https://repo.maven.apache.org/maven2/org/apache/apache/18/apache-18.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/apache/18/apache-18.pom (16 kB at 373 kB/s)
#0 27.18 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/commons-io/commons-io/2.6/commons-io-2.6.pom
#0 27.88 Downloading from jitpack.io: https://jitpack.io/commons-io/commons-io/2.6/commons-io-2.6.pom
#0 27.93 Downloading from central: https://repo.maven.apache.org/maven2/commons-io/commons-io/2.6/commons-io-2.6.pom
Downloaded from central: https://repo.maven.apache.org/maven2/commons-io/commons-io/2.6/commons-io-2.6.pom (14 kB at 432 kB/s)
#0 27.97 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/org/apache/commons/commons-parent/42/commons-parent-42.pom
#0 28.67 Downloading from jitpack.io: https://jitpack.io/org/apache/commons/commons-parent/42/commons-parent-42.pom
#0 28.96 Downloading from central: https://repo.maven.apache.org/maven2/org/apache/commons/commons-parent/42/commons-parent-42.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/commons/commons-parent/42/commons-parent-42.pom (68 kB at 1.7 MB/s)
#0 29.03 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/net/jodah/typetools/0.5.0/typetools-0.5.0.pom
#0 29.72 Downloading from jitpack.io: https://jitpack.io/net/jodah/typetools/0.5.0/typetools-0.5.0.pom
#0 30.51 Downloading from central: https://repo.maven.apache.org/maven2/net/jodah/typetools/0.5.0/typetools-0.5.0.pom
Downloaded from central: https://repo.maven.apache.org/maven2/net/jodah/typetools/0.5.0/typetools-0.5.0.pom (6.3 kB at 218 kB/s)
#0 30.55 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/org/sonatype/oss/oss-parent/7/oss-parent-7.pom
#0 31.24 Downloading from jitpack.io: https://jitpack.io/org/sonatype/oss/oss-parent/7/oss-parent-7.pom
#0 31.25 Downloading from central: https://repo.maven.apache.org/maven2/org/sonatype/oss/oss-parent/7/oss-parent-7.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/sonatype/oss/oss-parent/7/oss-parent-7.pom (4.8 kB at 166 kB/s)
#0 31.29 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/com/github/kenglxn/qrgen/javase/2.3.0/javase-2.3.0.pom
#0 31.98 Downloading from jitpack.io: https://jitpack.io/com/github/kenglxn/qrgen/javase/2.3.0/javase-2.3.0.pom
#0 32.23 Downloading from central: https://repo.maven.apache.org/maven2/com/github/kenglxn/qrgen/javase/2.3.0/javase-2.3.0.pom
#0 32.43 [WARNING] The POM for com.github.kenglxn.qrgen:javase:jar:2.3.0 is missing, no dependency information available
#0 32.44 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/junit/junit/4.13.1/junit-4.13.1.pom
#0 33.12 Downloading from jitpack.io: https://jitpack.io/junit/junit/4.13.1/junit-4.13.1.pom
#0 33.14 Downloading from central: https://repo.maven.apache.org/maven2/junit/junit/4.13.1/junit-4.13.1.pom
Downloaded from central: https://repo.maven.apache.org/maven2/junit/junit/4.13.1/junit-4.13.1.pom (25 kB at 392 kB/s)
#0 33.21 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.pom
#0 33.90 Downloading from jitpack.io: https://jitpack.io/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.pom
#0 33.92 Downloading from central: https://repo.maven.apache.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.pom (766 B at 32 kB/s)
#0 33.95 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/org/hamcrest/hamcrest-parent/1.3/hamcrest-parent-1.3.pom
#0 34.62 Downloading from jitpack.io: https://jitpack.io/org/hamcrest/hamcrest-parent/1.3/hamcrest-parent-1.3.pom
#0 34.64 Downloading from central: https://repo.maven.apache.org/maven2/org/hamcrest/hamcrest-parent/1.3/hamcrest-parent-1.3.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/hamcrest/hamcrest-parent/1.3/hamcrest-parent-1.3.pom (2.0 kB at 62 kB/s)
#0 34.68 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/com/fnproject/fn/testing/1.0.99/testing-1.0.99.pom
#0 35.38 Downloading from jitpack.io: https://jitpack.io/com/fnproject/fn/testing/1.0.99/testing-1.0.99.pom
#0 36.19 Downloading from central: https://repo.maven.apache.org/maven2/com/fnproject/fn/testing/1.0.99/testing-1.0.99.pom
Downloaded from central: https://repo.maven.apache.org/maven2/com/fnproject/fn/testing/1.0.99/testing-1.0.99.pom (1.3 kB at 2.9 kB/s)
#0 36.65 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/com/fnproject/fn/testing-core/1.0.99/testing-core-1.0.99.pom
#0 37.34 Downloading from jitpack.io: https://jitpack.io/com/fnproject/fn/testing-core/1.0.99/testing-core-1.0.99.pom
#0 37.56 Downloading from central: https://repo.maven.apache.org/maven2/com/fnproject/fn/testing-core/1.0.99/testing-core-1.0.99.pom
Downloaded from central: https://repo.maven.apache.org/maven2/com/fnproject/fn/testing-core/1.0.99/testing-core-1.0.99.pom (630 B at 1.8 kB/s)
#0 37.92 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/com/fnproject/fn/testing-junit4/1.0.99/testing-junit4-1.0.99.pom
#0 38.61 Downloading from jitpack.io: https://jitpack.io/com/fnproject/fn/testing-junit4/1.0.99/testing-junit4-1.0.99.pom
#0 38.84 Downloading from central: https://repo.maven.apache.org/maven2/com/fnproject/fn/testing-junit4/1.0.99/testing-junit4-1.0.99.pom
Downloaded from central: https://repo.maven.apache.org/maven2/com/fnproject/fn/testing-junit4/1.0.99/testing-junit4-1.0.99.pom (1.0 kB at 2.3 kB/s)
#0 39.29 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/junit/junit/4.12/junit-4.12.pom
#0 39.98 Downloading from jitpack.io: https://jitpack.io/junit/junit/4.12/junit-4.12.pom
#0 40.00 Downloading from central: https://repo.maven.apache.org/maven2/junit/junit/4.12/junit-4.12.pom
Downloaded from central: https://repo.maven.apache.org/maven2/junit/junit/4.12/junit-4.12.pom (24 kB at 551 kB/s)
#0 40.07 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/com/fnproject/fn/api/1.0.99/api-1.0.99.jar
#0 40.08 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/com/fasterxml/jackson/core/jackson-core/2.9.9/jackson-core-2.9.9.jar
#0 40.08 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/com/fasterxml/jackson/core/jackson-annotations/2.9.0/jackson-annotations-2.9.0.jar
#0 40.08 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/com/fasterxml/jackson/core/jackson-databind/2.9.9/jackson-databind-2.9.9.jar
#0 40.08 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/com/fnproject/fn/runtime/1.0.99/runtime-1.0.99.jar
#0 40.77 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/org/apache/httpcomponents/httpcore/4.4.10/httpcore-4.4.10.jar
#0 40.78 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/commons-io/commons-io/2.6/commons-io-2.6.jar
#0 40.78 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/net/jodah/typetools/0.5.0/typetools-0.5.0.jar
#0 40.79 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/com/github/kenglxn/qrgen/javase/2.3.0/javase-2.3.0.jar
#0 40.79 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/junit/junit/4.13.1/junit-4.13.1.jar
#0 41.45 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar
#0 41.46 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/com/fnproject/fn/testing/1.0.99/testing-1.0.99.jar
#0 41.46 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/com/fnproject/fn/testing-core/1.0.99/testing-core-1.0.99.jar
#0 41.47 Downloading from fn-release-repo: https://dl.bintray.com/fnproject/fnproject/com/fnproject/fn/testing-junit4/1.0.99/testing-junit4-1.0.99.jar
#0 42.17 Downloading from jitpack.io: https://jitpack.io/com/fnproject/fn/api/1.0.99/api-1.0.99.jar
#0 42.17 Downloading from jitpack.io: https://jitpack.io/com/fnproject/fn/runtime/1.0.99/runtime-1.0.99.jar
#0 42.18 Downloading from jitpack.io: https://jitpack.io/com/fasterxml/jackson/core/jackson-databind/2.9.9/jackson-databind-2.9.9.jar
#0 42.19 Downloading from jitpack.io: https://jitpack.io/com/fasterxml/jackson/core/jackson-annotations/2.9.0/jackson-annotations-2.9.0.jar
#0 42.21 Downloading from jitpack.io: https://jitpack.io/com/fasterxml/jackson/core/jackson-core/2.9.9/jackson-core-2.9.9.jar
#0 42.41 Downloading from jitpack.io: https://jitpack.io/org/apache/httpcomponents/httpcore/4.4.10/httpcore-4.4.10.jar
#0 42.42 Downloading from jitpack.io: https://jitpack.io/commons-io/commons-io/2.6/commons-io-2.6.jar
#0 42.43 Downloading from jitpack.io: https://jitpack.io/net/jodah/typetools/0.5.0/typetools-0.5.0.jar
#0 42.44 Downloading from jitpack.io: https://jitpack.io/com/github/kenglxn/qrgen/javase/2.3.0/javase-2.3.0.jar
#0 42.46 Downloading from jitpack.io: https://jitpack.io/junit/junit/4.13.1/junit-4.13.1.jar
#0 42.47 Downloading from jitpack.io: https://jitpack.io/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar
#0 42.49 Downloading from jitpack.io: https://jitpack.io/com/fnproject/fn/testing/1.0.99/testing-1.0.99.jar
#0 42.66 Downloading from jitpack.io: https://jitpack.io/com/fnproject/fn/testing-core/1.0.99/testing-core-1.0.99.jar
#0 42.69 Downloading from jitpack.io: https://jitpack.io/com/fnproject/fn/testing-junit4/1.0.99/testing-junit4-1.0.99.jar
#0 43.45 Downloading from central: https://repo.maven.apache.org/maven2/com/fnproject/fn/api/1.0.99/api-1.0.99.jar
#0 43.46 Downloading from central: https://repo.maven.apache.org/maven2/com/fnproject/fn/runtime/1.0.99/runtime-1.0.99.jar
#0 43.46 Downloading from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.9.9/jackson-databind-2.9.9.jar
#0 43.47 Downloading from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.9.0/jackson-annotations-2.9.0.jar
#0 43.48 Downloading from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/core/jackson-core/2.9.9/jackson-core-2.9.9.jar
Downloaded from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.9.0/jackson-annotations-2.9.0.jar (67 kB at 265 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/httpcomponents/httpcore/4.4.10/httpcore-4.4.10.jar
Downloaded from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/core/jackson-core/2.9.9/jackson-core-2.9.9.jar (326 kB at 852 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/commons-io/commons-io/2.6/commons-io-2.6.jar
Downloaded from central: https://repo.maven.apache.org/maven2/com/fnproject/fn/api/1.0.99/api-1.0.99.jar (21 kB at 46 kB/s)
#0 43.91 Downloading from central: https://repo.maven.apache.org/maven2/net/jodah/typetools/0.5.0/typetools-0.5.0.jar
Downloaded from central: https://repo.maven.apache.org/maven2/net/jodah/typetools/0.5.0/typetools-0.5.0.jar (12 kB at 23 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/com/github/kenglxn/qrgen/javase/2.3.0/javase-2.3.0.jar
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/httpcomponents/httpcore/4.4.10/httpcore-4.4.10.jar (326 kB at 558 kB/s)
#0 44.06 Downloading from central: https://repo.maven.apache.org/maven2/junit/junit/4.13.1/junit-4.13.1.jar
Downloaded from central: https://repo.maven.apache.org/maven2/commons-io/commons-io/2.6/commons-io-2.6.jar (215 kB at 331 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar
Downloading from central: https://repo.maven.apache.org/maven2/com/fnproject/fn/testing/1.0.99/testing-1.0.99.jar
Downloaded from central: https://repo.maven.apache.org/maven2/com/fnproject/fn/runtime/1.0.99/runtime-1.0.99.jar (67 kB at 89 kB/s)
#0 44.20 Downloading from central: https://repo.maven.apache.org/maven2/com/fnproject/fn/testing-core/1.0.99/testing-core-1.0.99.jar
Downloaded from central: https://repo.maven.apache.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar (45 kB at 58 kB/s)
#0 44.25 Downloading from central: https://repo.maven.apache.org/maven2/com/fnproject/fn/testing-junit4/1.0.99/testing-junit4-1.0.99.jar
Downloaded from central: https://repo.maven.apache.org/maven2/junit/junit/4.13.1/junit-4.13.1.jar (383 kB at 450 kB/s)
#0 44.33 Downloaded from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.9.9/jackson-databind-2.9.9.jar (1.3 MB at 1.5 MB/s)
Downloaded from central: https://repo.maven.apache.org/maven2/com/fnproject/fn/testing/1.0.99/testing-1.0.99.jar (1.6 kB at 1.6 kB/s)
Downloaded from central: https://repo.maven.apache.org/maven2/com/fnproject/fn/testing-core/1.0.99/testing-core-1.0.99.jar (8.9 kB at 7.2 kB/s)
#0 44.73 Downloaded from central: https://repo.maven.apache.org/maven2/com/fnproject/fn/testing-junit4/1.0.99/testing-junit4-1.0.99.jar (11 kB at 8.7 kB/s)
#0 44.77 [INFO] ------------------------------------------------------------------------
#0 44.77 [INFO] BUILD FAILURE
#0 44.77 [INFO] ------------------------------------------------------------------------
#0 44.77 [INFO] Total time:  41.592 s
#0 44.77 [INFO] Finished at: 2023-09-08T04:38:02Z
#0 44.77 [INFO] ------------------------------------------------------------------------
#0 44.77 [ERROR] Failed to execute goal on project hello: Could not resolve dependencies for project com.example.fn:hello:jar:1.0.0: Could not find artifact com.github.kenglxn.qrgen:javase:jar:2.3.0 in fn-release-repo (https://dl.bintray.com/fnproject/fnproject) -> [Help 1]
#0 44.77 [ERROR] 
#0 44.77 [ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
#0 44.77 [ERROR] Re-run Maven using the -X switch to enable full debug logging.
#0 44.77 [ERROR] 
#0 44.77 [ERROR] For more information about the errors and possible solutions, please read the following articles:
#0 44.77 [ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/DependencyResolutionException
------
Dockerfile:15
--------------------
  13 |     ADD pom.xml /function/pom.xml
  14 |     
  15 | >>> RUN ["mvn", "package"]
  16 |     
  17 |     ADD src /function/src
--------------------
ERROR: failed to solve: process "mvn package" did not complete successfully: exit code: 1


Fn: error running docker build: exit status 1

See 'fn <command> --help' for more information. Client version: 0.6.26
```

→別のエラー
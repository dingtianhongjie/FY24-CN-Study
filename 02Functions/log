# 実行すると、{“message”: “Hello World”}と結果を返すFunctionを実装し、ローカルで実行してください

```
[opc@fn-instance-20230701 ~]$ fn --version
fn version 0.6.26
[opc@fn-instance-20230701 ~]$ fn list context
CURRENT NAME    PROVIDER        API URL                 REGISTRY
*       default default         http://localhost:8080
[opc@fn-instance-20230701 ~]$ fn start
2023/09/07 00:19:09 ¡¡¡ 'fn start' should NOT be used for PRODUCTION !!! see https://github.com/fnproject/fn-helm/
time="2023-09-07T00:19:10Z" level=info msg="Setting log level to" fields.level=info
time="2023-09-07T00:19:10Z" level=info msg="Registering data store provider 'sql'"
time="2023-09-07T00:19:10Z" level=info msg="Connecting to DB" url="sqlite3:///app/data/fn.db"
time="2023-09-07T00:19:10Z" level=info msg="datastore dialed" datastore=sqlite3 max_idle_connections=256 url="sqlite3:///app/data/fn.db"
time="2023-09-07T00:19:10Z" level=info msg="agent starting cfg={MinDockerVersion:17.10.0-ce ContainerLabelTag: DockerNetworks: DockerLoadFile: DisableUnprivilegedContainers:false FreezeIdle:50ms HotPoll:200ms HotLauncherTimeout:1h0m0s HotPullTimeout:10m0s HotStartTimeout:5s DetachedHeadRoom:6m0s MaxResponseSize:0 MaxHdrResponseSize:0 MaxLogSize:1048576 MaxTotalCPU:0 MaxTotalMemory:0 MaxFsSize:0 MaxPIDs:50 MaxOpenFiles:0xc42040e678 MaxLockedMemory:0xc42040e690 MaxPendingSignals:0xc42040e698 MaxMessageQueue:0xc42040e6a0 PreForkPoolSize:0 PreForkImage:busybox PreForkCmd:tail -f /dev/null PreForkUseOnce:0 PreForkNetworks: EnableNBResourceTracker:false MaxTmpFsInodes:0 DisableReadOnlyRootFs:false DisableDebugUserLogs:false IOFSEnableTmpfs:false EnableFDKDebugInfo:false IOFSAgentPath:/iofs IOFSMountRoot:/home/opc/.fn/iofs IOFSOpts: ImageCleanMaxSize:0 ImageCleanExemptTags: ImageEnableVolume:false}"
time="2023-09-07T00:19:10Z" level=info msg="no docker auths from config files found (this is fine)" error="open /root/.dockercfg: no such file or directory"
time="2023-09-07T00:19:10Z" level=info msg="available memory" cgroup_limit=9223372036854771712 head_room=1389794099 total_memory=13897940992
time="2023-09-07T00:19:10Z" level=info msg="ram reservations" avail_memory=12508146893
time="2023-09-07T00:19:10Z" level=info msg="available cpu" avail_cpu=2000 total_cpu=2000
time="2023-09-07T00:19:10Z" level=info msg="cpu reservations" cpu=2000
time="2023-09-07T00:19:10Z" level=info msg="\n        ______\n       / ____/___\n      / /_  / __ \\\n     / __/ / / / /\n    /_/   /_/ /_/\n"
time="2023-09-07T00:19:10Z" level=info msg="Fn serving on `:8080`" type=full version=0.3.750
```

```
[opc@fn-instance-20230701 ~]$ docker ps
CONTAINER ID   IMAGE                       COMMAND        CREATED          STATUS          PORTS                                                 NAMES
be934b4da34c   fnproject/fnserver:latest   "./fnserver"   45 seconds ago   Up 43 seconds   2375/tcp, 0.0.0.0:8080->8080/tcp, :::8080->8080/tcp   fnserver
[opc@fn-instance-20230701 ~]$ mkdir cn-study
[opc@fn-instance-20230701 ~]$ cd cn-study/
[opc@fn-instance-20230701 cn-study]$ fn init --runtime java hello-world
Creating function at: ./hello-world
Function boilerplate generated.
func.yaml created.
[opc@fn-instance-20230701 cn-study]$ tree hello-world/
hello-world/
|-- func.yaml
|-- pom.xml
`-- src
    |-- main
    |   `-- java
    |       `-- com
    |           `-- example
    |               `-- fn
    |                   `-- HelloFunction.java
    `-- test
        `-- java
            `-- com
                `-- example
                    `-- fn
                        `-- HelloFunctionTest.java

11 directories, 4 files
[opc@fn-instance-20230701 cn-study]$ fn create app hello-app
Successfully created app:  hello-app
[opc@fn-instance-20230701 cn-study]$ fn list a
NAME            ID
hello-app       01H9PG9QT0NG8G00GZJ0000002
myapp           01H484S9DMNG8G00GZJ0000001
[opc@fn-instance-20230701 cn-study]$ cd hello-world/
[opc@fn-instance-20230701 hello-world]$ fn deploy --app hello-app --local
Deploying hello-world to app: hello-app
Bumped to version 0.0.2
Using Container engine docker
Building image hello-world:0.0.2 .....................................................
Updating function hello-world using image hello-world:0.0.2...
Successfully created function: hello-world with hello-world:0.0.2
[opc@fn-instance-20230701 cn-study]$ cd hello-world/
[opc@fn-instance-20230701 hello-world]$ fn deploy --app hello-app --local
Deploying hello-world to app: hello-app
Bumped to version 0.0.2
Using Container engine docker
Building image hello-world:0.0.2 .....................................................
Updating function hello-world using image hello-world:0.0.2...
Successfully created function: hello-world with hello-world:0.0.2
[opc@fn-instance-20230701 hello-world]$ 
[opc@fn-instance-20230701 hello-world]$ 
[opc@fn-instance-20230701 hello-world]$ docker image ls
REPOSITORY           TAG       IMAGE ID       CREATED         SIZE
hello-world          0.0.2     42fd79c45bda   2 minutes ago   490MB
hello                0.0.2     736144c8de40   2 months ago    116MB
fnproject/fnserver   latest    49e4484aa70f   3 months ago    162MB
hello-world          latest    9c7a54a9a43c   4 months ago    13.3kB
[opc@fn-instance-20230701 hello-world]$ fn invoke hello-app hello-world
Hello, world!
$ echo -n 'John' | fn invoke hello-app hello-world
Hello, John!
```

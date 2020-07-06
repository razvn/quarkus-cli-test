# quarkus-cl project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

It a command line test to see how it works.
It allows to run a shell command or an api call.

## App usage

```
Usage: quarkus-cl [-d=<postData>] [-g=<getUrl>] [-h=<headers>] [-p=<postUrl>]
            [-s=<shellCmd>]
  -d, --data=<postData>     What post data to send ?
  -g, --get=<getUrl>        url get comman to grab
  -h, --headers=<headers>   headers to pass (format: Name1: Value1, Name2:
                              Value2)
  -p, --post=<postUrl>      url post command send
  -s, --shell=<shellCmd>    shell command to run

```

## Exemple

### Run `ls -la`
```
java -jar ./build/quarkus-cl-1.0.0-SNAPSHOT-runner.jar -s "ls -la"
```

### Run get on url
```
java -jar ./build/quarkus-cl-1.0.0-SNAPSHOT-runner.jar -g "https://jsonplaceholder.typicode.com/todos/1"
```


### Run post on url
```
java -jar build/quarkus-cl-1.0.0-SNAPSHOT-runner.jar  -p "https://jsonplaceholder.typicode.com/todos" -d "{'userId'=1, 'title'='title test', 'completed'=true}"
```

## You can also run multiple commands at once (but one of each only)
```
java -jar ./build/quarkus-cl-1.0.0-SNAPSHOT-runner.jar -g "https://jsonplaceholder.typicode.com/todos/1" -s "ls -la"
```

The result will always print in order:
 - shell result
 - get
 - post

------

## Build the application

### Java runner

#### Build
```text
./gradlew build
```

#### Run
```
java -jar ./build/quarkus-cl-1.0.0-SNAPSHOT-runner.jar <params>
```

### Natif runner

#### Build

Requires: **GraalVN installation**
 - install latest graalvm using sdkman 
    ```shell script
   sdk install java 20.1.0.r11-grl
    ```
   (don't set as default SDK as it's not what you may want to use for all other apps)
 - add the env var
    ```shell script
    export GRAALVM_HOME=~/.sdkman/candidates/java/20.1.0.r11-grl
    ```
 - install the native image builder
    ```shell script
    $GRAALVM_HOME/bin/gu install native-image
    ```
  ```text
./gradlew build -Dquarkus.package.type=native
```

then run as:
```
./build/quarkus-cl-1.0.0-SNAPSHOT-runner <params>
```

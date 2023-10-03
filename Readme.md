# UnFork Maven Plugin

Did you ever want to change something small in a dependency you were using but don't want to fork the entire project?


The UnFork Maven Plugin allows you to easily swap out single files from an existing dependency during project compilation.

### Set up UnFork

Create a new maven project and configure the `pom.xml`:

The UnFork plugin resolves the original artifact by taking the defined 
`groupId`, `artifactId` and `version`. The `version` is truncated to the `-` symbol. 

```xml
<project>
    ...
    <groupId>org.jobrunr</groupId>
    <artifactId>jobrunr</artifactId>
    <version>6.2.2-neoutil</version>
    ...
</project>

```
Add the following bit to the build section of the UnForked `pom.xml`.
```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.neo.util.tools</groupId>
            <artifactId>unfork-maven-plugin</artifactId>
            <version>1.0.0</version>
            <executions>
                <execution>
                    <goals>
                        <goal>unfork-source</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

Add the dependencies of the original artifact to the UnForked `pom.xml`.
```xml
<dependencies>
    ...
</dependencies>
```

#### Recommendation

It is recommended to use a multi module maven project in order to not have to rely on an external project, 
since this would defeat the purpose of the UnFork plugin.

Example project structure:
```
+-- root
|    +-- pom.xml
+-- src
|    +-- ...
+-- pom.xml
+-- unfork
|    +-- unfork-project-1
|    +-- unfork-project-2
```

### Use UnFork

If the project is set up correctly run the maven compile command:

```shell
$ mvn clean package
```

UnFork will extract the original files from the artifact and place them into the target folder:
```
+-- src
|    +-- ...
+-- target
|    +-- classes
|    +-- generated-resources
|         +-- src <-- Original resource files get placed here
|    +-- generated-sources
|         +-- src <-- Original java files get placed here
+-- pom.xml
```

To modify an existing file copy it from target to src and recompile the project. A file won't be added to 
generated-sources/-resources when a file with the same name and package already exists in the src directory.

### Local Project Setup

Following programs are needed:

- jdk: 11
- maven: 3.9.3

Compile

```shell
$ mvn clean package
```
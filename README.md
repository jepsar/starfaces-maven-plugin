# StarFaces Maven plugin

Maven plugin to generate the `.taglib.xml` file based on the [StarFaces annotations](../starfaces-annotations).

Please note that this is all work in progress.

## Usage

Add this dependency in order to be able to use the annotations:

````xml
<dependency>
  <groupId>com.github.jepsar</groupId>
  <artifactId>starfaces-annotations</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
````

Add this plugin to the build plugins section:

````xml
<plugin>
  <groupId>com.github.jepsar</groupId>
  <artifactId>starfaces-maven-plugin</artifactId>
  <version>1.0-SNAPSHOT</version>
  <executions>
    <execution>
      <phase>process-classes</phase>
      <goals>
        <goal>taglib-xml</goal>
      </goals>
    </execution>
  </executions>
</plugin>
````

This is the seaport server code. You need to deploy it to your own server.

## How to

#### 1. Update the configuration
Change the configuation in appliation-{env}.yml, including:
* db connection
* Qiniu access key
* Qiniu Bucket name
* Qiniu Bucket base url

#### 2. Init the db
you can find the ddl in `src/main/resources/ddl`

#### 3.  Build the jar file by:
```bash
mvn package
```

#### 4. Run the application by:
```bash
java -jar seaport-server.jar --spring.profiles.active={env}
```

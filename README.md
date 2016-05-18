This is the seaport server code. You need to deploy it to your own server.

## How to

#### 1. Update the configuration
Change the configuation in appliation-{env}.yml, including:
* db connection
* Qiniu access key

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

#### 5. Create an App
You need to first create a bucket for that app in Qiniu, then insert a record into the "App" table, specify these fields:
* secret: the app secret, it will be used by the client
* bucket: the bucket name in Qiniu
* baseUrl: the bucket's base url

# Description

Thanks to great work from [A. Sirko](https://github.com/asirko) and [D. Gombert](https://github.com/dgombert) we now have 
a stub for France Connect OpenIdConnect provider (https://franceconnect.gouv.fr)

# Prerequisite

- [Java 7](https://java.com)
- [Maven](https://maven.apache.org)

# Build

```sh
git clone https://github.com/tchabaud/france-connect-stub
cd france-connect-stub
mvn clean compile package
```

# Usage

- Set up a directory with json stubs (*$JSON_FOLDER*)
- Launch jar with following command
```
java -jar target/france-connect-stub.jar -httpPort=1234 -Ddir.stub=$(JSON_FOLDER)
```
- Set up your France Connect service as following :

```
openidconnect.client.id=1111111111111111111111111111111111111111111111111111111111111111
openidconnect.client.secret=2222222222222222222222222222222222222222222222222222222222222222
openidconnect.authorize.endpoint=http://localhost:1234/fc/authorize
openidconnect.token.endpoint=http://localhost:1234/fc/token
openidconnect.userinfo.endpoint=http://localhost:1234/fc/userinfo
openidconnect.logout.endpoint=http://localhost:1234/fc/logout
```

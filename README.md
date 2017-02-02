# Description

Thanks to great work from [A. Sirko](https://github.com/asirko), [D. Gombert](https://github.com/dgombert) and [L. Breil](https://plus.google.com/110296642077310408331) we now have 
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
java -jar target/france-connect-stub.jar -httpPort=1234 -Ddir.stub=${JSON_FOLDER}
```
- Set up your France Connect service as following :

```
openidconnect.client.id=1111111111111111111111111111111111111111111111111111111111111111
openidconnect.client.secret=2222222222222222222222222222222222222222222222222222222222222222
openidconnect.authorize.endpoint=http://localhost:1234/fc/api/v1/authorize
openidconnect.token.endpoint=http://localhost:1234/fc/api/v1/token
openidconnect.userinfo.endpoint=http://localhost:1234/fc/api/v1/userinfo
openidconnect.logout.endpoint=http://localhost:1234/fc/api/v1/logout
```

# Credits

- [A. Sirko](https://github.com/asirko) : initial development.
- [D. Gombert](https://github.com/dgombert) : token generation and signature.
- [L. Breil](https://plus.google.com/110296642077310408331) : improved GET parameters handling
- [T. Chabaud](https://github.com/tchabaud) : packaging, minor fixes.

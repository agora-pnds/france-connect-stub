# Description

Thanks to great work from [A. Sirko](https://github.com/asirko) and [D. Gombert](https://github.com/dgombert) we now have 
a stub for France Connect OpenIdConnect provider (https://franceconnect.gouv.fr)

# Prerequisite

- [Java 8](https://java.com)
- [Maven](https://maven.apache.org)
- [BouncyCastle JCA](https://www.bouncycastle.org/latest_releases.html)

# Installation

```sh
git clone https://github.com/tchabaud/france-connect-stub
mvn clean compile install
mvn jetty:run
```

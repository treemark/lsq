# LSQ - Spring restful services angular framework

A base framework for developing restful services on spring boot, angular, docker and mysql with Flyway database schema management.

You'll need:

- Node.js			
- Angular CLI		
- Eclipse			
- Docker Desktop		
- JVM 1.8 or higher


1. Download and install Docker Desktop from [https://www.docker.com/products/docker-desktop](https://www.docker.com/products/docker-desktop)
1. If running on windows, You will need to enable the 'Expose daemon on tcp://localhost:2375 without TLS' option in settings -> General
1. Download and install the git client from [http://git-scm.com/download/win](http://git-scm.com/download/win)
1. If your network utilizes a network proxy, configure it by typing `git config --global http.proxy http://<user>:<pass?@<proxyhost>:<proxyport>`
1. Download Node.js from [https://nodejs.org/en](https://nodejs.org/en)
1. Configure node package manager to use web proxy `npm config set proxy http://<user>:<pass?@<proxyhost>:<proxyport>` and `npm config set https-proxy http://<user>:<pass>@<proxyhost>:<proxyport>`
1. Install Angular with `npm install -g @angular/cli`
1. Navigate to a working directory of your choice and check out this project with `git clone https://github.com/treemark/lsq.git`
1. Run `./gradlew.bat eclipse` from root project to initialize eclipse metadata files.
1. Import existing projects into eclipse (with search sub directories on)
1. Run `./gradlew.bat fatJar` to generate all binaries and angular java script

If running inside of eclipse..
1. Locate the class `SecurityAPIApplication` in security.api and execute it as a java application to bring the database and restful end points on line at http://localhost:8081/swaggger-ui.html
1. Locate the class `SecurityUIApplication` in security.ui and execute it as a java application to bring the angular applicaiton online
1. Open a web browser to http://localhost:8080 to view the demo angular application
1. Open http://localhost:8080/api/v2/api-docs to view swagger docs
1. Open http://localhost:8080/api/swagger-ui.html for swagger debug UI

If running via docker container..


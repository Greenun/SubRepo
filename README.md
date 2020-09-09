PROsentation Backend (COPIED)
===
This is a part of [*PROsentation*](https://git.swmgit.org/swmaestro/PROsentation) Service contains **Web API Server**, **ML Model Server**.

Removed Personal properties file and docker compose file

## Installation
    
* #### Clone Git Repository
```bash
$ git clone https://git.swmgit.org/swmaestro/PROsentation-Backend.git
```
* #### Prerequisites
    * Common
        * Postgresql 
        * RabbitMQ
    * WEB Module
        * Java 11
        * Gradle >= 6.4.1
        * Spring Boot >= 2.3.1
        * Refer to [here](web/build.gradle) for other dependencies
    
    * Analysis Module
        * Python >= 3.7
        * Flask >= 2.0.x
        * Celery >= 4.4.6

## Built With
* Web
    * Spring Boot
    * Gradle
* Analysis
    * Flask
    
* Common
    * Docker / Docker-compose
    
## Usage
  * Build Web
```bash
$ cd web
$ docker-compose up -d 
```

## Templates
- [Commit message Template](https://git.swmgit.org/swmaestro/PROsentation-Frontend/-/wikis/Commit-message-Template)
![image](https://git.swmgit.org/swmaestro/PROsentation-Frontend/uploads/90fbe8b1bf28e44f94173a2b2aa41017/image.png)
- [Issue Template](https://git.swmgit.org/swmaestro/PROsentation-Frontend/-/wikis/Issue-Template)
![image](https://git.swmgit.org/swmaestro/PROsentation-Frontend/uploads/67d5a4f22c1f7655801230d8d8758d0c/image.png)
![image](https://git.swmgit.org/swmaestro/PROsentation-Frontend/uploads/335562a3931f6c93b3c99923802c2567/image.png)
- [Merge requests Template](https://git.swmgit.org/swmaestro/PROsentation-Frontend/-/wikis/Merge-requests-Template)
![image](https://git.swmgit.org/swmaestro/PROsentation-Frontend/uploads/d7f4ddf09b43ceea4ea09d74913f5422/image.png)

## Organization

[SW마에스트로](http://swmaestro.org/)

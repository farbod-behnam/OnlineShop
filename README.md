# Online Shop

An Online Shop with Spring Boot REST API and MongoDb, which uses TDD for development.

It also uses jwt from [auth0/java-jwt](https://github.com/auth0/java-jwt) 
for authorization. OnlineShop uses a filter(middleware) called: [AuthTokenFilter.java](src/main/java/com/OnlineShop/filter/AuthTokenFilter.java) to check the HTTP 
header **Authorization** in order to see if it has **Bearer** and **token** string, but for registration 
or logging, it sends the created JWT in the response body of HTTP.

Also, there is a class for handling controller exception: [ControllerExceptionHandler.java](src/main/java/com/OnlineShop/controller/ControllerExceptionHandler.java) 
and another for handling filter exception: [ExceptionHandlerFilter.java](src/main/java/com/OnlineShop/filter/ExceptionHandlerFilter.java)


The MondoDb is seeded before the start of application with [SeedDatabase.java](src/main/java/com/OnlineShop/seeders/SeedDatabase.java)
class

It has two users:

~~~
username: john
password: 1234
role: user
~~~

~~~
username: arnold
password: 1234
role: admin
~~~

### Postman
I have also included a **Postman** collection: [OnlineShop-postman](OnlineShop.postman_collection.json)
for testing the REST API.
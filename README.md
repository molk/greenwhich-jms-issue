# Spring Cloud Greenwich JMS Issue

This project demonstrates an issue with Spring's JMS support when switching
to Spring Cloud [Greenwhich.RELEASE](https://github.com/spring-cloud/spring-cloud-release/releases/tag/vGreenwich.RELEASE).

## tl;dr

Run the following commands to see the queue listening integration test pass and fail

    $ cd jms-listener-service
    $ mvn clean verify -Dspring-cloud.version=Finchley.RELEASE
    $ mvn clean verify -Dspring-cloud.version=Greenwich.RELEASE
     
Building / running against `Finchley` runs fine, whereas `Greenwhich` does not.

The issue here is, that the queue listener does not get called on sending a message to the queue.
And the issue is not restricted to this test: the running application shows the same behaviour.


## The Architecture

There are two Maven projects realising two services:

1. jms-listener-service
2. hello-service

Both services are SpringBoot 2.1.2 applications. The JMS listener sets up a JMS listener listening
for messages from an ActiveMQ queue.

Incoming messages are forwarded to the _Hello Service_ that simply returns the infamous _hello response_.

This trivial setup has been extracted from a real life project we are currently trying to upgrade from SpringCloud Finchley to Greenwich.
 
To investigate the issue in action, see the following classes:

* [jms-listener-service/src/main/java/net/marcusolk/demo/jms/MessageListener.java](./jms-listener-service/src/main/java/net/marcusolk/demo/jms/MessageListener.java)
* [jms-listener-service/src/test/groovy/net/marcusolk/demo/jms/QueueListeningIT.groovy](.//jms-listener-service/src/test/groovy/net/marcusolk/demo/jms/QueueListeningIT.groovy)

The `MessageListener` class receives the incoming message and does the forwarding to the _Hello Service_.

The `QueueListeningIT` is a SpringBootTest simulating the queue send/consume process and fails
with SpringCloud Greenwich due to the fact, that the listener has not been called
and hence the verification of the _wire mocked_ hello service call fails:

        [ERROR] Failures:
        [ERROR]   QueueListeningIT.process message - happy path:47 Condition failed with Exception:
        
        wireMock.verify(1, postRequestedFor(urlEqualTo('/')).withRequestBody(equalTo(someMessage)))
        |        |         |                |                |               |       |
        |        |         |                |                |               |       Arthur
        |        |         |                |                |               equalTo Arthur
        |        |         |                |                <com.github.tomakehurst.wiremock.matching.RequestPatternBuilder@2431050d url=path and query equalTo / method=POST headers=[:] queryParams=[:] bodyPatterns=[equalTo Arthur] cookies=[:] basicCredentials=null multiparts=[] customMatcher=null customMatcherDefinition=null>
        |        |         |                path and query equalTo /
        |        |         <com.github.tomakehurst.wiremock.matching.RequestPatternBuilder@2431050d url=path and query equalTo / method=POST headers=[:] queryParams=[:] bodyPatterns=[equalTo Arthur] cookies=[:] basicCredentials=null multiparts=[] customMatcher=null customMatcherDefinition=null>
        |        com.github.tomakehurst.wiremock.client.VerificationException: Expected at least one request matching: {
        |          "url" : "/",
        |          "method" : "POST",
        |          "bodyPatterns" : [ {
        |            "equalTo" : "Arthur"
        |          } ]
        |        }
        |        Requests received: [ ]
        <com.github.tomakehurst.wiremock.junit.WireMockClassRule@30ed2a26 wireMockApp=com.github.tomakehurst.wiremock.core.WireMockApp@141a10bf stubRequestHandler=com.github.tomakehurst.wiremock.http.StubRequestHandler@4dffa400 httpServer=com.github.tomakehurst.wiremock.jetty92.Jetty92HttpServer@5e34a84b notifier=com.github.tomakehurst.wiremock.common.Slf4jNotifier@438c0aaf options=com.github.tomakehurst.wiremock.core.WireMockConfiguration@7577589 client=com.github.tomakehurst.wiremock.client.WireMock@37b80ec7>


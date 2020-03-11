package no.fotogjengen.hilfling.authserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer

/*
* @SpringBootApplication, indicates this is a spring boot application,
*   it also declares one or more beans, and triggers auto-configure and component scanning
*   it is equal to all the following annotations combined:
*       @Configuration
*       @EnableAutoConfiguration
*       @ComponentScan
* @EnableAuthorizationServer enables this application to be used as an authorization server
* */
@SpringBootApplication
@EnableAuthorizationServer
class AuthserverApplication

fun main(args: Array<String>) {
    runApplication<AuthserverApplication>(*args) // run the application
}

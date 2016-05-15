
#Microservices based login

This is a technology demonstration for a federated login system, run by a microservice based architecture that integrates providers at runtime.

This project uses:
- Spring Boot
- Spring Cloud Netflix (Netflix - Eureka/Ribbon/Zuul)

##Intent
The user-core application polls the Eureka registrations for new providers that register and uses them the generate login page that has
'Sign In with...' components rendered by each provider. When a user selects a sign-in option, the corresponding provider performs the sign-in and redirects back to the provider.




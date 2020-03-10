### Resilience

The system takes and handles bad input as well as unauthenticated requests gracefully. 
With Spring Boot, even if a request causes an internal server error, which it should not, the service will remain running and continue to take requests.

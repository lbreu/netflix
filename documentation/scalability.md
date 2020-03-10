### Scalability

As we are dealing with an authentication service, we cannot do any of the database calls in parallel; as for instance we must know if the user has the right credentials in order to proceed in associating a device.

The main method of scalability is code optimization and the ability for this service run on multiple boxes hitting the same database.


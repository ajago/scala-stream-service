## Scala Stream service

Uses Akka HTTP and Redis

#### To Run

Start redis using docker with `docker run -p 6379 redis:4.0`

Install Scala and SBT. From root directory run `sbt run`

#### API

Get number of streams - 

`curl -X GET http://localhost:7230/user/<user id>/stream/size`

Returns JSON indicating number of streams

Start a new stream - 

`curl -X POST http://localhost:7230/user/<user id>/stream`

This returns a stream id on successful creation of a stream. Returns a 409 http error
if 3 streams already exist for the user.

Stop an existing stream -

`curl -X DELETE http://localhost:7230/user/<user id>/stream/<stream id>`

Returns 204 no content

#### Scaling strategy

Redis is a clustered system that can be scaled up vertically and horizontally to 
increase system capacity. Data is partitioned across the cluster which can grow 
over time.

The application can also be scaled out to cope with demand.

#### Things to improve

* This application uses CAS style optimistic locking in Redis to protect against 
concurrency issues. I found out too late in the day that locking is applied at a
client level, therefore I had to update the app to open a new connection to redis with
every request. This is going to severely impact performance under load.

* As the app uses optimistic locking it will fail any concurrent requests, even if they could
be accommodated. For example if a new user requests 2 streams there is a chance that one of
the requests could be rejected.

* Delete endpoint always returns NoContent response, even if no data was deleted

* Error handling for endpoints.
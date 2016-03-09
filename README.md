# Shortr - URL Shortening as a Service

Link shorteners like bit.ly and TinyURL allow users to generate short URLs that are aliases for longer ones; the user enters the long URL, 
and receives a short one in return that can then be used in its place. These short URLs are convenient for embedding in 140­character Twitter messages, 
or in email messages (especially when they might break over a line and get mangled by the email client).


## High level description

This service provides 2 simple JSON HTTP endpoints:

### Shortening
```
curl -v http://localhost:8080/shorten?url=http%3a%2f%2ftwitter.com%2fherval
```

### Expanding
```
curl -v http://localhost:8080/expand?url=http%3a%2f%2ftwitter.com%2fherval
```

Both short and long urls must be [URL encoded](https://en.wikipedia.org/wiki/Percent-encoding).

The service is built on top of [Finagle](https://github.com/twitter/finagle). 

The service is composed of a few simple pieces:

- A `Validator` that validates the input URLs
- A `Storage` layer - an abstraction for the key-value storage used to store shortened URLs
- A `IdGenerator` used to generate unique IDs (database independent)

Short URLs are made by generating a unique ID. Looking up a URL based on the shortened version is, then, just a simple lookup based on the Id. 


## Le caveats

### Added latency
Each call to the in-memory storage adds a few miliseconds, in order to simulate some level of I/O.

### Id formats
The Ids are then encoded on base 36 to produce shorter URLs with letters and numbers. Eg. the short url with id `123` becomes `shr.tr/3f`.
Note that this can lead to generating bad words - a solution for that (in case a URL such as `shr.tr/damn` is unacceptable) is to either blacklist certain words or use a custom alphabet.

### Generating UUIDs
There's two strategies included on the package: A UUID-style generator (based on timestamp), which produces unique, but rather long numbers. The simpler
generator is a storage-backed sequential number, which is faster (although sequential, which can make guessing URLs easier), but can't be easily scaled horizontally.


## instructions

### Running the server
```
sbt run
```

### Running tests
```
sbt test
```

The test suite includes a `StressTest` that will boot up the service and run a stress test on both endpoints (not a good idea on a real unit test suite, but included here for practical reasons) 








7. Bonus (if you have time): The service should provide some basic analytics about shortened
URLs usage pattern, such as how many times they have been clicked in a given timeframe.


# Shortr - URL Shortening as a Service

Link shorteners like bit.ly and TinyURL allow users to generate short URLs that are aliases for longer ones; the user enters the long URL, 
and receives a short one in return that can then be used in its place. These short URLs are convenient for embedding in 140­character Twitter messages, 
or in email messages (especially when they might break over a line and get mangled by the email client).


This service provides 2 simple JSON HTTP endpoints:

### Shortening
```
curl -v http://localhost:8080/shorten?url=http%3a%2f%2ftwitter.com%2fherval
```

### Expanding
```
curl -v http://localhost:8080/expand?url=http%3a%2f%2fshr.tr%2f123
```

### Getting click stats
```
curl -v http://localhost:8080/stats?url=http%3a%2f%2fshr.tr%2f123
```


Both short and long urls must be [URL encoded](https://en.wikipedia.org/wiki/Percent-encoding).

The service is built on top of [Finagle](https://github.com/twitter/finagle). 

The service is composed of a few simple pieces:

- A `URLStorage` - an abstraction for the service used to store and retrieve shortened URLs
- A `StatsStorage` - a service that handles stats for a given URL
- An `IdGenerator` used to generate unique IDs (database independent)
- URLs, stats and IDs all make use of a `KeyValueStorage`, representing a general-usage key value storage
- A `Validator` validates the input URLs - currently just a simple check that the URL is valid
- Each endpoint is mapped to a Finagle `Service`

Short URLs are made by generating a unique ID and storing it in conjunction with the originating long URL. 
Looking up a URL based on the shortened version is, then, just a simple lookup based on the Id. 

## Le caveats

### Added latency
Each call to the in-memory storage adds a few miliseconds, in order to simulate some level of I/O.

### Id formats
The Ids are then encoded on base 36 to produce shorter URLs with letters and numbers. Eg. the short url with id `123` becomes `shr.tr/3f`.
Note that this can lead to generating bad words - a solution for that (in case a URL such as `shr.tr/damn` is unacceptable)
is to either blacklist certain words or use a custom alphabet.

### Generating UUIDs
There's two strategies included on the package: A UUID-style generator (based on timestamp), which produces unique, but rather long numbers. The simpler
generator is a storage-backed sequential number, which is faster and yields very small URLs (although sequential, which can make guessing URLs easier). 
It also can't be easily scaled horizontally.

### Stats
The current implementation of stats only retrieves clicks as a list. This can be improved to accomodate querying stats on any
given timeframe, as well as storing more information. The stats storage is also only done on `expand`s 

### Client degradation
Stats, Id generation and URLs are implemented as "Clients" - in the event of any of those aspects being split out as a separate service, 
these could be improved to use Finagle's Client mechanism, which would improve reliability, allow for load balancing, circuit breaking, etc


## instructions

### Running the server
```
sbt run
```

### Running tests
```
sbt test
```

The test suite includes a `StressTest` that will boot up the service and run a stress test on both endpoints 
(not a good idea on a real unit test suite, but included here for practical reasons)
# cljson

A simple web tool to convert between Clojure EDN and JSON.

Hosted at [cljson.com](http://cljson.com).

## Build and Run

```bash
clojure -A:fig:build
```

```bash
open localhost:9500
```

## Deployment

### One-time

Install the AWS CLI and create a cljson profile with account keys.

[AWS CLI installation instructions](https://docs.aws.amazon.com/cli/latest/userguide/install-macos.html#install-bundle-macos).

```bash
aws --profile cljson configure
```

### Deploy

```bash
./deploy.sh
```

## License

Copyright © 2014 FIXME

Distributed under the Apache License Version 2.0.

# play-mailchimp plugin

This plugin adds [MailChimp](https://www.mailchimp.com/) support to Play! Framework 1 applications.

# How to use

####  Add the dependency to your `dependencies.yml` file

```
require:
    - mailchimp -> mailchimp 1.0.0

repositories:
    - sismics:
        type:       http
        artifact:   "http://release.sismics.com/repo/play/[module]-[revision].zip"
        contains:
            - mailchimp -> *

```
####  Run the `play deps` command

####  Add the configuration to `application.conf`

```
mailchimp.mock=false
mailchimp.apiKey=API_KEY
mailchimp.listId=LIST_ID # Get it from https://us1.api.mailchimp.com/playground/
```

# License

This software is released under the terms of the Apache License, Version 2.0. See `LICENSE` for more
information or see <https://opensource.org/licenses/Apache-2.0>.

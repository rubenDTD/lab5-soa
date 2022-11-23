# GUIDE

This is the repository that we will use for the fourth assignment of the course 2022-2023. This guide is command line oriented, but you are free to use IDE like _VS Code_, _IntelliJ IDEA_ and _Eclipse_ which have full support of the tools that we are going to use. We also assume that you have installed in your box at least [Kotlin 1.7.0](https://kotlinlang.org/docs/getting-started.html#install-kotlin).

This laboratory is not a speed competition.

## Preparation

Fork this repository.
Next you will have <https://github.com/UNIZAR-30246-WebEngineering/lab5-soa> cloned in `https://github.com/your-github-username/lab5-soa`.

By default, GitHub Actions is disabled for your forked repository.
Go to `https://github.com/your-github-username/lab5-soa/actions` and enable them.

Next, go to your repository and click in `Code` on the `main` button and create a branch named `work`.

Next, clone locally the repository:

```bash
git clone https://github.com/your-github-username/lab5-soa
cd lab5-soa
git branch -a
```

Should show `main`, `work`, `remotes/origin/main` and `remotes/origin/work`.

Then, checkout the `work` branch:

```bash
git checkout -b work
```

Make changes to the files, commit the changes to the history and push the branch up to your forked version.

```bash
git push origin work
```

## Primary task

- Extend the query interface to support the command `max:n`, where _n_ is a number.

## Apache Camel

Camel is an Open Source integration framework that empowers you to quickly and easily integrate various systems
consuming or producing data. This is a common SOA scenario. The snippet below:

* Search tweets in Twitter and returns to the web client
* Dump to disk at least one of tweets retrieved encoded in JSON in the folder `log/{year}/{month}/{day}`
* Compute metrics (available at `/actuator/metrics/per-keyword-messages?tag=keyword:${value}`, values available at `http://localhost:8080/actuator/metrics/per-keyword-messages`)

```kotlin
from(DIRECT_ROUTE)
    .toD("twitter-search:\${header.keywords}") // Twitter search, returns a list of Tweets
    .wireTap(LOG_ROUTE) // Copy the list of tweets to the LOG_ROUTE, which is processed by its own thread  
    .wireTap(COUNT_ROUTE) // Copy the list of tweets to the COUNT_ROUTE, which is processed by its own thread
    // returns a list of tweets to the DIRECT_ROUTE caller

from(LOG_ROUTE)
    .marshal().json(JsonLibrary.Gson) // Encode the list of tweets as JSON
    .to("file://log?fileName=\${date:now:yyyy/MM/dd/HH-mm-ss.SSS}.json") // Write the JSON to a file

from(COUNT_ROUTE)
    .split(body()) // Split the list in single Tweets
    .process { exchange -> // Process a single Tweet
        val keyword = exchange.getIn().getHeader("keywords") as? String
        keyword?.split(" ")?.map { perKeywordMessages.increment(it) }
    }
```

## Steps required

The objective is to extend the query interface to support the command `max:n`, where _n_ is a number.
`max` sets a limit in the number of retrieved tweets.
That is `cool max:10` must return at most 10 tweets.
There are several ways to do it.
You can use the parameter `q` or create additional parameters.
There is a restriction.
You must explicitly tell the API Twitter to enforce such a limit.
Read the documentation of the [Twitter Search component](https://camel.apache.org/components/latest/twitter-search-component.html) of Apache Camel to discover how.

You need to [sing up a **free** Twitter developer account](https://developer.twitter.com/en/apply-for-access), 
create a [project](https://developer.twitter.com/en/portal/projects-and-apps),
and then apply for [elevated privileges (also **free**)](https://developer.twitter.com/en/portal/products/elevated).

**Do not add `application.properties` with the Twitter tokens to your git!**

Note: the Twitter Search endpoint is configured using URI syntax `twitter-search:{string}[?param=value[&param=value]*]`

## How to submit

In your master branch update your corresponding row in README.md with the link to your work branch, the GitHub actions badge for CI and a link to a document that proof or explains how you solved this lab.

Do a pull request from your `main` branch to this repo main branch.
The only file modified in the pull request must be `README.md`
The pull request will be accepted if:

- Your `work` branch contains proofs that shows that you have fulfilled the primary tasks.
- In `README.md` you provides a link to your work branch in your repository, i.e.:

```md
[your-github-user-name](https://github.com/your-github-username/lab5-soa/tree/work)
```

along with your NIA and a link to your CI workflow.

```md
![Build Status](https://github.com/your-github-username/lab5-soa/actions/workflows/CI.yml/badge.svg?branch=work&event=push)](https://github.com/your-github-username/lab5-soa/actions/workflows/CI.yml)  
```

and the color of your CI workflow triggered by a push event in the branch named work is green.

# JHUgle Search Engine

## Description

JHUhgle is a simple search engine which can parse text files containing a list of websites and the keywords on each site.
The program leverages hashing, constructing an index that maps each keyword to a collection of URLs in which it appears.
Once the index is created, the user can run the search engine and query it.

## Instructions

To run the search engine, run Driver.main in the hw7 package (if using a Linux or macOS machine, open Config.java and
follow the instructions annotated with a TODO, before running the driver program.)

Queries support the following operations, which can be combined in postfix notation
* _[keyword]_: Fetches all URLs containing the specified keyword
* _&&_: Fetches all URLs that match both the first query and the second query
* _||_: Fetches all URLs that match either the first query or the second query
* _?_: Prints the URLs corresponding to the most recent query result (based on a single keyword input or an operation result)
* _!_: Quits the program

A program run might resemble the following:

    === JHUgle Search Engine Running! ===
    >>> Add your query below >>>
    > ?
    > baz
    > red
    > ?
    https://en.wikipedia.org/wiki/Cat
    http://www.foobar.com/baz
    http://www.cars.com/
    > &&
    > ?
    http://www.foobar.com/baz
    > !
    
    Process finished with exit code 0

## Data Files

Users can control what data file is fed to JHUgle through Config.DATA_FILENAME.

Several data files have been included in `src/main/resources`, ranging from short to very large.

## Hashing

Different varieties of the Hash Table data structure make up the core logic of our search engine. With operations of **O(1)**,
Hash Tables enable efficient lookups, which makes them ideal for searching over large amounts of data.

All Hash Tables in JHUgle have been implemented with only arrays, and without using Java's built-in maps. There are
implementations of a **Chaining Hash Map** and **Open Addressing Hash Map**, which provide different strategies for
Collision Resolution.

## Testing and Profiling

Tests have been written in JUnit, and can be found can be found `src/test/java/hw7`. In particular, they verify the
accuracy of the map implementations and their core implementations. There is also a benchmark test `JmhRuntimeTest.java`,
which has been designed to build JHUgle with different data files and profile its time and memory performance.

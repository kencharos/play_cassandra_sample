Play Java Cassandra sample
-----------------------------------

## abstraction

Cassandra and Kundera sample.

add QueryDsl Integration sample.

## library version

+ JDK 8
+ play 2.5.8
+ kundera 3.6
    + with cassandra-ds-driver 3.1
+ cassandra 3.9
+ QueryDSL 3.7.4

## Query DSL Integration

For generate QueryDSL QXXX Class, run `querydsl` task in sbt.

This generate QXXX class in app/models directory.

This task defines in build.sbt.


## tags

### NORMAL_JDBC_SAMPLE

This is H2 DB IO sample, includes custom thread pool async IO.
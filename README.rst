graaltest2
##########

A small test/example of building a graalvm static native image on Linux using some heavy but common libraries:

* logback
* sttp
* akka/akka streams
* jackson

To build a native image::

    $ sbt nativeImage

Or with a standard native packager::

    $ sbt stage
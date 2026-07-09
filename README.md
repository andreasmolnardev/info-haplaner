# info-haplaner

## Compile and run

This is a Java Swing project created with NetBeans/Ant. The main class is
`mvc.View`.

### With Ant

```sh
ant clean jar
java -jar dist/MVC_Lsg.jar
```

Or run directly through Ant:

```sh
ant run
```

### Without Ant

If Ant is not installed, compile with the JDK directly:

```sh
rm -rf build/classes
mkdir -p build/classes
javac -source 8 -target 8 -encoding UTF-8 -d build/classes src/mvc/*.java src/mvc/shared/*.java
jar cfm dist/MVC_Lsg.jar manifest.mf -C build/classes .
java -jar dist/MVC_Lsg.jar
```

The project was configured for Java 8 source/target compatibility. Newer JDKs may
print warnings about `-source 8` and `-target 8`, but the build should still
complete.

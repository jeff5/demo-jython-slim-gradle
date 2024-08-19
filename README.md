# Using Jython 2 in a Modular Project with Gradle

Since v2.7.4, Jython has supported The Java module system,
and since v2.7.2 (experimentally at first)
it has provided a "slim" JAR as an alternative to the
"standalone" and "classic" JARs.
This project provides a simple example using both these features.

The project creates an application that performs a simple claculation.
Use this as a starting point for your own work.

The build uses Gradle,
but it should be possible to build it without installing Gradle:
the project will fetch its own local copy.


## Why provide this?

### The "Slim" JAR

The "standalone" and "classic" JARs include
Jython dependencies in "shaded" form.
That is, the classes are added to the Jython JAR with modified names.
This results in a large JAR,
quite possibly duplicating classes already present in the client project.
It also breaks certain classes that check their own integrity
(particularly the BouncyCastle classes behind the implementation of SSL).

Modern build tools like Maven and Gradle are also dependency managers.
That is,
components declare the dependencies they have and 
the tool resolves the full set of dependencies in your project.
It then obtains them from repositories on the Internet or elsewhere.

The slim JAR does not contain its dependencies.
It only requests them from the build tool.
This means there is less duplication and
the depended-on classes run with their proper names and integrity.

### A Modular Project

Jython 2 is unlikely ever fully to embrace
the Java module system for its own use.
It can, however, play nicely within a modular project.

Packages from the slim JAR appear in the automatic module `org.python.jython2`.

*Most* of the dependency JARs also present as modules
(explicit or automatic)
and will be found and resolved by the JVM during start-up.
Some modules are not resolved because
their packages are referenced by name, dynamically.
Even though a JAR may be placed on the module path by the build,
the JVM will not read it if it is not explicitly a dependency.
To make available the full set available we use the option
`--add-modules ALL-MODULE-PATH` on the Java command.

The project shows how to take care of this in the build script. 

## How to Build the Project

Clone this repository onto your own machine.

The application is in a sub-project called `app`.
YOu can build it in the root of the project with:

```
PS demo-jython-slim-gradle> .\gradlew --console=plain  app:install
> Task :app:compileJava
> Task :app:processResources NO-SOURCE
> Task :app:classes
> Task :app:jar
> Task :app:startScripts
> Task :app:installDist

BUILD SUCCESSFUL in 11s
4 actionable tasks: 4 executed
```
(I'm using Windows PowerShell here, but the Unix equivalent is easy to work out.
Also, I force Gradle to use a plain console because I find the colours unreadable.)

The command has compiled the program,
obtained the Jython slim JAR and all its dependencies,
and created a launch script you can run with:
```
PS demo-jython-slim-gradle> .\app\build\install\app\bin\app
42
```
You can run the application direct;y with Gradle:
```
PS demo-jython-slim-gradle> .\gradlew --console=plain  app:run
...
> Task :app:run
42
```
but the idea of the `install` directory is that you can take its contents
and "install" them elsewhere to run any time you need 42 printed on the console.
You can also ask for the contents as a zip or tar file.

The install directory has two subdirectories.
`bin` as we've seen contains the launch cript.
`lib` contains the application JAR, the Jython JAR,
and everything they depend on (that the JDK doesn't supply).




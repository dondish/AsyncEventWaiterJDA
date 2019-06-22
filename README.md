# AsyncEventWaiterJDA
An asynchronous event waiter for JDA using Scala's promises.

**DO NOT BLOCK THE EVENT LISTENER,** This will make the event listener stop listening for updates and may freeze the program.

  # Installation

**Gradle**:


Repositories:
```groovy
jcenter()
```


Dependencies:
```groovy
compile 'org.dondish:asynceventwaiterjda_2.12:1.0.1'
```

**Maven**:


Repositories:
```xml
<repository>
  <id>jcenter</id>
  <url>https://jcenter.bintray.com/</url>
</repository>
```


Dependencies:
```xml
<dependency>
  <groupId>org.dondish</groupId>
  <artifactId>asynceventwaiterjda_2.12</artifactId>
  <version>1.0.1</version>
  <type>pom</type>
</dependency>
```


**sbt**:
```scala
libraryDependencies += "org.dondish" % "asynceventwaiterjda_2.12" % "1.0.1"
```


# Example
```scala
import org.dondish.AsyncEventWaiterJDA.EventWaiter
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent
import scala.util.Success


EventWaiter.waitFor(classOf[GuildMessageReceivedEvent], (event: GuildMessageReceivedEvent) => true) onComplete {
  case Success(msg) => {
      ...
  }
}
``` 

# Contributing
Make a PR request!


I will preview those once in a while and merge them if I find them as valid.

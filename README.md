# AsyncEventWaiterJDA
An asynchronous event waiter for JDA using Scala's promises.

**DO NOT BLOCK THE EVENT LISTENER,** This can make the event listener pause listening for updates and may freeze the program.

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

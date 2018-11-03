# AsyncEventWaiterJDA
An asynchronous event waiter for JDA using Scala's promises.

# Blocking Example
```scala
import concurrent.Await
import org.dondish.AsyncEventWaiterJDA.EventWaiter

Await.result(EventWaiter.waitFor(classOf[MessageReceivedEvent], ))
``` 

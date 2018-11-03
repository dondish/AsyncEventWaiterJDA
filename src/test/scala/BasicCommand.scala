import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import org.dondish.AsyncEventWaiterJDA.EventWaiter

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success

class BasicCommand(eventWaiter: EventWaiter) extends ListenerAdapter {
  override def onGuildMessageReceived(event: GuildMessageReceivedEvent): Unit = {
    val msg = event.getMessage

    if (msg.getContentRaw == "!echome") {
      event.getChannel.sendMessage("Waiting for input").queue()
      eventWaiter.waitFor(classOf[GuildMessageReceivedEvent], (x: GuildMessageReceivedEvent) => x.getAuthor == msg.getAuthor) onComplete {
        case Success(awaited) => event.getChannel.sendMessage(awaited.getMessage).queue()
      }
    }
  }
}

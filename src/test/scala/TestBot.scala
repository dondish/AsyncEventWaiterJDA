import net.dv8tion.jda.core.JDABuilder
import org.dondish.AsyncEventWaiterJDA.EventWaiter

object TestBot extends App {
  val eventWaiter = new EventWaiter
  val jda = new JDABuilder().setToken(Config.TOKEN).addEventListener(eventWaiter, new BasicCommand(eventWaiter)).build().awaitReady()
}

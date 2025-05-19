package ru.ssau.lab3.shell;

import org.jline.terminal.Terminal;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.component.flow.ComponentFlow;

import lombok.RequiredArgsConstructor;
import ru.ssau.lab3.repository.message.PubSubRepository;

@Command(command = {"message"}, group = "Сообщения")
@RequiredArgsConstructor
public class PubSubCommand 
{
    protected static final String NULL = "NULL";
    protected final Terminal terminal;
    protected final ComponentFlow.Builder componentFlowBuilder;
    protected final PubSubRepository repository;
    protected int subCounter = 1;

    @Command(command = "publish", description = "Опубликовать сообщение в канал")
    public void publish(
        @Option(longNames = "channel", shortNames = 'c', required = true) String channel,
        @Option(longNames = "message", shortNames = 'm', required = true)  String message
    )
    {
        var clients = repository.publish(channel, message);
        terminal.writer().println("Опубликовано сообщение для " + clients + " слушателей");
    }

    @Command(command = "subscribe", description = "Подписаться на канал")
    public void subscribe(
        @Option(longNames = "channel", shortNames = 'c', required = true) String channel
    )
    {
        var listener = "Слушатель #" + subCounter;
        var isSub = repository.subscribe(
            channel, 
            (c,m) -> onMessageReceive(c, m, listener)
        );
        if(isSub)
        {
            terminal.writer().println(listener + " подписался на канал " + channel);
            subCounter++;
        }
        else
        {
            terminal.writer().println("Не удалось подписаться на канал" + channel);
        }
    }

    @Command(command = "unsubscribe", description = "Отписаться от канала")
    public void unsubscribe(
        @Option(longNames = "channel", shortNames = 'c', required = true) String channel
    )
    {
        var unSubs = repository.unsubscribe(channel);
        terminal.writer().println("От канала " + channel + " отписалось " + unSubs + " слушателей");
    }

    private void onMessageReceive(String channel, String message, String receiver)
    {
        terminal.writer().println(receiver + " | " + channel +  " | " + message);
    }
}

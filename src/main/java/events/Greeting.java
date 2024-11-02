package events;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Greeting extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event){
        // Ensure the bot does not respond to itself
        if (event.getAuthor().isBot()) {
            return;
        }

        String messageReceived = event.getMessage().getContentRaw();

        // Check if the message is "hello" (case insensitive)
        if (messageReceived.equalsIgnoreCase("hello")) {
            MessageChannel channel = event.getChannel();
            channel.sendMessage("Hello, I am Kylopoly and I am gay").queue();
        }
    }

    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        String guildId = event.getGuild().getId();
        String channelId = null;

        if (guildId.equals("1277817767336218674")) {
            channelId = "1277817767847919648";
        } else if (guildId.equals("1278187638985326612")) {
            channelId = "1278187639459020873";
        }

        if (channelId != null) {
            MessageChannel channel = event.getGuild().getTextChannelById(channelId);
            if (channel != null) {
                channel.sendMessage("Welcome to the Server! We will teach you how to become a millionaire, " + event.getMember().getAsMention()).queue();
            } else {
                System.out.println("Channel not found or bot does not have access.");
            }
        }
    }


}

package events;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MusicBot extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        // Ignore bot messages and messages not from guilds
        if (event.getAuthor().isBot() || !event.isFromGuild()) return;

        // Check if the message starts with the "!play" command
        if (!event.getMessage().getContentRaw().startsWith("!play")) return;

        Guild guild = event.getGuild();

        // Retrieve the voice channels with the name "music"
        List<VoiceChannel> channels = guild.getVoiceChannelsByName("music", true);

        // Check if there are any voice channels with the name "music"
        if (channels.isEmpty()) {
            event.getChannel().sendMessage("No voice channel named 'music' was found!").queue();
            return;
        }

        // Get the first "music" channel from the list
        VoiceChannel channel = channels.get(0);
        AudioManager manager = guild.getAudioManager();

        // Check if the bot is already connected to a voice channel
        if (manager.isConnected()) {
            event.getChannel().sendMessage("I am already connected to a voice channel!").queue();
            return;
        }

        // Open the audio connection to the "music" voice channel
        manager.openAudioConnection(channel);
        event.getChannel().sendMessage("Connecting to the 'music' voice channel!").queue();
    }
}

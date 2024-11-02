package commands;

import lavalplayer.ICommand;
import lavalplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Play implements ICommand {

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getDescription() {
        return "Playing music";
    }

    // Method to define the command options for /play
    public @NotNull List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "name", "Name of the song to play").setRequired(true));
        return options;
    }

    // Execute the /play command when triggered
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if (memberVoiceState == null || !memberVoiceState.inAudioChannel()) {
            event.reply("You need to be in a voice channel to use this command").queue();
            return;
        }

        Member self = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();

        if (selfVoiceState == null || !selfVoiceState.inAudioChannel()) {
            // Bot joins the member's voice channel
            event.getGuild().getAudioManager().openAudioConnection(memberVoiceState.getChannel());
        } else if (!selfVoiceState.getChannel().equals(memberVoiceState.getChannel())) {
            // Bot is in a different voice channel
            event.reply("You need to be in the same voice channel as the bot").queue();
            return;
        }

        // Get the song name from the slash command options
        String songName = event.getOption("name").getAsString();

        // Play the song using PlayerManager
        PlayerManager playerManager = PlayerManager.get();
        event.reply("Playing").queue();
        playerManager.play(event.getGuild(), event, songName);
    }
}

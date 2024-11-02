package First.Discord.Bot;

import commands.Play;
import events.Greeting;
import events.Reaction;
import lavalplayer.CommandManager;
import lavalplayer.ICommand;
import lavalplayer.PlayerManager;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;

public class Kylopoly {

    private final ShardManager shardManager;

    public Kylopoly() throws LoginException {
        String token = System.getenv("DISCORD_BOT_TOKEN");
        System.out.println(token);
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Bot token is missing! Please set the DISCORD_BOT_TOKEN environment variable.");
        }

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);

        // Set status and activity before building
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        builder.setActivity(Activity.watching("Guys"));

        // Enable required intents (including voice)
        builder.enableIntents(
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.MESSAGE_CONTENT,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_VOICE_STATES // Required for handling voice channels
        );

        // Add event listeners
        builder.addEventListeners(new Greeting(), new Reaction());

        // Add commands to CommandManager
        CommandManager manager = new CommandManager();
        manager.add(new Play());  // Ensure Play implements ICommand correctly
        builder.addEventListeners(manager);

        // Build JDA - connect to Discord
        shardManager = builder.build();
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    public static void main(String[] args) {
        try {
            Kylopoly bot = new Kylopoly();
            System.out.println("Bot initialized successfully.");
        } catch (LoginException e) {
            System.out.println("ERROR: Invalid bot token!");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred during bot initialization!");
            e.printStackTrace();
        }
    }
}

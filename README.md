# Discomc
* [Bukkit Page](https://dev.bukkit.org/projects/discomc)
* [Spigot Page](https://www.spigotmc.org/resources/discomc.88199/)
# What is discomc?
Discomc is a bukkit plugin to make bridge between discord and minecraft server. 
The console, multi-chat, role giving, nickname changing and connect function.
This project in development, report all bags to me
# How to setup plugin?
1. You need to make application on discord dev portal ([DiscordDevPortal](https://discord.com/developers/applications))
2. Download plugin and grab it into plugins folder
3. Start the server, then the plugin will create configs file
4. Click the application you created on discord dev portal, then click on Bot on left side of website and copy a token
5. Insert this token in config.yml
6. Invite the bot on your discord server, you can do it by clicking on OAuth2 button on discord dev portal, then bot button, the administrator permission, and insert the url in your browser.
7. Copy server id ([Tutorial](https://support.discord.com/hc/en-us/articles/206346498-Where-can-I-find-my-User-Server-Message-ID-))
8. Insert it into mainServerID in config.yml
9. restart the server <br>
Congratulations! You created discomc's bot.
# config.yml
* token - bot token
* mainServerID - id of server where the bot will send all information
* sql - settings of sql
* creationCategoryID - id of category where the bot will create all channels
* multiChat - the function of retranslating chat from minecraft to discord and discord to minecraft
* multiChat.pattern - pattern of message from discord to minecraft {0} - nickname, {1} - content
* multiChat.webhookURL - url of webhook using to send minecraft messages to discord
* mutliChat.getPremiumUuids - if enabled the plugin will get premium uuid of player (for cracked servers) to receive avatars
* connect - the function which connecting minecraft and discord account
* connect.codeRemoveSeconds - when this time spended the code will deleted from list.
* nicknameChange - the function which chaning the discord name 
* nicknameChange.pattern - pattern of nickname changing {0} - discord nickname, {1} - minecraft nickname
* console - retranslate all logs to discord text channel as messages and received all message from this channel like command
* console.formatting - symbols like * will change to \*
* roles - the function to give minecraft role if discord role gave
# permissions
* discomc.* - permission for all discomc commands (default is op)
* discomc.connect - permission for connect command (default is true)
* discomc.reload - permission for discomc reload command (default is op)
# roles.json
json file with schema ROLE: LONG ID DISCORD ROLE <br>
for example: 
``` json
{
 "admin": 1
}
```
minecraft role admin equals role with id "1", when this role gives on discord server discomc will give admin role to player with connected discord id (if player not connected to discord, the role giving will be skipped)
# thanks
Thanks to lombok's developers for java code generator [Lombok](https://projectlombok.org/) <br>
Thanks to JDA's developers for discord api [JDA](https://github.com/DV8FromTheWorld/JDA) <br>
Thanks to ConfigMe's developers for settings api [ConfigMe](https://github.com/AuthMe/ConfigMe) <br>
Thanks to JavaMojangApi's developers for api [MojangApi](https://github.com/SparklingComet/java-mojang-api) <br>

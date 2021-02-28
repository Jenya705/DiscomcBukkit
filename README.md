# Discomc
[![Codemc](https://ci.codemc.io/buildStatus/icon?job=Jenya705%2FDiscomc)](https://ci.codemc.io/job/Jenya705/job/Discomc/lastBuild/)
[![Servers](https://img.shields.io/bstats/servers/10416)](https://bstats.org/plugin/bukkit/Discomc/10416)
[![Players](https://img.shields.io/bstats/players/10416)](https://bstats.org/plugin/bukkit/Discomc/10416)
* [Bukkit Page](https://dev.bukkit.org/projects/discomc)
* [Spigot Page](https://www.spigotmc.org/resources/discomc.88199/)
# What is discomc?
Discomc is a bukkit plugin to make bridge between discord and minecraft server. 
The console, multi-chat, connect and shortcut commands function.
This project in development please note us if you found a bag in our discord
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
```yaml
#########################
#                       #
# Discomc configuration #
#                       #
#########################

# if player is not online his permissions is not loaded
# so we need using for offline players vault
# it can happens when command sender is some discord user
# working only with vault plugin
# FOR online players will be used bukkit permissions
useVaultFeaturesForPermissions: true

discord:
  # token of discord bot
  botToken: token
  # bot server id
  mainServerID: 0

database:
  # implemented sqlite, postgresql and mysql
  sqlType: sqlite
  host: localhost:5432
  name: minecraft
  user: postgres
  password: admin

# function to connect discord and minecraft account
connect:
  enabled: true
  # code will be deleted in this time of ticks
  codeRemovingTime: 6000
  maxCodeValue: 10000
  # message will be deleted in this value of seconds
  discordMessageDeleteTime: 5

# function to share console in specify text channel
console:
  enabled: true

# chat between discord and minecraft
multiChat:
  enabled: true
  # discord message convert to minecraft message
  # using this pattern, where {0} - nickname
  # {1} - visible content of message
  minecraftMessagePattern: '<{0}> {1}'
  # vault features like prefixes, suffixes and etc
  # working only with vault plugin
  vaultFeatures: true

# function to make shortcuts to execute them like default bot commands
# example: !clear - discord command
# all commands needs to be set in shortcuts.json
# or by command /discomc shortcut
# or by shortcut command shortcut in discord
shortcut:
  enabled: true
  # commands prefix
  commandsPrefix: >
  # in this time message will delete
  # count in seconds
  # -1 to disable
  messageDeleteTime: 5
  # response will delete in this seconds
  # -1 to disable
  responseDeleteTime: 20
  # command will delete in this seconds
  # -1 to disable
  commandDeleteTime: -1

# IF YOU DO NOT KNOW WHAT IS THIS VALUES ARE DOING
# DO NOT CHANGE THEM!
advanced:
  # in ticks
  scheduleCommandsTimer: 20
  # in ticks
  cachedPlayersClearTimer: 6000

```
# save.json
```json
{
  "categoryChannelID": 0,
  "multiChatChannelID": 0,
  "connectChannelID": 0,
  "consoleChannelID": 0,
  "multiChatWebhookURL": ""
}
```
# permissions
- discomc.connect - /connect command
- discomc.disconnect - /disconnect command
- discomc.discomc - /discomc command
- discomc.help - /discomc help command
- discomc.forceConnect - /discomc connectForce command
- discomc.userGet - /discomc userGet command
- discomc.reload - /discomc reload command
- discomc.shortcut - /discomc shortcut command
- discomc.module - /discomc module command
- discomc.module.info - /discomc module info command
- discomc.module.reload - /discomc module reload command
# commands
- /connect - command to link minecraft account with discord account
- /disconnect - command to unlink minecraft account with discord account
- /discomc - admin command
- /discomc help %page% - write help with all admin commands
- /discomc connectForce <minecraft nickname> <discord id or mention> - command to force link accounts
- /discomc userGet - command to get value about user
- /discomc reload - command to reload modules, better if you just restart the server
- /discomc module - module commands
- /discomc module %module name% info - write info about module, enabled and description
- /discomc module %module name% reload - reload module, better if you just restart the server
# thanks
Thanks to lombok's developers for java code generator [Lombok](https://projectlombok.org/) <br>
Thanks to JDA's developers for discord api [JDA](https://github.com/DV8FromTheWorld/JDA) <br>

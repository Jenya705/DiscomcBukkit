# Discomc
[![Discord](https://img.shields.io/discord/613163671870242838.svg?color=%237289da&label=discord)(https://discord.gg/wuBkYHhZqt)] 
# What is discomc?
Discomc is a bukkit plugin to make bridge between discord and minecraft server. 
The console, multi-chat, role giving, nickname changing and connect function.
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
9. restart the server
Congratulations! You created discomc's bot.
# config.yml
token - bot token
mainServerID - id of server where the bot will send all information
sql - settings of sql
creationCategoryID - id of category where the bot will create all channels
multiChat - the function of retranslating chat from minecraft to discord and discord to minecraft
multiChat.pattern - pattern of message from discord to minecraft {0} - nickname, {1} - content
multiChat.webhookURL - url of webhook using to send minecraft messages to discord
mutliChat.getPremiumUuids - if enabled the plugin will get premium uuid of player (for cracked servers) to receive avatars
connect - the function which connecting minecraft and discord account
connect.codeRemoveSeconds - 

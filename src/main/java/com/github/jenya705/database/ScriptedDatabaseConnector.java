package com.github.jenya705.database;

import com.github.jenya705.Discomc;
import com.github.jenya705.util.FilesUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.UUID;
import java.util.logging.Level;

@Getter
@Setter(AccessLevel.PROTECTED)
public class ScriptedDatabaseConnector implements DatabaseConnector {

    private SQLScript setupNoneUniqueScript;
    private SQLScript setupDiscordIDUniqueScript;
    private SQLScript insertScript;
    private SQLScript selectScript;
    private SQLScript selectByUUIDScript;
    private SQLScript selectByDiscordIDScript;
    private SQLScript selectByRegistrationIDScript;
    private SQLScript selectByUUIDAndDiscordIDScript;

    public ScriptedDatabaseConnector(String sqlPackage) {
        setSetupNoneUniqueScript(loadScript("setup_no_unique", sqlPackage));
        setSetupDiscordIDUniqueScript(loadScript("setup_discord_id_unique", sqlPackage));
        setInsertScript(loadScript("insert", sqlPackage));
        setSelectScript(loadScript("select_all", sqlPackage));
        setSelectByUUIDScript(loadScript("select_by_uuid", sqlPackage));
        setSelectByDiscordIDScript(loadScript("select_by_discord_id", sqlPackage));
        setSelectByRegistrationIDScript(loadScript("select_by_registration_id", sqlPackage));
        setSelectByUUIDAndDiscordIDScript(loadScript("select_by_uuid_and_discord_id", sqlPackage));
    }

    @Override
    @SneakyThrows
    public void setup() {
        Discomc discomc = Discomc.getPlugin();
        DatabaseModule databaseModule = discomc.getDatabaseModule();
        if (databaseModule.getConfig().isDiscordIDUnique()) {
            getSetupDiscordIDUniqueScript().update();
        }
        else {
            getSetupNoneUniqueScript().update();
        }
    }

    @Override
    public void insert(UUID uuid, long discordID) {
        insert(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits(), discordID);
    }

    @Override
    @SneakyThrows
    public void insert(long mostUUID, long leastUUID, long discordID) {
        getInsertScript().update(mostUUID, leastUUID, discordID);
    }

    @Override
    @SneakyThrows
    public ResultSet select() {
        return getSelectScript().query();
    }

    @Override
    public ResultSet select(UUID uuid) {
        return select(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
    }

    @Override
    @SneakyThrows
    public ResultSet select(long mostUUID, long leastUUID) {
        return getSelectByUUIDScript().query(mostUUID, leastUUID);
    }

    @Override
    @SneakyThrows
    public ResultSet select(long discordID) {
        return getSelectByDiscordIDScript().query(discordID);
    }

    @Override
    public ResultSet select(UUID uuid, long discordID) {
        return select(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits(), discordID);
    }

    @Override
    @SneakyThrows
    public ResultSet select(long mostUUID, long leastUUID, long discordID) {
        return getSelectByUUIDAndDiscordIDScript().query(mostUUID, leastUUID, discordID);
    }

    @Override
    @SneakyThrows
    public ResultSet selectRegistrationID(long registrationID) {
        return getSelectByRegistrationIDScript().query(registrationID);
    }

    protected SQLScript loadScript(String name, String sqlPackage) {
        Discomc discomc = Discomc.getPlugin();
        try {
            String fileName = name + ".sql";
            File fixedFile = FilesUtil.getRecursivelyFile("sql", sqlPackage, fileName);
            SQLScript script = null;
            if (fixedFile.exists()) {
                script = new ContentSQLScript(fixedFile);
            }
            else {
                File defaultFile = FilesUtil.getRecursivelyFile("sql", fileName);
                if (defaultFile.exists()) {
                    script = new ContentSQLScript(defaultFile);
                }
                else {
                    discomc.getLogger().warning(String.format("Failed loading %s:%s script because it is not exist", name, sqlPackage));
                }
            }
            return script;
        } catch (IOException e) {
            discomc.getLogger().log(Level.WARNING, String.format(
                    "Failed loading %s:%s script", name, sqlPackage), e);
        }
        return null;
    }

}

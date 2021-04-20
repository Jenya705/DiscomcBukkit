package com.github.jenya705.util;

import com.github.jenya705.Discomc;
import com.github.jenya705.DiscomcDatabaseUser;
import com.github.jenya705.database.DatabaseModule;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@UtilityClass
public class UsersUtil {

    @SneakyThrows
    public static DiscomcDatabaseUser getUser(UUID uuid, long discordID) {
        Discomc discomc = Discomc.getPlugin();
        DatabaseModule databaseModule = discomc.getDatabaseModule();
        ResultSet sqlResultSet = databaseModule.getConnector().select(uuid, discordID);
        DiscomcDatabaseUser user = null;
        while (sqlResultSet.next()) {
            user = new DiscomcDatabaseUser(uuid, discordID,
                    sqlResultSet.getLong("registration_id"));
        }
        return user;
    }

    @SneakyThrows
    public static DiscomcDatabaseUser getUser(long mostUUID, long leastUUID, long discordID) {
        Discomc discomc = Discomc.getPlugin();
        DatabaseModule databaseModule = discomc.getDatabaseModule();
        ResultSet sqlResultSet = databaseModule.getConnector().select(mostUUID, leastUUID, discordID);
        DiscomcDatabaseUser user = null;
        UUID uuid = new UUID(mostUUID, leastUUID);
        while (sqlResultSet.next()) {
            user = new DiscomcDatabaseUser(uuid, discordID,
                    sqlResultSet.getLong("registration_id"));
        }
        return user;
    }

    @SneakyThrows
    public static DiscomcDatabaseUser getUser(UUID uuid) {
        Discomc discomc = Discomc.getPlugin();
        DatabaseModule databaseModule = discomc.getDatabaseModule();
        ResultSet sqlResultSet = databaseModule.getConnector().select(uuid);
        DiscomcDatabaseUser user = null;
        while (sqlResultSet.next()) {
            user = new DiscomcDatabaseUser(uuid,
                    sqlResultSet.getLong("discord_id"),
                    sqlResultSet.getLong("registration_id"));
        }
        return user;
    }

    @SneakyThrows
    public static DiscomcDatabaseUser getUser(long mostUUID, long leastUUID) {
        Discomc discomc = Discomc.getPlugin();
        DatabaseModule databaseModule = discomc.getDatabaseModule();
        ResultSet sqlResultSet = databaseModule.getConnector().select(mostUUID, leastUUID);
        UUID uuid = new UUID(mostUUID, leastUUID);
        DiscomcDatabaseUser user = null;
        while (sqlResultSet.next()) {
            user = new DiscomcDatabaseUser(uuid,
                    sqlResultSet.getLong("discord_id"),
                    sqlResultSet.getLong("registration_id"));
        }
        return user;
    }

    @SneakyThrows
    public static Set<DiscomcDatabaseUser> getUsers(long discordID) {
        Discomc discomc = Discomc.getPlugin();
        DatabaseModule databaseModule = discomc.getDatabaseModule();
        ResultSet sqlResultSet = databaseModule.getConnector().select(discordID);
        Set<DiscomcDatabaseUser> result = new HashSet<>();
        while (sqlResultSet.next()) {
            result.add(new DiscomcDatabaseUser(
                    new UUID(sqlResultSet.getLong("most_uuid"),
                            sqlResultSet.getLong("least_uuid")),
                    discordID,
                    sqlResultSet.getLong("registration_id")));
        }
        return result;
    }

    @SneakyThrows
    public static DiscomcDatabaseUser getUser(long registrationID) {
        Discomc discomc = Discomc.getPlugin();
        DatabaseModule databaseModule = discomc.getDatabaseModule();
        ResultSet sqlResultSet = databaseModule.getConnector().selectRegistrationID(registrationID);
        if (sqlResultSet.next()) {
            return new DiscomcDatabaseUser(
                    new UUID(sqlResultSet.getLong("most_uuid"),
                            sqlResultSet.getLong("least_uuid")),
                    sqlResultSet.getLong("discord_id"),
                    registrationID);
        }
        return null;
    }

}

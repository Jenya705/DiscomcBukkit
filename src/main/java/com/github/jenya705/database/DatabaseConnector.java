package com.github.jenya705.database;

import java.sql.ResultSet;
import java.util.UUID;

public interface DatabaseConnector {

    void setup();

    void insert(UUID uuid, long discordID);

    void insert(long mostUUID, long leastUUID, long discordID);

    ResultSet select();

    ResultSet select(UUID uuid);

    ResultSet select(long mostUUID, long leastUUID);

    ResultSet select(long discordID);

    ResultSet select(UUID uuid, long discordID);

    ResultSet select(long mostUUID, long leastUUID, long discordID);

    ResultSet selectRegistrationID(long registrationID);

}

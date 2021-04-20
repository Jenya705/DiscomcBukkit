package com.github.jenya705;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class DiscomcDatabaseUser {

    private UUID uuid;
    private long discordID;
    private long registrationID;

}

CREATE TABLE IF NOT EXISTS dmc_players (
    uuidMost INTEGER,
    uuidLeast INTEGER,
    discordID INTEGER,
    PRIMARY KEY(uuidMost, uuidLeast)
);
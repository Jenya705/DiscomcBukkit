CREATE TABLE IF NOT EXISTS dmc_players (
	uuidMost bigint NOT NULL,
	uuidLeast bigint NOT NULL,
	discordID bigint NOT NULL
	CONSTRAINT PK_dmc_player PRIMARY KEY (uuidMost, uuidLeast);
);

DROP PROCEDURE IF EXISTS player_insert_with_check(BIGINT, BIGINT, BIGINT);

CREATE PROCEDURE player_insert_with_check(IN _uuidMost BIGINT, IN _uuidLeast BIGINT, IN _discordID BIGINT)
SQL SECURITY INVOKER
BEGIN
    INSERT INTO dmc_players (uuidLeast, uuidMost, discordID)
    VALUES (_uuidLeast, _uuidMost, _discordID) ON DUPLICATE
    KEY UPDATE discordID = _discordID;
END;
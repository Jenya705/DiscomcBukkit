CREATE TABLE IF NOT EXISTS dmc_players (
	uuidMost bigint NOT NULL,
	uuidLeast bigint NOT NULL,
	discordID bigint NOT NULL,
	UNIQUE(uuidMost, uuidLeast)
);

DROP FUNCTION IF EXISTS player_insert_with_check(BIGINT, BIGINT, BIGINT);

CREATE OR REPLACE FUNCTION player_insert_with_check(_uuidMost BIGINT, _uuidLeast BIGINT, _discordID BIGINT) RETURNS VOID
	LANGUAGE plpgsql AS
	$$
	DECLARE updated integer;
	BEGIN
		UPDATE dmc_players SET discordID = _discordID WHERE uuidMost = _uuidMost AND uuidLeast = _uuidLeast;
		GET DIAGNOSTICS updated = ROW_COUNT;
		IF updated = 0 THEN
			INSERT INTO dmc_players(uuidMost, uuidLeast, discordId) VALUES (0, 0, 0);
		END IF;
	END
	$$
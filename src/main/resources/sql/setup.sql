CREATE TABLE IF NOT EXISTS dmc_players (
	uuidMost bigint NOT NULL, -- primary key
	uuidLeast bigint NOT NULL, -- primary key
	discordID bigint NOT NULL
);

-- unique function
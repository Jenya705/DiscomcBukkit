INSERT INTO dmc_players(uuidMost, uuidLeast) VALUES (?, ?)
ON CONFLICT(uuidMost, uuidLeast) DO UPDATE SET discordID = ?;
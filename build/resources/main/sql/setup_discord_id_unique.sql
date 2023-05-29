create table if not exists dmc_players (
	most_uuid bigint,
	least_uuid bigint,
	discord_id bigint,
	registration_id bigint auto_increment,
	primary key(registration_id),
	unique(most_uuid, least_uuid),
	unique(discord_id)
);
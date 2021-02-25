DO $$
BEGIN
    SELECT player_insert_with_check(?, ?, ?);
END
$$;
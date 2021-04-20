package com.github.jenya705.database;

import com.github.jenya705.Discomc;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter(AccessLevel.PROTECTED)
public class ContentSQLScript implements SQLScript {

    private String sql;

    public ContentSQLScript(String sql) {
        setSql(sql);
    }

    public ContentSQLScript(File file) throws IOException {
        this(new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8));
    }

    @Override
    public ResultSet query(Object... objects) throws SQLException {
        return Discomc.getPlugin().getDatabaseModule().query(getSql(), objects);
    }

    @Override
    public void update(Object... objects) throws SQLException {
        Discomc.getPlugin().getDatabaseModule().update(getSql(), objects);
    }
}

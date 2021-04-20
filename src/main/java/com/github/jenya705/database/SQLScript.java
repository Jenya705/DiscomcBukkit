package com.github.jenya705.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SQLScript {

    ResultSet query(Object... objects) throws SQLException;

    void update(Object... objects) throws SQLException;

}

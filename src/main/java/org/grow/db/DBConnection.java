package org.grow.db;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import static org.grow.EnvConfig.ENV_CONFIG;

public interface DBConnection {

    static Jdbi createConnection() {
        return Jdbi
                .create(ENV_CONFIG.dbURL(), ENV_CONFIG.dbUser(), ENV_CONFIG.dbPass())
                .installPlugin(new SqlObjectPlugin());
    }
}

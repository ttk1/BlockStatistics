package net.ttk1.blockstatistics;

import com.google.inject.Singleton;
import io.ebean.EbeanServer;
import io.ebean.EbeanServerFactory;
import io.ebean.annotation.TxIsolation;
import io.ebean.config.ClassLoadConfig;
import io.ebean.config.ServerConfig;
import net.ttk1.blockstatistics.model.BlockEventHistoryModel;
import net.ttk1.blockstatistics.model.PlayerModel;
import org.avaje.datasource.DataSourceConfig;

import com.google.inject.Inject;
import org.bukkit.configuration.ConfigurationSection;

import javax.inject.Provider;

/**
 * @author ttk1 and mmtsk
 */
@Singleton
public class EbeanServerProvider implements Provider<EbeanServer> {
    private BlockStatistics plugin;
    private ConfigurationSection dbConfig;

    @Inject
    private void setPlugin(BlockStatistics plugin) {
        this.plugin = plugin;
        this.dbConfig = plugin.getConfig().getConfigurationSection("DB");
    }

    @Override
    public EbeanServer get() {
        if (dbConfig == null){
            return null;
        }

        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        ServerConfig serverConfig = new ServerConfig();

        String dbType = dbConfig.getString("type", "sqlite");
        String database = dbConfig.getString("database", "database");
        String hostname = dbConfig.getString("hostname", "hostname");
        String username = dbConfig.getString("username", "");
        String password = dbConfig.getString("password", "");

        switch (dbType) {
            case "sqlite":
                dataSourceConfig.setDriver("org.sqlite.JDBC");
                dataSourceConfig.setUrl("jdbc:sqlite:" + plugin.getDataFolder().getAbsolutePath() + "\\database.db");
                dataSourceConfig.setIsolationLevel(TxIsolation.valueOf("SERIALIZABLE").getLevel());
                break;
            case "mysql":
                dataSourceConfig.setDriver("com.mysql.jdbc.Driver");
                dataSourceConfig.setUrl("jdbc:mysql://" + hostname +"/"+ database);
                dataSourceConfig.setIsolationLevel(TxIsolation.valueOf("SERIALIZABLE").getLevel());
                break;
            default:
                return null;
        }
        dataSourceConfig.setUsername(username);
        dataSourceConfig.setPassword(password);

        serverConfig.setName(plugin.getDataFolder().getAbsolutePath()+"\\database");
        serverConfig.setDataSourceConfig(dataSourceConfig);
        serverConfig.addPackage("net.ttk1.blockstatistics.model");
        serverConfig.setClassLoadConfig(new ClassLoadConfig(this.getClass().getClassLoader()));

        EbeanServer ebeanServer = EbeanServerFactory.createWithContextClassLoader(serverConfig, this.getClass().getClassLoader());
        try {
            // try to access database
            PlayerModel.find.byId(1L);
            BlockEventHistoryModel.find.byId(1L);
        } catch (Exception e) {
            //TODO
            serverConfig.setDdlCreateOnly(dbConfig.getBoolean("protect", true));
            serverConfig.setDdlRun(true);
            serverConfig.setDdlGenerate(true);
            ebeanServer =  EbeanServerFactory.createWithContextClassLoader(serverConfig, this.getClass().getClassLoader());
        }
        return ebeanServer;
    }
}
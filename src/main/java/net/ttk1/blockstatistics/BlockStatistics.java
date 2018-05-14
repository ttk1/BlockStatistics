package net.ttk1.blockstatistics;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class BlockStatistics extends JavaPlugin {
    private Logger logger;

    public BlockStatistics() {
        this.logger = getLogger();
    }

    @Override
    public void onEnable() {
        logger.info("BlockStatistics enabled");
    }

    @Override
    public void onDisable() {
        logger.info("BlockStatistics disabled");
    }
}

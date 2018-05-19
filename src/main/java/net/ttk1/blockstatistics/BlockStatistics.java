package net.ttk1.blockstatistics;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.ttk1.blockstatistics.listener.BlockBreakEventListener;
import net.ttk1.blockstatistics.listener.BlockPlaceEventListener;
import net.ttk1.blockstatistics.service.BlockEventHistoryService;
import net.ttk1.blockstatistics.service.PlayerService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class BlockStatistics extends JavaPlugin {
    private Logger logger;
    private Configuration config;

    private BlockBreakEventListener blockBreakEventListener;
    private BlockPlaceEventListener blockPlaceEventListener;

    private BlockEventHistoryService blockEventHistoryService;
    private PlayerService playerService;

    @Inject
    private void setBlockPlaceEventListener(BlockPlaceEventListener blockPlaceEventListener) {
        this.blockPlaceEventListener = blockPlaceEventListener;
    }

    @Inject
    private void setBlockBreakEventListener(BlockBreakEventListener blockBreakEventListener) {
        this.blockBreakEventListener = blockBreakEventListener;
    }

    @Inject
    private void setBlockEventHistoryService(BlockEventHistoryService blockEventHistoryService) {
        this.blockEventHistoryService = blockEventHistoryService;
    }

    @Inject
    private void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    public BlockStatistics() {
    }

    @Override
    public void onEnable() {
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(getClassLoader());

        {
            // injector
            PluginModule module = new PluginModule(this);
            Injector injector = module.createInjector();
            injector.injectMembers(this);

            // logger
            logger = getLogger();

            // config
            initConfig();
            //config = getConfig();

            // listeners
            registerListeners();
        }

        Thread.currentThread().setContextClassLoader(currentClassLoader);
        logger.info("BlockStatistics enabled");
    }

    @Override
    public void onDisable() {
        logger.info("BlockStatistics disabled");
    }

    private void initConfig() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                getLogger().info("config.yml not found, creating!");
                saveDefaultConfig();
            } else {
                getLogger().info("config.yml found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerListeners(){
        getServer().getPluginManager().registerEvents(blockBreakEventListener, this);
        getServer().getPluginManager().registerEvents(blockPlaceEventListener, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            // player用
            if (command.getName().equalsIgnoreCase("bs")) {
                // 実験用に草ブロックの破壊数を表示する
                String playerUuid = ((Player) sender).getUniqueId().toString();
                long playerId = playerService.getPlayerID(playerUuid);
                int count = blockEventHistoryService.countBreakBlocks(playerId, 2, (byte) 0);

                sender.sendMessage("Grass: "+count);
            }
        } else {
            // コンソール用
            // TODO
        }

        return true;
    }
}

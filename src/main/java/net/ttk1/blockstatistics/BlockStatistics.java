package net.ttk1.blockstatistics;

import com.google.inject.Inject;
import com.google.inject.Injector;

import net.ttk1.blockstatistics.listener.BlockEventListener;
import net.ttk1.blockstatistics.listener.PlayerBucketEventListener;

import net.ttk1.blockstatistics.service.BlockHistoryService;
import net.ttk1.blockstatistics.service.PlayerService;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlockStatistics extends JavaPlugin {
    private Logger logger;
    private Configuration config;

    // event listeners
    private BlockEventListener blockEventListener;
    private PlayerBucketEventListener playerBucketEventListener;

    // services
    private BlockHistoryService blockHistoryService;
    private PlayerService playerService;

    // command regex patterns
    private final Pattern commandPattern =  Pattern.compile("(\\d+):(\\d+)");

    //
    // listener
    //

    @Inject
    private void setBlockEventListener(BlockEventListener blockEventListener) {
        this.blockEventListener = blockEventListener;
    }

    @Inject
    private void setPlayerBucketEventListener(PlayerBucketEventListener playerBucketEventListener) {
        this.playerBucketEventListener = playerBucketEventListener;
    }

    //
    // service
    //

    @Inject
    private void setBlockHistoryService(BlockHistoryService blockHistoryService) {
        this.blockHistoryService = blockHistoryService;
    }

    @Inject
    private void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    public BlockStatistics() {
    }

    @Override
    public void onEnable() {
        // config
        initConfig();

        // ebean周りの都合のためクラスローダを一時的に書き換える
        {
            ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(getClassLoader());

            // injector
            PluginModule module = new PluginModule(this);
            Injector injector = module.createInjector();
            injector.injectMembers(this);

            Thread.currentThread().setContextClassLoader(currentClassLoader);
        }

        // logger
        logger = getLogger();

        // listeners
        registerListeners();

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
        getServer().getPluginManager().registerEvents(blockEventListener, this);
        getServer().getPluginManager().registerEvents(playerBucketEventListener, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            // player用
            if (command.getName().equalsIgnoreCase("bs")) {
                if (args.length > 0) {
                    Matcher commandMatcher = commandPattern.matcher(args[0]);
                    if (commandMatcher.find()) {
                        String blockIdString = commandMatcher.group(1);
                        String blockDataString = commandMatcher.group(2);
                        logger.info(blockIdString);
                        logger.info(blockDataString);
                        try {
                            int blockId = Integer.parseInt(blockIdString);
                            byte blockData = (byte) Integer.parseInt(blockDataString);

                            // 実験用に草ブロックの破壊数を表示する
                            String playerUuid = ((Player) sender).getUniqueId().toString();
                            long playerId = playerService.getPlayerID(playerUuid);
                            //int count = blockHistoryService.countBreakBlocks(playerId, blockId, blockData);
                            //sender.sendMessage(String.valueOf(count));
                        } catch (Exception e) {
                            // TODO: デバッグ用, 後で取り除く
                            e.printStackTrace();
                            sender.sendMessage("Command syntax error!");
                        }

                    } else {
                        sender.sendMessage("Command syntax error!");
                    }
                } else {
                    sender.sendMessage("Please specify block_id and block_data!");
                }
            }
        } else {
            // コンソール用
            // TODO
        }

        return true;
    }
}

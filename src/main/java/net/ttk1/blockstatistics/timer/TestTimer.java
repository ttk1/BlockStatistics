package net.ttk1.blockstatistics.timer;

import com.google.inject.Inject;
import net.ttk1.blockstatistics.BlockStatistics;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.logging.Logger;

public class TestTimer extends BukkitRunnable {
    private BlockStatistics plugin;
    private Server server;
    private Logger logger;

    @Inject
    private void setPlugin(BlockStatistics plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.server = plugin.getServer();
    }

    public TestTimer() {
    }

    public void run() {
        // log
        logger.info("test test test");

        // broad cast
        server.broadcastMessage("本日は晴天なり");

        Player player = server.getPlayer("");

        /*
        if (player != null) {
            player.sendMessage(player.getTargetBlock(null, 500).toString());
        }
        */

        if (player != null) {
            List<Block> blocks = player.getLineOfSight(null, 200);

            for (Block block : blocks) {
                player.sendBlockChange(block.getLocation(), Material.LEAVES, (byte) 0x00);
            }
        }
    }
}

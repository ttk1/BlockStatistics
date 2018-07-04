package net.ttk1.blockstatistics.timer;

import com.google.inject.Inject;
import net.ttk1.blockstatistics.BlockStatistics;
import org.bukkit.scheduler.BukkitRunnable;

public class TestTimer extends BukkitRunnable {
    private BlockStatistics plugin;

    @Inject
    private void setPlugin(BlockStatistics plugin) {
        this.plugin = plugin;
    }

    public TestTimer() {
    }

    public void run() {
        // log
        plugin.getLogger().info("test test test");

        // broad cast
        plugin.getServer().broadcastMessage("本日は晴天なり");
    }
}

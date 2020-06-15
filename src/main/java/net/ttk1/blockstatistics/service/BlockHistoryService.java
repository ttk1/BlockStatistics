package net.ttk1.blockstatistics.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.ttk1.blockstatistics.BlockStatistics;
import net.ttk1.blockstatistics.model.BlockHistoryModel;
import org.bukkit.block.data.BlockData;
import org.bukkit.scheduler.BukkitRunnable;

import static net.ttk1.blockstatistics.model.BlockHistoryModel.BlockEventHistoryFinder;

@Singleton
public class BlockHistoryService {
    private BlockStatistics plugin;
    private String ebeanServerName;
    private BlockEventHistoryFinder blockEventHistoryFinder;

    @Inject
    private void setPlugin(BlockStatistics plugin) {
        this.plugin = plugin;
    }

    @Inject
    private void setEbeanServerName(@Named("ebeanServerName") String ebeanServerName) {
        this.ebeanServerName = ebeanServerName;
        this.blockEventHistoryFinder = new BlockEventHistoryFinder(ebeanServerName);
    }

    // レコードの登録
    public void registerRecord(long playerId, BlockData blockData) {
        RegisterRecordTask registerRecordTask = new RegisterRecordTask(playerId, blockData);
        registerRecordTask.runTaskAsynchronously(plugin);

        plugin.getLogger().info(blockData.getAsString());
    }

    // データの集計
    // TODO
    // 期間の指定
    private int countBlocks(long playerId, int blockId, byte blockData) {
        return blockEventHistoryFinder.query().where()
                .eq("player_id", playerId)
                .eq("block_id", blockId)
                .eq("block_data", blockData).findCount();
    }

    /*
    public int countBreakBlocks(long playerId, int blockId, byte blockData) {
        return countBlocks(playerId, 1, blockId, blockData);
    }

    public int countPlaceBlocks(long playerId, int blockId, byte blockData) {
        return countBlocks(playerId, 0, blockId, blockData);
    }
    */

    private class RegisterRecordTask extends BukkitRunnable {
        private BlockHistoryModel record;

        RegisterRecordTask(long playerId, BlockData blockData) {
            record = new BlockHistoryModel();
            record.setTime(System.currentTimeMillis());
            record.setPlayerId(playerId);
            record.setBlockData(blockData.getAsString());
        }

        @Override
        public void run() {
            record.insert(ebeanServerName);
        }
    }
}

package net.ttk1.blockstatistics.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import javafx.scene.paint.Material;
import net.ttk1.blockstatistics.BlockStatistics;
import net.ttk1.blockstatistics.model.BlockEventHistoryModel;
import static net.ttk1.blockstatistics.model.BlockEventHistoryModel.BlockEventHistoryFinder;

@Singleton
public class BlockEventHistoryService {
    public static int RECORD_TYPE_PLACE = 0;
    public static int RECORD_TYPE_REMOVE = 1;

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
    public void registerRecord(int recordType, long playerId, int blockId, byte blockData) {
        BlockEventHistoryModel record = new BlockEventHistoryModel();
        record.setTime(System.currentTimeMillis());
        record.setType(recordType);
        record.setPlayerId(playerId);
        record.setBlockId(blockId);
        record.setBlockData(blockData);
        record.insert(ebeanServerName);

        if (recordType == RECORD_TYPE_PLACE) {
            plugin.getLogger().info("Place: " + org.bukkit.Material.getMaterial(blockId).toString());
        } else {
            plugin.getLogger().info("Remove: " + org.bukkit.Material.getMaterial(blockId).toString());
        }
    }

    // データの集計
    // TODO
    // 期間の指定
    private int countBlocks(long playerId, int type, int blockId, byte blockData) {
        return blockEventHistoryFinder.query().where()
                .eq("player_id", playerId)
                .eq("type", type)
                .eq("block_id", blockId)
                .eq("block_data", blockData).findCount();
    }

    public int countBreakBlocks(long playerId, int blockId, byte blockData) {
        return countBlocks(playerId, 1, blockId, blockData);
    }

    public int countPlaceBlocks(long playerId, int blockId, byte blockData) {
        return countBlocks(playerId, 0, blockId, blockData);
    }
}

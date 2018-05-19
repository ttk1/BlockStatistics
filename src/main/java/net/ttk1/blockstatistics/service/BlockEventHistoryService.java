package net.ttk1.blockstatistics.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.ttk1.blockstatistics.BlockStatistics;
import net.ttk1.blockstatistics.model.BlockEventHistoryModel;

@Singleton
public class BlockEventHistoryService {
    private BlockStatistics plugin;

    @Inject
    private void setPlugin(BlockStatistics plugin) {
        this.plugin = plugin;
    }

    // レコードの登録
    public void registerRecord(int recordType, long playerId, int blockId, byte blockData) {
        BlockEventHistoryModel record = new BlockEventHistoryModel();
        record.setTime(System.currentTimeMillis());
        record.setType(recordType);
        record.setPlayerId(playerId);
        record.setBlockId(blockId);
        record.setBlockData(blockData);

        record.save();
    }

    // データの集計
    // TODO
    public int countBreakBlocks(long playerId, int blockId, byte blockData) {
        return BlockEventHistoryModel.find.query().where().eq("player_id", playerId)
                .eq("type", 1)
                .eq("block_id", blockId)
                .eq("block_data", blockData).findCount();
    }
}

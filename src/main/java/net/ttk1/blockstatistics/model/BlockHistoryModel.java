package net.ttk1.blockstatistics.model;

import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.Cache;
import org.bukkit.block.data.BlockData;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Cache
@Entity
@Table(name = "block_history")
public class BlockHistoryModel extends Model {
    @Id
    private long id;

    private long time;
    private long playerId;
    private BlockData blockData;

    // setter
    public void setTime(long time) {
        this.time = time;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public void setBlockData(BlockData blockData) {
        this.blockData = blockData;
    }

    // getter
    public long getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public long getPlayerId() {
        return playerId;
    }

    public BlockData getBlockData() {
        return blockData;
    }

    public static class BlockEventHistoryFinder extends Finder<Long, BlockHistoryModel>{
        public BlockEventHistoryFinder(String ebeanServerName){
            super(BlockHistoryModel.class, ebeanServerName);
        }
    }
}

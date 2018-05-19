package net.ttk1.blockstatistics.model;

import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.Cache;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Cache
@Entity
@Table(name = "block_event_history")
public class BlockEventHistoryModel extends Model {
    public static final BlockEventHistoryFinder find = new BlockEventHistoryFinder();

    @Id
    private long id;

    // 0: place event
    // 1: bleak event
    private int type;

    private long time;
    private long playerId;
    private int blockId;
    private byte blockData;

    // setter
    public void setTime(long time) {
        this.time = time;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    public void setBlockData(byte blockData) {
        this.blockData = blockData;
    }

    // getter
    public long getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public int getType() {
        return type;
    }

    public long getPlayerId() {
        return playerId;
    }

    public int getBlockId() {
        return blockId;
    }

    public byte getBlockData() {
        return blockData;
    }

    public static class BlockEventHistoryFinder extends Finder<Long, BlockEventHistoryModel>{
        public BlockEventHistoryFinder(){
            super(BlockEventHistoryModel.class);
        }

        public BlockEventHistoryFinder(String ebeanServerName){
            super(BlockEventHistoryModel.class, ebeanServerName);
        }
    }
}

package net.ttk1.blockstatistics.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.ttk1.blockstatistics.BlockStatistics;
import net.ttk1.blockstatistics.model.PlayerModel;

import javax.persistence.PersistenceException;

@Singleton
public class PlayerService {
    private BlockStatistics plugin;

    @Inject
    private void setPlugin(BlockStatistics plugin) {
        this.plugin = plugin;
    }

    // 負の値が返ったら未登録
    public long getPlayerID(String playerUuid) {
        PlayerModel player = PlayerModel.find.query().where().eq("uuid", playerUuid).findOne();
        if (player == null) {
            return -1;
        } else {
            return player.getId();
        }
    }

    // nullが返ったら未登録
    public String getPlayerName(long playerId) {
        PlayerModel player = PlayerModel.find.byId(playerId);
        if (player == null) {
            return null;
        } else {
            return player.getName();
        }
    }

    // nullが返ったら未登録
    public String getPlayerUuid(long playerId) {
        PlayerModel player = PlayerModel.find.byId(playerId);
        if (player == null) {
            return null;
        } else {
            return player.getUuid();
        }
    }

    public long registerPlayer(String playerUuid, String playerName) {
        PlayerModel player = new PlayerModel();
        player.setName(playerName);
        player.setUuid(playerUuid);

        // uuidが重複したら例外が飛ぶ
        try {
            player.save();
        } catch (PersistenceException e) {
            e.printStackTrace();
            return -1;
        }
        return player.getId();
    }

    public void updatePlayerName(long playerId, String playerName) {
        PlayerModel player = PlayerModel.find.byId(playerId);
        if (player != null) {
            player.setName(playerName);
            player.update();
        }
    }
}

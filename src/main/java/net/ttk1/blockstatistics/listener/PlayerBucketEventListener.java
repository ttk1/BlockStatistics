package net.ttk1.blockstatistics.listener;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.ttk1.blockstatistics.service.BlockHistoryService;
import net.ttk1.blockstatistics.service.PlayerService;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

/**
 * バケットイベントを監視するクラス
 */
@Singleton
public class PlayerBucketEventListener implements Listener {
    private PlayerService playerService;
    private BlockHistoryService blockHistoryService;

    @Inject
    private void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Inject
    private void setBlockHistoryService(BlockHistoryService blockHistoryService) {
        this.blockHistoryService = blockHistoryService;
    }

    @EventHandler
    public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        String playerUuid = player.getUniqueId().toString();
        Material bucket = event.getBucket();
        Block block = event.getBlockClicked();

        long playerId = playerService.getPlayerID(playerUuid);
        blockHistoryService.registerRecord(playerId, block.getBlockData());
    }

    @EventHandler
    public void onPlayerBucketFillEvent(PlayerBucketFillEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockClicked().getRelative(event.getBlockFace());

        if (block.isLiquid()) {
            String playerUuid = player.getUniqueId().toString();
            long playerId = playerService.getPlayerID(playerUuid);
            blockHistoryService.registerRecord(playerId, block.getBlockData());
        }
    }
}

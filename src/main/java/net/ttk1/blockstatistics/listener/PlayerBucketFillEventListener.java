package net.ttk1.blockstatistics.listener;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import net.ttk1.blockstatistics.service.BlockEventHistoryService;
import net.ttk1.blockstatistics.service.PlayerService;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketFillEvent;

/**
 * 水・マグマを掬うイベントを監視するクラス
 */
@Singleton
public class PlayerBucketFillEventListener implements Listener {
    private PlayerService playerService;
    private BlockEventHistoryService blockEventHistoryService;

    @Inject
    private void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Inject
    private void setBlockEventHistoryService(BlockEventHistoryService blockEventHistoryService) {
        this.blockEventHistoryService = blockEventHistoryService;
    }

    @EventHandler
    public void onPlayerBucketFillEvent(PlayerBucketFillEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockClicked().getRelative(event.getBlockFace());
        Material blockType = block.getType();

        if (
                blockType.equals(Material.WATER) ||
                        blockType.equals(Material.STATIONARY_WATER) ||
                        blockType.equals(Material.LAVA) ||
                        blockType.equals(Material.STATIONARY_LAVA)
                ) {
            String playerUuid = player.getUniqueId().toString();
            long playerId = playerService.getPlayerID(playerUuid);
            blockEventHistoryService.registerRecord(BlockEventHistoryService.RECORD_TYPE_REMOVE, playerId, block.getTypeId(), block.getData());
        }
    }
}

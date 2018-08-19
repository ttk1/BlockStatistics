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
import org.bukkit.event.player.PlayerBucketEmptyEvent;

/**
 * 水・マグマを設置するイベントを監視するクラス
 */
@Singleton
public class PlayerBucketEmptyEventListener implements Listener {
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
    public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        Material bucket = event.getBucket();

        if (bucket.equals(Material.LAVA_BUCKET)|| bucket.equals(Material.WATER_BUCKET)) {
            Block block = event.getBlockClicked();
            String playerUuid = player.getUniqueId().toString();
            long playerId = playerService.getPlayerID(playerUuid);

            if (bucket.equals(Material.LAVA_BUCKET)) {
                blockEventHistoryService.registerRecord(BlockEventHistoryService.RECORD_TYPE_PLACE, playerId, Material.LAVA.getId(), (byte) 0);
            } else {
                blockEventHistoryService.registerRecord(BlockEventHistoryService.RECORD_TYPE_PLACE, playerId, Material.WATER.getId(), (byte) 0);
            }
        }
    }
}

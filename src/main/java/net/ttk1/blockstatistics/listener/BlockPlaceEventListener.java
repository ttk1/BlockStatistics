package net.ttk1.blockstatistics.listener;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.ttk1.blockstatistics.service.BlockEventHistoryService;
import net.ttk1.blockstatistics.service.PlayerService;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * ブロックの設置を監視するクラス
 */
@Singleton
public class BlockPlaceEventListener implements Listener {
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
    public void onBlockPlaceEventListener(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();

        String playerUuid = player.getUniqueId().toString();
        long playerId = playerService.getPlayerID(playerUuid);

        blockEventHistoryService.registerRecord(BlockEventHistoryService.RECORD_TYPE_PLACE, playerId, block.getTypeId(), block.getData());
    }
}

package net.ttk1.blockstatistics.listener;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.ttk1.blockstatistics.service.BlockEventHistoryService;
import net.ttk1.blockstatistics.service.PlayerService;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * ブロックイベントを監視するクラス
 */
@Singleton
public class BlockEventListener implements Listener {
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
    public void onBlockBreakEventListener(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        String playerUuid = player.getUniqueId().toString();
        long playerId = playerService.getPlayerID(playerUuid);

        blockEventHistoryService.registerRecord(playerId, block.getBlockData());
    }

    @EventHandler
    public void onBlockPlaceEventListener(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        String playerUuid = player.getUniqueId().toString();
        long playerId = playerService.getPlayerID(playerUuid);

        blockEventHistoryService.registerRecord(playerId, block.getBlockData());
    }
}

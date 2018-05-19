package net.ttk1.blockstatistics.listener;

import com.google.inject.Inject;
import net.ttk1.blockstatistics.BlockStatistics;
import net.ttk1.blockstatistics.service.PlayerService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
// import org.bukkit.event.player.PlayerQuitEvent;

public class SessionListener implements Listener {
    private PlayerService playerService;
    private BlockStatistics plugin;

    @Inject
    private void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Inject
    private void setPlugin(BlockStatistics plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // playerが登録済みかどうかをチェックする
        String uuid = player.getUniqueId().toString();
        long playerID = playerService.getPlayerID(uuid);
        if (playerID > 0) {
            // 登録済みの場合は名前の変更がないかチェックする
            String registerdName = playerService.getPlayerName(playerID);
            String playerName = player.getName();
            if (!registerdName.equals(playerName)) {
                // 名前が一致しない場合更新する
                playerService.updatePlayerName(playerID, playerName);
            }
        } else {
            // 未登録の場合登録する
            String playerUuid = player.getUniqueId().toString();
            String playerName = player.getName();
            playerService.registerPlayer(playerUuid, playerName);
        }
    }

    /*
    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {

    }
    */
}

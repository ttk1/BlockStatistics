package net.ttk1.blockstatistics.test.model;

import net.ttk1.blockstatistics.model.PlayerModel;
import org.junit.Before;
import org.junit.Test;

import static net.ttk1.blockstatistics.model.PlayerModel.PlayerFinder;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import javax.persistence.PersistenceException;

public class PlayerModelTest {
    private PlayerFinder playerFinder;

    @Before
    public void before() {
        this.playerFinder = new PlayerFinder("db");
    }

    @Test
    public void registerPlayerTest() {
        String TEST_PLAYER = "test_player";
        String TEST_UUID = "test_uuid";

        PlayerModel playerModel = new PlayerModel();
        playerModel.setName(TEST_PLAYER);
        playerModel.setUuid(TEST_UUID);
        playerModel.save();

        playerModel = playerFinder.query().where().eq("uuid", TEST_UUID).findOne();
        assertThat(playerModel.getName(), is(TEST_PLAYER));
        assertThat(playerModel.getUuid(), is(TEST_UUID));
    }
    @Test(expected = PersistenceException.class)
    public void uniqueConstraintTest() {
        String TEST_PLAYER1 = "test_player1";
        String TEST_PLAYER2 = "test_player2";
        String TEST_UUID = "test_uuid";

        PlayerModel playerModel = new PlayerModel();
        playerModel.setName(TEST_PLAYER1);
        playerModel.setUuid(TEST_UUID);
        playerModel.save();

        playerModel = new PlayerModel();
        playerModel.setName(TEST_PLAYER2);
        playerModel.setUuid(TEST_UUID);
        playerModel.save();
    }
}

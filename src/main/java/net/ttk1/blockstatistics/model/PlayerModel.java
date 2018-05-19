package net.ttk1.blockstatistics.model;

import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.Cache;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Cache
@Entity
@Table(name = "player", uniqueConstraints = { @UniqueConstraint(columnNames = {"uuid"}) })
public class PlayerModel extends Model {
    public static final PlayerFinder find = new PlayerFinder();

    @Id
    private long id;
    private String uuid;
    private String name;

    // setter
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setName(String name) {
        this.name = name;
    }

    // getter
    public long getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public static class PlayerFinder extends Finder<Long, PlayerModel>{
        PlayerFinder(){
            super(PlayerModel.class);
        }
    }
}

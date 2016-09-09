package ru.jleague13.entity;

/**
 * @author ashevenkov 07.09.16 17:47.
 */
public class TransferQueryPlayer {

    private String playerName;
    private int replacement;
    private int price;
    private Player player;

    public TransferQueryPlayer(String playerName, int replacement, int price) {
        this.playerName = playerName;
        this.replacement = replacement;
        this.price = price;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getReplacement() {
        return replacement;
    }

    public void setReplacement(int replacement) {
        this.replacement = replacement;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}

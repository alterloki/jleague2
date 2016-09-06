package ru.jleague13.entity;

/**
 * @author ashevenkov 26.04.16 16:34.
 */
public class TeamPlayer extends Player {

    public TeamPlayer() {
        super();
    }

    public TeamPlayer(int id, int playerId, String name, PlayerType playerType,
                      String country, String seller, String buyer, int age, int talent,
                      int experience, int strength, int health, int price, int salary,
                      int payed, String abilities, int shooting, int handling, int reflexes,
                      int passing, int cross, int dribbling, int tackling, int heading,
                      int speed, int stamina, int birthtour) {
        super(id, playerId, name, playerType, country, seller, buyer, age, talent, experience,
                strength, health, price, salary, payed, abilities, birthtour,
                new Abilities(shooting, handling,
                        reflexes, passing, cross, dribbling, tackling, heading, speed, stamina));
    }
}

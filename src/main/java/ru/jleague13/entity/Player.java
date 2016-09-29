package ru.jleague13.entity;

/**
 * @author ashevenkov 25.09.15 23:01.
 */
public class Player {

    private int id;
    private int playerId;
    private String name;

    private PlayerType playerType;
    private String country;
    private int age;
    private int talent;
    private int experience;
    private int strength;
    private int health;
    private int price;
    private int salary;
    private int payed;

    private String seller;
    private String buyer;

    private Abilities abilities;

    private String abilitiesString;
    private int birthtour;
    private String clubName;

    private PlayerGameInfo gameInfo;

    public Player() {
    }

    public Player(int id, int playerId, String name, PlayerType playerType, String country,
                  String seller, String buyer, int age, int talent, int experience, int strength,
                  int health, int price, int salary, int payed, String abilitiesString, int birthtour,
                  Abilities abilities) {
        this.id = id;
        this.playerId = playerId;
        this.name = name;
        this.playerType = playerType;
        this.country = country;
        this.seller = seller;
        this.buyer = buyer;
        this.age = age;
        this.talent = talent;
        this.experience = experience;
        this.strength = strength;
        this.health = health;
        this.price = price;
        this.salary = salary;
        this.payed = payed;
        this.birthtour = birthtour;
        this.abilities = abilities;
        this.abilitiesString = abilitiesString;
    }

    public Player(int id, int playerId, String name, PlayerType playerType, String country,
                  String seller, String buyer, int age, int talent, int experience, int strength,
                  int health, int price, int salary, int payed, String abilitiesString, int birthtour,
                  Abilities abilities, PlayerGameInfo gameInfo) {
        this(id, playerId, name, playerType, country, seller, buyer, age, talent, experience,
        strength, health, price, salary, payed, abilitiesString, birthtour, abilities);
        this.gameInfo = gameInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getTalent() {
        return talent;
    }

    public void setTalent(int talent) {
        this.talent = talent;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getPayed() {
        return payed;
    }

    public void setPayed(int payed) {
        this.payed = payed;
    }

    public String getAbilitiesString() {
        return abilitiesString;
    }

    public void setAbilitiesString(String abilities) {
        this.abilitiesString = abilities;
    }

    public int getBirthtour() {
        return birthtour;
    }

    public void setBirthtour(int birthtour) {
        this.birthtour = birthtour;
    }

    public String getFunctions() {
        StringBuilder sb = new StringBuilder();
        addFunction(sb, this.abilities.getShooting(), "у");
        addFunction(sb, this.abilities.getHandling(), "т");
        addFunction(sb, this.abilities.getReflexes(), "р");
        addFunction(sb, this.abilities.getPassing(), "п");
        addFunction(sb, this.abilities.getCross(), "н");
        addFunction(sb, this.abilities.getDribbling(), "д");
        addFunction(sb, this.abilities.getTackling(), "о");
        addFunction(sb, this.abilities.getHeading(), "вг");
        addFunction(sb, this.abilities.getSpeed(), "с");
        addFunction(sb, this.abilities.getStamina(), "ф");
        return sb.toString();
    }

    private void addFunction(StringBuilder sb, int value, String let) {
        if(value > 20) {
            sb.append(let);
            sb.append(value);
        }
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public Abilities getAbilities() {
        return abilities;
    }

    public void setAbilities(Abilities abilities) {
        this.abilities = abilities;
    }

    public String getClubName() {
        return clubName;
    }
}

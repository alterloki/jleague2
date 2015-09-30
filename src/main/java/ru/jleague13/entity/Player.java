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
    private String seller;
    private String buyer;
    private int age;
    private int talent;
    private int experience;
    private int strength;
    private int health;
    private int price;
    private int salary;
    private int payed;

    private String abilities;
    private int shooting;
    private int handling;
    private int reflexes;
    private int passing;
    private int cross;
    private int dribbling;
    private int tackling;
    private int heading;
    private int speed;
    private int stamina;
    private int birthtour;

    public Player() {
    }

    public Player(int id, int playerId, String name, PlayerType playerType, String country,
                  String seller, String buyer, int age, int talent, int experience, int strength,
                  int health, int price, int salary, int payed, String abilities, int shooting,
                  int handling, int reflexes, int passing, int cross, int dribbling, int tackling,
                  int heading, int speed, int stamina, int birthtour) {
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
        this.abilities = abilities;
        this.shooting = shooting;
        this.handling = handling;
        this.reflexes = reflexes;
        this.passing = passing;
        this.cross = cross;
        this.dribbling = dribbling;
        this.tackling = tackling;
        this.heading = heading;
        this.speed = speed;
        this.stamina = stamina;
        this.birthtour = birthtour;
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

    public String getAbilities() {
        return abilities;
    }

    public void setAbilities(String abilities) {
        this.abilities = abilities;
    }

    public int getShooting() {
        return shooting;
    }

    public void setShooting(int shooting) {
        this.shooting = shooting;
    }

    public int getHandling() {
        return handling;
    }

    public void setHandling(int handling) {
        this.handling = handling;
    }

    public int getReflexes() {
        return reflexes;
    }

    public void setReflexes(int reflexes) {
        this.reflexes = reflexes;
    }

    public int getPassing() {
        return passing;
    }

    public void setPassing(int passing) {
        this.passing = passing;
    }

    public int getCross() {
        return cross;
    }

    public void setCross(int cross) {
        this.cross = cross;
    }

    public int getDribbling() {
        return dribbling;
    }

    public void setDribbling(int dribbling) {
        this.dribbling = dribbling;
    }

    public int getTackling() {
        return tackling;
    }

    public void setTackling(int tackling) {
        this.tackling = tackling;
    }

    public int getHeading() {
        return heading;
    }

    public void setHeading(int heading) {
        this.heading = heading;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public int getBirthtour() {
        return birthtour;
    }

    public void setBirthtour(int birthtour) {
        this.birthtour = birthtour;
    }

    public String getFunctions() {
        StringBuilder sb = new StringBuilder();
        addFunction(sb, this.shooting, "у");
        addFunction(sb, this.handling, "т");
        addFunction(sb, this.reflexes, "р");
        addFunction(sb, this.passing, "п");
        addFunction(sb, this.cross, "н");
        addFunction(sb, this.dribbling, "д");
        addFunction(sb, this.tackling, "о");
        addFunction(sb, this.heading, "вг");
        addFunction(sb, this.speed, "с");
        addFunction(sb, this.stamina, "ф");
        return sb.toString();
    }

    private void addFunction(StringBuilder sb, int value, String let) {
        if(value > 20) {
            sb.append(let);
            sb.append(value);
        }
    }
}

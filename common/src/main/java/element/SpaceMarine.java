package element;

import exceptions.NullFieldException;
import exceptions.WrongFieldException;

import java.util.Date;

public class SpaceMarine implements CollectionPart {
    private long
            id; // Значение поля должно быть больше 0, Значение этого поля должно быть уникальным,
    // Значение этого поля должно генерироваться автоматически
    private String name; // Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; // Поле не может быть null
    private final java.util.Date
            creationDate; // Поле не может быть null, Значение этого поля должно генерироваться
    // автоматически
    private Double health; // Поле может быть null, Значение поля должно быть больше 0
    private int heartCount; // Значение поля должно быть больше 0, Максимальное значение поля: 3
    private AstartesCategory category; // Поле может быть null
    private MeleeWeapon meleeWeapon; // Поле может быть null
    private Chapter chapter; // Поле не может быть null

    private String owner;

    public SpaceMarine(
            String name,
            Coordinates coordinates,
            Double health,
            int heartCount,
            AstartesCategory category,
            MeleeWeapon meleeWeapon,
            Chapter chapter,
            long id,
            Date creationDate,
            String owner) {
        if (name == null || name.equals("")) {
            throw new NullFieldException("name");
        }
        if (coordinates == null) {
            throw new NullFieldException("coordinates");
        }
        if (health != null && health <= 0) {
            throw new WrongFieldException("The value of the health field must be greater than 0");
        }
        if (heartCount <= 0 || heartCount > 3) {
            throw new WrongFieldException(
                    "The value of the heartCount field must be greater than 0.The value of the heartCount field must be less than or equal to 3.");
        }
        if (chapter == null) {
            throw new NullFieldException("chapter");
        }
        if (id <= 0) {
            throw new WrongFieldException("The value of the id field must be greater than 0");
        }
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.health = health;
        this.heartCount = heartCount;
        this.category = category;
        this.meleeWeapon = meleeWeapon;
        this.chapter = chapter;
        this.owner = owner;
    }

    public SpaceMarine(
            String name,
            Coordinates coordinates,
            Double health,
            int heartCount,
            AstartesCategory category,
            MeleeWeapon meleeWeapon,
            Chapter chapter,
            String owner) {
        this(
                name,
                coordinates,
                health,
                heartCount,
                category,
                meleeWeapon,
                chapter,
                Math.abs(new Date().getTime()),
                new Date(),
                owner);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Coordinates getCoordinates() {
        return coordinates;
    }

    @Override
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public Double getHealth() {
        return health;
    }

    @Override
    public void setHealth(Double health) {
        this.health = health;
    }

    @Override
    public int getHeartCount() {
        return heartCount;
    }

    @Override
    public void setHeartCount(int heartCount) {
        this.heartCount = heartCount;
    }

    @Override
    public AstartesCategory getCategory() {
        return category;
    }

    @Override
    public void setCategory(AstartesCategory category) {
        this.category = category;
    }

    @Override
    public MeleeWeapon getMeleeWeapon() {
        return meleeWeapon;
    }

    @Override
    public void setMeleeWeapon(MeleeWeapon meleeWeapon) {
        this.meleeWeapon = meleeWeapon;
    }

    @Override
    public Chapter getChapter() {
        return chapter;
    }

    @Override
    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    @Override
    public int compareTo(CollectionPart o) {
        return this.getName().compareTo(o.getName());
    }

    @Override
    public String toLineCSV() {
        String strCategory = "";
        if (this.category != null) {
            strCategory = this.category.toString();
        }
        String strMeleeWeapon = "";
        if (this.meleeWeapon != null) {
            strMeleeWeapon = this.meleeWeapon.toString();
        }
        String strHealth = "";
        if (this.health != null) {
            strHealth = this.health.toString();
        }
        return String.format(
                "%s,%d,%d,%s,%d,%s,%s,%s,%d,%d,%s",
                this.name,
                this.coordinates.getX(),
                this.coordinates.getY(),
                strHealth.replace(',', '.'),
                this.heartCount,
                strCategory,
                strMeleeWeapon,
                this.chapter.getName(),
                this.chapter.getMarinesCount(),
                this.id,
                this.creationDate);
    }

    @Override
    public String toString() {
        return String.format(
                "id: %d%n"
                        + "owner: %s%n"
                        + "name: %s%n"
                        + "coordinates:%n"
                        + "   x: %d%n"
                        + "   y: %d%n"
                        + "creationDate: %s%n"
                        + "health: %f%n"
                        + "heartCount: %d%n"
                        + "category: %s%n"
                        + "meleeWeapon: %s%n"
                        + "chapter:%n"
                        + "   name: %s%n"
                        + "   marinesCount: %d%n%n",
                this.id,
                this.owner,
                this.name,
                this.coordinates.getX(),
                this.coordinates.getY(),
                this.creationDate,
                this.health,
                this.heartCount,
                this.category,
                this.meleeWeapon,
                this.chapter.getName(),
                this.chapter.getMarinesCount());
    }

    @Override
    public String getOwner(){
        return this.owner;
    }

    @Override
    public void setId(long id){
        this.id = id;
    }
}
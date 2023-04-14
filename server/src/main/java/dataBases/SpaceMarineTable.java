package dataBases;

import element.*;

import java.sql.*;
import java.util.Date;
import java.util.LinkedList;

public class SpaceMarineTable {
    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void init() throws SQLException {
        try (Statement st = this.connection.createStatement()){
            String reqSQL = "CREATE SEQUENCE IF NOT EXISTS counter;" +
                    "CREATE TABLE IF NOT EXISTS space_marine (" +
                    "id bigint PRIMARY KEY DEFAULT nextval('counter')," +
                    "owner varchar(128) NOT NULL REFERENCES users(login)," +
                    "name varchar(128) NOT NULL," +
                    "x_coordinate integer NOT NULL CHECK (x_coordinate <= 201)," +
                    "y_coordinate integer NOT NULL CHECK (y_coordinate > -440)," +
                    "health float8 CHECK (health > 0)," +
                    "heart_count integer NOT NULL CHECK (heart_count > 0 AND heart_count <= 3)," +
                    "category varchar(20)," +
                    "melee_weapon varchar(20)," +
                    "chapter_name varchar(128) NOT NULL," +
                    "marines_count integer NOT NULL CHECK (marines_count > 0 AND marines_count <= 1000)," +
                    "creation_date timestamp NOT NULL);";
            st.executeUpdate(reqSQL);
        }
    }

    public long addSpaceMarine(CollectionPart elem) throws SQLException {
        String reqSQL = "INSERT INTO space_marine(owner, name, x_coordinate, y_coordinate, health, heart_count, category, melee_weapon, chapter_name, marines_count, creation_date)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id;";
        PreparedStatement st = this.connection.prepareStatement(reqSQL);
        this.setStatement(st, elem, false);
        ResultSet rs = st.executeQuery();
        rs.next();
        return rs.getLong("id");
    }

    public void clear(String owner) throws SQLException {
        String reqSQL = "DELETE FROM space_marine WHERE owner = ?;";
        PreparedStatement st = this.connection.prepareStatement(reqSQL);
        st.setString(1, owner);
        st.executeUpdate();
    }
    public void removeById(String owner, long id) throws SQLException {
        String reqSQL = "DELETE FROM space_marine WHERE owner = ? AND id = ?;";
        PreparedStatement st = this.connection.prepareStatement(reqSQL);
        st.setString(1, owner);
        st.setLong(2, id);
        st.executeUpdate();
    }

    public void removeFirst(String owner) throws SQLException {
        String reqSQL = "DELETE FROM space_marine WHERE ctid IN (SELECT ctid FROM space_marine WHERE owner = ? LIMIT 1);";
        PreparedStatement st = this.connection.prepareStatement(reqSQL);
        st.setString(1, owner);
        st.executeUpdate();
    }

    public void update(long id, String owner, CollectionPart newElem) throws SQLException {
        String reqSQL = "UPDATE space_marine SET " +
                "owner = ?," +
                "name = ?," +
                "x_coordinate = ?," +
                "y_coordinate = ?," +
                "health = ?," +
                "heart_count = ?," +
                "category = ?," +
                "melee_weapon = ?," +
                "chapter_name = ?," +
                "marines_count = ?" +
                "WHERE id = ? AND owner = ?;";
        PreparedStatement st = this.connection.prepareStatement(reqSQL);
        this.setStatement(st, newElem, true);
        st.setLong(11, id);
        st.setString(12, owner);
        st.executeUpdate();
    }

    public SpaceMarine getElem(ResultSet resultSet) throws SQLException {
        String categoryStr = resultSet.getString("category");
        AstartesCategory category;
        if (categoryStr == null){
            category = null;
        } else{
            category = AstartesCategory.valueOf(categoryStr);
        }
        String meleeWeaponStr = resultSet.getString("melee_weapon");
        MeleeWeapon meleeWeapon;
        if (meleeWeaponStr == null){
            meleeWeapon = null;
        } else{
            meleeWeapon = MeleeWeapon.valueOf(meleeWeaponStr);
        }
        Double health;
        if (resultSet.getDouble("health") == 0.0){
            health = null;
        } else {
            health = resultSet.getDouble("health");
        }
        SpaceMarine spaceMarine = new SpaceMarine(
                resultSet.getString("name"),
                new Coordinates(resultSet.getInt("x_coordinate"), resultSet.getInt("y_coordinate")),
                health,
                resultSet.getInt("heart_count"),
                category,
                meleeWeapon,
                new Chapter(resultSet.getString("chapter_name"), resultSet.getInt("marines_count")),
                resultSet.getLong("id"),
                new Date(resultSet.getTimestamp("creation_date").getTime()),
                resultSet.getString("owner"));
        return spaceMarine;
    }

    public LinkedList<CollectionPart> getSpaceMarineCollection(){
        LinkedList<CollectionPart> spaceMarineCol = new LinkedList<>();
        try {
            Statement st = this.connection.createStatement();
            String reqSQL = "SELECT * FROM space_marine";
            ResultSet rs = st.executeQuery(reqSQL);
            while (rs.next()){
                spaceMarineCol.add(this.getElem(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return spaceMarineCol;

    }

    public void setStatement(PreparedStatement st, CollectionPart elem, boolean update) throws SQLException {
        st.setString(1, elem.getOwner());
        st.setString(2, elem.getName());
        st.setInt(3, elem.getCoordinates().getX());
        st.setInt(4, elem.getCoordinates().getY());
        Double health = elem.getHealth();
        if (health != null){
            st.setDouble(5, health);
        } else {
            st.setNull(5, Types.DOUBLE);
        }
        st.setInt(6, elem.getHeartCount());
        AstartesCategory category = elem.getCategory();
        if (category != null){
            st.setString(7, category.toString());
        } else{
            st.setNull(7, Types.VARCHAR);
        }
        MeleeWeapon meleeWeapon = elem.getMeleeWeapon();
        if (meleeWeapon != null){
            st.setString(8, meleeWeapon.toString());
        } else{
            st.setNull(8, Types.VARCHAR);
        }
        st.setString(9, elem.getChapter().getName());
        st.setInt(10, elem.getChapter().getMarinesCount());
        if(!update){
            st.setTimestamp(11, new Timestamp(elem.getCreationDate().getTime()));
        }
    }

    public void reduceCounter(long id) throws SQLException {
        String reqSQL = "SELECT setval('counter', ?);";
        PreparedStatement st = this.connection.prepareStatement(reqSQL);
        st.setLong(1, id);
        st.executeQuery();

    }
}

package model;

import common.Constants;
import db.Database;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 *
 * Created by Kunmiao Yang on 4/5/2018.
 */
public class RoomTest {
    @Before
    public void setUp() throws Exception {
        Model.setDatabase(new Database(Constants.DB_DRIVER, Constants.DB_URL, Constants.DB_USER, Constants.DB_PASSWORD));
    }

    @After
    public void tearDown() throws Exception {
        Model.remove(Constants.TABLE_ROOM, "room_number = 123");
        Model.database.close();
    }

    public void initObject() {
        try {
            Model.database.getStatement().executeUpdate("INSERT INTO room(hotel_id, room_number, room_type, availability) VALUES " +
                    "(0001, 123, 'Economy', 1);");
        } catch (SQLException e) {
//            e.printStackTrace();
        }
    }

    @Test
    public void testConstructor() throws Exception {
        Room r = new Room(Hotel.getById(1), 123, "Economy", true);
        assertNotNull(r);
        ResultSet resultSet = Model.database.getStatement().executeQuery("SELECT * FROM room WHERE hotel_id = 1 AND room_number = 123;");
        assertTrue(resultSet.next());
        resultSet.close();
    }

    @Test
    public void testGetById() throws Exception {
        initObject();
        Room r = Room.getById(1, 123);
        assertNotNull(r);
        assertEquals(r.getType(), "Economy");
        assertEquals(r.getMaxOccupy(), 1);
        assertEquals((int)r.getNightlyRate(), 100);
    }

    @Test
    public void testRemove() throws Exception {
        initObject();
        Room r = Room.getById(1, 123);
        assertNotNull(r);
        assertTrue(r.remove());
        r = Room.getById(1, 123);
        assertNull(r);
    }

    @Test
    public void testUpdate() throws Exception {
        initObject();
        Room r = Room.getById(1, 123);
        assertNotNull(r);
        r.setAvailability(false);
        r.setType("Deluxe");
        assertTrue(r.update());
    }

    @Test
    public void testGetCurrentCheckIn() throws Exception {
        Room room = Room.getById(3, 2);
        assertNotNull(room);
        CheckIn checkIn = room.getCurrentCheckIn();
        assertNotNull(checkIn);
        assertEquals(4, checkIn.getId());

        room = Room.getById(1, 2);
        assertNotNull(room);
        checkIn = room.getCurrentCheckIn();
        assertNull(checkIn);

    }
}
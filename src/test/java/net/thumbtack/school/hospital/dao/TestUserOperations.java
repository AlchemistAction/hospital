package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.User;
import net.thumbtack.school.hospital.model.UserType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestUserOperations extends TestBase {

    @Test
    public void testLogin() {
        try {

           UserType userType = userDao.getUserTypeByLogin("SuperAdmin");

            User user = null;
            if (userType.equals(UserType.ADMIN)) {
                user = adminDao.getByLogin("SuperAdmin");
            }

            Admin adminFromDB = adminDao.getById(1);

            assertEquals(adminFromDB, user);

        } catch (RuntimeException e) {
            fail();
        }
    }

    @Test
    public void testSetSession() {
        try {

            Admin adminFromDB = adminDao.getById(1);

            userDao.setSession(adminFromDB.getId(),"uuid");

        } catch (RuntimeException e) {
            fail();
        }
    }

    @Test
    public void testEndSession() {
        try {

            Admin adminFromDB = adminDao.getById(1);

            userDao.setSession(adminFromDB.getId(),"uuid");

            userDao.endSession("uuid");

        } catch (RuntimeException e) {
            fail();
        }
    }

    @Test
    public void getUserTypeBySession() {
        try {
            Admin adminFromDB = adminDao.getById(1);

            userDao.setSession(adminFromDB.getId(),"uuid");

            UserType userType = userDao.getUserTypeBySession("uuid");

            assertEquals(UserType.ADMIN, userType);

        } catch (RuntimeException e) {
            fail();
        }
    }

    @Test
    public void getIDBySession() {
        try {
            Admin adminFromDB = adminDao.getById(1);

            userDao.setSession(adminFromDB.getId(),"uuid");

            int id = userDao.getIdBySession("uuid");

            assertEquals(adminFromDB.getId(), id);

        } catch (RuntimeException e) {
            fail();
        }
    }
}

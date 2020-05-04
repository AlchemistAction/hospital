package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.UserType;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestAdminOperations extends TestBase {

    @Test
    public void testInsertAdmin() {
        try {
            Admin admin = insertAdmin(UserType.ADMIN, "name", "surname",
                    "patronymic", "adminLogin", "adminPass", "regularAdmin");
            Admin adminFromDB = adminDao.getById(admin.getId());
            assertEquals(admin, adminFromDB);
        } catch (RuntimeException e) {
            fail();
        }
    }

    @Test(expected = RuntimeException.class)
    public void testInsertAdminWithNullFirstName() {
        Admin admin = new Admin(UserType.ADMIN, null, "surname", "patronymic",
                "adminLogin", "adminPass", "admin");
        adminDao.insert(admin);
    }

    @Test
    public void testUpdateAdmin() {
        try {
            Admin admin = insertAdmin(UserType.ADMIN, "name", "surname",
                    "patronymic", "adminLogin", "adminPass", "regularAdmin");

            admin.setFirstName("newName");
            admin.setLastName("newLastName");
            admin.setPatronymic("newPatronymic");
            admin.setPassword("newPassword");
            admin.setPosition("NormAdmin");

            Admin adminFromDB = adminDao.update(admin);
            assertEquals(admin, adminFromDB);
        } catch (RuntimeException e) {
            fail();
        }
    }

    @Test
    public void testDeleteAdmin() {
        try {
            Admin admin = insertAdmin(UserType.ADMIN, "name", "surname",
                    "patronymic", "adminLogin", "adminPass", "regularAdmin");
            userDao.delete(admin);
            Admin adminFromDB = adminDao.getById(admin.getId());
            assertNull(adminFromDB);
        } catch (RuntimeException e) {
            fail();
        }
    }
}



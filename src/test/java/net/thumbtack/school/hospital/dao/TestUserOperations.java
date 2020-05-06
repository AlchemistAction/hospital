package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.LoginVerificator;
import net.thumbtack.school.hospital.model.User;
import net.thumbtack.school.hospital.model.UserType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestUserOperations extends TestBase {

    @Test
    public void testLogin() {
        try {

            LoginVerificator loginVerificator = userDao.getByLogin("SuperAdmin");

            User user = null;
            if (loginVerificator.getUserType().equals(UserType.ADMIN)) {
                user = adminDao.getById(loginVerificator.getId());
            }

            Admin adminFromDB = adminDao.getById(1);

            assertEquals(adminFromDB, user);

        } catch (RuntimeException e) {
            fail();
        }
    }
}

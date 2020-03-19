package net.thumbtack.school.hospital.daoTest;

import net.thumbtack.school.hospital.model.Admin;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestAdminOperations extends TestBase {

    @Test
    public void testInsertAdmin() {
        try {
            Admin admin1 = insertAdmin("admin", "name1", "sename1",
                    "patronimic1", "adminLogin", "adminPass", "regularAdmin");
            Admin admin1FromDB = adminDao.getById(admin1.getId());
            assertEquals(admin1, admin1FromDB);
        } catch (RuntimeException e) {
            fail();
        }
    }

    @Test(expected = RuntimeException.class)
    public void testInsertAdminWithNullFirstName() {
        Admin admin1 = new Admin("admin", null, "sename1", "patronimic1",
                "adminLogin", "adminPass", "admin");
        adminDao.insert(admin1);
    }

}



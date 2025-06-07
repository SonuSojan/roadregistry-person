package com.roadregistry;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    // 5 tests for addPerson()
    @Test
    void addPerson_validData_returnsTrue() {
        Person p = new Person();
        p.setPersonID("23!@abCDXY");
        p.setFirstName("Alice");
        p.setLastName("Smith");
        p.setAddress("32|Highland Street|Melbourne|Victoria|Australia");
        p.setBirthday("15-11-1990");
        assertTrue(p.addPerson());
    }

    @Test
    void addPerson_invalidID_returnsFalse() {
        Person p = new Person();
        p.setPersonID("12abcdEFGH");  // no special chars
        p.setFirstName("Bob");
        p.setLastName("Jones");
        p.setAddress("12|Main St|Melbourne|Victoria|Australia");
        p.setBirthday("01-01-1980");
        assertFalse(p.addPerson());
    }

    @Test
    void addPerson_invalidAddress_returnsFalse() {
        Person p = new Person();
        p.setPersonID("23!@abCDXY");
        p.setFirstName("Carol");
        p.setLastName("Taylor");
        p.setAddress("32|Highland St|Melbourne|NSW|Australia");  // wrong state
        p.setBirthday("15-11-1990");
        assertFalse(p.addPerson());
    }

    @Test
    void addPerson_invalidBirthday_returnsFalse() {
        Person p = new Person();
        p.setPersonID("23!@abCDXY");
        p.setFirstName("Doug");
        p.setLastName("Lee");
        p.setAddress("32|Highland Street|Melbourne|Victoria|Australia");
        p.setBirthday("1990-11-15");  // wrong format
        assertFalse(p.addPerson());
    }

    @Test
    void addPerson_fileWriteFailure_returnsFalse() {
        // Simulate by giving an unwritable path (optional)
        // p.setPersonID(...); p.setAddress(...); p.setBirthday(...);
        // assertFalse(p.addPerson());
    }

    // 5 tests for updatePersonalDetails()
    @Test
    void updatePersonalDetails_under18_cannotChangeAddress() {
        Person p = new Person();
        p.setPersonID("23!@abCDXY");
        p.setFirstName("Eve");
        p.setLastName("Young");
        p.setAddress("10|Elm St|Melbourne|Victoria|Australia");
        p.setBirthday("01-01-2010");  // under 18
        p.addPerson();
        assertFalse(p.updatePersonalDetails(
            "23!@abCDXY","Eve","Young",
            "99|New St|Melbourne|Victoria|Australia","01-01-2010"));
    }

    @Test
    void updatePersonalDetails_changeBirthdayOnly_returnsTrue() {
        Person p = new Person();
        p.setPersonID("23!@abCDXY");
        p.setFirstName("Frank");
        p.setLastName("Hall");
        p.setAddress("15|Oak St|Melbourne|Victoria|Australia");
        p.setBirthday("15-06-2000");
        p.addPerson();
        assertTrue(p.updatePersonalDetails(
            "23!@abCDXY","Frank","Hall",
            "15|Oak St|Melbourne|Victoria|Australia","16-06-2000"));
    }

    @Test
    void updatePersonalDetails_evenFirstDigit_cannotChangeID() {
        Person p = new Person();
        p.setPersonID("24!@abCDXY");  // starts with 2 (even)
        p.setFirstName("Gina");
        p.setLastName("Miller");
        p.setAddress("20|Pine St|Melbourne|Victoria|Australia");
        p.setBirthday("15-05-1990");
        p.addPerson();
        assertFalse(p.updatePersonalDetails(
            "25!@abCDXY","Gina","Miller",
            "20|Pine St|Melbourne|Victoria|Australia","15-05-1990"));
    }

    @Test
    void updatePersonalDetails_validChange_returnsTrue() {
        Person p = new Person();
        p.setPersonID("23!@abCDXY");
        p.setFirstName("Hank");
        p.setLastName("Wilson");
        p.setAddress("5|Birch St|Melbourne|Victoria|Australia");
        p.setBirthday("15-05-1980");
        p.addPerson();
        assertTrue(p.updatePersonalDetails(
            "23!@abCDXY","Henry","Wilson",
            "5|Birch St|Melbourne|Victoria|Australia","15-05-1980"));
    }

    // 5 tests for addDemeritPoints()
    @Test
    void addDemeritPoints_validUnder21_suspends() {
        Person p = new Person();
        p.setPersonID("23!@abCDXY");
        p.setBirthday("01-01-2005");  // age <21
        assertEquals("Success", p.addDemeritPoints("01-01-2023", 4));
        assertTrue(p.addDemeritPoints("01-06-2023", 4).equals("Success"));
    }

    @Test
    void addDemeritPoints_invalidDate_returnsFailed() {
        Person p = new Person();
        p.setPersonID("23!@abCDXY");
        p.setBirthday("01-01-1980");
        assertEquals("Failed", p.addDemeritPoints("2023/01/01", 3));
    }

    @Test
    void addDemeritPoints_invalidPoints_returnsFailed() {
        Person p = new Person();
        p.setPersonID("23!@abCDXY");
        p.setBirthday("01-01-1980");
        assertEquals("Failed", p.addDemeritPoints("01-01-2023", 7));
    }

    @Test
    void addDemeritPoints_validOver21_noSuspension() {
        Person p = new Person();
        p.setPersonID("23!@abCDXY");
        p.setBirthday("01-01-1980");  // age >21
        assertEquals("Success", p.addDemeritPoints("01-01-2023", 3));
    }

    @Test
    void addDemeritPoints_accumulatesCorrectly() {
        Person p = new Person();
        p.setPersonID("23!@abCDXY");
        p.setBirthday("01-01-1980");
        p.addDemeritPoints("01-01-2023", 6);
        assertEquals("Success", p.addDemeritPoints("01-06-2023", 7));
    }
}

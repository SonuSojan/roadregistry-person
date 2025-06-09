package com.roadregistry;

public class main {
    public static void main(String[] args) {
        // Create a Person and add to persons.txt
        Person p = new Person();
        p.setPersonID("23!@abCDXY");
        p.setFirstName("Alice");
        p.setLastName("Smith");
        p.setAddress("32|Highland Street|Melbourne|Victoria|Australia");
        p.setBirthday("15-11-1990");

        boolean added = p.addPerson();
        System.out.println("Add Person Result: " + added);

        // Update the person's details
        boolean updated = p.updatePersonalDetails(
            "23!@abCDXY", // ID unchanged
            "Alicia",     // New first name
            "Smith",      // Last name
            "32|Highland Street|Melbourne|Victoria|Australia", // Same address
            "15-11-1990"  // Same birthday
        );
        System.out.println("Update Person Result: " + updated);

        // Add demerit points
        String result = p.addDemeritPoints("01-06-2024", 4);
        System.out.println("Add Demerit Points Result: " + result);
    }
}

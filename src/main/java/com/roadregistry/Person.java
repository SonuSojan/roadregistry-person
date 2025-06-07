package com.roadregistry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Pattern;

public class Person {
    private String personID;
    private String firstName;
    private String lastName;
    private String address;    // StreetNo|Street|City|State|Country
    private String birthday;   // DD-MM-YYYY
    private int demeritPoints;
    private boolean isSuspended;

    // ─── Getters & Setters ─────────────────────────────────────────
    public void setPersonID(String personID)   { this.personID = personID; }
    public void setFirstName(String firstName) { this.firstName  = firstName; }
    public void setLastName(String lastName)   { this.lastName   = lastName; }
    public void setAddress(String address)     { this.address    = address; }
    public void setBirthday(String birthday)   { this.birthday   = birthday; }
    public String getPersonID()                { return personID; }
    public String getBirthday()                { return birthday; }

    // ─── Activity 1: addPerson ──────────────────────────────────────
    public boolean addPerson() {
        if (!validatePersonID(personID)) return false;
        if (!validateAddress(address))   return false;
        if (!validateBirthday(birthday)) return false;

        String line = String.join(",", personID, firstName, lastName, address, birthday);
        try {
            Files.write(Paths.get("persons.txt"),
                        (line + System.lineSeparator()).getBytes(),
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // ─── Activity 2: updatePersonalDetails ──────────────────────────
    public boolean updatePersonalDetails(String newID,
                                         String newFirst,
                                         String newLast,
                                         String newAddress,
                                         String newBirthday) {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get("persons.txt"));
        } catch (IOException e) {
            return false;
        }

        boolean updated = false;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (!line.startsWith(personID + ",")) {
                continue;
            }

            LocalDate birthDate = LocalDate.parse(birthday,
                                    DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            int age = LocalDate.now().getYear() - birthDate.getYear();

            if (age < 18 && !newAddress.equals(address)) {
                return false;
            }
            if (!newBirthday.equals(birthday)
                && (!newID.equals(personID)
                    || !newFirst.equals(firstName)
                    || !newLast.equals(lastName)
                    || !newAddress.equals(address))) {
                return false;
            }
            char firstDigit = personID.charAt(0);
            if ((firstDigit - '0') % 2 == 0 && !newID.equals(personID)) {
                return false;
            }

            lines.set(i, String.join(",", newID, newFirst, newLast, newAddress, newBirthday));
            this.personID  = newID;
            this.firstName = newFirst;
            this.lastName  = newLast;
            this.address   = newAddress;
            this.birthday  = newBirthday;
            updated = true;
            break;
        }

        if (!updated) {
            return false;
        }

        try {
            Files.write(Paths.get("persons.txt"),
                        lines,
                        StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // ─── Activity 3: addDemeritPoints ───────────────────────────────
    public String addDemeritPoints(String offenseDate, int points) {
        // 1. Validate date format
        if (!validateBirthday(offenseDate)) {
            return "Failed";
        }

        // 2. Calculate age & threshold
        LocalDate birth = LocalDate.parse(birthday,
                            DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        int age = LocalDate.now().getYear() - birth.getYear();
        int threshold = (age < 21) ? 6 : 12;

        // 3. Points range check
        if (points < 1 || (points > 6 && demeritPoints + points <= threshold)) {
            return "Failed";
        }

        // 4. Apply points & suspension
        demeritPoints += points;
        if (demeritPoints > threshold) {
            isSuspended = true;
        }
        return "Success";
    }

    // ─── Validation Helpers ─────────────────────────────────────────
    private boolean validatePersonID(String id) {
        String pattern = "^[2-9]{2}(?=(?:.*\\W){2,}.*).{6}[A-Z]{2}$";
        return Pattern.matches(pattern, id);
    }
    private boolean validateAddress(String addr) {
        String[] parts = addr.split("\\|");
        return parts.length == 5 && "Victoria".equals(parts[3]);
    }
    private boolean validateBirthday(String bday) {
        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate.parse(bday, fmt);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}

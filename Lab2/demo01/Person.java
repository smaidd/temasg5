package demo01;

import org.springframework.stereotype.Component;

@Component
public class Person {
    private String firstName;
    private String lastname;
    private char gender;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(@Regex("[A-Z][a-z]+") String firstName) {
        this.firstName = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(@Regex("[A-Z][a-z]+") String lastname) {
        this.lastname = lastname;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(@Regex("[mMfF]") char gender) {
        this.gender = gender;
    }
}

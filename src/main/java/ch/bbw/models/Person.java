package ch.bbw.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

public class Person {

    @NotNull
    @Size(min = 2, max = 30)
    private String name;

    @NotNull
    @Size(min = 2, max = 30)
    private String surname;

    @NotNull
    @Email(message = "falsch")
    private String email;

    private String city;

    private String[] allCities = {"Zurich", "Basel", "Bern", "Lugano", "Locarno", "Frauenfeld"};

    private String zip;

    private String[] allZips = {"9000", "8032", "8053", "8004", "8001", "8008"};

    @NotNull
    private String street;

    @NotNull
    @Past(message="muss in der Verganganheit sein")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date birthday;

    private String cardType;

    private String[] allTypes = {"Visa", "MasterCard", "Maestro", "PostFinance"};

    @NotNull
    @Size(min = 2, max = 30)
    private String cardholder;

    @NotNull
    @Size(min = 16, max = 17)
    private String cardNumber;

    private String month;

    private String[] allMonths = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

    private String year;

    private String[] allYears = {"22", "23", "24", "25", "26", "27"};

    @NotNull
    @Size(min = 3, max = 4)
    private String cvv;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String[] getAllCities() {
        return allCities;
    }

    public void setAllCities(String[] allCities) {
        this.allCities = allCities;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String[] getAllZips() {
        return allZips;
    }

    public void setAllZips(String[] allZips) {
        this.allZips = allZips;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String[] getAllTypes() {
        return allTypes;
    }

    public void setAllTypes(String[] allTypes) {
        this.allTypes = allTypes;
    }

    public String getCardholder() {
        return cardholder;
    }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String[] getAllMonths() {
        return allMonths;
    }

    public void setAllMonths(String[] allMonths) {
        this.allMonths = allMonths;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String[] getAllYears() {
        return allYears;
    }

    public void setAllYears(String[] allYears) {
        this.allYears = allYears;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}

package com.officemanagementsystemapi.test;


public class TestSmallThings {
    public static void main(String[] args) {

        String phoneNumber = "0743010380";

        System.out.println(phoneNumber.matches("^(\\+\\d{1,2}\\s?)?1?\\-?\\.?\\s?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$"));
    }
}

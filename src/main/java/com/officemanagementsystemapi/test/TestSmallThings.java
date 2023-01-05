package com.officemanagementsystemapi.test;


import java.util.Set;

public class TestSmallThings {
    public static void main(String[] args) {

        Set<String> roles = Set.of("ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER");
        Set<String> rolesToCOmpare = Set.of("ROLE_ADMIN", "ROLE_USER", "ROLE_STAFF");

        rolesToCOmpare.forEach(roleToCompare -> {
            if (!roles.contains(roleToCompare)) try {
                throw new Exception("Doesn't contain");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

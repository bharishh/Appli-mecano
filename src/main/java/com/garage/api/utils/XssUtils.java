package com.garage.api.utils;

import org.springframework.stereotype.Component;

@Component
public class XssUtils {

   
    public static String sanitize(String input) {
        if (input == null) return null;
        return input
            .replaceAll("<[^>]*>", "")           // supprime toutes les balises HTML
            .replaceAll("javascript:", "")         // supprime javascript:
            .replaceAll("on\\w+\\s*=", "")         // supprime onload=, onclick=...
            .replaceAll("&lt;", "")
            .replaceAll("&gt;", "")
            .trim();
    }

  
    public static boolean isValidName(String input) {
        if (input == null) return false;
        return input.matches("^[a-zA-ZÀ-ÿ\\s'-]{2,100}$");
    }

    
    public static boolean isValidEmail(String input) {
        if (input == null) return false;
        return input.matches("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");
    }

 
    public static boolean isValidPhone(String input) {
        if (input == null) return true; 
        return input.matches("^[0-9+\\s()-]{10,20}$");
    }

   
    public static boolean isValidImmatriculation(String input) {
        if (input == null) return false;
        return input.matches("^[A-Z]{2}-[0-9]{3}-[A-Z]{2}$");
    }
}
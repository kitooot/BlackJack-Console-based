import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Utility class that manages persistence of user profiles (username, password, balance).
 */
public class SaveSystem {

    public static class ProfileData {
        private final String username;
        private final String password;
        private final int balance;

        public ProfileData(String username, String password, int balance) {
            this.username = username;
            this.password = password;
            this.balance = balance;
        }

        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public int getBalance() { return balance; }
    }

    public static void saveProfile(String username, String password, int balance) {
        String safePassword = password == null ? "" : password;
        String hashed = hashPassword(safePassword);
        try (PrintWriter writer = new PrintWriter(new FileWriter(username + ".txt"))) {
            writer.println(hashed);
            writer.println(balance);
        } catch (IOException e) {
            System.out.println("Error saving profile for " + username + ": " + e.getMessage());
        }
    }

    public static void saveProfileHashed(String username, String hashedPassword, int balance) {
        String safeHash = hashedPassword == null ? "" : hashedPassword;
        try (PrintWriter writer = new PrintWriter(new FileWriter(username + ".txt"))) {
            writer.println(safeHash);
            writer.println(balance);
        } catch (IOException e) {
            System.out.println("Error saving profile for " + username + ": " + e.getMessage());
        }
    }

    public static void saveBalance(String username, int balance) {
        ProfileData data = loadProfile(username);
        if (data == null) {
            saveProfile(username, "", balance);
        } else {
            saveProfileHashed(username, data.getPassword(), balance);
        }
    }

    public static ProfileData loadProfile(String username) {
        File file = new File(username + ".txt");
        if(!file.exists()) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String firstLine = reader.readLine();
            if(firstLine == null) {
                return new ProfileData(username, "", 500);
            }

            String secondLine = reader.readLine();
            if(secondLine == null) {
                try {
                    int legacyBalance = Integer.parseInt(firstLine.trim());
                    return new ProfileData(username, "", legacyBalance);
                } catch (NumberFormatException e) {
                    return new ProfileData(username, firstLine, 500);
                }
            }

            int balance;
            try {
                balance = Integer.parseInt(secondLine.trim());
            } catch (NumberFormatException e) {
                balance = 500;
            }
            return new ProfileData(username, firstLine, balance);
        } catch (IOException e) {
            System.out.println("Error loading profile for " + username + ": " + e.getMessage());
            return null;
        }
    }

    public static int loadBalance(String username) {
        ProfileData data = loadProfile(username);
        if(data == null) {
            System.out.println("Profile not found or corrupted. Using default balance for " + username + ".");
            return 500;
        }
        return data.getBalance();
    }

    public static String hashPassword(String password) {
        if(password == null) return "";
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean verifyPassword(String rawPassword, String storedHash) {
        if(storedHash == null) return rawPassword == null || rawPassword.isEmpty();
        String h = hashPassword(rawPassword == null ? "" : rawPassword);
        return h.equals(storedHash);
    }

    public static boolean profileExists(String username) {
        return new File(username + ".txt").exists();
    }

    public static boolean deleteProfile(String username) {
        File file = new File(username + ".txt");
        if(file.exists()) {
            return file.delete();
        } else {
            System.out.println("No profile found for " + username + ".");
            return false;
        }
    }
}

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SaveSystem {

    public static class ProfileData {
        public final String username;
        public final String password;
        public final int balance;

        public ProfileData(String username, String password, int balance) {
            this.username = username;
            this.password = password;
            this.balance = balance;
        }
    }

    public static void saveProfile(String username, String password, int balance) {
        String safePassword = password == null ? "" : password;
        try (PrintWriter writer = new PrintWriter(new FileWriter(username + ".txt"))) {
            writer.println(safePassword);
            writer.println(balance);
        } catch (IOException e) {
            System.out.println("Error saving profile.");
        }
    }

    public static void saveBalance(String username, int balance) {
        ProfileData data = loadProfile(username);
        String password = data == null ? "" : data.password;
        saveProfile(username, password, balance);
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
            return null;
        }
    }

    public static int loadBalance(String username) {
        ProfileData data = loadProfile(username);
        if(data == null) {
            return 500;
        }
        return data.balance;
    }

    public static boolean profileExists(String username) {
        return new File(username + ".txt").exists();
    }

    public static boolean deleteProfile(String username) {
        File file = new File(username + ".txt");
        if(file.exists()) {
            return file.delete();
        } else {
            return false;
        }
    }
}

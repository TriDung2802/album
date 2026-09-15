package murach.data;

import java.io.*;
import murach.business.User;

public class UserIO {

    public static User getUser(String email, String path) {

        try {
            BufferedReader in = new BufferedReader(
                    new FileReader(path));

            String line;

            while ((line = in.readLine()) != null) {

                String[] data = line.split("\\|");

                if (data[0].equals(email)) {

                    User user = new User();

                    user.setEmail(data[0]);
                    user.setFirstName(data[1]);
                    user.setLastName(data[2]);

                    in.close();

                    return user;
                }
            }

            in.close();

        } catch (IOException e) {
            System.out.println(e);
        }

        return null;
    }


    public static void add(User user, String path) {

        try {
            PrintWriter out = new PrintWriter(
                    new FileWriter(path, true));

            out.println(
                    user.getEmail() + "|" +
                    user.getFirstName() + "|" +
                    user.getLastName()
            );

            out.close();

        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
import java.io.*;
import java.util.ArrayList;

public class FileManager {

    private static final String DATA_FOLDER = "data";
    private static final String FILE_NAME = "books.dat";
    private static final File FILE =
            new File(DATA_FOLDER, FILE_NAME);

    static {

        File directory = new File(DATA_FOLDER);

        if (!directory.exists()) {
            directory.mkdirs();
        }

    }

    public static void saveBooks(ArrayList<Book> books) {

        try (ObjectOutputStream output =
                     new ObjectOutputStream(
                             new FileOutputStream(FILE))) {

            output.writeObject(books);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Book> loadBooks() {

        if (!FILE.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream input =
                     new ObjectInputStream(
                             new FileInputStream(FILE))) {

            return (ArrayList<Book>) input.readObject();

        } catch (IOException | ClassNotFoundException e) {

            e.printStackTrace();

            return new ArrayList<>();

        }

    }

}
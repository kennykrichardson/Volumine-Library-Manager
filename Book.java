import java.io.Serializable;

public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    private static int nextId = 1;

    private final int id;

    private String title;
    private String author;
    private String genre;
    private int year;
    private String isbn;

    private boolean borrowed;

    public Book(String title,
                String author,
                String genre,
                int year,
                String isbn) {

        this.id = nextId++;

        this.title = title;
        this.author = author;
        this.genre = genre;
        this.year = year;
        this.isbn = isbn;

        this.borrowed = false;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public boolean isBorrowed() {
        return borrowed;
    }

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }

    public static void updateNextId(int id) {

        if (id >= nextId) {
            nextId = id + 1;
        }

    }

    @Override
    public String toString() {
        return title + " - " + author;
    }

}
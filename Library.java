import java.util.ArrayList;
import java.util.List;

public class Library {

    private ArrayList<Book> books;

    public Library() {
        books = new ArrayList<>();
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Book> books) {

        this.books = books;

        for (Book book : books) {
            Book.updateNextId(book.getId());
        }
    }

    // -------------------------
    // Add Book
    // -------------------------

    public boolean addBook(Book book) {

        if (isbnExists(book.getIsbn())) {
            return false;
        }

        books.add(book);
        return true;
    }

    // -------------------------
    // Delete
    // -------------------------

    public boolean removeBook(int id) {

        return books.removeIf(book -> book.getId() == id);

    }

    // -------------------------
    // Find by ID
    // -------------------------

    public Book getBook(int id) {

        for (Book book : books) {

            if (book.getId() == id) {
                return book;
            }

        }

        return null;

    }

    // -------------------------
    // Borrow / Return
    // -------------------------

    public boolean toggleBorrow(int id) {

        Book book = getBook(id);

        if (book == null)
            return false;

        book.setBorrowed(!book.isBorrowed());

        return true;

    }

    // -------------------------
    // Edit
    // -------------------------

    public boolean editBook(int id,
                            String title,
                            String author,
                            String genre,
                            int year,
                            String isbn) {

        Book book = getBook(id);

        if (book == null)
            return false;

        if (!book.getIsbn().equalsIgnoreCase(isbn)
                && isbnExists(isbn))
            return false;

        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);
        book.setYear(year);
        book.setIsbn(isbn);

        return true;

    }

    // -------------------------
    // Search
    // -------------------------

    public List<Book> search(String keyword) {

        ArrayList<Book> results = new ArrayList<>();

        keyword = keyword.toLowerCase();

        for (Book book : books) {

            if (book.getTitle().toLowerCase().contains(keyword)
                    || book.getAuthor().toLowerCase().contains(keyword)
                    || book.getGenre().toLowerCase().contains(keyword)) {

                results.add(book);

            }

        }

        return results;

    }

    // -------------------------
    // Duplicate ISBN
    // -------------------------

    public boolean isbnExists(String isbn) {

        for (Book book : books) {

            if (book.getIsbn().equalsIgnoreCase(isbn))
                return true;

        }

        return false;

    }

    // -------------------------
    // Statistics
    // -------------------------

    public int getTotalBooks() {
        return books.size();
    }

    public int getBorrowedBooks() {

        int count = 0;

        for (Book book : books) {

            if (book.isBorrowed())
                count++;

        }

        return count;

    }

    public int getAvailableBooks() {

        return getTotalBooks() - getBorrowedBooks();

    }

}
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class LibraryGUI extends JFrame {

    private final Library library;

    private JTable table;
    private DefaultTableModel model;

    private JTextField searchField;

    private JLabel totalLabel;
    private JLabel availableLabel;
    private JLabel borrowedLabel;

    public LibraryGUI() {

        library = new Library();
        library.setBooks(FileManager.loadBooks());

        initializeFrame();
        createMenuBar();
        createSearchPanel();
        createTable();
        createBottomPanel();

        refreshTable();

        setVisible(true);
    }

    private void initializeFrame() {

        setTitle("Volumine");
        setSize(1000,650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout(10,10));

        ((JComponent)getContentPane())
                .setBorder(new EmptyBorder(10,10,10,10));

    }

    private void createMenuBar() {

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenu helpMenu = new JMenu("Help");

        JMenuItem refreshItem = new JMenuItem("Refresh");
        JMenuItem exitItem = new JMenuItem("Exit");
        JMenuItem aboutItem = new JMenuItem("About");

        refreshItem.addActionListener(e -> refreshTable());

        exitItem.addActionListener(e -> dispose());

        aboutItem.addActionListener(e ->
                JOptionPane.showMessageDialog(
                        this,
                        "Volumine\nVersion 1.0\n\nA Java Swing Library Manager\n\nCreated by Kenny Richardson",
                        "About Volumine",
                        JOptionPane.INFORMATION_MESSAGE));

        fileMenu.add(refreshItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

    }

    private void createSearchPanel() {

        JPanel panel = new JPanel(new BorderLayout(10,10));

        JLabel label = new JLabel("Search");

        searchField = new JTextField();

        JButton searchButton = new JButton("Search");

        JButton clearButton = new JButton("Clear");

        searchButton.addActionListener(e -> searchBooks());

        clearButton.addActionListener(e -> {

            searchField.setText("");

            refreshTable();

        });

        panel.add(label, BorderLayout.WEST);
        panel.add(searchField, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        buttons.add(searchButton);
        buttons.add(clearButton);

        panel.add(buttons, BorderLayout.EAST);

        add(panel, BorderLayout.NORTH);

    }

    private void createTable() {

        model = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Title",
                        "Author",
                        "Genre",
                        "Year",
                        "Status"
                }, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };

        table = new JTable(model);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(28);
        table.getTableHeader().setReorderingAllowed(false);
        table.setFillsViewportHeight(true);

        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 2) {
                    editBookDialog();
                }

            }

        });

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

    }

    private void createBottomPanel() {

        JPanel container = new JPanel(new BorderLayout());

        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        totalLabel = new JLabel("Books: 0");
        availableLabel = new JLabel("Available: 0");
        borrowedLabel = new JLabel("Borrowed: 0");

        statsPanel.add(totalLabel);
        statsPanel.add(Box.createHorizontalStrut(20));
        statsPanel.add(availableLabel);
        statsPanel.add(Box.createHorizontalStrut(20));
        statsPanel.add(borrowedLabel);

        container.add(statsPanel, BorderLayout.NORTH);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addButton = new JButton("Add Book");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton borrowButton = new JButton("Borrow / Return");

        addButton.setPreferredSize(new Dimension(130, 35));
        editButton.setPreferredSize(new Dimension(110, 35));
        deleteButton.setPreferredSize(new Dimension(110, 35));
        borrowButton.setPreferredSize(new Dimension(150, 35));

        addButton.addActionListener(e -> addBookDialog());
        editButton.addActionListener(e -> editBookDialog());
        deleteButton.addActionListener(e -> deleteSelectedBook());
        borrowButton.addActionListener(e -> toggleBorrow());

        actionPanel.add(addButton);
        actionPanel.add(editButton);
        actionPanel.add(deleteButton);
        actionPanel.add(borrowButton);

        container.add(actionPanel, BorderLayout.SOUTH);

        add(container, BorderLayout.SOUTH);

    }

    private void addBookDialog() {

        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField genreField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField isbnField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        panel.add(new JLabel("Title"));
        panel.add(titleField);

        panel.add(new JLabel("Author"));
        panel.add(authorField);

        panel.add(new JLabel("Genre"));
        panel.add(genreField);

        panel.add(new JLabel("Publication Year"));
        panel.add(yearField);

        panel.add(new JLabel("ISBN"));
        panel.add(isbnField);

        int option = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Add New Book",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (option != JOptionPane.OK_OPTION)
            return;

        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String genre = genreField.getText().trim();
        String isbn = isbnField.getText().trim();

        if (title.isEmpty() ||
                author.isEmpty() ||
                genre.isEmpty() ||
                isbn.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please complete every field.");

            return;
        }

        int year;

        try {

            year = Integer.parseInt(yearField.getText().trim());

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Publication year must be a number.");

            return;

        }

        Book book = new Book(
                title,
                author,
                genre,
                year,
                isbn
        );

        if (!library.addBook(book)) {

            JOptionPane.showMessageDialog(
                    this,
                    "A book with this ISBN already exists.");

            return;

        }

        FileManager.saveBooks(library.getBooks());

        refreshTable();

        JOptionPane.showMessageDialog(
                this,
                "Book added successfully.");

    }
    private void editBookDialog() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a book to edit.");

            return;

        }

        int id = (Integer) model.getValueAt(selectedRow, 0);

        Book book = library.getBook(id);

        if (book == null) {
            return;
        }

        JTextField titleField = new JTextField(book.getTitle());
        JTextField authorField = new JTextField(book.getAuthor());
        JTextField genreField = new JTextField(book.getGenre());
        JTextField yearField = new JTextField(String.valueOf(book.getYear()));
        JTextField isbnField = new JTextField(book.getIsbn());

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        panel.add(new JLabel("Title"));
        panel.add(titleField);

        panel.add(new JLabel("Author"));
        panel.add(authorField);

        panel.add(new JLabel("Genre"));
        panel.add(genreField);

        panel.add(new JLabel("Publication Year"));
        panel.add(yearField);

        panel.add(new JLabel("ISBN"));
        panel.add(isbnField);

        int option = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Edit Book",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (option != JOptionPane.OK_OPTION)
            return;

        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String genre = genreField.getText().trim();
        String isbn = isbnField.getText().trim();

        if (title.isEmpty() ||
                author.isEmpty() ||
                genre.isEmpty() ||
                isbn.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please complete every field.");

            return;

        }

        int year;

        try {

            year = Integer.parseInt(yearField.getText().trim());

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Publication year must be a number.");

            return;

        }

        if (!library.editBook(
                id,
                title,
                author,
                genre,
                year,
                isbn)) {

            JOptionPane.showMessageDialog(
                    this,
                    "A book with this ISBN already exists.");

            return;

        }

        FileManager.saveBooks(library.getBooks());

        refreshTable();

        JOptionPane.showMessageDialog(
                this,
                "Book updated successfully.");

    }

    private void deleteSelectedBook() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a book to delete.");

            return;

        }

        int id = (Integer) model.getValueAt(selectedRow, 0);

        Book book = library.getBook(id);

        if (book == null)
            return;

        int option = JOptionPane.showConfirmDialog(
                this,
                "Delete \"" + book.getTitle() + "\"?",
                "Delete Book",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (option != JOptionPane.YES_OPTION)
            return;

        library.removeBook(id);

        FileManager.saveBooks(library.getBooks());

        refreshTable();

        JOptionPane.showMessageDialog(
                this,
                "Book deleted successfully.");

    }

    private void toggleBorrow() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a book.");

            return;

        }

        int id = (Integer) model.getValueAt(selectedRow, 0);

        Book book = library.getBook(id);

        if (book == null)
            return;

        library.toggleBorrow(id);

        FileManager.saveBooks(library.getBooks());

        refreshTable();

        String message = book.isBorrowed()
                ? "Book borrowed."
                : "Book returned.";

        JOptionPane.showMessageDialog(
                this,
                message);

    }

    private void searchBooks() {

        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {

            refreshTable();
            return;

        }

        List<Book> results = library.search(keyword);

        model.setRowCount(0);

        for (Book book : results) {

            model.addRow(new Object[]{
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getGenre(),
                    book.getYear(),
                    book.isBorrowed() ? "Borrowed" : "Available"
            });

        }

        totalLabel.setText("Books: " + results.size());
        availableLabel.setText("");
        borrowedLabel.setText("");

    }

    private void refreshTable() {

        model.setRowCount(0);

        for (Book book : library.getBooks()) {

            model.addRow(new Object[]{
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getGenre(),
                    book.getYear(),
                    book.isBorrowed() ? "Borrowed" : "Available"
            });

        }

        totalLabel.setText("Books: " + library.getTotalBooks());
        availableLabel.setText("Available: " + library.getAvailableBooks());
        borrowedLabel.setText("Borrowed: " + library.getBorrowedBooks());

    }

}

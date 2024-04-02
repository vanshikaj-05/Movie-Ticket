import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class User {
    private String username;
    private String password;
    private ArrayList<String> bookingHistory;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.bookingHistory = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void addBooking(String booking) {
        bookingHistory.add(booking);
    }

    public ArrayList<String> getBookingHistory() {
        return bookingHistory;
    }
}

class Movie {
    private String title;
    private int availableSeats;
    private HashMap<String, Boolean> seats;

    public Movie(String title, int totalSeats) {
        this.title = title;
        this.availableSeats = totalSeats;
        this.seats = new HashMap<>();
        for (int i = 1; i <= totalSeats; i++) {
            seats.put(String.valueOf(i), true); // true means available, false means booked
        }
    }

    public String getTitle() {
        return title;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public HashMap<String, Boolean> getSeats() {
        return seats;
    }

    public void bookSeat(String seat) {
        seats.put(seat, false);
        availableSeats--;
    }
}

class Admin {
    private String username;
    private String password;

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}

public class MovieTicketBookingSystem {
    private ArrayList<User> users;
    private ArrayList<Movie> movies;
    private Admin admin;
    private Scanner scanner;

    public MovieTicketBookingSystem() {
        users = new ArrayList<>();
        movies = new ArrayList<>();
        admin = new Admin("admin", "admin123");
        scanner = new Scanner(System.in);

        // Adding some sample movies
        movies.add(new Movie("Avengers: Endgame", 100));
        movies.add(new Movie("Joker", 80));
        movies.add(new Movie("The Lion King", 120));
    }

    public void register() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        users.add(new User(username, password));
        System.out.println("Registration successful.");
    }

    public User login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.checkPassword(password)) {
                System.out.println("Login successful.");
                return user;
            }
        }
        System.out.println("Invalid username or password.");
        return null;
    }

    public void displayMovies() {
        System.out.println("Available Movies:");
        for (int i = 0; i < movies.size(); i++) {
            System.out.println((i + 1) + ". " + movies.get(i).getTitle());
        }
    }

    public void bookTicket(User user) {
        displayMovies();
        System.out.print("Enter movie number: ");
        int movieIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline
        if (movieIndex < 0 || movieIndex >= movies.size()) {
            System.out.println("Invalid movie number.");
            return;
        }
        Movie selectedMovie = movies.get(movieIndex);
        System.out.println("Available Seats for " + selectedMovie.getTitle() + ":");
        HashMap<String, Boolean> seats = selectedMovie.getSeats();
        for (String seat : seats.keySet()) {
            if (seats.get(seat)) {
                System.out.print(seat + " ");
            }
        }
        System.out.println();
        System.out.print("Enter seat number: ");
        String seatNumber = scanner.nextLine();
        if (!seats.containsKey(seatNumber) || !seats.get(seatNumber)) {
            System.out.println("Invalid seat number or seat already booked.");
            return;
        }
        selectedMovie.bookSeat(seatNumber);
        user.addBooking(selectedMovie.getTitle() + " - Seat: " + seatNumber);
        System.out.println("Ticket booked successfully.");
    }

    public void viewBookingHistory(User user) {
        ArrayList<String> bookingHistory = user.getBookingHistory();
        System.out.println("Booking History:");
        for (String booking : bookingHistory) {
            System.out.println(booking);
        }
    }

    public void adminDashboard() {
        System.out.print("Enter admin username: ");
        String username = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();
        if (admin.authenticate(username, password)) {
            System.out.println("Admin login successful.");
            System.out.println("Total Movies: " + movies.size());
            for (Movie movie : movies) {
                System.out.println(movie.getTitle() + " - Available Seats: " + movie.getAvailableSeats());
            }
        } else {
            System.out.println("Invalid admin credentials.");
        }
    }

    public static void main(String[] args) {
        MovieTicketBookingSystem bookingSystem = new MovieTicketBookingSystem();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("Movie Ticket Booking System");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Admin Dashboard");
            System.out.println("4. Quit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    bookingSystem.register();
                    break;
                case 2:
                    User user = bookingSystem.login();
                    if (user != null) {
                        int userChoice;
                        do {
                            System.out.println("User Menu:");
                            System.out.println("1. Book Ticket");
                            System.out.println("2. View Booking History");
                            System.out.println("3. Logout");
                            System.out.print("Enter your choice: ");
                            userChoice = scanner.nextInt();
                            scanner.nextLine(); // Consume newline

                            switch (userChoice) {
                                case 1:
                                    bookingSystem.bookTicket(user);
                                    break;
                                case 2:
                                    bookingSystem.viewBookingHistory(user);
                                    break;
                                case 3:
                                    System.out.println("Logged out successfully.");
                                    break;
                                default:
                                    System.out.println("Invalid choice. Please try again.");
                            }
                        } while (userChoice != 3);
                    }
                    break;
                case 3:
                    bookingSystem.adminDashboard();
                    break;
                case 4:
                    System.out.println("Thank you for using the Movie Ticket Booking System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 4

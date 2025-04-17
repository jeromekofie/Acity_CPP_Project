import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class FineCalculator {
    private static final double DAILY_FINE_RATE = 2.0; // $2 per day
    
    public static double calculateFine(LocalDate dueDate, LocalDate returnDate) {
        if (returnDate.isAfter(dueDate)) {
            long daysOverdue = ChronoUnit.DAYS.between(dueDate, returnDate);
            return daysOverdue * DAILY_FINE_RATE;
        }
        return 0.0;
    }
    
    public static double calculateProjectedFine(LocalDate dueDate) {
        LocalDate today = LocalDate.now();
        if (today.isAfter(dueDate)) {
            long daysOverdue = ChronoUnit.DAYS.between(dueDate, today);
            return daysOverdue * DAILY_FINE_RATE;
        }
        return 0.0;
    }
}
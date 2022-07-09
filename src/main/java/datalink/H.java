package datalink;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.Temporal;

/**
 * Helper class.
 *
 * @author Saleh
 */
public class H {

    public static YearMonth getYearMonth(Temporal temporal) {
        YearMonth ym = null;
        if (temporal instanceof YearMonth) {
            return (YearMonth) temporal;
        } else if (temporal instanceof LocalDate) {
            LocalDate ld = (LocalDate) temporal;
            ym = YearMonth.of(ld.getYear(), ld.getMonth());
        }
        return ym;
    }
}

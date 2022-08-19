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

    public static LocalDate getYearMonth1st(Temporal temporal) {
        if (temporal == null) {
            return null;
        }
        LocalDate ldt = null;
        if (temporal instanceof LocalDate) {
            LocalDate ldtTemp = (LocalDate) temporal;
            ldt = LocalDate.of(ldtTemp.getYear(), ldtTemp.getMonth(), 1);
        } else if (temporal instanceof YearMonth) {
            YearMonth ymTemp = (YearMonth) temporal;
            ldt = LocalDate.of(ymTemp.getYear(), ymTemp.getMonth(), 1);
        }
        return ldt;
    }
}

package gui.salary;

import java.math.BigDecimal;
import java.time.YearMonth;

/**
 *
 * @author Saleh
 */
public interface ComputeListener {

    public void computed(BigDecimal amount, YearMonth yearMonthSubjectOfCompution);
}

package gui.salary;

import java.time.YearMonth;

/**
 *
 * @author Saleh
 */
public interface SubjectDateChangeListener {

    public void yearOrMonthChanged(YearMonth yearMonth);

    public void yearAndMonthNotChanged(YearMonth yearMonth);

}

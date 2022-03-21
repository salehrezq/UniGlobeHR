package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

/**
 *
 * @author Saleh
 */
public class SalaryAdvance {

    private int id;
    private int employeeId;
    private YearMonth yearMonthSubject;
    private LocalDate dateTaken;
    private BigDecimal amount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public YearMonth getYearMonthSubject() {
        return yearMonthSubject;
    }

    public void setYearMonthSubject(YearMonth yearMonthSubject) {
        this.yearMonthSubject = yearMonthSubject;
    }

    public LocalDate getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(LocalDate dateTaken) {
        this.dateTaken = dateTaken;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}

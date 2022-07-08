package model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author Saleh
 */
public class SalaryAdvance {

    private int id;
    private int employeeId;
    private LocalDate yearMonthSubject;
    private LocalDate dateGiven;
    private BigDecimal amount;
    private boolean locked;

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

    public LocalDate getYearMonthSubject() {
        return yearMonthSubject;
    }

    public void setYearMonthSubject(LocalDate yearMonthSubject) {
        this.yearMonthSubject = yearMonthSubject;
    }

    public LocalDate getDateGiven() {
        return dateGiven;
    }

    public void setDateGiven(LocalDate dateGiven) {
        this.dateGiven = dateGiven;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

}

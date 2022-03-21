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

    public LocalDate getYearMonthSubject() {
        return yearMonthSubject;
    }

    public void setYearMonthSubject(LocalDate yearMonthSubject) {
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

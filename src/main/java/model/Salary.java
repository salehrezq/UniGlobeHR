package model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author Saleh
 */
public class Salary {

    private int id;
    private int employeeId;
    private LocalDate dateSubject;
    private LocalDate dateGiven;
    private BigDecimal agreedSalary;
    private BigDecimal payable;

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

    public LocalDate getDateSubject() {
        return dateSubject;
    }

    public void setDateSubject(LocalDate dateSubject) {
        this.dateSubject = dateSubject;
    }

    public LocalDate getDateGiven() {
        return dateGiven;
    }

    public void setDateGiven(LocalDate dateGiven) {
        this.dateGiven = dateGiven;
    }

    public BigDecimal getAgreedSalary() {
        return agreedSalary;
    }

    public void setAgreedSalary(BigDecimal agreedSalary) {
        this.agreedSalary = agreedSalary;
    }

    public BigDecimal getPayable() {
        return payable;
    }

    public void setPayable(BigDecimal payable) {
        this.payable = payable;
    }

}

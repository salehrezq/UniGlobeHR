package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author Saleh
 */
public class Performance {

    private int id;
    private int employeeId;
    private LocalDateTime dateTime;
    private String descriptionEN;
    private String descriptionAR;
    private double amount;
    private boolean state;

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

    public LocalDateTime getDate() {
        return dateTime;
    }

    public void setDate(LocalDateTime date) {
        this.dateTime = date;
    }

    public String getDescriptionEN() {
        return descriptionEN;
    }

    public void setDescriptionEN(String descriptionEN) {
        this.descriptionEN = descriptionEN;
    }

    public String getDescriptionAR() {
        return descriptionAR;
    }

    public void setDescriptionAR(String descriptionAR) {
        this.descriptionAR = descriptionAR;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

}

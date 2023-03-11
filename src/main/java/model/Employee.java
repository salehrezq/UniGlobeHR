/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import datalink.CRUDSalary;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 *
 * @author Saleh
 */
public class Employee {

    private int id;
    private String name;
    private LocalDate enrolledDate;
    private BigDecimal salary;
    private byte[] photo;
    private boolean active;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getEnrolledDate() {
        return enrolledDate;
    }

    public void setEnrolledDate(LocalDate enrolledDate) {
        this.enrolledDate = enrolledDate;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    public BigDecimal salaryOfSingleDay() {
        return this.salary.divide(new BigDecimal("30"), 3, RoundingMode.HALF_UP);
    }

    public BigDecimal salaryOfSubMonth(int days) {
        return this.salaryOfSingleDay().multiply(new BigDecimal(days));
    }

    public boolean isSalaryPaidForMonth(LocalDate ym) {
        return CRUDSalary.isEmployeeWithYearMonthSubjectExist(this.getId(), ym) != null;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

/**
 *
 * @author Saleh
 */
public class DatePicker {

    private JDatePickerImpl fDatePicker;
    private UtilDateModel dateModel;
    private JDatePanelImpl datePanel;
    private LocalDate date;
    private ArrayList<DateListener> dateListeners;

    public DatePicker() {

        super();

        dateListeners = new ArrayList<>();

        dateModel = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        datePanel = new JDatePanelImpl(dateModel, p);
    }

    public JDatePickerImpl getDatePicker() {

        // Don't know about the formatter, but there it is...
        fDatePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        fDatePicker.addActionListener((arg0) -> {
            Date selectedDate = (Date) fDatePicker.getModel().getValue();
            if (selectedDate != null) {
                date = LocalDate.ofInstant(selectedDate.toInstant(), ZoneId.systemDefault());
                this.notifyDateChange(date);
                //System.out.println("propagate date " + date);
            }
        });
        return fDatePicker;
    }

    public void setTodayAsDefault() {
        LocalDate today = LocalDate.now();
        dateModel.setDate(today.getYear(), today.getMonthValue() - 1, today.getDayOfMonth());
        dateModel.setSelected(true);
        this.date = today;
    }

    public void addDateListener(DateListener dateListner) {
        dateListeners.add(dateListner);
    }

    private void notifyDateChange(LocalDate date) {
        dateListeners.forEach((dateListener) -> {
            dateListener.dateChanged(date);
        });
    }

    public LocalDate getDate() {
        return this.date;
    }

}

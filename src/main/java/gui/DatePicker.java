/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
    private ArrayList<DateDeselectedListener> dateDeselectedListeners;

    public DatePicker() {

        super();

        dateListeners = new ArrayList<>();
        dateDeselectedListeners = new ArrayList<>();

        dateModel = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        datePanel = new JDatePanelImpl(dateModel, p);

        fDatePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        fDatePicker.addActionListener(new DateSelectActionHandler());
    }

    public JDatePickerImpl getDatePicker() {
        return fDatePicker;
    }

    public void setTodayAsDefault() {
        LocalDate today = LocalDate.now();
        dateModel.setDate(today.getYear(), today.getMonthValue() - 1, today.getDayOfMonth());
        dateModel.setSelected(true);
        this.date = today;
    }

    public void setDateValue(LocalDate date) {
        dateModel.setDate(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
        dateModel.setSelected(true);
        this.date = date;
    }

    public LocalDate getDefaultToday() {
        return this.date;
    }

    public void addDateListener(DateListener dateListner) {
        dateListeners.add(dateListner);
    }

    private void notifyDateChange(LocalDate date) {
        dateListeners.forEach((dateListener) -> {
            dateListener.dateChanged(date);
        });
    }

    public void addDateDeselectedListener(DateDeselectedListener ddesl) {
        dateDeselectedListeners.add(ddesl);
    }

    private void notifyDateDeselected() {
        dateDeselectedListeners.forEach((ddesl) -> {
            ddesl.dateDeselected();
        });
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setEnabled(boolean enabled) {
        fDatePicker.getComponent(1).setEnabled(enabled);
    }

    private class DateSelectActionHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Date selectedDate = (Date) fDatePicker.getModel().getValue();
            if (selectedDate != null) {
                date = LocalDate.ofInstant(selectedDate.toInstant(), ZoneId.systemDefault());
                notifyDateChange(date);
            } else {
                notifyDateDeselected();
            }
        }

    }

}

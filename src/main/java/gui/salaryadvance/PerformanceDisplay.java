package gui.salaryadvance;

import crud.UpdateListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

/**
 *
 * @author Saleh
 */
public class PerformanceDisplay
        implements
        EditableListener,
        CancelListener,
        UpdateListener {

    private PerformanceInput performanceInput;
    private JCheckBox checkDisplayMode;
    private List<DisplayableListener> PerformanceDisplayableListeners;

    public PerformanceDisplay() {

        PerformanceDisplayableListeners = new ArrayList<>();

        checkDisplayMode = new JCheckBox("Display Mode");
        checkDisplayMode.addItemListener(new ChechBoxListener());
    }

    public void setPerformanceInput(PerformanceInput performanceInput) {
        this.performanceInput = performanceInput;
    }

    public JCheckBox getCheckDisplayMode() {
        return checkDisplayMode;
    }

    public void addPerformanceDisplayableListener(DisplayableListener pdml) {
        this.PerformanceDisplayableListeners.add(pdml);
    }

    private void notifyPerformanceDisplayable() {
        this.PerformanceDisplayableListeners.forEach((pdml) -> {
            pdml.displayable();
        });
    }

    private void notifyPerformanceUnDisplayable() {
        this.PerformanceDisplayableListeners.forEach((ddl) -> {
            ddl.unDisplayable();
        });
    }

    @Override
    public void editable() {
        checkDisplayMode.setEnabled(false);
    }

    @Override
    public void cancelled() {
        checkDisplayMode.setEnabled(true);
    }

    @Override
    public void updated() {
        checkDisplayMode.setEnabled(true);
    }

    private class ChechBoxListener implements ItemListener {

        // https://stackoverflow.com/q/70354055/6811102
        // count is used to assist in checking when to display
        // the confirmation dialog box.
        int count = 0;

        @Override
        public void itemStateChanged(ItemEvent event) {

            Object source = event.getSource();

            if (source == checkDisplayMode) {

                if (event.getStateChange() == ItemEvent.SELECTED) {

                    count += 1;
                    if (count == 1) {
                        int dialogResult = JOptionPane.showConfirmDialog(null,
                                "To display descriptions; input fields\n"
                                + "will be cleared and disabled, sure?",
                                "Warning", JOptionPane.YES_OPTION);

                        if (dialogResult == JOptionPane.YES_OPTION) {
                            notifyPerformanceDisplayable();
                            count += 1;
                            checkDisplayMode.setSelected(true);
                        }
                    }
                } else if (event.getStateChange() == ItemEvent.DESELECTED && count == 0) {
                    notifyPerformanceUnDisplayable();
                }
            }
            count = 0;
        }
    }

}

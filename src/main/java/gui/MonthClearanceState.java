package gui;

import java.awt.CardLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Saleh
 */
public class MonthClearanceState {

    private JPanel container, panelCards;

    public MonthClearanceState() {

        container = new JPanel();

        JLabel lbNoLabel = new JLabel("");
        JLabel lbMonthCleared = new JLabel("This month was cleared.");

        panelCards = new JPanel(new CardLayout());

        JPanel panelLabelNoLabel = new JPanel();
        panelLabelNoLabel.add(lbNoLabel);
        JPanel panelLabelMonthCleared = new JPanel();
        panelLabelMonthCleared.add(lbMonthCleared);

        panelCards.add(panelLabelNoLabel, MonthClearanceStatePole.MONTH_NOT_CLEARED.state());
        panelCards.add(panelLabelMonthCleared, MonthClearanceStatePole.MONTH_CLEARED.state());
        container.add(panelCards);

        setMonthClearanceState(MonthClearanceStatePole.MONTH_NOT_CLEARED.state());
    }

    private enum MonthClearanceStatePole {

        MONTH_NOT_CLEARED("month not cleared"),
        MONTH_CLEARED("month cleared");

        private final String state;

        private MonthClearanceStatePole(String state) {
            this.state = state;
        }

        private String state() {
            return state;
        }
    }

    private void setMonthClearanceState(String state) {
        CardLayout cl = (CardLayout) (panelCards.getLayout());
        cl.show(panelCards, state);
    }

    public JPanel getContainer() {
        return this.container;
    }

    public void labelMonthNotCleared() {
        setMonthClearanceState(MonthClearanceStatePole.MONTH_NOT_CLEARED.state());
    }

    public void labelMonthCleared() {
        setMonthClearanceState(MonthClearanceStatePole.MONTH_CLEARED.state());
    }

}

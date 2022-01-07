package model;

/**
 *
 * @author Saleh
 */
public class PerformanceType {

    private int id;
    private String type;
    private Boolean state;

    public PerformanceType() {

    }

    public PerformanceType(int id, String type, Boolean state) {
        this.id = id;
        this.type = type;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return this.type;
    }

}

package gui.salaryadvance;

import gui.performance.*;

/**
 *
 * @author Saleh
 */
public interface RowSelectedListener {

    /**
     * id is id of the record from the database.
     *
     * @param id
     */
    public void rowSelectedWithRecordId(int id);
}

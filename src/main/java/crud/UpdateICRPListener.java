package crud;

/**
 * UpdateICRPListener; ICRP is abbreviation for Include Current Request
 * Parameters.
 *
 * Update which Include Current Request Parameters; which require different
 * measurement such as moving the requested record from the current view because
 * it is no longer relevant to the current request parameters.
 *
 * @author Saleh
 */
public interface UpdateICRPListener {

    /**
     * Update which Include Current Request Parameters; which may require
     * different measurements other than or including usual update measurements.
     */
    public void updatedICRP();
}

package crud;

import java.util.List;

/**
 *
 * @author Saleh
 * @param <E>
 */
public interface ReadListener<E> {

    public void read(List<E> records);
}

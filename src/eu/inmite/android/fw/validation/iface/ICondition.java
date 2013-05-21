package eu.inmite.android.fw.validation.iface;

/**
 * @author Tomas Vondracek
 */
public interface ICondition<T> {

	public boolean evaluate(T input);

}

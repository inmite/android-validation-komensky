package eu.inmite.android.lib.validations.form.iface;

/**
 * @author Tomas Vondracek
 */
public interface ICondition<T> {

	public boolean evaluate(T input);

}

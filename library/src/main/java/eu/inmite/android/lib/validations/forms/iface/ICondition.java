package eu.inmite.android.lib.validations.forms.iface;

/**
 * @author Tomas Vondracek
 */
public interface ICondition<T> {

	public boolean evaluate(T input);

}

package eu.inmite.android.lib.validations.form.iface;

/**
 * @author Tomas Vondracek
 */
public interface ICondition<T> {

	boolean evaluate(T input);

}

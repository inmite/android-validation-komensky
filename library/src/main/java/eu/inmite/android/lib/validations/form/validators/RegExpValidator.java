package eu.inmite.android.lib.validations.form.validators;

import eu.inmite.android.lib.validations.form.annotations.RegExp;
import eu.inmite.android.lib.validations.form.annotations.ValidatorFor;

import java.lang.annotation.Annotation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Tomas Vondracek
 */
@ValidatorFor(RegExp.class)
public class RegExpValidator extends BaseValidator<CharSequence> {

	private Pattern mCompiledPattern;

	@Override
	public boolean validate(Annotation annotation, CharSequence input) {
		final String regexp = ((RegExp) annotation).value();

		if (mCompiledPattern == null || ! mCompiledPattern.pattern().equals(regexp)) {
			mCompiledPattern = Pattern.compile(regexp);
		}

		final Matcher matcher = mCompiledPattern.matcher(input);
		return matcher.matches();
	}
}

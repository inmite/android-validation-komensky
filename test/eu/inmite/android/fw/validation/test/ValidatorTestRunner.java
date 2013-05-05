package eu.inmite.android.fw.validation.test;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricContext;
import org.robolectric.RobolectricTestRunner;

/**
 * @author Tomáš Kypta
 * @since 05/05/2013
 */
public class ValidatorTestRunner  extends RobolectricTestRunner {

	public ValidatorTestRunner(Class<?> testClass) throws InitializationError {
		super(RobolectricContext.bootstrap(ValidatorTestRunner.class, testClass,
				new RobolectricContext.Factory() {
					@Override
					public RobolectricContext create() {
						return new RobolectricContext() {
							@Override
							public boolean useAsm() {
								return true;
							}
						};
					}
				}));
	}
}

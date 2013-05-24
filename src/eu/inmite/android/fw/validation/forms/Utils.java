package eu.inmite.android.fw.validation.forms;

import android.text.TextUtils;
import android.util.Log;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * @author Tomas Vondracek
 */
public class Utils {

	private static DecimalFormat numberFormat = null;

	private static DecimalFormat getBigDecimalFormatter() {
		if (numberFormat == null) {
			numberFormat = new DecimalFormat();
			numberFormat.setMinimumFractionDigits(2);
			numberFormat.setMaximumFractionDigits(2);
			numberFormat.setParseBigDecimal(true);
		}
		return numberFormat;
	}

	public static BigDecimal parseAmountWithDecimalFormatter(String text) {
		if (TextUtils.isEmpty(text)) {
			return BigDecimal.ZERO;
		}

		BigDecimal amount = null;
		try {
			amount = (BigDecimal) getBigDecimalFormatter().parse(text);
		} catch (ParseException e) {
			Log.w("validation", "failed to parse amount with number formatter " + text, e);
		}
		return amount;
	}

	public static BigDecimal parseAmount(String text) {
		if (TextUtils.isEmpty(text)) {
			return BigDecimal.ZERO;
		}
		BigDecimal amount = parseAmountWithDecimalFormatter(text);
		if (amount != null) {
			return amount;
		} else {
			// decimal formatter failed let's try it simple way
			try {
				amount = new BigDecimal(text);
			} catch (Exception e) {
				Log.w("validation", "failed to create BigDecimal from " + text, e);
			}
		}
		return amount;
	}
}

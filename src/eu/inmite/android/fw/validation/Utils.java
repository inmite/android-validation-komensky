package eu.inmite.android.fw.validation;

import android.text.TextUtils;
import eu.inmite.android.fw.DebugLog;

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
			DebugLog.w("failed to parse amount with number formatter " + text, e);
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
			try {
				// this will probably work only on the common locales:
				final String numberText;
				int commaCount = text.replaceAll("[^,]", "").length();
				if (text.contains(".") || commaCount > 1) {
					numberText = text.replace(",", ""); // "," comma is group separator
				} else {
					int commaIndex = text.lastIndexOf(",");
					if (commaIndex >= 0) {
						int placesBehindComma = text.length() - commaIndex - 1;

						if (placesBehindComma != 3) {
							numberText = text.replace(",", ".");    // comma is decimal separator
						} else {
							numberText = text.replace(",", ""); // comma is group separator (we allow only 2 decimal places)
						}
					} else {
						numberText = text;
					}
				}
				amount = new BigDecimal(numberText.replace(" ", ""));   // space could be group separator
			} catch (Exception e) {
				DebugLog.e("failed to parse amount " + text);
			}
		}
		return amount;
	}
}

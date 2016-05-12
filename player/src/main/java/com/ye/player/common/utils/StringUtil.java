package com.ye.player.common.utils;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ImageSpan;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringUtil {

	// email
	private final static Pattern emailer = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");

	// phone
	private final static Pattern phoner = Pattern.compile("[1]\\d{10}");

	/**
	 * if string is empty
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	/**
	 * if email is valid
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (email == null || email.trim().length() == 0)
			return false;
		return emailer.matcher(email).matches();
	}

	/**
	 * 半角转换为全角
	 * 
	 * @param input
	 * @return
	 */
	public static String toDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/**
	 * encrypt phone number
	 * 
	 * @param phone
	 * @return
	 */
	public static String encryptPhoneNumber(String phone) {
		if (phone == null) {
			return "*****";
		}
		if (phone.length() < 11) {
			return phone;
		}
		return phone.substring(0, 3) + "*****" + phone.substring(8, 11);
	}

	/**
	 * convert user credit log's address
	 * 
	 * @param address
	 * @return
	 */
	public static String convertUserDeliveryAddress(String address) {
		String[] info = address.split("#");
		StringBuffer text = new StringBuffer();
		text.append(info[1]) // province
				.append(info[2]) // city
				.append(info[3]) // zone
				.append(info[4]) // street
				.append("\n").append(info[0]) // name
				.append(" ").append(info[6]) // phone
		;

		return text.toString();
	}

	public static boolean contains(String targetStr, String subStr) {
		if (isEmpty(targetStr) || isEmpty(subStr)) {
			return false;
		}
		return targetStr.contains(subStr);
	}

	public static String creditFormat(String creditStr) {
		StringBuffer buffer = new StringBuffer();
		if (creditStr.contains(".")) {
			int a = creditStr.indexOf(".");
			String string1 = creditStr.substring(0, a);
			String string2 = creditStr.substring(a + 1, creditStr.length());

			if (string2.length() == 1) {
				string2 = string2 + "0";
			} else {
				string2 = string2.substring(0, 2);
			}
			buffer = buffer.append(string1).append(".").append(string2);

		} else {
			buffer = buffer.append(creditStr).append(".00");
		}

		return buffer.toString();
	}

	public static String creFormat(String creditStr) {
		StringBuffer buffer = new StringBuffer();
		if (creditStr.contains(".")) {
			int a = creditStr.indexOf(".");
			String string1 = creditStr.substring(0, a);
			String string2 = creditStr.substring(a + 1, creditStr.length());

			if (string2.length() == 1) {
				string2 = string2 + "0";
			} else {
				string2 = string2.substring(0, 2);
			}
			if (Integer.parseInt(string2) > 0) {
				buffer = buffer.append(string1).append(".").append(string2);
			} else {
				buffer = buffer.append(string1);
			}

		} else {
			buffer = buffer.append(creditStr).append("");
		}

		return buffer.toString();
	}

	public static boolean isPhoneNum(String phoneNum) {

		if (phoneNum == null || phoneNum.trim().length() == 0)
			return false;
		return phoner.matcher(phoneNum).matches();
	}

	public static boolean isMobileNO(String mobiles) {
		boolean flag = false;
		try {
			mobiles = mobiles.replace(" ", "");
			mobiles = mobiles.replace("+86", "");
			Pattern p = Pattern.compile("^((1[0-9]))\\d{9}$");
			Matcher m = p.matcher(mobiles);
			flag = m.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	public static String currencyFormat(int credit) {
		NumberFormat nf = NumberFormat.getInstance();
		return StringUtil.creditFormat(nf.format(credit / 100.00f));
	}

	public static String curFormat(int credit) {
		NumberFormat nf = NumberFormat.getInstance();
		return StringUtil.creFormat(nf.format(credit));
	}

	public static String specialLettersFilter(String str) throws PatternSyntaxException {
		String regEx = "[/\\！@#￥……~·——+{}：“’《》？/。，‘；】【=-·~&*（）*?<>|\"\n\t`~!@#$%^&*()-_=+{\\[\\]}:;,.]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	public static boolean isValidStr(String str) {
		if (isEmpty(str)) {
			return true;
		}
		byte[] nameBytes = str.getBytes();
		for (int i = 0; i < nameBytes.length; i += 3) {
			if ((nameBytes[i] & 0xF8) == 0xF0) {
				return false;
			}
		}
		return true;
	}

	public static String replace(int from, int to, String source, String element) {
		if (source == null || from == -1 || to == -1)
			return null;
		String tmp = source.substring(from, to);
		source = source.replace(tmp, element);
		return source;
	}

	// public static SpannableStringBuilder colorPartiallyString1(String
	// content, String colorString, int color) {
	// SpannableStringBuilder style = new SpannableStringBuilder(content);
	// int start = content.indexOf(colorString);
	// int end = start + colorString.length();
	// style.setSpan(new ForegroundColorSpan(color), start, end,
	// Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
	// return style;
	// }

	public static Spanned colorPartiallyString(String content, String colorString, int color) {
		int start = content.indexOf(colorString);
		int end = start + colorString.length();
		String result = "";
		if (start >= 0) {
			if (start > 0) {
				result += content.substring(0, start);
			}
			String colorStr = Integer.toHexString(color);
			if (colorStr.length() > 6) {
				colorStr = colorStr.substring(2);
			}
			result += "<font color='#" + colorStr + "'>" + content.substring(start, end) + "</font>";
			if (end < content.length()) {
				result += content.substring(end);
			}
			return Html.fromHtml(result);

		} else {
			return Html.fromHtml(content);
		}
	}

	public static String dateToString(Date currentTime, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static Date stringToDate(String strDate, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	public static boolean equals(String left, String right) {
		if (!StringUtil.isEmpty(left)) {
			return left.equals(right);
		} else if (!StringUtil.isEmpty(right)) {
			return false;
		}
		return true;
	}

	public static SpannableStringBuilder createSpannable(Drawable drawable) {
		String text = "bitmap";
		SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
		ImageSpan span = new ImageSpan(drawable);//ImageSpan.ALIGN_BOTTOM);
		spannableStringBuilder.setSpan(span, 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		spannableStringBuilder.append("图文混排");
		spannableStringBuilder.setSpan(new BackgroundColorSpan(Color.parseColor("#8A2233B1")), 0, spannableStringBuilder.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		return spannableStringBuilder;
	}
}

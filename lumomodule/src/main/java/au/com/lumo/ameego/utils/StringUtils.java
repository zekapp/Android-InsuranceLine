package au.com.lumo.ameego.utils;

import java.text.DecimalFormat;

/**
 * Created by zeki on 21/05/15.
 */
public class StringUtils {
    private static final String TAG = StringUtils.class.getSimpleName();

    public static String makeUpperCaseEachWords(String line)
    {
        if(line == null || line.isEmpty() )
            return "";

        line = line.trim().toLowerCase();
        String data[] = line.split("\\s");
        line = "";
        for(int i =0;i< data.length;i++)
        {
            if(data[i].length()>1)
                line = line + data[i].substring(0,1).toUpperCase()+data[i].substring(1)+" ";
            else
                line = line + data[i].toUpperCase();
        }
        return line.trim();
    }

    public static String makeHtmlUrl(String url)
    {
        if(url ==null || url.isEmpty())
            return "";
        return url.replace(" ", "%20");
    }


    public static boolean isPasswordValid(String password) {
        return password.length() >= Constants.PASSWORD_LENGHT;
    }

    public static boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public static boolean isUrlValid(String url) {
        if(url == null) return false;
        else            return url.startsWith("http://") || url.startsWith("https://");
    }

    public static String getValidatedUrl(String url) {
        if(url == null || url.isEmpty())
            return Constants.Url.EMPTY_CASE_URL;

        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        return url;
    }

    public static String formatDoubleValue(double d) {
        DecimalFormat format = new DecimalFormat();
        format.setDecimalSeparatorAlwaysShown(false);
        return format.format(d);
    }
}

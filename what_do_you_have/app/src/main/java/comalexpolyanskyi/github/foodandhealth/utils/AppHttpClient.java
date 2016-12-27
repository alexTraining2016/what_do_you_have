package comalexpolyanskyi.github.foodandhealth.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AppHttpClient {

    private static final String METHOD = "POST";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String CONTENT_LANGUAGE = "Content-Language";
    private static final String EN_US = "en-US";

    private static AppHttpClient httpClient;
    private HttpURLConnection urlConnection;

    private AppHttpClient() {
    }

    public byte[] loadDataFromHttp(final String targetURL, final String urlParameters) {
        urlConnection = null;
        InputStream inputStream = null;
        ByteArrayOutputStream buffer = null;
        byte[] bytes = null;
        try {
            final URL url = new URL(targetURL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(METHOD);
            urlConnection.setRequestProperty(CONTENT_TYPE,
                    APPLICATION_X_WWW_FORM_URLENCODED);
            urlConnection.setRequestProperty(CONTENT_LENGTH, "" +
                    Integer.toString(urlParameters.getBytes().length));
            urlConnection.setRequestProperty(CONTENT_LANGUAGE, EN_US);

            urlConnection.setUseCaches(false);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            inputStream = new BufferedInputStream(urlConnection.getInputStream());
            buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            bytes = buffer.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (buffer != null) {
                    buffer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return bytes;
    }

    public byte[] loadDataFromHttp(final String stringUrl) {
        urlConnection = null;
        InputStream inputStream = null;
        ByteArrayOutputStream buffer = null;
        byte[] bytes = null;

        try {
            final URL url = new URL(stringUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream(urlConnection.getInputStream());
            buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            bytes = buffer.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (buffer != null) {
                    buffer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return bytes;
    }

    public static void install() {
        if (httpClient == null) {
            httpClient = new AppHttpClient();
        }
    }

    public static AppHttpClient getAppHttpClient() throws NullPointerException {
        if (httpClient != null) {
            return httpClient;
        } else {
            throw new NullPointerException();
        }
    }
}

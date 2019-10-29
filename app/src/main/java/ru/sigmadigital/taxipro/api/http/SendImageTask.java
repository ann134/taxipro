package ru.sigmadigital.taxipro.api.http;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.sigmadigital.taxipro.models.http.ImageResponse;
import ru.sigmadigital.taxipro.models.http.BaseResponse;
import ru.sigmadigital.taxipro.models.my.JsonParser;
import ru.sigmadigital.taxipro.util.SettingsHelper;

public class SendImageTask extends AsyncTask<Void, Void, ImageResponse> {

    private List<File> files;
    private OnGetImageResponseListener listener;

    private String boundary = "Asrf456BGe4h";

    public SendImageTask(OnGetImageResponseListener listener, List<File> files) {
        this.listener = listener;
        this.files = files;
    }

    @Override
    protected ImageResponse doInBackground(Void... voids) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Token", SettingsHelper.getToken().getEntityId());
        headers.put("Content-Type", "multipart/form-data; boundary=" + boundary);

        try {
            URL u = new URL("http://staging-api.taxipro.su/filestorage/files");
            HttpURLConnection con = (HttpURLConnection) u.openConnection();
            con.setRequestMethod("POST");
            setHeaders(con, headers);


            //write files
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            for (File file : files) {
                writeFile(os, file);
                Log.e("file", file.getName());
            }

            //last boundary
            String bend = "--" + boundary + "--";
            os.write(bend.getBytes("UTF-8"));
            os.write('\r');
            os.write('\n');
            os.flush();

            os.write('\r');
            os.write('\n');
            os.flush();

            os.flush();
            os.close();


            int code = con.getResponseCode();
            InputStream is = getInputStream(con);


            BaseResponse resp = new BaseResponse(code);
            resp.setData(readStream(is));
            System.out.println("date=" + resp.getData());
            is.close();


            ImageResponse imageResponse = JsonParser.fromJson(resp.getData(), ImageResponse.class);

            Log.e("array", imageResponse.getIds().length + "");

            return  imageResponse;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private void writeFile(OutputStream os, File file) throws IOException {
        if (os != null && file != null) {

            String b1 = "--" + boundary;

            os.write(b1.getBytes("UTF-8"));
            os.write('\r');
            os.write('\n');
            os.flush();

            Log.e("bound", b1);

            String title = "Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n";
            String type = "Content-Type: image/" + getFileExtension(file) + "\r\n";

            os.write(title.getBytes("UTF-8"));
            os.write('\r');
            os.write('\n');
            os.flush();

            os.write(type.getBytes("UTF-8"));
            os.write('\r');
            os.write('\n');
            os.flush();

            os.write('\r');
            os.write('\n');
            os.flush();

            Log.e("title", title);
            Log.e("type", type);

            writeData(os, new FileInputStream(file));
            os.flush();

            os.write('\r');
            os.write('\n');
            os.flush();
        }
    }


    @Override
    protected void onPostExecute(ImageResponse baseResponse) {
        super.onPostExecute(baseResponse);
            if (listener != null) {
                listener.onGetItemsImageResponse(baseResponse);
            }
    }


    private static void setHeaders(URLConnection connection, Map<String, String> headers) {
        if (connection != null) {
            if (headers != null) {
                for (String key : headers.keySet()) {
                    connection.addRequestProperty(key, headers.get(key));
                }
            }
        }
    }


    private void writeData(OutputStream os, InputStream data) throws IOException {
        if (os != null && data != null) {
            byte[] rd = new byte[1024];
            int r;
            while ((r = data.read(rd, 0, rd.length)) != -1) {
                os.write(rd, 0, r);
            }

            data.close();
        }
    }

    private InputStream getInputStream(HttpURLConnection connection) throws IOException {
        int code = connection.getResponseCode();
        if (code == 200) {
            return connection.getInputStream();
        } else {
            return connection.getErrorStream();
        }
    }

    private String readStream(InputStream is/*, boolean gzip*/) throws IOException {
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            byte[] rd = new byte[1024];
            int r;
            while ((r = is.read(rd, 0, rd.length)) != -1) {
                sb.append(new String(rd, 0, r, "utf-8"));
            }
            return sb.toString();
        }
        return null;
    }


    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else return "";
    }


    public interface OnGetImageResponseListener {
        void onGetItemsImageResponse(ImageResponse imageResponse);
    }

}

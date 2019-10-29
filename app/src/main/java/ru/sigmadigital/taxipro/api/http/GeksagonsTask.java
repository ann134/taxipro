package ru.sigmadigital.taxipro.api.http;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.sigmadigital.taxipro.models.Geo;
import ru.sigmadigital.taxipro.models.http.Geksagons;
import ru.sigmadigital.taxipro.util.SettingsHelper;

public class GeksagonsTask extends AsyncTask<Void, Void, List<Geksagons>> {

    private Geo geo;
    private OnGetGeksagonesListener listener;


    public GeksagonsTask(OnGetGeksagonesListener listener, Geo geo) {
        this.geo = geo;
        this.listener = listener;
    }

    @Override
    protected List<Geksagons> doInBackground(Void... voids) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Token", SettingsHelper.getToken().getEntityId());
        // headers.put("Content-Type", "multipart/form-data; boundary=" + boundary);

        Uri params = new Uri.Builder()
                .scheme("http")
                .path("//staging-api.taxipro.su/surger/map")
                .appendQueryParameter("lat", String.valueOf(geo.getLat()))
                .appendQueryParameter("lon", String.valueOf(geo.getLon()))
                .build();

        BaseResponse resp = Sender.sendGet(params.toString(), headers);

        if (resp != null && resp.getCode() == 200) {
            String r = resp.getData();
            Log.e("vghj", r);
            List<String> jsonArray = new ArrayList<>(Arrays.asList(r.split("\\n")));
            List<Geksagons> list = new ArrayList<>();
            for (String s : jsonArray) {
                Geksagons geksagons1 = new Geksagons();
                String[] both = s.split(":");
                geksagons1.setHex(both[0]);
                geksagons1.setAlfa(Double.parseDouble(both[1]));
                list.add(geksagons1);
            }

            return list;
        }

        return null;
    }


    @Override
    protected void onPostExecute(List<Geksagons> geksagons) {
        super.onPostExecute(geksagons);
        listener.onGetGeksagonesResponse(geksagons);

    }

    public interface OnGetGeksagonesListener {
        void onGetGeksagonesResponse(List<Geksagons> geksagons);
    }

}

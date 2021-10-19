package com.ilerna.vendesininmobiliarias.api;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.JsonWriter;

import com.ilerna.vendesininmobiliarias.models.FCMBody;
import com.ilerna.vendesininmobiliarias.models.Token;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class FCMApi extends AsyncTask<FCMBody, Void, Void> {

    public FCMApi() {
    }

    @Override
    protected Void doInBackground(FCMBody... body) {
        sendNotificaiton(body[0]);
        return null;
    }

    public void sendNotificaiton(FCMBody body) {
        try {
            // Create URL
            URL endPoint = new URL("https://fcm.googleapis.com/fcm/send");
            // Create connection
            HttpsURLConnection connection = (HttpsURLConnection) endPoint.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "key=AAAAfbCfwPY:APA91bHNx2c0cjKNz4X1fFEAiT9LbHCi87eJwAEDcPSwqcJDVXWaT1kaCFI7sQ3ajkoWdzPzR7_k6nSimiC7_bwT9CM6om6vDYDMs5VNea3q7JCTku21gauw_O3ybM7Bne1czQ7MRmr-");

            // Enable writing
            connection.setDoOutput(true);
            connection.setChunkedStreamingMode(0);

            writeJsonStream(connection.getOutputStream(), body);
            connection.getResponseCode();
            // connection.disconnect();
        } catch (IOException ex) {

        }
    }

    public void writeJsonStream(OutputStream out, FCMBody body) throws IOException {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out));
        writer.setIndent("  ");
        writeMessage(writer, body);
        writer.close();
    }


    public void writeMessage(JsonWriter writer, FCMBody body) throws IOException {
        writer.beginObject();
        writer.name("to").value(body.getTo());
        writer.name("priority").value(body.getPriority());
        writer.name("ttl").value(body.getTtl());

        writer.name("data");
        writeUser(writer, body.getData());
        writer.endObject();
    }

    public void writeUser(JsonWriter writer, Map<String, String> data) throws IOException {
        writer.beginObject();
        writer.name("title").value(data.get("title"));
        writer.name("body").value(data.get("body"));
        String a = writer.toString();
        writer.endObject();
    }

}

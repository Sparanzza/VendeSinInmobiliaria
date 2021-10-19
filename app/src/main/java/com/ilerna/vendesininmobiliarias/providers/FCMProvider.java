package com.ilerna.vendesininmobiliarias.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ilerna.vendesininmobiliarias.api.FCMApi;
import com.ilerna.vendesininmobiliarias.models.Comment;
import com.ilerna.vendesininmobiliarias.models.FCMBody;
import com.ilerna.vendesininmobiliarias.models.Token;

import java.util.HashMap;
import java.util.Map;

public class FCMProvider {


    public FCMProvider() {
    }

    public void createFCMComment(String title, String comment, String token) {
        new FCMApi().execute(createFCMBody(title, comment, token));
    }

    public FCMBody createFCMBody(String title, String comment, String token) {
        FCMBody body = new FCMBody();
        //body.setTo("e9s5uSRXTT2vtFPnsQb-7r:APA91bGKKB9fqi_PRxIfHCkmMJcM8iUKyGn2_1mDDNT2uPs2ZTnpjKpHGcPArSvmCZ9IAigvosLwOLT97I_OoQogTDhLpr0PnOjWc089-nTtv0hTQkc5bRS0NkSRR8vcj2SipwUdb_0X");
        body.setTo(token);
        body.setPriority("high");
        body.setTtl("4500s");

        Map<String, String> data = new HashMap<>();
        data.put("title", title);
        data.put("body", comment);
        body.setData(data);

        return body;
    }

}

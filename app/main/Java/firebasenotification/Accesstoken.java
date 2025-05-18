package com.dev.banksampahdigital.firebasenotification;

import android.util.Log;

import com.google.auth.oauth2.GoogleCredentials;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Accesstoken {

    private static final String firebaseMessagingScope =
            "https://www.googleapis.com/auth/firebase.messaging";

    public String getAccessToken() {
        try {
            String jsonString = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"hanum-bank-sampah\",\n" +
                    "  \"private_key_id\": \"74cc321a7405c46e89235846a90e1ddea53b2d89\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDe0hnQZFrm4P70\\n3yZtn+3k/YIwvcOivaf1umTTz3TPJtkVZRBvcjMNZQEERdEb81eEYt7kRK5ewaY4\\nVdjDP6JOb6WAKT276AQiVg09Al/B47ZXTnN0tK7ABomOMuJUV+7xVAp9ggTLVEdL\\nClcrVc05bJNndcKF5cOxnYsa0OeYCl26THh/0UkAtbX50wq0/3iuuFwPeDSOIbsN\\nZtJi18Up4lUegAX98rsXnvSMzcTmranlPA0teqQbT+9PleLEbMs8HkKjLoXHxERT\\nVi5jRU3GAwj3F8Dt/vpc12ScLhgAEI4sB9KitiqR1w7Ih2EzYFWhYOd/vhNOv83O\\neVJ0NoXJAgMBAAECggEADLxxoUm7MGs354NBzQ5RKL+cy3BtWEYC7bbDwJ+hC54q\\n1mA6ekRKyr81OatL8o8MN5L3LpEGboAfIiU2CKMAgf87tIGm97ChW90TUKNsGccM\\nSx0+2DLjsjItdqVN7ohALichml7uKzkcAhwLM3zqCBg+rLE8dy7U6rgYlWXPdw81\\nRTU6g+875tsMyOl737+O5DnFGI00QD5+qGTs6iAmFVfW8Y3ftaxpkK8821yAln7l\\nxR4j9WtpCZ14dZXag+Cjzl2Ri4HtCed8mln7wRjQJyddvb2zSsAdcjz/3UnUYMMA\\n2pKBaJ2zHwanaR6sr4hXOOGvxTuUf7S15xbZLODcAQKBgQD+G5BImaMp2tsw2Kut\\nHLmF5yHHvn3rf6LZnVU6RLDs93/61m0dPQTCBwBqCugHeyfVfDQGOZen+0nrco2B\\nXAbtYKcEmlKt+RoBjv/IzBwmrs52MfdCcDl6SbabiijFJpkfnI7Q8QOqcfAGzepc\\nQJQmqP+6N1istmB6a8WtJeaBwQKBgQDgeuQdxyvPLzgreHkuxC+gPX9A64qQTd0g\\nqNGs9TZGZRjvtpdmzX7nobe4qfz6lYS31+Jd6SHTdhi8K+ANpFI02mpDkJ4G1iWC\\nvmuTKt9Cs7xfB5dVl+rv2qYtX4ns2LBRERaZVND/dcCYMN6AfgDj2ZCPWwdiRTBV\\nuQgYaI12CQKBgQCNYubQDnRDyJtnPVegdbggci9QhYRosVb6QFl4l1neMAQMnJyP\\nxTDNiLnJRxI9f1/1oCsOP6NLez1w/QcRMBRGph2oKPnSBSVcMqXNowwS1fbYuwp5\\n9Bl3Gma5epEeyVAvz5uDVGcWwlisYIyDEgeczzGi+kKjrsJVu3t80PbFwQKBgGNw\\nLLLAMrjheulOwaekwlAiTceNT2EjDMxV3CPYr8U5LXWqlaC1cPzcl0zXCDhcmWDy\\n4dSl3BUXIFfiu6pvWvcMtbveSRJQNGeZQH/9os6iXdRewXvqAuljGu/IfYmMZ3ZR\\nsogTmU1AdSM1qqqSpoRIMRA/y6KNcZ7Xiy18ywU5AoGBAJsmv3YMeu8wGkBFmREB\\nQ1Z+IHEC/VQzKNrbAPh762x6mvyuzXRXlyCGGiqxGwRlE9SKYJ7s4hfmpXCOi0uc\\nPOH/whw6dZXD2WGn23RYWTfhzzcNzjTDxKg6+3Z1ihelXOs9fFNt004wHdkn7jpo\\nOT48bO6W+4Owerfq/vIgnAPb\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-66izf@hanum-bank-sampah.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"114407223976370533059\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-66izf%40hanum-bank-sampah.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}";
            InputStream stream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(stream).createScoped(firebaseMessagingScope);
            googleCredentials.refresh();
            return googleCredentials.getAccessToken().getTokenValue();
        } catch (Exception e) {
            Log.e("AccessToken", "getAccessToken: " + e.getLocalizedMessage());
            return null;
        }
    }

}

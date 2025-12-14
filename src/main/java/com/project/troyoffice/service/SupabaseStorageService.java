package com.project.troyoffice.service;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SupabaseStorageService {

    @Value("${supabase.url}")
    private String SUPABASE_URL;

    @Value("${supabase.key}")
    private String SUPABASE_KEY;

    @Value("${supabase.bucket}")
    private String BUCKET;

    OkHttpClient client = new OkHttpClient();

    public String uploadFile(MultipartFile file, String fileName) throws Exception {
        RequestBody body = RequestBody.create(
                file.getBytes(),
                MediaType.parse(file.getContentType())
        );

        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/storage/v1/object/" + BUCKET + "/" + fileName)
                .post(body)
                .addHeader("apikey", SUPABASE_KEY)
                .addHeader("Authorization", "Bearer " + SUPABASE_KEY)
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful())
            throw new RuntimeException("Upload failed: " + response.message());

        // URL public
        return SUPABASE_URL + "/storage/v1/object/public/" + BUCKET + "/" + fileName;
    }
}


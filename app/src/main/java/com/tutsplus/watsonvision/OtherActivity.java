package com.tutsplus.watsonvision;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.http.ServiceCallback;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.DetectedFaces;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.Face;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ImageClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ImageFace;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassifier;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualRecognitionOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OtherActivity extends AppCompatActivity {

    private VisualRecognition vrClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        vrClient = new VisualRecognition(
                VisualRecognition.VERSION_DATE_2016_05_20,
                getString(R.string.api_key)
        );
    }

    public void processImage(View view) {
        EditText imageURLInput = findViewById(R.id.image_url);
        final String url = imageURLInput.getText().toString();

        ImageView preview = findViewById(R.id.preview);
        Picasso.with(this).load(url).into(preview);

        vrClient.detectFaces(new VisualRecognitionOptions.Builder()
                .url(url)
                .build()
        ).enqueue(new ServiceCallback<DetectedFaces>() {
            @Override
            public void onResponse(DetectedFaces response) {
                List<Face> faces = response.getImages().get(0).getFaces();
                String output = "";
                for(Face face:faces) {
                    output += "<" + face.getGender().getGender() + ", " +
                            face.getAge().getMin() + " - " +
                            face.getAge().getMax() + " years old>\n";
                }

                TextView personDetails = findViewById(R.id.person_details);
                personDetails.setText(output);

            }

            @Override
            public void onFailure(Exception e) {
            }
        });
    }
}

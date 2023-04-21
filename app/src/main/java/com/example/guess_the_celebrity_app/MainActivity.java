package com.example.guess_the_celebrity_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    ArrayList<String> courseNames = new ArrayList<String>();
    ArrayList<String> newCourseNames = new ArrayList<>();
    HashMap<String, String> map = new HashMap<>();
    int index;
    Random random;
    String[] answers = new String[4];
    Button button0;
    Button button1;
    Button button2;
    Button button3;
    TextView tvpoints;
    int points = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        tvpoints = findViewById(R.id.tvpoints);
        courseNames.add("Android Game Development SurfaceView");
        courseNames.add("Android Game Development for Beginners");
        courseNames.add("Android Game Development Math Game");
        courseNames.add("Android Game Development Tutorial");
        courseNames.add("Android Sqlite Programing for Beginners");
        courseNames.add("Object-Oriented Programming Fundamentals");
        courseNames.add("jQuery Effects and Animations");
        index = 0;
        map.put(courseNames.get(0), "https://sandipbhattacharya.com/temp/images/android-game-development-surfaceview.jpg");
        map.put(courseNames.get(1), "https://sandipbhattacharya.com/temp/images/android-game-development-for-beginners.jpg");
        map.put(courseNames.get(2), "https://sandipbhattacharya.com/temp/images/android-game-development-math-game.jpg");
        map.put(courseNames.get(3), "https://sandipbhattacharya.com/temp/images/android-game-development-tutorial.jpg");
        map.put(courseNames.get(4), "https://sandipbhattacharya.com/temp/images/android-sqlite-programming-for-beginners.jpg");
        map.put(courseNames.get(5), "https://sandipbhattacharya.com/temp/images/object-oriented-programming-fundamentals.jpg");
        map.put(courseNames.get(6), "https://sandipbhattacharya.com/temp/images/jQuery-effects-and-animations.jpg");
        Collections.shuffle(courseNames);
        random = new Random();
        generateQuestions(index);
//        DownloadTask task = new DownloadTask();
//        String result = null;
//
//        //http://www.posh24.se/kandisar
//        //https://www.imdb.com/list/ls058011111/
//        //https://www.listchallenges.com/top-100-food-items-of-all-time
//        try {
//              result = task.execute("https://sandipbhattacharya.com/temp/images/android-game-development-surfaceview.jpg").get();
////            String[] splitResult = result.split("<div class=\"item-image-wrapper\">");
////            Pattern p = Pattern.compile("src=\"(.*?)\"");
////            Matcher m = p.matcher(splitResult[0]);
//
//
//              Log.i("Contents of URL", result);
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }


    private void generateQuestions(int index) {
        try {
            Bitmap bitmap = new ImageDownloader().execute(map.get(courseNames.get(index))).get();
            imageView.setImageBitmap(bitmap);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        newCourseNames = (ArrayList<String>) courseNames.clone();
        newCourseNames.remove(index);
        Collections.shuffle(newCourseNames);
        int correctAnswerPosition = random.nextInt(4);
        for(int i=0; i<4; i++){
            if(i == correctAnswerPosition){
                answers[i] = courseNames.get(index);
            }else{
                answers[i] = newCourseNames.get(i);
            }
            button0.setText(answers[0]);
            button1.setText(answers[1]);
            button2.setText(answers[2]);
            button3.setText(answers[3]);
            newCourseNames.clear();
        }
    }
    public void answerSelected(View view){
        String answer = ((Button)view).getText().toString();
        if(answer.equals(courseNames.get(index))){
            points++;
            tvpoints.setText((points + "/7"));
        }
        index++;
        if(index > courseNames.size()-1){
            tvpoints.setVisibility(View.GONE);
            button0.setVisibility(View.GONE);
            button1.setVisibility(View.GONE);
            button2.setVisibility(View.GONE);
            button3.setVisibility(View.GONE);
            if(points == 7){
                imageView.setImageResource(R.drawable.medals_gold);
            }else if(points == 6){
                imageView.setImageResource(R.drawable.medals_gold);
            }else if(points == 5){
                imageView.setImageResource(R.drawable.medals_gold);
            }
        }else{
            generateQuestions(index);
        }
    }

    private class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        HttpURLConnection httpURLConnection;

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                Bitmap temp = BitmapFactory.decodeStream(inputStream);
                return temp;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                httpURLConnection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                Toast.makeText(getApplicationContext(), "Download Successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Download Error!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);
        }
    }
}


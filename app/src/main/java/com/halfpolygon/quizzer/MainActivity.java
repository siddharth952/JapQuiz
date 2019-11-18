package com.halfpolygon.quizzer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizzer.R;

import java.util.Random;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    private int pressCounter = 0;
    private int maxPressCounter = 0;
    private String[] keys;
    private String[] textAnswers = {"おおさかふ","い","ふ"};
    private int questionNumber = 0;

    //UI
    TextView textScreen, textQuestion, textTitle;
    Animation smallbigforth;
    ProgressBar myProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Animation
        smallbigforth = AnimationUtils.loadAnimation(this,R.anim.smallbigforth);

        //gen keys from question at that position
        generateKeys(textAnswers[questionNumber]);

        keys = shuffleArray(keys);

        updateView();

        maxPressCounter = 6;

    }

    private String[] shuffleArray(String[] arr){
        Random rndm = new Random();
        for(int i = arr.length-1; i > 0; i--){
            int index = rndm.nextInt(i+1);
            String a = arr[index];
            arr[index] = arr[i];
            arr[i] = a;
        }

        return arr;
    }

    private void addView(LinearLayout viewParent, final String text, final EditText editText){
        LinearLayout.LayoutParams myLinearLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        myLinearLayoutParams.rightMargin = 32;

        //For TextView
        final TextView textView = new TextView(this);
        textView.setLayoutParams(myLinearLayoutParams);
        textView.setBackground(this.getResources().getDrawable(R.drawable.bgpink));
        textView.setTextColor(this.getResources().getColor(R.color.colorPurple));
   //     textView.getGravity();
        textView.setText(text);
        textView.setClickable(true);
        textView.setFocusable(true);
        textView.setTextSize(32);



        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/FredokaOneRegular.ttf");

        textQuestion = (TextView) findViewById(R.id.textQuestion);
        textScreen = (TextView) findViewById(R.id.textScreen);
        textTitle = (TextView) findViewById(R.id.textTitle);

        textQuestion.setTypeface(typeface);
        textScreen.setTypeface(typeface);
        textTitle.setTypeface(typeface);
        editText.setTypeface(typeface);
        textView.setTypeface(typeface);

        //For Char Counter
        textTitle.setText("Characters left:"+textAnswers[questionNumber].length());

        //Update keys
        generateKeys(textAnswers[questionNumber]);

        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(pressCounter < maxPressCounter){
                    if (pressCounter == 0 )
                        editText.setText("");



                        editText.setText(editText.getText().toString() + text);
                        textView.startAnimation(smallbigforth);
                        textView.animate().alpha(0.5f).setDuration(300);
                        pressCounter++;
                        textTitle.setText("Characters left:"+(textAnswers[questionNumber].length()-pressCounter));

                        if(pressCounter == textAnswers[questionNumber].length())
                            doValidate();

                        if(pressCounter == maxPressCounter){
                            doValidate();

                    }
                }
            }
        });


        viewParent.addView(textView);



    }

    private void doValidate(){
        pressCounter = 0;

        EditText editText = findViewById(R.id.editText);
        LinearLayout linearLayout = findViewById(R.id.layoutParent);
        ProgressBar myProgressBar = findViewById(R.id.my_progressBar);

        if (editText.getText().toString().equals(textAnswers[questionNumber])){
            Toast.makeText(MainActivity.this, "You got it!", Toast.LENGTH_SHORT).show();
            questionNumber++;

            if(questionNumber == textAnswers.length){

            //Next Activity
            Intent a = new Intent(MainActivity.this,SecondScreen.class);
            startActivity(a);
            }

            myProgressBar.setProgress(questionNumber);
            updateView();
            //Reset it
            editText.setText("");
        }else{
            Toast.makeText(MainActivity.this, "Try again!", Toast.LENGTH_SHORT).show();
            editText.setText("");
        }
        keys = shuffleArray(keys);
        linearLayout.removeAllViews();
        for (String key: keys){
            addView(linearLayout,key,editText);
        }
    }


    void generateKeys(String char_question){

        //MARK:Split the string into corresponding chars without rep.
       keys = char_question.split("(?!^)");
        Log.d(TAG, "generateKeys() returned: " + keys[0]);

        return;
    }

    void updateView(){

        for(String key: keys){
            addView((LinearLayout) findViewById(R.id.layoutParent), key,((EditText) findViewById(R.id.editText)));

        }
    }

}

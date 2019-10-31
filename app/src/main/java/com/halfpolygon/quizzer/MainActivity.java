package com.halfpolygon.quizzer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizzer.R;

import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private int pressCounter = 0;
    private int maxPressCounter = 0;
    private String[] keys = {"い","さ","か","ふ","お"};
    private String textAnswer = "おおさかふ";
    TextView textScreen, textQuestion, textTitle;
    Animation smallbigforth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Animation
        smallbigforth = AnimationUtils.loadAnimation(this,R.anim.smallbigforth);


        keys = shuffleArray(keys);

        for(String key: keys){
            addView((LinearLayout) findViewById(R.id.layoutParent), key,((EditText) findViewById(R.id.editText)));

        }

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
        textTitle.setText("Characters left:"+textAnswer.length());

        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(pressCounter < maxPressCounter){
                    if (pressCounter == 0 )
                        editText.setText("");



                        editText.setText(editText.getText().toString() + text);
                        textView.startAnimation(smallbigforth);
                        textView.animate().alpha(0).setDuration(300);
                        pressCounter++;
                        textTitle.setText("Characters left:"+(textAnswer.length()-pressCounter));

                        if(pressCounter == textAnswer.length())
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

        if (editText.getText().toString().equals(textAnswer)){
            //Toast.makeText(MainActivity.this, "You got it!", Toast.LENGTH_SHORT).show();

            //Next Activity
            Intent a = new Intent(MainActivity.this,SecondScreen.class);
            startActivity(a);

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

}
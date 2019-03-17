package com.example.naerl.ball;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView ball;
    Button buttonAddX, buttonRemoveX, buttonAddY, buttonRemoveY;

    float positionBallX, positionBallY;
    float speed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ball = findViewById(R.id.ball);
        buttonAddX = findViewById(R.id.addX);
        buttonRemoveX = findViewById(R.id.removeX);
        buttonAddY = findViewById(R.id.addY);
        buttonRemoveY = findViewById(R.id.removeY);

        positionBallX = 50;
        positionBallY = 50;
        speed = 5;

        setPosBall(positionBallX,positionBallY);

        buttonAddX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positionBallX = positionBallX + speed;
                setPosBall(positionBallX + speed, positionBallY);
            }
        });

        buttonRemoveX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positionBallX = positionBallX - speed;
                setPosBall(positionBallX - speed, positionBallY);
            }
        });

        buttonAddY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positionBallY = positionBallY + speed;
                setPosBall(positionBallX, positionBallY);
            }
        });

        buttonRemoveY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positionBallY = positionBallY - speed;
                setPosBall(positionBallX, positionBallY);
            }
        });
    }

    public void setPosBall(float PosX, float PosY)
    {
        ball.setX(PosX);
        ball.setY(PosY);
    }


}

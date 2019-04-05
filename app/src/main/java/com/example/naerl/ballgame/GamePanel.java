package com.example.naerl.ballgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.os.Vibrator;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {
    private MainThread thread;

    private BallPlayer player;
    private Point playerPoint;
    private int playerSize = 100;
    private float moveSpeed = 0.003125f;
    private float drag = moveSpeed*20;

    private SensorManager sensorManager;
    private Sensor sensor;
    private Vibrator vibrator;

    private float vibrationOffset = 2.5f;

    private boolean didVibrateX = false;
    private boolean didVibrateY = false;

    private float[] gyroOrientation;
    private float[] currentAngle;
    private float[] velocity;


    public GamePanel(Context context)
    {
        super (context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        setFocusable(true);

        player = new BallPlayer(new Rect(playerSize, playerSize, playerSize*2, playerSize*2), Color.RED);
        playerPoint = new Point(playerSize + playerSize/2, playerSize + playerSize/2);

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        // Get instance of Vibrator from current Context


        currentAngle = new float[2];
        currentAngle[0] = 0;
        currentAngle[1] = 0;

        velocity = new float[2];
        velocity[0] = 0;
        velocity[1] = 0;

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        thread = new MainThread(getHolder(), this);

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        boolean retry = true;
        while(true) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (Exception e){ e.printStackTrace();}
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                currentAngle[0] = 0;
                currentAngle[1] = 0;
                velocity[0] = 0;
                velocity[1] = 0;
                playerPoint.set((int)event.getX(), (int)event.getY());
        }
        return true;
        //return super.onTouchEvent(event);
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        gyroOrientation = event.values;

        currentAngle[0] += event.values[0];
        currentAngle[1] += event.values[1];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    public void update()
    {
        // On augmente la vélocité en fonction de l'angle (/!\ velocity[0]correspond à l'angle Y)
        velocity[0] += currentAngle[0] * moveSpeed;
        velocity[1] += currentAngle[1] * moveSpeed;

        // On applique le drag pour ralentir la boule d'elle même (friction)
        if (velocity[0] > 0)
        {
            velocity[0] -= drag;
        } else if (velocity[0] < 0)
        {
            velocity[0] += drag;
        } else {
            velocity[0] = 0;
        }

        if (velocity[1] > 0)
        {
            velocity[1] -= drag;
        } else if (velocity[1] < 0)
        {
            velocity[1] += drag;
        } else {
            velocity[1] = 0;
        }

        //On fait en sorte qu'elle ne sorte pas de l'écran
        playerPoint = new Point(playerPoint.x + (int)velocity[1], playerPoint.y + (int)velocity[0]);
        if (playerPoint.x < playerSize/2)
        {
            if (Math.abs(velocity[1]) > vibrationOffset)
            {
                vibrator.vibrate(50);
            }

            playerPoint.x = playerSize/2;
            velocity[1] = 0f;

        } else  if (playerPoint.x > this.getWidth() - playerSize/2)
        {
            if (Math.abs(velocity[1]) > vibrationOffset)
            {
                vibrator.vibrate(50);
            }

            playerPoint.x = this.getWidth() - playerSize/2;
            velocity[1] = 0f;

        }


        if (playerPoint.y < playerSize/2)
        {
            if (Math.abs(velocity[0]) > vibrationOffset)
            {
                vibrator.vibrate(50);
            }

            playerPoint.y = playerSize/2;
            velocity[0] = -1f;

        } else  if (playerPoint.y > this.getHeight() - playerSize/2)
        {
            if (Math.abs(velocity[0]) > vibrationOffset)
            {
                vibrator.vibrate(50);
            }


            playerPoint.y = this.getHeight() - playerSize/2;
            velocity[0] = 0f;

        }


        System.out.printf("Position X:" + playerPoint.x +" Y: "+ playerPoint.y + " Height: +  " + this.getHeight() + " Width: +  " + this.getWidth() + "\n");
        //On met à jour la position
        player.update(playerPoint);
    }

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);

        canvas.drawColor(Color.WHITE);

        player.draw(canvas);
    }


}

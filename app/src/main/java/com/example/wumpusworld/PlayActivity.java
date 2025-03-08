package com.example.wumpusworld;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class PlayActivity extends AppCompatActivity {
    ViewModel viewModel;
    TextView arrow, score, info_msg;
    Button moveButton, shootButton;
    ImageView backButton, rotateButton;
    MediaPlayer wallHitSound, arrowSound, fallingSound, successSound, moveSound, arrowHitSound, looseSound;
    ImageView[][] blocks = new ImageView[5][7];
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_play);
        blocks[1][1] = findViewById(R.id.imageView11);
        blocks[1][2] = findViewById(R.id.imageView12);
        blocks[1][3] = findViewById(R.id.imageView13);
        blocks[1][4] = findViewById(R.id.imageView14);
        blocks[1][5] = findViewById(R.id.imageView15);
        blocks[1][6] = findViewById(R.id.imageView16);
        blocks[2][1] = findViewById(R.id.imageView21);
        blocks[2][2] = findViewById(R.id.imageView22);
        blocks[2][4] = findViewById(R.id.imageView24);
        blocks[2][5] = findViewById(R.id.imageView25);
        blocks[2][6] = findViewById(R.id.imageView26);
        blocks[2][3] = findViewById(R.id.imageView23);
        blocks[3][1] = findViewById(R.id.imageView31);
        blocks[3][2] = findViewById(R.id.imageView32);
        blocks[3][3] = findViewById(R.id.imageView33);
        blocks[3][4] = findViewById(R.id.imageView34);
        blocks[3][5] = findViewById(R.id.imageView35);
        blocks[3][6] = findViewById(R.id.imageView36);
        blocks[4][1] = findViewById(R.id.imageView41);
        blocks[4][2] = findViewById(R.id.imageView42);
        blocks[4][3] = findViewById(R.id.imageView43);
        blocks[4][4] = findViewById(R.id.imageView44);
        blocks[4][5] = findViewById(R.id.imageView45);
        blocks[4][6] = findViewById(R.id.imageView46);
        backButton = findViewById(R.id.backButton);
        rotateButton = findViewById(R.id.rotateButton);
        shootButton = findViewById(R.id.shoot);
        moveButton = findViewById(R.id.move);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backFunction();
            }
        });
        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
                rotateButton.startAnimation(rotation);
                rotate_character();
            }
        });
        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                move_character();
            }
        });
        shootButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //shoot();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel = new ViewModel();
        score = findViewById(R.id.score);
        arrow = findViewById(R.id.arrow);
        info_msg = findViewById(R.id.info_message);
        score.setText("Score: " + viewModel.score);
        arrow.setText("Arrows: " + viewModel.arrows);
        info_msg.setText(R.string.wumpus_alive);
        wallHitSound = MediaPlayer.create(getApplicationContext(), R.raw.wall_hit_sound);
        arrowSound = MediaPlayer.create(getApplicationContext(), R.raw.arrow_firing);
        arrowHitSound = MediaPlayer.create(getApplicationContext(), R.raw.arrow_impact);
        successSound = MediaPlayer.create(getApplicationContext(), R.raw.success_sound);
        moveSound = MediaPlayer.create(getApplicationContext(), R.raw.move_sound);
        looseSound = MediaPlayer.create(getApplicationContext(), R.raw.level_loose);
        fallingSound = MediaPlayer.create(getApplicationContext(), R.raw.falling_woah);
        perceiveBlockEnvironment();

    }

    protected void onResume() {
        //hiding system UI
        super.onResume();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().
                setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setBlocksEnvironment();
    }

    @Override
    protected void onStop() {
        super.onStop();
        wallHitSound.release();
        arrowSound.release();
        fallingSound.release();
        successSound.release();
        moveSound.release();
        arrowHitSound.release();
        looseSound.release();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backFunction();
    }

    void backFunction(){
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayActivity.this);
        builder.setTitle("\" PAUSED \"");
        builder.setMessage("Resume the game or leave?");
        builder.setCancelable(false);
        builder.setNegativeButton("Continue", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });
        builder.setPositiveButton("Exit", (DialogInterface.OnClickListener) (dialog, which) -> {
            finish();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    void rotate_character(){
        --viewModel.face;              //up,left,down,right = 1,2,3,4
        if (viewModel.face < 1) viewModel.face = 4;
        setCharacterImage();
    }
    void setCharacterImage(){
        switch (viewModel.face){
            case 1: blocks[viewModel.x_pos][viewModel.y_pos].setImageResource(R.drawable.daco_up); break;
            case 2: blocks[viewModel.x_pos][viewModel.y_pos].setImageResource(R.drawable.daco_left); break;
            case 3: blocks[viewModel.x_pos][viewModel.y_pos].setImageResource(R.drawable.daco_down); break;
            case 4: blocks[viewModel.x_pos][viewModel.y_pos].setImageResource(R.drawable.daco_right); break;
        }
    }
    void move_character(){
        //up,left,down,right = 1,2,3,4
        int x = viewModel.x_pos, y = viewModel.y_pos;
        switch (viewModel.face){
            case 1: --viewModel.x_pos;
                if (viewModel.x_pos == 0){
                    ++viewModel.x_pos;
                    wallHitSound();
                    return;
                }
                break;
            case 2: --viewModel.y_pos;
                if (viewModel.y_pos == 0){
                    ++viewModel.y_pos;
                    wallHitSound();
                    return;
                }
                break;
            case 3: ++viewModel.x_pos;
                if (viewModel.x_pos == 5){
                    --viewModel.x_pos;
                    wallHitSound();
                    return;
                }
                break;
            case 4: ++viewModel.y_pos;
                if (viewModel.y_pos == 7){
                    --viewModel.y_pos;
                    wallHitSound();
                    return;
                }
                break;
        }
        blocks[x][y].setImageResource(0);     //remove image from initial block
        moveSound.start();
        setCharacterImage();                 //place image in new block
        viewModel.score += 5;
        score.setText("Score: " + viewModel.score);
        if (!viewModel.space[viewModel.x_pos][viewModel.y_pos].visited) {
            viewModel.space[viewModel.x_pos][viewModel.y_pos].visited = true;
        }
        perceiveBlockEnvironment();                   //set the text messages
        if (viewModel.space[viewModel.x_pos][viewModel.y_pos].wumpus){
            info_msg.setText(R.string.wumpus_encounter);
            viewModel.score -= 25;
            score.setText("Score: " + viewModel.score);
            looseSound.start();
            blocks[viewModel.x_pos][viewModel.y_pos].setColorFilter(Color.parseColor("#ff0000"));
            blocks[viewModel.x_pos][viewModel.y_pos].animate().alpha(0f).setDuration(2000);
            blocks[viewModel.x_pos][viewModel.y_pos].setAlpha(1f);
            blocks[viewModel.x_pos][viewModel.y_pos].setBackgroundResource(R.drawable.wumpus);
            //blocks[viewModel.x_pos][viewModel.y_pos].setImageResource(0);
        } else if (viewModel.space[viewModel.x_pos][viewModel.y_pos].pit) {
            info_msg.setText("Woah! Dropped in a pit");
            viewModel.score -= 20;
            score.setText("Score: " + viewModel.score);
            fallingSound.start();
            looseSound.start();
        } else if (viewModel.space[viewModel.x_pos][viewModel.y_pos].gold) {
            info_msg.setText("Victory! Gold Found");
            viewModel.score += 50;
            score.setText("Score: " + viewModel.score);
            successSound.start();
        }
    }

     void perceiveBlockEnvironment(){
         Space curBlock = viewModel.space[viewModel.x_pos][viewModel.y_pos];
         String msg = "";
         if (curBlock.breeze){
             msg += "Breeze perceived! ";
         }
         if (curBlock.glitter){
             msg += "Glitter perceived! ";
         }
         if (curBlock.stench){
             msg += "Stench perceived! ";
         }
         info_msg.setText(msg);
     }
     void setBlocksEnvironment(){
        for (int i = 1; i<=4; i++){
            for (int j=1; j<=6; j++){
                if (viewModel.space[i][j].wumpus)
                    blocks[i][j].setBackgroundResource(R.drawable.wumpus);
                else if (viewModel.space[i][j].gold)
                    blocks[i][j].setBackgroundResource(R.drawable.gold);
                else if (viewModel.space[i][j].pit)
                    blocks[i][j].setBackgroundResource(R.drawable.pit);
                else if (viewModel.space[i][j].breeze && viewModel.space[i][j].glitter && viewModel.space[i][j].stench)
                    blocks[i][j].setBackgroundResource(R.drawable.breeze_glitter_stench);
                else if (viewModel.space[i][j].breeze && viewModel.space[i][j].glitter)
                    blocks[i][j].setBackgroundResource(R.drawable.breeze_glitter);
                else if (viewModel.space[i][j].breeze && viewModel.space[i][j].stench)
                    blocks[i][j].setBackgroundResource(R.drawable.breeze_stench);
                else if (viewModel.space[i][j].glitter && viewModel.space[i][j].stench)
                    blocks[i][j].setBackgroundResource(R.drawable.glitter_stench);
                else if (viewModel.space[i][j].breeze)
                    blocks[i][j].setBackgroundResource(R.drawable.breeze);
                else if (viewModel.space[i][j].glitter)
                    blocks[i][j].setBackgroundResource(R.drawable.glitter);
                else if (viewModel.space[i][j].stench)
                    blocks[i][j].setBackgroundResource(R.drawable.stench);
            }
        }
     }
    void wallHitSound(){
        info_msg.setText(R.string.hit_wall);
        viewModel.score -= 5;
        score.setText("Score: " + viewModel.score);
        wallHitSound.start();
    }

}

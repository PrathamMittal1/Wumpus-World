package com.example.wumpusworld;

import java.util.Arrays;

class ViewModel extends androidx.lifecycle.ViewModel {
    public int score;
    public int x_pos;
    public int y_pos;
    public int face;       //up,left,down,right = 1,2,3,4
    public int arrows;
    public Space[][] space = new Space[6][8];
    ViewModel(){
        x_pos = 1;
        y_pos = 1;
        face = 3;
        score = 0;                                     //Math.random() * ((max - min) + 1)) + min
        for (int i=0; i<6; i++)
            for (int j=0; j<8; j++)                      //initializing the board
                space[i][j] = new Space();
        int[] wumpusPos = {(int) (Math.random() * (4 - 1 + 1) + 1), (int) (Math.random() * (6 - 1 + 1) + 1)};

        while (wumpusPos[0] == 1 & wumpusPos[1] == 1){
            wumpusPos[0] = (int) (Math.random() * (4 - 1 + 1) + 1);
            wumpusPos[1] = (int) (Math.random() * (6 - 1 + 1) + 1);
        }                                                 //placing wumpus

        space[wumpusPos[0]][wumpusPos[1]].wumpus = true;

        int[] goldPos = {(int) (Math.random() * (4 - 1 + 1) + 1), (int) (Math.random() * (6 - 1 + 1) + 1)};
        while ((goldPos[0] == 1 & goldPos[1] == 1) |
                (goldPos[0] == wumpusPos[0] & goldPos[1] == wumpusPos[1])){
            goldPos[0] = (int) (Math.random() * (4 - 1 + 1) + 1);
            goldPos[1] = (int) (Math.random() * (6 - 1 + 1) + 1);
        }
        space[goldPos[0]][goldPos[1]].gold = true;           //placing gold goal

        int[] pit1 = {(int) (Math.random() * (4 - 1 + 1) + 1), (int) (Math.random() * (6 - 1 + 1) + 1)};
        int[] pit2 = {(int) (Math.random() * (4 - 1 + 1) + 1), (int) (Math.random() * (6 - 1 + 1) + 1)};

        while ((pit1[0] == 1 & pit1[1] == 1) |
                (pit1[0] == wumpusPos[0] & pit1[1] == wumpusPos[1]) |
                (pit1[0] == goldPos[0] & pit1[1] == goldPos[1])){
            pit1[0] = (int) (Math.random() * (4 - 1 + 1) + 1);
            pit1[1] = (int) (Math.random() * (6 - 1 + 1) + 1);
        }
        space[pit1[0]][pit1[1]].pit = true;                         //placing pit1

        while ((pit2[0] == 1 & pit2[1] == 1) |
                (pit2[0] == wumpusPos[0] & pit2[1] == wumpusPos[1]) |
                (pit2[0] == goldPos[0] & pit2[1] == goldPos[1]) |
                (pit2[0] == pit1[0] & pit2[1] == pit1[1])){
            pit1[0] = (int) (Math.random() * (4 - 1 + 1) + 1);
            pit1[1] = (int) (Math.random() * (6 - 1 + 1) + 1);
        }
        space[pit2[0]][pit2[1]].pit = true;                          //placing pit2

        System.out.println("WumpusPos " + Arrays.toString(wumpusPos));
        System.out.println("Gold " + Arrays.toString(goldPos));
        System.out.println("pit1 " + Arrays.toString(pit1));
        System.out.println("pit2 " + Arrays.toString(pit2));

        //placing stench around wumpus
        space[wumpusPos[0]][wumpusPos[1] + 1].stench = true;
        space[wumpusPos[0]][wumpusPos[1] - 1].stench = true;
        space[wumpusPos[0] + 1][wumpusPos[1]].stench = true;
        space[wumpusPos[0] - 1][wumpusPos[1]].stench = true;

        //placing breeze around pit1
        space[pit1[0]][pit1[1] + 1].breeze = true;
        space[pit1[0]][pit1[1] - 1].breeze = true;
        space[pit1[0] + 1][pit1[1]].breeze = true;
        space[pit1[0] - 1][pit1[1]].breeze = true;

        //placing breeze around pit2
        space[pit2[0]][pit2[1] + 1].breeze = true;
        space[pit2[0]][pit2[1] - 1].breeze = true;
        space[pit2[0] + 1][pit2[1]].breeze = true;
        space[pit2[0] - 1][pit2[1]].breeze = true;

        //placing glitter around gold goal
        space[goldPos[0]][goldPos[1] + 1].glitter = true;
        space[goldPos[0]][goldPos[1] - 1].glitter = true;
        space[goldPos[0] + 1][goldPos[1]].glitter = true;
        space[goldPos[0] - 1][goldPos[1]].glitter = true;


    }
}


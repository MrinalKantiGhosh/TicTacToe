package com.example.mrinal.tictactoe;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView image;
    TextView text;
    int activePlayer = 0;
    int gameState[][] = {{2,2,2},{2,2,2},{2,2,2}};
    int countTap = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.newGame){
            initializeGame();
            Toast.makeText(this,"New Game is Ready.....", Toast.LENGTH_SHORT).show();
        }else if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void imageTapped(View view){
        image = (ImageView) view;
        String position = image.getTag().toString();
        if(isUsed(position) == 0){
            countTap++;
            if(activePlayer == 0){
                changeGameState(position, activePlayer);
                image.setImageResource(R.drawable.cross);
                image.animate().rotationY(image.getRotationY() + 180).setDuration(500);
                if(checkWin(position)) {
                    showWinner(activePlayer);
                    endGame();
                }else if(countTap >= 9)
                {
                    text = (TextView) findViewById(R.id.winnerInfo);
                    text.setText("Game Draw......");
                    endGame();
                }
                activePlayer = 1;
            }else if(activePlayer == 1){
                changeGameState(position, activePlayer);
                image.setImageResource(R.drawable.circle);
                image.animate().rotationY(image.getRotationY() - 180).setDuration(500);
                if(checkWin(position)) {
                    showWinner(activePlayer);
                    endGame();
                }else if(countTap >= 9)
                {
                    text = (TextView) findViewById(R.id.winnerInfo);
                    text.setText("Game Draw......");
                    endGame();
                }
                activePlayer = 0;
            }
        }
        else if(isUsed(position) == 1)
            Toast.makeText(this, "This Position is already used.....", Toast.LENGTH_SHORT).show();
        else {
            Toast.makeText(this, "Game is End.....Start a new Game", Toast.LENGTH_SHORT).show();
        }

    }

    public void changeGameState(String position, int activePlayer){

        int row = Character.getNumericValue(position.charAt(0));
        int column = Character.getNumericValue(position.charAt(1));
        gameState[row][column] = activePlayer;
    }
    public int isUsed(String position){
        int row = Character.getNumericValue(position.charAt(0));
        int column = Character.getNumericValue(position.charAt(1));
        if(gameState[row][column] == 2)
            return 0;
        else if(gameState[row][column] == -1)
            return -1;
        else
            return 1;

    }

    public boolean checkWin(String position){
        int row = Character.getNumericValue(position.charAt(0));
        int column = Character.getNumericValue(position.charAt(1));
        int state = getState(row, column);
       // Log.i("checkWin 1 = ", ""+state);

        boolean verticalStatus = checkVertical(row, column, state, 3);
        boolean horizontalStatus = checkHorizontal(row, column, state,3);

        boolean diagonalStatusLR = false;
        boolean diagonalStatusRL = false;
        if(Math.abs(row - column) == 2 || row == 1 && column == 1) {
            diagonalStatusLR = checkDiagonalLR(row, column, state, 3);
        }
        if(row == column)
            diagonalStatusRL = checkDiagonalRL(row, column, state, 3);

        if(verticalStatus || horizontalStatus || diagonalStatusLR || diagonalStatusRL)
            return true;
        else
            return false;

    }

    public boolean checkVertical(int row, int column, int state, int count){
        int currentState = getState(row, column);
        if(count == 0) {
            //Log.i("Vertical 1st if = ", ""+state);
            return true;
        }
        if(count > 0 && currentState != state) {
            //Log.i("Vertical 2nd if = ", ""+state);
            return false;
        }
        else {
            row++;
            row = changeBoundary(row);
            //Log.i("Vertical else = ", ""+state);
            return checkVertical(row, column, state, count - 1);
        }

    }

    public boolean checkHorizontal(int row, int column, int state, int count){
        int currentState = getState(row, column);
        if(count == 0)
            return true;
        if(count > 0 && currentState != state)
            return false;
        else{
            column++;
            column = changeBoundary(column);
            return checkHorizontal(row, column, state, count-1);
        }

    }

    public boolean checkDiagonalLR(int row, int column, int state, int count){
        int currentState = getState(row, column);
        if(count == 0)
            return true;
        if(count > 0 && currentState != state)
            return false;
        else {
            row--;
            row = changeBoundary(row);
            column++;
            column = changeBoundary(column);
            return checkDiagonalLR(row, column, state, count - 1);
        }

    }

    public boolean checkDiagonalRL(int row, int column, int state, int count){
        int currentState = getState(row, column);
        if(count == 0)
            return true;
        if(count > 0 && currentState != state)
            return false;
        else {
            row++;
            row = changeBoundary(row);
            column++;
            column = changeBoundary(column);
            return checkDiagonalRL(row, column, state, count - 1);
        }

    }

    public int getState(int row, int column){
        return gameState[row][column];
    }

    public void endGame(){
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                gameState[i][j] = -1;
    }

    public void initializeImage(int horizontalLayoutId){
        LinearLayout linearLayout = (LinearLayout) findViewById(horizontalLayoutId);
        for(int i = 0; i < linearLayout.getChildCount(); i++){
            ImageView currentImage = (ImageView) linearLayout.getChildAt(i);
            currentImage.setImageResource(R.drawable.blank);
            currentImage.animate().rotationBy(-180).setDuration(800);
        }
    }

    public void initializeGame(){
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                gameState[i][j] = 2;

        activePlayer = 0;

        initializeImage(R.id.firstRow);
        initializeImage(R.id.secondRow);
        initializeImage(R.id.thirdRow);



        countTap = 0;

        text = (TextView) findViewById(R.id.winnerInfo);
        text.setText(R.string.WinInfo);
        text.animate().translationY(10);

    }

    public int changeBoundary(int position){
        if(position == 3)
            position = 0;
        if(position == -1)
            position = 2;
        return  position;
    }

    public void showWinner(int activePlayer){
        text = (TextView)findViewById(R.id.winnerInfo);
        if(activePlayer == 0)
            text.setText("Player 1 Wins");
        else
            text.setText("Player 2 Wins");
        text.animate().rotationYBy(720).translationY(-180).setDuration(1000);


    }

}

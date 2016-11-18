package com.google.engedu.wordstack;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private static final int WORD_LENGTH = 4;
    public static final int LIGHT_BLUE = Color.rgb(176, 200, 255);
    public static final int LIGHT_GREEN = Color.rgb(200, 255, 200);
    public boolean isClickedFirstTime = true;
    private ArrayList<String> words = new ArrayList<>();
    private Random random = new Random();
    private StackedLayout stackedLayout;
    private Stack<LetterTile> placedTiles = new Stack<>();
    private String word1, word2;
    private String scrambledWord = "";
    private Queue<Character> queue1 = new LinkedList<>();
    private Queue<Character> queue2 = new LinkedList<>();
    private LinearLayout word1LinearLayout;
    private LinearLayout word2LinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while((line = in.readLine()) != null) {
                String word = line.trim();
                //my code
                if(word.length() == WORD_LENGTH){
                    words.add(word);
                }
            }
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        LinearLayout verticalLayout = (LinearLayout) findViewById(R.id.vertical_layout);
        stackedLayout = new StackedLayout(this);
        verticalLayout.addView(stackedLayout, 3);

        word1LinearLayout = (LinearLayout)findViewById(R.id.word1);
        //word1LinearLayout.setOnTouchListener(new TouchListener());
        word1LinearLayout.setOnDragListener(new DragListener());
        word2LinearLayout = (LinearLayout)findViewById(R.id.word2);
        //word2LinearLayout.setOnTouchListener(new TouchListener());
        word2LinearLayout.setOnDragListener(new DragListener());
    }

    private class TouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && !stackedLayout.empty()) {
                LetterTile tile = (LetterTile) stackedLayout.peek();
                tile.moveToViewGroup((ViewGroup) v);
                if (stackedLayout.empty()) {
                    TextView messageBox = (TextView) findViewById(R.id.message_box);
                    messageBox.setText(word1 + " " + word2);
                }
                /**
                 **
                 **  YOUR CODE GOES HERE
                 **
                 **/
                placedTiles.push(tile);

                return true;
            }
            return false;
        }
    }

    private class DragListener implements View.OnDragListener {

        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundColor(LIGHT_GREEN);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundColor(Color.WHITE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign Tile to the target Layout
                    LetterTile tile = (LetterTile) event.getLocalState();
                    tile.moveToViewGroup((ViewGroup) v);
                    if (stackedLayout.empty()) {
                        TextView messageBox = (TextView) findViewById(R.id.message_box);
                        messageBox.setText(word1 + " " + word2);
                    }
                    /**
                     **
                     **  YOUR CODE GOES HERE
                     **
                     **/
                    placedTiles.push(tile);
                    return true;
            }
            return false;
        }
    }

    protected boolean onStartGame(View view) {
        TextView messageBox = (TextView) findViewById(R.id.message_box);
        messageBox.setText("Game started");
        //my code

        if(isClickedFirstTime){

            word1 = words.get((int)(Math.random()*words.size()));
            word2 = words.get((int)(Math.random()*words.size()));
            //for(int i = 0; i < 2; i++){
            //    int random = (int)(Math.random()*words.size());
            //    if(word1 == null){
            //        word1 = words.get(random);
            //    }else {
            //        word2 = words.get(random);
            //    }
            //}
            for(int i = 0; i < WORD_LENGTH; i++){
                queue1.add(word1.charAt(i));
                queue2.add(word2.charAt(i));
            }
            for(int i = 0; i< 8; i++){
                int random =(int)(Math.random()*2);
                Log.d("random ", " rolled: " + random);


                if(random == 0 && queue1.peek() != null){
                    scrambledWord += queue1.remove();
                }else if(random == 0 && queue1.peek() == null && queue2.peek() != null){
                    scrambledWord +=queue2.remove();
                }else if(random == 1 && queue2.peek() != null){
                    scrambledWord += queue2.remove();
                }else if(random == 1 && queue2.peek() == null && queue1.peek() != null){
                    scrambledWord += queue1.remove();
                }
                //if(queue1.peek() == null){
                //   scrambledWord += queue2.remove();
                //}else{
                //    scrambledWord += queue1.remove();
                //}
                messageBox.setText(scrambledWord);

            }
            Log.d("Queue", "Word 1 peek: " + queue1.peek());
            Log.d("Queue", "Word 2 peek: " + queue2.peek());
            Log.d("Words:", "word1: " + word1);
            Log.d("Words:", "word2: " + word2);
            Log.d("New Word", "Scrambled Word: " + scrambledWord);

            for(int i = scrambledWord.length() -1; i >= 0; i--){
                LetterTile letterTile = new LetterTile(this, scrambledWord.charAt(i));
                Log.d("Letter ", "letter : " + scrambledWord.charAt(i));
                stackedLayout.push(letterTile);
            }
            isClickedFirstTime = false;
        }else{
            //View word1LinearLayout = findViewById(R.id.word1);
            word1LinearLayout.removeAllViews();
            word2LinearLayout.removeAllViews();
            stackedLayout.clear();
            scrambledWord = "";
            isClickedFirstTime = true;
        }

        return true;
    }

    protected boolean onUndo(View view) {
        //mycode
        LetterTile popped = placedTiles.pop();
        popped.moveToViewGroup(stackedLayout);

        return true;
    }
}

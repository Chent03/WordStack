package com.google.engedu.wordstack;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Stack;

public class StackedLayout extends LinearLayout {

    private Stack<View> tiles = new Stack();

    public StackedLayout(Context context) {
        super(context);
    }

    public void push(View tile) {
        //mycode
        //if(!empty()){
        //    this.removeView(tiles.peek());
        //}
        tiles.push(tile);
        this.addView(tiles.peek());
    }

    public View pop() {
        View popped = tiles.pop();
        this.removeView(popped);
        //if(!empty()){
        //    this.addView(tiles.peek());
        //}
        return popped;
    }

    public View peek() {
        return tiles.peek();
    }

    public boolean empty() {
        return tiles.empty();
    }

    public void clear() {
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
        View word1LinearLayout = findViewById(R.id.word1);
        View word2LinearLayout = findViewById(R.id.word2);
        removeViewInLayout(word1LinearLayout);
        removeViewInLayout(word2LinearLayout);

        removeView(word1LinearLayout);
        removeView(word2LinearLayout);
        removeAllViews();
    }
}

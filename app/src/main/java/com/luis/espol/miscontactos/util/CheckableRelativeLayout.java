package com.luis.espol.miscontactos.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luis on 18/03/2017.
 */
public class CheckableRelativeLayout extends RelativeLayout implements Checkable{
    private boolean isChecked;
    private List<Checkable> checkableViews;

    public CheckableRelativeLayout(Context context) {
        super(context);
        inicializar(null);
    }

    public CheckableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        inicializar(attrs);
    }

    public CheckableRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inicializar(attrs);
    }

    private void inicializar(Object o) {
        this.isChecked=false;
        this.checkableViews=new ArrayList<Checkable>();
    }

    @Override
    public void setChecked(boolean isChecked) {
        this.isChecked=isChecked;
        for(Checkable c:checkableViews)c.setChecked(isChecked);
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        this.isChecked=!this.isChecked;
        for(Checkable c:checkableViews)c.toggle();
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final int childCount=this.getChildCount();
        for(int i=0;i<childCount;i++){
            buscarComponentesCheckable(this.getChildAt(i));
        }
    }

    private void buscarComponentesCheckable(View view) {
        if(view instanceof Checkable)this.checkableViews.add((Checkable)view);
        if(view instanceof ViewGroup){
            final ViewGroup vg=(ViewGroup)view;
            final int childCount=vg.getChildCount();
            for(int i=0;i<childCount;i++){
                buscarComponentesCheckable(vg.getChildAt(i));
            }
        }
    }
}

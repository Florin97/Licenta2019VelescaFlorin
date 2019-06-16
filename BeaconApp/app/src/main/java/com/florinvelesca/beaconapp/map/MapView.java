package com.florinvelesca.beaconapp.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.florinvelesca.beaconapp.database.ClassroomCoordinates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Ionut Stirban on 2019-05-05.
 */

@SuppressLint("AppCompatCustomView")
public class MapView extends ImageView {
    private static final int RADIUS = 15;

    private Paint pathToFollowPaint = new Paint();
    private Paint visitedPaint = new Paint();
    private Map<String, ClassroomCoordinates> classroomCoordinates;


    //
    private List<String> visitedRooms = new ArrayList<>();

    private List<String> pathToFollow = Collections.emptyList();



    public MapView(Context context) {
        super(context);
    }

    public MapView(Context context, @androidx.annotation.Nullable @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MapView(Context context, @androidx.annotation.Nullable @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MapView(Context context, @androidx.annotation.Nullable @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setClassroomCoordinates(Map<String, ClassroomCoordinates> classroomCoordinates) {
        this.classroomCoordinates = classroomCoordinates;
    }

    public void setPathToFollow(List<String> pathToFollow) {
        this.pathToFollow = pathToFollow;
    }

    public void addVisitedRoom(String visitedRoom) {
        visitedRooms.add(visitedRoom);
        invalidate();
    }

    public Boolean isVisited(String beaconName){
        if(visitedRooms.contains(beaconName) && !visitedRooms.get(visitedRooms.size() -1).equals(beaconName)){
            Log.d("MAP VIEW","Wrong direction");
            return true;
        }
        return false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        pathToFollowPaint.setColor(Color.BLUE);
        pathToFollowPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        visitedPaint.setColor(Color.RED);
        visitedPaint.setStyle(Paint.Style.FILL_AND_STROKE);

//        List<Classroom> classrooms = new ArrayList<>(5);
//        int y = 250;
//        int stepSize = 340;
//        for (int i = 0; i < 5; i++) {
//            Classroom classroom = new Classroom();
//            classroom.setName("c30" + i);
//            classroom.setX(stepSize / 2 + stepSize * i);
//            classroom.setY(y);
//            classrooms.add(classroom);
//        }
//
//        String json = new Gson().toJson(classrooms);
//        Log.d("json", json);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        int y = 250;
//        int stepSize = 340;
//        for (int i = 0; i < 5; i++) {
//            canvas.drawCircle(stepSize / 2 + stepSize * i, y, 20, pathToFollowPaint);
//        }

        //filtreaze room de la etajul curent

        if (classroomCoordinates == null || classroomCoordinates.isEmpty()) {
            return;
        }
        for (String room : pathToFollow) {
            ClassroomCoordinates classroomCoordinates = this.classroomCoordinates.get(room);
            canvas.drawCircle(classroomCoordinates.getX(), classroomCoordinates.getY(), RADIUS, pathToFollowPaint);
        }

        for (String visitedRoom : visitedRooms) {
            ClassroomCoordinates classroomCoordinates = this.classroomCoordinates.get(visitedRoom);
            canvas.drawCircle(classroomCoordinates.getX(), classroomCoordinates.getY(), RADIUS, visitedPaint);
        }
    }


}

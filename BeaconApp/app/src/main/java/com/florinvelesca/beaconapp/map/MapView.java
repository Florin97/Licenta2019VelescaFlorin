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

import com.florinvelesca.beaconapp.database.BeaconTable;
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
    private static final int RADIUS = 18;

    private Paint pathToFollowPaint = new Paint();
    private Paint visitedPaint = new Paint();
    private Paint currentRoomPaint = new Paint();

    private Map<String, ClassroomCoordinates> classroomCoordinates;

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    private int currentFloor;

    //
    private List<BeaconTable> visitedRooms = new ArrayList<>();

    private List<BeaconTable> pathToFollow = Collections.emptyList();

    private BeaconTable currentRoom = null;

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

    public void setPathToFollow(List<BeaconTable> pathToFollow) {
        this.pathToFollow = pathToFollow;
    }

    public void addVisitedRoom(BeaconTable visitedRoom) {
        currentRoom = visitedRoom;
        if (!visitedRooms.contains(visitedRoom) && pathToFollow.contains(visitedRoom)) {
            visitedRooms.add(visitedRoom);
        }
        invalidate();
    }

    public Boolean isVisited(BeaconTable beaconName) {

        if (visitedRooms.contains(beaconName) && !visitedRooms.get(visitedRooms.size() - 1).equals(beaconName)) {
            Log.d("MAP VIEW", "Wrong direction");
            return true;
        }
        return false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        pathToFollowPaint.setColor(0xff00BCD4);
        pathToFollowPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        visitedPaint.setColor(0xff8BC34A);
        visitedPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        currentRoomPaint.setColor(Color.BLACK);
        currentRoomPaint.setStyle(Paint.Style.FILL_AND_STROKE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (classroomCoordinates == null || classroomCoordinates.isEmpty()) {
            return;
        }
        for (BeaconTable room : pathToFollow) {
            if (room.getFloor() != currentFloor) {
                continue;
            }
            ClassroomCoordinates classroomCoordinates = this.classroomCoordinates.get(room.getClassRoomName());
            canvas.drawCircle(classroomCoordinates.getX(), classroomCoordinates.getY(), RADIUS, pathToFollowPaint);
        }

        for (BeaconTable visitedRoom : visitedRooms) {
            if (visitedRoom.getFloor() != currentFloor) {
                continue;
            }

            ClassroomCoordinates classroomCoordinates = this.classroomCoordinates.get(visitedRoom.getClassRoomName());

            canvas.drawCircle(classroomCoordinates.getX(), classroomCoordinates.getY(), RADIUS, visitedPaint);
        }

        if (currentRoom != null) {
            ClassroomCoordinates classroomCoordinates = this.classroomCoordinates.get(currentRoom.getClassRoomName());
            canvas.drawCircle(classroomCoordinates.getX(), classroomCoordinates.getY(), RADIUS * 1.3f, visitedPaint);
            canvas.drawCircle(classroomCoordinates.getX(), classroomCoordinates.getY(), RADIUS * 0.4f, currentRoomPaint);
        }
    }


}

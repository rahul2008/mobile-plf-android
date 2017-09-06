/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.connectivitypowersleep;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.screens.utility.RALog;

public class SleepScoreProgressView extends View {
    
    private static final int DEFAULT_ANGLE = 0;
    private static final int DEFAULT_STROKE_SIZE = 0;
    private static final int DEFAULT_STARTING_POINT_WIDTH = 0;
    private static final int DEFAULT_STARTING_POINT_HEIGHT = 0;
    private static final int DEFAULT_DOT_RADIUS = 0;
    private static final int DEFAULT_REFERENCE_CIRCLE_SIZE = 0;

    private static final int START_ANGLE_POINT = 90;
    private static final int DELTA_BOOST_ANGLE = 1;
    private static final int DEFAULT_WIDTH_MULTIPLIER = 2;

    private Paint paintStroke;
    private Paint paintFill;

    private RectF rectStroke;
    private RectF rectStartingPoint;

    private int totalViewWidth;
    private int totalViewHeight;

    private final int colorBoost;
    private final int colorRegular;
    private final int colorStartingPoint;

    private final int dotRadius;
    private float strokeRectRadius;

    private float angleScore;
    private float angleBoost;

    public SleepScoreProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray customTypedArray = context.obtainStyledAttributes(attrs, R.styleable.SleepScoreProgressView);
        float referenceCircleSize = customTypedArray.getDimensionPixelSize(R.styleable.SleepScoreProgressView_ap_reference_circle_size, DEFAULT_REFERENCE_CIRCLE_SIZE);
        colorRegular = customTypedArray.getColor(R.styleable.SleepScoreProgressView_ap_regular_stroke_color, Color.WHITE);
        float strokeSize = customTypedArray.getDimensionPixelSize(R.styleable.SleepScoreProgressView_ap_stroke_size, DEFAULT_STROKE_SIZE);
        colorBoost = customTypedArray.getColor(R.styleable.SleepScoreProgressView_ap_boost_stroke_color, Color.WHITE);
        colorStartingPoint = customTypedArray.getColor(R.styleable.SleepScoreProgressView_ap_starting_point_color, Color.WHITE);
        float startingPointWidth = customTypedArray.getDimensionPixelSize(R.styleable.SleepScoreProgressView_ap_starting_point_width, DEFAULT_STARTING_POINT_WIDTH);
        float startingPointHeight = customTypedArray.getDimensionPixelSize(R.styleable.SleepScoreProgressView_ap_starting_point_height, DEFAULT_STARTING_POINT_HEIGHT);
        dotRadius = customTypedArray.getDimensionPixelSize(R.styleable.SleepScoreProgressView_ap_dot_radius, DEFAULT_DOT_RADIUS);
        angleScore = customTypedArray.getFloat(R.styleable.SleepScoreProgressView_ap_default_angle, DEFAULT_ANGLE);
        customTypedArray.recycle();

        findTotalViewSize(referenceCircleSize, strokeSize);
        initPaint(strokeSize);
        initRectangles(referenceCircleSize, strokeSize, startingPointWidth, startingPointHeight);
    }

    public float getScoreAngle(){
        return angleScore;
    }

    /* Score from 0 to 100 */
    public void setScoreAngle(float scoreAngle){
        this.angleScore = scoreAngle;
        RALog.d("SCORE", "boostScore:"+ scoreAngle+ ", angleScore:"+ angleScore);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawStartingPoint(canvas);
        drawRegularScoreStroke(canvas);
        drawBoostScoreStrokeIfNeeded(canvas, angleBoost);
        drawDot(canvas, angleBoost);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(totalViewWidth, totalViewHeight);
    }

    private void findTotalViewSize(float referenceCircleSize, float strokeSize) {
        totalViewWidth = (int) (referenceCircleSize + DEFAULT_WIDTH_MULTIPLIER * strokeSize + DEFAULT_WIDTH_MULTIPLIER * dotRadius);
        totalViewHeight = (int) (referenceCircleSize + DEFAULT_WIDTH_MULTIPLIER * strokeSize + DEFAULT_WIDTH_MULTIPLIER * dotRadius);
    }

    private void initRectangles(float referenceCircleSize, float strokeSize, float startingPointWidth, float startingPointHeight) {
        rectStroke = new RectF(
                dotRadius,
                dotRadius,
                dotRadius + referenceCircleSize + DEFAULT_WIDTH_MULTIPLIER * strokeSize,
                dotRadius + referenceCircleSize + DEFAULT_WIDTH_MULTIPLIER * strokeSize
        );
        rectStroke.inset(strokeSize / DEFAULT_WIDTH_MULTIPLIER, strokeSize / DEFAULT_WIDTH_MULTIPLIER);
        strokeRectRadius = rectStroke.width() / DEFAULT_WIDTH_MULTIPLIER;
        rectStartingPoint = new RectF(
                rectStroke.centerX() - startingPointWidth / DEFAULT_WIDTH_MULTIPLIER,
                rectStroke.centerY() + rectStroke.height() / DEFAULT_WIDTH_MULTIPLIER - startingPointHeight / DEFAULT_WIDTH_MULTIPLIER,
                rectStroke.centerY() + startingPointWidth / DEFAULT_WIDTH_MULTIPLIER,
                rectStroke.centerY() + rectStroke.height() / DEFAULT_WIDTH_MULTIPLIER + startingPointHeight / DEFAULT_WIDTH_MULTIPLIER
        );
    }

    private void initPaint(float strokeSize) {
        paintStroke = new Paint();
        paintStroke.setAntiAlias(true);
        paintStroke.setStyle(Paint.Style.STROKE);
        paintStroke.setStrokeWidth(strokeSize);

        paintFill = new Paint();
        paintFill.setAntiAlias(true);
        paintFill.setStyle(Paint.Style.FILL);
    }

    private void drawStartingPoint(Canvas canvas) {
        paintFill.setColor(colorStartingPoint);
        canvas.drawRect(rectStartingPoint, paintFill);
    }

    private void drawRegularScoreStroke(Canvas canvas) {
        paintStroke.setColor(colorRegular);
        canvas.drawArc(rectStroke, START_ANGLE_POINT, angleScore, false, paintStroke);
    }

    private void drawBoostScoreStrokeIfNeeded(Canvas canvas, float angleBoost) {
        if (angleBoost > 0) {
            paintStroke.setColor(colorBoost);
            canvas.drawArc(rectStroke, START_ANGLE_POINT + DELTA_BOOST_ANGLE + angleScore, angleBoost, false, paintStroke);
        }
    }

    private void drawDot(Canvas canvas, float angleBoost) {
        float endStrokePointX;
        float endStrokePointY;
        if (angleBoost > 0) {
            paintFill.setColor(colorBoost);
            endStrokePointX = getCenterPointX(rectStroke, strokeRectRadius, START_ANGLE_POINT + angleBoost + DELTA_BOOST_ANGLE + angleScore);
            endStrokePointY = getCenterPointY(rectStroke, strokeRectRadius, START_ANGLE_POINT + angleBoost + DELTA_BOOST_ANGLE + angleScore);
        } else {
            paintFill.setColor(colorRegular);
            endStrokePointX = getCenterPointX(rectStroke, strokeRectRadius, START_ANGLE_POINT + angleScore);
            endStrokePointY = getCenterPointY(rectStroke, strokeRectRadius, START_ANGLE_POINT + angleScore);
        }
        canvas.drawCircle(endStrokePointX, endStrokePointY, dotRadius, paintFill);
    }

    private float getCenterPointX(RectF rectF, float radius, float tetaAngle) {
        return (float) (Math.cos(Math.toRadians(tetaAngle)) * (radius) + rectF.centerX());
    }

    private float getCenterPointY(RectF rectF, float radius, float tetaAngle) {
        return (float) (Math.sin(Math.toRadians(tetaAngle)) * (radius) + rectF.centerY());
    }
}
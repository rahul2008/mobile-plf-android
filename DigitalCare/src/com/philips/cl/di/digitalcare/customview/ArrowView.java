package com.philips.cl.di.digitalcare.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 
 * @author naveen@philips.com
 * @description View to show arrow mark in PopupWindowView.
 * @Since March 26, 2015
 */
public class ArrowView extends LinearLayout {

	public ArrowView(Context context) {
		super(context);
		commonConstructor(context);
	}

	public ArrowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		commonConstructor(context);
	}

	private Paint trianglePaint;

	private Path trianglePath;

	private void commonConstructor(Context context) {
		trianglePaint = new Paint();
		trianglePaint.setStyle(Style.FILL);
		trianglePaint.setColor(Color.RED);
		Point point = new Point();
		point.x = 80;
		point.y = 80;
		trianglePath = getEquilateralTriangle(point, 10, Direction.NORTH);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		canvas.drawPath(trianglePath, trianglePaint);
	}

	private Path getEquilateralTriangle(Point p1, int width, Direction direction) {
		Point p2 = null, p3 = null;

		if (direction == Direction.NORTH) {
			p2 = new Point(p1.x + width, p1.y);
			p3 = new Point(p1.x + (width / 2), p1.y - width);
		} else if (direction == Direction.SOUTH) {
			p2 = new Point(p1.x + width, p1.y);
			p3 = new Point(p1.x + (width / 2), p1.y + width);
		} else if (direction == Direction.EAST) {
			p2 = new Point(p1.x, p1.y + width);
			p3 = new Point(p1.x - width, p1.y + (width / 2));
		} else if (direction == Direction.WEST) {
			p2 = new Point(p1.x, p1.y + width);
			p3 = new Point(p1.x + width, p1.y + (width / 2));
		}

		Path path = new Path();
		path.moveTo(p1.x, p1.y);
		path.lineTo(p2.x, p2.y);
		path.lineTo(p3.x, p3.y);

		return path;
	}

	public enum Direction {
		NORTH, SOUTH, EAST, WEST;
	}

}
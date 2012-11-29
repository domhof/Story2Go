package at.ac.tuwien.igw.story2go;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.util.AttributeSet;
import android.view.View;

public class CompassView extends View {

	private float direction = 0;
	private float bearing = 0;
	private Paint paintDirection = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintBearing = new Paint(Paint.ANTI_ALIAS_FLAG);
	private boolean firstDraw;

	public CompassView(Context context) {
		super(context);
		init();
	}

	public CompassView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CompassView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {

		paintDirection.setStyle(Paint.Style.STROKE);
		paintDirection.setStrokeWidth(2);
		paintDirection.setColor(0x10000000);

		paintBearing.setStyle(Paint.Style.STROKE);
		paintBearing.setStrokeWidth(10);
		paintBearing.setColor(Color.GREEN);
		paintBearing.setStrokeCap(Cap.ROUND);
		paintBearing.setShadowLayer(10, 0, 0, 0x7000ff00);

		firstDraw = true;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
				MeasureSpec.getSize(heightMeasureSpec));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int cxCompass = getMeasuredWidth() / 2;
		int cyCompass = getMeasuredHeight() / 2;
		float radiusCompass;

		if (cxCompass > cyCompass) {
			radiusCompass = (float) (cyCompass * 0.9);
		} else {
			radiusCompass = (float) (cxCompass * 0.9);
		}
		canvas.drawCircle(cxCompass, cyCompass, radiusCompass, paintDirection);
		// canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(),
		// paint);

		if (!firstDraw) {

			canvas.drawLine(
					cxCompass,
					cyCompass,
					(float) (cxCompass + radiusCompass
							* Math.sin((double) (-direction) * 3.14 / 180)),
					(float) (cyCompass - radiusCompass
							* Math.cos((double) (-direction) * 3.14 / 180)),
					paintDirection);

			canvas.drawLine(
					cxCompass,
					cyCompass,
					(float) (cxCompass + radiusCompass
							* Math.sin((double) (-bearing) * 3.14 / 180)),
					(float) (cyCompass - radiusCompass
							* Math.cos((double) (-bearing) * 3.14 / 180)),
					paintBearing);

			// canvas.drawText(String.valueOf(direction), cxCompass, cyCompass,
			// paint);
		}

	}

	public void updateDirection(float direction) {
		firstDraw = false;
		this.direction = direction;
		invalidate();
	}

	public void updateBearing(float bearing) {
		firstDraw = false;
		this.bearing = bearing;
		invalidate();
	}

}

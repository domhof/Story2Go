package at.ac.tuwien.igw.story2go;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

// Tutorial: http://mindtherobot.com/blog/272/android-custom-ui-making-a-vintage-thermometer/
public class CompassView extends View {

	private static final String TAG = CompassView.class.getSimpleName();

	// Rim
	private RectF rimRect;
	private Paint rimPaint;
	private Paint rimCirclePaint;

	// Face
	private RectF faceRect;
	private Bitmap faceTexture;
	private Paint facePaint;
	private Paint rimShadowPaint;

	// Scale
	private Paint scalePaint;
	private RectF scaleRect;

	private float direction = 0;
	private float bearing = 0;
	private Paint paintDirection = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintPointer = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint handScrewPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Path pointerPath;

	// scale configuration
	private static final int totalNicks = 8;
	private static final float degreesPerNick = 360.0f / totalNicks;
	private static final String[] compassLabels = { "N", "NE", "E", "SE", "S",
			"SW", "W", "NW" };

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

		// paint for pointer
		paintPointer.setAntiAlias(true);
		paintPointer.setColor(0xff392f2c);
		paintPointer.setShadowLayer(0.01f, -0.005f, -0.005f, 0x7f000000);
		paintPointer.setStyle(Paint.Style.FILL);

		handScrewPaint.setAntiAlias(true);
		handScrewPaint.setColor(0xff493f3c);
		handScrewPaint.setStyle(Paint.Style.FILL);

		// Rim
		rimRect = new RectF(0.1f, 0.1f, 0.9f, 0.9f);

		// The linear gradient is a bit skewed for realism
		rimPaint = new Paint();
		rimPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		rimPaint.setShader(new LinearGradient(0.40f, 0.0f, 0.60f, 1.0f, Color
				.rgb(0xf0, 0xf5, 0xf0), Color.rgb(0x30, 0x31, 0x30),
				Shader.TileMode.CLAMP));

		rimCirclePaint = new Paint();
		rimCirclePaint.setAntiAlias(true);
		rimCirclePaint.setStyle(Paint.Style.STROKE);
		rimCirclePaint.setColor(Color.argb(0x4f, 0x33, 0x36, 0x33));
		rimCirclePaint.setStrokeWidth(0.005f);

		// Face
		float rimSize = 0.02f;
		faceRect = new RectF();
		faceRect.set(rimRect.left + rimSize, rimRect.top + rimSize,
				rimRect.right - rimSize, rimRect.bottom - rimSize);

		faceTexture = BitmapFactory.decodeResource(getContext().getResources(),
				R.drawable.plastic);
		BitmapShader paperShader = new BitmapShader(faceTexture,
				Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
		Matrix paperMatrix = new Matrix();
		facePaint = new Paint();
		facePaint.setFilterBitmap(true);
		paperMatrix.setScale(1.0f / faceTexture.getWidth(),
				1.0f / faceTexture.getHeight());
		paperShader.setLocalMatrix(paperMatrix);
		facePaint.setStyle(Paint.Style.FILL);
		facePaint.setShader(paperShader);

		rimShadowPaint = new Paint();
		rimShadowPaint.setShader(new RadialGradient(0.5f, 0.5f, faceRect
				.width() / 2.0f,
				new int[] { 0x00000000, 0x00000500, 0x50000500 }, new float[] {
						0.96f, 0.96f, 0.99f }, Shader.TileMode.MIRROR));
		rimShadowPaint.setStyle(Paint.Style.FILL);

		// Scale
		scalePaint = new Paint();
		scalePaint.setStyle(Paint.Style.STROKE);
		scalePaint.setColor(0x90000000);
		scalePaint.setStrokeWidth(0.005f);
		scalePaint.setAntiAlias(true);

		scalePaint.setTextSize(0.045f);
		scalePaint.setTypeface(Typeface.SANS_SERIF);
		scalePaint.setTextAlign(Paint.Align.CENTER);

		float scalePosition = 0.10f;
		scaleRect = new RectF();
		scaleRect.set(faceRect.left + scalePosition, faceRect.top
				+ scalePosition, faceRect.right - scalePosition,
				faceRect.bottom - scalePosition);

		loadPointerPath();

		// firstDraw = true;
	}

	private void loadPointerPath() {
		pointerPath = new Path();
		pointerPath.moveTo(0.5f, 0.5f + 0.2f);
		pointerPath.lineTo(0.5f - 0.010f, 0.5f + 0.2f - 0.007f);
		pointerPath.lineTo(0.5f - 0.002f, 0.5f - 0.32f);
		pointerPath.lineTo(0.5f + 0.002f, 0.5f - 0.32f);
		pointerPath.lineTo(0.5f + 0.010f, 0.5f + 0.2f - 0.007f);
		pointerPath.lineTo(0.5f, 0.5f + 0.2f);
		pointerPath.addCircle(0.5f, 0.5f, 0.025f, Path.Direction.CW);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Log.d(TAG, "Width spec: " + MeasureSpec.toString(widthMeasureSpec));
		Log.d(TAG, "Height spec: " + MeasureSpec.toString(heightMeasureSpec));

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);

		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int chosenWidth = chooseDimension(widthMode, widthSize);
		int chosenHeight = chooseDimension(heightMode, heightSize);

		int chosenDimension = Math.min(chosenWidth, chosenHeight);

		setMeasuredDimension(chosenDimension, chosenDimension);
	}

	private int chooseDimension(int mode, int size) {
		if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.EXACTLY) {
			return size;
		} else { // (mode == MeasureSpec.UNSPECIFIED)
			return getPreferredSize();
		}
	}

	// in case there is no size specified
	private int getPreferredSize() {
		return 300;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		float scale = (float) getWidth();
		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		canvas.scale(scale, scale);

		drawRim(canvas);
		drawFace(canvas);
		drawScale(canvas);
		drawPointer(canvas);

		canvas.restore();
	}

	private void drawRim(Canvas canvas) {
		// first, draw the metallic body
		canvas.drawOval(rimRect, rimPaint);
		// now the outer rim circle
		canvas.drawOval(rimRect, rimCirclePaint);
	}

	private void drawFace(Canvas canvas) {
		canvas.drawOval(faceRect, facePaint);
		// draw the inner rim circle
		canvas.drawOval(faceRect, rimCirclePaint);
		// draw the rim shadow inside the face
		canvas.drawOval(faceRect, rimShadowPaint);
	}

	private void drawScale(Canvas canvas) {
		canvas.drawOval(scaleRect, scalePaint);

		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		canvas.rotate(-direction, 0.5f, 0.5f);
		for (int i = 0; i < totalNicks; ++i) {
			float y1 = scaleRect.top;
			float y2 = y1 - 0.020f;

			canvas.drawLine(0.5f, y1, 0.5f, y2, scalePaint);

			canvas.drawText(compassLabels[i % compassLabels.length], 0.5f,
					y2 - 0.015f, scalePaint);

			canvas.rotate(degreesPerNick, 0.5f, 0.5f);
		}
		canvas.restore();
	}

	private void drawPointer(Canvas c) {
		try {
			// rotate pointer according to value
			// float handAngle = degreeToAngle(handPosition);
			c.save(Canvas.MATRIX_SAVE_FLAG);
			c.rotate(-bearing, 0.5f, 0.5f);
			c.drawPath(pointerPath, paintPointer);
			c.restore();

			c.drawCircle(0.5f, 0.5f, 0.01f, handScrewPaint);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}

	}

	public void updateDirection(float direction) {
		invalidate();
	}

	public void updateBearing(float bearing) {
		this.bearing = bearing;
		invalidate();
	}

}

package at.ac.tuwien.igw.story2go.util;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public abstract class GenericXmlParser<T> {
	private static final String TAG = GenericXmlParser.class.getSimpleName();
	protected XmlPullParser mParser;
	protected T mResult;

	public GenericXmlParser() {
		init();
	}

	private void init() {
		XmlPullParserFactory factory;
		try {
			factory = XmlPullParserFactory.newInstance();
			mParser = factory.newPullParser();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}

	public T parse(InputStream _is) {
		Log.d(TAG, "parsing");
		if (null == mParser) {
			Log.e(TAG, "parser was null, calling init");
			init();
		}
		try {
			mParser.setInput(_is, "UTF-8");
			int eventType = mParser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
					onStartDocument();
				} else if (eventType == XmlPullParser.START_TAG) {
					onStartTag();
				} else if (eventType == XmlPullParser.END_TAG) {
					onEndTag();
				}
				eventType = mParser.next();
			}
			onEndDocument();

		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mResult;
	}

	protected String getNextText() {
		try {
			return mParser.nextText();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected boolean isParserNameEqualTo(Object _name) {
		return mParser.getName().equals(_name);
	}

	protected abstract void onStartDocument();

	protected abstract void onStartTag();

	protected abstract void onEndTag();

	protected abstract void onEndDocument();
}

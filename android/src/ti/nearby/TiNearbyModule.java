package ti.nearby;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.NearbyPermissions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;
import org.appcelerator.titanium.TiApplication;

@Kroll.module(name = "TiNearby", id = "ti.nearby")
public class TiNearbyModule extends KrollModule
{

	// Standard Debugging variables
	private static final String LCAT = "TiNearbyModule";
	private static final boolean DBG = TiConfig.LOGD;
	private static MessageListener mMessageListener;

	public TiNearbyModule()
	{
		super();
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app)
	{
		//
	}

	// Methods
	@Kroll.method
	public void initialize()
	{
		Context context = TiApplication.getInstance().getAppRootOrCurrentActivity();
		mMessageListener = new MessageListener() {
			@Override
			public void onFound(Message message)
			{
				Log.d(LCAT, "Found message: " + new String(message.getContent()));
				KrollDict data = new KrollDict();
				data.put("message", new String(message.getContent()));
				fireEvent("message", data);
			}

			@Override
			public void onLost(Message message)
			{
				Log.d(LCAT, "Lost sight of message: " + new String(message.getContent()));
			}
		};

		Nearby.getMessagesClient(context)
			.subscribe(mMessageListener)
			.addOnSuccessListener(new OnSuccessListener<Void>() {
				@Override
				public void onSuccess(Void aVoid)
				{
					KrollDict data = new KrollDict();
					data.put("success", true);
					fireEvent("subscribe", data);
				}
			})
			.addOnFailureListener(new OnFailureListener() {
				@Override
				public void onFailure(Exception arg0)
				{
					KrollDict data = new KrollDict();
					data.put("success", false);
					fireEvent("subscribe", data);
					Log.e(LCAT, "error " + arg0.getMessage());
				}
			});
	}

	@Kroll.method
	public void send(String data)
	{
		Context context = TiApplication.getInstance().getAppRootOrCurrentActivity();
		Message mMessage = new Message(data.getBytes());
		Nearby.getMessagesClient(context).publish(mMessage);
	}

	@Kroll.method
	public void stop()
	{
		Context context = TiApplication.getInstance().getAppRootOrCurrentActivity();
		Nearby.getMessagesClient(context).unsubscribe(mMessageListener);
	}
}

# Google Nearby module for Appcelerator Titanium

<span class="badge-buymeacoffee"><a href="https://www.buymeacoffee.com/miga" title="donate"><img src="https://img.shields.io/badge/buy%20me%20a%20coke-donate-orange.svg" alt="Buy Me A Coke donate button" /></a></span>

<a href="https://developers.google.com/nearbyGoogle">Google Nearby</a> is an API to quickly connect nearby devices.
> The Nearby platform makes it easy to discover nearby devices and establish communication with them. It uses technologies such as Bluetooth, Wi-Fi, IP, and audio.

<img src="noise.jpg"/>
<br/>
<small><i>visible high frequency pattern when sharing a message</i></small>

## API key

Create a Google API key and enable Google Nearby.

## Permissions

Needs Bluetooth and ACCESS_FINE_LOCATION permission.

## Example

```javascript
var win = Ti.UI.createWindow({
	layout: "vertical",
	backgroundColor: "#fff"
});
const nearby = require('ti.nearby');
var btn = Ti.UI.createButton({title:"send"});
var tf = Ti.UI.createTextField({color:"#000", width: 500, borderColor:"#000",borderWidth:1});
var lbl = Ti.UI.createLabel({color:"#000"});

win.add([lbl,tf,btn]);


win.addEventListener("open",function(e){
	// initialize nearby
	nearby.initialize();
});
win.addEventListener("close",function(e){
	// stop nearby
	nearby.close();
});
btn.addEventListener("click",function(e){
	// send text
	nearby.send(tf.value);
});
nearby.addEventListener("message",function(e){
	// receive text
	lbl.text = e.message;
})
nearby.addEventListener("subscribe",function(e){
	if (e.success){
		lbl.text = "subscribed";
	} else {
		lbl.text = "error";
	}
})

win.open();
```

tiapp.xml
```xml
<android xmlns:android="http://schemas.android.com/apk/res/android">
		<manifest xmlns:android="http://schemas.android.com/apk/res/android">
			<application>
				<meta-data android:name="com.google.android.nearby.messages.API_KEY" android:value="YOUR_KEY"/>
			</application>

			<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
			<uses-permission android:name="android.permission.BLUETOOTH"/>
		</manifest>
	</android>
```

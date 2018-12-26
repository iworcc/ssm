package cn.ioms.ssm.util;

import org.springframework.util.StringUtils;
import push.AndroidNotification;
import push.PushClient;
import push.android.*;
import push.ios.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class PushUtil {

    private static String appkey = null;
    private static String appMasterSecret = null;
    private String timestamp = null;
    private PushClient client = new PushClient();


    public PushUtil(String key, String secret) {
        try {
            appkey = key;
            appMasterSecret = secret;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void sendAndroidBroadcast() throws Exception {
        AndroidBroadcast broadcast = new AndroidBroadcast(appkey, appMasterSecret);
        broadcast.setTicker("Android broadcast ticker");
        broadcast.setTitle("中文的title");
        broadcast.setText("Android broadcast text");
        broadcast.goAppAfterOpen();
        broadcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
        // TODO Set 'production_mode' to 'false' if it's a test device.
        // For how to register a test device, please see the developer doc.
        broadcast.setProductionMode();
        // Set customized fields
        broadcast.setExtraField("test", "helloworld");
        client.send(broadcast);
    }

    public void sendAndroidUnicast(String deviceToken, String title, String text, String sound) throws Exception {
        AndroidUnicast unicast = new AndroidUnicast(this.appkey, this.appMasterSecret);
        // TODO Set your device token
        unicast.setDeviceToken(deviceToken);
        unicast.setTicker(title);
        unicast.setTitle(title);
        unicast.setText(text);
        unicast.goAppAfterOpen();
        unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
        // TODO Set 'production_mode' to 'false' if it's a test device.
        // For how to register a test device, please see the developer doc.
        unicast.setProductionMode();
        // Set customized fields
        unicast.setExtraField("test", "helloworld");
        if(sound != null){
            unicast.setSound(sound);
        }
        client.send(unicast);
    }


    public void sendAndroidGroupcast() throws Exception {
        AndroidGroupcast groupcast = new AndroidGroupcast(appkey, appMasterSecret);
        /*  TODO
		 *  Construct the filter condition:
		 *  "where":
		 *	{
    	 *		"and":
    	 *		[
      	 *			{"tag":"test"},
      	 *			{"tag":"Test"}
    	 *		]
		 *	}
		 */
        JSONObject filterJson = new JSONObject();
        JSONObject whereJson = new JSONObject();
        JSONArray tagArray = new JSONArray();
        JSONObject testTag = new JSONObject();
        JSONObject TestTag = new JSONObject();
        testTag.put("tag", "test");
        TestTag.put("tag", "Test");
        tagArray.put(testTag);
        tagArray.put(TestTag);
        whereJson.put("and", tagArray);
        filterJson.put("where", whereJson);
        System.out.println(filterJson.toString());

        groupcast.setFilter(filterJson);
        groupcast.setTicker("Android groupcast ticker");
        groupcast.setTitle("中文的title");
        groupcast.setText("Android groupcast text");
        groupcast.goAppAfterOpen();
        groupcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
        // TODO Set 'production_mode' to 'false' if it's a test device.
        // For how to register a test device, please see the developer doc.
        groupcast.setProductionMode();
        client.send(groupcast);
    }

    public void sendAndroidCustomizedcast() throws Exception {
        AndroidCustomizedcast customizedcast = new AndroidCustomizedcast(appkey, appMasterSecret);
        // TODO Set your alias here, and use comma to split them if there are multiple alias.
        // And if you have many alias, you can also upload a file containing these alias, then
        // use file_id to send customized notification.
        customizedcast.setAlias("alias", "alias_type");
        customizedcast.setTicker("Android customizedcast ticker");
        customizedcast.setTitle("中文的title");
        customizedcast.setText("Android customizedcast text");
        customizedcast.goAppAfterOpen();
        customizedcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
        // TODO Set 'production_mode' to 'false' if it's a test device.
        // For how to register a test device, please see the developer doc.
        customizedcast.setProductionMode();
        client.send(customizedcast);
    }

    public void sendAndroidCustomizedcastFile() throws Exception {
        AndroidCustomizedcast customizedcast = new AndroidCustomizedcast(appkey, appMasterSecret);
        // TODO Set your alias here, and use comma to split them if there are multiple alias.
        // And if you have many alias, you can also upload a file containing these alias, then
        // use file_id to send customized notification.
        String fileId = client.uploadContents(appkey, appMasterSecret, "aa" + "\n" + "bb" + "\n" + "alias");
        customizedcast.setFileId(fileId, "alias_type");
        customizedcast.setTicker("Android customizedcast ticker");
        customizedcast.setTitle("中文的title");
        customizedcast.setText("Android customizedcast text");
        customizedcast.goAppAfterOpen();
        customizedcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
        // TODO Set 'production_mode' to 'false' if it's a test device.
        // For how to register a test device, please see the developer doc.
        customizedcast.setProductionMode();
        client.send(customizedcast);
    }

    public void sendAndroidFilecast() throws Exception {
        AndroidFilecast filecast = new AndroidFilecast(appkey, appMasterSecret);
        // TODO upload your device tokens, and use '\n' to split them if there are multiple tokens
        String fileId = client.uploadContents(appkey, appMasterSecret, "aa" + "\n" + "bb");
        filecast.setFileId(fileId);
        filecast.setTicker("Android filecast ticker");
        filecast.setTitle("中文的title");
        filecast.setText("Android filecast text");
        filecast.goAppAfterOpen();
        filecast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
        client.send(filecast);
    }

    public void sendIOSBroadcast() throws Exception {
        IOSBroadcast broadcast = new IOSBroadcast(appkey, appMasterSecret);

        broadcast.setAlert("IOS 广播测试");
        broadcast.setBadge(0);
        broadcast.setSound("default");
        // TODO set 'production_mode' to 'true' if your app is under production mode
        broadcast.setTestMode();
        // Set customized fields
        broadcast.setCustomizedField("test", "helloworld");
        client.send(broadcast);
    }

    public void sendIOSUnicast(String deviceToken, String title, String text, String sound) throws Exception {
        IOSUnicast unicast = new IOSUnicast(this.appkey, this.appMasterSecret);
        // TODO Set your device token
        unicast.setDeviceToken(deviceToken);
        unicast.setAlert(text);
        unicast.setBadge(1);
        // TODO set 'production_mode' to 'true' if your app is under production mode
        unicast.setTestMode();
        // Set customized fields
        unicast.setCustomizedField(title, text);
        unicast.setSound(StringUtils.isEmpty(sound)?"default":sound);
        client.send(unicast);
    }

    public void sendIOSGroupcast() throws Exception {
        IOSGroupcast groupcast = new IOSGroupcast(appkey, appMasterSecret);
		/*  TODO
		 *  Construct the filter condition:
		 *  "where":
		 *	{
    	 *		"and":
    	 *		[
      	 *			{"tag":"iostest"}
    	 *		]
		 *	}
		 */
        JSONObject filterJson = new JSONObject();
        JSONObject whereJson = new JSONObject();
        JSONArray tagArray = new JSONArray();
        JSONObject testTag = new JSONObject();
        testTag.put("tag", "iostest");
        tagArray.put(testTag);
        whereJson.put("and", tagArray);
        filterJson.put("where", whereJson);
        System.out.println(filterJson.toString());

        // Set filter condition into rootJson
        groupcast.setFilter(filterJson);
        groupcast.setAlert("IOS 组播测试");
        groupcast.setBadge(0);
        groupcast.setSound("default");
        // TODO set 'production_mode' to 'true' if your app is under production mode
        groupcast.setTestMode();
        client.send(groupcast);
    }

    public void sendIOSCustomizedcast(String deviceToken) throws Exception {
        IOSCustomizedcast customizedcast = new IOSCustomizedcast(appkey, appMasterSecret);
        // TODO Set your alias and alias_type here, and use comma to split them if there are multiple alias.
        // And if you have many alias, you can also upload a file containing these alias, then
        // use file_id to send customized notification.
        customizedcast.setAlias("alias", "alias_type");
        customizedcast.setAlert("IOS 个性化测试");
        customizedcast.setBadge(0);
        customizedcast.setSound("default");
        // TODO set 'production_mode' to 'true' if your app is under production mode
        customizedcast.setTestMode();
        client.send(customizedcast);
    }

    public void sendIOSFilecast() throws Exception {
        IOSFilecast filecast = new IOSFilecast(appkey, appMasterSecret);
        // TODO upload your device tokens, and use '\n' to split them if there are multiple tokens
        String fileId = client.uploadContents(appkey, appMasterSecret, "aa" + "\n" + "bb");
        filecast.setFileId(fileId);
        filecast.setAlert("IOS 文件播测试");
        filecast.setBadge(0);
        filecast.setSound("default");
        // TODO set 'production_mode' to 'true' if your app is under production mode
        filecast.setTestMode();
        client.send(filecast);
    }

    /**
     * 消息推送
     * @param deviceToken   设备唯一标识Token
     * @param title         发送内容title
     * @param content       发送内容
     * @param sound         声音标识
     */
    public static void push(String deviceToken,String title,String content,String sound){
        if(!StringUtils.isEmpty(deviceToken)){
            try {
                synchronized(PushUtil.class){
                    //根据Token 长度判断设备类型 44 位为安卓  64位为IOS
                    if(deviceToken.length() == Integer.valueOf(ConfigUtils.getProperty("androidTokenLength"))){
                        PushUtil pushUtil = new PushUtil(ConfigUtils.getProperty("umengAndroid.key"),ConfigUtils.getProperty("umengAndroid.secret"));
                        pushUtil.sendAndroidUnicast(deviceToken,title,content,sound);
                    }else if(deviceToken.length() == Integer.valueOf(ConfigUtils.getProperty("iosTokenLength"))){
                        PushUtil pushUtil = new PushUtil(ConfigUtils.getProperty("umengIOS.key"),ConfigUtils.getProperty("umengIOS.secret"));
                        pushUtil.sendIOSUnicast(deviceToken,title,content,sound);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            System.out.println("暂无推送");
        }
    }


    public static void main(String[] args) throws Exception {

//    	PushUtil pushUtil = new PushUtil("5a6d6987f43e4822af0003c8", "ml6cfgk3gbbkzdlvjk9cmtqesgkyti7w");
//
//    	pushUtil.sendIOSUnicast("c00e2ef0012d2edde11212c7e0ea7e7d63c8b8fa6c5a712dde0722932381e587", "测试", "测试内容", "new_order.caf");

//    	PushUtil pushUtil = new PushUtil("5a6d6b9cb27b0a245b000143", "a29hgfixrekh2cwxmsf4dlc4ss5ejlec");
//    	pushUtil.sendAndroidUnicast("Aiyw9h6tbIYNgDWy1qqOtthU0oJrRh-gnWJfYYBCcJPA", "测试", "测试内容", "new_order.caf");
        PushUtil.push("AvA35j__gxOSxFQD5F0P7XIjx9j1zuKSTCwPDJafAx_q","test","contnet",null);

    }
}

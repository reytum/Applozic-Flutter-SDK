package com.applozic.flutter_applozic_plugin;

import android.content.Context;
import android.content.Intent;

import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.api.account.register.RegistrationResponse;
import com.applozic.mobicomkit.api.account.user.User;
import com.applozic.mobicomkit.listners.AlLoginHandler;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * FlutterApplozicPlugin
 */
public class FlutterApplozicPlugin implements MethodCallHandler {

    private final Registrar registrar;
    private final MethodChannel channel;

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_applozic_plugin");
        FlutterApplozicPlugin plugin = new FlutterApplozicPlugin(registrar, channel);
        channel.setMethodCallHandler(plugin);
    }

    private FlutterApplozicPlugin(Registrar registrar, MethodChannel channel) {
        this.registrar = registrar;
        this.channel = channel;
    }

    @Override
    public void onMethodCall(MethodCall call, final Result result) {
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else if (call.method.equals("loginUser")) {
            User user = call.arguments();
            Applozic.connectUser(registrar.context(), user, new AlLoginHandler() {
                @Override
                public void onSuccess(RegistrationResponse registrationResponse, Context context) {
                    result.success(registrationResponse);
                }

                @Override
                public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                    result.error("Error", exception != null ? exception.getMessage() : null, registrationResponse);
                }
            });
        } else if (call.method.equals("launchChat")) {
            Intent intent = new Intent(registrar.activity(), ConversationActivity.class);
            registrar.context().startActivity(intent);
        } else {
            result.notImplemented();
        }
    }
}

package stoliarov.me.myapplication;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by VladS on 1/27/2016.
 */
public class Messengerq {
    private static final String login = "qweqweqweqwe@safeboard";
    private static final Messengerq messengerq = new Messengerq();

    public static Messengerq getMessengerq() {
        return messengerq;
    }
    boolean operationResult = false;
    static {
        System.loadLibrary("messengerq");
    }

    public void send(String recpt, String text) throws IOException{
        nativeSend(recpt, text);
    };

    public void login() throws IOException{
        nativeLogin(login);
    }

    public void disconnect() throws IOException{
        nativeDisconnect();
    }

    public ArrayList<String> usersList() throws IOException{
        return nativeUserslist();
    }

    public void onOperationResult(boolean result) {
        this.operationResult = true;
        System.out.println(operationResult);
    }

    private native void nativeSend(String recpt, String text) throws IOException;
    private native void nativeLogin(String login) throws IOException;
    private native void nativeDisconnect() throws IOException;
    private native ArrayList<String> nativeUserslist() throws IOException;
}

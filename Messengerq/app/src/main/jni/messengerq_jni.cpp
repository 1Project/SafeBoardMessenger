#include <jni.h>
#include <future>
#include <memory>
#include <iostream>

#include "messenger/messenger.h"

#define JNI_CALL(__ret, __f) extern "C" __attribute__ ((visibility("default"))) __ret JNICALL Java_stoliarov_me_myapplication_Messengerq_##__f

std::shared_ptr<messenger::IMessenger> g_messenger;
std::promise<messenger::UserList> g_userList;
std::condition_variable m_cv;
std::string m_receivedMsg;
static JavaVM *jvm;
class TestClient : public messenger::ILoginCallback, public messenger::IMessagesObserver, public messenger::IRequestUsersCallback
{
public:
    TestClient()
    {
        messenger::MessengerSettings settings;
        settings.serverUrl = "safeboard.cloudapp.net";
        settings.serverPort = 5222;
        g_messenger = messenger::GetMessengerInstance(settings);
    }

    ~TestClient() {
        g_messenger->Disconnect();
    }

    void OnMessageStatusChanged(const messenger::MessageId& msgId, messenger::message_status::Type status) override
    {
        m_receivedMsg = "<error>";
        m_cv.notify_all();
    }

    void OnMessageReceived(const messenger::UserId& senderId, const messenger::Message& msg) override
    {
        m_receivedMsg.assign(reinterpret_cast<const char*>(&msg.content.data[0]), msg.content.data.size());
        m_cv.notify_all();
    }
    void OnOperationResult(messenger::operation_result::Type result) override
    {
//        jvm->AttachCurrentThread(&myEnv, NULL);
//        jclass todoItemClass = myEnv->FindClass("stoliarov/me/myapplication/Messengerq");
//        jmethodID todoItemCtor = myEnv->GetMethodID(todoItemClass, "onOperationResult", "(Z)");
    }

    void OnOperationResult(messenger::operation_result::Type result, const messenger::UserList& users) override
    {
        g_userList.set_value(users);
    }

private:
    JNIEnv* myEnv;
    JavaVM* mJvm;
};

TestClient *tcl = new TestClient();

JNI_CALL(void, nativeDisconnect)(JNIEnv* env, jclass caller)
{
    delete tcl;
}

JNI_CALL(jstring, nativeReceive)(JNIEnv* env, jclass caller)
{
        std::string msg = m_receivedMsg;
        m_receivedMsg.clear();
        jstring result_msg = env->NewStringUTF(msg.c_str());
        return result_msg;
}

JNI_CALL(void, nativeSend)(JNIEnv* env, jclass caller, jstring recpt, jstring text)
{
    char buf[128];
    const char *recpt_chars = env->GetStringUTFChars(recpt, 0);
    std::string recpt_str(recpt_chars, 50);
    messenger::MessageContent msg;
    msg.type = messenger::message_content_type::Text;
    std::copy(recpt_str.begin(), recpt_str.end(), std::back_inserter(msg.data));
    g_messenger->SendMessage("test@localhost", msg);

    env->ReleaseStringUTFChars(recpt, recpt_chars);
}


JNI_CALL(void, nativeLogin)(JNIEnv* env, jclass caller, jstring login)
{
    char buf[128];
    const char *login_chars = env->GetStringUTFChars(login, 0);
    env->GetJavaVM(&jvm);
    messenger::SecurityPolicy securityPolicy;
    g_messenger->Login(login_chars, "", securityPolicy, tcl);
    env->ReleaseStringUTFChars(login, login_chars);
}

JNI_CALL(jobject, nativeUserslist)(JNIEnv* env, jclass caller)
{
    messenger::UserList list;
    g_messenger->RequestActiveUsers(tcl);
    list = g_userList.get_future().get();

    jclass ArrayList_class = env->FindClass("java/util/ArrayList");
    jmethodID ArrayList_init_id = env->GetMethodID(ArrayList_class, "<init>", "()V");
    jmethodID ArrayList_add_id = env->GetMethodID(ArrayList_class, "add", "(Ljava/lang/Object;)Z");
    jobject List_obj = env->NewObject(ArrayList_class, ArrayList_init_id);

    for (messenger::User item: list) {
        env->CallBooleanMethod(List_obj, ArrayList_add_id, env->NewStringUTF(item.identifier.c_str()));
    }
    return List_obj;
}






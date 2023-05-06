**基于 SPI(Service Provider Interface) +AIDL(Android Interface Definition Language)+命令模式实现的一款高扩展低耦合的 webview。**

#### 概念了解：

##### SPI

SPI（Service Provider Interface）是Java提供的一种服务提供发现机制，可以用来动态装配系统中的各种服务实现。其核心思想是，定义一组接口规范，而服务的具体实现则由第三方开发者提供，通过Java的SPI机制，运行时动态发现并装配实现，从而实现了松耦合的插件式扩展。

在SPI机制中，主要有三个角色：服务接口、服务实现者和服务提供者配置文件。其中，服务接口是定义一组标准接口规范；服务实现者是针对服务接口的具体实现；服务提供者配置文件是用来描述服务接口和服务实现者之间的对应关系，通常存放在META-INF/services目录下，以服务接口的全限定名作为文件名，文件内容为该接口对应的实现类的全限定名。

代码示例：

```
// 定义 SPI 接口
public interface HelloService {
    void sayHello();
}

// 定义 SPI 接口的实现类1
public class HelloServiceImpl1 implements HelloService {
    @Override
    public void sayHello() {
        System.out.println("Hello from HelloServiceImpl1!");
    }
}

// 定义 SPI 接口的实现类2
public class HelloServiceImpl2 implements HelloService {
    @Override
    public void sayHello() {
        System.out.println("Hello from HelloServiceImpl2!");
    }
}

// 在 resources 目录下创建 META-INF/services 目录，并在该目录下创建文件
// 文件名为 SPI 接口的全限定名，文件内容为 SPI 接口的实现类的全限定名
// 文件内容为 HelloService 的实现类的全限定名，也就是HelloServiceImpl1和HelloServiceImpl2的全限定名
// 文件名为 com.example.spi.HelloService
// 文件内容为 com.example.spi.HelloServiceImpl1
//            com.example.spi.HelloServiceImpl2

// 定义 SPI 的调用者
public class HelloSPI {
    public static void main(String[] args) {
        // 获取 HelloService 的所有实现类
        ServiceLoader<HelloService> serviceLoader = ServiceLoader.load(HelloService.class);
        // 遍历实现类，并调用 sayHello() 方法
        for (HelloService helloService : serviceLoader) {
            helloService.sayHello();
        }
    }
}

```

以上示例中，首先定义了一个 SPI 接口 `HelloService`，以及两个实现类 `HelloServiceImpl1` 和 `HelloServiceImpl2`。然后在 `resources` 目录下创建了 `META-INF/services` 目录，并在该目录下创建了一个名为 `com.example.spi.HelloService` 的文件，文件中写入了 `HelloServiceImpl1` 和 `HelloServiceImpl2` 的全限定名。最后在 `HelloSPI` 类中通过 `ServiceLoader` 加载 `HelloService` 的所有实现类，并遍历调用它们的 `sayHello()` 方法。

在这个示例中，SPI 接口和实现类可以分别由不同的人或组织实现和提供，而调用者只需要编写一段通用的代码，就可以动态地获取 SPI 接口的所有实现类，并进行调用。这种解耦的设计能够更好地实现代码的扩展和灵活性。

##### Autoservice

AutoService是Google开发的一个Java库，它可以帮助我们自动生成Java SPI(Service Provider Interface)的代码，避免手写META-INF/services下的文件，减少代码量，提高开发效率。通过使用AutoService，我们只需要添加@AutoService注解，AutoService就会自动将其注册到SPI配置文件中。同时，AutoService还支持用于生成基于注解的服务加载器的通用实用程序。

举个例子，我们可以通过以下方式使用AutoService来生成服务提供者接口的实现类:

1. 首先，我们需要在build.gradle中添加AutoService依赖：

   ```
   dependencies {
       implementation 'com.google.auto.service:auto-service:1.0-rc6'
   }
   ```

   

2. 然后，在服务提供者接口的实现类上添加@AutoService注解：

   ```
   @AutoService(IService.class)
   public class ServiceImpl implements IService {
       //...
   }
   ```

3. 最后，编译项目，AutoService就会自动生成META-INF/services/IService文件，并将ServiceImpl类的全限定名写入到该文件中，实现了服务提供者的自动注册。


##### AIDL

AIDL（Android Interface Definition Language）是 Android 提供的一种跨进程通信的机制，用于在不同进程之间传输数据和调用远程方法。它是一种类似于 Java 接口的定义语言，用于定义客户端与服务端之间的通信接口。

AIDL 可以支持基本类型、String、CharSequence、List、Map、自定义 Parcelable 对象等数据类型的跨进程传输，并且支持回调方法。

使用 AIDL 可以实现一些特定的需求，例如多进程共享数据、进程间通信等。

下面是一个简单的 AIDL 示例，展示如何定义一个服务端接口和对应的客户端接口。

服务端接口：

```
// 定义一个接口，继承自 IInterface
interface IMyService extends IInterface {
    // 定义方法，用于计算两个数的和并返回结果
    int add(int num1, int num2);
}

```

客户端接口：

```
// 定义一个接口，用于绑定服务并获取服务端接口实例
interface IMyService {
    // 绑定服务
    void bindService(Context context);
    
    // 获取服务端接口实例
    IMyService getService();
}

```

服务端实现：

```
// 实现服务端接口
class MyService extends Service {
    // 实现 IMyService 接口
    private final IMyService.Stub mBinder = new IMyService.Stub() {
        @Override
        public int add(int num1, int num2) throws RemoteException {
            return num1 + num2;
        }
    };

    // 返回 IBinder 对象
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
```

客户端实现：

```
// 实现客户端接口
class MyClient implements IMyService {
    // 服务端接口实例
    private IMyService mService;
    
    // 绑定服务
    @Override
    public void bindService(Context context) {
        Intent intent = new Intent(context, MyService.class);
        context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }
    
    // 获取服务端接口实例
    @Override
    public IMyService getService() {
        return mService;
    }
    
    // 服务连接回调
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            // 获取服务端接口实例
            mService = IMyService.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            // 服务断开连接
            mService = null;
        }
    };
}
```

在客户端中，通过绑定服务并获取服务端接口实例，就可以通过调用服务端接口中定义的方法，实现跨进程通信。



#### 开源项目应用

<img src="./RelaxWebView/项目结构图.png" alt="项目结构图" style="zoom: 50%;" />

##### SPI实现

base 模块封装 serviceloader ，其他业务模块依赖 base 模块。

```
package com.relaxgao.base.serviceloader
import java.lang.Exception
import java.util.*

object RelaxServiceLoader {

    fun <S> load(service: Class<S>?): S? {
        return try {
            ServiceLoader.load(service).iterator().next()
        } catch (e: Exception) {
            null
        }
    }
}
```

##### 命令模式实现

common 模块定义 command 接口和相关服务接口

```
package com.relaxgao.common.autoservice.relaxgaowebview;
import com.relaxgao.common.ICallbackFromMainprocessToWebViewProcessInterface;
import java.util.Map;

public interface Command {
    String name();
    void execute(Map parameters, ICallbackFromMainprocessToWebViewProcessInterface callback);
}
```

app模块实现具体命令

```
@AutoService({Command.class})//AutoService会生成提供接口实现的配置文件
public class CommandLogin implements Command {
    IUserCenterService iUserCenterService = RelaxServiceLoader.INSTANCE.load(IUserCenterService.class);
    ICallbackFromMainprocessToWebViewProcessInterface callback;
    String callbacknameFromNativeJs;
    public CommandLogin(){
        EventBus.getDefault().register(this);
    }
    @Override
    public String name() {
        return "login";
    }
    @Override
    public void execute(final Map parameters, ICallbackFromMainprocessToWebViewProcessInterface callback) {
        Log.d("CommandLogin", new Gson().toJson(parameters));

        if (iUserCenterService != null && !iUserCenterService.isLogined()) {
            iUserCenterService.login();
            this.callback = callback;//app process callback webview process
            this.callbacknameFromNativeJs = parameters.get("callbackname").toString();//callback js
        }
    }
    @Subscribe
    public void onMessageEvent(LoginEvent event) {
        Log.d("CommandLogin", event.userName);
        HashMap map = new HashMap();
        map.put("accountName", event.userName);
        if(this.callback != null) {
            try {
                this.callback.onResult(callbacknameFromNativeJs, new Gson().toJson(map));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
```

##### AIDL 实现

因为 webview 的内存管理比较复杂，因此考虑开启独立的进程承载其运行，即使发生了内存造成的崩溃问题也不会影响主进程的运行。在 conmmon 模块定义了 aidl 接口用于 webview 进程和主进程的通信，webview 模块定义了 service ，该服务运行在主进程，负责承载 webviw的 activity 运行在独立的进程，运行在主进程的 service 在初始化的时候会通过 serviceloader 加载所有的 commad 实现类，当收到 webview 端发来的命令，会遍历寻找相关命令的实现类 进行命令的执行，比如跳转登录后获取用户信息 再通过 aidl 回传给 webview端，Webview 端收到服务端发来的用户信息之后 进行界面的刷新。

用于生成主进程binder接口的aidl接口，这个binder用于webview进程通知主进程

```
// IWebviewProcessToMainProcessInterface.aidl
package com.relaxgao.common;
// Declare any non-default types here with import statements
import com.relaxgao.common.ICallbackFromMainprocessToWebViewProcessInterface;
interface IWebviewProcessToMainProcessInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void handleWebCommand(String commandName, String jsonParams, in ICallbackFromMainprocessToWebViewProcessInterface callback);
}

```

主进程处理aidl接口回调的实现

```
/**
   * @description主进程服务端的binder
   * @author gaojiangfeng
   * @email  relaxgao@gmail.com
   * @time 2022/3/16 13:59
   */
object MainProcessCommandsManager : IWebviewProcessToMainProcessInterface.Stub() {
    private val mCommands: HashMap<String, Command> = HashMap<String, Command>()

    init {
        //该类初始化的时候 会将commad所有实现类的加载进内存
        val serviceLoader = ServiceLoader.load(
            Command::class.java
        )
        for (command in serviceLoader) {
            if (!mCommands.containsKey(command.name())) {
                mCommands[command.name()] = command
            }
        }
    }
    const val TAG = "CommandsManager"
      /**
        * @description 当webview端的js界面交互后会通过aidl回调该接口，通过命令名称查到对应的命令实现类进行命令的执行
        * @param
        * @return
        * @author gaojiangfeng
        * @email  relaxgao@gmail.com
        */
    override fun handleWebCommand(
        commandName: String?,
        jsonParams: String?,
        callback: ICallbackFromMainprocessToWebViewProcessInterface?
    ) {
        Log.i(TAG, "Main process commands manager handle web command")
        //找到具体的commad 执行commad
        mCommands[commandName]?.execute(Gson().fromJson(jsonParams, MutableMap::class.java), callback)
    }
}
```

用于生成webview进程binder接口的aidl接口，这个binder用于主进程回调webview进程

```
// ICallbackFromMainprocessToWebViewProcessInterface.aidl
package com.relaxgao.common;

interface ICallbackFromMainprocessToWebViewProcessInterface {
    void onResult(String kotlinToJavescriptCallBackName, String response);
}
```

webview的页面交互会响应js接口，js回调java，然后跨进程通知主进程 执行相关命令

```
 @JavascriptInterface
    public void takeNativeAction(final String jsParam) {
        Log.e(TAG, "this is a call from html javascript." + jsParam);
        if (!TextUtils.isEmpty(jsParam)) {
            final JsParam jsParamObject = new Gson().fromJson(jsParam, JsParam.class);
            if (jsParamObject != null) {
                WebViewProcessCommandsDispatcher.INSTANCE.executeCommand(jsParamObject.name, new 			Gson().toJson(jsParamObject.param), this);
            }
        }
    }
```

##### Autoservice 

所有用于SPI机制的具体实现类都会使用@AutoService注解，就是给所有 commad 接口和 service 接口的实现类打上标签，并在 META-INFO 目录下生成对应的接口文件，该文件里的内容是该接口所有的实现类，该文件用于 serviceloader 加载并解析出所有的 commad或者service 的实现类。


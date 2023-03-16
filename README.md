# RelaxWebView
基于 SPI(Service Provider Interface) +AIDL(Android Interface Definition Language)+命令模式实现的一款高扩展低耦合的 webview。
1. SPI，base  模块封装 serviceloader ，其他业务模块依赖 base  模块，common  模块定义 command  接口和相关服务接口，其他业务模块依赖 common  模块，比如 usercenter  模块依赖 common  模块并实现 IUserservice  接口，IUserservice  的实现类处理跳转登录和获取用户信息的逻辑，usercenter  模块还需要定义 command  接口的实现类 LoginCommand ，该实现类负责执行登录命令，也就是拿到 IUserservice  的实现类进行登录的跳转，app  模块定义了 commnad  接口的实现类 openPageCommad  负责打开新的界面。
2. AIDL ，因为 webview  的内存管理比较复杂，因此考虑开启独立的进程承载其运行，即使发生了内存造成的崩溃问题也不会影响主进程的运行。在 conmmon  模块定义了 aidl  接口用于 webview  进程和主进程的通信，webview  模块定义了 service ，该服务运行在主进程，负责承载 webviw的 activity  运行在独立的进程，运行在主进程的 service  在初始化的时候会通过 serviceloader  加载所有的 commad  实现类，当收到 webview  端发来的命令，会遍历寻找相关命令的实现类 进行命令的执行，比如跳转登录后获取用户信息 再通过 aidl  回传给 webview端，Webview  端收到服务端发来的用户信息之后 进行界面的刷新。
3. Autoservice ，使用它的主要目的是给所有 commad  接口和 service  接口的实现类打上标签，并在 META-INFO  目录下生成对应的接口文件，该文件里的内容是该接口所有的实现类，该文件用于 serviceloader  加载并解析出所有的 commad  实现类
4.前辈们的开源精神让我获益良多，也希望将这份精神传递下去，等我稍加整理后会上传到 github ，希望遇见同样热爱技术的伙伴一起探讨学习。

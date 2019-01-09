# MyButterKnife
APT(Annotation Processing Tool) 即注解处理器,实现一个轻量级ButterKnife.

APT(**Annotation Processing Tool**) 即注解处理器，是一种注解处理工具，用来在编译期扫描和处理注解，通过注解来生成 Java 文件。即以注解作为桥梁，通过预先规定好的代码生成规则来自动生成 Java 文件。此类注解框架的代表有 **ButterKnife、Dragger2、EventBus** 等

#### 1.1、建立 Module
首先在工程中新建一个 **Java Library**，命名为 **apt_processor**，用于存放 **AbstractProcessor** 的实现类。
再新建一个 **Java Library**，命名为 **apt_annotation** ，用于存放各类注解。
当中，**apt_processor** 需要导入如下依赖
```
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation 'com.google.auto.service:auto-service:1.0-rc2'
    implementation 'com.squareup:javapoet:1.10.0'
    implementation project(':apt_annotation')
}
```
当中，**JavaPoet** 是 square 开源的 Java 代码生成框架，可以很方便地通过其提供的 API 来生成指定格式（修饰符、返回值、参数、函数体等）的代码。**auto-service** 是由 Google 开源的注解注册处理器

实际上，上面两个依赖库并不是必须的，可以通过硬编码代码生成规则来替代，但还是建议使用这两个库，因为这样代码的可读性会更高，且能提高开发效率

**app Module** 需要依赖这两个 Java Library
```
    implementation project(':apt_annotation')
    annotationProcessor project(':apt_processor')
```
这样子，我们需要的所有基础依赖关系就搭建好了
![](https://github.com/ljrRookie/MyButterKnife/blob/master/gif/b22b74ce7fbdcd311c14ac17f85f934.png)


#### 1.2、编写代码生成规则


首先观察自动生成的代码，可以归纳出几点需要实现的地方：

1、文件和源 Activity 处在同个包名下

2、类名以 **Activity名 + ViewBinding** 组成

3、**bind()** 方法通过传入 Activity 对象来获取其声明的控件对象来对其进行实例化，这也是 ButterKnife 要求需要绑定的控件变量不能声明为 **private** 的原因



### 1.3、注解绑定效果

首先在 **MainActivity** 中声明两个 **BindView** 注解，然后 **Rebuild Project**，使编译器根据 **BindViewProcessor** 生成我们需要的代码.
**rebuild** 结束后，就可以在 **generatedJava** 文件夹下看到 **MainActivityViewBinding** 类自动生成了
![](https://github.com/ljrRookie/MyButterKnife/blob/master/gif/db78026edfcc69fa1ccca061b8e0aa6.png)

此时有两种方式可以用来触发 **bind()** 方法
1. 在 **MainActivity** 方法中直接调用 **MainActivityViewBinding** 的 **bind()** 方法
2. 因为 **MainActivityViewBinding** 的包名路径和 **Activity** 是相同的，所以也可以通过反射来触发 **MainActivityViewBinding** 的 **bind()** 方法
```

public class ButterKnife {

    public static void bind(Activity activity) {
        Class clazz = activity.getClass();
        try {
            Class bindViewClass = Class.forName(clazz.getName() + "ViewBinding");
            Method method = bindViewClass.getMethod("bind", activity.getClass());
            method.invoke(bindViewClass.newInstance(), activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
```
两种方式各有优缺点。第一种方式在每次 **build project** 后才会生成代码，在这之前无法引用到对应的 **ViewBinding** 类。第二种方式可以用固定的方法调用方式，但是相比方式一，反射会略微多消耗一些性能.但这两种方式的运行结果是完全相同的.

![](https://github.com/ljrRookie/MyButterKnife/blob/master/gif/5654dacfb53eb8c7f538c77379d1b7d.png)



![](https://github.com/ljrRookie/MyButterKnife/blob/master/gif/GIF.gif)


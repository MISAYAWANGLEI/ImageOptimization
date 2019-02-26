# ImageOptimization

安卓图片优化插件：能有效减少apk安装包大小，支持png/jpg转为webp，支持png图片有损无损压缩，支持jpg有损压缩

### 支持的os

`macOS`、`windows10`上已经测试通过，linux上暂时没测试。

### 引入ImageOptimization

在Project的build.gradle文件中:  

```
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        ...
        classpath 'com.wanglei.image-optimization:optimization:1.0'
    }
}
```  

在你想要优化的module的build.gradle文件中引入插件: 如在多个module使用则每个module都要引入插件 

`apply plugin:'com.wanglei.image-optimization'`  

同步之后会生成如下任务：双击执行任务即可

![Task](imageafter/tasks.jpg)  

### 任务生成规则

imageOptimization_project名字_variantname

### 配置项

在module的build.gradle文件中添加配置选项，配置插件优化的策略以及参数:  

```
ImageOptimization{
    pluginStrategy = "compress"
    convertWebpQuality = 80
    jpegCompressQuality = 90
    appIconName = "ic_launcher"
    appIconRoundName = "ic_launcher_round"
    convertWebpType = "lossy"
    compressPngType = "lossless"
    pngCompressQuality = 79
}
```  
























package com.wanglei.optimizer

class ImageOptimizationExtensions{

    def jpegCompressQuality = ImageOptimizationConstants.defaultJpegCompressQuality
    //有损模式指定才有意义，无损模式用不到这个参数
    def convertWebpQuality = ImageOptimizationConstants.defaultConvertWebpQuality
    def pngCompressQuality = ImageOptimizationConstants.defaultCompressPngQuality
    String appIconName //启动图标的图片名字
    String appIconRoundName //圆形启动图标的图片名字
    def convertWebpType = ImageOptimizationConstants.LOSSY //转换webp的模式，默认有损
    def compressPngType = ImageOptimizationConstants.LOSSY //压缩png的模式，默认有损
    def pluginStrategy = ImageOptimizationConstants.STRATEGY_ONLY_COMPRESS //默认插件策略为只压缩图片
}
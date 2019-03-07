package com.wanglei.optimizer

class ImageOptimizationConstants {

    def static final PNG = ".png"
    def static final PNG9 = ".9.png"
    def static final JPG = ".jpg"
    def static final JPEG = ".jpeg"
    def static final TOOLSDIR = "image_tools"
    def static final DRAWABLE_FOLDER = "drawable"
    def static final MIPMAP_FOLDER = "mipmap"
    def static final IMG_EXT_NAME = "ImageOptimization"
    def static final IMG_FILTER_EXT_NAME = "filter"
    //
    def static final defaultJpegCompressQuality = 84//[84,100]
    def static final defaultConvertWebpQuality = 75//[0,100]
    def static final defaultCompressPngQuality = 80//[0,100]
    //转换webp的工具类型
    def static final LOSSY = "lossy"//默认，有损模式，可指定压缩质量
    def static final LOSSLESS = "lossless"//无损模式

    def static final CWEBP_TOOL_NAME = "cwebp"
    def static final LOSSY_PNG_TOOL_NAME = "pngquant"
    def static final LOSSLESS_PNG_TOOL_NAME = "pngout"
    def static final JPG_TOOL_NAME = "guetzli"

    //插件策略
    def static final STRATEGY_WEBP = "webp"//优先转换为webp，不能转换的进行压缩
    def static final STRATEGY_ONLIY_COMPRESS = "compress"//只进行压缩，不转为webp

}
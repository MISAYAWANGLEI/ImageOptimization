package com.wanglei.optimizer


import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.GradleException
import org.gradle.api.Project

import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.security.InvalidParameterException

class ImageOptimizationUtils {

    /**
     * 判断是否是文件夹
     * @param file
     * @return
     */
    def static isImageFolder(File file) {
        return file.name.startsWith(ImageOptimizationConstants.DRAWABLE_FOLDER) || file.name.startsWith(ImageOptimizationConstants.MIPMAP_FOLDER)
    }

    def static isPngImage(File file) {
        return (file.name.endsWith(ImageOptimizationConstants.PNG) || file.name.endsWith(ImageOptimizationConstants.PNG.toUpperCase())) &&
                !file.name.endsWith(ImageOptimizationConstants.PNG9) && !file.name.endsWith(ImageOptimizationConstants.PNG9.toUpperCase())
    }

    def static isJpgImage(File file) {
        return file.name.endsWith(ImageOptimizationConstants.JPG) || file.name.endsWith(ImageOptimizationConstants.JPG.toUpperCase()) ||
                file.name.endsWith(ImageOptimizationConstants.JPEG) || file.name.endsWith(ImageOptimizationConstants.JPEG.toUpperCase())
    }

    /**
     * 判断图片是否存在透明通道
     */
    def static isTransparent(File file) {
        BufferedImage image = ImageIO.read(file)
        return image.colorModel.hasAlpha()
    }

    /**
     * 获取图片处理工具
     */
    def static copyTool(Project project, String name) {
        String toolName
        //获得对应平台的工具
        if (Os.isFamily(Os.FAMILY_MAC)) {
            toolName = "${name}_darwin"
        } else if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            toolName = "${name}_win.exe"
        } else {
            toolName = "${name}_linux"
        }
        def tool = "${project.buildDir}/${ImageOptimizationConstants.TOOLSDIR}/$toolName"
        def file = new File(tool)
        //resources目录下对应工具拷贝到app/build/tools/目录下
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.withOutputStream {
                def is = ImageOptimizationUtils.class.getResourceAsStream("/$name/$toolName")
                it << is.bytes
                is.close()
            }
        }
        if (file.exists() && file.setExecutable(true)) {
            return file.absolutePath
        }
        throw new GradleException("$tool :无法执行或者不存在")
    }

    def static checkOptionalParams(def defaultJpegCompressQuality, def defaultConvertWebpQuality, String appIconName, String convertWebpType, int minSdk, String compressPngType, String pluginStrategy, int pngCompressQuality) {
        if (defaultJpegCompressQuality < 84 || defaultJpegCompressQuality > 100) {
            throw new InvalidParameterException("JpegCompressQuality should be [84,100] , default value is 84 ，you set $defaultJpegCompressQuality")
        }
        if (defaultConvertWebpQuality < 0 || defaultConvertWebpQuality > 100) {
            throw new InvalidParameterException("ConvertWebpQuality should be [0,100] , default value is 75 ，you set $defaultConvertWebpQuality")
        }

        if (appIconName == null || appIconName.isEmpty()) {
            throw new InvalidParameterException("appIconName should not be empty ，you set $appIconName")
        }

        if (!convertWebpType.equalsIgnoreCase(ImageOptimizationConstants.LOSSY)
                && !convertWebpType.equalsIgnoreCase(ImageOptimizationConstants.LOSSLESS)) {
            throw new InvalidParameterException("convertWebpType error. Please use lossy or lossless, default is lossy ，you set $convertWebpType")
        }

        if (convertWebpType.equalsIgnoreCase(ImageOptimizationConstants.LOSSLESS) && minSdk < 18) {
            throw new InvalidParameterException("convertWebpType lossless type only support minsdk >=18 ，you set $minSdk")
        }

        if (!compressPngType.equalsIgnoreCase(ImageOptimizationConstants.LOSSY)
                && !compressPngType.equalsIgnoreCase(ImageOptimizationConstants.LOSSLESS)) {
            throw new InvalidParameterException("compressPngType error. Please use lossy or lossless, default is lossy ，you set $compressPngType")
        }

        if (!pluginStrategy.equalsIgnoreCase(ImageOptimizationConstants.STRATEGY_ONLY_COMPRESS)
                && !pluginStrategy.equalsIgnoreCase(ImageOptimizationConstants.STRATEGY_WEBP)) {
            throw new InvalidParameterException("pluginStrategy error. Please use webp or compress, default is compress ，you set $pluginStrategy")
        }

        if (pngCompressQuality < 0 || pngCompressQuality > 100){
            throw new InvalidParameterException("pngCompressQuality should be [0,100] , default value is 80 ，you set $pngCompressQuality")
        }
    }

    //排除启动图标
    def static isNotLauncherIcon(File file, String appIconName, String appIconRoundName) {
        return (appIconRoundName == null || appIconRoundName.isEmpty()) ?
                (file.name != "${appIconName}.png")
                : (file.name != "${appIconName}.png" && file.name != "${appIconRoundName}.png")
    }

}
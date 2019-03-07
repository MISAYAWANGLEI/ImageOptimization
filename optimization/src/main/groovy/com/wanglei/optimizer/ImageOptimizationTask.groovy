package com.wanglei.optimizer

import com.wanglei.optimizer.compressjpg.factory.CompressJpgFactory
import com.wanglei.optimizer.compressjpg.inter.ICompressJpg
import com.wanglei.optimizer.compresspng.factory.CompressPngFactory
import com.wanglei.optimizer.compresspng.inter.ICompressPng
import com.wanglei.optimizer.convertwebp.factory.ConvertWebpFactory
import com.wanglei.optimizer.convertwebp.inter.IConvertWebp
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

class ImageOptimizationTask extends DefaultTask{

    def webPTool
    def pngTool
    def jpgTool

//    @Input
//    def manifestFile

    @Input
    def minSdk

    @Input
    List<File> res

    @Input
    @Optional
    def jpegCompressQuality = ImageOptimizationConstants.defaultJpegCompressQuality

    @Input
    @Optional
    def convertWebpQuality = ImageOptimizationConstants.defaultConvertWebpQuality

    @Input
    @Optional
    def pngCompressQuality  = ImageOptimizationConstants.defaultCompressPngQuality

    @Input
    String appIconName

    @Input
    @Optional
    String appIconRoundName

    @Input
    @Optional
    String convertWebpType = ImageOptimizationConstants.LOSSY

    @Input
    @Optional
    String compressPngType = ImageOptimizationConstants.LOSSY

    @Input
    @Optional
    String pluginStrategy = ImageOptimizationConstants.STRATEGY_ONLIY_COMPRESS

    @Input
    @Optional
    def filterImageNames = null //需要过滤的图片名字集合

    //jpg的压缩目前不支持外部设置，统一有损压缩
    String compressJpgType = ImageOptimizationConstants.LOSSY

    //
    IConvertWebp convertWebpTool
    ICompressPng compressPngTool
    ICompressJpg compressJpgTool

    ImageOptimizationTask(){
        group = "WLImageOptimization"
    }

    @TaskAction
    def run(){
        //检查参数是否合法
        ImageOptimizationUtils.checkOptionalParams(jpegCompressQuality,convertWebpQuality,appIconName,convertWebpType,minSdk,compressPngType,pluginStrategy,pngCompressQuality)
        //拷贝工具
        webPTool = ImageOptimizationUtils.copyTool(project,ImageOptimizationConstants.CWEBP_TOOL_NAME)
        if (compressPngType.equalsIgnoreCase(ImageOptimizationConstants.LOSSY)){
            pngTool = ImageOptimizationUtils.copyTool(project,ImageOptimizationConstants.LOSSY_PNG_TOOL_NAME)
        }else {
            pngTool = ImageOptimizationUtils.copyTool(project,ImageOptimizationConstants.LOSSLESS_PNG_TOOL_NAME)
        }
        jpgTool = ImageOptimizationUtils.copyTool(project,ImageOptimizationConstants.JPG_TOOL_NAME)
        //获取对应工具
        convertWebpTool = ConvertWebpFactory.getConvertWebpTool(convertWebpType)
        compressPngTool = CompressPngFactory.getCompressPngTool(compressPngType)
        compressJpgTool = CompressJpgFactory.getCompressJpgTool(compressJpgType)
        //
        project.logger.error "=========WL ImageOptimization Start=========="
        project.logger.error "pluginStrategy : $pluginStrategy"
        project.logger.error "jpegCompressQuality : $jpegCompressQuality"
        project.logger.error "pngCompressQuality : $pngCompressQuality"
        project.logger.error "convertWebpQuality : $convertWebpQuality"
        project.logger.error "webp tool : $webPTool"
        project.logger.error "png tool : $pngTool"
        project.logger.error "jpg tool : $jpgTool"
        project.logger.error "mini sdk : $minSdk"
        project.logger.error "convertWebpType : $convertWebpType"
        project.logger.error "compressPngType : $compressPngType"
        project.logger.error "需要过滤的图片：$filterImageNames"
        res.each {
            project.logger.error "image dir : $it.absolutePath"
        }
//        project.logger.error "manifestFile : $manifestFile.absolutePath"
        project.logger.error "=========1,parse Manifest.xml=========="
        //解析Manifest.xml获取启动图标，启动图标不转为webp
//        def ns  = new Namespace("http://schemas.android.com/apk/res/android","android")
//        def node = new XmlParser().parse(manifestFile)
//        Node application = node.application[0]
//        icon = application.attributes()[ns.icon]//@mipmap/ic_launcher
//        roundIcon = application.attributes()[ns.roundIcon]//@mipmap/ic_launcher_round
//        icon = icon.substring(icon.lastIndexOf("/")+1,icon.length())//ic_launcher
//        roundIcon = roundIcon.substring(roundIcon.lastIndexOf("/")+1,roundIcon.length())//ic_launcher_round

        project.logger.error "launcher Icon Name:$appIconName"
        project.logger.error "launcher Round Icon Name :$appIconRoundName"

        project.logger.error "=========2, parse resDir=========="
        //存放png图片
        def pngs = []
        //存放jpg图片
        def jpgs = []
        //遍历文件夹
        res.each{
            if (ImageOptimizationUtils.isImageFolder(it)){
                it.eachFile {
                    if (ImageOptimizationUtils.isJpgImage(it) && ImageOptimizationUtils.isNotLauncherIcon(it,appIconName,appIconRoundName)){
                        if (filterImageNames == null ||
                                (filterImageNames != null && !filterImageNames.contains(it.name))){
                            jpgs << it
                        }
                    }else if(ImageOptimizationUtils.isPngImage(it) && ImageOptimizationUtils.isNotLauncherIcon(it,appIconName,appIconRoundName)){
                        if (filterImageNames == null ||
                                (filterImageNames != null && !filterImageNames.contains(it.name))){
                            pngs << it
                        }
                    }
                }
            }
        }

        //输出全部图片信息
        pngs.each {
            project.logger.error "${it.absolutePath}"
        }

        jpgs.each {
            project.logger.error "${it.absolutePath}"
        }
        //这里只进行简单的判断
        if (pluginStrategy.equalsIgnoreCase(ImageOptimizationConstants.STRATEGY_ONLIY_COMPRESS)){
            project.logger.error "=========3,start compress Image=========="
            pngs.each {
                compressPng(it)
            }
            //
            jpgs.each {
                compressJpg(it)
            }
        }else {
            project.logger.error "=========3,start ConvertImage to Webp=========="
            //保存png转webp失败或者转换后图片更大的集合
            def pngConvertFailed = []
            //保存jpg转webp失败或者转换后图片更大的集合
            def jpgConvertFailed = []
            if (minSdk >= 14 && minSdk < 18){
                def compress = []//保存不能转换的图片，[14,18)之间有透明通道的png图片不能转为webp，18以上均可以
                //处理png 如果带透明度 则加入压缩集合，不能转为webp
                project.logger.error "=========minSdk >= 14 && minSdk < 18=========="
                pngs.each {
                    if (ImageOptimizationUtils.isTransparent(it)){
                        compress << it
                    }else{
                        convertWebp(it,pngConvertFailed)
                    }
                }
                compress.each {
                    compressPng(it)
                }
                //
                jpgs.each {
                    convertWebp(it,jpgConvertFailed)
                }
            }else if(minSdk >=18){//18及以上均可转换
                project.logger.error "=========minSdk >=18=========="
                pngs.each {
                    convertWebp(it,pngConvertFailed)
                }
                //
                jpgs.each {
                    convertWebp(it,jpgConvertFailed)
                }
            }else{//小于14只能压缩操作
                project.logger.error "=========minSdk < 14=========="
                pngs.each {
                    compressPng(it)
                }
                //
                jpgs.each {
                    compressJpg(it)
                }
            }
            project.logger.error "=========4,start compress Failed Image=========="
            //输出信息
            pngConvertFailed.each {
                project.logger.error "${it.absolutePath}"
            }

            jpgConvertFailed.each {
                project.logger.error "${it.absolutePath}"
            }
            //转换失败的图片执行压缩操作
            pngConvertFailed.each {
                compressPng(it)
            }

            jpgConvertFailed.each {
                compressJpg(it)
            }
        }
        project.logger.error "=========WL ImageOptimization End=========="
    }


    def convertWebp(File file, def convertFailedList){
        convertWebpTool.convertWebp(webPTool,project,file,convertFailedList,convertWebpQuality)
    }

    def compressPng(File file){
        compressPngTool.compressPng(pngTool,project,file,pngCompressQuality)
    }

    def compressJpg(File file){
        compressJpgTool.compressJpg(jpgTool,project,file,jpegCompressQuality)
    }
}
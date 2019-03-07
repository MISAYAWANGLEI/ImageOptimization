package com.wanglei.optimizer

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.api.BaseVariant
import com.android.sdklib.repository.targets.PlatformTarget
import org.gradle.api.DomainObjectCollection
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

class ImageOptimizationPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        if (project.plugins.hasPlugin(AppPlugin)) {
            startApply(project, (DomainObjectCollection<BaseVariant>) project.android.applicationVariants)
        } else if (project.plugins.hasPlugin(LibraryPlugin)) {
            startApply(project, (DomainObjectCollection<BaseVariant>) project.android.libraryVariants)
        } else {
            throw new GradleException("image-optimization plugin should be used in android module")
        }
    }

    static void startApply(Project project, DomainObjectCollection<BaseVariant> variants) {
        //创建扩展，外部可以配置的参数
        def imgExt = project.extensions.create(ImageOptimizationConstants.IMG_EXT_NAME, ImageOptimizationExtensions)
        def imgFilterExt = project.ImageOptimization.extensions.create(ImageOptimizationConstants.IMG_FILTER_EXT_NAME, ImageOptimizationFilterExtensions)

        project.afterEvaluate {
            project.logger.error "过滤图片文件夹目录名：${imgFilterExt.filterDirs}"
            variants.all {
                BaseVariant variant ->
                    //存放图片文件夹集合
                    List<File> imgDirs = []
                    variant.sourceSets.each { sourceSet ->
                        sourceSet.resDirectories.each { res ->
                            if (res.exists()) {
                                res.eachDir {
                                    //是图片目录
                                    if (it.directory && ImageOptimizationUtils.isImageFolder(it)) {
                                        //过滤用户配置的满足条件的图片
                                        if (imgFilterExt.filterDirs == null ||
                                                (imgFilterExt.filterDirs != null
                                                        && !imgFilterExt.filterDirs.contains(it.name))){
                                            imgDirs << it
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (!imgDirs.empty) {
                        def task = project.tasks.create("imageOptimization_${project.name.capitalize()}_${variant.name.capitalize()}", ImageOptimizationTask) {
//                            //获取manifest.xml文件
//                            def variantOutput = variant.outputs.first()
//                            if (variantOutput.processManifest.properties['manifestOutputFile'] != null) {
//                                manifestFile = variantOutput.processManifest.manifestOutputFile
//                            } else if (variantOutput.processResources.properties['manifestFile'] != null) {
//                                manifestFile = variantOutput.processResources.manifestFile
//                            }
                            //获取minSdk
                            //variant.mergedFlavor.minSdkVersion.apiLevel
                            //variant.variantData.variantConfiguration
                            minSdk = variant.mergeResourcesProvider.get().minSdk
                            //图片文件夹集合
                            res = imgDirs
                            //启动图标名字
                            appIconName = imgExt.appIconName
                            appIconRoundName = imgExt.appIconRoundName
                            //
                            convertWebpType = imgExt.convertWebpType
                            compressPngType = imgExt.compressPngType
                            //配置参数
                            jpegCompressQuality = imgExt.jpegCompressQuality
                            convertWebpQuality = imgExt.convertWebpQuality
                            pngCompressQuality = imgExt.pngCompressQuality
                            //插件策略，默认只压缩
                            pluginStrategy = imgExt.pluginStrategy
                            //
                            filterImageNames = imgFilterExt.filterImageNames
                        }
                        //task.dependsOn variant.outputs.first().processManifest
                    }
            }
        }
    }

}
package com.wanglei.optimizer.convertwebp

import com.wanglei.optimizer.convertwebp.inter.IConvertWebp
import org.gradle.api.Project

abstract class AbstractConvertWebp implements IConvertWebp {

    @Override
    void convertWebp(String webPTool, Project project, File file, List<File> convertFailedList, int convertWebpQuality) {

        def name = file.name.substring(0,file.name.lastIndexOf("."))
        def outPutFile = new File(file.parent, "${name}.webp")
        //
        def result = commandLine(webPTool,file,outPutFile,convertWebpQuality).execute()
        result.waitForProcessOutput()
        if (result.exitValue() == 0){
            def orgFileLen = file.length()//没有转换前图片大小
            def outPutFileLen = outPutFile.length()//转为webp后图片大小
            if(orgFileLen > outPutFileLen){
                file.delete()
                project.logger.error "convert webp ${file.absolutePath} success"
            }else {
                convertFailedList << file
                outPutFile.delete()
                project.logger.error "convert webp ${file.absolutePath} success but bigger than orgFile"
            }
        }else{
            convertFailedList << file
            project.logger.error "convert webp ${file.absolutePath} error"
        }
    }

    abstract String commandLine(String webPTool,File inFile,File outFile,int convertWebpQuality)

}
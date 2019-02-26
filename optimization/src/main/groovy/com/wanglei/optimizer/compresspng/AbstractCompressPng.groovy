package com.wanglei.optimizer.compresspng

import com.wanglei.optimizer.compresspng.inter.ICompressPng

abstract class AbstractCompressPng implements ICompressPng{

    @Override
    void compressPng(Object pngTool, Object project, Object file, Object compressPngQuality) {
        long originalSize = file.length()
        def result = commandLine(pngTool,file,compressPngQuality).execute()
        result.waitForProcessOutput()
        if (result.exitValue() == 0){
            long compressedSize = new File(file.absolutePath).length()
            float rate = 1.0f * (originalSize - compressedSize) / originalSize * 100
            project.logger.error "compress png image ${file.absolutePath} success  from ${originalSize}B down to ${compressedSize}B, ${rate}% saved!"
        }else if (result.exitValue() == 98) {//有损压缩已经被压缩过则放弃压缩
            project.logger.error "Lossy Type skip compress ${file.absolutePath} has been compressed"
        }else if (result.exitValue() == 2) {//无损压缩已经被压缩过则放弃压缩
            project.logger.error "Lossless Type skip compress ${file.absolutePath} has been compressed"
        } else{
            project.logger.error "compress png image ${file.absolutePath} error : ${result.err.text}"
        }
    }

    abstract String commandLine(pngTool,inFile,compressPngQuality)
}
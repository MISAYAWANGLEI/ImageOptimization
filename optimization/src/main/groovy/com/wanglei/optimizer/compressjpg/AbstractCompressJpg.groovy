package com.wanglei.optimizer.compressjpg

import com.wanglei.optimizer.compressjpg.inter.ICompressJpg

abstract class AbstractCompressJpg implements ICompressJpg{

    @Override
    void compressJpg(Object jpgTool, Object project, Object file, Object compressJpgQuality) {
        long originalSize = file.length()
        def out = new File(file.parent,"temp-${file.name}")
        def result =commandLine(jpgTool,file,out,compressJpgQuality) .execute()
        result.waitForProcessOutput()
        if (result.exitValue() == 0){
            long compressedSize = out.length()
            float rate = 1.0f * (originalSize - compressedSize) / originalSize * 100
            file.delete()
            out.renameTo(file)
            project.logger.error "compress jpg image ${file.absolutePath} success  from ${originalSize}B down to ${compressedSize}B, ${rate}% saved!"
        }else{
            project.logger.error "compress jpg image ${file.absolutePath} error : ${result.err.text}"
        }
    }

    abstract String commandLine(jpgTool,inFile,outFile,compressJpgQuality)

}
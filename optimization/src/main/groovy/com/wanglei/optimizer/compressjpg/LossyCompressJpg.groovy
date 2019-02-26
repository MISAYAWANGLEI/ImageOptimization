package com.wanglei.optimizer.compressjpg

class LossyCompressJpg extends AbstractCompressJpg{


    @Override
    String commandLine(Object jpgTool, Object file, Object out, Object jpegCompressQuality) {
        return "${jpgTool} --quality ${jpegCompressQuality} ${file.absolutePath} ${out.absolutePath}"
    }
}
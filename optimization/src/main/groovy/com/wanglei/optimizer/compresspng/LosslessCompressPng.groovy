package com.wanglei.optimizer.compresspng


class LosslessCompressPng extends AbstractCompressPng {

    @Override
    String commandLine(Object pngTool, Object file, Object compressPngQuality) {
        return  "${pngTool} ${file.absolutePath}"
    }
}
package com.wanglei.optimizer.convertwebp


class LosslessConvertWebp extends AbstractConvertWebp{

    @Override
    String commandLine(String webPTool, File inFile, File outFile, int convertWebpQuality) {
        return "$webPTool -lossless ${inFile.absolutePath} -o ${outFile.absolutePath}"
    }
}
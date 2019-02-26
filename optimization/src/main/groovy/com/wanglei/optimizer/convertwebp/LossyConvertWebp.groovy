package com.wanglei.optimizer.convertwebp


class LossyConvertWebp extends AbstractConvertWebp {

    @Override
    String commandLine(String webPTool,File inFile, File outFile, int convertWebpQuality) {
        return "$webPTool -q ${convertWebpQuality} ${inFile.absolutePath} -o ${outFile.absolutePath}"
    }
}
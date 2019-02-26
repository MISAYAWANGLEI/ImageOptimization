package com.wanglei.optimizer.compresspng

class LossyCompressPng extends AbstractCompressPng{

    @Override
    String commandLine(Object pngTool, Object file, Object pngCompressQuality) {
        def suffix = file.name.substring(file.name.lastIndexOf("."), file.name.length())
        //--quality min-max min and max are numbers in range 0 (worst) to 100 (perfect)
        //--speed N, -sN Speed/quality trade-off from 1 (brute-force) to 10 (fastest).
        //The default is 3. Speed 10 has 5% lower quality, but is 8 times faster than the default.
        return  "${pngTool} -f --skip-if-larger --ext $suffix --quality ${pngCompressQuality}-${pngCompressQuality} --speed 1 ${file.absolutePath}"
    }
}
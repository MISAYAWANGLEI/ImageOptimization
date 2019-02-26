package com.wanglei.optimizer.compresspng.factory

import com.wanglei.optimizer.ImageOptimizationConstants
import com.wanglei.optimizer.compresspng.LosslessCompressPng
import com.wanglei.optimizer.compresspng.LossyCompressPng
import com.wanglei.optimizer.compresspng.inter.ICompressPng

class CompressPngFactory {

    private CompressPngFactory() {}

    static ICompressPng getCompressPngTool(String type) {
        if (ImageOptimizationConstants.LOSSY .equalsIgnoreCase(type)) {
            return new LossyCompressPng()
        } else if (ImageOptimizationConstants.LOSSLESS .equalsIgnoreCase(type)) {
            return new LosslessCompressPng()
        } else {
            throw new IllegalArgumentException("compressPngType error. Please use lossy or lossless, default is lossy")
        }
    }
}
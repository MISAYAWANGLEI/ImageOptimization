package com.wanglei.optimizer.compressjpg.factory

import com.wanglei.optimizer.ImageOptimizationConstants
import com.wanglei.optimizer.compressjpg.LosslessCompressJpg
import com.wanglei.optimizer.compressjpg.LossyCompressJpg
import com.wanglei.optimizer.compressjpg.inter.ICompressJpg

class CompressJpgFactory {

    private CompressJpgFactory() {}

    static ICompressJpg getCompressJpgTool(String type) {
        if (ImageOptimizationConstants.LOSSY .equalsIgnoreCase(type)) {
            return new LossyCompressJpg()
        } else if (ImageOptimizationConstants.LOSSLESS .equalsIgnoreCase(type)) {
            return new LosslessCompressJpg()
        } else {
            throw new IllegalArgumentException("compressJpgType error. Please use lossy or lossless, default is lossy")
        }
    }
}
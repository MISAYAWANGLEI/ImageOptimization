package com.wanglei.optimizer.convertwebp.factory

import com.wanglei.optimizer.ImageOptimizationConstants
import com.wanglei.optimizer.convertwebp.LosslessConvertWebp
import com.wanglei.optimizer.convertwebp.LossyConvertWebp
import com.wanglei.optimizer.convertwebp.inter.IConvertWebp


class ConvertWebpFactory{

    private ConvertWebpFactory() {}

    static IConvertWebp getConvertWebpTool(String type) {
        if (ImageOptimizationConstants.LOSSY .equalsIgnoreCase(type)) {
            return new LossyConvertWebp()
        } else if (ImageOptimizationConstants.LOSSLESS .equalsIgnoreCase(type)) {
            return new LosslessConvertWebp()
        } else {
            throw new IllegalArgumentException("convertWebpType error. Please use lossy or lossless, default is lossy")
        }
    }
}